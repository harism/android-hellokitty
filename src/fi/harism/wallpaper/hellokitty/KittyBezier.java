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

/**
 * Container class for bezier fills.
 */
public class KittyBezier {
	// Bezier color.
	public float[] mColor;
	// Bezier control points top.
	public float[] mCtrlPts0;
	// Bezier control points bottom.
	public float[] mCtrlPts1;
	// Control point rotate and scale.
	public float mRotate, mScale;
	// Bezier draw start time and duration.
	public long mTimeStart, mTimeDuration;
	// Control point translate.
	public float[] mTranslate;

	/**
	 * Default constructor.
	 */
	KittyBezier(float[] color, long timeStart, long timeDuration,
			float[] translate, float rotate, float scale) {
		mColor = color;
		mTimeStart = timeStart;
		mTimeDuration = timeDuration;
		mTranslate = translate;
		mRotate = rotate;
		mScale = scale;
	}
}
