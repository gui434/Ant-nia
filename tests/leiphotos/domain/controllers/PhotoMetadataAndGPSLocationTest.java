package leiphotos.domain.controllers;

import org.junit.jupiter.api.Test;

import leiphotos.domain.core.GPSLocation;
import leiphotos.domain.core.PhotoMetadata;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for PhotoMetadata and GPSLocation.
 */
class PhotoMetadataAndGPSLocationTest {

    // ==================================================================
    //  GPSLocation
    // ==================================================================

    @Test
    void gpsLocation_latitude_returnsCorrectValue() {
        GPSLocation gps = new GPSLocation(38.70, -9.19);
        assertEquals(38.70, gps.latitude(), 0.001);
    }

    @Test
    void gpsLocation_longitude_returnsCorrectValue() {
        GPSLocation gps = new GPSLocation(38.70, -9.19);
        assertEquals(-9.19, gps.longitude(), 0.001);
    }

    @Test
    void gpsLocation_matches_byLatitude_returnsTrue() {
        GPSLocation gps = new GPSLocation(38.70, -9.19);
        // Double.toString(38.7) == "38.7"
        assertTrue(gps.matches("38\\.7.*"));
    }

    @Test
    void gpsLocation_matches_byLongitude_returnsTrue() {
        GPSLocation gps = new GPSLocation(38.70, -9.19);
        assertTrue(gps.matches("-9\\.19.*"));
    }

    @Test
    void gpsLocation_matches_noMatch_returnsFalse() {
        GPSLocation gps = new GPSLocation(38.70, -9.19);
        assertFalse(gps.matches(".*Vasconcelos.*"));
    }

    @Test
    void gpsLocation_toString_containsLat() {
        GPSLocation gps = new GPSLocation(38.70, -9.19);
        assertTrue(gps.toString().contains("38.70"));
    }

    @Test
    void gpsLocation_toString_containsLong() {
        GPSLocation gps = new GPSLocation(38.70, -9.19);
        assertTrue(gps.toString().contains("-9.19"));
    }

    @Test
    void gpsLocation_recordEquality_sameValues() {
        GPSLocation a = new GPSLocation(38.70, -9.19);
        GPSLocation b = new GPSLocation(38.70, -9.19);
        assertEquals(a, b);
    }

    @Test
    void gpsLocation_recordEquality_differentValues() {
        GPSLocation a = new GPSLocation(38.70, -9.19);
        GPSLocation b = new GPSLocation(41.88, -87.62);
        assertNotEquals(a, b);
    }

    // ==================================================================
    //  PhotoMetadata – with GPS
    // ==================================================================

    private static final LocalDateTime DATE = LocalDateTime.of(2024, 6, 15, 18, 30);

    private PhotoMetadata metaWithGps() {
        GPSLocation gps = new GPSLocation(38.70, -9.19);
        return new PhotoMetadata(Optional.of(gps), DATE, "iPhone SE", "Apple");
    }

    private PhotoMetadata metaNoGps() {
        return new PhotoMetadata(Optional.empty(), DATE, "Canon G5", "Canon");
    }

    private PhotoMetadata metaNullCameraManufacturer() {
        return new PhotoMetadata(Optional.empty(), DATE, null, null);
    }

    @Test
    void photoMetadata_gpsLocation_presentWhenProvided() {
        assertTrue(metaWithGps().gpsLocation().isPresent());
    }

    @Test
    void photoMetadata_gpsLocation_emptyWhenNotProvided() {
        assertTrue(metaNoGps().gpsLocation().isEmpty());
    }

    @Test
    void photoMetadata_date_returnsCorrectDate() {
        assertEquals(DATE, metaWithGps().date());
    }

    @Test
    void photoMetadata_camera_returnsCorrectCamera() {
        assertEquals("iPhone SE", metaWithGps().camera());
    }

    @Test
    void photoMetadata_manufacturer_returnsCorrectManufacturer() {
        assertEquals("Apple", metaWithGps().manufacturer());
    }

    // ------------------------------------------------------------------
    //  matches
    // ------------------------------------------------------------------

    @Test
    void photoMetadata_matches_byCamera_returnsTrue() {
        assertTrue(metaWithGps().matches(".*iPhone.*"));
    }

    @Test
    void photoMetadata_matches_byManufacturer_returnsTrue() {
        assertTrue(metaWithGps().matches(".*Apple.*"));
    }

    @Test
    void photoMetadata_matches_byGpsCoordinate_returnsTrue() {
        // latitude 38.70 → Double.toString → "38.7"
        assertTrue(metaWithGps().matches("38\\.7.*"));
    }

    @Test
    void photoMetadata_matches_noMatch_returnsFalse() {
        assertFalse(metaWithGps().matches(".*Vasconcelos.*"));
    }

    @Test
    void photoMetadata_matches_nullCamera_doesNotThrow() {
        assertDoesNotThrow(() -> metaNullCameraManufacturer().matches(".*anything.*"));
    }

    @Test
    void photoMetadata_matches_nullCamera_returnsFalse() {
        assertFalse(metaNullCameraManufacturer().matches(".*anything.*"));
    }

    @Test
    void photoMetadata_matches_noGps_doesNotMatchGpsPattern() {
        // no GPS in metaNoGps
        assertFalse(metaNoGps().matches("38\\.7.*"));
    }

    // ------------------------------------------------------------------
    //  toString
    // ------------------------------------------------------------------

    @Test
    void photoMetadata_toString_withGps_containsCoordinates() {
        String s = metaWithGps().toString();
        assertTrue(s.contains("38.70") || s.contains("38,70")); // locale-safe check
    }

    @Test
    void photoMetadata_toString_noGps_containsNoLocation() {
        assertTrue(metaNoGps().toString().contains("No Location"));
    }

    @Test
    void photoMetadata_toString_containsCamera() {
        assertTrue(metaWithGps().toString().contains("iPhone SE"));
    }

    @Test
    void photoMetadata_toString_containsManufacturer() {
        assertTrue(metaWithGps().toString().contains("Apple"));
    }

    @Test
    void photoMetadata_toString_nullCamera_doesNotThrow() {
        assertDoesNotThrow(() -> metaNullCameraManufacturer().toString());
    }

    // ------------------------------------------------------------------
    //  record equality
    // ------------------------------------------------------------------

    @Test
    void photoMetadata_recordEquality_sameValues() {
        GPSLocation gps = new GPSLocation(38.70, -9.19);
        PhotoMetadata a = new PhotoMetadata(Optional.of(gps), DATE, "iPhone SE", "Apple");
        PhotoMetadata b = new PhotoMetadata(Optional.of(gps), DATE, "iPhone SE", "Apple");
        assertEquals(a, b);
    }

    @Test
    void photoMetadata_recordEquality_differentCamera() {
        GPSLocation gps = new GPSLocation(38.70, -9.19);
        PhotoMetadata a = new PhotoMetadata(Optional.of(gps), DATE, "iPhone SE", "Apple");
        PhotoMetadata b = new PhotoMetadata(Optional.of(gps), DATE, "Canon G5",  "Canon");
        assertNotEquals(a, b);
    }
}
