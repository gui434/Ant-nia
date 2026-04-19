package leiphotos.domain.core.events;

import leiphotos.domain.core.Library;
import leiphotos.domain.facade.IPhoto;

/**
 * Event representing a change in a photo within a library. This event is triggered when a photo is modified, such as when its metadata is updated or when it is moved to a different library.
 * 
 * @author Guilherme Santos fc63768 , Tomás Peres fc63721
 */
public class PhotoChangedLibraryEvent extends PhotoLibraryEvent{
    
    /**
     * Constructor for PhotoChangedLibraryEvent.
     *
     * @param photo the photo that was changed within the library
     * @param lib   the library containing the changed photo
     * 
     */
    public PhotoChangedLibraryEvent(IPhoto photo, Library lib) {
        super(photo, lib);
    }
    
}
