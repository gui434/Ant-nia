package leiphotos.domain.metadatareader;

/**
 * Exception class for errors related to reading JPEG metadata.
 * 
 * @author Guilherme Santos fc63768 , Tomás Peres fc63721
 */
public class JpegMetadataException extends Exception {

	/**
	 * The serial version id (generated automatically by Eclipse)
	 */
	private static final long serialVersionUID = -3416001628323171383L;

	
	/**
	 * Creates an exception given an error message
	 * 
	 * @param message The error message
	 */
	public JpegMetadataException(String message) {
		super (message);
	}
	
	
	/**
	 * Creates an exception wrapping a lower level exception.
	 * 
	 * @param message The error message
	 * @param e The wrapped exception.
	 */
	public JpegMetadataException(String message, Exception e) {
		super (message, e);
	}

}
