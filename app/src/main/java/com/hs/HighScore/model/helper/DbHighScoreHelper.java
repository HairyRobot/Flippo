package com.hs.HighScore.model.helper;

import com.hs.HighScore.model.DbSchema;
import com.hs.HighScore.model.HighScoreDbAdapter;
import com.hs.HighScore.model.HighScoreObject;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;

public final class DbHighScoreHelper {

	private DbHighScoreHelper() {
	}

////////////////////////////////////////////////////////////////////////////////

	/**
	 * Return total record count.
	 */
	public static int getCount(Context context) {
		HighScoreDbAdapter dbAdapter = new HighScoreDbAdapter(context);

		dbAdapter.open();
		int count = dbAdapter.getCount();
		dbAdapter.close();

		return count;
	}

////////////////////////////////////////////////////////////////////////////////

	/**
	 * Return ArrayList of high score list (Object).
	 */
	public static ArrayList<HighScoreObject> getList(Context context) {
		ArrayList<HighScoreObject> al = new ArrayList<HighScoreObject>();

		HighScoreDbAdapter dbAdapter = new HighScoreDbAdapter(context);

		// Open the database.
		dbAdapter.open();

		// Fetch records from DB.
		Cursor cursor = dbAdapter.fetchAll();

		al.clear();

		if (cursor.moveToFirst()) {
			do {
				HighScoreObject obj = new HighScoreObject();
				obj.setRowId(cursor.getLong(cursor.getColumnIndex(DbSchema.HighScoreSchema.COLUMN_ROWID)));
				obj.setImage(cursor.getBlob(cursor.getColumnIndex(DbSchema.HighScoreSchema.COLUMN_IMAGE)));
				obj.setName(cursor.getString(cursor.getColumnIndex(DbSchema.HighScoreSchema.COLUMN_NAME)));
				obj.setDate(cursor.getString(cursor.getColumnIndex(DbSchema.HighScoreSchema.COLUMN_DATE)));
				obj.setScore(cursor.getInt(cursor.getColumnIndex(DbSchema.HighScoreSchema.COLUMN_SCORE)));
				al.add(0, obj);
			} while (cursor.moveToNext());
		}

		cursor.close();
		dbAdapter.close();

		return al;
	}

	/**
	 * Return ArrayList of high score list by date range (Object).
	 */
	public static ArrayList<HighScoreObject> getDateRangeList(Context context, HighScoreObject fromObj, HighScoreObject toObj) {
		ArrayList<HighScoreObject> al = new ArrayList<HighScoreObject>();

		HighScoreDbAdapter dbAdapter = new HighScoreDbAdapter(context);

		// Open the database.
		dbAdapter.open();

		// Fetch records from DB.
		Cursor cursor = dbAdapter.fetchByDateRange(fromObj, toObj);

		al.clear();

		if (cursor.moveToFirst()) {
			do {
				HighScoreObject obj = new HighScoreObject();
				obj.setRowId(cursor.getLong(cursor.getColumnIndex(DbSchema.HighScoreSchema.COLUMN_ROWID)));
				obj.setImage(cursor.getBlob(cursor.getColumnIndex(DbSchema.HighScoreSchema.COLUMN_IMAGE)));
				obj.setName(cursor.getString(cursor.getColumnIndex(DbSchema.HighScoreSchema.COLUMN_NAME)));
				obj.setDate(cursor.getString(cursor.getColumnIndex(DbSchema.HighScoreSchema.COLUMN_DATE)));
				obj.setScore(cursor.getInt(cursor.getColumnIndex(DbSchema.HighScoreSchema.COLUMN_SCORE)));
				al.add(0, obj);
			} while (cursor.moveToNext());
		}

		cursor.close();
		dbAdapter.close();

		return al;
	}

	/**
	 * Return ArrayList of high score list by name and date (Object).
	 */
	public static ArrayList<HighScoreObject> getNameDateList(Context context, HighScoreObject object) {
		ArrayList<HighScoreObject> al = new ArrayList<HighScoreObject>();

		HighScoreDbAdapter dbAdapter = new HighScoreDbAdapter(context);

		// Open the database.
		dbAdapter.open();

		// Fetch records from DB.
		Cursor cursor = dbAdapter.fetchByNameDate(object);

		al.clear();

		if (cursor.moveToFirst()) {
			do {
				HighScoreObject obj = new HighScoreObject();
				obj.setRowId(cursor.getLong(cursor.getColumnIndex(DbSchema.HighScoreSchema.COLUMN_ROWID)));
				obj.setImage(cursor.getBlob(cursor.getColumnIndex(DbSchema.HighScoreSchema.COLUMN_IMAGE)));
				obj.setName(cursor.getString(cursor.getColumnIndex(DbSchema.HighScoreSchema.COLUMN_NAME)));
				obj.setDate(cursor.getString(cursor.getColumnIndex(DbSchema.HighScoreSchema.COLUMN_DATE)));
				obj.setScore(cursor.getInt(cursor.getColumnIndex(DbSchema.HighScoreSchema.COLUMN_SCORE)));
				al.add(0, obj);
			} while (cursor.moveToNext());
		}

		cursor.close();
		dbAdapter.close();

		return al;
	}

////////////////////////////////////////////////////////////////////////////////

	/**
	 * Insert record, with exist check.
	 */
	public static boolean insert(Context context, HighScoreObject object) {
		boolean status = false;
		HighScoreDbAdapter dbAdapter = new HighScoreDbAdapter(context);

		// Open the database.
		dbAdapter.open();

		// Fetch records from DB.
		Cursor cursor = dbAdapter.fetchByNameDate(object);

		// If not found, create new record.
		if (!cursor.moveToFirst()) {
			long rowId = dbAdapter.insert(object);
			if (rowId == -1)
				status = false;
			else
				status = true;
		}

		cursor.close();
		dbAdapter.close();
		return status;
	}

////////////////////////////////////////////////////////////////////////////////

	/**
	 * Deletes records.
	 */
	public static boolean deleteByNameDate(Context context, HighScoreObject object) {
		HighScoreDbAdapter dbAdapter = new HighScoreDbAdapter(context);

		// Open the database.
		dbAdapter.open();

		boolean status = dbAdapter.deleteByNameDate(object);

		dbAdapter.close();

		return status;
	}

////////////////////////////////////////////////////////////////////////////////

	/**
	 * Updates record by row id.
	 */
	public static boolean updateByRowId(Context context, long rowId, HighScoreObject object) {
		HighScoreDbAdapter dbAdapter = new HighScoreDbAdapter(context);

		// Open the database.
		dbAdapter.open();

		boolean status = dbAdapter.update(rowId, object);

		dbAdapter.close();

		return status;
	}

	/**
	 * Updates records by name and date.
	 */
	public static boolean updateByNameDate(Context context, HighScoreObject object) {
		HighScoreDbAdapter dbAdapter = new HighScoreDbAdapter(context);

		// Open the database.
		dbAdapter.open();

		boolean status = dbAdapter.updateByNameDate(object);

		dbAdapter.close();

		return status;
	}
}
