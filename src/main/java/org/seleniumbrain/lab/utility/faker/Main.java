package org.seleniumbrain.lab.utility.faker;

import java.util.List;

public class Main {
    public static void main(String[] args) {

        // Example 1: Generate a random file name with a specified set of extensions
        String fakeFile1 = CustomFaker.fakeFileName("txt", "pdf", "jpg");
        System.out.println(fakeFile1);  // Outputs: someword.pdf or someword.jpg, etc.
        
        // Example 2: Generate a random file name using a List of extensions
        List<String> extensions = List.of("png", "docx", "xlsx");
        String fakeFile2 = CustomFaker.fakeFileName(extensions);
        System.out.println(fakeFile2);  // Outputs: someword.png or someword.xlsx, etc.
    }
}
