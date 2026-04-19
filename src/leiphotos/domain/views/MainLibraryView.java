package leiphotos.domain.views;

import java.util.List;
import java.util.function.Predicate;

import leiphotos.domain.facade.IPhoto;
import leiphotos.utils.Listener;
import leiphotos.domain.core.MainLibrary;
import leiphotos.domain.core.events.PhotoLibraryEvent;

public class MainLibraryView extends ALibraryView implements Listener<PhotoLibraryEvent> {
	private List<IPhoto> cache;
	
	/**
	 * Constructor for MainLibraryView. Initializes the view with the given library and a filter that determines which photos belong to the view.
	 * @param lib
	 * @param belongsToView
	 * @returns a new instance of MainLibraryView that displays photos from the main library that satisfy the given filter condition
	 */
	public MainLibraryView(MainLibrary lib, Predicate<IPhoto> belongsToView) {
		super(lib, belongsToView);
		this.cache = null;
		lib.registerListener(this);
	}

	@Override
	public void processEvent(PhotoLibraryEvent e) {
		cache = null;
	}

	@Override
	public List<IPhoto> getPhotos() {
		if(cache == null) {
			cache = super.getPhotos();
		}
		return cache;
	}

}
