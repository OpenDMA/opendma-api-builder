package org.opendma.apibuilder.apiwriter;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.opendma.apibuilder.structure.ApiDescription;
import org.opendma.apibuilder.structure.ScalarTypeDescription;

public abstract class AbstractPropertyFileWriter
{
    
    protected abstract void writePropertyFileHeader(ApiDescription apiDescription, List requiredImports, PrintWriter out) throws IOException;
    
    protected abstract void writeGenericSection(ApiDescription apiDescription, PrintWriter out) throws IOException;
    
    protected abstract void writeSingleValueScalarAccess(ScalarTypeDescription scalarTypeDescription, PrintWriter out) throws IOException;
    
    protected abstract void writeMultiValueScalarAccess(ScalarTypeDescription scalarTypeDescription, PrintWriter out) throws IOException;
    
    protected abstract void writePropertyFileFooter(ApiDescription apiDescription, PrintWriter out) throws IOException;
    
    protected abstract void appendRequiredImportsGlobal(List requiredImports);
    
    protected abstract void appendRequiredImportsScalarAccess(List requiredImports, ScalarTypeDescription scalarTypeDescription);

    public void createPropertyFile(ApiDescription apiDescription, OutputStream propertyOutputStream) throws IOException
    {
        // create output Writer
        PrintWriter out = new PrintWriter(propertyOutputStream);
        // collect required imports
        ArrayList requiredImports = new ArrayList();
        appendRequiredImportsGlobal(requiredImports);
        List scalarTypes = apiDescription.getScalarTypes();
        Iterator itScalarTypes = scalarTypes.iterator();
        while(itScalarTypes.hasNext())
        {
            ScalarTypeDescription scalarTypeDescription = (ScalarTypeDescription)itScalarTypes.next();
            if(!scalarTypeDescription.isInternal())
            {
                appendRequiredImportsScalarAccess(requiredImports,scalarTypeDescription);
            }
        }
        // write Header
        writePropertyFileHeader(apiDescription,requiredImports,out);
        // write Header of types section
        writeGenericSection(apiDescription,out);
        // iterate through all scalar types
        itScalarTypes = scalarTypes.iterator();
        while(itScalarTypes.hasNext())
        {
            ScalarTypeDescription scalarTypeDescription = (ScalarTypeDescription)itScalarTypes.next();
            if(!scalarTypeDescription.isInternal())
            {
                writeSingleValueScalarAccess(scalarTypeDescription,out);
            }
        }
        itScalarTypes = scalarTypes.iterator();
        while(itScalarTypes.hasNext())
        {
            ScalarTypeDescription scalarTypeDescription = (ScalarTypeDescription)itScalarTypes.next();
            if(!scalarTypeDescription.isInternal())
            {
                writeMultiValueScalarAccess(scalarTypeDescription,out);
            }
        }
        // write Footer
        writePropertyFileFooter(apiDescription,out);
        // close writer and streams
        out.close();
        propertyOutputStream.close();
    }

}