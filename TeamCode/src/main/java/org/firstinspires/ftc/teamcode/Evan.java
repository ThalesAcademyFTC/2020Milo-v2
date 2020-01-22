//<><><><><><><><><><><><><><><><><><><><><><><><><><><>
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name="Evan", group="Pushbot")
//@Disabled
public class Evan extends OpMode {

    private Anvil robot;
    double cs[] = {-0.75, 1};
    double ws[] = {1, 0.5};
    int swap = 0;
    boolean aSwap = false;
    double speed = 1;

    int dpady = 0;
    boolean bSwap = false;
    double ss[] = {0, 0.7};


    @Override
    public void init() {
        robot = new Anvil(hardwareMap, Anvil.Drivetrain.EVAN, telemetry);
    }

    @Override
    public void loop() {
        telemetry.addData("touchyBlock", robot.touchyStatus());
        telemetry.addData("armencoder", robot.armMotor.getCurrentPosition());
        telemetry.update();
        if (gamepad1.a && !aSwap) {
            robot.collect(cs[swap]);
            aSwap = true;
        }
        else if (aSwap && !gamepad1.a){
            swap ^= 1;
            aSwap = false;
        }
        else if (gamepad1.b) robot.collect(0);

        if (gamepad1.x) speed = 1; //full speed
        else if (gamepad1.y) speed = 4; //quarter speed

        if (gamepad1.left_trigger > 0.5) robot.clawMove(0.23);
        else if (gamepad1.right_trigger > 0.5) robot.clawMove(0);

        if (gamepad1.left_bumper && !robot.touchyStatus()){
            robot.armDownSpecial(gamepad1);
        } else if (gamepad1.right_bumper){
            robot.armUpSpecial();
        } else {
            robot.armMotor.setPower(0);
        }
        //Backup movements for arm
        if (gamepad1.dpad_left && !robot.touchyStatus()) robot.armMotor.setPower(0.5);
        else if (gamepad1.dpad_right) robot.armMotor.setPower(-0.5);
        else if (gamepad1.dpad_down) robot.downOverride(gamepad1);

        if (gamepad1.dpad_up && !bSwap) {
            robot.moveRServo(ss[dpady]);
            bSwap = true;
        }
        else if (bSwap && !gamepad1.dpad_up){
            dpady ^= 1;
            bSwap = false;
        }
        if (Math.abs(gamepad1.left_stick_x) + Math.abs(gamepad1.left_stick_y) > 0 || Math.abs(gamepad1.left_stick_x) + Math.abs(gamepad1.left_stick_y) > 0) {
            if (Math.abs(gamepad1.left_stick_x) + Math.abs(gamepad1.left_stick_y) > 1.3) {
                robot.moveDiagonal(-gamepad1.left_stick_x, -gamepad1.left_stick_y, speed);
            } else if (Math.abs(gamepad1.left_stick_x) > Math.abs(gamepad1.left_stick_y)) {
                robot.holoMoveRight(gamepad1.left_stick_x / speed);
            } else if (Math.abs(gamepad1.right_stick_x) > Math.abs(gamepad1.right_stick_y)) {
                robot.turnRight(gamepad1.right_stick_x / speed);
            } else robot.moveForward(gamepad1.left_stick_y);
        }
    }
}

//dillon was here
