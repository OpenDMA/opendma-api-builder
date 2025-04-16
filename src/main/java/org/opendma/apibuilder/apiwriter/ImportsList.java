package org.opendma.apibuilder.apiwriter;

import java.util.ArrayList;

public class ImportsList extends ArrayList<String>
{

    private static final long serialVersionUID = -5081098427617393516L;
    
    public void registerImport(String importPackage)
    {
        if(importPackage == null)
        {
            return;
        }
        if(!this.contains(importPackage))
        {
            add(importPackage);
        }
    }
    
    public void registerImports(String[] importPackages)
    {
        if(importPackages == null)
        {
            return;
        }
        for(int i = 0; i < importPackages.length; i++)
        {
            String importPackage = importPackages[i];
            if(!this.contains(importPackage))
            {
                add(importPackage);
            }
        }
    }

}
