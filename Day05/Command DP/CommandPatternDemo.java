// CommandPatternDemo.java

/*
    COMMAND DESIGN PATTERN
    -----------------------
    • Encapsulates a request as an object.
    • Decouples sender (invoker) from receiver (actual business logic).
    • Supports undo/redo, batching commands, logging, etc.
*/

// ----------- Command Interface -----------
interface Command {
    void execute();
}

// ----------- Receiver -----------
class Light {
    public void turnOn() {
        System.out.println("Light is ON");
    }

    public void turnOff() {
        System.out.println("Light is OFF");
    }
}

// ----------- Concrete Commands -----------
class TurnLightOnCommand implements Command {
    private Light light;

    public TurnLightOnCommand(Light light) {
        this.light = light;
    }

    @Override
    public void execute() {
        light.turnOn();
    }
}

class TurnLightOffCommand implements Command {
    private Light light;

    public TurnLightOffCommand(Light light) {
        this.light = light;
    }

    @Override
    public void execute() {
        light.turnOff();
    }
}

// ----------- Invoker (button / remote) -----------
class RemoteControl {
    private Command command;

    public void setCommand(Command command) {
        this.command = command;
    }

    // When user presses button → execute command
    public void pressButton() {
        command.execute();
    }
}

// ----------- Demo Class -----------
public class CommandPatternDemo {
    public static void main(String[] args) {

        Light light = new Light();

        // Create commands
        Command turnOn = new TurnLightOnCommand(light);
        Command turnOff = new TurnLightOffCommand(light);

        // Remote control (Invoker)
        RemoteControl remote = new RemoteControl();

        // Turn ON
        remote.setCommand(turnOn);
        remote.pressButton();

        // Turn OFF
        remote.setCommand(turnOff);
        remote.pressButton();
    }
}
