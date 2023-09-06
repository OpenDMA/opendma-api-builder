package org.opendma.apibuilder;

import java.io.IOException;
import java.io.InputStream;

import org.opendma.apibuilder.apiwriter.ApiWriterException;
import org.opendma.apibuilder.structure.ApiDescription;
import org.opendma.apibuilder.structure.ScalarTypeDescription;

public interface OdmaApiWriter
{
    
    public void writeOdmaApi(ApiDescription classHierarchy) throws IOException, ApiWriterException;
    
    public String getScalarDataType(ScalarTypeDescription scalarTypeDescription, boolean multiValue, boolean notNull);
    
    public String[] getScalarDataTypeImports(ScalarTypeDescription scalarTypeDescription, boolean multiValue, boolean notNull);
    
    public String getName();
    
    public InputStream getTemplateAsStream(String templateName);

}