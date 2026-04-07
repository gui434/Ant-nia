package leiphotos.domain.core;

import leiphotos.domain.facade.GPSCoordinates;

class GPSLocation implements GPSCoordinates{
    double latitude;
    double longitude;

    public GPSLocation(double latitude, double longitude){
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    public double latitude() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'latitude'");
    }
    @Override
    public double longitude() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'longitude'");
    }

    
}
