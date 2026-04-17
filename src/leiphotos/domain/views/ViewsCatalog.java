package leiphotos.domain.views;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import leiphotos.domain.core.MainLibrary;
import leiphotos.domain.core.TrashLibrary;
import leiphotos.domain.facade.ViewsType;

public class ViewsCatalog implements IViewsCatalog{
    Map <ViewsType, ILibraryView> views = new HashMap<>();

    public ViewsCatalog(MainLibrary main, TrashLibrary trash) {
        this.views.put(ViewsType.ALL_MAIN, new MainLibraryView(main, photo -> true));
        this.views.put(ViewsType.ALL_TRASH, new TrashLibraryView(trash));
        this.views.put(ViewsType.FAVOURITES, new MainLibraryView(main, photo -> photo.isFavourite()));
        this.views.put(ViewsType.MOST_RECENT, new MainLibraryView(main, photo -> photo.capturedDate().isAfter(LocalDateTime.now().minusMonths(12))));
    }

    @Override
    public ILibraryView getView(ViewsType t) {
        return this.views.get(t);
    }
    
    
}
