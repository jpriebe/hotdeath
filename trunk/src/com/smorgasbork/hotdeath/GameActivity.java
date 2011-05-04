package com.smorgasbork.hotdeath;


import android.app.Activity;
import org.json.*;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.app.Dialog;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.view.*;
import android.widget.ImageView;
import android.widget.TextView;
import com.smorgasbork.hotdeath.R;

public class GameActivity extends Activity 
{
	public static final String STARTUP_MODE = "com.smorgasbork.hotdeath.startup_mode";
	
	public static final int STARTUP_MODE_NEW = 1;
	public static final int STARTUP_MODE_CONTINUE = 2;
	
	public static final int DIALOG_CARD_HELP = 0;
	public static final int DIALOG_CARD_CATALOG = 1;
	
	private GameTable m_gt;
	private Game m_game;
	private GameOptions m_go;
	
	public Integer getCardImageID (int id)
	{
		return m_gt.getCardImageID (id);
	}
	
	public Integer[] getCardIDs ()
	{
		return m_gt.getCardIDs();
	}
	
	public Game getGame ()
	{
		return m_game;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		
	    int startup_mode = getIntent().getIntExtra(STARTUP_MODE, STARTUP_MODE_NEW);
		
		m_go = new GameOptions (this);
		
		m_game = null;
	    if (startup_mode == STARTUP_MODE_CONTINUE)
	    {
	    	SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
			String s = prefs.getString("gamestate", "");

	    	JSONObject o;
	    	try
	    	{
		    	o = new JSONObject (s);	    	
				m_game = new Game (o, this, m_go);
	    	}
	    	catch (JSONException e)
	    	{
	    	}
	    }
	    
	    // we're here either because the user chose "New game" or because we couldn't
	    // parse the game state JSON
	    if (m_game == null)
	    {
			m_game = new Game (this, m_go);
	    }
	    
	    m_gt = new GameTable(this, m_game, m_go);
		
	    setContentView(m_gt);
	    m_gt.invalidate(); // force view to be laid out before we start the game
	    m_gt.requestFocus();
	    
	    m_gt.startGameWhenReady();
    }
	
	@Override
	protected void onPause ()
	{
		super.onPause();
		m_game.pause();
		String gamestate = m_game.getSnapshot();
		
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString("gamestate", gamestate);
		editor.commit();
	}
	
	@Override
	protected void onResume ()
	{
		super.onResume ();
		m_game.unpause();
	}
	
    @Override 
    public void onConfigurationChanged(Configuration newConfig) { 
      //ignore orientation change  (use in conjunction with settings in the manifest)
      super.onConfigurationChanged(newConfig); 
    } 
		
	@Override
	protected Dialog onCreateDialog(int id)
	{
		Dialog dlg = null;
		switch (id) 
		{
			case DIALOG_CARD_HELP:
				dlg = new TapDismissableDialog(this);
				dlg.setContentView(R.layout.dlg_card_help);
				
				break;
				
			case DIALOG_CARD_CATALOG:
				dlg = new Dialog(this);
				dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);
				dlg.setContentView(R.layout.dlg_card_catalog);
				GridView gridview = (GridView) dlg.findViewById(R.id.gridview);
			    gridview.setAdapter(new CardImageAdapter(this));
			    
			    gridview.setOnItemClickListener(new OnItemClickListener() {
			        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
			        	CardImageAdapter cia = (CardImageAdapter)((GridView)parent).getAdapter();
			        	Integer[] cardids = cia.getCardIDs();
			        	
			        	GameActivity.this.m_gt.setHelpCardID (cardids[position]);
			        	GameActivity.this.showDialog(GameActivity.DIALOG_CARD_HELP);
			        }
			    });
			    
				break;
		}
		
		return dlg;
	}
	
	@Override
	protected void onPrepareDialog(int id, Dialog d)
	{
		switch (id) 
		{
			case DIALOG_CARD_HELP:
				int cid = m_gt.getHelpCardID();
				Card c = m_gt.getCardByID (cid);
				if (c != null)
				{
					d.setTitle(m_game.cardToString(c));
	
					TextView text = (TextView) d.findViewById(R.id.text);
					text.setText(m_gt.getCardHelpText(cid));
	
					ImageView image = (ImageView) d.findViewById(R.id.image);
					image.setImageBitmap(m_gt.getCardBitmap(cid));
				}

				break;
		}
		
		return;
	}
	
	
	@Override
	public boolean onCreateOptionsMenu (Menu menu)
	{
		super.onCreateOptionsMenu(menu);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate (R.menu.gameplay_menu, menu);
		return true;
	}
	
	@Override
	public boolean onPrepareOptionsMenu (Menu menu)
	{
		if (m_game.getCurrPlayer() instanceof HumanPlayer)
		{
			if (m_game.getCurrPlayerDrawn())
			{
				menu.getItem(0).setEnabled(false);
				menu.getItem(1).setEnabled(true);				
			}
			else
			{
				menu.getItem(0).setEnabled(true);
				menu.getItem(1).setEnabled(false);				
			}
		}
		else
		{
			menu.getItem(0).setEnabled(false);
			menu.getItem(1).setEnabled(false);
		}
		
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected (MenuItem item)
	{
		switch (item.getItemId())
		{
		case R.id.menu_item_draw:
			m_game.humanPlayerDraw();
			return true;
		case R.id.menu_item_pass:
			m_game.humanPlayerPass();
			return true;
		case R.id.menu_item_card_info:
			showDialog(DIALOG_CARD_CATALOG);
			return true;
		}
		
		return false;
	}
}
