package org.cnv0;

import org.cnv0.translate.Translator;
import org.cnv0.translate.instructions.Instruction;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Main {
    public static void main(String[] args) {
        translate("../ProgramFlow/BasicLoop/", new String[]{"BasicLoop"}, "BasicLoop");
        translate("../ProgramFlow/FibonacciSeries/", new String[]{"FibonacciSeries"}, "FibonacciSeries");
        translate("../FunctionCalls/FibonacciElement/", new String[]{"Sys", "Main"}, "FibonacciElement");
        translate("../FunctionCalls/NestedCall/", new String[]{"Sys"}, "NestedCall");
        translate("../FunctionCalls/SimpleFunction/", new String[]{"SimpleFunction"}, "SimpleFunction");
        translate("../FunctionCalls/StaticsTest/", new String[]{"Sys", "Class1", "Class2"}, "StaticsTest");
    }

    private static void translate(String filepath, String[] files, String outputLocation) {
        Instruction.currentLabelCount = 0;
        String output = "";

        for (int i = 0; i < files.length; i++) {
            Translator translator = new Translator(filepath + files[i] + ".vm");
            output += translator.getOutput() + "\n";
        }

        Path outputPath = Path.of(filepath + outputLocation + ".asm");
        try {
            Files.writeString(outputPath, output);
        } catch(IOException e) {
            System.err.println("Could not write to file");
        }
    }
}