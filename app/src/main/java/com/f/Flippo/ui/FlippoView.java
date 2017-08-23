package com.f.Flippo.ui;

import com.f.Flippo.R;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.ImageView;

public class FlippoView extends ImageView {

	private boolean mState = true;

	public FlippoView(Context context) {
		super(context);
	}

	public FlippoView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public FlippoView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public boolean getState() {
		return mState;
	}

	public void setState(boolean state) {
		mState = state;
	}

	public void toggle() {
		setState(!getState());
		this.invalidate();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		if (mState) {
			super.setImageResource(R.drawable.t);
		} else {
			super.setImageResource(R.drawable.f);
		}
		super.onDraw(canvas);
	}
}
