package org.opendma.apibuilder.apiwriter.cs;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;

import org.opendma.apibuilder.apiwriter.AbstractApiWriter;
import org.opendma.apibuilder.structure.ApiDescription;
import org.opendma.apibuilder.structure.ClassDescription;
import org.opendma.apibuilder.structure.ScalarTypeDescription;

public class CsApiWriter extends AbstractApiWriter
{

    public String getName()
    {
        return "C#";
    }
    
    protected String getTargetFolderName()
    {
        return "cs";
    }

    //-------------------------------------------------------------------------
    // S O U R C E   F I L E   H E L P E R
    //-------------------------------------------------------------------------
    
    private OutputStream createGenericCsFile(String outputFolder, String projectName, String className) throws IOException
    {
        String packageDirectory = outputFolder + projectName;
        File containingDir = new File(packageDirectory);
        if(!containingDir.exists())
        {
            if(!containingDir.mkdirs())
            {
                throw new IOException("Can not create package directory");
            }
        }
        return new FileOutputStream(packageDirectory+File.separator+className+".cs");
    }
    
    private OutputStream createOdmaApiCsFile(String outputFolder, String className) throws IOException
    {
        return createGenericCsFile(outputFolder,"OpenDMA.Api",className);
    }
    
    private OutputStream createOdmaTemplatesCsFile(String outputFolder, String className) throws IOException
    {
        return createGenericCsFile(outputFolder,"OpenDMA.Templates",className);
    }

    private void createClassFromTemplate(String outputFolder, String className) throws IOException
    {
        copyTemplateToStream(className,createOdmaApiCsFile(outputFolder,className));
    }

    //-------------------------------------------------------------------------
    // C O N S T A N T S   F I L E
    //-------------------------------------------------------------------------

    protected void createDataTypesFile(ApiDescription apiDescription, String outputFolder) throws IOException
    {
        // create type enumeration
        PrintWriter out = new PrintWriter(createOdmaApiCsFile(outputFolder,"OdmaType"));
        out.println("using OpenDMA.Api;");
        out.println("");
        out.println("namespace OpenDMA.Api");
        out.println("{");
        out.println("");
        out.println("    /// <summary>OpenDMA property data types.</summary>");
        out.println("    public enum OdmaType");
        out.println("    {");
        List scalarTypes = apiDescription.getScalarTypes();
        Iterator itScalarTypes = scalarTypes.iterator();
        while(itScalarTypes.hasNext())
        {
            ScalarTypeDescription scalarTypeDescription = (ScalarTypeDescription)itScalarTypes.next();
            out.println("        "+scalarTypeDescription.getName().toUpperCase()+" = "+scalarTypeDescription.getNumericID()+(itScalarTypes.hasNext()?",":""));
        }
        out.println("    }");
        out.println("");
        out.println("}");
        out.close();
    }

    protected void createConstantsFile(ApiDescription apiDescription, String outputFolder) throws IOException
    {
        // create common names file
        CsConstantsFileWriter constantsFileWriter = new CsConstantsFileWriter();
        constantsFileWriter.createConstantsFile(apiDescription, createOdmaApiCsFile(outputFolder,"OdmaCommonNames"));
    }

    //-------------------------------------------------------------------------
    // B A S I C   F I L E S
    //-------------------------------------------------------------------------

    protected void createQNameFile(ApiDescription apiDescription, String outputFolder) throws IOException
    {
        createClassFromTemplate(outputFolder,"OdmaQName");
    }

    protected void createIdFile(ApiDescription apiDescription, String outputFolder) throws IOException
    {
        createClassFromTemplate(outputFolder,"OdmaId");
    }

    protected void createGuidFile(ApiDescription apiDescription, String outputFolder) throws IOException
    {
        createClassFromTemplate(outputFolder,"OdmaGuid");
    }

    protected void createContentFile(ApiDescription apiDescription, String outputFolder) throws IOException
    {
        createClassFromTemplate(outputFolder,"IOdmaContent");
    }

    protected void createSearchResultFile(ApiDescription apiDescription, String outputFolder) throws IOException
    {
        createClassFromTemplate(outputFolder,"IOdmaSearchResult");
    }

    protected void createExceptionFiles(ApiDescription apiDescription, String outputFolder) throws IOException
    {
        createClassFromTemplate(outputFolder,"OdmaObjectNotFoundException");
        createClassFromTemplate(outputFolder,"OdmaInvalidDataTypeException");
        createClassFromTemplate(outputFolder,"OdmaRuntimeException");
        createClassFromTemplate(outputFolder,"OdmaAccessDeniedException");
        createClassFromTemplate(outputFolder,"OdmaQuerySyntaxException");
        createClassFromTemplate(outputFolder,"OdmaSearchException");
    }

    protected void createSessionManagementFiles(ApiDescription apiDescription, String outputFolder) throws IOException
    {
        createClassFromTemplate(outputFolder,"IOdmaDataSource");
        createClassFromTemplate(outputFolder,"IOdmaSession");
    }

    //-------------------------------------------------------------------------
    // P R O P E R T Y   F I L E
    //-------------------------------------------------------------------------

    protected void createPropertyFile(ApiDescription apiDescription, String outputFolder) throws IOException
    {
        CsPropertyFileWriter csPropertyFileWriter = new CsPropertyFileWriter(this);
        csPropertyFileWriter.createPropertyFile(apiDescription, createOdmaApiCsFile(outputFolder,"IOdmaProperty"));
    }

    //-------------------------------------------------------------------------
    // C L A S S   F I L E
    //-------------------------------------------------------------------------

    protected void createClassFile(ClassDescription classDescription, String outputFolder) throws IOException
    {
        CsClassFileWriter classFileWriter = new CsClassFileWriter(this);
        classFileWriter.createClassFile(classDescription, createOdmaApiCsFile(outputFolder,"I"+classDescription.getApiName()));
    }
    
    //-------------------------------------------------------------------------
    // C O L L E C T I O N   F I L E S
    //-------------------------------------------------------------------------
    
    protected void createEnumerationFile(ClassDescription classDescription, String baseFolder) throws IOException
    {
        // We are using generics in the form of IEnumerable<OdmaObject>. There is no need for enumeration files
    }

    protected void createListFile(ScalarTypeDescription scalarTypeDescription, String baseFolder) throws IOException
    {
        // We are using generics in the form of IList<Object>. There is no need for list files
    }
    
    //-------------------------------------------------------------------------
    // I M P L E M E N T A T I O N   F I L E S
    //-------------------------------------------------------------------------

    protected void createPropertyImplementationFile(ApiDescription apiDescription, String outputFolder) throws IOException
    {
        CsPropertyImplementationFileWriter csPropertyImplementationFileWriter = new CsPropertyImplementationFileWriter(this);
        csPropertyImplementationFileWriter.createPropertyFile(apiDescription, createOdmaApiCsFile(outputFolder,"OdmaProperty"));
    }

    protected void createListImplementationFile(ScalarTypeDescription scalarTypeDescription, String baseFolder) throws IOException
    {
        // We are using generics in the form of IList<Object>. There is no need for list files
    }

    //-------------------------------------------------------------------------
    // C L A S S   T E M P L A T E S
    //-------------------------------------------------------------------------
    
    protected OutputStream getClassTemplateFileStream(String outputFolder, ClassDescription classDescription) throws IOException
    {
        return createOdmaTemplatesCsFile(outputFolder,classDescription.getApiName()+"Template");
    }

    protected void createClassTemplateFile(ClassDescription classDescription, String outputFolder) throws IOException
    {
        CsClassTemplateFileWriter classTemplateFileWriter = new CsClassTemplateFileWriter(this);
        classTemplateFileWriter.createClassFile(classDescription, getClassTemplateFileStream(outputFolder,classDescription));
    }
   
    //-------------------------------------------------------------------------
    // B U I L D   F I L E
    //-------------------------------------------------------------------------
    
    protected void createBuildFile(ApiDescription apiDescription, String baseFolder) throws IOException
    {
        // create Api project files
        OutputStream to = new FileOutputStream(baseFolder+File.separator+"OpenDMA.Api"+File.separator+"OpenDMA.Api.csproj");
        InputStream csprojFrom = getTemplateAsStream("OpenDMA.Api.csproj");
        streamCopy(csprojFrom, to);
        csprojFrom.close();
        to.close();
        // create Templates project files
        to = new FileOutputStream(baseFolder+File.separator+"OpenDMA.Templates"+File.separator+"OpenDMA.Templates.csproj");
        csprojFrom = getTemplateAsStream("OpenDMA.Templates.csproj");
        streamCopy(csprojFrom, to);
        csprojFrom.close();
        to.close();
        // create solution file
        to = new FileOutputStream(baseFolder+"OpenDMA.sln");
        InputStream solutionFrom = getTemplateAsStream("OpenDMA.sln");
        streamCopy(solutionFrom, to);
        solutionFrom.close();
        to.close();
   }

}