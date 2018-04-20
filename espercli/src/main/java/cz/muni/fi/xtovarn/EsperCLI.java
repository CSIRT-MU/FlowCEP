package cz.muni.fi.xtovarn;

import org.kohsuke.args4j.Option;

import java.util.Arrays;
import java.util.List;

public class EsperCLI {

    @Option(name = "-m", required = true, usage = "Esper EPL module definition (required)", metaVar = "STRING")
    private String module;

    @Option(name = "-i", required = false, usage = "Input file (default STDIN)", metaVar = "FILE")
    private String input;

	@Option(name = "-o", required = true, usage = "List statements to produce output into STDOUT", metaVar = "STRING")
	private String output;

    public String getModule() {
        return module;
    }

    public String getInput() {
        return input;
    }

    public List<String> getOutput() {
        String[] split = output.split(",\\s?");
        return Arrays.asList(split);
    }
}
