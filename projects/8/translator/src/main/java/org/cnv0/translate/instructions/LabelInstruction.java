package org.cnv0.translate.instructions;

public class LabelInstruction extends Instruction {
    private String label;

    public LabelInstruction(String label) {
        this.label = label;
        this.translate();
    }

    public void translate() {
        assembly.add("(" + label + "." + currentFileName + ")");
    }
}
