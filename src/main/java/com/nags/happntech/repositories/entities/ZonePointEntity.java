package com.nags.happntech.repositories.entities;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ZonePointEntity {

    private String id;

    private Double lat;

    private Double lon;

}
