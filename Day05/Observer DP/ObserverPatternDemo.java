// ObserverPatternDemo.java

/*
    OBSERVER DESIGN PATTERN
    ------------------------
    • Defines a one-to-many relationship between objects.
    • When the Subject (StockMarket) changes state, all Observers (MobileApp, WebApp)
      are automatically notified.
    • Used for event handling, listeners, UI updates, stock price trackers, notifications, etc.
*/

import java.util.ArrayList;
import java.util.List;

// ----------- Observer Interface -----------
// All observers must implement the update() method to get notifications.
interface Observer {
    void update(float price);
}

// ----------- Subject Interface -----------
// Subject (publisher) maintains list of observers & notifies them.
interface Subject {
    void registerObserver(Observer o);
    void removeObserver(Observer o);
    void notifyObservers();
}

// ----------- Concrete Subject ------------
// This class holds the actual stock price and notifies all observers on update.
class StockMarket implements Subject {

    private List<Observer> observers = new ArrayList<>();
    private float stockPrice;

    // Add observer
    @Override
    public void registerObserver(Observer o) {
        observers.add(o);
    }

    // Remove observer
    @Override
    public void removeObserver(Observer o) {
        observers.remove(o);
    }

    // When price updates, automatically notify all observers
    public void setStockPrice(float price) {
        this.stockPrice = price;
        notifyObservers();
    }

    // Notify each observer by calling its update()
    @Override
    public void notifyObservers() {
        for (Observer o : observers) {
            o.update(stockPrice);
        }
    }
}

// ----------- Concrete Observers ----------
// Classes that want updates implement Observer

class MobileApp implements Observer {
    @Override
    public void update(float price) {
        System.out.println("Mobile App: Stock Price Updated to " + price);
    }
}

class WebApp implements Observer {
    @Override
    public void update(float price) {
        System.out.println("Web App: Stock Price Updated to " + price);
    }
}

// ----------- Demo Class (main method) ----
// Demonstrates registering, updating, and removing observers.
public class ObserverPatternDemo {
    public static void main(String[] args) {

        // Create subject (publisher)
        StockMarket stock = new StockMarket();

        // Create observers (subscribers)
        Observer mobileApp = new MobileApp();
        Observer webApp = new WebApp();

        // Register observers
        stock.registerObserver(mobileApp);
        stock.registerObserver(webApp);

        System.out.println("Setting stock price to 120.5");
        stock.setStockPrice(120.5f);   // Both observers get update

        System.out.println("\nRemoving WebApp Observer...");
        stock.removeObserver(webApp);  // WebApp unsubscribes

        System.out.println("Setting stock price to 132.0");
        stock.setStockPrice(132.0f);   // Only MobileApp receives update
    }
}
