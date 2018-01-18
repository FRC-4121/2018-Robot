package org.usfirst.frc.team4121.robot.subsystems;

import org.usfirst.frc.team4121.robot.Robot;
import org.usfirst.frc.team4121.robot.RobotMap;
import org.usfirst.frc.team4121.robot.commands.DriveWithJoysticksCommand;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.command.Subsystem;

/**         
 *	DriveTrain subsystem	
 *	                                                                    
 *	@author Ben Hayden
 */
public class DriveTrainSubsystem extends Subsystem {
	
	double leftDirection, rightDirection;
	
	//Initializing all Talons using CAN                                                
	CANTalon leftMotor1 = new CANTalon(RobotMap.LEFT_MOTOR_1);
	CANTalon leftMotor2 = new CANTalon(RobotMap.LEFT_MOTOR_2);
	CANTalon leftMotor3 = new CANTalon(RobotMap.LEFT_MOTOR_3);
	CANTalon rightMotor1 = new CANTalon(RobotMap.RIGHT_MOTOR_1);
	CANTalon rightMotor2 = new CANTalon(RobotMap.RIGHT_MOTOR_2);
	CANTalon rightMotor3 = new CANTalon(RobotMap.RIGHT_MOTOR_3);
	
	//Creating 2 robot drives for all 6 motors
	RobotDrive drive = new RobotDrive(leftMotor1, leftMotor2, rightMotor1, rightMotor2);
	RobotDrive driveSlave = new RobotDrive(leftMotor3, rightMotor3);
	
	
	//Setting the default command to DriveWithJoysticksCommands
	public void initDefaultCommand() {
		setDefaultCommand(new DriveWithJoysticksCommand());
	}
	
	
	//Drive method that creates two tank drives with the left and right joysticks
	public void drive() {

		if(RobotMap.DIRECTION_MULTIPLIER==1)
		{
			drive.tankDrive(Robot.oi.leftJoy.getY()*RobotMap.DIRECTION_MULTIPLIER*.97, Robot.oi.rightJoy.getY()*RobotMap.DIRECTION_MULTIPLIER);
			driveSlave.tankDrive(Robot.oi.leftJoy.getY()*RobotMap.DIRECTION_MULTIPLIER*.97, Robot.oi.rightJoy.getY()*RobotMap.DIRECTION_MULTIPLIER);
		}
		else
		{
			drive.tankDrive(Robot.oi.rightJoy.getY()*RobotMap.DIRECTION_MULTIPLIER*.98, Robot.oi.leftJoy.getY()*RobotMap.DIRECTION_MULTIPLIER);
			driveSlave.tankDrive(Robot.oi.rightJoy.getY()*RobotMap.DIRECTION_MULTIPLIER*.98, Robot.oi.leftJoy.getY()*RobotMap.DIRECTION_MULTIPLIER);
		
		}
		
		drive.setSafetyEnabled(false);
		driveSlave.setSafetyEnabled(false);
		
		drive.setMaxOutput(0.8);
		driveSlave.setMaxOutput(0.8);
	}
	
	
	//Auto drive that inputs two doubles for the speeds of the motors
	public void autoDrive(double leftMotor, double rightMotor) {
		
		drive.setSafetyEnabled(false);
		driveSlave.setSafetyEnabled(false);
		
		drive.setMaxOutput(0.8);
		driveSlave.setMaxOutput(0.8);
		
		drive.tankDrive(leftMotor*0.97, rightMotor);
		driveSlave.tankDrive(leftMotor*0.97, rightMotor);
	}
	
	
	//A method that stops all motors
	public void autoStop() {
		
		drive.tankDrive(0, 0);
		driveSlave.tankDrive(0, 0);
	}
	
}

