package leiphotos.core;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.util.Collection;

import leiphotos.domain.core.RecentlyDeletedLibrary;
import leiphotos.domain.facade.IPhoto;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for RecentlyDeletedLibrary.
 *
 * The library keeps photos for MAX_SECONDS_IN_TRASH (15 s) and only
 * cleans when MIN_SECONDS_BETWEEN_CLEANING (5 s) have also elapsed since
 * the last check.  Tests that require waiting use @Timeout to avoid
 * hanging the suite indefinitely.
 */
class RecentlyDeletedLibraryTest {

    private RecentlyDeletedLibrary trash;
    private MockPhoto p1;
    private MockPhoto p2;
    private MockPhoto p3;

    @BeforeEach
    void setUp() {
        trash = new RecentlyDeletedLibrary();
        p1 = new MockPhoto("Photo1");
        p2 = new MockPhoto("Photo2");
        p3 = new MockPhoto("Photo3");
    }

    // ------------------------------------------------------------------ addPhoto

    @Test
    void addPhoto_newPhoto_returnsTrue() {
        assertTrue(trash.addPhoto(p1));
    }

    @Test
    void addPhoto_newPhoto_increasesCount() {
        trash.addPhoto(p1);
        assertEquals(1, trash.getNumberOfPhotos());
    }

    @Test
    void addPhoto_duplicate_returnsFalse() {
        trash.addPhoto(p1);
        assertFalse(trash.addPhoto(p1));
    }

    @Test
    void addPhoto_duplicate_doesNotIncreaseCount() {
        trash.addPhoto(p1);
        trash.addPhoto(p1);
        assertEquals(1, trash.getNumberOfPhotos());
    }

    @Test
    void addPhoto_null_returnsFalse() {
        assertFalse(trash.addPhoto(null));
    }

    @Test
    void addPhoto_multiplePhotos_allPresent() {
        trash.addPhoto(p1);
        trash.addPhoto(p2);
        trash.addPhoto(p3);
        assertEquals(3, trash.getNumberOfPhotos());
    }

    // ------------------------------------------------------------------ deletePhoto

    @Test
    void deletePhoto_existingPhoto_returnsTrue() {
        trash.addPhoto(p1);
        assertTrue(trash.deletePhoto(p1));
    }

    @Test
    void deletePhoto_existingPhoto_decreasesCount() {
        trash.addPhoto(p1);
        trash.addPhoto(p2);
        trash.deletePhoto(p1);
        assertEquals(1, trash.getNumberOfPhotos());
    }

    @Test
    void deletePhoto_nonExistingPhoto_returnsFalse() {
        assertFalse(trash.deletePhoto(p1));
    }

    @Test
    void deletePhoto_null_returnsFalse() {
        assertFalse(trash.deletePhoto(null));
    }

    @Test
    void deletePhoto_removedPhotoNotInGetPhotos() {
        trash.addPhoto(p1);
        trash.addPhoto(p2);
        trash.deletePhoto(p1);
        assertFalse(trash.getPhotos().contains(p1));
    }

    // ------------------------------------------------------------------ getNumberOfPhotos

    @Test
    void getNumberOfPhotos_emptyLibrary_returnsZero() {
        assertEquals(0, trash.getNumberOfPhotos());
    }

    @Test
    void getNumberOfPhotos_afterAddingPhotos_returnsCorrectCount() {
        trash.addPhoto(p1);
        trash.addPhoto(p2);
        assertEquals(2, trash.getNumberOfPhotos());
    }

    // ------------------------------------------------------------------ getPhotos

    @Test
    void getPhotos_emptyLibrary_returnsEmptyCollection() {
        assertTrue(trash.getPhotos().isEmpty());
    }

    @Test
    void getPhotos_withPhotos_containsAddedPhotos() {
        trash.addPhoto(p1);
        trash.addPhoto(p2);
        Collection<IPhoto> photos = trash.getPhotos();
        assertTrue(photos.contains(p1));
        assertTrue(photos.contains(p2));
    }

    @Test
    void getPhotos_recentPhotos_notCleanedBeforeTimeout() {
        trash.addPhoto(p1);
        // No sleep — photo was just added, should still be present
        assertTrue(trash.getPhotos().contains(p1));
    }

    // ------------------------------------------------------------------ deleteAll

    @Test
    void deleteAll_withPhotos_returnsTrue() {
        trash.addPhoto(p1);
        assertTrue(trash.deleteAll());
    }

    @Test
    void deleteAll_withPhotos_removesAll() {
        trash.addPhoto(p1);
        trash.addPhoto(p2);
        trash.deleteAll();
        assertEquals(0, trash.getNumberOfPhotos());
    }

    @Test
    void deleteAll_emptyLibrary_returnsFalse() {
        assertFalse(trash.deleteAll());
    }

    @Test
    void deleteAll_photosNotReturnedAfterwards() {
        trash.addPhoto(p1);
        trash.deleteAll();
        assertTrue(trash.getPhotos().isEmpty());
    }

    // ------------------------------------------------------------------ getMatches

    @Test
    void getMatches_matchingTitle_returnsCorrectPhoto() {
        trash.addPhoto(p1);
        trash.addPhoto(p2);
        Collection<IPhoto> result = trash.getMatches(".*Photo1.*");
        assertTrue(result.contains(p1));
        assertFalse(result.contains(p2));
    }

    @Test
    void getMatches_noMatch_returnsEmptyCollection() {
        trash.addPhoto(p1);
        Collection<IPhoto> result = trash.getMatches(".*Vasconcelos.*");
        assertTrue(result.isEmpty());
    }

    @Test
    void getMatches_allMatch_returnsAll() {
        trash.addPhoto(p1);
        trash.addPhoto(p2);
        Collection<IPhoto> result = trash.getMatches(".*Photo.*");
        assertEquals(2, result.size());
    }

    // ------------------------------------------------------------------ auto-cleanup (slow tests)

    /**
     * Waits long enough for the photo to be past its 15-second TTL AND for
     * the 5-second minimum between checks to also have elapsed.
     * Total wait: 20 s (same as SimpleClient uses).
     */
    @Test
    @Timeout(25) // fail if test takes longer than 25 s
    void getPhotos_afterTTLExpired_photoIsRemoved() throws InterruptedException {
        trash.addPhoto(p1);
        Thread.sleep(20_000); // 20 s > MAX_SECONDS_IN_TRASH (15) + MIN_SECONDS_BETWEEN_CLEANING (5)
        assertTrue(trash.getPhotos().isEmpty(),
                "Photo should have been automatically removed after TTL");
    }

    @Test
    @Timeout(25)
    void getPhotos_afterTTLExpired_countDropsToZero() throws InterruptedException {
        trash.addPhoto(p1);
        trash.addPhoto(p2);
        Thread.sleep(20_000);
        trash.getPhotos(); // trigger cleaning
        assertEquals(0, trash.getNumberOfPhotos());
    }

    @Test
    @Timeout(10)
    void getPhotos_beforeTTLExpired_photoStillPresent() throws InterruptedException {
        trash.addPhoto(p1);
        Thread.sleep(3_000); // 3 s — well within the 15 s TTL
        assertTrue(trash.getPhotos().contains(p1),
                "Photo should still be present before TTL");
    }
}
