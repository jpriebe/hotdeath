package com.smorgasbork.hotdeath;

import org.json.JSONException;
import org.json.JSONObject;

public class Penalty {

	public static final int PENTYPE_NONE = 0;
	public static final int PENTYPE_CARD = 1;
	public static final int PENTYPE_EJECT = 2;
	public static final int PENTYPE_FACEUP = 3;
	
	private Player m_generatingPlayer;
	private Player m_victim;
	private Player m_secondaryVictim;
	private int m_type;
	private Card m_origCard;

	private int m_numcards;

	public Player getGeneratingPlayer () 
	{
		return m_generatingPlayer; 
	}

	public void setGeneratingPlayer(Player p) 
	{
		m_generatingPlayer = p; 
	}
	
	public Player getVictim () 
	{ 
		return m_victim; 
	}
	
	public void setVictim(Player p) 
	{ 
		m_victim = p; 
	}
	
	public Player getSecondaryVictim () 
	{
		return m_secondaryVictim; 
	}
	
	public void setSecondaryVictim(Player p)
	{
		m_secondaryVictim = p; 
	}
	
	public Card getOrigCard() 
	{ 
		return m_origCard; 
	}

	public int getType() 
	{ 
		return m_type; 
	}
	
	public void setType(int t)
	{ 
		m_type = t; 
	}

	public int getNumCards() 
	{ 
		return m_numcards; 
	}
	
	
	public Penalty()
	{
		reset();
	}


	public void reset()
	{
		m_origCard = null;
		m_numcards = 0;
		m_type = PENTYPE_NONE;
		m_generatingPlayer = null;
		m_victim = null;
		m_secondaryVictim = null;
	}



	public void addCards(Card c, int n, Player p, Player pVictim)
	{
		if (c != null) 
		{
			m_origCard = c;
		}

		m_numcards += n;
		m_type = PENTYPE_CARD;
		m_generatingPlayer = p;
		m_victim = pVictim;
	}


	public void setNumCards(Card c, int n, Player p, Player pVictim)
	{
		if (c != null) 
		{
			m_origCard = c;
		}

		m_numcards = n;
		m_type = PENTYPE_CARD;
		m_generatingPlayer = p;
		m_victim = pVictim;
	}


	public void setEject(Card c, Player p, Player pVictim)
	{
		if (c != null)
		{
			m_origCard = c;
		}
		
		m_type = PENTYPE_EJECT;
		m_generatingPlayer = p;
		m_victim = pVictim;
	}


	public void setFaceup(Card c, Player p, Player pVictim)
	{
		if (c != null)
		{
			m_origCard = c;
		}
		
		m_type = PENTYPE_FACEUP;
		m_generatingPlayer = p;
		m_victim = pVictim;
	}
	
	public Penalty (JSONObject o, Game g, CardDeck d) throws JSONException
	{
		this();
		
		this.m_type = o.getInt("type");
		this.m_numcards = o.getInt("numcards");
		
		int n = o.getInt("generatingPlayer");
		if (n == 0)
		{
			m_generatingPlayer = null;
		}
		else
		{
			m_generatingPlayer = g.getPlayer(n - 1);
		}
		
		n = o.getInt("victim");
		if (n == 0)
		{
			m_victim = null;
		}
		else
		{
			m_victim = g.getPlayer(n - 1);
		}
		
		n = o.getInt("secondaryVictim");
		if (n == 0)
		{
			m_secondaryVictim = null;
		}
		else
		{
			m_secondaryVictim = g.getPlayer(n - 1);
		}
		
		n = o.getInt("origCard");
		if (n == -1)
		{
			m_origCard = null;
		}
		else
		{
			m_origCard = d.getCard(n);
		}
	}
	
	public JSONObject toJSON () throws JSONException
	{
		JSONObject o = new JSONObject ();
		
		o.put ("type", m_type);
		o.put ("numcards", m_numcards);

		if (m_generatingPlayer != null)
		{
			o.put ("generatingPlayer", m_generatingPlayer.getSeat());
		}
		else
		{
			o.put ("generatingPlayer", 0);
		}
		
		if (m_victim != null)
		{
			o.put ("victim", m_victim.getSeat());
		}
		else
		{
			o.put ("victim", 0);
		}
		
		if (m_secondaryVictim != null)
		{
			o.put ("secondaryVictim", m_secondaryVictim.getSeat());
		}
		else
		{
			o.put ("secondaryVictim", 0);
		}
		
		
		if (m_origCard != null)
		{
			o.put ("origCard", m_origCard.getDeckIndex());
		}
		else
		{
			o.put ("origCard", -1);
		}
		
		return o;
	}
	
}
