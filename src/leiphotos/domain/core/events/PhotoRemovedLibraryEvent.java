package leiphotos.domain.core.events;
import leiphotos.domain.core.Library;
import leiphotos.domain.facade.IPhoto;

public class PhotoRemovedLibraryEvent extends PhotoLibraryEvent {
    
    /**
     * Constructor for PhotoRemovedLibraryEvent.
     *
     * @param photo the photo that was removed from the library
     * @param lib   the library from which the photo was removed
     * 
     * @author Guilherme Santos fc63768 , Tomás Peres fc63721
     */
    public PhotoRemovedLibraryEvent(IPhoto photo, Library lib) {
        super(photo, lib);
    }
}
