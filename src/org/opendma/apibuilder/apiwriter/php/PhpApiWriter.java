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
//        // create common names file
//        PhpConstantsFileWriter constantsFileWriter = new PhpConstantsFileWriter();
//        constantsFileWriter.createConstantsFile(apiDescription, createPhpFile(outputFolder,"OpenDMA/Api","OdmaCommonNames"));
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
//        createClassFromTemplate(outputFolder,"OpenDMA/Api","OdmaSearchResult");
    }

    protected void createExceptionFiles(ApiDescription apiDescription) throws IOException
    {
//        createClassFromTemplate(outputFolder,"OpenDMA/Exceptions","OdmaException");
//        createClassFromTemplate(outputFolder,"OpenDMA/Exceptions","OdmaObjectNotFoundException");
//        createClassFromTemplate(outputFolder,"OpenDMA/Exceptions","OdmaInvalidDataTypeException");
//        createClassFromTemplate(outputFolder,"OpenDMA/Exceptions","OdmaAccessDeniedException");
//        createClassFromTemplate(outputFolder,"OpenDMA/Exceptions","OdmaQuerySyntaxException");
//        createClassFromTemplate(outputFolder,"OpenDMA/Exceptions","OdmaSearchException");
    }

    protected void createSessionManagementFiles(ApiDescription apiDescription) throws IOException
    {
//        createClassFromTemplate(outputFolder,"OpenDMA/Api","OdmaDataSource");
//        createClassFromTemplate(outputFolder,"OpenDMA/Api","OdmaSession");
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
//        PhpClassFileWriter classFileWriter = new PhpClassFileWriter(this);
//        classFileWriter.createClassFile(classDescription, createPhpFile(outputFolder,"OpenDMA/Api",classDescription.getApiName()));
    }
    
    //-------------------------------------------------------------------------
    // C O L L E C T I O N   F I L E S
    //-------------------------------------------------------------------------
    
    protected void createEnumerationFile(ClassDescription classDescription) throws IOException
    {
    }

    protected void createListFile(ScalarTypeDescription scalarTypeDescription) throws IOException
    {
    }
    
    //-------------------------------------------------------------------------
    // I M P L E M E N T A T I O N   F I L E S
    //-------------------------------------------------------------------------

    protected void createPropertyImplementationFile(ApiDescription apiDescription) throws IOException
    {
    }

    protected void createListImplementationFile(ScalarTypeDescription scalarTypeDescription) throws IOException
    {
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
    
    private File opendmaTemplatesFolder;
    
    protected void prepareProjectStructureAndBuildFiles(ApiDescription apiDescription) throws IOException
    {
        // opendma-api folder structure
        opendmaApiFolder = new File(baseFolder, "opendma-api");
        opendmaApiFolder.mkdirs();
        // opendma-api composer file - see https://getcomposer.org/doc/
        copyTemplateToStream("opendma-api-composer", new FileOutputStream(new File(opendmaApiFolder, "composer.json")));
        // opendma-templates folder structure
        opendmaTemplatesFolder = new File(baseFolder, "opendma-templates");
        opendmaTemplatesFolder.mkdirs();
    }
    
    protected void finaliseProjectStructureAndBuildFiles(ApiDescription apiDescription) throws IOException
    {
    }

}