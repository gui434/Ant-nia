package leiphotos.domain.albums;
import leiphotos.domain.core.MainLibrary;

/**
 * Album class representing a photo album in the application.
 * It extends AAlbum and implements the necessary functionality for managing photos within the album.
 * @author Guilherme Santos fc63768 , Tomás Peres fc63721
 */
public class Album extends AAlbum {
    /**
     * Constructor for Album.
     * @param name the name of the album
     * @param lib  the main library to register the album as a listener for photo events
     */
    public Album(String name, MainLibrary lib) {
        super(name, lib);
    }

}
