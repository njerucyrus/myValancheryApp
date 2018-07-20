package com.hudutech.mymanjeri.models;

import java.io.Serializable;

public class Bus implements Serializable {
    private static final long serialVersionUID = 1L;

    private String docKey;
    private String busName;
    private String startPoint;
    private String endPoint;
    private String way;
    private String departureTime;
    private String arrivalTime;
    private boolean isValidated;

    public Bus() {

    }

    public Bus(String docKey, String busName, String startPoint, String endPoint, String way, String departureTime, String arrivalTime, boolean isValidated) {
        this.docKey = docKey;
        this.busName = busName;
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.way = way;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.isValidated = isValidated;
    }

    public String getDocKey() {
        return docKey;
    }

    public void setDocKey(String docKey) {
        this.docKey = docKey;
    }

    public String getBusName() {
        return busName;
    }

    public void setBusName(String busName) {
        this.busName = busName;
    }

    public String getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(String startPoint) {
        this.startPoint = startPoint;
    }

    public String getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(String endPoint) {
        this.endPoint = endPoint;
    }

    public String getWay() {
        return way;
    }

    public void setWay(String way) {
        this.way = way;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public boolean isValidated() {
        return isValidated;
    }

    public void setValidated(boolean validated) {
        isValidated = validated;
    }

    @Override
    public String toString() {
        return "Bus{" +
                "docKey='" + docKey + '\'' +
                ", busName='" + busName + '\'' +
                ", startPoint='" + startPoint + '\'' +
                ", endPoint='" + endPoint + '\'' +
                ", way='" + way + '\'' +
                ", departureTime='" + departureTime + '\'' +
                ", arrivalTime='" + arrivalTime + '\'' +
                ", isValidated=" + isValidated +
                '}';
    }
}
