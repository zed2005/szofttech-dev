package com.szofttech.backbone;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

public class TestFactory {
    private static Path getTestFilePath() {
        String localDir = System.getProperty("user.dir");
        return Paths.get(localDir, "src", "test", "blackbox-test-files");
    }

    private static File generateFile() {
        File dir = new File(TestFactory.getTestFilePath().toString());

        String fileUUID = UUID.randomUUID().toString();

        String fullFileName = fileUUID+".txt";

        return new File(dir, fullFileName);
    }

    public void createTestCase(String name, String type, String usedFunction, List<String> parameters, String expectedOutput) {
        File testCase = TestFactory.generateFile();
        
        try (PrintWriter pw = new PrintWriter(testCase)) {
            pw.println(name);
            pw.println(type);
            pw.println(usedFunction);
            
            for(String parameter : parameters) {
                pw.print(parameter);
                if(!parameter.equals(parameters.get(parameters.size()-1))) pw.print(","); 
            }
            pw.println();

            pw.println(expectedOutput);
        } catch (FileNotFoundException e) {
            System.out.println("Exception: "+ e);
        }
                
    }
}