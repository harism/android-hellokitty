package fi.harism.wallpaper.hellokitty;

import android.opengl.GLSurfaceView;
import android.service.wallpaper.WallpaperService;
import android.service.wallpaper.WallpaperService.Engine;
import android.view.SurfaceHolder;

public class KittyService extends WallpaperService {

	@Override
	public Engine onCreateEngine() {
		return new WallpaperEngine();
	}

	/**
	 * Private wallpaper engine implementation.
	 */
	private final class WallpaperEngine extends Engine {

		private WallpaperSurfaceView mWallpaperSurfaceView;

		@Override
		public void onCreate(SurfaceHolder surfaceHolder) {

			// Uncomment for debugging.
			// android.os.Debug.waitForDebugger();

			super.onCreate(surfaceHolder);
			mWallpaperSurfaceView = new WallpaperSurfaceView();
			
			mWallpaperSurfaceView.setEGLContextClientVersion(2);
			mWallpaperSurfaceView.setRenderer(new KittyRenderer(mWallpaperSurfaceView));
			mWallpaperSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
		}
		
		@Override
		public final void onDestroy() {
			super.onDestroy();
			mWallpaperSurfaceView.onDestroy();
			mWallpaperSurfaceView = null;
		}
		
		@Override
		public final void onVisibilityChanged(boolean visible) {
			super.onVisibilityChanged(visible);
			if (visible) {
				mWallpaperSurfaceView.onResume();
				mWallpaperSurfaceView.requestRender();
			} else {
				mWallpaperSurfaceView.onPause();
			}
		}
		
		/**
		 * Lazy as I am, I din't bother using GLWallpaperService (found on
		 * GitHub) project for wrapping OpenGL functionality into my wallpaper
		 * service. Instead am using GLSurfaceView and trick it into hooking
		 * into Engine provided SurfaceHolder instead of SurfaceView provided
		 * one GLSurfaceView extends. For saving some bytes Renderer is
		 * implemented here too.
		 */
		private final class WallpaperSurfaceView extends GLSurfaceView {
			public WallpaperSurfaceView() {
				super(KittyService.this);
			}
			
			@Override
			public final SurfaceHolder getHolder() {
				return WallpaperEngine.this.getSurfaceHolder();
			}
			
			/**
			 * Should be called once underlying Engine is destroyed. Calling
			 * onDetachedFromWindow() will stop rendering thread which is lost
			 * otherwise.
			 */
			public final void onDestroy() {
				super.onDetachedFromWindow();
			}		
		}
	}
}
