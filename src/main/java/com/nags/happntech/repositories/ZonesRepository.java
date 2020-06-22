package com.nags.happntech.repositories;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.nags.happntech.repositories.entities.ZonePointEntity;
import com.univocity.parsers.tsv.TsvParser;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

@Component
@AllArgsConstructor
@Slf4j
public class ZonesRepository {

    private static final int DATA_STARTING_LINE = 1;
    private final TsvParser parser;

    /*
     * maybe cached if there are performance needs
     */
    public List<ZonePointEntity> findAll() {
        try {
            List<String[]> rows = parser.parseAll(ResourceUtils.getFile("classpath:data/density-input.tsv"));
            List<ZonePointEntity> result = new ArrayList<>();
            for (int i = DATA_STARTING_LINE; i < rows.size(); i++) {
                result.add(new ZonePointEntity(rows.get(i)[0], Double.parseDouble(rows.get(i)[1]), Double.parseDouble(rows.get(i)[2])));
            }

            return result;
        } catch (FileNotFoundException e) {
            log.error("Could not extract data from data/density-input.tsv because file not found ", e);
            return Collections.emptyList();
        }
    }
}
