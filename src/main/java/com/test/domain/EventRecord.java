package com.test.domain;

public class EventRecord {
    private String id;
    private String type;
    private String host;
    private Event started;
    private Event finished;
    private long duration;
    private boolean alert = false;

    public EventRecord(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Event getStarted() {
        return started;
    }

    public void setStarted(Event started) {
        this.started = started;
    }

    public Event getFinished() {
        return finished;
    }

    public void setFinished(Event finished) {
        this.finished = finished;
    }

    public int isAlert() {
        if(alert == true){
            return 1;
        }else{
            return 0;
        }
    }

    public void setAlert(boolean alert) {
        this.alert = alert;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void print() {
        System.out.println(id + "\t" + duration+ "\t" + alert+ "\t" + host+ "\t" + type);
    }
}
