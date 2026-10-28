package org.cnv0.translate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Instruction {
    protected static int currentLabelCount;
    protected static String currentFileName;

    protected List<String> assembly = new ArrayList<>();
    public List<String> getAssembly() {
        return assembly;
    }

    public abstract void translate();


    public static class OperationInstruction extends Instruction {
        private String operation;

        public OperationInstruction(String operation) {
            this.operation = operation;
            this.translate();
        }

        @Override
        public void translate() {
            String operationChar = switch (operation) {
                case "add" -> "D+M";
                case "sub" -> "M-D";
                case "and" -> "D&M";
                case "or" -> "D|M";

                case "not" -> "!";
                case "neg" -> "-";

                case "eq" -> "JEQ";
                case "gt" -> "JGT";
                case "lt" -> "JLT";

                default -> throw new IllegalArgumentException("No Operation " + operation);
            };

            String type = switch (operation) {
                case "add", "sub", "and", "or" -> "doubleValue";
                case "neg", "not" -> "singleValue";
                case "eq", "gt", "lt" -> "comparison";
                default -> throw new IllegalArgumentException("No Operation " + operation);
            };

            if (type.equals("doubleValue")) {
                // load sp - 1 to d
                assembly.add("@SP");
                assembly.add("A=M-1");
                assembly.add("D=M");

                // add load sp - 2
                assembly.add("A=A-1");

                // do the operation
                assembly.add("M=" + operationChar);

                // decrement the stack pointer
                assembly.add("@SP");
                assembly.add("M=M-1");
            } else if (type.equals("singleValue")) {
                // get to sp - 1
                assembly.add("@SP");
                assembly.add("A=M-1");

                // apply the operation
                assembly.add("M=" + operationChar + "M");
            } else {
                assembly.add("@SP");
                assembly.add("AM=M-1");
                assembly.add("D=M");

                assembly.add("A=A-1");
                assembly.add("D=M-D");

                // jump to true if true
                assembly.add("@COMPARE_TRUE" + currentLabelCount);
                assembly.add("D;" + operationChar);

                // false case
                assembly.add("@SP");
                assembly.add("A=M-1");
                assembly.add("M=0");
                // skip true
                assembly.add("@COMPARE_END" + currentLabelCount);
                assembly.add("0;JMP");

                // true case
                assembly.add("(COMPARE_TRUE" + currentLabelCount + ")");
                assembly.add("@SP");
                assembly.add("A=M-1");
                assembly.add("M=-1");

                // end
                assembly.add("(COMPARE_END" + currentLabelCount++ + ")");
            }
        }
    }

    public static class PushPopInstruction extends Instruction {
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
            if (initialized) return;
            initialized = true;

            baseMemoryAddresses.put("local", 1);
            baseMemoryAddresses.put("argument", 2);
            baseMemoryAddresses.put("this", 3);
            baseMemoryAddresses.put("that", 4);
        }

        @Override
        public void translate() {
            init();
            if (pushPop.equals("push")) {
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
                switch (variableType) {
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
                assembly.add("M=M-1");

                // load the value on the stack into the D register
                assembly.add("@SP");
                assembly.add("A=M");
                assembly.add("D=M");

                // put the value in the correct place
                assembly.add("@15");
                assembly.add("A=M");
                assembly.add("M=D");
            }
        }
    }
}
