package org.cnv0.translate.instructions;

public class FunctionDeclarationInstruction extends Instruction {
    private String name;
    private int localCount;

    public FunctionDeclarationInstruction(String name, int localCount) {
        this.name = name;
        this.localCount = localCount;
        this.translate();
    }

    public void translate() {
        // add a label
        assembly.add("(" + name + ")");
        // push localCount constants
        for (int i = 0; i < localCount; i++) assembly.addAll((new PushPopInstruction("push", "constant", "0")).assembly);
    }
}
