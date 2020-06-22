package com.nags.happntech.services;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import com.nags.happntech.models.ZonePoint;
import com.nags.happntech.repositories.ZonesRepository;
import com.nags.happntech.repositories.entities.ZonePointEntity;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class DensityServiceIT {

    @MockBean
    private ZonesRepository repo;

    private DensityService service;

    @BeforeEach
    void setup() {

        repo = Mockito.mock(ZonesRepository.class);
        this.service = new DensityService(repo);
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("getDensityTestCases")
    public void shouldReturnCorrectDensityInZone(DensityServiceTestCase testCase) {
        when(repo.findAll()).thenReturn(testCase.mockedFindAll);
        long result = service.getDensity(testCase.minLat, testCase.minLon);
        assertEquals(testCase.expectedResult, result);
    }

    @Test
    @DisplayName("Test top density zones normal case")
    public void shouldReturnCorrectTopDensityZones() {
        when(repo.findAll()).thenReturn(
            Arrays.asList(
                new ZonePointEntity("id1", -12.4, 3.1),
                new ZonePointEntity("id2", -12.2, 3.3),
                new ZonePointEntity("id3", 6.6, -6.9),
                new ZonePointEntity("id4", 6.8, -6.5),
                new ZonePointEntity("id5", 2.6, -5.9)
            )
        );

        List<ZonePoint> zones = service.getTopDensityZonePoints(2);
        assertEquals(zones.size(), 2);
        assertEquals(zones.get(0).toString(), "{min_lat : -12.5, max_lat : 3.0, min_lon : -12.0, max_lon : 3.5}");
        assertEquals(zones.get(1).toString(), "{min_lat : 6.5, max_lat : -7.0, min_lon : 7.0, max_lon : -6.5}");
    }

    @Test
    @DisplayName("Test top density zones without data")
    public void shouldReturnCorrectTopDensityZonesWithoutData() {
        when(repo.findAll()).thenReturn(
            Collections.emptyList()
        );

        List<ZonePoint> zones = service.getTopDensityZonePoints(10);
        assertEquals(zones.size(), 0);
    }

    @Test
    @DisplayName("Test top density zones with limit >  data length")
    public void shouldReturnCorrectTopDensityToMuchLimit() {
        when(repo.findAll()).thenReturn(
            Arrays.asList(
                new ZonePointEntity("id1", -12.4, 3.1),
                new ZonePointEntity("id2", -12.2, 3.3),
                new ZonePointEntity("id3", 6.6, -6.9),
                new ZonePointEntity("id4", 6.8, -6.5),
                new ZonePointEntity("id5", 2.6, -5.9)
            )
        );

        List<ZonePoint> zones = service.getTopDensityZonePoints(10);
        assertEquals(zones.size(), 3);
        assertEquals(zones.get(0).toString(), "{min_lat : -12.5, max_lat : 3.0, min_lon : -12.0, max_lon : 3.5}");
        assertEquals(zones.get(1).toString(), "{min_lat : 6.5, max_lat : -7.0, min_lon : 7.0, max_lon : -6.5}");
        assertEquals(zones.get(2).toString(), "{min_lat : 2.5, max_lat : -6.0, min_lon : 3.0, max_lon : -5.5}");
    }

    private static Stream<Arguments> getDensityTestCases() {
        return Stream.of(
            Arguments.of(
                // normal case
                new DensityServiceTestCase(
                    Arrays.asList(
                        new ZonePointEntity("id1", -12.4, 3.1),
                        new ZonePointEntity("id2", -12.2, 3.3),
                        new ZonePointEntity("id3", 6.6, -6.9),
                        new ZonePointEntity("id3", 2.6, -5.9)
                    ),
                    -12.5, 3d, 2,
                    "Should return correct number for expected position")
            ),
            Arguments.of(
                // with negative values only
                new DensityServiceTestCase(
                    Arrays.asList(
                        new ZonePointEntity("id1", -12.4, -3.1),
                        new ZonePointEntity("id2", -12.2, -3.3),
                        new ZonePointEntity("id3", -6.6, -6.9),
                        new ZonePointEntity("id3", -2.6, -5.9)
                    ),
                    -12.5, -3.5, 2,
                    "Should return correct number for negative values only")
            ),
            Arguments.of(
                // with positive value born
                new DensityServiceTestCase(
                    Arrays.asList(
                        new ZonePointEntity("id1", 12.1, 3.1),
                        new ZonePointEntity("id2", 12.2, 3.3),
                        new ZonePointEntity("id3", 12.6, 3.6), // should not be count
                        new ZonePointEntity("id3", 12.0, 3.0) // should not be count
                    ),
                    12.0d, 3d, 2,
                    "Should return correct number for test with positive born")
            ),
            Arguments.of(
                // with negative value born
                new DensityServiceTestCase(
                    Arrays.asList(
                        new ZonePointEntity("id1", -12.4, -3.1),
                        new ZonePointEntity("id2", -12.0, -3.0),
                        new ZonePointEntity("id3", -12.5, -3.5), // should not be count
                        new ZonePointEntity("id3", -11.9, -2.9) // should not be count
                    ),
                    -12.5, -3.5, 2,
                    "Should return correct number for test with negative born")
            ),
            Arguments.of(
                // without data
                new DensityServiceTestCase(
                    Collections.emptyList(),
                    -12.5, -3.5, 0,
                    "Should return correct number for test without")
            )
        );
    }

    @AllArgsConstructor
    private static class DensityServiceTestCase {
        private List<ZonePointEntity> mockedFindAll;
        private Double minLat;
        private Double minLon;
        private long expectedResult;
        private final String caseName;

        @Override
        public String toString() {
            return caseName;
        }
    }
}
