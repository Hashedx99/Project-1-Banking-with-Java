package com.ga.banking.with.java.interfaces;

import com.ga.banking.with.java.entities.User;

import java.io.File;

public interface FileHandler {
    Object readFromFile(Object file);
    boolean writeToFile(String name, String id, Object content);
}
