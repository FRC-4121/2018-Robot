package org.usfirst.frc.team4121.robot.subsystems;

import org.usfirst.frc.team4121.robot.RobotMap;

import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 * Climber subsystem
 * 
 * @author Salivyia Crestpan
 */
public class ClimberSubsystem extends Subsystem {
	public Talon climb1 = new Talon(RobotMap.CLIMBER1);
	public Talon climb2 = new Talon(RobotMap.CLIMBER2);

    public void initDefaultCommand() {
    	
    }
    
    public void climb(double climbspeed) {
    	climb1.set(climbspeed);
    	climb2.set(climbspeed);
    	
    }
}