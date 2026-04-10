package leiphotos.domain.core.events;

import leiphotos.domain.core.Library;
import leiphotos.domain.facade.IPhoto;

public class PhotoChangedLibraryEvent extends PhotoLibraryEvent{

    PhotoChangedLibraryEvent(IPhoto photo, Library lib) {
        super(photo, lib);
    }
    
}
