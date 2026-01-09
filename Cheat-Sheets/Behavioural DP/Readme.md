# Behavioral Design Patterns - Interview Cheat Sheet

> A comprehensive guide to all behavioral design patterns with real-world e-commerce examples for interview preparation.


## Quick Overview

| Pattern | Intent | Key Benefit | When to Use |
|---------|--------|-------------|-------------|
| **Strategy** | Define family of algorithms, make them interchangeable | Switch algorithms at runtime | Multiple ways to do same thing |
| **Observer** | One-to-many dependency, notify all dependents | Loose coupling between objects | Event-driven systems, pub-sub |
| **Command** | Encapsulate request as object | Undo/redo, queuing, logging | Transaction systems, macro recording |
| **Chain of Responsibility** | Pass request along chain of handlers | Decouple sender from receiver | Validation pipelines, approval workflows |
| **State** | Object changes behavior when state changes | Avoid complex conditionals | Order status, game states |
| **Template Method** | Define algorithm skeleton, let subclasses override steps | Code reuse with variation | Frameworks, hooks system |
| **Iterator** | Access elements sequentially without exposing structure | Uniform traversal interface | Collections, pagination |
| **Mediator** | Centralize complex communications | Reduce coupling between components | Chat systems, UI components |
| **Memento** | Capture and restore object state | Undo/restore without breaking encapsulation | Undo functionality, snapshots |
| **Visitor** | Add operations to objects without changing them | Add new operations easily | Reporting, analysis on object structures |
| **Interpreter** | Define grammar and interpret sentences | Domain-specific languages | Search queries, rule engines |


## Detailed Pattern Reference

### 1. Strategy Pattern

**Purpose:** Define a family of algorithms, encapsulate each one, and make them interchangeable at runtime.

**E-commerce Example:**
```java
// Pricing strategy for different customer types
interface PricingStrategy {
    double calculatePrice(Order order);
}

class RegularCustomerPricing implements PricingStrategy {
    public double calculatePrice(Order order) {
        return order.getSubtotal();
    }
}

class PremiumCustomerPricing implements PricingStrategy {
    public double calculatePrice(Order order) {
        return order.getSubtotal() * 0.90; // 10% discount
    }
}

class WholesaleCustomerPricing implements PricingStrategy {
    public double calculatePrice(Order order) {
        double subtotal = order.getSubtotal();
        if (order.getItemCount() > 100) {
            return subtotal * 0.70; // 30% bulk discount
        }
        return subtotal * 0.80; // 20% discount
    }
}

class BlackFridayPricing implements PricingStrategy {
    public double calculatePrice(Order order) {
        return order.getSubtotal() * 0.50; // 50% off everything
    }
}

// Context
class ShoppingCart {
    private List<Item> items;
    private PricingStrategy pricingStrategy;
    
    public void setPricingStrategy(PricingStrategy strategy) {
        this.pricingStrategy = strategy;
    }
    
    public double checkout() {
        Order order = new Order(items);
        return pricingStrategy.calculatePrice(order);
    }
}

// Usage
ShoppingCart cart = new ShoppingCart();
cart.addItem(laptop);
cart.addItem(mouse);

// Change strategy based on customer type
if (customer.isPremium()) {
    cart.setPricingStrategy(new PremiumCustomerPricing());
} else if (isBlackFriday()) {
    cart.setPricingStrategy(new BlackFridayPricing());
} else {
    cart.setPricingStrategy(new RegularCustomerPricing());
}

double total = cart.checkout();
```

| Aspect | Details |
|--------|---------|
| **Real-world Use Cases** | - Discount calculation (member, seasonal, bulk, coupon)<br>- Shipping cost calculation (standard, express, international)<br>- Payment processing (credit card, PayPal, crypto, buy-now-pay-later)<br>- Product sorting (price, rating, newest, bestselling)<br>- Tax calculation (different countries/states) |
| **Why This Pattern?** | - Algorithm selection happens at runtime based on customer/context<br>- Easy to add new pricing strategies without changing existing code<br>- Avoids massive if-else or switch statements<br>- Each strategy is independently testable |
| **Why NOT Others?** | - State: Strategies don't transition between each other<br>- Command: Strategy is about HOW to do something, not encapsulating requests<br>- Template Method: Strategies are completely different, not variations of same algorithm |
| **Interview Pitfalls** | - Strategy changes behavior, State changes based on internal state<br>- Client must know about different strategies<br>- Can increase number of objects in application |
| **Pros** | ✓ Open/Closed Principle<br>✓ Runtime algorithm switching<br>✓ Eliminates conditionals |
| **Cons** | ✗ Client must be aware of strategies<br>✗ Increases number of objects<br>✗ Overkill for simple cases |

### 2. Observer Pattern

**Purpose:** Define one-to-many dependency so when one object changes state, all dependents are notified automatically.

**E-commerce Example:**
```java
// Product availability notification system
interface Observer {
    void update(Product product);
}

class EmailNotificationObserver implements Observer {
    private String email;
    
    public void update(Product product) {
        sendEmail(email, "Product " + product.getName() + " is back in stock!");
    }
}

class SMSNotificationObserver implements Observer {
    private String phoneNumber;
    
    public void update(Product product) {
        sendSMS(phoneNumber, product.getName() + " available now!");
    }
}

class PushNotificationObserver implements Observer {
    private String deviceToken;
    
    public void update(Product product) {
        sendPushNotification(deviceToken, "Your watched item is available!");
    }
}

class InventoryAnalyticsObserver implements Observer {
    public void update(Product product) {
        logRestockEvent(product);
        updateDashboard(product);
    }
}

// Subject
class Product {
    private String id;
    private String name;
    private int stockQuantity;
    private List<Observer> observers = new ArrayList<>();
    
    public void attach(Observer observer) {
        observers.add(observer);
    }
    
    public void detach(Observer observer) {
        observers.remove(observer);
    }
    
    public void setStock(int quantity) {
        boolean wasOutOfStock = this.stockQuantity == 0;
        this.stockQuantity = quantity;
        
        if (wasOutOfStock && quantity > 0) {
            notifyObservers();
        }
    }
    
    private void notifyObservers() {
        for (Observer observer : observers) {
            observer.update(this);
        }
    }
}

// Usage
Product iPhone = new Product("iPhone 15 Pro");

// Users subscribe to notifications
iPhone.attach(new EmailNotificationObserver("user@email.com"));
iPhone.attach(new SMSNotificationObserver("+1234567890"));
iPhone.attach(new PushNotificationObserver("device_token_123"));
iPhone.attach(new InventoryAnalyticsObserver());

// When restocked, all observers notified automatically
iPhone.setStock(50); // Triggers notifications to all subscribers
```

| Aspect | Details |
|--------|---------|
| **Real-world Use Cases** | - Stock availability alerts (notify users when back in stock)<br>- Order status updates (email, SMS, push when order ships)<br>- Price drop alerts (notify wishlist users)<br>- Flash sale notifications<br>- Inventory level monitoring (warehouse, analytics, purchasing) |
| **Why This Pattern?** | - Unknown number of subscribers at compile time<br>- Subscribers can be added/removed dynamically<br>- Subject doesn't need to know concrete observer types<br>- Loose coupling between event source and handlers |
| **Why NOT Others?** | - Mediator: Observer is one-to-many, Mediator centralizes many-to-many<br>- Event Bus: Observer is simpler for direct object relationships<br>- Strategy: Observer is about notification, not algorithm selection |
| **Interview Pitfalls** | - Push vs Pull model (push sends data, pull observers fetch data)<br>- Memory leaks if observers not detached properly<br>- Order of notification is not guaranteed<br>- Different from Pub-Sub (Observer has direct references) |
| **Pros** | ✓ Loose coupling<br>✓ Dynamic relationships<br>✓ Broadcast communication |
| **Cons** | ✗ Memory leaks if not detached<br>✗ Unexpected updates<br>✗ No control over notification order |


### 3. Command Pattern

**Purpose:** Encapsulate a request as an object, allowing parameterization, queuing, logging, and undo operations.

**E-commerce Example:**
```java
// Order processing with undo capability
interface Command {
    void execute();
    void undo();
}

class AddItemCommand implements Command {
    private ShoppingCart cart;
    private Product product;
    private int quantity;
    
    public AddItemCommand(ShoppingCart cart, Product product, int quantity) {
        this.cart = cart;
        this.product = product;
        this.quantity = quantity;
    }
    
    public void execute() {
        cart.addItem(product, quantity);
    }
    
    public void undo() {
        cart.removeItem(product, quantity);
    }
}

class ApplyCouponCommand implements Command {
    private Order order;
    private Coupon coupon;
    private double previousDiscount;
    
    public void execute() {
        previousDiscount = order.getDiscount();
        order.applyCoupon(coupon);
    }
    
    public void undo() {
        order.setDiscount(previousDiscount);
    }
}

class PlaceOrderCommand implements Command {
    private Order order;
    private PaymentService paymentService;
    private String transactionId;
    
    public void execute() {
        transactionId = paymentService.charge(order);
        order.setStatus(OrderStatus.CONFIRMED);
    }
    
    public void undo() {
        paymentService.refund(transactionId);
        order.setStatus(OrderStatus.CANCELLED);
    }
}

// Invoker with undo/redo support
class OrderManager {
    private Stack<Command> executedCommands = new Stack<>();
    private Stack<Command> undoneCommands = new Stack<>();
    
    public void executeCommand(Command command) {
        command.execute();
        executedCommands.push(command);
        undoneCommands.clear(); // Clear redo history
    }
    
    public void undo() {
        if (!executedCommands.isEmpty()) {
            Command command = executedCommands.pop();
            command.undo();
            undoneCommands.push(command);
        }
    }
    
    public void redo() {
        if (!undoneCommands.isEmpty()) {
            Command command = undoneCommands.pop();
            command.execute();
            executedCommands.push(command);
        }
    }
}

// Usage
OrderManager manager = new OrderManager();

Command addLaptop = new AddItemCommand(cart, laptop, 1);
Command addMouse = new AddItemCommand(cart, mouse, 2);
Command applyCoupon = new ApplyCouponCommand(order, coupon);

manager.executeCommand(addLaptop);
manager.executeCommand(addMouse);
manager.executeCommand(applyCoupon);

// Oops, didn't mean to add mouse
manager.undo(); // Removes mouse

// Actually, I want it
manager.redo(); // Adds mouse back
```

| Aspect | Details |
|--------|---------|
| **Real-world Use Cases** | - Shopping cart operations (add, remove, update with undo/redo)<br>- Transaction processing (execute, rollback)<br>- Macro recording (record user actions, replay later)<br>- Job queues (background tasks: email, image processing)<br>- Audit logging (track all actions performed) |
| **Why This Pattern?** | - Need undo/redo functionality<br>- Queue operations for later execution<br>- Log/audit all requests<br>- Implement transactional behavior<br>- Decouple invoker from receiver |
| **Why NOT Others?** | - Strategy: Command encapsulates requests, Strategy encapsulates algorithms<br>- Memento: Command tracks actions, Memento tracks state<br>- Chain of Responsibility: Command doesn't pass along chain |
| **Interview Pitfalls** | - Command stores all parameters needed for execution<br>- Receiver is the actual object performing the work<br>- Invoker doesn't know implementation details<br>- Can be combined with Memento for stateful undo |
| **Pros** | ✓ Undo/Redo support<br>✓ Deferred execution<br>✓ Composable commands |
| **Cons** | ✗ Increased number of classes<br>✗ Commands can become complex<br>✗ Memory overhead for history |


### 4. Chain of Responsibility Pattern

**Purpose:** Pass requests along a chain of handlers until one handles it, avoiding coupling sender to receiver.

**E-commerce Example:**
```java
// Order validation chain
abstract class OrderValidator {
    protected OrderValidator next;
    
    public void setNext(OrderValidator next) {
        this.next = next;
    }
    
    public abstract void validate(Order order) throws ValidationException;
    
    protected void validateNext(Order order) throws ValidationException {
        if (next != null) {
            next.validate(order);
        }
    }
}

class StockAvailabilityValidator extends OrderValidator {
    public void validate(Order order) throws ValidationException {
        for (OrderItem item : order.getItems()) {
            if (item.getQuantity() > item.getProduct().getStockQuantity()) {
                throw new ValidationException("Insufficient stock for " + item.getProduct().getName());
            }
        }
        validateNext(order);
    }
}

class PaymentMethodValidator extends OrderValidator {
    public void validate(Order order) throws ValidationException {
        if (order.getPaymentMethod() == null) {
            throw new ValidationException("Payment method not selected");
        }
        
        if (order.getPaymentMethod().isExpired()) {
            throw new ValidationException("Payment method expired");
        }
        
        validateNext(order);
    }
}

class ShippingAddressValidator extends OrderValidator {
    public void validate(Order order) throws ValidationException {
        if (order.getShippingAddress() == null) {
            throw new ValidationException("Shipping address required");
        }
        
        if (!order.getShippingAddress().isDeliverable()) {
            throw new ValidationException("Cannot deliver to this address");
        }
        
        validateNext(order);
    }
}

class FraudDetectionValidator extends OrderValidator {
    private FraudDetectionService fraudService;
    
    public void validate(Order order) throws ValidationException {
        FraudScore score = fraudService.analyzeOrder(order);
        
        if (score.isHighRisk()) {
            throw new ValidationException("Order flagged for fraud review");
        }
        
        validateNext(order);
    }
}

class MinimumOrderValueValidator extends OrderValidator {
    private double minimumValue = 10.0;
    
    public void validate(Order order) throws ValidationException {
        if (order.getTotal() < minimumValue) {
            throw new ValidationException("Order total must be at least $" + minimumValue);
        }
        
        validateNext(order);
    }
}

// Setup chain
class OrderValidationChain {
    public static OrderValidator createChain() {
        OrderValidator stockValidator = new StockAvailabilityValidator();
        OrderValidator paymentValidator = new PaymentMethodValidator();
        OrderValidator addressValidator = new ShippingAddressValidator();
        OrderValidator fraudValidator = new FraudDetectionValidator();
        OrderValidator minValueValidator = new MinimumOrderValueValidator();
        
        stockValidator.setNext(paymentValidator);
        paymentValidator.setNext(addressValidator);
        addressValidator.setNext(fraudValidator);
        fraudValidator.setNext(minValueValidator);
        
        return stockValidator; // Return first validator
    }
}

// Usage
OrderValidator validationChain = OrderValidationChain.createChain();

try {
    validationChain.validate(order);
    order.process(); // All validations passed
} catch (ValidationException e) {
    showError(e.getMessage());
}
```

| Aspect | Details |
|--------|---------|
| **Real-world Use Cases** | - Order validation pipeline (stock, payment, address, fraud)<br>- Discount/promotion application (try coupons in priority order)<br>- Request authentication/authorization (session → OAuth → API key)<br>- Customer support escalation (L1 → L2 → L3 → Manager)<br>- Logging levels (DEBUG → INFO → WARN → ERROR) |
| **Why This Pattern?** | - Multiple handlers might process request<br>- Handler determined at runtime<br>- Set of handlers changes dynamically<br>- Avoid coupling sender to specific receiver<br>- Handlers should be tried in specific order |
| **Why NOT Others?** | - Decorator: Adds responsibilities, doesn't stop at first handler<br>- Strategy: Client chooses algorithm, Chain determines at runtime<br>- Command: Encapsulates request, doesn't pass along chain |
| **Interview Pitfalls** | - Request might not be handled (need default handler)<br>- Order matters (most specific to least specific)<br>- Each handler decides whether to pass along<br>- Can use early termination or process-and-pass |
| **Pros** | ✓ Decouples sender/receiver<br>✓ Flexible chain configuration<br>✓ Single Responsibility |
| **Cons** | ✗ No guarantee of handling<br>✗ Hard to debug long chains<br>✗ Performance impact |



### 5. State Pattern

**Purpose:** Allow an object to alter its behavior when its internal state changes, appearing to change its class.

**E-commerce Example:**
```java
// Order state management
interface OrderState {
    void next(Order order);
    void prev(Order order);
    void printStatus();
}

class PendingState implements OrderState {
    public void next(Order order) {
        order.setState(new ConfirmedState());
    }
    
    public void prev(Order order) {
        System.out.println("Order is in initial state");
    }
    
    public void printStatus() {
        System.out.println("Order pending - awaiting payment confirmation");
    }
}

class ConfirmedState implements OrderState {
    public void next(Order order) {
        order.setState(new ProcessingState());
    }
    
    public void prev(Order order) {
        order.setState(new PendingState());
    }
    
    public void printStatus() {
        System.out.println("Order confirmed - payment received");
    }
}

class ProcessingState implements OrderState {
    public void next(Order order) {
        order.setState(new ShippedState());
    }
    
    public void prev(Order order) {
        order.setState(new ConfirmedState());
    }
    
    public void printStatus() {
        System.out.println("Order processing - preparing for shipment");
    }
}

class ShippedState implements OrderState {
    public void next(Order order) {
        order.setState(new DeliveredState());
    }
    
    public void prev(Order order) {
        order.setState(new ProcessingState());
    }
    
    public void printStatus() {
        System.out.println("Order shipped - in transit");
    }
}

class DeliveredState implements OrderState {
    public void next(Order order) {
        System.out.println("Order already delivered");
    }
    
    public void prev(Order order) {
        order.setState(new ShippedState());
    }
    
    public void printStatus() {
        System.out.println("Order delivered - thank you!");
    }
}

// Context
class Order {
    private OrderState state;
    
    public Order() {
        state = new PendingState();
    }
    
    public void setState(OrderState state) {
        this.state = state;
    }
    
    public void nextState() {
        state.next(this);
    }
    
    public void previousState() {
        state.prev(this);
    }
    
    public void printStatus() {
        state.printStatus();
    }
}

// Usage
Order order = new Order();
order.printStatus(); // Pending

order.nextState(); // → Confirmed
order.printStatus();

order.nextState(); // → Processing
order.nextState(); // → Shipped
order.nextState(); // → Delivered
order.printStatus();
```

| Aspect | Details |
|--------|---------|
| **Real-world Use Cases** | - Order lifecycle (pending → confirmed → processing → shipped → delivered)<br>- Payment processing (initiated → authorized → captured → settled)<br>- Product listing (draft → pending review → published → archived)<br>- Return request (requested → approved → picked up → refunded)<br>- User account (active → suspended → deactivated → deleted) |
| **Why This Pattern?** | - Object has distinct states with different behaviors<br>- Complex state transitions with rules<br>- Avoid massive switch/if-else statements<br>- State-specific behavior clearly separated<br>- States know their valid transitions |
| **Why NOT Others?** | - Strategy: State transitions automatically, Strategy chosen by client<br>- Chain of Responsibility: State is about transitions, not request passing<br>- If-else: State pattern eliminates conditionals, centralizes state logic |
| **Interview Pitfalls** | - State objects can be singletons (if no state-specific data)<br>- Context delegates to current state<br>- States know about each other (transitions)<br>- Similar to Strategy but with transitions |
| **Pros** | ✓ Eliminates complex conditionals<br>✓ Single Responsibility<br>✓ Easy to add states |
| **Cons** | ✗ Overkill for few states<br>✗ States know about transitions<br>✗ Increases number of classes |


### 6. Template Method Pattern

**Purpose:** Define the skeleton of an algorithm, letting subclasses override specific steps without changing structure.

**E-commerce Example:**
```java
// Order processing template
abstract class OrderProcessor {
    
    // Template method - defines the algorithm structure
    public final void processOrder(Order order) {
        validateOrder(order);
        
        if (checkInventory(order)) {
            reserveInventory(order);
            
            if (processPayment(order)) {
                confirmOrder(order);
                sendConfirmation(order);
                updateAnalytics(order);
            } else {
                releaseInventory(order);
                notifyPaymentFailure(order);
            }
        } else {
            notifyOutOfStock(order);
        }
    }
    
    // Abstract methods - must be implemented
    protected abstract boolean processPayment(Order order);
    protected abstract void sendConfirmation(Order order);
    
    // Hook methods - optional override
    protected void updateAnalytics(Order order) {
        // Default implementation: basic analytics
        System.out.println("Logging order to analytics");
    }
    
    // Concrete methods - same for all
    private void validateOrder(Order order) {
        if (order == null || order.getItems().isEmpty()) {
            throw new IllegalArgumentException("Invalid order");
        }
    }
    
    private boolean checkInventory(Order order) {
        return order.getItems().stream()
            .allMatch(item -> item.getProduct().isInStock(item.getQuantity()));
    }
    
    private void reserveInventory(Order order) {
        order.getItems().forEach(item -> 
            item.getProduct().reserve(item.getQuantity())
        );
    }
    
    private void confirmOrder(Order order) {
        order.setStatus(OrderStatus.CONFIRMED);
        order.setConfirmationTime(LocalDateTime.now());
    }
    
    private void releaseInventory(Order order) {
        order.getItems().forEach(item ->
            item.getProduct().release(item.getQuantity())
        );
    }
    
    private void notifyPaymentFailure(Order order) {
        System.out.println("Payment failed for order " + order.getId());
    }
    
    private void notifyOutOfStock(Order order) {
        System.out.println("Items out of stock for order " + order.getId());
    }
}

// Regular order processing
class RegularOrderProcessor extends OrderProcessor {
    private PaymentGateway paymentGateway;
    private EmailService emailService;
    
    protected boolean processPayment(Order order) {
        return paymentGateway.charge(
            order.getPaymentMethod(),
            order.getTotal()
        );
    }
    
    protected void sendConfirmation(Order order) {
        emailService.send(
            order.getCustomer().getEmail(),
            "Order Confirmation",
            generateEmailBody(order)
        );
    }
}

// Wholesale order with different confirmation and analytics
class WholesaleOrderProcessor extends OrderProcessor {
    private PaymentGateway paymentGateway;
    private SMSService smsService;
    private SalesforceAPI salesforce;
    
    protected boolean processPayment(Order order) {
        // Different payment terms for wholesale
        return paymentGateway.authorizeNet30(
            order.getPaymentMethod(),
            order.getTotal()
        );
    }
    
    protected void sendConfirmation(Order order) {
        // Use SMS instead of email
        smsService.send(
            order.getCustomer().getPhone(),
            "Wholesale order " + order.getId() + " confirmed"
        );
    }
    
    @Override
    protected void updateAnalytics(Order order) {
        // Custom analytics for wholesale
        super.updateAnalytics(order); // Call default
        salesforce.createOpportunity(order);
        salesforce.updateAccountValue(order.getCustomer());
    }
}

// Usage
OrderProcessor regularProcessor = new RegularOrderProcessor();
regularProcessor.processOrder(regularOrder);

OrderProcessor wholesaleProcessor = new WholesaleOrderProcessor();
wholesaleProcessor.processOrder(wholesaleOrder);
```

| Aspect | Details |
|--------|---------|
| **Real-world Use Cases** | - Order processing workflows (validate → charge → confirm → notify)<br>- Data import pipelines (parse → validate → transform → load)<br>- Report generation (fetch data → format → add headers → export)<br>- Authentication flows (verify credentials → check permissions → create session)<br>- Product publishing (validate → process images → update SEO → go live) |
| **Why This Pattern?** | - Common algorithm structure with variant steps<br>- Framework defines control flow, apps customize steps<br>- Code reuse while allowing customization<br>- Hollywood Principle: "Don't call us, we'll call you" |
| **Why NOT Others?** | - Strategy: Swaps entire algorithm, Template Method swaps steps<br>- State: About transitions, not algorithm structure<br>- Factory Method: Creates objects, doesn't define algorithm flow |
| **Interview Pitfalls** | - Template method should be final (prevent override)<br>- Hook methods are optional overrides<br>- Abstract methods must be implemented<br>- Inversion of control pattern |
| **Pros** | ✓ Code reuse<br>✓ Control over extension points<br>✓ Common structure enforced |
| **Cons** | ✗ Inheritance-based (less flexible)<br>✗ Liskov Substitution violations<br>✗ Limited by class structure |


### 7. Iterator Pattern

**Purpose:** Provide a way to access elements of a collection sequentially without exposing its underlying representation.

**E-commerce Example:**
```java
// Product collection with multiple iteration strategies
interface Iterator<T> {
    boolean hasNext();
    T next();
}

interface Iterable<T> {
    Iterator<T> createIterator();
}

class Product {
    private String id;
    private String name;
    private double price;
    private String category;
    private int popularity;
    // getters...
}

class ProductCollection implements Iterable<Product> {
    private List<Product> products = new ArrayList<>();
    
    public void addProduct(Product product) {
        products.add(product);
    }
    
    // Different iterators for different traversal strategies
    public Iterator<Product> createIterator() {
        return new SequentialIterator();
    }
    
    public Iterator<Product> createPriceOrderIterator() {
        return new PriceOrderIterator();
    }
    
    public Iterator<Product> createCategoryIterator(String category) {
        return new CategoryFilterIterator(category);
    }
    
    // Sequential iterator
    private class SequentialIterator implements Iterator<Product> {
        private int index = 0;
        
        public boolean hasNext() {
            return index < products.size();
        }
        
        public Product next() {
            return products.get(index++);
        }
    }
    
    // Price-sorted iterator
    private class PriceOrderIterator implements Iterator<Product> {
        private List<Product> sortedProducts;
        private int index = 0;
        
        public PriceOrderIterator() {
            sortedProducts = new ArrayList<>(products);
            sortedProducts.sort(Comparator.comparing(Product::getPrice));
        }
        
        public boolean hasNext() {
            return index < sortedProducts.size();
        }
        
        public Product next() {
            return sortedProducts.get(index++);
        }
    }
    
    // Category filter iterator
    private class CategoryFilterIterator implements Iterator<Product> {
        private String category;
        private int index = 0;
        
        public CategoryFilterIterator(String category) {
            this.category = category;
        }
        
        public boolean hasNext() {
            while (index < products.size()) {
                if (products.get(index).getCategory().equals(category)) {
                    return true;
                }
                index++;
            }
            return false;
        }
        
        public Product next() {
            return products.get(index++);
        }
    }
}

// Usage
ProductCollection catalog = new ProductCollection();
catalog.addProduct(laptop);
catalog.addProduct(mouse);
catalog.addProduct(keyboard);

// Iterate normally
Iterator<Product> iterator = catalog.createIterator();
while (iterator.hasNext()) {
    Product p = iterator.next();
    System.out.println(p.getName());
}

// Iterate by price
Iterator<Product> priceIterator = catalog.createPriceOrderIterator();
while (priceIterator.hasNext()) {
    Product p = priceIterator.next();
    System.out.println(p.getName() + " - $" + p.getPrice());
}

// Iterate by category
Iterator<Product> electronicsIterator = catalog.createCategoryIterator("Electronics");
while (electronicsIterator.hasNext()) {
    System.out.println(electronicsIterator.next().getName());
}
```

| Aspect | Details |
|--------|---------|
| **Real-world Use Cases** | - Product catalog pagination (lazy loading page by page)<br>- Order history browsing (chronological, filtered)<br>- Search results traversal<br>- Shopping cart items iteration<br>- Breadth-first/depth-first category navigation |
| **Why This Pattern?** | - Hide internal collection structure<br>- Multiple simultaneous traversals needed<br>- Uniform interface for different collections<br>- Support different iteration strategies |
| **Why NOT Others?** | - Visitor: Performs operations on elements, Iterator accesses them<br>- Composite: Tree structure traversal, Iterator is linear<br>- Built-in iterators: Custom iteration logic needed |
| **Interview Pitfalls** | - Iterator maintains traversal state<br>- Collection shouldn't change during iteration<br>- Multiple iterators can exist simultaneously<br>- Java/C# have built-in Iterator support |
| **Pros** | ✓ Single Responsibility<br>✓ Uniform interface<br>✓ Parallel iteration |
| **Cons** | ✗ Overkill for simple collections<br>✗ Less efficient than direct access<br>✗ Complexity overhead |



### 8. Mediator Pattern

**Purpose:** Define an object that encapsulates how a set of objects interact, promoting loose coupling by preventing direct references.

**E-commerce Example:**
```java
// Checkout component mediator
interface CheckoutMediator {
    void notify(Component sender, String event);
}

abstract class Component {
    protected CheckoutMediator mediator;
    
    public void setMediator(CheckoutMediator mediator) {
        this.mediator = mediator;
    }
}

class ShippingAddressForm extends Component {
    private Address address;
    
    public void updateAddress(Address newAddress) {
        this.address = newAddress;
        mediator.notify(this, "address_changed");
    }
    
    public Address getAddress() {
        return address;
    }
}

class ShippingMethodSelector extends Component {
    private ShippingMethod selectedMethod;
    private List<ShippingMethod> availableMethods;
    
    public void updateAvailableMethods(List<ShippingMethod> methods) {
        this.availableMethods = methods;
        if (!methods.contains(selectedMethod)) {
            selectedMethod = methods.get(0); // Reset to first available
            mediator.notify(this, "shipping_method_changed");
        }
    }
    
    public void selectMethod(ShippingMethod method) {
        this.selectedMethod = method;
        mediator.notify(this, "shipping_method_changed");
    }
    
    public ShippingMethod getSelectedMethod() {
        return selectedMethod;
    }
}

class PaymentForm extends Component {
    private PaymentMethod paymentMethod;
    
    public void selectPayment(PaymentMethod method) {
        this.paymentMethod = method;
        mediator.notify(this, "payment_selected");
    }
    
    public boolean validate() {
        return paymentMethod != null && paymentMethod.isValid();
    }
}

class OrderSummary extends Component {
    private double subtotal;
    private double shippingCost;
    private double tax;
    private double total;
    
    public void recalculate(double subtotal, double shipping, double tax) {
        this.subtotal = subtotal;
        this.shippingCost = shipping;
        this.tax = tax;
        this.total = subtotal + shipping + tax;
        mediator.notify(this, "total_updated");
    }
    
    public double getTotal() {
        return total;
    }
}

class CheckoutButton extends Component {
    private boolean enabled = false;
    
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
    
    public void click() {
        if (enabled) {
            mediator.notify(this, "checkout_clicked");
        }
    }
}

// Concrete Mediator
class CheckoutPageMediator implements CheckoutMediator {
    private ShippingAddressForm addressForm;
    private ShippingMethodSelector shippingSelector;
    private PaymentForm paymentForm;
    private OrderSummary orderSummary;
    private CheckoutButton checkoutButton;
    
    private ShippingService shippingService;
    private TaxCalculator taxCalculator;
    
    public void setComponents(ShippingAddressForm address,
                             ShippingMethodSelector shipping,
                             PaymentForm payment,
                             OrderSummary summary,
                             CheckoutButton button) {
        this.addressForm = address;
        this.shippingSelector = shipping;
        this.paymentForm = payment;
        this.orderSummary = summary;
        this.checkoutButton = button;
        
        // Set mediator for all components
        address.setMediator(this);
        shipping.setMediator(this);
        payment.setMediator(this);
        summary.setMediator(this);
        button.setMediator(this);
    }
    
    public void notify(Component sender, String event) {
        switch (event) {
            case "address_changed":
                handleAddressChange();
                break;
            case "shipping_method_changed":
                handleShippingMethodChange();
                break;
            case "payment_selected":
                handlePaymentSelected();
                break;
            case "total_updated":
                validateCheckout();
                break;
            case "checkout_clicked":
                processCheckout();
                break;
        }
    }
    
    private void handleAddressChange() {
        // Update available shipping methods based on address
        Address address = addressForm.getAddress();
        List<ShippingMethod> methods = shippingService.getAvailableMethods(address);
        shippingSelector.updateAvailableMethods(methods);
        
        // Recalculate totals
        recalculateOrder();
    }
    
    private void handleShippingMethodChange() {
        recalculateOrder();
    }
    
    private void handlePaymentSelected() {
        validateCheckout();
    }
    
    private void recalculateOrder() {
        Address address = addressForm.getAddress();
        ShippingMethod method = shippingSelector.getSelectedMethod();
        
        double shipping = shippingService.calculateCost(method, address);
        double tax = taxCalculator.calculate(address, orderSummary.getTotal());
        
        orderSummary.recalculate(500.0, shipping, tax); // 500 is subtotal
    }
    
    private void validateCheckout() {
        boolean isValid = addressForm.getAddress() != null &&
                         shippingSelector.getSelectedMethod() != null &&
                         paymentForm.validate();
        
        checkoutButton.setEnabled(isValid);
    }
    
    private void processCheckout() {
        // Process the order
        System.out.println("Processing checkout...");
    }
}

// Usage
CheckoutPageMediator mediator = new CheckoutPageMediator();

ShippingAddressForm addressForm = new ShippingAddressForm();
ShippingMethodSelector shippingSelector = new ShippingMethodSelector();
PaymentForm paymentForm = new PaymentForm();
OrderSummary orderSummary = new OrderSummary();
CheckoutButton checkoutButton = new CheckoutButton();

mediator.setComponents(addressForm, shippingSelector, paymentForm, 
                       orderSummary, checkoutButton);

// When user updates address, everything else updates automatically
addressForm.updateAddress(new Address("123 Main St", "New York", "NY"));
// → Shipping methods update → Costs recalculate → Button validates
```

| Aspect | Details |
|--------|---------|
| **Real-world Use Cases** | - Checkout page (address, shipping, payment, total interact)<br>- Product filter system (category, price, brand filters affect each other)<br>- Chat room (users don't message directly, through room)<br>- Drag-and-drop UI builder (components notify changes)<br>- Auction bidding (bidders communicate through auction) |
| **Why This Pattern?** | - Complex interactions between many components<br>- Components shouldn't know about each other<br>- Centralized control logic needed<br>- Avoid spaghetti of cross-references |
| **Why NOT Others?** | - Observer: One-to-many, Mediator is many-to-many coordination<br>- Facade: Simplifies interface, doesn't coordinate interactions<br>- Event Bus: Mediator provides more structured coordination |
| **Interview Pitfalls** | - Mediator can become "God object" if too complex<br>- Different from Observer: coordinates bidirectional communication<br>- Components only know about mediator, not each other |
| **Pros** | ✓ Reduces coupling<br>✓ Centralizes control<br>✓ Easy to understand interactions |
| **Cons** | ✗ Mediator can become complex<br>✗ Single point of failure<br>✗ Can violate Single Responsibility |


### 9. Memento Pattern

**Purpose:** Capture and externalize an object's internal state without violating encapsulation, allowing restoration later.

**E-commerce Example:**
```java
// Shopping cart undo/restore
class CartMemento {
    private final List<CartItem> items;
    private final String couponCode;
    private final double discount;
    private final LocalDateTime timestamp;
    
    public CartMemento(List<CartItem> items, String couponCode, double discount) {
        // Deep copy to prevent external modification
        this.items = new ArrayList<>();
        for (CartItem item : items) {
            this.items.add(new CartItem(item));
        }
        this.couponCode = couponCode;
        this.discount = discount;
        this.timestamp = LocalDateTime.now();
    }
    
    List<CartItem> getItems() { return new ArrayList<>(items); }
    String getCouponCode() { return couponCode; }
    double getDiscount() { return discount; }
    LocalDateTime getTimestamp() { return timestamp; }
}

// Originator
class ShoppingCart {
    private List<CartItem> items = new ArrayList<>();
    private String couponCode;
    private double discount;
    
    public void addItem(Product product, int quantity) {
        items.add(new CartItem(product, quantity));
    }
    
    public void removeItem(String productId) {
        items.removeIf(item -> item.getProduct().getId().equals(productId));
    }
    
    public void applyCoupon(String code, double discount) {
        this.couponCode = code;
        this.discount = discount;
    }
    
    public void clearCoupon() {
        this.couponCode = null;
        this.discount = 0;
    }
    
    // Create memento
    public CartMemento save() {
        return new CartMemento(items, couponCode, discount);
    }
    
    // Restore from memento
    public void restore(CartMemento memento) {
        this.items = memento.getItems();
        this.couponCode = memento.getCouponCode();
        this.discount = memento.getDiscount();
    }
    
    public double getTotal() {
        double subtotal = items.stream()
            .mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity())
            .sum();
        return subtotal - (subtotal * discount);
    }
}

// Caretaker - manages memento history
class CartHistory {
    private Stack<CartMemento> history = new Stack<>();
    private Stack<CartMemento> redoStack = new Stack<>();
    private ShoppingCart cart;
    
    public CartHistory(ShoppingCart cart) {
        this.cart = cart;
    }
    
    public void save() {
        history.push(cart.save());
        redoStack.clear(); // Clear redo history on new action
    }
    
    public void undo() {
        if (history.size() > 1) {
            CartMemento current = history.pop();
            redoStack.push(current);
            cart.restore(history.peek());
        }
    }
    
    public void redo() {
        if (!redoStack.isEmpty()) {
            CartMemento memento = redoStack.pop();
            history.push(memento);
            cart.restore(memento);
        }
    }
    
    public List<String> getHistory() {
        return history.stream()
            .map(m -> m.getTimestamp() + " - " + m.getItems().size() + " items")
            .collect(Collectors.toList());
    }
}

// Usage
ShoppingCart cart = new ShoppingCart();
CartHistory history = new CartHistory(cart);

history.save(); // Save initial empty state

cart.addItem(laptop, 1);
history.save();

cart.addItem(mouse, 2);
history.save();

cart.applyCoupon("SAVE10", 0.10);
history.save();

System.out.println("Total: $" + cart.getTotal());

// Oops, remove the coupon
history.undo(); // Back to before coupon
System.out.println("Total: $" + cart.getTotal());

// Actually, I want the coupon
history.redo(); // Restore coupon
System.out.println("Total: $" + cart.getTotal());
```

| Aspect | Details |
|--------|---------|
| **Real-world Use Cases** | - Shopping cart undo/redo<br>- Form autosave and restore (draft orders)<br>- Game save points<br>- Transaction rollback<br>- Version history (product listings, prices) |
| **Why This Pattern?** | - Need to save/restore state without exposing internals<br>- Undo/redo functionality required<br>- Snapshot for later restoration<br>- Maintain encapsulation of state |
| **Why NOT Others?** | - Command: Stores actions, Memento stores state<br>- Prototype: Creates new objects, doesn't restore state<br>- Serialization: Memento is more controlled and encapsulated |
| **Interview Pitfalls** | - Memento should be immutable<br>- Caretaker manages mementos, doesn't know internal state<br>- Memory intensive for large states<br>- Can be combined with Command for undo |
| **Pros** | ✓ Preserves encapsulation<br>✓ Simplifies originator<br>✓ State recovery |
| **Cons** | ✗ Memory intensive<br>✗ Caretaker overhead<br>✗ State copying cost |


### 10. Visitor Pattern

**Purpose:** Add new operations to objects without changing their classes by using a visitor object.

**E-commerce Example:**
```java
// Product hierarchy
interface ProductElement {
    void accept(ProductVisitor visitor);
}

class Electronics implements ProductElement {
    private String name;
    private double price;
    private int warrantyMonths;
    
    public void accept(ProductVisitor visitor) {
        visitor.visit(this);
    }
    
    // Getters...
}

class Clothing implements ProductElement {
    private String name;
    private double price;
    private String size;
    private String material;
    
    public void accept(ProductVisitor visitor) {
        visitor.visit(this);
    }
    
    // Getters...
}

class Food implements ProductElement {
    private String name;
    private double price;
    private LocalDate expiryDate;
    
    public void accept(ProductVisitor visitor) {
        visitor.visit(this);
    }
    
    // Getters...
}

// Visitor interface
interface ProductVisitor {
    void visit(Electronics electronics);
    void visit(Clothing clothing);
    void visit(Food food);
}

// Concrete Visitor: Price calculation with category-specific logic
class PriceCalculatorVisitor implements ProductVisitor {
    private double totalPrice = 0;
    
    public void visit(Electronics electronics) {
        double price = electronics.getPrice();
        // Electronics: add warranty cost
        double warrantyCost = electronics.getWarrantyMonths() * 5;
        totalPrice += price + warrantyCost;
    }
    
    public void visit(Clothing clothing) {
        double price = clothing.getPrice();
        // Clothing: premium materials cost more
        if (clothing.getMaterial().equals("Silk") || 
            clothing.getMaterial().equals("Cashmere")) {
            totalPrice += price * 1.2; // 20% premium
        } else {
            totalPrice += price;
        }
    }
    
    public void visit(Food food) {
        double price = food.getPrice();
        // Food: discount if near expiry
        long daysUntilExpiry = ChronoUnit.DAYS.between(
            LocalDate.now(), 
            food.getExpiryDate()
        );
        
        if (daysUntilExpiry < 3) {
            totalPrice += price * 0.5; // 50% off
        } else if (daysUntilExpiry < 7) {
            totalPrice += price * 0.8; // 20% off
        } else {
            totalPrice += price;
        }
    }
    
    public double getTotalPrice() {
        return totalPrice;
    }
}

// Concrete Visitor: Export to different formats
class ProductExportVisitor implements ProductVisitor {
    private StringBuilder xml = new StringBuilder();
    
    public void visit(Electronics electronics) {
        xml.append("<electronics>")
           .append("<name>").append(electronics.getName()).append("</name>")
           .append("<price>").append(electronics.getPrice()).append("</price>")
           .append("<warranty>").append(electronics.getWarrantyMonths()).append("</warranty>")
           .append("</electronics>");
    }
    
    public void visit(Clothing clothing) {
        xml.append("<clothing>")
           .append("<name>").append(clothing.getName()).append("</name>")
           .append("<price>").append(clothing.getPrice()).append("</price>")
           .append("<size>").append(clothing.getSize()).append("</size>")
           .append("<material>").append(clothing.getMaterial()).append("</material>")
           .append("</clothing>");
    }
    
    public void visit(Food food) {
        xml.append("<food>")
           .append("<name>").append(food.getName()).append("</name>")
           .append("<price>").append(food.getPrice()).append("</price>")
           .append("<expiry>").append(food.getExpiryDate()).append("</expiry>")
           .append("</food>");
    }
    
    public String getXML() {
        return xml.toString();
    }
}

// Concrete Visitor: Shipping cost calculation
class ShippingCostVisitor implements ProductVisitor {
    private double totalShippingCost = 0;
    
    public void visit(Electronics electronics) {
        totalShippingCost += 15.0; // Fragile, special handling
    }
    
    public void visit(Clothing clothing) {
        totalShippingCost += 5.0; // Light, easy to ship
    }
    
    public void visit(Food food) {
        totalShippingCost += 10.0; // Perishable, expedited shipping
    }
    
    public double getTotalShippingCost() {
        return totalShippingCost;
    }
}

// Usage
List<ProductElement> cart = Arrays.asList(
    new Electronics("Laptop", 1000, 24),
    new Clothing("Silk Shirt", 80, "M", "Silk"),
    new Food("Milk", 4, LocalDate.now().plusDays(2))
);

// Calculate total price
PriceCalculatorVisitor priceCalc = new PriceCalculatorVisitor();
for (ProductElement product : cart) {
    product.accept(priceCalc);
}
System.out.println("Total: $" + priceCalc.getTotalPrice());

// Calculate shipping
ShippingCostVisitor shippingCalc = new ShippingCostVisitor();
for (ProductElement product : cart) {
    product.accept(shippingCalc);
}
System.out.println("Shipping: $" + shippingCalc.getTotalShippingCost());

// Export to XML
ProductExportVisitor exporter = new ProductExportVisitor();
for (ProductElement product : cart) {
    product.accept(exporter);
}
System.out.println(exporter.getXML());
```

| Aspect | Details |
|--------|---------|
| **Real-world Use Cases** | - Tax calculation (different rules per product category)<br>- Export products to multiple formats (XML, JSON, CSV)<br>- Shipping cost calculation (fragile, perishable, standard)<br>- Discount application (category-specific promotions)<br>- Inventory auditing (different checks per product type) |
| **Why This Pattern?** | - Need to perform many different operations on object structure<br>- Operations change frequently, object structure is stable<br>- Avoid polluting classes with unrelated operations<br>- Keep related operations together in visitor |
| **Why NOT Others?** | - Strategy: One operation with variants, Visitor is many operations<br>- Decorator: Adds responsibilities, doesn't operate on structure<br>- If-else: Visitor eliminates type-checking conditionals |
| **Interview Pitfalls** | - Double dispatch mechanism (runtime type resolution)<br>- Adding new element types requires changing all visitors<br>- Good when operations change often, structure is stable<br>- Violates encapsulation (visitor accesses element internals) |
| **Pros** | ✓ Easy to add operations<br>✓ Related operations together<br>✓ Visits different object types |
| **Cons** | ✗ Hard to add new elements<br>✗ Breaks encapsulation<br>✗ Circular dependencies |


### 11. Interpreter Pattern

**Purpose:** Define a grammar for a language and an interpreter to interpret sentences in that language.

**E-commerce Example:**
```java
// Product search query language interpreter
// Grammar: "category:electronics AND price:<1000 OR brand:apple"

interface Expression {
    boolean interpret(Product product);
}

// Terminal expression for category
class CategoryExpression implements Expression {
    private String category;
    
    public CategoryExpression(String category) {
        this.category = category;
    }
    
    public boolean interpret(Product product) {
        return product.getCategory().equalsIgnoreCase(category);
    }
}

// Terminal expression for price comparison
class PriceExpression implements Expression {
    private String operator; // <, >, =
    private double value;
    
    public PriceExpression(String operator, double value) {
        this.operator = operator;
        this.value = value;
    }
    
    public boolean interpret(Product product) {
        double price = product.getPrice();
        switch (operator) {
            case "<": return price < value;
            case ">": return price > value;
            case "=": return price == value;
            default: return false;
        }
    }
}

// Terminal expression for brand
class BrandExpression implements Expression {
    private String brand;
    
    public BrandExpression(String brand) {
        this.brand = brand;
    }
    
    public boolean interpret(Product product) {
        return product.getBrand().equalsIgnoreCase(brand);
    }
}

// Terminal expression for in-stock
class InStockExpression implements Expression {
    public boolean interpret(Product product) {
        return product.getStockQuantity() > 0;
    }
}

// Non-terminal expression: AND
class AndExpression implements Expression {
    private Expression expr1;
    private Expression expr2;
    
    public AndExpression(Expression expr1, Expression expr2) {
        this.expr1 = expr1;
        this.expr2 = expr2;
    }
    
    public boolean interpret(Product product) {
        return expr1.interpret(product) && expr2.interpret(product);
    }
}

// Non-terminal expression: OR
class OrExpression implements Expression {
    private Expression expr1;
    private Expression expr2;
    
    public OrExpression(Expression expr1, Expression expr2) {
        this.expr1 = expr1;
        this.expr2 = expr2;
    }
    
    public boolean interpret(Product product) {
        return expr1.interpret(product) || expr2.interpret(product);
    }
}

// Non-terminal expression: NOT
class NotExpression implements Expression {
    private Expression expr;
    
    public NotExpression(Expression expr) {
        this.expr = expr;
    }
    
    public boolean interpret(Product product) {
        return !expr.interpret(product);
    }
}

// Query parser
class ProductQueryParser {
    public Expression parse(String query) {
        // Simple parser for demonstration
        // Real implementation would use proper parsing techniques
        
        // Example: "category:electronics AND price:<1000"
        if (query.contains(" AND ")) {
            String[] parts = query.split(" AND ");
            return new AndExpression(parse(parts[0]), parse(parts[1]));
        }
        
        if (query.contains(" OR ")) {
            String[] parts = query.split(" OR ");
            return new OrExpression(parse(parts[0]), parse(parts[1]));
        }
        
        if (query.startsWith("NOT ")) {
            return new NotExpression(parse(query.substring(4)));
        }
        
        if (query.startsWith("category:")) {
            return new CategoryExpression(query.substring(9));
        }
        
        if (query.startsWith("brand:")) {
            return new BrandExpression(query.substring(6));
        }
        
        if (query.startsWith("price:")) {
            String priceQuery = query.substring(6);
            char operator = priceQuery.charAt(0);
            double value = Double.parseDouble(priceQuery.substring(1));
            return new PriceExpression(String.valueOf(operator), value);
        }
        
        if (query.equals("instock")) {
            return new InStockExpression();
        }
        
        throw new IllegalArgumentException("Invalid query: " + query);
    }
}

// Usage
ProductQueryParser parser = new ProductQueryParser();

// Query: Electronics under $1000 that are in stock
String query = "category:electronics AND price:<1000 AND instock";
Expression expression = parser.parse(query);

List<Product> allProducts = productCatalog.getAll();
List<Product> results = allProducts.stream()
    .filter(expression::interpret)
    .collect(Collectors.toList());

// Query: Apple or Samsung products
query = "brand:apple OR brand:samsung";
expression = parser.parse(query);
results = allProducts.stream()
    .filter(expression::interpret)
    .collect(Collectors.toList());

// Query: Not electronics
query = "NOT category:electronics";
expression = parser.parse(query);
results = allProducts.stream()
    .filter(expression::interpret)
    .collect(Collectors.toList());
```

| Aspect | Details |
|--------|---------|
| **Real-world Use Cases** | - Advanced product search queries (filters with AND/OR/NOT)<br>- Discount rule engine ("if cart>$100 AND member=premium THEN 20% off")<br>- Promotion eligibility ("category:books OR category:electronics")<br>- Custom report filters<br>- Configuration/business rule languages |
| **Why This Pattern?** | - Need domain-specific language (DSL)<br>- Grammar is simple and doesn't change often<br>- Efficiency is not critical<br>- Users need flexible query/rule creation |
| **Why NOT Others?** | - Strategy: Interpreter defines grammar, Strategy swaps algorithms<br>- Regex/Parser: Interpreter for custom business logic<br>- If-else: Interpreter for complex, composable rules |
| **Interview Pitfalls** | - Rarely used in practice (complex to implement)<br>- Better alternatives exist (parsers, regex, rule engines)<br>- Each grammar rule is a class<br>- Recursive structure for complex expressions |
| **Pros** | ✓ Easy to change grammar<br>✓ Easy to add new rules<br>✓ Flexible expressions |
| **Cons** | ✗ Complex grammars are hard<br>✗ Many classes for rules<br>✗ Performance issues |


## Pattern Comparison Table

| Scenario | Best Pattern | Why? |
|----------|-------------|------|
| Multiple ways to calculate price | **Strategy** | Swap pricing algorithm at runtime |
| Notify users when product back in stock | **Observer** | One-to-many notification |
| Shopping cart with undo/redo | **Command** + **Memento** | Command for actions, Memento for state |
| Order validation pipeline | **Chain of Responsibility** | Multiple validators in sequence |
| Order status workflow | **State** | Different behavior per state |
| Process different order types similarly | **Template Method** | Common structure, variant steps |
| Paginate search results | **Iterator** | Sequential access without exposing structure |
| Checkout page with interacting components | **Mediator** | Centralize complex interactions |
| Save/restore shopping cart | **Memento** | Preserve and restore state |
| Different operations on product catalog | **Visitor** | Add operations without changing products |
| Advanced search query language | **Interpreter** | Parse and execute custom queries |


## Pattern Relationships

```
Observer ← → Mediator
(one-to-many vs many-to-many coordination)

Command ← → Memento
(stores actions vs stores state, often used together)

Strategy ← → State
(swap algorithm vs behavior changes with state)

Template Method ← → Strategy
(algorithm skeleton vs entire algorithm swap)

Iterator ← → Visitor
(access elements vs operate on elements)

Chain of Responsibility ← → Command
(pass request along vs encapsulate request)
```

## Interview Quick Tips

### Common Questions

**Q: Strategy vs State - what's the difference?**
- **Strategy:** Client chooses and sets the strategy explicitly
- **State:** Object changes its own state based on internal logic
- **Strategy:** Strategies are independent, don't know about each other
- **State:** States know about transitions to other states

**Q: Observer vs Mediator?**
- **Observer:** One-to-many (subject → multiple observers)
- **Mediator:** Many-to-many (components ← → mediator ← → components)
- **Observer:** Simple notification
- **Mediator:** Complex interaction coordination

**Q: Command vs Memento?**
- **Command:** Stores actions/operations (what was done)
- **Memento:** Stores state (snapshot of data)
- **Often used together:** Command for undo via re-execution, Memento for undo via state restoration

**Q: When to use Template Method vs Strategy?**
- **Template Method:** Steps of algorithm vary, overall structure same
- **Strategy:** Entire algorithm swapped
- **Template Method:** Inheritance-based (compile-time)
- **Strategy:** Composition-based (runtime)

**Q: Why is Visitor rarely used?**
- Violates encapsulation (accesses internals)
- Hard to add new element types
- Better alternatives exist (Strategy for operations, double dispatch manually)
