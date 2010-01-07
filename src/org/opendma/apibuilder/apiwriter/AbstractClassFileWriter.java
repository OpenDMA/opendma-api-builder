package org.opendma.apibuilder.apiwriter;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.opendma.apibuilder.structure.ClassDescription;
import org.opendma.apibuilder.structure.PropertyDescription;

public abstract class AbstractClassFileWriter
{

    protected abstract void writeClassFileHeader(ClassDescription classDescription, List requiredImports, PrintWriter out);

    protected abstract void writeClassFileFooter(ClassDescription classDescription, PrintWriter out);

    protected abstract void writeClassGenericPropertyAccess(ClassDescription classDescription, PrintWriter out) throws IOException;

    protected abstract void writeClassObjectSpecificPropertyAccessSectionHeader(ClassDescription classDescription, PrintWriter out);

    protected abstract void writeClassPropertyAccess(PropertyDescription property, PrintWriter out);
    
    protected abstract void appendRequiredImportsGlobal(ClassDescription classDescription, List requiredImports);
    
    protected abstract void appendRequiredImportsGenericPropertyAccess(List requiredImports);
    
    protected abstract void appendRequiredImportsClassPropertyAccess(List requiredImports, PropertyDescription property);

    public void createClassFile(ClassDescription classDescription, OutputStream classOutputStream) throws IOException
    {
        // create output Writer
        PrintWriter out = new PrintWriter(classOutputStream);
        // collect required imports
        ArrayList requiredImports = new ArrayList();
        appendRequiredImportsGlobal(classDescription,requiredImports);
        if( (!classDescription.getAspect()) && (classDescription.getExtendsOdmaName() == null) )
        {
            appendRequiredImportsGenericPropertyAccess(requiredImports);
        }
        List propertyDescriptions = classDescription.getPropertyDescriptions();
        Iterator itPropertyDescriptions = propertyDescriptions.iterator();
        while(itPropertyDescriptions.hasNext())
        {
            PropertyDescription property = (PropertyDescription)itPropertyDescriptions.next();
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
        // write Footer
        writeClassFileFooter(classDescription,out);
        // close writer and streams
        out.close();
        classOutputStream.close();
    }

}
