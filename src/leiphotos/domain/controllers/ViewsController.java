package leiphotos.domain.controllers;

import java.util.Comparator;
import java.util.List;

import leiphotos.domain.facade.IPhoto;
import leiphotos.domain.facade.IViewsController;
import leiphotos.domain.facade.ViewsType;
import leiphotos.domain.views.ILibraryView;
import leiphotos.domain.views.IViewsCatalog;

/**
 * ViewsController is responsible for managing different views of photos based on various criteria.
 * It interacts with the IViewsCatalog to retrieve and manipulate photo views.
 * 
 * @author Guilherme Santos fc63768 , Tomás Peres fc63721
 */
public class ViewsController implements IViewsController {

    private IViewsCatalog viewsCatalog;

    /**
     * Constructor for ViewsController.
     * @param views the views catalog to manage different photo views
     */
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("***** VIEWS *****\n");
        for (ViewsType type : ViewsType.values()) {
            ILibraryView view = this.viewsCatalog.getView(type);
            sb.append("***** VIEW ").append(type).append(": ")
            .append(view.numberOfPhotos()).append(" photos *****\n");
            view.getPhotos().forEach(p -> sb.append(p.file()).append("\n"));
        }
        return sb.toString();
    }
    
}
