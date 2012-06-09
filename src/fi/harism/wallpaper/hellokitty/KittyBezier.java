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
public final class KittyBezier {
	// Bezier color.
	public float[] mColor;
	// Bezier control points top.
	public float[] mPts0;
	// Bezier control points bottom.
	public float[] mPts1;
	// Bezier draw start time and duration.
	public long mTimeStart, mTimeDuration;

	/**
	 * Default constructor.
	 */
	KittyBezier(float[] color, long timeStart, long timeDuration) {
		mColor = color;
		mTimeStart = timeStart;
		mTimeDuration = timeDuration;
	}
}
