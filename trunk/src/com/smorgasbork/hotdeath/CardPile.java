package com.smorgasbork.hotdeath;

import java.util.Random;
import org.json.*;

public class CardPile 
{
	private int m_numCards = 0;
	private Card[] m_cards;
		
	public int getNumCards() 
	{ 
		return m_numCards; 
	}
	
	public Card getCard(int i) 
	{ 
		return m_cards[i]; 
	}
	
	public CardPile ()
	{
		m_cards = new Card[Game.MAX_NUM_CARDS];
	}
	
	public void addCard(Card c)
	{
		m_cards[m_numCards++] = c;
	}


	public Card drawCard()
	{
		if (m_numCards < 1)
		{
			return null;
		}

		Card c = m_cards[m_numCards - 1];
		m_cards[m_numCards - 1] = null;
		m_numCards--;
		
		return c;
	}
	
	public void shuffle ()
	{
		shuffle (7);
	}

	public void shuffle (int numTimes)
	{
		int i, j, k;
		Card cTemp;

		for ( i = 0; i < m_numCards; i++) {
			m_cards[i].setFaceUp(false);
		}

		Random rgen = new Random();
		
		for ( i = 0; i < numTimes; i++) 
		{
			for ( j = 0; j < m_numCards; j++) 
			{
				k = rgen.nextInt(m_numCards);

				cTemp = m_cards[j];
				m_cards[j] = m_cards[k];
				m_cards[k] = cTemp;
			}
		}
    }
	
	public CardPile (JSONObject o, CardDeck d) throws JSONException
	{
		this();
		
		JSONArray a = o.getJSONArray("cards");
		m_numCards = a.length();
		for (int i = 0; i < m_numCards; i++)
		{
			m_cards[i] = d.getCard(a.getInt(i));
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
