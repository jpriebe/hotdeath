package com.smorgasbork.hotdeath;

import android.app.Dialog;
import android.content.Context;
import android.view.MotionEvent;

public class TapDismissableDialog extends Dialog 
{
	int _down_x;
	int _down_y;
	int _threshold = 7;
	
	public TapDismissableDialog (Context context)
	{
		super(context);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) 
	{
		if (event.getAction() == MotionEvent.ACTION_DOWN)
		{
			_down_x = (int)(event.getX());
			_down_y = (int)(event.getY());
		}
		
		if (event.getAction() == MotionEvent.ACTION_UP)
		{
			if (Math.abs((int)(event.getX()) - _down_x) > _threshold)
			{
				return true;
			}
			if (Math.abs((int)(event.getY()) - _down_y) > _threshold)
			{
				return true;
			}
			
			this.dismiss();
		}
		
		return true;
	}
}
