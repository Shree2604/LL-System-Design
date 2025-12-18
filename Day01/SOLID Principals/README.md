# SOLID Principles in Java: Swiggy Delivery System

```mermaid
classDiagram
    %% SRP: Single Responsibility
    class DeliveryPartner {
        -String name
        +deliverOrder(String orderId)
    }
    
    %% OCP: Open/Closed Principle
    class DeliveryVehicle {
        <<abstract>>
        +deliver(String orderId)
    }
    
    class Bike {
        +deliver(String orderId)
    }
    
    class Car {
        +deliver(String orderId)
    }
    
    %% LSP: Liskov Substitution
    class SwiggyDeliveryService {
        +startDelivery(DeliveryVehicle, String)
    }
    
    %% ISP: Interface Segregation
    class Deliverable {
        <<interface>>
        +deliverOrder(String)
    }
    
    class Trackable {
        <<interface>>
        +trackLocation(String)
    }
    
    class SwiggyPartner {
        -String name
        +deliverOrder(String)
        +trackLocation(String)
    }
    
    %% DIP: Dependency Inversion
    class PaymentService {
        <<interface>>
        +processPayment(double)
    }
    
    class UpiPayment {
        +processPayment(double)
    }
    
    class SwiggyApp {
        -PaymentService paymentService
        +makePayment(double)
    }
    
    %% Relationships
    DeliveryVehicle <|-- Bike
    DeliveryVehicle <|-- Car
    SwiggyPartner ..|> Deliverable
    SwiggyPartner ..|> Trackable
    SwiggyApp --> PaymentService
    UpiPayment ..|> PaymentService
```

## Architecture Overview

### 1. Single Responsibility Principle (SRP)
```mermaid
graph TD
    A[DeliveryPartner] -->|Responsibility| B[Deliver Orders]
    
    style A fill:#f9f,stroke:#333,stroke-width:2px
    style B fill:#bbf,stroke:#333,stroke-width:2px
```

### 2. Open/Closed Principle (OCP)
```mermaid
graph TD
    A[DeliveryVehicle] -->|Extended by| B[Bike]
    A -->|Extended by| C[Car]
    
    style A fill:#9f9,stroke:#333,stroke-width:2px
    style B fill:#bbf,stroke:#333,stroke-width:2px
    style C fill:#bbf,stroke:#333,stroke-width:2px
```

### 3. Liskov Substitution Principle (LSP)
```mermaid
flowchart LR
    A[SwiggyDeliveryService] -->|Uses| B[DeliveryVehicle]
    B -->|Implemented by| C[Bike & Car]
    
    style A fill:#ff9,stroke:#333,stroke-width:2px
    style B fill:#9f9,stroke:#333,stroke-width:2px
```

### 4. Interface Segregation Principle (ISP)
```mermaid
classDiagram
    class Deliverable {
        <<interface>>
        +deliverOrder()
    }
    
    class Trackable {
        <<interface>>
        +trackLocation()
    }
    
    class SwiggyPartner {
        +deliverOrder()
        +trackLocation()
    }
    
    SwiggyPartner ..|> Deliverable
    SwiggyPartner ..|> Trackable
```

### 5. Dependency Inversion Principle (DIP)
```mermaid
flowchart TD
    A[SwiggyApp] -->|Depends on| B[PaymentService Interface]
    B <|-- C[UpiPayment]
    
    style A fill:#f9f,stroke:#333,stroke-width:2px
    style B fill:#9f9,stroke:#333,stroke-width:2px
    style C fill:#bbf,stroke:#333,stroke-width:2px
```

This project demonstrates the five SOLID principles of object-oriented programming using a Swiggy-like food delivery system as an example.

## 1. Single Responsibility Principle (SRP)
- **Class**: `DeliveryPartner`
- **Principle**: A class should have only one reason to change
- **Implementation**:
  ```java
  class DeliveryPartner {
      private String name;
      
      public void deliverOrder(String orderId) {
          System.out.println(name + " is delivering order " + orderId);
      }
  }
  ```
- **Benefit**: Each class has only one responsibility, making the code more maintainable.

## 2. Open/Closed Principle (OCP)
- **Classes**: `DeliveryVehicle` (abstract), `Bike`, `Car`
- **Principle**: Software entities should be open for extension but closed for modification
- **Implementation**:
  ```java
  abstract class DeliveryVehicle {
      public abstract void deliver(String orderId);
  }
  
  class Bike extends DeliveryVehicle { /* ... */ }
  class Car extends DeliveryVehicle { /* ... */ }
  ```
- **Benefit**: New vehicle types can be added without modifying existing code.

## 3. Liskov Substitution Principle (LSP)
- **Class**: `SwiggyDeliveryService`
- **Principle**: Objects of a superclass should be replaceable with objects of its subclasses
- **Implementation**:
  ```java
  public void startDelivery(DeliveryVehicle vehicle, String orderId) {
      vehicle.deliver(orderId); // works with any DeliveryVehicle subclass
  }
  ```
- **Benefit**: Ensures that a subclass can stand in for its parent class.

## 4. Interface Segregation Principle (ISP)
- **Interfaces**: `Deliverable`, `Trackable`
- **Principle**: Clients shouldn't be forced to depend on interfaces they don't use
- **Implementation**:
  ```java
  interface Deliverable { void deliverOrder(String orderId); }
  interface Trackable { void trackLocation(String partnerName); }
  
  class SwiggyPartner implements Deliverable, Trackable { /* ... */ }
  ```
- **Benefit**: Clients only need to know about the methods that are of interest to them.

## 5. Dependency Inversion Principle (DIP)
- **Classes**: `PaymentService`, `UpiPayment`, `SwiggyApp`
- **Principle**: Depend on abstractions, not on concrete classes
- **Implementation**:
  ```java
  interface PaymentService { void processPayment(double amount); }
  
  class SwiggyApp {
      private PaymentService paymentService;
      public SwiggyApp(PaymentService paymentService) {
          this.paymentService = paymentService;
      }
  }
  ```
- **Benefit**: Makes the system more flexible and easier to modify.

## How to Run
1. Compile the Java file:
   ```bash
   javac SwiggySOLID.java
   ```
2. Run the compiled class:
   ```bash
   java SwiggySOLID
   ```

## Expected Output
```
Rahul is delivering order ORDER101
Delivering order ORDER102 using Bike 🚴
Delivering order ORDER103 using Car 🚗
Priya delivered order ORDER104
Tracking location of Priya...
Payment of Rs.299.0 done via UPI ✅
```

## Real-world Applications
- **SRP**: Microservices architecture
- **OCP**: Plugin architectures
- **LSP**: Collections framework in Java
- **ISP**: Adapter pattern implementations
- **DIP**: Dependency injection frameworks

## Benefits of SOLID Principles
- Improved code maintainability
- Easier to extend
- More testable code
- Reduced coupling between components
- Better code organization
