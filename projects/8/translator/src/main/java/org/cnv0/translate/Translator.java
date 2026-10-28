package org.cnv0.translate;

import org.cnv0.translate.instructions.*;

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
        Matcher matcher = Pattern.compile(".*/(\\w+)\\.vm").matcher(filepath);
        if (matcher.matches()) Instruction.currentFileName = matcher.group(1);

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
        Pattern pushPopPattern = Pattern.compile("^(push|pop)\\s+(\\w+)\\s+(\\d+)$");
        Pattern operationPattern = Pattern.compile("^(\\w{2,3})$");
        Pattern labelPattern = Pattern.compile("^label\\s+(\\w+)$");
        Pattern gotoPattern = Pattern.compile("^(if-)?goto\\s+(\\w+)$");
        Pattern functionPattern = Pattern.compile("^function\\s+([\\w.]+)\\s+(\\d+)$");
        Pattern callPattern = Pattern.compile("^call ([\\w.]+) (\\d+)$");
        Pattern returnPattern = Pattern.compile("^return$");
        for (String s : input){
            Matcher pushPopMatcher = pushPopPattern.matcher(s);
            if (pushPopMatcher.matches()) {
                processPushPop(pushPopMatcher);
                continue;
            }

            Matcher operationMatcher = operationPattern.matcher(s);
            if (operationMatcher.matches()) {
                processOperation(operationMatcher);
                continue;
            }

            Matcher labelMatcher = labelPattern.matcher(s);
            if (labelMatcher.matches()) {
                processLabel(labelMatcher);
                continue;
            }

            Matcher gotoMatcher = gotoPattern.matcher(s);
            if (gotoMatcher.matches()) {
                processGoto(gotoMatcher);
                continue;
            }

            Matcher functionMatcher = functionPattern.matcher(s);
            if (functionMatcher.matches()) {
                processFunction(functionMatcher);
                continue;
            }

            Matcher callMatcher = callPattern.matcher(s);
            if (callMatcher.matches()) {
                processCall(callMatcher);
                continue;
            }

            Matcher returnMatcher = returnPattern.matcher(s);
            if (returnMatcher.matches()) {
                processReturn(returnMatcher);
                continue;
            }
        }
    }

    private void processPushPop(Matcher pushPopMatcher) {
        instructions.add(new PushPopInstruction(pushPopMatcher.group(1), pushPopMatcher.group(2), pushPopMatcher.group(3)));
    }

    private void processOperation(Matcher operationMatcher) {
        instructions.add(new OperationInstruction(operationMatcher.group(1)));
    }

    private void processLabel(Matcher labelMatcher) {
        instructions.add(new LabelInstruction(labelMatcher.group(1)));
    }

    private void processGoto(Matcher gotoMatcher) {
        instructions.add(new GotoInstruction(gotoMatcher.group(2), gotoMatcher.group(1) != null));
    }

    private void processFunction(Matcher functionMatcher) {
        instructions.add(new FunctionDeclarationInstruction(functionMatcher.group(1), Integer.parseInt(functionMatcher.group(2))));
    }

    private void processCall(Matcher callMatcher) {
        instructions.add(new FunctionCallInstruction(callMatcher.group(1), Integer.parseInt(callMatcher.group(2))));
    }

    private void processReturn(Matcher returnMatcher) {
        instructions.add(new FunctionReturnInstruction());
    }


    private void translate() {
        output = "@256\nD=A\n@SP\nM=D\n";

        String temp = "";
        for (Instruction instruction : instructions) {
            for (String assembly : instruction.getAssembly()) {
                temp += assembly + "\n";
            }
        }

        if (temp.contains("Sys.init")) {
            for (String assembly : (new FunctionCallInstruction("Sys.init", 0)).getAssembly()) {
                output += assembly + "\n";
            }
        }

        output += temp;
    }


    public String getOutput() { return output; }
}
