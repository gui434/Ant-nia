package leiphotos.domain.views;

import leiphotos.domain.core.Library;
/**
 * A view of the trash library that shows all photos in the trash.
 * 
 * @author Guilherme Santos fc63768 , Tomás Peres fc63721
 */
public class TrashLibraryView extends ALibraryView {
    
	/**
	 * Constructor for TrashLibraryView. Initializes the view with the given library and a filter that shows all photos in the trash.
	 * @param library
	 * @returns a new instance of TrashLibraryView that displays all photos in the trash library
	 */
	public TrashLibraryView(Library library) {
		super(library, photo -> true);
	}
}
