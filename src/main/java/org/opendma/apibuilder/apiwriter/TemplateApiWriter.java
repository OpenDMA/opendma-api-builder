package org.opendma.apibuilder.apiwriter;

import java.io.File;
import java.io.IOException;

import org.opendma.apibuilder.structure.ApiDescription;
import org.opendma.apibuilder.structure.ClassDescription;

public class TemplateApiWriter extends AbstractApiWriter
{

    public TemplateApiWriter(File outputFolderRoot) throws ApiWriterException
    {
        super(outputFolderRoot);
    }

    public String getName()
    {
        return "-----";
    }
    
    protected String getTargetFolderName()
    {
        return "-----";
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

    protected void createCoreObjectFile(ApiDescription apiDescription) throws IOException
    {
        // TODO Auto-generated method stub
        
    }

    protected void createClassFile(ClassDescription classDescription) throws IOException
    {
    }
    
    //-------------------------------------------------------------------------
    // I M P L E M E N T A T I O N   F I L E S
    //-------------------------------------------------------------------------

    protected void createPropertyImplementationFile(ApiDescription apiDescription) throws IOException
    {
    }

    //-------------------------------------------------------------------------
    // C L A S S   T E M P L A T E S
    //-------------------------------------------------------------------------

    protected void createClassTemplateFile(ClassDescription classDescription) throws IOException
    {
    }
   
    //-------------------------------------------------------------------------
    // P R O J E C T   S T R U C T U R E   A N  D   B U I L D   F I L E
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