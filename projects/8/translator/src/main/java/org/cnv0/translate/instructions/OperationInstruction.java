package org.cnv0.translate.instructions;

public class OperationInstruction extends Instruction {
    private String operation;

    public OperationInstruction(String operation) {
        this.operation = operation;
        this.translate();
    }

    @Override
    public void translate() {
        String operationChar = switch(operation) {
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

        String type = switch(operation) {
            case "add", "sub", "and", "or" -> "doubleValue";
            case "neg", "not" -> "singleValue";
            case "eq", "gt", "lt" -> "comparison";
            default -> throw new IllegalArgumentException("No Operation " + operation);
        };

        if(type.equals("doubleValue")) {
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
        } else if(type.equals("singleValue")) {
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
