/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package storageautomatic;

import java.io.*;
/**
 *
 * @author dnyyy
 */
public abstract class User {
    public abstract void menu() throws IOException;
    // returns true if has access to storage
    public abstract boolean hasAccessToStorage();
    // save the given class' changes into the json files
    public abstract void saveAllModification();
}
