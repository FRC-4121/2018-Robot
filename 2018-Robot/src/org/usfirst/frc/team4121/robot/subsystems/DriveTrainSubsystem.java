package org.usfirst.frc.team4121.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.*;
import org.usfirst.frc.team4121.robot.Robot;
import org.usfirst.frc.team4121.robot.RobotMap;
import org.usfirst.frc.team4121.robot.commands.DriveWithJoysticksCommand;

import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.command.Subsystem;

/**         
 *	DriveTrain subsystem	
 *	                                                                    
 *	@author Saliva Crustyman 
 */
public class DriveTrainSubsystem extends Subsystem {
	
	double leftDirection, rightDirection;
	
	//Initializing all WPI_TalonSRXs using CAN                                                
	WPI_TalonSRX leftMotorFront = new WPI_TalonSRX(RobotMap.LEFT_MOTOR_FRONT);
	WPI_TalonSRX leftMotorMid = new WPI_TalonSRX(RobotMap.LEFT_MOTOR_MID);
	WPI_TalonSRX leftMotorRear = new WPI_TalonSRX(RobotMap.LEFT_MOTOR_REAR);
	SpeedControllerGroup leftMotor = new SpeedControllerGroup(leftMotorFront, leftMotorMid, leftMotorRear);
	
	WPI_TalonSRX rightMotorFront = new WPI_TalonSRX(RobotMap.RIGHT_MOTOR_FRONT);
	WPI_TalonSRX rightMotorMid = new WPI_TalonSRX(RobotMap.RIGHT_MOTOR_MID);
	WPI_TalonSRX rightMotorRear = new WPI_TalonSRX(RobotMap.RIGHT_MOTOR_REAR);
	SpeedControllerGroup rightMotor = new SpeedControllerGroup(rightMotorFront, rightMotorMid, rightMotorRear);
	
	//Creating 2 robot drives for all 6 motors
	DifferentialDrive drive = new DifferentialDrive(leftMotor, rightMotor);
	
	
	//Setting the default command to DriveWithJoysticksCommands
	public void initDefaultCommand() {
		setDefaultCommand(new DriveWithJoysticksCommand());
	}
	
	
	//Drive method that creates two tank drives with the left and right joysticks
	public void drive() {

		if(RobotMap.DIRECTION_MULTIPLIER==1)
		{
			drive.tankDrive(Robot.oi.leftJoy.getY()*RobotMap.DIRECTION_MULTIPLIER*.97, Robot.oi.rightJoy.getY()*RobotMap.DIRECTION_MULTIPLIER);
			//drive.tankDrive(Robot.oi.xbox.getY(Hand.kLeft)*RobotMap.DIRECTION_MULTIPLIER, Robot.oi.xbox.getY(Hand.kRight)*RobotMap.DIRECTION_MULTIPLIER);
		
		}
		else
		{
			drive.tankDrive(Robot.oi.rightJoy.getY()*RobotMap.DIRECTION_MULTIPLIER*.98, Robot.oi.leftJoy.getY()*RobotMap.DIRECTION_MULTIPLIER);
			// drive.tankDrive(Robot.oi.xbox.getY(Hand.kRight)*RobotMap.DIRECTION_MULTIPLIER, Robot.oi.xbox.getY(Hand.kLeft)*RobotMap.DIRECTION_MULTIPLIER); 
		}
		
		drive.setSafetyEnabled(false);
		
		drive.setMaxOutput(0.8);
	}
	
	
	//Auto drive that inputs two doubles for the speeds of the motors
	public void autoDrive(double leftMotor, double rightMotor) {
		
		drive.setSafetyEnabled(false);
		
		drive.setMaxOutput(0.8);
		
		drive.tankDrive(leftMotor*0.97, rightMotor);
	}
	
	
	//A method that stops all motors
	public void autoStop() {
		
		drive.tankDrive(0, 0);
	}
	
}

