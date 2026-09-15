package org.cnv0;

import org.cnv0.parse.Parser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Main {
    public static void main(String[] args) {
        Parser parser0 = new Parser("../add/Add.asm");
        Path output0 = Path.of("../add/Add.hack");
        try {
            Files.writeString(output0, parser0.getOutput());
        } catch (IOException e) {
            System.err.println("Could not write to file");
        }

        Parser parser1 = new Parser("../max/Max.asm");
        Path output1 = Path.of("../max/Max.hack");
        try {
            Files.writeString(output1, parser1.getOutput());
        } catch (IOException e) {
            System.err.println("Could not write to file");
        }
        Parser parser2 = new Parser("../max/MaxL.asm");
        Path output2 = Path.of("../max/MaxL.hack");
        try {
            Files.writeString(output2, parser2.getOutput());
        } catch (IOException e) {
            System.err.println("Could not write to file");
        }

        Parser parser3 = new Parser("../rect/Rect.asm");
        Path output3 = Path.of("../rect/Rect.hack");
        try {
            Files.writeString(output3, parser3.getOutput());
        } catch (IOException e) {
            System.err.println("Could not write to file");
        }
        Parser parser4 = new Parser("../rect/RectL.asm");
        Path output4 = Path.of("../rect/RectL.hack");
        try {
            Files.writeString(output4, parser4.getOutput());
        } catch (IOException e) {
            System.err.println("Could not write to file");
        }

        Parser parser5 = new Parser("../pong/Pong.asm");
        Path output5 = Path.of("../pong/Pong.hack");
        try {
            Files.writeString(output5, parser5.getOutput());
        } catch (IOException e) {
            System.err.println("Could not write to file");
        }
        Parser parser6 = new Parser("../pong/PongL.asm");
        Path output6 = Path.of("../pong/PongL.hack");
        try {
            Files.writeString(output6, parser6.getOutput());
        } catch (IOException e) {
            System.err.println("Could not write to file");
        }
    }
}