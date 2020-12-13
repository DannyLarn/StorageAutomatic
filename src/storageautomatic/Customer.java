/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package storageautomatic;

import static java.lang.System.out;
import java.io.*;
import java.util.*;
/**
 *
 * @author dnyyy
 */
public class Customer extends User {
    // global vars:
    private final ShoppingCart cart;
    private final Shelf shelf;
    private final Checkout checkout;
    private final BufferedReader console = new BufferedReader(new InputStreamReader(System.in)); // for reading input from user:
    private final String menuPoints[] = {
        "(q) vasarlas befejezese,", "(c) termek kosarba helyezese,", "(b) termek visszarakasa,",
        "(e) kosar uritese: ", "(ls) polc elemei,", "(lc) kosar elemei"
    };    
    
    // constructor:
    public Customer(Shelf shelf, Checkout checkout) {
        cart = new ShoppingCart();
        this.shelf = shelf;
        this.checkout = checkout;
    }
    
    // void functions:
    // add to cart the given product and remove from shelf the given quantity
    private void addToCart(Product prod) {
        // need to verify if the given quantity is okey
        Product verifiedProd = shelf.addToCart(prod);
        if (verifiedProd.getQuantity() != 0) {
           cart.add(verifiedProd);
        }
    }
    // it removes the given product from cart and puts back to shelf
    private void removeFromCart(Product prod) {
        Product verifiedProd = cart.backToShelf(prod);
        out.println(verifiedProd.getQuantity());
        shelf.backToShelf(verifiedProd);
    }
    // list cart's all product without its ID
    public void listCartElements() {
        cart.listCartElements();
    }
    // it handles every operations in the class (it handles the menu of the Customer)
    @Override public void menu() throws IOException {
        boolean exit = false;
        do {
            
            // write menuPoints
            for (String menuPoint : menuPoints) out.println(menuPoint);
            out.print("Valasszon a fenti menupontok kozul: ");
            
            String input = console.readLine().trim();
            out.println();
            
            switch (input) {
                case "q": exit = true; break;
                case "c": chooseProduct("add"); break;
                case "b": {
                    if (!cart.isEmpty()) {
                        chooseProduct("remove");
                    } else {
                        out.println("Ures a kosara!");
                        out.println();
                    }
                    break;
                }
                case "e": makeCartEmpty(); break;
                case "ls": shelf.listAllElements(); break;
                case "lc": listCartElements(); break;
                default: out.println("Hibas menukod! Probalja ujra!"); break;
            }
            
        } while (!exit);
        
        // the checkout gives the receit and it also saves everything and calls the verification what makes upload and ordering if it needs
        checkout.setNewShoppingCart(cart);
        checkout.readProducts();
    }
    // "add": the user select a new product what is gonna be in the cart
    // "remove": the user choose a product from cart what he/she does not want anymore
    private void chooseProduct(String action) throws IOException {
        switch (action) {
            case "add": shelf.listAllElements(); break;
            case "remove": cart.listCartElements(); break;
            default: out.println("Hibas parameter..."); break;
        }
        
        out.print("Adja meg a termek nevet: ");
        String chosenProdName = console.readLine().trim();
        Product chosenProd = shelf.find(chosenProdName);
        
        if (chosenProd != null ) {
            out.print("Adja meg a mennyiseget: ");
            String input = console.readLine().trim();
            
            if (input != null) {
                int quantity = Integer.parseInt(input);
                Product result = new Product(chosenProd.getId(), chosenProdName, chosenProd.getPrice(), quantity);
                
                switch (action) {
                    case "add": addToCart(result); break;
                    case "remove": removeFromCart(result); break;
                    default: out.println("Hibas parameter..."); break;
                }
            } else {
                out.println("Hibas mennyiseget adott meg!");
            }
        } else {
            out.println("Az On altal megadott termek nincs!");
        }
        
        cart.listCartElements();
    }
    // it removes everything from cart back to the shelf
    private void makeCartEmpty() throws IOException {
        out.print("Biztosan visszapakol mindent? (y) ");
        boolean makeItEmpty = console.readLine().trim().equalsIgnoreCase("y");
        if (makeItEmpty) {
            List<Product> removed = cart.getCartElements();
            removed.forEach((prod) -> {
                shelf.backToShelf(prod);
            });
            cart.removeAll();
        }
    }
    
    // var functions:
    // it calls the shelf's saving function to save all new data to the shelf.json
    @Override public void saveAllModification() {
        // the checkout saves everything
    }
    // no access to storage because this user is only a customer
    @Override public boolean hasAccessToStorage() { return false; }
}