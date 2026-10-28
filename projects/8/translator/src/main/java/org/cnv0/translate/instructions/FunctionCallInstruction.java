package org.cnv0.translate.instructions;

import java.util.HashMap;
import java.util.Map;

public class FunctionCallInstruction extends Instruction {
    private String name;
    private int argumentCount;

    private static Map<String, Integer> callMap = new HashMap<>();

    public FunctionCallInstruction(String name, int argumentCount) {
        this.name = name;
        this.argumentCount = argumentCount;
        this.translate();
    }

    public void translate() {
        int currentKey = 0;
        if (!callMap.containsKey(name)) {
            callMap.put(name, 0);
        } else {
            currentKey = callMap.get(name) + 1;
            callMap.put(name, currentKey);
        }

        // save return address, LCL, ARG, THIS, THAT
        assembly.add("@" + name + ".ret" + currentKey);
        assembly.add("D=A");
        assembly.add("@SP");
        assembly.add("A=M");
        assembly.add("M=D");
        assembly.add("@SP");
        assembly.add("M=M+1");

        assembly.add("@LCL");
        assembly.add("D=M");
        assembly.add("@SP");
        assembly.add("A=M");
        assembly.add("M=D");
        assembly.add("@SP");
        assembly.add("M=M+1");

        assembly.add("@ARG");
        assembly.add("D=M");
        assembly.add("@SP");
        assembly.add("A=M");
        assembly.add("M=D");
        assembly.add("@SP");
        assembly.add("M=M+1");

        assembly.add("@THIS");
        assembly.add("D=M");
        assembly.add("@SP");
        assembly.add("A=M");
        assembly.add("M=D");
        assembly.add("@SP");
        assembly.add("M=M+1");

        assembly.add("@THAT");
        assembly.add("D=M");
        assembly.add("@SP");
        assembly.add("A=M");
        assembly.add("M=D");
        assembly.add("@SP");
        assembly.add("M=M+1");

        // ARG = SP - 5 - argumentCount
        assembly.add("@SP");
        assembly.add("D=M");
        assembly.add("@" + (5 + argumentCount));
        assembly.add("D=D-A");
        assembly.add("@ARG");
        assembly.add("M=D");

        // LCL = SP
        assembly.add("@SP");
        assembly.add("D=M");
        assembly.add("@LCL");
        assembly.add("M=D");

        // go to the label
        assembly.add("@" + name);
        assembly.add("0;JMP");

        // add the label to return to
        assembly.add("(" + name + ".ret" + currentKey + ")");
    }
}
