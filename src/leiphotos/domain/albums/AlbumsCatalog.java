package leiphotos.domain.albums;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

import leiphotos.domain.core.MainLibrary;
import leiphotos.domain.facade.IPhoto;


public class AlbumsCatalog implements IAlbumsCatalog {
    private MainLibrary lib;
    private HashMap<String, IAlbum> hashMap;


    public AlbumsCatalog(MainLibrary lib) {
        this.lib = lib;
        this.hashMap = new HashMap<>();
    }

    @Override
    public boolean createAlbum(String name) {
        if(!containsAlbum(name)){
            IAlbum newAlbum = new Album(name, lib);
            hashMap.put(name, newAlbum);
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteAlbum(String name) {
        if(containsAlbum(name)){
            hashMap.remove(name);
            return true;
        }
        return false;

    }

    @Override
    public boolean containsAlbum(String name) {
        return hashMap.containsKey(name);
    }

    @Override
    public boolean addPhotos(String albumName, Set<IPhoto> selectedPhotos) {
        if(containsAlbum(albumName)){
            IAlbum album = hashMap.get(albumName);
            album.addPhotos(selectedPhotos);
            return true;
            }
        return false;
       
    }

    @Override
    public boolean removePhotos(String albumName, Set<IPhoto> selectedPhotos) {
        if(containsAlbum(albumName)){
            IAlbum album = hashMap.get(albumName);
            return album.removePhotos(selectedPhotos);
            }
        return false;
    }

    @Override
    public List<IPhoto> getPhotos(String albumName) {
        if(containsAlbum(albumName)){
            return hashMap.get(albumName).getPhotos();
        }
        return null;
        
    }

    @Override
    public Set<String> getAlbumsNames() {
        return hashMap.keySet();
    }    

  
}
