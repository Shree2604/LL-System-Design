# Factory Method Pattern

## 🏗️ Architecture Overview

```mermaid
classDiagram
    %% Product
    class Delivery {
        <<interface>>
        +deliver(orderId: int) String
    }
    
    %% Concrete Products
    class BikeDelivery {
        +deliver(orderId: int) String
    }
    
    class CarDelivery {
        +deliver(orderId: int) String
    }
    
    class DroneDelivery {
        +deliver(orderId: int) String
    }
    
    %% Creator
    class DeliveryCreator {
        <<abstract>>
        #createDelivery()* Delivery
        +send(orderId: int) String
    }
    
    %% Concrete Creators
    class UrbanDeliveryCreator {
        +createDelivery() Delivery
    }
    
    class LongDistanceDeliveryCreator {
        +createDelivery() Delivery
    }
    
    class ExperimentalDeliveryCreator {
        +createDelivery() Delivery
    }
    
    %% Relationships
    Delivery <|.. BikeDelivery
    Delivery <|.. CarDelivery
    Delivery <|.. DroneDelivery
    
    DeliveryCreator <|-- UrbanDeliveryCreator
    DeliveryCreator <|-- LongDistanceDeliveryCreator
    DeliveryCreator <|-- ExperimentalDeliveryCreator
    
    UrbanDeliveryCreator --> BikeDelivery : creates
    LongDistanceDeliveryCreator --> CarDelivery : creates
    ExperimentalDeliveryCreator --> DroneDelivery : creates
    
    %% Styling for dark theme
    classDef interface fill:#1e3a1e,stroke:#10b981,color:#e0e0e0,stroke-width:2px
    classDef concrete fill:#1e3a8a,stroke:#3b82f6,color:#e0e0e0,stroke-width:2px
    classDef creator fill:#5b21b6,stroke:#8b5cf6,color:#e0e0e0,stroke-width:2px
    
    %% Apply styles to individual classes
    class Delivery interface
    
    class BikeDelivery concrete
    class CarDelivery concrete
    class DroneDelivery concrete
    
    class DeliveryCreator creator
    class UrbanDeliveryCreator creator
    class LongDistanceDeliveryCreator creator
    class ExperimentalDeliveryCreator creator
```

## 🚀 How It Works

The Factory Method pattern defines an interface for creating an object, but lets subclasses decide which class to instantiate. It lets a class defer instantiation to subclasses.

### Key Components

1. **Product** (`Delivery`)
   - Defines the interface of objects the factory method creates.

2. **Concrete Products** (`BikeDelivery`, `CarDelivery`, `DroneDelivery`)
   - Implement the Product interface.

3. **Creator** (`DeliveryCreator`)
   - Declares the factory method that returns an object of type Product.

4. **Concrete Creators** (`UrbanDeliveryCreator`, `LongDistanceDeliveryCreator`, `ExperimentalDeliveryCreator`)
   - Override the factory method to return an instance of a ConcreteProduct.

## 🛠️ Usage

```java
// Create creators
DeliveryCreator urban = new UrbanDeliveryCreator();
DeliveryCreator longDist = new LongDistanceDeliveryCreator();
DeliveryCreator drone = new ExperimentalDeliveryCreator();

// Use creators to create and use products
System.out.println(urban.send(101));
System.out.println(longDist.send(202));
System.out.println(drone.send(303));
```

## 📊 Sequence Diagram

```mermaid
sequenceDiagram
    participant C as Client
    participant UC as UrbanDeliveryCreator
    participant LC as LongDistanceDeliveryCreator
    participant EC as ExperimentalDeliveryCreator
    participant BD as BikeDelivery
    participant CD as CarDelivery
    participant DD as DroneDelivery
    
    %% Styling for dark theme
    rect rgba(0, 0, 0, 0.1)
    Note over C,DD: Dark Theme - Factory Method Pattern
    end
    
    C->>UC: send(101)
    UC->>UC: createDelivery()
    UC->>BD: new BikeDelivery()
    BD-->>UC: delivery
    UC->>BD: deliver(101)
    BD-->>UC: result
    UC-->>C: result
    
    C->>LC: send(202)
    LC->>LC: createDelivery()
    LC->>CD: new CarDelivery()
    CD-->>LC: delivery
    LC->>CD: deliver(202)
    CD-->>LC: result
    LC-->>C: result
    
    C->>EC: send(303)
    EC->>EC: createDelivery()
    EC->>DD: new DroneDelivery()
    DD-->>EC: delivery
    EC->>DD: deliver(303)
    DD-->>EC: result
    EC-->>C: result
    
    %% Styling
    rect rgba(30, 58, 38, 0.1)
    Note over UC,DD: Object Creation and Method Invocation
    end
```

## ✅ When to Use

- When a class can't anticipate the class of objects it must create
- When a class wants its subclasses to specify the objects it creates
- When classes delegate responsibility to one of several helper subclasses, and you want to localize the knowledge of which helper subclass is the delegate

## 📝 Notes

- Follows the Open/Closed Principle: You can introduce new types of products into the program without breaking existing client code.
- Avoids tight coupling between the creator and the concrete products.
- Single Responsibility Principle: The product creation code is in one place in the program, making the code easier to support.
