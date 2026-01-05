package org.opendma.apibuilder.apiwriter.php;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;

import org.opendma.apibuilder.apiwriter.AbstractApiWriter;
import org.opendma.apibuilder.apiwriter.ApiWriterException;
import org.opendma.apibuilder.structure.ApiDescription;
import org.opendma.apibuilder.structure.ClassDescription;
import org.opendma.apibuilder.structure.ScalarTypeDescription;

public class PhpApiWriter extends AbstractApiWriter
{

    public PhpApiWriter(File outputFolderRoot) throws ApiWriterException
    {
        super(outputFolderRoot);
    }

    public String getName()
    {
        return "PHP";
    }
    
    protected String getTargetFolderName()
    {
        return "php";
    }

    //-------------------------------------------------------------------------
    // S O U R C E   F I L E   H E L P E R
    //-------------------------------------------------------------------------
    
    private OutputStream createPhpFile(File targetFolder, String className) throws IOException
    {
        return new FileOutputStream(new File(targetFolder, className+".php"));
    }
    
    private void createClassFromTemplate(File targetFolder, String className) throws IOException
    {
        copyTemplateToStream(className,createPhpFile(targetFolder,className));
    }

    //-------------------------------------------------------------------------
    // C O N S T A N T S   F I L E
    //-------------------------------------------------------------------------

    protected void createDataTypesFile(ApiDescription apiDescription) throws IOException
    {
        // create type enumeration
        PrintWriter out = new PrintWriter(createPhpFile(opendmaApiFolder, "OdmaType"));
        out.println("<?php");
        out.println("");
        out.println("namespace OpenDMA\\Api;");
        out.println("");
        out.println("enum OdmaType");
        out.println("{");
        List<ScalarTypeDescription> scalarTypes = apiDescription.getScalarTypes();
        Iterator<ScalarTypeDescription> itScalarTypes = scalarTypes.iterator();
        while(itScalarTypes.hasNext())
        {
            ScalarTypeDescription scalarTypeDescription = itScalarTypes.next();
            out.println("    case "+scalarTypeDescription.getName().toUpperCase()+";");
        }
        out.println("}");
        out.close();
    }

    protected void createConstantsFile(ApiDescription apiDescription) throws IOException
    {
        // create common names file
        PhpCommonNamesFileWriter constantsFileWriter = new PhpCommonNamesFileWriter();
        constantsFileWriter.createConstantsFile(apiDescription, createPhpFile(opendmaApiFolder, "OdmaCommonNames"));
    }

    //-------------------------------------------------------------------------
    // B A S I C   F I L E S
    //-------------------------------------------------------------------------

    protected void createQNameFile(ApiDescription apiDescription) throws IOException
    {
        createClassFromTemplate(opendmaApiFolder, "OdmaQName");
    }

    protected void createIdFile(ApiDescription apiDescription) throws IOException
    {
        createClassFromTemplate(opendmaApiFolder, "OdmaId");
    }

    protected void createGuidFile(ApiDescription apiDescription) throws IOException
    {
        createClassFromTemplate(opendmaApiFolder, "OdmaGuid");
    }

    protected void createContentFile(ApiDescription apiDescription) throws IOException
    {
        createClassFromTemplate(opendmaApiFolder, "OdmaContent");
    }

    protected void createSearchResultFile(ApiDescription apiDescription) throws IOException
    {
        createClassFromTemplate(opendmaApiFolder, "OdmaSearchResult");
    }

    protected void createExceptionFiles(ApiDescription apiDescription) throws IOException
    {
        createClassFromTemplate(opendmaApiFolder,"OdmaException");
        // OdmaRuntimeException: Php does not distinguish between checked and unchecked Exceptions
        createClassFromTemplate(opendmaApiFolder,"OdmaObjectNotFoundException");
        createClassFromTemplate(opendmaApiFolder,"OdmaPropertyNotFoundException");
        createClassFromTemplate(opendmaApiFolder,"OdmaInvalidDataTypeException");
        createClassFromTemplate(opendmaApiFolder,"OdmaAccessDeniedException");
        createClassFromTemplate(opendmaApiFolder,"OdmaQuerySyntaxException");
        createClassFromTemplate(opendmaApiFolder,"OdmaServiceException");
        createClassFromTemplate(opendmaApiFolder,"OdmaAuthenticationException");
    }

    protected void createSessionManagementFiles(ApiDescription apiDescription) throws IOException
    {
//        createClassFromTemplate(outputFolder,"OpenDMA/Api","OdmaDataSource");
        createClassFromTemplate(opendmaApiFolder,"OdmaSession");
    }

    //-------------------------------------------------------------------------
    // P R O P E R T Y   F I L E
    //-------------------------------------------------------------------------

    protected void createPropertyFile(ApiDescription apiDescription) throws IOException
    {
        PhpPropertyFileWriter phpPropertyFileWriter = new PhpPropertyFileWriter(this);
        phpPropertyFileWriter.createPropertyFile(apiDescription, createPhpFile(opendmaApiFolder, "OdmaProperty"));
    }

    //-------------------------------------------------------------------------
    // C L A S S   F I L E
    //-------------------------------------------------------------------------

    protected void createCoreObjectFile(ApiDescription apiDescription) throws IOException
    {
        createClassFromTemplate(opendmaApiFolder,"OdmaCoreObject");
    }

    protected void createClassFile(ClassDescription classDescription) throws IOException
    {
        PhpObjectsInterfaceFileWriter classFileWriter = new PhpObjectsInterfaceFileWriter(this);
        classFileWriter.createClassFile(classDescription, createPhpFile(opendmaApiFolder,classDescription.getApiName()));
    }
    
    //-------------------------------------------------------------------------
    // I M P L E M E N T A T I O N   F I L E S
    //-------------------------------------------------------------------------

    protected void createPropertyImplementationFile(ApiDescription apiDescription) throws IOException
    {
        PhpPropertyImplementationFileWriter phpPropertyImplementationFileWriter = new PhpPropertyImplementationFileWriter(this);
        phpPropertyImplementationFileWriter.createPropertyFile(apiDescription, createPhpFile(opendmaApiFolder,"OdmaPropertyImpl"));
    }

    //-------------------------------------------------------------------------
    // C L A S S   T E M P L A T E S
    //-------------------------------------------------------------------------

    protected void createClassTemplateFile(ClassDescription classDescription) throws IOException
    {
//        PhpClassTemplateFileWriter classTemplateFileWriter = new PhpClassTemplateFileWriter(this);
//        classTemplateFileWriter.createClassFile(classDescription, createPhpFile(outputFolder,"OpenDMA/Templates",classDescription.getApiName()));
    }
   
    //-------------------------------------------------------------------------
    // P R O J E C T   S T R U C T U R E   A N  D   B U I L D   F I L E
    //-------------------------------------------------------------------------
    
    private File opendmaApiFolder;
    
    protected void prepareProjectStructureAndBuildFiles(ApiDescription apiDescription) throws IOException
    {
        // opendma-api folder structure
        opendmaApiFolder = new File(baseFolder, "opendma-api");
        opendmaApiFolder.mkdirs();
        // opendma-api composer file - see https://getcomposer.org/doc/
        copyTemplateToStream("opendma-api-composer", new FileOutputStream(new File(opendmaApiFolder, "composer.json")));
        // LICENSE
        copyGlobalTemplateToStream("apache-license", new FileOutputStream(new File(opendmaApiFolder, "LICENSE")), null,  true);
    }
    
    protected void finaliseProjectStructureAndBuildFiles(ApiDescription apiDescription) throws IOException
    {
    }

}