package leiphotos.domain.core;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Optional;

import leiphotos.domain.facade.GPSCoordinates;
import leiphotos.domain.facade.IPhoto;
import leiphotos.utils.RegExpMatchable;


class Photo implements IPhoto,RegExpMatchable {
    private PhotoMetadata photoMetadata;
    private LocalDateTime addedDate;
    private String title;
    private File file;
    private boolean isFavourite;

    Photo(String title, LocalDateTime dateAddedToLib, PhotoMetadata metadata, File pathToFile){
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
