package leiphotos.domain.core;

import java.util.ArrayList;
import java.util.Collection;

import leiphotos.domain.facade.IPhoto;

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
