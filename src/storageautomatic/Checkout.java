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
public class Checkout {
    // global vars:
    private final Shelf shelf;
    private final Storage storage;
    private ShoppingCart cart;
    private int uploadedQuantity = 5;
    
    // constructos:
    public Checkout(Shelf shelf, Storage storage) {
        this.shelf = shelf;
        this.storage = storage;
    }
    
    // void functions:
    // setter for cart
    public void setNewShoppingCart(ShoppingCart cart) {
        this.cart = cart;
    }
    //setter for uploadedQuantity
    public void setUploadedQuantity(int uploadedQuantity) {
        this.uploadedQuantity = uploadedQuantity;
    }
    // read the products from cart and make a receit
    public void readProducts() {
        if (cart != null && cart.size() > 0) {
            cart.listCartElements();
            cart.total();
            verification();
        } else {
            out.printf("Az On kosara ures...\n\n");
        }
        cart = null;
    }
    // it is gonna verify if we need to load a product to shelf or need to order to storage
    private void verification() {
        upload(storage.productShortageIDs(), Storage.class);
        upload(shelf.productShortage(), Shelf.class);
        upload(storage.productShortageIDs(), Storage.class);
        
        shelf.saveAllModification();
        storage.saveAllModification();
    }
    // executes the uploading depends on class type
    private void upload(List<Integer> ids, Class<?> obj) {
        ids.forEach((id) -> {
            if (obj == Storage.class)
                storage.orderProduct(id, uploadedQuantity+5);
            else if (obj == Shelf.class) {
                storage.loadToShelf(id, uploadedQuantity);
                shelf.upload(id, uploadedQuantity);
            }
            else
                out.println("Hibas parameter");
        });
    }
    
    // var functions:
}
