package org.usfirst.frc.team4121.robot.subsystems;


import org.usfirst.frc.team4121.robot.RobotMap;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.*;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 * Climber subsystem
 * 
 * @author Salivyia Crestpan
 */
public class ClimberSubsystem extends Subsystem {
	public WPI_TalonSRX climb1 = new WPI_TalonSRX(RobotMap.CLIMBER1);

    public void initDefaultCommand() {
    	
    }
    
    public void climb(double climbspeed) {
    	
    	climb1.setNeutralMode(NeutralMode.Brake);
    	
    	
    	climb1.set(climbspeed);
    	
    	
    }
    
    public void autoClimb()
    {
    	climb1.setNeutralMode(NeutralMode.Brake);
    	
    	climb1.set(RobotMap.CLIMBER_SPEED);
      	
    	//insert new limit switch
    	//if ()
    }
}