package org.cnv0.translate;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Translator {
    private final String filepath;
    private List<String> input;
    private final List<Instruction> instructions = new ArrayList<>();
    private String output;

    public Translator(String filepath) {
        this.filepath = filepath;
        Instruction.currentLabelCount = 0;

        readFile();
        prepareInput();
        readInstructions();
        translate();
    }


    private void readFile() {
        try {
            input = Files.readAllLines(Path.of(filepath));
        } catch (IOException e) {
            System.err.println("Could not load file " + filepath + ":" + e);
        }
    }

    private void prepareInput() {
        for (int i = 0; i < input.size(); i++) {
            input.set(i, input.get(i).replaceAll("//.*", "").trim());
            if (input.get(i).isEmpty()) {
                input.remove(i);
                i--;
            }
        }
    }

    private void readInstructions() {
        Pattern pushPopPattern = Pattern.compile("^(\\w{3,4}) (\\w+) (\\w+)$");
        Pattern operationPattern = Pattern.compile("^(\\w+)$");
        for (String s : input){
            Matcher pushPopMatcher = pushPopPattern.matcher(s);
            if (pushPopMatcher.matches()) processPushPop(pushPopMatcher);

            Matcher operationMatcher = operationPattern.matcher(s);
            if (operationMatcher.matches()) processOperation(operationMatcher);
        }
    }

    private void processPushPop(Matcher pushPopMatcher) {
        instructions.add(new Instruction.PushPopInstruction(pushPopMatcher.group(1), pushPopMatcher.group(2), pushPopMatcher.group(3)));
    }

    private void processOperation(Matcher operationMatcher) {
        instructions.add(new Instruction.OperationInstruction(operationMatcher.group(1)));
    }

    private void translate() {
        output = "";
        for (Instruction instruction : instructions) {
            for (String assembly : instruction.getAssembly()) {
                output += assembly + "\n";
            }
        }
    }


    public String getOutput() { return output; }
}
