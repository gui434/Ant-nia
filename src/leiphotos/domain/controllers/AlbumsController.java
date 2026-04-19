package leiphotos.domain.controllers;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;

import leiphotos.domain.albums.IAlbumsCatalog;
import leiphotos.domain.facade.IAlbumsController;
import leiphotos.domain.facade.IPhoto;

/**
 * AlbumsController is responsible for managing albums and their photos.
 * It interacts with the IAlbumsCatalog to perform operations on albums.
 * 
 * @author Guilherme Santos fc63768 , Tomás Peres fc63721
 */
public class AlbumsController implements IAlbumsController {
    private IAlbumsCatalog catalog;
    private String albumNameSelected;

    /**
     * Constructor for AlbumsController.
     * @param albumsCatalog the albums catalog to manage albums
     */
    public AlbumsController(IAlbumsCatalog albumsCatalog) {
        this.catalog = albumsCatalog;
        
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
        return false;
    }

    @Override
    public Set<String> getAlbumNames() {
        return catalog.getAlbumsNames();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("***** ALBUMS *****\n");
        for (String albumName : catalog.getAlbumsNames()) {
            List<IPhoto> photos = catalog.getPhotos(albumName);
            sb.append("\n***** Album ").append(albumName).append(": ")
            .append(photos.size()).append(" photos  *****\n");
            photos.forEach(p -> sb.append(p.file()).append("\n"));
        }
        return sb.toString();
    }

}
