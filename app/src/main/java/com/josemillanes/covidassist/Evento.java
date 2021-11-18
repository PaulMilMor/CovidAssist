package com.josemillanes.covidassist;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Evento {

    private String eventId;
    private String eventTitle;
    private String eventPlace;
    private Date eventDate;
    private String eventStatus;
    private int eventCapacity;
    private String eventCreator;
    private boolean eventContagio;
    private List<Usuario> eventAttendance;

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getEventTitle() {
        return eventTitle;
    }

    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
    }

    public String getEventPlace() {
        return eventPlace;
    }

    public void setEventPlace(String eventPlace) {
        this.eventPlace = eventPlace;
    }

    public Date getEventDate() {
        return eventDate;
    }

    public void setEventDate(Date eventDate) {
        this.eventDate = eventDate;
    }

    public String getEventStatus() {
        return eventStatus;
    }

    public void setEventStatus(String eventStatus) {
        this.eventStatus = eventStatus;
    }

    public int getEventCapacity() {
        return eventCapacity;
    }

    public void setEventCapacity(int eventCapacity) {
        this.eventCapacity = eventCapacity;
    }

    public String getEventCreator() {
        return eventCreator;
    }

    public void setEventCreator(String eventCreator) {
        this.eventCreator = eventCreator;
    }

    public boolean isEventContagio() {
        return eventContagio;
    }

    public void setEventContagio(boolean eventContagio) {
        this.eventContagio = eventContagio;
    }

    public List<Usuario> getEventAttendance() {
        return eventAttendance;
    }

    public void setEventAttendance(List<Usuario> eventAttendance) {
        this.eventAttendance = eventAttendance;
    }

    public Evento(String eventId, String eventTitle, String eventPlace, Date eventDate, String eventStatus, int eventCapacity, String eventCreator, boolean eventContagio, List<Usuario> eventAttendance) {
        this.eventId = eventId;
        this.eventTitle = eventTitle;
        this.eventPlace = eventPlace;
        this.eventDate = eventDate;
        this.eventStatus = eventStatus;
        this.eventCapacity = eventCapacity;
        this.eventCreator = eventCreator;
        this.eventContagio = eventContagio;
        this.eventAttendance = eventAttendance;
    }

    public Evento(String eventTitle, String eventPlace, Date eventDate, String eventStatus, int eventCapacity, String eventCreator, boolean eventContagio, List<Usuario> eventAttendance) {
        this.eventTitle = eventTitle;
        this.eventPlace = eventPlace;
        this.eventDate = eventDate;
        this.eventStatus = eventStatus;
        this.eventCapacity = eventCapacity;
        this.eventCreator = eventCreator;
        this.eventContagio = eventContagio;
        this.eventAttendance = eventAttendance;
    }

    public Map<String, Object> toMap(){
        Map<String, Object> eventMap = new HashMap<>();
        eventMap.put("eventTitle",eventTitle);
        eventMap.put("eventPlace",eventPlace);
        eventMap.put("eventDate",eventDate);
        eventMap.put("eventStatus",eventStatus);
        eventMap.put("eventCapacity",eventCapacity);
        eventMap.put("eventCreator",eventCreator);
        eventMap.put("eventContagio",eventContagio);
        eventMap.put("eventAttendance",eventAttendance);
        return eventMap;
    }
}
