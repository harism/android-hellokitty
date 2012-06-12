/*
   Copyright 2012 Harri Smatt

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */

package fi.harism.wallpaper.hellokitty;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.Vector;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.graphics.Matrix;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.widget.Toast;

/**
 * Renderer class.
 */
public final class KittyRenderer implements GLSurfaceView.Renderer, Runnable {

	// Bezier curve split count.
	private static final int BEZIER_VERTEX_COUNT = 20;
	// Background fill color.
	private static final float[] COLOR_BG = { .2f, .5f, .8f };
	// Render states.
	private static final int STATE_BLINK_EYE_BOTH = 0;
	private static final int STATE_BLINK_EYE_LEFT = 1;
	private static final int STATE_BLINK_EYE_RIGHT = 2;
	private static final int STATE_CLEAR = 3;
	private static final int STATE_MOVE_BOW = 4;
	private static final int STATE_MOVE_PAW_LEFT = 5;
	private static final int STATE_MOVE_PAW_RIGHT = 6;
	private static final int STATE_RENDERKITTY = 7;

	// View aspect ratio.
	private final float mAspectRatio[] = new float[2];
	// Vertex buffers.
	private FloatBuffer mBufferBezier;
	private ByteBuffer mBufferScreen;
	// Clear layer.
	private KittyLayer mClearLayer = new KittyLayer("clear", new Matrix());
	private final Handler mDelayedHandler = new Handler(Looper.getMainLooper());
	// Owner surface view.
	private GLSurfaceView mGLSurfaceView;
	private final KittyFbo mKittyFbo = new KittyFbo();
	private final KittySvg mKittySvg = new KittySvg();
	// Shader variables.
	private final KittyShader mShaderBezier = new KittyShader();
	private final boolean[] mShaderCompilerSupport = new boolean[1];
	private final KittyShader mShaderCopy = new KittyShader();
	private final Vector<Integer> mStateArray = new Vector<Integer>();
	private final float[] mTempBezier = new float[16];
	private final Matrix mTempMatrix = new Matrix();
	private long mTimeStart, mTimeLast = -1;
	// View width and height.
	private int mWidth, mHeight;

	/**
	 * Default constructor.
	 */
	public KittyRenderer(GLSurfaceView glSurfaceView) {
		mGLSurfaceView = glSurfaceView;

		// Screen sized coordinates.
		final byte SCREEN_COORDS[] = { -1, 1, -1, -1, 1, 1, 1, -1 };
		mBufferScreen = ByteBuffer.allocateDirect(2 * 4);
		mBufferScreen.put(SCREEN_COORDS).position(0);

		// Spline float buffer generation.
		ByteBuffer buf = ByteBuffer.allocateDirect(4 * 4 * BEZIER_VERTEX_COUNT);
		mBufferBezier = buf.order(ByteOrder.nativeOrder()).asFloatBuffer();
		for (int i = 0; i < BEZIER_VERTEX_COUNT; ++i) {
			float t = (float) i / (BEZIER_VERTEX_COUNT - 1);
			mBufferBezier.put(t).put(-1);
			mBufferBezier.put(t).put(1);
		}
		mBufferBezier.position(0);

		// Initialize clear layer.
		for (int i = 0; i < 5; ++i) {
			KittyBezier bezier = new KittyBezier(COLOR_BG, 0, 0);
			mClearLayer.mBeziers.add(bezier);
		}

		// Load kitty svg..
		try {
			InputStream is = mGLSurfaceView.getContext().getResources()
					.openRawResource(R.raw.kitty_svg);
			mKittySvg.read(is);
		} catch (Exception ex) {
			ex.printStackTrace();
			showError(ex.getMessage());
		}
	}

	/**
	 * Generates new random state array list.
	 */
	public void genNewStateArray() {
		// First render kitty.
		mStateArray.add(STATE_RENDERKITTY);
		// Add N random events.
		for (int i = 0; i < 20; ++i) {
			double rand = Math.random();
			if (rand < 0.5) {
				rand = Math.random();
				if (rand < 0.4)
					mStateArray.add(STATE_BLINK_EYE_LEFT);
				else if (rand < 0.8)
					mStateArray.add(STATE_BLINK_EYE_RIGHT);
				else
					mStateArray.add(STATE_BLINK_EYE_BOTH);
			} else {
				rand = Math.random();
				if (rand < 0.333)
					mStateArray.add(STATE_MOVE_PAW_LEFT);
				else if (rand < 0.667)
					mStateArray.add(STATE_MOVE_PAW_RIGHT);
				else
					mStateArray.add(STATE_MOVE_BOW);
			}
		}
		// Finally clear kitty for redrawing.
		mStateArray.add(STATE_CLEAR);
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

		// If shader compiler is not supported.
		if (mShaderCompilerSupport[0] == false) {
			GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);
			GLES20.glViewport(0, 0, mWidth, mHeight);
			GLES20.glClearColor(COLOR_BG[0], COLOR_BG[1], COLOR_BG[2], 1f);
			GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
			return;
		}

		// Default settings.
		GLES20.glDisable(GLES20.GL_BLEND);
		GLES20.glDisable(GLES20.GL_DEPTH_TEST);
		GLES20.glDisable(GLES20.GL_CULL_FACE);

		// Render to FBO.
		mKittyFbo.bind();
		mKittyFbo.bindTexture(0);

		boolean requestRender = false;
		if (mStateArray.size() == 0) {
			genNewStateArray();
		}
		switch (mStateArray.get(0)) {
		case STATE_RENDERKITTY:
			requestRender = renderKitty();
			break;
		case STATE_BLINK_EYE_LEFT:
			requestRender = renderBlinkEye("eye_left_bg", "eye_left");
			break;
		case STATE_BLINK_EYE_RIGHT:
			requestRender = renderBlinkEye("eye_right_bg", "eye_right");
			break;
		case STATE_BLINK_EYE_BOTH:
			requestRender = renderBlinkEye("eye_left_bg", "eye_left",
					"eye_right_bg", "eye_right");
			break;
		case STATE_MOVE_BOW:
			requestRender = renderMoveLayer("bow");
			break;
		case STATE_MOVE_PAW_LEFT:
			requestRender = renderMoveLayer("paw_left");
			break;
		case STATE_MOVE_PAW_RIGHT:
			requestRender = renderMoveLayer("paw_right");
			break;
		case STATE_CLEAR:
			requestRender = renderClear();
			break;
		}

		// If request render, call for new render iteration.
		if (requestRender) {
			mGLSurfaceView.requestRender();
		}
		// Otherwise post delayed render request.
		else {
			mDelayedHandler.postDelayed(this, 5000);
		}

		// Bind screen buffer.
		GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);
		GLES20.glViewport(0, 0, mWidth, mHeight);
		// Copy FBO to screen.
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
		mTimeStart = mTimeLast = -1;
		mStateArray.clear();
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

		// Try to load shaders.
		try {
			String vertexSource, fragmentSource;
			vertexSource = loadRawString(R.raw.copy_vs);
			fragmentSource = loadRawString(R.raw.copy_fs);
			mShaderCopy.setProgram(vertexSource, fragmentSource);
			vertexSource = loadRawString(R.raw.bezier_vs);
			fragmentSource = loadRawString(R.raw.bezier_fs);
			mShaderBezier.setProgram(vertexSource, fragmentSource);
		} catch (Exception ex) {
			mShaderCompilerSupport[0] = false;
			showError(ex.getMessage());
		}
	}

	/**
	 * Renders bezier onto current buffer. tStart and tEnd are values between
	 * [0, 1].
	 */
	private void renderBezier(KittyBezier bezier, Matrix transform,
			float tStart, float tEnd) {

		for (int i = 0; i < 8; ++i) {
			mTempBezier[i] = bezier.mPts0[i];
			mTempBezier[i + 8] = bezier.mPts1[i];
		}
		transform.mapPoints(mTempBezier);

		mShaderBezier.useProgram();
		int uAspectRatio = mShaderBezier.getHandle("uAspectRatio");
		int uLimitsT = mShaderBezier.getHandle("uLimitsT");
		int uControlPts = mShaderBezier.getHandle("uControlPts");
		int uColor = mShaderBezier.getHandle("uColor");
		int aBezierPos = mShaderBezier.getHandle("aBezierPos");

		GLES20.glUniform2fv(uAspectRatio, 1, mAspectRatio, 0);
		GLES20.glUniform2f(uLimitsT, tStart, tEnd);
		GLES20.glUniform2fv(uControlPts, 8, mTempBezier, 0);
		GLES20.glUniform3fv(uColor, 1, bezier.mColor, 0);

		GLES20.glVertexAttribPointer(aBezierPos, 2, GLES20.GL_FLOAT, false, 0,
				mBufferBezier);
		GLES20.glEnableVertexAttribArray(aBezierPos);

		GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0,
				2 * BEZIER_VERTEX_COUNT);
	}

	/**
	 * Handles eye blink animation.
	 */
	private boolean renderBlinkEye(String... layerIds) {
		long timeCurrent = SystemClock.uptimeMillis();
		if (mTimeLast < 0) {
			mTimeStart = mTimeLast = timeCurrent;
		}

		long diffLast = mTimeLast - mTimeStart;
		long diffCurrent = timeCurrent - mTimeStart;

		float tStart = diffLast / 500f;
		float tEnd = diffCurrent / 500f;

		for (int i = 0; i < layerIds.length; ++i) {
			KittyLayer layer = mKittySvg.getLayer(layerIds[i]);
			for (KittyBezier bezier : layer.mBeziers) {
				renderBezier(bezier, layer.mTransform, i % 2 == 0 ? tStart
						: 2 - tEnd, i % 2 == 0 ? tEnd : 2 - tStart);
			}
		}

		mTimeLast = timeCurrent;
		return diffCurrent < 1000;
	}

	/**
	 * Handles clearing current buffer.
	 */
	private boolean renderClear() {
		long timeCurrent = SystemClock.uptimeMillis();
		if (mTimeLast < 0) {
			for (int i = 0; i < mClearLayer.mBeziers.size(); ++i) {
				KittyBezier bezier = mClearLayer.mBeziers.get(i);

				float x = (float) (Math.random() * 2 - 1);
				float y = (float) (Math.random() * 2 - 1);
				float dx = 4f / (mClearLayer.mBeziers.size() - i);
				float dy = 3f / (mClearLayer.mBeziers.size() - i);

				bezier.mPts0 = new float[] { x, y - dy, x - dx, y - dy, x - dx,
						y + dy, x, y + dy };
				bezier.mPts1 = new float[] { x, y - dy, x + dx, y - dy, x + dx,
						y + dy, x, y + dy };

			}
			mTimeStart = mTimeLast = timeCurrent;
		}

		long diffCurrent = timeCurrent - mTimeStart;
		float scale = diffCurrent / 5000f;
		scale *= scale * (3 - 2 * scale);

		for (KittyBezier bezier : mClearLayer.mBeziers) {
			float dx = bezier.mPts0[0];
			float dy = bezier.mPts0[1] + 1;
			mClearLayer.mTransform.setTranslate(-dx, -dy);
			mClearLayer.mTransform.postScale(scale, scale);
			mClearLayer.mTransform.postTranslate(dx, dy);
			renderBezier(bezier, mClearLayer.mTransform, 0f, 1f);
		}

		mTimeLast = timeCurrent;
		if (diffCurrent >= 4000) {
			mTimeLast = mTimeStart = -1;
			mStateArray.clear();
		}
		return true;
	}

	/**
	 * Handles procedural kitty rendering.
	 */
	private boolean renderKitty() {
		long timeCurrent = SystemClock.uptimeMillis();
		if (mTimeStart < 0) {
			GLES20.glClearColor(COLOR_BG[0], COLOR_BG[1], COLOR_BG[2], 1f);
			GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
			mTimeStart = mTimeLast = timeCurrent;
		}

		boolean requestRender = false;
		for (KittyLayer layer : mKittySvg.getLayers()) {
			for (KittyBezier bezier : layer.mBeziers) {
				long diffLast = mTimeLast - mTimeStart;
				long diffCurrent = timeCurrent - mTimeStart;
				long diffEnd = bezier.mTimeStart + bezier.mTimeDuration;

				if (diffLast <= bezier.mTimeStart && diffCurrent >= diffEnd) {
					renderBezier(bezier, layer.mTransform, 0f, 1f);
				} else if ((diffLast >= bezier.mTimeStart && diffLast <= diffEnd)
						|| (diffCurrent >= bezier.mTimeStart && diffCurrent <= diffEnd)) {
					float tStart = (float) (diffLast - bezier.mTimeStart)
							/ bezier.mTimeDuration;
					float tEnd = (float) (diffCurrent - bezier.mTimeStart)
							/ bezier.mTimeDuration;

					renderBezier(bezier, layer.mTransform, tStart >= 0 ? tStart
							: 0, tEnd <= 1 ? tEnd : 1);
				}

				if (diffEnd > diffCurrent) {
					requestRender = true;
				}
			}
		}

		mTimeLast = timeCurrent;
		return requestRender;
	}

	/**
	 * Renders layer movement animation.
	 */
	private boolean renderMoveLayer(String layerId) {
		long timeCurrent = SystemClock.uptimeMillis();
		if (mTimeStart < 0) {
			mTimeStart = timeCurrent;
			mTimeLast = (long) (Math.random() * 1440 - 720);
		}

		long diffCurrent = timeCurrent - mTimeStart;
		if (diffCurrent > 2880) {
			diffCurrent = 2880;
		}

		float t = (float) Math.sin(diffCurrent * Math.PI / 2880);
		t = t * t * t * (3 - 2 * t) * 0.08f;
		float dx = (float) Math.sin((diffCurrent + mTimeLast) * Math.PI / 720);
		float dy = (float) Math.cos((diffCurrent + mTimeLast) * Math.PI / 720);
		if (mTimeLast < 0) {
			float tmp = dx;
			dx = dy;
			dy = tmp;
		}

		KittyLayer layerMove = mKittySvg.getLayer(layerId);
		mTempMatrix.set(layerMove.mTransform);
		mTempMatrix.postTranslate(t * dx, t * dy);

		GLES20.glClearColor(COLOR_BG[0], COLOR_BG[1], COLOR_BG[2], 1f);
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

		for (KittyLayer layer : mKittySvg.getLayers()) {
			for (KittyBezier bezier : layer.mBeziers) {
				if (layer == layerMove) {
					renderBezier(bezier, mTempMatrix, 0, 1);
				} else {
					renderBezier(bezier, layer.mTransform, 0, 1);
				}
			}
		}

		return diffCurrent < 2880;
	}

	@Override
	public void run() {
		mTimeLast = -1;
		mTimeStart = -1;
		if (mStateArray.size() > 0) {
			mStateArray.remove(0);
		}
		mGLSurfaceView.requestRender();
	}

	/**
	 * Shows Toast on screen with given message.
	 */
	private void showError(final String errorMsg) {
		mDelayedHandler.post(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(mGLSurfaceView.getContext(), errorMsg,
						Toast.LENGTH_LONG).show();
			}
		});
	}

	/**
	 * Removes callbacks from Handler.
	 */
	public void stopHandler() {
		mDelayedHandler.removeCallbacks(this);
	}

}
