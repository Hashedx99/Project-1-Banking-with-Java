package com.ga.banking.with.java.service;

import com.ga.banking.with.java.entities.Customer;
import com.ga.banking.with.java.entities.User;
import com.ga.banking.with.java.interfaces.FileHandler;
import tools.jackson.databind.ObjectMapper;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import static com.ga.banking.with.java.helpers.CommonUtil.createDirectoriesAndWriteFile;

public class CustomerFileHandler implements FileHandler {

    @Override
    public User readFromFile(Object file) {
        if (!(file instanceof File)) {
            System.out.println("Invalid file content for customer. Expected a File.");
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue((File) file, Customer.class);
    }

    @Override
    public boolean writeToFile(String customerName, String customerId, Object fileContent) {
        if (!(fileContent instanceof String)) {
            System.out.println("Invalid file content for customer. Expected a JSON string.");
            return false;
        }
        try {
            String fileName = "Customer-" + customerName + "-" + customerId + ".json";
            Path dataPath = Paths.get("Data");
            Path customersPath = dataPath.resolve("Customers");

            createDirectoriesAndWriteFile((String) fileContent, fileName, dataPath, customersPath);
        } catch (Exception e) {
            System.out.println("An error occurred while writing to the customer file.");
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }
}
