package leiphotos.core;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import leiphotos.domain.core.GPSLocation;
import leiphotos.domain.core.PhotoMetadata;
import leiphotos.domain.core.Photo;

import java.io.File;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Photo class.
 *
 * Note: Photo's constructor has package-private access, so these tests
 * must be in the same package (leiphotos.domain.core).
 */
class PhotoTest {

    // ------------------------------------------------------------------ fixtures

    private static final String TITLE = "Sunset";
    private static final LocalDateTime ADDED = LocalDateTime.of(2025, 1, 10, 12, 0);
    private static final LocalDateTime CAPTURED = LocalDateTime.of(2024, 6, 15, 18, 30);
    private static final File FILE = new File("photos/sunset.jpg");

    /** Metadata with GPS */
    private PhotoMetadata metaWithGps;
    /** Metadata without GPS */
    private PhotoMetadata metaNoGps;

    private Photo photoWithGps;
    private Photo photoNoGps;

    @BeforeEach
    void setUp() {
        GPSLocation gps = new GPSLocation(38.70, -9.19);
        metaWithGps = new PhotoMetadata(Optional.of(gps), CAPTURED, "iPhone SE", "Apple");
        metaNoGps   = new PhotoMetadata(Optional.empty(), CAPTURED, "Canon G5", "Canon");

        photoWithGps = new Photo(TITLE, ADDED, metaWithGps, FILE);
        photoNoGps   = new Photo(TITLE, ADDED, metaNoGps,   FILE);
    }

    // ------------------------------------------------------------------ title

    @Test
    void title_returnsCorrectTitle() {
        assertEquals(TITLE, photoWithGps.title());
    }

    // ------------------------------------------------------------------ dates

    @Test
    void capturedDate_returnsMetadataDate() {
        assertEquals(CAPTURED, photoWithGps.capturedDate());
    }

    @Test
    void addedDate_returnsDateAddedToLib() {
        assertEquals(ADDED, photoWithGps.addedDate());
    }

    // ------------------------------------------------------------------ favourite

    @Test
    void newPhoto_isNotFavourite() {
        assertFalse(photoWithGps.isFavourite());
    }

    @Test
    void toggleFavourite_onceMakesItFavourite() {
        photoWithGps.toggleFavourite();
        assertTrue(photoWithGps.isFavourite());
    }

    @Test
    void toggleFavourite_twiceRestoresOriginalState() {
        photoWithGps.toggleFavourite();
        photoWithGps.toggleFavourite();
        assertFalse(photoWithGps.isFavourite());
    }

    // ------------------------------------------------------------------ GPS / place

    @Test
    void getPlace_withGps_returnsNonEmptyOptional() {
        assertTrue(photoWithGps.getPlace().isPresent());
    }

    @Test
    void getPlace_withGps_returnsCorrectCoordinates() {
        var coords = photoWithGps.getPlace().get();
        assertEquals(38.70, coords.latitude(),  0.001);
        assertEquals(-9.19, coords.longitude(), 0.001);
    }

    @Test
    void getPlace_withoutGps_returnsEmptyOptional() {
        assertTrue(photoNoGps.getPlace().isEmpty());
    }

    // ------------------------------------------------------------------ file & size

    @Test
    void file_returnsCorrectFile() {
        assertEquals(FILE, photoWithGps.file());
    }

    @Test
    void size_matchesActualFileLength() {
        // FILE does not exist on disk, so length() == 0
        assertEquals(FILE.length(), photoWithGps.size());
    }

    // ------------------------------------------------------------------ matches

    @Test
    void matches_byTitle_returnsTrue() {
        assertTrue(photoWithGps.matches(".*Sunset.*"));
    }

    @Test
    void matches_byTitle_caseInsensitiveRegex_returnsTrue() {
        assertTrue(photoWithGps.matches("(?i).*sunset.*"));
    }

    @Test
    void matches_byFileName_returnsTrue() {
        // file name is "sunset.jpg"
        assertTrue(photoWithGps.matches(".*sunset.*"));
    }

    @Test
    void matches_byCamera_returnsTrue() {
        // metadata camera = "iPhone SE"
        assertTrue(photoWithGps.matches(".*iPhone.*"));
    }

    @Test
    void matches_byManufacturer_returnsTrue() {
        assertTrue(photoWithGps.matches(".*Apple.*"));
    }

    @Test
    void matches_noMatch_returnsFalse() {
        assertFalse(photoWithGps.matches(".*Vasconcelos.*"));
    }

    @Test
    void matches_emptyRegex_returnsTrue() {
        // ".*" matches everything; empty string also matches title ""
        assertTrue(photoWithGps.matches(".*"));
    }

    // ------------------------------------------------------------------ toString

    @Test
    void toString_containsTitle() {
        assertTrue(photoWithGps.toString().contains(TITLE));
    }

    @Test
    void toString_notFavourite_doesNotContainFAV() {
        assertFalse(photoWithGps.toString().contains("FAV"));
    }

    @Test
    void toString_favourite_containsFAV() {
        photoWithGps.toggleFavourite();
        assertTrue(photoWithGps.toString().contains("FAV"));
    }

    @Test
    void toString_containsFilePath() {
        assertTrue(photoWithGps.toString().contains("sunset.jpg"));
    }

    // ------------------------------------------------------------------ different titles/files

    @Test
    void twoPhotos_differentTitles_haveCorrectTitles() {
        Photo other = new Photo("Beach", ADDED, metaNoGps, new File("photos/beach.jpg"));
        assertNotEquals(photoWithGps.title(), other.title());
    }
}
