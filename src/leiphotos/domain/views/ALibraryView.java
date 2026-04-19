package leiphotos.domain.views;

import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

import leiphotos.domain.core.Library;
import leiphotos.domain.facade.IPhoto;

/**
 * An abstract implementation of ILibraryView that provides common functionality for different types of library views.
 * It allows filtering photos based on a predicate and sorting them using a comparator.
 * 
 * @author Guilherme Santos fc63768 , Tomás Peres fc63721
 */
abstract class ALibraryView implements ILibraryView {

    private Comparator<IPhoto> comparator;
    private Predicate<IPhoto> belongsToView;
    private final Library library;

    /**
     * Constructs an ALibraryView with the specified library and predicate for filtering photos.
     * @param library the library containing the photos
     * @param belongsToView the predicate for filtering photos
     */
    public ALibraryView(Library library, Predicate<IPhoto> belongsToView) {
        this.library = library;
        this.belongsToView = belongsToView;
        this.comparator = Comparator.comparingLong((IPhoto p) -> p.size());
    }

    @Override
    public void setComparator(Comparator<IPhoto> c) {
        this.comparator = c;
    }

    @Override
    public int numberOfPhotos() {
        return getPhotos().size();
    }

    @Override
    public List<IPhoto> getPhotos() {
        List<IPhoto> photos = library.getPhotos().stream()
                .filter(belongsToView)
                .sorted(comparator)
                .toList();
        return photos;
    }

    @Override
    public List<IPhoto> getMatches(String regexp) {
        List<IPhoto> photos = getPhotos().stream()
                .filter(photo -> photo.matches(regexp))
                .toList();
        return photos;
    }

    
}
