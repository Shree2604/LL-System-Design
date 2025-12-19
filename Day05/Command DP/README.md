# Command Design Pattern

<div style="background-color: #1e1e1e; padding: 20px; border-radius: 8px; color: #e0e0e0;">

## 🏗️ Overview
Encapsulate a request as an object, thereby letting you parameterize clients with different requests, queue or log requests, and support undoable operations.

## 📊 Architecture

```mermaid
classDiagram
    %%{init: {'theme': 'dark'}}%%
    class Command {
        <<interface>>
        +execute()
        +undo()
    }
    
    class LightOnCommand {
        -light: Light
        +execute()
        +undo()
    }
    
    class Light {
        +on()
        +off()
    }
    
    class RemoteControl {
        -onCommands: Command[]
        -offCommands: Command[]
        +setCommand(slot: int, onCommand: Command, offCommand: Command)
        +onButtonWasPushed(slot: int)
        +offButtonWasPushed(slot: int)
    }
    
    Command <|-- LightOnCommand
    LightOnCommand --> Light
    RemoteControl --> Command
```

## 🎯 When to Use
- Need to parameterize objects by an action to perform
- Need to specify, queue, and execute requests at different times
- Need to support undo/redo operations
- Need to support logging changes

## ✅ Pros
- Decouples the object that invokes the operation from the one that knows how to perform it
- Commands can be manipulated and extended like any other object
- Easy to implement undo/redo functionality
- Can assemble commands into composite commands

## ❌ Cons
- Can lead to an increase in the number of classes
- May complicate the code if you only need a simple command

## 🔍 Real-world Analogy
Think of a restaurant order. The waiter (Invoker) takes the order (Command) from the customer and gives it to the chef (Receiver) who prepares the meal. The waiter doesn't need to know how to prepare the food, just what was ordered.

## 🛠️ Implementation Details
- `Command` interface declares the execution method
- Concrete command classes implement the command interface
- Invoker asks the command to carry out the request
- Receiver knows how to perform the operations

## 📝 Example Usage
```java
// Create the receiver
Light light = new Light();

// Create the command with the receiver
Command lightOn = new LightOnCommand(light);

// Create the invoker and set the command
RemoteControl remote = new RemoteControl();
remote.setCommand(0, lightOn, null);

// Execute the command
remote.onButtonWasPushed(0);
```

## 🌟 Key Points
- Commands are objects that encapsulate a request
- Can support undo by maintaining state
- Can be queued or logged
- Can support transactions

</div>

<style>
  body {
    background-color: #1e1e1e;
    color: #e0e0e0;
  }
  h1, h2, h3, h4, h5, h6 {
    color: #4ec9b0;
  }
  code {
    background-color: #2d2d2d;
    color: #d4d4d4;
  }
  pre {
    background-color: #2d2d2d;
    border-radius: 4px;
    padding: 12px;
  }
</style>
