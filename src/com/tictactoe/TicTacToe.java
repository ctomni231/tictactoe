package com.tictactoe;

import java.applet.Applet;
import java.awt.AWTEvent;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.Random;
import java.awt.Font;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.swing.JFrame;
import javax.swing.JTextField;

public class TicTacToe extends Applet implements Runnable{

	// -----------------------
	// Engine Variables
	// -----------------------
	
	/** This holds the initial width of the screen */
	private final int SCREEN_WIDTH = 300;//800
	/** This holds the initial height of the screen */
	private final int SCREEN_HEIGHT = 300;//600
	/** This holds the length of the text box */
	private final int TEXT_LENGTH = 25;//50
	
	/** These create the background graphics for the game. The reason there are two is to prevent screen tearing. */
	private Graphics2D g, g2;
	/** This holds the image to be drawn to the frame */
	private BufferedImage image;
	/** This holds the default background color */
	private final Color BACKGROUND_COLOR = Color.WHITE;
	/** This is the object holding the frame for the application window */
	private JFrame frame;
	/** This holds the text input window for the user name */
	private JTextField textFieldUserName;
	
	/** Holds the x-axis position of the mouse */
	private int mouseX;
	/** Holds the y-axis position of the mouse */
	private int mouseY;
	/** Holds when the user clicks a mouse */
	private boolean[] mouseClick = new boolean[4];
	
	/** This is for showing the text bar **/
	private boolean drawTextbar = false;
	
	// ------------------------
	// Game Variables
	// ------------------------
	
	public int gameState = 0;
	
	public int boardX = 75;
	public int boardY = 75;//90
	public int boardBox = 50;
	
	public int[] tickBox = new int[] {0, 0, 0, 0, 0, 0, 0, 0, 0};
	public boolean tickToggle = true;
	public int basicNum = 0;
	
	// Holds to see if someone won
	public int[] combo = new int[] {0,1,2,3,4,5,6,7,8,0,3,6,1,4,7,2,5,8,0,4,8,2,4,6};
	
	public boolean startOfGame = true;
	
	// -----------------------
	// Game Loop
	// -----------------------
	
	/** This is the game loop initialization function, runs once at start */
	public void init() {
		
	}
	
	/** This is the game loop render function, update and render are pretty much the same thing */
	public void render(Graphics g) {
		
		// Draws the mouse position text to the screen
		drawMouseText(g, 4, 10);
		
		// DEBUG state
		if(gameState == -1) {
			
			// This toggles the text bar on and off depending on the middle mouse click
			if(mouseClick[2]) {
				showTextbar(true);
				mouseClick[2] = false;
			}//*/
			
			// Let's draw debug on the top
			drawButtonText(g, "DEBUG", 250, 10);
			
			// Shows the board
			drawBoard(g);
			drawSelection(g);
			
			// For fun, check the win state
			drawCheckWin(g);
			
			// This draws the button text widgets
			drawButtonText(g, "Reset", 0, 290);
			
			// Handle all click functionality here in a hierarchy
			if(mouseClick[1]) {
				
				mouseClick[1] = tapResetButton(mouseClick[1]);
				tapTestRender(mouseClick[1]);
				
				mouseClick[1] = false;
			}
		}
		
		// SINGLE PLAYER GAME state
		else if(gameState == 0) {
			
			// Shows the board
			drawBoard(g);
			drawSelection(g);
			drawCheckWin(g);
			
			// This draws the button text widgets
			drawButtonText(g, "Reset", 0, 290);
			
			// Handle all click functionality here in a hierarchy
			if(mouseClick[1]) {
				
				mouseClick[1] = tapResetButton(mouseClick[1]);
			    mouseClick[1] = tapGameBoard(mouseClick[1]);
				
				mouseClick[1] = false;
			}
		}
		
	}
	
	// ------------------------
	// Game Logic Section
	// ------------------------
	
	/**
	 * A function for handling the clicks of the game board
	 * @param click Holds whether the user clicked a button
	 * @return The new click state after this function is done
	 */
	public boolean tapGameBoard(boolean click) {
		for(int i = 0; i < 9; i++){
			if(mouseX > boardX+boardBox*(i%3) &&
					mouseX < boardX+boardBox*(i%3)+boardBox &&
					mouseY > boardY+boardBox*(int)(i/3) &&
					mouseY < boardY+boardBox*(int)(i/3)+boardBox){
		        if(tickBox[i] == 0){
		        	tickBox[i] = tickToggle ? 1 : 2;
		        	tickToggle = !tickToggle;
		        	startOfGame = false;
		        }
		        return false;
		    }
		}
		return click;
	}
	
	/**
	 * A function that goes through board empty state, all Xs, and finally all Os via left mouse click
	 * @param click Holds whether the user clicked a button
	 * @return The new click state after this function is done
	 */
	public boolean tapTestRender(boolean click) {
		// For now we will do click things here
		if(click) {
			basicNum += 1;
			if(basicNum > 2)
				basicNum = 0;

			for(int i = 0; i < tickBox.length; i++){
				tickBox[i] = basicNum;
			}
		}
		return click;
	}
	
	/**
	 * This function handles all the reset game functionality
	 * @param click Holds whether the user clicked a button
	 * @return The new click state after this function is complete
	 */
	public boolean tapResetButton(boolean click) {
		
		// Sets up the functionality
		if(click && mouseY > 280 && mouseX < 100) {
			
			// Sets the variables to normal
			basicNum = 0;
			tickToggle = true;
			startOfGame = true;
			
			// Reset the board to normal
			for(int i = 0; i < tickBox.length; i++) {
				tickBox[i] = 0;
			}
			
			return false;
		}
		return click;
	}
	
	// ------------------------
	// Game Section
	// ------------------------
	
	public void drawBoard(Graphics g) {
		g.setColor(Color.WHITE);
		for(int i = 0; i < 9; i++) {
			g.fillRect((boardX+boardBox*(i%3))+3, boardY+boardBox*((int)(i/3))+3, boardBox-7, boardBox-7);
		}
		
		g.setColor(Color.BLACK);
		
		g.drawLine(boardX, boardY+boardBox, boardX+boardBox*3, boardY+boardBox);
		g.drawLine(boardX, boardY+boardBox*2, boardX+boardBox*3, boardY+boardBox*2);
		
		g.drawLine(boardX+boardBox, boardY, boardX+boardBox, boardY+boardBox*3);
		g.drawLine(boardX+boardBox*2, boardY, boardX+boardBox*2, boardY+boardBox*3);
	}
	
	/**
	 * This draws the selection that is represented by the tick box
	 * @param g The graphics object to draw this to
	 */
	public void drawSelection(Graphics g){
		for(int i = 0; i < tickBox.length; i++){
			if(tickBox[i] == 1)
				drawX(g, i);
			else if(tickBox[i] == 2)
				drawO(g, i);
		}
	}
	
	public void drawX(Graphics g, int index) {
		g.drawLine((boardX+boardBox*(index%3))+5, (boardY+boardBox*(int)(index/3))+5, 
				(boardX+boardBox*(index%3))+boardBox-5, (boardY+boardBox*(int)(index/3))+boardBox-5);
		g.drawLine((boardX+boardBox*(index%3))+5, (boardY+boardBox*(int)(index/3))+boardBox-5, 
				(boardX+boardBox*(index%3))+boardBox-5, (boardY+boardBox*(int)(index/3))+5);
	}
	
	public void drawO(Graphics g, int index) {
		g.drawOval((boardX+boardBox*(index%3))+3, boardY+boardBox*((int)(index/3))+3, boardBox-7, boardBox-7);
	}
	
	public void drawCheckWin(Graphics g) {
		
		// Check to see if you got a basic win
		for(int i = 0; i < combo.length; i+=3) {
			if(tickBox[combo[i]] != 0 && tickBox[combo[i]] != 3 &&
				tickBox[combo[i]] == tickBox[combo[i+1]] &&
				tickBox[combo[i+1]] == tickBox[combo[i+2]]) {
				if(tickBox[combo[i]] == 1) {
					drawButtonText(g, "[X] Wins", 250, 290);
				}else if(tickBox[combo[i]] == 2) {
					drawButtonText(g, "[O] Wins", 250, 290);
				}
				for(int j = 0; j < tickBox.length; j++){
					if(tickBox[j] == 0)
						tickBox[j] = 3;
				}
				drawWinLine(g);
				return;
			}
		}
		
		// Checks to see if you got a Cat's Game
		for(int i = 0; i < tickBox.length; i++){
			if(tickBox[i] == 0 || tickBox[i] == 3) {
				break;
			}
			if(i == tickBox.length - 1) {
				drawButtonText(g, "Cat's Game", 225, 290);
			}
		}
	}
	
	public void drawWinLine(Graphics g) {
		for(int i = 0; i < combo.length; i+=3) {
			if(tickBox[combo[i]] != 0 && tickBox[combo[i]] != 3 && tickBox[combo[i]] == tickBox[combo[i+1]] && tickBox[combo[i+1]] == tickBox[combo[i+2]]) {
				int difNum = combo[i+2] - combo[i];
				
				// Horizontal Matches
				if(difNum == 2) {
					g.setColor(Color.RED);
					g.drawLine((boardX-5), (boardY+boardBox*((int)(combo[i]/3)))+(boardBox/2), 
							(boardX+boardBox*3)+5, (boardY+boardBox*((int)(combo[i]/3)))+(boardBox/2));
				}
				// Vertical Matches
				else if(difNum == 6) {
					g.setColor(Color.RED);
					g.drawLine((boardX+boardBox*(combo[i]))+(boardBox/2), boardY-5, 
							(boardX+boardBox*(combo[i]))+(boardBox/2), (boardY+boardBox*3)+5);
				}
				// Backslash Matches
				else if(difNum == 8) {
					g.setColor(Color.RED);
					g.drawLine(boardX-5, boardY-5, 
							(boardX+boardBox*3)+5, (boardY+boardBox*3)+5);
				}
				// Forward Slash Matches
				else if(difNum == 4) {
					g.setColor(Color.RED);
					g.drawLine((boardX+boardBox*3)+5, boardY-5, 
							boardX-5, (boardY+boardBox*3)+5);
				}
			}
		}
		
	}
	
	/**
	 * Used for drawing text elements to the screen
	 * @param g The graphics object to draw to
	 * @param text The text to write to the screen
	 * @param posx The x-axis text position
	 * @param posy The y-axis text position
	 */
	public void drawButtonText(Graphics g, String text, int posx, int posy) {
		g.setColor(Color.BLACK);
		g.drawString(text, posx, posy);
	}
	
	// ------------------------
	// Engine Section
	// ------------------------
	
	/** This is for showing the mouse text on the screen */
	public void drawMouseText(Graphics g, int posx, int posy) {
		g.setColor(Color.BLACK);
		g.drawString(String.format("Mouse:(%d,%d)", mouseX, mouseY), posx, posy);
	}
	
	
	/**
	 * This function revalidates the text bar and has an option for toggling it on and off
	 * @param toggle If true, the text bar will toggle turn off if on, and on if off
	 */
	public void showTextbar(boolean toggle) {
		if (toggle)
			drawTextbar = !drawTextbar;
		textFieldUserName.setVisible(drawTextbar);
		frame.revalidate();
		
		// Just needed a place to drop this in case I needed it
		//if(textFieldUserName.getText() != "") {
		//	g.drawString(textFieldUserName.getText(), 150, 150);
		//}
	}
	
	// ------------------------
	// Window Section
	// ------------------------
	
	@Override
    protected void processMouseEvent(MouseEvent e) {
		
		// This tracks the mouse clicks from the user
		mouseClick = new boolean[] {false, false, false, false};
		mouseClick[e.getButton()] = (e.getID() == MouseEvent.MOUSE_PRESSED);
    }
	
	@Override
	protected void processMouseMotionEvent(MouseEvent e) {
		
		// This tracks where the mouse is on the window
		mouseX = e.getX();
        mouseY = e.getY();
        //mouseClick = new boolean[] {false, false, false, false};
	}
	
	/** This runs when the applet starts */
	@Override
    public void start() {
		// Turns the cursor into a Crosshair cursor
    	setCursor(new java.awt.Cursor(Cursor.CROSSHAIR_CURSOR));
    	
    	// Allows this Applet to accept mouse events (we could also use a MouseListener)
        enableEvents(AWTEvent.MOUSE_EVENT_MASK);
        enableEvents(AWTEvent.MOUSE_MOTION_EVENT_MASK);
        
        // Starts up the Thread
        new Thread(this).start();
    }
	
	/** This runs the game screen */
	@Override
	public void run() {
		
		// Creates a new Buffered image sized to the screen
		image = new BufferedImage(SCREEN_WIDTH, SCREEN_HEIGHT, 1);
        g = (Graphics2D)image.getGraphics();
        g2 = null;
        
        // This initializes the objects if necessary
        init();
        
        // This starts the rendering game loop
        while(true) {
        	
        	// This renders the graphics to the screen
        	render(g);
        	
        	// Makes sure the graphics window always has focus when Frame is active
            if(g2 == null){
                g2 = (Graphics2D)getGraphics();
                requestFocus();
            }else
                g2.drawImage(image, 0, 0, SCREEN_WIDTH, SCREEN_HEIGHT, null);
            
            // This makes sure the background is always on the back of the screen
            g.setColor(BACKGROUND_COLOR);
            g.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);         
            
            // This adds a little bit of wait time in-between screens for it to draw
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
            	System.err.println(e);
            }	
        }
	}
	
	public void createWindow() throws InterruptedException {
		frame = new JFrame("CWT TicTacToe");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        textFieldUserName = new JTextField(TEXT_LENGTH);
        frame.add(textFieldUserName, java.awt.BorderLayout.PAGE_END);
        textFieldUserName.setVisible(false);
        this.setPreferredSize(new java.awt.Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        frame.add(this, java.awt.BorderLayout.CENTER);
        frame.setResizable(true);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        Thread.sleep(250);
        this.start();
	}
	
	/** This is the main class that boots up the gaming window */
	// to run in window, uncomment below
    public static void main(String[] args) throws Throwable {
    	TicTacToe applet = new TicTacToe();
    	applet.createWindow();
    }//*/

	
}
