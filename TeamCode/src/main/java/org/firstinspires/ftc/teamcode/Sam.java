package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

/**
 * Created by dcrenshaw on 4/7/19.
 * Clasp is a basic, bare-bones teleop designed to run on top of Anvil.
 */

@TeleOp(name= "Sam", group="Pushbot")
//@Disabled
public class Sam extends OpMode {

    private Anvil robot;

    @Override
    public void init() {
        robot = new Anvil(hardwareMap, Anvil.Drivetrain.SAM, telemetry);
    }

    @Override
    public void loop() {
        if (gamepad1.a) robot.servo1.setPosition(1);
        if (gamepad1.b) robot.servo1.setPosition(0);
        if (gamepad1.x) robot.servo1.setPosition(0.5);
        if (gamepad1.y) robot.servo1.setPosition(-1);
        if (gamepad1.atRest()) robot.rest();
        else {
            if (Math.abs(gamepad1.left_stick_x) + Math.abs(gamepad1.left_stick_y) > 1.3){
                robot.moveDiagonal(-gamepad1.left_stick_x, -gamepad1.left_stick_y, 1);
            } else if (Math.abs(gamepad1.left_stick_x) > Math.abs(gamepad1.left_stick_y)) {
                robot.holoMoveRight(gamepad1.left_stick_x);
            } else robot.moveBackward(gamepad1.left_stick_y);
            if (Math.abs(gamepad1.right_stick_x) > Math.abs(gamepad1.right_stick_y)){
                robot.turnRight(gamepad1.right_stick_x);
            }
        }
    }

}
