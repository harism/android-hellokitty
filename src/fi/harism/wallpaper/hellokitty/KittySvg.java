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

import java.io.InputStream;
import java.util.Vector;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.AttributeList;
import org.xml.sax.HandlerBase;

import android.graphics.Color;

public final class KittySvg extends HandlerBase {

	private KittyLayer mLayerCurrent;
	private Vector<KittyLayer> mLayers = new Vector<KittyLayer>();

	@Override
	public void endElement(String name) {
		if (name.equals("layer")) {
			mLayerCurrent.mTranslate[0] = 0;
			mLayerCurrent.mTranslate[1] = 0;
			mLayerCurrent.mScale = 1;
			mLayers.add(mLayerCurrent);
		}
	}

	public KittyLayer getLayer(String id) {
		for (KittyLayer layer : mLayers) {
			if (layer.mName.equals(id)) {
				return layer;
			}
		}
		return null;
	}

	public Vector<KittyLayer> getLayers() {
		return mLayers;
	}

	public void read(InputStream is) throws Exception {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser parser = factory.newSAXParser();
		parser.parse(is, this);
	}

	private float[] readCoords(String d, int count) {
		float[] ret = new float[count * 2];

		for (int i = 0; i < count; ++i) {
			int splitIdx = d.indexOf(',');
			int endIdx = d.indexOf(' ');
			if (endIdx == -1) {
				endIdx = d.length();
			}

			ret[i * 2 + 0] = Float.parseFloat(d.substring(0, splitIdx));
			ret[i * 2 + 1] = Float
					.parseFloat(d.substring(splitIdx + 1, endIdx));

			d = d.substring(endIdx).trim();
		}

		return ret;
	}

	@Override
	public void startElement(String name, AttributeList attrs) {
		if (name.equals("layer")) {
			String id = attrs.getValue("id");

			int color = Color.parseColor(attrs.getValue("color"));
			float colorArr[] = new float[3];
			colorArr[0] = Color.red(color) / 255f;
			colorArr[1] = Color.green(color) / 255f;
			colorArr[2] = Color.blue(color) / 255f;

			float[] translate = readCoords(attrs.getValue("translate"), 1);
			float scale = Float.parseFloat(attrs.getValue("scale"));

			mLayerCurrent = new KittyLayer(id, colorArr, translate, scale);
		}
		if (name.equals("bezier")) {
			float[] time = readCoords(attrs.getValue("time"), 1);
			KittyBezier bezier = new KittyBezier((long) time[0], (long) time[1]);
			bezier.mCtrlPts0 = readCoords(attrs.getValue("ctrl1"), 4);
			bezier.mCtrlPts1 = readCoords(attrs.getValue("ctrl2"), 4);
			for (int i = 0; i < 8; ++i) {
				bezier.mCtrlPts0[i] += mLayerCurrent.mTranslate[i % 2];
				bezier.mCtrlPts0[i] *= mLayerCurrent.mScale;
				bezier.mCtrlPts1[i] += mLayerCurrent.mTranslate[i % 2];
				bezier.mCtrlPts1[i] *= mLayerCurrent.mScale;
			}
			mLayerCurrent.mBeziers.add(bezier);
		}
	}
}
