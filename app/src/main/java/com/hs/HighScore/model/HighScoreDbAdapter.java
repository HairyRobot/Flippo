package com.hs.HighScore.model;

import com.hs.HighScore.model.DbSchema.HighScoreSchema;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;

public class HighScoreDbAdapter extends AbstractDbAdapter {

	public HighScoreDbAdapter(Context context) {
		super(context);
	}

////////////////////////////////////////////////////////////////////////////////

	/**
	 * get total records count
	 */
	public int getCount() {
		// http://stackoverflow.com/questions/3094257/android-sqlite-cursor-out-of-bounds-exception-on-select-count-from-table
		// http://www.samcoles.co.uk/mobile/android-get-number-of-rows-in-sqlite-database-table/
		int count = 0;

		Cursor cursor = mDb.rawQuery("select count(*) from " + DbSchema.HighScoreSchema.TABLE_NAME,  // sql
				null  // selectionArgs[]
		);
		if (cursor != null) {
			cursor.moveToFirst();
			count = cursor.getInt(0);
			cursor.close();
		}
		return count;
	}

////////////////////////////////////////////////////////////////////////////////

	/**
	 * insert a record into the database
	 */
	public long insert(HighScoreObject obj) {
		ContentValues cv = new ContentValues();
		cv.put(DbSchema.HighScoreSchema.COLUMN_IMAGE, obj.getImage());
		cv.put(DbSchema.HighScoreSchema.COLUMN_NAME, obj.getName());
		cv.put(DbSchema.HighScoreSchema.COLUMN_DATE, obj.getDate());
		cv.put(DbSchema.HighScoreSchema.COLUMN_SCORE, obj.getScore());
		return mDb.insert(DbSchema.HighScoreSchema.TABLE_NAME, null, cv);
	}

////////////////////////////////////////////////////////////////////////////////

	/**
	 * deletes a record by row id
	 */
	public boolean delete(long rowId) {
		return mDb.delete(DbSchema.HighScoreSchema.TABLE_NAME,
				DbSchema.HighScoreSchema._ID + "=" + rowId, null) > 0;
	}

	/**
	 * deletes records by name and date
	 */
	public boolean deleteByNameDate(HighScoreObject obj) {
		return mDb.delete(DbSchema.HighScoreSchema.TABLE_NAME,  // table
				DbSchema.HighScoreSchema.COLUMN_NAME + "=? and " +
						DbSchema.HighScoreSchema.COLUMN_DATE + "=?",  // whereClause
				new String[]{obj.getName(), obj.getDate()}  // whereArgs[]
		) > 0;
	}

////////////////////////////////////////////////////////////////////////////////

	/**
	 * retrieves all records
	 */
	public Cursor fetchAll() {
		return mDb.query(DbSchema.HighScoreSchema.TABLE_NAME,  // table
				DbSchema.HighScoreSchema.ALL_COLUMNS,  // columns[]
				null,  // selection
				null,  // selectionArgs[]
				null,  // groupBy
				null,  // having
				HighScoreSchema.COLUMN_SCORE + DbSchema.SORT_ASC + "," +
						HighScoreSchema.COLUMN_NAME + DbSchema.SORT_ASC + "," +
						HighScoreSchema.COLUMN_DATE + DbSchema.SORT_ASC  // orderBy
		);
	}

	/**
	 * retrieves a record by row id
	 */
	public Cursor fetch(long rowId) throws SQLException {
		Cursor cursor = mDb.query(true,  // distinct
				DbSchema.HighScoreSchema.TABLE_NAME,  // table
				DbSchema.HighScoreSchema.ALL_COLUMNS,  // columns[]
				DbSchema.HighScoreSchema._ID + "=" + rowId,  // selection
				null,  // selectionArgs[]
				null,  // groupBy
				null,  // having
				null,  // orderBy
				null  // limit
		);
		if (cursor != null) {
			cursor.moveToFirst();
		}
		return cursor;
	}

	/**
	 * retrieves records by name
	 */
	public Cursor fetchByName(HighScoreObject obj) throws SQLException {
		Cursor cursor = mDb.query(true,  // distinct
				DbSchema.HighScoreSchema.TABLE_NAME,  // table
				DbSchema.HighScoreSchema.ALL_COLUMNS,  // columns[]
				DbSchema.HighScoreSchema.COLUMN_NAME + "=?",  // selection
				new String[]{obj.getName()},  // selectionArgs[]
				null,  // groupBy
				null,  // having
				HighScoreSchema.COLUMN_SCORE + DbSchema.SORT_ASC + "," +
						HighScoreSchema.COLUMN_DATE + DbSchema.SORT_ASC,  // orderBy
				null  // limit
		);
		if (cursor != null) {
			cursor.moveToFirst();
		}
		return cursor;
	}

	/**
	 * retrieves records by name and date
	 */
	public Cursor fetchByNameDate(HighScoreObject obj) throws SQLException {
		Cursor cursor = mDb.query(true,  // distinct
				DbSchema.HighScoreSchema.TABLE_NAME,  // table
				DbSchema.HighScoreSchema.ALL_COLUMNS,  // columns[]
				DbSchema.HighScoreSchema.COLUMN_NAME + "=? and " +
						DbSchema.HighScoreSchema.COLUMN_DATE + "=?",  // selection
				new String[]{obj.getName(), obj.getDate()},  // selectionArgs[]
				null,  // groupBy
				null,  // having
				HighScoreSchema.COLUMN_SCORE + DbSchema.SORT_ASC + "," +
						HighScoreSchema.COLUMN_DATE + DbSchema.SORT_ASC,  // orderBy
				null  // limit
		);
		if (cursor != null) {
			cursor.moveToFirst();
		}
		return cursor;
	}

	/**
	 * retrieves records by date range (inclusive)
	 */
	public Cursor fetchByDateRange(HighScoreObject fromObj, HighScoreObject toObj) throws SQLException {
		Cursor cursor = mDb.query(true,  // distinct
				DbSchema.HighScoreSchema.TABLE_NAME,  // table
				DbSchema.HighScoreSchema.ALL_COLUMNS,  // columns[]
				DbSchema.HighScoreSchema.COLUMN_DATE + ">=? and " +
						DbSchema.HighScoreSchema.COLUMN_DATE + "<=?",  // selection
				new String[]{fromObj.getDate(), toObj.getDate()},  // selectionArgs[]
				null,  // groupBy
				null,  // having
				HighScoreSchema.COLUMN_SCORE + DbSchema.SORT_ASC + "," +
						HighScoreSchema.COLUMN_NAME + DbSchema.SORT_ASC + "," +
						HighScoreSchema.COLUMN_DATE + DbSchema.SORT_ASC,  // orderBy
				null  // limit
		);
		if (cursor != null) {
			cursor.moveToFirst();
		}
		return cursor;
	}

////////////////////////////////////////////////////////////////////////////////

	/**
	 * updates record by row id
	 */
	public boolean update(long rowId, HighScoreObject obj) {
		ContentValues cv = new ContentValues();
		cv.put(DbSchema.HighScoreSchema.COLUMN_IMAGE, obj.getImage());
		cv.put(DbSchema.HighScoreSchema.COLUMN_NAME, obj.getName());
		cv.put(DbSchema.HighScoreSchema.COLUMN_DATE, obj.getDate());
		cv.put(DbSchema.HighScoreSchema.COLUMN_SCORE, obj.getScore());
		return mDb.update(DbSchema.HighScoreSchema.TABLE_NAME,  // table
				cv,  // values
				DbSchema.HighScoreSchema._ID + "=" + rowId,  // whereClause
				null  // whereArgs[]
		) > 0;
	}

	/**
	 * updates records by name and date
	 */
	public boolean updateByNameDate(HighScoreObject obj) {
		ContentValues cv = new ContentValues();
//		cv.put(DbSchema.HighScoreSchema.COLUMN_IMAGE, obj.getImage());
//		cv.put(DbSchema.HighScoreSchema.COLUMN_NAME, obj.getName());
		cv.put(DbSchema.HighScoreSchema.COLUMN_DATE, obj.getDate());
		cv.put(DbSchema.HighScoreSchema.COLUMN_SCORE, obj.getScore());
		return mDb.update(DbSchema.HighScoreSchema.TABLE_NAME,  // table
				cv,  // values
				DbSchema.HighScoreSchema.COLUMN_NAME + "=? and " +
						DbSchema.HighScoreSchema.COLUMN_DATE + "=?",  // whereClause
				new String[]{obj.getName(), obj.getDate()}  // whereArgs[]
		) > 0;
	}
}
