package com.szofttech.backbone;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public abstract class TestFactory {

    private String generateUUIDName() {
        String fileUUID = UUID.randomUUID().toString();

        return fileUUID;
    }

    protected static Path getTestFilePath() {
        String localDir = System.getProperty("user.dir");
        return Paths.get(localDir, "src", "test", "blackbox-test-files");
    }

    protected File generateDirectory() {
        Path testPath = Paths.get(getTestFilePath().toString(), generateUUIDName());
        File newDir = new File(testPath.toString());

        if(!newDir.exists()) newDir.mkdir();

        return newDir;
    }

    protected File generateFile(String type, File dir) {
        String fullFileName = type+".txt";

        return new File(dir, fullFileName);
    }

    protected void writeIntoFile(List<String> parameters, File testcaseFile) {
        try (PrintWriter pw = new PrintWriter(testcaseFile)) {
            for(String parameter : parameters) {
                pw.println(parameter);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Exception: "+ e);
        }  
    }

    protected void generateInfoFile(String description, File testDir) {
        File infoFile = generateFile("info", testDir);

        writeIntoFile(new ArrayList<>(List.of(description)), infoFile);
    }

    protected void generateCommandsFile(List<String> commands, File testDir) {
        File commandFile = generateFile("commands", testDir);

        writeIntoFile(commands, commandFile);
    }

    protected void generateExpectedOutputFile(String expected, File testDir) {
        File outputFile = generateFile("expected-output", testDir);

        writeIntoFile(new ArrayList<>(List.of(expected)), outputFile);
    }
}
