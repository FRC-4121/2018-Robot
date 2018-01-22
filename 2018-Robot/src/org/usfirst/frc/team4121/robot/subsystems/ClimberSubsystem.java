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
	public Talon climb = new Talon(RobotMap.CLIMBER);

    public void initDefaultCommand() {
    	
    }
    
    public void climb(double climbspeed) {
    	climb.set(climbspeed);
    	
    }
}