package com.test.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.domain.Event;
import com.test.domain.EventRecord;
import com.test.domain.State;
import org.apache.commons.io.FileUtils;
import org.hsqldb.lib.FileUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class JsonParser {

    private static Map<String, EventRecord> eventRecords = new HashMap<>();

    public static Map<String, EventRecord> parseFile(File file) {
        try {
            List<String> lines = FileUtils.readLines(file, StandardCharsets.UTF_8);
            lines.forEach(line -> {
                try {
                    parseProcessAndStoreJson(line);
                } catch (JsonProcessingException e) {
                    System.out.println("Exception: "+e.getMessage());
                    System.exit(-1);
                }
            });
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return eventRecords;
    }

    public static void parseProcessAndStoreJson(String json) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();

        Event event = objectMapper.readValue(json, Event.class);

        EventRecord eventRecord;
        if(eventRecords.keySet().contains(event.getId())){
            eventRecord = eventRecords.get(event.getId());
        } else {
            eventRecord = new EventRecord(event.getId());
            eventRecords.put(event.getId(), eventRecord);
        }

        if (event.getState() == State.STARTED) {
            eventRecord.setStarted(event);
        } else {
            eventRecord.setFinished(event);
        }

        if (eventRecord.getStarted() != null && eventRecord.getFinished() !=null) {
            processData(eventRecord);
        }
    }

    public static void processData(EventRecord eventRecord) {
        eventRecord.setDuration(eventRecord.getFinished().getTimestamp() - eventRecord.getStarted().getTimestamp());

        if(eventRecord.getDuration() >= 4) {
            eventRecord.setAlert(true);
        }

        if (eventRecord.getStarted() != null && eventRecord.getHost() == null) {
            eventRecord.setHost(eventRecord.getStarted().getHost());
        }

        if (eventRecord.getFinished() != null && eventRecord.getHost() == null) {
            eventRecord.setHost(eventRecord.getFinished().getHost());
        }

        if (eventRecord.getStarted() != null && eventRecord.getType() == null) {
            eventRecord.setType(eventRecord.getStarted().getType());
        }

        if (eventRecord.getFinished() != null && eventRecord.getType() == null) {
            eventRecord.setType(eventRecord.getFinished().getType());
        }
        // In case the file is too big, clearing extra memory.
        eventRecord.setStarted(null);
        eventRecord.setFinished(null);
    }

    public static Map<String, EventRecord> getEventRecords() {
        return eventRecords;
    }
}
