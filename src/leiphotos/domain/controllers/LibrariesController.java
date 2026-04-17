package leiphotos.domain.controllers;

import java.io.FileNotFoundException;
import java.util.Optional;
import java.util.Set;

import leiphotos.domain.core.MainLibrary;
import leiphotos.domain.core.PhotoFactory;
import leiphotos.domain.core.TrashLibrary;
import leiphotos.domain.facade.ILibrariesController;
import leiphotos.domain.facade.IPhoto;

public class LibrariesController implements ILibrariesController {

    private TrashLibrary trash;
    private MainLibrary main;

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
            e.printStackTrace();
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
        
    }

    @Override
    public void emptyTrash() {
        trash.deleteAll();
    }

    @Override
    public void toggleFavourite(Set<IPhoto> selectedPhotos) {
        for (IPhoto photo : selectedPhotos) {
            if (main.getPhotos().contains(photo)) {
                photo.toggleFavourite();
            }
        }
    }

    @Override
    public Iterable<IPhoto> getMatches(String regExp) {
        return main.getMatches(regExp);
    }


}
