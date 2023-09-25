package org.opendma.apibuilder.apiwriter;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.opendma.apibuilder.OdmaApiBuilderQName;
import org.opendma.apibuilder.structure.ApiDescription;
import org.opendma.apibuilder.structure.ClassDescription;
import org.opendma.apibuilder.structure.PropertyDescription;
import org.opendma.apibuilder.structure.ScalarTypeDescription;

public abstract class AbstractConstantsFileWriter
{
    
    /** list of all unique class names across the entire hierarchy */
    protected Map<String, ClassDescription> uniqueClassNames = new LinkedHashMap<String, ClassDescription>();
    
    /** list of all unique property names across the entire hierarchy */
    protected Map<String, PropertyDescription> uniquePropertyNames = new LinkedHashMap<String, PropertyDescription>();
    
    protected abstract void writeConstantsFileHeader(ApiDescription apiDescription, PrintWriter out);
    
    protected abstract void writeConstantsTypesSectionHeader(ApiDescription apiDescription, PrintWriter out);

    protected abstract void writeConstantsScalarTypeConstant(ScalarTypeDescription scalarTypeDescription, String constantName, PrintWriter out);
    
    protected abstract void writeConstantsClassesSectionHeader(ApiDescription apiDescription, PrintWriter out);

    protected abstract void writeConstantsClassSeperator(ClassDescription classDescription, PrintWriter out);

    protected abstract void writeConstantsClassnameConstant(ClassDescription classDescription, String constantName, PrintWriter out);

    protected abstract void writeConstantsPropertynameConstant(PropertyDescription propertyDescription, String constantName, PrintWriter out);

    protected abstract void writeConstantsBackrefPropertynameHint(PropertyDescription propertyDescription, String constantName, PrintWriter out);
    
    protected abstract void writeConstantsFileFooter(ApiDescription apiDescription, PrintWriter out);
    
    public void createConstantsFile(ApiDescription apiDescription, OutputStream constantsOutputStream) throws IOException
    {
        createConstantsFile(apiDescription, constantsOutputStream, true);
    }

    public void createConstantsFile(ApiDescription apiDescription, OutputStream constantsOutputStream, boolean closeStream) throws IOException
    {
        // create output Writer
        PrintWriter out = new PrintWriter(constantsOutputStream);
        // write Header
        writeConstantsFileHeader(apiDescription,out);
        // write Header of types section
        writeConstantsTypesSectionHeader(apiDescription,out);
        // iterate through all scalar types
        List<ScalarTypeDescription> scalarTypes = apiDescription.getScalarTypes();
        Iterator<ScalarTypeDescription> itScalarTypes = scalarTypes.iterator();
        while(itScalarTypes.hasNext())
        {
            ScalarTypeDescription scalarTypeDescription = itScalarTypes.next();
            String constantScalarTypeName = "TYPE_" + scalarTypeDescription.getName().toUpperCase();
            //if(!scalarTypeDescription.isInternal())
            //{
                writeConstantsScalarTypeConstant(scalarTypeDescription,constantScalarTypeName,out);
            //}
        }
        // write Header of class section
        writeConstantsClassesSectionHeader(apiDescription,out);
        // iterate through all classes and all properties
        uniqueClassNames.clear();
        uniquePropertyNames.clear();
        List<ClassDescription> describedClasses = apiDescription.getDescribedClasses();
        Iterator<ClassDescription> itDescribedClasses = describedClasses.iterator();
        while(itDescribedClasses.hasNext())
        {
            ClassDescription classDescription = itDescribedClasses.next();
            writeConstantsClassSeperator(classDescription,out);
            OdmaApiBuilderQName classOdmaName = classDescription.getOdmaName();
            String constantClassName = "CLASS_" + classOdmaName.getName().toUpperCase();
            if(!uniqueClassNames.containsKey(constantClassName))
            {
                writeConstantsClassnameConstant(classDescription,constantClassName,out);
                uniqueClassNames.put(constantClassName, classDescription);
            }
            List<PropertyDescription> declaredProperties = classDescription.getPropertyDescriptions();
            Iterator<PropertyDescription> itDeclaredProperties = declaredProperties.iterator();
            while(itDeclaredProperties.hasNext())
            {
                PropertyDescription propertyDescription = itDeclaredProperties.next();
                OdmaApiBuilderQName propertyOdmaName = propertyDescription.getOdmaName();
                String constantPropertyName = "PROPERTY_" + propertyOdmaName.getName().toUpperCase();
                if(!uniquePropertyNames.containsKey(constantPropertyName))
                {
                    writeConstantsPropertynameConstant(propertyDescription,constantPropertyName,out);
                    uniquePropertyNames.put(constantPropertyName, propertyDescription);
                }
                else
                {
                    writeConstantsBackrefPropertynameHint(propertyDescription,constantPropertyName,out);
                }
            }
        }
        // write Footer
        writeConstantsFileFooter(apiDescription,out);
        // flush writer and optionally close streams
        out.flush();
        if(closeStream)
        {
            constantsOutputStream.close();
        }
    }

}