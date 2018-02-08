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


	public WPI_TalonSRX m_motor = new WPI_TalonSRX (RobotMap.ELEVATOR_MOTOR1);
	public WPI_TalonSRX m_motor2_follower = new WPI_TalonSRX (RobotMap.ELEVATOR_MOTOR2);

	public double targetPos = 0;
	public double oldTargetPos;
	public double inchesPerRev;
	public int encoderPulsesPerOutputRev; // number of motor encoders pulses per encoder output revolution


	StringBuilder _sb = new StringBuilder();
	int _loops = 0;
	
	


	public void initDefaultCommand() {
		// Set the default command for a subsystem here.
		//setDefaultCommand(new MySpecialCommand());
	}


//	public void during5yuElevator() { //was originally Robot during teleop
//		double motorOutput = m_motor.getMotorOutputPercent();
//		/* prepare line to print */
//		_sb.append("\tout:");
//		_sb.append(motorOutput);
//		_sb.append("\tspeed:");
//		_sb.append(m_motor.getSelectedSensorVelocity(RobotMap.kPIDLoopIdx));
//		_sb.append("\tsensor pos:");
//		_sb.append(m_motor.getSelectedSensorPosition(RobotMap.kPIDLoopIdx));
//		_sb.append("\tcurrent:");
//		_sb.append(m_motor.getOutputCurrent());
//		_sb.append("\terror:");
//		_sb.append(m_motor.getSelectedSensorPosition(RobotMap.kPIDLoopIdx) -
//				m_motor.getClosedLoopTarget(RobotMap.kPIDLoopIdx));
//	}

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


}


	   
	 