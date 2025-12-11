package com.ga.banking.with.java.interfaces;

public interface FileHandler {
    Object readFromFile(Object file);
    boolean writeToFile(String name, String id, Object content);
}
