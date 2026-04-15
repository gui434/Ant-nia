package leiphotos.domain.albums;

import java.util.List;
import java.util.Set;

import leiphotos.domain.facade.IPhoto;

public interface IAlbumsCatalog {
    
    boolean createAlbum(String name);

    boolean deleteAlbum(String name);

    boolean containsAlbum(String name);

    boolean addPhotos(String albumName, Set<IPhoto> selectedPhotos);

    boolean removePhotos(String albumName, Set<IPhoto> selectedPhotos);

    List<IPhoto> getPhotos(String albumName);

    Set<String> getAlbumsNames();
}
