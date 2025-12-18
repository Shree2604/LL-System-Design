// SimpleFactorySwiggy.java
// 👉 Pattern: SIMPLE FACTORY
// One centralized factory creates restaurant objects based on type.

interface Restaurant {
    String prepareOrder(String dishName);
}

// --- Concrete Products ---
class PizzaCorner implements Restaurant {
    public String prepareOrder(String dishName) {
        return "PizzaCorner preparing " + dishName + " with extra cheese.";
    }
}

class BurgerHouse implements Restaurant {
    public String prepareOrder(String dishName) {
        return "BurgerHouse preparing " + dishName + " with fries.";
    }
}

class SushiExpress implements Restaurant {
    public String prepareOrder(String dishName) {
        return "SushiExpress preparing " + dishName + " with wasabi.";
    }
}

// --- Simple Factory ---
class RestaurantFactory {
    // Decides which restaurant object to return
    public static Restaurant createRestaurant(String type) {
        switch (type.toLowerCase()) {
            case "pizza":
                return new PizzaCorner();
            case "burger":
                return new BurgerHouse();
            case "sushi":
                return new SushiExpress();
            default:
                throw new IllegalArgumentException("Unknown restaurant type: " + type);
        }
    }
}

// --- Client code ---
public class SimpleFactorySwiggy {
    public static void main(String[] args) {
        Restaurant r1 = RestaurantFactory.createRestaurant("pizza");
        System.out.println(r1.prepareOrder("Margherita"));

        Restaurant r2 = RestaurantFactory.createRestaurant("burger");
        System.out.println(r2.prepareOrder("Classic Burger"));
    }
}
