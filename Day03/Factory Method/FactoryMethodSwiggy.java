// FactoryMethodSwiggy.java
// 👉 Pattern: FACTORY METHOD
// Each creator subclass defines its own delivery creation logic.

interface Delivery {
    String deliver(int orderId);
}

// --- Concrete Products ---
class BikeDelivery implements Delivery {
    public String deliver(int orderId) {
        return "Delivering order " + orderId + " by bike (fast for short distances).";
    }
}

class CarDelivery implements Delivery {
    public String deliver(int orderId) {
        return "Delivering order " + orderId + " by car (good for long distances).";
    }
}

class DroneDelivery implements Delivery {
    public String deliver(int orderId) {
        return "Delivering order " + orderId + " by drone (experimental).";
    }
}

// --- Creator Abstract Class ---
abstract class DeliveryCreator {
    // Factory Method - must be implemented by subclasses
    public abstract Delivery createDelivery();

    // Common operation using the product
    public String send(int orderId) {
        Delivery delivery = createDelivery();
        return delivery.deliver(orderId);
    }
}

// --- Concrete Creators ---
class UrbanDeliveryCreator extends DeliveryCreator {
    public Delivery createDelivery() {
        return new BikeDelivery();
    }
}

class LongDistanceDeliveryCreator extends DeliveryCreator {
    public Delivery createDelivery() {
        return new CarDelivery();
    }
}

class ExperimentalDeliveryCreator extends DeliveryCreator {
    public Delivery createDelivery() {
        return new DroneDelivery();
    }
}

// --- Client code ---
public class FactoryMethodSwiggy {
    public static void main(String[] args) {
        DeliveryCreator urban = new UrbanDeliveryCreator();
        System.out.println(urban.send(101));

        DeliveryCreator longDist = new LongDistanceDeliveryCreator();
        System.out.println(longDist.send(202));

        DeliveryCreator drone = new ExperimentalDeliveryCreator();
        System.out.println(drone.send(303));
    }
}
