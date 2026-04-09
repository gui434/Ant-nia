package leiphotos.domain.core.events;

import leiphotos.domain.core.Library;
import leiphotos.domain.facade.IPhoto;

public class PhotoAddedLibraryEvent extends PhotoLibraryEvent{

    PhotoAddedLibraryEvent(IPhoto photo, Library lib) {
        super(photo, lib);
        //TODO Auto-generated constructor stub
    }
    
}
