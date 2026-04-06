package leiphotos.domain.core;

import java.time.LocalDateTime;

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
            PhotoMetadata metadata = new PhotoMetadata(
                leitor.getCamera(),
                leitor.getManufacturer(),
                leitor.getDate(),
                leitor.getAperture(),
                leitor.getGpsLocation()
            );
            return new Photo(title, LocalDateTime.now(), metadata, file);
        } catch (java.io.FileNotFoundException e){
            throw new java.io.FileNotFoundException("File not found: " + pathToPhotoFile);
        } catch (JpegMetadataException e){
            throw new RuntimeException("Error reading metadata from file: " + pathToPhotoFile, e);
        }

    }
    
}
