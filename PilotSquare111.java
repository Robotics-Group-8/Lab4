import lejos.hardware.motor.BaseRegulatedMotor;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.robotics.chassis.Chassis;
import lejos.robotics.chassis.Wheel;
import lejos.robotics.chassis.WheeledChassis;
import lejos.robotics.geometry.Line;
import lejos.robotics.geometry.Rectangle;
import lejos.robotics.localization.OdometryPoseProvider;
import lejos.robotics.localization.PoseProvider;
import lejos.robotics.mapping.LineMap;
import lejos.robotics.navigation.DestinationUnreachableException;
import lejos.robotics.navigation.MovePilot;
import lejos.robotics.navigation.Navigator;
import lejos.robotics.navigation.Pose;
import lejos.robotics.navigation.Waypoint;
import lejos.robotics.pathfinding.Path;
import lejos.robotics.pathfinding.PathFinder;
import lejos.robotics.pathfinding.ShortestPathFinder;

public class PilotSquare111 {
	
	final static float WHEEL_DIAMETER = 51; // Diameter of the wheels (mm)
	final static float AXLE_LENGTH = 44; // Distance of the two driven wheels
	final static float AGULAR_SPEED = 100; // How fast it turns corners (degrees/sec)
	final static float LINEAR_SPEED = 70; // How fast in a straight line (mm/sec)

	public static void main(String[] args) throws Exception {



			BaseRegulatedMotor mLeft = new EV3LargeRegulatedMotor(MotorPort.A);
			BaseRegulatedMotor mRight = new EV3LargeRegulatedMotor(MotorPort.B);

			// create a wheel with Diameter 51mm and offset 22mm left of centre
			Wheel wLeft = WheeledChassis.modelWheel(mLeft, WHEEL_DIAMETER).offset(-AXLE_LENGTH / 2);

			// create wheel with Diameter 51mm and offset 22mm right of centre
			Wheel wRight = WheeledChassis.modelWheel(mRight, WHEEL_DIAMETER).offset(AXLE_LENGTH / 2);

			// create a chassis with two wheels on it
			Chassis chassis = new WheeledChassis((new Wheel[] { wRight, wLeft }), WheeledChassis.TYPE_DIFFERENTIAL);

			// finally create a pilot which can drive its wheel separately
			MovePilot plt = new MovePilot(chassis);

			PoseProvider poseProvider = new OdometryPoseProvider(plt);
			Navigator navigator = new Navigator(plt, poseProvider);
		
		Line [] lines = new Line [4] ;
		lines [0] = new Line(-20f,20f,100f,20f) ;
		lines [1] = new Line(-20f,40f,20f,40f) ;
		lines [2] = new Line(-20f,60f,20f,60f) ;
		lines [3] = new Line(-20f,80f,20f,80f) ;
		Rectangle bounds = new Rectangle (-50,-50,250,250);
		LineMap myMap = new LineMap (lines,bounds) ;
		PathFinder pf = new ShortestPathFinder (myMap ) ;
		Path route = pf. findRoute (new Pose(0 , 0 , 0), new Waypoint ( 0 , 100)) ;
		navigator.followPath(route);
		navigator.waitForStop();

	}
}