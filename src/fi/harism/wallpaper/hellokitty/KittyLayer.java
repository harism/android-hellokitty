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

import android.graphics.Matrix;

public final class KittyLayer {

	// Beziers array.
	public final Vector<KittyBezier> mBeziers = new Vector<KittyBezier>();
	// Layer name.
	public String mId;
	// Transformation matrix.
	public final Matrix mTransform = new Matrix();

	public KittyLayer(String id, Matrix transform) {
		mId = id;
		mTransform.set(transform);
	}

	public void add(KittyBezier bezier) {
		mBeziers.add(bezier);
	}

}
