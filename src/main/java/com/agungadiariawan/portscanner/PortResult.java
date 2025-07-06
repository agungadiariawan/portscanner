package com.agungadiariawan.portscanner;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
public class PortResult {
    private final StringProperty ip;
    private final StringProperty port;
    private final StringProperty status;

    public PortResult(String ip, String port, String status) {
        this.ip = new SimpleStringProperty(ip);
        this.port = new SimpleStringProperty(port);
        this.status = new SimpleStringProperty(status);
    }

    public StringProperty ipProperty() { return ip; }
    public StringProperty portProperty() { return port; }
    public StringProperty statusProperty() { return status; }
}
