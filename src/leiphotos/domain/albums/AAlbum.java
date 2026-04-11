
package leiphotos.domain.albums;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import leiphotos.domain.core.MainLibrary;
import leiphotos.domain.facade.IPhoto;

abstract class AAlbum implements IAlbum {

    private String name;
    private MainLibrary lib;
    private List<IPhoto> photos;

    public AAlbum(String name, MainLibrary lib) {
       this.name = name;
       this.lib = lib;
       this.photos = new ArrayList<>(); 
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
}