package leiphotos.domain.controllers;

import java.util.Comparator;
import java.util.List;

import leiphotos.domain.facade.IPhoto;
import leiphotos.domain.facade.IViewsController;
import leiphotos.domain.facade.ViewsType;
import leiphotos.domain.views.IViewsCatalog;

public class ViewsController implements IViewsController {

    public ViewsController(IViewsCatalog views) {
        //TODO Auto-generated constructor stub
    }

    @Override
    public List<IPhoto> getPhotos(ViewsType viewType) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getPhotos'");
    }

    @Override
    public List<IPhoto> getMatches(ViewsType viewType, String regexp) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getMatches'");
    }

    @Override
    public void setSortingCriteria(ViewsType v, Comparator<IPhoto> criteria) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setSortingCriteria'");
    }
    
}
