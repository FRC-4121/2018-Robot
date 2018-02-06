package org.usfirst.frc.team4121.robot.subsystems;

import org.usfirst.frc.team4121.robot.Robot;
import org.usfirst.frc.team4121.robot.RobotMap;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class ElevatorSubsystem extends Subsystem {

	// Put methods for controlling this subsystem
	// here. Call these from Commands.


	private WPI_TalonSRX m_motor = new WPI_TalonSRX (RobotMap.ELEVATOR_MOTOR1);
	private WPI_TalonSRX m_motor2_follower = new WPI_TalonSRX (RobotMap.ELEVATOR_MOTOR2);

	private double targetPos = 0;
	private double oldTargetPos;
	private double inchesPerRev;
	private int encoderPulsesPerOutputRev; // number of motor encoders pulses per encoder output revolution


	StringBuilder _sb = new StringBuilder();
	int _loops = 0;
	
	


	public void initDefaultCommand() {
		// Set the default command for a subsystem here.
		//setDefaultCommand(new MySpecialCommand());
	}

	public void beforeElevatorBegins() { //was RobotInit before

		// * inches per rev = Drum Dia * PI * motor-drum sprocket ratio
		// */
		inchesPerRev = RobotMap.kWinchDrumDia * 3.1415 * RobotMap.kMotorSprocketTeeth /
				RobotMap.kDrumShaftSprocketTeeth;
		encoderPulsesPerOutputRev = RobotMap.kEncoderPPR * RobotMap.kEncoderRatio;

		/* first choose the sensor */
		m_motor.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, RobotMap.kTimeoutMs);
		m_motor.setSensorPhase(true);

		/* Set relevant frame periods to be at least as fast as periodic rate*/
		m_motor.setStatusFramePeriod(StatusFrameEnhanced.Status_13_Base_PIDF0, 10, RobotMap.kTimeoutMs);
		m_motor.setStatusFramePeriod(StatusFrameEnhanced.Status_10_MotionMagic, 10, RobotMap.kTimeoutMs);

		/* set the peak, nominal outputs */
		m_motor.configNominalOutputForward(0, RobotMap.kTimeoutMs);
		m_motor.configNominalOutputReverse(0, RobotMap.kTimeoutMs);
		m_motor.configPeakOutputForward(1, RobotMap.kTimeoutMs);
		m_motor.configPeakOutputReverse(-1, RobotMap.kTimeoutMs);

		m_motor2_follower.configNominalOutputForward(0, RobotMap.kTimeoutMs);
		m_motor2_follower.configNominalOutputReverse(0, RobotMap.kTimeoutMs);
		m_motor2_follower.configPeakOutputForward(1, RobotMap.kTimeoutMs);
		m_motor2_follower.configPeakOutputReverse(-1, RobotMap.kTimeoutMs);

		/* set closed loop gains in slot0 */
		/* these will need to be tuned once the final masses are known */
		m_motor.config_kF(RobotMap.kPIDLoopIdx, RobotMap.kf, RobotMap.kTimeoutMs);
		m_motor.config_kP(RobotMap.kPIDLoopIdx, RobotMap.kp, RobotMap.kTimeoutMs);
		m_motor.config_kI(RobotMap.kPIDLoopIdx, RobotMap.ki, RobotMap.kTimeoutMs);
		m_motor.config_kD(RobotMap.kPIDLoopIdx, RobotMap.kd, RobotMap.kTimeoutMs);

		/* set acceleration and vcruise velocity - see documentation */
		/* velocity and acceleration has to be in encoder units
		 * rev/s = inches per sec / inches per revolution
		 * velocity is encoder pules per 100ms = rev/s*PPR*GearRatio/10 ;
		 */
		m_motor.configMotionCruiseVelocity((int) RobotMap.kCruiseSpeedUp / (int) inchesPerRev * encoderPulsesPerOutputRev / 10, RobotMap.kTimeoutMs);
		m_motor.configMotionAcceleration((int) RobotMap.kAccelerationUp / (int) inchesPerRev * encoderPulsesPerOutputRev / 10, RobotMap.kTimeoutMs);

		/* zero the sensor */
		m_motor.setSelectedSensorPosition(0, RobotMap.kPIDLoopIdx, RobotMap.kTimeoutMs);

		/* set current limit */
		m_motor.configPeakCurrentLimit(RobotMap.kMaxMotorCurrent, 100);

		m_motor2_follower.set(ControlMode.Follower, RobotMap.kMotorPort);

	}

	public void duringElevator() { //was originally Robot during teleop
		double motorOutput = m_motor.getMotorOutputPercent();
		/* prepare line to print */
		_sb.append("\tout:");
		_sb.append(motorOutput);
		_sb.append("\tspeed:");
		_sb.append(m_motor.getSelectedSensorVelocity(RobotMap.kPIDLoopIdx));
		_sb.append("\tsensor pos:");
		_sb.append(m_motor.getSelectedSensorPosition(RobotMap.kPIDLoopIdx));
		_sb.append("\tcurrent:");
		_sb.append(m_motor.getOutputCurrent());
		_sb.append("\terror:");
		_sb.append(m_motor.getSelectedSensorPosition(RobotMap.kPIDLoopIdx) -
				m_motor.getClosedLoopTarget(RobotMap.kPIDLoopIdx));
	}

	//fix this section...commands?

	public void runToScale() // run to scale height
	{
		/* go to scale height */
		oldTargetPos = targetPos;
		targetPos = RobotMap.dPosScale / inchesPerRev * 4096 / RobotMap.dFudgeFactor;
		if (targetPos > oldTargetPos) {
			m_motor.configMotionCruiseVelocity((int) RobotMap.kCruiseSpeedUp / (int) inchesPerRev * encoderPulsesPerOutputRev / 10, RobotMap.kTimeoutMs);
			m_motor.configMotionAcceleration((int) RobotMap.kAccelerationUp / (int) inchesPerRev * encoderPulsesPerOutputRev / 10, RobotMap.kTimeoutMs);
			m_motor.set(ControlMode.MotionMagic, targetPos);
		} 
		else {
			m_motor.configMotionCruiseVelocity((int) RobotMap.kCruiseSpeedDown / (int) inchesPerRev * encoderPulsesPerOutputRev / 10, RobotMap.kTimeoutMs);
			m_motor.configMotionAcceleration((int) RobotMap.kAccelerationDown / (int) inchesPerRev * encoderPulsesPerOutputRev / 10, RobotMap.kTimeoutMs);
			m_motor.set(ControlMode.MotionMagic, targetPos);
		}
	}



	public void runToSwitch() // run to switch height
	{
		oldTargetPos = targetPos;
		targetPos = RobotMap.dPosSwitch / inchesPerRev * 4096 / RobotMap.dFudgeFactor;
		if (targetPos > oldTargetPos) {
			m_motor.configMotionCruiseVelocity((int) RobotMap.kCruiseSpeedUp / (int) inchesPerRev * encoderPulsesPerOutputRev / 10, RobotMap.kTimeoutMs);
			m_motor.configMotionAcceleration((int) RobotMap.kAccelerationUp / (int) inchesPerRev * encoderPulsesPerOutputRev / 10, RobotMap.kTimeoutMs);
			m_motor.set(ControlMode.MotionMagic, targetPos);
		} 
		else {
			m_motor.configMotionCruiseVelocity((int) RobotMap.kCruiseSpeedDown / (int) inchesPerRev * encoderPulsesPerOutputRev / 10, RobotMap.kTimeoutMs);
			m_motor.configMotionAcceleration((int) RobotMap.kAccelerationDown / (int) inchesPerRev * encoderPulsesPerOutputRev / 10, RobotMap.kTimeoutMs);
			m_motor.set(ControlMode.MotionMagic, targetPos);
		}
	}

	public void goToHome() // go to home
	{
		targetPos = 0;
		m_motor.configMotionCruiseVelocity((int) RobotMap.kCruiseSpeedDown / (int) inchesPerRev * encoderPulsesPerOutputRev / 10, RobotMap.kTimeoutMs);
		m_motor.configMotionAcceleration((int) RobotMap.kAccelerationDown / (int) inchesPerRev * encoderPulsesPerOutputRev / 10, RobotMap.kTimeoutMs);
		m_motor.set(ControlMode.MotionMagic, targetPos);
	}


//	if (++_loops >= 10) {
//		_loops = 0;
//		System.out.println(_sb.toString());
//	}
//	_sb.setLength(0);

}


	   
	 