package leiphotos.domain.core.events;
import leiphotos.domain.core.Library;
import leiphotos.domain.facade.IPhoto;

public class PhotoRemovedLibraryEvent extends PhotoLibraryEvent {
    public PhotoRemovedLibraryEvent(IPhoto photo, Library lib) {
        super(photo, lib);
    }
}
