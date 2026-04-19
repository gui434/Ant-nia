package leiphotos.domain.albums;

import java.util.List;
import java.util.Set;

import leiphotos.domain.facade.IPhoto;

/**
 * Interface for the albums catalog, which manages the albums and their photos.
    * @author Guilherme Santos fc63768 , Tomás Peres fc63721
 */
public interface IAlbumsCatalog {
    
    /**
     * Creates a new album with the given name. The album is added to the catalog and can be accessed by its name.
     * @param name
     * @returns true if the album was successfully created and added to the catalog, false if an album with the same name already exists or if the name is invalid
     */
    boolean createAlbum(String name);

    /**
     * Deletes the album with the given name from the catalog. The album and its photos are removed from the catalog.
     * @param name the name of the album to be deleted
     * @returns true if the album was successfully deleted from the catalog, false if no album with the given name exists in the catalog
     */
    boolean deleteAlbum(String name);

    /**
     * Checks if an album with the given name exists in the catalog.
     * @param name
     * @returns true if an album with the given name exists in the catalog, false otherwise
     */
    boolean containsAlbum(String name);

    /**
     * Adds the selected photos to the album with the given name. The photos are added to the album's collection of photos.
     * @param albumName
     * @param selectedPhotos
     * @returns true if the photos were successfully added to the album, false otherwise
     */
    boolean addPhotos(String albumName, Set<IPhoto> selectedPhotos);

    /**
     * Removes the selected photos from the album with the given name. The photos are removed from the album's collection of photos.
     * @param albumName
     * @param selectedPhotos
     * @returns true if the photos were successfully removed from the album, false otherwise
     */
    boolean removePhotos(String albumName, Set<IPhoto> selectedPhotos);

    /**
     * Retrieves the list of photos in the album with the given name.
     * @param albumName
     * @returns the list of photos in the album, or an empty list if the album does not exist
     */
    List<IPhoto> getPhotos(String albumName);

    /**
     * Retrieves the names of all albums in the catalog.
     * @returns the set of all album names in the catalog
     */
    Set<String> getAlbumsNames();
}
