package com.nags.happntech.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.nags.happntech.models.ZonePoint;
import com.nags.happntech.repositories.ZonesRepository;
import com.nags.happntech.repositories.entities.ZonePointEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class DensityService {

    private final ZonesRepository repo;

    private final Double ZONE_SIZE = 0.5;

    /**
     * Get the density in a given position
     *
     * @param minLat
     * @param minLon
     * @return Nb density
     */
    public Long getDensity(Double minLat, Double minLon) {
        return this.repo.findAll()
            .stream()
            .filter(c -> c.getLat() > minLat && c.getLat() <= (minLat + ZONE_SIZE)
                && c.getLon() > minLon && c.getLon() <= (minLon + ZONE_SIZE))
            .count();
    }

    /**
     * Get zones with the most density limited by the param limit
     *
     * @param limit
     * @return List ZonePoint
     */
    public List<ZonePoint> getTopDensityZonePoints(Integer limit) {
        List<ZonePointEntity> entities = this.repo.findAll();
        Map<ZonePoint, Long> zoneMap = this.buildZoneMap(entities);

        return getTopDensityZonePoints(zoneMap, limit);
    }

    /**
     * Return the list of zones with the most density limited by the param limit
     *
     * @param zoneMap
     * @param limit
     * @return List<ZonePoint>
     */
    private List<ZonePoint> getTopDensityZonePoints(Map<ZonePoint, Long> zoneMap, Integer limit) {
        return zoneMap.entrySet()
            .stream()
            .sorted((Map.Entry.<ZonePoint, Long>comparingByValue().reversed())) // sort the ZonePoint map with value
            .limit(limit) // limit to the asked limit
            .map(Map.Entry::getKey)
            .collect(Collectors.toList());
    }

    /**
     * Convert a zone on a regrouped zone map with density value
     *
     * @param entities ZonePointEntity
     * @return Map<ZonePoint, Long>
     */
    private Map<ZonePoint, Long> buildZoneMap(List<ZonePointEntity> entities) {
        Map<ZonePoint, Long> zoneMap = new HashMap<>();
        for (ZonePointEntity entity : entities) {
            double minZoneLat = this.calculateZone(entity.getLat());
            double minZoneLon = this.calculateZone(entity.getLon());
            double maxZoneLat = minZoneLat + ZONE_SIZE;
            double maxZoneLon = minZoneLon + ZONE_SIZE;
            ZonePoint zonePoint = new ZonePoint(minZoneLat, minZoneLon, maxZoneLat, maxZoneLon);

            if (!zoneMap.containsKey(zonePoint)) {
                zoneMap.put(zonePoint, this.getDensity(minZoneLat, minZoneLon));
            }
        }

        return zoneMap;
    }

    /**
     * take a zone in parameter and return the zone start position.
     * example : for 6.9, will return 6.5, for -37.7 will return -38
     *
     * @param zone
     * @return zone start position
     */
    private double calculateZone(double zone) {
        double result = zone - zone % ZONE_SIZE;
        if (zone < 0) {
            // with the previous Euclidean division,
            // we have to remove one time a zone to get the good one for negative zone position.
            result -= ZONE_SIZE;
        }
        return result;
    }
}
