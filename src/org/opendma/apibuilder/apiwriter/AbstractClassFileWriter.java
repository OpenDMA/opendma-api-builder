package org.opendma.apibuilder.apiwriter;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.opendma.apibuilder.structure.ApiHelperDescription;
import org.opendma.apibuilder.structure.ClassDescription;
import org.opendma.apibuilder.structure.PropertyDescription;

public abstract class AbstractClassFileWriter
{
    
    protected Map<String,ApiHelperWriter> apiHelperWriters = new HashMap<String,ApiHelperWriter>();

    protected abstract void writeClassFileHeader(ClassDescription classDescription, List<String> requiredImports, PrintWriter out);

    protected abstract void writeClassFileFooter(ClassDescription classDescription, PrintWriter out);

    protected abstract void writeClassGenericPropertyAccess(ClassDescription classDescription, PrintWriter out) throws IOException;

    protected abstract void writeClassObjectSpecificPropertyAccessSectionHeader(ClassDescription classDescription, PrintWriter out);

    protected abstract void writeClassPropertyAccess(PropertyDescription property, PrintWriter out);

    protected void writeClassApiHelper(ClassDescription classDescription, ApiHelperDescription apiHelper, PrintWriter out)
    {
        ApiHelperWriter helperWriter = (ApiHelperWriter)apiHelperWriters.get(apiHelper.getApiName());
        if(helperWriter == null)
        {
            throw new RuntimeException("No ApiHelperWriter registered for ApiHelper "+apiHelper.getApiName());
        }
        helperWriter.writeApiHelper(classDescription, apiHelper, out);
    }
    
    protected abstract void appendRequiredImportsGlobal(ClassDescription classDescription, ImportsList requiredImports);
    
    protected void appendRequiredImportsApiHelper(ClassDescription classDescription, ImportsList requiredImports)
    {
        Iterator<ApiHelperDescription> itApiHelperDescriptions = classDescription.getApiHelpers().iterator();
        while(itApiHelperDescriptions.hasNext())
        {
            ApiHelperDescription apiHelper = itApiHelperDescriptions.next();
            ApiHelperWriter helperWriter = apiHelperWriters.get(apiHelper.getApiName());
            if(helperWriter == null)
            {
                throw new RuntimeException("No ApiHelperWriter registered for ApiHelper "+apiHelper.getApiName());
            }
            helperWriter.appendRequiredImportsGlobal(classDescription, apiHelper, requiredImports);
        }

    }
    
    protected abstract void appendRequiredImportsGenericPropertyAccess(ImportsList requiredImports);
    
    protected abstract void appendRequiredImportsClassPropertyAccess(ImportsList requiredImports, PropertyDescription property);

    public void createClassFile(ClassDescription classDescription, OutputStream classOutputStream) throws IOException
    {
        // create output Writer
        PrintWriter out = new PrintWriter(classOutputStream);
        // collect required imports
        ImportsList requiredImports = new ImportsList();
        appendRequiredImportsGlobal(classDescription,requiredImports);
        appendRequiredImportsApiHelper(classDescription,requiredImports);
        if( (!classDescription.getAspect()) && (classDescription.getExtendsOdmaName() == null) )
        {
            appendRequiredImportsGenericPropertyAccess(requiredImports);
        }
        List<PropertyDescription> propertyDescriptions = classDescription.getPropertyDescriptions();
        Iterator<PropertyDescription> itPropertyDescriptions = propertyDescriptions.iterator();
        while(itPropertyDescriptions.hasNext())
        {
            PropertyDescription property = itPropertyDescriptions.next();
            appendRequiredImportsClassPropertyAccess(requiredImports,property);
        }
        // write Header
        writeClassFileHeader(classDescription,requiredImports,out);
        // write section for generic property access (if required)
        if( (!classDescription.getAspect()) && (classDescription.getExtendsOdmaName() == null) )
        {
            writeClassGenericPropertyAccess(classDescription,out);
        }
        // write Header of object specific property access section
        writeClassObjectSpecificPropertyAccessSectionHeader(classDescription,out);
        // write getter and setter for all properties
        itPropertyDescriptions = propertyDescriptions.iterator();
        while(itPropertyDescriptions.hasNext())
        {
            PropertyDescription property = (PropertyDescription)itPropertyDescriptions.next();
            writeClassPropertyAccess(property,out);
        }
        // write all ApiHelper for this class
        Iterator<ApiHelperDescription> itApiHelperDescriptions = classDescription.getApiHelpers().iterator();
        while(itApiHelperDescriptions.hasNext())
        {
            ApiHelperDescription apiHelper = itApiHelperDescriptions.next();
            writeClassApiHelper(classDescription,apiHelper,out);
        }
        // write Footer
        writeClassFileFooter(classDescription,out);
        // close writer and streams
        out.close();
        classOutputStream.close();
    }

}
