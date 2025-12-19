// VehicleFactoryDemo.java

interface Vehicle {
    String drive();
}

class Car implements Vehicle {
    public String drive() {
        return "Driving a Car";
    }
}

class Bike implements Vehicle {
    public String drive() {
        return "Riding a Bike";
    }
}

class VehicleFactory {
    public static Vehicle getVehicle(String type) {
        switch (type.toLowerCase()) {
            case "car": return new Car();
            case "bike": return new Bike();
            default: throw new IllegalArgumentException("Unknown vehicle type");
        }
    }
}

public class VehicleFactoryDemo {
    public static void main(String[] args) {
        Vehicle v = VehicleFactory.getVehicle("car");
        System.out.println(v.drive());
    }
}
