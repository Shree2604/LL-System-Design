// AbstractFactorySwiggy.java
// 👉 Pattern: ABSTRACT FACTORY
// Creates families of related products (Veg/Non-Veg meals).

// --- Product A: MainCourse ---
interface MainCourse {
    String serve();
}

// --- Product B: Beverage ---
interface Beverage {
    String serve();
}

// --- Veg Products ---
class VegMainCourse implements MainCourse {
    public String serve() {
        return "Serving Paneer Butter Masala (Veg Main).";
    }
}

class VegBeverage implements Beverage {
    public String serve() {
        return "Serving Mango Lassi (Veg Beverage).";
    }
}

// --- Non-Veg Products ---
class NonVegMainCourse implements MainCourse {
    public String serve() {
        return "Serving Butter Chicken (Non-Veg Main).";
    }
}

class NonVegBeverage implements Beverage {
    public String serve() {
        return "Serving Masala Chai (Non-Veg Beverage).";
    }
}

// --- Abstract Factory ---
interface MealFactory {
    MainCourse createMain();
    Beverage createBeverage();
}

// --- Concrete Factories ---
class VegMealFactory implements MealFactory {
    public MainCourse createMain() {
        return new VegMainCourse();
    }
    public Beverage createBeverage() {
        return new VegBeverage();
    }
}

class NonVegMealFactory implements MealFactory {
    public MainCourse createMain() {
        return new NonVegMainCourse();
    }
    public Beverage createBeverage() {
        return new NonVegBeverage();
    }
}

// --- Client code ---
public class AbstractFactorySwiggy {
    // Function uses any factory to prepare meal
    static String prepareMeal(MealFactory factory) {
        MainCourse main = factory.createMain();
        Beverage bev = factory.createBeverage();
        return main.serve() + " + " + bev.serve();
    }

    public static void main(String[] args) {
        MealFactory vegFactory = new VegMealFactory();
        MealFactory nonVegFactory = new NonVegMealFactory();

        System.out.println("Veg Meal: " + prepareMeal(vegFactory));
        System.out.println("Non-Veg Meal: " + prepareMeal(nonVegFactory));
    }
}
