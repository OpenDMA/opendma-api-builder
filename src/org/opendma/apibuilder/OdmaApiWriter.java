package org.opendma.apibuilder;

import java.io.IOException;

import org.opendma.apibuilder.apiwriter.ApiWriterException;
import org.opendma.apibuilder.structure.ApiDescription;

public interface OdmaApiWriter
{
    
    public void writeOdmaApi(ApiDescription classHierarchy, String outputFolderRoot) throws IOException, ApiWriterException;
    
    public String getProgrammingLanguageSpecificScalarDataType(boolean multiValue, int dataType);
    
    public String[] getRequiredScalarDataTypeImports(boolean multiValue, int dataType);
    
    public String getName();

}