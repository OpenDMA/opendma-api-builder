package org.opendma.api;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class OdmaDefaultPagingIterator<E extends OdmaObject> implements Iterator<E>
{

    private OdmaPageIterator<E> pageIterator;
    
    private int posInPage = -1;
    
    public OdmaDefaultPagingIterator(OdmaPageIterator<E> pageIterator)
    {
        super();
        this.pageIterator = pageIterator;
    }

    @Override
    public boolean hasNext()
    {
        if(posInPage == -2) {
            return false;
        } else if(posInPage == -1) {
            if(pageIterator.nextPage()) {
                posInPage = 0;
                // sanity test
                if(pageIterator.getPage() == null || pageIterator.getPage().size() <= 0) {
                    posInPage = -2;
                    return false;
                }
            } else {
                posInPage = -2;
                return false;
            }
        }
        if(posInPage >= pageIterator.getPage().size()) {
            if(pageIterator.nextPage()) {
                posInPage = 0;
                // sanity test
                if(pageIterator.getPage() == null || pageIterator.getPage().size() <= 0) {
                    posInPage = -2;
                    return false;
                }
            } else {
                posInPage = -2;
                return false;
            }
        }
        return true;
    }

    @Override
    public E next()
    {
        if(!hasNext()) {
            throw new NoSuchElementException();
        }
        return pageIterator.getPage().get(posInPage++);
    }

    @Override
    public void remove()
    {
        throw new UnsupportedOperationException();
    }

}
