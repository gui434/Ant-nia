package leiphotos.domain.core.events;
import leiphotos.domain.core.Library;
import leiphotos.domain.facade.IPhoto;

/**
 * Event representing the addition of a photo to the library.
 * 
 * @author Guilherme Santos fc63768 , Tomás Peres fc63721
 */
public class PhotoAddedLibraryEvent extends PhotoLibraryEvent {

    /**
     * Constructor for PhotoAddedLibraryEvent.
     * @param photo the photo that was added to the library
     * @param lib   the library where the photo was added
     */
    public PhotoAddedLibraryEvent(IPhoto photo, Library lib) {
        super(photo, lib);
    }

}
