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

import java.util.Vector;

/**
 * Kitty object, container for bezier fills.
 */
public class KittyObject {

	// Default colors.
	private static final float[] COLOR_GRAY = { 0.376f, 0.376f, 0.377f };
	private static final float[] COLOR_PINK = { 0.906f, 0.314f, 0.584f };
	private static final float[] COLOR_WHITE = { 1f, 1f, 1f };
	private static final float[] COLOR_YELLOW = { 1f, 0.792f, 0.043f };

	// Modifier constants.
	private static final float ROTATE_NONE = 0f;
	private static final float SCALE = 0.03f;
	private static final float[] TRANSLATE_MAIN = { 0f, -25f };

	// Bezier array.
	private final Vector<KittyBezier> mBeziers = new Vector<KittyBezier>();

	/**
	 * Default constructor.
	 */
	public KittyObject() {
		long t = genBgHeart(100);
		t = genBgHead(t);
		t = genHead(t);
		t = genPaws(t);
		t = genNose(t);
		t = genEyes(t);
		t = genHair(t);
		t = genBgBow(t);
		t = genBow(t);
	}

	private long genBgBow(long t) {
		KittyBezier b;

		// Middle...
		b = new KittyBezier(COLOR_PINK, t += 700, 200, TRANSLATE_MAIN,
				ROTATE_NONE, SCALE);
		b.mCtrlPts0 = new float[] { 12, 38, 13, 39, 16, 39, 17, 38 };
		b.mCtrlPts1 = new float[] { 12, 38, 11, 37, 11, 34, 12, 33 };
		mBeziers.add(b);
		b = new KittyBezier(COLOR_PINK, t += 200, 200, TRANSLATE_MAIN,
				ROTATE_NONE, SCALE);
		b.mCtrlPts0 = new float[] { 17, 38, 18, 37, 18, 34, 17, 33 };
		b.mCtrlPts1 = new float[] { 12, 33, 13, 32, 16, 32, 17, 33 };
		mBeziers.add(b);

		// Left...
		b = new KittyBezier(COLOR_PINK, t += 200, 200, TRANSLATE_MAIN,
				ROTATE_NONE, SCALE);
		b.mCtrlPts0 = new float[] { 8, 47, 13, 48, 14, 44, 14, 38 };
		b.mCtrlPts1 = new float[] { 8, 47, 3, 46, 0, 38, 3, 35 };
		mBeziers.add(b);
		b = new KittyBezier(COLOR_PINK, t += 200, 200, TRANSLATE_MAIN,
				ROTATE_NONE, SCALE);
		b.mCtrlPts0 = new float[] { 14, 38, 14, 38, 14, 38 };
		b.mCtrlPts1 = new float[] { 3, 35, 7, 31, 12, 35 };
		mBeziers.add(b);

		// Right...
		b = new KittyBezier(COLOR_PINK, t += 200, 200, TRANSLATE_MAIN,
				ROTATE_NONE, SCALE);
		b.mCtrlPts0 = new float[] { 28, 37, 26, 41, 19, 39, 17, 37 };
		b.mCtrlPts1 = new float[] { 28, 37, 30, 33, 27, 26, 23, 26 };
		mBeziers.add(b);
		b = new KittyBezier(COLOR_PINK, t += 200, 200, TRANSLATE_MAIN,
				ROTATE_NONE, SCALE);
		b.mCtrlPts0 = new float[] { 17, 37, 17, 37, 17, 37 };
		b.mCtrlPts1 = new float[] { 23, 26, 19, 26, 16, 34 };
		mBeziers.add(b);

		return t;

	}

	private long genBgHead(long t) {
		KittyBezier b;
		// Left ear.
		b = new KittyBezier(COLOR_WHITE, t += 700, 200, TRANSLATE_MAIN,
				ROTATE_NONE, SCALE);
		b.mCtrlPts0 = new float[] { -24, 46, -26, 44, -24, 34 };
		b.mCtrlPts1 = new float[] { -24, 46, -22, 48, -12, 42 };
		mBeziers.add(b);
		// Right ear.
		b = new KittyBezier(COLOR_WHITE, t += 700, 200, TRANSLATE_MAIN,
				ROTATE_NONE, SCALE);
		b.mCtrlPts0 = new float[] { 24, 46, 26, 44, 24, 34 };
		b.mCtrlPts1 = new float[] { 24, 46, 22, 48, 12, 42 };
		mBeziers.add(b);
		// Skull.
		b = new KittyBezier(COLOR_WHITE, t += 200, 200, TRANSLATE_MAIN,
				ROTATE_NONE, SCALE);
		b.mCtrlPts0 = new float[] { 0, 44, -4, 44, -12, 42 };
		b.mCtrlPts1 = new float[] { 0, 44, 4, 44, 12, 42 };
		mBeziers.add(b);
		b = new KittyBezier(COLOR_WHITE, t += 200, 200, TRANSLATE_MAIN,
				ROTATE_NONE, SCALE);
		b.mCtrlPts0 = new float[] { -12, 42, -20, 40, -24, 34 };
		b.mCtrlPts1 = new float[] { 12, 42, 20, 40, 24, 34 };
		mBeziers.add(b);
		b = new KittyBezier(COLOR_WHITE, t += 200, 200, TRANSLATE_MAIN,
				ROTATE_NONE, SCALE);
		b.mCtrlPts0 = new float[] { -24, 34, -28, 32, -28, 26 };
		b.mCtrlPts1 = new float[] { 24, 34, 28, 32, 28, 26 };
		mBeziers.add(b);
		b = new KittyBezier(COLOR_WHITE, t += 200, 200, TRANSLATE_MAIN,
				ROTATE_NONE, SCALE);
		b.mCtrlPts0 = new float[] { -28, 26, -28, 20, -26, 14, -20, 10 };
		b.mCtrlPts1 = new float[] { 28, 26, 28, 20, 26, 14, 20, 10 };
		mBeziers.add(b);
		b = new KittyBezier(COLOR_WHITE, t += 200, 200, TRANSLATE_MAIN,
				ROTATE_NONE, SCALE);
		b.mCtrlPts0 = new float[] { -20, 10, -14, 6, -6, 6, 0, 6 };
		b.mCtrlPts1 = new float[] { 20, 10, 14, 6, 6, 6, 0, 6 };
		mBeziers.add(b);

		return t;
	}

	private long genBgHeart(long t) {
		KittyBezier b;
		b = new KittyBezier(COLOR_PINK, t += 1000, 1000, TRANSLATE_MAIN,
				ROTATE_NONE, SCALE);
		b.mCtrlPts0 = new float[] { -36, 41, -20, 64, 0, 43 };
		b.mCtrlPts1 = new float[] { -36, 41, -52, 18, 0, -7 };
		mBeziers.add(b);
		b = new KittyBezier(COLOR_PINK, t += 1000, 1000, TRANSLATE_MAIN,
				ROTATE_NONE, SCALE);
		b.mCtrlPts0 = new float[] { 0, 43, 20, 64, 36, 41 };
		b.mCtrlPts1 = new float[] { 0, -7, 52, 18, 36, 41 };
		mBeziers.add(b);

		return t;
	}

	private long genBow(long t) {
		KittyBezier b;

		// Left...
		b = new KittyBezier(COLOR_GRAY, t += 700, 200, TRANSLATE_MAIN,
				ROTATE_NONE, SCALE);
		b.mCtrlPts0 = new float[] { 11, 35, 6, 32, 3, 35 };
		b.mCtrlPts1 = new float[] { 11, 34, 5, 31, 2, 34 };
		mBeziers.add(b);
		b = new KittyBezier(COLOR_GRAY, t += 200, 200, TRANSLATE_MAIN,
				ROTATE_NONE, SCALE);
		b.mCtrlPts0 = new float[] { 3, 35, 0, 38, 3, 46, 8, 47 };
		b.mCtrlPts1 = new float[] { 2, 34, -1, 38, 3, 47, 8, 48 };
		mBeziers.add(b);
		b = new KittyBezier(COLOR_GRAY, t += 200, 200, TRANSLATE_MAIN,
				ROTATE_NONE, SCALE);
		b.mCtrlPts0 = new float[] { 8, 47, 13, 48, 14, 39 };
		b.mCtrlPts1 = new float[] { 8, 48, 14, 49, 15, 39 };
		mBeziers.add(b);

		// Right...
		b = new KittyBezier(COLOR_GRAY, t += 700, 200, TRANSLATE_MAIN,
				ROTATE_NONE, SCALE);
		b.mCtrlPts0 = new float[] { 18, 37, 26, 41, 28, 37 };
		b.mCtrlPts1 = new float[] { 18, 38, 27, 42, 29, 38 };
		mBeziers.add(b);
		b = new KittyBezier(COLOR_GRAY, t += 200, 200, TRANSLATE_MAIN,
				ROTATE_NONE, SCALE);
		b.mCtrlPts0 = new float[] { 28, 37, 30, 33, 27, 26, 23, 26 };
		b.mCtrlPts1 = new float[] { 29, 38, 32, 34, 27, 25, 23, 25 };
		mBeziers.add(b);
		b = new KittyBezier(COLOR_GRAY, t += 200, 200, TRANSLATE_MAIN,
				ROTATE_NONE, SCALE);
		b.mCtrlPts0 = new float[] { 23, 26, 19, 26, 17, 33 };
		b.mCtrlPts1 = new float[] { 23, 25, 19, 25, 16, 32 };
		mBeziers.add(b);

		// Center...
		b = new KittyBezier(COLOR_GRAY, t += 700, 200, TRANSLATE_MAIN,
				ROTATE_NONE, SCALE);
		b.mCtrlPts0 = new float[] { 12, 33, 11, 34, 11, 37, 12, 38 };
		b.mCtrlPts1 = new float[] { 11.5f, 32.5f, 10, 33, 10, 38, 11.5f, 38.5f };
		mBeziers.add(b);
		b = new KittyBezier(COLOR_GRAY, t += 200, 200, TRANSLATE_MAIN,
				ROTATE_NONE, SCALE);
		b.mCtrlPts0 = new float[] { 12, 38, 13, 39, 16, 39, 17, 38 };
		b.mCtrlPts1 = new float[] { 11.5f, 38.5f, 12, 40, 16, 40, 17.5f, 38.5f };
		mBeziers.add(b);
		b = new KittyBezier(COLOR_GRAY, t += 200, 200, TRANSLATE_MAIN,
				ROTATE_NONE, SCALE);
		b.mCtrlPts0 = new float[] { 17, 38, 18, 37, 18, 34, 17, 33 };
		b.mCtrlPts1 = new float[] { 17.5f, 38.5f, 19, 38, 19, 33, 17.5f, 32.5f };
		mBeziers.add(b);
		b = new KittyBezier(COLOR_GRAY, t += 200, 200, TRANSLATE_MAIN,
				ROTATE_NONE, SCALE);
		b.mCtrlPts0 = new float[] { 17, 33, 16, 32, 13, 32, 12, 33 };
		b.mCtrlPts1 = new float[] { 17.5f, 32.5f, 17, 31, 12, 31, 11.5f, 32.5f };
		mBeziers.add(b);

		// Knot left..
		b = new KittyBezier(COLOR_GRAY, t += 200, 200, TRANSLATE_MAIN,
				ROTATE_NONE, SCALE);
		b.mCtrlPts0 = new float[] { 11, 36, 9, 37, 9, 39 };
		b.mCtrlPts1 = new float[] { 11, 35, 8, 36, 8, 40 };
		mBeziers.add(b);
		b = new KittyBezier(COLOR_GRAY, t += 200, 200, TRANSLATE_MAIN,
				ROTATE_NONE, SCALE);
		b.mCtrlPts0 = new float[] { 9, 39, 11, 41, 13, 39 };
		b.mCtrlPts1 = new float[] { 8, 40, 11, 42, 14, 39 };
		mBeziers.add(b);

		// Knot right..
		b = new KittyBezier(COLOR_GRAY, t += 200, 200, TRANSLATE_MAIN,
				ROTATE_NONE, SCALE);
		b.mCtrlPts0 = new float[] { 18, 36, 22, 35, 21, 33 };
		b.mCtrlPts1 = new float[] { 18, 37, 23, 36, 22, 34 };
		mBeziers.add(b);
		b = new KittyBezier(COLOR_GRAY, t += 200, 200, TRANSLATE_MAIN,
				ROTATE_NONE, SCALE);
		b.mCtrlPts0 = new float[] { 21, 33, 20, 31, 18, 33 };
		b.mCtrlPts1 = new float[] { 22, 34, 21, 30, 17, 32 };
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
		b = new KittyBezier(COLOR_GRAY, t += 500, 500, TRANSLATE_MAIN,
				ROTATE_NONE, SCALE);
		b.mCtrlPts0 = new float[] { 17, 24, 20, 24, 20, 18, 17, 18 };
		b.mCtrlPts1 = new float[] { 17, 24, 14, 24, 14, 18, 17, 18 };
		mBeziers.add(b);

		return t;
	}

	private long genHair(long t) {
		KittyBezier b;

		// Left...
		b = new KittyBezier(COLOR_GRAY, t += 700, 200, TRANSLATE_MAIN,
				ROTATE_NONE, SCALE);
		b.mCtrlPts0 = new float[] { -21, 13, -25, 12, -27, 10 };
		b.mCtrlPts1 = new float[] { -21, 14, -26, 13, -28, 11 };
		mBeziers.add(b);
		b = new KittyBezier(COLOR_GRAY, t += 700, 200, TRANSLATE_MAIN,
				ROTATE_NONE, SCALE);
		b.mCtrlPts0 = new float[] { -24, 17, -28, 17, -30, 15 };
		b.mCtrlPts1 = new float[] { -24, 18, -29, 18, -31, 16 };
		mBeziers.add(b);
		b = new KittyBezier(COLOR_GRAY, t += 700, 200, TRANSLATE_MAIN,
				ROTATE_NONE, SCALE);
		b.mCtrlPts0 = new float[] { -24, 22, -29, 23, -32, 21 };
		b.mCtrlPts1 = new float[] { -24, 23, -29, 24, -33, 22 };
		mBeziers.add(b);

		// Right...
		b = new KittyBezier(COLOR_GRAY, t += 700, 200, TRANSLATE_MAIN,
				ROTATE_NONE, SCALE);
		b.mCtrlPts0 = new float[] { 21, 13, 25, 12, 27, 10 };
		b.mCtrlPts1 = new float[] { 21, 14, 26, 13, 28, 11 };
		mBeziers.add(b);
		b = new KittyBezier(COLOR_GRAY, t += 700, 200, TRANSLATE_MAIN,
				ROTATE_NONE, SCALE);
		b.mCtrlPts0 = new float[] { 24, 17, 28, 17, 30, 15 };
		b.mCtrlPts1 = new float[] { 24, 18, 29, 18, 31, 16 };
		mBeziers.add(b);
		b = new KittyBezier(COLOR_GRAY, t += 700, 200, TRANSLATE_MAIN,
				ROTATE_NONE, SCALE);
		b.mCtrlPts0 = new float[] { 24, 22, 29, 23, 32, 21 };
		b.mCtrlPts1 = new float[] { 24, 23, 29, 24, 33, 22 };
		mBeziers.add(b);

		return t;
	}

	private long genHead(long t) {
		KittyBezier b;

		// Left ear...
		b = new KittyBezier(COLOR_GRAY, t += 200, 200, TRANSLATE_MAIN,
				ROTATE_NONE, SCALE);
		b.mCtrlPts0 = new float[] { -24, 34, -26, 44, -24, 46 };
		b.mCtrlPts1 = new float[] { -25, 34, -27, 45, -25, 47 };
		mBeziers.add(b);
		b = new KittyBezier(COLOR_GRAY, t += 200, 200, TRANSLATE_MAIN,
				ROTATE_NONE, SCALE);
		b.mCtrlPts0 = new float[] { -24, 46, -22, 48, -12, 42 };
		b.mCtrlPts1 = new float[] { -25, 47, -23, 49, -11, 43 };
		mBeziers.add(b);

		// Right ear...
		b = new KittyBezier(COLOR_GRAY, t += 700, 200, TRANSLATE_MAIN,
				ROTATE_NONE, SCALE);
		b.mCtrlPts0 = new float[] { 12, 42, 22, 48, 24, 46 };
		b.mCtrlPts1 = new float[] { 11, 43, 23, 49, 25, 47 };
		mBeziers.add(b);
		b = new KittyBezier(COLOR_GRAY, t += 200, 200, TRANSLATE_MAIN,
				ROTATE_NONE, SCALE);
		b.mCtrlPts0 = new float[] { 24, 46, 26, 44, 24, 34 };
		b.mCtrlPts1 = new float[] { 25, 47, 27, 45, 25, 34 };
		mBeziers.add(b);

		// Skull...
		b = new KittyBezier(COLOR_GRAY, t += 700, 200, TRANSLATE_MAIN,
				ROTATE_NONE, SCALE);
		b.mCtrlPts0 = new float[] { -12, 42, -4, 44, 0, 44 };
		b.mCtrlPts1 = new float[] { -12, 43, -3, 45, 0, 45 };
		mBeziers.add(b);
		b = new KittyBezier(COLOR_GRAY, t += 200, 200, TRANSLATE_MAIN,
				ROTATE_NONE, SCALE);
		b.mCtrlPts0 = new float[] { 0, 44, 4, 44, 12, 42 };
		b.mCtrlPts1 = new float[] { 0, 45, 3, 45, 12, 43 };
		mBeziers.add(b);
		b = new KittyBezier(COLOR_GRAY, t += 700, 200, TRANSLATE_MAIN,
				ROTATE_NONE, SCALE);
		b.mCtrlPts0 = new float[] { -23, 34, -28, 32, -28, 26 };
		b.mCtrlPts1 = new float[] { -24, 35, -29, 33, -29, 26 };
		mBeziers.add(b);
		b = new KittyBezier(COLOR_GRAY, t += 200, 200, TRANSLATE_MAIN,
				ROTATE_NONE, SCALE);
		b.mCtrlPts0 = new float[] { -28, 26, -28, 20, -26, 14, -20, 10 };
		b.mCtrlPts1 = new float[] { -29, 26, -29, 20, -27, 13, -21, 9 };
		mBeziers.add(b);
		b = new KittyBezier(COLOR_GRAY, t += 200, 200, TRANSLATE_MAIN,
				ROTATE_NONE, SCALE);
		b.mCtrlPts0 = new float[] { -20, 10, -14, 6, -6, 6, 0, 6 };
		b.mCtrlPts1 = new float[] { -21, 9, -15, 5, -6, 5, 0, 5 };
		mBeziers.add(b);
		b = new KittyBezier(COLOR_GRAY, t += 200, 200, TRANSLATE_MAIN,
				ROTATE_NONE, SCALE);
		b.mCtrlPts0 = new float[] { 0, 6, 6, 6, 14, 6, 20, 10 };
		b.mCtrlPts1 = new float[] { 0, 5, 6, 5, 15, 5, 21, 9 };
		mBeziers.add(b);
		b = new KittyBezier(COLOR_GRAY, t += 200, 200, TRANSLATE_MAIN,
				ROTATE_NONE, SCALE);
		b.mCtrlPts0 = new float[] { 20, 10, 26, 14, 28, 20, 28, 26 };
		b.mCtrlPts1 = new float[] { 21, 9, 27, 13, 29, 20, 29, 26 };
		mBeziers.add(b);
		b = new KittyBezier(COLOR_GRAY, t += 200, 200, TRANSLATE_MAIN,
				ROTATE_NONE, SCALE);
		b.mCtrlPts0 = new float[] { 28, 26, 28, 32, 23, 34 };
		b.mCtrlPts1 = new float[] { 29, 26, 29, 33, 24, 35 };
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
		b = new KittyBezier(COLOR_GRAY, t += 800, 200, TRANSLATE_MAIN,
				ROTATE_NONE, SCALE);
		b.mCtrlPts0 = new float[] { -4, 17, -4, 21, 4, 21, 4, 17 };
		b.mCtrlPts1 = new float[] { -3, 17, -3, 20, 3, 20, 3, 17 };
		mBeziers.add(b);
		b = new KittyBezier(COLOR_GRAY, t += 800, 200, TRANSLATE_MAIN,
				ROTATE_NONE, SCALE);
		b.mCtrlPts0 = new float[] { 4, 17, 4, 13, -4, 13, -4, 17 };
		b.mCtrlPts1 = new float[] { 3, 17, 3, 14, -3, 14, -3, 17 };
		mBeziers.add(b);

		return t;
	}

	private long genPaws(long t) {
		KittyBezier b;

		// Left bg...
		b = new KittyBezier(COLOR_WHITE, t += 700, 200, TRANSLATE_MAIN,
				ROTATE_NONE, SCALE);
		b.mCtrlPts0 = new float[] { -20, 10, -18, 12, -15, 13, -11, 11 };
		b.mCtrlPts1 = new float[] { -20, 10, -22, 8, -22, 6, -20, 4 };
		mBeziers.add(b);
		b = new KittyBezier(COLOR_WHITE, t += 200, 200, TRANSLATE_MAIN,
				ROTATE_NONE, SCALE);
		b.mCtrlPts0 = new float[] { -11, 11, -7, 9, -6, 8, -5, 6 };
		b.mCtrlPts1 = new float[] { -20, 4, -18, 2, -16, 2, -14, 2 };
		mBeziers.add(b);
		b = new KittyBezier(COLOR_WHITE, t += 200, 200, TRANSLATE_MAIN,
				ROTATE_NONE, SCALE);
		b.mCtrlPts0 = new float[] { -5, 6, -4, 4, -4, 0, -8, 0 };
		b.mCtrlPts1 = new float[] { -14, 2, -13, 1, -12, 0, -8, 0 };
		mBeziers.add(b);

		// Left fg...
		b = new KittyBezier(COLOR_GRAY, t += 700, 200, TRANSLATE_MAIN,
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
		b = new KittyBezier(COLOR_WHITE, t += 700, 200, TRANSLATE_MAIN,
				ROTATE_NONE, SCALE);
		b.mCtrlPts0 = new float[] { 20, 10, 18, 12, 15, 13, 11, 11 };
		b.mCtrlPts1 = new float[] { 20, 10, 22, 8, 22, 6, 20, 4 };
		mBeziers.add(b);
		b = new KittyBezier(COLOR_WHITE, t += 200, 200, TRANSLATE_MAIN,
				ROTATE_NONE, SCALE);
		b.mCtrlPts0 = new float[] { 11, 11, 7, 9, 6, 8, 5, 6 };
		b.mCtrlPts1 = new float[] { 20, 4, 18, 2, 16, 2, 14, 2 };
		mBeziers.add(b);
		b = new KittyBezier(COLOR_WHITE, t += 200, 200, TRANSLATE_MAIN,
				ROTATE_NONE, SCALE);
		b.mCtrlPts0 = new float[] { 5, 6, 4, 4, 4, 0, 8, 0 };
		b.mCtrlPts1 = new float[] { 14, 2, 13, 1, 12, 0, 8, 0 };
		mBeziers.add(b);

		// Right fg...
		b = new KittyBezier(COLOR_GRAY, t += 700, 200, TRANSLATE_MAIN,
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

	public Vector<KittyBezier> getBeziers() {
		return mBeziers;
	}

}
