package org.cnv0.translate.instructions;

import java.util.ArrayList;
import java.util.List;

public abstract class Instruction {
    public static int currentLabelCount;
    public static String currentFileName;

    protected List<String> assembly = new ArrayList<>();
    public List<String> getAssembly() {
        return assembly;
    }

    public abstract void translate();
}
