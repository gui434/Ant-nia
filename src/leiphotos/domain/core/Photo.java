package leiphotos.domain.core;
import java.io.File;
import java.time.LocalDateTime;
import java.util.Optional;

import leiphotos.domain.facade.GPSCoordinates;
import leiphotos.domain.facade.IPhoto;


class Photo implements IPhoto{

        Photo(String title, LocalDateTime dateAddedToLib, PhotoMetadata metadata, File
    pathToFile){

    }

    @Override
    public String title() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'title'");
    }

    @Override
    public LocalDateTime capturedDate() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'capturedDate'");
    }

    @Override
    public LocalDateTime addedDate() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addedDate'");
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
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'size'");
    }

    @Override
    public File file() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'file'");
    }

    @Override
    public boolean matches(String regexp) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'matches'");
    }

    
}
