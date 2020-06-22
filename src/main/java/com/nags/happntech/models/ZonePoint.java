package com.nags.happntech.models;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

@Value
public class ZonePoint {

    @JsonProperty("min_lat")
    private double minLat;

    @JsonProperty("max_lat")
    private double maxLat;

    @JsonProperty("min_lon")
    private double minLon;

    @JsonProperty("max_lon")
    private double maxLon;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ZonePoint zonePoint = (ZonePoint) o;
        return Double.compare(zonePoint.minLat, minLat) == 0 &&
            Double.compare(zonePoint.maxLat, maxLat) == 0 &&
            Double.compare(zonePoint.minLon, minLon) == 0 &&
            Double.compare(zonePoint.maxLon, maxLon) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(minLat, maxLat, minLon, maxLon);
    }

    @Override
    public String toString() {
        return "{" +
            "min_lat : " + minLat +
            ", max_lat : " + maxLat +
            ", min_lon : " + minLon +
            ", max_lon : " + maxLon +
            '}';
    }
}
