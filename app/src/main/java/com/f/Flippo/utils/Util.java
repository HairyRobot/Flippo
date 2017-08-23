package com.f.Flippo.utils;

import android.content.Context;
import android.util.TypedValue;

public final class Util {

	private Util() {
	}

	/**
	 * Returns the number of pixels for given dip value in relation to display density (160dpi screen, the density value is 1.0).
	 */
	public static int getDisplayPixel(Context context, float dip) {
		// http://stackoverflow.com/questions/2238883/what-is-the-correct-way-to-specify-dimensions-in-dip-from-java-code
		// http://developer.android.com/reference/android/util/TypedValue.html
		int pixel;

		try {
			pixel = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip,
					context.getResources().getDisplayMetrics());
		} catch (Exception e) {
			pixel = (int) dip;
		}

		return pixel;
	}
}
