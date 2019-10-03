package com.smorgasbork.hotdeath;

import org.json.JSONException;
import org.json.JSONObject;

import com.smorgasbork.hotdeath.R;

public class HumanPlayer extends Player 
{
	private boolean m_turnDecision = false;
	private boolean m_colorDecision = false;
	private boolean m_victimDecision = false;
	
	private boolean m_numCardsToDealDecision = false;
	
	public HumanPlayer (Game g, GameOptions go)
	{
		super (g, go);
	}
	
	public HumanPlayer (JSONObject o, Game g, GameOptions go) throws JSONException
	{
		super (o, g, go);
	}

	
	public void turnDecisionPass()
	{
		m_wantsToPass = true;
		m_turnDecision = true;
	}

	public void turnDecisionDrawCard()
	{
		m_wantsToDraw = true;
		m_turnDecision = true;
	}

	public void turnDecisionPlayCard(Card c)
	{
		// when the human player is playing, we're in a spin-wait mode, waiting for
		// m_turnDecision to turn true.  So if we make a valid play here, we'll 
		// set that bool to true.
		
		if (!m_hand.isInHand(c))
		{
			return;
		}
		

		if (m_game.checkCard(m_hand, c, true)) 
		{
			m_playingCard = c;
			m_wantsToPlayCard = true;
			m_lastDrawn = null;
			m_turnDecision = true;				
		}
		else 
		{
			m_game.promptUser(m_game.getString (R.string.msg_card_no_good), false);
		}
	}
	
	// called to wait for the user to make a move -- either to play a
	// card, draw, or pass
	public void startTurn()
	{
		m_wantsToPass = false;
		m_wantsToPlayCard = false;
		m_wantsToDraw = false;
		
		m_turnDecision = false;
		while (!m_turnDecision)
		{
			try
			{
				Thread.sleep(100);
			}
			catch (InterruptedException e)
			{
				
			}
			if (m_game.getStopping())
			{
				return;
			}
		}
		
		return;
	}	

	
	public int getNumCardsToDeal ()
	{
		m_game.promptForNumCardsToDeal();
		
		m_numCardsToDealDecision = false;
		while (!m_numCardsToDealDecision)
		{
			try
			{
				Thread.sleep(100);
			}
			catch (InterruptedException e)
			{
				
			}
			if (m_game.getStopping())
			{
				return 0;
			}
		}
		
		return m_numCardsToDeal;
	}
	
	// called after user selects from the alert dialog
	public void setNumCardsToDeal (int numCardsToDeal)
	{
		m_numCardsToDeal = numCardsToDeal;
		m_numCardsToDealDecision = true;
	}
	

	public int chooseColor()
	{
		m_game.promptForColor ();

		m_colorDecision = false;
		while (!m_colorDecision)
		{
			try
			{
				Thread.sleep(100);
			}
			catch (InterruptedException e)
			{
				
			}
			if (m_game.getStopping())
			{
				return 0;
			}
		}
		
		return m_chosenColor;
	}

	// called after user selects from the alert dialog
	public void setColor (int color)
	{
		m_chosenColor = color;
		m_colorDecision = true;
	}


	public void chooseVictim()
	{
		// if there's only one other active player, it is silly to
		// prompt the user for the victim..
		int activeplayercount = 0;
		int onlyactiveplayer = 0;
		if (m_game.getPlayer(Game.SEAT_WEST - 1).getActive())
		{
			activeplayercount++;
			onlyactiveplayer = Game.SEAT_WEST;
		}
		if (m_game.getPlayer(Game.SEAT_NORTH - 1).getActive())
		{
			activeplayercount++;
			onlyactiveplayer = Game.SEAT_NORTH;
		}
		if (m_game.getPlayer(Game.SEAT_EAST - 1).getActive())
		{
			activeplayercount++;
			onlyactiveplayer = Game.SEAT_EAST;
		}
		
		if (activeplayercount == 1)
		{
			m_victimDecision = true;
			m_chosenVictim = onlyactiveplayer;
			return;
		}
		
		// ok -- we've got more than one, so we prompt the user...
		m_game.promptForVictim();
		
		m_victimDecision = false;
		while (!m_victimDecision)
		{
			try
			{
				Thread.sleep(100);
			}
			catch (InterruptedException e)
			{
				
			}
			if (m_game.getStopping())
			{
				return;
			}
		}
	}

	// called after user selects from the alert dialog
	public void setVictim (int victim)
	{
		m_chosenVictim = victim;
		m_victimDecision = true;
	}





}
