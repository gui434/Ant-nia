package leiphotos.domain.core;

import java.time.LocalDateTime;
import java.util.Optional;

import leiphotos.utils.RegExpMatchable;

public record PhotoMetadata(Optional<GPSLocation> gpsLocation, LocalDateTime date, String camera, String manufacturer) implements RegExpMatchable {
    @Override
    public boolean matches(String regex) {
        return this.camera.matches(regex) || this.manufacturer.matches(regex) || this.gpsLocation.map(loc -> loc.matches(regex)).orElse(false);
    }
}
