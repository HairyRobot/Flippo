package com.hs.HighScore.model;

import android.provider.BaseColumns;

public final class DbSchema {
	public static final int DATABASE_VERSION = 1;
	public static final String DATABASE_NAME = "scores.db";
	public static final String SORT_ASC = " DESC";
	public static final String SORT_DESC = " ASC";
//	public static final String[] ORDERS = {SORT_ASC,SORT_DESC};

	public static final class HighScoreSchema implements BaseColumns {
		public static final String TABLE_NAME = "highscores";
		public static final String COLUMN_ROWID = _ID;
		public static final String COLUMN_IMAGE = "board";
		public static final String COLUMN_NAME = "name";
		public static final String COLUMN_DATE = "date";
		public static final String COLUMN_SCORE = "score";
		public static final String[] ALL_COLUMNS = new String[]{
				_ID,
				COLUMN_IMAGE,
				COLUMN_NAME,
				COLUMN_DATE,
				COLUMN_SCORE
		};
		public static final String CREATE_TABLE = "create table if not exists "
				+ TABLE_NAME + " ("
				+ _ID + " integer primary key autoincrement,"
				+ COLUMN_IMAGE + " blob,"
				+ COLUMN_NAME + " text not null,"
				+ COLUMN_DATE + " text not null,"
				+ COLUMN_SCORE + " integer default 0);";
		public static final String DROP_TABLE = "drop table if exists " + TABLE_NAME;
	}
}
