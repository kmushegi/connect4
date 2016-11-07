// C4Applet.java
 
//So that I can view the source file in applet viewer
//<Applet Code="C4Applet.class" Width=450 Height=390> </Applet>

/** 
 *
 * @author  Sean Bridges
 * @version 1.0
 *
 *
 */

import java.applet.*; 
import java.awt.*;

public class C4Applet extends Applet 
{
	

//------------------------------------------------------	
	//class variables

	final static String ABOUT = "Four in a row, created by Sean Bridges, www.geocities.com/sbridges.geo";
	
	private final static String L1 = "Level 1";
	private final static String L2 = "Level 2";
	private final static String L3 = "Level 3";
	private final static String L4 = "Level 4";
	private final static String L5 = "Level 5";
	private final static String L6 = "Level 6";
	private final static String L7 = "Level 7";
	private final static String L8 = "Level 8";
	
	private final static String STARTING_LEVEL = L4;
	private final static int STARTING_DEPTH = 4;
	
	private final static String RED_PIECE_IMAGE_NAME = "redpiece.gif";
	private final static String BLACK_PIECE_IMAGE_NAME = "blackpiece.gif";
	private final static String BOARD_IMAGE_NAME = "board.jpg";
	
//------------------------------------------------
	//instance variables
	private Button restart;
	private Choice levels;
	private ImagePanel imagePanel;

	private MinimaxPlayer computer;
	private AsynchronousPlayer human; 
	private C4Board board;
	private GameMaster gameMaster;
	
//------------------------------------------------
	//instance methods
	
	public void init () 
	{
		System.out.println("C4Applet initializing");
		System.out.println(ABOUT);
	
		Image blackPieceImage = null;
		Image redPieceImage = null;
		Image boardImage = null;
		
		//load the images
		try
		{
			System.out.println("Loading images...");
			blackPieceImage = this.getImage(getCodeBase(),BLACK_PIECE_IMAGE_NAME);
			redPieceImage = this.getImage(getCodeBase(),RED_PIECE_IMAGE_NAME);
			boardImage = this.getImage(getCodeBase(), BOARD_IMAGE_NAME);			
			
			MediaTracker imageTracker = new MediaTracker(this);
		
			imageTracker.addImage(boardImage, 1);
			imageTracker.addImage(redPieceImage, 2);
			imageTracker.addImage(blackPieceImage, 3);
			imageTracker.waitForAll();
			System.out.println("Images loaded");
		}//end try
		catch (Exception e)
		{
			System.out.println("Error loading images");
			e.printStackTrace();
		}	
		
		
		
		//if the init method is called more than once, 
		//then remove all will remove the components that were 
		//previously contained on the applet.
		//Done mainly so that the appletviewer restart works.
		removeAll();
    
		//create the game
		human = new AsynchronousPlayer("human",C4Board.FIRST_PLAYER_NUMBER);
		computer = new MinimaxPlayer("computer", C4Board.SECOND_PLAYER_NUMBER, human);	
		computer.setDepth(STARTING_DEPTH);
		board = new C4Board(human, computer);
		Player[] players = new Player[2];
		players[0] = human;
		players[1] = computer;
    
		gameMaster = new GameMaster(board, players);
    
		//lay out the components
    
		this.setLayout(new BorderLayout());
    	
    
		//the control panel has the level drop down list and
		//the restart button
    
		Panel controlPanel = new Panel();
		controlPanel.setBackground(Color.white);
		controlPanel.setLayout(new FlowLayout() );

		restart = new Button("restart");
		levels = new Choice();
		levels.add(L1);
		levels.add(L2);
		levels.add(L3);
		levels.add(L4);
		levels.add(L5);
		levels.add(L6);
		levels.add(L7);
		levels.add(L8);
		levels.select(STARTING_LEVEL);

		controlPanel.add(restart);
		controlPanel.add(new Label("              Level"));
		controlPanel.add(levels);
   	 
		this.add(controlPanel, BorderLayout.SOUTH);
    
		//the image panel draws the board.
		imagePanel = new ImagePanel(gameMaster, board, computer, human,boardImage, redPieceImage, blackPieceImage);
		this.add(imagePanel, BorderLayout.CENTER);
 	
	}

 	public void start()
	{
		System.out.println("C4Applet starting");
		gameMaster.startGame();
		this.repaint();
	}
  
	public void stop()
	{
		System.out.println("C4Applet stopping");
		gameMaster.stopGame();
	}

	
	
	public String getAppletInfo()   
	{
		return ABOUT;
	}
	
//----------------------------------------------
	//event handling
	public boolean action (Event evt, Object arg)
	{
		if(arg.equals("restart"))
		{
			gameMaster.restartGame();	
		}
		else if(evt.target.equals(levels))
		{
			if(levels.getSelectedItem() == L1)
			{
				computer.setDepth(1);
			}
			if(levels.getSelectedItem() == L2)
			{
				computer.setDepth(2);
			}
			if(levels.getSelectedItem() == L3)
			{
				computer.setDepth(3);
			}
			if(levels.getSelectedItem() == L4)
			{
				computer.setDepth(4);
			}
			if(levels.getSelectedItem() == L5)
			{
				computer.setDepth(5);
			}
			if(levels.getSelectedItem() == L6)
			{
				computer.setDepth(6);
			}
			if(levels.getSelectedItem() == L7)
			{
				computer.setDepth(7);
			}
			if(levels.getSelectedItem() == L8)
			{
				computer.setDepth(8);
			}
		
		}
		else
		{
			return super.action(evt,arg);
		}
		return true;

	}
	
	

}//end class C4Applet

class ImagePanel extends Panel implements GameEventListener
{
	
//----------------------------------------
	//class variables
	private final static int BOARD_TOP_X = 5;
	private final static int BOARD_TOP_Y = 25;
	
	private final static int BOARD_WIDTH = 420;
	private final static int BOARD_HEIGHT = 320;

	private final static int COLUMN_WIDTH = 50;
	private final static int ROW_HEIGHT = 50;
	private final static int X_OFFSET = 15;
	private final static int Y_OFFSET = 15;
	
	private final static int TEXT_TOP_X = 80;
	private final static int TEXT_TOP_Y = 80;
	private final static Font TEXT_FONT = new Font("SansSerif", Font.BOLD, 36);
	
	private final static int TIP_DIAMETER = 20;
	private final static int TIP_TOP_X = 5;
	private final static int TIP_TOP_Y = 5;
	private final static int BLACK_TIP_OFFSET = 15;
	private final static int RED_TIP_OFFSET = 35;
	
//----------------------------------------
	//instance variables
  
	//drawing is done to a buffer offscreen so that the user doesnt see 
	//the screen flicker
	private Image offscreenImage;
	private Graphics offscreenGraphics;
  
	Image boardImage;
	Image blackPieceImage;
	Image redPieceImage;
	
	private Move lastComputerMove;
  
	private GameMaster game;
	private Player computer;
	private AsynchronousPlayer human;
	private C4Board board;
	
	//the column in which the mouse is currently in, if -1, the mouse is in no column
	int blackColumn = -1;
	
 
//----------------------------------------
	//constructors
	
	ImagePanel(GameMaster aGame, C4Board aBoard, Player computer, AsynchronousPlayer human, 
				Image boardImage, Image redPieceImage, Image blackPieceImage)
	{
		super();
		
		this.setBackground(Color.white);
		
		this.redPieceImage = redPieceImage;
		this.blackPieceImage = blackPieceImage;
		this.boardImage = boardImage;
		
		game = aGame;
		board = aBoard;
		this.computer = computer;
		this.human = human;
		game.addListener(this);
	}
	
	  
//----------------------------------------
	//instance methods
  
  
	public void paint(Graphics g)
	{ 
		g.drawImage(offscreenImage,0,0, this);
	}
  
	public void update()
	{
		Graphics g = this.getGraphics();
		g.drawImage(offscreenImage,0,0, this);
	}


//----------------------------------------
	//mouse management
	
	/**
	 * the mouse has moved
	 * we want to track which column the player is in
	 */
	public boolean mouseMove(Event evt,int x, int y)
	{
		if((BOARD_TOP_X < x) & (x < (BOARD_TOP_X + BOARD_WIDTH)))
		{
			if((TIP_TOP_Y < y) & (y < (BOARD_TOP_Y + BOARD_HEIGHT)))
			{
				x = x - BOARD_TOP_X - 10;
				int column = x /COLUMN_WIDTH;
				if(column >= C4Board.NUMBER_OF_COLUMNS) 
				{
					column = -1;
				}
				
				setBlackColumn(column);
			}
		}
		else
		{
			setBlackColumn(-1);
		}
		
		return true;
	}

	/**
	 * Set the column where the black tip is.
	 * If the column hasn't changed does nothing,
	 * if the column has changed, draws the new tip, 
	 * and or clears the old, and updates the screen.
	 */
	private void setBlackColumn(int newColumn)
	{
		if(newColumn != blackColumn)
		{
			if(newColumn != -1)
			{
				drawBlackTip(newColumn);
			}
			if(blackColumn != -1)
			{
				clearBlackTip(blackColumn);
			}
			blackColumn = newColumn;
			update();
			
		}
		
	}
	
	public boolean mouseDown(Event evt, int x, int y)
	{
		if(blackColumn != -1)
		{
			C4Move move = new C4Move(human, blackColumn);
			human.makeMove(move);
		}
		return true;
	}

  
//----------------------------------------
	//drawing
  
	private void resetOffScreen()
	{
		offscreenImage = createImage(this.getSize().width, this.getSize().height);
		offscreenGraphics = offscreenImage.getGraphics(); 
		drawBoard();
		update();
	}
	
	private void drawBoard()
	{
		offscreenGraphics.drawImage(boardImage, BOARD_TOP_X,BOARD_TOP_Y,this);
	}
	
	/**
	 * Draw a red tip above the given column.
	 * Draws to offscreen, must call update to see it onscreen.
	 */
	private void drawRedTip(int column)
	{
		offscreenGraphics.setColor(Color.red);
		offscreenGraphics.fillOval((column * COLUMN_WIDTH) + (TIP_TOP_X + RED_TIP_OFFSET), TIP_TOP_Y, TIP_DIAMETER, TIP_DIAMETER);		
	}

	/**
	 * Draw a black tip above the given column.
	 * Draws to offscreen, must call update to see it onscreen.
	 */
	private void drawBlackTip(int column)
	{
		offscreenGraphics.setColor(Color.black);
		offscreenGraphics.fillOval((column * COLUMN_WIDTH) + (TIP_TOP_X + BLACK_TIP_OFFSET), TIP_TOP_Y, TIP_DIAMETER, TIP_DIAMETER);		
	}
	
	private void clearBlackTip(int column)
	{
		offscreenGraphics.setColor(Color.white);
		offscreenGraphics.fillOval((column * COLUMN_WIDTH) + (TIP_TOP_X + BLACK_TIP_OFFSET), TIP_TOP_Y, TIP_DIAMETER, TIP_DIAMETER);				
	}
	
	private void clearRedTip(int column)
	{
		offscreenGraphics.setColor(Color.white);
		offscreenGraphics.fillOval((column * COLUMN_WIDTH) + (TIP_TOP_X + RED_TIP_OFFSET), TIP_TOP_Y, TIP_DIAMETER, TIP_DIAMETER);		
	}
	
	private void drawBlackToken(int row, int column)
	{
		int xPos = BOARD_TOP_X + (column * COLUMN_WIDTH) + X_OFFSET;
		int yPos = BOARD_TOP_Y + ((C4Board.NUMBER_OF_ROWS - 1 - row) * ROW_HEIGHT) + Y_OFFSET;
		offscreenGraphics.drawImage(blackPieceImage,xPos,yPos,this);
	}
	
	private void drawRedToken(int row, int column)
	{
		int xPos = BOARD_TOP_X + (column * COLUMN_WIDTH) + X_OFFSET;
		int yPos = BOARD_TOP_Y + ((C4Board.NUMBER_OF_ROWS - 1 - row) * ROW_HEIGHT) + Y_OFFSET;
		offscreenGraphics.drawImage(redPieceImage,xPos,yPos,this);
	}
	
//-------------------------------------------------
	//GameEventListener methods
	
   /** The game has started.
   */
	public void gameStarted() 
	{
		lastComputerMove = null; 
		resetOffScreen();
	}
	/** The game has stopped
	*/
	public void gameStoped() 
	{
		if(board.isGameOver())
		{
			offscreenGraphics.setFont(TEXT_FONT);
			offscreenGraphics.setColor(Color.black);
			if(board.getBoardStats().getScore(human) != 0)
			{
				offscreenGraphics.drawString("You win", TEXT_TOP_X, TEXT_TOP_Y);
			}
			else if(board.getBoardStats().getScore(computer) != 0)
			{
				offscreenGraphics.drawString("You lose", TEXT_TOP_X, TEXT_TOP_Y);
			}
			else
			{
				offscreenGraphics.drawString("Tie game", TEXT_TOP_X, TEXT_TOP_Y);
			}
			update();
		}
		
	}

	/**
	 * The game has been restarted.
	 */
	public void gameRestarted() 
	{
		lastComputerMove = null; 
		resetOffScreen();
	}
	
	/**
	 * A player has moved.
	 */
	public void moveMade(Move aMove) 
	{
		int column = aMove.toInt();
		int row = board.numerOfChipsInColumn(column) - 1;
		
		if(aMove.maker().getNumber() == C4Board.FIRST_PLAYER_NUMBER)
		{
			drawBlackToken(row,column);
			update();
		}
		else
		{
			drawRedToken(row, column);
			
			if(lastComputerMove != null)
			{
				clearRedTip(lastComputerMove.toInt());
			}
			
			drawRedTip(aMove.toInt());
			lastComputerMove = aMove;
			update();
			
		}
		
	}
	
}