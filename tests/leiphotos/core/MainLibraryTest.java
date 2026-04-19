package leiphotos.core;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import leiphotos.domain.core.MainLibrary;
import leiphotos.domain.core.events.PhotoAddedLibraryEvent;
import leiphotos.domain.core.events.PhotoChangedLibraryEvent;
import leiphotos.domain.core.events.PhotoLibraryEvent;
import leiphotos.domain.core.events.PhotoRemovedLibraryEvent;
import leiphotos.domain.facade.IPhoto;
import leiphotos.utils.Listener;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for MainLibrary.
 */
class MainLibraryTest {

    // ------------------------------------------------------------------ test listener

    /** Captures all events emitted by a MainLibrary. */
    static class EventCapture implements Listener<PhotoLibraryEvent> {
        final List<PhotoLibraryEvent> events = new ArrayList<>();

        @Override
        public void processEvent(PhotoLibraryEvent e) {
            events.add(e);
        }

        boolean hasEventOfType(Class<? extends PhotoLibraryEvent> type) {
            return events.stream().anyMatch(type::isInstance);
        }

        PhotoLibraryEvent last() {
            return events.get(events.size() - 1);
        }
    }

    // ------------------------------------------------------------------ fixtures

    private MainLibrary lib;
    private MockPhoto p1, p2, p3;
    private EventCapture capture;

    @BeforeEach
    void setUp() {
        lib = new MainLibrary();
        p1 = new MockPhoto("Alpha");
        p2 = new MockPhoto("Beta");
        p3 = new MockPhoto("Gamma");

        capture = new EventCapture();
        lib.registerListener(capture);
    }

    // ------------------------------------------------------------------ initial state

    @Test
    void newLibrary_isEmpty() {
        assertEquals(0, lib.getNumberOfPhotos());
    }

    @Test
    void newLibrary_getPhotos_returnsEmpty() {
        assertTrue(lib.getPhotos().isEmpty());
    }

    // ------------------------------------------------------------------ addPhoto

    @Test
    void addPhoto_newPhoto_returnsTrue() {
        assertTrue(lib.addPhoto(p1));
    }

    @Test
    void addPhoto_newPhoto_increasesCount() {
        lib.addPhoto(p1);
        assertEquals(1, lib.getNumberOfPhotos());
    }

    @Test
    void addPhoto_duplicate_returnsFalse() {
        lib.addPhoto(p1);
        assertFalse(lib.addPhoto(p1));
    }

    @Test
    void addPhoto_duplicate_doesNotIncreaseCount() {
        lib.addPhoto(p1);
        lib.addPhoto(p1);
        assertEquals(1, lib.getNumberOfPhotos());
    }

    @Test
    void addPhoto_null_returnsFalse() {
        assertFalse(lib.addPhoto(null));
    }

    @Test
    void addPhoto_multiplePhotos_allPresent() {
        lib.addPhoto(p1);
        lib.addPhoto(p2);
        lib.addPhoto(p3);
        assertEquals(3, lib.getNumberOfPhotos());
        Collection<IPhoto> photos = lib.getPhotos();
        assertTrue(photos.contains(p1));
        assertTrue(photos.contains(p2));
        assertTrue(photos.contains(p3));
    }

    // ------------------------------------------------------------------ deletePhoto

    @Test
    void deletePhoto_existingPhoto_returnsTrue() {
        lib.addPhoto(p1);
        assertTrue(lib.deletePhoto(p1));
    }

    @Test
    void deletePhoto_existingPhoto_decreasesCount() {
        lib.addPhoto(p1);
        lib.addPhoto(p2);
        lib.deletePhoto(p1);
        assertEquals(1, lib.getNumberOfPhotos());
    }

    @Test
    void deletePhoto_nonExistingPhoto_returnsFalse() {
        assertFalse(lib.deletePhoto(p1));
    }

    @Test
    void deletePhoto_null_returnsFalse() {
        assertFalse(lib.deletePhoto(null));
    }

    @Test
    void deletePhoto_removedPhoto_notInGetPhotos() {
        lib.addPhoto(p1);
        lib.addPhoto(p2);
        lib.deletePhoto(p1);
        assertFalse(lib.getPhotos().contains(p1));
        assertTrue(lib.getPhotos().contains(p2));
    }

    // ------------------------------------------------------------------ getMatches

    @Test
    void getMatches_matchingTitle_returnsCorrectPhoto() {
        lib.addPhoto(p1); // "Alpha"
        lib.addPhoto(p2); // "Beta"
        Collection<IPhoto> result = lib.getMatches(".*Alpha.*");
        assertTrue(result.contains(p1));
        assertFalse(result.contains(p2));
    }

    @Test
    void getMatches_noMatch_returnsEmptyCollection() {
        lib.addPhoto(p1);
        assertTrue(lib.getMatches(".*Vasconcelos.*").isEmpty());
    }

    @Test
    void getMatches_allMatch_returnsAll() {
        lib.addPhoto(p1); // "Alpha"
        lib.addPhoto(p2); // "Beta"
        Collection<IPhoto> result = lib.getMatches(".*");
        assertEquals(2, result.size());
    }

    @Test
    void getMatches_emptyLibrary_returnsEmpty() {
        assertTrue(lib.getMatches(".*").isEmpty());
    }

    // ------------------------------------------------------------------ observer: PhotoAddedLibraryEvent

    @Test
    void addPhoto_emitsPhotoAddedEvent() {
        lib.addPhoto(p1);
        assertTrue(capture.hasEventOfType(PhotoAddedLibraryEvent.class));
    }

    @Test
    void addPhoto_event_referencesCorrectPhoto() {
        lib.addPhoto(p1);
        assertEquals(p1, capture.last().getPhoto());
    }

    @Test
    void addPhoto_event_referencesCorrectLibrary() {
        lib.addPhoto(p1);
        assertEquals(lib, capture.last().getLibrary());
    }

    @Test
    void addPhoto_duplicate_doesNotEmitEvent() {
        lib.addPhoto(p1);
        int countBefore = capture.events.size();
        lib.addPhoto(p1);
        assertEquals(countBefore, capture.events.size());
    }

    // ------------------------------------------------------------------ observer: PhotoRemovedLibraryEvent

    @Test
    void deletePhoto_emitsPhotoRemovedEvent() {
        lib.addPhoto(p1);
        capture.events.clear();
        lib.deletePhoto(p1);
        assertTrue(capture.hasEventOfType(PhotoRemovedLibraryEvent.class));
    }

    @Test
    void deletePhoto_event_referencesCorrectPhoto() {
        lib.addPhoto(p1);
        capture.events.clear();
        lib.deletePhoto(p1);
        assertEquals(p1, capture.last().getPhoto());
    }

    @Test
    void deletePhoto_nonExisting_doesNotEmitEvent() {
        int countBefore = capture.events.size();
        lib.deletePhoto(p1);
        assertEquals(countBefore, capture.events.size());
    }

    // ------------------------------------------------------------------ observer: PhotoChangedLibraryEvent

    @Test
    void photoChanged_existingPhoto_emitsPhotoChangedEvent() {
        lib.addPhoto(p1);
        capture.events.clear();
        lib.photoChanged(p1);
        assertTrue(capture.hasEventOfType(PhotoChangedLibraryEvent.class));
    }

    @Test
    void photoChanged_nonExistingPhoto_doesNotEmitEvent() {
        int countBefore = capture.events.size();
        lib.photoChanged(p1); // p1 not in lib
        assertEquals(countBefore, capture.events.size());
    }

    // ------------------------------------------------------------------ toString

    @Test
    void toString_containsPhotoCount() {
        lib.addPhoto(p1);
        lib.addPhoto(p2);
        assertTrue(lib.toString().contains("2"));
    }

    @Test
    void toString_containsMAIN() {
        assertTrue(lib.toString().contains("MAIN"));
    }
}
