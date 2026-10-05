package org.cnv0;

import org.cnv0.translate.Translator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Main {
    public static void main(String[] args) {// vm file to asm file
        Translator translator0 = new Translator("../StackArithmetic/SimpleAdd/SimpleAdd.vm");
        Path output0 = Path.of("../StackArithmetic/SimpleAdd/SimpleAdd.asm");
        try {
            Files.writeString(output0, translator0.getOutput());
        } catch (IOException e) {
            System.err.println("Could not write to file");
        }

        Translator translator1 = new Translator("../StackArithmetic/StackTest/StackTest.vm");
        Path output1 = Path.of("../StackArithmetic/StackTest/StackTest.asm");
        try {
            Files.writeString(output1, translator1.getOutput());
        } catch (IOException e) {
            System.err.println("Could not write to file");
        }

        Translator translator2 = new Translator("../MemoryAccess/BasicTest/BasicTest.vm");
        Path output2 = Path.of("../MemoryAccess/BasicTest/BasicTest.asm");
        try {
            Files.writeString(output2, translator2.getOutput());
        } catch (IOException e) {
            System.err.println("Could not write to file");
        }

        Translator translator3 = new Translator("../MemoryAccess/PointerTest/PointerTest.vm");
        Path output3 = Path.of("../MemoryAccess/PointerTest/PointerTest.asm");
        try {
            Files.writeString(output3, translator3.getOutput());
        } catch (IOException e) {
            System.err.println("Could not write to file");
        }

        Translator translator4 = new Translator("../MemoryAccess/StaticTest/StaticTest.vm");
        Path output4 = Path.of("../MemoryAccess/StaticTest/StaticTest.asm");
        try {
            Files.writeString(output4, translator4.getOutput());
        } catch (IOException e) {
            System.err.println("Could not write to file");
        }
    }
}