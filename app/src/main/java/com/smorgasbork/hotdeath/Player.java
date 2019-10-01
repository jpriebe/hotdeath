package com.smorgasbork.hotdeath;

import java.util.Random;
import org.json.*;

public class Player {
	protected Game	m_game;
	protected GameOptions m_go;
	protected int		m_skill;
	protected int       m_aggression;
	protected Hand	    m_hand;
	protected Player    m_leftOpp;
	protected Player    m_rightOpp;
	protected int		m_seat;
	protected boolean   m_passing;
	protected boolean[][]	m_othersVoids;
	
	protected boolean m_wantsToDraw = false;
	protected boolean m_wantsToPlayCard = false;
	protected boolean m_wantsToPass = false;
	protected Card m_playingCard = null;
	protected int m_chosenColor = 0;
	protected int m_chosenVictim = 0;
	protected int m_numCardsToDeal = 0;

	protected int     m_lastScore;
	protected int     m_lastVirusPenalty;
	protected int     m_totalScore;

	protected Card    m_lastDrawn;
	
	protected boolean    m_active;

	protected int     m_virusPenalty;
	
	protected Card m_changedLastClicked = null;
	
	
	public boolean getWantsToDraw ()
	{
		return m_wantsToDraw;
	}
	
	public boolean getWantsToPlayCard ()
	{
		return m_wantsToPlayCard;
	}
	
	public boolean getWantsToPass ()
	{
		return m_wantsToPass;
	}
	
	public Card getPlayingCard ()
	{
		return m_playingCard;
	}

	public int getChosenColor ()
	{
		return m_chosenColor;
	}
	
	public int getChosenVictim ()
	{
		return m_chosenVictim;
	}

	public void setLeftOpp(Player p) 
	{
		m_leftOpp = p; 
	}
	
	public Player getLeftOpp() 
	{ 
		return m_leftOpp; 
	}
	
	public void setRightOpp(Player p)
	{ 
		m_rightOpp = p; 
	}
	
	public Player getRightOpp() 
	{ 
		return m_rightOpp; 
	}
	
	public int getSeat() 
	{
		return m_seat; 
	}
	
	public void setSeat(int s)
	{
		m_seat = s; 
	}
	
	public int getVirusPenalty() 
	{
		return m_virusPenalty; 
	}

	public void setVirusPenalty(int p) 
	{
		m_virusPenalty = p; 
	}

	public int getLastVirusPenalty() 
	{ 
		return m_lastVirusPenalty; 
	}
	
	public void setLastVirusPenalty(int p) 
	{
		m_lastVirusPenalty = p; 
	}

	public Hand getHand() 
	{ 
		return m_hand; 
	}

	public void finishTrick() 
	{ 
		return; 
	}
	

	public int getLastScore() 
	{ 
		return m_lastScore; 
	}
	
	public void setLastScore(int s) 
	{
		m_lastScore = s; 
	}
	
	public int getTotalScore() 
	{
		return m_totalScore; 
	}
	
	public void setTotalScore(int s) 
	{
		m_totalScore = s; 
	}
	
	public Card getLastDrawn() 
	{
		return m_lastDrawn; 
	}
	
	public void resetLastDrawn() 
	{
		m_lastDrawn = null; 
	}

	public boolean getActive()
	{
		return m_active;
	}

	public void setActive(boolean a)
	{
		m_active = a; 
	}

	public void shutdown ()
	{
		m_game = null;
		m_go = null;
		m_hand = null;
	}
	
	public Player(Game g, GameOptions go)
	{
		m_othersVoids = new boolean[4][4];
		m_game = g;
		m_go = go;
		m_hand = null;
		m_skill = 1;		// medium
		m_aggression = 0;	// neutral
		m_virusPenalty = 0;
		m_lastDrawn = null;
	}
	


	public void addCardToHand (Card c)
	{
		m_hand.addCard (c);
	}


	public void resetRound()
	{
		for (int i = 0; i < 4; i++) 
		{
			for (int j = 0; j < 4; j++) 
			{
				m_othersVoids[i][j] = false;
			}
		}

		m_hand = new Hand(this);
		m_active = true;
		m_passing = false;
	}


	public void resetGame()
	{
		m_lastScore = 0;
		m_virusPenalty = 0;
		if (android.os.Debug.isDebuggerConnected())
		{
			m_totalScore = 0;
		}
		else
		{
			m_totalScore = 0;		
		}
		resetRound();
	}

	public void chooseNumCardsToDeal ()
	{
	}
	
	public void startTurn()
	{
	}
	
	public int chooseColor() 
	{ 
		return Card.COLOR_WILD; 
	}
	
	public void chooseVictim() 
	{ 
		return; 
	}
	
	public Card getChangedLastClicked ()
	{
		return m_changedLastClicked;
	}

	public int getNumCardsToDeal()
	{
		Random rgen = new Random();
		return rgen.nextInt (11) + 5;
	}

	protected void drawCard()
	{
		Card c = m_game.drawCard();

		// we shouldn't get null except in the rarest of circumstances
		// (when draw pile is empty and there is only one card on the
		// table).  But it _can_ happen.
		if (c == null) 
		{
			return;
		}
		
		if (m_seat == Game.SEAT_SOUTH)
		{
			c.setFaceUp(true);
		}

		m_hand.addCard (c);
		m_game.sortHand(m_hand);
		m_lastDrawn = c;
	}
	
	public JSONObject toJSON () throws JSONException
	{
		JSONObject o = new JSONObject ();
		o.put ("active", m_active);
		o.put ("totalScore", m_totalScore);
		o.put ("lastScore", m_lastScore);
		if (m_lastDrawn != null)
		{
			o.put ("lastDrawn", m_lastDrawn.getDeckIndex());			
		}
		else
		{
			o.put ("lastDrawn", -1);
		}
		o.put ("virusPenalty", m_virusPenalty);
		o.put ("lastVirusPenalty", m_lastVirusPenalty);
		o.put ("hand", m_hand.toJSON());
		
		return o;
	}

	public Player(JSONObject o, Game g, GameOptions go) throws JSONException
	{
		this (g, go);
		m_hand = new Hand (o.getJSONObject("hand"), this, g.getDeck());
		m_totalScore = o.getInt("totalScore");
		m_lastScore = o.getInt("lastScore");
		m_virusPenalty = o.getInt("virusPenalty");
		m_lastVirusPenalty = o.getInt("lastVirusPenalty");
		m_active = o.getBoolean("active");
		
		int nLastDrawn = o.getInt("lastDrawn");
		if (nLastDrawn != -1)
		{
			m_lastDrawn = g.getDeck().getCard(nLastDrawn);
		}
		else
		{
			m_lastDrawn = null;
		}
	}
	
}
