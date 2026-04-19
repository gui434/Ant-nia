package leiphotos.domain.core;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Optional;

import leiphotos.domain.facade.GPSCoordinates;
import leiphotos.domain.facade.IPhoto;
import leiphotos.utils.RegExpMatchable;

/**
 * Photo represents a photo in the library.
 * It contains metadata about the photo and provides methods to access and modify its properties.
 * 
 * @author Guilherme Santos fc63768 , Tomás Peres fc63721
 */
public class Photo implements IPhoto,RegExpMatchable {
    private PhotoMetadata photoMetadata;
    private LocalDateTime addedDate;
    private String title;
    private File file;
    private boolean isFavourite;

    /**
     * Constructor for Photo.
     * @param title the title of the photo
     * @param dateAddedToLib the date and time when the photo was added to the library
     * @param metadata the metadata of the photo, including GPS location, captured date, camera, and manufacturer
     * @param pathToFile the file path to the photo
     * @returns a new instance of Photo with the provided title, metadata, and file path
     */
    public Photo(String title, LocalDateTime dateAddedToLib, PhotoMetadata metadata, File pathToFile){
        this.title = title;
        this.addedDate = dateAddedToLib;
        this.file = pathToFile;
        this.photoMetadata = metadata;
        this.isFavourite = false;
    }

    @Override
    public String title() {
        return this.title;
    }

    @Override
    public LocalDateTime capturedDate() {
        return this.photoMetadata.date();
    }

    @Override
    public LocalDateTime addedDate() {
        return this.addedDate;
 
    }

    @Override
    public boolean isFavourite() {
        return this.isFavourite;
    }

    @Override
    public void toggleFavourite() {
        this.isFavourite = !this.isFavourite;
    }

    @Override
    public Optional<? extends GPSCoordinates> getPlace() {
        return this.photoMetadata.gpsLocation();
    }

    @Override
    public long size() {
        return this.file.length();
    }

    @Override
    public File file() {
        return this.file;
    }

    @Override
    public boolean matches(String regexp) {
        return title.matches(regexp) || photoMetadata.matches(regexp) || file.getName().matches(regexp);
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return " File:" + file + "\n" +
           "   Title:" + title + 
           "  Added:" + addedDate.format(formatter) + 
           "  Size:" + String.format(Locale.ENGLISH, "%,d", size()) + "\n" +
           "   " + photoMetadata +
           (isFavourite ? "  FAV" : "");
    }

}
