package leiphotos.domain.controllers;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;

import leiphotos.domain.albums.IAlbumsCatalog;
import leiphotos.domain.facade.IAlbumsController;
import leiphotos.domain.facade.IPhoto;

public class AlbumsController implements IAlbumsController {
    private IAlbumsCatalog catalog;
    private String albumNameSelected;
    private Predicate<IPhoto> criteria;

    public AlbumsController(){
        
    }

    @Override
    public boolean createAlbum(String name) {
        return catalog.createAlbum(name);
    }

    @Override
    public void removeAlbum() {
        if(albumNameSelected != null)
            catalog.deleteAlbum(albumNameSelected);
        
    }

    @Override
    public void selectAlbum(String name) {
        albumNameSelected = name;
    }

    @Override
    public void addPhotos(Set<IPhoto> selectedPhotos) {
        if(albumNameSelected != null)
            catalog.addPhotos(albumNameSelected, selectedPhotos);
        
    }

    @Override
    public void removePhotos(Set<IPhoto> selectedPhotos) {
        if(albumNameSelected != null)
            catalog.removePhotos(albumNameSelected, selectedPhotos);      
    }

    @Override
    public List<IPhoto> getPhotos() {
        return catalog.getPhotos(albumNameSelected);
    }

    @Override
    public Optional<String> getSelectedAlbum() {
        return Optional.ofNullable(albumNameSelected);
    }

    @Override
    public boolean createSmartAlbum(String name, Predicate<IPhoto> criteria) {
        this.criteria = criteria;
        if(!catalog.containsAlbum(name)){
            createAlbum(name);
            return true;
        }
        return false;
    }

    @Override
    public Set<String> getAlbumNames() {
        return catalog.getAlbumsNames();
    }

}
