package leiphotos.domain.core;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import leiphotos.domain.facade.IPhoto;

/**
 * RecentlyDeletedLibrary is responsible for managing photos that have been recently deleted.
 * It provides functionalities to add, delete, and retrieve recently deleted photos.
 * 
 * @author Guilherme Santos fc63768 , Tomás Peres fc63721
 */
public class RecentlyDeletedLibrary extends ATrashLibrary{
   private Map<IPhoto, LocalDateTime> photosHash = new LinkedHashMap<>();
   private LocalDateTime lastCheckTime;
   private final int MAX_SECONDS_IN_TRASH = 15;
   private final int MIN_SECONDS_BETWEEN_CLEANING = 5;

    
    public RecentlyDeletedLibrary() {
        this.lastCheckTime = LocalDateTime.now();
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
        if (this.photos.add(photo)) {
            this.photosHash.put(photo, LocalDateTime.now());
            return true;
        }
        return false;
    }

    @Override
    public boolean deletePhoto(IPhoto photo) {
        if (photo == null || !this.photos.contains(photo)) {
            return false;
        }

        if (this.photos.remove(photo)) {
            this.photosHash.remove(photo);
            return true;
        }
        return false;
    }

    @Override
    public Collection<IPhoto> getMatches(String regexp) {
        Collection<IPhoto> matches = new ArrayList<>();
        for (IPhoto photo : this.photos) {
            if (photo.matches(regexp)) {
                matches.add(photo);
            }
        }
        return matches;
    }

    @Override
    protected void clean() {
        LocalDateTime now = LocalDateTime.now();
        lastCheckTime = now;

        Iterator<IPhoto> it = this.photos.iterator();
        while (it.hasNext()) {
            IPhoto photo = it.next();
            
            if (ChronoUnit.SECONDS.between(this.photosHash.get(photo), now) >= MAX_SECONDS_IN_TRASH) {
                it.remove();
                photosHash.remove(photo);
            }
        }
    }

    @Override
    protected boolean cleaningTime() {
        if (this.photos.isEmpty()) {
            return false;
        }

        LocalDateTime now = LocalDateTime.now();
        long secondsSinceLastCheck = ChronoUnit.SECONDS.between(lastCheckTime, now);
        IPhoto oldesPhoto = this.photos.iterator().next();
        LocalDateTime addedDate = this.photosHash.get(oldesPhoto);
        long secondsInTrash = ChronoUnit.SECONDS.between(addedDate, now);

        return secondsInTrash >= MAX_SECONDS_IN_TRASH && secondsSinceLastCheck >= MIN_SECONDS_BETWEEN_CLEANING;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("***** TRASH PHOTO LIBRARY: ").append(photos.size()).append(" photos ****\n");
        photos.forEach(p -> sb.append(p).append("\n"));
        return sb.toString();
    }
    
}
