package frc.robot.subsystems.shooter;

import org.littletonrobotics.junction.AutoLog;

public interface ShooterIO {
    @AutoLog
  public static class ShooterIOInputs {
    public boolean connected = false;
    public double shooterVelocity;
  }

  public default void updateInputs(ShooterIOInputs inputs) {}

  public default void setShooterVelocity(double velocity) {}

  public default void shoot() {}
}
