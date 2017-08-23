package com.f.Flippo;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public final class Pref {

	private static final String PREF_ACCEPTED_DISCLAIMER = "acceptedDisclaimer";
	private static final boolean AD_DEFAULT = false;

	private static final String PREF_ANIMATION_ENABLED = "animationEnabled";
	private static final boolean AE_DEFAULT = true;
	private static final String PREF_SOUND_ENABLED = "soundEnabled";
	private static final boolean SE_DEFAULT = true;

	private static final String PREF_UNSOLVED_BOARD_EXISTS_FLAG = "unsolvedBoardExistsFlag";
	private static final boolean UBEF_DEFAULT = true;


	/**
	 * Get is disclaimer accepted flag
	 */
	static boolean getIsDisclaimerAccepted(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getBoolean(PREF_ACCEPTED_DISCLAIMER, AD_DEFAULT);
	}

	/**
	 * Set is disclaimer accepted flag
	 */
	static void setIsDisclaimerAccepted(Context context, boolean flag) {
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = pref.edit();
		editor.putBoolean(PREF_ACCEPTED_DISCLAIMER, flag);
		editor.commit();
	}

	/**
	 * Get animation enabled flag
	 */
	static boolean getAnimationEnabledFlag(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getBoolean(PREF_ANIMATION_ENABLED, AE_DEFAULT);
	}

	/**
	 * Get sound enabled flag
	 */
	static boolean getSoundEnabledFlag(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getBoolean(PREF_SOUND_ENABLED, SE_DEFAULT);
	}

	/**
	 * Get unsolved board exists flag
	 */
	static boolean getUnsolvedBoardExistsFlag(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getBoolean(PREF_UNSOLVED_BOARD_EXISTS_FLAG, UBEF_DEFAULT);
	}

	/**
	 * Set unsolved board exists flag
	 */
	static void setUnsolvedBoardExistsFlag(Context context, boolean flag) {
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = pref.edit();
		editor.putBoolean(PREF_UNSOLVED_BOARD_EXISTS_FLAG, flag);
		editor.commit();
	}
}
