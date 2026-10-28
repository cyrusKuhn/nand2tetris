package org.cnv0.translate.instructions;

public class GotoInstruction extends Instruction {
    private String label;
    private boolean conditional;

    public GotoInstruction(String label, boolean conditional) {
        this.label = label;
        this.conditional = conditional;
        this.translate();
    }

    public void translate() {
        String jmp = "JMP";
        if (conditional) {
            // pop the value off of the stack
            assembly.add("@SP");
            assembly.add("AM=M-1");
            assembly.add("D=M");

            // jump if true
            jmp = "JNE";
        }

        assembly.add("@" + label + "." + currentFileName);
        assembly.add("D;" + jmp);
    }
}
