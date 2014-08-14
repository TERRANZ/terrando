package ru.terra.ndo.server.entity;

/**
 * Date: 26.06.14
 * Time: 15:30
 */
public class PhotoInfo {
    private Double lon, lat;
    private String photo;
    private Boolean respond;

    public PhotoInfo() {
    }

    public PhotoInfo(Double lon, Double lat, String photo, Boolean respond) {
        this.lon = lon;
        this.lat = lat;
        this.photo = photo;
        this.respond = respond;
    }

    public Boolean getRespond() {
        return respond;
    }

    public void setRespond(Boolean respond) {
        this.respond = respond;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    @Override
    public String toString() {
        return "PhotoInfo{" +
                "lon=" + lon +
                ", lat=" + lat +
                ", photo='" + photo + '\'' +
                ", respond=" + respond +
                '}';
    }
}
