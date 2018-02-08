package org.usfirst.frc.team4121.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class SpitOutCube extends CommandGroup {

    public SpitOutCube() {
    	 addParallel(new OpenArmsCommand());
         addParallel(new EjectCommand());
    }
}
