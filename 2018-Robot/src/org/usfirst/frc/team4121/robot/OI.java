package org.usfirst.frc.team4121.robot;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;


import org.usfirst.frc.team4121.robot.commands.ClimbCommand;
import org.usfirst.frc.team4121.robot.commands.ShiftDownCommand;
import org.usfirst.frc.team4121.robot.commands.ShiftUpCommand;
import org.usfirst.frc.team4121.robot.commands.StopClimbCommand;
import org.usfirst.frc.team4121.robot.commands.SwitchDriveCommand;

/**
 * This is the main class that initializes and tells what buttons to do what.
 * 
 * @author Saliva Cressman
 */
public class OI {
	
	//Initializations
	public Joystick leftJoy, rightJoy;
	public XboxController xbox;
	public DigitalInput limitSwitch;
	public ADXRS450_Gyro MainGyro;
	public Encoder rightEncoder, leftEncoder;
	public Button shoot, feed, climb, servo, shiftUp, shiftDown, gear, boiler, switchDrive, increaseShootSpeed, decreaseShootSpeed;
	public Button switchDrivexbox;
	
	
	public OI() {
	
		//Encoders
		rightEncoder = new Encoder(0,1, false, Encoder.EncodingType.k4X);
		rightEncoder.setDistancePerPulse(.05277);  //needs to be reset
		rightEncoder.setReverseDirection(true);
		rightEncoder.setSamplesToAverage(7);
		leftEncoder = new Encoder(2,3, false, Encoder.EncodingType.k4X);
		leftEncoder.setDistancePerPulse(.05277);
		leftEncoder.setSamplesToAverage(7);
				
		//Limit Switch
		limitSwitch = new DigitalInput(4);      
		
		//Gyro
		MainGyro = new ADXRS450_Gyro();
				
		//Joysticks
		leftJoy = new Joystick(0);
		rightJoy = new Joystick(1);
		//xbox = new XboxController(0); //can change later
		
		//Buttons
		shoot = new JoystickButton(rightJoy, 1);
		//decreaseShootSpeed = new JoystickButton (rightJoy, 2);
		//servo = new JoystickButton(rightJoy,2);
		//increaseShootSpeed = new JoystickButton (rightJoy, 3);
		
		//switchDrivexbox = new JoystickButton(rightJoy, 4);
		
		//feed = new JoystickButton(rightJoy, 3);
		climb = new JoystickButton(leftJoy, 1);
		gear = new JoystickButton(leftJoy, 2);
		boiler = new JoystickButton(leftJoy, 3);
		shiftDown = new JoystickButton(leftJoy, 4);
		shiftUp = new JoystickButton(leftJoy, 5);
		
		
		//Commands
		//servo.whileHeld(new OpenGateCommand());
		//servo.whenReleased(new CloseGateCommand());
		//feed.whileHeld(new FeedCommand());
		//climb.whileHeld(new ClimbCommand());
		//climb.whenReleased(new StopClimbCommand());
		//shiftUp.whenActive(new ShiftUpCommand());
		//shiftDown.whenActive(new ShiftDownCommand());
		//gear.whenPressed(new FindGearTargetCommand());
		//boiler.whenPressed(new FindBoilerTargetCommand());
//		switchDrive.whenPressed(new SwitchDriveCommand());
		
		//switchDrivexbox.whenPressed(new SwitchDriveCommand());
		
		//decreaseShootSpeed.whenPressed(new DecreaseShootSpeedCommand());
		//increaseShootSpeed.whenPressed(new IncreaseShootSpeedCommand());
		
	}
}
