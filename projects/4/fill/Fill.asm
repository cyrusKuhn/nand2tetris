// This file is part of www.nand2tetris.org
// and the book "The Elements of Computing Systems"
// by Nisan and Schocken, MIT Press.
// File name: projects/4/Fill.asm

// Runs an infinite loop that listens to the keyboard input. 
// When a key is pressed (any key), the program blackens the screen,
// i.e. writes "black" in every pixel. When no key is pressed, 
// the screen should be cleared.

(START)
// iterate through the keyboard

// once the keyboard has been iterated through
// if there is a key pressed, set the screen to black
// else set the screen to white
// go back to start

(RESTART)
    @16384 // Initialize the SCREEN variable
    D=A
    @SCREEN
    M=D

    @24576 // If the keyboard is pressed, lighten
    D=M
    @STARTLIGHTEN
    D;JEQ

    @STARTBLACKEN // If the keyboard is not pressed, blacken




(STARTBLACKEN)
    @SCREEN // Get the start of the screen
    D=A
    @CURRENTPIXEL // Set the current pixel to be the start of the screen
    M=D
    @8192 // Determine when the loop should exit (at the last pixel)
    D=D+A
    @FINALPIXEL
    M=D

(BLACKEN)
    @CURRENTPIXEL // put the value of current pixel into M, put the pointer to current pixel in A
    A=M // put the value of current pixel into A
    M=-1 // turn off the current pixel

    D=A+1 // increase the value of the current pixel
    @CURRENTPIXEL // Store that value in the variabe current pixel
    M=D

    // if the last pixel has been reached, exit
    @FINALPIXEL
    D=D-M
    @RESTART
    D;JGE
    // else repeat
    @BLACKEN
    0;JMP

(STARTLIGHTEN)
    @SCREEN // Get the start of the screen
    D=A
    @CURRENTPIXEL // Set the current pixel to be the start of the screen
    M=D
    @8192 // Determine when the loop should exit (at the last pixel)
    D=D+A
    @FINALPIXEL
    M=D

(LIGHTEN)
    @CURRENTPIXEL // put the value of current pixel into M, put the pointer to current pixel in A
    A=M // put the value of current pixel into A
    M=0 // turn on the current pixel

    D=A+1 // increase the value of the current pixel
    @CURRENTPIXEL // Store that value in the variabe current pixel
    M=D

    // if the last pixel has been reached, exit
    @FINALPIXEL
    D=D-M
    @RESTART
    D;JGE
    // else repeat
    @LIGHTEN
    0;JMP