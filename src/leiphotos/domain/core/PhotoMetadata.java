package leiphotos.domain.core;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import leiphotos.utils.RegExpMatchable;
/**
 * Immutable record representing metadata of a photo, including GPS location, date taken, camera model, and manufacturer.
 * Implements RegExpMatchable to allow regex-based searching on camera, manufacturer, and GPS location.
 * 
 * @author Guilherme Santos fc63768 , Tomás Peres fc63721
 */
public record PhotoMetadata(Optional<GPSLocation> gpsLocation, LocalDateTime date, String camera, String manufacturer) implements RegExpMatchable {
    @Override
    public boolean matches(String regex) {
        return (camera != null && camera.matches(regex)) || (manufacturer != null && manufacturer.matches(regex)) || this.gpsLocation.map(loc -> loc.matches(regex)).orElse(false);
    }

    @Override
    public String toString() {
        String loc = gpsLocation.map(Object::toString).orElse("No Location");
        String cam = camera != null ? camera : "";
        String man = manufacturer != null ? manufacturer : "";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return "[" + loc + ", " + date.format(formatter) + ", " + cam + ", " + man + "]";
    }
}
