package org.usfirst.frc.team4121.robot.subsystems;

import org.usfirst.frc.team4121.robot.Robot;
import org.usfirst.frc.team4121.robot.RobotMap;

import com.ctre.phoenix.motorcontrol.can.*;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class EndEffector extends Subsystem {

	public WPI_TalonSRX endmotor1 = new WPI_TalonSRX(RobotMap.ENDMOTOR1);
	public WPI_TalonSRX endmotor2 = new WPI_TalonSRX(RobotMap.ENDMOTOR2);
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
    	if(!Robot.oi.limitSwitch.get())
    	{
    		endmotor1.set(0);
        	endmotor2.set(0);
    	}
    	
    }
    
}

