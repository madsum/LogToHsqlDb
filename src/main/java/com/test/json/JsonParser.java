package com.test.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.domain.Event;
import com.test.domain.EventRecord;
import com.test.domain.State;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class JsonParser {

    private static Map<String, EventRecord> eventRecords = new HashMap<>();

    public static Map<String, EventRecord> parseFile(File file) throws IOException {
        FileInputStream inputStream;
        inputStream = new FileInputStream(file);

        try (Scanner sc = new Scanner(inputStream, "UTF-8");){
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                if(line.trim().isEmpty()){
                    continue;
                }
                parseProcessAndStoreJson(line);
            }
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
