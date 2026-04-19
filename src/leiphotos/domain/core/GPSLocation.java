package leiphotos.domain.core;

import java.util.Locale;

import leiphotos.domain.facade.GPSCoordinates;
import leiphotos.utils.RegExpMatchable;

/**
 * A record representing a GPS location with latitude and longitude.
 * Implements GPSCoordinates and RegExpMatchable interfaces.
 */
public record GPSLocation(double latitude, double longitude) implements GPSCoordinates, RegExpMatchable {
    @Override
    public boolean matches(String regex) {
        return Double.toString(this.latitude).matches(regex) || Double.toString(this.longitude).matches(regex);
    } 

    @Override
    public String toString() {
        return "{Lat:" + String.format(Locale.ENGLISH, "%.2f", this.latitude) + 
               " Long:" + String.format(Locale.ENGLISH, "%.2f", this.longitude) + " DESC:}";
    }

}
