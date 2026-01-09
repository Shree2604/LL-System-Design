# Creational Design Patterns - Interview Cheat Sheet

> A comprehensive guide to all creational design patterns with real-world e-commerce examples for interview preparation.


## Quick Overview

| Pattern | Intent | Key Benefit | When to Use |
|---------|--------|-------------|-------------|
| **Singleton** | Ensure only one instance exists | Global access point | Database connections, logging, caching |
| **Factory Method** | Create objects without specifying exact class | Decouple creation from usage | Multiple product variants, plugin systems |
| **Abstract Factory** | Create families of related objects | Consistency across product families | UI themes, cross-platform components |
| **Builder** | Construct complex objects step-by-step | Readable construction with many parameters | Complex configurations, fluent APIs |
| **Prototype** | Clone existing objects | Avoid costly initialization | Object pooling, config templates |



## Detailed Pattern Reference

### 1. Singleton Pattern

**Purpose:** Ensure a class has only one instance and provide a global access point to it.

**E-commerce Example:**
```java
// Shopping cart session manager
public class CartSessionManager {
    private static CartSessionManager instance;
    private Map<String, ShoppingCart> activeCarts;
    
    private CartSessionManager() {
        activeCarts = new ConcurrentHashMap<>();
    }
    
    public static synchronized CartSessionManager getInstance() {
        if (instance == null) {
            instance = new CartSessionManager();
        }
        return instance;
    }
    
    public ShoppingCart getCart(String sessionId) {
        return activeCarts.computeIfAbsent(sessionId, k -> new ShoppingCart());
    }
}
```

| Aspect | Details |
|--------|---------|
| **Real-world Use Cases** | - Payment gateway connection pool<br>- Application configuration manager<br>- Logger instance<br>- Cache manager |
| **Why This Pattern?** | - Database connections are expensive to create<br>- Need consistent state across application<br>- Prevent multiple configuration instances causing conflicts |
| **Why NOT Others?** | - Factory: Creates multiple instances (wasteful for shared resources)<br>- Prototype: Cloning doesn't prevent multiple instances<br>- Builder: Focuses on construction, not instance control |
| **Interview Pitfalls** | - Thread safety: Use double-checked locking or enum<br>- Serialization breaks singleton (use readResolve)<br>- Reflection can break it (throw exception in constructor) |
| **Pros** | ✓ Controlled access<br>✓ Lazy initialization<br>✓ Global access |
| **Cons** | ✗ Difficult to test (global state)<br>✗ Violates Single Responsibility<br>✗ Thread-safety concerns |



### 2. Factory Method Pattern

**Purpose:** Define an interface for creating objects, but let subclasses decide which class to instantiate.

**E-commerce Example:**
```java
// Payment processor factory
abstract class PaymentProcessor {
    abstract Payment createPayment();
    
    public void processOrder(Order order) {
        Payment payment = createPayment();
        payment.validate(order);
        payment.execute(order);
        payment.sendConfirmation();
    }
}

class CreditCardProcessor extends PaymentProcessor {
    Payment createPayment() {
        return new CreditCardPayment();
    }
}

class PayPalProcessor extends PaymentProcessor {
    Payment createPayment() {
        return new PayPalPayment();
    }
}

class CryptoProcessor extends PaymentProcessor {
    Payment createPayment() {
        return new CryptoPayment();
    }
}
```

| Aspect | Details |
|--------|---------|
| **Real-world Use Cases** | - Payment method selection (credit card, PayPal, crypto)<br>- Shipping method creation (standard, express, overnight)<br>- Notification delivery (email, SMS, push)<br>- Document export (PDF, Excel, CSV) |
| **Why This Pattern?** | - New payment methods added without changing existing code<br>- Each payment type has unique validation/processing logic<br>- Business logic stays in parent class, creation delegated to subclasses |
| **Why NOT Others?** | - Singleton: Need multiple payment instances per transaction<br>- Abstract Factory: Only creating one type (Payment), not families<br>- Builder: Payments aren't complex multi-step constructions |
| **Interview Pitfalls** | - Difference from Abstract Factory: Factory Method creates ONE product type<br>- Not the same as Simple Factory (which isn't a pattern)<br>- Subclasses control instantiation, not the client |
| **Pros** | ✓ Open/Closed Principle<br>✓ Single Responsibility<br>✓ Loose coupling |
| **Cons** | ✗ Can create many subclasses<br>✗ More complex code structure |


### 3. Abstract Factory Pattern

**Purpose:** Provide an interface for creating families of related objects without specifying their concrete classes.

**E-commerce Example:**
```java
// UI theme factory for different platforms
interface UIFactory {
    Button createButton();
    Checkbox createCheckbox();
    Dialog createDialog();
}

class WebUIFactory implements UIFactory {
    public Button createButton() { return new WebButton(); }
    public Checkbox createCheckbox() { return new WebCheckbox(); }
    public Dialog createDialog() { return new WebDialog(); }
}

class MobileUIFactory implements UIFactory {
    public Button createButton() { return new MobileButton(); }
    public Checkbox createCheckbox() { return new MobileCheckbox(); }
    public Dialog createDialog() { return new MobileDialog(); }
}

class AdminUIFactory implements UIFactory {
    public Button createButton() { return new AdminButton(); }
    public Checkbox createCheckbox() { return new AdminCheckbox(); }
    public Dialog createDialog() { return new AdminDialog(); }
}

// Usage in checkout page
class CheckoutPage {
    private Button checkoutBtn;
    private Checkbox termsCheckbox;
    
    public CheckoutPage(UIFactory factory) {
        checkoutBtn = factory.createButton();
        termsCheckbox = factory.createCheckbox();
    }
}
```

| Aspect | Details |
|--------|---------|
| **Real-world Use Cases** | - Multi-platform UI components (Web, Mobile, Desktop)<br>- Product catalog variants (B2B, B2C, Wholesale)<br>- Database connection families (MySQL, PostgreSQL, MongoDB)<br>- Report generation suites (Analytics, Financial, Inventory) |
| **Why This Pattern?** | - Components must work together (Web button with Web dialog)<br>- Consistency across component families guaranteed<br>- Easy to switch entire UI theme by changing one factory<br>- Components are incompatible across families |
| **Why NOT Others?** | - Factory Method: Creates single products, not families<br>- Builder: Focuses on complex construction, not related families<br>- Prototype: Cloning doesn't ensure family relationships |
| **Interview Pitfalls** | - Key difference: Creates FAMILIES of related objects<br>- All products from one factory work together seamlessly<br>- Adding new product types requires changing all factories |
| **Pros** | ✓ Ensures product compatibility<br>✓ Isolates concrete classes<br>✓ Easy to exchange product families |
| **Cons** | ✗ Difficult to add new products<br>✗ Increases complexity<br>✗ Many classes/interfaces |



### 4. Builder Pattern

**Purpose:** Separate the construction of a complex object from its representation, allowing step-by-step creation.

**E-commerce Example:**
```java
// Complex product search query builder
class ProductSearchQuery {
    private String category;
    private Double minPrice;
    private Double maxPrice;
    private List<String> brands;
    private String sortBy;
    private Boolean inStock;
    private Integer minRating;
    private List<String> features;
    
    private ProductSearchQuery() {}
    
    public static class Builder {
        private ProductSearchQuery query = new ProductSearchQuery();
        
        public Builder category(String category) {
            query.category = category;
            return this;
        }
        
        public Builder priceRange(Double min, Double max) {
            query.minPrice = min;
            query.maxPrice = max;
            return this;
        }
        
        public Builder brands(String... brands) {
            query.brands = Arrays.asList(brands);
            return this;
        }
        
        public Builder sortBy(String sortBy) {
            query.sortBy = sortBy;
            return this;
        }
        
        public Builder onlyInStock() {
            query.inStock = true;
            return this;
        }
        
        public Builder minRating(Integer rating) {
            query.minRating = rating;
            return this;
        }
        
        public Builder features(String... features) {
            query.features = Arrays.asList(features);
            return this;
        }
        
        public ProductSearchQuery build() {
            // Validation logic here
            if (query.minPrice != null && query.maxPrice != null) {
                if (query.minPrice > query.maxPrice) {
                    throw new IllegalStateException("Min price cannot exceed max price");
                }
            }
            return query;
        }
    }
}

// Usage
ProductSearchQuery laptopSearch = new ProductSearchQuery.Builder()
    .category("Electronics")
    .priceRange(500.0, 2000.0)
    .brands("Dell", "HP", "Lenovo")
    .minRating(4)
    .onlyInStock()
    .features("SSD", "16GB RAM", "Backlit Keyboard")
    .sortBy("price_low_to_high")
    .build();
```

| Aspect | Details |
|--------|---------|
| **Real-world Use Cases** | - Product filter queries (many optional parameters)<br>- Order creation (items, address, payment, shipping, gift options)<br>- Email templates (subject, body, attachments, recipients, CC, BCC)<br>- Complex product configurations (laptops with custom specs) |
| **Why This Pattern?** | - Too many constructor parameters (telescoping constructor problem)<br>- Many optional parameters make constructors unreadable<br>- Need validation across multiple fields before creation<br>- Step-by-step construction improves readability |
| **Why NOT Others?** | - Constructor: 10+ parameters become unreadable and error-prone<br>- Factory: Doesn't handle complex step-by-step construction well<br>- Prototype: Cloning doesn't help with custom configurations |
| **Interview Pitfalls** | - Director class is optional (not always needed)<br>- Builder can have validation in build() method<br>- Immutable objects are common outcome<br>- Fluent interface (method chaining) is typical |
| **Pros** | ✓ Readable code (fluent interface)<br>✓ Different representations<br>✓ Control over construction |
| **Cons** | ✗ More code complexity<br>✗ Requires creating separate Builder class |



### 5. Prototype Pattern

**Purpose:** Create new objects by copying an existing object (prototype) instead of creating from scratch.

**E-commerce Example:**
```java
// Product template cloning for similar products
abstract class Product implements Cloneable {
    protected String id;
    protected String name;
    protected Double basePrice;
    protected String category;
    protected List<String> images;
    protected Map<String, String> specifications;
    
    public Product clone() {
        try {
            Product cloned = (Product) super.clone();
            // Deep copy collections
            cloned.images = new ArrayList<>(this.images);
            cloned.specifications = new HashMap<>(this.specifications);
            return cloned;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }
}

class Laptop extends Product {
    private String processor;
    private Integer ram;
    private Integer storage;
    
    public Laptop clone() {
        return (Laptop) super.clone();
    }
}

// Usage: Create product variants from template
class ProductCatalogManager {
    private Map<String, Product> productTemplates = new HashMap<>();
    
    public void initializeTemplates() {
        // Base laptop template
        Laptop dellXPS = new Laptop();
        dellXPS.setName("Dell XPS 15");
        dellXPS.setCategory("Laptops");
        dellXPS.setBasePrice(1200.0);
        dellXPS.setProcessor("Intel i7");
        dellXPS.setImages(Arrays.asList("xps-front.jpg", "xps-side.jpg"));
        
        productTemplates.put("dell-xps-template", dellXPS);
    }
    
    public Product createProductVariant(String templateKey, String variantId) {
        Product template = productTemplates.get(templateKey);
        Product variant = template.clone();
        variant.setId(variantId);
        return variant;
    }
}

// Create variants quickly
Product xps16GB = catalogManager.createProductVariant("dell-xps-template", "XPS-001");
xps16GB.setName("Dell XPS 15 - 16GB");
xps16GB.setRam(16);

Product xps32GB = catalogManager.createProductVariant("dell-xps-template", "XPS-002");
xps32GB.setName("Dell XPS 15 - 32GB");
xps32GB.setRam(32);
xps32GB.setBasePrice(1500.0);
```

| Aspect | Details |
|--------|---------|
| **Real-world Use Cases** | - Product variants (same base, different specs/colors/sizes)<br>- Email/notification templates<br>- Default user profile configurations<br>- Game object spawning (enemies, NPCs with default stats)<br>- Document templates (invoice, receipt formats) |
| **Why This Pattern?** | - Creating from scratch is expensive (database queries, API calls)<br>- Objects share 80% of properties, only 20% differs<br>- Complex initialization that should be reused<br>- Runtime object creation based on dynamic prototypes |
| **Why NOT Others?** | - Constructor: Too expensive to reinitialize everything<br>- Factory: Still creates objects from scratch each time<br>- Builder: Overkill when you just need copies with minor tweaks |
| **Interview Pitfalls** | - Shallow vs Deep copy is critical (collections, nested objects)<br>- Implements Cloneable interface in Java<br>- Clone() method must be carefully implemented<br>- Consider using copy constructors as alternative |
| **Pros** | ✓ Reduces initialization cost<br>✓ Simplifies object creation<br>✓ Dynamic configuration |
| **Cons** | ✗ Cloning complex objects is tricky<br>✗ Deep copy implementation complexity<br>✗ Circular references can cause issues |


## Pattern Comparison Table

| Scenario | Best Pattern | Why? |
|----------|-------------|------|
| Need exactly one instance globally | **Singleton** | Controls instantiation and provides global access |
| Multiple product types, one at a time | **Factory Method** | Each type has different creation logic |
| Families of related products | **Abstract Factory** | Ensures products work together |
| Object with 7+ parameters (many optional) | **Builder** | Readable, handles complexity |
| Creating similar objects repeatedly | **Prototype** | Avoids expensive initialization |
| Plugin architecture | **Factory Method** | New plugins added via subclasses |
| Database connection pooling | **Singleton** | Shared resource management |
| Cross-platform UI | **Abstract Factory** | Platform-specific component families |
| Configuration objects | **Builder** or **Prototype** | Builder for creation, Prototype for templates |



## Interview Quick Tips

### Common Questions

**Q: What's the difference between Factory Method and Abstract Factory?**
- **Factory Method:** Creates ONE product type (e.g., Payment)
- **Abstract Factory:** Creates FAMILIES of products (e.g., Button + Checkbox + Dialog that work together)

**Q: When would you use Builder over Constructor?**
- When you have 4+ parameters, especially if many are optional
- When parameter order matters and is error-prone
- When you need validation across multiple fields

**Q: Isn't Singleton an anti-pattern?**
- It can be when overused (creates global state, testing difficulties)
- Valid for truly shared resources (logging, caching, connection pools)
- Consider dependency injection as alternative

**Q: How is Prototype different from just copying?**
- Prototype is a formal pattern with clone() interface
- Handles deep vs shallow copying systematically
- Can be combined with factory pattern (prototype registry)

**Remember:** Design patterns are tools, not rules. The best pattern is the one that solves your specific problem most simply and clearly.


