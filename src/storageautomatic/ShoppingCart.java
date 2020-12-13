/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package storageautomatic;

import static java.lang.System.out;
import java.util.*;

 /**
 *
 * @author dnyyy
 */
public class ShoppingCart {
    // global vars:
    private final List<Product> products;
    
    // constructor:
    public ShoppingCart() {
        products = new ArrayList();
    }
    
    // void functions:
    // lists cart's products without its ID
    public void listCartElements() {
        if (!isEmpty()) {
            out.println("- A kosara tartalma: ");
            products.forEach((prod) -> {
                out.printf("\t");
                prod.writeWithoutID();
            });
            out.println();
        } else {
            out.println("- A kosara ures!");
            out.println();
        }
    }
    // add a given product to the cart
    public void add(Product prod) {
        boolean hasThatProd = false;
        for (Product thisp : products) {
            if (thisp.equals(prod)) {
                thisp.setQuantity(thisp.getQuantity() + prod.getQuantity());
                hasThatProd = true;
                break;
            }
        }
        if (!hasThatProd) {
            products.add(prod);
        }
    }
    // writes out the receit of the purchase
    public void total() {
        int sum = 0;
        for (Product prod : products) {
            sum += (prod.getPrice() * prod.getQuantity());
        }
        out.println("________________________________________");
        out.printf("Az On altal vasarolt termekek osszege: %dFt\n\n", sum);
    }
    // remove all product from cart
    public void removeAll() {
        products.removeAll(products);
    }
    
    // var functions:
    // if the cart has the chosen product it removes the given number of that and returns the removed product
    public Product backToShelf(Product prod) {
        for (Product thisp : products) {
            if (thisp.equals(prod)) {
                if (thisp.getQuantity() > prod.getQuantity()) {
                    thisp.setQuantity(thisp.getQuantity() - prod.getQuantity());
                } else {
                    prod.setQuantity(thisp.getQuantity());
                    products.remove(thisp);
                }
                // if made it once it does not need to run more times
                break;
            }
        }
        return prod;
    }
    // returns with all products of a list
    public List<Product> getCartElements() {
        return products;
    }
    // returns true if the cart empty
    public boolean isEmpty() {
        return products.isEmpty();
    }
    // returns the size of the cart's list
    public int size() {
        return products.size();
    }
}
