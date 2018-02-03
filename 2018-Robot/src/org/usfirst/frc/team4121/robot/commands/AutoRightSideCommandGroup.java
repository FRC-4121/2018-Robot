package org.usfirst.frc.team4121.robot.commands;

import org.usfirst.frc.team4121.robot.RobotMap;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class AutoRightSideCommandGroup extends CommandGroup {

    public AutoRightSideCommandGroup() {
        // Add Commands here:
    	
    	if (RobotMap.AUTO_SWITCH_POSITION== 'R')
    	{
    		addSequential(new AutoDrive(1,2,3,4));
    		//addSequential drop box
    	}
    	else
    	{
    		addSequential(new AutoDrive(1,2,3,4));
    	}
        // e.g. addSequential(new Command1());
        //      addSequential(new Command2());
        // these will run in order.
    		
        // To run multiple commands at the same time,
        // use addParallel()
        // e.g. addParallel(new Command1());
        //      addSequential(new Command2());
        // Command1 and Command2 will run in parallel.

        // A command group will require all of the subsystems that each member
        // would require.
        // e.g. if Command1 requires chassis, and Command2 requires arm,
        // a CommandGroup containing them would require both the chassis and the
        // arm.
    }
}
