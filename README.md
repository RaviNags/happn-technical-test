# Technical test for Happn

The goal of this project is to handle zone densities in the world.

### Technical Requirement

You need to get JDK 11 version

Libraries : 
- Spring boot
- Lombok
- Univocity-parsers : to parse TSV files data in Java objects

I used Spring boot because it seems to be the framework you use at Happn.

### Build and run

    ./mvnw spring-boot:run

### Rest calls

Get density by zone {minLat} and {minLon}

    GET /nbpoi?min_lat={minLat}&min_lon={minLon}
    
    Result :
    {
        "value" : 2    
    }

Get top density zones limited by the param {nbPoint}

    GET /densest/{nbPoint}
    
    Result :
    [
        {
            "min_lat" : -2.5,
            "max_lat" : 38.0,
            "min_lon" : -2.0,
            "max_lon" : 38.5
        },
        {
            "min_lat" : 6.5,
            "max_lat" : -7.0,
            "min_lon" : 7.0,
            "max_lon" : -6.5
        }
    ]
    

### Issue

I don't know how to call my service with spring boot in CLI without starting an application server.
