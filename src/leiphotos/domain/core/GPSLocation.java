package leiphotos.domain.core;

import leiphotos.domain.facade.GPSCoordinates;

class GPSLocation implements GPSCoordinates{
    private double latitude;
    private double longitude;

    public GPSLocation(double latitude, double longitude){
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    public double latitude() {
        return this.latitude;
    }
    @Override
    public double longitude() {
        return this.longitude;
    }

    
}
