package leiphotos.domain.views;

import leiphotos.domain.core.Library;
/**
 * A view of the library that shows all photos in the trash.
 * 
 * @author Guilherme Santos fc63768 , Tomás Peres fc63721
 */
public class TrashLibraryView extends ALibraryView {
    
	public TrashLibraryView(Library library) {
		super(library, photo -> true);
	}
}
