package com.josemillanes.covidassist;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Evento implements Serializable {

    private int eventId;
    private String eventTitle;
    private String eventDescription;
    private String eventPlace;
    private Date eventDate;
    private String eventStatus;
    private int eventCapacity;
    private int eventCreator;
    private boolean eventContagio;
    private List<Usuario> eventAttendance;

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public String getEventTitle() {
        return eventTitle;
    }

    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
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

    public int getEventCreator() {
        return eventCreator;
    }

    public void setEventCreator(int eventCreator) {
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

    public Evento(int eventId, String eventTitle, String eventDescription, String eventPlace, Date eventDate, String eventStatus, int eventCapacity, int eventCreator, boolean eventContagio) {
        this.eventId = eventId;                     //int
        this.eventTitle = eventTitle;               //String
        this.eventDescription = eventDescription;   //String
        this.eventPlace = eventPlace;               //String
        this.eventDate = eventDate;                 //Date
        this.eventStatus = eventStatus;             //String
        this.eventCapacity = eventCapacity;         //int
        this.eventCreator = eventCreator;           //int
        this.eventContagio = eventContagio;         //bool
        //this.eventAttendance = eventAttendance;   //List<Usuario>
    }

    public Evento(String eventTitle, String eventDescription, String eventPlace, Date eventDate, String eventStatus, int eventCapacity, int eventCreator, boolean eventContagio, List<Usuario> eventAttendance) {
        this.eventTitle = eventTitle;
        this.eventDescription = eventDescription;
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