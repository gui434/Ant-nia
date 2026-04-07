package leiphotos.domain.core;

import java.time.LocalDateTime;

import leiphotos.utils.RegExpMatchable;

public class PhotoMetadata implements RegExpMatchable{
    // Associação
    private GPSLocation location;

    private String camera;
    private String manufacturer;
    private String aperture;
    private LocalDateTime date;


    PhotoMetadata(String camera, String manufacturer, LocalDateTime date, String aperture,
            double[] gpsLocation) {
                this.camera = camera;
                this.manufacturer = manufacturer;
                this.aperture = aperture;
                this.date = date;

                if(gpsLocation != null && gpsLocation.length <= 2){
                    this.location = new GPSLocation(gpsLocation[0],gpsLocation[1]);
                }
    }

    @Override
    public boolean matches(String regexp) {
        return this.manufacturer.matches(regexp);
    }

}
