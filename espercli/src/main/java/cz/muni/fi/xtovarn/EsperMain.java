package cz.muni.fi.xtovarn;

import com.espertech.esper.client.*;
import com.espertech.esper.client.annotation.Tag;
import com.espertech.esper.client.deploy.DeploymentException;
import com.espertech.esper.client.deploy.Module;
import com.espertech.esper.client.deploy.ParseException;
import com.espertech.esper.client.time.CurrentTimeEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.ISO8601Utils;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.OptionHandlerFilter;

import java.io.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.text.ParsePosition;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class EsperMain {

    public static void main(String[] args) throws IOException, ParseException, DeploymentException {
        EsperCLI esperCLI = new EsperCLI();
        CmdLineParser parser = new CmdLineParser(esperCLI);
        try {
            parser.parseArgument(args);
        } catch (CmdLineException e) {
            System.err.println(e.getMessage());
            System.err.println("espercli [options...]");
            parser.printUsage(System.err);
            System.err.println();
            // print option sample. This is useful some time
            System.err.println("Example: espercli" + parser.printExample(OptionHandlerFilter.REQUIRED));
            System.exit(1);
        }

        Configuration configuration = new Configuration();
        configuration.getEngineDefaults().getThreading().setInternalTimerEnabled(false);
        EPServiceProvider epService = EPServiceProviderManager.getDefaultProvider(configuration);

        Module module = epService.getEPAdministrator().getDeploymentAdmin().read(new File(esperCLI.getModule()));
        epService.getEPAdministrator().getDeploymentAdmin().deploy(module, null);
        epService.getEPAdministrator().getStatement("Output").addListener(new SystemOutListener());

        EPStatement schemaStatement = epService.getEPAdministrator().getStatement("Schema");
        EventSender eventSender = epService.getEPRuntime().getEventSender(schemaStatement.getEventType().getName());

        Map<String, String> tags = new HashMap<>();
        for (Annotation annotation : schemaStatement.getAnnotations()) {
            if (annotation instanceof Tag) {
                Tag tag = (Tag) annotation;
                tags.put(tag.name(), tag.value());
            }
        }

        String timestamp = tags.get("timestamp");

        Reader in;
        if (esperCLI.getInput() == null) {
            in = new InputStreamReader(System.in);
        } else {
            in = new FileReader(esperCLI.getInput());
        }

        BufferedReader br = new BufferedReader(in);
        JSONFlattener jsonFlattener = new JSONFlattener(new ObjectMapper());

        String line;
        while ((line = br.readLine()) != null) {
            Map<String, Object> theEvent = jsonFlattener.jsonToFlatMap(line);
            try {
//		System.out.println(theEvent.get(timestamp));
//                Date date = ISO8601Utils.parse((String) theEvent.get(timestamp), new ParsePosition(0));
 //               epService.getEPRuntime().sendEvent(new CurrentTimeEvent(date.getTime()));
		epService.getEPRuntime().sendEvent(new CurrentTimeEvent((long) theEvent.get(timestamp)));
                eventSender.sendEvent(theEvent);
            } catch (Exception e) {
                System.err.printf("Invalid date format or the timestamp attribute does not exist: '%s'. Exiting%n", timestamp);
                System.exit(1);
            }
        }
        in.close();
    }
}
