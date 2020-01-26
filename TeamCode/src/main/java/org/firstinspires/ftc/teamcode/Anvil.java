package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.rev.RevColorSensorV3;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcontroller.external.samples.SensorColor;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import java.lang.reflect.Array;
import java.util.List;

import static org.firstinspires.ftc.robotcore.external.navigation.AngleUnit.DEGREES;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesOrder.XYZ;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesReference.EXTRINSIC;
import static org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit.mmPerInch;

/**
 * Created by dcrenshaw on 3/3/18.
 * Modern hardware augmentation for FIRST Tech Challenge.
 */

public class Anvil {
    //Define servo and motor variables
    public DcMotor motor1, motor2, motor3, motor4, cmotor1, cmotor2;
    DigitalChannel touchyBlock;
    ColorSensor sensorColor;
    public CRServo crservo1;
    public Servo servo1, rservo1, rservo2, skyServo;
    public DcMotor clawMotor, armMotor;
    public OpenGLMatrix lastLocation = null;
    int[] positions = {700, 4000, 5200};
    int target = positions[0];

    //Reference to mapped servo/motor controller
    private HardwareMap hwMap;

    public Telemetry telemetry;

    public DcMotor[] forward, right, left, special, unique, collect;

    private double ticksPerInch = 15.26;

    public boolean hs = true;

    public enum Drivetrain {
        HOLONOMIC,
        TANK,
        WEST_COAST,
        MECHANUM,
        OMNIDRIVE,
        EVAN,
        SAM
    }

    public Anvil(HardwareMap ahwMap, Drivetrain type, Telemetry telem) {
        hwMap = ahwMap;

        telemetry = telem;

        //Each of these cases are for different drive trains, the setup for each drive train is within.
        switch (type) {
            /*Example drive train:
            case TRAIN_NAME: //Make sure TRAIN_NAME is in the types enum!
                //Map all motors to proper variables.
                motor1 = hwMap.dcMotor.get("motor1");
                motor2 = hwMap.dcMotor.get("motor2");
                //Set motor directions. These should all be set so that power 1 for all
                //motors == robot moves forwards.
                motor1.setDirection(DcMotor.Direction.REVERSE);
                motor2.setDirection(DcMotor.Direction.FORWARD);
                //Set motor purposes for maneuvers. Motors in 'right' are the motors which must
                //move in reverse for the robot to turn right, and the same applies for left.
                //'forward' should contain all motors.
                forward = new DcMotor[]{motor1, motor2};
                right = new DcMotor[]{motor1};
                left = new DcMotor[]{motor2};
                //There is also a "special" motor array which is used for any additional
                //nonsense you want to do with the robot. Right now, it's just used for
                //holonomic and mechanum special movements.
                break;
             */

            case OMNIDRIVE:
                //Weird drive train, only two wheels move for the robot to go forward. Will need to
                // consider this when programming the robot to move.
                motor1 = hwMap.dcMotor.get("motor1");
                motor2 = hwMap.dcMotor.get("motor2");
                motor3 = hwMap.dcMotor.get("motor3");
                motor4 = hwMap.dcMotor.get("motor4");
                clawMotor = hwMap.dcMotor.get("clawMotor");
                motor1.setDirection(DcMotor.Direction.FORWARD);
                motor2.setDirection(DcMotor.Direction.REVERSE);
                motor3.setDirection(DcMotor.Direction.FORWARD);
                motor3.setDirection(DcMotor.Direction.REVERSE);
                forward = new DcMotor[]{motor1, motor2, motor3, motor4};
                right = new DcMotor[]{motor1, motor3};
                left = new DcMotor[]{motor2, motor4};
                break;


            case HOLONOMIC:
                //Assign motors
                clawMotor = hwMap.dcMotor.get("clawMotor");
                motor1 = hwMap.dcMotor.get("motor1");
                motor2 = hwMap.dcMotor.get("motor2");
                motor3 = hwMap.dcMotor.get("motor3");
                motor4 = hwMap.dcMotor.get("motor4");
                //Set motor directions
                motor1.setDirection(DcMotor.Direction.FORWARD);
                motor2.setDirection(DcMotor.Direction.REVERSE);
                motor3.setDirection(DcMotor.Direction.FORWARD);
                motor4.setDirection(DcMotor.Direction.REVERSE);
                //Set motor purposes
                forward = new DcMotor[]{motor1, motor2, motor3, motor4};
                right = new DcMotor[]{motor2, motor4};
                left = new DcMotor[]{motor1, motor3};
                special = new DcMotor[]{motor1, motor4};
                unique = new DcMotor[]{motor2, motor3};
                hs = false;
                break;
            case TANK:
                motor1 = hwMap.dcMotor.get("motor1");
                motor2 = hwMap.dcMotor.get("motor2");
                motor1.setDirection(DcMotor.Direction.FORWARD);
                motor2.setDirection(DcMotor.Direction.FORWARD);
                forward = new DcMotor[]{motor1, motor2};
                right = new DcMotor[]{motor1};
                left = new DcMotor[]{motor2};
                break;
            case WEST_COAST:
                motor1 = hwMap.dcMotor.get("motor1");
                motor2 = hwMap.dcMotor.get("motor2");
                motor3 = hwMap.dcMotor.get("motor3");
                motor4 = hwMap.dcMotor.get("motor4");
                motor1.setDirection(DcMotor.Direction.REVERSE);
                motor2.setDirection(DcMotor.Direction.FORWARD);
                motor3.setDirection(DcMotor.Direction.REVERSE);
                motor4.setDirection(DcMotor.Direction.REVERSE);
                forward = new DcMotor[]{motor1, motor2, motor3, motor4};
                right = new DcMotor[]{motor1, motor4};
                left = new DcMotor[]{motor2, motor3};
                break;
            case EVAN:
                //Map all motors to proper variables.
                motor1 = hwMap.dcMotor.get("motor1");
                motor2 = hwMap.dcMotor.get("motor2");
                motor3 = hwMap.dcMotor.get("motor3");
                motor4 = hwMap.dcMotor.get("motor4");
                cmotor1 = hwMap.dcMotor.get("cmotor1");
                cmotor2 = hwMap.dcMotor.get("cmotor2");
                armMotor = hwMap.dcMotor.get("armMotor");
                rservo1 = hwMap.servo.get("rservo1");
                rservo2 = hwMap.servo.get("rservo2");
                skyServo = hwMap.servo.get("skyServo");
                touchyBlock = hwMap.get(DigitalChannel.class, "touchyBlock");
                touchyBlock.setMode(DigitalChannel.Mode.INPUT);
                sensorColor = hwMap.get(com.qualcomm.robotcore.hardware.ColorSensor.class, "sensorColorDistance");
                servo1 = hwMap.servo.get("servo1");
                motor1.setDirection(DcMotor.Direction.FORWARD);
                motor2.setDirection(DcMotor.Direction.REVERSE);
                motor3.setDirection(DcMotor.Direction.FORWARD);
                motor4.setDirection(DcMotor.Direction.REVERSE);
                cmotor1.setDirection(DcMotor.Direction.FORWARD);
                cmotor2.setDirection(DcMotor.Direction.FORWARD);
                armMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                armMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
                forward = new DcMotor[]{motor1, motor2, motor3, motor4};
                left = new DcMotor[]{motor1, motor3};
                right = new DcMotor[]{motor2, motor4};
                special = new DcMotor[]{motor2, motor3};
                unique = new DcMotor[]{motor1, motor4};
                collect = new DcMotor[]{cmotor1, cmotor2};
                hs = false;
                break;
            case MECHANUM:
                //Map all motors to proper variables.
                motor1 = hwMap.dcMotor.get("motor1");
                motor2 = hwMap.dcMotor.get("motor2");
                motor3 = hwMap.dcMotor.get("motor3");
                motor4 = hwMap.dcMotor.get("motor4");
                motor1.setDirection(DcMotor.Direction.REVERSE);
                motor2.setDirection(DcMotor.Direction.FORWARD);
                motor3.setDirection(DcMotor.Direction.REVERSE);
                motor4.setDirection(DcMotor.Direction.FORWARD);
                forward = new DcMotor[]{motor1, motor2, motor3, motor4};
                left = new DcMotor[]{motor3, motor2};
                right = new DcMotor[]{motor1, motor4};
                special = new DcMotor[]{motor2, motor4};
                unique = new DcMotor[]{motor1, motor3};
                hs = false;
                break;
            case SAM:
                motor1 = hwMap.dcMotor.get("motor1");
                motor2 = hwMap.dcMotor.get("motor2");
                motor3 = hwMap.dcMotor.get("motor3");
                motor4 = hwMap.dcMotor.get("motor4");
                servo1 = hwMap.servo.get("servo1");
                motor1.setDirection(DcMotor.Direction.FORWARD);
                motor2.setDirection(DcMotor.Direction.REVERSE);
                motor3.setDirection(DcMotor.Direction.FORWARD);
                motor4.setDirection(DcMotor.Direction.FORWARD);
                forward = new DcMotor[]{motor1, motor2, motor3, motor4};
                left = new DcMotor[]{motor3, motor1};
                right = new DcMotor[]{motor4, motor2};
                special = new DcMotor[]{motor1, motor4};
                unique = new DcMotor[]{motor2, motor3};
                break;
            default:
                telemetry.addLine("Invalid type " + type + " passed to Anvil's init function. Nothing has been set up.");
                break;
        }
        if (forward != null) {
            for (DcMotor x : forward) {
                x.setPower(0);
                x.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            }
        }
    }


    public void initCustom(HardwareMap ahwMap, Telemetry telem, DcMotor[] rightI, DcMotor[] leftI, DcMotor[] forwardI, DcMotor.Direction[] orderedDirections) {
        //Allows initialization of custom drive trains not in list programatically.
        telemetry = telem;
        hwMap = ahwMap;
        right = rightI;
        left = leftI;
        forward = forwardI;
        for (int x = 0; x < forward.length; x++) {
            forward[x].setDirection(orderedDirections[x]);
        }
    }

    //Movement, turning, and resting methods for the all current drive trains
    public void moveForward(double pace) {
        for (DcMotor x : forward) x.setPower(pace);
    }

    public void turnRight(double pace) {
        if (hs) {
            for (DcMotor x : right) x.setPower(-pace / 2);
            for (DcMotor x : left) x.setPower(pace / 2);
        } else {
            for (DcMotor x : right) x.setPower(-pace);
            for (DcMotor x : left) x.setPower(pace);
        }
    }

    public void turnLeft(double pace) {
        if (hs) {
            for (DcMotor x : left) x.setPower(-pace / 2);
            for (DcMotor x : right) x.setPower(pace / 2);
        } else {
            for (DcMotor x : left) x.setPower(-pace);
            for (DcMotor x : right) x.setPower(pace);
        }
    }

    public void moveBackward(double pace) {
        for (DcMotor x : forward)
            x.setPower(-pace);
    }

    public void rest() {
        for (DcMotor x : forward) {
            x.setPower(0);
        }
    }

    //Experimental function to turn while moving forward. Increases Maneuverability of robot.
    //ctx = controller x
    public void diff(double ctx, double pace) {
        for (DcMotor x : left) x.setPower(pace - (ctx * 2));
        for (DcMotor x : right) x.setPower(pace + (ctx * 2));
    }

    public void eDia(double pacex, double pacey) {
        double pace = (Math.abs(pacex) + Math.abs(pacey)) / 2;
        if (pacex < 0 && pacey < 0) { //Forward left
            for (DcMotor x : special) x.setPower(-pace);
            for (DcMotor x : unique) x.setPower(0);
        }
        if (pacex > 0 && pacey < 0) { //Forward right
            for (DcMotor x : special) x.setPower(0);
            for (DcMotor x : unique) x.setPower(-pace);
        }
        if (pacex < 0 && pacey > 0) { //Back left
            for (DcMotor x : special) x.setPower(0);
            for (DcMotor x : unique) x.setPower(pace);
        }
        if (pacex > 0 && pacey > 0) { //Back right
            for (DcMotor x : special) x.setPower(pace);
            for (DcMotor x : unique) x.setPower(0);
        }
    }

    public void moveDiagonal(double pacex, double pacey, double speed) {
        double pace = (Math.abs(pacex) + Math.abs(pacey)) / 2;
        for (DcMotor x : special) x.setPower((Math.round(pacex + pacey) * pace)/speed);
        for (DcMotor x : unique) x.setPower((Math.round(pacey - pacex) * pace)/speed);
    }

    //Holonomic specific movements
    public void holoMoveRight(double pace) {
        for (DcMotor x : unique) x.setPower(pace);
        for (DcMotor x : special) x.setPower(-pace);
    }

    public void holoMoveLeft(double pace) {
        for (DcMotor x : unique) x.setPower(-pace);
        for (DcMotor x : special) x.setPower(pace);
    }
    public void collectForTicks(int ticks, Telemetry telemetry) {
        for (DcMotor x : collect) {
            x.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            x.setTargetPosition(ticks);
            x.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        }
        this.collect(0.2);
        while (collect[0].getCurrentPosition() > -ticks - 5 && collect[0].getCurrentPosition() < -ticks + 5) {
            telemetry.addData("cencoder1", collect[0].getCurrentPosition());
            telemetry.update();
        }
        for (DcMotor x : collect) {
            x.setPower(0);
            x.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        }
    }
    public boolean touchyStatus(){ return !touchyBlock.getState(); }

    public void armMotorEMove(int t){
        armMotor.setTargetPosition(t);
        armMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        armMotor.setPower(1);
        while (armMotor.getCurrentPosition() < t - 25 || armMotor.getCurrentPosition() > t + 25){
            continue;
        }
        armMotor.setPower(0);
        armMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    public void armUpSpecial(){
        if (touchyStatus()){
            armMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            target = positions[0];
            armMotorEMove(target);
        }
        else if (target == positions[0]){
            target = positions[1];
            armMotorEMove(target);
        }
        else if (target == positions[1]){
            target = positions[2];
            armMotorEMove(target);
        }
    }

    public void armDownSpecial(Gamepad x){
        if (target == positions[0]){
            clawMove(0);
            while (!touchyStatus()) {
                armMotor.setPower(-0.5);
                if (x.right_bumper) break;
            }
            clawMove(0.23);
        }
        else if (target == positions[1]){
            target = positions[0];
            armMotorEMove(target);
        } else if (target == positions[2]){
            target = positions[1];
            armMotorEMove(target);
        }
    }
    public void downOverride(Gamepad x){
        clawMove(0);
        while (!touchyStatus()) {
            armMotor.setPower(-1);
            if (x.right_bumper) break;
        }
        clawMove(0.23);
    }
    public void moveRServo(double x){
        rservo1.setPosition(x);
        rservo2.setPosition(1-x);
    }
    public void skyMove(double pos){
        skyServo.setPosition(pos);
    }
    public void moveForTicks(int ticks) {
        //Blocks until the robot has gotten to the desired location.
        this.rest();
        for (DcMotor x : forward) {
            x.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            x.setTargetPosition(-ticks);
            x.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        }
        this.moveForward(0.25);
        while (forward[0].isBusy()) {
            continue;
        }
        for (DcMotor x : forward) {
            x.setPower(0);
            x.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        }
    }
    public void MSForTicks(int ticks) {
        //Blocks until the robot has gotten to the desired location.
        this.rest();
        for (DcMotor x : unique) {
            x.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            x.setTargetPosition(ticks);
            x.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        }
        for (DcMotor x : special){
            x.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            x.setTargetPosition(-ticks);
            x.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        }
        this.moveForward(0.25);
        while (unique[0].isBusy() && special[0].isBusy()){
            continue;
        }
        for (DcMotor x : forward) {
            x.setPower(0);
            x.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        }
    }
    public void turnForTicks(int ticks) { //positive = right
        //Blocks until the robot has gotten to the desired location.
        this.rest();
        for (DcMotor x : right) {
            x.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            x.setTargetPosition(-ticks);
            x.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        }
        for (DcMotor x : left){
            x.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            x.setTargetPosition(ticks);
            x.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        }

        this.moveForward(0.25);
        while (right[0].isBusy() && left[0].isBusy()){
        }
        for (DcMotor x : forward) {
            x.setPower(0);
            x.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        }
    }

    public void clawMove(double pos) {
        servo1.setPosition(pos);
    }

    public void collect(double pow) {
        for (DcMotor x : collect) x.setPower(pow);
    }

    public void moveByInches(double inches) {
        //Requires that the "ticksPerInch" variable is correctly set.
        moveForTicks((int) (inches * ticksPerInch));
    }

    private void sleep(long milliseconds) {
        //Ripped right from the FTC OpMode specifications
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void moveX(float ox, float x) {
        this.moveByInches((int) ((ox - x) * 25.4));
    }

    public void moveY(float oy, float y) {
        this.moveByInches((int) ((oy - y) * 25.4));
    }


    public double getX(VuforiaTrackable trackable) {
        if (((VuforiaTrackableDefaultListener) trackable.getListener()).isVisible()) {
            OpenGLMatrix robotLocationTransform = ((VuforiaTrackableDefaultListener) trackable.getListener()).getUpdatedRobotLocation();
            if (robotLocationTransform != null) {
                lastLocation = robotLocationTransform;
            }
        }
        if (lastLocation != null) {
            return lastLocation.getTranslation().get(0);
        } else {
            return 1000;
        }
    }

    public double getY(VuforiaTrackable trackable) {
        if (((VuforiaTrackableDefaultListener) trackable.getListener()).isVisible()) {
            OpenGLMatrix robotLocationTransform = ((VuforiaTrackableDefaultListener) trackable.getListener()).getUpdatedRobotLocation();
            if (robotLocationTransform != null) {
                lastLocation = robotLocationTransform;
            }
        }
        if (lastLocation != null) {
            return lastLocation.getTranslation().get(1);
        } else {
            return 1000;
        }
    }
}