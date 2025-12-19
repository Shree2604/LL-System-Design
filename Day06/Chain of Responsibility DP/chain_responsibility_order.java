/*
 * CHAIN OF RESPONSIBILITY DESIGN PATTERN - ORDER PROCESSING SYSTEM
 * 
 * What is this pattern?
 * - A behavioral design pattern that allows passing requests along a chain of handlers
 * - Each handler decides either to process the request or pass it to the next handler
 * - Decouples sender and receiver of a request
 * 
 * Real-world analogy:
 * - Like a customer support escalation system (Level 1 → Level 2 → Manager)
 * - Or a food delivery order flow (Validate → Pay → Prepare → Deliver → Track)
 * 
 * Key Benefits:
 * 1. Reduces coupling between sender and receiver
 * 2. Adds flexibility in assigning responsibilities
 * 3. Easy to add or remove handlers without breaking code
 * 4. Each handler follows Single Responsibility Principle
 * 
 * Use cases:
 * - Event handling systems
 * - Logging frameworks (DEBUG → INFO → WARN → ERROR)
 * - Middleware in web applications
 * - Approval workflows
 * - Order/request processing pipelines
 */

// Abstract Handler class - Defines the interface for handling requests
abstract class OrderHandler {
    // Reference to the next handler in the chain
    protected OrderHandler nextHandler;
    
    // Method to set the next handler in the chain
    public void setNextHandler(OrderHandler nextHandler) {
        this.nextHandler = nextHandler;
    }
    
    // Abstract method that each concrete handler must implement
    public abstract void processOrder(String order);
}

// Concrete handler for order validation
class OrderValidationHandler extends OrderHandler {
    @Override
    public void processOrder(String order) {
        if (order == null || order.isEmpty()) {
            System.out.println("❌ Order Validation Failed: Order is empty!");
            return;
        }
        
        System.out.println("✓ Order Validation: Order validated successfully - " + order);
        
        if (nextHandler != null) {
            nextHandler.processOrder(order);
        }
    }
}

// Concrete handler for payment processing
class PaymentProcessingHandler extends OrderHandler {
    @Override
    public void processOrder(String order) {
        System.out.println("✓ Payment Processing: Payment processed for - " + order);
        
        if (nextHandler != null) {
            nextHandler.processOrder(order);
        }
    }
}

// Concrete handler for order preparation
class OrderPreparationHandler extends OrderHandler {
    @Override
    public void processOrder(String order) {
        System.out.println("✓ Order Preparation: Order is being prepared - " + order);
        
        if (nextHandler != null) {
            nextHandler.processOrder(order);
        }
    }
}

// Concrete handler for delivery assignment
class DeliveryAssignmentHandler extends OrderHandler {
    @Override
    public void processOrder(String order) {
        System.out.println("✓ Delivery Assignment: Delivery agent assigned for - " + order);
        
        if (nextHandler != null) {
            nextHandler.processOrder(order);
        }
    }
}

// Concrete handler for order tracking
class OrderTrackingHandler extends OrderHandler {
    @Override
    public void processOrder(String order) {
        System.out.println("✓ Order Tracking: Order is out for delivery - " + order);
        System.out.println("🎉 Order completed successfully!\n");
        
        if (nextHandler != null) {
            nextHandler.processOrder(order);
        }
    }
}

// Main class to demonstrate the pattern
public class SwiggyOrder {
    public static void main(String[] args) {
        // Create handlers
        OrderHandler validationHandler = new OrderValidationHandler();
        OrderHandler paymentHandler = new PaymentProcessingHandler();
        OrderHandler preparationHandler = new OrderPreparationHandler();
        OrderHandler deliveryHandler = new DeliveryAssignmentHandler();
        OrderHandler trackingHandler = new OrderTrackingHandler();
        
        // Build the chain
        validationHandler.setNextHandler(paymentHandler);
        paymentHandler.setNextHandler(preparationHandler);
        preparationHandler.setNextHandler(deliveryHandler);
        deliveryHandler.setNextHandler(trackingHandler);
        
        // Process orders
        System.out.println("========== Processing Order 1 ==========");
        validationHandler.processOrder("Pizza Margherita - Customer: John");
        
        System.out.println("========== Processing Order 2 ==========");
        validationHandler.processOrder("Burger Combo - Customer: Sarah");
        
        System.out.println("========== Processing Invalid Order ==========");
        validationHandler.processOrder("");
    }
}