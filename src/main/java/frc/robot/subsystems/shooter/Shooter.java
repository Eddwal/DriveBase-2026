package frc.robot.subsystems.shooter;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Shooter extends SubsystemBase {
    ShooterIO shooter;
    ShooterIOInputsAutoLogged shooterInputs = new ShooterIOInputsAutoLogged();
    public Shooter (ShooterIO shooterIO){
        this.shooter = shooterIO;
        shooterIO.setShooterVelocity(5000);
    }
    @Override
    public void periodic() {
        shooter.updateInputs(shooterInputs);
    }

    public InstantCommand shoot = new InstantCommand(shooter::shoot);
    
}
