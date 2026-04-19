package leiphotos.albums;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import leiphotos.domain.albums.AlbumsCatalog;
import leiphotos.domain.core.MainLibrary;
import leiphotos.domain.core.MockPhoto;
import leiphotos.domain.facade.IPhoto;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for AlbumsCatalog and Album.
 *
 * MockPhoto lives in leiphotos.domain.core.
 */
class AlbumsCatalogTest {

    // ------------------------------------------------------------------ fixtures

    private MainLibrary lib;
    private AlbumsCatalog catalog;
    private MockPhoto p1, p2, p3;

    private static final String ALBUM_A = "Oppenheimer";
    private static final String ALBUM_B = "Monuments";
    private static final String ALBUM_C = "JoanaVasconcelos";

    @BeforeEach
    void setUp() {
        lib     = new MainLibrary();
        catalog = new AlbumsCatalog(lib);

        p1 = new MockPhoto("Photo1", null, null, false, null, 0, false);
        p2 = new MockPhoto("Photo2", null, null, false, null, 0, false);
        p3 = new MockPhoto("Photo3", null, null, false, null, 0, false);

        lib.addPhoto(p1);
        lib.addPhoto(p2);
        lib.addPhoto(p3);
    }

    // ==================================================================
    //  createAlbum
    // ==================================================================

    @Test
    void createAlbum_newName_returnsTrue() {
        assertTrue(catalog.createAlbum(ALBUM_A));
    }

    @Test
    void createAlbum_newName_albumExists() {
        catalog.createAlbum(ALBUM_A);
        assertTrue(catalog.containsAlbum(ALBUM_A));
    }

    @Test
    void createAlbum_duplicateName_returnsFalse() {
        catalog.createAlbum(ALBUM_A);
        assertFalse(catalog.createAlbum(ALBUM_A));
    }

    @Test
    void createAlbum_duplicateName_doesNotCreateSecondAlbum() {
        catalog.createAlbum(ALBUM_A);
        catalog.createAlbum(ALBUM_A);
        // still only one album
        assertEquals(1, catalog.getAlbumsNames().size());
    }

    @Test
    void createAlbum_multipleAlbums_allExist() {
        catalog.createAlbum(ALBUM_A);
        catalog.createAlbum(ALBUM_B);
        catalog.createAlbum(ALBUM_C);
        assertTrue(catalog.containsAlbum(ALBUM_A));
        assertTrue(catalog.containsAlbum(ALBUM_B));
        assertTrue(catalog.containsAlbum(ALBUM_C));
    }

    // ==================================================================
    //  deleteAlbum
    // ==================================================================

    @Test
    void deleteAlbum_existingAlbum_returnsTrue() {
        catalog.createAlbum(ALBUM_A);
        assertTrue(catalog.deleteAlbum(ALBUM_A));
    }

    @Test
    void deleteAlbum_existingAlbum_albumNoLongerExists() {
        catalog.createAlbum(ALBUM_A);
        catalog.deleteAlbum(ALBUM_A);
        assertFalse(catalog.containsAlbum(ALBUM_A));
    }

    @Test
    void deleteAlbum_nonExistingAlbum_returnsFalse() {
        assertFalse(catalog.deleteAlbum(ALBUM_A));
    }

    @Test
    void deleteAlbum_onlyDeletesTargetAlbum() {
        catalog.createAlbum(ALBUM_A);
        catalog.createAlbum(ALBUM_B);
        catalog.deleteAlbum(ALBUM_A);
        assertFalse(catalog.containsAlbum(ALBUM_A));
        assertTrue(catalog.containsAlbum(ALBUM_B));
    }

    // ==================================================================
    //  containsAlbum
    // ==================================================================

    @Test
    void containsAlbum_beforeCreation_returnsFalse() {
        assertFalse(catalog.containsAlbum(ALBUM_A));
    }

    @Test
    void containsAlbum_afterCreation_returnsTrue() {
        catalog.createAlbum(ALBUM_A);
        assertTrue(catalog.containsAlbum(ALBUM_A));
    }

    @Test
    void containsAlbum_afterDeletion_returnsFalse() {
        catalog.createAlbum(ALBUM_A);
        catalog.deleteAlbum(ALBUM_A);
        assertFalse(catalog.containsAlbum(ALBUM_A));
    }

    // ==================================================================
    //  addPhotos
    // ==================================================================

    @Test
    void addPhotos_existingAlbum_returnsTrue() {
        catalog.createAlbum(ALBUM_A);
        assertTrue(catalog.addPhotos(ALBUM_A, Set.of(p1, p2)));
    }

    @Test
    void addPhotos_existingAlbum_photosAreInAlbum() {
        catalog.createAlbum(ALBUM_A);
        catalog.addPhotos(ALBUM_A, Set.of(p1, p2));
        List<IPhoto> photos = catalog.getPhotos(ALBUM_A);
        assertTrue(photos.contains(p1));
        assertTrue(photos.contains(p2));
    }

    @Test
    void addPhotos_nonExistingAlbum_returnsFalse() {
        assertFalse(catalog.addPhotos(ALBUM_A, Set.of(p1)));
    }

    @Test
    void addPhotos_emptySet_doesNotChangeAlbum() {
        catalog.createAlbum(ALBUM_A);
        catalog.addPhotos(ALBUM_A, Set.of());
        assertTrue(catalog.getPhotos(ALBUM_A).isEmpty());
    }

    @Test
    void addPhotos_toTwoAlbums_photosIndependent() {
        catalog.createAlbum(ALBUM_A);
        catalog.createAlbum(ALBUM_B);
        catalog.addPhotos(ALBUM_A, Set.of(p1));
        catalog.addPhotos(ALBUM_B, Set.of(p2));

        assertTrue(catalog.getPhotos(ALBUM_A).contains(p1));
        assertFalse(catalog.getPhotos(ALBUM_A).contains(p2));
        assertTrue(catalog.getPhotos(ALBUM_B).contains(p2));
        assertFalse(catalog.getPhotos(ALBUM_B).contains(p1));
    }

    // ==================================================================
    //  removePhotos
    // ==================================================================

    @Test
    void removePhotos_existingPhoto_returnsTrue() {
        catalog.createAlbum(ALBUM_A);
        catalog.addPhotos(ALBUM_A, Set.of(p1, p2));
        assertTrue(catalog.removePhotos(ALBUM_A, Set.of(p1)));
    }

    @Test
    void removePhotos_existingPhoto_photoNoLongerInAlbum() {
        catalog.createAlbum(ALBUM_A);
        catalog.addPhotos(ALBUM_A, Set.of(p1, p2));
        catalog.removePhotos(ALBUM_A, Set.of(p1));
        assertFalse(catalog.getPhotos(ALBUM_A).contains(p1));
        assertTrue(catalog.getPhotos(ALBUM_A).contains(p2));
    }

    @Test
    void removePhotos_nonExistingAlbum_returnsFalse() {
        assertFalse(catalog.removePhotos(ALBUM_A, Set.of(p1)));
    }

    @Test
    void removePhotos_photoNotInAlbum_returnsFalse() {
        catalog.createAlbum(ALBUM_A);
        catalog.addPhotos(ALBUM_A, Set.of(p1));
        assertFalse(catalog.removePhotos(ALBUM_A, Set.of(p3)));
    }

    // ==================================================================
    //  getPhotos
    // ==================================================================

    @Test
    void getPhotos_emptyAlbum_returnsEmptyList() {
        catalog.createAlbum(ALBUM_A);
        assertTrue(catalog.getPhotos(ALBUM_A).isEmpty());
    }

    @Test
    void getPhotos_nonExistingAlbum_returnsNull() {
        // per current implementation: returns null when album does not exist
        assertNull(catalog.getPhotos(ALBUM_A));
    }

    @Test
    void getPhotos_afterAdding_returnsAllAdded() {
        catalog.createAlbum(ALBUM_A);
        catalog.addPhotos(ALBUM_A, Set.of(p1, p2, p3));
        List<IPhoto> photos = catalog.getPhotos(ALBUM_A);
        assertEquals(3, photos.size());
        assertTrue(photos.containsAll(List.of(p1, p2, p3)));
    }

    // ==================================================================
    //  getAlbumsNames
    // ==================================================================

    @Test
    void getAlbumsNames_noAlbums_returnsEmptySet() {
        assertTrue(catalog.getAlbumsNames().isEmpty());
    }

    @Test
    void getAlbumsNames_afterCreation_containsAlbumName() {
        catalog.createAlbum(ALBUM_A);
        assertTrue(catalog.getAlbumsNames().contains(ALBUM_A));
    }

    @Test
    void getAlbumsNames_afterDeletion_doesNotContainDeletedAlbum() {
        catalog.createAlbum(ALBUM_A);
        catalog.createAlbum(ALBUM_B);
        catalog.deleteAlbum(ALBUM_A);
        assertFalse(catalog.getAlbumsNames().contains(ALBUM_A));
        assertTrue(catalog.getAlbumsNames().contains(ALBUM_B));
    }

    @Test
    void getAlbumsNames_multipleAlbums_returnsCorrectCount() {
        catalog.createAlbum(ALBUM_A);
        catalog.createAlbum(ALBUM_B);
        catalog.createAlbum(ALBUM_C);
        assertEquals(3, catalog.getAlbumsNames().size());
    }

    // ==================================================================
    //  Observer integration: photo removed from library → removed from album
    // ==================================================================

    @Test
    void photoRemovedFromLibrary_alsoRemovedFromAlbum() {
        catalog.createAlbum(ALBUM_A);
        catalog.addPhotos(ALBUM_A, Set.of(p1, p2));

        lib.deletePhoto(p1); // triggers PhotoRemovedLibraryEvent → album listener

        assertFalse(catalog.getPhotos(ALBUM_A).contains(p1),
                "Photo removed from library should be automatically removed from album");
        assertTrue(catalog.getPhotos(ALBUM_A).contains(p2));
    }

    @Test
    void photoRemovedFromLibrary_removedFromAllAlbums() {
        catalog.createAlbum(ALBUM_A);
        catalog.createAlbum(ALBUM_B);
        catalog.addPhotos(ALBUM_A, Set.of(p1));
        catalog.addPhotos(ALBUM_B, Set.of(p1));

        lib.deletePhoto(p1);

        assertFalse(catalog.getPhotos(ALBUM_A).contains(p1));
        assertFalse(catalog.getPhotos(ALBUM_B).contains(p1));
    }

    @Test
    void photoRemovedFromLibrary_otherPhotosUnaffected() {
        catalog.createAlbum(ALBUM_A);
        catalog.addPhotos(ALBUM_A, Set.of(p1, p2));

        lib.deletePhoto(p1);

        assertTrue(catalog.getPhotos(ALBUM_A).contains(p2));
    }
}
