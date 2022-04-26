package org.carour.loggercore.util;

public record SqlConfiguration(String host, int port, String username, String password, String database) {
}
