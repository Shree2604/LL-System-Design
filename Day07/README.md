# Design Patterns: Strategy, Strategy with Factory, and Template Method

This directory contains implementations of three design patterns in Java:
1. Strategy Pattern
2. Strategy Pattern with Factory
3. Template Method Pattern

## Table of Contents
1. [Strategy Design Pattern](#strategy-design-pattern)
   - [Overview](#overview)
   - [Class Diagram](#class-diagram)
   - [Implementation Details](#implementation-details)
   - [Usage](#usage)

2. [Strategy + Factory Design Pattern](#strategy--factory-design-pattern)
   - [Overview](#overview-1)
   - [Class Diagram](#class-diagram-1)
   - [Implementation Details](#implementation-details-1)
   - [Usage](#usage-1)

3. [Template Method Design Pattern](#template-method-design-pattern)
   - [Overview](#overview-2)
   - [Class Diagram](#class-diagram-2)
   - [Implementation Details](#implementation-details-2)
   - [Usage](#usage-2)

## Strategy Design Pattern

### Overview

### Class Diagram

### Implementation Details

### Usage

## Strategy + Factory Design Pattern

### Overview

### Class Diagram

### Implementation Details

### Usage

## Template Method Design Pattern

### Overview
The Template Method pattern defines the skeleton of an algorithm in a method, deferring some steps to subclasses. It lets subclasses redefine certain steps of an algorithm without changing the algorithm's structure.

### Class Diagram
```mermaid
classDiagram
    class OrderProcessingTemplate {
        <<abstract>>
        +processOrder()
        #verifyOrder()
        #assignDeliveryAgent()
        #trackDelivery()
    }
    
    class LocalOrderProcessor {
        +verifyOrder()
        +assignDeliveryAgent()
        +trackDelivery()
    }
    
    class InternationalOrderProcessor {
        +verifyOrder()
        +assignDeliveryAgent()
        +trackDelivery()
    }
    
    OrderProcessingTemplate <|-- LocalOrderProcessor
    OrderProcessingTemplate <|-- InternationalOrderProcessor
```

### Implementation Details
- **OrderProcessingTemplate**: Abstract class that defines the template method `processOrder()` and declares abstract methods for each step.
- **LocalOrderProcessor**: Concrete implementation for processing local orders.
- **InternationalOrderProcessor**: Concrete implementation for processing international orders.

### Usage
```java
public class AmazonOrderProcessor {
    public static void main(String[] args) {
        // Process a local order
        OrderProcessingTemplate localOrder = new LocalOrderProcessor();
        System.out.println("Processing a local order:");
        localOrder.processOrder();

        // Process an international order
        OrderProcessingTemplate internationalOrder = new InternationalOrderProcessor();
        System.out.println("\nProcessing an international order:");
        internationalOrder.processOrder();
    }
}
```

For more detailed information, see the [Template DP README](./Template%20DP/README.md).
The Strategy pattern defines a family of algorithms, encapsulates each one, and makes them interchangeable. Strategy lets the algorithm vary independently from clients that use it.

### Class Diagram
```mermaid
classDiagram
    class PaymentStrategy {
        <<interface>>
        +processPayment(amount: double): void
    }
    
    class CreditCardPayment {
        +processPayment(amount: double): void
    }
    
    class PayPalPayment {
        +processPayment(amount: double): void
    }
    
    class CryptocurrencyPayment {
        +processPayment(amount: double): void
    }
    
    class PaymentProcessor {
        -paymentStrategy: PaymentStrategy
        +setPaymentStrategy(strategy: PaymentStrategy): void
        +processPayment(amount: double): void
    }
    
    PaymentStrategy <|-- CreditCardPayment
    PaymentStrategy <|-- PayPalPayment
    PaymentStrategy <|-- CryptocurrencyPayment
    PaymentProcessor o-- PaymentStrategy
```

### Implementation Details
The Strategy pattern is implemented with the following components:
- `PaymentStrategy`: Interface that defines the contract for all payment strategies
- Concrete strategies: `CreditCardPayment`, `PayPalPayment`, `CryptocurrencyPayment`
- `PaymentProcessor`: Context class that uses a strategy to process payments

### Usage
```java
PaymentProcessor processor = new PaymentProcessor();

// Set and use CreditCard payment
PaymentStrategy strategy = new CreditCardPayment();
processor.setPaymentStrategy(strategy);
processor.processPayment(100.0);

// Change to PayPal payment
strategy = new PayPalPayment();
processor.setPaymentStrategy(strategy);
processor.processPayment(50.0);
```

## Strategy + Factory Design Pattern

### Overview
This combines the Strategy pattern with the Factory pattern to create strategy objects. The Factory pattern encapsulates the object creation logic, making the client code cleaner and more maintainable.

### Class Diagram
```mermaid
classDiagram
    class PaymentStrategy {
        <<interface>>
        +processPayment(amount: double): void
    }
    
    class CreditCardPayment {
        +processPayment(amount: double): void
    }
    
    class PayPalPayment {
        +processPayment(amount: double): void
    }
    
    class CryptocurrencyPayment {
        +processPayment(amount: double): void
    }
    
    class PaymentStrategyFactory {
        +createPaymentStrategy(paymentMethod: String): PaymentStrategy
    }
    
    class PaymentProcessor {
        -paymentStrategy: PaymentStrategy
        +setPaymentStrategy(paymentMethod: String): void
        +processPayment(amount: double): void
    }
    
    PaymentStrategy <|-- CreditCardPayment
    PaymentStrategy <|-- PayPalPayment
    PaymentStrategy <|-- CryptocurrencyPayment
    PaymentProcessor o-- PaymentStrategy
    PaymentProcessor --> PaymentStrategyFactory
```

### Implementation Details
The combined pattern adds:
- `PaymentStrategyFactory`: Creates appropriate strategy instances based on input
- Modified `PaymentProcessor` that works with the factory

### Usage
```java
PaymentProcessor processor = new PaymentProcessor();

// Use factory to create and set payment strategy
processor.setPaymentStrategy("CreditCard");
processor.processPayment(100.0);

// Change payment method using factory
processor.setPaymentStrategy("PayPal");
processor.processPayment(50.0);
```

## Benefits of Using Strategy + Factory Pattern
1. **Decoupling**: Client code is decoupled from concrete strategy classes
2. **Flexibility**: Easy to add new payment methods without changing existing code
3. **Maintainability**: Object creation logic is centralized in the factory
4. **Testability**: Strategies can be easily mocked and tested in isolation

## When to Use
- When you need to select an algorithm at runtime
- When you have multiple related classes that only differ in their behavior
- When you want to avoid conditional statements for selecting algorithms
- When you want to hide the implementation details of strategies from clients
