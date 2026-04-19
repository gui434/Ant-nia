package leiphotos.views;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

import leiphotos.domain.core.MainLibrary;
import leiphotos.domain.core.MockPhoto;
import leiphotos.domain.core.RecentlyDeletedLibrary;
import leiphotos.domain.core.TrashLibrary;
import leiphotos.domain.facade.IPhoto;
import leiphotos.domain.facade.ViewsType;
import leiphotos.domain.views.ViewsCatalog;
import leiphotos.domain.views.ILibraryView;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for MainLibraryView and ViewsCatalog.
 *
 * MockPhoto lives in domain.core — these tests depend on it being visible.
 */
class MainLibraryViewAndCatalogTest {

    // ------------------------------------------------------------------ fixtures

    private MainLibrary mainLib;
    private TrashLibrary trashLib;
    private ViewsCatalog catalog;

    private MockPhoto small;   // size 100
    private MockPhoto medium;  // size 500
    private MockPhoto large;   // size 900
    private MockPhoto recent;  // captured 6 months ago  → in MOST_RECENT
    private MockPhoto old;     // captured 5 years ago   → NOT in MOST_RECENT

    @BeforeEach
    void setUp() {
        mainLib  = new MainLibrary();
        trashLib = new RecentlyDeletedLibrary();
        catalog  = new ViewsCatalog(mainLib, trashLib);

        small  = new MockPhoto(new java.io.File("small.jpg"));
        medium = new MockPhoto(new java.io.File("medium.jpg"));
        large  = new MockPhoto(new java.io.File("large.jpg"));

        recent = new MockPhoto(new java.io.File("recent.jpg"));
        old    = new MockPhoto(new java.io.File("old.jpg"));
    }

    // ==================================================================
    //  MainLibraryView — basic behaviour
    // ==================================================================

    @Test
    void allMain_emptyLibrary_returnsEmptyList() {
        List<IPhoto> photos = catalog.getView(ViewsType.ALL_MAIN).getPhotos();
        assertTrue(photos.isEmpty());
    }

    @Test
    void allMain_afterAddingPhoto_containsPhoto() {
        mainLib.addPhoto(small);
        assertTrue(catalog.getView(ViewsType.ALL_MAIN).getPhotos().contains(small));
    }

    @Test
    void allMain_numberOfPhotos_matchesLibrary() {
        mainLib.addPhoto(small);
        mainLib.addPhoto(medium);
        assertEquals(2, catalog.getView(ViewsType.ALL_MAIN).numberOfPhotos());
    }

    // ------------------------------------------------------------------ default sort (ascending size)

    @Test
    void allMain_defaultSort_isAscendingBySize() {
        mainLib.addPhoto(large);
        mainLib.addPhoto(small);
        mainLib.addPhoto(medium);

        List<IPhoto> photos = catalog.getView(ViewsType.ALL_MAIN).getPhotos();
        assertEquals(small,  photos.get(0));
        assertEquals(medium, photos.get(1));
        assertEquals(large,  photos.get(2));
    }

    // ------------------------------------------------------------------ custom comparator

    @Test
    void setComparator_byTitle_sortsAlphabetically() {
        mainLib.addPhoto(large);   // "LargePhoto"
        mainLib.addPhoto(small);   // "SmallPhoto"
        mainLib.addPhoto(medium);  // "MediumPhoto"

        ILibraryView view = catalog.getView(ViewsType.ALL_MAIN);
        view.setComparator(Comparator.comparing(IPhoto::title));

        List<IPhoto> photos = view.getPhotos();
        assertEquals("LargePhoto",  photos.get(0).title());
        assertEquals("MediumPhoto", photos.get(1).title());
        assertEquals("SmallPhoto",  photos.get(2).title());
    }

    // ------------------------------------------------------------------ cache invalidation

    @Test
    void cache_invalidatedOnAdd_returnsUpdatedList() {
        ILibraryView view = catalog.getView(ViewsType.ALL_MAIN);
        view.getPhotos(); // prime cache

        mainLib.addPhoto(small);
        assertTrue(view.getPhotos().contains(small));
    }

    @Test
    void cache_invalidatedOnDelete_returnsUpdatedList() {
        mainLib.addPhoto(small);
        ILibraryView view = catalog.getView(ViewsType.ALL_MAIN);
        view.getPhotos(); // prime cache

        mainLib.deletePhoto(small);
        assertFalse(view.getPhotos().contains(small));
    }

    @Test
    void cache_invalidatedOnToggleFavourite_returnsUpdatedList() {
        mainLib.addPhoto(small);
        ILibraryView view = catalog.getView(ViewsType.FAVOURITES);
        view.getPhotos(); // prime cache — small is not favourite

        small.toggleFavourite();
        mainLib.photoChanged(small); // notify library → invalidate cache

        assertTrue(view.getPhotos().contains(small));
    }

    // ------------------------------------------------------------------ getMatches

    @Test
    void getMatches_matchingRegex_returnsCorrectPhotos() {
        mainLib.addPhoto(small);
        mainLib.addPhoto(medium);
        List<IPhoto> result = catalog.getView(ViewsType.ALL_MAIN).getMatches(".*Small.*");
        assertTrue(result.contains(small));
        assertFalse(result.contains(medium));
    }

    @Test
    void getMatches_noMatch_returnsEmptyList() {
        mainLib.addPhoto(small);
        assertTrue(catalog.getView(ViewsType.ALL_MAIN).getMatches(".*Vasconcelos.*").isEmpty());
    }

    @Test
    void getMatches_matchAll_returnsAll() {
        mainLib.addPhoto(small);
        mainLib.addPhoto(medium);
        List<IPhoto> result = catalog.getView(ViewsType.ALL_MAIN).getMatches(".*");
        assertEquals(2, result.size());
    }

    // ==================================================================
    //  ViewsCatalog — FAVOURITES view
    // ==================================================================

    @Test
    void favourites_noFavourites_isEmpty() {
        mainLib.addPhoto(small);
        assertTrue(catalog.getView(ViewsType.FAVOURITES).getPhotos().isEmpty());
    }

    @Test
    void favourites_afterToggle_containsFavPhoto() {
        mainLib.addPhoto(small);
        small.toggleFavourite();
        mainLib.photoChanged(small);

        assertTrue(catalog.getView(ViewsType.FAVOURITES).getPhotos().contains(small));
    }

    @Test
    void favourites_afterDoubleToggle_doesNotContainPhoto() {
        mainLib.addPhoto(small);
        small.toggleFavourite();
        mainLib.photoChanged(small);
        small.toggleFavourite();
        mainLib.photoChanged(small);

        assertFalse(catalog.getView(ViewsType.FAVOURITES).getPhotos().contains(small));
    }

    @Test
    void favourites_onlyFavouritesIncluded() {
        mainLib.addPhoto(small);
        mainLib.addPhoto(medium);
        small.toggleFavourite();
        mainLib.photoChanged(small);

        List<IPhoto> favs = catalog.getView(ViewsType.FAVOURITES).getPhotos();
        assertTrue(favs.contains(small));
        assertFalse(favs.contains(medium));
    }

    // ==================================================================
    //  ViewsCatalog — MOST_RECENT view  (last 12 months)
    // ==================================================================

    @Test
    void mostRecent_recentPhoto_included() {
        mainLib.addPhoto(recent);
        assertTrue(catalog.getView(ViewsType.MOST_RECENT).getPhotos().contains(recent));
    }

    @Test
    void mostRecent_oldPhoto_notIncluded() {
        mainLib.addPhoto(old);
        assertFalse(catalog.getView(ViewsType.MOST_RECENT).getPhotos().contains(old));
    }

    @Test
    void mostRecent_mixedPhotos_onlyRecentIncluded() {
        mainLib.addPhoto(recent);
        mainLib.addPhoto(old);
        List<IPhoto> result = catalog.getView(ViewsType.MOST_RECENT).getPhotos();
        assertTrue(result.contains(recent));
        assertFalse(result.contains(old));
    }

    // ==================================================================
    //  ViewsCatalog — ALL_TRASH view
    // ==================================================================

    @Test
    void allTrash_emptyTrash_isEmpty() {
        assertTrue(catalog.getView(ViewsType.ALL_TRASH).getPhotos().isEmpty());
    }

    @Test
    void allTrash_afterAddingToTrash_containsPhoto() {
        trashLib.addPhoto(small);
        assertTrue(catalog.getView(ViewsType.ALL_TRASH).getPhotos().contains(small));
    }

    @Test
    void allTrash_afterDeleteAll_isEmpty() {
        trashLib.addPhoto(small);
        trashLib.deleteAll();
        assertTrue(catalog.getView(ViewsType.ALL_TRASH).getPhotos().isEmpty());
    }

    // ==================================================================
    //  ViewsCatalog — getView returns non-null for all types
    // ==================================================================

    @Test
    void catalog_allViewTypes_nonNull() {
        for (ViewsType t : ViewsType.values()) {
            assertNotNull(catalog.getView(t), "Expected non-null view for " + t);
        }
    }
}
