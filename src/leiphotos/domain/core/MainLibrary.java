package leiphotos.domain.core;

import java.util.Collection;
import java.util.Comparator;

import leiphotos.domain.facade.IPhoto;
import leiphotos.utils.AbsSubject;
import leiphotos.domain.core.events.PhotoAddedLibraryEvent;
import leiphotos.domain.core.events.PhotoChangedLibraryEvent;
import leiphotos.domain.core.events.PhotoLibraryEvent;
import leiphotos.domain.core.events.PhotoRemovedLibraryEvent;

public class MainLibrary extends AbsSubject<PhotoLibraryEvent> implements Library {
    private Collection<IPhoto> photos;

    public MainLibrary() {
        this.photos = new java.util.ArrayList<>();
    }

    @Override
    public int getNumberOfPhotos() {
        return this.photos.size();
    }

    @Override
    public boolean addPhoto(IPhoto photo) {
        if (photo == null || this.photos.contains(photo)) {
            return false;
        }

        boolean success = this.photos.add(photo);
        if (success) {
            this.emitEvent(new PhotoAddedLibraryEvent(photo, this));
        }
        return success;
    }

    @Override
    public boolean deletePhoto(IPhoto photo) {
        if (photo == null || !this.photos.contains(photo)) {
            return false;
        }

        boolean success = this.photos.remove(photo);
        if (success) {
            this.emitEvent(new PhotoRemovedLibraryEvent(photo, this));
        }
        return success;
    }

    @Override
    public Collection<IPhoto> getPhotos() {
        return this.photos;
    }

    @Override
    public Collection<IPhoto> getMatches(String regexp) {
        Collection<IPhoto> matches = new java.util.ArrayList<>();
        for (IPhoto photo : this.photos) {
            if (photo.matches(regexp)) {
                matches.add(photo);
            }
        }
        return matches;
    }

    public void photoChanged(IPhoto photo) {
        if (this.photos.contains(photo)) {
            this.emitEvent(new PhotoChangedLibraryEvent(photo, this));
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("***** MAIN PHOTO LIBRARY: ").append(photos.size()).append(" photos *****\n");
        photos.stream().sorted(Comparator.comparing(IPhoto::title)).forEach(p -> sb.append(p).append("\n"));
        return sb.toString();
    }

}
