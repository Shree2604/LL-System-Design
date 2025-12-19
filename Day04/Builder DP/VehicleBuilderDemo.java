// VehicleBuilderDemo.java

class Vehicle {
    private int wheels;
    private String color;
    private String engine;

    public Vehicle(int wheels, String color, String engine) {
        this.wheels = wheels;
        this.color = color;
        this.engine = engine;
    }

    @Override
    public String toString() {
        return "Vehicle(wheels=" + wheels + ", color=" + color + ", engine=" + engine + ")";
    }
}

class VehicleBuilder {
    private int wheels;
    private String color;
    private String engine;

    public VehicleBuilder setWheels(int wheels) {
        this.wheels = wheels;
        return this;
    }

    public VehicleBuilder setColor(String color) {
        this.color = color;
        return this;
    }

    public VehicleBuilder setEngine(String engine) {
        this.engine = engine;
        return this;
    }

    public Vehicle build() {
        return new Vehicle(wheels, color, engine);
    }
}

public class VehicleBuilderDemo {
    public static void main(String[] args) {
        Vehicle car = new VehicleBuilder()
                .setWheels(4)
                .setColor("Red")
                .setEngine("V8")
                .build();

        System.out.println(car);
    }
}
