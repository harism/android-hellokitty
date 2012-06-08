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

	/**
	 * Calculates normal at given t.
	 */
	private float[] calculateNormal(float[] bezier, float t) {
		float ret[] = new float[2];
		// Calculate x -coordinate.
		float xq0 = bezier[0] + (bezier[2] - bezier[0]) * t;
		float xq1 = bezier[2] + (bezier[4] - bezier[2]) * t;
		float xq2 = bezier[4] + (bezier[6] - bezier[4]) * t;
		float xr0 = xq0 + (xq1 - xq0) * t;
		float xr1 = xq1 + (xq2 - xq1) * t;
		// Calculate y -coordinate.
		float yq0 = bezier[1] + (bezier[3] - bezier[1]) * t;
		float yq1 = bezier[3] + (bezier[5] - bezier[3]) * t;
		float yq2 = bezier[5] + (bezier[7] - bezier[5]) * t;
		float yr0 = yq0 + (yq1 - yq0) * t;
		float yr1 = yq1 + (yq2 - yq1) * t;
		// Normal direction.
		ret[0] = yr0 - yr1;
		ret[1] = xr1 - xr0;
		// Normalize length.
		float len = (float) Math.sqrt(ret[0] * ret[0] + ret[1] * ret[1]);
		ret[0] /= len;
		ret[1] /= len;
		return ret;
	}

	/**
	 * Pre-compute translate and scale values into bezier.
	 */
	private void calculateScaleAndTranslate(KittyBezier bezier,
			float[] translate, float scale) {
		for (int i = 0; i < 8; ++i) {
			bezier.mCtrlPts0[i] += translate[i % 2];
			bezier.mCtrlPts0[i] *= scale;
			bezier.mCtrlPts1[i] += translate[i % 2];
			bezier.mCtrlPts1[i] *= scale;
		}
	}

	@Override
	public void endElement(String name) {
		if (name.equals("layer")) {
			// Translate and scaling is done into beziers already.
			mLayerCurrent.mTranslate[0] = 0;
			mLayerCurrent.mTranslate[1] = 0;
			mLayerCurrent.mScale = 1;
			mLayers.add(mLayerCurrent);
		}
	}

	/**
	 * Returns layer with given id, or null if not found.
	 */
	public KittyLayer getLayer(String id) {
		for (KittyLayer layer : mLayers) {
			if (layer.mName.equals(id)) {
				return layer;
			}
		}
		return null;
	}

	/**
	 * Returns all layers.
	 */
	public Vector<KittyLayer> getLayers() {
		return mLayers;
	}

	/**
	 * Reads bezier xml file from given InputStream.
	 */
	public void read(InputStream is) throws Exception {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser parser = factory.newSAXParser();
		parser.parse(is, this);
	}

	/**
	 * Reads values from given String of format "xx,yy,.. xx,yy,.. xx,yy,..".
	 */
	private float[] readValues(String d, int values, int count) {
		float[] ret = new float[values * count];
		// We read count number of elements.
		for (int i = 0; i < count; ++i) {
			// Each element contains values number of values.
			for (int j = 0; j < values; ++j) {
				int splitIdx = d.indexOf(',');
				if (j == values - 1) {
					splitIdx = d.indexOf(' ');
					splitIdx = splitIdx == -1 ? d.length() : splitIdx;
				}

				ret[i * values + j] = Float
						.parseFloat(d.substring(0, splitIdx));

				if (splitIdx < d.length())
					++splitIdx;
				d = d.substring(splitIdx);
			}
			d = d.trim();
		}

		return ret;
	}

	@Override
	public void startElement(String name, AttributeList attrs) {
		// New layer.
		if (name.equals("layer")) {
			String id = attrs.getValue("id");
			float[] translate = readValues(attrs.getValue("translate"), 2, 1);
			float scale = Float.parseFloat(attrs.getValue("scale"));
			mLayerCurrent = new KittyLayer(id, translate, scale);
		}
		// New fill element.
		if (name.equals("fill")) {
			float[] time = readValues(attrs.getValue("time"), 1, 2);

			int color = Color.parseColor(attrs.getValue("color"));
			float colorArr[] = new float[3];
			colorArr[0] = Color.red(color) / 255f;
			colorArr[1] = Color.green(color) / 255f;
			colorArr[2] = Color.blue(color) / 255f;

			KittyBezier bezier = new KittyBezier(colorArr, (long) time[0],
					(long) time[1]);

			bezier.mCtrlPts0 = readValues(attrs.getValue("pts1"), 2, 4);
			bezier.mCtrlPts1 = readValues(attrs.getValue("pts2"), 2, 4);

			calculateScaleAndTranslate(bezier, mLayerCurrent.mTranslate,
					mLayerCurrent.mScale);
			mLayerCurrent.add(bezier);
		}
		// New line element.
		if (name.equals("line")) {
			float[] time = readValues(attrs.getValue("time"), 1, 2);
			float scale[] = readValues(attrs.getValue("scale"), 1, 4);

			int color = Color.parseColor(attrs.getValue("color"));
			float colorArr[] = new float[3];
			colorArr[0] = Color.red(color) / 255f;
			colorArr[1] = Color.green(color) / 255f;
			colorArr[2] = Color.blue(color) / 255f;

			KittyBezier bezier = new KittyBezier(colorArr, (long) time[0],
					(long) time[1]);

			bezier.mCtrlPts0 = readValues(attrs.getValue("pts"), 2, 4);
			bezier.mCtrlPts1 = new float[8];

			float[] normal0 = calculateNormal(bezier.mCtrlPts0, 0);
			float[] normal1 = calculateNormal(bezier.mCtrlPts0, 1);

			for (int i = 0; i < 4; ++i) {
				float diff = normal0[i % 2] * scale[0] / 2;
				bezier.mCtrlPts1[i] = bezier.mCtrlPts0[i] + diff;
				bezier.mCtrlPts0[i] -= diff;

				diff = normal1[i % 2] * scale[3] / 2;
				bezier.mCtrlPts1[i + 4] = bezier.mCtrlPts0[i + 4] + diff;
				bezier.mCtrlPts0[i + 4] -= diff;
			}
			for (int i = 0; i < 2; ++i) {
				float diff = (bezier.mCtrlPts1[i + 2] - bezier.mCtrlPts1[i + 0])
						* scale[1];
				bezier.mCtrlPts1[i + 2] += diff;
				diff = (bezier.mCtrlPts0[i + 4] - bezier.mCtrlPts1[i + 6])
						* scale[2];
				bezier.mCtrlPts1[i + 4] += diff;
			}

			calculateScaleAndTranslate(bezier, mLayerCurrent.mTranslate,
					mLayerCurrent.mScale);
			mLayerCurrent.add(bezier);
		}
	}
}
