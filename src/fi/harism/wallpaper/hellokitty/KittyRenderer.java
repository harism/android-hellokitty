package fi.harism.wallpaper.hellokitty;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.os.Handler;
import android.os.SystemClock;
import android.widget.Toast;

public class KittyRenderer implements GLSurfaceView.Renderer {

	private static final int SPLINE_VERTEX_COUNT = 20;
	private final float mAspectRatio[] = new float[2];
	private long mBezierTimeStart = -1, mBezierTimeCurrent = -1;
	private ByteBuffer mBufferScreen;
	private FloatBuffer mBufferSpline;
	private GLSurfaceView mGLSurfaceView;
	private final KittyFbo mKittyFbo = new KittyFbo();
	private final KittyObject mObject = new KittyObject();
	private final KittyShader mShaderBezier = new KittyShader();
	private final boolean[] mShaderCompilerSupport = new boolean[1];
	private final KittyShader mShaderCopy = new KittyShader();
	private int mWidth, mHeight;

	public KittyRenderer(GLSurfaceView glSurfaceView) {
		mGLSurfaceView = glSurfaceView;

		final byte SCREEN_COORDS[] = { -1, 1, -1, -1, 1, 1, 1, -1 };
		mBufferScreen = ByteBuffer.allocateDirect(2 * 4);
		mBufferScreen.put(SCREEN_COORDS).position(0);

		ByteBuffer buf = ByteBuffer.allocateDirect(4 * 4 * SPLINE_VERTEX_COUNT);
		mBufferSpline = buf.order(ByteOrder.nativeOrder()).asFloatBuffer();
		for (int i = 0; i < SPLINE_VERTEX_COUNT; ++i) {
			float t = (float) i / (SPLINE_VERTEX_COUNT - 1);
			mBufferSpline.put(t).put(-1);
			mBufferSpline.put(t).put(1);
		}
		mBufferSpline.position(0);
	}

	/**
	 * Loads String from raw resources with given id.
	 */
	private String loadRawString(int rawId) throws Exception {
		InputStream is = mGLSurfaceView.getContext().getResources()
				.openRawResource(rawId);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] buf = new byte[1024];
		int len;
		while ((len = is.read(buf)) != -1) {
			baos.write(buf, 0, len);
		}
		return baos.toString();
	}

	@Override
	public void onDrawFrame(GL10 unused) {

		if (mShaderCompilerSupport[0] == false) {
			GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);
			GLES20.glViewport(0, 0, mWidth, mHeight);
			GLES20.glClearColor(.2f, .5f, .8f, 1f);
			GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
			return;
		}

		GLES20.glDisable(GLES20.GL_BLEND);
		GLES20.glDisable(GLES20.GL_DEPTH_TEST);
		GLES20.glDisable(GLES20.GL_CULL_FACE);

		renderKitty();

		GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);
		GLES20.glViewport(0, 0, mWidth, mHeight);

		mShaderCopy.useProgram();
		int aPosition = mShaderCopy.getHandle("aPosition");
		GLES20.glVertexAttribPointer(aPosition, 2, GLES20.GL_BYTE, false, 0,
				mBufferScreen);
		GLES20.glEnableVertexAttribArray(aPosition);

		GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
	}

	@Override
	public void onSurfaceChanged(GL10 unused, int width, int height) {
		mWidth = width;
		mHeight = height;

		mAspectRatio[0] = (float) Math.min(mWidth, mHeight) / mWidth;
		mAspectRatio[1] = (float) Math.min(mWidth, mHeight) / mHeight;

		mKittyFbo.init(mWidth, mHeight, 1);
		mBezierTimeStart = mBezierTimeCurrent = -1;
	}

	@Override
	public void onSurfaceCreated(GL10 unused, EGLConfig config) {
		// Check if shader compiler is supported.
		GLES20.glGetBooleanv(GLES20.GL_SHADER_COMPILER, mShaderCompilerSupport,
				0);

		// If not, show user an error message and return immediately.
		if (mShaderCompilerSupport[0] == false) {
			String msg = mGLSurfaceView.getContext().getString(
					R.string.error_shader_compiler);
			showError(msg);
			return;
		}

		try {
			String vertexSource, fragmentSource;
			vertexSource = loadRawString(R.raw.copy_vs);
			fragmentSource = loadRawString(R.raw.copy_fs);
			mShaderCopy.setProgram(vertexSource, fragmentSource);
			vertexSource = loadRawString(R.raw.bezier_vs);
			fragmentSource = loadRawString(R.raw.bezier_fs);
			mShaderBezier.setProgram(vertexSource, fragmentSource);
		} catch (Exception ex) {
			showError(ex.getMessage());
		}
	}

	private void renderBezier(KittyBezier bezier, float tStart, float tEnd) {
		int sz = bezier.mCtrlPts0.length;
		final float[] ctrlPts = new float[16];
		for (int i = 0; i < sz; i += 2) {
			float cos = (float) Math.cos(bezier.mRotate / 180 * Math.PI);
			float sin = (float) Math.sin(bezier.mRotate / 180 * Math.PI);

			float x1 = bezier.mCtrlPts0[i];
			float y1 = bezier.mCtrlPts0[i + 1];
			float tmp = x1;
			x1 = x1 * cos - y1 * sin;
			y1 = tmp * sin + y1 * cos;
			ctrlPts[i] = (x1 + bezier.mTranslate[0]) * bezier.mScale;
			ctrlPts[i + 1] = (y1 + bezier.mTranslate[1]) * bezier.mScale;

			float x2 = bezier.mCtrlPts1[i];
			float y2 = bezier.mCtrlPts1[i + 1];
			tmp = x2;
			x2 = x2 * cos - y2 * sin;
			y2 = tmp * sin + y2 * cos;
			ctrlPts[i + 8] = (x2 + bezier.mTranslate[0]) * bezier.mScale;
			ctrlPts[i + 9] = (y2 + bezier.mTranslate[1]) * bezier.mScale;
		}

		mShaderBezier.useProgram();
		int uAspectRatio = mShaderBezier.getHandle("uAspectRatio");
		int uInterpolatorLimits = mShaderBezier
				.getHandle("uInterpolatorLimits");
		int uControlPtsCount = mShaderBezier.getHandle("uControlPtsCount");
		int uControlPts = mShaderBezier.getHandle("uControlPts");
		int uWidth = mShaderBezier.getHandle("uWidth");
		int uColor = mShaderBezier.getHandle("uColor");
		int aSplinePos = mShaderBezier.getHandle("aSplinePos");

		GLES20.glUniform2fv(uAspectRatio, 1, mAspectRatio, 0);
		GLES20.glUniform2f(uInterpolatorLimits, tStart, tEnd);
		GLES20.glUniform1i(uControlPtsCount, sz / 2);
		GLES20.glUniform2fv(uControlPts, 16, ctrlPts, 0);
		GLES20.glUniform2f(uWidth, .05f, .05f);
		GLES20.glUniform3fv(uColor, 1, bezier.mColor, 0);

		GLES20.glVertexAttribPointer(aSplinePos, 2, GLES20.GL_FLOAT, false, 0,
				mBufferSpline);
		GLES20.glEnableVertexAttribArray(aSplinePos);

		GLES20.glEnable(GLES20.GL_BLEND);
		GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);

		GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0,
				2 * SPLINE_VERTEX_COUNT);

		GLES20.glDisable(GLES20.GL_BLEND);
	}

	private void renderKitty() {
		mKittyFbo.bind();
		mKittyFbo.bindTexture(0);

		long time = SystemClock.uptimeMillis();
		if (mBezierTimeStart < 0) {
			GLES20.glClearColor(.2f, .5f, .8f, 1f);
			GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
			mBezierTimeStart = mBezierTimeCurrent = time;
		}

		boolean requestRender = false;
		for (KittyBezier bezier : mObject.getBeziers()) {
			long diff1 = mBezierTimeCurrent - mBezierTimeStart;
			long diff2 = time - mBezierTimeStart;
			long sum1 = bezier.mTimeStart + bezier.mTimeDuration;

			if (diff1 <= bezier.mTimeStart && diff2 >= sum1) {
				renderBezier(bezier, 0f, 1f);
			} else if ((diff1 >= bezier.mTimeStart && diff1 <= sum1)
					|| (diff2 >= bezier.mTimeStart && diff2 <= sum1)) {
				float tStart = (float) (diff1 - bezier.mTimeStart)
						/ bezier.mTimeDuration;
				float tEnd = (float) (diff2 - bezier.mTimeStart)
						/ bezier.mTimeDuration;
				renderBezier(bezier, tStart, tEnd);
			}

			if (sum1 > diff2) {
				requestRender = true;
			}
		}

		mBezierTimeCurrent = time;
		if (requestRender) {
			mGLSurfaceView.requestRender();
		}
	}

	/**
	 * Shows Toast on screen with given message.
	 */
	private void showError(final String errorMsg) {
		Handler handler = new Handler(mGLSurfaceView.getContext()
				.getMainLooper());
		handler.post(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(mGLSurfaceView.getContext(), errorMsg,
						Toast.LENGTH_LONG).show();
			}
		});
	}
}
