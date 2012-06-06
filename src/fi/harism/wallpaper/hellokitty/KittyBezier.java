package fi.harism.wallpaper.hellokitty;

public class KittyBezier {
	public float[] mColor;
	public float[] mCtrlPts0;
	public float[] mCtrlPts1;
	public float mRotate, mScale;
	public long mTimeStart, mTimeDuration;
	public float[] mTranslate;

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
