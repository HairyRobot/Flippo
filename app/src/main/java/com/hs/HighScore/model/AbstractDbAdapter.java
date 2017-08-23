package com.hs.HighScore.model;
// http://pheide.com/go/blog/computors/post/managing-multiple-tables-in-the-android-sdk

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public abstract class AbstractDbAdapter {
	protected static final String TAG = "AbstractDbAdapter";

	protected final Context mContext;

	protected DatabaseHelper mDbHelper;
	protected SQLiteDatabase mDb;

	/**
	 * Constructor - takes the context to allow the database to be opened/created
	 *
	 * @param context the Context within which to work
	 */
	public AbstractDbAdapter(Context context) {
		mContext = context;
	}

	/**
	 * Open or create the routes database.
	 *
	 * @return this
	 * @throws SQLException if the database could be neither opened or created
	 */
	public AbstractDbAdapter open() throws SQLException {
		if (mDbHelper == null) {
			mDbHelper = new DatabaseHelper(mContext);
		}
		mDb = mDbHelper.getWritableDatabase();
		return this;
	}

	/**
	 * closes the database
	 */
	public void close() {
		if (mDbHelper != null) {
			mDbHelper.close();
			mDbHelper = null;
		}
	}

	protected static class DatabaseHelper extends SQLiteOpenHelper {

		DatabaseHelper(Context context) {
			super(context, DbSchema.DATABASE_NAME, null, DbSchema.DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(DbSchema.HighScoreSchema.CREATE_TABLE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
					+ newVersion + ", which will destroy all old data");
//			db.execSQL(DbSchema.HighScoreSchema.DROP_TABLE);
			onCreate(db);
		}
	}
}
