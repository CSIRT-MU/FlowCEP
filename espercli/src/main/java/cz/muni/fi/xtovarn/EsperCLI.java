package cz.muni.fi.xtovarn;

import org.kohsuke.args4j.Option;

public class EsperCLI {

    @Option(name = "-m", required = true, usage = "Esper EPL module definition (required)", metaVar = "STRING")
    private String module;

    @Option(name = "-i", required = false, usage = "Input file (default STDIN)", metaVar = "FILE")
    private String input;

    public String getModule() {
        return module;
    }

    public String getInput() {
        return input;
    }
}
