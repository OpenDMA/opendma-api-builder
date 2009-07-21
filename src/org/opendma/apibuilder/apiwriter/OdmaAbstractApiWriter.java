package org.opendma.apibuilder.apiwriter;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.opendma.apibuilder.OdmaApiBuilderQName;
import org.opendma.apibuilder.OdmaApiWriter;
import org.opendma.apibuilder.structure.ApiDescription;
import org.opendma.apibuilder.structure.ClassDescription;
import org.opendma.apibuilder.structure.PropertyDescription;
import org.opendma.apibuilder.structure.ScalarTypeDescription;

public abstract class OdmaAbstractApiWriter implements OdmaApiWriter
{
    
    protected abstract String getProgrammingLanguageSpecificFolderName();

    public void writeOdmaApi(ApiDescription apiDescription, String outputFolderRoot) throws IOException, ApiWriterException
    {
        // sanity checks
        if(apiDescription == null)
        {
            throw new NullPointerException("OdmaAbstractApiWriter.writeOdmaApi: apiDescription must not be null.");
        }
        if(outputFolderRoot == null)
        {
            throw new NullPointerException("OdmaAbstractApiWriter.writeOdmaApi: outputFolderRoot must not be null.");
        }
        // check root folder
        File outputFolderRootFile = new File(outputFolderRoot);
        if(!outputFolderRootFile.isDirectory())
        {
            throw new ApiWriterException("The output folder '"+outputFolderRoot+"' does not exist or is not a directory.");
        }
        // create base folder
        String baseFolder = outputFolderRoot;
        if(!baseFolder.endsWith(File.separator))
        {
            baseFolder = baseFolder + File.separator;
        }
        baseFolder = baseFolder + getProgrammingLanguageSpecificFolderName();
        File baseFolderFile = new File(baseFolder);
        if(!baseFolderFile.exists())
        {
            if(!baseFolderFile.mkdir())
            {
                throw new ApiWriterException("Can not create API specific folder '"+baseFolder+".");
            }
        }
        baseFolder = baseFolder + File.separator;
        // create the constants file
        createConstantsFile(apiDescription,baseFolder);
        // create class files
        List classes = apiDescription.getDescribedClasses();
        Iterator itClasses = classes.iterator();
        while(itClasses.hasNext())
        {
            ClassDescription classDescription = (ClassDescription)itClasses.next();
            createClassFile(classDescription,baseFolder);
        }
    }

    //-------------------------------------------------------------------------
    // C O N S T A N T S   F I L E
    //-------------------------------------------------------------------------
    
    protected abstract OutputStream getConstantsFileStream(String outputFolder) throws IOException;
    
    protected abstract void writeConstantsFileHeader(ApiDescription apiDescription, PrintWriter out);
    
    protected abstract void writeConstantsTypesSectionHeader(ApiDescription apiDescription, PrintWriter out);

    protected abstract void writeConstantsScalarTypeConstant(ScalarTypeDescription scalarTypeDescription, String constantName, PrintWriter out);
    
    protected abstract void writeConstantsClassesSectionHeader(ApiDescription apiDescription, PrintWriter out);

    protected abstract void writeConstantsClassSeperator(ClassDescription classDescription, PrintWriter out);

    protected abstract void writeConstantsClassnameConstant(ClassDescription classDescription, String constantName, PrintWriter out);

    protected abstract void writeConstantsPropertynameConstant(PropertyDescription propertyDescription, String constantName, PrintWriter out);

    protected abstract void writeConstantsBackrefPropertynameHint(PropertyDescription propertyDescription, String constantName, PrintWriter out);
    
    protected abstract void writeConstantsFileFooter(ApiDescription apiDescription, PrintWriter out);
    
    /** list of all unique class names across the entire hierarchy */
    protected Map uniqueClassNames = new LinkedHashMap();
    
    /** list of all unique property names across the entire hierarchy */
    protected Map uniquePropertyNames = new LinkedHashMap();
    
    protected void createConstantsFile(ApiDescription apiDescription, String outputFolder) throws IOException
    {
        // create output Writer
        OutputStream constantsOutputStream = getConstantsFileStream(outputFolder);
        PrintWriter out = new PrintWriter(constantsOutputStream);
        // write Header
        writeConstantsFileHeader(apiDescription,out);
        // write Header of types section
        writeConstantsTypesSectionHeader(apiDescription,out);
        // iterate through all scalar types
        List scalarTypes = apiDescription.getScalarTypes();
        Iterator itScalarTypes = scalarTypes.iterator();
        while(itScalarTypes.hasNext())
        {
            ScalarTypeDescription scalarTypeDescription = (ScalarTypeDescription)itScalarTypes.next();
            String constantScalarTypeName = "TYPE_" + scalarTypeDescription.getName().toUpperCase();
            if(!scalarTypeDescription.isInternal())
            {
                writeConstantsScalarTypeConstant(scalarTypeDescription,constantScalarTypeName,out);
            }
        }
        // write Header of class section
        writeConstantsClassesSectionHeader(apiDescription,out);
        // iterate through all classes and all properties
        uniqueClassNames.clear();
        uniquePropertyNames.clear();
        List describedClasses = apiDescription.getDescribedClasses();
        Iterator itDescribedClasses = describedClasses.iterator();
        while(itDescribedClasses.hasNext())
        {
            ClassDescription classDescription = (ClassDescription)itDescribedClasses.next();
            writeConstantsClassSeperator(classDescription,out);
            OdmaApiBuilderQName classOdmaName = classDescription.getOdmaName();
            String constantClassName = "CLASS_" + classOdmaName.getName().toUpperCase();
            if(!uniqueClassNames.containsKey(constantClassName))
            {
                writeConstantsClassnameConstant(classDescription,constantClassName,out);
                uniqueClassNames.put(constantClassName, classDescription);
            }
            List declaredProperties = classDescription.getPropertyDescriptions();
            Iterator itDeclaredProperties = declaredProperties.iterator();
            while(itDeclaredProperties.hasNext())
            {
                PropertyDescription propertyDescription = (PropertyDescription)itDeclaredProperties.next();
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
        // close writer and streams
        out.close();
        constantsOutputStream.close();
    }
    
    //-------------------------------------------------------------------------
    // C L A S S   F I L E
    //-------------------------------------------------------------------------
    
    protected void createClassFile(ClassDescription classDescription, String outputFolder) throws IOException
    {
        // create output Writer
        OutputStream classOutputStream = getClassFileStream(outputFolder,classDescription);
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

    protected abstract OutputStream getClassFileStream(String outputFolder, ClassDescription classDescription) throws IOException;

    protected abstract void writeClassFileHeader(ClassDescription classDescription, PrintWriter out);

    protected abstract void writeClassFileFooter(ClassDescription classDescription, PrintWriter out);

    protected abstract void writeClassObjectSpecificPropertyAccessSectionHeader(ClassDescription classDescription, PrintWriter out);

    protected abstract void writeClassPropertyAccess(PropertyDescription property, PrintWriter out);

}