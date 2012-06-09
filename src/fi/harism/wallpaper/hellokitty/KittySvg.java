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
import android.graphics.Matrix;

public final class KittySvg extends HandlerBase {

	private KittyLayer mLayerCurrent;
	private Vector<KittyLayer> mLayers = new Vector<KittyLayer>();

	private KittyBezier allocBezier(AttributeList attrs, int idx) {
		// Read start and end time.
		Vector<Float> t = readValues(attrs.getValue("time"));
		long tStart = Math.round(t.get(idx * 2 + 0));
		long tDuration = Math.round(t.get(idx * 2 + 1));

		int color = Color.parseColor(attrs.getValue("color"));
		float colorArr[] = new float[3];
		colorArr[0] = Color.red(color) / 255f;
		colorArr[1] = Color.green(color) / 255f;
		colorArr[2] = Color.blue(color) / 255f;

		KittyBezier bezier = new KittyBezier(colorArr, tStart, tDuration);
		return bezier;
	}

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
	 * Returns layer with given id, or null if not found.
	 */
	public KittyLayer getLayer(String id) {
		for (KittyLayer layer : mLayers) {
			if (layer.mId.equals(id)) {
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
	private Vector<Float> readValues(String d) {
		Vector<Float> ret = new Vector<Float>();
		while (true) {
			int splitIdx = d.indexOf(',');
			int splitIdx2 = d.indexOf(' ');
			if (splitIdx == -1 || (splitIdx2 > 0 && splitIdx2 < splitIdx)) {
				splitIdx = splitIdx2;
			}
			if (splitIdx == -1) {
				break;
			}
			ret.add(Float.parseFloat(d.substring(0, splitIdx)));
			d = d.substring(splitIdx + 1).trim();
		}
		if (d.length() > 0) {
			ret.add(Float.parseFloat(d));
		}
		return ret;
	}

	@Override
	public void startElement(String name, AttributeList attrs) {
		// New layer.
		if (name.equals("layer")) {
			String id = attrs.getValue("id");
			Vector<Float> t = readValues(attrs.getValue("translate"));
			float s = Float.parseFloat(attrs.getValue("scale"));

			final Matrix transform = new Matrix();
			transform.setTranslate(t.get(0), t.get(1));
			transform.postScale(s, s);

			mLayerCurrent = new KittyLayer(id, transform);
			mLayers.add(mLayerCurrent);
		}
		// New fill element.
		if (name.equals("fill")) {

			Vector<Float> pts1 = readValues(attrs.getValue("pts1"));
			Vector<Float> pts2 = readValues(attrs.getValue("pts2"));

			int count = (Math.min(pts1.size(), pts2.size()) - 2) / 6;
			for (int i = 0; i < count; ++i) {
				KittyBezier bezier = allocBezier(attrs, i);
				bezier.mPts0 = new float[8];
				bezier.mPts1 = new float[8];

				for (int j = 0; j < 8; ++j) {
					int idx = i * 6 + j;
					bezier.mPts0[j] = pts1.get(idx);
					bezier.mPts1[j] = pts2.get(idx);
				}

				mLayerCurrent.add(bezier);
			}
		}
		// New line element.
		if (name.equals("line")) {

			Vector<Float> scale = readValues(attrs.getValue("scale"));
			Vector<Float> pts = readValues(attrs.getValue("pts"));

			int count = (pts.size() - 2) / 6;
			for (int i = 0; i < count; ++i) {
				KittyBezier bezier = allocBezier(attrs, i);

				float[] pts0 = bezier.mPts0 = new float[8];
				float[] pts1 = bezier.mPts1 = new float[8];

				for (int j = 0; j < 8; ++j) {
					int idx = i * 6 + j;
					pts0[j] = pts.get(idx);
					pts1[j] = pts.get(idx);
				}

				float[] normal0 = calculateNormal(pts0, 0);
				float[] normal1 = calculateNormal(pts0, 1);

				int scaleIdx = i * 4;
				for (int j = 0; j < 4; ++j) {
					float diff = normal0[j % 2] * scale.get(scaleIdx + 0) / 2;
					pts0[j] -= diff;
					pts1[j] += diff;

					diff = normal1[j % 2] * scale.get(scaleIdx + 3) / 2;
					pts0[j + 4] -= diff;
					pts1[j + 4] += diff;
				}
				for (int j = 0; j < 2; ++j) {
					pts1[j + 2] += (pts1[j + 2] - pts1[j + 0])
							* scale.get(scaleIdx + 1);
					pts1[j + 4] += (pts0[j + 4] - pts1[j + 6])
							* scale.get(scaleIdx + 2);
				}

				// If this isn't first bezier, connect control points at the end
				// of last one and first one of this one.
				if (i > 0) {
					KittyBezier prevBezier = mLayerCurrent.mBeziers
							.get(mLayerCurrent.mBeziers.size() - 1);
					pts0[0] = prevBezier.mPts0[6];
					pts0[1] = prevBezier.mPts0[7];
					pts1[0] = prevBezier.mPts1[6];
					pts1[1] = prevBezier.mPts1[7];
				}

				mLayerCurrent.add(bezier);
			}

		}
	}
}
