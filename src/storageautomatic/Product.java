/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package storageautomatic;

import static java.lang.System.out;
/**
 *
 * @author dnyyy
 */
public class Product {
    // global vars:
    private final int id;
    private final String name;
    private int price;
    private int quantity;
    
    // constructors:
    public Product(int id, String name, int price, int quantity) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }
    
    // void functions:
    // getter functions of all global vars
    public int getId() { return id; }
    public String getName() { return name; }
    public int getPrice() { return price; }
    public int getQuantity() { return quantity; }
    
    // setter to price and quantity
    public void setPrice(int price) { this.price = price; }
    public void setQuantity(int quantity) { if (quantity >= 0) this.quantity = quantity; }
    
    // writes all data out
    public void write() {
        out.printf("(%d) %s: %dFt, %ddb\n", id, name, price, quantity);
    }
    // write all data except ID
    public void writeWithoutID() {
        out.printf("%s: %dFt, %ddb\n", name, price, quantity);        
    }
    // write all data except quantity
    public void writeWithoutQuantity() {
        out.printf("(%d) %s: %d\n", id, name, price);
    }
    
    // var functions:
    // returns true if the given product has the same name, id and price
    public boolean equals(Product prod) {
        return (id == prod.getId() && name.equals(prod.getName()) && price == prod.getPrice());
    }
    
}
