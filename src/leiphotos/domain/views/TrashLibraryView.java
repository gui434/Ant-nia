package leiphotos.domain.views;

import leiphotos.domain.core.Library;

public class TrashLibraryView extends ALibraryView {
    
	public TrashLibraryView(Library library) {
		super(library, photo -> true);
	}
}
