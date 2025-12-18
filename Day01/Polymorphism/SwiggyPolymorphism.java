// File: SwiggyPolymorphism.java

// ---------- Base class for Runtime Polymorphism ----------
class DeliveryPartner {
    public void deliver(String orderId) {
        System.out.println("Delivering order " + orderId + " in a generic way.");
    }
}

// Child class 1
class BikePartner extends DeliveryPartner {
    @Override
    public void deliver(String orderId) {
        System.out.println("Delivering order " + orderId + " using a Bike 🚴");
    }
}

// Child class 2
class CarPartner extends DeliveryPartner {
    @Override
    public void deliver(String orderId) {
        System.out.println("Delivering order " + orderId + " using a Car 🚗");
    }
}

// ---------- Class for Compile-Time Polymorphism ----------
class OrderHelper {
    // Method with 1 parameter
    public void assignOrder(String partnerName) {
        System.out.println("Order assigned to delivery partner: " + partnerName);
    }

    // Overloaded method with 2 parameters
    public void assignOrder(String partnerName, String vehicle) {
        System.out.println("Order assigned to " + partnerName + " with vehicle: " + vehicle);
    }
}

public class SwiggyPolymorphism {
    public static void main(String[] args) {
        // ---------- Compile-Time Polymorphism ----------
        OrderHelper helper = new OrderHelper();
        helper.assignOrder("Rahul");
        helper.assignOrder("Priya", "Scooter");

        // ---------- Runtime Polymorphism ----------
        DeliveryPartner partner;

        partner = new BikePartner(); 
        partner.deliver("ORDER123");

        partner = new CarPartner(); 
        partner.deliver("ORDER456");
    }
}
