package com.smorgasbork.hotdeath;


import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.text.method.ScrollingMovementMethod;


import com.smorgasbork.hotdeath.R;

public class Main extends Activity implements OnClickListener 
{
	public static final int DIALOG_ABOUT = 0;
	public static final int DIALOG_HELP = 1;
	
	@Override
	protected Dialog onCreateDialog(int id)
	{
		Dialog dlg = null;
		switch (id) 
		{
			case DIALOG_ABOUT:
				dlg = new Dialog(this);
				dlg.setContentView(R.layout.dlg_about);
				break;
			case DIALOG_HELP:
				dlg = new Dialog(this);
				dlg.setContentView(R.layout.dlg_help);
				break;
		}
		
		return dlg;
	}
	
	@Override
	protected void onPrepareDialog(int id, Dialog d)
	{
		TextView text;
		switch (id) 
		{
			case DIALOG_ABOUT:
				d.setTitle(this.getString(R.string.dlg_about_title));
	
				text = (TextView) d.findViewById(R.id.text);
				text.setMovementMethod(ScrollingMovementMethod.getInstance());
				
				try
				{
					String app_ver = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
					String str_about = this.getString(R.string.dlg_about_text);
					
					str_about = String.format(str_about, app_ver);
					text.setText(str_about);
				}
				catch (Exception e)
				{
					
				}
	
				break;
				
			case DIALOG_HELP:
				d.setTitle(this.getString(R.string.dlg_help_title));
	
				text = (TextView) d.findViewById(R.id.text);
				text.setMovementMethod(ScrollingMovementMethod.getInstance());
				text.setText(this.getString(R.string.dlg_help_text));
	
				break;
		}
	}
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        findViewById(R.id.btn_continue).setOnClickListener(this);
        findViewById(R.id.btn_new_game).setOnClickListener(this);
        findViewById(R.id.btn_settings).setOnClickListener(this);
        findViewById(R.id.btn_help).setOnClickListener(this);
        findViewById(R.id.btn_about).setOnClickListener(this);
        findViewById(R.id.btn_exit).setOnClickListener(this);        
    }
    
    @Override
    public void onResume ()
    {
    	super.onResume ();
    	
        View continueButton = findViewById(R.id.btn_continue);
        continueButton.setOnClickListener(this);
        
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		String s = prefs.getString("gamestate", "");
		
		if (s == "")
		{
			continueButton.setVisibility(View.GONE);
		}
		else
		{
			continueButton.setVisibility(View.VISIBLE);
		}
    }
    
    public void onClick(View v) {
    	Intent intent;
    	
        switch (v.getId()) {
        
        case R.id.btn_new_game:
            intent = new Intent(this, GameActivity.class);
            intent.putExtra(GameActivity.STARTUP_MODE, GameActivity.STARTUP_MODE_NEW);
            startActivity(intent);
            break;

        case R.id.btn_continue:
			intent = new Intent(this, GameActivity.class);
			intent.putExtra(GameActivity.STARTUP_MODE, GameActivity.STARTUP_MODE_CONTINUE);
			startActivity(intent);
			break;
			
        case R.id.btn_settings:
        	startActivity (new Intent (this, Prefs.class));
        	break;
           
        case R.id.btn_help:
        	this.showDialog(DIALOG_HELP);
        	break;

        case R.id.btn_about:
        	this.showDialog(DIALOG_ABOUT);
        	break;

        case R.id.btn_exit:
			finish();
			break;
        }
     }
}