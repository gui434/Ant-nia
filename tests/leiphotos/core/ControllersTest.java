package leiphotos.core;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import leiphotos.domain.albums.AlbumsCatalog;
import leiphotos.domain.controllers.AlbumsController;
import leiphotos.domain.controllers.LibrariesController;
import leiphotos.domain.controllers.ViewsController;
import leiphotos.domain.core.MainLibrary;
import leiphotos.domain.core.RecentlyDeletedLibrary;
import leiphotos.domain.core.TrashLibrary;
import leiphotos.domain.facade.IPhoto;
import leiphotos.domain.facade.ViewsType;
import leiphotos.domain.views.ViewsCatalog;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration-level tests for the three controllers and the two remaining
 * domain-core value types (PhotoMetadata, GPSLocation).
 *
 * All tests use MockPhoto so no real JPEG files are needed.
 */
class ControllersTest {

    // ------------------------------------------------------------------
    // shared fixtures rebuilt before every test
    // ------------------------------------------------------------------

    private MainLibrary        mainLib;
    private TrashLibrary       trashLib;
    private ViewsCatalog       viewsCatalog;
    private AlbumsCatalog      albumsCatalog;

    private LibrariesController libCtrl;
    private ViewsController     viewCtrl;
    private AlbumsController    albCtrl;

    private MockPhoto p1, p2, p3;

    @BeforeEach
    void setUp() {
        mainLib      = new MainLibrary();
        trashLib     = new RecentlyDeletedLibrary();
        viewsCatalog = new ViewsCatalog(mainLib, trashLib);
        albumsCatalog = new AlbumsCatalog(mainLib);

        libCtrl  = new LibrariesController(mainLib, trashLib);
        viewCtrl = new ViewsController(viewsCatalog);
        albCtrl  = new AlbumsController(albumsCatalog);

        p1 = new MockPhoto("Alpha");
        p2 = new MockPhoto("Beta");
        p3 = new MockPhoto("Gamma");
    }

    // ==================================================================
    //  LibrariesController – importPhoto
    //  (real file import tested via SimpleClient; here we test the
    //   library-level behaviour through direct addPhoto calls)
    // ==================================================================

    // We cannot call importPhoto() without real JPEG files, so we
    // exercise the controller through the underlying library directly
    // and test deletePhotos / emptyTrash / toggleFavourite / getMatches.

    // ------------------------------------------------------------------
    //  deletePhotos
    // ------------------------------------------------------------------

    @Test
    void deletePhotos_movesPhotoFromMainToTrash() {
        mainLib.addPhoto(p1);
        libCtrl.deletePhotos(Set.of(p1));

        assertFalse(mainLib.getPhotos().contains(p1));
        assertTrue(trashLib.getPhotos().contains(p1));
    }

    @Test
    void deletePhotos_multiplePhotos_allMoved() {
        mainLib.addPhoto(p1);
        mainLib.addPhoto(p2);
        libCtrl.deletePhotos(Set.of(p1, p2));

        assertEquals(0, mainLib.getNumberOfPhotos());
        assertTrue(trashLib.getPhotos().contains(p1));
        assertTrue(trashLib.getPhotos().contains(p2));
    }

    @Test
    void deletePhotos_photoNotInMain_notAddedToTrash() {
        // p1 was never added to mainLib
        libCtrl.deletePhotos(Set.of(p1));
        assertFalse(trashLib.getPhotos().contains(p1));
    }

    @Test
    void deletePhotos_emptySet_noChange() {
        mainLib.addPhoto(p1);
        libCtrl.deletePhotos(Set.of());
        assertEquals(1, mainLib.getNumberOfPhotos());
    }

    // ------------------------------------------------------------------
    //  emptyTrash
    // ------------------------------------------------------------------

    @Test
    void emptyTrash_removesAllPhotosFromTrash() {
        mainLib.addPhoto(p1);
        mainLib.addPhoto(p2);
        libCtrl.deletePhotos(Set.of(p1, p2));

        libCtrl.emptyTrash();

        assertTrue(trashLib.getPhotos().isEmpty());
    }

    @Test
    void emptyTrash_doesNotAffectMainLibrary() {
        mainLib.addPhoto(p1);
        mainLib.addPhoto(p2);
        libCtrl.deletePhotos(Set.of(p1));

        libCtrl.emptyTrash();

        assertTrue(mainLib.getPhotos().contains(p2));
    }

    @Test
    void emptyTrash_onEmptyTrash_noException() {
        assertDoesNotThrow(() -> libCtrl.emptyTrash());
    }

    // ------------------------------------------------------------------
    //  toggleFavourite
    // ------------------------------------------------------------------

    @Test
    void toggleFavourite_photoInMain_becomesFavourite() {
        mainLib.addPhoto(p1);
        libCtrl.toggleFavourite(Set.of(p1));
        assertTrue(p1.isFavourite());
    }

    @Test
    void toggleFavourite_twice_restoresOriginalState() {
        mainLib.addPhoto(p1);
        libCtrl.toggleFavourite(Set.of(p1));
        libCtrl.toggleFavourite(Set.of(p1));
        assertFalse(p1.isFavourite());
    }

    @Test
    void toggleFavourite_photoNotInMain_notToggled() {
        // p1 not in mainLib
        libCtrl.toggleFavourite(Set.of(p1));
        assertFalse(p1.isFavourite());
    }

    @Test
    void toggleFavourite_multiplePhotos_allToggled() {
        mainLib.addPhoto(p1);
        mainLib.addPhoto(p2);
        libCtrl.toggleFavourite(Set.of(p1, p2));
        assertTrue(p1.isFavourite());
        assertTrue(p2.isFavourite());
    }

    // ------------------------------------------------------------------
    //  getMatches
    // ------------------------------------------------------------------

    @Test
    void getMatches_matchingTitle_returnsPhoto() {
        mainLib.addPhoto(p1); // "Alpha"
        Iterable<IPhoto> result = libCtrl.getMatches(".*Alpha.*");
        assertTrue(iterableContains(result, p1));
    }

    @Test
    void getMatches_noMatch_returnsEmpty() {
        mainLib.addPhoto(p1);
        assertFalse(libCtrl.getMatches(".*Vasconcelos.*").iterator().hasNext());
    }

    // ==================================================================
    //  ViewsController
    // ==================================================================

    @Test
    void getPhotos_allMain_returnsAllMainPhotos() {
        mainLib.addPhoto(p1);
        mainLib.addPhoto(p2);
        List<IPhoto> photos = viewCtrl.getPhotos(ViewsType.ALL_MAIN);
        assertEquals(2, photos.size());
        assertTrue(photos.contains(p1));
        assertTrue(photos.contains(p2));
    }

    @Test
    void getPhotos_allMain_emptyLibrary_returnsEmpty() {
        assertTrue(viewCtrl.getPhotos(ViewsType.ALL_MAIN).isEmpty());
    }

    @Test
    void getPhotos_favourites_onlyFavourites() {
        mainLib.addPhoto(p1);
        mainLib.addPhoto(p2);
        libCtrl.toggleFavourite(Set.of(p1));

        List<IPhoto> favs = viewCtrl.getPhotos(ViewsType.FAVOURITES);
        assertTrue(favs.contains(p1));
        assertFalse(favs.contains(p2));
    }

    @Test
    void getPhotos_allTrash_returnsPhotosInTrash() {
        mainLib.addPhoto(p1);
        libCtrl.deletePhotos(Set.of(p1));
        List<IPhoto> trash = viewCtrl.getPhotos(ViewsType.ALL_TRASH);
        assertTrue(trash.contains(p1));
    }

    @Test
    void getMatches_viewsController_matchingRegex() {
        mainLib.addPhoto(p1); // "Alpha"
        mainLib.addPhoto(p2); // "Beta"
        List<IPhoto> result = viewCtrl.getMatches(ViewsType.ALL_MAIN, ".*Alpha.*");
        assertTrue(result.contains(p1));
        assertFalse(result.contains(p2));
    }

    @Test
    void getMatches_viewsController_noMatch_returnsEmpty() {
        mainLib.addPhoto(p1);
        assertTrue(viewCtrl.getMatches(ViewsType.ALL_MAIN, ".*Vasconcelos.*").isEmpty());
    }

    @Test
    void setSortingCriteria_byTitle_sortsAlphabetically() {
        // sizes: p1=1000, p2=1000, p3=1000 (all same in MockPhoto)
        // use title comparator to get deterministic order
        mainLib.addPhoto(p3); // "Gamma"
        mainLib.addPhoto(p1); // "Alpha"
        mainLib.addPhoto(p2); // "Beta"

        viewCtrl.setSortingCriteria(ViewsType.ALL_MAIN,
                Comparator.comparing(IPhoto::title));

        List<IPhoto> photos = viewCtrl.getPhotos(ViewsType.ALL_MAIN);
        assertEquals(p1, photos.get(0)); // Alpha
        assertEquals(p2, photos.get(1)); // Beta
        assertEquals(p3, photos.get(2)); // Gamma
    }

    @Test
    void setSortingCriteria_reverseTitle_sortDescending() {
        mainLib.addPhoto(p1);
        mainLib.addPhoto(p2);
        mainLib.addPhoto(p3);

        viewCtrl.setSortingCriteria(ViewsType.ALL_MAIN,
                Comparator.comparing(IPhoto::title).reversed());

        List<IPhoto> photos = viewCtrl.getPhotos(ViewsType.ALL_MAIN);
        assertEquals(p3, photos.get(0)); // Gamma
        assertEquals(p2, photos.get(1)); // Beta
        assertEquals(p1, photos.get(2)); // Alpha
    }

    // ==================================================================
    //  AlbumsController
    // ==================================================================

    @Test
    void createAlbum_newName_returnsTrue() {
        assertTrue(albCtrl.createAlbum("Oppenheimer"));
    }

    @Test
    void createAlbum_duplicate_returnsFalse() {
        albCtrl.createAlbum("Oppenheimer");
        assertFalse(albCtrl.createAlbum("Oppenheimer"));
    }

    @Test
    void selectAlbum_setsSelectedAlbum() {
        albCtrl.createAlbum("Oppenheimer");
        albCtrl.selectAlbum("Oppenheimer");
        assertEquals(Optional.of("Oppenheimer"), albCtrl.getSelectedAlbum());
    }

    @Test
    void getSelectedAlbum_beforeSelect_isEmpty() {
        assertTrue(albCtrl.getSelectedAlbum().isEmpty());
    }

    @Test
    void removeAlbum_withSelectedAlbum_removesIt() {
        albCtrl.createAlbum("Oppenheimer");
        albCtrl.selectAlbum("Oppenheimer");
        albCtrl.removeAlbum();
        assertFalse(albCtrl.getAlbumNames().contains("Oppenheimer"));
    }

    @Test
    void removeAlbum_withNoSelection_noException() {
        assertDoesNotThrow(() -> albCtrl.removeAlbum());
    }

    @Test
    void addPhotos_toSelectedAlbum_photosAreAdded() {
        mainLib.addPhoto(p1);
        mainLib.addPhoto(p2);
        albCtrl.createAlbum("Oppenheimer");
        albCtrl.selectAlbum("Oppenheimer");
        albCtrl.addPhotos(Set.of(p1, p2));

        List<IPhoto> photos = albCtrl.getPhotos();
        assertTrue(photos.contains(p1));
        assertTrue(photos.contains(p2));
    }

    @Test
    void addPhotos_noAlbumSelected_noException() {
        assertDoesNotThrow(() -> albCtrl.addPhotos(Set.of(p1)));
    }

    @Test
    void removePhotos_fromSelectedAlbum_photosAreRemoved() {
        mainLib.addPhoto(p1);
        mainLib.addPhoto(p2);
        albCtrl.createAlbum("Oppenheimer");
        albCtrl.selectAlbum("Oppenheimer");
        albCtrl.addPhotos(Set.of(p1, p2));
        albCtrl.removePhotos(Set.of(p1));

        assertFalse(albCtrl.getPhotos().contains(p1));
        assertTrue(albCtrl.getPhotos().contains(p2));
    }

    @Test
    void removePhotos_noAlbumSelected_noException() {
        assertDoesNotThrow(() -> albCtrl.removePhotos(Set.of(p1)));
    }

    @Test
    void getPhotos_noAlbumSelected_returnsNull() {
        // per current implementation: catalog.getPhotos(null) returns null
        assertNull(albCtrl.getPhotos());
    }

    @Test
    void getAlbumNames_afterCreating_containsNames() {
        albCtrl.createAlbum("Oppenheimer");
        albCtrl.createAlbum("Monuments");
        Set<String> names = albCtrl.getAlbumNames();
        assertTrue(names.contains("Oppenheimer"));
        assertTrue(names.contains("Monuments"));
    }

    @Test
    void getAlbumNames_empty_returnsEmptySet() {
        assertTrue(albCtrl.getAlbumNames().isEmpty());
    }

    // ==================================================================
    //  Full scenario: add → mark fav → delete → emptyTrash → album
    // ==================================================================

    @Test
    void fullScenario_addFavDeleteAlbum() {
        mainLib.addPhoto(p1);
        mainLib.addPhoto(p2);
        mainLib.addPhoto(p3);

        // mark p1 as favourite
        libCtrl.toggleFavourite(Set.of(p1));
        assertTrue(viewCtrl.getPhotos(ViewsType.FAVOURITES).contains(p1));

        // delete p2
        libCtrl.deletePhotos(Set.of(p2));
        assertFalse(viewCtrl.getPhotos(ViewsType.ALL_MAIN).contains(p2));
        assertTrue(viewCtrl.getPhotos(ViewsType.ALL_TRASH).contains(p2));

        // empty trash
        libCtrl.emptyTrash();
        assertTrue(viewCtrl.getPhotos(ViewsType.ALL_TRASH).isEmpty());

        // create album and add remaining photos
        albCtrl.createAlbum("MyAlbum");
        albCtrl.selectAlbum("MyAlbum");
        albCtrl.addPhotos(Set.of(p1, p3));
        assertEquals(2, albCtrl.getPhotos().size());

        // delete p1 from main → auto-removed from album
        libCtrl.deletePhotos(Set.of(p1));
        assertEquals(1, albCtrl.getPhotos().size());
        assertFalse(albCtrl.getPhotos().contains(p1));
    }

    // ==================================================================
    //  Helper
    // ==================================================================

    private static boolean iterableContains(Iterable<IPhoto> it, IPhoto target) {
        for (IPhoto p : it) {
            if (p.equals(target)) return true;
        }
        return false;
    }
}
