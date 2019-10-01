package com.smorgasbork.hotdeath;

import java.util.HashMap;

import android.widget.BaseAdapter;
import android.content.Context;
import android.view.*;
import android.widget.ImageView;
import android.widget.GridView;

public class CardImageAdapter extends BaseAdapter {
    private Context mContext;
    private Integer[] m_cardIDs;
    private Integer[] m_thumbIDs;

    public Integer[] getCardIDs()
    {
    	return m_cardIDs;
    }
    
    public CardImageAdapter(Context c) 
    {
    	GameActivity ga = (GameActivity)c;
    	
    	// query the current card deck to see what cards are actually in use
    	Game g = ga.getGame();
    	CardDeck d = g.getDeck();
    	Card[] cary = d.getCards();
    	
    	HashMap<Integer, Boolean> usedIDs = new HashMap<Integer, Boolean>();
    	for (int i = 0; i < cary.length; i++)
    	{
    		if (usedIDs.containsKey(cary[i].getID()))
    		{
    			continue;
    		}
    		
    		usedIDs.put(cary[i].getID(), true);
    	}

    	// go through all cards in order and add them to the array
    	Integer[] cardids = ga.getCardIDs();
    	
    	Integer idx = 0;
    	m_thumbIDs = new Integer[usedIDs.size()];
    	m_cardIDs = new Integer[usedIDs.size()];
    	for (int i = 0; i < cardids.length; i++)
    	{
    		if (usedIDs.containsKey(cardids[i]))
    		{
    			m_cardIDs[idx] = cardids[i];
    	   		m_thumbIDs[idx] = ((GameActivity)c).getCardImageID(cardids[i]);
    	   		idx++;
       		}
    	}
    	
        mContext = c;
    }

    public int getCount() {
        return m_thumbIDs.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) 
    {
        ImageView imageView;
        if (convertView == null) {  // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) convertView;
        }
        
        imageView.setImageResource(m_thumbIDs[position]);
        return imageView;
    }

}