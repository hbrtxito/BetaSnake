package com.clara;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.Random;
import javax.imageio.ImageIO;

import javax.swing.JPanel;

/** This class responsible for displaying the graphics, so the snake, grid, kibble, instruction text and high score
 * 
 * @author Clara
 *
 */
public class DrawSnakeGamePanel extends JPanel {
	
	private static int gameStage = SnakeGame.BEFORE_GAME;  //use this to figure out what to paint
	
	private Snake snake;
	private Kibble kibble;
	private Score score;

	// This is going to storage  the image from the source
        private BufferedImage image;
        private BufferedImage image_wall;
        private BufferedImage image_snake;
        private BufferedImage image_apple;
        
	//Constructor
	DrawSnakeGamePanel(Snake s, Kibble k, Score sc){
            this.snake = s;
            this.kibble = k;
            this.score = sc;

            try {  
                //---------To load the images of the game ------------
                image = ImageIO.read(new File("src/snake_game.png"));

                image_snake = ImageIO.read(new File("src/snake_segment.png"));

                image_apple = ImageIO.read(new File("src/apple.png"));

                image_wall = ImageIO.read(new File("src/wall.jpg"));
                //---------------------------------------------------

             }
            // Catching any exceptions
			catch (Exception ex) {
                  System.out.println(ex.getMessage());
             }
	}
	
	public Dimension getPreferredSize() {
        return new Dimension(SnakeGame.xPixelMaxDimension, SnakeGame.yPixelMaxDimension);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);       

        /* Where are we at in the game? 4 phases.. 
         * 1. Before game starts
         * 2. During game
         * 3. Game lost aka game over
         * 4. or, game won
         */

        gameStage = SnakeGame.getGameStage();
        
        switch (gameStage) {
            case 1: {
                    displayInstructions(g);
                    break;
            } 
            case 2 : {
                    displayGame(g);
                    break;
            }
            case 3: {
                    displayGameOver(g);
                    break;
            }
            case 4: {
                    displayGameWon(g);
                    break;
            }
        }        
    }
	//To display if You win the game
	private void displayGameWon(Graphics g) {
		// TODO Replace this with something really special!
		g.clearRect(100,100,350,350);
		//This is to Set the type of font that We want
		g.setFont(new Font ("Arial Black" ,Font.BOLD,16));
		g.drawString("YOU WON SNAKE!!!", 150, 150);

		
	}
	private void displayGameOver(Graphics g) {

		g.clearRect(100,100,350,350);
		//This is to Set the type of font that We want
		g.setFont(new Font ("Arial Black" ,Font.BOLD,16));
		//Set the color of the font
		g.setColor(Color.blue);
		g.drawString("GAME OVER", 150, 150);
		
		String textScore = score.getStringScore();
		String textHighScore = score.getStringHighScore();
		String newHighScore = score.newHighScore();
		
		g.drawString("SCORE = " + textScore, 150, 250);
		
		g.drawString("HIGH SCORE = " + textHighScore, 150, 300);
		g.drawString(newHighScore, 150, 400);
		//Text in different colors
		g.drawString("press a key to play again", 150, 350);
		g.setColor(Color.red);
		g.drawString("Press q to quit the game",150,400);
		g.setColor(Color.blue);
    			
	}

	private void displayGame(Graphics g) {
		displayGameGrid(g);
		displaySnake(g);
		displayKibble(g);	
                displayRandomWall(g);
	}

	private void displayGameGrid(Graphics g) {
		//Wat to get different colors
		Random rand = new Random();

		int  n = rand.nextInt(255) + 1;
		int  n1 = rand.nextInt(255) + 1;
		int  n2 = rand.nextInt(255) + 1;

		//This is to draw the grid in the game
		int maxX = SnakeGame.xPixelMaxDimension;
		int maxY= SnakeGame.yPixelMaxDimension;
		int squareSize = SnakeGame.squareSize;
		
		g.clearRect(0, 0, maxX, maxY);
		// Getting the random color fo the grid
		g.setColor(new Color(n,n1,n2));

		//Draw grid - horizontal lines
		for (int y=0; y <= maxY ; y+= squareSize){			
			g.drawLine(0, y, maxX, y);
		}
		//Draw grid - vertical lines
		for (int x=0; x <= maxX ; x+= squareSize){			
			g.drawLine(x, 0, x, maxY);
		}
	}

	private void displayKibble(Graphics g) {

		//Draw the kibble in green
		//g.setColor(Color.GREEN);
		//We get the X and y as coordination points for display the kibble , in this case
		int x = kibble.getKibbleX() * SnakeGame.squareSize;
		int y = kibble.getKibbleY() * SnakeGame.squareSize;

		//g.fillRect(x+1, y+1, SnakeGame.squareSize-2, SnakeGame.squareSize-2);
		
                //I draw the image of the apple
                g.drawImage(image_apple,x,y,null);
	}

	private void displaySnake(Graphics g) {

		LinkedList<Point> coordinates = snake.segmentsToDraw();
	
		//Draw head in grey
		g.setColor(Color.LIGHT_GRAY);
		Point head = coordinates.pop();
		//g.fillRect((int)head.getX(), (int)head.getY(), SnakeGame.squareSize, SnakeGame.squareSize);
                
                //I replace the head with the green point image
		g.drawImage(image_snake,(int)head.getX(), (int)head.getY(),null);
                
                
		//Draw rest of snake in black
		g.setColor(Color.BLACK);
		for (Point p : coordinates) {
                    //g.fillRect((int)p.getX(), (int)p.getY(), SnakeGame.squareSize, SnakeGame.squareSize);

                    //I replace the rest of the segments with the green point image
                    g.drawImage(image_snake, (int)p.getX(), (int)p.getY(),null);
		}

	}

	private void displayInstructions(Graphics g) {
            
            //I added the image of the instructions for the game
            g.drawImage(image, 100, 70, null); 

            //g.drawString("Press any key to begin!",100,200);		
            //g.drawString("Press q to quit the game",100,300);		
    	}
	
        //This is my method to display the Random wall
        private void displayRandomWall(Graphics g) {

            //Draw the kibble in green
            g.setColor(Color.RED);

            int[][] board = snake.getSnakeSquares();

            for (int i = 0; i < SnakeGame.xSquares; i++) {
                for (int j = 0; j < SnakeGame.ySquares; j++) {
                    if(board[i][j] > 250){
                        int x = i * SnakeGame.squareSize;
                        int y = j * SnakeGame.squareSize;

                        //g.fillRect(x+1, y+1, SnakeGame.squareSize-2, SnakeGame.squareSize-2);
                        
                        //We draw the wall here
                        g.drawImage(image_wall, x+1, y+1,null);
                    }        
                }
            }
                
	}
}

