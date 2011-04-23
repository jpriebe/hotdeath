package com.smorgasbork.hotdeath;

import java.util.Random;
import org.json.*;


public class ComputerPlayer extends Player 
{
	public ComputerPlayer (Game g, GameOptions go)
	{
		super (g, go);
	}
	
	public ComputerPlayer (JSONObject o, Game g, GameOptions go) throws JSONException
	{
		super (o, g, go);
	}
	
	public void chooseNumCardsToDeal ()
	{
		Random rgen = new Random();
		m_numCardsToDeal = rgen.nextInt(11) + 5;
	}
	
	public void startTurn ()
	{
		this.m_wantsToDraw = false;
		this.m_wantsToPass = false;
		this.m_wantsToPlayCard = false;
				
		m_game.waitABit ();
		
		if (!m_hand.hasValidCards(m_game))
		{
			// if we have no valid cards, we either pass or draw, depending on
			// whether we've already drawn...
			
			if (this.m_lastDrawn != null)
			{
				// if we just drew a card, then we have to pass
				this.m_wantsToPass = true;
				return;
			}
			
			// otherwise, we can draw
			this.m_wantsToDraw = true;
			return;
		}

		this.playCard();
		
		return;
	}
	
	@Override
	public void drawCard ()
	{
		m_game.waitABit ();
		super.drawCard();
	}

	public int chooseColor()
	{
		m_game.waitABit();
		
		// FIXME -- we need a real strategy
		int maxCount = 0;
		int maxColor = 0;

		for (int i = Card.COLOR_RED; i < Card.COLOR_WILD; i++)
		{
			int cnt = m_hand.countSuit(i);
			if (cnt > maxCount) 
			{
				maxCount = cnt;
				maxColor = i;
			}
		}

		m_chosenColor = maxColor;
		
		return m_chosenColor;
	}
	
	public void readAggressionAndSkill()
	{
		if (m_seat == Game.SEAT_WEST) 
		{
			m_aggression = m_go.getP1Agg() - 6;
			m_skill = m_go.getP1Skill();
		}
		else if (m_seat == Game.SEAT_NORTH)
		{
			m_aggression = m_go.getP2Agg() - 6;
			m_skill = m_go.getP2Skill();
		}
		else if (m_seat == Game.SEAT_EAST)
		{
			m_aggression = m_go.getP3Agg() - 6;
			m_skill = m_go.getP3Skill();
		}
		// this is only here so that we can have a 4th computer player
		// if we are testing.
		else 
		{
			m_aggression = m_go.getP2Agg() - 6;
			m_skill = m_go.getP2Skill();
		}
	}


	public void playCard()
	{
		readAggressionAndSkill();

		int maxpointval = -1000;
		Card bestcard = null;
		m_hand.calculateValue();

		m_wantsToPass = false;
		
		// if we just drew something, we can either hold it or throw it;
		// for now, we always throw it if it's valid
		if (m_lastDrawn != null)
		{
			if (m_game.checkCard(m_hand, m_lastDrawn, false))
			{
				m_playingCard = m_lastDrawn;
			}
			else
			{
				m_wantsToPass = true;
			}
		}

		if (bestcard == null)
		{
			for (int i = 0; i < m_hand.getNumCards(); i++) 
			{
				Card tc = m_hand.getCard(i);
				if (m_game.checkCard(m_hand, tc, false))
				{
					boolean bIsDefender = m_game.getLastCardCheckedIsDefender();
					// if we've got a defender, play it
					if (bIsDefender)
					{
						bestcard = tc;
						break;
					}
					
					// otherwise, try to find the one with the highest point value
					// so we can toss it out of our hand
					int testval = (this.m_skill > 1) ? tc.getCurrentValue() : tc.getPointValue();
					if (testval >= maxpointval) 
					{
						maxpointval = testval;
						bestcard = tc;
					}
				}
			}
		}
		
		if (bestcard != null)
		{
			m_playingCard = bestcard;
			m_wantsToPlayCard = true;
		}
		else
		{
			// nothing to play
			this.m_wantsToDraw = true;
		}

	}


	public void addCardToHand(Card c)
	{
		super.addCardToHand(c);

		readAggressionAndSkill();
	}


	public void finishTrick()
	{
		readAggressionAndSkill();
	}




	public void chooseVictim()
	{
		m_game.waitABit ();
		
		// weak skill level
		if (m_skill == 0) 
		{
			// find the first player who is still active
			if (m_aggression > 3) 
			{
				// be nasty and go after the south player first
				for (int i = 0; i <= 3; i++)
				{
					// don't punish yourself
					if (m_seat == i + 1)
					{
						continue;
					}
					if ((m_game.getPlayer(i)).getActive())
					{
						m_chosenVictim = i + 1;
						return;
					}
				}
			}
			else 
			{
				// find the next player, ccw from East
				for (int i = 3; i >= 0; i--) 
				{
					// don't punish yourself
					if (m_seat == i + 1)
					{
						continue;
					}
					if ((m_game.getPlayer(i)).getActive())
					{
						m_chosenVictim = i + 1;
						return;
					}
				}

			}
		}

		// strong and expert
		int minpoints = 1000000;
		int minplayer = 0;

		// find the next player
		for (int i = 3; i >= 0; i--) 
		{
			// don't punish yourself
			if (m_seat == i + 1) 
			{
				continue;
			}

			if (!(m_game.getPlayer(i)).getActive())
			{
				continue;
			}

			int score = (m_game.getPlayer(i)).getTotalScore();
			
			// artificially inflate or deflate south's score based on
			// player aggression
			if (i == Game.SEAT_SOUTH - 1) 
			{
				score -= 25 * m_aggression;
			}
			if (score < minpoints) 
			{
				minplayer = i + 1;
				minpoints = score;
			}
		}
		
		m_chosenVictim = minplayer;
	}




}
