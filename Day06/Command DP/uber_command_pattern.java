import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

// ==================== RECEIVER ====================
// RideService - Performs the actual ride operations
class RideService {
    private String rideId;
    private String passenger;
    private String driver;
    private String pickup;
    private String destination;
    private String status;
    
    public void requestRide(String rideId, String passenger, String pickup, String destination) {
        this.rideId = rideId;
        this.passenger = passenger;
        this.pickup = pickup;
        this.destination = destination;
        this.status = "REQUESTED";
        
        System.out.println("\n🚗 RIDE REQUESTED");
        System.out.println("Ride ID: " + rideId);
        System.out.println("Passenger: " + passenger);
        System.out.println("Pickup: " + pickup);
        System.out.println("Destination: " + destination);
        System.out.println("Status: " + status);
        System.out.println("Searching for nearby drivers...");
    }
    
    public void confirmRide(String driver) {
        if (this.status.equals("REQUESTED")) {
            this.driver = driver;
            this.status = "CONFIRMED";
            
            System.out.println("\n✅ RIDE CONFIRMED");
            System.out.println("Ride ID: " + rideId);
            System.out.println("Driver: " + driver + " accepted your ride");
            System.out.println("Passenger: " + passenger);
            System.out.println("Pickup: " + pickup);
            System.out.println("Destination: " + destination);
            System.out.println("Status: " + status);
            System.out.println("Driver is on the way!");
        } else {
            System.out.println("❌ Cannot confirm ride. Current status: " + status);
        }
    }
    
    public void cancelRide() {
        if (!this.status.equals("COMPLETED")) {
            String previousStatus = this.status;
            this.status = "CANCELLED";
            
            System.out.println("\n❌ RIDE CANCELLED");
            System.out.println("Ride ID: " + rideId);
            System.out.println("Passenger: " + passenger);
            System.out.println("Previous Status: " + previousStatus);
            System.out.println("Current Status: " + status);
            System.out.println("Refund will be processed if applicable");
            
            // Reset ride details
            this.driver = null;
        } else {
            System.out.println("❌ Cannot cancel. Ride already completed.");
        }
    }
    
    public void deleteRide() {
        System.out.println("\n🗑️  RIDE DELETED");
        System.out.println("Ride ID: " + rideId);
        System.out.println("Removing ride from system...");
        
        // Clear all data
        this.rideId = null;
        this.passenger = null;
        this.driver = null;
        this.pickup = null;
        this.destination = null;
        this.status = "DELETED";
        
        System.out.println("Ride data has been permanently deleted");
    }
    
    public String getRideInfo() {
        return String.format("Ride[ID=%s, Passenger=%s, Driver=%s, Status=%s]", 
                           rideId, passenger, driver, status);
    }
}

// ==================== COMMAND INTERFACE ====================
interface RideCommand {
    void execute();
    void undo(); // For undo functionality
    String getCommandName();
}

// ==================== CONCRETE COMMANDS ====================

// Command to Request a Ride
class RequestRideCommand implements RideCommand {
    private RideService rideService;
    private String rideId;
    private String passenger;
    private String pickup;
    private String destination;
    
    public RequestRideCommand(RideService rideService, String rideId, 
                            String passenger, String pickup, String destination) {
        this.rideService = rideService;
        this.rideId = rideId;
        this.passenger = passenger;
        this.pickup = pickup;
        this.destination = destination;
    }
    
    @Override
    public void execute() {
        rideService.requestRide(rideId, passenger, pickup, destination);
    }
    
    @Override
    public void undo() {
        System.out.println("\n↩️  UNDO: Cancelling ride request...");
        rideService.cancelRide();
    }
    
    @Override
    public String getCommandName() {
        return "REQUEST_RIDE";
    }
}

// Command to Confirm a Ride
class ConfirmRideCommand implements RideCommand {
    private RideService rideService;
    private String driver;
    
    public ConfirmRideCommand(RideService rideService, String driver) {
        this.rideService = rideService;
        this.driver = driver;
    }
    
    @Override
    public void execute() {
        rideService.confirmRide(driver);
    }
    
    @Override
    public void undo() {
        System.out.println("\n↩️  UNDO: Reverting ride confirmation...");
        rideService.cancelRide();
    }
    
    @Override
    public String getCommandName() {
        return "CONFIRM_RIDE";
    }
}

// Command to Cancel a Ride
class CancelRideCommand implements RideCommand {
    private RideService rideService;
    
    public CancelRideCommand(RideService rideService) {
        this.rideService = rideService;
    }
    
    @Override
    public void execute() {
        rideService.cancelRide();
    }
    
    @Override
    public void undo() {
        System.out.println("\n↩️  UNDO: Cannot undo cancellation. Please request a new ride.");
    }
    
    @Override
    public String getCommandName() {
        return "CANCEL_RIDE";
    }
}

// Command to Delete a Ride
class DeleteRideCommand implements RideCommand {
    private RideService rideService;
    
    public DeleteRideCommand(RideService rideService) {
        this.rideService = rideService;
    }
    
    @Override
    public void execute() {
        rideService.deleteRide();
    }
    
    @Override
    public void undo() {
        System.out.println("\n↩️  UNDO: Cannot undo deletion. Ride data is permanently removed.");
    }
    
    @Override
    public String getCommandName() {
        return "DELETE_RIDE";
    }
}

// ==================== INVOKER ====================
// RideController - Manages and executes ride commands
class RideController {
    private RideCommand lastCommand; // For undo functionality
    
    public void executeCommand(RideCommand command) {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("⚡ Executing: " + command.getCommandName());
        System.out.println("=".repeat(50));
        
        command.execute();
        lastCommand = command; // Store for potential undo
    }
    
    public void undoLastCommand() {
        if (lastCommand != null) {
            System.out.println("\n" + "=".repeat(50));
            System.out.println("⚡ Undoing: " + lastCommand.getCommandName());
            System.out.println("=".repeat(50));
            lastCommand.undo();
        } else {
            System.out.println("\n❌ No command to undo");
        }
    }
}

// ==================== CLIENT ====================
public class UberRideSystem {
    public static void main(String[] args) {
        System.out.println("🚕 UBER RIDE MANAGEMENT SYSTEM 🚕");
        System.out.println("Using Command Design Pattern\n");
        
        // Create Receiver (RideService)
        RideService rideService = new RideService();
        
        // Create Invoker (RideController)
        RideController controller = new RideController();
        
        // ===== SCENARIO 1: Request a Ride =====
        RideCommand requestRide = new RequestRideCommand(
            rideService, 
            "RIDE-12345", 
            "John Doe", 
            "Times Square, NYC", 
            "Central Park, NYC"
        );
        controller.executeCommand(requestRide);
        
        // Simulate delay
        sleep(1000);
        
        // ===== SCENARIO 2: Confirm the Ride =====
        RideCommand confirmRide = new ConfirmRideCommand(
            rideService, 
            "Mike Wilson (4.8★)"
        );
        controller.executeCommand(confirmRide);
        
        // Simulate delay
        sleep(1000);
        
        // ===== SCENARIO 3: Undo Confirmation (Cancel) =====
        System.out.println("\n\n📱 Passenger changed their mind...");
        controller.undoLastCommand();
        
        sleep(1000);
        
        // ===== SCENARIO 4: Request a New Ride =====
        RideCommand requestRide2 = new RequestRideCommand(
            rideService, 
            "RIDE-12346", 
            "Jane Smith", 
            "Brooklyn Bridge", 
            "JFK Airport"
        );
        controller.executeCommand(requestRide2);
        
        sleep(1000);
        
        // ===== SCENARIO 5: Confirm New Ride =====
        RideCommand confirmRide2 = new ConfirmRideCommand(
            rideService, 
            "Sarah Johnson (4.9★)"
        );
        controller.executeCommand(confirmRide2);
        
        sleep(1000);
        
        // ===== SCENARIO 6: Cancel Ride =====
        System.out.println("\n\n📱 Emergency! Passenger needs to cancel...");
        RideCommand cancelRide = new CancelRideCommand(rideService);
        controller.executeCommand(cancelRide);
        
        sleep(1000);
        
        // ===== SCENARIO 7: Delete Ride from System =====
        RideCommand deleteRide = new DeleteRideCommand(rideService);
        controller.executeCommand(deleteRide);
        
        System.out.println("\n\n" + "=".repeat(50));
        System.out.println("✅ UBER RIDE SYSTEM DEMO COMPLETED");
        System.out.println("=".repeat(50));
    }
    
    private static void sleep(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}