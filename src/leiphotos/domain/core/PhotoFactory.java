package leiphotos.domain.core;

import java.time.LocalDateTime;
import java.util.Optional;

import leiphotos.domain.metadatareader.JpegMetadataException;
import leiphotos.domain.metadatareader.JpegMetadataReader;
import leiphotos.domain.metadatareader.JpegMetadataReaderFactory;
import java.io.File;

public enum PhotoFactory {
    INSTANCE;

    public Photo createPhoto(String title, String pathToPhotoFile) throws 
    java.io.FileNotFoundException {
        try{
            File file = new File(pathToPhotoFile);
            JpegMetadataReader leitor = JpegMetadataReaderFactory.INSTANCE.createMetadataReader(file);
            double[] gpsCoordinates = leitor.getGpsLocation();
            Optional<GPSLocation> gpsLocation = gpsCoordinates == null
                ? Optional.empty()
                : Optional.of(new GPSLocation(gpsCoordinates[1], gpsCoordinates[0]));
            PhotoMetadata metadata = new PhotoMetadata(
                gpsLocation,
                leitor.getDate(),
                leitor.getCamera(),
                leitor.getManufacturer()
            );
            return new Photo(title, LocalDateTime.now(), metadata, file);
        } catch (java.io.FileNotFoundException e){
            throw new java.io.FileNotFoundException("File not found: " + pathToPhotoFile);
        } catch (JpegMetadataException e){
            throw new RuntimeException("Error reading metadata from file: " + pathToPhotoFile, e);
        }

    }
    
}
