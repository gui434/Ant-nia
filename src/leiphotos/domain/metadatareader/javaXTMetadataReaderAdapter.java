package leiphotos.domain.metadatareader;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import leiphotos.services.JavaXTJpegMetadataReader;

import java.io.File;
import java.io.FileNotFoundException;

public class javaXTMetadataReaderAdapter implements JpegMetadataReader {
    private JavaXTJpegMetadataReader reader;

    javaXTMetadataReaderAdapter(File file) throws FileNotFoundException {
         try {
            this.reader = new JavaXTJpegMetadataReader(file);
        } catch (IllegalArgumentException e) {
            throw new FileNotFoundException(file.getName()); // converte a exceção
        }
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
        String date = reader.getDate();
        if (date == null) {
            return LocalDateTime.of(1970, 1, 1, 0, 0); // data por omissão
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy:MM:dd HH:mm:ss");
        return LocalDateTime.parse(date, formatter);
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
