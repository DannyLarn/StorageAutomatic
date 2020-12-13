/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package storageautomatic;

import static java.lang.System.out;
import java.io.*;
import java.io.Console;

/**
 *
 * @author dnyyy
 */
public class Admin extends User {
    // global vars:
    private final Shelf shelf;
    private final Storage storage;
    private final String password = "Alma12345";
    private final BufferedReader console = new BufferedReader(new InputStreamReader(System.in)); // for reading input from user:
    private final String menuPoints[] = {
        "(q) kezeles befejezese,", "(ms) polc termekeinek modositasa,", "(mst) raktar termekeinek modositasa,",
        "(ls) polc elemei,", "(lst) raktar elemei,", "(r) termek eltavolitasa,", "(n) uj termek feltoltese"
    };
    
    // constructor:
    public Admin(Shelf shelf, Storage storage) throws IOException {
        this.shelf = shelf;
        this.storage = storage;
    }
    
    // void functions:
    // if the user knows the password it calls the manage() function
    @Override public void menu() throws IOException {
        // login to access menu (manage())
        if (hasAccess() && shelf != null && storage != null) {
            manage();
        } else {
            out.println("Hozzaferes megtagadva!");
        }
    }
    // it handles every operations in the class (it handles the menu of the Admin)
    private void manage() throws IOException {
        out.printf("Hozzaferes engedelyezve!\n\n");
        boolean exit = false;
        
        do {
            // write menuPoints
            for (String menuPoint : menuPoints) out.println(menuPoint);
            out.print("Valasszon a fenti menupontok kozul: ");
            
            String input = console.readLine().toLowerCase().trim();
            out.println();
            
            switch(input) {
                case "q": exit = true; break;
                case "ms": manageProducts(Shelf.class); break;
                case "mst": manageProducts(Storage.class); break;
                case "ls": shelf.listAllElementsWithID(); break;
                case "lst": storage.listStorage(); break;
                case "r": removeProduct(); break;
                case "n": addNewProduct(); break;
                default: out.println("Hibas kodot adott meg!"); break;
            }
            out.println();
        } while(!exit);
        
        saveAllModification();
        
        out.printf("Admin kilepett!\n\n");
    }
    // here the admin can change the quantity or the price of chosen product
    private void manageProducts(Class<?> obj) throws IOException {
        Product prod = selectProductByIdFromInput(obj == Storage.class ? Storage.class : Shelf.class);
        
        out.printf("A valsztott termek adatai:\n\t");
        prod.write();
        out.println();
        // price modification:
        out.print("Ar modositasa (ha nem kivan modositani csak nyomjon egy entert): ");
        String input = console.readLine().trim();
        if (!input.equals("")) prod.setPrice(Integer.parseInt(input));
        
        // quantity modification:
        out.print("Mennyiseg modositasa (ha nem kivan modositani csak nyomjon egy entert): ");
        input = console.readLine().trim();
        if (!input.equals("")) prod.setQuantity(Integer.parseInt(input));
        
        out.printf("Modositott termek adatai:\n\t");
        prod.write();
    }
    // the admin can remove a chosen product
    private void removeProduct() throws IOException {
        Product chosenProd = selectProductByIdFromInput(Storage.class);
        
        if (chosenProd != null) {
            out.printf("A valsztott termek adatai:\n\t");
            chosenProd.write();
            out.println();
            out.print("Biztosan eltavolitja? (y/n) ");
            String result = console.readLine().trim().toLowerCase();
        
            if (result.equals("y")) {
                storage.remove(storage.find(chosenProd.getId()));
                shelf.remove(shelf.find(chosenProd.getId()));
                out.println("A termek el lett tavolitva.");
            } else {
                out.println("Eltavolitas visszavonva.");
            }
        } else {
            out.println("Az On altal megadott azonosito ervenytelen.");
        }
    }
    // the admin can add a new product
    private void addNewProduct() throws IOException {
        out.print("Adja meg az uj termek azonositojat: ");
        int id = Integer.parseInt(console.readLine().trim());
        
        if (storage.find(id) == null) {
            out.print("Adja meg az uj termek nevet: ");
            String name = console.readLine().trim();
            
            if (storage.find(name) == null) {
                out.print("Adja meg az uj termek arat: ");
                int price = Integer.parseInt(console.readLine().trim());
                
                if (price > 0) {
                    out.print("Adja meg a polcra rakando mennyiseget: ");
                    int quantityShelf = Integer.parseInt(console.readLine().trim());
                    
                    if (quantityShelf > 0) {
                        
                        storage.upload(new Product(id, name, price, 0));
                        shelf.upload(new Product(id, name, price, quantityShelf));
                        
                        out.println("A termek sikeresen fel lett jegyezve.");
                    } else {
                        out.println("Minimum egyet ki kell rakni a polcra!");
                    }
                } else {
                    out.println("Ervenytelen ar!");
                }
                
            } else {
                out.println("Ilyen nevu termek mar letezik!");
            }
            
        } else {
            out.println("Ilyen azonositoju termek mar letezik!");
        }
    }
    
    // var functions:
    // request an id from user and returns the given object's product if it finds it
    private Product selectProductByIdFromInput(Class<?> obj) throws IOException {
        if (obj == Storage.class) storage.listAllWithoutQunatity();
        else if (obj == Shelf.class) shelf.listAllElementsWithID();
        else {
            out.println("Hibas parameter...");
            return null;
        }
        out.print("Adja meg a modositando termek id-t: ");
        int id = Integer.parseInt(console.readLine().trim());
        
        return obj == Storage.class ? storage.find(id) : shelf.find(id);
    }
    // it requests the password and return true if it is right
    private boolean hasAccess() throws IOException {
        Console consoleR = System.console();
        
        if (consoleR != null) {
            // if it runs from console then the password will be hidden!!!
            char[] passwordArray = consoleR.readPassword("Jelszo: ");
            return password.equals(new String(passwordArray));
        } else {
            // but if it runs in netbeans it does not work like that
            out.print("Jelszo: ");
            return password.equals(console.readLine());
        }        
    }
    // it calls the shelf's and storage's saving function to save all new data to the shelf.json and storage.json
    @Override public void saveAllModification() {
        shelf.saveAllModification();
        storage.saveAllModification();
    }
    // has access to storage because it is an admin user
    @Override public boolean hasAccessToStorage() { return true; }
}
