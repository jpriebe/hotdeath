package com.smorgasbork.hotdeath;

import java.util.Random;
import android.util.Log;

import com.smorgasbork.hotdeath.R;
import org.json.*;

public class Game extends Thread {
	public static final int MAX_NUM_CARDS = 216;
	
	private boolean m_stopping = false;
	
	public static final int SEAT_SOUTH = 1;
	public static final int SEAT_WEST = 2;
	public static final int SEAT_NORTH = 3;
	public static final int SEAT_EAST = 4;
	
	public static final int DIR_CLOCKWISE = 1;
	public static final int DIR_CCLOCKWISE = 2;

	private boolean m_roundComplete = true;
	private boolean m_waitingToStartRound = false;
	private boolean m_gameOver = false;
	private int m_winner = 0;
	
	private GameTable	m_gt;
	private GameOptions m_go;
	private GameActivity m_ga;
	private Player[]	m_players;

	private Player		m_startPlayer;
	private Player		m_currPlayer;
	private Player      m_nextPlayerPreset;
	private Player		m_dealer;
	private int         m_numCardsToDeal;
	private Card		m_currCard;
	private Card		m_prevCard;
	private CardDeck	m_deck;
	private CardPile    m_drawPile;
	private CardPile    m_discardPile;
	private int			m_numCardsPlayed;
	private int			m_direction;
	private int         m_currColor;
	private int			m_cardsPlayed;
	private boolean     m_forceDrawing = false;
	
	private boolean     m_fastForward = false;

	private Penalty     m_penalty;
	
	private boolean m_lastCardCheckedIsDefender = false;
	
	private Object m_pauseLock = new Object();  
	private boolean m_paused = false;
	private boolean m_resumingSavedGame = false;
	
	private JSONObject m_snapshot = null;
	
	public boolean getStopping ()
	{
		return m_stopping;
	}
	
	public void setFastForward (boolean ff)
	{
		Log.d("HDU", "setFastForward (" 
				+ (ff ? "true"  : "false")
				+ ")");

		m_fastForward = ff;
	}
	
	public boolean getFastForward ()
	{
		return m_fastForward;
	}

	public int getWinner ()
	{
		return m_winner;
	}
	
	public void setGameTable (GameTable gt)
	{
		m_gt = gt;
	}

	public Player getDealer()
    {
        return m_dealer;
    }
	
	public boolean getRoundComplete ()
	{
		return m_roundComplete;
	}

	public Player getStartPlayer()
    {
        return m_startPlayer;
    }

	public Player getCurrPlayer()
    {
        return m_currPlayer;
    }
	
	public boolean getCurrPlayerUnderAttack ()
	{
		return m_penalty.getType() != Penalty.PENTYPE_NONE;
	}
	
	public boolean getCurrPlayerDrawn()
	{
		return (m_currPlayer.getLastDrawn() != null);
	}

	public Player getPlayer(int i)
    {
        return m_players[i];
    }

	public Card getLastPlayedCard()
    {
        return m_currCard;
    }

	public int getNumCardsPlayed()
    {
        return m_numCardsPlayed;
    }

	public CardPile getDrawPile()
    {
        return m_drawPile;
    }

	public CardPile getDiscardPile()
    {
        return m_discardPile;
    }

	public CardDeck getDeck ()
    {
        return m_deck;
    }

	public int getCurrColor()
    {
        return m_currColor;
    }

	public int getDirection()
    {
        return m_direction;
    }

	public Penalty getPenalty()
    {
        return m_penalty;
    }
	
	private void waitUntilUnpaused ()
	{
		synchronized (m_pauseLock) {
		    while (m_paused) {
		        try {
		            m_pauseLock.wait();
		        } catch (InterruptedException e) {
		        }
		    }
		}
	}
	
	public void shutdown ()
	{
		if (!m_stopping)
		{
			// on the first call, we'll set a flag
			m_stopping = true;
			Log.d("HDU", "Game thread shutdown requested...");
			return;
		}

		// on the second call, we'll really shut down; but also be careful
		// in case we get called more than two times...
		Log.d("HDU", "Game thread nulling out references and exiting...");
    	if  (m_gt != null)
    	{
    		m_gt.shutdown ();
    		m_go.shutdown ();
    		
    		for (int i = 0; i < m_players.length; i++)
    		{
    			m_players[i].shutdown();
    		}
    	}
		m_go = null;
		m_ga = null;
		m_gt = null;
	}
	
	public Game (JSONObject gamestate, GameActivity ga, GameOptions go)
	{
		m_go = go;
		m_ga = ga;
		m_penalty = null;

		m_players = new Player[4];
		
		JSONObject o;
		JSONArray a;
		
		try
		{
			o = gamestate.getJSONObject("state");
			
			m_snapshot = gamestate;
			
			m_deck = new CardDeck(gamestate.getJSONObject("deck"));
			m_drawPile = new CardPile(gamestate.getJSONObject("drawPile"), m_deck);
			m_discardPile = new CardPile(gamestate.getJSONObject("discardPile"), m_deck);
	
			m_currColor = o.getInt("currColor");
			m_direction = o.getInt("direction");
			m_cardsPlayed = o.getInt("cardsPlayed");
			m_roundComplete = o.getBoolean("roundComplete");
			
			int nCurrCard = o.getInt("currCard");
			if (nCurrCard != -1)
			{
				m_currCard = m_deck.getCard(nCurrCard);
			}
			else
			{
				m_currCard = null;
			}

			int nCurrPlayer = o.getInt("currPlayer") - 1;
			int nDealer = o.getInt("dealer") - 1;
			
			a = gamestate.getJSONArray ("players");
			
			if (m_go.getComputer4th())
			{
				m_players[0] = new ComputerPlayer (a.getJSONObject(0), this, m_go);
			}
			else
			{
				m_players[0] = new HumanPlayer (a.getJSONObject(0), this, m_go);
			}
	
			m_players[1] = new ComputerPlayer (a.getJSONObject(1), this, m_go);
			m_players[2] = new ComputerPlayer (a.getJSONObject(2), this, m_go);
			m_players[3] = new ComputerPlayer (a.getJSONObject(3), this, m_go);
	
			(m_players[0]).setSeat (SEAT_SOUTH);
			(m_players[1]).setSeat (SEAT_WEST);
			(m_players[2]).setSeat (SEAT_NORTH);
			(m_players[3]).setSeat (SEAT_EAST);
	
			(m_players[0]).setLeftOpp (m_players[1]);
			(m_players[1]).setLeftOpp (m_players[2]);
			(m_players[2]).setLeftOpp (m_players[3]);
			(m_players[3]).setLeftOpp (m_players[0]);
	
			(m_players[0]).setRightOpp (m_players[3]);
			(m_players[2]).setRightOpp (m_players[1]);
			(m_players[1]).setRightOpp (m_players[0]);
			(m_players[3]).setRightOpp (m_players[2]);

			m_penalty = new Penalty(gamestate.getJSONObject("penalty"), this, m_deck);
			
			m_currPlayer = m_players[nCurrPlayer];
			m_dealer = m_players[nDealer];

			m_resumingSavedGame = true;
		}
		catch (JSONException e)
		{
			// FIXME: not sure what to do here if we couldn't load the object from JSON
			
		}
	}
	
	public String getSnapshot ()
	{
		if (m_gameOver)
		{
			// when game is over, we don't save a snapshot
			return "";
		}
		
		if (m_snapshot == null)
		{
			return "";
		}
		
		return m_snapshot.toString();
	}
	
	public JSONObject toJSON ()
	{
		synchronized (m_pauseLock) {
			JSONObject o = new JSONObject ();
			
			try
			{
				JSONObject o2 = new JSONObject ();
				o2.put("dealer", m_dealer.getSeat());
				o2.put("currPlayer", m_currPlayer.getSeat());
				o2.put("currColor", m_currColor);
				o2.put("direction", m_direction);
				o2.put("cardsPlayed", m_cardsPlayed);
				o2.put("roundComplete", m_roundComplete);
				
				if (m_currCard != null)
				{
					o2.put("currCard", m_currCard.getDeckIndex());
				}
				else
				{
					o2.put ("currCard", -1);
				}
	
				o.put("state", o2);
				o.put("deck", m_deck.toJSON ());
				o.put("drawPile", m_drawPile.toJSON ());
				o.put("discardPile", m_discardPile.toJSON ());
				o.put("penalty", m_penalty.toJSON());
				
				JSONArray a = new JSONArray ();
				for (int i = 0; i < 4; i++)
				{
					a.put (m_players[i].toJSON ());
				}
				o.put("players", a);
			}
			catch (JSONException e)
			{
				Log.d("HDU", "JSON exception in Game.toJSON(): " + e.getMessage());
			}
			
			return o;
		}
	}
	
	public void pause ()
	{
		synchronized (m_pauseLock)
		{
			m_paused = true;
		}
	}
	
	public void unpause ()
	{
		synchronized (m_pauseLock)
		{
			m_paused = false;
			m_pauseLock.notifyAll();
		}
	}

	
	public Game(GameActivity ga, GameOptions go)
	{
		m_go = go;
		m_ga = ga;
		m_penalty = null;

		m_players = new Player[4];
		
		m_deck = null;
		m_drawPile = null;
		m_discardPile = null;

		m_direction = DIR_CLOCKWISE;

		if (m_go.getComputer4th())
		{
			m_players[0] = new ComputerPlayer (this, m_go);
		}
		else
		{
			m_players[0] = new HumanPlayer (this, m_go);
		}

		m_players[1] = new ComputerPlayer (this, m_go);
		m_players[2] = new ComputerPlayer (this, m_go);
		m_players[3] = new ComputerPlayer (this, m_go);

		(m_players[0]).setSeat (SEAT_SOUTH);
		(m_players[1]).setSeat (SEAT_WEST);
		(m_players[2]).setSeat (SEAT_NORTH);
		(m_players[3]).setSeat (SEAT_EAST);

		(m_players[0]).setLeftOpp (m_players[1]);
		(m_players[1]).setLeftOpp (m_players[2]);
		(m_players[2]).setLeftOpp (m_players[3]);
		(m_players[3]).setLeftOpp (m_players[0]);

		(m_players[0]).setRightOpp (m_players[3]);
		(m_players[2]).setRightOpp (m_players[1]);
		(m_players[1]).setRightOpp (m_players[0]);
		(m_players[3]).setRightOpp (m_players[2]);
	}


	public void resetRound()
	{
		m_direction = DIR_CLOCKWISE;
		m_deck =        new CardDeck ();
		m_drawPile =    new CardPile ();
		m_discardPile = new CardPile ();
		m_cardsPlayed = 0;
		m_roundComplete = false;

		for (int i = 0; i < 4; i++) 
		{
			m_players[i].resetRound();
		}
	}


	public void resetGame()
	{
		m_gameOver = false;
		m_winner = 0;
		
		for (int i = 0; i < 4; i++) 
		{
			m_players[i].resetGame();
		}

		Random rgen = new Random();
		int dealer = rgen.nextInt(4);
		m_dealer = m_players[dealer];
	}


	public void dealHands()
	{
		int i;
		
		waitUntilUnpaused ();
		
		Player p = m_dealer.getLeftOpp();
		String msg = String.format (getString(R.string.msg_dealing), seatToString(m_dealer.getSeat()), m_numCardsToDeal);
		promptUser(msg);
		
		// use this mechanism to set up scenarios for testing edge cases
		boolean debugDeal = false;
		if (android.os.Debug.isDebuggerConnected() && debugDeal)
		{
			int[][] hands = {
					/*
				// try to get draw 4s stacked with hotdeath on top, and a magic 5 to null it
				{Card.ID_RED_5_MAGIC, Card.ID_WILD_DRAWFOUR},
				{Card.ID_BLUE_0, Card.ID_BLUE_1, Card.ID_BLUE_2, Card.ID_WILD_DB},
				{Card.ID_RED_3, Card.ID_RED_4, Card.ID_RED_5, Card.ID_WILD_DRAWFOUR},
				{Card.ID_GREEN_6, Card.ID_GREEN_7, Card.ID_GREEN_8, Card.ID_WILD_HD}
				*/
					
				/*
				// All four bastard cards...
				{Card.ID_BLUE_0_FUCKYOU, Card.ID_GREEN_0_QUITTER, Card.ID_YELLOW_0_SHITTER, Card.ID_RED_0_HD, Card.ID_BLUE_1, Card.ID_BLUE_2, Card.ID_BLUE_3},
				{Card.ID_GREEN_1, Card.ID_GREEN_2, Card.ID_GREEN_3, Card.ID_GREEN_4, Card.ID_GREEN_5, Card.ID_GREEN_6, Card.ID_GREEN_7}, 
				{Card.ID_RED_1, Card.ID_RED_2, Card.ID_RED_3, Card.ID_RED_4, Card.ID_RED_5, Card.ID_RED_6, Card.ID_RED_7}, 
				{Card.ID_YELLOW_1, Card.ID_YELLOW_2, Card.ID_YELLOW_3, Card.ID_YELLOW_4, Card.ID_YELLOW_5, Card.ID_YELLOW_6, Card.ID_YELLOW_7}
				*/
				
				/*
				// this makes a mystery on a 69 highly likely
				{Card.ID_YELLOW_6, Card.ID_WILD_MYSTERY, Card.ID_YELLOW_0_SHITTER},
				{Card.ID_YELLOW_3, Card.ID_YELLOW_4, 
						Card.ID_RED_1, Card.ID_RED_2, Card.ID_RED_3, Card.ID_RED_4, Card.ID_RED_5, Card.ID_RED_6, Card.ID_RED_7, Card.ID_RED_8, Card.ID_RED_D, Card.ID_RED_R, Card.ID_RED_S, Card.ID_RED_S_DOUBLE, Card.ID_RED_R_SKIP,
						Card.ID_GREEN_1, Card.ID_GREEN_2, Card.ID_GREEN_3, Card.ID_GREEN_4, Card.ID_GREEN_5, Card.ID_GREEN_6, Card.ID_GREEN_7, Card.ID_GREEN_8, Card.ID_GREEN_9, Card.ID_GREEN_D, Card.ID_GREEN_R, Card.ID_GREEN_S, Card.ID_GREEN_S_DOUBLE, Card.ID_GREEN_R_SKIP,
						Card.ID_BLUE_1, Card.ID_BLUE_2, Card.ID_BLUE_3, Card.ID_BLUE_4, Card.ID_BLUE_5, Card.ID_BLUE_6, Card.ID_BLUE_7, Card.ID_BLUE_8, Card.ID_BLUE_9, Card.ID_BLUE_D, Card.ID_BLUE_R, Card.ID_BLUE_S, Card.ID_BLUE_S_DOUBLE, Card.ID_BLUE_R_SKIP
				},
				{Card.ID_YELLOW_1, Card.ID_YELLOW_2}, 
				{Card.ID_YELLOW_69, Card.ID_RED_9
				 */
				
				/*
				// get the south player ejected
				{Card.ID_YELLOW_3, Card.ID_YELLOW_4, 
					Card.ID_RED_1, Card.ID_RED_2, Card.ID_RED_3, Card.ID_RED_4, Card.ID_RED_5, Card.ID_RED_6, Card.ID_RED_7, Card.ID_RED_8, Card.ID_RED_9, Card.ID_RED_D, Card.ID_RED_R, Card.ID_RED_S, Card.ID_RED_S_DOUBLE, Card.ID_RED_R_SKIP,
					Card.ID_BLUE_1, Card.ID_BLUE_2, Card.ID_BLUE_3, Card.ID_BLUE_4, Card.ID_BLUE_5, Card.ID_BLUE_6, Card.ID_BLUE_7, Card.ID_BLUE_8, Card.ID_BLUE_9, Card.ID_BLUE_D, Card.ID_BLUE_R, Card.ID_BLUE_S, Card.ID_BLUE_S_DOUBLE, Card.ID_BLUE_R_SKIP
				},
				{Card.ID_YELLOW_1, Card.ID_YELLOW_2}, 
				{Card.ID_GREEN_1, Card.ID_GREEN_2, Card.ID_GREEN_3, Card.ID_GREEN_4, Card.ID_GREEN_5, Card.ID_GREEN_6, Card.ID_GREEN_7, Card.ID_GREEN_8, Card.ID_GREEN_9, Card.ID_GREEN_D, Card.ID_GREEN_R, Card.ID_GREEN_S, Card.ID_GREEN_S_DOUBLE, Card.ID_GREEN_R_SKIP},
				{Card.ID_YELLOW_1_MAD, Card.ID_GREEN_0_QUITTER}
				*/
				
				/*
				// get East to stack a Draw Four on a Hot Death so we can see what happens
				// with the Magic 5
				{Card.ID_RED_5_MAGIC, Card.ID_RED_1, Card.ID_RED_2},
				{Card.ID_GREEN_3, Card.ID_GREEN_4},
				{Card.ID_WILD_HD, Card.ID_BLUE_5, Card.ID_BLUE_6},
				{Card.ID_WILD_DRAWFOUR, Card.ID_RED_7, Card.ID_RED_8}
				*/
				
				/*
				// put south player under attack
				{Card.ID_RED_1, Card.ID_RED_2, Card.ID_WILD_DRAWFOUR},
				{Card.ID_GREEN_3, Card.ID_GREEN_4, Card.ID_WILD_DRAWFOUR},
				{Card.ID_BLUE_5, Card.ID_BLUE_6, Card.ID_WILD_DRAWFOUR},
				{Card.ID_RED_7, Card.ID_RED_8, Card.ID_WILD_DRAWFOUR}
				*/

				/*
				// stick player one with 69 and the shitter
				{Card.ID_YELLOW_69, Card.ID_YELLOW_0_SHITTER, Card.ID_WILD_DRAWFOUR, Card.ID_WILD_DRAWFOUR},
				{Card.ID_RED_1},
				{Card.ID_RED_2},
				{Card.ID_GREEN_3_AIDS, Card.ID_BLUE_2_SHIELD, Card.ID_GREEN_4_IRISH}
				*/
				
				/*
				// want a retaliation thrown on a delayed blast
				{Card.ID_BLUE_0_FUCKYOU, Card.ID_BLUE_1, Card.ID_BLUE_2, Card.ID_BLUE_3},
				{Card.ID_RED_1, Card.ID_RED_2, Card.ID_RED_3, Card.ID_RED_3},
				{Card.ID_GREEN_1, Card.ID_GREEN_2, Card.ID_WILD_DB},
				{Card.ID_YELLOW_1, Card.ID_YELLOW_2, Card.ID_YELLOW_3}
				*/
			};
			
			for (i = 0; i < 4; i++)
			{
				p = this.m_players[i];
				int[] hCards = hands[i];
				for (int j = 0; j < hCards.length; j++)
				{
					for (int k = 0; k < m_deck.getNumCards(); k++)
					{
						Card c = m_deck.getCard(k);
						if ((c.getHand() == null) && (c.getID() == hands[i][j]))
						{
							if (p.getSeat() == SEAT_SOUTH)
							{
								c.setFaceUp(true);
							}
							else
							{
								c.setFaceUp(false);
							}
							p.addCardToHand(c);
							break;
						}
					}
				}
			}
			
			for (i = 0; i < m_deck.getNumCards(); i++) 
			{
				Card c = m_deck.getCard(i);
				if (c.getHand() == null)
				{
					m_drawPile.addCard(c);
				}
			}
		}
		else
		{
			for (i = 0; i < 4 * m_numCardsToDeal; i++) 
			{
				Card c = m_deck.getCard(i);
	
				if (p.getSeat() == SEAT_SOUTH)
				{
					c.setFaceUp(true);
				}
				else
				{
					c.setFaceUp(false);
				}
	
				p.addCardToHand (c);
	
				p = p.getLeftOpp();
			}
		
			int cheatlevel = m_go.getCheatLevel();
	
			for (; i < m_deck.getNumCards(); i++) 
			{
				Card c = m_deck.getCard(i);
	
				// MWUAHAHAHA
				if (cheatlevel > 0) 
				{
					if (c.getID() == Card.ID_RED_0_HD
						|| c.getID() == Card.ID_RED_2_GLASNOST
						|| c.getID() == Card.ID_RED_5_MAGIC
						|| c.getID() == Card.ID_RED_D_SPREADER
						|| c.getID() == Card.ID_YELLOW_69
						|| c.getID() == Card.ID_GREEN_D_SPREADER
						|| c.getID() == Card.ID_WILD_MYSTERY
						|| c.getID() == Card.ID_GREEN_3_AIDS
						|| c.getID() == Card.ID_WILD_DB
						|| c.getID() == Card.ID_BLUE_2_SHIELD
						|| c.getID() == Card.ID_GREEN_4_IRISH
						|| c.getID() == Card.ID_WILD_DRAWFOUR
						) 
					{
						c.setFaceUp(true);
						c = ((m_players[SEAT_SOUTH - 1]).getHand()).swapCard(c);
						c.setFaceUp(false);
						cheatlevel--;
					}
				}
	
				m_drawPile.addCard(c);
			}
		}
		
		for (i = 0; i < 4; i++) 
		{
			Hand h = (m_players[i]).getHand();
			sortHand(h);
		}
	}	


	public int rolloverDiscardPile()
	{
		int numPlayedCards = m_discardPile.getNumCards();

		if (numPlayedCards > 1) 
		{
			Card topCard = m_discardPile.drawCard();

			String msg;
			if (numPlayedCards > 2) 
			{
				msg = String.format (getString(R.string.msg_shuffling_discard), numPlayedCards - 1);
			}
			else 
			{
				msg = getString (R.string.msg_shuffling_discard_1);
			}
			
			promptUser (msg);
				
			for (int i = 1; i < numPlayedCards; i++) {
				Card tc = m_discardPile.drawCard();
				tc.setFaceUp(false);
				m_drawPile.addCard(tc);
			}
			m_drawPile.shuffle();
			m_discardPile.addCard(topCard);

			return numPlayedCards - 1;
		}

		// we got here b/c there is only one card in the discard
		// pile, and it's the face up card, so we can't roll it over...
		if (!m_forceDrawing)
		{
			redrawTable();
			promptUser (getString(R.string.msg_discard_empty));
		}
		
		return 0;
	}


	public Card drawCard ()
	{
		Card c = null;

		// if we purged the draw and discard piles on the last draw (like
		// a big draw 69), we might end up with no draw pile at all.
		if (m_drawPile.getNumCards() == 0) 
		{
			// try to roll over the discard pile
			if (rolloverDiscardPile() > 0) 
			{
				c = m_drawPile.drawCard();
			}
			else 
			{
			}

		}
		else 
		{
			c = m_drawPile.drawCard();
		}

		// do we need to reset the draw pile?  It's best to do this
		// immediately after we draw the last card in the draw pile
		// so that we don't have an empty draw pile on screen
		if (m_drawPile.getNumCards() == 0) 
		{
			rolloverDiscardPile();
		}

		return c;
	}


	public void startRound()
	{
		waitUntilUnpaused ();
		
		resetRound();
		m_startPlayer = m_dealer.getLeftOpp();

		m_penalty = new Penalty ();

		m_currCard = null;
		m_prevCard = null;

		int i;
		
		m_deck.reset(m_go.getStandardRules(), m_go.getOneDeck());
		m_deck.shuffle();

		for (i = 0; i < 4; i++) 
		{
			((m_players[i]).getHand()).reset();
		}

		if (m_go.getStandardRules())
		{
			m_numCardsToDeal = 7;
		}
		else
		{
			m_numCardsToDeal = m_dealer.getNumCardsToDeal();
			
			if (m_stopping)
			{
				return;
			}
		}
		
		dealHands();
		postDealHands();
	}
	
	private void postDealHands ()
	{
		do 
		{
			// FIXME!!! the dealer is supposed to eat penalties...
			if ((m_currCard = m_drawPile.drawCard()) != null) 
			{
				m_currColor = m_currCard.getColor();
				m_discardPile.addCard (m_currCard);
				m_currCard.setFaceUp(true);
			}
		} while (m_currColor == Card.COLOR_WILD);

		m_startPlayer = m_dealer.getLeftOpp();
		m_numCardsPlayed = 0;

		m_currPlayer = m_startPlayer;

		redrawTable();

		for (int i = 0; i < 4; i++) 
		{
			Hand h = (m_players[i]).getHand();
			if (checkForAllBastardCards(h)) 
			{
				redrawTable ();
				gotAllBastardCards (m_players[i]);
				finishRound(m_players[i]);
				
				return;
			}
		}
	}
	
	private void runRound ()
	{
		while (true)
		{
			m_snapshot = this.toJSON();
			waitUntilUnpaused ();
			
			if (m_roundComplete)
			{
				// this could conceivably happen if the round ends immediately after the deal
				return;
			}
			
			if (m_stopping)
			{
				return;
			}
			
			if (advanceRound() == false)
			{
				break;
			}
		}
	}

	public void run ()
	{
		if (m_resumingSavedGame)
		{
			m_resumingSavedGame = false;
			if (m_roundComplete)
			{
				waitForNextRound ();
				startRound ();
			}
		}
		else
		{
			startGame ();
		}

		while (!m_gameOver)
		{
			runRound ();
			if (m_stopping)
			{
				shutdown ();
				Log.d("HDU", "exiting Game.run()...");
				return;
			}
			if (!m_gameOver)
			{
				waitForNextRound();
				startRound ();
			}
		}
		
		// call shutdown() once; when user backs out of the activity, shutdown() will
		// get called for the second time, releasing all big references
		shutdown();

		Log.d("HDU", "exiting Game.run()...");
	}

	public void startGame()
	{
		resetGame();
		startRound();
	}


	public Player getNextPlayer ()
	{
		return getNextPlayer (null);
	}
	
	public Player getNextPlayer(Player currentplayer)
	{
		boolean notdone = true;

		if (currentplayer == null) 
		{
			currentplayer = m_currPlayer;
		}

		Player p = currentplayer;
		while (notdone) 
		{
			p = (m_direction == DIR_CLOCKWISE)
				? p.getLeftOpp()
				: p.getRightOpp();

			if (p.getActive()) 
			{
				notdone = false;
			}
		}
		
		return p;
	}


	public Player nextPlayer()
	{
		m_currPlayer.resetLastDrawn();

		Player p = getNextPlayer();

		return p;
	}
	
	public boolean advanceRound()
	{
		// if for some reason we don't have a current player, bail out
		if (m_currPlayer == null) 
		{
			return false;
		}

		if (m_currPlayer instanceof HumanPlayer)
		{
			promptUser(getString(R.string.msg_your_play), false);
		}
		
		m_currPlayer.startTurn();

		if (m_stopping)
		{
			return false;
		}
		
		if ((m_currPlayer.getHand()).hasValidCards(this)
				&& !(m_currPlayer.getWantsToPass())) 
		{
			if (m_currPlayer.getWantsToPlayCard()) 
			{
				m_prevCard = m_currCard;
				m_currCard = m_currPlayer.getPlayingCard();
				m_currPlayer.getHand().removeCard(m_currCard);
					
				logCardPlay(m_currPlayer, m_currCard);

				m_cardsPlayed++;

				m_currCard.setFaceUp(true);
				m_discardPile.addCard(m_currCard);
				
				m_numCardsPlayed++;

				redrawTable();

				m_currColor = m_currCard.getColor();
				if (m_currColor == Card.COLOR_WILD) 
				{
					m_currColor = m_currPlayer.chooseColor();
					
					if (m_stopping)
					{
						return false;
					}
					
					String msg = String.format(getString (R.string.msg_color_chosen), seatToString(m_currPlayer.getSeat()), colorToString(m_currColor));
					redrawTable ();
					Log.d("HDU", msg);
					//PromptUser(msg);
				}

				handleSpecialCards();
				if (m_stopping)
				{
					return false;
				}

				// if previous player set us up, and we did not throw something
				// that would negate the penalty, then we get penalized now
				if ((m_penalty.getType() != Penalty.PENTYPE_NONE)
					&& ((m_penalty.getVictim() == m_currPlayer)
							|| m_penalty.getSecondaryVictim() == m_currPlayer))
				{
					assessPenalty();
				}
				
				// go to next player
				if (m_nextPlayerPreset != null) 
				{
					m_currPlayer = m_nextPlayerPreset;
				}
				else 
				{
					m_currPlayer = nextPlayer();
				}
				

				// if we just threw something that set up the next player, and he has
				// no defender, hit him now
				if (m_penalty.getType() != Penalty.PENTYPE_NONE) 
				{
					if (m_go.getStandardRules()) 
					{
						assessPenalty();
					}
					else 
					{
						int ndef = checkForDefender(m_currPlayer.getHand());
						if (ndef > 0) 
						{
							if (m_currPlayer instanceof HumanPlayer)
							{
								if (ndef == 1)
								{
									promptUser(getString (R.string.msg_you_have_defender));
								}
								else
								{
									promptUser (String.format(getString (R.string.msg_you_have_defenders), ndef));
								}
							}
						}
						else 
						{
							assessPenalty();
						}
					}
				}

				redrawTable();
			}
			
			else if (m_currPlayer.getWantsToDraw() && (m_currPlayer.getLastDrawn() == null))
			{
				m_currPlayer.drawCard();
				redrawTable ();

				if (m_currPlayer instanceof HumanPlayer)
				{
					String msg = String.format(getString (R.string.msg_player_draws_specific_card), seatToString(m_currPlayer.getSeat()), cardToString(m_currPlayer.getLastDrawn()));
					Log.d("HDU", msg);
					promptUser(msg);
				}				
			}
			
			else if (m_currPlayer.getWantsToPass() && (m_currPlayer.getLastDrawn() != null)) 
			{
				if (m_penalty.getType() != Penalty.PENTYPE_NONE) 
				{
					assessPenalty();
				}
				
				m_currPlayer = nextPlayer();
				redrawTable ();
			}
		}

		else 
		{
			if (m_penalty.getType() != Penalty.PENTYPE_NONE) 
			{
				assessPenalty();
			}
			
			else
			{
				if (m_currPlayer.getLastDrawn() != null)
				{
					m_currPlayer = nextPlayer();
				}
				else
				{
					m_currPlayer.drawCard();
		
					sortHand (m_currPlayer.getHand());
					redrawTable();
		
					String msg = String.format(getString (R.string.msg_player_draws_card), seatToString(m_currPlayer.getSeat()));
					Log.d("HDU", msg);
					if (m_currPlayer.getSeat() != SEAT_SOUTH) 
					{
						promptUser (msg);
					}
		
					if (!(m_currPlayer.getHand()).hasValidCards(this))
					{
						m_currPlayer = nextPlayer();
					}
				}
			}
			redrawTable ();
		}

		for (int i = 0; i < 4; i++) 
		{
			Hand h = (m_players[i]).getHand();

			boolean hasAllBastardCards = false;
			if (checkForAllBastardCards(h)) {
				gotAllBastardCards (m_players[i]);
				hasAllBastardCards = true;
			}

			// check for a winner
			if (hasAllBastardCards || (h.getNumCards() == 0)) 
			{
				if (m_penalty.getType() == Penalty.PENTYPE_NONE) 
				{
					finishRound(m_players[i]);
					return false;
				}
			}
		}
		
		return true;
	}

	boolean getLastCardCheckedIsDefender ()
	{
		return m_lastCardCheckedIsDefender;
	}
	
	boolean checkCard(Hand h, Card c, boolean interactive)
	{
		if (!(h.isInHand(c))) return false;

		int value = m_currCard.getValue();
		int id = m_currCard.getID();

		int cvalue = c.getValue();
		int cid = c.getID();

		boolean bHasMatch = h.hasColorMatch (m_currColor);

		m_lastCardCheckedIsDefender = true;

		if (m_penalty.getType() != Penalty.PENTYPE_NONE)
		{
			int tid = m_penalty.getOrigCard().getID();
			int pvalue = m_penalty.getOrigCard().getValue();

			// if the penalty's original card is not the current
			// card, we're dealing with defenders.  At this point, we
			// can only throw defenders (we can't stack more)
			boolean defenderAlreadyThrown = (m_penalty.getOrigCard() != m_currCard);

			// can play aids, fuckyou, and holydefender on various cards
			if (((cid == Card.ID_BLUE_0_FUCKYOU) 
				|| (cid == Card.ID_RED_0_HD)
				|| (cid == Card.ID_GREEN_3_AIDS))
				&& ((pvalue == Card.VAL_WILD_DRAWFOUR) 
				   || (tid == Card.ID_GREEN_0_QUITTER)
				   || (tid == Card.ID_RED_2_GLASNOST)))
			{
				return true;
			}

			// assuming we're not dealing with a defender on top of the
			// draw four, we can stack drawfours (except on the harvester
			// of sorrows and mystery)
			if (!(m_go.getStandardRules())) {
				if (!defenderAlreadyThrown
					&& (pvalue == Card.VAL_WILD_DRAWFOUR) 
					&& (tid != Card.ID_WILD_HOS) && (tid != Card.ID_WILD_MYSTERY)) {
					if (cvalue == Card.VAL_WILD_DRAWFOUR) 
					{
						return true;
					}
				}
			}

			// magic 5 is a defender against the hot death wild card only
			// (although it can be played on any card)
			if ((tid == Card.ID_WILD_HD) && (cid == Card.ID_RED_5_MAGIC)) 
			{
				return true;
			}

			return false;
		}
		
		m_lastCardCheckedIsDefender = false;

		// if player holds 69, he can throw 6s on 9s and vice-versa
		if (((cvalue == 6) && (value == 9)) || ((cvalue == 9) && (value == 6))) 
		{
			for (int i = 0; i < h.getNumCards(); i++) 
			{
				Card tc = h.getCard(i);
				if (tc.getID() == Card.ID_YELLOW_69) 
				{
					return true;
				}
			}
		}

		if (cid == Card.ID_YELLOW_0_SHITTER) 
		{
			if ((id == Card.ID_RED_0_HD) 
			   || (id == Card.ID_RED_5_MAGIC)
			   || (h.getNumCards() == 1))
			{				
				return true;
			}
			else 
			{
				return false;
			}
		}

		// 69 can be played on 6 or 9; likewise, 6 or 9 can be played on 69
		if ((id == Card.ID_YELLOW_69) && ((cvalue == 6) || (cvalue == 9))) return true;
		if ((cid == Card.ID_YELLOW_69) && ((value == 6) || (value == 9))) return true;

		// can play magic red 5 on any card
		if (cid == Card.ID_RED_5_MAGIC) return true;

		// the variants of D, S, and R
		if ((value == Card.VAL_D) && (cvalue == Card.VAL_D_SPREAD)) return true;
		if ((value == Card.VAL_D_SPREAD) && (cvalue == Card.VAL_D)) return true;
		if ((value == Card.VAL_R) && (cvalue == Card.VAL_R_SKIP)) return true;
		if ((value == Card.VAL_R_SKIP) && (cvalue == Card.VAL_R)) return true;
		if ((value == Card.VAL_S) && (cvalue == Card.VAL_S_DOUBLE)) return true;
		if ((value == Card.VAL_S_DOUBLE) && (cvalue == Card.VAL_S)) return true;

		if (m_go.getStandardRules()) 
		{
			// cannot play wild draw four if you've got a matching card
			if (bHasMatch && (c.getValue() == Card.VAL_WILD_DRAWFOUR)) 
			{
				return false;
			}
		}

		// cards of same color, wild cards, or cards of equal value
		if ((c.getColor() == m_currColor) || (c.getColor() == Card.COLOR_WILD)
				|| (cvalue == value)) {
			return true;
		}

		return false;
	}


	public void finishRound(Player p)
	{
		m_fastForward = false;
		showFastForwardButton(false);

		m_dealer = p;

		calculateScore(p);
		m_roundComplete = true;

		for (int i = 0; i < 4; i++) 
		{
			m_players[i].setActive(true);
			((m_players[i]).getHand()).setFaceUp(true);
			sortHand ((m_players[i]).getHand());
		}

		redrawTable();
		String msg = String.format (getString(R.string.msg_declare_round_winner), seatToString(p.getSeat()));
		promptUser(msg);

		int minScore = 1000000;
		int minPlayer = 0;
		int gameEndScore = (m_go.getStandardRules()) ? 500 : 1000;

		for (int i = 0; i < 4; i++) 
		{
			if (m_players[i].getTotalScore() < minScore) 
			{
				minScore = m_players[i].getTotalScore();
				minPlayer = i;
			}
			if (m_players[i].getTotalScore() >= gameEndScore) 
			{
				m_gameOver = true;
			}
		}
		
		m_snapshot = this.toJSON();

		if (m_gameOver) 
		{
			m_winner = m_players[minPlayer].getSeat();
			redrawTable();
		}

	}

	public boolean roundIsActive ()
	{
		if (m_waitingToStartRound)
		{
			return false;
		}
		if (m_gameOver)
		{
			return false;
		}
		
		return true;
	}
	
	private void waitForNextRound ()
	{
		String msg = getString(R.string.msg_tap_draw_pile);
		promptUser(msg);

		m_waitingToStartRound = true;
		while (m_waitingToStartRound)
		{
			try
			{
				Thread.sleep(100);
			}
			catch (InterruptedException e)
			{
				
			}
		}
	}
	
	public void drawPileTapped ()
	{
		if (m_waitingToStartRound)
		{
			m_waitingToStartRound = false;
		}
		
		if (!(m_currPlayer instanceof HumanPlayer))
		{
			return;
		}
		
		((HumanPlayer)m_currPlayer).turnDecisionDrawCard();
	}
	
	
	public void discardPileTapped ()
	{
		if (m_waitingToStartRound)
		{
			m_waitingToStartRound = false;
		}
	}

	private void calculateScore(Player pWinner)
	{
		int[] newscore = new int[4];
		int i;
		
		
		int maxscore = 0;
		for (i = 0; i < 4; i++) 
		{
			Hand h = (m_players[i]).getHand();

			if (checkForAllBastardCards(h))
			{
				newscore[i] = 0;
			}
			else
			{
				newscore[i] = h.calculateValue(true);
			}

			if (newscore[i] > maxscore) 
			{
				maxscore = newscore[i];
			}

		}

		// if a player has the shitter, he gets the worst score of all hands
		for (i = 0; i < 4; i++) 
		{
			Hand h = (m_players[i]).getHand();
			if (checkForAllBastardCards(h))
			{
				// unless he's got all 4 bastard cards, in which case, he 
				// gets 0 points
			}
			else
			{
				for (int j = 0; j < h.getNumCards(); j++) 
				{
					Card c = h.getCard(j);
	
					if (c.getID() == Card.ID_YELLOW_0_SHITTER) 
					{
						newscore[i] = maxscore;
					}
				}
			}

			(m_players[i]).setLastScore(newscore[i]);
			
			if (m_players[i] == pWinner)
			{
				(m_players[i]).setLastVirusPenalty(0);
			}
			else
			{
				(m_players[i]).setLastVirusPenalty(m_players[i].getVirusPenalty());
			}

			(m_players[i]).setTotalScore ((m_players[i]).getTotalScore()
											+ newscore[i] 
											+ m_players[i].getLastVirusPenalty());
		}

	}



	public void sortHand (Hand h)
	{
		Card[] cards = new Card[MAX_NUM_CARDS];
		int i;
		
		int p = 0;
		Card[] cd = m_deck.getCards();

		if (m_go.getFaceUp())
		{
			for (i = 0; i < m_deck.getNumCards(); i++) 
			{
				Card c = cd[i];
				if (c.getHand() == h) 
				{
					cards[p++] = c;
				}
			}			
		}

		else
		{
			// sort according to deck order, but do faceup cards first
			for (i = 0; i < m_deck.getNumCards(); i++) 
			{
				Card c = cd[i];
				if (c.getHand() == h && c.getFaceUp()) 
				{
					cards[p++] = c;
				}
			}
			
			for (i = 0; i < m_deck.getNumCards(); i++) 
			{
				Card c = cd[i];
				if (c.getHand() == h && !(c.getFaceUp())) 
				{
					cards[p++] = c;
				}
			}
		}

		h.reorderCards(cards);
	}

	
	private void redrawTable ()
	{
		m_ga.runOnUiThread(new Runnable () {
			public void run ()
			{
				m_gt.RedrawTable();
			}
		});
	}
	
	private void showFastForwardButton (final boolean show)
	{
		m_ga.runOnUiThread(new Runnable () {
			public void run ()
			{
				 m_gt.showFastForwardButton(show);
			}
		});
	}

	public void promptForNumCardsToDeal()
	{
		m_ga.runOnUiThread(new Runnable () {
					public void run ()
					{
						m_gt.PromptForNumCardsToDeal();
					}
		});
	}

	public void promptForVictim()
	{
		m_ga.runOnUiThread(new Runnable () {
			public void run ()
			{
				m_gt.PromptForVictim();
			}
		});
	}
	
	public void promptForColor()
	{
		m_ga.runOnUiThread(new Runnable () {
			public void run ()
			{
				m_gt.PromptForColor();
			}
		});
	}


	
	public void humanPlayerPass ()
	{
		if (!(m_currPlayer instanceof HumanPlayer))
		{
			return;
		}
		
		((HumanPlayer)m_currPlayer).turnDecisionPass();
	}
	
	void promptUser(final String msg)
	{
		promptUser(msg, true);
	}

	void promptUser(final String msg, boolean wait)
	{
		if (m_fastForward)
		{
			return;
		}

		m_ga.runOnUiThread(new Runnable () {
			public void run ()
			{
				Log.d("HDU", "[promptUser] prompt: " + msg);
				m_gt.Toast(msg);
			}
		});
		if (wait)
		{
			waitABit ();			
		}
	}


	void promptForDrawCard()
	{
		promptUser (getString(R.string.msg_prompt_draw));
	}



	// this routine looks in the hand for a card 
	// like the Blue Shield
	public boolean checkForShield(Hand h)
	{
		for (int i = 0; i < h.getNumCards(); i++) 
		{
			int id = (h.getCard(i)).getID();
			
			if (id == Card.ID_BLUE_2_SHIELD) 
			{
				(h.getCard(i)).setFaceUp(true);
				return true;
			}
		}

		return false;
	}



	/* check for defenders against stacked draw 4s
	     - Fuck You
		 - Holy Defender
		 - Virus card
		 - Harvester of Sorrows
	*/
	public int checkForDefender(Hand h)
	{
		Card c = m_penalty.getOrigCard();
		int prevID = c.getID();
		int prevVal = c.getValue();

		// if the penalty's original card is not the current
		// card, we're dealing with defenders.  At this point, we
		// can only throw defenders (we can't stack more)
		boolean defenderAlreadyThrown = (c != m_currCard);

		int defenderCount = 0;
		// we can stack on all draw fours except HOS and mystery draw
		if ((prevVal == Card.VAL_WILD_DRAWFOUR) && (prevID != Card.ID_WILD_HOS)) 
		{
			for (int i = 0; i < h.getNumCards(); i++) 
			{
				int id = (h.getCard(i)).getID();
				int val = (h.getCard(i)).getValue();
				
				// all draw fours can stack, except for mystery draw
				if (!defenderAlreadyThrown) {
					if ((val == Card.VAL_WILD_DRAWFOUR) && (prevID != Card.ID_WILD_MYSTERY) && (id != Card.ID_WILD_MYSTERY)) 
					{
						defenderCount++;
					}
				}

				// no stacking on AIDS -- it gets too messy because it would
				// throw the penalty back against the direction of play

				if (id == Card.ID_RED_0_HD) 
				{
					defenderCount++;
				}
				if (id == Card.ID_BLUE_0_FUCKYOU)
				{
					defenderCount++;
				}
				if (id == Card.ID_GREEN_3_AIDS)
				{
					defenderCount++;
				}

				// magic red 5 nulls the hotdeath card
				if ((id == Card.ID_RED_5_MAGIC) && (prevID == Card.ID_WILD_HD)) 
				{
					defenderCount++;
				}
			}
			
		}

		if (m_penalty.getVictim() == m_currPlayer) 
		{
			if (prevID == Card.ID_RED_0_HD) 
			{
			}

			if (prevID == Card.ID_RED_2_GLASNOST) 
			{
				for (int i = 0; i < h.getNumCards(); i++) 
				{
					int id = (h.getCard(i)).getID();
					if ((id == Card.ID_RED_0_HD) 
						|| (id == Card.ID_GREEN_3_AIDS)
						|| (id == Card.ID_BLUE_0_FUCKYOU)) 
					{
						defenderCount++;
					}
				}
			}

			if (prevID == Card.ID_GREEN_0_QUITTER) 
			{
				for (int i = 0; i < h.getNumCards(); i++) 
				{
					int id = (h.getCard(i)).getID();
					if ((id == Card.ID_RED_0_HD) 
						|| (id == Card.ID_GREEN_3_AIDS)
						|| (id == Card.ID_BLUE_0_FUCKYOU)) 
					{
						defenderCount++;
					}
				}
			}

			if (prevID == Card.ID_YELLOW_1_MAD) 
			{
			}
		}

		return defenderCount;
	}


	public boolean checkForAllBastardCards(Hand h)
	{
		int bastardCount = 0;
		for (int i = 0; i < h.getNumCards(); i++) 
		{
			Card c = h.getCard(i);

			int id = c.getID();

			if (id == Card.ID_RED_0_HD) bastardCount++;
			if (id == Card.ID_GREEN_0_QUITTER) bastardCount++;
			if (id == Card.ID_BLUE_0_FUCKYOU) bastardCount++;
			if (id == Card.ID_YELLOW_0_SHITTER) bastardCount++;
		}

		if (bastardCount == 4) 
		{
			return true;
		}
		
		return false;
	}


	public void gotAllBastardCards(Player p)
	{
		String msg;
		msg = String.format (getString(R.string.msg_all_bastard_cards), seatToString(p.getSeat()));
		promptUser (msg);

		Hand h = p.getHand();
		int numcards = h.getNumCards();
		for (int i = 0; i < numcards; i++) 
		{
			Card c = h.getCard(i);
			c.setFaceUp(true);
		}

		redrawTable();
	}



	public int getActivePlayerCount() 
	{
		int count = 0;
		for (int i = 0; i < 4; i++) 
		{
			if (m_players[i].getActive()) count++;
		}
		return count;
	}

	public void handleSpecialCards()
	{
		int currVal = m_currCard.getValue();
		int currID  = m_currCard.getID();
		m_nextPlayerPreset = null;

		if (currVal == Card.VAL_R) 
		{
			m_direction = (m_direction == DIR_CLOCKWISE) ? DIR_CCLOCKWISE : DIR_CLOCKWISE;
			Log.d("HDU", "direction change: " + directionToString(m_direction));

			if (getActivePlayerCount() == 2) 
			{
				m_currPlayer = nextPlayer();
			}
		}

		if (currVal == Card.VAL_R_SKIP) 
		{
			m_direction = (m_direction == DIR_CLOCKWISE) ? DIR_CCLOCKWISE : DIR_CLOCKWISE;
			Log.d("HDU", "direction change: " + directionToString(m_direction));

			m_currPlayer = nextPlayer();
		}

		if ((currVal == Card.VAL_S)
			|| (currVal == Card.VAL_S_DOUBLE)) 
		{
			m_currPlayer = nextPlayer();
		}

		// double skip (only if more than 2 players left in game)
		if ((currVal == Card.VAL_S_DOUBLE) && (getActivePlayerCount() > 2)) 
		{
			m_currPlayer = nextPlayer();
		}

		if (currVal == Card.VAL_D) 
		{
			Player victim = nextPlayer();

			forceDraw(victim, 2);
			if (!(m_go.getStandardRules()))
			{
				m_currPlayer = nextPlayer();
			}
		}

		// spreaders
		if (currVal == Card.VAL_D_SPREAD) 
		{
			// we're going to manipulate the m_currPlayer just so that 
			// the drawing engine will point at each player as he draws
			Player realCurrPlayer = m_currPlayer;
			
			// by default, the player who threw the spreader will play
			// again, unless somebody's got the shield
			Player victim = m_currPlayer;
			m_nextPlayerPreset = m_currPlayer;
			for (int i = 0; i < 3; i++) 
			{
				victim = getNextPlayer(victim);

				// once we've gone around the table, bail out
				if (victim == realCurrPlayer) 
				{
					break;
				}
				if (!victim.getActive())
				{
					continue;
				}

				if (checkForShield(victim.getHand())) 
				{
					// somebody's got the shield
					m_nextPlayerPreset = victim;

					String msg = String.format (getString(R.string.msg_has_blue_shield), seatToString(victim.getSeat()));
					promptUser (msg);

					forceDraw(m_currPlayer, 2);

					continue;
				}

				forceDraw(victim, 2);
			}
			m_currPlayer = realCurrPlayer;
		}

		// check the wild draw fours
		if (currID == Card.ID_WILD_DRAWFOUR) 
		{
			m_penalty.addCards (m_currCard, 4, m_currPlayer, getNextPlayer());
			String msg;
			msg = (m_penalty.getNumCards() > 4)
				? String.format (getString(R.string.msg_penalty_stacked_drawfour), 
						seatToString(m_penalty.getGeneratingPlayer().getSeat()), m_penalty.getNumCards(), seatToString(m_penalty.getVictim().getSeat()))
				: String.format (getString(R.string.msg_penalty_first_drawfour),
						seatToString(m_penalty.getGeneratingPlayer().getSeat()), m_penalty.getNumCards(), seatToString(m_penalty.getVictim().getSeat()));
			promptUser (msg);
		}

		else if (currID == Card.ID_WILD_HD) 
		{
			m_penalty.addCards (m_currCard, 8, m_currPlayer, getNextPlayer());
			String msg;
			msg = (m_penalty.getNumCards() > 8)
				? String.format (getString(R.string.msg_penalty_stacked_wild_hd), 
						seatToString(m_penalty.getGeneratingPlayer().getSeat()), m_penalty.getNumCards(), seatToString(m_penalty.getVictim().getSeat()))
				: String.format (getString(R.string.msg_penalty_first_wild_hd),
						seatToString(m_penalty.getGeneratingPlayer().getSeat()), m_penalty.getNumCards(), seatToString(m_penalty.getVictim().getSeat()));
			promptUser (msg);
		}

		else if (currID == Card.ID_WILD_DB) 
		{
			Player p = m_currPlayer;

			if (getActivePlayerCount() > 2) 
			{
				m_currPlayer = nextPlayer();
			}

			m_penalty.addCards (m_currCard, 4, p, getNextPlayer());
			String msg;
			msg = (m_penalty.getNumCards() > 4)
				? String.format (getString(R.string.msg_penalty_stacked_wild_db), 
						seatToString(m_penalty.getGeneratingPlayer().getSeat()), m_penalty.getNumCards(), seatToString(m_penalty.getVictim().getSeat()))
				: String.format (getString(R.string.msg_penalty_first_wild_db),
						seatToString(m_penalty.getGeneratingPlayer().getSeat()), m_penalty.getNumCards(), seatToString(m_penalty.getVictim().getSeat()));
			promptUser (msg);
		}

		else if (currID == Card.ID_WILD_HOS) 
		{
			m_penalty.addCards (m_currCard, 4, m_currPlayer, getNextPlayer());
			String msg;
			msg = (m_penalty.getNumCards() > 4)
				? String.format (getString(R.string.msg_penalty_stacked_wild_hos), 
						seatToString(m_penalty.getGeneratingPlayer().getSeat()), m_penalty.getNumCards(), seatToString(m_penalty.getVictim().getSeat()))
				: String.format (getString(R.string.msg_penalty_first_wild_hos),
						seatToString(m_penalty.getGeneratingPlayer().getSeat()), m_penalty.getNumCards(), seatToString(m_penalty.getVictim().getSeat()));
			promptUser (msg);
		}

		else if (currID == Card.ID_WILD_MYSTERY) 
		{
			int prevVal = m_prevCard.getValue();
			int prevID  = m_prevCard.getID();

			int prevPenalty = m_penalty.getNumCards();
			
			if (prevID == Card.ID_YELLOW_69) 
			{
				m_penalty.addCards(m_currCard, 69, m_currPlayer, getNextPlayer());
			}
			else if (prevVal > 0 && prevVal < 10)
			{
				m_penalty.addCards(m_currCard, prevVal, m_currPlayer, getNextPlayer());
			}
			else
			{
				// mystery thrown on top of a non-numbered card -- just the same as a wild card
				String msg = String.format (getString(R.string.msg_penalty_null_wild_mystery), 
						seatToString(m_currPlayer.getSeat()));
				promptUser (msg);
				return;
			}			
			String msg;
			msg = (prevPenalty > 0)
				? String.format (getString(R.string.msg_penalty_stacked_wild_mystery), 
						seatToString(m_penalty.getGeneratingPlayer().getSeat()), m_penalty.getNumCards(), seatToString(m_penalty.getVictim().getSeat()))
				: String.format (getString(R.string.msg_penalty_first_wild_mystery),
						seatToString(m_penalty.getGeneratingPlayer().getSeat()), m_penalty.getNumCards(), seatToString(m_penalty.getVictim().getSeat()));
			promptUser (msg);
		}

		// check other special cards
		if ((currID == Card.ID_RED_0_HD) && (m_penalty.getVictim() == m_currPlayer)) 
		{
			m_penalty.setGeneratingPlayer(m_currPlayer);
			m_penalty.setVictim(getNextPlayer());

			String msg = String.format (getString(R.string.msg_holy_defender), seatToString(m_penalty.getVictim().getSeat()));
			promptUser (msg);
		}

		if (currID == Card.ID_RED_2_GLASNOST) 
		{
			m_currPlayer.chooseVictim();
			if (m_stopping)
			{
				return;
			}
			int victim = m_currPlayer.getChosenVictim();
			// we'll set the victim after prompting the user for it
			m_penalty.setFaceup(m_currCard, m_currPlayer, m_players[victim - 1]);
		}

		// if the magic red 5 is played on the hot death wild, it nulls it
		if ((currID == Card.ID_RED_5_MAGIC) && (m_prevCard.getID() == Card.ID_WILD_HD)
			 && (m_penalty.getVictim() == m_currPlayer)) 
		{
			m_penalty.reset();

			String msg = getString(R.string.msg_magic_5);
			promptUser (msg);
		}

		if (currID == Card.ID_GREEN_0_QUITTER) 
		{
			if (getActivePlayerCount() > 2) 
			{
				m_penalty.setEject(m_currCard, m_currPlayer, getNextPlayer());
			}
		}

		if ((currID == Card.ID_GREEN_3_AIDS)
			&& (m_penalty.getVictim() == m_currPlayer)) 
		{
			Player g = m_penalty.getGeneratingPlayer();
			m_penalty.setVictim(g);
			m_penalty.setGeneratingPlayer(m_currPlayer);
			m_penalty.setSecondaryVictim(m_currPlayer);
			
			String msg = String.format(getString(R.string.msg_sharing_penalty), seatToString(m_penalty.getVictim().getSeat()));
			promptUser (msg);
		}

		if ((currID == Card.ID_BLUE_0_FUCKYOU)
			&& ((m_penalty.getVictim() == m_currPlayer) || (m_penalty.getSecondaryVictim() == m_currPlayer))) 
		{
			m_penalty.setVictim(m_penalty.getGeneratingPlayer());
			m_penalty.setSecondaryVictim(null);
			m_penalty.setGeneratingPlayer(m_currPlayer);

			m_direction = (m_direction == DIR_CLOCKWISE) ? DIR_CCLOCKWISE : DIR_CLOCKWISE;
			redrawTable();

			String msg = String.format(getString(R.string.msg_sending_penalty), seatToString(m_penalty.getVictim().getSeat()));

			Log.d("HDU", "direction change: " + directionToString(m_direction));
			promptUser (msg);
		}

		if (currID == Card.ID_YELLOW_1_MAD) 
		{
			if (getActivePlayerCount() > 3) 
			{
				m_currPlayer.chooseVictim();
				if (m_stopping)
				{
					return;
				}
				int victim = m_currPlayer.getChosenVictim();

				// we'll set the victim after prompting the user for it
				m_penalty.setEject(m_currCard, m_currPlayer, m_players[victim - 1]);
				m_penalty.setSecondaryVictim(m_currPlayer);
			}
		}
	}

	public void assessPenalty()
	{
		int i;
		
		if (m_penalty.getType() == Penalty.PENTYPE_NONE)
		{
			return;
		}
			
		Player pVictim = m_penalty.getVictim();
		Player pVictim2 = m_penalty.getSecondaryVictim();

		if (pVictim == null)
		{
			return;
		}

		Hand h = pVictim.getHand();
		String msg;

		if (m_penalty.getType() == Penalty.PENTYPE_CARD) 
		{
			int numcards = m_penalty.getNumCards();
			if (pVictim2 != null)
			{
				// divide by 2 (and round up)
				numcards = (numcards + 1) / 2;
			}

			// check for the luck of the irish card (if numcards = 0, there's no point)
			if (numcards > 0) 
			{
				for (i = 0; i < h.getNumCards(); i++) 
				{
					int id = (h.getCard(i)).getID();
					if (id == Card.ID_GREEN_4_IRISH) 
					{
						numcards--;
						h.getCard(i).setFaceUp(true);

						String msg_player = getString(R.string.msg_luck_of_irish);
						promptUser (msg_player);

						break;
					}
				}
			}

			forceDraw(pVictim, numcards);
			m_currPlayer = pVictim;

			if (!(m_go.getStandardRules()))
			{
				m_currPlayer = nextPlayer();
			}
			
			if (pVictim2 != null) 
			{
				h = pVictim2.getHand();
				numcards = (m_penalty.getNumCards() + 1) / 2;
				// check for the luck of the irish card
				for (i = 0; i < h.getNumCards(); i++) 
				{
					int id = (h.getCard(i)).getID();
					if (id == Card.ID_GREEN_4_IRISH) 
					{
						numcards--;
						(h.getCard(i)).setFaceUp(true);
						break;
					}
				}
				forceDraw(pVictim2, numcards);
			}
		}
		else if (m_penalty.getType() == Penalty.PENTYPE_FACEUP) 
		{
			h.setFaceUp(true);

			if (m_players[SEAT_SOUTH - 1] instanceof HumanPlayer) 
			{
				msg = String.format (getString(R.string.msg_player_faceup), seatToString(pVictim.getSeat()));
				promptUser (msg);
			}

			if (pVictim2 != null) 
			{
				h = pVictim2.getHand();
				for (i = 0; i < h.getNumCards(); i++) 
				{
					Card c = h.getCard(i);
					c.setFaceUp(true);
				}
				if (m_players[SEAT_SOUTH - 1] instanceof HumanPlayer) 
				{
					msg = String.format (getString(R.string.msg_player_faceup), seatToString(pVictim2.getSeat()));
					promptUser (msg);
				}
			}

		}
		else if (m_penalty.getType() == Penalty.PENTYPE_EJECT) 
		{
			pVictim.setActive(false);

			msg = String.format (getString(R.string.msg_player_ejected), seatToString (pVictim.getSeat()));
			redrawTable();
			promptUser (msg);

			if (pVictim == m_players[SEAT_SOUTH - 1])
			{
				showFastForwardButton (true);
			}

			if (pVictim2 != null) 
			{
				pVictim2.setActive(false);

				msg = String.format (getString(R.string.msg_player_ejected), seatToString (pVictim2.getSeat()));
				redrawTable();
				promptUser (msg);

				if (pVictim2 == m_players[SEAT_SOUTH - 1])
				{
					showFastForwardButton (true);
				}
			}

			if ((m_currPlayer == pVictim) || (m_currPlayer == pVictim2)) 
			{
				m_currPlayer = nextPlayer();
			}

			// FIXME!!! My personal rule -- if we end up with only
			// one player left, that player wins
			if (getActivePlayerCount() == 1) 
			{
				Hand th = m_currPlayer.getHand();
				th.reset();
				m_penalty.reset();
				Player pWinner = null;
				for (i = 0; i < 4; i++) 
				{
					if (m_players[i].getActive()) 
					{
						pWinner = m_players[i];
					}
				}
				finishRound(pWinner);
			}
		}

		m_penalty.reset();
	}

	// gets the pause delay in milliseconds
	public int getDelay ()
	{
		if (m_fastForward)
		{
			return 0;
		}
		
		if (!m_players[SEAT_SOUTH - 1].getActive())
		{
			return 250;
		}

		int delay = m_go.getPauseLength();
		
		switch (delay)
		{
		case 0:
			return 700;
		case 1:
			return 1200;
		case 2:
			return 1700;
		case 3:
			return 2900;
		default:
			return 4000;
		}
	}

	public void waitABit()
	{
		int delay = this.getDelay();
		
		if (delay == 0)
		{
			return;
		}
		
		try
		{
			Thread.sleep (delay);
		}
		catch (InterruptedException e)
		{
			// do nothing for now...
		}
	}

	void forceDraw(Player p, int numcards)
	{	
		// manipulate the m_currPlayer so that the drawing engine will
		// point at the player who is drawing; we'll put it back when done.
		Player realCurrPlayer = m_currPlayer;
		m_currPlayer = p;
		redrawTable();

		String msg = String.format(getString(R.string.msg_player_drawing), seatToString(p.getSeat()), numcards);
		Log.d("HDU", msg);
		promptUser (msg);

		boolean notEnoughCards = false;
		m_forceDrawing = true;
		for (int i = 0; i < numcards; i++) 
		{
			Card c = drawCard();

			if (c != null) 
			{
				p.addCardToHand(c);
				if ((p.getSeat() == SEAT_SOUTH) || m_go.getFaceUp()) 
				{
					c.setFaceUp(true);
				}
			}
			else
			{
				notEnoughCards = true;
				break;
			}
		}
		m_forceDrawing = false;
		
		if (notEnoughCards)
		{
			promptUser (getString(R.string.msg_discard_empty));
		}
		
		sortHand (p.getHand());
		redrawTable();
		m_currPlayer = realCurrPlayer;
	}
	
	private void logCardPlay (Player p, Card c)
	{
		Log.d("HDU", seatToString (p.getSeat()) + " plays " + cardToString(c));
	}
	
	public String cardToString (Card c)
	{
		return c.toString(m_gt.getContext(), m_go.getFamilyFriendly());
	}
	
	private String directionToString (int dir)
	{
		if (dir == Game.DIR_CLOCKWISE)
		{
			return getString(R.string.direction_clockwise);
		}
		
		return getString(R.string.direction_counterclockwise);
	}
	
	private String colorToString (int c)
	{
		switch (c)
		{
		case Card.COLOR_BLUE:
			return getString(R.string.cardcolor_blue);
		case Card.COLOR_GREEN:
			return getString(R.string.cardcolor_green);
		case Card.COLOR_RED:
			return getString(R.string.cardcolor_red);
		case Card.COLOR_YELLOW:
			return getString(R.string.cardcolor_yellow);
		}
		
		return "";
	}
	
	private String seatToString (int seat)
	{
		switch (seat)
		{
		case Game.SEAT_NORTH:
			return getString(R.string.seat_north);
		case Game.SEAT_EAST:
			return getString(R.string.seat_east);
		case Game.SEAT_SOUTH:
			return getString(R.string.seat_south);
		case Game.SEAT_WEST:
			return getString(R.string.seat_west);
		}
		
		return "";
	}
		

	/**
	 * Convenience function; lets us retrieve resource strings with minimal syntax;
	 * also lets the player objects retrieve strings without knowledge of the
	 * Activity/View/Context.
	 * @param resid
	 * @return
	 */
	public String getString(int resid)
	{
		return m_gt.getContext().getString(resid);
	}
	

}
