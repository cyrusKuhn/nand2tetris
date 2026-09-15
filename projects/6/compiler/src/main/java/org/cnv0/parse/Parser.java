package org.cnv0.parse;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {
    private final String filepath;
    private List<String> input;
    private final List<Instruction> instructions = new ArrayList<>();
    private final Map<String, Integer> labels = new HashMap<>();
    private final List<String> binary = new ArrayList<>();
    private String output;

    public Parser(String filepath) {
        this.filepath = filepath;
        readFile();
        prepareInput();
        readInstructions();
        assignSymbols();
        assemble();
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
            input.set(i, input.get(i).replaceAll("[ \t]", ""));
            input.set(i, input.get(i).replaceAll("//.*", ""));

            if (input.get(i).isEmpty()) {
                input.remove(i);
                i--;
            }
        }
    }

    private void readInstructions() {
        Pattern labelPattern = Pattern.compile("^\\(([A-Za-z_.$:0-9]+)\\)$");
        Pattern addressPattern = Pattern.compile("^@([A-Za-z_.$:0-9]+)$");
        Pattern computePattern = Pattern.compile("^([ADM]{1,3}=)?([-!]?[ADM01]([+\\-&|][ADM01])?)?(;J[EGLMN][EPQT])?$");
        for(String s : input) {
            Matcher labelMatcher = labelPattern.matcher(s);
            if(labelMatcher.matches()) processLabel(labelMatcher);

            Matcher addressMatcher = addressPattern.matcher(s);
            if(addressMatcher.matches()) processAddress(addressMatcher);

            Matcher computeMatcher = computePattern.matcher(s);
            if(computeMatcher.matches()) processCompute(computeMatcher);
        }
    }

    private void processLabel(Matcher labelMatcher) {
        labels.put(labelMatcher.group(1), instructions.size());
    }

    private void processAddress(Matcher addressMatcher) {
        if (Pattern.compile("^\\d+$").matcher(addressMatcher.group(1)).matches()) instructions.add(new Instruction.AInstruction(Integer.parseInt(addressMatcher.group(1))));
        else if (Pattern.compile("R[0-9]{1,2}").matcher(addressMatcher.group(1)).matches()) instructions.add(new Instruction.AInstruction(Integer.parseInt(addressMatcher.group(1).substring(1))));
        else {
            if (!labels.containsKey(addressMatcher.group(1))) {
                labels.put(addressMatcher.group(1), Integer.MIN_VALUE);
            }

            instructions.add(new Instruction.AInstruction(addressMatcher.group(1)));
        }
    }

    private void processCompute(Matcher computeMatcher) {
        String destination = "";
        if (computeMatcher.group(1) != null) {
            if(computeMatcher.group(1).contains("A")) destination += "A";
            if(computeMatcher.group(1).contains("D")) destination += "D";
            if(computeMatcher.group(1).contains("M")) destination += "M";
        }

        String operation = null;
        if (!computeMatcher.group(2).isEmpty()) operation = computeMatcher.group(2);

        String jump = null;
        if (computeMatcher.group(4) != null && !computeMatcher.group(4).isEmpty()) jump = computeMatcher.group(4).substring(1);

        instructions.add(new Instruction.CInstruction(destination, operation, jump));
    }

    private void assignSymbols() {
        int memoryAddress = 16;
        for (int i = 0; i < instructions.size(); i++) {
            if (instructions.get(i) instanceof Instruction.AInstruction casted) {
                if (casted.getLabel() != null) {
                    Integer value = labels.getOrDefault(casted.getLabel(), Integer.MAX_VALUE);
                    if (value == Integer.MAX_VALUE) throw new IllegalArgumentException("Could not recognize label " + casted.getLabel());
                    if (value == Integer.MIN_VALUE) {
                        value = memoryAddress;
                        labels.put(casted.getLabel(), memoryAddress++);
                        if (memoryAddress >= 32767) throw new IllegalArgumentException("Too may labels assigned");
                    }
                    casted.setValue(value);
                    instructions.set(i, casted);
                }
            }
        }
    }

    private void assemble() {
        for (int i = 0; i < instructions.size(); i++) {
            instructions.get(i).assemble();
            binary.add(instructions.get(i).getBinary());
        }

        output = String.join("\n", binary);
    }


    public List<String> getBinary() { return binary; }
    public String getOutput() { return output; }
}
