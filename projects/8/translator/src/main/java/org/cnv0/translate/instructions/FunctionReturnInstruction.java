package org.cnv0.translate.instructions;

public class FunctionReturnInstruction extends Instruction {
    public FunctionReturnInstruction() { translate(); }

    public void translate() {
        // frame = LCL (frame in R13)
        assembly.add("@LCL");
        assembly.add("D=M");
        assembly.add("@R13");
        assembly.add("M=D");

        // return address = value at frame - 5 (frame currently in D) (return address in R14)
        assembly.add("@5");
        assembly.add("A=D-A");
        assembly.add("D=M");
        assembly.add("@R14");
        assembly.add("M=D");

        // value in arg = top of the stack
        assembly.add("@SP");
        assembly.add("AM=M-1");
        assembly.add("D=M");
        assembly.add("@ARG");
        assembly.add("A=M");
        assembly.add("M=D");

        // SP = ARG + 1
        assembly.add("@ARG");
        assembly.add("D=M+1");
        assembly.add("@SP");
        assembly.add("M=D");

        // THAT = value at frame - 1
        assembly.add("@R13");
        assembly.add("AM=M-1");
        assembly.add("D=M");
        assembly.add("@THAT");
        assembly.add("M=D");

        // THIS = value at frame - 2
        assembly.add("@R13");
        assembly.add("AM=M-1");
        assembly.add("D=M");
        assembly.add("@THIS");
        assembly.add("M=D");

        // ARG = value at frame - 3
        assembly.add("@R13");
        assembly.add("AM=M-1");
        assembly.add("D=M");
        assembly.add("@ARG");
        assembly.add("M=D");

        // LCL = value at frame - 4
        assembly.add("@R13");
        assembly.add("AM=M-1");
        assembly.add("D=M");
        assembly.add("@LCL");
        assembly.add("M=D");

        // go to RET
        assembly.add("@R14");
        assembly.add("A=M");
        assembly.add("0;JMP");
    }
}
