
import java.awt.event.KeyEvent;
import java.util.concurrent.TimeUnit;
import java.awt.Color;
import java.awt.Font;
import java.util.ArrayDeque;
import java.util.Deque;
import java.awt.Point;
import java.util.Random;

public class Snake {

   // Constants
   // -sizes
   // Actual HEIGHT & WIDTH will be greater by 1
   static final int HEIGHT = 40; // must be an even number
   static final int WIDTH = 60; // must be an even number
   static final int THICKNESS = 12; // thickness of the borderr, snake and fruit
   static final int BORDER_SIZE = THICKNESS*2; // size of the double line border
   static final int BOARD_WIDTH = (WIDTH*THICKNESS)+THICKNESS; // width of play area
   static final int BOARD_HEIGHT = (HEIGHT*THICKNESS)+THICKNESS; // height of play area
   static final int CANVAS_WIDTH = (BOARD_WIDTH)+(BORDER_SIZE*2); // total canvas width
   static final int CANVAS_HEIGHT = (BOARD_HEIGHT)+(BORDER_SIZE*2); // total canvas height
   static final int SNAKE_LENGTH = 4; // starting snake length
   static final int DELAY = 75; // starting delay
   // -colors
   static final Color CANVAS_COLOR = StdDraw.BLACK;
   static final Color BORDER_COLOR = StdDraw.WHITE;
   static final Color SNAKE_COLOR = StdDraw.WHITE;
   static final Color FOOD_COLOR = StdDraw.RED;
   static final Color CRASH_COLOR = StdDraw.BLUE;
   static final Color GAME_OVER_COLOR = StdDraw.WHITE;
   static final Color SCORE_COLOR = StdDraw.GREEN;
   // -keys
   static final int KEY_UP = KeyEvent.VK_UP;
   static final int KEY_DOWN = KeyEvent.VK_DOWN;
   static final int KEY_LEFT = KeyEvent.VK_LEFT;
   static final int KEY_RIGHT = KeyEvent.VK_RIGHT;
   // -fonts
   static final Font GAME_OVER = new Font("Courier NEW", Font.PLAIN, 100);
   static final Font SCORE = new Font("Courier NEW", Font.PLAIN, 20);
   // Global Variables
   static int headX = -WIDTH/4;
   static int headY = 0;
   static int snakeDirection = KEY_RIGHT;
   static Deque<Point> snakeCoords = new ArrayDeque<>();  
   static Point newPoint = new Point();
   static Point food = new Point(WIDTH/4, 0);
   static int score = 0;
   static int curSnakeLength = SNAKE_LENGTH;
   static int delay = DELAY;

   static final Random RNG = new Random(System.nanoTime());

   public static void main (final String[] args) {

      boolean stopGame = false;

      setUpBoard();

      while (!StdDraw.isKeyPressed('Q')) {

         getInput();

         processMovement();

         // Check for self crash
         stopGame = stopGame || snakeCrash();
         // Check if snake is in bounds
         stopGame = stopGame || outOfBounds();
         if (stopGame) {
            drawSquare(newPoint, CRASH_COLOR);
            break;
         }

         if (food.equals(newPoint)) {
            spawnFood();
            score++;
            writeScore();
            curSnakeLength += 2;
            delay = Math.max(20, delay - 2);
         }

         snakeCoords.addFirst(newPoint);

         // Check if the snake is too long
         if (snakeCoords.size() > curSnakeLength) {
            Point tempPoint = snakeCoords.removeLast();
            drawSquare(tempPoint, CANVAS_COLOR);
         }

         // Draw next snake position
         drawSquare(newPoint, SNAKE_COLOR);

         // Redraw the score in case the snake ate it
         //writeScore(false);





         StdDraw.pause(delay);

      }
      
      StdDraw.setPenColor(GAME_OVER_COLOR);
      StdDraw.setFont(GAME_OVER);
      StdDraw.text(0, 0, "GAME OVER!");

      
   }

   static void setUpBoard () {
      StdDraw.setPenRadius(0.0);

      // Draw main canvas
      StdDraw.setCanvasSize(CANVAS_WIDTH, CANVAS_HEIGHT);
      StdDraw.setXscale(-(CANVAS_WIDTH)/2, (CANVAS_WIDTH)/2);
      StdDraw.setYscale(-(CANVAS_HEIGHT)/2, (CANVAS_HEIGHT)/2);
      StdDraw.clear(CANVAS_COLOR);
      // Draw border
      StdDraw.setPenColor(BORDER_COLOR);
      StdDraw.filledRectangle(0, 0, (CANVAS_WIDTH-BORDER_SIZE)/2, (CANVAS_HEIGHT-BORDER_SIZE)/2);
      // Draw board
      StdDraw.setPenColor(CANVAS_COLOR);
      StdDraw.filledRectangle(0, 0, (BOARD_WIDTH)/2, (BOARD_HEIGHT)/2);

      // Set initial position of snake
      snakeCoords.add(new Point(headX, headY));
      drawSquare(food, FOOD_COLOR);
      writeScore();
   }

   static void getInput () {
      // Change direction without reversing
      if (StdDraw.isKeyPressed(KEY_UP) && snakeDirection != KEY_DOWN) {
         snakeDirection = KEY_UP;
      } else if (StdDraw.isKeyPressed(KEY_DOWN) && snakeDirection != KEY_UP) {
         snakeDirection = KEY_DOWN;
      } else if (StdDraw.isKeyPressed(KEY_LEFT) && snakeDirection != KEY_RIGHT) {
         snakeDirection = KEY_LEFT;
      } else if (StdDraw.isKeyPressed(KEY_RIGHT) && snakeDirection != KEY_LEFT) {
         snakeDirection = KEY_RIGHT;
      } 
   }

   static void processMovement () {
      switch (snakeDirection) {
         case KEY_UP:
            headY++;
            break;
         case KEY_DOWN:
            headY--;
            break;
         case KEY_LEFT:
            headX--;
            break;
         case KEY_RIGHT:
            headX++;
            break;
      }
      // Add new coordinates to the queue
      newPoint = new Point(headX, headY);
   }

   static boolean outOfBounds () {
      if (headX < -(WIDTH/2) || headX > (WIDTH/2) || headY < -(HEIGHT/2) || headY > (HEIGHT/2)) {
         return true;
      } 
      return false;      
   }

   static boolean snakeCrash () {
      if (snakeCoords.contains(newPoint)) {
         return true;
      }
      return false;
   }

   static void spawnFood () {
      int tempX = RNG.nextInt(WIDTH)-WIDTH/2;
      int tempY = RNG.nextInt(HEIGHT)-HEIGHT/2;
      food = new Point(tempX, tempY);
      drawSquare(food, FOOD_COLOR);
   }

   static void drawSquare (final Point myPoint, final Color myColor) {
      StdDraw.setPenColor(myColor);
      StdDraw.filledSquare(myPoint.getX() * THICKNESS, myPoint.getY() * THICKNESS, THICKNESS/2);
   }

   static void writeScore () {
         writeScore(true);
   }

   static void writeScore (final Boolean clearAndWrite) {
      int tempX = -BOARD_WIDTH/2 + THICKNESS;
      int tempY = BOARD_HEIGHT/2 - THICKNESS;
      StdDraw.setFont(SCORE);
      if (clearAndWrite) {
         StdDraw.setPenColor(CANVAS_COLOR);
         StdDraw.textLeft(tempX, tempY, "SCORE: " + (score - 1));
      }
      StdDraw.setPenColor(SCORE_COLOR);
      StdDraw.textLeft(tempX, tempY, "SCORE: " + score);
   }
}
