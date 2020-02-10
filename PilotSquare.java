import lejos.hardware.motor.BaseRegulatedMotor;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.robotics.chassis.Chassis;
import lejos.robotics.chassis.Wheel;
import lejos.robotics.chassis.WheeledChassis;
import lejos.robotics.localization.OdometryPoseProvider;
import lejos.robotics.localization.PoseProvider;
import lejos.robotics.navigation.MovePilot;
import lejos.robotics.navigation.Navigator;
import lejos.robotics.navigation.Waypoint;
import lejos.robotics.pathfinding.Path;

public class PilotSquare {

	
	final static float WHEEL_DIAMETER = 51; // Diameter of the wheels (mm)
	final static float AXLE_LENGTH = 44;    // Distance of the two driven wheels
	final static float AGULAR_SPEED = 100;  // How fast it turns corners (degrees/sec)
	final static float LINEAR_SPEED = 70;   // How fast in a straight line (mm/sec)
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

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

		Path path = new Path();
		path.add(new Waypoint(100, 0));
		path.add(new Waypoint(100, 1000));
		path.add(new Waypoint(0, 1000));
		path.add(new Waypoint(0, 0));

		navigator.followPath(path);
		
	}

}
