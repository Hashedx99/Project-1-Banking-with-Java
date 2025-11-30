package com.ga.banking.with.java.interfaces;

import com.ga.banking.with.java.entities.User;

public interface FileHandler {
    String readFromFile(String filePath);
    boolean writeToFile(String name, String id,String fileContent);
}
