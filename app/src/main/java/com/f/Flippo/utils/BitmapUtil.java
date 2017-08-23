package com.f.Flippo.utils;

import java.io.ByteArrayOutputStream;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;

public final class BitmapUtil {

	private BitmapUtil() {
	}

	/**
	 * Convert Bitmap to ByteArray.
	 */
	public static byte[] toByteArray(Bitmap bitmap) {
		// http://androidcommunity.com/forums/f4/png-jpg-file-to-byte-array-and-vice-verca-22108/
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
		byte[] byteArray = baos.toByteArray();
		return byteArray;
	}

	/**
	 * Convert ByteArray to Bitmap.
	 */
	public static Bitmap toBitmap(byte[] byteArray) {
		// http://www.tutorialforandroid.com/2009/10/how-to-insert-image-data-to-sqlite.html
		Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
		return bitmap;
	}

	/**
	 * Combine two images side by side.
	 */
	public static Bitmap combineImages(Bitmap c, Bitmap s) { // can add a 3rd parameter 'String loc' if you want to save the new image - left some code to do that at the bottom
		// http://stackoverflow.com/questions/4863518/combining-two-bitmap-image-side-by-side
		Bitmap cs;

		int width, height;

		if (c.getWidth() > s.getWidth()) {
			width = c.getWidth() + s.getWidth();
			height = c.getHeight();
		} else {
			width = s.getWidth() + s.getWidth();
			height = c.getHeight();
		}

		cs = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

		Canvas comboImage = new Canvas(cs);

		comboImage.drawBitmap(c, 0f, 0f, null);
		comboImage.drawBitmap(s, c.getWidth(), 0f, null);

		// this is an extra bit I added, just incase you want to save the new
		// image somewhere and then return the location
		/*
		 * String tmpImg = String.valueOf(System.currentTimeMillis()) + ".png";
		 *
		 * OutputStream os = null; try { os = new FileOutputStream(loc +
		 * tmpImg); cs.compress(CompressFormat.PNG, 100, os); }
		 * catch(IOException e) { Log.e("combineImages",
		 * "problem combining images", e); }
		 */

		return cs;
	}

	/**
	 * Combine two images top to bottom.
	 */
	public static Bitmap combineImagesBelow(Bitmap c, Bitmap s) { // can add a 3rd parameter 'String loc' if you want to save the new image - left some code to do that at the bottom
		// http://stackoverflow.com/questions/4863518/combining-two-bitmap-image-side-by-side
		Bitmap cs;

		int width, height;

		if (c.getHeight() > s.getHeight()) {
			width = c.getWidth();
			height = c.getHeight() + s.getHeight();
		} else {
			width = c.getWidth();
			height = s.getHeight() + s.getHeight();
		}

		cs = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

		Canvas comboImage = new Canvas(cs);

		comboImage.drawBitmap(c, 0f, 0f, null);
		comboImage.drawBitmap(s, 0f, c.getHeight(), null);

		// this is an extra bit I added, just incase you want to save the new
		// image somewhere and then return the location
		/*
		 * String tmpImg = String.valueOf(System.currentTimeMillis()) + ".png";
		 * 
		 * OutputStream os = null; try { os = new FileOutputStream(loc +
		 * tmpImg); cs.compress(CompressFormat.PNG, 100, os); }
		 * catch(IOException e) { Log.e("combineImages",
		 * "problem combining images", e); }
		 */

		return cs;
	}

	/**
	 * Overlay an image on top of another.
	 */
	public static Bitmap combineImagesOver(Bitmap bmp1, Bitmap bmp2) {
		// http://stackoverflow.com/questions/1540272/android-how-to-overlay-a-bitmap-draw-over-a-bitmap
		Bitmap bmOverlay = Bitmap.createBitmap(bmp1.getWidth(), bmp1.getHeight(), bmp1.getConfig());
		Canvas canvas = new Canvas(bmOverlay);
		canvas.drawBitmap(bmp1, new Matrix(), null);
		canvas.drawBitmap(bmp2, new Matrix(), null);
		return bmOverlay;
	}

	/**
	 * Convert Bitmap to white.
	 */
	public static Bitmap getWhiteBitmap(Bitmap bitmap) {
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
		output.eraseColor(Color.WHITE);
		return output;
	}

	/**
	 * Scale a Bitmap to given new width.
	 */
	public static Bitmap scaleBitmap(Bitmap scaled, int newWidth) {
		// http://zerocredibility.wordpress.com/2011/01/27/android-bitmap-scaling/
		int width = scaled.getWidth();
		int height = scaled.getHeight();

		float scaleWidth = ((float) newWidth) / width;
		float ratio = ((float) scaled.getWidth()) / newWidth;
		int newHeight = (int) (height / ratio);
		float scaleHeight = ((float) newHeight) / height;

		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);

		return Bitmap.createBitmap(scaled, 0, 0, width, height, matrix, true);
	}
}
