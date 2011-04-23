package com.smorgasbork.hotdeath;

import org.json.*;

import android.content.Context;
import com.smorgasbork.hotdeath.R;

public class Card {

	public static final int COLOR_RED    = 1;
	public static final int COLOR_GREEN  = 2;
	public static final int COLOR_BLUE   = 3;
	public static final int COLOR_YELLOW = 4;
	public static final int COLOR_WILD   = 5;

	public static final int VAL_D              = 11;
	public static final int VAL_S              = 12;
	public static final int VAL_R              = 13;
	public static final int VAL_D_SPREAD       = 14;
	public static final int VAL_S_DOUBLE       = 15;
	public static final int VAL_R_SKIP         = 16;
	public static final int VAL_WILD           = 17;
	public static final int VAL_WILD_DRAWFOUR  = 18;

	public static final int ID_RED_0             = 100;
	public static final int ID_RED_1             = 101;
	public static final int ID_RED_2             = 102;
	public static final int ID_RED_3             = 103;
	public static final int ID_RED_4             = 104;
	public static final int ID_RED_5             = 105;
	public static final int ID_RED_6             = 106;
	public static final int ID_RED_7             = 107;
	public static final int ID_RED_8             = 108;
	public static final int ID_RED_9             = 109;
	public static final int ID_RED_D             = 110;
	public static final int ID_RED_S             = 111;
	public static final int ID_RED_R             = 112;
	public static final int ID_GREEN_0           = 113;
	public static final int ID_GREEN_1           = 114;
	public static final int ID_GREEN_2           = 115;
	public static final int ID_GREEN_3           = 116;
	public static final int ID_GREEN_4           = 117;
	public static final int ID_GREEN_5           = 118;
	public static final int ID_GREEN_6           = 119;
	public static final int ID_GREEN_7           = 120;
	public static final int ID_GREEN_8           = 121;
	public static final int ID_GREEN_9           = 122;
	public static final int ID_GREEN_D           = 123;
	public static final int ID_GREEN_S           = 124;
	public static final int ID_GREEN_R           = 125;
	public static final int ID_BLUE_0            = 126;
	public static final int ID_BLUE_1            = 127;
	public static final int ID_BLUE_2            = 128;
	public static final int ID_BLUE_3            = 129;
	public static final int ID_BLUE_4            = 130;
	public static final int ID_BLUE_5            = 131;
	public static final int ID_BLUE_6            = 132;
	public static final int ID_BLUE_7            = 133;
	public static final int ID_BLUE_8            = 134;
	public static final int ID_BLUE_9            = 135;
	public static final int ID_BLUE_D            = 136;
	public static final int ID_BLUE_S            = 137;
	public static final int ID_BLUE_R            = 138;
	public static final int ID_YELLOW_0          = 139;
	public static final int ID_YELLOW_1          = 140;
	public static final int ID_YELLOW_2          = 141;
	public static final int ID_YELLOW_3          = 142;
	public static final int ID_YELLOW_4          = 143;
	public static final int ID_YELLOW_5          = 144;
	public static final int ID_YELLOW_6          = 145;
	public static final int ID_YELLOW_7          = 146;
	public static final int ID_YELLOW_8          = 147;
	public static final int ID_YELLOW_9          = 148;
	public static final int ID_YELLOW_D          = 149;
	public static final int ID_YELLOW_S          = 150;
	public static final int ID_YELLOW_R          = 151;
	public static final int ID_WILD              = 152;
	public static final int ID_WILD_DRAWFOUR     = 153;
	public static final int ID_WILD_HOS          = 154;
	public static final int ID_WILD_HD           = 155;
	public static final int ID_WILD_MYSTERY      = 156;
	public static final int ID_WILD_DB           = 157;
	public static final int ID_RED_0_HD          = 158;
	public static final int ID_RED_2_GLASNOST    = 159;
	public static final int ID_RED_5_MAGIC       = 160;
	public static final int ID_RED_D_SPREADER    = 161;
	public static final int ID_RED_S_DOUBLE      = 162;
	public static final int ID_RED_R_SKIP        = 163;
	public static final int ID_GREEN_0_QUITTER   = 164;
	public static final int ID_GREEN_3_AIDS      = 165;
	public static final int ID_GREEN_4_IRISH     = 166;
	public static final int ID_GREEN_D_SPREADER  = 167;
	public static final int ID_GREEN_S_DOUBLE    = 168;
	public static final int ID_GREEN_R_SKIP      = 169;
	public static final int ID_BLUE_0_FUCKYOU    = 170;
	public static final int ID_BLUE_2_SHIELD     = 171;
	public static final int ID_BLUE_D_SPREADER   = 172;
	public static final int ID_BLUE_S_DOUBLE     = 173;
	public static final int ID_BLUE_R_SKIP       = 174;
	public static final int ID_YELLOW_0_SHITTER  = 175;
	public static final int ID_YELLOW_1_MAD      = 176;
	public static final int ID_YELLOW_69         = 177;
	public static final int ID_YELLOW_D_SPREADER = 178;
	public static final int ID_YELLOW_S_DOUBLE   = 179;
	public static final int ID_YELLOW_R_SKIP     = 180;

	private Hand	m_hand = null;
	private int		m_color = 0;
	private int		m_value = 0;
	private int     m_currentValue = 0;
	private int     m_pointValue = 0;
	private double  m_pointMultiplier = 1.0;
	private int		m_cumulativePenalty = 0;
	private int		m_highestCardMatch = 0;
	private int     m_deckIndex = 0;

	private int		m_id = 0;
	private boolean	m_faceup = false;

	
    public Hand getHand () { return m_hand; }
    public void setHand (Hand h) { m_hand = h; }
    
    public int getDeckIndex () { return m_deckIndex; }
    public int getColor () { return m_color; }
    public int getValue () { return m_value; }
    public int getID () { return m_id; }
    public int getPointValue() { return m_pointValue; }
    public double getPointMultiplier() { return m_pointMultiplier; }
    public int getCumulativePenalty() { return m_cumulativePenalty; }
    public int getHighestCardMatch() { return m_highestCardMatch; }

    public void setCurrentValue(int cv) { m_currentValue = cv; }
    public  int getCurrentValue() { return m_currentValue; }

    public boolean getFaceUp () { return m_faceup; }
    public void setFaceUp (boolean f) { m_faceup = f; }
	
	
	public Card (int deckIndex, int Color, int Value, int ID, int PointValue)
	{
		m_deckIndex = deckIndex;
		m_color = Color;
		m_value = Value;
		m_id = ID;
		m_pointValue = PointValue;
	}
	


	public Card(int deckIndex, int Color, int Value, int ID, int PointValue, double PointMultiplier)
	{
		this (deckIndex, Color, Value, ID, PointValue);
		m_pointValue = PointValue;
		m_pointMultiplier = PointMultiplier;
	}


	public Card(int deckIndex, int Color, int Value, int ID, int PointValue, double PointMultiplier, int CumulativePenalty)
	{
		this (deckIndex, Color, Value, ID, PointValue, PointMultiplier);
		m_cumulativePenalty = CumulativePenalty;
	}


	public Card(int deckIndex, int Color, int Value, int ID, int PointValue, double PointMultiplier, int CumulativePenalty, int HighestCardMatch)
	{
		this (deckIndex, Color, Value, ID, PointValue, PointMultiplier, CumulativePenalty);
		m_highestCardMatch = HighestCardMatch;
	}

	public String toString (Context ctx)
	{
		return this.toString (ctx, true);
	}

	public String toString(Context ctx, boolean familyFriendly)
	{
		String msg;
		String strColor = "";
		String strValue = "";

		switch (m_color) {
		case COLOR_RED:
			strColor = ctx.getString (R.string.cardcolor_red);
			break;
		case COLOR_GREEN:
			strColor = ctx.getString (R.string.cardcolor_green);
			break;
		case COLOR_BLUE:
			strColor = ctx.getString (R.string.cardcolor_blue);
			break;
		case COLOR_YELLOW:
			strColor = ctx.getString (R.string.cardcolor_yellow);
			break;
		}

		switch (m_id) {
		case ID_WILD:
			strValue = ctx.getString (R.string.cardval_wild);
			break;
		case ID_WILD_DRAWFOUR:
			strValue = ctx.getString (R.string.cardval_wild_drawfour);
			break;		
		case ID_WILD_HD:
			strValue = ctx.getString (R.string.cardname_wild_hd); 
			break;
		case ID_WILD_DB:
			strValue = ctx.getString (R.string.cardname_wild_db); 
			break;
		case ID_WILD_HOS:
			strValue = ctx.getString (R.string.cardname_wild_hos);
			break;
		case ID_WILD_MYSTERY:
			strValue = ctx.getString (R.string.cardname_wild_mystery);
			break;
		case ID_RED_0_HD:
			strValue = ctx.getString (R.string.cardname_red_0_hd);
			break;
		case ID_RED_2_GLASNOST:
			strValue = ctx.getString (R.string.cardname_red_2_glasnost);
			break;
		case ID_RED_5_MAGIC:
			strValue = ctx.getString (R.string.cardname_red_5_magic);
			break;
		case ID_GREEN_0_QUITTER:
			strValue = ctx.getString (R.string.cardname_green_0_quitter);
			break;
		case ID_GREEN_3_AIDS:
			if (familyFriendly)
			{
				strValue = ctx.getString (R.string.cardname_green_3_aids_ff);
			}
			else
			{
				strValue = ctx.getString (R.string.cardname_green_3_aids);
			}
			break;
		case ID_GREEN_4_IRISH:
			strValue = ctx.getString (R.string.cardname_green_4_irish);
			break;
		case ID_BLUE_0_FUCKYOU:
			if (familyFriendly)
			{
				strValue = ctx.getString (R.string.cardname_blue_0_fuck_you_ff);
			}
			else
			{
				strValue = ctx.getString (R.string.cardname_blue_0_fuck_you);
			}
			break;
		case ID_BLUE_2_SHIELD:
			strValue = ctx.getString (R.string.cardname_blue_2_shield);
			break;
		case ID_YELLOW_0_SHITTER:
			if (familyFriendly)
			{
				strValue = ctx.getString (R.string.cardname_yellow_0_shitter_ff);
			}
			else
			{
				strValue = ctx.getString (R.string.cardname_yellow_0_shitter);
			}
			break;

		case ID_YELLOW_1_MAD:
			strValue = ctx.getString (R.string.cardname_yellow_1_mad);
			break;
		case ID_YELLOW_69:
			strValue = ctx.getString (R.string.cardname_yellow_69);
			break;
		}

		if (strValue != "") 
		{
			return strValue;
		}


		switch (m_value) {
		case VAL_D:
			strValue = ctx.getString (R.string.cardval_d);
			break;
		case VAL_S:
			strValue = ctx.getString (R.string.cardval_s);
			break;
		case VAL_R:
			strValue = ctx.getString (R.string.cardval_r);
			break;
		case VAL_D_SPREAD:
			strValue = ctx.getString (R.string.cardval_d_spread);
			break;
		case VAL_S_DOUBLE:
			strValue = ctx.getString (R.string.cardval_s_double);
			break;
		case VAL_R_SKIP:
			strValue = ctx.getString (R.string.cardval_r_skip);
			break;

		default:
			strValue = "" + m_value;
			break;
		}	

		msg = strColor + " " + strValue;
		return msg;
	}
	
	
	public Card(JSONObject o) throws JSONException
	{
		m_deckIndex = o.getInt("deckIndex");
		m_color = o.getInt("color");
		m_value = o.getInt("value");
		m_currentValue = o.getInt("currentValue");
		m_pointValue = o.getInt("pointValue");
		m_pointMultiplier = o.getInt("pointMultiplier");
		m_cumulativePenalty = o.getInt("cumulativePenalty");
		m_highestCardMatch = o.getInt("highestCardMatch");
		m_id = o.getInt("id");
		m_faceup = o.getBoolean("faceup");
	}


	public JSONObject toJSON () throws JSONException
	{
		JSONObject o = new JSONObject();
		
		o.put("deckIndex", m_deckIndex);
		o.put("color", m_color);
		o.put("value", m_value);
		o.put("currentValue", m_currentValue);
		o.put("pointValue", m_pointValue);
		o.put("pointMultiplier", m_pointMultiplier);
		o.put("cumulativePenalty", m_cumulativePenalty);
		o.put("highestCardMatch", m_highestCardMatch);
		o.put("id", m_id);
		o.put("faceup", m_faceup);

		return o;
	}
	
}
