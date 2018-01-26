package org.usfirst.frc.team4121.robot.subsystems;

import org.usfirst.frc.team4121.robot.RobotMap;

import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class EndEffector extends Subsystem {

	public Talon endmotor1 = new Talon(RobotMap.ENDMOTOR1);
	public Talon endmotor2 = new Talon(RobotMap.ENDMOTOR2);
    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
    
    public void endeffector(double endspeed) {
    	endmotor1.set(endspeed);
    	endmotor2.set(endspeed);
    	
    }
    public void stopEndEffector()
    {
     	endmotor1.set(0);
    	endmotor2.set(0);
    	
    }
}

