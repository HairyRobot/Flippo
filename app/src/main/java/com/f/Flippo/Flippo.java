package com.f.Flippo;

import com.f.Flippo.ui.FlippoView;
import com.f.Flippo.utils.BitmapUtil;
import com.f.Flippo.utils.DateUtil;
import com.hs.HighScore.HighScore;
import com.hs.HighScore.model.HighScoreObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnCancelListener;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.view.animation.Animation.AnimationListener;
import android.widget.TextView;

public class Flippo extends Activity {
	private static final String TAG = "Flippo";

	private static final int MENU_ITEM_PREFERENCES = Menu.FIRST;
	private static final int MENU_ITEM_HIGH_SCORE = Menu.FIRST + 1;
	private static final int MENU_ITEM_NEW_GAME = Menu.FIRST + 2;

	private static final int DIALOG_PUZZLE_SOLVED = 1;

	/**
	 * Contains the rules of the game: which buttons to toggle when each of the 9 buttons is clicked.
	 */
	private static final Map<String, int[]> mRules = new HashMap<String, int[]>();
	private static String mPlayBoard = "";
	private static ArrayList<String> mUnsolvedBoard = new ArrayList<String>();

	static {
		mRules.put("1", new int[]{1, 3, 4});
		mRules.put("2", new int[]{0, 2});
		mRules.put("3", new int[]{1, 4, 5});
		mRules.put("4", new int[]{0, 6});
		mRules.put("5", new int[]{1, 3, 5, 7});
		mRules.put("6", new int[]{2, 8});
		mRules.put("7", new int[]{3, 4, 7});
		mRules.put("8", new int[]{6, 8});
		mRules.put("9", new int[]{4, 5, 7});
	}

	/**
	 * tags to pick up the buttons from the View.
	 */
	private String[] mTags = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9"};
	private FlippoView[] mBoard = new FlippoView[9];
	private boolean[] mBoardStatus = new boolean[9];
	private boolean[] mBoardSaved = new boolean[9];
	private boolean mIsRunning = false;
	private int mFlipCount = 0;
	private int mTotalFlipCount = 0;
	private Bitmap mBitmap = null;

	private Random mRandom = new Random(System.currentTimeMillis());

	/**
	 * Set the board to play next.
	 */
	public static void setPlayBoard(String board) {
		mPlayBoard = board;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.main);

		View mainTable = findViewById(R.id.mainTable);
		for (int i = 0; i < 9; i++) {
			mBoard[i] = (FlippoView) mainTable.findViewWithTag(mTags[i]);
		}

		generateUnsolvedBoardList();
		newGame();
	}

	@Override
	protected void onResume() {
		super.onResume();
		this.setTitle(getString(R.string.app_name) + ": " + mTotalFlipCount);
	}

	@Override
	protected void onDestroy() {
		HighScore.housekeepHighScore(this);
		super.onDestroy();
	}

	@Override
	protected void onRestart() {
		if (mPlayBoard.length() > 0) {
			newGame();
		}
		super.onRestart();
	}

	/**
	 * Generate lists of unsolved boards.
	 */
	private void generateUnsolvedBoardList() {
		mUnsolvedBoard.clear();
		if (!Pref.getUnsolvedBoardExistsFlag(this)) return;

		for (int i = 0; i < 512; i++) {
			mUnsolvedBoard.add(FlippoHelper.decimalToString(i));
		}

		ArrayList<HighScoreObject> objects = HighScore.getAllTimeHighScoreList(this);

		for (HighScoreObject obj : objects) {
			mUnsolvedBoard.remove(obj.getName());
		}
		mUnsolvedBoard.remove("000010000");  // remove the winning board

		for (String str : mUnsolvedBoard) {
			Log.d(TAG, "-- mUnsolvedBoard: " + str);
		}
		Log.i(TAG, "-- mUnsolvedBoard: " + mUnsolvedBoard.size());

		if (mUnsolvedBoard.size() == 0) {
			Pref.setUnsolvedBoardExistsFlag(this, false);
		}
	}

	/**
	 * Create a new board to play.
	 */
	private void newGame() {
		mTotalFlipCount = 0;

		if (mPlayBoard.length() == 0) {
			if (!mUnsolvedBoard.isEmpty()) {
				if (mUnsolvedBoard.size() <= 361) {
					Random generator = new Random(System.currentTimeMillis());
					int i = generator.nextInt(mUnsolvedBoard.size());
					mPlayBoard = mUnsolvedBoard.get(i);
					mUnsolvedBoard.remove(mPlayBoard);
				}
			}
		}

		if (mPlayBoard.length() == 0) {
			generateNewBoard();
		} else {
			char[] board = mPlayBoard.toCharArray();

			for (int i = 0; i < board.length; i++) {
				if (board[i] == '1') {
					mBoard[i].setState(true);
				} else {
					mBoard[i].setState(false);
				}
				mBoard[i].invalidate();
				mBoardStatus[i] = mBoard[i].getState();
				mBoardSaved[i] = mBoard[i].getState();
			}

			mPlayBoard = "";
		}

//		boolean[] array = {false, true, false, true, false, true, false, true, false};
//		mBoardSaved = array;
//		replayGame();
		this.setTitle(getString(R.string.app_name) + ": " + mTotalFlipCount);
		FlippoHelper.writeBoardToLog(mBoardStatus);
		mBitmap = FlippoHelper.generateBitmap(this, mBoardSaved, 48);
		Log.i(TAG, "-- board=" + FlippoHelper.getString(mBoardSaved));
	}

	/**
	 * Generate a new board randomly.
	 */
	private void generateNewBoard() {
		for (int i = 0; i < 9; i++) {
			mBoard[i].setState(mRandom.nextBoolean());
			mBoard[i].invalidate();
			mBoardStatus[i] = mBoard[i].getState();
			mBoardSaved[i] = mBoard[i].getState();
		}

		if (checkWinning()) {
			generateNewBoard();
		}
	}

	/**
	 * Re-create a just played board for replay.
	 */
	private void replayGame() {
		mTotalFlipCount = 0;

		for (int i = 0; i < 9; i++) {
			boolean state = mBoardSaved[i];
			mBoard[i].setState(state);
			mBoard[i].invalidate();
			mBoardStatus[i] = mBoard[i].getState();
		}

		this.setTitle(getString(R.string.app_name) + ": " + mTotalFlipCount);
		FlippoHelper.writeBoardToLog(mBoardStatus);
		mBitmap = FlippoHelper.generateBitmap(this, mBoardSaved, 48);
		Log.i(TAG, "-- board=" + FlippoHelper.getString(mBoardSaved));
	}

	/**
	 * Return true, if winning board is reached.
	 */
	private boolean checkWinning() {
		boolean result = !mBoardStatus[0] && !mBoardStatus[1] && !mBoardStatus[2] &&
				!mBoardStatus[3] && mBoardStatus[4] && !mBoardStatus[5] &&
				!mBoardStatus[6] && !mBoardStatus[7] && !mBoardStatus[8];
		return result;
	}

	/**
	 * Click listener of FlippoView.
	 */
	public void clickHandler(final View view) {
		if (mIsRunning) return;

		mIsRunning = true;
		mFlipCount = 0;
		mTotalFlipCount++;
		this.setTitle(getString(R.string.app_name) + ": " + mTotalFlipCount);
		soundOnClick();

		String tag = (String) view.getTag();
		Log.i(TAG, "flipped tile " + tag);
		int idx = Integer.valueOf(tag) - 1;

		if (Pref.getAnimationEnabledFlag(this)) {
			mBoardStatus[idx] = !mBoardStatus[idx];
			flipView(tag);
			for (int i : mRules.get(tag)) {
				mBoardStatus[i] = !mBoardStatus[i];
				flipView("" + (i + 1));
			}
		} else {
			mBoardStatus[idx] = !mBoardStatus[idx];
			mBoard[idx].toggle();
			for (int i : mRules.get(tag)) {
				mBoardStatus[i] = !mBoardStatus[i];
				mBoard[i].toggle();
			}
			mIsRunning = false;
		}

		if (checkWinning()) {
			Log.i(TAG, "-- Puzzle solved in " + mTotalFlipCount + " flips");
			String boardString = FlippoHelper.getString(mBoardSaved);
			HighScoreObject obj = new HighScoreObject();
			obj.setName(boardString);
			obj.setDate(DateUtil.today(DateUtil.ISO_DATE_FORMAT));
			obj.setScore(mTotalFlipCount);
			obj.setImage(BitmapUtil.toByteArray(mBitmap));
			HighScore.updateDailyHighScore(this, obj);
			showDialog(DIALOG_PUZZLE_SOLVED);
		} else {
			Log.i(TAG, "-- Flip count: " + mTotalFlipCount);
		}

		FlippoHelper.writeBoardToLog(mBoardStatus);
	}

	/**
	 * Toggle the FlippoView with animation.
	 */
	private void flipView(String tag) {
		final FlippoView view = mBoard[Integer.valueOf(tag) - 1];
		Animation flip;
		mFlipCount++;

		if (view.getState()) {
			flip = new ScaleAnimation(1f, 0f, 1f, 1f,
					Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		} else {
			flip = new ScaleAnimation(1f, 1f, 1f, 0f,
					Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		}
		flip.setDuration(100);
		flip.setAnimationListener(new AnimationListener() {

			public void onAnimationStart(Animation ad) {
			}

			public void onAnimationRepeat(Animation ad) {
			}

			public void onAnimationEnd(Animation ad) {
				Animation flip;
				view.toggle();
				if (view.getState()) {
					flip = new ScaleAnimation(0f, 1f, 1f, 1f,
							Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
				} else {
					flip = new ScaleAnimation(1f, 1f, 0f, 1f,
							Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
				}
				flip.setDuration(350);
				view.startAnimation(flip);
				mFlipCount--;
				if (mFlipCount == 0)
					mIsRunning = false;
			}
		});
		view.startAnimation(flip);
	}

	private void soundOnClick() {
		if (!Pref.getSoundEnabledFlag(this)) return;

		// http://stackoverflow.com/questions/2753943/android-playing-sound-when-button-clicked
		// http://stackoverflow.com/questions/3369068/android-play-sound-on-button-click-null-pointer-exception
		new Thread() {
			public void run() {
				MediaPlayer mp = MediaPlayer.create(Flippo.this, R.raw.click);
				mp.start();
				mp.setOnCompletionListener(new OnCompletionListener() {
					@Override
					public void onCompletion(MediaPlayer mp) {
						mp.release();
					}
				});
			}
		}.start();
	}

	/**
	 * Start the high scores activity.
	 */
	private void highScores() {
		startActivity(new Intent(this, HighScoreActivity.class));
	}

	@Override
	protected void onPrepareDialog(int id, Dialog dialog) {
		// http://stackoverflow.com/questions/834139/android-can-not-change-the-text-appears-in-alertdialog
		// http://code.google.com/p/android/issues/detail?id=857

		switch (id) {
			case DIALOG_PUZZLE_SOLVED:
				removeDialog(id);
		}
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		// http://developer.android.com/guide/topics/ui/dialogs.html
		// http://www.androidsnippets.com/convert-bitmap-to-drawable
		LayoutInflater factory = LayoutInflater.from(this);

		switch (id) {
			case DIALOG_PUZZLE_SOLVED:
				final View puzzleSolvedView = factory.inflate(R.layout.onelinedialog, null);
				TextView puzzleSolvedTextView = (TextView) puzzleSolvedView.findViewById(R.id.one_line_dialog_id);
				if (mTotalFlipCount > 9) {
					puzzleSolvedTextView.setText(getString(R.string.puzzle_solved_9_text, "" + mTotalFlipCount));
				} else {
					puzzleSolvedTextView.setText(getString(R.string.puzzle_solved_text, "" + mTotalFlipCount));
				}
				return new AlertDialog.Builder(this)
//						.setIcon(R.drawable.icon)
						.setIcon(new BitmapDrawable(mBitmap))
						.setTitle(R.string.puzzle_solved_title)
						.setView(puzzleSolvedView)
						.setPositiveButton(R.string.btn_replay_game,
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int id) {
										replayGame();
									}
								}
						)
						.setNeutralButton(R.string.btn_new_game,
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int id) {
										newGame();
									}
								}
						)
						.setNegativeButton(R.string.btn_high_score,
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int id) {
										highScores();
										newGame();
									}
								}
						)
						// Capture the BACK key as cancel.
						.setOnCancelListener(new OnCancelListener() {
							@Override
							public void onCancel(DialogInterface dialog) {
								newGame();
							}
						})
						.create();
		}

		return null;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		populateMenu(menu);
		return super.onCreateOptionsMenu(menu);
	}

	/* Handles Menu item selections */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case MENU_ITEM_PREFERENCES:
				startActivity(new Intent(this, PrefActivity.class));
				return true;
			case MENU_ITEM_HIGH_SCORE:
				highScores();
				return true;
			case MENU_ITEM_NEW_GAME:
				newGame();
				return true;
		}

		return super.onOptionsItemSelected(item);
	}

	private void populateMenu(Menu menu) {
		menu.add(Menu.NONE, MENU_ITEM_NEW_GAME, Menu.NONE, getString(R.string.menu_new_game))
				.setIcon(R.drawable.ic_menu_new_game);
		menu.add(Menu.NONE, MENU_ITEM_HIGH_SCORE, Menu.NONE, getString(R.string.menu_high_scores))
				.setIcon(R.drawable.ic_menu_high_scores);
		menu.add(Menu.NONE, MENU_ITEM_PREFERENCES, Menu.NONE, getString(R.string.menu_preferences))
				.setIcon(R.drawable.ic_menu_preferences);
	}
}
