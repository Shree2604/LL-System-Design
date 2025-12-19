// VehiclePrototypeDemo.java

interface Vehicle extends Cloneable {
    Vehicle clone();
    String drive();
}

// Concrete Prototype: Car
class Car implements Vehicle {
    public Vehicle clone() {
        return new Car(); // cloning by creating a copy
    }

    public String drive() {
        return "Driving a Car";
    }
}

// Concrete Prototype: Bike
class Bike implements Vehicle {
    public Vehicle clone() {
        return new Bike();
    }

    public String drive() {
        return "Riding a Bike";
    }
}

// Prototype Registry
class VehicleRegistry {
    private static java.util.Map<String, Vehicle> vehicles = new java.util.HashMap<>();

    static {
        vehicles.put("car", new Car());
        vehicles.put("bike", new Bike());
    }

    public static Vehicle getVehicle(String type) {
        Vehicle prototype = vehicles.get(type.toLowerCase());
        if (prototype == null) {
            throw new IllegalArgumentException("Unknown vehicle type");
        }
        return prototype.clone(); // cloning instead of new
    }
}

// Demo Class
public class VehiclePrototypeDemo {
    public static void main(String[] args) {
        Vehicle v1 = VehicleRegistry.getVehicle("car");
        Vehicle v2 = VehicleRegistry.getVehicle("car");

        System.out.println(v1.drive());
        System.out.println(v2.drive());

        System.out.println("Different Objects? " + (v1 != v2));
    }
}
