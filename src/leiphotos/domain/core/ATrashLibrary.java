package leiphotos.domain.core;

import java.util.ArrayList;
import java.util.Collection;

import leiphotos.domain.facade.IPhoto;

/**
 * An abstract implementation of the TrashLibrary interface, providing a base for concrete implementations.
 * This class manages a collection of photos and defines the structure for cleaning and determining cleaning time.
 * 
 * @author Guilherme Santos fc63768 , Tomás Peres fc63721
 */
public abstract class ATrashLibrary implements TrashLibrary {

    protected Collection<IPhoto> photos = new ArrayList<>();

     protected abstract void clean();

     protected abstract boolean cleaningTime();

    @Override
    public Collection<IPhoto> getPhotos() {
        while (cleaningTime()) {
            clean();
        }
        return this.photos;
    }

    @Override
    public boolean deleteAll() {
        boolean hadPhotos = !this.photos.isEmpty();
        while (!this.photos.isEmpty()) {
            clean();
        }
        return hadPhotos;
    }

}
