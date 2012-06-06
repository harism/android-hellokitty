package fi.harism.wallpaper.hellokitty;

import java.util.Vector;

public class KittyObject {

	private static final float[] COLOR_GRAY = { 0.376f, 0.376f, 0.377f };
	private static final float[] COLOR_PINK = { 0.906f, 0.314f, 0.584f };
	private static final float[] COLOR_WHITE = { 1f, 1f, 1f };
	private static final float[] COLOR_YELLOW = { 1f, 0.792f, 0.043f };

	private static final float ROTATE_NONE = 0f;
	private static final float SCALE = 0.03f;
	private static final float[] TRANSLATE_MAIN = { 0f, -25f };

	private final Vector<KittyBezier> mBeziers = new Vector<KittyBezier>();

	public KittyObject() {
		long t = genBgHeart(100);
		t = genBgHead(t);
		t = genNose(t);
		t = genEyes(t);
		t = genPaws(t);
	}

	private long genBgHead(long t) {
		KittyBezier b;
		// Left ear.
		b = new KittyBezier(COLOR_WHITE, t += 500, 500, TRANSLATE_MAIN,
				ROTATE_NONE, SCALE);
		b.mCtrlPts0 = new float[] { -24, 46, -26, 44, -24, 34 };
		b.mCtrlPts1 = new float[] { -24, 46, -22, 48, -12, 42 };
		mBeziers.add(b);
		// Right ear.
		b = new KittyBezier(COLOR_WHITE, t += 500, 500,
				TRANSLATE_MAIN, ROTATE_NONE, SCALE);
		b.mCtrlPts0 = new float[] { 24, 46, 26, 44, 24, 34 };
		b.mCtrlPts1 = new float[] { 24, 46, 22, 48, 12, 42 };
		mBeziers.add(b);
		// Skull.
		b = new KittyBezier(COLOR_WHITE, t += 500, 200,
				TRANSLATE_MAIN, ROTATE_NONE, SCALE);
		b.mCtrlPts0 = new float[] { 0, 44, -4, 44, -12, 42 };
		b.mCtrlPts1 = new float[] { 0, 44, 4, 44, 12, 42 };
		mBeziers.add(b);
		b = new KittyBezier(COLOR_WHITE, t += 200, 300,
				TRANSLATE_MAIN, ROTATE_NONE, SCALE);
		b.mCtrlPts0 = new float[] { -12, 42, -20, 40, -24, 34 };
		b.mCtrlPts1 = new float[] { 12, 42, 20, 40, 24, 34 };
		mBeziers.add(b);
		b = new KittyBezier(COLOR_WHITE, t += 300, 500,
				TRANSLATE_MAIN, ROTATE_NONE, SCALE);
		b.mCtrlPts0 = new float[] { -24, 34, -28, 32, -28, 26 };
		b.mCtrlPts1 = new float[] { 24, 34, 28, 32, 28, 26 };
		mBeziers.add(b);
		b = new KittyBezier(COLOR_WHITE, t += 500, 500,
				TRANSLATE_MAIN, ROTATE_NONE, SCALE);
		b.mCtrlPts0 = new float[] { -28, 26, -28, 20, -26, 14, -20, 10 };
		b.mCtrlPts1 = new float[] { 28, 26, 28, 20, 26, 14, 20, 10 };
		mBeziers.add(b);
		b = new KittyBezier(COLOR_WHITE, t += 500, 500,
				TRANSLATE_MAIN, ROTATE_NONE, SCALE);
		b.mCtrlPts0 = new float[] { -20, 10, -14, 6, -6, 6, 0, 6 };
		b.mCtrlPts1 = new float[] { 20, 10, 14, 6, 6, 6, 0, 6 };
		mBeziers.add(b);

		return t;
	}

	private long genBgHeart(long t) {
		KittyBezier b;
		b = new KittyBezier(COLOR_PINK, t += 1000, 1000, TRANSLATE_MAIN,
				ROTATE_NONE, SCALE);
		b.mCtrlPts0 = new float[] { -36, 41, -20, 64, 0, 41 };
		b.mCtrlPts1 = new float[] { -36, 41, -52, 18, 0, -7 };
		mBeziers.add(b);
		b = new KittyBezier(COLOR_PINK, t += 1000, 1000, TRANSLATE_MAIN,
				ROTATE_NONE, SCALE);
		b.mCtrlPts0 = new float[] { 0, 41, 20, 64, 36, 41 };
		b.mCtrlPts1 = new float[] { 0, -7, 52, 18, 36, 41 };
		mBeziers.add(b);
		
		return t;
	}

	private long genEyes(long t) {
		KittyBezier b;
		b = new KittyBezier(COLOR_GRAY, t += 500, 500, TRANSLATE_MAIN,
				ROTATE_NONE, SCALE);
		b.mCtrlPts0 = new float[] { -17, 24, -20, 24, -20, 18, -17, 18 };
		b.mCtrlPts1 = new float[] { -17, 24, -14, 24, -14, 18, -17, 18 };
		mBeziers.add(b);
		b = new KittyBezier(COLOR_GRAY, t += 500, 500,
				TRANSLATE_MAIN, ROTATE_NONE, SCALE);
		b.mCtrlPts0 = new float[] { 17, 24, 20, 24, 20, 18, 17, 18 };
		b.mCtrlPts1 = new float[] { 17, 24, 14, 24, 14, 18, 17, 18 };
		mBeziers.add(b);

		return t;
	}
	
	private long genPaws(long t) {
		KittyBezier b;
		
		// Left bg...
		b = new KittyBezier(COLOR_WHITE, t += 500, 500, TRANSLATE_MAIN,
				ROTATE_NONE, SCALE);
		b.mCtrlPts0 = new float[] { -20, 10, -18, 12, -15, 13, -11, 11 };
		b.mCtrlPts1 = new float[] { -20, 10, -22, 8, -22, 6, -20, 4 };
		mBeziers.add(b);
		b = new KittyBezier(COLOR_WHITE, t += 500, 500, TRANSLATE_MAIN,
				ROTATE_NONE, SCALE);
		b.mCtrlPts0 = new float[] { -11, 11, -7, 9, -6, 8, -5, 6 };
		b.mCtrlPts1 = new float[] { -20, 4, -18, 2, -16, 2, -14, 2 };
		mBeziers.add(b);
		b = new KittyBezier(COLOR_WHITE, t += 500, 500, TRANSLATE_MAIN,
				ROTATE_NONE, SCALE);
		b.mCtrlPts0 = new float[] { -5, 6, -4, 4, -4, 0, -8, 0 };
		b.mCtrlPts1 = new float[] { -14, 2, -13, 1, -12, 0, -8, 0 };
		mBeziers.add(b);
		
		// Left fg...
		b = new KittyBezier(COLOR_GRAY, t += 200, 200, TRANSLATE_MAIN,
				ROTATE_NONE, SCALE);
		b.mCtrlPts0 = new float[] { -14, 2, -18, 2, -20, 4 };
		b.mCtrlPts1 = new float[] { -14, 1, -19, 1, -21, 3 };
		mBeziers.add(b);
		b = new KittyBezier(COLOR_GRAY, t += 200, 200, TRANSLATE_MAIN,
				ROTATE_NONE, SCALE);
		b.mCtrlPts0 = new float[] { -20, 4, -22, 6, -22, 8, -20, 10 };
		b.mCtrlPts1 = new float[] { -21, 3, -23, 5, -23, 9, -21, 11 };
		mBeziers.add(b);
		b = new KittyBezier(COLOR_GRAY, t += 200, 200, TRANSLATE_MAIN,
				ROTATE_NONE, SCALE);
		b.mCtrlPts0 = new float[] { -20, 10, -18, 12, -15, 13, -11, 11 };
		b.mCtrlPts1 = new float[] { -21, 11, -19, 13, -14, 14, -10, 12 };
		mBeziers.add(b);
		b = new KittyBezier(COLOR_GRAY, t += 200, 200, TRANSLATE_MAIN,
				ROTATE_NONE, SCALE);
		b.mCtrlPts0 = new float[] { -11, 11, -7, 9, -6, 8, -5, 6 };
		b.mCtrlPts1 = new float[] { -10, 12, -6, 10, -5, 9, -4, 7 };
		mBeziers.add(b);
		b = new KittyBezier(COLOR_GRAY, t += 200, 200, TRANSLATE_MAIN,
				ROTATE_NONE, SCALE);
		b.mCtrlPts0 = new float[] { -5, 6, -4, 4, -4, 0, -8, 0 };
		b.mCtrlPts1 = new float[] { -4, 7, -2, 5, -3, -1, -8, -1 };
		mBeziers.add(b);
		b = new KittyBezier(COLOR_GRAY, t += 200, 200, TRANSLATE_MAIN,
				ROTATE_NONE, SCALE);
		b.mCtrlPts0 = new float[] { -8, 0, -12, 0, -14, 2 };
		b.mCtrlPts1 = new float[] { -8, -1, -12, -1, -14, 1 };
		mBeziers.add(b);
		
		// Right bg...
		b = new KittyBezier(COLOR_WHITE, t += 500, 500, TRANSLATE_MAIN,
				ROTATE_NONE, SCALE);
		b.mCtrlPts0 = new float[] { 20, 10, 18, 12, 15, 13, 11, 11 };
		b.mCtrlPts1 = new float[] { 20, 10, 22, 8, 22, 6, 20, 4 };
		mBeziers.add(b);
		b = new KittyBezier(COLOR_WHITE, t += 500, 500, TRANSLATE_MAIN,
				ROTATE_NONE, SCALE);
		b.mCtrlPts0 = new float[] { 11, 11, 7, 9, 6, 8, 5, 6 };
		b.mCtrlPts1 = new float[] { 20, 4, 18, 2, 16, 2, 14, 2 };
		mBeziers.add(b);
		b = new KittyBezier(COLOR_WHITE, t += 500, 500, TRANSLATE_MAIN,
				ROTATE_NONE, SCALE);
		b.mCtrlPts0 = new float[] { 5, 6, 4, 4, 4, 0, 8, 0 };
		b.mCtrlPts1 = new float[] { 14, 2, 13, 1, 12, 0, 8, 0 };
		mBeziers.add(b);
		
		// Right fg...
		b = new KittyBezier(COLOR_GRAY, t += 200, 200, TRANSLATE_MAIN,
				ROTATE_NONE, SCALE);
		b.mCtrlPts0 = new float[] { 14, 2, 18, 2, 20, 4 };
		b.mCtrlPts1 = new float[] { 14, 1, 19, 1, 21, 3 };
		mBeziers.add(b);
		b = new KittyBezier(COLOR_GRAY, t += 200, 200, TRANSLATE_MAIN,
				ROTATE_NONE, SCALE);
		b.mCtrlPts0 = new float[] { 20, 4, 22, 6, 22, 8, 20, 10 };
		b.mCtrlPts1 = new float[] { 21, 3, 23, 5, 23, 9, 21, 11 };
		mBeziers.add(b);
		b = new KittyBezier(COLOR_GRAY, t += 200, 200, TRANSLATE_MAIN,
				ROTATE_NONE, SCALE);
		b.mCtrlPts0 = new float[] { 20, 10, 18, 12, 15, 13, 11, 11 };
		b.mCtrlPts1 = new float[] { 21, 11, 19, 13, 14, 14, 10, 12 };
		mBeziers.add(b);
		b = new KittyBezier(COLOR_GRAY, t += 200, 200, TRANSLATE_MAIN,
				ROTATE_NONE, SCALE);
		b.mCtrlPts0 = new float[] { 11, 11, 7, 9, 6, 8, 5, 6 };
		b.mCtrlPts1 = new float[] { 10, 12, 6, 10, 5, 9, 4, 7 };
		mBeziers.add(b);
		b = new KittyBezier(COLOR_GRAY, t += 200, 200, TRANSLATE_MAIN,
				ROTATE_NONE, SCALE);
		b.mCtrlPts0 = new float[] { 5, 6, 4, 4, 4, 0, 8, 0 };
		b.mCtrlPts1 = new float[] { 4, 7, 2, 5, 3, -1, 8, -1 };
		mBeziers.add(b);
		b = new KittyBezier(COLOR_GRAY, t += 200, 200, TRANSLATE_MAIN,
				ROTATE_NONE, SCALE);
		b.mCtrlPts0 = new float[] { 8, 0, 12, 0, 14, 2 };
		b.mCtrlPts1 = new float[] { 8, -1, 12, -1, 14, 1 };
		mBeziers.add(b);
		
		return t;
	}

	private long genNose(long t) {
		KittyBezier b;
		b = new KittyBezier(COLOR_YELLOW, t += 500, 500, TRANSLATE_MAIN,
				ROTATE_NONE, SCALE);
		b.mCtrlPts0 = new float[] { -3, 17, -3, 20, 3, 20, 3, 17 };
		b.mCtrlPts1 = new float[] { -3, 17, -3, 14, 3, 14, 3, 17 };
		mBeziers.add(b);
		b = new KittyBezier(COLOR_GRAY, t += 800, 200,
				TRANSLATE_MAIN, ROTATE_NONE, SCALE);
		b.mCtrlPts0 = new float[] { -4, 17, -4, 21, 4, 21, 4, 17 };
		b.mCtrlPts1 = new float[] { -3, 17, -3, 20, 3, 20, 3, 17 };
		mBeziers.add(b);
		b = new KittyBezier(COLOR_GRAY, t += 800, 200,
				TRANSLATE_MAIN, ROTATE_NONE, SCALE);
		b.mCtrlPts0 = new float[] { 4, 17, 4, 13, -4, 13, -4, 17 };
		b.mCtrlPts1 = new float[] { 3, 17, 3, 14, -3, 14, -3, 17 };
		mBeziers.add(b);

		return t;
	}

	public Vector<KittyBezier> getBeziers() {
		return mBeziers;
	}

}
