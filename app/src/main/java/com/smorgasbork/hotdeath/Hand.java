package com.smorgasbork.hotdeath;

import java.util.Random;
import org.json.*;

public class Hand {
	private Player	m_player;
	private Card[]	m_cards;
	private int		m_numCards;
	
	Card[] getCards() { return m_cards; }
	int getNumCards() { return m_numCards; }
	
	Card getCard(int i)
	{ 
		if (i < 0 || i > m_numCards - 1)
		{
			return null;
		}
		return m_cards[i]; 
	}
	

	public Hand(Player p)
	{
		m_player = p;
		m_numCards = 0;
		m_cards = new Card[Game.MAX_NUM_CARDS];
	}

	public void reset()
	{
		for (int i = 0; i < m_numCards; i++) 
		{
			m_cards[i].setHand(null);
		}
		m_cards = new Card[Game.MAX_NUM_CARDS];
		m_numCards = 0;
	}


	public void addCard (Card c)
	{
		m_cards[m_numCards++] = c;
		c.setHand(this);
	}


	public Card swapCard (Card c)
	{
		Random rgen = new Random();
		int cnum = rgen.nextInt (m_numCards);
		
		Card oc = m_cards[cnum];
		
		m_cards[cnum] = c;
		c.setHand(this);
		oc.setHand(null);
		return oc;
	}

	public void setFaceUp(boolean f)
	{
		for (int i = 0; i < m_numCards; i++) 
		{
			m_cards[i].setFaceUp(f);
		}
	}


	public void removeCard (Card c)
	{
		boolean bRemoving = false;

		for (int i = 0; i < m_numCards; i++) 
		{
			if (m_cards[i] == c)
			{
				bRemoving = true;
			}
			if (bRemoving) 
			{
				Card cnew = (i == m_numCards - 1) ? null : m_cards[i+1];
				m_cards[i] = cnew;
			}
		}
		if (bRemoving)
		{
			c.setHand(null);
			m_cards[m_numCards - 1] = null;
			m_numCards--;
		}
	}


	public boolean isInHand(Card c)
	{
		for (int i = 0; i < m_numCards; i++) 
		{
			if (m_cards[i] == c) return true;
		}
		return false;
	}

	public boolean hasColorMatch(int color)
	{
		for (int i = 0; i < m_numCards; i++) 
		{
			Card c = m_cards[i];
			if (color == c.getColor())
			{
				return true;
			}
		}
		return false;
	}


	public boolean isInHand(int color, int val)
	{
		for (int i = 0; i < m_numCards; i++) 
		{
			if (((m_cards[i]).getColor() == color)
				 && ((m_cards[i]).getValue() == val)) 
			{
				return true;
			}
		}
		return false;
	}


	public int countSuit(int color)
	{
		int total = 0;
		for (int i = 0; i < m_numCards; i++) 
		{
			if ((m_cards[i]).getColor() == color) 
			{
				total++;
			}
		}
		return total;
	}


	public Card getLowestCard(int color)
	{
		Card retval = null;

		int lowestval = 1000000;
		for (int i = 0; i < m_numCards; i++) 
		{
			if ((color > 0) && (m_cards[i].getColor() != color))
			{
				continue;
			}
			
			int val = (m_cards[i]).getValue();
			if (val < lowestval) 
			{
				retval = m_cards[i];
				lowestval = val;
			}
		}

		return retval;
	}


	public Card getHighestCard(int color)
	{
		Card retval = null;

		int highestval = -1;
		for (int i = 0; i < m_numCards; i++) 
		{
			if ((color > 0) && (m_cards[i].getColor() != color))
			{
				continue;
			}

			int val = (m_cards[i]).getValue();
			if (val > highestval) 
			{
				retval = m_cards[i];
				highestval = val;
			}
		}

		return retval;
	}


	public Card getHighestNonTrump(int color)
	{
		Card retval = null;

		int highestval = -1;
		for (int i = 0; i < m_numCards; i++) 
		{
			if ((m_cards[i]).getColor() == color) 
			{
				continue;
			}

			int val = (m_cards[i]).getValue();
			if (val > highestval) 
			{
				retval = m_cards[i];
				highestval = val;
			}
		}

		return retval;
	}


	public void replaceCard(Card c, Card newc)
	{
		for (int i = 0; i < m_numCards; i++) 
		{
			if (m_cards[i] == c) 
			{
				m_cards[i] = newc;
			}
		}
	}


	public void reorderCards(Card[] cards) 
	{
		for (int i = 0; i < m_numCards; i++) 
		{
			m_cards[i] = cards[i];
		}
	}


	public boolean hasValidCards(Game g)
	{
		for (int i = 0; i < m_numCards; i++) 
		{
			if (g.checkCard(this, m_cards[i], false)) 
			{
				return true;
			}
		}
		return false;
	}

	public int calculateValue()
	{
		return this.calculateValue(false);
	}
	
	public int calculateValue(boolean isfinal)
	{
		return this.calculateValue(isfinal, null);
	}
	
	/*
	This routine was overhauled for version 1.01.  It now has all sorts of
	special card knowledge, which I had tried to avoid.  The upshot is that
	the Cards have all sorts of variables that are useless now (like
	multipliers, etc.)

	To add new types of cards, you'd have to fix this routine, the CheckCard(),
	CheckForDefender(), and HandleSpecialCards() routines in Game.cpp.

	*/
	public int calculateValue(boolean isfinal, Card withoutCard)
	{
		int highest = 0;
		int highestNum = 0;
		int total = 0;
		int i;
		
		Card cFuckYou = null;
		Card cShitter = null;
		Card cQuitter = null;
		Card cMystery = null;
		Card cBlueShield = null;
		Card cSixtyNine = null;
		Card cMagic5 = null;
		Card cHolyDefender = null;

		// Step 1.  Does the prior code check for an All-Bastards combination on the
		// final forced-draw (draw forced when a player goes out by laying a "draw" card)? 
		// If not, check for this combination.  If true, then no further processing necessary.

		// THIS IS DONE IN Game.cpp; at the end of each AdvanceRound, we check to see
		// if anybody has the bastard cards.  This should also kick in at the end of
		// the game on a forced draw.

		// Step 2.  Check for 1000 point combination.  If true, then these
		// cards need to be skipped for all future processing, and set "total = 1000"
		for (i = 0; i < m_numCards; i++) 
		{
			Card c = m_cards[i];
			if (c == withoutCard)
			{
				continue;
			}
			
			if (c.getID() == Card.ID_BLUE_0_FUCKYOU) 
			{
				cFuckYou = c;
			}
			if (c.getID() == Card.ID_YELLOW_0_SHITTER)
			{
				cShitter = c;
			}
			if (c.getID() == Card.ID_GREEN_0_QUITTER)
			{
				cQuitter = c;
			}
		}

		boolean bFullMonty = false;
		if ((cFuckYou != null) && (cQuitter != null)) 
		{
			bFullMonty = true;
			total = 1000;

			// give the cards a value of 500 so that computer players will
			// want to unload the cards ASAP
			cFuckYou.setCurrentValue(500);
			cQuitter.setCurrentValue(500);
		}
		else 
		{
			// if we've got the Shitter but not the others, we give a pseudo
			// value of 150 to the card so that computer players will want
			// to unload it ASAP
			if (cShitter != null)
			{
				cShitter.setCurrentValue(150);
			}
		}

		// Step 3.  This is the Big Step...  Process all "fixed value" cards.  This 
		// step includes ALL CARDS except Mystery Wild, Blue Shield, 69, Magic 5, 
		// F.U., and Holy Def.  It SHOULD include the shitter and virus VALUES, but
		// not their PENALTIES (you'll need to set a variable to indicate the shitter's
		// presence, and/or also increment a "number of virus infections" variable, for
		// use in later steps).  During this step, highest value card and highest numerical
		// card variables should be determined, .  Set "total += [each card's value]"
		for (i = 0; i < m_numCards; i++) 
		{
			Card c = m_cards[i];
			
			if (c == withoutCard)
			{
				continue;
			}
			
			int id = c.getID();
			// if we're getting the 1000 point penalty, we don't need to
			// look at these three cards again
			if (bFullMonty 
				&& ((id == Card.ID_BLUE_0_FUCKYOU)
				    || (id == Card.ID_YELLOW_0_SHITTER)
					|| (id == Card.ID_GREEN_0_QUITTER))) 
			{
				continue;
			}

			// increment the virus penalty if this is the end of the round.
			if (isfinal && (id == Card.ID_GREEN_3_AIDS)) 
			{
				m_player.setVirusPenalty(m_player.getVirusPenalty() + 10);
			}

			// don't assess points for these cards yet
			if (id == Card.ID_WILD_MYSTERY) 
			{
				cMystery = c;
				continue;
			}

			if (id == Card.ID_BLUE_2_SHIELD) 
			{
				cBlueShield = c;
				continue;
			}

			if (id == Card.ID_YELLOW_69) {
				cSixtyNine = c;
				continue;
			}

			if (id == Card.ID_RED_5_MAGIC) 
			{
				cMagic5 = c;
				continue;
			}

			if (id == Card.ID_BLUE_0_FUCKYOU) 
			{
				continue;
			}

			if (id == Card.ID_RED_0_HD) 
			{
				cHolyDefender = c;
				continue;
			}

			int pv = c.getPointValue();
			
			if (pv > highest) highest = pv;

			if ((c.getValue() > 0) && (c.getValue() < 10)
				&& (c.getValue() > highestNum)) 
				highestNum = c.getValue();

			c.setCurrentValue(pv);
			total += pv;
		}


		// Step 4.  Using "higest numerical card" variable from step 3, calculate 
		// Mystery Wild value (if applicable).  If this value is higher than "Highest
		// Value Card" from previous step, adjust that variable as well (so that the ?W
		// could be the highest value card, something that is not possible in the
		// current code). Set "total += [?W value]"
		if (cMystery != null) 
		{
			int pv;

			if (highestNum > 0) 
			{
				pv = 10 * highestNum;
			}
			else 
			{
				pv = 10;
			}

			if (pv > highest) 
			{
				highest = pv;
			}

			cMystery.setCurrentValue(pv);
			total += pv;
		}


		// Step 5.  Using "Highest Value Card" variable from last two steps, calculate
		// Blue Shield value.  Set "total += [BS value]"
		if (cBlueShield != null) 
		{
			cBlueShield.setCurrentValue(highest);
			total += highest;
		}


		// Step 6.  If 69 card exists, then set "total = 69"
		if (cSixtyNine != null) 
		{
			cSixtyNine.setCurrentValue(69 - total);
			total = 69;
		}

		// Step 7.  If M5 card exists, then set "total -= 5"
		if (cMagic5 != null) 
		{
			cMagic5.setCurrentValue(-5);
			total -= 5;
		}

		// Step 8.  (Assuming not skipped due to Step 2) If F.U.
		// card exists, then set "total *= 2"
		if (!bFullMonty && (cFuckYou != null)) 
		{
			cFuckYou.setCurrentValue(total);
			total *= 2;
		}

		// Step 9.  If Holy Def. card
		// exists, then set "total /= 2" (rounded up, if necessary)
		if (cHolyDefender != null) 
		{
			int newtotal = (int)((total + 1) / 2);
			cHolyDefender.setCurrentValue(newtotal - total);
			total = newtotal;
		}

		// Step 10.  You now have a good "total" with which to calculate the shitter
		// penalty.  If another players's hand is higher than "total" then set
		// "total = [other player's hand]"
		
		// When the round is over, the game object applies the shitter penalty, since the game
		// has knowledge of all final scores and can apply the highest one.
		// However, for mid-game score estimates, we can apply our artificial value for
		// the shitter right here.
		if (cShitter != null && !isfinal)
		{
			if (total < cShitter.getCurrentValue())
			{
				total = cShitter.getCurrentValue ();
			}
		}

		// Step 11.  Did the player win this hand?  If not, set "total += (10 * number
		// of virus infections)".
		// ALSO DONE IN Game.cpp, because we want to display the score and the virus penalty
		// separately.
		
		return total;
	}
	
	
	public Hand(JSONObject o, Player p, CardDeck d) throws JSONException
	{
		m_player = p;
		m_cards = new Card[Game.MAX_NUM_CARDS];
		
		JSONArray a = o.getJSONArray("cards");
		int numCards = a.length();
		for (int i = 0; i < numCards; i++)
		{
			Card c = d.getCard(a.getInt(i));
			this.addCard(c);
		}
	}
	
	public JSONObject toJSON () throws JSONException
	{
		JSONArray a = new JSONArray ();
		for (int i = 0; i < m_numCards; i++)
		{
			Card c = m_cards[i];
			a.put(c.getDeckIndex());
		}
		
		JSONObject o = new JSONObject ();
		o.put ("cards", a);
		
		return o;
	}
}
