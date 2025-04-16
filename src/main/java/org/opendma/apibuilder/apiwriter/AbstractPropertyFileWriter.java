package org.opendma.apibuilder.apiwriter;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;

import org.opendma.apibuilder.structure.ApiDescription;
import org.opendma.apibuilder.structure.ScalarTypeDescription;

public abstract class AbstractPropertyFileWriter
{
    
    protected abstract void writePropertyFileHeader(ApiDescription apiDescription, List<String> requiredImports, PrintWriter out) throws IOException;
    
    protected abstract void writeGenericSection(ApiDescription apiDescription, PrintWriter out) throws IOException;
    
    protected abstract void writeSingleValueScalarAccess(ScalarTypeDescription scalarTypeDescription, PrintWriter out) throws IOException;
    
    protected abstract void writeMultiValueScalarAccess(ScalarTypeDescription scalarTypeDescription, PrintWriter out) throws IOException;
    
    protected abstract void writePropertyFileFooter(ApiDescription apiDescription, PrintWriter out) throws IOException;
    
    protected abstract void appendRequiredImportsGlobal(ImportsList requiredImports);
    
    protected abstract void appendRequiredImportsScalarAccess(ImportsList requiredImports, ScalarTypeDescription scalarTypeDescription);


    public void createPropertyFile(ApiDescription apiDescription, OutputStream propertyOutputStream) throws IOException
    {
        createPropertyFile(apiDescription, propertyOutputStream, true);
    }

    public void createPropertyFile(ApiDescription apiDescription, OutputStream propertyOutputStream, boolean closeSteam) throws IOException
    {
        // create output Writer
        PrintWriter out = new PrintWriter(propertyOutputStream);
        // collect required imports
        ImportsList requiredImports = new ImportsList();
        appendRequiredImportsGlobal(requiredImports);
        List<ScalarTypeDescription> scalarTypes = apiDescription.getScalarTypes();
        Iterator<ScalarTypeDescription> itScalarTypes = scalarTypes.iterator();
        while(itScalarTypes.hasNext())
        {
            ScalarTypeDescription scalarTypeDescription = itScalarTypes.next();
            //if(!scalarTypeDescription.isInternal())
            //{
                appendRequiredImportsScalarAccess(requiredImports,scalarTypeDescription);
            //}
        }
        // write Header
        writePropertyFileHeader(apiDescription,requiredImports,out);
        // write Header of types section
        writeGenericSection(apiDescription,out);
        // iterate through all scalar types
        itScalarTypes = scalarTypes.iterator();
        while(itScalarTypes.hasNext())
        {
            ScalarTypeDescription scalarTypeDescription = itScalarTypes.next();
            //if(!scalarTypeDescription.isInternal())
            //{
                writeSingleValueScalarAccess(scalarTypeDescription,out);
            //}
        }
        itScalarTypes = scalarTypes.iterator();
        while(itScalarTypes.hasNext())
        {
            ScalarTypeDescription scalarTypeDescription = itScalarTypes.next();
            //if(!scalarTypeDescription.isInternal())
            //{
                writeMultiValueScalarAccess(scalarTypeDescription,out);
            //}
        }
        // write Footer
        writePropertyFileFooter(apiDescription,out);
        // flush writer and optionally close streams
        out.flush();
        if(closeSteam)
        {
            propertyOutputStream.close();
        }
    }

}