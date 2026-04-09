package leiphotos.domain.views;

import java.util.Comparator;
import java.util.List;

import leiphotos.domain.facade.IPhoto;

abstract class ALibraryView implements ILibraryView {

    protected Comparator<IPhoto> comparator;

    @Override
    public void setComparator(Comparator<IPhoto> c) {
        this.comparator = c;
    }

    public int numberOfPhotos() {
        throw ExceptionInInitializerError("numberOfPhotos not implemented");
    }

    public List<IPhoto> getPhotos() {

    }

    List<IPhoto> getMatches(String regexp) {
        
    }

    
}
