import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;

//import lejos.nxt.*;

/**
 *
 * @author WandereR
 */
public class Test {
  // Extra functions needed
  public static void pause(int time) {
    try{ Thread.sleep(time);
  }
    catch(InterruptedException e){}
  }
  void fill_map(int maze[][], int maze_X, int maze_Y) {
      int a=0;
      int b=0;
      for ( a=0; a<maze_X; a++) {
        for ( b=0; b<maze_Y;b++) {
             maze[a][b] = 1;
        }
      }
      for (int c=1; c<maze_X-1; c++) {
        for (int d=1; d<maze_Y-1;d++) {
             maze[c][d] = 0;
        }
      }
  }
  void fill_maze(int maze[][], int maze_X, int maze_Y) {
      int a=0;
      int b=0;
      for ( a=0; a<maze_X; a++) {
        for ( b=0; b<maze_Y;b++) {
             maze[a][b] = 1;
        }
      }
      for (int c=1; c<maze_X-1; c++) {
        for (int d=1; d<maze_Y-1;d++) {
             maze[c][d] = 0;
        }
      }
      maze[1][1] = 5;
      maze[2][1] = 5;
      maze[3][1] = 5;
      maze[4][1] = 5;
      maze[5][1] = 5;
      maze[3][2] = 5;
      maze[3][3] = 5;
      maze[5][3] = 5;
      maze[5][4] = 5;
      maze[5][5] = 5;
      maze[4][5] = 5;
      maze[3][5] = 5;
      maze[2][5] = 5;
      maze[1][5] = 5;
      maze[1][3] = 5;
      maze[1][4] = 5;
      maze[6][1] = 5;
      maze[7][1] = 5;
      maze[7][2] = 5;
      maze[7][3] = 5;
      maze[7][4] = 5;
      maze[7][5] = 5;
      maze[4][6] = 5;
      maze[4][7] = 5;
      maze[5][7] = 5;
      maze[6][7] = 5;
      maze[7][7] = 5;
      maze[8][7] = 5;
      maze[9][7] = 5;
      maze[9][6] = 5;
      maze[9][5] = 5;
      maze[8][5] = 5;


  }
  void show_map(int maze[][], int maze_X, int maze_Y) {
      for (int a=1; a<maze_X; a++) {
        for (int b=1; b<maze_Y;b++) {
             LCD.drawInt(maze[a][b], a, b);
        }
      }
  }
  public int decreaseVisited(int maze){
      if(maze == 1){
        return maze;
      }
      else {
         maze = maze -1;
         return maze;
      }
  }
  // *END*
  //All the functions needed to get informations and get around the maze

  public int RightOf(int Direction){ //Gives the Right direction of the actual direction
      int newDir;
      if (Direction != 3){
          newDir = Direction +1;
      }
      else {
          newDir = 0;
      }
      return newDir;
  }
  public int LeftOf(int Direction){ //Gives the Left direction of the actual direction
     int newDir;
        if (Direction != 0){
            newDir = Direction -1;
        }
        else {
            newDir = 3;
        }
     return newDir;
  }

  public int RelPosX(int Direction) {
      int result;
      if (Direction == 0){
          result = 1;
      }
      else if (Direction == 2) {
          result = -1;
      }
      else result = 0;
      return result;
  }
  public int RelPosY(int Direction) {
      int result;
      if (Direction == 3){
          result = -1;
      }
      else if (Direction == 1) {
          result = 1;
      }
      else result = 0;
      return result;
  }
 // *END*

 //Functions to move the robot
  public int wallFinder(int filledMaze[][],int direction,int cellX,int cellY){
      if (direction == 3) {
          if (filledMaze[cellX][cellY-1] == 5){
              return 1;
          }
      }
      else if (direction == 1) {
          if (filledMaze[cellX][cellY+1] == 5) {
              return 1;
          }
      }
      else if (direction == 0) {
          if(filledMaze[cellX+1][cellY] == 5) {
              return 1;
          }
      }
      else if (direction == 2) {
          if(filledMaze[cellX-1][cellY] == 5) {
              return 1;
          }
      }
      return 0;
  }



  // *END*


  public static void main(String[] args ){
    TravelTest traveler = new TravelTest();
    int walls[] = new int[3];
    int cellX;//The current cell we are on.
    int cellY;//The current cell we are on.
    int maze_X = 22;// We can solve a 20 by 20 cell maze
    int maze_Y = 22;// So that the robots doesn't want to go outside the maze, we set the outer cells to 1
    int filledMaze[][] = new int[maze_X][maze_Y];
    int maze[][] = new int[maze_X][maze_Y]; //Identifies each cell of the maze. We can solve a 20 by 20 cell maze
    int myPath[] = new int[maze_X*maze_Y];
    int Direction = 0;//we suppose that the robot faces north when it gets in;
    int nextMove;
    int moves = 0;
    int exit = 0;
    int RelX;
    int RelY;
     // North = 0; West =1; South = 2; East =3;
     //notvisited = 0; we haven't visited that map position.
     //visited = 1; we have already visited all the posibles paths of that cell
     //onepathleft = 2; we still have one path to go
     //twopathsleft = 3;we still have 2 two paths to go
    traveler.fill_maze(filledMaze, maze_X, maze_Y);
    traveler.show_map(filledMaze, maze_X, maze_Y);
    Button.waitForAnyPress();
    traveler.fill_map(maze,maze_X,maze_Y);
    cellX = 1; //we suppose the robots starts at cell 1 2;
    cellY = 2;
LCD.drawInt(1, cellX, cellY);


    while (exit != 1) { //loop while we haven't found the exit
        Button.waitForAnyPress();
        LCD.drawInt(Direction, 15, 1);
        if((cellX == 7)&&(cellY==6) ){
           exit = 1;
        }
        Direction = traveler.LeftOf(Direction);
        walls[0] = traveler.wallFinder(filledMaze,Direction,cellX,cellY);
        Direction = traveler.RightOf(Direction);
        walls[1] = traveler.wallFinder(filledMaze,Direction,cellX,cellY);
        Direction = traveler.RightOf(Direction);
        walls[2] = traveler.wallFinder(filledMaze,Direction,cellX,cellY);
        Direction = traveler.LeftOf(Direction);
        //we assume all the paths unvisited
        maze[cellX][cellY] = 3 - walls[2] - walls[1] - walls[0];
        if (maze[cellX][cellY] == 0){
            maze[cellX][cellY] = 1;
        }
        nextMove=-1;

        //left
        if (walls[0] != 1){
             RelX = traveler.RelPosX(traveler.LeftOf(Direction)); 
             RelY = traveler.RelPosY(traveler.LeftOf(Direction));
            if (maze[cellX+RelX][cellY+RelY] == 0) {
                nextMove = 1;
            }
            else {
                maze[cellX][cellY] = traveler.decreaseVisited(maze[cellX][cellY]);
                maze[cellX+RelX][cellY+RelY] = traveler.decreaseVisited(maze[cellX+RelX][cellY+RelY]);
            }
        }
        if (walls[1] != 1){
             RelX = traveler.RelPosX(Direction);
             RelY = traveler.RelPosY(Direction);
            if (maze[cellX+RelX][cellY+RelY] == 0) {
                nextMove = 2;

            }
            else {
                maze[cellX][cellY] = traveler.decreaseVisited(maze[cellX][cellY]);
                maze[cellX+RelX][cellY+RelY] = traveler.decreaseVisited(maze[cellX+RelX][cellY+RelY]);
            }
        }
        if (walls[2] != 1){
             RelX = traveler.RelPosX(traveler.RightOf(Direction));
             RelY = traveler.RelPosY(traveler.RightOf(Direction));
            if (maze[cellX+RelX][cellY+RelY] == 0) {
                nextMove = 3;
            }
            else {
                maze[cellX][cellY] = traveler.decreaseVisited(maze[cellX][cellY]);
                maze[cellX+RelX][cellY+RelY] = traveler.decreaseVisited(maze[cellX+RelX][cellY+RelY]);
            }
        }
        switch(nextMove) {
            case 1:
               
                Direction = traveler.LeftOf(Direction);
                break;
            case 3:
                
                Direction = traveler.RightOf(Direction);
                break;
      }
       if(nextMove != -1) {
             
           myPath[moves] = nextMove;
           moves++;
           LCD.drawInt(maze[cellX][cellY], cellX, cellY);
           cellX =cellX + traveler.RelPosX(Direction);
           cellY =cellY + traveler.RelPosY(Direction);          
           LCD.drawInt(cellX, 12, 5);
           LCD.drawInt(cellY, 12, 6);
           continue;
       }
        LCD.drawInt(4, cellX, cellY);
        LCD.drawInt(7, 15, 6);
        Direction = traveler.RightOf(traveler.RightOf(Direction));
        LCD.drawInt(Direction, 15, 5);
        Button.waitForAnyPress();
	while ((maze[cellX][cellY] < 2) && (moves > 0)){
        	if (myPath[moves] == 1) {
                	Direction = traveler.RightOf(Direction);

		}
		else if (myPath[moves] == 3) {
                	Direction = traveler.LeftOf(Direction);

		}

       cellX = cellX + traveler.RelPosX(Direction);
       cellY = cellY + traveler.RelPosY(Direction);
       moves--;
       LCD.drawInt(Direction, 15, 5);
       LCD.drawInt(cellX, 12, 5);
       LCD.drawInt(cellY, 12, 6);
       Button.waitForAnyPress();
       }

    }
     LCD.drawString("YOU WON", 10, 10);
     Button.waitForAnyPress();
    }
}
