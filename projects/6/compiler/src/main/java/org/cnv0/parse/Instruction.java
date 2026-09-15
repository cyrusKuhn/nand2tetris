package org.cnv0.parse;

public abstract class Instruction {
    protected String binary;
    public String getBinary() {
        return binary;
    }

    public abstract void assemble();


    public static class AInstruction extends Instruction {
        private String attachedLabel;
        private int value;

        public AInstruction(String attachedLabel) {
            this.attachedLabel = attachedLabel;
        }

        public AInstruction(int value) {
            this.value = value;
        }

        public void setValue(int value) {
            this.value = value;
        }

        public String getLabel() {
            return attachedLabel;
        }

        @Override
        public void assemble() {
            binary = toBinary(value, 16);
        }
    }

    public static class CInstruction extends Instruction {
        private String dest;
        private String operation;
        private String jump;

        public CInstruction(String dest, String operation, String jump) {
            this.dest = dest;
            this.operation = operation;
            this.jump = jump;
        }

        @Override
        public void assemble() {
            binary = "111";
            binary += computationSection(operation);
            binary += destinationSection(dest);
            binary += jumpSection(jump);
        }

        private static String computationSection(String operation) {
            return switch (operation) {
                case "0" -> "0101010";
                case "1" -> "0111111";
                case "-1" -> "0111010";

                case "D" -> "0001100";
                case "!D" -> "0001101";
                case "-D" -> "0001111";

                case "A" -> "0110000";
                case "!A" -> "0110001";
                case "-A" -> "0110011";

                case "D+1" -> "0011111";
                case "D-1" -> "0001110";

                case "A+1" -> "0110111";
                case "A-1" -> "0110010";

                case "D+A" -> "0000010";
                case "D-A" -> "0010011";
                case "A-D" -> "0000111";
                case "D&A" -> "0000000";
                case "D|A" -> "0010101";

                case "M" -> "1110000";
                case "!M" -> "1110001";
                case "-M" -> "1110011";

                case "M+1" -> "1110111";
                case "M-1" -> "1110010";

                case "D+M" -> "1000010";
                case "D-M" -> "1010011";
                case "M-D" -> "1000111";
                case "D&M" -> "1000000";
                case "D|M" -> "1010101";

                default -> throw new IllegalArgumentException("Invalid computation: " + operation);
            };
        }

        private static String destinationSection(String dest) {
            String binary = "";
            binary += dest.contains("A") ? 1 : 0;
            binary += dest.contains("D") ? 1 : 0;
            binary += dest.contains("M") ? 1 : 0;
            return binary;
        }

        private static String jumpSection(String jump) {
            if(jump == null) return "000";
            return switch (jump) {
                case "JGT" -> "001";
                case "JEQ" -> "010";
                case "JGE" -> "011";
                case "JLT" -> "100";
                case "JNE" -> "101";
                case "JLE" -> "110";
                case "JMP" -> "111";
                default -> "000";
            };
        }
    }


    protected static String toBinary(int value, int size) {
        if (value > 32767 || value < 0) throw new IllegalArgumentException("Invalid Value: " + value);
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < size; i++) {
            output.insert(0, value % 2);
            value /= 2;
        }
        return output.toString();
    }
}
