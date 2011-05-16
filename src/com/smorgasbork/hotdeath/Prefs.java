package com.smorgasbork.hotdeath;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;

public class Prefs extends PreferenceActivity implements OnSharedPreferenceChangeListener
{
	private static final String OPT_GAME_SPEED = "game_speed";
	private static final String OPT_GAME_SPEED_DEF = "1";
	private static final String OPT_TWO_DECKS = "two_decks";
	private static final boolean OPT_TWO_DECKS_DEF = false;
	private static final String OPT_COMPUTER_4TH = "computer_4th";
	private static final boolean OPT_COMPUTER_4TH_DEF = false;
	private static final String OPT_FACE_UP = "face_up";
	private static final boolean OPT_FACE_UP_DEF = false;
	private static final String OPT_CHEAT_LEVEL = "cheat_level";
	private static final String OPT_CHEAT_LEVEL_DEF = "0";
	private static final String OPT_CHEAT_CODE = "cheat_code";
	private static final String OPT_CHEAT_CODE_DEF = "";

	private static final String OPT_P1_SKILL_LEVEL = "p1_skill";
	private static final String OPT_P1_SKILL_LEVEL_DEF = "1";
	private static final String OPT_P2_SKILL_LEVEL = "p2_skill";
	private static final String OPT_P2_SKILL_LEVEL_DEF = "1";
	private static final String OPT_P3_SKILL_LEVEL = "p3_skill";
	private static final String OPT_P3_SKILL_LEVEL_DEF = "1";
	
	private static final String OPT_P1_AGGRESSION_LEVEL = "p1_aggression";
	private static final String OPT_P1_AGGRESSION_LEVEL_DEF = "0";
	private static final String OPT_P2_AGGRESSION_LEVEL = "p2_aggression";
	private static final String OPT_P2_AGGRESSION_LEVEL_DEF = "0";
	private static final String OPT_P3_AGGRESSION_LEVEL = "p3_aggression";
	private static final String OPT_P3_AGGRESSION_LEVEL_DEF = "0";


	// LOTS of code here to show ListPreference and EditTextPreference values
	// in the summary space
	
    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preferences);
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

        for(int i = 0; i < getPreferenceScreen().getPreferenceCount();i++)
        {
        	initSummary (getPreferenceScreen().getPreference(i));
        }
    }

    @Override 
    protected void onResume()
    {
        super.onResume();
        // Set up a listener whenever a key changes             
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override 
    protected void onPause() 
    { 
        super.onPause();
        // Unregister the listener whenever a key changes             
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);     
    } 

    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) 
    { 
        updatePrefSummary(findPreference(key));
    }

    private void initSummary(Preference p)
    {
       if (p instanceof PreferenceCategory)
       {
            PreferenceCategory pCat = (PreferenceCategory)p;
            for(int i = 0; i < pCat.getPreferenceCount(); i++)
            {
                initSummary(pCat.getPreference(i));
            }
       }
       else if (p instanceof PreferenceScreen)
       {
    	   PreferenceScreen pScreen = (PreferenceScreen)p;
            for(int i = 0; i < pScreen.getPreferenceCount(); i++)
            {
                initSummary(pScreen.getPreference(i));
            }
       }      
       else
       {
            updatePrefSummary(p);
       }

    }

    private void updatePrefSummary(Preference p)
    {
        if (p instanceof ListPreference) 
        {
            ListPreference listPref = (ListPreference) p; 
            p.setSummary(listPref.getEntry()); 
        }
        if (p instanceof EditTextPreference) {
            EditTextPreference editTextPref = (EditTextPreference) p; 
            p.setSummary(editTextPref.getText()); 
        }
    }
	
	public static int getGameSpeed (Context context)
	{
		String s = PreferenceManager.getDefaultSharedPreferences(context)
			.getString (OPT_GAME_SPEED, OPT_GAME_SPEED_DEF);
		return Integer.parseInt (s);
	}
	
	public static boolean getTwoDecks (Context context)
	{
		return PreferenceManager.getDefaultSharedPreferences(context)
					.getBoolean (OPT_TWO_DECKS, OPT_TWO_DECKS_DEF);
	}

	public static boolean getComputer4th (Context context)
	{
		return PreferenceManager.getDefaultSharedPreferences(context)
					.getBoolean (OPT_COMPUTER_4TH, OPT_COMPUTER_4TH_DEF);
	}

	public static boolean getFaceUp (Context context)
	{
		return PreferenceManager.getDefaultSharedPreferences(context)
					.getBoolean (OPT_FACE_UP, OPT_FACE_UP_DEF);
	}

	public static int getCheatLevel (Context context)
	{
		return Integer.parseInt (PreferenceManager.getDefaultSharedPreferences(context)
					.getString (OPT_CHEAT_LEVEL, OPT_CHEAT_LEVEL_DEF));
	}
	
    public static String getCheatCode (Context context)
    {
		String s = PreferenceManager.getDefaultSharedPreferences(context)
			.getString (OPT_CHEAT_CODE, OPT_CHEAT_CODE_DEF);
		return s;
    }	
	
	public static int getP1SkillLevel (Context context)
	{
		return Integer.parseInt (PreferenceManager.getDefaultSharedPreferences(context)
					.getString (OPT_P1_SKILL_LEVEL, OPT_P1_SKILL_LEVEL_DEF));
	}

	public static int getP1AggressionLevel (Context context)
	{
		return Integer.parseInt (PreferenceManager.getDefaultSharedPreferences(context)
					.getString (OPT_P1_AGGRESSION_LEVEL, OPT_P1_AGGRESSION_LEVEL_DEF));
	}

	public static int getP2SkillLevel (Context context)
	{
		return Integer.parseInt (PreferenceManager.getDefaultSharedPreferences(context)
					.getString (OPT_P2_SKILL_LEVEL, OPT_P2_SKILL_LEVEL_DEF));
	}

	public static int getP2AggressionLevel (Context context)
	{
		return Integer.parseInt (PreferenceManager.getDefaultSharedPreferences(context)
					.getString (OPT_P2_AGGRESSION_LEVEL, OPT_P2_AGGRESSION_LEVEL_DEF));
	}

	public static int getP3SkillLevel (Context context)
	{
		return Integer.parseInt (PreferenceManager.getDefaultSharedPreferences(context)
					.getString (OPT_P3_SKILL_LEVEL, OPT_P3_SKILL_LEVEL_DEF));
	}

	public static int getP3AggressionLevel (Context context)
	{
		return Integer.parseInt (PreferenceManager.getDefaultSharedPreferences(context)
					.getString (OPT_P3_AGGRESSION_LEVEL, OPT_P3_AGGRESSION_LEVEL_DEF));
	}
}
