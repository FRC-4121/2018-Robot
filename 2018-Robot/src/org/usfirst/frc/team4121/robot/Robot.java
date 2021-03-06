
package org.usfirst.frc.team4121.robot;

import org.usfirst.frc.team4121.robot.commands.AutoStopCommand;
import org.usfirst.frc.team4121.robot.commands.AutoStraightCommandGroup;
import org.opencv.core.Rect;
import org.opencv.imgproc.Imgproc;
import org.usfirst.frc.team4121.robot.commands.AutoStraightCommandGroup;
import org.usfirst.frc.team4121.robot.commands.AutoLeftSideCommandGroup;
import org.usfirst.frc.team4121.robot.commands.AutoRightSideCommandGroup;
import org.usfirst.frc.team4121.robot.commands.AutoTurnLeftCommandGroup;
import org.usfirst.frc.team4121.robot.commands.AutoTurnRightCommandGroup;
import org.usfirst.frc.team4121.robot.commands.FindGearTargetCommand;
import org.usfirst.frc.team4121.robot.extraClasses.VisionRead;
import org.usfirst.frc.team4121.robot.subsystems.ClimberSubsystem;
import org.usfirst.frc.team4121.robot.subsystems.DriveTrainSubsystem;
import org.usfirst.frc.team4121.robot.subsystems.ElevatorSubsystem;
import org.usfirst.frc.team4121.robot.subsystems.EndEffector;
import org.usfirst.frc.team4121.robot.subsystems.ShifterSubsystem;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.vision.VisionThread;
import edu.wpi.first.wpilibj.vision.VisionRunner;
/**
 * The VM is configured to automatically run this class, and to call the
 * We made a thing
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 * 
 * @author Saliva Crustyman
 */
public class Robot extends IterativeRobot {
		
	//Subsystems
	public static DriveTrainSubsystem driveTrain;
	public static ShifterSubsystem shifter;
	public static ClimberSubsystem climber;
	public static EndEffector end;
	public static ElevatorSubsystem elevator;
	
	//Commands
	public static FindGearTargetCommand findGear;
	public static OI oi;

	//SmartDashboard chooser
	private SendableChooser<Command> chooser;
	
	Command autonomousCommand;

	//encoder math values
	public static double distanceTraveled;
	public static double angleTraveled;
	public static double leftDistance;
	public static double rightDistance;
	
	//Vision variables
	public static VisionRead visionReader;
	public static double[] visionArray;
	public static Object imgLock;
	public static UsbCamera gearCam;
	public static CameraServer camServer;
	private VisionThread visionProcThread;
	public static double leftTargetCenter;
	public static double rightTargetCenter;
	public static double centerOfTargets;
	public static double targetError;
	
	//2018 Game Data
	public static String gameData;
	
	//elevator code Mr.Dermiggio
//    private static final int kMotorPort = 0;
//    private static final int kXboxPort = 0;
//
//    private TalonSRX m_motor;
//    private XboxController m_xboxController;
//    private double targetPos = 0;
//    private double oldTargetPos;
//    private double inchesPerRev;
//    private int encoderPulsesPerOutputRev; // number of motor encoders pulses per encoder output revolution


    StringBuilder _sb = new StringBuilder();
    int _loops = 0;

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() {
		
		//Initialize subsystems
		driveTrain = new DriveTrainSubsystem();
		shifter = new ShifterSubsystem();
		climber = new ClimberSubsystem();
		oi = new OI();
	
		
		//Initialize commands
		findGear = new FindGearTargetCommand();
		
		
		//Initialize dashboard choosers
		chooser = new SendableChooser<>();
		chooser.addDefault("Center", new AutoStraightCommandGroup());
		chooser.addObject("Left", new AutoLeftSideCommandGroup());
		chooser.addObject("Right", new AutoRightSideCommandGroup());
		SmartDashboard.putData("Auto mode", chooser);

	
		//Initialize vision processing, cameras and start autocapture for dashboard
		imgLock = new Object();
		camServer = CameraServer.getInstance();		
		Robot.gearCam = new UsbCamera("cam0", 0);		
		Robot.camServer.addCamera(Robot.gearCam);		
		Robot.gearCam.setResolution(320, 240);
		Robot.gearCam.setBrightness(10);		
		Robot.camServer.startAutomaticCapture(Robot.gearCam);
				
		visionReader = new VisionRead();
		visionProcThread = new VisionThread(gearCam,visionReader, new VisionRunner.Listener<VisionRead>() {

			@Override
			public void copyPipelineOutputs(VisionRead pipeline) {
				
				synchronized (imgLock) {
					//visionArray = vision.update(pipeline);
					
					if (!pipeline.filterContoursOutput().isEmpty()) {
						Rect leftRect = Imgproc.boundingRect(pipeline.filterContoursOutput().get(0));
						Rect rightRect = Imgproc.boundingRect(pipeline.filterContoursOutput().get(1));
						
						leftTargetCenter = leftRect.x+(leftRect.width/2);
						rightTargetCenter = rightRect.x+(rightRect.width/2);
						centerOfTargets = (leftTargetCenter + rightTargetCenter)/2;
						targetError = centerOfTargets - (RobotMap.IMG_WIDTH/2);
					}
					else
					{
						leftTargetCenter = -9999.0;
						rightTargetCenter = -9999.0;
						centerOfTargets = -9999.0;
						targetError = 0.0;
					}
						
				}
			}
		});
		visionProcThread.start();
		
		
		//Calibrate the main gyro
		Robot.oi.MainGyro.calibrate();
		Robot.oi.MainGyro.reset();
		
		
		//setting Encoders up
		distanceTraveled = 0.0;
		
		//gyro
		angleTraveled =0.0;
		
		//elevator code
		// * inches per rev = Drum Dia * PI * motor-drum sprocket ratio
		// */
		elevator.inchesPerRev = RobotMap.kWinchDrumDia * 3.1415 * RobotMap.kMotorSprocketTeeth /
				RobotMap.kDrumShaftSprocketTeeth;
		elevator.encoderPulsesPerOutputRev = RobotMap.kEncoderPPR * RobotMap.kEncoderRatio;

		/* first choose the sensor */
		elevator.m_motor.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, RobotMap.kTimeoutMs);
		elevator.m_motor.setSensorPhase(true);

		/* Set relevant frame periods to be at least as fast as periodic rate*/
		elevator.m_motor.setStatusFramePeriod(StatusFrameEnhanced.Status_13_Base_PIDF0, 10, RobotMap.kTimeoutMs);
		elevator.m_motor.setStatusFramePeriod(StatusFrameEnhanced.Status_10_MotionMagic, 10, RobotMap.kTimeoutMs);

		/* set the peak, nominal outputs */
		elevator.m_motor.configNominalOutputForward(0, RobotMap.kTimeoutMs);
		elevator.m_motor.configNominalOutputReverse(0, RobotMap.kTimeoutMs);
		elevator.m_motor.configPeakOutputForward(1, RobotMap.kTimeoutMs);
		elevator.m_motor.configPeakOutputReverse(-1, RobotMap.kTimeoutMs);

		elevator.m_motor2_follower.configNominalOutputForward(0, RobotMap.kTimeoutMs);
		elevator.m_motor2_follower.configNominalOutputReverse(0, RobotMap.kTimeoutMs);
		elevator.m_motor2_follower.configPeakOutputForward(1, RobotMap.kTimeoutMs);
		elevator.m_motor2_follower.configPeakOutputReverse(-1, RobotMap.kTimeoutMs);

		/* set closed loop gains in slot0 */
		/* these will need to be tuned once the final masses are known */
		elevator.m_motor.config_kF(RobotMap.kPIDLoopIdx, RobotMap.kf, RobotMap.kTimeoutMs);
		elevator.m_motor.config_kP(RobotMap.kPIDLoopIdx, RobotMap.kp, RobotMap.kTimeoutMs);
		elevator.m_motor.config_kI(RobotMap.kPIDLoopIdx, RobotMap.ki, RobotMap.kTimeoutMs);
		elevator.m_motor.config_kD(RobotMap.kPIDLoopIdx, RobotMap.kd, RobotMap.kTimeoutMs);

		/* set acceleration and vcruise velocity - see documentation */
		/* velocity and acceleration has to be in encoder units
		 * rev/s = inches per sec / inches per revolution
		 * velocity is encoder pules per 100ms = rev/s*PPR*GearRatio/10 ;
		 */
		elevator.m_motor.configMotionCruiseVelocity((int) RobotMap.kCruiseSpeedUp / (int) elevator.inchesPerRev * elevator.encoderPulsesPerOutputRev / 10, RobotMap.kTimeoutMs);
		elevator.m_motor.configMotionAcceleration((int) RobotMap.kAccelerationUp / (int) elevator.inchesPerRev * elevator.encoderPulsesPerOutputRev / 10, RobotMap.kTimeoutMs);

		/* zero the sensor */
		elevator.m_motor.setSelectedSensorPosition(0, RobotMap.kPIDLoopIdx, RobotMap.kTimeoutMs);

		/* set current limit */
		elevator.m_motor.configPeakCurrentLimit(RobotMap.kMaxMotorCurrent, 100);

		elevator.m_motor2_follower.set(ControlMode.Follower, RobotMap.kMotorPort);
		
	}
	
	void Disabled() {
		while(isDisabled()) {
			
		}
	}

	
	/**
	 * This function is called once each time the robot enters Disabled mode.
	 * You can use it to reset any subsystem information you want to clear when
	 * the robot is disabled.
	 */
	@Override
	public void disabledInit() {

	}

	
	@Override
	public void disabledPeriodic() {
		
		//Start scheduler
		Scheduler.getInstance().run();
		
	}

	
	/**
	 * This autonomous (along with the chooser code above) shows how to select
	 * between different autonomous modes using the dashboard. The sendable
	 * chooser code works with the Java SmartDashboard. If you prefer the
	 * LabVIEW Dashboard, remove all of the chooser code and uncomment the
	 * getString code to get the auto name from the text box below the Gyro
	 *
	 * You can add additional auto modes by adding additional commands to the
	 * chooser code above (like the commented example) or additional comparisons
	 * to the switch structure below with additional strings & commands.
	 */
	@Override
	public void autonomousInit() {
		
		/*
		 * String autoSelected = SmartDashboard.getString("Auto Selector",
		 * "Default"); switch(autoSelected) { case "My Auto": autonomousCommand
		 * = new MyAutoCommand(); break; case "Default Auto": default:
		 * autonomousCommand = new ExampleCommand(); break; }
		 */
		
		String gameData;
		gameData = DriverStation.getInstance().getGameSpecificMessage();
		RobotMap.AUTO_SWITCH_POSITION = gameData.charAt(0);

		//Get selected autonomous command
		autonomousCommand = chooser.getSelected();
		
		//Reset encoders
		Robot.oi.rightEncoder.reset();
		Robot.oi.leftEncoder.reset();
		
		gameData = DriverStation.getInstance().getGameSpecificMessage(); //get this data from the field people
		RobotMap.AUTO_SWITCH_POSITION = gameData.charAt(0); 
		

		// schedule the autonomous command (example)
		if (autonomousCommand != null) {
			autonomousCommand.start();
		}
		
	}
	
	
	/**
	 * This function is called periodically during autonomous
	 */
	@Override
	public void autonomousPeriodic() {
		
		//Start autonomous scheduler
		Scheduler.getInstance().run();
		
		//Put key values on smart dashboard
		SmartDashboard.putString("Drive Angle:", Double.toString(Robot.oi.MainGyro.getAngle()));
		SmartDashboard.putString("Left Drive Distance: ", Double.toString(Robot.oi.leftEncoder.getDistance()));
		SmartDashboard.putString("Right Drive Distance: ", Double.toString(Robot.oi.rightEncoder.getDistance()));
		SmartDashboard.putString("Gear Position: ", shifter.gearPosition());

	}

	
	@Override
	public void teleopInit() {
		
		// This makes sure that the autonomous stops running when
		// teleop starts running. If you want the autonomous to
		// continue until interrupted by another command, remove
		// this line or comment it out.
		if (autonomousCommand != null){
			autonomousCommand.cancel();
		}
		Scheduler.getInstance().removeAll();
		
		//Robot.gearCam.setBrightness(100);
		
		//Reset encoders
		Robot.oi.rightEncoder.reset();
		Robot.oi.leftEncoder.reset();
		
	}

	
	/**
	 * This function is called periodically during operator control
	 */
	@Override
	public void teleopPeriodic() {
		
		

		//Start scheduler
		Scheduler.getInstance().run();
 
		//Put key values on the smart dashboard
		SmartDashboard.putString("Gear Position: ", shifter.gearPosition());
		SmartDashboard.putString("Drive Direction:", Integer.toString(RobotMap.DIRECTION_MULTIPLIER));		
		SmartDashboard.putString("Right Drive Distance (in inches): ", Double.toString(Robot.oi.rightEncoder.getDistance()));
		SmartDashboard.putString("Left Drive Distance (in inches): ", Double.toString(Robot.oi.leftEncoder.getDistance()));
		
		synchronized (imgLock) {
			SmartDashboard.putString("Target Error:", Double.toString(targetError));
			SmartDashboard.putString("Left Rect Center: ", Double.toString(leftTargetCenter));
			SmartDashboard.putString("Right Rect Center: ", Double.toString(rightTargetCenter));

		}
		
		//SmartDashboard.putString("Limit Switch: ", Boolean.toString(Robot.oi.limitSwitch.get()));


	}
	

	/**
	 * This function is called periodically during test mode
	 */
	@Override
	public void testPeriodic() {
		
		
		
	}
}
