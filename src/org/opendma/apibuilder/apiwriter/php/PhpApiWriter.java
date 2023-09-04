package org.opendma.apibuilder.apiwriter.php;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;

import org.opendma.apibuilder.apiwriter.AbstractApiWriter;
import org.opendma.apibuilder.structure.ApiDescription;
import org.opendma.apibuilder.structure.ClassDescription;
import org.opendma.apibuilder.structure.ScalarTypeDescription;

public class PhpApiWriter extends AbstractApiWriter
{

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
    
    private OutputStream createPhpFile(String outputFolder, String packageName, String className) throws IOException
    {
        String packageDirectory = outputFolder + packageName;
        File containingDir = new File(packageDirectory);
        if(!containingDir.exists())
        {
            if(!containingDir.mkdirs())
            {
                throw new IOException("Can not create package directory");
            }
        }
        return new FileOutputStream(packageDirectory+(packageName.length()==0?"":File.separator)+className+".php");
    }
    
    private void createClassFromTemplate(String outputFolder, String packageName, String className) throws IOException
    {
        copyTemplateToStream(className,createPhpFile(outputFolder,packageName,className));
    }

    //-------------------------------------------------------------------------
    // C O N S T A N T S   F I L E
    //-------------------------------------------------------------------------

    protected void createDataTypesFile(ApiDescription apiDescription, String outputFolder) throws IOException
    {
        // create type enumeration
        PrintWriter out = new PrintWriter(createPhpFile(outputFolder,"OpenDMA/Api","OdmaType"));
        out.println("<?php");
        out.println("enum OdmaType");
        out.println("{");
        List scalarTypes = apiDescription.getScalarTypes();
        Iterator itScalarTypes = scalarTypes.iterator();
        while(itScalarTypes.hasNext())
        {
            ScalarTypeDescription scalarTypeDescription = (ScalarTypeDescription)itScalarTypes.next();
            out.println("    case "+scalarTypeDescription.getName().toUpperCase()+";");
        }
        out.println("}");
        out.close();
    }

    protected void createConstantsFile(ApiDescription apiDescription, String outputFolder) throws IOException
    {
        // create common names file
        PhpConstantsFileWriter constantsFileWriter = new PhpConstantsFileWriter();
        constantsFileWriter.createConstantsFile(apiDescription, createPhpFile(outputFolder,"OpenDMA/Api","OdmaCommonNames"));
    }

    //-------------------------------------------------------------------------
    // B A S I C   F I L E S
    //-------------------------------------------------------------------------

    protected OutputStream getBasicFileStream(String classname, String outputFolder) throws IOException
    {
        return createPhpFile(outputFolder,"OpenDMA/Api",classname);
    }

    protected void createQNameFile(ApiDescription apiDescription, String outputFolder) throws IOException
    {
        createClassFromTemplate(outputFolder,"OpenDMA/Api","OdmaQName");
    }

    protected void createIdFile(ApiDescription apiDescription, String outputFolder) throws IOException
    {
        createClassFromTemplate(outputFolder,"OpenDMA/Api","OdmaId");
    }

    protected void createGuidFile(ApiDescription apiDescription, String outputFolder) throws IOException
    {
        createClassFromTemplate(outputFolder,"OpenDMA/Api","OdmaGuid");
    }

    protected void createContentFile(ApiDescription apiDescription, String outputFolder) throws IOException
    {
        createClassFromTemplate(outputFolder,"OpenDMA/Api","OdmaContent");
    }

    protected void createSearchResultFile(ApiDescription apiDescription, String outputFolder) throws IOException
    {
        createClassFromTemplate(outputFolder,"OpenDMA/Api","OdmaSearchResult");
    }

    protected void createExceptionFiles(ApiDescription apiDescription, String outputFolder) throws IOException
    {
        createClassFromTemplate(outputFolder,"OpenDMA/Exceptions","OdmaException");
        createClassFromTemplate(outputFolder,"OpenDMA/Exceptions","OdmaObjectNotFoundException");
        createClassFromTemplate(outputFolder,"OpenDMA/Exceptions","OdmaInvalidDataTypeException");
        createClassFromTemplate(outputFolder,"OpenDMA/Exceptions","OdmaAccessDeniedException");
        createClassFromTemplate(outputFolder,"OpenDMA/Exceptions","OdmaQuerySyntaxException");
        createClassFromTemplate(outputFolder,"OpenDMA/Exceptions","OdmaSearchException");
    }

    protected void createSessionManagementFiles(ApiDescription apiDescription, String outputFolder) throws IOException
    {
        createClassFromTemplate(outputFolder,"OpenDMA/Api","OdmaDataSource");
        createClassFromTemplate(outputFolder,"OpenDMA/Api","OdmaSession");
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
        PhpClassFileWriter classFileWriter = new PhpClassFileWriter(this);
        classFileWriter.createClassFile(classDescription, createPhpFile(outputFolder,"OpenDMA/Api",classDescription.getApiName()));
    }
    
    //-------------------------------------------------------------------------
    // C O L L E C T I O N   F I L E S
    //-------------------------------------------------------------------------
    
    protected void createEnumerationFile(ClassDescription classDescription, String baseFolder) throws IOException
    {
    }

    protected void createListFile(ScalarTypeDescription scalarTypeDescription, String baseFolder) throws IOException
    {
    }
    
    //-------------------------------------------------------------------------
    // I M P L E M E N T A T I O N   F I L E S
    //-------------------------------------------------------------------------

    protected void createPropertyImplementationFile(ApiDescription apiDescription, String outputFolder) throws IOException
    {
    }

    protected void createListImplementationFile(ScalarTypeDescription scalarTypeDescription, String baseFolder) throws IOException
    {
    }

    //-------------------------------------------------------------------------
    // C L A S S   T E M P L A T E S
    //-------------------------------------------------------------------------

    protected void createClassTemplateFile(ClassDescription classDescription, String outputFolder) throws IOException
    {
        PhpClassTemplateFileWriter classTemplateFileWriter = new PhpClassTemplateFileWriter(this);
        classTemplateFileWriter.createClassFile(classDescription, createPhpFile(outputFolder,"OpenDMA/Templates",classDescription.getApiName()));
    }
   
    //-------------------------------------------------------------------------
    // B U I L D   F I L E
    //-------------------------------------------------------------------------
    
    protected void createBuildFile(ApiDescription apiDescription, String baseFolder) throws IOException
    {
        // we do not create a build file for now. We use Eclipse.
    }

}