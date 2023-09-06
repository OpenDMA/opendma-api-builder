package org.opendma.apibuilder.apiwriter.go;

import java.io.File;
import java.io.IOException;

import org.opendma.apibuilder.apiwriter.AbstractApiWriter;
import org.opendma.apibuilder.apiwriter.ApiWriterException;
import org.opendma.apibuilder.structure.ApiDescription;
import org.opendma.apibuilder.structure.ClassDescription;
import org.opendma.apibuilder.structure.ScalarTypeDescription;

public class GoApiWriter extends AbstractApiWriter
{

    public GoApiWriter(File outputFolderRoot) throws ApiWriterException
    {
        super(outputFolderRoot);
    }

    public String getName()
    {
        return "Go";
    }
    
    protected String getTargetFolderName()
    {
        return "go";
    }

    //-------------------------------------------------------------------------
    // S O U R C E   F I L E   H E L P E R
    //-------------------------------------------------------------------------
    
    // basic tools to create files for target programming language

    //-------------------------------------------------------------------------
    // C O N S T A N T S   F I L E
    //-------------------------------------------------------------------------

    protected void createDataTypesFile(ApiDescription apiDescription) throws IOException
    {
    }

    protected void createConstantsFile(ApiDescription apiDescription) throws IOException
    {
    }

    //-------------------------------------------------------------------------
    // B A S I C   F I L E S
    //-------------------------------------------------------------------------

    protected void createQNameFile(ApiDescription apiDescription) throws IOException
    {
    }

    protected void createIdFile(ApiDescription apiDescription) throws IOException
    {
    }

    protected void createGuidFile(ApiDescription apiDescription) throws IOException
    {
    }

    protected void createContentFile(ApiDescription apiDescription) throws IOException
    {
    }

    protected void createSearchResultFile(ApiDescription apiDescription) throws IOException
    {
    }

    protected void createExceptionFiles(ApiDescription apiDescription) throws IOException
    {
    }

    protected void createSessionManagementFiles(ApiDescription apiDescription) throws IOException
    {
    }

    //-------------------------------------------------------------------------
    // P R O P E R T Y   F I L E
    //-------------------------------------------------------------------------

    protected void createPropertyFile(ApiDescription apiDescription) throws IOException
    {
    }

    //-------------------------------------------------------------------------
    // C L A S S   F I L E
    //-------------------------------------------------------------------------

    protected void createClassFile(ClassDescription classDescription) throws IOException
    {
    }
    
    //-------------------------------------------------------------------------
    // C O L L E C T I O N   F I L E S
    //-------------------------------------------------------------------------
    
    protected void createEnumerationFile(ClassDescription classDescription) throws IOException
    {
        // We are using generics in the form of Iterable<OdmaObject>. There is no need for enumeration files
    }

    protected void createListFile(ScalarTypeDescription scalarTypeDescription) throws IOException
    {
        // We are using generics in the form of List<Object>. There is no need for list files
    }
    
    //-------------------------------------------------------------------------
    // I M P L E M E N T A T I O N   F I L E S
    //-------------------------------------------------------------------------

    protected void createPropertyImplementationFile(ApiDescription apiDescription) throws IOException
    {
    }

    protected void createListImplementationFile(ScalarTypeDescription scalarTypeDescription) throws IOException
    {
        // We are using generics in the form of List<Object>. There is no need for list files
    }

    //-------------------------------------------------------------------------
    // C L A S S   T E M P L A T E S
    //-------------------------------------------------------------------------

    protected void createClassTemplateFile(ClassDescription classDescription) throws IOException
    {
    }
   
    //-------------------------------------------------------------------------
    // B U I L D   F I L E
    //-------------------------------------------------------------------------
    
    protected void prepareProjectStructureAndBuildFiles(ApiDescription apiDescription) throws IOException
    {
    }
    
    protected void finaliseProjectStructureAndBuildFiles(ApiDescription apiDescription) throws IOException
    {
    }
    
    //-------------------------------------------------------------------------
    // S T A T I C   H E L P E R
    //-------------------------------------------------------------------------
    
    // helper tools like needToImportPackage(String importDeclaration, String intoPackage)

    //-------------------------------------------------------------------------
    // E X T R A S
    //-------------------------------------------------------------------------
    
    // protected void createExtras(ApiDescription apiDescription) throws IOException, ApiWriterException {}

}