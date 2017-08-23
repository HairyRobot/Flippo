package com.hs.HighScore.model;

public class HighScoreObject {
	private long mRowId = 0L;
	private String mKey = "";
	private String mName = "";
	private String mDate = "";
	private int mScore = 0;
	private byte[] mImage = null;

//////////////////////////////////////////////////////////////////////

	public long getRowId() {
		return mRowId;
	}

	public void setRowId(long rowId) {
		this.mRowId = rowId;
	}

//////////////////////////////////////////////////////////////////////

	public String getName() {
		return mName;
	}

	public void setName(String name) {
		this.mName = name;
		setKey();
	}

	public String getDate() {
		return mDate;
	}

	public void setDate(String date) {
		this.mDate = date;
		setKey();
	}

	public int getScore() {
		return mScore;
	}

	public void setScore(int score) {
		this.mScore = score;
	}

	public byte[] getImage() {
		return mImage;
	}

	public void setImage(byte[] image) {
		this.mImage = image;
	}

//////////////////////////////////////////////////////////////////////

	/**
	 * name + date
	 */
	public String getKey() {
		return mKey;
	}

	private void setKey() {
		String kName = (null == mName ? "" : mName);
		String kDate = (null == mDate ? "" : mDate);

		mKey = kName + kDate;
	}

//////////////////////////////////////////////////////////////////////////////////////

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 31 * hash + (null == mName ? 0 : mName.hashCode());
		hash = 31 * hash + (null == mDate ? 0 : mDate.hashCode());
		hash = 31 * hash + mScore;
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this)
			return true;
		if (obj == null)
			return false;
		if (obj.getClass() != this.getClass())
			return false;

		final HighScoreObject other = (HighScoreObject) obj;
		return (this.mName == other.mName || (this.mName != null && this.mName.equals(other.mName)) &&
				this.mDate == other.mDate || (this.mDate != null && this.mDate.equals(other.mDate)) &&
				this.mScore == other.mScore
		);
	}

	@Override
	public String toString() {
//		String dataString = "rowId=" + mRowId
		String dataString = "rowId=" + 0
				+ "  name=" + mName
				+ "  date=" + mDate
				+ "  score=" + mScore
//				+ "\n---------------------------------------------------------"
				;
		return dataString;
	}
}
