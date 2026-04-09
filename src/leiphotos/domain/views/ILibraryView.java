package leiphotos.domain.views;

import java.util.Comparator;
import java.util.List;

import leiphotos.domain.facade.IPhoto;

public interface ILibraryView {
    void setComparator(Comparator<IPhoto> c);

    int numberOfPhotos();

    List<IPhoto> getPhotos();

    List<IPhoto> getMatches(String regexp);
}
