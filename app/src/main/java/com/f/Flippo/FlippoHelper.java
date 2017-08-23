package com.f.Flippo;

import com.f.Flippo.utils.BitmapUtil;
import com.f.Flippo.utils.Util;

import java.util.Arrays;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public final class FlippoHelper {
	private static final String TAG = "FlippoHelper";

	private static final int[] POWER_OF_TWO = {1, 2, 4, 8, 16, 32, 64, 128, 256};

	/**
	 * Convert board String to ByteArray.
	 */
	public static boolean[] getArray(String board) {
		boolean[] arr = new boolean[board.length()];

		for (int i = 0; i < board.length(); i++) {
			String c = board.substring(i, i + 1);

			if (c.equals("1")) {
				arr[i] = true;
			} else {
				arr[i] = false;
			}
		}

		return arr;
	}

	/**
	 * Convert board ByteArray to String.
	 */
	public static String getString(boolean[] board) {
		StringBuffer str = new StringBuffer();

		for (int i = 0; i < 9; i++) {
			if (board[i]) {
				str.append('1');
			} else {
				str.append('0');
			}
		}

		return str.toString();
	}

	/**
	 * Generate a Bitmap in given dip from given board ByteArray.
	 */
	public static Bitmap generateBitmap(final Context context, boolean[] board, float dip) {
		Bitmap bm;
		Bitmap c;
		Bitmap s;
		Bitmap cs;

		c = getBitmap(context, board[0]);
		s = getBitmap(context, board[1]);
		cs = BitmapUtil.combineImages(c, s);
		c = cs;
		s = getBitmap(context, board[2]);
		cs = BitmapUtil.combineImages(c, s);
		Bitmap row1 = cs;

		c = getBitmap(context, board[3]);
		s = getBitmap(context, board[4]);
		cs = BitmapUtil.combineImages(c, s);
		c = cs;
		s = getBitmap(context, board[5]);
		cs = BitmapUtil.combineImages(c, s);
		Bitmap row2 = cs;

		c = getBitmap(context, board[6]);
		s = getBitmap(context, board[7]);
		cs = BitmapUtil.combineImages(c, s);
		c = cs;
		s = getBitmap(context, board[8]);
		cs = BitmapUtil.combineImages(c, s);
		Bitmap row3 = cs;

		c = row1;
		s = row2;
		cs = BitmapUtil.combineImagesBelow(c, s);
		c = cs;
		s = row3;
		cs = BitmapUtil.combineImagesBelow(c, s);

		c = BitmapUtil.getWhiteBitmap(cs);
		c = BitmapUtil.scaleBitmap(c, Util.getDisplayPixel(context, dip));
		s = BitmapUtil.scaleBitmap(cs, Util.getDisplayPixel(context, dip));
		cs = BitmapUtil.combineImagesOver(c, s);

		bm = BitmapUtil.scaleBitmap(cs, Util.getDisplayPixel(context, dip));

		c.recycle();
		s.recycle();
		cs.recycle();
		row1.recycle();
		row2.recycle();
		row3.recycle();

		return bm;
	}

	/**
	 * Return Bitmap of the corresponding flag.
	 */
	private static Bitmap getBitmap(final Context context, boolean flag) {
		Bitmap bm;

		if (flag) {
			bm = BitmapFactory.decodeResource(context.getResources(), R.drawable.t);
		} else {
			bm = BitmapFactory.decodeResource(context.getResources(), R.drawable.f);
		}

		return bm;
	}

	/**
	 * Convert board string to decimal.
	 */
	public static int stringToDecimal(String board) {
		int num = 0;
		int pos = 0;

		for (int i = board.length() - 1; i >= 0; i--) {
			String c = board.substring(i, i + 1);

			if (c.equals("1")) {
				num += POWER_OF_TWO[pos];
			}

			pos++;
		}

		return num;
	}

	/**
	 * Convert decimal to board string.
	 */
	public static String decimalToString(int number) {
		String board = "000000000";
		String str = Integer.toBinaryString(number);

		if (str.length() == 9) {
			board = str;
		} else {
			board = board.substring(0, 9 - str.length()) + str;
		}

		return board;
	}

	/**
	 * Get similar boards string to given board.
	 */
	public static String[] getRotatedBoardsString(final String board) {
		int[] b = getRotatedBoardsDecimal(board);
		String[] r = new String[4];

		r[0] = decimalToString(b[0]);
		r[1] = decimalToString(b[1]);
		r[2] = decimalToString(b[2]);
		r[3] = decimalToString(b[3]);

		return r;
	}

	/**
	 * Get similar boards decimal to given board.
	 */
	public static int[] getRotatedBoardsDecimal(final String board) {
		String tmpBoard = board;
		int[] r = new int[4];

		r[0] = stringToDecimal(tmpBoard);
		tmpBoard = rotate(tmpBoard);
		r[1] = stringToDecimal(tmpBoard);
		tmpBoard = rotate(tmpBoard);
		r[2] = stringToDecimal(tmpBoard);
		tmpBoard = rotate(tmpBoard);
		r[3] = stringToDecimal(tmpBoard);

		Arrays.sort(r);
		int tmp = r[0];
		r[0] = r[3];
		r[3] = tmp;
		tmp = r[1];
		r[1] = r[2];
		r[2] = tmp;

		return r;
	}

	/**
	 * Rotate board by 90 degree clockwise.
	 */
	public static String rotate(final String board) {
		boolean[] src = getArray(board);
		boolean[] des = new boolean[9];

		des[0] = src[2];
		des[1] = src[5];
		des[2] = src[8];

		des[3] = src[1];
		des[4] = src[4];
		des[5] = src[7];

		des[6] = src[0];
		des[7] = src[3];
		des[8] = src[6];

		return getString(des);
	}

	/**
	 * Write board to logcat.
	 */
	public static void writeBoardToLog(final boolean[] board) {
		String s = "";
		if (board[0]) s = s + "1";
		else s = s + " ";
		if (board[1]) s = s + "1";
		else s = s + " ";
		if (board[2]) s = s + "1";
		else s = s + " ";
		Log.i(TAG, "-- " + s + " --");
		s = "";
		if (board[3]) s = s + "1";
		else s = s + " ";
		if (board[4]) s = s + "1";
		else s = s + " ";
		if (board[5]) s = s + "1";
		else s = s + " ";
		Log.i(TAG, "-- " + s + " --");
		s = "";
		if (board[6]) s = s + "1";
		else s = s + " ";
		if (board[7]) s = s + "1";
		else s = s + " ";
		if (board[8]) s = s + "1";
		else s = s + " ";
		Log.i(TAG, "-- " + s + " --");
	}
}
