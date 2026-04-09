package leiphotos.domain.core.events;

import leiphotos.domain.core.Library;
import leiphotos.domain.facade.IPhoto;

public class PhotoRemovedLibraryEvent extends PhotoLibraryEvent{

    PhotoRemovedLibraryEvent(IPhoto photo, Library lib) {
        super(photo, lib);
        //TODO Auto-generated constructor stub
    }
    
}
