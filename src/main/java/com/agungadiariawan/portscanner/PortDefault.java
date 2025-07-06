package com.agungadiariawan.portscanner;


import java.util.Arrays;
import java.util.List;

public class PortDefault {

    public static List<Integer> getDefaultPorts() {
        return Arrays.asList(
                // Networking & Remote Access
                20, 21, 22, 23, 25, 53, 67, 68, 69, 80,
                110, 111, 123, 135, 137, 138, 139, 143,
                161, 162, 179,

                // Microsoft
                389, 443, 445, 465, 514, 587, 636, 873,

                // Database
                1433, 1521, 1723, 1883, 2049, 2082, 2083,
                2483, 2484, 3306,3307,3308,3309,3310, 3389, 3690,

                // Web & Proxy
                4000, 4443, 4567, 5000, 5432, 5672,
                5800, 5900, 5984, 6000, 6379, 6667,
                7001, 7071, 7474, 8000, 8008, 8080,
                8081, 8443, 8888, 9000, 9200, 9300,
                10000, 11211, 27017
        );
    }
}
