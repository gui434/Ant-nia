package leiphotos.domain.albums;

import java.util.List;
import java.util.Set;

import leiphotos.domain.core.MainLibrary;
import leiphotos.domain.core.events.PhotoLibraryEvent;
import leiphotos.domain.facade.IPhoto;


public class Album extends AAlbum {


    public Album(String name, MainLibrary lib) {
        super(name, lib);
    }

     @Override
    public int numberOfPhotos() {
        return getPhotos().size();
    }

    @Override
    public String getName() {
        return super.getName();
    }

    @Override
    public List<IPhoto> getPhotos() {
        return super.getPhotos();
    }

    @Override
    public boolean addPhotos(Set<IPhoto> photos) {
        return super.addPhotos(photos);
    }  

    @Override
    public boolean removePhotos(Set<IPhoto> photos) {
        return super.removePhotos(photos);

    }

    @Override
    public void processEvent(PhotoLibraryEvent e) {
        if(e instanceof PhotoLibraryEvent.PhotoLibraryPhotoRemovedEvent) {
            removePhotos(Set.of(e.getPhoto()));
            removePhotos(null);
        }

    }


}
