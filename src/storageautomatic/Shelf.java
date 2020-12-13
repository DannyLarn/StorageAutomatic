/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package storageautomatic;

import static java.lang.System.out;
import java.util.*;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import org.json.simple.parser.JSONParser;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;


/**
 *
 * @author dnyyy
 */
public class Shelf {
    // global vars:
    private final List<Product> products;
    
    // constructor:
    public Shelf() {
        products = new ArrayList();
        loadInDatasFromFile("./src/storageautomatic/Files/shelf.json");
    }
    
    // void functions:
    // it read datas from the shelf.json file and loads to shelf
    private void loadInDatasFromFile(String filepath) {
                
        JSONParser parser = new JSONParser();
        
        try {
            Object obj = parser.parse(new FileReader(filepath));
            
            JSONObject jsonObject = (JSONObject) obj;
            
            JSONArray shelfList = (JSONArray) jsonObject.get("shelf");
                        
            Iterator<JSONObject> iterator = shelfList.iterator();
            while(iterator.hasNext()) {
                JSONObject product = (JSONObject) iterator.next();
                
                int id = Integer.parseInt(product.get("id").toString());
                String name = (String) product.get("name");
                int price = Integer.parseInt(product.get("price").toString());
                int quantity = Integer.parseInt(product.get("quantity").toString());
                
                upload(new Product(id, name, price, quantity));
            }
        } catch(FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // upload a product and it is verifying if it has already added
    public void upload(Product prod) {
        // if it has a price we add it
        if (prod.getPrice() > 0) {
            // check if it already has it
            boolean added = false;
            for (Product thisProd : products) {
                if (thisProd.equals(prod)) {
                    thisProd.setQuantity(thisProd.getQuantity() + prod.getQuantity());
                    added = true;
                    break;
                }
            }
            // if it is not added yet add a new
            if (!added) {
                products.add(prod);
            }
        }
    }
    // upload a product but it gives id and quantity (its used in checkout uploading from storage
    public void upload(int id, int quantity) {
        Product uploadedProd = find(id);
        uploadedProd.setQuantity(uploadedProd.getQuantity() + quantity);
    }
    // it lists all elements from shelf without ID (its planed for customers)
    public void listAllElements() {
        out.println("- A polc tartalma:");
        products.forEach((prod) -> {
            out.printf("\t- ");
            prod.writeWithoutID();
        });
        out.println();
    }
    // it list every data about a product (it planed for admin)
    public void listAllElementsWithID() {
        out.println("- A polc tartalma:");
        products.forEach((prod) -> {
            out.printf("\t- ");
            prod.write();
        });
        out.println();
    }
    // if the custemer dont need something and put it back
    // from the cart its gonna load the product back to shelf
    public void backToShelf(Product prod) {
        for (Product thisp : products) {
            if (thisp.equals(prod)) {
                thisp.setQuantity(thisp.getQuantity() + prod.getQuantity());
                break;
            }
        } 
    }
    // remove a product from shelf
    public void remove(Product prod) {
        products.remove(prod);
    }
    // at the end of the shopping or making admin modifications it saves everything to shelf.json
    public void saveAllModification() {
        JSONArray shelfList = new JSONArray();
        
        for (Product prod : products) {
            JSONObject obj = new JSONObject();
            
            obj.put("id", prod.getId());
            obj.put("name", prod.getName());
            obj.put("price", prod.getPrice());
            obj.put("quantity", prod.getQuantity());
            
            shelfList.add(obj);
        }
        JSONObject result = new JSONObject();
        result.put("shelf", shelfList);
        FileWriter file;
        try {
            file = new FileWriter("./src/storageautomatic/Files/shelf.json");
            file.write(result.toJSONString());
            file.close();
            
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
    
    // var functions:
    // it return true if the given ID is on the shelf
    public Product find(int id) {
        for (Product prod : products) {
            if (prod.getId() == id) {
                return prod;
            }
        }
        return null;
    }
    // it return true if the given NAME is on the shelf
    public Product find(String name) {
        for (Product prod : products) {
            if (name.trim().equalsIgnoreCase(prod.getName())) {
                return prod;
            }
        }
        return null;
    }
    // it removes the given quantity product from shelf but if the user wanted more
    // than the shelf has from it then it gives back notifcations and gives a different quantity
    public Product addToCart(Product prod) {
        // find if there is a product like that
        Product result = new Product(prod.getId(), prod.getName(), prod.getPrice(), prod.getQuantity());
        
        products.forEach((thisProd) -> {
            if (thisProd.equals(prod)) {
                // difference between quantities
                int difference = thisProd.getQuantity() - result.getQuantity(); 
                
                //  result.write();
                if (difference >= 0) {
                    out.printf("\nKosarba helyezett %ddb %s-t\n", prod.getQuantity(), prod.getName());
                    thisProd.setQuantity(difference);
                } else if (thisProd.getQuantity() != 0) {
                    // if the shelf has that product but the customer wanted more than there is it gives him all what left...
                    out.printf("\nCsak %ddb van a polcon. Kosarba helyezett %ddb %s-t\n",
                            thisProd.getQuantity(), thisProd.getQuantity(), thisProd.getName());
                    result.setQuantity(thisProd.getQuantity());
                    thisProd.setQuantity(0);
                } else {
                    out.printf("\nA(z) %s termekbol nincs tobb a polcon!\n", prod.getName());
                    result.setQuantity(0);
                }
            }
        });
        return result;
    }
    // verify if there is product shortage on shelf and returns a list of Product ids
    public List<Integer> productShortage() {
        List<Integer> result = new ArrayList();
        
        products.stream().filter((prod) -> (prod.getQuantity() <= 5)).forEachOrdered((prod) -> {
            result.add(prod.getId());
        });
        
        return result;
    }
}
