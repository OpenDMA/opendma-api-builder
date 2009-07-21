package org.opendma.apibuilder.apiwriter;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;

import org.opendma.apibuilder.structure.ClassDescription;
import org.opendma.apibuilder.structure.PropertyDescription;

public abstract class AbstractClassFileWriter
{

    protected abstract void writeClassFileHeader(ClassDescription classDescription, PrintWriter out);

    protected abstract void writeClassFileFooter(ClassDescription classDescription, PrintWriter out);

    protected abstract void writeClassObjectSpecificPropertyAccessSectionHeader(ClassDescription classDescription, PrintWriter out);

    protected abstract void writeClassPropertyAccess(PropertyDescription property, PrintWriter out);

    public void createClassFile(ClassDescription classDescription, OutputStream classOutputStream) throws IOException
    {
        // create output Writer
        PrintWriter out = new PrintWriter(classOutputStream);
        // write Header
        writeClassFileHeader(classDescription,out);
        // write Header of object specific property access section
        writeClassObjectSpecificPropertyAccessSectionHeader(classDescription,out);
        // write getter and setter for all properties
        List propertyDescriptions = classDescription.getPropertyDescriptions();
        Iterator itPropertyDescriptions = propertyDescriptions.iterator();
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
