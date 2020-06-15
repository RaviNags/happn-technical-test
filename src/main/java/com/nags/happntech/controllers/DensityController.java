package com.nags.happntech.controllers;

import java.util.List;
import javax.validation.constraints.NotEmpty;

import com.nags.happntech.models.NbPoint;
import com.nags.happntech.models.ZonePoint;
import com.nags.happntech.services.DensityService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class DensityController {

    private final DensityService densityService;

    @GetMapping(path = "/nbpoi")
    public NbPoint getNbPoints(
        @NotEmpty @RequestParam("min_lat") Double minLat,
        @NotEmpty @RequestParam("min_lon") Double minLon
    ) {
        return new NbPoint(this.densityService.getDensity(minLat, minLon));
    }

    @GetMapping(path = "/densest/{nbPoint}")
    public List<ZonePoint> getDensest(
        @NotEmpty @PathVariable(value = "nbPoint") Integer nbPoint
    ) {
        return this.densityService.getTopDensityZonePoints(nbPoint);
    }


}
