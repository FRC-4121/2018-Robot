package org.usfirst.frc.team4121.robot.subsystems;

import org.usfirst.frc.team4121.robot.Robot;
import org.usfirst.frc.team4121.robot.RobotMap;

import com.ctre.phoenix.motorcontrol.can.*;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class EndEffector extends Subsystem {

	public WPI_TalonSRX endmotor1 = new WPI_TalonSRX(RobotMap.ENDMOTOR1);
	public WPI_TalonSRX endmotor2 = new WPI_TalonSRX(RobotMap.ENDMOTOR2);
	
	//solenoid setup
	Compressor compressor = new Compressor(RobotMap.COMPRESSOR);
	public DoubleSolenoid armSolenoid = new DoubleSolenoid(2,3);
	
    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
    
    //sets wheels spinning
    public void endeffector(double endspeed) {
    	endmotor1.set(endspeed);
    	endmotor2.set(endspeed);
    	
    }
    //stops wheels with limit switch 
    public void stopWithLimitSwitch(){
    	endmotor1.set(.5);
    	endmotor2.set(.5);
//    	if(!Robot.oi.limitSwitch1.get())
//    	{
//    		endmotor1.set(0); //sets motor speed to 0
//        	endmotor2.set(0);
//    	}
//    	
    }
    public void openArms()
    {
    	armSolenoid.set(DoubleSolenoid.Value.kForward);
    }
    public void closeArms()
    {
    	armSolenoid.set(DoubleSolenoid.Value.kReverse);
    }
    public void resetArms()
    {
    	armSolenoid.set(DoubleSolenoid.Value.kOff);
    }
}

