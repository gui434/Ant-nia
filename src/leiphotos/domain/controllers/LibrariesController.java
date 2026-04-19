package leiphotos.domain.controllers;

import java.io.FileNotFoundException;
import java.util.Optional;
import java.util.Set;

import leiphotos.domain.core.MainLibrary;
import leiphotos.domain.core.PhotoFactory;
import leiphotos.domain.core.TrashLibrary;
import leiphotos.domain.facade.ILibrariesController;
import leiphotos.domain.facade.IPhoto;

/**
 * LibrariesController is responsible for managing the main library and the trash library.
 * It provides functionalities to import photos, delete photos, empty the trash, toggle favourite status, and search for photos.
 * 
 * @author Guilherme Santos fc63768 , Tomás Peres fc63721
 */
public class LibrariesController implements ILibrariesController {

    private TrashLibrary trash;
    private MainLibrary main;

    /**
     * Constructor for LibrariesController.
     * @param mainLib the main library to manage photos
     * @param trashLib the trash library to manage deleted photos
     */
    public LibrariesController(MainLibrary mainLib, TrashLibrary trashLib) {
        this.main = mainLib;
        this.trash = trashLib;
    }

    @Override
    public Optional<IPhoto> importPhoto(String title, String pathToPhotoFile) {
        IPhoto photo;
        try {
            photo = PhotoFactory.INSTANCE.createPhoto(title, pathToPhotoFile);
        } catch (FileNotFoundException e) {
            return Optional.empty();
        }
        main.addPhoto(photo);
        return Optional.of(photo);
    }

    @Override
    public void deletePhotos(Set<IPhoto> selectedPhotos) {
        for (IPhoto photo : selectedPhotos) {
            if (main.deletePhoto(photo)) {
                trash.addPhoto(photo);
            }
        }
        System.out.println("Selected photos have been moved to the trash.");
    }

    @Override
    public void emptyTrash() {
        trash.deleteAll();
        System.out.println("Trash has been emptied.");
    }

    @Override
    public void toggleFavourite(Set<IPhoto> selectedPhotos) {
        for (IPhoto photo : selectedPhotos) {
            if (main.getPhotos().contains(photo)) {
                photo.toggleFavourite();
                main.photoChanged(photo);
            }
        }
    }

    @Override
    public Iterable<IPhoto> getMatches(String regExp) {
        return main.getMatches(regExp);
    }


    @Override
    public String toString() {
        return main.toString() + "\n" + trash.toString();
    }

}
