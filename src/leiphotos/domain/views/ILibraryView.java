package leiphotos.domain.views;

import java.util.Comparator;
import java.util.List;

import leiphotos.domain.facade.IPhoto;

public interface ILibraryView {
    /**
     * Sets the comparator used to sort the photos in the view. The photos will be sorted according to the order defined by the comparator.
     * @param c
     */
    void setComparator(Comparator<IPhoto> c);

    /**
     * Returns the number of photos currently displayed in the view. This method provides a way to retrieve the count of photos that are part of the view's current state.
     * @returns the number of photos currently displayed in the view
     */
    int numberOfPhotos();

    /**
    * Retrieves the list of photos currently displayed in the view. This method returns a list of photos that are part of the view's current state, which may be filtered or sorted based on the view's configuration.
    * @returns the list of photos currently displayed in the view
    */
    List<IPhoto> getPhotos();

    /**
     * Retrieves a list of photos that match the given regular expression. This method allows for searching photos based on specific criteria defined by the regular expression.
     * @param regexp the regular expression used to filter the photos
     * @returns a list of photos that match the given regular expression
     */
    List<IPhoto> getMatches(String regexp);
}
  