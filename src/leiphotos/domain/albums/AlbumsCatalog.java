package leiphotos.domain.albums;

import java.util.List;
import java.util.Set;

import leiphotos.domain.facade.IPhoto;


public class AlbumsCatalog implements IAlbumsCatalog {
    

    @Override
    public boolean createAlbum(String name) {
        throw new UnsupportedOperationException("Unimplemented method 'createAlbum'");
    }

    @Override
    public boolean deleteAlbum(String name) {
        throw new UnsupportedOperationException("Unimplemented method 'deleteAlbum'");
    }

    @Override
    public boolean containsAlbum(String name) {
        throw new UnsupportedOperationException("Unimplemented method 'containsAlbum'");
    }

    @Override
    public boolean addPhotos(String albumName, Set<IPhoto> selectedPhotos) {
        throw new UnsupportedOperationException("Unimplemented method 'addPhotos'");
    }

    @Override
    public boolean removePhotos(String albumName, Set<IPhoto> selectedPhotos) {
        throw new UnsupportedOperationException("Unimplemented method 'removePhotos'");
    }

    @Override
    public List<IPhoto> getPhotos(String albumName) {
        throw new UnsupportedOperationException("Unimplemented method 'getPhotos'");
    }

    @Override
    public Set<String> getAlbumsNames() {
        throw new UnsupportedOperationException("Unimplemented method 'getAlbumsNames'");
    }

  
}
