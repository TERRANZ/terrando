package ru.terra.ndo.server.dto;

import ru.terra.server.dto.CommonDTO;

/**
 * Date: 26.06.14
 * Time: 15:12
 */
public class PhotoDTO extends CommonDTO {
    public String url = "";
    public Double lon = 0d, lat = 0d;

    public PhotoDTO() {
    }

    public PhotoDTO(String url, Double lon, Double lat) {
        this.url = url;
        this.lon = lon;
        this.lat = lat;
    }
}
