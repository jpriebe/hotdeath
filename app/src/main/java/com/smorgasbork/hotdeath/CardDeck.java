package com.smorgasbork.hotdeath;

import com.smorgasbork.hotdeath.Card;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CardDeck {
	
	Card[]   m_cards;
	Card[]   m_oCards;
	int      m_numCards = 0;

	/*  

	108 cards
	0 * 4     =  4
	1-9 * 8   = 72
	D,S,R * 8 = 24
	Wild      =  4
	DF Wild   =  4
	???
	*/
	
	public Card[] getCards() 
	{
		return m_cards; 
	}
	
	public int getNumCards()
	{ 
		return m_numCards; 
	}



	public CardDeck()
	{
	}

	public void reset(boolean standardRules, boolean oneDeck)
	{
		for (int i = 0 ; i < m_numCards; i++) {
			m_cards[i] = null;
		}
		m_numCards = 0;

		if (oneDeck) 
		{
			m_cards = new Card[108];
			m_oCards = new Card[108];
		}
		else
		{
			m_cards = new Card[216];
			m_oCards = new Card[216];
		}
		
		int i = 0;
		if (!standardRules) 
		{
			if (oneDeck) 
			{
				m_cards[i] = new Card(i, Card.COLOR_RED, 0, Card.ID_RED_0_HD, 0, 0.5); i++;
				m_cards[i] = new Card(i, Card.COLOR_RED, 1, Card.ID_RED_1, 1); i++;
				m_cards[i] = new Card(i, Card.COLOR_RED, 1, Card.ID_RED_1, 1); i++;
				m_cards[i] = new Card(i, Card.COLOR_RED, 2, Card.ID_RED_2, 2); i++;
				m_cards[i] = new Card(i, Card.COLOR_RED, 2, Card.ID_RED_2_GLASNOST, 75); i++;
				m_cards[i] = new Card(i, Card.COLOR_RED, 3, Card.ID_RED_3, 3); i++;
				m_cards[i] = new Card(i, Card.COLOR_RED, 3, Card.ID_RED_3, 3); i++;
				m_cards[i] = new Card(i, Card.COLOR_RED, 4, Card.ID_RED_4, 4); i++;
				m_cards[i] = new Card(i, Card.COLOR_RED, 4, Card.ID_RED_4, 4); i++;
				m_cards[i] = new Card(i, Card.COLOR_RED, 5, Card.ID_RED_5, 5); i++;
				m_cards[i] = new Card(i, Card.COLOR_RED, 5, Card.ID_RED_5_MAGIC, -5); i++;
				m_cards[i] = new Card(i, Card.COLOR_RED, 6, Card.ID_RED_6, 6); i++;
				m_cards[i] = new Card(i, Card.COLOR_RED, 6, Card.ID_RED_6, 6); i++;
				m_cards[i] = new Card(i, Card.COLOR_RED, 7, Card.ID_RED_7, 7); i++;
				m_cards[i] = new Card(i, Card.COLOR_RED, 7, Card.ID_RED_7, 7); i++;
				m_cards[i] = new Card(i, Card.COLOR_RED, 8, Card.ID_RED_8, 8); i++;
				m_cards[i] = new Card(i, Card.COLOR_RED, 8, Card.ID_RED_8, 8); i++;
				m_cards[i] = new Card(i, Card.COLOR_RED, 9, Card.ID_RED_9, 9); i++;
				m_cards[i] = new Card(i, Card.COLOR_RED, 9, Card.ID_RED_9, 9); i++;
				m_cards[i] = new Card(i, Card.COLOR_RED, Card.VAL_D, Card.ID_RED_D, 20); i++;
				m_cards[i] = new Card(i, Card.COLOR_RED, Card.VAL_D_SPREAD, Card.ID_RED_D_SPREADER, 60); i++;
				m_cards[i] = new Card(i, Card.COLOR_RED, Card.VAL_S, Card.ID_RED_S, 20); i++;
				m_cards[i] = new Card(i, Card.COLOR_RED, Card.VAL_S_DOUBLE, Card.ID_RED_S_DOUBLE, 40); i++;
				m_cards[i] = new Card(i, Card.COLOR_RED, Card.VAL_R, Card.ID_RED_R, 20); i++;
				m_cards[i] = new Card(i, Card.COLOR_RED, Card.VAL_R_SKIP, Card.ID_RED_R_SKIP, 40); i++;

				m_cards[i] = new Card(i, Card.COLOR_GREEN, 0, Card.ID_GREEN_0_QUITTER, 100); i++;
				m_cards[i] = new Card(i, Card.COLOR_GREEN, 1, Card.ID_GREEN_1, 1); i++;
				m_cards[i] = new Card(i, Card.COLOR_GREEN, 1, Card.ID_GREEN_1, 1); i++;
				m_cards[i] = new Card(i, Card.COLOR_GREEN, 2, Card.ID_GREEN_2, 2); i++;
				m_cards[i] = new Card(i, Card.COLOR_GREEN, 2, Card.ID_GREEN_2, 2); i++;
				m_cards[i] = new Card(i, Card.COLOR_GREEN, 3, Card.ID_GREEN_3, 3); i++;
				m_cards[i] = new Card(i, Card.COLOR_GREEN, 3, Card.ID_GREEN_3_AIDS, 3, 1.0, 10); i++;
				m_cards[i] = new Card(i, Card.COLOR_GREEN, 4, Card.ID_GREEN_4, 4); i++;
				m_cards[i] = new Card(i, Card.COLOR_GREEN, 4, Card.ID_GREEN_4_IRISH, 75); i++;
				m_cards[i] = new Card(i, Card.COLOR_GREEN, 5, Card.ID_GREEN_5, 5); i++;
				m_cards[i] = new Card(i, Card.COLOR_GREEN, 5, Card.ID_GREEN_5, 5); i++;
				m_cards[i] = new Card(i, Card.COLOR_GREEN, 6, Card.ID_GREEN_6, 6); i++;
				m_cards[i] = new Card(i, Card.COLOR_GREEN, 6, Card.ID_GREEN_6, 6); i++;
				m_cards[i] = new Card(i, Card.COLOR_GREEN, 7, Card.ID_GREEN_7, 7); i++;
				m_cards[i] = new Card(i, Card.COLOR_GREEN, 7, Card.ID_GREEN_7, 7); i++;
				m_cards[i] = new Card(i, Card.COLOR_GREEN, 8, Card.ID_GREEN_8, 8); i++;
				m_cards[i] = new Card(i, Card.COLOR_GREEN, 8, Card.ID_GREEN_8, 8); i++;
				m_cards[i] = new Card(i, Card.COLOR_GREEN, 9, Card.ID_GREEN_9, 9); i++;
				m_cards[i] = new Card(i, Card.COLOR_GREEN, 9, Card.ID_GREEN_9, 9); i++;
				m_cards[i] = new Card(i, Card.COLOR_GREEN, Card.VAL_D, Card.ID_GREEN_D, 20); i++;
				m_cards[i] = new Card(i, Card.COLOR_GREEN, Card.VAL_D_SPREAD, Card.ID_GREEN_D_SPREADER, 60); i++;
				m_cards[i] = new Card(i, Card.COLOR_GREEN, Card.VAL_S, Card.ID_GREEN_S, 20); i++;
				m_cards[i] = new Card(i, Card.COLOR_GREEN, Card.VAL_S_DOUBLE, Card.ID_GREEN_S_DOUBLE, 40); i++;
				m_cards[i] = new Card(i, Card.COLOR_GREEN, Card.VAL_R, Card.ID_GREEN_R, 20); i++;
				m_cards[i] = new Card(i, Card.COLOR_GREEN, Card.VAL_R_SKIP, Card.ID_GREEN_R_SKIP, 40); i++;

				m_cards[i] = new Card(i, Card.COLOR_BLUE, 0, Card.ID_BLUE_0_FUCKYOU, 0, 2.0); i++;
				m_cards[i] = new Card(i, Card.COLOR_BLUE, 1, Card.ID_BLUE_1, 1); i++;
				m_cards[i] = new Card(i, Card.COLOR_BLUE, 1, Card.ID_BLUE_1, 1); i++;
				m_cards[i] = new Card(i, Card.COLOR_BLUE, 2, Card.ID_BLUE_2, 2); i++;
				m_cards[i] = new Card(i, Card.COLOR_BLUE, 2, Card.ID_BLUE_2_SHIELD, 0, 1.0, 0, 1); i++;
				m_cards[i] = new Card(i, Card.COLOR_BLUE, 3, Card.ID_BLUE_3, 3); i++;
				m_cards[i] = new Card(i, Card.COLOR_BLUE, 3, Card.ID_BLUE_3, 3); i++;
				m_cards[i] = new Card(i, Card.COLOR_BLUE, 4, Card.ID_BLUE_4, 4); i++;
				m_cards[i] = new Card(i, Card.COLOR_BLUE, 4, Card.ID_BLUE_4, 4); i++;
				m_cards[i] = new Card(i, Card.COLOR_BLUE, 5, Card.ID_BLUE_5, 5); i++;
				m_cards[i] = new Card(i, Card.COLOR_BLUE, 5, Card.ID_BLUE_5, 5); i++;
				m_cards[i] = new Card(i, Card.COLOR_BLUE, 6, Card.ID_BLUE_6, 6); i++;
				m_cards[i] = new Card(i, Card.COLOR_BLUE, 6, Card.ID_BLUE_6, 6); i++;
				m_cards[i] = new Card(i, Card.COLOR_BLUE, 7, Card.ID_BLUE_7, 7); i++;
				m_cards[i] = new Card(i, Card.COLOR_BLUE, 7, Card.ID_BLUE_7, 7); i++;
				m_cards[i] = new Card(i, Card.COLOR_BLUE, 8, Card.ID_BLUE_8, 8); i++;
				m_cards[i] = new Card(i, Card.COLOR_BLUE, 8, Card.ID_BLUE_8, 8); i++;
				m_cards[i] = new Card(i, Card.COLOR_BLUE, 9, Card.ID_BLUE_9, 9); i++;
				m_cards[i] = new Card(i, Card.COLOR_BLUE, 9, Card.ID_BLUE_9, 9); i++;
				m_cards[i] = new Card(i, Card.COLOR_BLUE, Card.VAL_D, Card.ID_BLUE_D, 20); i++;
				m_cards[i] = new Card(i, Card.COLOR_BLUE, Card.VAL_D_SPREAD, Card.ID_BLUE_D_SPREADER, 60); i++;
				m_cards[i] = new Card(i, Card.COLOR_BLUE, Card.VAL_S, Card.ID_BLUE_S, 20); i++;
				m_cards[i] = new Card(i, Card.COLOR_BLUE, Card.VAL_S_DOUBLE, Card.ID_BLUE_S_DOUBLE, 40); i++;
				m_cards[i] = new Card(i, Card.COLOR_BLUE, Card.VAL_R, Card.ID_BLUE_R, 20); i++;
				m_cards[i] = new Card(i, Card.COLOR_BLUE, Card.VAL_R_SKIP, Card.ID_BLUE_R_SKIP, 40); i++;

				m_cards[i] = new Card(i, Card.COLOR_YELLOW, 0, Card.ID_YELLOW_0_SHITTER, 0); i++;  // this is a simplification
				m_cards[i] = new Card(i, Card.COLOR_YELLOW, 1, Card.ID_YELLOW_1, 1); i++;
				m_cards[i] = new Card(i, Card.COLOR_YELLOW, 1, Card.ID_YELLOW_1_MAD, 100); i++;
				m_cards[i] = new Card(i, Card.COLOR_YELLOW, 2, Card.ID_YELLOW_2, 2); i++;
				m_cards[i] = new Card(i, Card.COLOR_YELLOW, 2, Card.ID_YELLOW_2, 2); i++;
				m_cards[i] = new Card(i, Card.COLOR_YELLOW, 3, Card.ID_YELLOW_3, 3); i++;
				m_cards[i] = new Card(i, Card.COLOR_YELLOW, 3, Card.ID_YELLOW_3, 3); i++;
				m_cards[i] = new Card(i, Card.COLOR_YELLOW, 4, Card.ID_YELLOW_4, 4); i++;
				m_cards[i] = new Card(i, Card.COLOR_YELLOW, 4, Card.ID_YELLOW_4, 4); i++;
				m_cards[i] = new Card(i, Card.COLOR_YELLOW, 5, Card.ID_YELLOW_5, 5); i++;
				m_cards[i] = new Card(i, Card.COLOR_YELLOW, 5, Card.ID_YELLOW_5, 5); i++;
				m_cards[i] = new Card(i, Card.COLOR_YELLOW, 6, Card.ID_YELLOW_6, 6); i++;
				m_cards[i] = new Card(i, Card.COLOR_YELLOW, 6, Card.ID_YELLOW_69, 6); i++;
				m_cards[i] = new Card(i, Card.COLOR_YELLOW, 7, Card.ID_YELLOW_7, 7); i++;
				m_cards[i] = new Card(i, Card.COLOR_YELLOW, 7, Card.ID_YELLOW_7, 7); i++;
				m_cards[i] = new Card(i, Card.COLOR_YELLOW, 8, Card.ID_YELLOW_8, 8); i++;
				m_cards[i] = new Card(i, Card.COLOR_YELLOW, 8, Card.ID_YELLOW_8, 8); i++;
				m_cards[i] = new Card(i, Card.COLOR_YELLOW, 9, Card.ID_YELLOW_9, 9); i++;
				m_cards[i] = new Card(i, Card.COLOR_YELLOW, 9, Card.ID_YELLOW_9, 9); i++;
				m_cards[i] = new Card(i, Card.COLOR_YELLOW, Card.VAL_D, Card.ID_YELLOW_D, 20); i++;
				m_cards[i] = new Card(i, Card.COLOR_YELLOW, Card.VAL_D_SPREAD, Card.ID_YELLOW_D_SPREADER, 60); i++;
				m_cards[i] = new Card(i, Card.COLOR_YELLOW, Card.VAL_S, Card.ID_YELLOW_S, 20); i++;
				m_cards[i] = new Card(i, Card.COLOR_YELLOW, Card.VAL_S_DOUBLE, Card.ID_YELLOW_S_DOUBLE, 40); i++;
				m_cards[i] = new Card(i, Card.COLOR_YELLOW, Card.VAL_R, Card.ID_YELLOW_R, 20); i++;
				m_cards[i] = new Card(i, Card.COLOR_YELLOW, Card.VAL_R_SKIP, Card.ID_YELLOW_R_SKIP, 40); i++;

				m_cards[i] = new Card(i, Card.COLOR_WILD, Card.VAL_WILD_DRAWFOUR, Card.ID_WILD_DRAWFOUR, 50); i++;
				m_cards[i] = new Card(i, Card.COLOR_WILD, Card.VAL_WILD_DRAWFOUR, Card.ID_WILD_DRAWFOUR, 50); i++;
				m_cards[i] = new Card(i, Card.COLOR_WILD, Card.VAL_WILD_DRAWFOUR, Card.ID_WILD_DRAWFOUR, 50); i++;
				m_cards[i] = new Card(i, Card.COLOR_WILD, Card.VAL_WILD_DRAWFOUR, Card.ID_WILD_DRAWFOUR, 50); i++;

				m_cards[i] = new Card(i, Card.COLOR_WILD, Card.VAL_WILD_DRAWFOUR, Card.ID_WILD_HD, 100); i++;
				m_cards[i] = new Card(i, Card.COLOR_WILD, Card.VAL_WILD_DRAWFOUR, Card.ID_WILD_DB, 100); i++;
				m_cards[i] = new Card(i, Card.COLOR_WILD, Card.VAL_WILD_DRAWFOUR, Card.ID_WILD_HOS, 0); i++;
				m_cards[i] = new Card(i, Card.COLOR_WILD, Card.VAL_WILD_DRAWFOUR, Card.ID_WILD_MYSTERY, 0); i++;
			}

			else {
				m_cards[i] = new Card(i, Card.COLOR_RED, 0, Card.ID_RED_0, 0); i++;
				m_cards[i] = new Card(i, Card.COLOR_RED, 0, Card.ID_RED_0_HD, 0, 0.5); i++;
				m_cards[i] = new Card(i, Card.COLOR_RED, 1, Card.ID_RED_1, 1); i++;
				m_cards[i] = new Card(i, Card.COLOR_RED, 1, Card.ID_RED_1, 1); i++;
				m_cards[i] = new Card(i, Card.COLOR_RED, 1, Card.ID_RED_1, 1); i++;
				m_cards[i] = new Card(i, Card.COLOR_RED, 1, Card.ID_RED_1, 1); i++;
				m_cards[i] = new Card(i, Card.COLOR_RED, 2, Card.ID_RED_2, 2); i++;
				m_cards[i] = new Card(i, Card.COLOR_RED, 2, Card.ID_RED_2, 2); i++;
				m_cards[i] = new Card(i, Card.COLOR_RED, 2, Card.ID_RED_2, 2); i++;
				m_cards[i] = new Card(i, Card.COLOR_RED, 2, Card.ID_RED_2_GLASNOST, 75); i++;
				m_cards[i] = new Card(i, Card.COLOR_RED, 3, Card.ID_RED_3, 3); i++;
				m_cards[i] = new Card(i, Card.COLOR_RED, 3, Card.ID_RED_3, 3); i++;
				m_cards[i] = new Card(i, Card.COLOR_RED, 3, Card.ID_RED_3, 3); i++;
				m_cards[i] = new Card(i, Card.COLOR_RED, 3, Card.ID_RED_3, 3); i++;
				m_cards[i] = new Card(i, Card.COLOR_RED, 4, Card.ID_RED_4, 4); i++;
				m_cards[i] = new Card(i, Card.COLOR_RED, 4, Card.ID_RED_4, 4); i++;
				m_cards[i] = new Card(i, Card.COLOR_RED, 4, Card.ID_RED_4, 4); i++;
				m_cards[i] = new Card(i, Card.COLOR_RED, 4, Card.ID_RED_4, 4); i++;
				m_cards[i] = new Card(i, Card.COLOR_RED, 5, Card.ID_RED_5, 5); i++;
				m_cards[i] = new Card(i, Card.COLOR_RED, 5, Card.ID_RED_5, 5); i++;
				m_cards[i] = new Card(i, Card.COLOR_RED, 5, Card.ID_RED_5, 5); i++;
				m_cards[i] = new Card(i, Card.COLOR_RED, 5, Card.ID_RED_5_MAGIC, -5); i++;
				m_cards[i] = new Card(i, Card.COLOR_RED, 6, Card.ID_RED_6, 6); i++;
				m_cards[i] = new Card(i, Card.COLOR_RED, 6, Card.ID_RED_6, 6); i++;
				m_cards[i] = new Card(i, Card.COLOR_RED, 6, Card.ID_RED_6, 6); i++;
				m_cards[i] = new Card(i, Card.COLOR_RED, 6, Card.ID_RED_6, 6); i++;
				m_cards[i] = new Card(i, Card.COLOR_RED, 7, Card.ID_RED_7, 7); i++;
				m_cards[i] = new Card(i, Card.COLOR_RED, 7, Card.ID_RED_7, 7); i++;
				m_cards[i] = new Card(i, Card.COLOR_RED, 7, Card.ID_RED_7, 7); i++;
				m_cards[i] = new Card(i, Card.COLOR_RED, 7, Card.ID_RED_7, 7); i++;
				m_cards[i] = new Card(i, Card.COLOR_RED, 8, Card.ID_RED_8, 8); i++;
				m_cards[i] = new Card(i, Card.COLOR_RED, 8, Card.ID_RED_8, 8); i++;
				m_cards[i] = new Card(i, Card.COLOR_RED, 8, Card.ID_RED_8, 8); i++;
				m_cards[i] = new Card(i, Card.COLOR_RED, 8, Card.ID_RED_8, 8); i++;
				m_cards[i] = new Card(i, Card.COLOR_RED, 9, Card.ID_RED_9, 9); i++;
				m_cards[i] = new Card(i, Card.COLOR_RED, 9, Card.ID_RED_9, 9); i++;
				m_cards[i] = new Card(i, Card.COLOR_RED, 9, Card.ID_RED_9, 9); i++;
				m_cards[i] = new Card(i, Card.COLOR_RED, 9, Card.ID_RED_9, 9); i++;
				m_cards[i] = new Card(i, Card.COLOR_RED, Card.VAL_D, Card.ID_RED_D, 20); i++;
				m_cards[i] = new Card(i, Card.COLOR_RED, Card.VAL_D, Card.ID_RED_D, 20); i++;
				m_cards[i] = new Card(i, Card.COLOR_RED, Card.VAL_D_SPREAD, Card.ID_RED_D_SPREADER, 60); i++;
				m_cards[i] = new Card(i, Card.COLOR_RED, Card.VAL_D_SPREAD, Card.ID_RED_D_SPREADER, 60); i++;
				m_cards[i] = new Card(i, Card.COLOR_RED, Card.VAL_S, Card.ID_RED_S, 20); i++;
				m_cards[i] = new Card(i, Card.COLOR_RED, Card.VAL_S, Card.ID_RED_S, 20); i++;
				m_cards[i] = new Card(i, Card.COLOR_RED, Card.VAL_S_DOUBLE, Card.ID_RED_S_DOUBLE, 40); i++;
				m_cards[i] = new Card(i, Card.COLOR_RED, Card.VAL_S_DOUBLE, Card.ID_RED_S_DOUBLE, 40); i++;
				m_cards[i] = new Card(i, Card.COLOR_RED, Card.VAL_R, Card.ID_RED_R, 20); i++;
				m_cards[i] = new Card(i, Card.COLOR_RED, Card.VAL_R, Card.ID_RED_R, 20); i++;
				m_cards[i] = new Card(i, Card.COLOR_RED, Card.VAL_R_SKIP, Card.ID_RED_R_SKIP, 40); i++;
				m_cards[i] = new Card(i, Card.COLOR_RED, Card.VAL_R_SKIP, Card.ID_RED_R_SKIP, 40); i++;

				m_cards[i] = new Card(i, Card.COLOR_GREEN, 0, Card.ID_GREEN_0, 0); i++;
				m_cards[i] = new Card(i, Card.COLOR_GREEN, 0, Card.ID_GREEN_0_QUITTER, 100); i++;
				m_cards[i] = new Card(i, Card.COLOR_GREEN, 1, Card.ID_GREEN_1, 1); i++;
				m_cards[i] = new Card(i, Card.COLOR_GREEN, 1, Card.ID_GREEN_1, 1); i++;
				m_cards[i] = new Card(i, Card.COLOR_GREEN, 1, Card.ID_GREEN_1, 1); i++;
				m_cards[i] = new Card(i, Card.COLOR_GREEN, 1, Card.ID_GREEN_1, 1); i++;
				m_cards[i] = new Card(i, Card.COLOR_GREEN, 2, Card.ID_GREEN_2, 2); i++;
				m_cards[i] = new Card(i, Card.COLOR_GREEN, 2, Card.ID_GREEN_2, 2); i++;
				m_cards[i] = new Card(i, Card.COLOR_GREEN, 2, Card.ID_GREEN_2, 2); i++;
				m_cards[i] = new Card(i, Card.COLOR_GREEN, 2, Card.ID_GREEN_2, 2); i++;
				m_cards[i] = new Card(i, Card.COLOR_GREEN, 3, Card.ID_GREEN_3, 3); i++;
				m_cards[i] = new Card(i, Card.COLOR_GREEN, 3, Card.ID_GREEN_3, 3); i++;
				m_cards[i] = new Card(i, Card.COLOR_GREEN, 3, Card.ID_GREEN_3, 3); i++;
				m_cards[i] = new Card(i, Card.COLOR_GREEN, 3, Card.ID_GREEN_3_AIDS, 3, 1.0, 10); i++;
				m_cards[i] = new Card(i, Card.COLOR_GREEN, 4, Card.ID_GREEN_4, 4); i++;
				m_cards[i] = new Card(i, Card.COLOR_GREEN, 4, Card.ID_GREEN_4, 4); i++;
				m_cards[i] = new Card(i, Card.COLOR_GREEN, 4, Card.ID_GREEN_4, 4); i++;
				m_cards[i] = new Card(i, Card.COLOR_GREEN, 4, Card.ID_GREEN_4_IRISH, 75); i++;
				m_cards[i] = new Card(i, Card.COLOR_GREEN, 5, Card.ID_GREEN_5, 5); i++;
				m_cards[i] = new Card(i, Card.COLOR_GREEN, 5, Card.ID_GREEN_5, 5); i++;
				m_cards[i] = new Card(i, Card.COLOR_GREEN, 5, Card.ID_GREEN_5, 5); i++;
				m_cards[i] = new Card(i, Card.COLOR_GREEN, 5, Card.ID_GREEN_5, 5); i++;
				m_cards[i] = new Card(i, Card.COLOR_GREEN, 6, Card.ID_GREEN_6, 6); i++;
				m_cards[i] = new Card(i, Card.COLOR_GREEN, 6, Card.ID_GREEN_6, 6); i++;
				m_cards[i] = new Card(i, Card.COLOR_GREEN, 6, Card.ID_GREEN_6, 6); i++;
				m_cards[i] = new Card(i, Card.COLOR_GREEN, 6, Card.ID_GREEN_6, 6); i++;
				m_cards[i] = new Card(i, Card.COLOR_GREEN, 7, Card.ID_GREEN_7, 7); i++;
				m_cards[i] = new Card(i, Card.COLOR_GREEN, 7, Card.ID_GREEN_7, 7); i++;
				m_cards[i] = new Card(i, Card.COLOR_GREEN, 7, Card.ID_GREEN_7, 7); i++;
				m_cards[i] = new Card(i, Card.COLOR_GREEN, 7, Card.ID_GREEN_7, 7); i++;
				m_cards[i] = new Card(i, Card.COLOR_GREEN, 8, Card.ID_GREEN_8, 8); i++;
				m_cards[i] = new Card(i, Card.COLOR_GREEN, 8, Card.ID_GREEN_8, 8); i++;
				m_cards[i] = new Card(i, Card.COLOR_GREEN, 8, Card.ID_GREEN_8, 8); i++;
				m_cards[i] = new Card(i, Card.COLOR_GREEN, 8, Card.ID_GREEN_8, 8); i++;
				m_cards[i] = new Card(i, Card.COLOR_GREEN, 9, Card.ID_GREEN_9, 9); i++;
				m_cards[i] = new Card(i, Card.COLOR_GREEN, 9, Card.ID_GREEN_9, 9); i++;
				m_cards[i] = new Card(i, Card.COLOR_GREEN, 9, Card.ID_GREEN_9, 9); i++;
				m_cards[i] = new Card(i, Card.COLOR_GREEN, 9, Card.ID_GREEN_9, 9); i++;
				m_cards[i] = new Card(i, Card.COLOR_GREEN, Card.VAL_D, Card.ID_GREEN_D, 20); i++;
				m_cards[i] = new Card(i, Card.COLOR_GREEN, Card.VAL_D, Card.ID_GREEN_D, 20); i++;
				m_cards[i] = new Card(i, Card.COLOR_GREEN, Card.VAL_D_SPREAD, Card.ID_GREEN_D_SPREADER, 60); i++;
				m_cards[i] = new Card(i, Card.COLOR_GREEN, Card.VAL_D_SPREAD, Card.ID_GREEN_D_SPREADER, 60); i++;
				m_cards[i] = new Card(i, Card.COLOR_GREEN, Card.VAL_S, Card.ID_GREEN_S, 20); i++;
				m_cards[i] = new Card(i, Card.COLOR_GREEN, Card.VAL_S, Card.ID_GREEN_S, 20); i++;
				m_cards[i] = new Card(i, Card.COLOR_GREEN, Card.VAL_S_DOUBLE, Card.ID_GREEN_S_DOUBLE, 40); i++;
				m_cards[i] = new Card(i, Card.COLOR_GREEN, Card.VAL_S_DOUBLE, Card.ID_GREEN_S_DOUBLE, 40); i++;
				m_cards[i] = new Card(i, Card.COLOR_GREEN, Card.VAL_R, Card.ID_GREEN_R, 20); i++;
				m_cards[i] = new Card(i, Card.COLOR_GREEN, Card.VAL_R, Card.ID_GREEN_R, 20); i++;
				m_cards[i] = new Card(i, Card.COLOR_GREEN, Card.VAL_R_SKIP, Card.ID_GREEN_R_SKIP, 40); i++;
				m_cards[i] = new Card(i, Card.COLOR_GREEN, Card.VAL_R_SKIP, Card.ID_GREEN_R_SKIP, 40); i++;

				m_cards[i] = new Card(i, Card.COLOR_BLUE, 0, Card.ID_BLUE_0, 0); i++;
				m_cards[i] = new Card(i, Card.COLOR_BLUE, 0, Card.ID_BLUE_0_FUCKYOU, 0, 2.0); i++;
				m_cards[i] = new Card(i, Card.COLOR_BLUE, 1, Card.ID_BLUE_1, 1); i++;
				m_cards[i] = new Card(i, Card.COLOR_BLUE, 1, Card.ID_BLUE_1, 1); i++;
				m_cards[i] = new Card(i, Card.COLOR_BLUE, 1, Card.ID_BLUE_1, 1); i++;
				m_cards[i] = new Card(i, Card.COLOR_BLUE, 1, Card.ID_BLUE_1, 1); i++;
				m_cards[i] = new Card(i, Card.COLOR_BLUE, 2, Card.ID_BLUE_2, 2); i++;
				m_cards[i] = new Card(i, Card.COLOR_BLUE, 2, Card.ID_BLUE_2, 2); i++;
				m_cards[i] = new Card(i, Card.COLOR_BLUE, 2, Card.ID_BLUE_2, 2); i++;
				m_cards[i] = new Card(i, Card.COLOR_BLUE, 2, Card.ID_BLUE_2_SHIELD, 0, 1.0, 0, 1); i++;
				m_cards[i] = new Card(i, Card.COLOR_BLUE, 3, Card.ID_BLUE_3, 3); i++;
				m_cards[i] = new Card(i, Card.COLOR_BLUE, 3, Card.ID_BLUE_3, 3); i++;
				m_cards[i] = new Card(i, Card.COLOR_BLUE, 3, Card.ID_BLUE_3, 3); i++;
				m_cards[i] = new Card(i, Card.COLOR_BLUE, 3, Card.ID_BLUE_3, 3); i++;
				m_cards[i] = new Card(i, Card.COLOR_BLUE, 4, Card.ID_BLUE_4, 4); i++;
				m_cards[i] = new Card(i, Card.COLOR_BLUE, 4, Card.ID_BLUE_4, 4); i++;
				m_cards[i] = new Card(i, Card.COLOR_BLUE, 4, Card.ID_BLUE_4, 4); i++;
				m_cards[i] = new Card(i, Card.COLOR_BLUE, 4, Card.ID_BLUE_4, 4); i++;
				m_cards[i] = new Card(i, Card.COLOR_BLUE, 5, Card.ID_BLUE_5, 5); i++;
				m_cards[i] = new Card(i, Card.COLOR_BLUE, 5, Card.ID_BLUE_5, 5); i++;
				m_cards[i] = new Card(i, Card.COLOR_BLUE, 5, Card.ID_BLUE_5, 5); i++;
				m_cards[i] = new Card(i, Card.COLOR_BLUE, 5, Card.ID_BLUE_5, 5); i++;
				m_cards[i] = new Card(i, Card.COLOR_BLUE, 6, Card.ID_BLUE_6, 6); i++;
				m_cards[i] = new Card(i, Card.COLOR_BLUE, 6, Card.ID_BLUE_6, 6); i++;
				m_cards[i] = new Card(i, Card.COLOR_BLUE, 6, Card.ID_BLUE_6, 6); i++;
				m_cards[i] = new Card(i, Card.COLOR_BLUE, 6, Card.ID_BLUE_6, 6); i++;
				m_cards[i] = new Card(i, Card.COLOR_BLUE, 7, Card.ID_BLUE_7, 7); i++;
				m_cards[i] = new Card(i, Card.COLOR_BLUE, 7, Card.ID_BLUE_7, 7); i++;
				m_cards[i] = new Card(i, Card.COLOR_BLUE, 7, Card.ID_BLUE_7, 7); i++;
				m_cards[i] = new Card(i, Card.COLOR_BLUE, 7, Card.ID_BLUE_7, 7); i++;
				m_cards[i] = new Card(i, Card.COLOR_BLUE, 8, Card.ID_BLUE_8, 8); i++;
				m_cards[i] = new Card(i, Card.COLOR_BLUE, 8, Card.ID_BLUE_8, 8); i++;
				m_cards[i] = new Card(i, Card.COLOR_BLUE, 8, Card.ID_BLUE_8, 8); i++;
				m_cards[i] = new Card(i, Card.COLOR_BLUE, 8, Card.ID_BLUE_8, 8); i++;
				m_cards[i] = new Card(i, Card.COLOR_BLUE, 9, Card.ID_BLUE_9, 9); i++;
				m_cards[i] = new Card(i, Card.COLOR_BLUE, 9, Card.ID_BLUE_9, 9); i++;
				m_cards[i] = new Card(i, Card.COLOR_BLUE, 9, Card.ID_BLUE_9, 9); i++;
				m_cards[i] = new Card(i, Card.COLOR_BLUE, 9, Card.ID_BLUE_9, 9); i++;
				m_cards[i] = new Card(i, Card.COLOR_BLUE, Card.VAL_D, Card.ID_BLUE_D, 20); i++;
				m_cards[i] = new Card(i, Card.COLOR_BLUE, Card.VAL_D, Card.ID_BLUE_D, 20); i++;
				m_cards[i] = new Card(i, Card.COLOR_BLUE, Card.VAL_D_SPREAD, Card.ID_BLUE_D_SPREADER, 60); i++;
				m_cards[i] = new Card(i, Card.COLOR_BLUE, Card.VAL_D_SPREAD, Card.ID_BLUE_D_SPREADER, 60); i++;
				m_cards[i] = new Card(i, Card.COLOR_BLUE, Card.VAL_S, Card.ID_BLUE_S, 20); i++;
				m_cards[i] = new Card(i, Card.COLOR_BLUE, Card.VAL_S, Card.ID_BLUE_S, 20); i++;
				m_cards[i] = new Card(i, Card.COLOR_BLUE, Card.VAL_S_DOUBLE, Card.ID_BLUE_S_DOUBLE, 40); i++;
				m_cards[i] = new Card(i, Card.COLOR_BLUE, Card.VAL_S_DOUBLE, Card.ID_BLUE_S_DOUBLE, 40); i++;
				m_cards[i] = new Card(i, Card.COLOR_BLUE, Card.VAL_R, Card.ID_BLUE_R, 20); i++;
				m_cards[i] = new Card(i, Card.COLOR_BLUE, Card.VAL_R, Card.ID_BLUE_R, 20); i++;
				m_cards[i] = new Card(i, Card.COLOR_BLUE, Card.VAL_R_SKIP, Card.ID_BLUE_R_SKIP, 40); i++;
				m_cards[i] = new Card(i, Card.COLOR_BLUE, Card.VAL_R_SKIP, Card.ID_BLUE_R_SKIP, 40); i++;

				m_cards[i] = new Card(i, Card.COLOR_YELLOW, 0, Card.ID_YELLOW_0, 0); i++;
				m_cards[i] = new Card(i, Card.COLOR_YELLOW, 0, Card.ID_YELLOW_0_SHITTER, 0); i++;
				m_cards[i] = new Card(i, Card.COLOR_YELLOW, 1, Card.ID_YELLOW_1, 1); i++;
				m_cards[i] = new Card(i, Card.COLOR_YELLOW, 1, Card.ID_YELLOW_1, 1); i++;
				m_cards[i] = new Card(i, Card.COLOR_YELLOW, 1, Card.ID_YELLOW_1, 1); i++;
				m_cards[i] = new Card(i, Card.COLOR_YELLOW, 1, Card.ID_YELLOW_1_MAD, 100); i++;
				m_cards[i] = new Card(i, Card.COLOR_YELLOW, 2, Card.ID_YELLOW_2, 2); i++;
				m_cards[i] = new Card(i, Card.COLOR_YELLOW, 2, Card.ID_YELLOW_2, 2); i++;
				m_cards[i] = new Card(i, Card.COLOR_YELLOW, 2, Card.ID_YELLOW_2, 2); i++;
				m_cards[i] = new Card(i, Card.COLOR_YELLOW, 2, Card.ID_YELLOW_2, 2); i++;
				m_cards[i] = new Card(i, Card.COLOR_YELLOW, 3, Card.ID_YELLOW_3, 3); i++;
				m_cards[i] = new Card(i, Card.COLOR_YELLOW, 3, Card.ID_YELLOW_3, 3); i++;
				m_cards[i] = new Card(i, Card.COLOR_YELLOW, 3, Card.ID_YELLOW_3, 3); i++;
				m_cards[i] = new Card(i, Card.COLOR_YELLOW, 3, Card.ID_YELLOW_3, 3); i++;
				m_cards[i] = new Card(i, Card.COLOR_YELLOW, 4, Card.ID_YELLOW_4, 4); i++;
				m_cards[i] = new Card(i, Card.COLOR_YELLOW, 4, Card.ID_YELLOW_4, 4); i++;
				m_cards[i] = new Card(i, Card.COLOR_YELLOW, 4, Card.ID_YELLOW_4, 4); i++;
				m_cards[i] = new Card(i, Card.COLOR_YELLOW, 4, Card.ID_YELLOW_4, 4); i++;
				m_cards[i] = new Card(i, Card.COLOR_YELLOW, 5, Card.ID_YELLOW_5, 5); i++;
				m_cards[i] = new Card(i, Card.COLOR_YELLOW, 5, Card.ID_YELLOW_5, 5); i++;
				m_cards[i] = new Card(i, Card.COLOR_YELLOW, 5, Card.ID_YELLOW_5, 5); i++;
				m_cards[i] = new Card(i, Card.COLOR_YELLOW, 5, Card.ID_YELLOW_5, 5); i++;
				m_cards[i] = new Card(i, Card.COLOR_YELLOW, 6, Card.ID_YELLOW_6, 6); i++;
				m_cards[i] = new Card(i, Card.COLOR_YELLOW, 6, Card.ID_YELLOW_6, 6); i++;
				m_cards[i] = new Card(i, Card.COLOR_YELLOW, 6, Card.ID_YELLOW_6, 6); i++;
				m_cards[i] = new Card(i, Card.COLOR_YELLOW, 6, Card.ID_YELLOW_69, 6); i++;
				m_cards[i] = new Card(i, Card.COLOR_YELLOW, 7, Card.ID_YELLOW_7, 7); i++;
				m_cards[i] = new Card(i, Card.COLOR_YELLOW, 7, Card.ID_YELLOW_7, 7); i++;
				m_cards[i] = new Card(i, Card.COLOR_YELLOW, 7, Card.ID_YELLOW_7, 7); i++;
				m_cards[i] = new Card(i, Card.COLOR_YELLOW, 7, Card.ID_YELLOW_7, 7); i++;
				m_cards[i] = new Card(i, Card.COLOR_YELLOW, 8, Card.ID_YELLOW_8, 8); i++;
				m_cards[i] = new Card(i, Card.COLOR_YELLOW, 8, Card.ID_YELLOW_8, 8); i++;
				m_cards[i] = new Card(i, Card.COLOR_YELLOW, 8, Card.ID_YELLOW_8, 8); i++;
				m_cards[i] = new Card(i, Card.COLOR_YELLOW, 8, Card.ID_YELLOW_8, 8); i++;
				m_cards[i] = new Card(i, Card.COLOR_YELLOW, 9, Card.ID_YELLOW_9, 9); i++;
				m_cards[i] = new Card(i, Card.COLOR_YELLOW, 9, Card.ID_YELLOW_9, 9); i++;
				m_cards[i] = new Card(i, Card.COLOR_YELLOW, 9, Card.ID_YELLOW_9, 9); i++;
				m_cards[i] = new Card(i, Card.COLOR_YELLOW, 9, Card.ID_YELLOW_9, 9); i++;
				m_cards[i] = new Card(i, Card.COLOR_YELLOW, Card.VAL_D, Card.ID_YELLOW_D, 20); i++;
				m_cards[i] = new Card(i, Card.COLOR_YELLOW, Card.VAL_D, Card.ID_YELLOW_D, 20); i++;
				m_cards[i] = new Card(i, Card.COLOR_YELLOW, Card.VAL_D_SPREAD, Card.ID_YELLOW_D_SPREADER, 60); i++;
				m_cards[i] = new Card(i, Card.COLOR_YELLOW, Card.VAL_D_SPREAD, Card.ID_YELLOW_D_SPREADER, 60); i++;
				m_cards[i] = new Card(i, Card.COLOR_YELLOW, Card.VAL_S, Card.ID_YELLOW_S, 20); i++;
				m_cards[i] = new Card(i, Card.COLOR_YELLOW, Card.VAL_S, Card.ID_YELLOW_S, 20); i++;
				m_cards[i] = new Card(i, Card.COLOR_YELLOW, Card.VAL_S_DOUBLE, Card.ID_YELLOW_S_DOUBLE, 40); i++;
				m_cards[i] = new Card(i, Card.COLOR_YELLOW, Card.VAL_S_DOUBLE, Card.ID_YELLOW_S_DOUBLE, 40); i++;
				m_cards[i] = new Card(i, Card.COLOR_YELLOW, Card.VAL_R, Card.ID_YELLOW_R, 20); i++;
				m_cards[i] = new Card(i, Card.COLOR_YELLOW, Card.VAL_R, Card.ID_YELLOW_R, 20); i++;
				m_cards[i] = new Card(i, Card.COLOR_YELLOW, Card.VAL_R_SKIP, Card.ID_YELLOW_R_SKIP, 40); i++;
				m_cards[i] = new Card(i, Card.COLOR_YELLOW, Card.VAL_R_SKIP, Card.ID_YELLOW_R_SKIP, 40); i++;

				m_cards[i] = new Card(i, Card.COLOR_WILD, Card.VAL_WILD, Card.ID_WILD, 50); i++;
				m_cards[i] = new Card(i, Card.COLOR_WILD, Card.VAL_WILD, Card.ID_WILD, 50); i++;
				m_cards[i] = new Card(i, Card.COLOR_WILD, Card.VAL_WILD, Card.ID_WILD, 50); i++;
				m_cards[i] = new Card(i, Card.COLOR_WILD, Card.VAL_WILD, Card.ID_WILD, 50); i++;

				m_cards[i] = new Card(i, Card.COLOR_WILD, Card.VAL_WILD_DRAWFOUR, Card.ID_WILD_DRAWFOUR, 50); i++;
				m_cards[i] = new Card(i, Card.COLOR_WILD, Card.VAL_WILD_DRAWFOUR, Card.ID_WILD_DRAWFOUR, 50); i++;
				m_cards[i] = new Card(i, Card.COLOR_WILD, Card.VAL_WILD_DRAWFOUR, Card.ID_WILD_DRAWFOUR, 50); i++;
				m_cards[i] = new Card(i, Card.COLOR_WILD, Card.VAL_WILD_DRAWFOUR, Card.ID_WILD_DRAWFOUR, 50); i++;
				m_cards[i] = new Card(i, Card.COLOR_WILD, Card.VAL_WILD_DRAWFOUR, Card.ID_WILD_DRAWFOUR, 50); i++;
				m_cards[i] = new Card(i, Card.COLOR_WILD, Card.VAL_WILD_DRAWFOUR, Card.ID_WILD_DRAWFOUR, 50); i++;
				m_cards[i] = new Card(i, Card.COLOR_WILD, Card.VAL_WILD_DRAWFOUR, Card.ID_WILD_DRAWFOUR, 50); i++;
				m_cards[i] = new Card(i, Card.COLOR_WILD, Card.VAL_WILD_DRAWFOUR, Card.ID_WILD_DRAWFOUR, 50); i++;

				m_cards[i] = new Card(i, Card.COLOR_WILD, Card.VAL_WILD_DRAWFOUR, Card.ID_WILD_HD, 100); i++;
				m_cards[i] = new Card(i, Card.COLOR_WILD, Card.VAL_WILD_DRAWFOUR, Card.ID_WILD_DB, 100); i++;
				m_cards[i] = new Card(i, Card.COLOR_WILD, Card.VAL_WILD_DRAWFOUR, Card.ID_WILD_HOS, 0); i++;
				m_cards[i] = new Card(i, Card.COLOR_WILD, Card.VAL_WILD_DRAWFOUR, Card.ID_WILD_MYSTERY, 0); i++;
			}
		}
		else {
			if (oneDeck) {
				m_cards[i] = new Card(i, Card.COLOR_RED, 0, Card.ID_RED_0, 0); i++;
				m_cards[i] = new Card(i, Card.COLOR_RED, 1, Card.ID_RED_1, 1); i++;
				m_cards[i] = new Card(i, Card.COLOR_RED, 1, Card.ID_RED_1, 1); i++;
				m_cards[i] = new Card(i, Card.COLOR_RED, 2, Card.ID_RED_2, 2); i++;
				m_cards[i] = new Card(i, Card.COLOR_RED, 2, Card.ID_RED_2, 2); i++;
				m_cards[i] = new Card(i, Card.COLOR_RED, 3, Card.ID_RED_3, 3); i++;
				m_cards[i] = new Card(i, Card.COLOR_RED, 3, Card.ID_RED_3, 3); i++;
				m_cards[i] = new Card(i, Card.COLOR_RED, 4, Card.ID_RED_4, 4); i++;
				m_cards[i] = new Card(i, Card.COLOR_RED, 4, Card.ID_RED_4, 4); i++;
				m_cards[i] = new Card(i, Card.COLOR_RED, 5, Card.ID_RED_5, 5); i++;
				m_cards[i] = new Card(i, Card.COLOR_RED, 5, Card.ID_RED_5, 5); i++;
				m_cards[i] = new Card(i, Card.COLOR_RED, 6, Card.ID_RED_6, 6); i++;
				m_cards[i] = new Card(i, Card.COLOR_RED, 6, Card.ID_RED_6, 6); i++;
				m_cards[i] = new Card(i, Card.COLOR_RED, 7, Card.ID_RED_7, 7); i++;
				m_cards[i] = new Card(i, Card.COLOR_RED, 7, Card.ID_RED_7, 7); i++;
				m_cards[i] = new Card(i, Card.COLOR_RED, 8, Card.ID_RED_8, 8); i++;
				m_cards[i] = new Card(i, Card.COLOR_RED, 8, Card.ID_RED_8, 8); i++;
				m_cards[i] = new Card(i, Card.COLOR_RED, 9, Card.ID_RED_9, 9); i++;
				m_cards[i] = new Card(i, Card.COLOR_RED, 9, Card.ID_RED_9, 9); i++;
				m_cards[i] = new Card(i, Card.COLOR_RED, Card.VAL_D, Card.ID_RED_D, 20); i++;
				m_cards[i] = new Card(i, Card.COLOR_RED, Card.VAL_D, Card.ID_RED_D, 20); i++;
				m_cards[i] = new Card(i, Card.COLOR_RED, Card.VAL_S, Card.ID_RED_S, 20); i++;
				m_cards[i] = new Card(i, Card.COLOR_RED, Card.VAL_S, Card.ID_RED_S, 20); i++;
				m_cards[i] = new Card(i, Card.COLOR_RED, Card.VAL_R, Card.ID_RED_R, 20); i++;
				m_cards[i] = new Card(i, Card.COLOR_RED, Card.VAL_R, Card.ID_RED_R, 20); i++;

				m_cards[i] = new Card(i, Card.COLOR_GREEN, 0, Card.ID_GREEN_0, 0); i++;
				m_cards[i] = new Card(i, Card.COLOR_GREEN, 1, Card.ID_GREEN_1, 1); i++;
				m_cards[i] = new Card(i, Card.COLOR_GREEN, 1, Card.ID_GREEN_1, 1); i++;
				m_cards[i] = new Card(i, Card.COLOR_GREEN, 2, Card.ID_GREEN_2, 2); i++;
				m_cards[i] = new Card(i, Card.COLOR_GREEN, 2, Card.ID_GREEN_2, 2); i++;
				m_cards[i] = new Card(i, Card.COLOR_GREEN, 3, Card.ID_GREEN_3, 3); i++;
				m_cards[i] = new Card(i, Card.COLOR_GREEN, 3, Card.ID_GREEN_3, 3); i++;
				m_cards[i] = new Card(i, Card.COLOR_GREEN, 4, Card.ID_GREEN_4, 4); i++;
				m_cards[i] = new Card(i, Card.COLOR_GREEN, 4, Card.ID_GREEN_4, 4); i++;
				m_cards[i] = new Card(i, Card.COLOR_GREEN, 5, Card.ID_GREEN_5, 5); i++;
				m_cards[i] = new Card(i, Card.COLOR_GREEN, 5, Card.ID_GREEN_5, 5); i++;
				m_cards[i] = new Card(i, Card.COLOR_GREEN, 6, Card.ID_GREEN_6, 6); i++;
				m_cards[i] = new Card(i, Card.COLOR_GREEN, 6, Card.ID_GREEN_6, 6); i++;
				m_cards[i] = new Card(i, Card.COLOR_GREEN, 7, Card.ID_GREEN_7, 7); i++;
				m_cards[i] = new Card(i, Card.COLOR_GREEN, 7, Card.ID_GREEN_7, 7); i++;
				m_cards[i] = new Card(i, Card.COLOR_GREEN, 8, Card.ID_GREEN_8, 8); i++;
				m_cards[i] = new Card(i, Card.COLOR_GREEN, 8, Card.ID_GREEN_8, 8); i++;
				m_cards[i] = new Card(i, Card.COLOR_GREEN, 9, Card.ID_GREEN_9, 9); i++;
				m_cards[i] = new Card(i, Card.COLOR_GREEN, 9, Card.ID_GREEN_9, 9); i++;
				m_cards[i] = new Card(i, Card.COLOR_GREEN, Card.VAL_D, Card.ID_GREEN_D, 20); i++;
				m_cards[i] = new Card(i, Card.COLOR_GREEN, Card.VAL_D, Card.ID_GREEN_D, 20); i++;
				m_cards[i] = new Card(i, Card.COLOR_GREEN, Card.VAL_S, Card.ID_GREEN_S, 20); i++;
				m_cards[i] = new Card(i, Card.COLOR_GREEN, Card.VAL_S, Card.ID_GREEN_S, 20); i++;
				m_cards[i] = new Card(i, Card.COLOR_GREEN, Card.VAL_R, Card.ID_GREEN_R, 20); i++;
				m_cards[i] = new Card(i, Card.COLOR_GREEN, Card.VAL_R, Card.ID_GREEN_R, 20); i++;

				m_cards[i] = new Card(i, Card.COLOR_BLUE, 0, Card.ID_BLUE_0, 0); i++;
				m_cards[i] = new Card(i, Card.COLOR_BLUE, 1, Card.ID_BLUE_1, 1); i++;
				m_cards[i] = new Card(i, Card.COLOR_BLUE, 1, Card.ID_BLUE_1, 1); i++;
				m_cards[i] = new Card(i, Card.COLOR_BLUE, 2, Card.ID_BLUE_2, 2); i++;
				m_cards[i] = new Card(i, Card.COLOR_BLUE, 2, Card.ID_BLUE_2, 2); i++;
				m_cards[i] = new Card(i, Card.COLOR_BLUE, 3, Card.ID_BLUE_3, 3); i++;
				m_cards[i] = new Card(i, Card.COLOR_BLUE, 3, Card.ID_BLUE_3, 3); i++;
				m_cards[i] = new Card(i, Card.COLOR_BLUE, 4, Card.ID_BLUE_4, 4); i++;
				m_cards[i] = new Card(i, Card.COLOR_BLUE, 4, Card.ID_BLUE_4, 4); i++;
				m_cards[i] = new Card(i, Card.COLOR_BLUE, 5, Card.ID_BLUE_5, 5); i++;
				m_cards[i] = new Card(i, Card.COLOR_BLUE, 5, Card.ID_BLUE_5, 5); i++;
				m_cards[i] = new Card(i, Card.COLOR_BLUE, 6, Card.ID_BLUE_6, 6); i++;
				m_cards[i] = new Card(i, Card.COLOR_BLUE, 6, Card.ID_BLUE_6, 6); i++;
				m_cards[i] = new Card(i, Card.COLOR_BLUE, 7, Card.ID_BLUE_7, 7); i++;
				m_cards[i] = new Card(i, Card.COLOR_BLUE, 7, Card.ID_BLUE_7, 7); i++;
				m_cards[i] = new Card(i, Card.COLOR_BLUE, 8, Card.ID_BLUE_8, 8); i++;
				m_cards[i] = new Card(i, Card.COLOR_BLUE, 8, Card.ID_BLUE_8, 8); i++;
				m_cards[i] = new Card(i, Card.COLOR_BLUE, 9, Card.ID_BLUE_9, 9); i++;
				m_cards[i] = new Card(i, Card.COLOR_BLUE, 9, Card.ID_BLUE_9, 9); i++;
				m_cards[i] = new Card(i, Card.COLOR_BLUE, Card.VAL_D, Card.ID_BLUE_D, 20); i++;
				m_cards[i] = new Card(i, Card.COLOR_BLUE, Card.VAL_D, Card.ID_BLUE_D, 20); i++;
				m_cards[i] = new Card(i, Card.COLOR_BLUE, Card.VAL_S, Card.ID_BLUE_S, 20); i++;
				m_cards[i] = new Card(i, Card.COLOR_BLUE, Card.VAL_S, Card.ID_BLUE_S, 20); i++;
				m_cards[i] = new Card(i, Card.COLOR_BLUE, Card.VAL_R, Card.ID_BLUE_R, 20); i++;
				m_cards[i] = new Card(i, Card.COLOR_BLUE, Card.VAL_R, Card.ID_BLUE_R, 20); i++;

				m_cards[i] = new Card(i, Card.COLOR_YELLOW, 0, Card.ID_YELLOW_0, 0); i++;
				m_cards[i] = new Card(i, Card.COLOR_YELLOW, 1, Card.ID_YELLOW_1, 1); i++;
				m_cards[i] = new Card(i, Card.COLOR_YELLOW, 1, Card.ID_YELLOW_1, 1); i++;
				m_cards[i] = new Card(i, Card.COLOR_YELLOW, 2, Card.ID_YELLOW_2, 2); i++;
				m_cards[i] = new Card(i, Card.COLOR_YELLOW, 2, Card.ID_YELLOW_2, 2); i++;
				m_cards[i] = new Card(i, Card.COLOR_YELLOW, 3, Card.ID_YELLOW_3, 3); i++;
				m_cards[i] = new Card(i, Card.COLOR_YELLOW, 3, Card.ID_YELLOW_3, 3); i++;
				m_cards[i] = new Card(i, Card.COLOR_YELLOW, 4, Card.ID_YELLOW_4, 4); i++;
				m_cards[i] = new Card(i, Card.COLOR_YELLOW, 4, Card.ID_YELLOW_4, 4); i++;
				m_cards[i] = new Card(i, Card.COLOR_YELLOW, 5, Card.ID_YELLOW_5, 5); i++;
				m_cards[i] = new Card(i, Card.COLOR_YELLOW, 5, Card.ID_YELLOW_5, 5); i++;
				m_cards[i] = new Card(i, Card.COLOR_YELLOW, 6, Card.ID_YELLOW_6, 6); i++;
				m_cards[i] = new Card(i, Card.COLOR_YELLOW, 6, Card.ID_YELLOW_6, 6); i++;
				m_cards[i] = new Card(i, Card.COLOR_YELLOW, 7, Card.ID_YELLOW_7, 7); i++;
				m_cards[i] = new Card(i, Card.COLOR_YELLOW, 7, Card.ID_YELLOW_7, 7); i++;
				m_cards[i] = new Card(i, Card.COLOR_YELLOW, 8, Card.ID_YELLOW_8, 8); i++;
				m_cards[i] = new Card(i, Card.COLOR_YELLOW, 8, Card.ID_YELLOW_8, 8); i++;
				m_cards[i] = new Card(i, Card.COLOR_YELLOW, 9, Card.ID_YELLOW_9, 9); i++;
				m_cards[i] = new Card(i, Card.COLOR_YELLOW, 9, Card.ID_YELLOW_9, 9); i++;
				m_cards[i] = new Card(i, Card.COLOR_YELLOW, Card.VAL_D, Card.ID_YELLOW_D, 20); i++;
				m_cards[i] = new Card(i, Card.COLOR_YELLOW, Card.VAL_D, Card.ID_YELLOW_D, 20); i++;
				m_cards[i] = new Card(i, Card.COLOR_YELLOW, Card.VAL_S, Card.ID_YELLOW_S, 20); i++;
				m_cards[i] = new Card(i, Card.COLOR_YELLOW, Card.VAL_S, Card.ID_YELLOW_S, 20); i++;
				m_cards[i] = new Card(i, Card.COLOR_YELLOW, Card.VAL_R, Card.ID_YELLOW_R, 20); i++;
				m_cards[i] = new Card(i, Card.COLOR_YELLOW, Card.VAL_R, Card.ID_YELLOW_R, 20); i++;

				m_cards[i] = new Card(i, Card.COLOR_WILD, Card.VAL_WILD, Card.ID_WILD, 50); i++;
				m_cards[i] = new Card(i, Card.COLOR_WILD, Card.VAL_WILD, Card.ID_WILD, 50); i++;
				m_cards[i] = new Card(i, Card.COLOR_WILD, Card.VAL_WILD, Card.ID_WILD, 50); i++;
				m_cards[i] = new Card(i, Card.COLOR_WILD, Card.VAL_WILD, Card.ID_WILD, 50); i++;

				m_cards[i] = new Card(i, Card.COLOR_WILD, Card.VAL_WILD_DRAWFOUR, Card.ID_WILD_DRAWFOUR, 50); i++;
				m_cards[i] = new Card(i, Card.COLOR_WILD, Card.VAL_WILD_DRAWFOUR, Card.ID_WILD_DRAWFOUR, 50); i++;
				m_cards[i] = new Card(i, Card.COLOR_WILD, Card.VAL_WILD_DRAWFOUR, Card.ID_WILD_DRAWFOUR, 50); i++;
				m_cards[i] = new Card(i, Card.COLOR_WILD, Card.VAL_WILD_DRAWFOUR, Card.ID_WILD_DRAWFOUR, 50); i++;
			}
			else {
				m_cards[i] = new Card(i, Card.COLOR_RED, 0, Card.ID_RED_0, 0); i++;
				m_cards[i] = new Card(i, Card.COLOR_RED, 0, Card.ID_RED_0, 0); i++;
				m_cards[i] = new Card(i, Card.COLOR_RED, 1, Card.ID_RED_1, 1); i++;
				m_cards[i] = new Card(i, Card.COLOR_RED, 1, Card.ID_RED_1, 1); i++;
				m_cards[i] = new Card(i, Card.COLOR_RED, 1, Card.ID_RED_1, 1); i++;
				m_cards[i] = new Card(i, Card.COLOR_RED, 1, Card.ID_RED_1, 1); i++;
				m_cards[i] = new Card(i, Card.COLOR_RED, 2, Card.ID_RED_2, 2); i++;
				m_cards[i] = new Card(i, Card.COLOR_RED, 2, Card.ID_RED_2, 2); i++;
				m_cards[i] = new Card(i, Card.COLOR_RED, 2, Card.ID_RED_2, 2); i++;
				m_cards[i] = new Card(i, Card.COLOR_RED, 2, Card.ID_RED_2, 2); i++;
				m_cards[i] = new Card(i, Card.COLOR_RED, 3, Card.ID_RED_3, 3); i++;
				m_cards[i] = new Card(i, Card.COLOR_RED, 3, Card.ID_RED_3, 3); i++;
				m_cards[i] = new Card(i, Card.COLOR_RED, 3, Card.ID_RED_3, 3); i++;
				m_cards[i] = new Card(i, Card.COLOR_RED, 3, Card.ID_RED_3, 3); i++;
				m_cards[i] = new Card(i, Card.COLOR_RED, 4, Card.ID_RED_4, 4); i++;
				m_cards[i] = new Card(i, Card.COLOR_RED, 4, Card.ID_RED_4, 4); i++;
				m_cards[i] = new Card(i, Card.COLOR_RED, 4, Card.ID_RED_4, 4); i++;
				m_cards[i] = new Card(i, Card.COLOR_RED, 4, Card.ID_RED_4, 4); i++;
				m_cards[i] = new Card(i, Card.COLOR_RED, 5, Card.ID_RED_5, 5); i++;
				m_cards[i] = new Card(i, Card.COLOR_RED, 5, Card.ID_RED_5, 5); i++;
				m_cards[i] = new Card(i, Card.COLOR_RED, 5, Card.ID_RED_5, 5); i++;
				m_cards[i] = new Card(i, Card.COLOR_RED, 5, Card.ID_RED_5, 5); i++;
				m_cards[i] = new Card(i, Card.COLOR_RED, 6, Card.ID_RED_6, 6); i++;
				m_cards[i] = new Card(i, Card.COLOR_RED, 6, Card.ID_RED_6, 6); i++;
				m_cards[i] = new Card(i, Card.COLOR_RED, 6, Card.ID_RED_6, 6); i++;
				m_cards[i] = new Card(i, Card.COLOR_RED, 6, Card.ID_RED_6, 6); i++;
				m_cards[i] = new Card(i, Card.COLOR_RED, 7, Card.ID_RED_7, 7); i++;
				m_cards[i] = new Card(i, Card.COLOR_RED, 7, Card.ID_RED_7, 7); i++;
				m_cards[i] = new Card(i, Card.COLOR_RED, 7, Card.ID_RED_7, 7); i++;
				m_cards[i] = new Card(i, Card.COLOR_RED, 7, Card.ID_RED_7, 7); i++;
				m_cards[i] = new Card(i, Card.COLOR_RED, 8, Card.ID_RED_8, 8); i++;
				m_cards[i] = new Card(i, Card.COLOR_RED, 8, Card.ID_RED_8, 8); i++;
				m_cards[i] = new Card(i, Card.COLOR_RED, 8, Card.ID_RED_8, 8); i++;
				m_cards[i] = new Card(i, Card.COLOR_RED, 8, Card.ID_RED_8, 8); i++;
				m_cards[i] = new Card(i, Card.COLOR_RED, 9, Card.ID_RED_9, 9); i++;
				m_cards[i] = new Card(i, Card.COLOR_RED, 9, Card.ID_RED_9, 9); i++;
				m_cards[i] = new Card(i, Card.COLOR_RED, 9, Card.ID_RED_9, 9); i++;
				m_cards[i] = new Card(i, Card.COLOR_RED, 9, Card.ID_RED_9, 9); i++;
				m_cards[i] = new Card(i, Card.COLOR_RED, Card.VAL_D, Card.ID_RED_D, 20); i++;
				m_cards[i] = new Card(i, Card.COLOR_RED, Card.VAL_D, Card.ID_RED_D, 20); i++;
				m_cards[i] = new Card(i, Card.COLOR_RED, Card.VAL_D, Card.ID_RED_D, 20); i++;
				m_cards[i] = new Card(i, Card.COLOR_RED, Card.VAL_D, Card.ID_RED_D, 20); i++;
				m_cards[i] = new Card(i, Card.COLOR_RED, Card.VAL_S, Card.ID_RED_S, 20); i++;
				m_cards[i] = new Card(i, Card.COLOR_RED, Card.VAL_S, Card.ID_RED_S, 20); i++;
				m_cards[i] = new Card(i, Card.COLOR_RED, Card.VAL_S, Card.ID_RED_S, 20); i++;
				m_cards[i] = new Card(i, Card.COLOR_RED, Card.VAL_S, Card.ID_RED_S, 20); i++;
				m_cards[i] = new Card(i, Card.COLOR_RED, Card.VAL_R, Card.ID_RED_R, 20); i++;
				m_cards[i] = new Card(i, Card.COLOR_RED, Card.VAL_R, Card.ID_RED_R, 20); i++;
				m_cards[i] = new Card(i, Card.COLOR_RED, Card.VAL_R, Card.ID_RED_R, 20); i++;
				m_cards[i] = new Card(i, Card.COLOR_RED, Card.VAL_R, Card.ID_RED_R, 20); i++;

				m_cards[i] = new Card(i, Card.COLOR_GREEN, 0, Card.ID_GREEN_0, 0); i++;
				m_cards[i] = new Card(i, Card.COLOR_GREEN, 0, Card.ID_GREEN_0, 0); i++;
				m_cards[i] = new Card(i, Card.COLOR_GREEN, 1, Card.ID_GREEN_1, 1); i++;
				m_cards[i] = new Card(i, Card.COLOR_GREEN, 1, Card.ID_GREEN_1, 1); i++;
				m_cards[i] = new Card(i, Card.COLOR_GREEN, 1, Card.ID_GREEN_1, 1); i++;
				m_cards[i] = new Card(i, Card.COLOR_GREEN, 1, Card.ID_GREEN_1, 1); i++;
				m_cards[i] = new Card(i, Card.COLOR_GREEN, 2, Card.ID_GREEN_2, 2); i++;
				m_cards[i] = new Card(i, Card.COLOR_GREEN, 2, Card.ID_GREEN_2, 2); i++;
				m_cards[i] = new Card(i, Card.COLOR_GREEN, 2, Card.ID_GREEN_2, 2); i++;
				m_cards[i] = new Card(i, Card.COLOR_GREEN, 2, Card.ID_GREEN_2, 2); i++;
				m_cards[i] = new Card(i, Card.COLOR_GREEN, 3, Card.ID_GREEN_3, 3); i++;
				m_cards[i] = new Card(i, Card.COLOR_GREEN, 3, Card.ID_GREEN_3, 3); i++;
				m_cards[i] = new Card(i, Card.COLOR_GREEN, 3, Card.ID_GREEN_3, 3); i++;
				m_cards[i] = new Card(i, Card.COLOR_GREEN, 3, Card.ID_GREEN_3, 3); i++;
				m_cards[i] = new Card(i, Card.COLOR_GREEN, 4, Card.ID_GREEN_4, 4); i++;
				m_cards[i] = new Card(i, Card.COLOR_GREEN, 4, Card.ID_GREEN_4, 4); i++;
				m_cards[i] = new Card(i, Card.COLOR_GREEN, 4, Card.ID_GREEN_4, 4); i++;
				m_cards[i] = new Card(i, Card.COLOR_GREEN, 4, Card.ID_GREEN_4, 4); i++;
				m_cards[i] = new Card(i, Card.COLOR_GREEN, 5, Card.ID_GREEN_5, 5); i++;
				m_cards[i] = new Card(i, Card.COLOR_GREEN, 5, Card.ID_GREEN_5, 5); i++;
				m_cards[i] = new Card(i, Card.COLOR_GREEN, 5, Card.ID_GREEN_5, 5); i++;
				m_cards[i] = new Card(i, Card.COLOR_GREEN, 5, Card.ID_GREEN_5, 5); i++;
				m_cards[i] = new Card(i, Card.COLOR_GREEN, 6, Card.ID_GREEN_6, 6); i++;
				m_cards[i] = new Card(i, Card.COLOR_GREEN, 6, Card.ID_GREEN_6, 6); i++;
				m_cards[i] = new Card(i, Card.COLOR_GREEN, 6, Card.ID_GREEN_6, 6); i++;
				m_cards[i] = new Card(i, Card.COLOR_GREEN, 6, Card.ID_GREEN_6, 6); i++;
				m_cards[i] = new Card(i, Card.COLOR_GREEN, 7, Card.ID_GREEN_7, 7); i++;
				m_cards[i] = new Card(i, Card.COLOR_GREEN, 7, Card.ID_GREEN_7, 7); i++;
				m_cards[i] = new Card(i, Card.COLOR_GREEN, 7, Card.ID_GREEN_7, 7); i++;
				m_cards[i] = new Card(i, Card.COLOR_GREEN, 7, Card.ID_GREEN_7, 7); i++;
				m_cards[i] = new Card(i, Card.COLOR_GREEN, 8, Card.ID_GREEN_8, 8); i++;
				m_cards[i] = new Card(i, Card.COLOR_GREEN, 8, Card.ID_GREEN_8, 8); i++;
				m_cards[i] = new Card(i, Card.COLOR_GREEN, 8, Card.ID_GREEN_8, 8); i++;
				m_cards[i] = new Card(i, Card.COLOR_GREEN, 8, Card.ID_GREEN_8, 8); i++;
				m_cards[i] = new Card(i, Card.COLOR_GREEN, 9, Card.ID_GREEN_9, 9); i++;
				m_cards[i] = new Card(i, Card.COLOR_GREEN, 9, Card.ID_GREEN_9, 9); i++;
				m_cards[i] = new Card(i, Card.COLOR_GREEN, 9, Card.ID_GREEN_9, 9); i++;
				m_cards[i] = new Card(i, Card.COLOR_GREEN, 9, Card.ID_GREEN_9, 9); i++;
				m_cards[i] = new Card(i, Card.COLOR_GREEN, Card.VAL_D, Card.ID_GREEN_D, 20); i++;
				m_cards[i] = new Card(i, Card.COLOR_GREEN, Card.VAL_D, Card.ID_GREEN_D, 20); i++;
				m_cards[i] = new Card(i, Card.COLOR_GREEN, Card.VAL_D, Card.ID_GREEN_D, 20); i++;
				m_cards[i] = new Card(i, Card.COLOR_GREEN, Card.VAL_D, Card.ID_GREEN_D, 20); i++;
				m_cards[i] = new Card(i, Card.COLOR_GREEN, Card.VAL_S, Card.ID_GREEN_S, 20); i++;
				m_cards[i] = new Card(i, Card.COLOR_GREEN, Card.VAL_S, Card.ID_GREEN_S, 20); i++;
				m_cards[i] = new Card(i, Card.COLOR_GREEN, Card.VAL_S, Card.ID_GREEN_S, 20); i++;
				m_cards[i] = new Card(i, Card.COLOR_GREEN, Card.VAL_S, Card.ID_GREEN_S, 20); i++;
				m_cards[i] = new Card(i, Card.COLOR_GREEN, Card.VAL_R, Card.ID_GREEN_R, 20); i++;
				m_cards[i] = new Card(i, Card.COLOR_GREEN, Card.VAL_R, Card.ID_GREEN_R, 20); i++;
				m_cards[i] = new Card(i, Card.COLOR_GREEN, Card.VAL_R, Card.ID_GREEN_R, 20); i++;
				m_cards[i] = new Card(i, Card.COLOR_GREEN, Card.VAL_R, Card.ID_GREEN_R, 20); i++;

				m_cards[i] = new Card(i, Card.COLOR_BLUE, 0, Card.ID_BLUE_0, 0); i++;
				m_cards[i] = new Card(i, Card.COLOR_BLUE, 0, Card.ID_BLUE_0, 0); i++;
				m_cards[i] = new Card(i, Card.COLOR_BLUE, 1, Card.ID_BLUE_1, 1); i++;
				m_cards[i] = new Card(i, Card.COLOR_BLUE, 1, Card.ID_BLUE_1, 1); i++;
				m_cards[i] = new Card(i, Card.COLOR_BLUE, 1, Card.ID_BLUE_1, 1); i++;
				m_cards[i] = new Card(i, Card.COLOR_BLUE, 1, Card.ID_BLUE_1, 1); i++;
				m_cards[i] = new Card(i, Card.COLOR_BLUE, 2, Card.ID_BLUE_2, 2); i++;
				m_cards[i] = new Card(i, Card.COLOR_BLUE, 2, Card.ID_BLUE_2, 2); i++;
				m_cards[i] = new Card(i, Card.COLOR_BLUE, 2, Card.ID_BLUE_2, 2); i++;
				m_cards[i] = new Card(i, Card.COLOR_BLUE, 2, Card.ID_BLUE_2, 2); i++;
				m_cards[i] = new Card(i, Card.COLOR_BLUE, 3, Card.ID_BLUE_3, 3); i++;
				m_cards[i] = new Card(i, Card.COLOR_BLUE, 3, Card.ID_BLUE_3, 3); i++;
				m_cards[i] = new Card(i, Card.COLOR_BLUE, 3, Card.ID_BLUE_3, 3); i++;
				m_cards[i] = new Card(i, Card.COLOR_BLUE, 3, Card.ID_BLUE_3, 3); i++;
				m_cards[i] = new Card(i, Card.COLOR_BLUE, 4, Card.ID_BLUE_4, 4); i++;
				m_cards[i] = new Card(i, Card.COLOR_BLUE, 4, Card.ID_BLUE_4, 4); i++;
				m_cards[i] = new Card(i, Card.COLOR_BLUE, 4, Card.ID_BLUE_4, 4); i++;
				m_cards[i] = new Card(i, Card.COLOR_BLUE, 4, Card.ID_BLUE_4, 4); i++;
				m_cards[i] = new Card(i, Card.COLOR_BLUE, 5, Card.ID_BLUE_5, 5); i++;
				m_cards[i] = new Card(i, Card.COLOR_BLUE, 5, Card.ID_BLUE_5, 5); i++;
				m_cards[i] = new Card(i, Card.COLOR_BLUE, 5, Card.ID_BLUE_5, 5); i++;
				m_cards[i] = new Card(i, Card.COLOR_BLUE, 5, Card.ID_BLUE_5, 5); i++;
				m_cards[i] = new Card(i, Card.COLOR_BLUE, 6, Card.ID_BLUE_6, 6); i++;
				m_cards[i] = new Card(i, Card.COLOR_BLUE, 6, Card.ID_BLUE_6, 6); i++;
				m_cards[i] = new Card(i, Card.COLOR_BLUE, 6, Card.ID_BLUE_6, 6); i++;
				m_cards[i] = new Card(i, Card.COLOR_BLUE, 6, Card.ID_BLUE_6, 6); i++;
				m_cards[i] = new Card(i, Card.COLOR_BLUE, 7, Card.ID_BLUE_7, 7); i++;
				m_cards[i] = new Card(i, Card.COLOR_BLUE, 7, Card.ID_BLUE_7, 7); i++;
				m_cards[i] = new Card(i, Card.COLOR_BLUE, 7, Card.ID_BLUE_7, 7); i++;
				m_cards[i] = new Card(i, Card.COLOR_BLUE, 7, Card.ID_BLUE_7, 7); i++;
				m_cards[i] = new Card(i, Card.COLOR_BLUE, 8, Card.ID_BLUE_8, 8); i++;
				m_cards[i] = new Card(i, Card.COLOR_BLUE, 8, Card.ID_BLUE_8, 8); i++;
				m_cards[i] = new Card(i, Card.COLOR_BLUE, 8, Card.ID_BLUE_8, 8); i++;
				m_cards[i] = new Card(i, Card.COLOR_BLUE, 8, Card.ID_BLUE_8, 8); i++;
				m_cards[i] = new Card(i, Card.COLOR_BLUE, 9, Card.ID_BLUE_9, 9); i++;
				m_cards[i] = new Card(i, Card.COLOR_BLUE, 9, Card.ID_BLUE_9, 9); i++;
				m_cards[i] = new Card(i, Card.COLOR_BLUE, 9, Card.ID_BLUE_9, 9); i++;
				m_cards[i] = new Card(i, Card.COLOR_BLUE, 9, Card.ID_BLUE_9, 9); i++;
				m_cards[i] = new Card(i, Card.COLOR_BLUE, Card.VAL_D, Card.ID_BLUE_D, 20); i++;
				m_cards[i] = new Card(i, Card.COLOR_BLUE, Card.VAL_D, Card.ID_BLUE_D, 20); i++;
				m_cards[i] = new Card(i, Card.COLOR_BLUE, Card.VAL_D, Card.ID_BLUE_D, 20); i++;
				m_cards[i] = new Card(i, Card.COLOR_BLUE, Card.VAL_D, Card.ID_BLUE_D, 20); i++;
				m_cards[i] = new Card(i, Card.COLOR_BLUE, Card.VAL_S, Card.ID_BLUE_S, 20); i++;
				m_cards[i] = new Card(i, Card.COLOR_BLUE, Card.VAL_S, Card.ID_BLUE_S, 20); i++;
				m_cards[i] = new Card(i, Card.COLOR_BLUE, Card.VAL_S, Card.ID_BLUE_S, 20); i++;
				m_cards[i] = new Card(i, Card.COLOR_BLUE, Card.VAL_S, Card.ID_BLUE_S, 20); i++;
				m_cards[i] = new Card(i, Card.COLOR_BLUE, Card.VAL_R, Card.ID_BLUE_R, 20); i++;
				m_cards[i] = new Card(i, Card.COLOR_BLUE, Card.VAL_R, Card.ID_BLUE_R, 20); i++;
				m_cards[i] = new Card(i, Card.COLOR_BLUE, Card.VAL_R, Card.ID_BLUE_R, 20); i++;
				m_cards[i] = new Card(i, Card.COLOR_BLUE, Card.VAL_R, Card.ID_BLUE_R, 20); i++;

				m_cards[i] = new Card(i, Card.COLOR_YELLOW, 0, Card.ID_YELLOW_0, 0); i++;
				m_cards[i] = new Card(i, Card.COLOR_YELLOW, 0, Card.ID_YELLOW_0, 0); i++;
				m_cards[i] = new Card(i, Card.COLOR_YELLOW, 1, Card.ID_YELLOW_1, 1); i++;
				m_cards[i] = new Card(i, Card.COLOR_YELLOW, 1, Card.ID_YELLOW_1, 1); i++;
				m_cards[i] = new Card(i, Card.COLOR_YELLOW, 1, Card.ID_YELLOW_1, 1); i++;
				m_cards[i] = new Card(i, Card.COLOR_YELLOW, 1, Card.ID_YELLOW_1, 1); i++;
				m_cards[i] = new Card(i, Card.COLOR_YELLOW, 2, Card.ID_YELLOW_2, 2); i++;
				m_cards[i] = new Card(i, Card.COLOR_YELLOW, 2, Card.ID_YELLOW_2, 2); i++;
				m_cards[i] = new Card(i, Card.COLOR_YELLOW, 2, Card.ID_YELLOW_2, 2); i++;
				m_cards[i] = new Card(i, Card.COLOR_YELLOW, 2, Card.ID_YELLOW_2, 2); i++;
				m_cards[i] = new Card(i, Card.COLOR_YELLOW, 3, Card.ID_YELLOW_3, 3); i++;
				m_cards[i] = new Card(i, Card.COLOR_YELLOW, 3, Card.ID_YELLOW_3, 3); i++;
				m_cards[i] = new Card(i, Card.COLOR_YELLOW, 3, Card.ID_YELLOW_3, 3); i++;
				m_cards[i] = new Card(i, Card.COLOR_YELLOW, 3, Card.ID_YELLOW_3, 3); i++;
				m_cards[i] = new Card(i, Card.COLOR_YELLOW, 4, Card.ID_YELLOW_4, 4); i++;
				m_cards[i] = new Card(i, Card.COLOR_YELLOW, 4, Card.ID_YELLOW_4, 4); i++;
				m_cards[i] = new Card(i, Card.COLOR_YELLOW, 4, Card.ID_YELLOW_4, 4); i++;
				m_cards[i] = new Card(i, Card.COLOR_YELLOW, 4, Card.ID_YELLOW_4, 4); i++;
				m_cards[i] = new Card(i, Card.COLOR_YELLOW, 5, Card.ID_YELLOW_5, 5); i++;
				m_cards[i] = new Card(i, Card.COLOR_YELLOW, 5, Card.ID_YELLOW_5, 5); i++;
				m_cards[i] = new Card(i, Card.COLOR_YELLOW, 5, Card.ID_YELLOW_5, 5); i++;
				m_cards[i] = new Card(i, Card.COLOR_YELLOW, 5, Card.ID_YELLOW_5, 5); i++;
				m_cards[i] = new Card(i, Card.COLOR_YELLOW, 6, Card.ID_YELLOW_6, 6); i++;
				m_cards[i] = new Card(i, Card.COLOR_YELLOW, 6, Card.ID_YELLOW_6, 6); i++;
				m_cards[i] = new Card(i, Card.COLOR_YELLOW, 6, Card.ID_YELLOW_6, 6); i++;
				m_cards[i] = new Card(i, Card.COLOR_YELLOW, 6, Card.ID_YELLOW_6, 6); i++;
				m_cards[i] = new Card(i, Card.COLOR_YELLOW, 7, Card.ID_YELLOW_7, 7); i++;
				m_cards[i] = new Card(i, Card.COLOR_YELLOW, 7, Card.ID_YELLOW_7, 7); i++;
				m_cards[i] = new Card(i, Card.COLOR_YELLOW, 7, Card.ID_YELLOW_7, 7); i++;
				m_cards[i] = new Card(i, Card.COLOR_YELLOW, 7, Card.ID_YELLOW_7, 7); i++;
				m_cards[i] = new Card(i, Card.COLOR_YELLOW, 8, Card.ID_YELLOW_8, 8); i++;
				m_cards[i] = new Card(i, Card.COLOR_YELLOW, 8, Card.ID_YELLOW_8, 8); i++;
				m_cards[i] = new Card(i, Card.COLOR_YELLOW, 8, Card.ID_YELLOW_8, 8); i++;
				m_cards[i] = new Card(i, Card.COLOR_YELLOW, 8, Card.ID_YELLOW_8, 8); i++;
				m_cards[i] = new Card(i, Card.COLOR_YELLOW, 9, Card.ID_YELLOW_9, 9); i++;
				m_cards[i] = new Card(i, Card.COLOR_YELLOW, 9, Card.ID_YELLOW_9, 9); i++;
				m_cards[i] = new Card(i, Card.COLOR_YELLOW, 9, Card.ID_YELLOW_9, 9); i++;
				m_cards[i] = new Card(i, Card.COLOR_YELLOW, 9, Card.ID_YELLOW_9, 9); i++;
				m_cards[i] = new Card(i, Card.COLOR_YELLOW, Card.VAL_D, Card.ID_YELLOW_D, 20); i++;
				m_cards[i] = new Card(i, Card.COLOR_YELLOW, Card.VAL_D, Card.ID_YELLOW_D, 20); i++;
				m_cards[i] = new Card(i, Card.COLOR_YELLOW, Card.VAL_D, Card.ID_YELLOW_D, 20); i++;
				m_cards[i] = new Card(i, Card.COLOR_YELLOW, Card.VAL_D, Card.ID_YELLOW_D, 20); i++;
				m_cards[i] = new Card(i, Card.COLOR_YELLOW, Card.VAL_S, Card.ID_YELLOW_S, 20); i++;
				m_cards[i] = new Card(i, Card.COLOR_YELLOW, Card.VAL_S, Card.ID_YELLOW_S, 20); i++;
				m_cards[i] = new Card(i, Card.COLOR_YELLOW, Card.VAL_S, Card.ID_YELLOW_S, 20); i++;
				m_cards[i] = new Card(i, Card.COLOR_YELLOW, Card.VAL_S, Card.ID_YELLOW_S, 20); i++;
				m_cards[i] = new Card(i, Card.COLOR_YELLOW, Card.VAL_R, Card.ID_YELLOW_R, 20); i++;
				m_cards[i] = new Card(i, Card.COLOR_YELLOW, Card.VAL_R, Card.ID_YELLOW_R, 20); i++;
				m_cards[i] = new Card(i, Card.COLOR_YELLOW, Card.VAL_R, Card.ID_YELLOW_R, 20); i++;
				m_cards[i] = new Card(i, Card.COLOR_YELLOW, Card.VAL_R, Card.ID_YELLOW_R, 20); i++;

				m_cards[i] = new Card(i, Card.COLOR_WILD, Card.VAL_WILD, Card.ID_WILD, 50); i++;
				m_cards[i] = new Card(i, Card.COLOR_WILD, Card.VAL_WILD, Card.ID_WILD, 50); i++;
				m_cards[i] = new Card(i, Card.COLOR_WILD, Card.VAL_WILD, Card.ID_WILD, 50); i++;
				m_cards[i] = new Card(i, Card.COLOR_WILD, Card.VAL_WILD, Card.ID_WILD, 50); i++;
				m_cards[i] = new Card(i, Card.COLOR_WILD, Card.VAL_WILD, Card.ID_WILD, 50); i++;
				m_cards[i] = new Card(i, Card.COLOR_WILD, Card.VAL_WILD, Card.ID_WILD, 50); i++;
				m_cards[i] = new Card(i, Card.COLOR_WILD, Card.VAL_WILD, Card.ID_WILD, 50); i++;
				m_cards[i] = new Card(i, Card.COLOR_WILD, Card.VAL_WILD, Card.ID_WILD, 50); i++;

				m_cards[i] = new Card(i, Card.COLOR_WILD, Card.VAL_WILD_DRAWFOUR, Card.ID_WILD_DRAWFOUR, 50); i++;
				m_cards[i] = new Card(i, Card.COLOR_WILD, Card.VAL_WILD_DRAWFOUR, Card.ID_WILD_DRAWFOUR, 50); i++;
				m_cards[i] = new Card(i, Card.COLOR_WILD, Card.VAL_WILD_DRAWFOUR, Card.ID_WILD_DRAWFOUR, 50); i++;
				m_cards[i] = new Card(i, Card.COLOR_WILD, Card.VAL_WILD_DRAWFOUR, Card.ID_WILD_DRAWFOUR, 50); i++;
				m_cards[i] = new Card(i, Card.COLOR_WILD, Card.VAL_WILD_DRAWFOUR, Card.ID_WILD_DRAWFOUR, 50); i++;
				m_cards[i] = new Card(i, Card.COLOR_WILD, Card.VAL_WILD_DRAWFOUR, Card.ID_WILD_DRAWFOUR, 50); i++;
				m_cards[i] = new Card(i, Card.COLOR_WILD, Card.VAL_WILD_DRAWFOUR, Card.ID_WILD_DRAWFOUR, 50); i++;
				m_cards[i] = new Card(i, Card.COLOR_WILD, Card.VAL_WILD_DRAWFOUR, Card.ID_WILD_DRAWFOUR, 50); i++;
			}
		}
		m_numCards = i;
		
		for (i = 0; i < m_numCards; i++) {
			m_oCards[i] = m_cards[i];
		}
	}


	public void shuffle ()
	{
		int i;
		
		for ( i = 0; i < m_numCards; i++) {
			m_cards[i].setFaceUp(false);
		}

		shuffle (7);
	}

	public void shuffle (int numTimes)
	{
		int i, j, k;
		Card cTemp;

		Random rgen = new Random();
		
		for ( i = 0; i < numTimes; i++) 
		{
			for ( j = 0; j < m_numCards; j++) 
			{
				k = rgen.nextInt(m_numCards);

				cTemp = m_oCards[j];
				m_oCards[j] = m_oCards[k];
				m_oCards[k] = cTemp;
			}
		}
    }


	public Card getCard (int i)
	{
		if (i >= 0 && i < m_numCards) return m_oCards[i];

		return null;
	}


	public Card getCard(int Color, int Value)
	{
		for (int i = 0; i < m_numCards; i++) 
		{
			if ((m_cards[i].getColor() == Color) && (m_cards[i].getValue() == Value))
			{
				return m_cards[i];
			}
		}
		
		return null;
	}
	
	public CardDeck(JSONObject o) throws JSONException
	{
		this();
		
		JSONArray a = o.getJSONArray("cards");
		m_numCards = a.length();
		m_cards = new Card[m_numCards];
		m_oCards = new Card[m_numCards];
		for (int i = 0; i < m_numCards; i++)
		{
			m_cards[i] = new Card (a.getJSONObject(i));
		}
	
		for (int i = 0; i < m_numCards; i++) {
			m_oCards[i] = m_cards[i];
		}
	}
	
	public JSONObject toJSON () throws JSONException
	{
		JSONArray a = new JSONArray ();
		for (int i = 0; i < m_numCards; i++)
		{
			Card c = m_cards[i];
			a.put(c.toJSON());
		}
		
		JSONObject o = new JSONObject ();
		o.put ("cards", a);
		
		return o;
	}
}
