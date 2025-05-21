package org.opendma.api;

import java.util.List;

public interface OdmaPageIterator<T extends OdmaObject>
{

    boolean nextPage();
    
    List<T> getPage();

    String nextPageMark();
    
    void goToMark(String mark);

}
