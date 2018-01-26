package org.usfirst.frc.team4121.robot;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 * 
 * @author Hen "traitor" Bayden thanks ben for showing up 
 */
public class RobotMap {
	public static final int LEFT_MOTOR_FRONT = 1;
	public static final int LEFT_MOTOR_MID = 2;
	public static final int LEFT_MOTOR_REAR = 3;
	public static final int RIGHT_MOTOR_FRONT = 4;
	public static final int RIGHT_MOTOR_MID = 5;
	public static final int RIGHT_MOTOR_REAR = 6;
	
	public static final int CLIMBER1 = 7;
	public static final int CLIMBER2 = 8;
	
	public static final int ENDMOTOR1 = 9;
	public static final int ENDMOTOR2 = 10;
	public static final double END_EFFECTOR_SPEED = .5;
	
	public static final int FEEDER = 11;
	
	public static final int SHOOTER = 12;
	
	public static final double DRIVE_SPEED = 0.8;
	
	public static double AUTO_DRIVE_SPEED = 0.5;//changed from .8
	
	public static double AUTO_TURN_SPEED = 0.5;
	
	public static int DIRECTION_MULTIPLIER = 1;
	
	public static double SHOOTER_SPEED = -.6;//can change later depending on speed
	
	public static double CLIMBER_SPEED = 1.0;//can change later depending on speed
	
	public static final int COMPRESSOR = 0;
	
	public static double ANGLE_TOLERANCE = 2.0;
	
	public static final int IMG_WIDTH = 320;
	
	public static final int IMG_HEIGHT = 240;
}
