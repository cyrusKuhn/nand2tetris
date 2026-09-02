// This file is part of www.nand2tetris.org
// and the book "The Elements of Computing Systems"
// by Nisan and Schocken, MIT Press.
// File name: projects/4/Mult.asm

// Multiplies R0 and R1 and stores the result in R2.
// (R0, R1, R2 refer to RAM[0], RAM[1], and RAM[2], respectively.)
// The algorithm is based on repetitive addition.

//// Replace this comment with your code.

(RESTART)
    @MASK
    M=1
    @3
    M=0

    @0
    D=M
    @MODIFIEDA
    M=D

    @1
    D=M
    @MODIFIEDB
    M=D

    @2
    M=0

(MULTIPLY)
    // skip add
    @MASK
    D=M
    @0
    D=D&M
    @SKIPADD
    D;JEQ

    // add the value of the multiplied b
    @MODIFIEDB
    D=M
    @2
    M=M+D

    @MASK
    D=M
    @MODIFIEDA
    M=M-D

    D=M
    @END
    D;JEQ
    
(SKIPADD)
    @MODIFIEDB
    D=M
    M=D+M
    @MASK
    D=M
    M=D+M

    // if not zero repeat
    D=M
    @MULTIPLY
    D;JNE

(END)
    @END
    0;JMP