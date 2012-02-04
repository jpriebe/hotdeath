package com.smorgasbork.hotdeath;


/**
 * This class wraps all the prefs checking; it's a bit of a vestige of this application's
 * PocketPC origins, but it also makes it easier to call Prefs.get*() functions, since it
 * always has handy access to the GameActivity, and thus, the context.
 * @author priebe
 *
 */
public class GameOptions {
	
	private GameActivity m_ga;
	
	public GameOptions (GameActivity ga)
	{
		m_ga = ga;
	}
	
	public void shutdown ()
	{
		m_ga = null;
	}

	public int getP1Skill() 
    {
		return Prefs.getP1SkillLevel(m_ga);
    }

	public int getP2Skill() 
    {
		return Prefs.getP2SkillLevel(m_ga);
    }

	public int getP3Skill() 
    {
		return Prefs.getP3SkillLevel(m_ga);
    }

	public int getP1Agg() 
    {
		return Prefs.getP1AggressionLevel(m_ga);
    }

	public int getP2Agg() 
    {
		return Prefs.getP2AggressionLevel(m_ga);
    }

	public int getP3Agg() 
    {
		return Prefs.getP3AggressionLevel(m_ga);
    }

	public int getPauseLength() 
    {
		return Prefs.getGameSpeed(m_ga);
    }

	public int getCheatLevel() 
    {
		return Prefs.getCheatLevel(m_ga);
    }
	
	public boolean getFamilyFriendly ()
	{
		String s = Prefs.getCheatCode (m_ga);
		if (s.contains("originalhotdeath"))
		{
			return false;
		}
		
		return true;
	}

	public boolean getFaceUp() 
    {
        return Prefs.getFaceUp(m_ga); 
    }

	public boolean getComputer4th() 
    {
		return Prefs.getComputer4th(m_ga);
    }

	public boolean getStandardRules() 
    {
		String s = Prefs.getCheatCode (m_ga);
		if (s.contains("standardrules"))
		{
			return true;
		}
		
		return false;
    }

	public boolean getOneDeck() 
    {
		return !Prefs.getTwoDecks(m_ga);
    }

}
