package com.szofttech.backbone;

import java.io.File;
import java.util.List;

public class BasicTestGenerator extends TestFactory implements TestFunctionInterface {

    @Override
    public void createFunctionTestCase(String description, String type, String part, List<String> parameters, String expectedOutput) {
        File testDir = generateDirectory();
      
        generateInfoFile(description, testDir);
        generateTypeFile(type, testDir);
        generatePartFile(part, testDir);
        generateCommandsFile(parameters, testDir);
        generateExpectedOutputFile(expectedOutput, testDir);
        generateFile("actual-output", testDir);
    }
}
