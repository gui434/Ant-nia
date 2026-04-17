package leiphotos.domain.controllers;

import java.util.Comparator;
import java.util.List;

import leiphotos.domain.facade.IPhoto;
import leiphotos.domain.facade.IViewsController;
import leiphotos.domain.facade.ViewsType;
import leiphotos.domain.views.IViewsCatalog;
import leiphotos.domain.views.ViewsCatalog;

public class ViewsController implements IViewsController {

    private IPhoto photo;
    private IViewsCatalog viewsCatalog;

    public ViewsController(IViewsCatalog views) {
        this.viewsCatalog = views;
    }

    @Override
    public List<IPhoto> getPhotos(ViewsType viewType) {
        return this.viewsCatalog.getView(viewType).getPhotos();
    }

    @Override
    public List<IPhoto> getMatches(ViewsType viewType, String regexp) {
        return this.viewsCatalog.getView(viewType).getMatches(regexp);
    }

    @Override
    public void setSortingCriteria(ViewsType v, Comparator<IPhoto> criteria) {
        this.viewsCatalog.getView(v).setComparator(criteria);
    }
    
}
