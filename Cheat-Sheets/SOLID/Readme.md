# SOLID Principles Cheat Sheet 🚀

> **Transform clumsy code into clean, maintainable, and professional software**

## 📌 What is SOLID?

SOLID is a set of **5 design principles** that help you write better object-oriented code. Following SOLID makes your code:
- ✅ **Easy to understand**
- ✅ **Easy to maintain**
- ✅ **Easy to extend**
- ✅ **Easy to test**
- ✅ **Less prone to bugs**

Think of SOLID as **architectural rules** for building software that won't collapse under its own weight.


## 🎯 The 5 SOLID Principles

| Principle | What it means |
|-----------|---------------|
| **S** - Single Responsibility | A class should do ONE thing only |
| **O** - Open/Closed | Open for extension, closed for modification |
| **L** - Liskov Substitution | Subtypes must be substitutable for their base types |
| **I** - Interface Segregation | Don't force classes to implement unused methods |
| **D** - Dependency Inversion | Depend on abstractions, not concrete implementations |


## 🔴 Before & After: Real Use Case

### **Use Case: E-commerce Order Processing System**

Let's see how SOLID transforms clumsy code into professional code.

## 1️⃣ **S - Single Responsibility Principle**

> **"A class should have only ONE reason to change"**

### ❌ **CLUMSY CODE** (Doing everything in one class)

```python
class Order:
    def __init__(self, items, customer_email):
        self.items = items
        self.customer_email = customer_email
    
    def calculate_total(self):
        """Calculate order total"""
        return sum(item.price for item in self.items)
    
    def save_to_database(self):
        """Save order to database"""
        db.connect()
        db.insert("orders", self.items)
        db.close()
    
    def send_confirmation_email(self):
        """Send email to customer"""
        smtp = SMTP('smtp.gmail.com')
        smtp.login('user', 'pass')
        smtp.send(self.customer_email, "Order confirmed!")
        smtp.quit()
    
    def generate_invoice_pdf(self):
        """Generate PDF invoice"""
        pdf = PDF()
        pdf.add_text(f"Total: ${self.calculate_total()}")
        pdf.save("invoice.pdf")
```

**Problems:**
- Order class does 4 different jobs (calculation, database, email, PDF)
- Hard to test
- Hard to maintain
- Changes in email logic require touching Order class

### ✅ **SOLID CODE** (Each class has ONE responsibility)

```python
# Responsibility 1: Order logic only
class Order:
    def __init__(self, items):
        self.items = items
    
    def calculate_total(self):
        return sum(item.price for item in self.items)


# Responsibility 2: Database operations
class OrderRepository:
    def save(self, order):
        db.connect()
        db.insert("orders", order.items)
        db.close()


# Responsibility 3: Email notifications
class EmailService:
    def send_order_confirmation(self, email, order):
        smtp = SMTP('smtp.gmail.com')
        smtp.login('user', 'pass')
        smtp.send(email, f"Order confirmed! Total: ${order.calculate_total()}")
        smtp.quit()


# Responsibility 4: PDF generation
class InvoiceGenerator:
    def generate(self, order):
        pdf = PDF()
        pdf.add_text(f"Total: ${order.calculate_total()}")
        pdf.save("invoice.pdf")
```

**Benefits:**
- Each class has ONE clear purpose
- Easy to test individually
- Changes are isolated (email changes don't affect Order)



## 2️⃣ **O - Open/Closed Principle**

> **"Open for extension, closed for modification"**

### ❌ **CLUMSY CODE** (Modifying existing code for new features)

```python
class PaymentProcessor:
    def process_payment(self, amount, method):
        if method == "credit_card":
            # Process credit card
            print(f"Processing ${amount} via Credit Card")
        elif method == "paypal":
            # Process PayPal
            print(f"Processing ${amount} via PayPal")
        elif method == "bitcoin":  # NEW: Had to modify existing code!
            # Process Bitcoin
            print(f"Processing ${amount} via Bitcoin")
        # Every new payment method requires modifying this function!
```

**Problems:**
- Adding new payment methods requires modifying existing code
- Risk of breaking existing functionality
- Violates "closed for modification"

### ✅ **SOLID CODE** (Extend without modifying)

```python
from abc import ABC, abstractmethod

# Abstract base (closed for modification)
class PaymentMethod(ABC):
    @abstractmethod
    def process(self, amount):
        pass


# Extend with new classes (open for extension)
class CreditCardPayment(PaymentMethod):
    def process(self, amount):
        print(f"Processing ${amount} via Credit Card")


class PayPalPayment(PaymentMethod):
    def process(self, amount):
        print(f"Processing ${amount} via PayPal")


class BitcoinPayment(PaymentMethod):  # NEW: Just add a new class!
    def process(self, amount):
        print(f"Processing ${amount} via Bitcoin")


class PaymentProcessor:
    def process_payment(self, amount, payment_method: PaymentMethod):
        payment_method.process(amount)  # No modification needed!


# Usage
processor = PaymentProcessor()
processor.process_payment(100, CreditCardPayment())
processor.process_payment(200, BitcoinPayment())  # Works seamlessly!
```

**Benefits:**
- Add new payment methods without touching existing code
- Less risk of bugs
- Scales infinitely



## 3️⃣ **L - Liskov Substitution Principle**

> **"Subtypes must be substitutable for their base types"**

### ❌ **CLUMSY CODE** (Subclass breaks parent behavior)

```python
class Bird:
    def fly(self):
        print("Flying in the sky!")


class Sparrow(Bird):
    def fly(self):
        print("Sparrow flying!")


class Penguin(Bird):  # Problem: Penguins can't fly!
    def fly(self):
        raise Exception("Penguins can't fly!")  # Breaks contract!


# Code that expects all birds to fly
def make_bird_fly(bird: Bird):
    bird.fly()  # Will crash with Penguin!

make_bird_fly(Sparrow())  # ✅ Works
make_bird_fly(Penguin())  # ❌ Crashes!
```

**Problems:**
- Penguin violates the Bird contract
- Code expecting Bird behavior breaks

### ✅ **SOLID CODE** (Proper abstraction)

```python
class Bird:
    def eat(self):
        print("Eating food")


class FlyingBird(Bird):
    def fly(self):
        print("Flying in the sky!")


class Sparrow(FlyingBird):
    def fly(self):
        print("Sparrow flying!")


class Penguin(Bird):  # Penguin is a Bird, but not a FlyingBird
    def swim(self):
        print("Penguin swimming!")


# Code works with appropriate types
def make_bird_fly(bird: FlyingBird):
    bird.fly()

make_bird_fly(Sparrow())  # ✅ Works
# make_bird_fly(Penguin())  # Won't compile - Penguin is not FlyingBird
```

**Benefits:**
- No broken contracts
- Type system prevents errors
- Clear hierarchies



## 4️⃣ **I - Interface Segregation Principle**

> **"Don't force classes to implement methods they don't need"**

### ❌ **CLUMSY CODE** (Fat interfaces)

```python
class Worker(ABC):
    @abstractmethod
    def work(self):
        pass
    
    @abstractmethod
    def eat(self):
        pass
    
    @abstractmethod
    def sleep(self):
        pass


class Human(Worker):
    def work(self):
        print("Human working")
    
    def eat(self):
        print("Human eating")
    
    def sleep(self):
        print("Human sleeping")


class Robot(Worker):  # Problem: Robots don't eat or sleep!
    def work(self):
        print("Robot working")
    
    def eat(self):
        pass  # Forced to implement unused method!
    
    def sleep(self):
        pass  # Forced to implement unused method!
```

**Problems:**
- Robot forced to implement irrelevant methods
- Confusing interface
- Maintenance nightmare

### ✅ **SOLID CODE** (Segregated interfaces)

```python
class Workable(ABC):
    @abstractmethod
    def work(self):
        pass


class Eatable(ABC):
    @abstractmethod
    def eat(self):
        pass


class Sleepable(ABC):
    @abstractmethod
    def sleep(self):
        pass


class Human(Workable, Eatable, Sleepable):
    def work(self):
        print("Human working")
    
    def eat(self):
        print("Human eating")
    
    def sleep(self):
        print("Human sleeping")


class Robot(Workable):  # Only implements what it needs!
    def work(self):
        print("Robot working")
```

**Benefits:**
- Classes only implement what they need
- Clear, focused interfaces
- Easier to understand and maintain



## 5️⃣ **D - Dependency Inversion Principle**

> **"Depend on abstractions, not concrete implementations"**

### ❌ **CLUMSY CODE** (Tight coupling)

```python
class MySQLDatabase:
    def save(self, data):
        print(f"Saving {data} to MySQL")


class UserService:
    def __init__(self):
        self.db = MySQLDatabase()  # Tightly coupled to MySQL!
    
    def save_user(self, user):
        self.db.save(user)


# Problem: Can't switch to PostgreSQL without rewriting UserService!
```

**Problems:**
- UserService is tightly coupled to MySQL
- Can't test without real database
- Can't switch databases easily

### ✅ **SOLID CODE** (Depend on abstractions)

```python
from abc import ABC, abstractmethod

# Abstraction
class Database(ABC):
    @abstractmethod
    def save(self, data):
        pass


# Concrete implementations
class MySQLDatabase(Database):
    def save(self, data):
        print(f"Saving {data} to MySQL")


class PostgreSQLDatabase(Database):
    def save(self, data):
        print(f"Saving {data} to PostgreSQL")


class MockDatabase(Database):  # For testing!
    def save(self, data):
        print(f"Mock save: {data}")


# High-level module depends on abstraction
class UserService:
    def __init__(self, db: Database):  # Depends on interface!
        self.db = db
    
    def save_user(self, user):
        self.db.save(user)


# Usage: Easy to swap implementations!
service = UserService(MySQLDatabase())
service = UserService(PostgreSQLDatabase())
service = UserService(MockDatabase())  # For testing!
```

**Benefits:**
- Easy to swap implementations
- Easy to test (use mock)
- Loose coupling


## 📋 Quick Checklist: Is Your Code SOLID?

Use this checklist when writing/reviewing code:

- [ ] **S**: Does each class have only ONE reason to change?
- [ ] **O**: Can I add new features without modifying existing code?
- [ ] **L**: Can I substitute subclasses without breaking functionality?
- [ ] **I**: Are my interfaces focused and not bloated?
- [ ] **D**: Am I depending on abstractions rather than concrete classes?



## 🎓 Step-by-Step: Refactoring Clumsy Code to SOLID

### **Step 1: Identify Responsibilities**
- List everything your class does
- Each responsibility = potential new class

### **Step 2: Create Abstractions**
- Find common behaviors
- Create interfaces/abstract classes

### **Step 3: Apply Single Responsibility**
- Split fat classes into focused ones
- One class = one job

### **Step 4: Use Dependency Injection**
- Pass dependencies through constructors
- Depend on interfaces, not implementations

### **Step 5: Test Each Component**
- Each class should be testable in isolation
- Use mocks for dependencies


## 🛠️ Real-World Example: Complete Refactoring

### Before (Clumsy)
```python
class OrderProcessor:
    def process_order(self, items, customer_email, payment_method):
        # Calculate total
        total = sum(item.price for item in items)
        
        # Process payment
        if payment_method == "card":
            print("Card payment")
        elif payment_method == "paypal":
            print("PayPal payment")
        
        # Save to database
        db.save(items)
        
        # Send email
        smtp.send(customer_email, "Order confirmed")
        
        # Generate PDF
        pdf.create("invoice.pdf")
```

### After (SOLID)
```python
# S: Single Responsibility
class Order:
    def __init__(self, items):
        self.items = items
    
    def calculate_total(self):
        return sum(item.price for item in items)


# O: Open/Closed + D: Dependency Inversion
class PaymentMethod(ABC):
    @abstractmethod
    def process(self, amount): pass

class CardPayment(PaymentMethod):
    def process(self, amount): 
        print(f"Card: ${amount}")

class PayPalPayment(PaymentMethod):
    def process(self, amount): 
        print(f"PayPal: ${amount}")


# S: Single Responsibility
class OrderRepository:
    def save(self, order): 
        db.save(order)

class EmailService:
    def send_confirmation(self, email, order): 
        smtp.send(email, "Confirmed")

class InvoiceGenerator:
    def generate(self, order): 
        pdf.create("invoice.pdf")


# D: Dependency Inversion - Orchestrator
class OrderProcessor:
    def __init__(self, repository, email_service, invoice_generator):
        self.repository = repository
        self.email_service = email_service
        self.invoice_generator = invoice_generator
    
    def process(self, order, payment: PaymentMethod, email):
        payment.process(order.calculate_total())
        self.repository.save(order)
        self.email_service.send_confirmation(email, order)
        self.invoice_generator.generate(order)
```


## 🎯 Benefits Summary

| Before SOLID | After SOLID |
|--------------|-------------|
| 🔴 Hard to test | ✅ Easy to test |
| 🔴 Hard to change | ✅ Easy to extend |
| 🔴 Tightly coupled | ✅ Loosely coupled |
| 🔴 Fragile code | ✅ Robust code |
| 🔴 Difficult to understand | ✅ Self-documenting |


## 📚 Resources

- [Clean Code by Robert Martin](https://www.amazon.com/Clean-Code-Handbook-Software-Craftsmanship/dp/0132350882)
- [Refactoring Guru - SOLID Principles](https://refactoring.guru/design-patterns/solid-principles)
- [SOLID Principles Explained with Examples](https://stackify.com/solid-design-principles/)


## 🤝 Contributing

Found a better example? Want to add more use cases? PRs are welcome!

## 📝 License

MIT License - Feel free to use this cheat sheet in your projects!

**Remember:** SOLID principles are guidelines, not laws. Use them wisely, but don't over-engineer. Start simple, refactor when needed! 🚀
