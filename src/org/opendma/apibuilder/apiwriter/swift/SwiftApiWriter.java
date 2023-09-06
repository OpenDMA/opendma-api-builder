package org.opendma.apibuilder.apiwriter.swift;

import java.io.IOException;

import org.opendma.apibuilder.apiwriter.AbstractApiWriter;
import org.opendma.apibuilder.structure.ApiDescription;
import org.opendma.apibuilder.structure.ClassDescription;
import org.opendma.apibuilder.structure.ScalarTypeDescription;

public class SwiftApiWriter extends AbstractApiWriter
{

    public String getName()
    {
        return "Swift";
    }

    protected String getTargetFolderName()
    {
        return "swift";
    }

    //-------------------------------------------------------------------------
    // S O U R C E   F I L E   H E L P E R
    //-------------------------------------------------------------------------
    
    // basic tools to create files for target programming language

    //-------------------------------------------------------------------------
    // C O N S T A N T S   F I L E
    //-------------------------------------------------------------------------

    protected void createDataTypesFile(ApiDescription apiDescription, String outputFolder) throws IOException
    {
    }

    protected void createConstantsFile(ApiDescription apiDescription, String outputFolder) throws IOException
    {
    }

    //-------------------------------------------------------------------------
    // B A S I C   F I L E S
    //-------------------------------------------------------------------------

    protected void createQNameFile(ApiDescription apiDescription, String outputFolder) throws IOException
    {
    }

    protected void createIdFile(ApiDescription apiDescription, String outputFolder) throws IOException
    {
    }

    protected void createGuidFile(ApiDescription apiDescription, String outputFolder) throws IOException
    {
    }

    protected void createContentFile(ApiDescription apiDescription, String outputFolder) throws IOException
    {
    }

    protected void createSearchResultFile(ApiDescription apiDescription, String outputFolder) throws IOException
    {
    }

    protected void createExceptionFiles(ApiDescription apiDescription, String outputFolder) throws IOException
    {
    }

    protected void createSessionManagementFiles(ApiDescription apiDescription, String outputFolder) throws IOException
    {
    }

    //-------------------------------------------------------------------------
    // P R O P E R T Y   F I L E
    //-------------------------------------------------------------------------

    protected void createPropertyFile(ApiDescription apiDescription, String outputFolder) throws IOException
    {
    }

    //-------------------------------------------------------------------------
    // C L A S S   F I L E
    //-------------------------------------------------------------------------

    protected void createClassFile(ClassDescription classDescription, String outputFolder) throws IOException
    {
    }
    
    //-------------------------------------------------------------------------
    // C O L L E C T I O N   F I L E S
    //-------------------------------------------------------------------------
    
    protected void createEnumerationFile(ClassDescription classDescription, String baseFolder) throws IOException
    {
        // We are using generics in the form of Iterable<OdmaObject>. There is no need for enumeration files
    }

    protected void createListFile(ScalarTypeDescription scalarTypeDescription, String baseFolder) throws IOException
    {
        // We are using generics in the form of List<Object>. There is no need for list files
    }
    
    //-------------------------------------------------------------------------
    // I M P L E M E N T A T I O N   F I L E S
    //-------------------------------------------------------------------------

    protected void createPropertyImplementationFile(ApiDescription apiDescription, String outputFolder) throws IOException
    {
    }

    protected void createListImplementationFile(ScalarTypeDescription scalarTypeDescription, String baseFolder) throws IOException
    {
        // We are using generics in the form of List<Object>. There is no need for list files
    }

    //-------------------------------------------------------------------------
    // C L A S S   T E M P L A T E S
    //-------------------------------------------------------------------------

    protected void createClassTemplateFile(ClassDescription classDescription, String outputFolder) throws IOException
    {
    }
   
    //-------------------------------------------------------------------------
    // B U I L D   F I L E
    //-------------------------------------------------------------------------
    
    protected void prepareProjectStructureAndBuildFiles(ApiDescription apiDescription, String baseFolder) throws IOException
    {
    }
    
    protected void finaliseProjectStructureAndBuildFiles(ApiDescription apiDescription, String baseFolder) throws IOException
    {
    }
    
    //-------------------------------------------------------------------------
    // S T A T I C   H E L P E R
    //-------------------------------------------------------------------------
    
    // helper tools like needToImportPackage(String importDeclaration, String intoPackage)

    //-------------------------------------------------------------------------
    // E X T R A S
    //-------------------------------------------------------------------------
    
    // protected void createExtras(ApiDescription apiDescription, String baseFolder) throws IOException, ApiWriterException {}

}