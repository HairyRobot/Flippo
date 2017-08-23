package com.f.Flippo;

import com.f.Flippo.utils.DateUtil;

import com.hs.HighScore.HighScore;
import com.hs.HighScore.HignScoreArrayAdapter;
import com.hs.HighScore.model.HighScoreObject;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TabHost.OnTabChangeListener;

public class HighScoreActivity extends Activity {

	private static final String TAG_TAB_ALL = "All";
	private static final String TAG_TAB_WEEKLY = "Weekly";
	private static final String TAG_TAB_DAILY = "Daily";

	private static ArrayList<HighScoreObject> mAllHighScore = new ArrayList<HighScoreObject>();
	private static ArrayList<HighScoreObject> mWeeklyHighScore = new ArrayList<HighScoreObject>();
	private static ArrayList<HighScoreObject> mDailyHighScore = new ArrayList<HighScoreObject>();

	private Activity mThisActivity;

	private ListView mAllHighScoreList;
	private ListView mWeeklyHighScoreList;
	private ListView mDailyHighScoreList;

	private TabHost.TabContentFactory mAllTabContent;
	private TabHost.TabContentFactory mWeeklyTabContent;
	private TabHost.TabContentFactory mDailyTabContent;

	/**
	 * Click listener for the mDailyHighScoreList ListView.
	 */
	private OnItemClickListener dailyHighScoreClickListener = new AdapterView.OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			HighScoreObject obj = mDailyHighScore.get(position);
			Flippo.setPlayBoard(obj.getName());
			finish();
		}
	};

	/**
	 * Click listener for the mWeeklyHighScoreList ListView.
	 */
	private OnItemClickListener weeklyHighScoreClickListener = new AdapterView.OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			HighScoreObject obj = mWeeklyHighScore.get(position);
			Flippo.setPlayBoard(obj.getName());
			finish();
		}
	};

	/**
	 * Click listener for the mAllHighScoreList ListView.
	 */
	private OnItemClickListener allHighScoreClickListener = new AdapterView.OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			HighScoreObject obj = mAllHighScore.get(position);
			Flippo.setPlayBoard(obj.getName());
			finish();
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mThisActivity = this;

		setContentView(R.layout.highscorelist);

		Button btnBack = (Button) findViewById(R.id.btnBack);
		btnBack.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});

		TabHost tabHost = (TabHost) findViewById(R.id.tabHost);
		tabHost.setup();
		tabHost.setOnTabChangedListener(new OnTabChangeListener() {
			// http://stackoverflow.com/questions/1062476/onclicklistener-on-tabs-not-working
			@Override
			public void onTabChanged(String tabId) {
				if (tabId.equals(TAG_TAB_DAILY)) {
					mThisActivity.setTitle(getString(R.string.app_name) +
							" (" + getTotalBoardSolvedWithinNineFlips(mDailyHighScore) +
							"/" + getTotalBoardSolved(mDailyHighScore) +
							")  Score: " +
							getTotalScore(mDailyHighScore)
					);
				} else if (tabId.equals(TAG_TAB_WEEKLY)) {
					mThisActivity.setTitle(getString(R.string.app_name) +
							" (" + getTotalBoardSolvedWithinNineFlips(mWeeklyHighScore) +
							"/" + getTotalBoardSolved(mWeeklyHighScore) +
							")  Score: " +
							getTotalScore(mWeeklyHighScore)
					);
				} else {
					mThisActivity.setTitle(getString(R.string.app_name) +
							" (" + getTotalBoardSolvedWithinNineFlips(mAllHighScore) +
							"/" + getTotalBoardSolved(mAllHighScore) +
							")  Score: " +
							getTotalScore(mAllHighScore)
					);

					if (getTotalBoardSolved(mAllHighScore) < 511) {
						Pref.setUnsolvedBoardExistsFlag(mThisActivity, true);
					}
				}
			}
		});

		TabHost.TabSpec spec;

		mDailyHighScoreList = new ListView(HighScoreActivity.this);
		mDailyHighScoreList.setFastScrollEnabled(false);
		mDailyHighScoreList.setOnItemClickListener(dailyHighScoreClickListener);

		mDailyTabContent = new TabHost.TabContentFactory() {
			public View createTabContent(String tag) {
				HignScoreArrayAdapter dailyHsArrayAdapter = new HignScoreArrayAdapter(HighScoreActivity.this,
						R.layout.highscore_item, mDailyHighScore);
				mDailyHighScoreList.setAdapter(dailyHsArrayAdapter);
				return mDailyHighScoreList;
			}
		};

		spec = tabHost.newTabSpec(TAG_TAB_DAILY);
		spec.setContent(R.id.dailyTab);
		spec.setIndicator(getString(R.string.tab_daily), getResources().getDrawable(R.drawable.ic_tab_bronze));
		spec.setContent(mDailyTabContent);
		tabHost.addTab(spec);

		mWeeklyHighScoreList = new ListView(HighScoreActivity.this);
		mWeeklyHighScoreList.setFastScrollEnabled(false);
		mWeeklyHighScoreList.setOnItemClickListener(weeklyHighScoreClickListener);

		mWeeklyTabContent = new TabHost.TabContentFactory() {
			public View createTabContent(String tag) {
				HignScoreArrayAdapter weeklyHsArrayAdapter = new HignScoreArrayAdapter(HighScoreActivity.this,
						R.layout.highscore_item, mWeeklyHighScore);
				mWeeklyHighScoreList.setAdapter(weeklyHsArrayAdapter);
				return mWeeklyHighScoreList;
			}
		};

		spec = tabHost.newTabSpec(TAG_TAB_WEEKLY);
		spec.setContent(R.id.weeklyTab);
		spec.setIndicator(getString(R.string.tab_weekly), getResources().getDrawable(R.drawable.ic_tab_silver));
		spec.setContent(mWeeklyTabContent);
		tabHost.addTab(spec);

		mAllHighScoreList = new ListView(HighScoreActivity.this);
		mAllHighScoreList.setFastScrollEnabled(true);
		mAllHighScoreList.setOnItemClickListener(allHighScoreClickListener);

		mAllTabContent = new TabHost.TabContentFactory() {
			public View createTabContent(String tag) {
				HignScoreArrayAdapter allHsArrayAdapter = new HignScoreArrayAdapter(HighScoreActivity.this,
						R.layout.highscore_item, mAllHighScore);
				mAllHighScoreList.setAdapter(allHsArrayAdapter);
				return mAllHighScoreList;
			}
		};

		spec = tabHost.newTabSpec(TAG_TAB_ALL);
		spec.setContent(R.id.allTab);
		spec.setIndicator(getString(R.string.tab_all), getResources().getDrawable(R.drawable.ic_tab_gold));
		spec.setContent(mAllTabContent);
		tabHost.addTab(spec);

		buildAllList();
		buildWeeklyList();
		buildDailyList();

		if (mDailyHighScore.isEmpty()) {
			if (mWeeklyHighScore.isEmpty()) {
				tabHost.setCurrentTabByTag(TAG_TAB_ALL);
			} else {
				tabHost.setCurrentTabByTag(TAG_TAB_WEEKLY);
			}
		} else {
			tabHost.setCurrentTabByTag(TAG_TAB_DAILY);
		}
	}

	public void onStart() {
		super.onStart();

		if (mAllHighScore.isEmpty())
			buildAllList();
		if (mWeeklyHighScore.isEmpty())
			buildWeeklyList();
		if (mDailyHighScore.isEmpty())
			buildDailyList();
	}

	/**
	 * Build all time high score list from DB.
	 */
	private void buildAllList() {
		ArrayList<HighScoreObject> objects = HighScore.getAllTimeHighScoreList(this);

		mAllHighScore.clear();
		mAllHighScore.addAll(objects);
		mAllTabContent.createTabContent(TAG_TAB_ALL);
	}

	/**
	 * Build weekly high score list from DB.
	 */
	private void buildWeeklyList() {
		HighScoreObject obj = new HighScoreObject();
		obj.setDate(DateUtil.today(DateUtil.ISO_DATE_FORMAT));
		ArrayList<HighScoreObject> objects = HighScore.getWeeklyHighScoreList(this, obj);

		mWeeklyHighScore.clear();
		mWeeklyHighScore.addAll(objects);
		mWeeklyTabContent.createTabContent(TAG_TAB_WEEKLY);
	}

	/**
	 * Build daily high score list from DB.
	 */
	private void buildDailyList() {
		HighScoreObject obj = new HighScoreObject();
		obj.setDate(DateUtil.today(DateUtil.ISO_DATE_FORMAT));
		ArrayList<HighScoreObject> objects = HighScore.getDailyHighScoreList(this, obj);

		mDailyHighScore.clear();
		mDailyHighScore.addAll(objects);
		mDailyTabContent.createTabContent(TAG_TAB_DAILY);
	}

	private int getTotalBoardSolved(ArrayList<HighScoreObject> alObj) {
		return alObj.size();
	}

	private int getTotalBoardSolvedWithinNineFlips(ArrayList<HighScoreObject> alObj) {
		int count = 0;

		for (HighScoreObject obj : alObj) {
			if (obj.getScore() <= 9)
				count++;
		}

		return count;
	}

	private int getTotalScore(ArrayList<HighScoreObject> alObj) {
		int count = 0;
		int score = 0;

		for (HighScoreObject obj : alObj) {
			if (obj.getScore() <= 9) {
				count++;
				score += 10 - obj.getScore();
			}
		}

		return (int) (score * 100f * ((float) (count) / alObj.size()));
	}
}
