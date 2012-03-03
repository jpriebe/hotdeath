package com.smorgasbork.hotdeath;


import android.os.Handler;
import android.util.Log;

import android.app.AlertDialog;

import android.content.DialogInterface;

import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;
import android.content.Context;
import java.util.HashMap;

import android.graphics.*;
import android.content.res.Resources;
import com.smorgasbork.hotdeath.R;


public class GameTable extends View 
{
	private static final int ID = 42;  
	
	private int[] m_cardoffset;
	private int[] m_currentDrag;
	
	private int m_maxCardsDisplay = 7;
	
	private Matrix m_drawMatrix;
	
	private Point m_ptDiscardPile;
	private Point m_ptDrawPile;
		
	private Point[] m_ptSeat;
	private Point[] m_ptEmoticon;
	private Point[] m_ptPlayerIndicator;
	private Point[] m_ptCardBadge;
	private Point[] m_ptScoreText;
	private Point m_ptDirColor;
	private Point m_ptWinningMessage;	
	private Point m_ptMessages;
	
	private Rect[] m_handBoundingRect;
	private Rect m_drawPileBoundingRect;
	private Rect m_discardPileBoundingRect;
	
	private int m_leftMargin = 0;
	private int m_rightMargin = 0;
	private int m_topMargin = 0;
	private int m_bottomMargin = 0;
	
	private int m_cardSpacing = 0;
	private int m_cardSpacingHuman = 0;
	
	private int m_maxWidthHand;
	private int m_maxHeightHand;

	private int m_maxWidthHandHuman;

	// FIXME: make resolution independent (at least just query the bitmaps for their width and height)
	/*  LDPI
	private int m_cardWidth = 43;
	private int m_cardHeight = 59;
	*/
	private int m_cardWidth = 0;
	private int m_cardHeight = 0;
	
	private int m_emoticonWidth = 0;
	private int m_emoticonHeight = 0;
	
	private Point m_ptTouchDown = null;
	private boolean m_heldSteady = false;
	private boolean m_waitingForTouchAndHold = false;
	private boolean m_touchAndHold = false;
	private boolean m_touchDrawPile = false;
	private boolean m_touchDiscardPile = false;
	private int m_touchSeat = 0;
	
	private Integer[] m_cardIDs;
	private HashMap<Integer, Card> m_cardLookup;
	private HashMap<Integer, Integer> m_imageIDLookup;
	private HashMap<Integer, Bitmap> m_imageLookup;
	private HashMap<Integer, Integer> m_cardHelpLookup;
	
	private Bitmap m_bmpCardBack;
	
	private Bitmap m_bmpDirColorCCW, m_bmpDirColorCCWRed, m_bmpDirColorCCWGreen, m_bmpDirColorCCWBlue, m_bmpDirColorCCWYellow;
	private Bitmap m_bmpDirColorCW, m_bmpDirColorCWRed, m_bmpDirColorCWGreen, m_bmpDirColorCWBlue, m_bmpDirColorCWYellow;
	private Bitmap m_bmpEmoticonAggressor, m_bmpEmoticonVictim;
	private Bitmap[][] m_bmpPlayerIndicator;
	private Bitmap[] m_bmpWinningMessage;
	private Bitmap m_bmpCardBadge;
		
	private Paint m_paintTable;
	private Paint m_paintTableText;
	private Paint m_paintScoreText;
	private Paint m_paintCardBadgeText;
	
	private boolean m_readyToStartGame = false;
	private boolean m_waitingToStartGame = false;
	
	private Handler m_handler = new Handler();
	
	private Toast m_toast = null;
	
	private int m_helpCardID = -1;
	
	private Game m_game;
	private GameOptions m_go;
	
	public void setHelpCardID (int id)
	{
		m_helpCardID = id;
	}

	public int getHelpCardID ()
	{
		return m_helpCardID;
	}
	
	public Card getCardByID (int id)
	{
		return m_cardLookup.get(id);
	}
	
	public int getCardImageID(int id)
	{
		return m_imageIDLookup.get(id);
	}	
	
	public int getCardHelpText (int id)
	{
		return m_cardHelpLookup.get(id);
	}

	public Bitmap getCardBitmap (int id)
	{
		return m_imageLookup.get(id);
	}
	
	public Integer[] getCardIDs()
	{
		return m_cardIDs;
	}

	
	public GameTable(Context context, Game g, GameOptions go) 
	{
		super(context);

		this.setBackgroundResource(R.drawable.table_background);
		
		m_drawMatrix = new Matrix();
		
		setFocusable(true);
		setFocusableInTouchMode(true);
		setId(ID); 

		m_go = go;
		m_game = g;
		m_game.setGameTable (this);
		
		m_cardoffset = new int[4];
		m_currentDrag = new int[4];
		for (int i = 0; i < 4; i++)
		{
			m_cardoffset[i] = 0;
			m_currentDrag[i] = 0;
		}

		final float scale = getContext().getResources().getDisplayMetrics().density;
		
		m_paintTable = new Paint();
		m_paintTable.setColor(getResources().getColor(
				R.color.table_background));
		
		m_paintTableText = new Paint(Paint.ANTI_ALIAS_FLAG);
		m_paintTableText.setColor(getResources().getColor(
				R.color.table_text));
		m_paintTableText.setTextAlign(Paint.Align.CENTER);
		m_paintTableText.setTextSize(12 * scale);
		m_paintTableText.setTypeface(Typeface.DEFAULT);
        
		m_paintScoreText = new Paint(Paint.ANTI_ALIAS_FLAG);
		m_paintScoreText.setColor(getResources().getColor(
				R.color.score_text));
		m_paintScoreText.setTextSize(12 * scale);
		m_paintScoreText.setTypeface(Typeface.DEFAULT_BOLD);
		
		m_paintCardBadgeText = new Paint(Paint.ANTI_ALIAS_FLAG);
		m_paintCardBadgeText.setColor(getResources().getColor(
				R.color.card_badge_text));
		m_paintCardBadgeText.setTextAlign(Paint.Align.CENTER);
		m_paintCardBadgeText.setTextSize(14 * scale);
		m_paintCardBadgeText.setTypeface(Typeface.DEFAULT_BOLD);

		m_ptSeat = new Point[4];
		m_ptEmoticon = new Point[4];
		m_ptPlayerIndicator = new Point[4];
		m_ptCardBadge = new Point[4];
		m_ptScoreText = new Point[4];
		
		m_handBoundingRect = new Rect[4];
		
		m_bmpPlayerIndicator = new Bitmap[5][4];
		m_bmpWinningMessage = new Bitmap[4];
		
		initCards();
		
		m_cardHeight = m_bmpCardBack.getHeight();
		m_cardWidth = m_bmpCardBack.getWidth();
		
		m_emoticonHeight = m_bmpEmoticonAggressor.getHeight();
		m_emoticonWidth = m_bmpEmoticonAggressor.getWidth();
		
	}
	
	public void shutdown ()
	{
		m_game = null;
		m_go = null;
	}
	
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) 
	{
		m_leftMargin = m_cardWidth / 4;
		m_rightMargin = m_cardWidth / 4;
		m_topMargin = m_cardHeight / 3;
		m_bottomMargin = m_cardHeight / 3;
		
		if (h < 4.5 * m_cardHeight)
		{
			// probably landscape on a small device...
			m_topMargin = m_cardHeight / 4;
			m_bottomMargin = m_cardHeight / 4;
			m_ptDrawPile = new Point (w / 2 - 5 * m_cardWidth / 4, h / 2 - m_cardHeight / 2);
			m_ptDiscardPile = new Point (w / 2 + m_cardWidth / 4, h / 2 - m_cardHeight / 2);
			m_ptDirColor = new Point (m_ptDiscardPile.x + 2 * m_cardWidth + m_bmpDirColorCCW.getWidth() / 4 - m_bmpPlayerIndicator[0][0].getWidth(), h / 2 - m_bmpDirColorCCW.getWidth() / 2);
		}
		else
		{
			// portrait
			m_ptDrawPile = new Point (w / 2 - 5 * m_cardWidth / 4, h / 2 - m_cardHeight);
			m_ptDiscardPile = new Point (w / 2 + m_cardWidth / 4, h / 2 - m_cardHeight);
			m_ptDirColor = new Point (w /2 - m_bmpDirColorCCW.getWidth() / 2, h / 2 + m_cardHeight / 4);
		}

		m_ptPlayerIndicator[Game.SEAT_NORTH - 1] = new Point (m_ptDirColor.x + m_bmpDirColorCCW.getWidth() / 2 - m_bmpPlayerIndicator[0][0].getWidth() / 2, m_ptDirColor.y - m_bmpPlayerIndicator[0][0].getHeight());
		m_ptPlayerIndicator[Game.SEAT_EAST - 1] = new Point (m_ptDirColor.x + m_bmpDirColorCCW.getWidth(), m_ptDirColor.y + m_bmpDirColorCCW.getHeight() / 2 -  m_bmpPlayerIndicator[0][0].getHeight() / 2);
		m_ptPlayerIndicator[Game.SEAT_SOUTH - 1] = new Point (m_ptDirColor.x + m_bmpDirColorCCW.getWidth() / 2 - m_bmpPlayerIndicator[0][0].getWidth() / 2, m_ptDirColor.y + m_bmpDirColorCCW.getHeight());
		m_ptPlayerIndicator[Game.SEAT_WEST - 1] = new Point (m_ptDirColor.x - m_bmpPlayerIndicator[0][0].getWidth(), m_ptDirColor.y + m_bmpDirColorCCW.getHeight() / 2 -  m_bmpPlayerIndicator[0][0].getHeight() / 2);

		String numstr = "0";
		Rect textBounds = new Rect();
		m_paintScoreText.getTextBounds(numstr, 0, numstr.length(), textBounds);
		
		m_cardSpacing = (int)(m_cardWidth / 2);
		m_cardSpacingHuman = 2 * (int)(m_cardWidth / 3);
		
		// figure out what the maximum number of cards you can display will be
		
		// calculate max cards in layout 1 (N/S cards live between E/W cards)
		
		int humanPlayerArea = w - 2 * m_cardWidth - 2 * m_leftMargin - 2 * m_rightMargin;
		int maxNumHumanCards = (int)((humanPlayerArea - m_cardWidth) / m_cardSpacingHuman) + 1;

		int computerPlayerArea = h - m_topMargin - m_bottomMargin - (int)(textBounds.height() * 1.2);
		int maxNumComputerCards = (int)((computerPlayerArea - m_cardHeight) / m_cardSpacing) + 1;
		
		int maxCardsLayout1 = (maxNumComputerCards > maxNumHumanCards) ? maxNumHumanCards : maxNumComputerCards;

		// calculate max cards in layout 2 (E/W cards live between N/S cards)
		
		humanPlayerArea = w - m_leftMargin - m_rightMargin;
		maxNumHumanCards = (int)((humanPlayerArea - m_cardWidth) / m_cardSpacingHuman) + 1;

		computerPlayerArea = h - 2 * m_cardHeight - 2 * m_topMargin - 2 * m_bottomMargin;
		maxNumComputerCards = (int)((computerPlayerArea - m_cardHeight) / m_cardSpacing) + 1;
			
		int maxCardsLayout2 = (maxNumComputerCards > maxNumHumanCards) ? maxNumHumanCards : maxNumComputerCards;
		
		m_maxCardsDisplay = (maxCardsLayout1 > maxCardsLayout2) ? maxCardsLayout1 : maxCardsLayout2;

		Log.d("HDU", "[onSizeChanged] maxCardsLayout1: " + maxCardsLayout1);
		Log.d("HDU", "[onSizeChanged] maxCardsLayout2: " + maxCardsLayout2);
		Log.d("HDU", "[onSizeChanged] m_maxCardsDisplay: " + m_maxCardsDisplay);

		
		m_maxWidthHand = (m_maxCardsDisplay - 1) * m_cardSpacing + m_cardWidth;
		m_maxHeightHand = (m_maxCardsDisplay - 1) * m_cardSpacing + m_cardHeight;

		m_maxWidthHandHuman = (m_maxCardsDisplay - 1) * m_cardSpacingHuman + m_cardWidth;
		
		m_ptSeat[Game.SEAT_NORTH - 1] = new Point (w / 2, m_topMargin);
		m_ptSeat[Game.SEAT_EAST - 1] = new Point (w - (m_cardWidth + m_rightMargin), h / 2);
		m_ptSeat[Game.SEAT_SOUTH - 1] = new Point (w / 2, h - (m_cardHeight + m_bottomMargin));
		m_ptSeat[Game.SEAT_WEST - 1] = new Point (m_leftMargin, h / 2);
		
		m_ptWinningMessage = new Point (m_ptSeat[Game.SEAT_SOUTH - 1].x - m_bmpWinningMessage[0].getWidth() / 2, m_ptSeat[Game.SEAT_SOUTH - 1].y - m_bmpWinningMessage[0].getHeight() * 5 / 4);
		
		m_ptEmoticon[Game.SEAT_NORTH - 1] = new Point (m_ptSeat[Game.SEAT_NORTH - 1].x - m_emoticonWidth / 2, m_ptSeat[Game.SEAT_NORTH - 1].y + m_cardHeight * 11 / 10);
		m_ptEmoticon[Game.SEAT_EAST - 1] = new Point (m_ptSeat[Game.SEAT_EAST - 1].x - m_emoticonWidth - m_cardWidth / 10, m_ptSeat[Game.SEAT_EAST - 1].y - m_emoticonHeight / 2);
		m_ptEmoticon[Game.SEAT_SOUTH - 1] = new Point (m_ptSeat[Game.SEAT_SOUTH - 1].x - m_emoticonWidth / 2, m_ptSeat[Game.SEAT_SOUTH - 1].y - m_emoticonHeight - m_cardHeight / 10);
		m_ptEmoticon[Game.SEAT_WEST - 1] = new Point (m_ptSeat[Game.SEAT_WEST - 1].x + m_cardWidth * 11 / 10, m_ptSeat[Game.SEAT_WEST - 1].y - m_emoticonHeight / 2);		
		
		m_ptCardBadge[Game.SEAT_NORTH - 1] = new Point (m_ptSeat[Game.SEAT_NORTH - 1].x + m_maxWidthHand / 2 - m_bmpCardBadge.getWidth() / 2,
					m_ptSeat[Game.SEAT_NORTH - 1].y + m_cardHeight - m_bmpCardBadge.getHeight()  / 2);
		m_ptCardBadge[Game.SEAT_EAST - 1] = new Point (m_ptSeat[Game.SEAT_EAST - 1].x + m_cardWidth - m_bmpCardBadge.getWidth() / 2,
				m_ptSeat[Game.SEAT_EAST - 1].y + m_maxHeightHand / 2 - m_bmpCardBadge.getHeight() / 2);
		m_ptCardBadge[Game.SEAT_SOUTH - 1] = new Point (m_ptSeat[Game.SEAT_SOUTH - 1].x + m_maxWidthHandHuman / 2 - m_bmpCardBadge.getWidth() / 2,
				m_ptSeat[Game.SEAT_SOUTH - 1].y + m_cardHeight - m_bmpCardBadge.getHeight() / 2);
		m_ptCardBadge[Game.SEAT_WEST - 1] = new Point (m_ptSeat[Game.SEAT_WEST - 1].x + m_cardWidth - m_bmpCardBadge.getWidth() / 2,
				m_ptSeat[Game.SEAT_WEST - 1].y + m_maxHeightHand / 2 - m_bmpCardBadge.getHeight() / 2);
		
		m_ptScoreText[Game.SEAT_NORTH - 1] = new Point (m_ptSeat[Game.SEAT_NORTH - 1].x,
				m_ptSeat[Game.SEAT_NORTH - 1].y - (int)(textBounds.height() * 1.1));
		m_ptScoreText[Game.SEAT_EAST - 1] = new Point (m_ptSeat[Game.SEAT_EAST - 1].x + m_cardWidth,
			m_ptSeat[Game.SEAT_EAST - 1].y - m_maxHeightHand / 2 - (int)(textBounds.height() * 1.1));
		m_ptScoreText[Game.SEAT_SOUTH - 1] = new Point (m_ptSeat[Game.SEAT_SOUTH - 1].x,
				m_ptSeat[Game.SEAT_SOUTH - 1].y + m_cardHeight + (int)(textBounds.height() * 1.5));
		m_ptScoreText[Game.SEAT_WEST - 1] = new Point (m_ptSeat[Game.SEAT_WEST - 1].x,
				m_ptSeat[Game.SEAT_WEST - 1].y - m_maxHeightHand / 2 - (int)(textBounds.height() * 1.1));

		m_ptMessages = new Point (m_ptSeat[Game.SEAT_SOUTH - 1].x, m_ptSeat[Game.SEAT_SOUTH - 1].y - 3 * m_cardHeight / 4);
		
		super.onSizeChanged(w, h, oldw, oldh);
		
		m_readyToStartGame = true;
		if (m_waitingToStartGame)
		{
			m_waitingToStartGame = false;
			m_game.start ();
		}
	}
	
	public void startGameWhenReady ()
	{
		if (m_readyToStartGame)
		{
			m_game.start ();
			return;
		}
		
		m_waitingToStartGame = true;
	}
	
	public void showFastForwardButton (boolean show)
	{
		GameActivity a = (GameActivity)(getContext());
		if (show)
		{
			a.getBtnFastForward().setVisibility(View.VISIBLE);
		}
		else
		{
			a.getBtnFastForward().setVisibility(View.INVISIBLE);
		}
	}
	
	private Runnable m_touchAndHoldTask = new Runnable() 
	{
		public void run() {

			// if something cancelled the wait (like ACTION_UP, ACTION_CANCEL, or a 
			// large enough ACTION_MOVE), we don't show card help
			if (!m_waitingForTouchAndHold)
			{
				return;
			}
			
			m_touchAndHold = true;

			// only show card help while it's the human player's turn or the
			// round is complete
			Player p = m_game.getCurrPlayer();
			if (!((p instanceof HumanPlayer)
					|| (m_game.getRoundComplete())))
			{
				return;
			}

			// only show card help for face-up cards!
			Card c = findTouchedCard (m_ptTouchDown);
			if (c == null)
			{
				return;
			}
			if (!c.getFaceUp())
			{
				return;
			}

			android.os.Vibrator v = (android.os.Vibrator) GameTable.this.getContext().getSystemService(Context.VIBRATOR_SERVICE);
			v.vibrate (100);
			ShowCardHelp(c);
		}
	};
		
		
	private boolean heldSteadyHand()
	{
		if (m_touchSeat == 0)
		{
			return false;
		}
		
		return m_heldSteady;
	}
	
	private boolean heldSteadyDraw()
	{
		// check for draw (DOWN/UP in the draw pile)
		if (!m_touchDrawPile)
		{
			return false;
		}
		
		return m_heldSteady;
	}
	
	private boolean heldSteadyDiscard()
	{
		// check for draw (DOWN/UP in the draw pile)
		if (!m_touchDiscardPile)
		{
			return false;
		}
		
		return m_heldSteady;
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) 
	{
		if (event.getAction() == MotionEvent.ACTION_CANCEL)
		{
			m_handler.removeCallbacks(m_touchAndHoldTask);
			m_waitingForTouchAndHold = false;
			return true;
		}
		
		if (event.getAction() == MotionEvent.ACTION_DOWN)
		{
			int x = (int)(event.getX());
			int y = (int)(event.getY());

			m_ptTouchDown = new Point (x, y);
			m_touchAndHold = false;
			m_heldSteady = true;

			m_touchDiscardPile = false;
			m_touchDrawPile = false;
			m_touchSeat = 0;
			if ((m_handBoundingRect[Game.SEAT_SOUTH - 1] != null)
					&& m_handBoundingRect[Game.SEAT_SOUTH - 1].contains(x, y))
			{
				m_touchSeat = Game.SEAT_SOUTH;
			}
			else if ((m_handBoundingRect[Game.SEAT_WEST - 1] != null)
				&& m_handBoundingRect[Game.SEAT_WEST - 1].contains(x, y))
			{
				m_touchSeat = Game.SEAT_WEST;			
			}
			else if ((m_handBoundingRect[Game.SEAT_NORTH - 1] != null)
					&& m_handBoundingRect[Game.SEAT_NORTH - 1].contains(x, y))
			{
				m_touchSeat = Game.SEAT_NORTH;				
			}
			else if ((m_handBoundingRect[Game.SEAT_EAST - 1] != null)
					&& m_handBoundingRect[Game.SEAT_EAST - 1].contains(x, y))
			{
				m_touchSeat = Game.SEAT_EAST;			
			}
			
			if (m_touchSeat != 0)
			{
				m_waitingForTouchAndHold = true;
				m_handler.postDelayed (m_touchAndHoldTask, 1000);

				m_ptTouchDown = new Point (x, y);
				return true;
			}

			if (m_drawPileBoundingRect != null && m_drawPileBoundingRect.contains (x, y))
			{
				m_touchDrawPile = true;
			}
			
			if (m_discardPileBoundingRect != null && m_discardPileBoundingRect.contains (x, y))
			{
				m_waitingForTouchAndHold = true;
				m_handler.postDelayed (m_touchAndHoldTask, 1000);

				m_touchDiscardPile = true;
			}
						
			return true;
		}
		else if (event.getAction() == MotionEvent.ACTION_UP)
		{
			if (m_touchAndHold)
			{
				return true;
			}
			
			m_waitingForTouchAndHold = false;

			// if we haven't moved from the card we originally touched down on, 
			// we'll play that card.
			if (this.heldSteadyHand())
			{
				handCardTapped (m_touchSeat, m_ptTouchDown);
				return true;
			}

			if (this.heldSteadyDraw())
			{
				drawPileTapped ();
				return true;
			}

			if (this.heldSteadyDiscard())
			{
				discardPileTapped ();
				return true;
			}
			
			// if we're letting up on a drag, commit the drag value
			if (m_touchSeat != 0)
			{
				int idx = m_touchSeat - 1;					
				if (m_currentDrag[idx] != 0)
				{
					m_cardoffset[idx] += m_currentDrag[idx];

					// set bounds properly
					Player p = m_game.getPlayer(idx);
					int ncards = p.getHand().getNumCards();
					
					if (m_cardoffset[idx] >= ncards - m_maxCardsDisplay)
					{
						m_cardoffset[idx] = ncards - m_maxCardsDisplay;
					}
					
					if (m_cardoffset[idx] < 0)
					{
						m_cardoffset[idx] = 0;
					}
					
					m_currentDrag[idx] = 0;
				}
				m_touchSeat = 0;
				return true;
			}
			
			return true;
		}
		else if (event.getAction() == MotionEvent.ACTION_MOVE)
		{
			if (m_touchSeat != 0)
			{
				int spacing = (m_game.getPlayer(m_touchSeat - 1) instanceof HumanPlayer)
					? m_cardSpacingHuman
					: m_cardSpacing;
				
				int cardoffset;
				
				if (m_touchSeat == Game.SEAT_NORTH || m_touchSeat == Game.SEAT_SOUTH)
				{
					int distx = (int)(event.getX()) - m_ptTouchDown.x;
					cardoffset = distx / (spacing / 2);
				}
				else
				{
					int disty = (int)(event.getY()) - m_ptTouchDown.y;
					cardoffset = disty / spacing;
				}
				
				if (cardoffset != 0)
				{
					if (m_heldSteady)
					{
						Log.d("HDU", "[ACTION_MOVE] cardoffset = " + cardoffset + ", m_heldSteady=false now");
						m_waitingForTouchAndHold = false;
						m_handler.removeCallbacks(m_touchAndHoldTask);
						m_heldSteady = false;
					}
				}
				
				// invert the offset, as a slide to the left means increase the offset
				m_currentDrag[m_touchSeat - 1] = 0 - cardoffset;
				this.invalidate();
				
				return true;
			}
		}
		
		return super.onTouchEvent(event);
	}
	
	private void drawPileTapped ()
	{
		m_game.drawPileTapped();
	}
	
	private void discardPileTapped ()
	{
		m_game.discardPileTapped();
	}
	
	private Card findTouchedCardHand (int seat, Point pt)
	{
		Rect r = m_handBoundingRect[seat - 1];
		if (r == null)
		{
			return null;
		}
		
		if (!r.contains(pt.x, pt.y))
		{
			return null;
		}
		
		int spacing = (m_game.getPlayer(seat - 1) instanceof HumanPlayer)
				? m_cardSpacingHuman
				: m_cardSpacing;
				
		int idx = 0;
		switch (seat)
		{
		case Game.SEAT_NORTH:
		case Game.SEAT_SOUTH:
			idx = (int)((pt.x - r.left) / spacing);
			
			break;
			
		case Game.SEAT_WEST:
		case Game.SEAT_EAST:
			idx = (int)((pt.y - r.top) / spacing);

			break;
		}
		
		Player p = m_game.getPlayer(seat - 1);
		Hand h = p.getHand();
		
		int numcardsshowing = h.getNumCards() - m_cardoffset[seat - 1];
		numcardsshowing = (numcardsshowing > m_maxCardsDisplay) ? m_maxCardsDisplay : numcardsshowing;
		
		if (idx >= numcardsshowing)
		{
			idx = numcardsshowing - 1;
		}
		idx += m_cardoffset[seat - 1];
		

		Card c = h.getCard(idx);
		
		return c;
	}
	
	private Card findTouchedCardDiscardPile (Point pt)
	{
		if (m_discardPileBoundingRect.contains(pt.x, pt.y))
		{
			int numcards = m_game.getDiscardPile().getNumCards();
			return m_game.getDiscardPile().getCard(numcards - 1);
		}
		
		return null;
	}
	
	private Card findTouchedCard (Point pt)
	{
		if (m_touchDiscardPile)
		{
			return findTouchedCardDiscardPile (pt);
		}
		if (m_touchSeat != 0)
		{
			return findTouchedCardHand (m_touchSeat, pt);
		}
		
		return null;
	}
	
	private void handCardTapped (int seat, Point pt)
	{
		if (!m_game.roundIsActive())
		{
			return;
		}
		
		Player p = m_game.getPlayer(seat - 1);
		if (p instanceof HumanPlayer)
		{
			Card c = findTouchedCardHand (seat, pt);
			
			if (c != null)
			{
				((HumanPlayer)p).turnDecisionPlayCard (c);
			}
		}
	}

	
	public void RedrawTable ()
	{
		this.invalidate();
	}
	
	@Override
	protected void onDraw(Canvas canvas)
	{	
		int i;
	
		// canvas.drawRect(0, 0, getWidth(), getHeight(), m_paintTable);
						
		// draw the color and direction indicator
		
		Bitmap bmp = null;
		
		int curr_color = m_game.getCurrColor();
		
		if (m_game.getDirection() == Game.DIR_CCLOCKWISE)
		{
			switch (curr_color)
			{
			case Card.COLOR_WILD:
				bmp = m_bmpDirColorCCW;
				break;
			case Card.COLOR_RED:
				bmp = m_bmpDirColorCCWRed;
				break;
			case Card.COLOR_GREEN:
				bmp = m_bmpDirColorCCWGreen;
				break;
			case Card.COLOR_BLUE:
				bmp = m_bmpDirColorCCWBlue;
				break;
			case Card.COLOR_YELLOW:
				bmp = m_bmpDirColorCCWYellow;
				break;
			}
		}
		else
		{
			switch (curr_color)
			{
			case Card.COLOR_WILD:
				bmp = m_bmpDirColorCW;
				break;
			case Card.COLOR_RED:
				bmp = m_bmpDirColorCWRed;
				break;
			case Card.COLOR_GREEN:
				bmp = m_bmpDirColorCWGreen;
				break;
			case Card.COLOR_BLUE:
				bmp = m_bmpDirColorCWBlue;
				break;
			case Card.COLOR_YELLOW:
				bmp = m_bmpDirColorCWYellow;
				break;
			}
		}

		// before the deal, we don't have a direction
		if (bmp == null)
		{
			return;
		}
		
		m_drawMatrix.reset();
		m_drawMatrix.setScale(1, 1);
		m_drawMatrix.setTranslate(m_ptDirColor.x, m_ptDirColor.y);
		canvas.drawBitmap(bmp, m_drawMatrix, null);

		displayScore (canvas);
				
		int x = 0;
		int y = 0;

		// draw the hands

		for (i = 0; i < 4; i++) 
		{
			Player p = m_game.getPlayer(i);


			// don't draw ejected players' cards
			
			if (p.getActive()) 
			{
				RedrawHand (canvas, i + 1);
			}
		}
		
		Player p = m_game.getCurrPlayer();
		if (p != null)
		{
			Point pt = m_ptPlayerIndicator[p.getSeat() - 1];
	
			m_drawMatrix.reset();
			m_drawMatrix.setScale(1, 1);
			m_drawMatrix.setTranslate(pt.x, pt.y);
			
			canvas.drawBitmap(m_bmpPlayerIndicator[curr_color - 1][p.getSeat() - 1], m_drawMatrix, null);
		}		
		
		if (m_game.getFastForward())
		{
			return;
		}
		
		// draw the discard pile
		
		CardPile pile = m_game.getDiscardPile ();
		int numCardsInPlay = pile.getNumCards();
		CardDeck deck = m_game.getDeck ();

		int skip = 16;
		if (deck != null)
		{
			if (deck.getNumCards () > 108) 
			{
				skip = 32;
			}
		}
		
		if (pile != null)
		{			
			for (i = 0; i < numCardsInPlay; i += skip) 
			{
				// make sure that the top card is drawn...
				if (i >= numCardsInPlay - skip) 
				{
					i = numCardsInPlay - 1;
				}
				
				Card c = pile.getCard(i);
				if (c != null) 
				{
					// FIXME -- make resolution independent
					x = m_ptDiscardPile.x + (int)((float)i / (float)skip) * 2;
					y = m_ptDiscardPile.y + (int)((float)i / (float)skip) * 2;
					c.setFaceUp(true);

					this.drawCard (canvas, c, x, y, true);
				}
			}
		}

		
		m_discardPileBoundingRect = new Rect(m_ptDiscardPile.x, m_ptDiscardPile.y, x + m_cardWidth, y + m_cardHeight);
		
		// draw the draw pile
		
		pile = m_game.getDrawPile();
		

		if (pile != null)
		{
			x = m_ptDrawPile.x;
			y = m_ptDrawPile.y;
			int numCardsInPile = pile.getNumCards();
			for (i = 0; i < numCardsInPile; i += skip) 
			{
				if (i >= numCardsInPile - skip)
				{
					i = numCardsInPile - 1;
				}

				Card c = pile.getCard(i);
				if (c != null)
				{
					this.drawCard (canvas, c, x, y, false);
					// FIXME -- make resolution independent!
					x += 2;
					y += 2;
				}
			}
		}
		
		m_drawPileBoundingRect = new Rect(m_ptDrawPile.x, m_ptDrawPile.y, x + m_cardWidth, y + m_cardHeight);
		

		
		if (m_game.getWinner() != 0)
		{
			m_drawMatrix.reset();
			m_drawMatrix.setScale(1, 1);
			m_drawMatrix.setTranslate(m_ptWinningMessage.x, m_ptWinningMessage.y);

			canvas.drawBitmap(m_bmpWinningMessage[m_game.getWinner() - 1], m_drawMatrix, null);			
		}
		
		drawPenalty(canvas);		
	}
	
	private void RedrawHand (Canvas cv, int seat)
	{
		Hand h = m_game.getPlayer(seat - 1).getHand();
		if (h == null) 
		{
			return;
		}

		int x = 0;
		int y = 0;
		int dx = 0;
		int dy = 0;
		int numcards = h.getNumCards();

		// keep the offsets sane
		if (m_cardoffset[seat-1] > numcards - m_maxCardsDisplay) 
		{
			m_cardoffset[seat-1] = numcards - m_maxCardsDisplay;
		}
		if (m_cardoffset[seat-1] < 0) 
		{
			m_cardoffset[seat-1] = 0;
		}

		// apply the current drag
		int cardoffset = m_cardoffset[seat - 1] + m_currentDrag[seat - 1];
		if (cardoffset > numcards - m_maxCardsDisplay) 
		{
			cardoffset = numcards - m_maxCardsDisplay;
		}
		if (cardoffset < 0) 
		{
			cardoffset = 0;
		}

		int numcardsshowing = numcards - m_cardoffset[seat - 1];
		numcardsshowing = (numcardsshowing > m_maxCardsDisplay) ? m_maxCardsDisplay : numcardsshowing;

		int handWidth = 0;
		int handHeight = 0;

		int spacing = (m_game.getPlayer(seat - 1) instanceof HumanPlayer)
				? m_cardSpacingHuman
				: m_cardSpacing;
		
		switch (seat) {
		case Game.SEAT_SOUTH:
			dx = spacing;
			dy = 0;
			handWidth = (numcardsshowing - 1) * spacing + m_cardWidth;
			x = m_ptSeat[Game.SEAT_SOUTH - 1].x - handWidth / 2;
			y = m_ptSeat[Game.SEAT_SOUTH - 1].y;
			m_handBoundingRect[Game.SEAT_SOUTH - 1] = new Rect(x, y, x + handWidth, y + m_cardHeight);
			break;
		case Game.SEAT_WEST:
			dx = 0;
			dy = spacing;
			handHeight = (numcardsshowing - 1) * spacing + m_cardHeight;
			x = m_ptSeat[Game.SEAT_WEST - 1].x; 
			y = m_ptSeat[Game.SEAT_WEST - 1].y - handHeight / 2;
			m_handBoundingRect[Game.SEAT_WEST - 1] = new Rect(x, y, x + m_cardWidth, y + handHeight);
			break;
		case Game.SEAT_NORTH:
			dx = spacing;
			dy = 0;
			handWidth = (numcardsshowing - 1) * spacing + m_cardWidth;
			x = m_ptSeat[Game.SEAT_NORTH - 1].x - handWidth / 2;
			y = m_ptSeat[Game.SEAT_NORTH - 1].y;
			m_handBoundingRect[Game.SEAT_NORTH - 1] = new Rect(x, y, x + handWidth, y + m_cardHeight);
			break;
		case Game.SEAT_EAST:
			dx = 0;
			dy = spacing;
			handHeight = (numcardsshowing - 1) * spacing + m_cardHeight;
			x = m_ptSeat[Game.SEAT_EAST - 1].x; 
			y = m_ptSeat[Game.SEAT_EAST - 1].y - handHeight / 2;
			m_handBoundingRect[Game.SEAT_EAST - 1] = new Rect(x, y, x + m_cardWidth, y + handHeight);
			break;
		}

		// draw the cards that are on the table

		int stop = numcards;
		if (cardoffset + m_maxCardsDisplay < numcards)
		{
			stop = cardoffset + m_maxCardsDisplay;
		}

		int j;
		for (j = cardoffset; j < stop; j++) 
		{
			Card c = h.getCard(j);
			if (c == null) 
			{
				continue;
			}

			this.drawCard (cv, c, x, y, c.getFaceUp());

			x += dx;
			y += dy;
		}
		
		if (numcards > m_maxCardsDisplay)
		{
			Point pt = m_ptCardBadge[seat - 1];
			
            m_drawMatrix.reset();
    		m_drawMatrix.setScale(1, 1);
    		m_drawMatrix.setTranslate(pt.x, pt.y);
    		
            cv.drawBitmap(m_bmpCardBadge, m_drawMatrix, null);
            
            float fx = (float)(pt.x + m_bmpCardBadge.getWidth() / 2);
            Rect textBounds = new Rect();
            String numstr = "" + numcards;
            
            m_paintCardBadgeText.getTextBounds(numstr, 0, numstr.length(), textBounds);
            float fy = (float)(pt.y + m_bmpCardBadge.getHeight() / 2 + (int)(textBounds.height() / 2));
            
    		cv.drawText(numstr, fx, fy, m_paintCardBadgeText);
		}
	}
	
	private void initCards ()
	{
		/*
		 * I admit -- this code is nasty; it started with a simple lookup HashMap,
		 * and gradually grew into 4 separate ones.  This could be a LOT cleaner.
		 * I also don't like that I have to create all these card objects when there
		 * are already card objects in the card deck.  But this was more convenient,
		 * and it's hard to imagine that these objects are really taking up a lot of
		 * RAM in the grand scheme of things.
		 */
		m_cardLookup = new HashMap<Integer, Card>();
		m_imageIDLookup = new HashMap<Integer, Integer>();
		m_imageLookup = new HashMap<Integer, Bitmap>();
		m_cardHelpLookup = new HashMap<Integer, Integer>();
		m_cardIDs = new Integer[81];
		
	    Resources res = this.getContext().getResources ();

	    BitmapFactory.Options opt = new BitmapFactory.Options();
	    //opt.inScaled = false;
	    
	    m_bmpCardBack = BitmapFactory.decodeResource(res, R.drawable.card_back, opt);

		m_imageIDLookup.put (Card.ID_RED_0, R.drawable.card_red_0);
		m_imageLookup.put (Card.ID_RED_0, BitmapFactory.decodeResource(res, R.drawable.card_red_0, opt));
		m_cardHelpLookup.put (Card.ID_RED_0, R.string.cardhelp_0);
        m_cardLookup.put (Card.ID_RED_0, new Card(-1, Card.COLOR_RED, 0, Card.ID_RED_0_HD, 0, 0));
        
		m_imageIDLookup.put (Card.ID_RED_1, R.drawable.card_red_1);
		m_imageLookup.put (Card.ID_RED_1, BitmapFactory.decodeResource(res, R.drawable.card_red_1, opt));
		m_cardHelpLookup.put (Card.ID_RED_1, R.string.cardhelp_1);
        m_cardLookup.put (Card.ID_RED_1, new Card(-1, Card.COLOR_RED, 1, Card.ID_RED_1, 1));
		
		m_imageIDLookup.put (Card.ID_RED_2, R.drawable.card_red_2);
		m_imageLookup.put (Card.ID_RED_2, BitmapFactory.decodeResource(res, R.drawable.card_red_2, opt));
		m_cardHelpLookup.put (Card.ID_RED_2, R.string.cardhelp_2);
        m_cardLookup.put (Card.ID_RED_2, new Card(-1, Card.COLOR_RED, 2, Card.ID_RED_2, 2));
		
		m_imageIDLookup.put (Card.ID_RED_3, R.drawable.card_red_3);
		m_imageLookup.put (Card.ID_RED_3, BitmapFactory.decodeResource(res, R.drawable.card_red_3, opt));
		m_cardHelpLookup.put (Card.ID_RED_3, R.string.cardhelp_3);
        m_cardLookup.put (Card.ID_RED_3, new Card(-1, Card.COLOR_RED, 3, Card.ID_RED_3, 3));
		
		m_imageIDLookup.put (Card.ID_RED_4, R.drawable.card_red_4);
		m_imageLookup.put (Card.ID_RED_4, BitmapFactory.decodeResource(res, R.drawable.card_red_4, opt));
		m_cardHelpLookup.put (Card.ID_RED_4, R.string.cardhelp_4);
        m_cardLookup.put (Card.ID_RED_4, new Card(-1, Card.COLOR_RED, 4, Card.ID_RED_4, 4));
		
		m_imageIDLookup.put (Card.ID_RED_5, R.drawable.card_red_5);
		m_imageLookup.put (Card.ID_RED_5, BitmapFactory.decodeResource(res, R.drawable.card_red_5, opt));
		m_cardHelpLookup.put (Card.ID_RED_5, R.string.cardhelp_5);
        m_cardLookup.put (Card.ID_RED_5, new Card(-1, Card.COLOR_RED, 5, Card.ID_RED_5, 5));
		
		m_imageIDLookup.put (Card.ID_RED_6, R.drawable.card_red_6);
		m_imageLookup.put (Card.ID_RED_6, BitmapFactory.decodeResource(res, R.drawable.card_red_6, opt));
		m_cardHelpLookup.put (Card.ID_RED_6, R.string.cardhelp_6);
        m_cardLookup.put (Card.ID_RED_6, new Card(-1, Card.COLOR_RED, 6, Card.ID_RED_6, 6));
		
		m_imageIDLookup.put (Card.ID_RED_7, R.drawable.card_red_7);
		m_imageLookup.put (Card.ID_RED_7, BitmapFactory.decodeResource(res, R.drawable.card_red_7, opt));
		m_cardHelpLookup.put (Card.ID_RED_7, R.string.cardhelp_7);
        m_cardLookup.put (Card.ID_RED_7, new Card(-1, Card.COLOR_RED, 7, Card.ID_RED_7, 7));
		
		m_imageIDLookup.put (Card.ID_RED_8, R.drawable.card_red_8);
		m_imageLookup.put (Card.ID_RED_8, BitmapFactory.decodeResource(res, R.drawable.card_red_8, opt));
		m_cardHelpLookup.put (Card.ID_RED_8, R.string.cardhelp_8);
        m_cardLookup.put (Card.ID_RED_8, new Card(-1, Card.COLOR_RED, 8, Card.ID_RED_8, 8));
		
		m_imageIDLookup.put (Card.ID_RED_9, R.drawable.card_red_9);
		m_imageLookup.put (Card.ID_RED_9, BitmapFactory.decodeResource(res, R.drawable.card_red_9, opt));
		m_cardHelpLookup.put (Card.ID_RED_9, R.string.cardhelp_9);
        m_cardLookup.put (Card.ID_RED_9, new Card(-1, Card.COLOR_RED, 9, Card.ID_RED_9, 9));
		
		m_imageIDLookup.put (Card.ID_RED_D, R.drawable.card_red_d);
		m_imageLookup.put (Card.ID_RED_D, BitmapFactory.decodeResource(res, R.drawable.card_red_d, opt));
		m_cardHelpLookup.put (Card.ID_RED_D, R.string.cardhelp_d);
        m_cardLookup.put (Card.ID_RED_D, new Card(-1, Card.COLOR_RED, Card.VAL_D, Card.ID_RED_D, 20));

		m_imageIDLookup.put (Card.ID_RED_S, R.drawable.card_red_s);
		m_imageLookup.put (Card.ID_RED_S, BitmapFactory.decodeResource(res, R.drawable.card_red_s, opt));
		m_cardHelpLookup.put (Card.ID_RED_S, R.string.cardhelp_s);
        m_cardLookup.put (Card.ID_RED_S, new Card(-1, Card.COLOR_RED, Card.VAL_S, Card.ID_RED_S, 20));

		m_imageIDLookup.put (Card.ID_RED_R, R.drawable.card_red_r);
		m_imageLookup.put (Card.ID_RED_R, BitmapFactory.decodeResource(res, R.drawable.card_red_r, opt));
		m_cardHelpLookup.put (Card.ID_RED_R, R.string.cardhelp_r);
        m_cardLookup.put (Card.ID_RED_R, new Card(-1, Card.COLOR_RED, Card.VAL_R, Card.ID_RED_R, 20));

		m_imageIDLookup.put (Card.ID_GREEN_0, R.drawable.card_green_0);
		m_imageLookup.put (Card.ID_GREEN_0, BitmapFactory.decodeResource(res, R.drawable.card_green_0, opt));
		m_cardHelpLookup.put (Card.ID_GREEN_0, R.string.cardhelp_0);
        m_cardLookup.put (Card.ID_GREEN_0, new Card(-1, Card.COLOR_GREEN, 0, Card.ID_GREEN_0_QUITTER, 0));
		
		m_imageIDLookup.put (Card.ID_GREEN_1, R.drawable.card_green_1);
		m_imageLookup.put (Card.ID_GREEN_1, BitmapFactory.decodeResource(res, R.drawable.card_green_1, opt));
		m_cardHelpLookup.put (Card.ID_GREEN_1, R.string.cardhelp_1);
        m_cardLookup.put (Card.ID_GREEN_1, new Card(-1, Card.COLOR_GREEN, 1, Card.ID_GREEN_1, 1));

		m_imageIDLookup.put (Card.ID_GREEN_2, R.drawable.card_green_2);
		m_imageLookup.put (Card.ID_GREEN_2, BitmapFactory.decodeResource(res, R.drawable.card_green_2, opt));
		m_cardHelpLookup.put (Card.ID_GREEN_2, R.string.cardhelp_2);
        m_cardLookup.put (Card.ID_GREEN_2, new Card(-1, Card.COLOR_GREEN, 2, Card.ID_GREEN_2, 2));
		
		m_imageIDLookup.put (Card.ID_GREEN_3, R.drawable.card_green_3);
		m_imageLookup.put (Card.ID_GREEN_3, BitmapFactory.decodeResource(res, R.drawable.card_green_3, opt));
		m_cardHelpLookup.put (Card.ID_GREEN_3, R.string.cardhelp_3);
        m_cardLookup.put (Card.ID_GREEN_3, new Card(-1, Card.COLOR_GREEN, 3, Card.ID_GREEN_3, 3));
		
		m_imageIDLookup.put (Card.ID_GREEN_4, R.drawable.card_green_4);
		m_imageLookup.put (Card.ID_GREEN_4, BitmapFactory.decodeResource(res, R.drawable.card_green_4, opt));
		m_cardHelpLookup.put (Card.ID_GREEN_4, R.string.cardhelp_4);
        m_cardLookup.put (Card.ID_GREEN_4, new Card(-1, Card.COLOR_GREEN, 4, Card.ID_GREEN_4, 4));

		m_imageIDLookup.put (Card.ID_GREEN_5, R.drawable.card_green_5);
		m_imageLookup.put (Card.ID_GREEN_5, BitmapFactory.decodeResource(res, R.drawable.card_green_5, opt));
		m_cardHelpLookup.put (Card.ID_GREEN_5, R.string.cardhelp_5);
        m_cardLookup.put (Card.ID_GREEN_5, new Card(-1, Card.COLOR_GREEN, 5, Card.ID_GREEN_5, 5));
		
		m_imageIDLookup.put (Card.ID_GREEN_6, R.drawable.card_green_6);
		m_imageLookup.put (Card.ID_GREEN_6, BitmapFactory.decodeResource(res, R.drawable.card_green_6, opt));
		m_cardHelpLookup.put (Card.ID_GREEN_6, R.string.cardhelp_6);
        m_cardLookup.put (Card.ID_GREEN_6, new Card(-1, Card.COLOR_GREEN, 6, Card.ID_GREEN_6, 6));
		
		m_imageIDLookup.put (Card.ID_GREEN_7, R.drawable.card_green_7);
		m_imageLookup.put (Card.ID_GREEN_7, BitmapFactory.decodeResource(res, R.drawable.card_green_7, opt));
		m_cardHelpLookup.put (Card.ID_GREEN_7, R.string.cardhelp_7);
        m_cardLookup.put (Card.ID_GREEN_7, new Card(-1, Card.COLOR_GREEN, 7, Card.ID_GREEN_7, 7));
		
		m_imageIDLookup.put (Card.ID_GREEN_8, R.drawable.card_green_8);
		m_imageLookup.put (Card.ID_GREEN_8, BitmapFactory.decodeResource(res, R.drawable.card_green_8, opt));
		m_cardHelpLookup.put (Card.ID_GREEN_8, R.string.cardhelp_8);
        m_cardLookup.put (Card.ID_GREEN_8, new Card(-1, Card.COLOR_GREEN, 8, Card.ID_GREEN_8, 8));
		
		m_imageIDLookup.put (Card.ID_GREEN_9, R.drawable.card_green_9);
		m_imageLookup.put (Card.ID_GREEN_9, BitmapFactory.decodeResource(res, R.drawable.card_green_9, opt));
		m_cardHelpLookup.put (Card.ID_GREEN_9, R.string.cardhelp_9);
        m_cardLookup.put (Card.ID_GREEN_9, new Card(-1, Card.COLOR_GREEN, 9, Card.ID_GREEN_9, 9));
		
		m_imageIDLookup.put (Card.ID_GREEN_D, R.drawable.card_green_d);
		m_imageLookup.put (Card.ID_GREEN_D, BitmapFactory.decodeResource(res, R.drawable.card_green_d, opt));
		m_cardHelpLookup.put (Card.ID_GREEN_D, R.string.cardhelp_d);
        m_cardLookup.put (Card.ID_GREEN_D, new Card(-1, Card.COLOR_GREEN, Card.VAL_D, Card.ID_GREEN_D, 20));

		m_imageIDLookup.put (Card.ID_GREEN_S, R.drawable.card_green_s);
		m_imageLookup.put (Card.ID_GREEN_S, BitmapFactory.decodeResource(res, R.drawable.card_green_s, opt));
		m_cardHelpLookup.put (Card.ID_GREEN_S, R.string.cardhelp_s);
        m_cardLookup.put (Card.ID_GREEN_S, new Card(-1, Card.COLOR_GREEN, Card.VAL_S, Card.ID_GREEN_S, 20));

		m_imageIDLookup.put (Card.ID_GREEN_R, R.drawable.card_green_r);
		m_imageLookup.put (Card.ID_GREEN_R, BitmapFactory.decodeResource(res, R.drawable.card_green_r, opt));
		m_cardHelpLookup.put (Card.ID_GREEN_R, R.string.cardhelp_r);
        m_cardLookup.put (Card.ID_GREEN_R, new Card(-1, Card.COLOR_GREEN, Card.VAL_R, Card.ID_GREEN_R, 20));
				
		m_imageIDLookup.put (Card.ID_BLUE_0, R.drawable.card_blue_0);
		m_imageLookup.put (Card.ID_BLUE_0, BitmapFactory.decodeResource(res, R.drawable.card_blue_0, opt));
		m_cardHelpLookup.put (Card.ID_BLUE_0, R.string.cardhelp_0);
        m_cardLookup.put (Card.ID_BLUE_0, new Card(-1, Card.COLOR_BLUE, 0, Card.ID_BLUE_0, 0));
		
		m_imageIDLookup.put (Card.ID_BLUE_1, R.drawable.card_blue_1);
		m_imageLookup.put (Card.ID_BLUE_1, BitmapFactory.decodeResource(res, R.drawable.card_blue_1, opt));
		m_cardHelpLookup.put (Card.ID_BLUE_1, R.string.cardhelp_1);
        m_cardLookup.put (Card.ID_BLUE_1, new Card(-1, Card.COLOR_BLUE, 1, Card.ID_BLUE_1, 1));

		m_imageIDLookup.put (Card.ID_BLUE_2, R.drawable.card_blue_2);
		m_imageLookup.put (Card.ID_BLUE_2, BitmapFactory.decodeResource(res, R.drawable.card_blue_2, opt));
		m_cardHelpLookup.put (Card.ID_BLUE_2, R.string.cardhelp_2);
        m_cardLookup.put (Card.ID_BLUE_2, new Card(-1, Card.COLOR_BLUE, 2, Card.ID_BLUE_2, 2));
		
		m_imageIDLookup.put (Card.ID_BLUE_3, R.drawable.card_blue_3);
		m_imageLookup.put (Card.ID_BLUE_3, BitmapFactory.decodeResource(res, R.drawable.card_blue_3, opt));
		m_cardHelpLookup.put (Card.ID_BLUE_3, R.string.cardhelp_3);
        m_cardLookup.put (Card.ID_BLUE_3, new Card(-1, Card.COLOR_BLUE, 3, Card.ID_BLUE_3, 3));
		
		m_imageIDLookup.put (Card.ID_BLUE_4, R.drawable.card_blue_4);
		m_imageLookup.put (Card.ID_BLUE_4, BitmapFactory.decodeResource(res, R.drawable.card_blue_4, opt));
		m_cardHelpLookup.put (Card.ID_BLUE_4, R.string.cardhelp_4);
        m_cardLookup.put (Card.ID_BLUE_4, new Card(-1, Card.COLOR_BLUE, 4, Card.ID_BLUE_4, 4));
		
		m_imageIDLookup.put (Card.ID_BLUE_5, R.drawable.card_blue_5);
		m_imageLookup.put (Card.ID_BLUE_5, BitmapFactory.decodeResource(res, R.drawable.card_blue_5, opt));
		m_cardHelpLookup.put (Card.ID_BLUE_5, R.string.cardhelp_5);
        m_cardLookup.put (Card.ID_BLUE_5, new Card(-1, Card.COLOR_BLUE, 5, Card.ID_BLUE_5, 5));
		
		m_imageIDLookup.put (Card.ID_BLUE_6, R.drawable.card_blue_6);
		m_imageLookup.put (Card.ID_BLUE_6, BitmapFactory.decodeResource(res, R.drawable.card_blue_6, opt));
		m_cardHelpLookup.put (Card.ID_BLUE_6, R.string.cardhelp_6);
        m_cardLookup.put (Card.ID_BLUE_6, new Card(-1, Card.COLOR_BLUE, 6, Card.ID_BLUE_6, 6));
		
		m_imageIDLookup.put (Card.ID_BLUE_7, R.drawable.card_blue_7);
		m_imageLookup.put (Card.ID_BLUE_7, BitmapFactory.decodeResource(res, R.drawable.card_blue_7, opt));
		m_cardHelpLookup.put (Card.ID_BLUE_7, R.string.cardhelp_7);
        m_cardLookup.put (Card.ID_BLUE_7, new Card(-1, Card.COLOR_BLUE, 7, Card.ID_BLUE_7, 7));
		
		m_imageIDLookup.put (Card.ID_BLUE_8, R.drawable.card_blue_8);
		m_imageLookup.put (Card.ID_BLUE_8, BitmapFactory.decodeResource(res, R.drawable.card_blue_8, opt));
		m_cardHelpLookup.put (Card.ID_BLUE_8, R.string.cardhelp_8);
        m_cardLookup.put (Card.ID_BLUE_8, new Card(-1, Card.COLOR_BLUE, 8, Card.ID_BLUE_8, 8));
		
		m_imageIDLookup.put (Card.ID_BLUE_9, R.drawable.card_blue_9);
		m_imageLookup.put (Card.ID_BLUE_9, BitmapFactory.decodeResource(res, R.drawable.card_blue_9, opt));
		m_cardHelpLookup.put (Card.ID_BLUE_9, R.string.cardhelp_9);
        m_cardLookup.put (Card.ID_BLUE_9, new Card(-1, Card.COLOR_BLUE, 9, Card.ID_BLUE_9, 9));
		
		m_imageIDLookup.put (Card.ID_BLUE_D, R.drawable.card_blue_d);
		m_imageLookup.put (Card.ID_BLUE_D, BitmapFactory.decodeResource(res, R.drawable.card_blue_d, opt));
		m_cardHelpLookup.put (Card.ID_BLUE_D, R.string.cardhelp_d);
        m_cardLookup.put (Card.ID_BLUE_D, new Card(-1, Card.COLOR_BLUE, Card.VAL_D, Card.ID_BLUE_D, 20));

		m_imageIDLookup.put (Card.ID_BLUE_S, R.drawable.card_blue_s);
		m_imageLookup.put (Card.ID_BLUE_S, BitmapFactory.decodeResource(res, R.drawable.card_blue_s, opt));
		m_cardHelpLookup.put (Card.ID_BLUE_S, R.string.cardhelp_s);
        m_cardLookup.put (Card.ID_BLUE_S, new Card(-1, Card.COLOR_BLUE, Card.VAL_S, Card.ID_BLUE_S, 20));

		m_imageIDLookup.put (Card.ID_BLUE_R, R.drawable.card_blue_r);
		m_imageLookup.put (Card.ID_BLUE_R, BitmapFactory.decodeResource(res, R.drawable.card_blue_r, opt));
		m_cardHelpLookup.put (Card.ID_BLUE_R, R.string.cardhelp_r);
        m_cardLookup.put (Card.ID_BLUE_R, new Card(-1, Card.COLOR_BLUE, Card.VAL_R, Card.ID_BLUE_R, 20));
				
		m_imageIDLookup.put (Card.ID_YELLOW_0, R.drawable.card_yellow_0);
		m_imageLookup.put (Card.ID_YELLOW_0, BitmapFactory.decodeResource(res, R.drawable.card_yellow_0, opt));
		m_cardHelpLookup.put (Card.ID_YELLOW_0, R.string.cardhelp_0);
        m_cardLookup.put (Card.ID_YELLOW_0, new Card(-1, Card.COLOR_YELLOW, 0, Card.ID_YELLOW_0, 0));

		m_imageIDLookup.put (Card.ID_YELLOW_1, R.drawable.card_yellow_1);
		m_imageLookup.put (Card.ID_YELLOW_1, BitmapFactory.decodeResource(res, R.drawable.card_yellow_1, opt));
		m_cardHelpLookup.put (Card.ID_YELLOW_1, R.string.cardhelp_1);
        m_cardLookup.put (Card.ID_YELLOW_1, new Card(-1, Card.COLOR_YELLOW, 1, Card.ID_YELLOW_1, 1));
		
		m_imageIDLookup.put (Card.ID_YELLOW_2, R.drawable.card_yellow_2);
		m_imageLookup.put (Card.ID_YELLOW_2, BitmapFactory.decodeResource(res, R.drawable.card_yellow_2, opt));
		m_cardHelpLookup.put (Card.ID_YELLOW_2, R.string.cardhelp_2);
        m_cardLookup.put (Card.ID_YELLOW_2, new Card(-1, Card.COLOR_YELLOW, 2, Card.ID_YELLOW_2, 2));

		m_imageIDLookup.put (Card.ID_YELLOW_3, R.drawable.card_yellow_3);
		m_imageLookup.put (Card.ID_YELLOW_3, BitmapFactory.decodeResource(res, R.drawable.card_yellow_3, opt));
		m_cardHelpLookup.put (Card.ID_YELLOW_3, R.string.cardhelp_3);
        m_cardLookup.put (Card.ID_YELLOW_3, new Card(-1, Card.COLOR_YELLOW, 3, Card.ID_YELLOW_3, 3));
		
		m_imageIDLookup.put (Card.ID_YELLOW_4, R.drawable.card_yellow_4);
		m_imageLookup.put (Card.ID_YELLOW_4, BitmapFactory.decodeResource(res, R.drawable.card_yellow_4, opt));
		m_cardHelpLookup.put (Card.ID_YELLOW_4, R.string.cardhelp_4);
        m_cardLookup.put (Card.ID_YELLOW_4, new Card(-1, Card.COLOR_YELLOW, 4, Card.ID_YELLOW_4, 4));
		
		m_imageIDLookup.put (Card.ID_YELLOW_5, R.drawable.card_yellow_5);
		m_imageLookup.put (Card.ID_YELLOW_5, BitmapFactory.decodeResource(res, R.drawable.card_yellow_5, opt));
		m_cardHelpLookup.put (Card.ID_YELLOW_5, R.string.cardhelp_5);
        m_cardLookup.put (Card.ID_YELLOW_5, new Card(-1, Card.COLOR_YELLOW, 5, Card.ID_YELLOW_5, 5));
		
		m_imageIDLookup.put (Card.ID_YELLOW_6, R.drawable.card_yellow_6);
		m_imageLookup.put (Card.ID_YELLOW_6, BitmapFactory.decodeResource(res, R.drawable.card_yellow_6, opt));
		m_cardHelpLookup.put (Card.ID_YELLOW_6, R.string.cardhelp_6);
        m_cardLookup.put (Card.ID_YELLOW_6, new Card(-1, Card.COLOR_YELLOW, 6, Card.ID_YELLOW_6, 6));
		
		m_imageIDLookup.put (Card.ID_YELLOW_7, R.drawable.card_yellow_7);
		m_imageLookup.put (Card.ID_YELLOW_7, BitmapFactory.decodeResource(res, R.drawable.card_yellow_7, opt));
		m_cardHelpLookup.put (Card.ID_YELLOW_7, R.string.cardhelp_7);
        m_cardLookup.put (Card.ID_YELLOW_7, new Card(-1, Card.COLOR_YELLOW, 7, Card.ID_YELLOW_7, 7));

		m_imageIDLookup.put (Card.ID_YELLOW_8, R.drawable.card_yellow_8);
		m_imageLookup.put (Card.ID_YELLOW_8, BitmapFactory.decodeResource(res, R.drawable.card_yellow_8, opt));
		m_cardHelpLookup.put (Card.ID_YELLOW_8, R.string.cardhelp_8);
        m_cardLookup.put (Card.ID_YELLOW_8, new Card(-1, Card.COLOR_YELLOW, 8, Card.ID_YELLOW_8, 8));
		
		m_imageIDLookup.put (Card.ID_YELLOW_9, R.drawable.card_yellow_9);
		m_imageLookup.put (Card.ID_YELLOW_9, BitmapFactory.decodeResource(res, R.drawable.card_yellow_9, opt));
		m_cardHelpLookup.put (Card.ID_YELLOW_9, R.string.cardhelp_9);
        m_cardLookup.put (Card.ID_YELLOW_9, new Card(-1, Card.COLOR_YELLOW, 9, Card.ID_YELLOW_9, 9));
		
		m_imageIDLookup.put (Card.ID_YELLOW_D, R.drawable.card_yellow_d);
		m_imageLookup.put (Card.ID_YELLOW_D, BitmapFactory.decodeResource(res, R.drawable.card_yellow_d, opt));
		m_cardHelpLookup.put (Card.ID_YELLOW_D, R.string.cardhelp_d);
        m_cardLookup.put (Card.ID_YELLOW_D, new Card(-1, Card.COLOR_YELLOW, Card.VAL_D, Card.ID_YELLOW_D, 20));

		m_imageIDLookup.put (Card.ID_YELLOW_S, R.drawable.card_yellow_s);
		m_imageLookup.put (Card.ID_YELLOW_S, BitmapFactory.decodeResource(res, R.drawable.card_yellow_s, opt));
		m_cardHelpLookup.put (Card.ID_YELLOW_S, R.string.cardhelp_s);
        m_cardLookup.put (Card.ID_YELLOW_S, new Card(-1, Card.COLOR_YELLOW, Card.VAL_S, Card.ID_YELLOW_S, 20));

		m_imageIDLookup.put (Card.ID_YELLOW_R, R.drawable.card_yellow_r);
		m_imageLookup.put (Card.ID_YELLOW_R, BitmapFactory.decodeResource(res, R.drawable.card_yellow_r, opt));
		m_cardHelpLookup.put (Card.ID_YELLOW_R, R.string.cardhelp_r);
        m_cardLookup.put (Card.ID_YELLOW_R, new Card(-1, Card.COLOR_YELLOW, Card.VAL_R, Card.ID_YELLOW_R, 20));
		
		
		m_imageIDLookup.put (Card.ID_WILD, R.drawable.card_wild);
		m_imageLookup.put (Card.ID_WILD, BitmapFactory.decodeResource(res, R.drawable.card_wild, opt));
		m_cardHelpLookup.put (Card.ID_WILD, R.string.cardhelp_wild);
        m_cardLookup.put (Card.ID_WILD, new Card(-1, Card.COLOR_WILD, Card.VAL_WILD, Card.ID_WILD, 50));
		
		m_imageIDLookup.put (Card.ID_WILD_DRAWFOUR, R.drawable.card_wild_drawfour);
		m_imageLookup.put (Card.ID_WILD_DRAWFOUR, BitmapFactory.decodeResource(res, R.drawable.card_wild_drawfour, opt));
		m_cardHelpLookup.put (Card.ID_WILD_DRAWFOUR, R.string.cardhelp_wild_drawfour);
        m_cardLookup.put (Card.ID_WILD_DRAWFOUR, new Card(-1, Card.COLOR_WILD, Card.VAL_WILD_DRAWFOUR, Card.ID_WILD_DRAWFOUR, 50));
		
		m_imageIDLookup.put (Card.ID_WILD_HOS, R.drawable.card_wild_hos);
		m_imageLookup.put (Card.ID_WILD_HOS, BitmapFactory.decodeResource(res, R.drawable.card_wild_hos, opt));
		m_cardHelpLookup.put (Card.ID_WILD_HOS, R.string.cardhelp_wild_hos);
        m_cardLookup.put (Card.ID_WILD_HOS, new Card(-1, Card.COLOR_WILD, Card.VAL_WILD_DRAWFOUR, Card.ID_WILD_HOS, 0));
		
		m_imageIDLookup.put (Card.ID_WILD_HD, R.drawable.card_wild_hd);
		m_imageLookup.put (Card.ID_WILD_HD, BitmapFactory.decodeResource(res, R.drawable.card_wild_hd, opt));
		m_cardHelpLookup.put (Card.ID_WILD_HD, R.string.cardhelp_wild_hd);
        m_cardLookup.put (Card.ID_WILD_HD, new Card(-1, Card.COLOR_WILD, Card.VAL_WILD_DRAWFOUR, Card.ID_WILD_HD, 100));
		
		m_imageIDLookup.put (Card.ID_WILD_MYSTERY, R.drawable.card_wild_mystery);
		m_imageLookup.put (Card.ID_WILD_MYSTERY, BitmapFactory.decodeResource(res, R.drawable.card_wild_mystery, opt));
		m_cardHelpLookup.put (Card.ID_WILD_MYSTERY, R.string.cardhelp_wild_mystery);
        m_cardLookup.put (Card.ID_WILD_MYSTERY, new Card(-1, Card.COLOR_WILD, Card.VAL_WILD_DRAWFOUR, Card.ID_WILD_MYSTERY, 0));
		
		m_imageIDLookup.put (Card.ID_WILD_DB, R.drawable.card_wild_db);
		m_imageLookup.put (Card.ID_WILD_DB, BitmapFactory.decodeResource(res, R.drawable.card_wild_db, opt));
		m_cardHelpLookup.put (Card.ID_WILD_DB, R.string.cardhelp_wild_db);
        m_cardLookup.put (Card.ID_WILD_DB, new Card(-1, Card.COLOR_WILD, Card.VAL_WILD_DRAWFOUR, Card.ID_WILD_DB, 100));
		
		m_imageIDLookup.put (Card.ID_RED_0_HD, R.drawable.card_red_0_hd);
		m_imageLookup.put (Card.ID_RED_0_HD, BitmapFactory.decodeResource(res, R.drawable.card_red_0_hd, opt));
        if (m_go.getFamilyFriendly())
		{
			m_cardHelpLookup.put (Card.ID_RED_0_HD, R.string.cardhelp_red_0_hd_ff);
		}
		else
		{
			m_cardHelpLookup.put (Card.ID_RED_0_HD, R.string.cardhelp_red_0_hd);
		}
        m_cardLookup.put (Card.ID_RED_0_HD, new Card(-1, Card.COLOR_RED, 0, Card.ID_RED_0_HD, 0, 0.5));

		m_imageIDLookup.put (Card.ID_RED_2_GLASNOST, R.drawable.card_red_2_glasnost);
		m_imageLookup.put (Card.ID_RED_2_GLASNOST, BitmapFactory.decodeResource(res, R.drawable.card_red_2_glasnost, opt));
		m_cardHelpLookup.put (Card.ID_RED_2_GLASNOST, R.string.cardhelp_red_2_glasnost);
        m_cardLookup.put (Card.ID_RED_2_GLASNOST, new Card(-1, Card.COLOR_RED, 2, Card.ID_RED_2_GLASNOST, 75));
		
		m_imageIDLookup.put (Card.ID_RED_5_MAGIC, R.drawable.card_red_5_magic);
		m_imageLookup.put (Card.ID_RED_5_MAGIC, BitmapFactory.decodeResource(res, R.drawable.card_red_5_magic, opt));
		m_cardHelpLookup.put (Card.ID_RED_5_MAGIC, R.string.cardhelp_red_5_magic);
        m_cardLookup.put (Card.ID_RED_5_MAGIC, new Card(-1, Card.COLOR_RED, 5, Card.ID_RED_5_MAGIC, -5));
		
		m_imageIDLookup.put (Card.ID_RED_D_SPREADER, R.drawable.card_red_d_spreader);
		m_imageLookup.put (Card.ID_RED_D_SPREADER, BitmapFactory.decodeResource(res, R.drawable.card_red_d_spreader, opt));
		m_cardHelpLookup.put (Card.ID_RED_D_SPREADER, R.string.cardhelp_d_spread);
        m_cardLookup.put (Card.ID_RED_D_SPREADER, new Card(-1, Card.COLOR_RED, Card.VAL_D_SPREAD, Card.ID_RED_D_SPREADER, 60));

		m_imageIDLookup.put (Card.ID_RED_S_DOUBLE, R.drawable.card_red_s_double);
		m_imageLookup.put (Card.ID_RED_S_DOUBLE, BitmapFactory.decodeResource(res, R.drawable.card_red_s_double, opt));
		m_cardHelpLookup.put (Card.ID_RED_S_DOUBLE, R.string.cardhelp_s_double);
        m_cardLookup.put (Card.ID_RED_S_DOUBLE, new Card(-1, Card.COLOR_RED, Card.VAL_S_DOUBLE, Card.ID_RED_S_DOUBLE, 40));
		
		m_imageIDLookup.put (Card.ID_RED_R_SKIP, R.drawable.card_red_r_skip);
		m_imageLookup.put (Card.ID_RED_R_SKIP, BitmapFactory.decodeResource(res, R.drawable.card_red_r_skip, opt));
		m_cardHelpLookup.put (Card.ID_RED_R_SKIP, R.string.cardhelp_r_skip);
        m_cardLookup.put (Card.ID_RED_R_SKIP, new Card(-1, Card.COLOR_RED, Card.VAL_R_SKIP, Card.ID_RED_R_SKIP, 40));
		
		m_imageIDLookup.put (Card.ID_GREEN_0_QUITTER, R.drawable.card_green_0_quitter);
		m_imageLookup.put (Card.ID_GREEN_0_QUITTER, BitmapFactory.decodeResource(res, R.drawable.card_green_0_quitter, opt));
		if (m_go.getFamilyFriendly())
		{
			m_cardHelpLookup.put (Card.ID_GREEN_0_QUITTER, R.string.cardhelp_green_0_quitter_ff);
		}
		else
		{
			m_cardHelpLookup.put (Card.ID_GREEN_0_QUITTER, R.string.cardhelp_green_0_quitter);			
		}
        m_cardLookup.put (Card.ID_GREEN_0_QUITTER, new Card(-1, Card.COLOR_GREEN, 0, Card.ID_GREEN_0_QUITTER, 100));
		
        if (m_go.getFamilyFriendly())
		{
			m_imageIDLookup.put (Card.ID_GREEN_3_AIDS, R.drawable.card_green_3_aids_ff);
			m_imageLookup.put (Card.ID_GREEN_3_AIDS, BitmapFactory.decodeResource(res, R.drawable.card_green_3_aids_ff, opt));
			m_cardHelpLookup.put (Card.ID_GREEN_3_AIDS, R.string.cardhelp_green_3_aids_ff);
		}
		else
		{
			m_imageIDLookup.put (Card.ID_GREEN_3_AIDS, R.drawable.card_green_3_aids);
			m_imageLookup.put (Card.ID_GREEN_3_AIDS, BitmapFactory.decodeResource(res, R.drawable.card_green_3_aids, opt));
			m_cardHelpLookup.put (Card.ID_GREEN_3_AIDS, R.string.cardhelp_green_3_aids);
		}
        m_cardLookup.put (Card.ID_GREEN_3_AIDS, new Card(-1, Card.COLOR_GREEN, 3, Card.ID_GREEN_3_AIDS, 3, 1.0, 10));
		
		m_imageIDLookup.put (Card.ID_GREEN_4_IRISH, R.drawable.card_green_4_irish);
		m_imageLookup.put (Card.ID_GREEN_4_IRISH, BitmapFactory.decodeResource(res, R.drawable.card_green_4_irish, opt));
		m_cardHelpLookup.put (Card.ID_GREEN_4_IRISH, R.string.cardhelp_green_4_irish);
        m_cardLookup.put (Card.ID_GREEN_4_IRISH, new Card(-1, Card.COLOR_GREEN, 4, Card.ID_GREEN_4_IRISH, 75));
		
		m_imageIDLookup.put (Card.ID_GREEN_D_SPREADER, R.drawable.card_green_d_spreader);
		m_imageLookup.put (Card.ID_GREEN_D_SPREADER, BitmapFactory.decodeResource(res, R.drawable.card_green_d_spreader, opt));
		m_cardHelpLookup.put (Card.ID_GREEN_D_SPREADER, R.string.cardhelp_d_spread);
        m_cardLookup.put (Card.ID_GREEN_D_SPREADER, new Card(-1, Card.COLOR_GREEN, Card.VAL_D_SPREAD, Card.ID_GREEN_D_SPREADER, 60));
		
		m_imageIDLookup.put (Card.ID_GREEN_S_DOUBLE, R.drawable.card_green_s_double);
		m_imageLookup.put (Card.ID_GREEN_S_DOUBLE, BitmapFactory.decodeResource(res, R.drawable.card_green_s_double, opt));
		m_cardHelpLookup.put (Card.ID_GREEN_S_DOUBLE, R.string.cardhelp_s_double);
        m_cardLookup.put (Card.ID_GREEN_S_DOUBLE, new Card(-1, Card.COLOR_GREEN, Card.VAL_S_DOUBLE, Card.ID_GREEN_S_DOUBLE, 40));
		
		m_imageIDLookup.put (Card.ID_GREEN_R_SKIP, R.drawable.card_green_r_skip);
		m_imageLookup.put (Card.ID_GREEN_R_SKIP, BitmapFactory.decodeResource(res, R.drawable.card_green_r_skip, opt));
		m_cardHelpLookup.put (Card.ID_GREEN_R_SKIP, R.string.cardhelp_r_skip);
        m_cardLookup.put (Card.ID_GREEN_R_SKIP, new Card(-1, Card.COLOR_GREEN, Card.VAL_R_SKIP, Card.ID_GREEN_R_SKIP, 40));		
		
        if (m_go.getFamilyFriendly())
		{
			m_imageIDLookup.put (Card.ID_BLUE_0_FUCKYOU, R.drawable.card_blue_0_fuckyou_ff);
			m_imageLookup.put (Card.ID_BLUE_0_FUCKYOU, BitmapFactory.decodeResource(res, R.drawable.card_blue_0_fuckyou_ff, opt));
			m_cardHelpLookup.put (Card.ID_BLUE_0_FUCKYOU, R.string.cardhelp_blue_0_fuck_you_ff);
		}
		else
		{
			m_imageIDLookup.put (Card.ID_BLUE_0_FUCKYOU, R.drawable.card_blue_0_fuckyou);
			m_imageLookup.put (Card.ID_BLUE_0_FUCKYOU, BitmapFactory.decodeResource(res, R.drawable.card_blue_0_fuckyou, opt));
			m_cardHelpLookup.put (Card.ID_BLUE_0_FUCKYOU, R.string.cardhelp_blue_0_fuck_you);
		}
        m_cardLookup.put (Card.ID_BLUE_0_FUCKYOU, new Card(-1, Card.COLOR_BLUE, 0, Card.ID_BLUE_0_FUCKYOU, 0, 2.0));
		
		m_imageIDLookup.put (Card.ID_BLUE_2_SHIELD, R.drawable.card_blue_2_shield);
		m_imageLookup.put (Card.ID_BLUE_2_SHIELD, BitmapFactory.decodeResource(res, R.drawable.card_blue_2_shield, opt));
		m_cardHelpLookup.put (Card.ID_BLUE_2_SHIELD, R.string.cardhelp_blue_2_shield);
        m_cardLookup.put (Card.ID_BLUE_2_SHIELD, new Card(-1, Card.COLOR_BLUE, 2, Card.ID_BLUE_2_SHIELD, 0, 1.0, 0, 1));
		
		m_imageIDLookup.put (Card.ID_BLUE_D_SPREADER, R.drawable.card_blue_d_spreader);
		m_imageLookup.put (Card.ID_BLUE_D_SPREADER, BitmapFactory.decodeResource(res, R.drawable.card_blue_d_spreader, opt));
		m_cardHelpLookup.put (Card.ID_BLUE_D_SPREADER, R.string.cardhelp_d_spread);
        m_cardLookup.put (Card.ID_BLUE_D_SPREADER, new Card(-1, Card.COLOR_BLUE, Card.VAL_D_SPREAD, Card.ID_BLUE_D_SPREADER, 60));

		m_imageIDLookup.put (Card.ID_BLUE_S_DOUBLE, R.drawable.card_blue_s_double);
		m_imageLookup.put (Card.ID_BLUE_S_DOUBLE, BitmapFactory.decodeResource(res, R.drawable.card_blue_s_double, opt));
		m_cardHelpLookup.put (Card.ID_BLUE_S_DOUBLE, R.string.cardhelp_s_double);
        m_cardLookup.put (Card.ID_BLUE_S_DOUBLE, new Card(-1, Card.COLOR_BLUE, Card.VAL_S_DOUBLE, Card.ID_BLUE_S_DOUBLE, 40));

		m_imageIDLookup.put (Card.ID_BLUE_R_SKIP, R.drawable.card_blue_r_skip);
		m_imageLookup.put (Card.ID_BLUE_R_SKIP, BitmapFactory.decodeResource(res, R.drawable.card_blue_r_skip, opt));
		m_cardHelpLookup.put (Card.ID_BLUE_R_SKIP, R.string.cardhelp_r_skip);
        m_cardLookup.put (Card.ID_BLUE_R_SKIP, new Card(-1, Card.COLOR_BLUE, Card.VAL_R_SKIP, Card.ID_BLUE_R_SKIP, 40));		
		
        if (m_go.getFamilyFriendly())
		{
			m_imageIDLookup.put (Card.ID_YELLOW_0_SHITTER, R.drawable.card_yellow_0_shitter_ff);
			m_imageLookup.put (Card.ID_YELLOW_0_SHITTER, BitmapFactory.decodeResource(res, R.drawable.card_yellow_0_shitter_ff, opt));
			m_cardHelpLookup.put (Card.ID_YELLOW_0_SHITTER, R.string.cardhelp_yellow_0_shitter_ff);
		}
		else
		{
			m_imageIDLookup.put (Card.ID_YELLOW_0_SHITTER, R.drawable.card_yellow_0_shitter);
			m_imageLookup.put (Card.ID_YELLOW_0_SHITTER, BitmapFactory.decodeResource(res, R.drawable.card_yellow_0_shitter, opt));
			m_cardHelpLookup.put (Card.ID_YELLOW_0_SHITTER, R.string.cardhelp_yellow_0_shitter);
		}
        m_cardLookup.put (Card.ID_YELLOW_0_SHITTER, new Card(-1, Card.COLOR_YELLOW, 0, Card.ID_YELLOW_0_SHITTER, 0));

		m_imageIDLookup.put (Card.ID_YELLOW_1_MAD, R.drawable.card_yellow_1_mad);
		m_imageLookup.put (Card.ID_YELLOW_1_MAD, BitmapFactory.decodeResource(res, R.drawable.card_yellow_1_mad, opt));
		m_cardHelpLookup.put (Card.ID_YELLOW_1_MAD, R.string.cardhelp_yellow_1_mad);
        m_cardLookup.put (Card.ID_YELLOW_1_MAD, new Card(-1, Card.COLOR_YELLOW, 1, Card.ID_YELLOW_1_MAD, 100));

		m_imageIDLookup.put (Card.ID_YELLOW_69, R.drawable.card_yellow_69);
		m_imageLookup.put (Card.ID_YELLOW_69, BitmapFactory.decodeResource(res, R.drawable.card_yellow_69, opt));
		if (m_go.getFamilyFriendly())
		{
			m_cardHelpLookup.put (Card.ID_YELLOW_69, R.string.cardhelp_yellow_69_ff);
		}
		else
		{
			m_cardHelpLookup.put (Card.ID_YELLOW_69, R.string.cardhelp_yellow_69);	
		}
        m_cardLookup.put (Card.ID_YELLOW_69, new Card(-1, Card.COLOR_YELLOW, 6, Card.ID_YELLOW_69, 6));

		m_imageIDLookup.put (Card.ID_YELLOW_D_SPREADER, R.drawable.card_yellow_d_spreader);
		m_imageLookup.put (Card.ID_YELLOW_D_SPREADER, BitmapFactory.decodeResource(res, R.drawable.card_yellow_d_spreader, opt));
		m_cardHelpLookup.put (Card.ID_YELLOW_D_SPREADER, R.string.cardhelp_d_spread);
        m_cardLookup.put (Card.ID_YELLOW_D_SPREADER, new Card(-1, Card.COLOR_YELLOW, Card.VAL_D_SPREAD, Card.ID_YELLOW_D_SPREADER, 60));

		m_imageIDLookup.put (Card.ID_YELLOW_S_DOUBLE, R.drawable.card_yellow_s_double);
		m_imageLookup.put (Card.ID_YELLOW_S_DOUBLE, BitmapFactory.decodeResource(res, R.drawable.card_yellow_s_double, opt));
		m_cardHelpLookup.put (Card.ID_YELLOW_S_DOUBLE, R.string.cardhelp_s_double);
        m_cardLookup.put (Card.ID_YELLOW_S_DOUBLE, new Card(-1, Card.COLOR_YELLOW, Card.VAL_S_DOUBLE, Card.ID_YELLOW_S_DOUBLE, 40));

		m_imageIDLookup.put (Card.ID_YELLOW_R_SKIP, R.drawable.card_yellow_r_skip);
		m_imageLookup.put (Card.ID_YELLOW_R_SKIP, BitmapFactory.decodeResource(res, R.drawable.card_yellow_r_skip, opt));
		m_cardHelpLookup.put (Card.ID_YELLOW_R_SKIP, R.string.cardhelp_r_skip);
        m_cardLookup.put (Card.ID_YELLOW_R_SKIP, new Card(-1, Card.COLOR_YELLOW, Card.VAL_R_SKIP, Card.ID_YELLOW_R_SKIP, 40));		

		m_bmpDirColorCCW = BitmapFactory.decodeResource(res, R.drawable.ccw, opt);
		m_bmpDirColorCCWRed = BitmapFactory.decodeResource(res, R.drawable.ccw_red, opt);
		m_bmpDirColorCCWBlue = BitmapFactory.decodeResource(res, R.drawable.ccw_blue, opt);
		m_bmpDirColorCCWGreen = BitmapFactory.decodeResource(res, R.drawable.ccw_green, opt);
		m_bmpDirColorCCWYellow = BitmapFactory.decodeResource(res, R.drawable.ccw_yellow, opt);

		m_bmpDirColorCW = BitmapFactory.decodeResource(res, R.drawable.cw, opt);
		m_bmpDirColorCWRed = BitmapFactory.decodeResource(res, R.drawable.cw_red, opt);
		m_bmpDirColorCWBlue = BitmapFactory.decodeResource(res, R.drawable.cw_blue, opt);
		m_bmpDirColorCWGreen = BitmapFactory.decodeResource(res, R.drawable.cw_green, opt);
		m_bmpDirColorCWYellow = BitmapFactory.decodeResource(res, R.drawable.cw_yellow, opt);
		
		m_bmpPlayerIndicator[Card.COLOR_RED - 1][Game.SEAT_SOUTH - 1] = BitmapFactory.decodeResource(res, R.drawable.player_red_south, opt);
		m_bmpPlayerIndicator[Card.COLOR_GREEN - 1][Game.SEAT_SOUTH - 1] = BitmapFactory.decodeResource(res, R.drawable.player_green_south, opt);
		m_bmpPlayerIndicator[Card.COLOR_BLUE - 1][Game.SEAT_SOUTH - 1] = BitmapFactory.decodeResource(res, R.drawable.player_blue_south, opt);
		m_bmpPlayerIndicator[Card.COLOR_YELLOW - 1][Game.SEAT_SOUTH - 1] = BitmapFactory.decodeResource(res, R.drawable.player_yellow_south, opt);
		m_bmpPlayerIndicator[Card.COLOR_WILD - 1][Game.SEAT_SOUTH - 1] = BitmapFactory.decodeResource(res, R.drawable.player_south, opt);

		m_bmpPlayerIndicator[Card.COLOR_RED - 1][Game.SEAT_WEST - 1] = BitmapFactory.decodeResource(res, R.drawable.player_red_west, opt);
		m_bmpPlayerIndicator[Card.COLOR_GREEN - 1][Game.SEAT_WEST - 1] = BitmapFactory.decodeResource(res, R.drawable.player_green_west, opt);
		m_bmpPlayerIndicator[Card.COLOR_BLUE - 1][Game.SEAT_WEST - 1] = BitmapFactory.decodeResource(res, R.drawable.player_blue_west, opt);
		m_bmpPlayerIndicator[Card.COLOR_YELLOW - 1][Game.SEAT_WEST - 1] = BitmapFactory.decodeResource(res, R.drawable.player_yellow_west, opt);
		m_bmpPlayerIndicator[Card.COLOR_WILD - 1][Game.SEAT_WEST - 1] = BitmapFactory.decodeResource(res, R.drawable.player_west, opt);
		
		m_bmpPlayerIndicator[Card.COLOR_RED - 1][Game.SEAT_NORTH - 1] = BitmapFactory.decodeResource(res, R.drawable.player_red_north, opt);
		m_bmpPlayerIndicator[Card.COLOR_GREEN - 1][Game.SEAT_NORTH - 1] = BitmapFactory.decodeResource(res, R.drawable.player_green_north, opt);
		m_bmpPlayerIndicator[Card.COLOR_BLUE - 1][Game.SEAT_NORTH - 1] = BitmapFactory.decodeResource(res, R.drawable.player_blue_north, opt);
		m_bmpPlayerIndicator[Card.COLOR_YELLOW - 1][Game.SEAT_NORTH - 1] = BitmapFactory.decodeResource(res, R.drawable.player_yellow_north, opt);
		m_bmpPlayerIndicator[Card.COLOR_WILD - 1][Game.SEAT_NORTH - 1] = BitmapFactory.decodeResource(res, R.drawable.player_north, opt);

		m_bmpPlayerIndicator[Card.COLOR_RED - 1][Game.SEAT_EAST - 1] = BitmapFactory.decodeResource(res, R.drawable.player_red_east, opt);
		m_bmpPlayerIndicator[Card.COLOR_GREEN - 1][Game.SEAT_EAST - 1] = BitmapFactory.decodeResource(res, R.drawable.player_green_east, opt);
		m_bmpPlayerIndicator[Card.COLOR_BLUE - 1][Game.SEAT_EAST - 1] = BitmapFactory.decodeResource(res, R.drawable.player_blue_east, opt);
		m_bmpPlayerIndicator[Card.COLOR_YELLOW - 1][Game.SEAT_EAST - 1] = BitmapFactory.decodeResource(res, R.drawable.player_yellow_east, opt);
		m_bmpPlayerIndicator[Card.COLOR_WILD - 1][Game.SEAT_EAST - 1] = BitmapFactory.decodeResource(res, R.drawable.player_east, opt);
		
		m_bmpWinningMessage[Game.SEAT_SOUTH - 1] = BitmapFactory.decodeResource(res, R.drawable.winner_south, opt);
		m_bmpWinningMessage[Game.SEAT_WEST - 1] = BitmapFactory.decodeResource(res, R.drawable.winner_west, opt);
		m_bmpWinningMessage[Game.SEAT_NORTH - 1] = BitmapFactory.decodeResource(res, R.drawable.winner_north, opt);
		m_bmpWinningMessage[Game.SEAT_EAST - 1] = BitmapFactory.decodeResource(res, R.drawable.winner_east, opt);
		

		m_bmpCardBadge = BitmapFactory.decodeResource(res, R.drawable.card_badge, opt);
		
		m_bmpEmoticonAggressor = BitmapFactory.decodeResource(res, R.drawable.emoticon_aggressor, opt);
		m_bmpEmoticonVictim = BitmapFactory.decodeResource(res, R.drawable.emoticon_victim, opt);
		
		int i = 0;
		
	    m_cardIDs[i++] = Card.ID_RED_0;
	    m_cardIDs[i++] = Card.ID_RED_0_HD;		
	    m_cardIDs[i++] = Card.ID_RED_1;
	    m_cardIDs[i++] = Card.ID_RED_2;
	    m_cardIDs[i++] = Card.ID_RED_2_GLASNOST;		
	    m_cardIDs[i++] = Card.ID_RED_3;
	    m_cardIDs[i++] = Card.ID_RED_4;
	    m_cardIDs[i++] = Card.ID_RED_5;
	    m_cardIDs[i++] = Card.ID_RED_5_MAGIC;		
	    m_cardIDs[i++] = Card.ID_RED_6;
	    m_cardIDs[i++] = Card.ID_RED_7;
	    m_cardIDs[i++] = Card.ID_RED_8;
	    m_cardIDs[i++] = Card.ID_RED_9;
	    m_cardIDs[i++] = Card.ID_RED_D;
	    m_cardIDs[i++] = Card.ID_RED_D_SPREADER;		
	    m_cardIDs[i++] = Card.ID_RED_S;
	    m_cardIDs[i++] = Card.ID_RED_S_DOUBLE;		
	    m_cardIDs[i++] = Card.ID_RED_R;		
	    m_cardIDs[i++] = Card.ID_RED_R_SKIP;
	    	    
	    m_cardIDs[i++] = Card.ID_GREEN_0;
	    m_cardIDs[i++] = Card.ID_GREEN_0_QUITTER;		
	    m_cardIDs[i++] = Card.ID_GREEN_1;
	    m_cardIDs[i++] = Card.ID_GREEN_2;
	    m_cardIDs[i++] = Card.ID_GREEN_3;
	    m_cardIDs[i++] = Card.ID_GREEN_3_AIDS;		
	    m_cardIDs[i++] = Card.ID_GREEN_4;
	    m_cardIDs[i++] = Card.ID_GREEN_4_IRISH;
	    m_cardIDs[i++] = Card.ID_GREEN_5;
	    m_cardIDs[i++] = Card.ID_GREEN_6;
	    m_cardIDs[i++] = Card.ID_GREEN_7;
	    m_cardIDs[i++] = Card.ID_GREEN_8;
	    m_cardIDs[i++] = Card.ID_GREEN_9;
	    m_cardIDs[i++] = Card.ID_GREEN_D;
	    m_cardIDs[i++] = Card.ID_GREEN_D_SPREADER;
	    m_cardIDs[i++] = Card.ID_GREEN_S;
	    m_cardIDs[i++] = Card.ID_GREEN_S_DOUBLE;
	    m_cardIDs[i++] = Card.ID_GREEN_R;
	    m_cardIDs[i++] = Card.ID_GREEN_R_SKIP;
	    	    
	    m_cardIDs[i++] = Card.ID_BLUE_0;
	    m_cardIDs[i++] = Card.ID_BLUE_0_FUCKYOU;
	    m_cardIDs[i++] = Card.ID_BLUE_1;
	    m_cardIDs[i++] = Card.ID_BLUE_2;
	    m_cardIDs[i++] = Card.ID_BLUE_2_SHIELD;
	    m_cardIDs[i++] = Card.ID_BLUE_3;
	    m_cardIDs[i++] = Card.ID_BLUE_4;
	    m_cardIDs[i++] = Card.ID_BLUE_5;
	    m_cardIDs[i++] = Card.ID_BLUE_6;
	    m_cardIDs[i++] = Card.ID_BLUE_7;
	    m_cardIDs[i++] = Card.ID_BLUE_8;
	    m_cardIDs[i++] = Card.ID_BLUE_9;
	    m_cardIDs[i++] = Card.ID_BLUE_D;
	    m_cardIDs[i++] = Card.ID_BLUE_D_SPREADER;
	    m_cardIDs[i++] = Card.ID_BLUE_S;
	    m_cardIDs[i++] = Card.ID_BLUE_S_DOUBLE;
	    m_cardIDs[i++] = Card.ID_BLUE_R;
	    m_cardIDs[i++] = Card.ID_BLUE_R_SKIP;		

	    m_cardIDs[i++] = Card.ID_YELLOW_0;
	    m_cardIDs[i++] = Card.ID_YELLOW_0_SHITTER;		
	    m_cardIDs[i++] = Card.ID_YELLOW_1;
	    m_cardIDs[i++] = Card.ID_YELLOW_1_MAD;
	    m_cardIDs[i++] = Card.ID_YELLOW_2;
	    m_cardIDs[i++] = Card.ID_YELLOW_3;
	    m_cardIDs[i++] = Card.ID_YELLOW_4;
	    m_cardIDs[i++] = Card.ID_YELLOW_5;
	    m_cardIDs[i++] = Card.ID_YELLOW_6;
	    m_cardIDs[i++] = Card.ID_YELLOW_69;
	    m_cardIDs[i++] = Card.ID_YELLOW_7;
	    m_cardIDs[i++] = Card.ID_YELLOW_8;
	    m_cardIDs[i++] = Card.ID_YELLOW_9;
	    m_cardIDs[i++] = Card.ID_YELLOW_D;
	    m_cardIDs[i++] = Card.ID_YELLOW_D_SPREADER;
	    m_cardIDs[i++] = Card.ID_YELLOW_S;
	    m_cardIDs[i++] = Card.ID_YELLOW_S_DOUBLE;
	    m_cardIDs[i++] = Card.ID_YELLOW_R;	
	    m_cardIDs[i++] = Card.ID_YELLOW_R_SKIP;
	    	    
	    m_cardIDs[i++] = Card.ID_WILD;
	    m_cardIDs[i++] = Card.ID_WILD_DRAWFOUR;
	    m_cardIDs[i++] = Card.ID_WILD_HOS;
	    m_cardIDs[i++] = Card.ID_WILD_HD;
	    m_cardIDs[i++] = Card.ID_WILD_MYSTERY;
	    m_cardIDs[i++] = Card.ID_WILD_DB;
	}
	
	
	private void drawCard (Canvas cv, Card c, int x, int y, boolean faceup)
	{
		m_drawMatrix.reset();
		m_drawMatrix.setScale(1, 1);
		m_drawMatrix.setTranslate(x, y);

		Bitmap b;
		if (faceup || m_go.getFaceUp()) 
		{
			b = m_imageLookup.get(c.getID());
		}
		else 
		{
			b = m_bmpCardBack;
			/*
			 * show some cards upside down -- this doesn't look as good as I thought it would
			 */
			/*
			Random rgen = new Random();
			int orientation = rgen.nextInt(100);
			if (orientation < 25)
			{
				m_drawMatrix.postRotate(180, x + b.getWidth() / 2, y + b.getHeight() / 2); 
			}
			*/
		}

		
		cv.drawBitmap(b, m_drawMatrix, null);
	}
	
	
	private void drawPenalty(Canvas cv)
	{
	    // draw penalty!
	    Penalty p = m_game.getPenalty();
	    
	    if (p.getType() == Penalty.PENTYPE_NONE)
	    {
	    	return;
	    }
	    
        if (p.getType() == Penalty.PENTYPE_CARD) 
        {
            //int nc = p.GetNumCards();
        }

        Point pt;
        
        Player pv = p.getVictim();
        if (pv != null) 
        {
            pt = m_ptEmoticon[pv.getSeat() - 1];
    		
            m_drawMatrix.reset();
    		m_drawMatrix.setScale(1, 1);
    		m_drawMatrix.setTranslate(pt.x, pt.y);
    		
            cv.drawBitmap(m_bmpEmoticonVictim, m_drawMatrix, null);
        }

        Player pa = p.getGeneratingPlayer();
        if (pa != null) 
        {
            pt = m_ptEmoticon[pa.getSeat() - 1];
    		
            m_drawMatrix.reset();
    		m_drawMatrix.setScale(1, 1);
    		m_drawMatrix.setTranslate(pt.x, pt.y);
    		
            cv.drawBitmap(m_bmpEmoticonAggressor, m_drawMatrix, null);
        }

	}
	
	
	public void ShowCardHelp (Card c)
	{
		m_helpCardID = c.getID();

		GameActivity a = (GameActivity)(getContext());
		//a.showDialog(GameActivity.DIALOG_CARD_HELP);
		a.showCardHelp();
	}
	
	public void Toast (String msg)
	{
		// not sure exactly how long it takes to fade out a Toast, but we're going to
		// show the toast for a duration that's a little lower than the game delay
		// to accommodate some fade out time.
		if (m_toast == null)
		{
			m_toast = Toast.makeText(this.getContext(), msg, m_game.getDelay() - 500);
			m_toast.setGravity(Gravity.TOP | Gravity.CENTER, 0, m_ptMessages.y);
		}
		else
		{
			m_toast.setText(msg);
		}
		
		m_toast.show();
	}
	
	public void displayScore (Canvas canvas)
	{
		int i;

		for (i = 0; i < 4; i++)
		{
			if ((i == Game.SEAT_SOUTH - 1) || (i == Game.SEAT_NORTH - 1))
			{
				m_paintScoreText.setTextAlign(Paint.Align.CENTER);
			}
			else if (i == Game.SEAT_WEST - 1)
			{
				m_paintScoreText.setTextAlign(Paint.Align.LEFT);
			}
			else if (i == Game.SEAT_EAST - 1)
			{
				m_paintScoreText.setTextAlign(Paint.Align.RIGHT);
			}

			String msg;
			if (m_game.getRoundComplete() == false)
			{
				msg = "" + m_game.getPlayer(i).getTotalScore();
			}
			else
			{
				Player p = m_game.getPlayer(i);

				int lastScore = p.getLastScore();
				int virusPenalty = p.getLastVirusPenalty();
				int totalScore = p.getTotalScore();

				if (lastScore < 0) 
				{
					msg = String.format (m_game.getString(R.string.msg_round_score_negative), totalScore - lastScore - virusPenalty, 0 - lastScore, virusPenalty, totalScore);
				}
				else 
				{
					msg = String.format (m_game.getString(R.string.msg_round_score_positive), totalScore - lastScore - virusPenalty, lastScore, virusPenalty, totalScore);
				}
			}
			canvas.drawText(msg, 
				(float)(m_ptScoreText[i].x), (float)(m_ptScoreText[i].y),
				m_paintScoreText);
		}
	}
	
	public int PromptForVictim ()
	{
		int count = 0;
		if (m_game.getPlayer(Game.SEAT_WEST - 1).getActive())
		{
			count++;
		}
		if (m_game.getPlayer(Game.SEAT_NORTH - 1).getActive())
		{
			count++;
		}
		if (m_game.getPlayer(Game.SEAT_EAST - 1).getActive())
		{
			count++;
		}
		
		CharSequence[] items = new CharSequence[count];
		count = 0;
		if (m_game.getPlayer(Game.SEAT_WEST - 1).getActive())
		{
			items[count] = m_game.getString(R.string.seat_west);
			count++;
		}
		if (m_game.getPlayer(Game.SEAT_NORTH - 1).getActive())
		{
			items[count] = m_game.getString(R.string.seat_north);
			count++;
		}
		if (m_game.getPlayer(Game.SEAT_EAST - 1).getActive())
		{
			items[count] = m_game.getString(R.string.seat_east);
			count++;
		}
		
		new AlertDialog.Builder(this.getContext())
		.setCancelable(false)
		.setTitle(R.string.prompt_victim)
		.setItems(items,
			new DialogInterface.OnClickListener()
			{
				public void onClick (DialogInterface dialoginterface, int i)
				{
					Player p = m_game.getCurrPlayer();
					if (p instanceof HumanPlayer)
					{
						if (m_game.getPlayer(Game.SEAT_WEST - 1).getActive())
						{
							if (i == 0)
							{
								((HumanPlayer)p).setVictim(Game.SEAT_WEST);
								return;
							}
							i--;
						}
						if (m_game.getPlayer(Game.SEAT_NORTH - 1).getActive())
						{
							if (i == 0)
							{
								((HumanPlayer)p).setVictim(Game.SEAT_NORTH);
								return;
							}
							i--;
						}
						if (m_game.getPlayer(Game.SEAT_EAST - 1).getActive())
						{
							if (i == 0)
							{
								((HumanPlayer)p).setVictim(Game.SEAT_EAST);
								return;
							}
							i--;
						}
					}
				}
			})
			.show();

		return 0;
	}
	
	public int PromptForNumCardsToDeal ()
	{
		new AlertDialog.Builder(this.getContext())
			.setCancelable(false)
			.setTitle(R.string.prompt_deal)
			.setItems(R.array.deal_values,
				new DialogInterface.OnClickListener()
				{
					public void onClick (DialogInterface dialoginterface, int i)
					{
						Player p = m_game.getDealer();
						if (p instanceof HumanPlayer)
						{
							((HumanPlayer)p).setNumCardsToDeal(i + 5);
						}
					}
				})
				.show();
		return 0;	
	}
	
	public int PromptForColor ()
	{
		new AlertDialog.Builder(this.getContext())
			.setCancelable(false)
			.setTitle(R.string.prompt_color)
			.setItems(R.array.colors,
				new DialogInterface.OnClickListener()
				{
					public void onClick (DialogInterface dialoginterface, int i)
					{
						Player p = m_game.getCurrPlayer();
						if (p instanceof HumanPlayer)
						{
							((HumanPlayer)p).setColor(i + 1);
						}
					}
				})
				.show();
		return 0;
	}
}
