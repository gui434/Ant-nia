package leiphotos.domain.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import leiphotos.domain.facade.GPSCoordinates;

class PhotoTest {

	@Test
	void testCreatePhotoWithoutGPS() {
		LocalDateTime expectedCapturedDate = LocalDateTime.of(2024, 1, 1, 0, 0);
		PhotoMetadata meta = new PhotoMetadata(java.util.Optional.empty(), expectedCapturedDate, "test camara",
				"test manufaturer");
		File expectedFile = new File("test.jpg");
		String expectedTitle = "Test Photo";
		LocalDateTime expectedAddedDate = LocalDateTime.now();
		Photo photo = new Photo(expectedTitle, expectedAddedDate, meta, expectedFile);

		LocalDateTime actualCapturedDate = photo.capturedDate();
		assertEquals(expectedCapturedDate, actualCapturedDate);

		LocalDateTime actualAddedDate = photo.addedDate();
		assertEquals(expectedAddedDate, actualAddedDate);

		boolean isFavourite = photo.isFavourite();
		assertFalse(isFavourite);

		String actualTitle = photo.title();
		assertEquals(expectedTitle, actualTitle);

		Optional<? extends GPSCoordinates> actualPlace = photo.getPlace();
		assertEquals(Optional.empty(), actualPlace);
	}

	@Test
	void testCreatePhotoWithGPS() {
		//COMPLETE METHOD
	}

	@Test
	void testToggleFavourite() {
		LocalDateTime expectedCapturedDate = LocalDateTime.of(2024, 1, 1, 0, 0);
		PhotoMetadata meta = new PhotoMetadata(java.util.Optional.empty(), expectedCapturedDate, "test camara",
				"test manufaturer");
		File expectedFile = new File("test.jpg");
		String expectedTitle = "Test Photo";
		LocalDateTime expectedAddedDate = LocalDateTime.now();
		Photo photo = new Photo(expectedTitle, expectedAddedDate, meta, expectedFile);

		photo.toggleFavourite();
		boolean isFavouriteAfterToggle1 = photo.isFavourite();
		photo.toggleFavourite();
		boolean isFavouriteAfterToggle2 = photo.isFavourite();

		// Assert
		assertTrue(isFavouriteAfterToggle1);
		assertFalse(isFavouriteAfterToggle2);
	}

	@Test
	void testSize() { // requires the use of a mock file class
		LocalDateTime expectedCapturedDate = LocalDateTime.of(2024, 1, 1, 0, 0);
		PhotoMetadata meta = new PhotoMetadata(java.util.Optional.empty(), expectedCapturedDate, "test camara",
				"test manufaturer");
		long expectedSize = 1024;
		MockFile expectedFile = new MockFile("test.jpg", expectedSize);
		String expectedTitle = "Test Photo";
		LocalDateTime expectedAddedDate = LocalDateTime.now();
		//COMPLETE METHOD
	}

	@Test
	void testNoMatches() {
		String regexp = "Exp.*";
		PhotoMetadata meta = new PhotoMetadata(java.util.Optional.empty(), LocalDateTime.now(), "", "");
		Photo photo = new Photo("Test Photo", LocalDateTime.now(), meta, new File("test.jpg"));

		boolean matches = photo.matches(regexp);
		assertFalse(matches);
	}

	@Test
	void testMatchesTitle() {
		//COMPLETE METHOD
	}

	@Test
	void testMatchesFile() {
		//COMPLETE METHOD
	}

	@Test
	void testEquals() {
		File file1 = new File("test1.jpg");
		File file2 = new File("test2.jpg");
		File file3 = new File("test1.jpg");

		//COMPLETE METHOD
	}

	@Test
	void testHashCode() {
		File file1 = new File("test1.jpg");
		File file2 = new File("test2.jpg");

		//COMPLETE METHOD
	}

}