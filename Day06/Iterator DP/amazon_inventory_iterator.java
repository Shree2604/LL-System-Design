/*
 * ITERATOR DESIGN PATTERN - AMAZON INVENTORY SYSTEM
 * 
 * What is this pattern?
 * - A behavioral design pattern that provides a way to access elements
 *   of a collection sequentially without exposing its underlying representation
 * - Separates the traversal logic from the collection itself
 * 
 * Real-world analogy:
 * - Like browsing through Amazon products without knowing if they're stored
 *   in an array, database, or linked list
 * - TV remote control iterating through channels
 * 
 * Key Benefits:
 * 1. Single Responsibility - Separates traversal from collection
 * 2. Open/Closed Principle - Add new iterators without changing collection
 * 3. Uniform interface for different collection types
 * 4. Can have multiple iterators on same collection simultaneously
 * 
 * Use cases:
 * - Traversing different data structures uniformly
 * - Filtering/sorting collections
 * - Lazy loading of elements
 * - Pagination systems
 */

// Product class - represents an item in inventory
class Product {
    private String id;
    private String name;
    private double price;
    private String category;
    private int stock;
    
    public Product(String id, String name, double price, 
                   String category, int stock) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.category = category;
        this.stock = stock;
    }
    
    // Getters
    public String getId() { return id; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public String getCategory() { return category; }
    public int getStock() { return stock; }
    
    @Override
    public String toString() {
        return String.format("[%s] %s - $%.2f (Stock: %d, Category: %s)",
            id, name, price, stock, category);
    }
}

// Iterator interface - defines traversal operations
interface Iterator<T> {
    boolean hasNext();
    T next();
    void reset();
}

// Aggregate interface - defines method to create iterator
interface InventoryCollection {
    Iterator<Product> createIterator();
    Iterator<Product> createCategoryIterator(String category);
    Iterator<Product> createPriceRangeIterator(double minPrice, double maxPrice);
}

// Concrete Iterator - implements traversal logic
class ProductIterator implements Iterator<Product> {
    private Product[] products;
    private int position = 0;
    
    public ProductIterator(Product[] products) {
        this.products = products;
    }
    
    @Override
    public boolean hasNext() {
        return position < products.length && products[position] != null;
    }
    
    @Override
    public Product next() {
        if (!hasNext()) {
            return null;
        }
        return products[position++];
    }
    
    @Override
    public void reset() {
        position = 0;
    }
}

// Category Filter Iterator
class CategoryIterator implements Iterator<Product> {
    private Product[] products;
    private String category;
    private int position = 0;
    
    public CategoryIterator(Product[] products, String category) {
        this.products = products;
        this.category = category;
    }
    
    @Override
    public boolean hasNext() {
        while (position < products.length && products[position] != null) {
            if (products[position].getCategory().equalsIgnoreCase(category)) {
                return true;
            }
            position++;
        }
        return false;
    }
    
    @Override
    public Product next() {
        if (!hasNext()) {
            return null;
        }
        return products[position++];
    }
    
    @Override
    public void reset() {
        position = 0;
    }
}

// Price Range Iterator
class PriceRangeIterator implements Iterator<Product> {
    private Product[] products;
    private double minPrice;
    private double maxPrice;
    private int position = 0;
    
    public PriceRangeIterator(Product[] products, double minPrice, double maxPrice) {
        this.products = products;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
    }
    
    @Override
    public boolean hasNext() {
        while (position < products.length && products[position] != null) {
            double price = products[position].getPrice();
            if (price >= minPrice && price <= maxPrice) {
                return true;
            }
            position++;
        }
        return false;
    }
    
    @Override
    public Product next() {
        if (!hasNext()) {
            return null;
        }
        return products[position++];
    }
    
    @Override
    public void reset() {
        position = 0;
    }
}

// Concrete Collection - Amazon Inventory
class AmazonInventory implements InventoryCollection {
    private Product[] products;
    private int index = 0;
    private static final int MAX_ITEMS = 100;
    
    public AmazonInventory() {
        products = new Product[MAX_ITEMS];
    }
    
    public void addProduct(Product product) {
        if (index < MAX_ITEMS) {
            products[index++] = product;
        }
    }
    
    @Override
    public Iterator<Product> createIterator() {
        return new ProductIterator(products);
    }
    
    @Override
    public Iterator<Product> createCategoryIterator(String category) {
        return new CategoryIterator(products, category);
    }
    
    @Override
    public Iterator<Product> createPriceRangeIterator(double minPrice, double maxPrice) {
        return new PriceRangeIterator(products, minPrice, maxPrice);
    }
}

// Client code - demonstrates the pattern
public class AmazonInventorySystem {
    public static void main(String[] args) {
        // Create inventory
        AmazonInventory inventory = new AmazonInventory();
        
        // Add products
        inventory.addProduct(new Product("P001", "Laptop", 899.99, "Electronics", 15));
        inventory.addProduct(new Product("P002", "Wireless Mouse", 29.99, "Electronics", 50));
        inventory.addProduct(new Product("P003", "Java Programming Book", 45.00, "Books", 30));
        inventory.addProduct(new Product("P004", "Office Chair", 199.99, "Furniture", 20));
        inventory.addProduct(new Product("P005", "Mechanical Keyboard", 89.99, "Electronics", 25));
        inventory.addProduct(new Product("P006", "Design Patterns Book", 55.00, "Books", 18));
        inventory.addProduct(new Product("P007", "Desk Lamp", 34.99, "Furniture", 40));
        
        // 1. Iterate through all products
        System.out.println("========== ALL PRODUCTS ==========");
        Iterator<Product> allProducts = inventory.createIterator();
        while (allProducts.hasNext()) {
            System.out.println(allProducts.next());
        }
        
        // 2. Filter by category - Electronics
        System.out.println("\n========== ELECTRONICS ONLY ==========");
        Iterator<Product> electronics = inventory.createCategoryIterator("Electronics");
        while (electronics.hasNext()) {
            System.out.println(electronics.next());
        }
        
        // 3. Filter by price range - $30 to $100
        System.out.println("\n========== PRODUCTS $30-$100 ==========");
        Iterator<Product> priceRange = inventory.createPriceRangeIterator(30.0, 100.0);
        while (priceRange.hasNext()) {
            System.out.println(priceRange.next());
        }
        
        // 4. Filter by category - Books
        System.out.println("\n========== BOOKS ONLY ==========");
        Iterator<Product> books = inventory.createCategoryIterator("Books");
        while (books.hasNext()) {
            System.out.println(books.next());
        }
    }
}