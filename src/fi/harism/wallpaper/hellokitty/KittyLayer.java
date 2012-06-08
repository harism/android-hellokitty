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

public final class KittyLayer {

	// Beziers array.
	public final Vector<KittyBezier> mBeziers = new Vector<KittyBezier>();
	// Bezier color.
	public float[] mColor;
	// Layer name.
	public String mName;
	// Control point scale.
	public float mScale;
	// Control point translate.
	public float[] mTranslate;

	public KittyLayer(String name, float[] color, float[] translate, float scale) {
		mName = name;
		mColor = color;
		mTranslate = translate;
		mScale = scale;
	}

}
