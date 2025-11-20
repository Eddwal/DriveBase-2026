package frc.robot.subsystems.shooter;

import static edu.wpi.first.units.Units.Meters;
import static edu.wpi.first.units.Units.MetersPerSecond;
import static edu.wpi.first.units.Units.RPM;
import static edu.wpi.first.units.Units.Radians;

import org.ironmaple.simulation.SimulatedArena;
import org.ironmaple.simulation.drivesims.SwerveDriveSimulation;
import org.ironmaple.simulation.gamepieces.GamePieceProjectile;
import org.ironmaple.simulation.seasonspecific.crescendo2024.NoteOnFly;
import org.ironmaple.utils.FieldMirroringUtils;
import org.littletonrobotics.junction.Logger;

import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.geometry.Translation3d;
import edu.wpi.first.units.measure.Angle;
import edu.wpi.first.units.measure.Distance;
import edu.wpi.first.units.measure.LinearVelocity;

public class ShooterIOSim implements ShooterIO {
    private SwerveDriveSimulation driveSim;
    private double velocityRPM = 0;
    private GamePieceProjectile note;

    public ShooterIOSim(SwerveDriveSimulation driveSim) {
        this.driveSim = driveSim;
        note = new NoteOnFly(
            // Specify the position of the chassis when the note is launched
            driveSim.getSimulatedDriveTrainPose().getTranslation(),
            // Specify the translation of the shooter from the robot center (in the
            // shooter’s reference frame)
            new Translation2d(0.2, 0),
            // Specify the field-relative speed of the chassis, adding it to the initial
            // velocity of the projectile
            driveSim.getDriveTrainSimulatedChassisSpeedsFieldRelative(),
            // The shooter facing direction is the same as the robot’s facing direction
            driveSim.getSimulatedDriveTrainPose().getRotation(),
            // Initial height of the flying note
            Distance.ofBaseUnits(0.45, Meters),
            // The launch speed is proportional to the RPM; assumed to be 16 meters/second
            // at 6000 RPM
            LinearVelocity.ofBaseUnits(velocityRPM / 6000 * 20, MetersPerSecond),
            // The angle at which the note is launched
            Angle.ofBaseUnits(Math.toRadians(55), Radians))
            .asSpeakerShotNote(() -> System.out.println("HIT SPEAKER!"))
            .enableBecomeNoteOnFieldAfterTouchGround();

        note = note.withProjectileTrajectoryDisplayCallBack(
            // Callback for when the note will eventually hit the target (if configured)
            (pose3ds) -> Logger.recordOutput("Shooter/NoteProjectileSuccessfulShot",
                    pose3ds.toArray(Pose3d[]::new)),
            // Callback for when the note will eventually miss the target, or if no target
            // is configured
            (pose3ds) -> Logger.recordOutput("Shooter/NoteProjectileUnsuccessfulShot",
                    pose3ds.toArray(Pose3d[]::new)));
    }

    @Override
    public void setShooterVelocity(double velocity) {
        this.velocityRPM = velocity;
    }

    @Override
    public void shoot() {
        SimulatedArena.getInstance().addGamePieceProjectile(note);
    }

    @Override
    public void updateInputs(ShooterIOInputs inputs) {
        inputs.connected = true;
        inputs.shooterVelocity = velocityRPM;
    }

}
