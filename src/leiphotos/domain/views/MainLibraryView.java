package leiphotos.domain.views;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import leiphotos.domain.facade.IPhoto;
import leiphotos.utils.Listener;
import leiphotos.domain.core.MainLibrary;
import leiphotos.domain.core.events.PhotoLibraryEvent;

public class MainLibraryView extends ALibraryView implements Listener<PhotoLibraryEvent> {
	private List<IPhoto> cache;
	
	public MainLibraryView(MainLibrary lib, Predicate<IPhoto> belongsToView) {
		super(lib, belongsToView);
		this.cache = new ArrayList<>();
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
