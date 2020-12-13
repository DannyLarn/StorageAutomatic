/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package storageautomatic;

import static java.lang.System.out;
import java.io.*;
/**
 *
 * @author dnyyy
 */

// main class
public class StorageAutomatic {

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
    */
    
    public static void main(String[] args) throws IOException {
        // all vars what needs later at menu
        BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
        Shelf shelf = new Shelf();
        Storage storage = new Storage();
        Checkout checkout = new Checkout(shelf, storage);
        // main menu points:
        String menuPoints[] = {
            "(s) Vasarlas", "(a) Admin bejelentkezes", "(q) Kilepes"
        };
        String input;
        boolean exit = false;
        
        // main menu:
        do {
            for (String menuPoint : menuPoints) out.println(menuPoint);
            out.print("Valasszon a fenti menupontok kozul: ");
            
            input = console.readLine().trim().toLowerCase();
            out.println();
            
            switch (input) {
                case "s": {
                    User customer = new Customer(shelf, checkout);
                    customer.menu();
                    break;
                }
                case "a": {
                    Admin admin = new Admin(shelf, storage);
                    admin.menu();
                    break;
                }
                case "q": {
                    exit = input.equals("q");
                    break;
                }
                default: out.println("Hibas menukod!"); break;
            }
        } while (!exit);
        
        out.println("Kilepett a programbol!");
        // TODO code application logic here
        // [x] finish checkout and clean
        // [ ] need tests
    }
    
}
