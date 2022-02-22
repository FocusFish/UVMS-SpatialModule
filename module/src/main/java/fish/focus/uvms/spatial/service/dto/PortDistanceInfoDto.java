package fish.focus.uvms.spatial.service.dto;

import fish.focus.uvms.spatial.service.entity.PortEntity;

public class PortDistanceInfoDto {


    private PortEntity port;
    private double distance;

    public PortDistanceInfoDto(PortEntity port, double distance) {
        this.port = port;
        this.distance = distance;
    }

    public PortEntity getPort() {
        return port;
    }

    public void setPort(PortEntity port) {
        this.port = port;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }
}
