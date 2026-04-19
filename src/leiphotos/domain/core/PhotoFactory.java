package leiphotos.domain.core;

import java.time.LocalDateTime;
import java.util.Optional;

import leiphotos.domain.metadatareader.JpegMetadataException;
import leiphotos.domain.metadatareader.JpegMetadataReader;
import leiphotos.domain.metadatareader.JpegMetadataReaderFactory;
import java.io.File;

/**
 * PhotoFactory is responsible for creating Photo instances.
 * It provides a method to create a photo with the given title and file path.
 * 
 * @author Guilherme Santos fc63768 , Tomás Peres fc63721
 */
public enum PhotoFactory {
    INSTANCE;

    public Photo createPhoto(String title, String pathToPhotoFile) throws java.io.FileNotFoundException {
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
            System.out.println("File " + pathToPhotoFile + " not found or could not be opened");
            throw e;
        } catch (JpegMetadataException e){
            throw new RuntimeException("Error reading metadata from file: " + pathToPhotoFile, e);
        }

    }
    
}
