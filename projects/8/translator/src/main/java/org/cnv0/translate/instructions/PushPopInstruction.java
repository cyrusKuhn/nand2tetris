package org.cnv0.translate.instructions;

import java.util.HashMap;
import java.util.Map;

public class PushPopInstruction extends Instruction {
    private String pushPop;
    private String variableType;
    private String value;

    public PushPopInstruction(String pushPop, String variableType, String value) {
        this.pushPop = pushPop;
        this.variableType = variableType;
        this.value = value;
        this.translate();
    }

    private static Map<String, Integer> baseMemoryAddresses = new HashMap<>();
    private static boolean initialized = false;

    private static void init() {
        if(initialized) return;
        initialized = true;

        baseMemoryAddresses.put("local", 1);
        baseMemoryAddresses.put("argument", 2);
        baseMemoryAddresses.put("this", 3);
        baseMemoryAddresses.put("that", 4);
    }

    @Override
    public void translate() {
        init();
        if(pushPop.equals("push")) {
            // load the value into the D register
            switch(variableType) {
                case "constant":
                    // directly use the value
                    assembly.add("@" + Integer.parseInt(value));
                    assembly.add("D=A");
                    break;
                case "temp":
                    assembly.add("@" + (5 + Integer.parseInt(value)));
                    assembly.add("D=M");
                    break;
                case "pointer":
                    assembly.add("@" + (3 + Integer.parseInt(value)));
                    assembly.add("D=M");
                    break;
                case "static":
                    assembly.add("@" + currentFileName + "." + Integer.parseInt(value));
                    assembly.add("D=M");
                    break;
                default:
                    // grab the value in RAM[base]
                    assembly.add("@" + (baseMemoryAddresses.get(variableType)));
                    assembly.add("D=M");

                    // add i
                    assembly.add("@" + Integer.parseInt(value));
                    assembly.add("A=D+A");
                    assembly.add("D=M");
                    break;
            }

            // put the value onto the stack
            assembly.add("@SP");
            assembly.add("A=M");
            assembly.add("M=D");

            // increment the stack pointer
            assembly.add("@SP");
            assembly.add("M=M+1");
        } else {
            // find the address to put the value in
            switch(variableType) {
                case "temp":
                    assembly.add("@" + (5 + Integer.parseInt(value)));
                    assembly.add("D=A");
                    assembly.add("@15");
                    assembly.add("M=D");
                    break;
                case "pointer":
                    assembly.add("@" + (3 + Integer.parseInt(value)));
                    assembly.add("D=A");
                    assembly.add("@15");
                    assembly.add("M=D");
                    break;
                case "static":
                    assembly.add("@" + currentFileName + "." + Integer.parseInt(value));
                    assembly.add("D=A");
                    assembly.add("@15");
                    assembly.add("M=D");
                    break;
                default:
                    assembly.add("@" + baseMemoryAddresses.get(variableType));
                    assembly.add("D=M");
                    assembly.add("@" + Integer.parseInt(value));
                    assembly.add("D=D+A");
                    assembly.add("@15");
                    assembly.add("M=D");
            }

            // decrement the stack pointer
            assembly.add("@SP");
            assembly.add("AM=M-1");

            // load the value on the stack into the D register
            assembly.add("D=M");

            // put the value in the correct place
            assembly.add("@15");
            assembly.add("A=M");
            assembly.add("M=D");
        }
    }
}
