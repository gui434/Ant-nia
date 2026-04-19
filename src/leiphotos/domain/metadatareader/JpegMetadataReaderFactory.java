package leiphotos.domain.metadatareader;
import java.io.File;

/**
 * Factory class for creating JPEG metadata readers.
 * 
 * @author Guilherme Santos fc63768 , Tomás Peres fc63721
 */
public enum JpegMetadataReaderFactory{
    INSTANCE;

    /**
     * Creates a JPEG metadata reader for the given file.
     *
     * @param file the file for which to create a metadata reader
     * @return a JPEG metadata reader for the given file
     * @throws JpegMetadataException if an error occurs while reading the metadata
     * @throws java.io.FileNotFoundException if the file is not found
     */
    public JpegMetadataReader createMetadataReader(File file) throws JpegMetadataException, java.io.FileNotFoundException {
        return new javaXTMetadataReaderAdapter(file);
    }
  
}
