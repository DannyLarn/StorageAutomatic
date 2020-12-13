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
public class Storage {
    // global vars:
    private final List<Product> products;
    
    // constructor:
    public Storage() {
        products = new ArrayList();
        loadInDatasFromFile("./src/storageautomatic/Files/storage.json");
    }
    
    // void functions:
    // it read datas from the shelf.json file and loads to shelf
    private void loadInDatasFromFile(String filepath) {
        JSONParser parser = new JSONParser();
        
        try {
            Object obj = parser.parse(new FileReader(filepath));
            
            JSONObject jsonObject = (JSONObject) obj;
            
            JSONArray storageList = (JSONArray) jsonObject.get("storage");
            
            Iterator<JSONObject> iterator = storageList.iterator();
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
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    // upload a product and it is verifying if it has already added
    public void upload(Product prod) {
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
            // if it is not added yet
            if (!added) {
                products.add(prod);
            }
        }
    }
    // it lists out everything about all products from storage 
    public void listStorage() {
        out.println("- Raktar tartalma: ");
        products.forEach((prod) -> {
            out.printf("\t- ");
            prod.write();
        });
        out.println();
    }
    // it lists out everything without quantity about all products from storage 
    public void listAllWithoutQunatity() {
        out.println("- Raktar tartalma");
        products.forEach((prod) -> {
            out.printf("\t- ");
            prod.writeWithoutQuantity();
        });
    }
    // it removes a given product from storage
    public void remove(Product prod) {
        products.remove(prod);
    }
    // at the end of the admin modifications it saves everything into the storage.json
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
        result.put("storage", shelfList);
        FileWriter file;
        try {
            file = new FileWriter("./src/storageautomatic/Files/storage.json");
            file.write(result.toJSONString());
            file.close();
            
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
    // order given quantity from the given id of a Product
    public void orderProduct(int orderedProdID, int orderedQuantity) {
        Product orderedProduct = find(orderedProdID);
        orderedProduct.setQuantity(orderedProduct.getQuantity() + orderedQuantity);
    }
    // if the shelf uploading from storage it has to be removed from the storage
    public void loadToShelf(int id, int uploadedQuantity) {
        Product loadedFromThis = find(id);
        loadedFromThis.setQuantity(loadedFromThis.getQuantity() - uploadedQuantity);
    }
    
    // var functinos:
    // it returns true if the given ID is in the storage
    public Product find(int id) {
        for (Product prod : products) {
            if (prod.getId() == id) {
                return prod;
            }
        }
        return null;
    }
    // it returns true if the given NAME is in the storage
    public Product find(String name) {
        for (Product prod : products) {
            if (name.trim().equalsIgnoreCase(prod.getName())) {
                return prod;
            }
        }
        return null;
    }
    // return a list of ids of products which quantity is under or equals 15
    public List<Integer> productShortageIDs() {
        List<Integer> result = new ArrayList();
        
        products.stream().filter((prod) -> (prod.getQuantity() <= 15)).forEachOrdered((prod) -> {
            result.add(prod.getId());
        });

        return result;
    }
}
