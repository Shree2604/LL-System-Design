// -------------------- S (Single Responsibility Principle) --------------------
// Each class should have only ONE reason to change.
// Here, DeliveryPartner only handles delivery, not payment or order processing.
class DeliveryPartner {
    private String name;

    public DeliveryPartner(String name) {
        this.name = name;
    }

    public void deliverOrder(String orderId) {
        System.out.println(name + " is delivering order " + orderId);
    }
}

// -------------------- O (Open/Closed Principle) --------------------
// Software should be open for extension, but closed for modification.
// We can extend DeliveryVehicle without modifying existing code.
abstract class DeliveryVehicle {
    public abstract void deliver(String orderId);
}

class Bike extends DeliveryVehicle {
    @Override
    public void deliver(String orderId) {
        System.out.println("Delivering order " + orderId + " using Bike 🚴");
    }
}

class Car extends DeliveryVehicle {
    @Override
    public void deliver(String orderId) {
        System.out.println("Delivering order " + orderId + " using Car 🚗");
    }
}

// -------------------- L (Liskov Substitution Principle) --------------------
// Subclasses should be replaceable with their parent type without breaking behavior.
class SwiggyDeliveryService {
    public void startDelivery(DeliveryVehicle vehicle, String orderId) {
        vehicle.deliver(orderId); // works with Bike, Car, or any future vehicle
    }
}

// -------------------- I (Interface Segregation Principle) --------------------
// Clients should not be forced to depend on interfaces they don't use.
// We split into smaller interfaces: one for delivery, one for tracking.
interface Deliverable {
    void deliverOrder(String orderId);
}

interface Trackable {
    void trackLocation(String partnerName);
}

class SwiggyPartner implements Deliverable, Trackable {
    private String name;

    public SwiggyPartner(String name) {
        this.name = name;
    }

    @Override
    public void deliverOrder(String orderId) {
        System.out.println(name + " delivered order " + orderId);
    }

    @Override
    public void trackLocation(String partnerName) {
        System.out.println("Tracking location of " + partnerName + "...");
    }
}

// -------------------- D (Dependency Inversion Principle) --------------------
// High-level modules should not depend on low-level modules directly.
// Both should depend on abstractions (interfaces).
interface PaymentService {
    void processPayment(double amount);
}

class UpiPayment implements PaymentService {
    @Override
    public void processPayment(double amount) {
        System.out.println("Payment of Rs." + amount + " done via UPI ✅");
    }
}

class SwiggyApp {
    private PaymentService paymentService;

    // Dependency is injected via constructor
    public SwiggyApp(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    public void makePayment(double amount) {
        paymentService.processPayment(amount);
    }
}

// -------------------- MAIN --------------------
public class SwiggySOLID {
    public static void main(String[] args) {
        // S - Single Responsibility
        DeliveryPartner p1 = new DeliveryPartner("Rahul");
        p1.deliverOrder("ORDER101");

        // O & L - Open/Closed + Liskov
        SwiggyDeliveryService service = new SwiggyDeliveryService();
        service.startDelivery(new Bike(), "ORDER102");
        service.startDelivery(new Car(), "ORDER103");

        // I - Interface Segregation
        SwiggyPartner partner = new SwiggyPartner("Priya");
        partner.deliverOrder("ORDER104");
        partner.trackLocation("Priya");

        // D - Dependency Inversion
        PaymentService payment = new UpiPayment(); // abstraction
        SwiggyApp app = new SwiggyApp(payment);
        app.makePayment(299.0);
    }
}
