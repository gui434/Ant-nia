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

    /**
     * Cleans the trash library by removing photos that have been in the trash for too long. The specific cleaning logic is defined in the concrete implementation.
     * @ensures that photos that have been in the trash for too long are removed from the library
     */
     protected abstract void clean();

     /**
      * Determines if it's time to clean the trash library based on the time photos have been in the trash. The specific logic for determining cleaning time is defined in the concrete implementation.
      * @returns true if it's time to clean the trash library, false otherwise
      */
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
