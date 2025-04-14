package org.opendma.apibuilder.apiwriter;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.opendma.apibuilder.structure.ApiHelperDescription;
import org.opendma.apibuilder.structure.ClassDescription;
import org.opendma.apibuilder.structure.PropertyDescription;
import org.opendma.apibuilder.structure.ScalarTypeDescription;

public abstract class AbstractObjectsInterfaceFileWriter
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
        createClassFile(classDescription, classOutputStream, true);
    }

    public void createClassFile(ClassDescription classDescription, OutputStream classOutputStream, boolean closeStream) throws IOException
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
        writeGeneric(classDescription, out);
        writeSpecific(classDescription, out);
        // write Footer
        writeClassFileFooter(classDescription,out);
        // flush writer and optionally close stream
        out.flush();
        if(closeStream)
        {
            classOutputStream.close();
        }
    }
    
    public void writeGeneric(ClassDescription classDescription, PrintWriter out) throws IOException
    {
        // write section for generic property access (if required)
        if( (!classDescription.getAspect()) && (classDescription.getExtendsOdmaName() == null) )
        {
            writeClassGenericPropertyAccess(classDescription,out);
        }
    }
    
    public void writeSpecific(ClassDescription classDescription, PrintWriter out) throws IOException
    {
        // write Header of object specific property access section
        writeClassObjectSpecificPropertyAccessSectionHeader(classDescription,out);
        // write getter and setter for all properties
        Iterator<PropertyDescription> itPropertyDescriptions = classDescription.getPropertyDescriptions().iterator();
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
    }
    
    protected List<String> getPropertyDetails(PropertyDescription property)
    {
        LinkedList<String> result = new LinkedList<String>();
        ScalarTypeDescription scalarTypeDescription = property.getDataType();
        String dataTypeName = scalarTypeDescription.isInternal() ? scalarTypeDescription.getBaseScalar() : scalarTypeDescription.getName();
        if(property.getDataType().isReference())
        {
            dataTypeName = dataTypeName + " to " + property.getReferenceClassName().getName() + " ("+property.getReferenceClassName().getQualifier()+")";
        }
        result.add("<p>Property <b>"+property.getOdmaName().getName()+"</b> ("+property.getOdmaName().getQualifier()+"): <b>"+dataTypeName+"</b><br/>");
        result.add((property.getMultiValue()?"[MultiValue]":"[SingleValue]")+" "+(property.isReadOnly()?"[ReadOnly]":"[Writable]")+" "+(property.getRequired()?"[Required]":"[NotRequired]")+"<br/>");
        result.add(property.getDescription()+"</p>");
        return result;
    }

}
