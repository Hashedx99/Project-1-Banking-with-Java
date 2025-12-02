package com.ga.banking.with.java.interfaces;

import com.ga.banking.with.java.entities.User;

import java.io.File;

public interface FileHandler {
    User readFromFile(File file);
    boolean writeToFile(String name, String id,String fileContent);
}
