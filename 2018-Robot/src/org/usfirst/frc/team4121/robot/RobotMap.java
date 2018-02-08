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
	public static final int LEFT_MOTOR_FRONT = 0;
	public static final int LEFT_MOTOR_MID = 1;
	public static final int LEFT_MOTOR_REAR = 2;
	public static final int RIGHT_MOTOR_FRONT = 3;
	public static final int RIGHT_MOTOR_MID = 4;
	public static final int RIGHT_MOTOR_REAR = 5;

	public static final int CLIMBER1 = 6;

	public static final int ENDMOTOR1 = 7;
	public static final int ENDMOTOR2 = 8;
	public static final double END_EFFECTOR_SPEED = .5; 

	public static final int ELEVATOR_MOTOR1 = 9;
	public static final int ELEVATOR_MOTOR2 = 10;


	public static final int FEEDER = 11;

	public static final int SHOOTER = 12;

	public static final double DRIVE_SPEED = 0.8;

	public static double AUTO_DRIVE_SPEED = 0.5;//changed from .8

	public static double AUTO_TURN_SPEED = 0.5;

	public static int DIRECTION_MULTIPLIER = 1;

	public static double SHOOTER_SPEED = -.6;//can change later depending on speed

	public static double CLIMBER_SPEED = .6;//can change later depending on speed
	
	public static double CLIMBER_REVERSE_SPEED = -.6;

	public static final int COMPRESSOR = 0;

	public static double ANGLE_TOLERANCE = 2.0;

	public static final int IMG_WIDTH = 320;

	public static final int IMG_HEIGHT = 240;

	public static char AUTO_SWITCH_POSITION;
	


	//stuff for elevator

	public static final int kSlotIdx = 0;

	/*
	 * Talon SRX/ Victor SPX will supported multiple (cascaded) PID loops. For
	 * now we just want the primary one.
	 */
	public static final int kPIDLoopIdx = 0;

	/*
	 * set to zero to skip waiting for confirmation, set to nonzero to wait and
	 * report to DS if action fails.
	 */
	public static final int kTimeoutMs = 10;



	/* Elevator target positions */
	public static final double dPosSwitch = 30 ;
	public static final double dPosScale = 80;
	public static final double dPosBumpUp = 12 ;
	public static final double dPosBumpDown = -12 ;

	public static final double dFudgeFactor = 1.135 ;  // actual distance/programmed distance

	/* Elevator drive ratios */
	public static final int kMotorGearRatio = 5 ;
	public static final int kEncoderRatio = 1 ; //ratio of encoder revs to output revs
	public static final int kMotorSprocketTeeth = 12 ;
	public static final int kDrumShaftSprocketTeeth = 12 ;
	public static final double kWinchDrumDia = 0.5 ;

	/* Motor encoder info */
	public static final int kEncoderPPR = 4096 ;  // encoder pulses per revolution

	/* PID constants */
	public static final double kf = 0.5 ;
	public static final double kp = 0.25 ;
	public static final double ki = 0 ;
	public static final double kd = 7 ;

	/* Motion magic motion parameters */
	/* Test values only - for real try to increase 4x by competition */

	public static final double kCruiseSpeed = 50.0 ; // inches per sec
	public static final double kAcceleration = 30.0 ; // inches/s^2

	public static final double kCruiseSpeedUp = 50.0 ; // inches per sec
	public static final double kAccelerationUp = 30.0 ; // inches/s^2

	/* speed and acceleration down need to be set to keep tension on the cable */
	/* g = 386.4 in/s^ */

	public static final double kCruiseSpeedDown = 10.0 ; // inches per sec
	public static final double kAccelerationDown = 10.0 ; // inches/s^2

	/* motor IDS */
	public static final int kMotorPort = 0;
	public static final int kMotor2Port = 1 ;
	public static final int kMaxMotorCurrent = 35 ;

}
