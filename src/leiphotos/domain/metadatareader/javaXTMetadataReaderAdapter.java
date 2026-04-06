package leiphotos.domain.metadatareader;

import java.time.LocalDateTime;

import leiphotos.services.JavaXTJpegMetadataReader;

import java.io.File;

public class javaXTMetadataReaderAdapter implements JpegMetadataReader {
    private JavaXTJpegMetadataReader reader;

    javaXTMetadataReaderAdapter(File file){
        this.reader = new JavaXTJpegMetadataReader(file);
    }

    @Override
    public String getCamera() {
        return reader.getCamera();
    }

    @Override
    public String getManufacturer() {
        return reader.getManufacturer();
    }

    @Override
    public LocalDateTime getDate() {
        return LocalDateTime.parse(reader.getDate());
    }

    @Override
    public String getAperture() {
        return reader.getAperture();
    }

    @Override
    public double[] getGpsLocation() {
        return reader.getGPS();
    }
    
}
