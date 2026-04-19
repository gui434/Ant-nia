
package leiphotos.domain.albums;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import leiphotos.domain.core.MainLibrary;
import leiphotos.domain.core.events.PhotoLibraryEvent;
import leiphotos.domain.core.events.PhotoRemovedLibraryEvent;
import leiphotos.domain.facade.IPhoto;


/**
 * Abstract class AAlbum that implements the IAlbum interface. This class provides common functionality for all album types,
 * such as managing the album's name and photos, and handling photo removal events from the library.
 * 
 * @author Guilherme Santos fc63768 , Tomás Peres fc63721
 */
abstract class AAlbum implements IAlbum {

    private String name;
    private List<IPhoto> photos;

    /**
     * Constructor for AAlbum.
     * @param name the name of the album
     * @param lib  the main library to register the album as a listener for photo events
     */
    public AAlbum(String name, MainLibrary lib) {
        this.name = name;
        this.photos = new ArrayList<>();
        lib.registerListener(this); 
    }

    @Override
    public int numberOfPhotos() {
        return getPhotos().size();
    }
    
    @Override
    public String getName() {
        return name;
    }

    @Override
    public List<IPhoto> getPhotos() {
        return photos;
    }

    @Override
    public boolean addPhotos(Set<IPhoto> photos) {
        return this.photos.addAll(photos);
    }  
    
    @Override
    public boolean removePhotos(Set<IPhoto> photos) {
        return this.photos.removeAll(photos);
    }

    @Override
    public void processEvent(PhotoLibraryEvent e) {
        if(e instanceof PhotoRemovedLibraryEvent) {
            removePhotos(Set.of(e.getPhoto()));
        }
    }
}