package cz.muni.fi.xtovarn;

import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPStatement;
import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.StatementAwareUpdateListener;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.IOException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Map;
import java.util.TimeZone;

public class SystemOutListener implements StatementAwareUpdateListener {
    private ObjectMapper objectMapper = new ObjectMapper();

    public SystemOutListener() {
        this.objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    @Override
    public void update(EventBean[] newEvents, EventBean[] oldEvents, EPStatement statement, EPServiceProvider epServiceProvider) {
        for (EventBean newEvent : newEvents) {
            try {
                Map<String, Object> map = (Map<String, Object>) newEvent.getUnderlying();
                Instant instant = new Date(epServiceProvider.getEPRuntime().getCurrentTime()).toInstant();
                ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(instant, TimeZone.getDefault().toZoneId());
                map.put("timestamp", zonedDateTime.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));
                System.out.println(this.objectMapper.writeValueAsString(map));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
