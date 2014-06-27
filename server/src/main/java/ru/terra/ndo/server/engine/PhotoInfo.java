package ru.terra.ndo.server.engine;

/**
 * Date: 26.06.14
 * Time: 15:30
 */
public class PhotoInfo {
    public String uid;
    public Double lon, lat;
    public String photo;

    public PhotoInfo() {
    }

    public PhotoInfo(String uid, Double lon, Double lat, String photo) {
        this.uid = uid;
        this.lon = lon;
        this.lat = lat;
        this.photo = photo;
    }

    @Override
    public String toString() {
        return "PhotoInfo{" +
                "uid='" + uid + '\'' +
                ", lon=" + lon +
                ", lat=" + lat +
                ", photo='" + photo + '\'' +
                '}';
    }
}
