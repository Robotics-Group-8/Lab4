import lejos.hardware.sensor.EV3UltrasonicSensor;
import java.lang.Math.*;
import java.io.*;
//import lejos.nxt.*;
import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.Motor;
import lejos.hardware.port.SensorPort;
import lejos.robotics.SampleProvider;
import lejos.robotics.navigation.*;

/*Lego Robot that exits mazes using the Tremaux Algorithm*/

public class practice {

  //All the variables and instanciations of the motors, sensors, etc...
  TachoPilot robot; // The Class that will deal with the movement of the robot
  EV3UltrasonicSensor  sensor = new EV3UltrasonicSensor (SensorPort.S1);// The sensor
  TouchSensor exitdetector = new TouchSensor(SensorPort.S4);
  // *END*

  // Extra functions needed
  public static void pause(int time) {
    try{ Thread.sleep(time);
  }
    catch(InterruptedException e){}
  }
  public SampleProvider getDistance(){
      SampleProvider Distance= sensor.getDistanceMode();
      pause(30);
      return sensor.getDistanceMode();
  }
  void fill_map(int maze[][], int maze_X, int maze_Y) {//fill the outer boundaries of the maze to 1
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
  void show_map(int maze[][], int maze_X, int maze_Y) {//show the result
      for (int a=1; a<maze_X; a++) {
        for (int b=1; b<maze_Y;b++) {
             LCD.drawInt(maze[a][b], a, b);
        }
      }
  }
  public boolean exit() {
      if ( exitdetector.isPressed()== true) {
        return true;
      }
      return false;
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
  public int BehindX(int Direction) {
      int result;
      if(Direction == 0){
          result = -1;
      }
      else if (Direction == 2){
          result = 1;
      }
      else result = 0;
      return result;
  }
  public int BehindY(int Direction) {
      int result;
      if(Direction == 3){
          result = 1;
      }
      else if (Direction == 1){
          result = -1;
      }
      else result = 0;
      return result;
  }
  public int InFrontX(int Direction) {//the cell in front of the robot
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
  public int InFrontY(int Direction) {
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
  public int DecreaseVisited(int maze) {
    maze = maze -1;
    if (maze < 1) {
         return 1;
    }
    else {
         return maze;
    }
  }
  public int wallFinder(){ //the ultrasonic sensors gets the distance to the wall.
      int Distance;
      int wallPresence;
      sensor.getDistanceMode();
      pause(30);
      Distance= sensor.getDistanceMode();
      LCD.drawInt(Distance, 5, 5);
      if (Distance > 12)
      {
          wallPresence = 0;
      }
      else
      {
          wallPresence = 1;
      }
      return wallPresence;

  }
 // *END*

 //Functions to move the robot

  public void forward() { //The Robot advances one cell
      robot.travel(19);
  }
  public void backward() { //The Robot advances one cell
      robot.travel(-19);
  }
  public void backturn() { //The Robot makes a 180 degrees turn
      robot.rotate(-180);
  }
  public void rotateRight(){ //The Robot rotates to the right
      robot.rotate(90);
      pause(500);
  }
  public void rotateLeft(){
      robot.rotate(-90);
      pause(500);
    }
  public void testParallelRight1(int refdistance){
      int Distance;
      Distance= sensor.getDistanceMode();
      pause(30);
      Distance = sensor.getDistanceMode();
      if( refdistance > Distance) {
        robot.rotate(-5);
      }
  }
  public void testParallelLeft1(int refdistance){
      int Distance;
      Distance= sensor.getDistanceMode();
      pause(30);
      Distance = sensor.getDistanceMode();
      if( refdistance > Distance) {
        robot.rotate(5);
      }
  }

  // *END*

  public static void main(String[] args ){
    practice traveler = new practice();
    traveler.robot = new TachoPilot(5.6f,13.7f,Motor.A,Motor.C);
    int walls[] = new int[3];
    int cellX;//The current cell we are on.
    int cellY;//The current cell we are on.
    int maze_X = 22;// We can solve a 20 by 20 cell maze
    int maze_Y = 22;// So that the robots doesn't want to go outside the maze, we set the outer cells to 1
    int maze[][] = new int[maze_X][maze_Y]; //Identifies each cell of the maze. We can solve a 20 by 20 cell maze
    int myPath[] = new int[maze_X*maze_Y];
    int Direction = 0;//we suppose that the robot faces north when it gets in;
    int nextMove = 0;
    int moves = 0;
    int exit = 0;
    int RelX;
    int RelY;
    int oldDistanceL = 0;
    int oldDistanceR = 0;
     // North = 0; West =1; South = 2; East =3;
     //notvisited = 0; we haven't visited that map position.
     //visited = 1; we have already visited all the posibles paths of that cell
     //onepathleft = 2; we still have one path to go
     //twopathsleft = 3;we still have 2 two paths to go


    traveler.fill_map(maze,maze_X,maze_Y);
    cellX = 4; //we suppose the robots starts at cell 4 2;
    cellY = 2;


    while (traveler.exit() == false) { //loop while we haven't found the exit
        traveler.rotateLeft();
        pause(500);
        walls[0] = traveler.wallFinder();

       if (moves == 0) {
     oldDistanceL = traveler.getDistance();
       }
      else if (walls[0] == 1){
          pause(100);
          traveler.testParallelLeft1(oldDistanceL);
          pause(200);
      }
        traveler.rotateRight();
        pause(500);
        walls[1] = traveler.wallFinder();
        traveler.rotateRight();

        pause(500);
        walls[2] = traveler.wallFinder();
        if (moves == 0) {
        oldDistanceR = traveler.getDistance();
        }
        else if( walls[2]== 1){
         pause(100);
         traveler.testParallelRight1(oldDistanceR);
         pause(200);
        }
        traveler.rotateLeft();
        //we assume all the paths unvisited
        maze[cellX][cellY] = 3 - walls[2] - walls[1] - walls[0];
        if (maze[cellX][cellY] == 0) {
            maze[cellX][cellY] = 1;
        }
        nextMove=-1;

        if (walls[0] == 0){
             RelX = traveler.InFrontX(traveler.LeftOf(Direction));
             RelY = traveler.InFrontY(traveler.LeftOf(Direction));
            if (maze[cellX+RelX][cellY+RelY] == 0) {
                nextMove = 1;
            }
            else {
                maze[cellX][cellY] = traveler.DecreaseVisited(maze[cellX][cellY]);
                maze[cellX+RelX][cellY+RelY] = traveler.DecreaseVisited(maze[cellX+RelX][cellY+RelY]);
            }
        }
        if (walls[1] == 0){
             RelX = traveler.InFrontX(Direction);
             RelY = traveler.InFrontY(Direction);
            if (maze[cellX+RelX][cellY+RelY] == 0) {
                nextMove = 2;
            }
            else {
                maze[cellX][cellY] = traveler.DecreaseVisited(maze[cellX][cellY]);
                maze[cellX+RelX][cellY+RelY] = traveler.DecreaseVisited(maze[cellX+RelX][cellY+RelY]);
            }
        }
        if (walls[2] == 0){
             RelX = traveler.InFrontX(traveler.RightOf(Direction));
             RelY = traveler.InFrontY(traveler.RightOf(Direction));
            if (maze[cellX+RelX][cellY+RelY] == 0) {
                nextMove = 3;
            }
            else {
                maze[cellX][cellY] = traveler.DecreaseVisited(maze[cellX][cellY]);
                maze[cellX+RelX][cellY+RelY] = traveler.DecreaseVisited(maze[cellX+RelX][cellY+RelY]);
            }
        }

        switch(nextMove) {
            case 1:
                traveler.rotateLeft();
                Direction = traveler.LeftOf(Direction);
                break;
            case 3:
                traveler.rotateRight();
                Direction = traveler.RightOf(Direction);
                break;
      }
       if(nextMove != -1) {
           traveler.forward();
           myPath[moves++] = nextMove;
           cellX = cellX + traveler.InFrontX(Direction);
           cellY = cellY + traveler.InFrontY(Direction);
           continue;
       }
//cul-de-sac     
       while ((maze[cellX][cellY] < 2) && (moves-- > 0)){
        	if (myPath[moves] == 1) {
                    traveler.rotateLeft();
                    Direction = traveler.RightOf(Direction);
                }
		else if (myPath[moves] == 3) {
                    traveler.rotateRight();
                    Direction = traveler.LeftOf(Direction);
		}
       traveler.backward();
       cellX =cellX + traveler.BehindX(Direction);
       cellY =cellY + traveler.BehindY(Direction);
       }
    }
    traveler.show_map(maze, maze_X, maze_Y);
    Button.waitForAnyPress();
  }
}
