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
import android.view.View;
import android.widget.GridView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.view.*;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.*;
import android.widget.Button;

public class GameActivity extends Activity 
{
	public static final String STARTUP_MODE = "com.smorgasbork.hotdeath.startup_mode";
	
	public static final int STARTUP_MODE_NEW = 1;
	public static final int STARTUP_MODE_CONTINUE = 2;
	
	public static final int DIALOG_CARD_HELP = 0;
	public static final int DIALOG_CARD_CATALOG = 1;
	
	private Dialog m_dlgCardCatalog = null;
	private Dialog m_dlgCardHelp = null;

	private View m_vMenuPanel = null;

	private Button m_btnFastForward = null;
	private Button m_btnMenuDraw = null;
	private Button m_btnMenuPass = null;
	private Button m_btnMenuHelp = null;

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

	public Button getBtnFastForward ()
	{
		return m_btnFastForward;
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
	    m_gt.setId(View.generateViewId());
	    
	    RelativeLayout l = new RelativeLayout (this);

		m_btnFastForward = (Button)getLayoutInflater().inflate(R.layout.action_button, null);
	    m_btnFastForward.setText(getString(R.string.lbl_fast_forward));
	    m_btnFastForward.setId(View.generateViewId());
	    m_btnFastForward.setVisibility(View.INVISIBLE);
	    m_btnFastForward.setOnClickListener (new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				m_btnFastForward.setVisibility (View.INVISIBLE);
				m_game.setFastForward (true);
			}
		});

		m_vMenuPanel = getLayoutInflater().inflate(R.layout.options_menu, null);
		m_vMenuPanel.setId(View.generateViewId());
		m_vMenuPanel.setVisibility(View.INVISIBLE);

		final float scale = m_gt.getContext().getResources().getDisplayMetrics().density;
		int btn_width = (int) (160 * scale + 0.5f);
		int btn_height = (int) (42 * scale + 0.5f);

	    l.addView(m_gt);

		RelativeLayout.LayoutParams lp;

		lp = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		lp.addRule(RelativeLayout.ALIGN_BOTTOM, m_gt.getId());
		lp.addRule(RelativeLayout.CENTER_HORIZONTAL, m_gt.getId());
		lp.bottomMargin = (int)(8 * scale + 0.5f);
		//lp.width = btn_width;
		lp.height = btn_height;

		l.addView(m_vMenuPanel, lp);

		lp = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		lp.addRule(RelativeLayout.ABOVE, m_vMenuPanel.getId());
		lp.addRule(RelativeLayout.CENTER_HORIZONTAL, m_gt.getId());
		lp.bottomMargin = (int)(8 * scale + 0.5f);
		lp.width = btn_width;
		lp.height = btn_height;

		l.addView(m_btnFastForward, lp);

		setContentView (l);


		m_btnMenuDraw = findViewById(R.id.btn_menu_draw);
		m_btnMenuPass = findViewById(R.id.btn_menu_pass);
		m_btnMenuHelp = findViewById(R.id.btn_menu_help);

		m_btnMenuDraw.setOnClickListener (new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				m_game.drawPileTapped();
				showMenuButtons();
			}
		});
		m_btnMenuPass.setOnClickListener (new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				m_game.humanPlayerPass();
				showMenuButtons();
			}
		});
		m_btnMenuHelp.setOnClickListener (new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				showCardCatalog();
			}
		});

		m_gt.setBottomMargin((int)(58 * scale + 0.5f));

	    m_gt.invalidate(); // force view to be laid out before we start the game
	    m_gt.requestFocus();
	    
	    m_gt.startGameWhenReady();
    }


	@Override
	public void openOptionsMenu() {
		super.invalidateOptionsMenu();
		super.openOptionsMenu();
		Configuration config = getResources().getConfiguration();
		if ((config.screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) > Configuration.SCREENLAYOUT_SIZE_LARGE) {
			int originalScreenLayout = config.screenLayout;
			config.screenLayout = Configuration.SCREENLAYOUT_SIZE_LARGE;
			super.openOptionsMenu();
			config.screenLayout = originalScreenLayout;
		} else {
			super.openOptionsMenu();
		}
	}
	
	@Override
	protected void onPause ()
	{
		if (m_dlgCardHelp != null && m_dlgCardHelp.isShowing())
		{
			m_dlgCardHelp.dismiss();
		}
		if (m_dlgCardCatalog != null && m_dlgCardCatalog.isShowing())
		{
			m_dlgCardCatalog.dismiss();
		}

		getIntent().putExtra(GameActivity.STARTUP_MODE, GameActivity.STARTUP_MODE_CONTINUE);

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
    protected void onDestroy() {
    	m_game.shutdown ();
    	m_game = null;
    	m_gt = null;
       	m_go = null;
    	
    	super.onDestroy ();
    };
    
    public void showCardHelp ()
    {
    	if (m_dlgCardHelp == null)
    	{
    		m_dlgCardHelp = new TapDismissableDialog(this);
    		m_dlgCardHelp.setContentView(R.layout.dlg_card_help);    		
    	}
		    	
		int cid = m_gt.getHelpCardID();
		Card c = m_gt.getCardByID (cid);
		if (c != null)
		{
			m_dlgCardHelp.setTitle(m_game.cardToString(c));

			TextView text = (TextView) m_dlgCardHelp.findViewById(R.id.text);
			text.setText(m_gt.getCardHelpText(cid));

			ImageView image = (ImageView) m_dlgCardHelp.findViewById(R.id.image);
			image.setImageBitmap(m_gt.getCardBitmap(cid));
		}

    	m_dlgCardHelp.show();
    }
    
    public void showCardCatalog ()
    {
    	if (m_dlgCardCatalog == null)
    	{
    		m_dlgCardCatalog = new Dialog(this);
    		m_dlgCardCatalog.requestWindowFeature(Window.FEATURE_NO_TITLE);
    		m_dlgCardCatalog.setContentView(R.layout.dlg_card_catalog);
			GridView gridview = (GridView) m_dlgCardCatalog.findViewById(R.id.gridview);
		    gridview.setAdapter(new CardImageAdapter(this));
		    
		    gridview.setOnItemClickListener(new OnItemClickListener() {
		        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
		        	CardImageAdapter cia = (CardImageAdapter)((GridView)parent).getAdapter();
		        	Integer[] cardids = cia.getCardIDs();
		        	
		        	GameActivity.this.m_gt.setHelpCardID (cardids[position]);
		        	//GameActivity.this.showDialog(GameActivity.DIALOG_CARD_HELP);
		        	showCardHelp();
		        }
		    });
    	}
    	
    	m_dlgCardCatalog.show();
    }


	public void showMenuButtons ()
	{
		if (m_game.getCurrPlayer() instanceof HumanPlayer)
		{
			if (m_game.getCurrPlayerUnderAttack() || m_game.getCurrPlayerDrawn())
			{
				m_btnMenuDraw.setEnabled(false);
				m_btnMenuDraw.setTextColor(0xff7f7f7f);
				m_btnMenuPass.setEnabled(true);
				m_btnMenuPass.setTextColor(0xffffffff);
			}
			else
			{
				m_btnMenuDraw.setEnabled(true);
				m_btnMenuDraw.setTextColor(0xffffffff);
				m_btnMenuPass.setEnabled(false);
				m_btnMenuPass.setTextColor(0xff7f7f7f);
			}
		}
		else
		{
			m_btnMenuDraw.setEnabled(false);
			m_btnMenuPass.setEnabled(false);
		}


		m_vMenuPanel.setVisibility(View.VISIBLE);
	}

	public void hideMenuButtons ()
	{
		m_vMenuPanel.setVisibility(View.INVISIBLE);
	}

}
