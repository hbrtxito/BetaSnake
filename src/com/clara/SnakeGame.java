package com.clara;

import java.util.Random;
import java.util.Timer;

import javax.swing.*;


public class SnakeGame {

	public final static int xPixelMaxDimension = 501;  //Pixels in window. 501 to have 50-pixel squares plus 1 to draw a border on last square
	public final static int yPixelMaxDimension = 501;

	public static int xSquares ;
	public static int ySquares ;

	public final static int squareSize = 50;

	protected static Snake snake ;

	protected static Kibble kibble;

	protected static Score score;

	static final int BEFORE_GAME = 1;
	static final int DURING_GAME = 2;
	static final int GAME_OVER = 3;
	static final int GAME_WON = 4;   //The values are not important. The important thing is to use the constants 
	//instead of the values so you are clear what you are setting. Easy to forget what number is Game over vs. game won
	//Using constant names instead makes it easier to keep it straight. Refer to these variables 
	//using statements such as SnakeGame.GAME_OVER 

	private static int gameStage = BEFORE_GAME;  //use this to figure out what should be happening. 
	//Other classes like Snake and DrawSnakeGamePanel will need to query this, and change its value

	protected static long clockInterval = 500; //controls game speed
	//Every time the clock ticks, the snake moves
	//This is the time between clock ticks, in milliseconds
	//1000 milliseconds = 1 second.

	static JFrame snakeFrame;
	static DrawSnakeGamePanel snakePanel;
	//Framework for this class adapted from the Java Swing Tutorial, FrameDemo and Custom Painting Demo. You should find them useful too.
	//http://docs.oracle.com/javase/tutorial/displayCode.html?code=http://docs.oracle.com/javase/tutorial/uiswing/examples/components/FrameDemoProject/src/components/FrameDemo.java
	//http://docs.oracle.com/javase/tutorial/uiswing/painting/step2.html

	private static void createAndShowGUI() {
		//Create and set up the window.
		snakeFrame = new JFrame();
		snakeFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		snakeFrame.setSize(xPixelMaxDimension, yPixelMaxDimension);
		snakeFrame.setUndecorated(true); //hide title bar
		snakeFrame.setVisible(true);
		snakeFrame.setResizable(false);

		snakePanel = new DrawSnakeGamePanel(snake, kibble, score);
		snakePanel.setFocusable(true);
		snakePanel.requestFocusInWindow(); //required to give this component the focus so it can generate KeyEvents

		snakeFrame.add(snakePanel);
		snakePanel.addKeyListener(new GameControls(snake));

		setGameStage(BEFORE_GAME);

		snakeFrame.setVisible(true);
	}

	private static void initializeGame() {
		//set up score, snake and first kibble
		xSquares = xPixelMaxDimension / squareSize;
		ySquares = yPixelMaxDimension / squareSize;

		snake = new Snake(xSquares, ySquares, squareSize);
		kibble = new Kibble(snake);
		score = new Score();
				//initialzes the walls
                setWallRandom();
                
		gameStage = BEFORE_GAME;
	}

	protected static void newGame() {
		// here , I set the randoms walls
                setWallRandom();
		Timer timer = new Timer();
		GameClock clockTick = new GameClock(snake, kibble, score, snakePanel);
		timer.scheduleAtFixedRate(clockTick, 0 , clockInterval);
	}

	public static void main(String[] args) {
		//Schedule a job for the event-dispatching thread:
		//creating and showing this application's GUI.
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				initializeGame();
				createAndShowGUI();
			}
		});
	}



	public static int getGameStage() {
		return gameStage;
	}

	public static boolean gameEnded() {
		if (gameStage == GAME_OVER || gameStage == GAME_WON){
			return true;
		}
		return false;
	}

	public static void setGameStage(int gameStage) {
		SnakeGame.gameStage = gameStage;
	}
        
        /*
        This is the function to set the aleatory or random walls
        */
        public static void setWallRandom(){
            
            int[][] board = snake.getSnakeSquares();
			// this is similar to make the kibbles
            board[kibble.getKibbleX()][kibble.getKibbleY()]= 400;

            Random rng = new Random();

            int numberWallRandom = 0;
            

			// The is a loop to build the walls without collapse
			// This block of code have a correction from a forum on te internet ... stackoverflow.com
            while (numberWallRandom != 2) {

                int randomX = rng.nextInt(xSquares-3);
                int randomY = rng.nextInt(ySquares-3);

                boolean collision = false;

                if(board[randomX][randomY] != 0)
                    collision = true;
                if(board[randomX][randomY+1] != 0 && board[randomX][randomY+2] != 0 && board[randomX][randomY+3] != 0)
                    collision = true;
                if(board[randomX+1][randomY] != 0 && board[randomX+2][randomY] != 0 && board[randomX+3][randomY] != 0)
                    collision = true;
                if(randomY==5)
                    collision = true;

                if(!collision){
                    int directionWall = rng.nextInt(2);

                    if(directionWall == 0){
                        for (int j = randomX; j < randomX+4 ; j++) {
                            board[j][randomY] = 300;
                        }
                    }else{
                        for (int j = randomY; j < randomY+4 ; j++) {
                            if(j!=5){
                                board[randomX][j] = 300;
                            }
                        }
                    }
                    numberWallRandom++;
                }
            }

            board[kibble.getKibbleX()][kibble.getKibbleY()]= 0;

            snake.setSnakeSquares(board);// I set the table game again
        
        }
}
