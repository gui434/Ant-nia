package leiphotos.domain.views;

import java.time.LocalDateTime;

import leiphotos.domain.core.MainLibrary;
import leiphotos.domain.core.TrashLibrary;
import leiphotos.domain.facade.ViewsType;

public class viewsCatalog implements IViewsCatalog{
    private final MainLibraryView allMain;
    private final TrashLibraryView allTrash;
    private final MainLibraryView favourites;
    private final MainLibraryView mostRecent;

    public viewsCatalog(MainLibrary main, TrashLibrary trash) {
        this.allMain = new MainLibraryView(main, photo -> true);
        this.allTrash = new TrashLibraryView(trash);
        this.favourites = new MainLibraryView(main, photo -> photo.isFavourite());
        this.mostRecent = new MainLibraryView(main, photo -> photo.capturedDate().isAfter(LocalDateTime.now().minusMonths(12)));
    }

    @Override
    public ILibraryView getView(ViewsType t) {
        return switch (t) {
            case ALL_MAIN -> allMain;
            case ALL_TRASH -> allTrash;
            case FAVOURITES -> favourites;
            case MOST_RECENT -> mostRecent;
        };
    }
    
}
