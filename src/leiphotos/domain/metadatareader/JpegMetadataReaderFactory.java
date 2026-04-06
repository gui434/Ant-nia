package leiphotos.domain.metadatareader;
import java.io.File;

public enum JpegMetadataReaderFactory{
    INSTANCE;

    public JpegMetadataReader createMetadataReader(File file) throws JpegMetadataException, java.io.FileNotFoundException {
        return new javaXTMetadataReaderAdapter(file);
    }
  
}
