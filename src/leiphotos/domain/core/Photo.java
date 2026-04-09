package leiphotos.domain.core;
import java.io.File;
import java.time.LocalDateTime;
import java.util.Optional;

import leiphotos.domain.facade.GPSCoordinates;
import leiphotos.domain.facade.IPhoto;
import leiphotos.utils.RegExpMatchable;


class Photo implements IPhoto,RegExpMatchable {
    private PhotoMetadata photoMetadata;
    private LocalDateTime addedDate;
    private String title;
    private File file;

    Photo(String title, LocalDateTime dateAddedToLib, PhotoMetadata metadata, File pathToFile){
        this.title = title;
        this.addedDate = dateAddedToLib;
        this.file = pathToFile;
        this.photoMetadata = metadata;
    }

    @Override
    public String title() {
        return this.title;
    }

    @Override
    public LocalDateTime capturedDate() {
        throw new UnsupportedOperationException("Unimplemented method 'CapturedDate'");
    }

    @Override
    public LocalDateTime addedDate() {
        return this.addedDate;
 
    }

    @Override
    public boolean isFavourite() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'isFavourite'");
    }

    @Override
    public void toggleFavourite() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'toggleFavourite'");
    }

    @Override
    public Optional<? extends GPSCoordinates> getPlace() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getPlace'");
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
        return title.matches(regexp) || photoMetadata.matches(regexp);
    }

    
}
