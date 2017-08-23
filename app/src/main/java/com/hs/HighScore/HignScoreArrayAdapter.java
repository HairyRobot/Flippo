package com.hs.HighScore;

import com.f.Flippo.R;
import com.f.Flippo.utils.BitmapUtil;
import com.hs.HighScore.model.HighScoreObject;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class HignScoreArrayAdapter extends ArrayAdapter<HighScoreObject> {

	private Context mContext;
	private int mId;
	private ArrayList<HighScoreObject> mItems;

	public HignScoreArrayAdapter(Context context, int textViewResourceId, ArrayList<HighScoreObject> objects) {
		super(context, textViewResourceId, objects);
		mContext = context;
		mId = textViewResourceId;
		mItems = objects;
	}

	public HighScoreObject getItem(int i) {
		return mItems.get(i);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;

		if (v == null) {
			LayoutInflater vi = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(mId, null);
		}

		final HighScoreObject obj = getItem(position);

		if (obj != null) {
			ImageView iv = (ImageView) v.findViewById(R.id.hs_item_image);
			TextView sv = (TextView) v.findViewById(R.id.hs_item_score);
			TextView dv = (TextView) v.findViewById(R.id.hs_item_date);

			if (obj.getImage() != null) {
				iv.setImageBitmap(BitmapUtil.toBitmap(obj.getImage()));
			}
			if (obj.getScore() == 1) {
				sv.setText("" + obj.getScore() + " flip");
			} else {
				sv.setText("" + obj.getScore() + " flips");
			}
			dv.setText(obj.getDate());
		}

		return v;
	}
}
