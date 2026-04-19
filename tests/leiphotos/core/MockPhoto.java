package leiphotos.core;

import java.io.File;
import java.time.LocalDateTime;
import java.util.Optional;

import leiphotos.domain.facade.GPSCoordinates;
import leiphotos.domain.facade.IPhoto;

/**
 * A simple mock implementation of IPhoto for testing purposes.
 * Avoids dependency on real JPEG files.
 */
public class MockPhoto implements IPhoto {

    private final String title;
    private final LocalDateTime capturedDate;
    private final LocalDateTime addedDate;
    private boolean favourite;
    private final long size;
    private final File file;

    public MockPhoto(String title) {
        this(title, LocalDateTime.now(), LocalDateTime.now(), 1000L, new File(title + ".jpg"));
    }

    public MockPhoto(String title, LocalDateTime capturedDate) {
        this(title, capturedDate, LocalDateTime.now(), 1000L, new File(title + ".jpg"));
    }

    public MockPhoto(String title, LocalDateTime capturedDate, LocalDateTime addedDate, long size, File file) {
        this.title = title;
        this.capturedDate = capturedDate;
        this.addedDate = addedDate;
        this.favourite = false;
        this.size = size;
        this.file = file;
    }

    @Override public String title() { return title; }
    @Override public LocalDateTime capturedDate() { return capturedDate; }
    @Override public LocalDateTime addedDate() { return addedDate; }
    @Override public boolean isFavourite() { return favourite; }
    @Override public void toggleFavourite() { favourite = !favourite; }
    @Override public Optional<? extends GPSCoordinates> getPlace() { return Optional.empty(); }
    @Override public long size() { return size; }
    @Override public File file() { return file; }

    @Override
    public boolean matches(String regexp) {
        return title.matches(regexp) || file.getName().matches(regexp);
    }

    @Override
    public String toString() {
        return "MockPhoto[" + title + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MockPhoto other)) return false;
        return title.equals(other.title) && file.equals(other.file);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(title, file);
    }
}
