package com.hs.HighScore;

import com.f.Flippo.utils.DateUtil;
import com.hs.HighScore.model.HighScoreObject;
import com.hs.HighScore.model.helper.DbHighScoreHelper;

import java.util.ArrayList;
import java.util.Date;

import android.content.Context;
import android.util.Log;

public final class HighScore {
	private static final String TAG = "HighScore";

	/**
	 * Return ArrayList of daily high score list (Object).
	 */
	public static ArrayList<HighScoreObject> getDailyHighScoreList(Context context, final HighScoreObject object) {
		return DbHighScoreHelper.getDateRangeList(context, object, object);
	}

	/**
	 * Return ArrayList of weekly high score list (Object).
	 */
	public static ArrayList<HighScoreObject> getWeeklyHighScoreList(Context context, final HighScoreObject object) {
		HighScoreObject toObj = new HighScoreObject();
		toObj.setDate(object.getDate());
		HighScoreObject fromObj = new HighScoreObject();
		fromObj.setDate(object.getDate());

		Date date = DateUtil.stringToDate(fromObj.getDate(), DateUtil.ISO_DATE_FORMAT);
		date = DateUtil.addDays(date, -6);
		String fromDate = DateUtil.dateFormat(date, DateUtil.ISO_DATE_FORMAT);
		fromObj.setDate(fromDate);

		ArrayList<HighScoreObject> al = DbHighScoreHelper.getDateRangeList(context, fromObj, toObj);
		cleanupList(al);
		return al;
	}

	/**
	 * Return ArrayList of all time high score list (Object).
	 */
	public static ArrayList<HighScoreObject> getAllTimeHighScoreList(Context context) {
		ArrayList<HighScoreObject> al = DbHighScoreHelper.getList(context);
		cleanupList(al);
		return al;
	}

	/**
	 * Cleanup high scores list.
	 */
	private static void cleanupList(ArrayList<HighScoreObject> al) {
		for (int i = 0; i < al.size(); i++) {
			HighScoreObject iObj = al.get(i);
			if (iObj.getName() == null) {
				continue;
			}

			for (int j = 0; j < al.size(); j++) {
				HighScoreObject jObj = al.get(j);
				if (jObj.getName() == null || jObj == iObj) {
					continue;
				}

				if (jObj.getName().equals(iObj.getName())) {
					if (jObj.getScore() > iObj.getScore()) {
						jObj.setName(null);
					} else if (jObj.getDate().compareTo(iObj.getDate()) > 0) {
						jObj.setName(null);
					}
				}
			}
		}

		for (int i = al.size() - 1; i >= 0; i--) {
			HighScoreObject obj = al.get(i);
			if (obj.getName() == null) {
				al.remove(i);
			}
		}
	}

////////////////////////////////////////////////////////////////////////////////

	/**
	 * Return true, if object is a new daily high score.
	 */
	public static boolean checkDailyHighScore(Context context, final HighScoreObject object) {
		ArrayList<HighScoreObject> al = DbHighScoreHelper.getNameDateList(context, object);

		for (HighScoreObject obj : al) {
			if (obj.getScore() < object.getScore()) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Return true, if object is a all time new high score.
	 */
	public static boolean checkAllTimeHighScore(Context context, final HighScoreObject object) {
		ArrayList<HighScoreObject> al = DbHighScoreHelper.getList(context);

		for (HighScoreObject obj : al) {
			if (obj.getKey().equals(object.getKey())) {
				if (obj.getScore() < object.getScore()) {
					return true;
				}
			}
		}

		return false;
	}

////////////////////////////////////////////////////////////////////////////////

	/**
	 * Copy HighScoreObject.
	 */
	private static HighScoreObject copyObject(final HighScoreObject obj) {
		HighScoreObject object = new HighScoreObject();

		object.setName(obj.getName());
		object.setDate(obj.getDate());
		object.setScore(obj.getScore());

		return object;
	}

	/**
	 * Update daily high score.
	 */
	public static boolean updateDailyHighScore(Context context, final HighScoreObject obj) {
		ArrayList<HighScoreObject> al = DbHighScoreHelper.getNameDateList(context, obj);

		if (al.isEmpty()) {
			Log.i(TAG, "inserting new high score");
			return DbHighScoreHelper.insert(context, obj);
		} else {
			if (obj.getScore() < al.get(0).getScore()) {
				Log.i(TAG, "updating new high score - " + obj.getScore() + " : " + al.get(0).getScore());
				return DbHighScoreHelper.updateByNameDate(context, obj);
			} else {
				Log.i(TAG, "no update");
			}
		}

		return false;
	}

////////////////////////////////////////////////////////////////////////////////

	/**
	 * Housekeep high score DB.
	 */
	public static void housekeepHighScore(Context context) {
		String dateOneWeekAgo = DateUtil.dateFormat(DateUtil.addDays(new Date(), -7), DateUtil.ISO_DATE_FORMAT);
		ArrayList<HighScoreObject> toDeleteList = new ArrayList<HighScoreObject>();
		toDeleteList.clear();

		// Get all high scores order by (score, name, date).
		ArrayList<HighScoreObject> al = DbHighScoreHelper.getList(context);

		// ordering is not important.
		for (int i = 0; i < al.size(); i++) {
			HighScoreObject iObj = al.get(i);
			if (iObj.getName() == null) {
				continue;
			}

			for (int j = 0; j < al.size(); j++) {
				HighScoreObject jObj = al.get(j);
				if (jObj.getName() == null || jObj == iObj) {
					continue;
				}
				if (!jObj.getName().equals(iObj.getName())) {
					continue;
				}
				if (jObj.getScore() < iObj.getScore()) {
					continue;
				}
				if (jObj.getDate().compareTo(dateOneWeekAgo) > 0) {
					continue;
				}
				// if jDate before iDate
				if (jObj.getDate().compareTo(iObj.getDate()) < 0) {
					if (jObj.getScore() == iObj.getScore()) {
						continue;
					}
					toDeleteList.add(copyObject(jObj));
					jObj.setName(null);
				} else {
					toDeleteList.add(copyObject(iObj));
					iObj.setName(null);
					break;
				}
			}
		}

		for (HighScoreObject obj : toDeleteList) {
			DbHighScoreHelper.deleteByNameDate(context, obj);
		}
	}
}
