package org.opendma.apibuilder.apiwriter.cs;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
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
    
    /** List of files that need to be compiled by the build file */
    protected List compileFileList = new ArrayList();
    
    private OutputStream createCsFile(String outputFolder, String packageName, String className) throws IOException
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
        compileFileList.add(packageName+File.separator+className+".cs");
        return new FileOutputStream(packageDirectory+File.separator+className+".cs");
    }
    
    protected String getProgrammingLanguageSpecificFolderName()
    {
        return "cs";
    }

    //-------------------------------------------------------------------------
    // C O N S T A N T S   F I L E
    //-------------------------------------------------------------------------

    protected void createDataTypesFile(ApiDescription apiDescription, String outputFolder) throws IOException
    {
        // create type enumeration
        PrintWriter out = new PrintWriter(createCsFile(outputFolder,"OpenDMA.Api","OdmaType"));
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

    protected OutputStream getConstantsFileStream(String outputFolder) throws IOException
    {
        return createCsFile(outputFolder,"OpenDMA.Api","OdmaCommonNames");
    }

    protected void createConstantsFile(ApiDescription apiDescription, String outputFolder) throws IOException
    {
        CsConstantsFileWriter constantsFileWriter = new CsConstantsFileWriter();
        constantsFileWriter.createConstantsFile(apiDescription, getConstantsFileStream(outputFolder));
    }

    //-------------------------------------------------------------------------
    // B A S I C   F I L E S
    //-------------------------------------------------------------------------

    protected OutputStream getBasicFileStream(String classname, String outputFolder) throws IOException
    {
        return createCsFile(outputFolder,"OpenDMA.Api",classname);
    }

    protected void createQNameFile(ApiDescription apiDescription, String outputFolder) throws IOException
    {
        OutputStream to = getBasicFileStream("OdmaQName",outputFolder);
        InputStream from = getTemplateAsStream("OdmaQName");
        streamCopy(from, to);
        from.close();
        to.close();
    }

    protected void createIdFile(ApiDescription apiDescription, String outputFolder) throws IOException
    {
        OutputStream to = getBasicFileStream("OdmaId",outputFolder);
        InputStream from = getTemplateAsStream("OdmaId");
        streamCopy(from, to);
        from.close();
        to.close();
    }

    protected void createGuidFile(ApiDescription apiDescription, String outputFolder) throws IOException
    {
        OutputStream to = getBasicFileStream("OdmaGuid",outputFolder);
        InputStream from = getTemplateAsStream("OdmaGuid");
        streamCopy(from, to);
        from.close();
        to.close();
    }

    protected void createContentFile(ApiDescription apiDescription, String outputFolder) throws IOException
    {
        OutputStream to = getBasicFileStream("IOdmaContent",outputFolder);
        InputStream from = getTemplateAsStream("IOdmaContent");
        streamCopy(from, to);
        from.close();
        to.close();
    }

    protected void createSearchResultFile(ApiDescription apiDescription, String outputFolder) throws IOException
    {
        OutputStream to = getBasicFileStream("IOdmaSearchResult",outputFolder);
        InputStream from = getTemplateAsStream("IOdmaSearchResult");
        streamCopy(from, to);
        from.close();
        to.close();
    }

    protected void createExceptionFiles(ApiDescription apiDescription, String outputFolder) throws IOException
    {
        internalCreateExceptionFile(outputFolder,"OdmaObjectNotFoundException");
        internalCreateExceptionFile(outputFolder,"OdmaInvalidDataTypeException");
        internalCreateExceptionFile(outputFolder,"OdmaRuntimeException");
        internalCreateExceptionFile(outputFolder,"OdmaAccessDeniedException");
        internalCreateExceptionFile(outputFolder,"OdmaQuerySyntaxException");
        internalCreateExceptionFile(outputFolder,"OdmaSearchException");
    }
    
    protected void internalCreateExceptionFile(String outputFolder, String exceptionClassName) throws IOException
    {
        OutputStream to = createCsFile(outputFolder,"OpenDMA.Exceptions",exceptionClassName);
        InputStream from = getTemplateAsStream(exceptionClassName);
        streamCopy(from,to);
        from.close();
        to.close();
    }

    protected void createSessionManagementFiles(ApiDescription apiDescription, String outputFolder) throws IOException
    {
        internalCreateSessionManagementFile(outputFolder,"IOdmaDataSource");
        internalCreateSessionManagementFile(outputFolder,"IOdmaSession");
    }
    
    protected void internalCreateSessionManagementFile(String outputFolder, String className) throws IOException
    {
        OutputStream to =  getBasicFileStream(className,outputFolder);
        InputStream from = getTemplateAsStream(className);
        streamCopy(from,to);
        from.close();
        to.close();
    }

    //-------------------------------------------------------------------------
    // P R O P E R T Y   F I L E
    //-------------------------------------------------------------------------

    protected void createPropertyFile(ApiDescription apiDescription, String outputFolder) throws IOException
    {
        CsPropertyFileWriter csPropertyFileWriter = new CsPropertyFileWriter(this);
        csPropertyFileWriter.createPropertyFile(apiDescription, getBasicFileStream("IOdmaProperty",outputFolder));
    }

    //-------------------------------------------------------------------------
    // C L A S S   F I L E
    //-------------------------------------------------------------------------

    protected OutputStream getClassFileStream(String outputFolder, ClassDescription classDescription) throws IOException
    {
        return createCsFile(outputFolder,"OpenDMA.Api","I"+classDescription.getApiName());
    }

    protected void createClassFile(ClassDescription classDescription, String outputFolder) throws IOException
    {
        CsClassFileWriter classFileWriter = new CsClassFileWriter(this);
        classFileWriter.createClassFile(classDescription, getClassFileStream(outputFolder,classDescription));
    }
    
    //-------------------------------------------------------------------------
    // C O L L E C T I O N   F I L E S
    //-------------------------------------------------------------------------

    protected OutputStream getEnumerationFileStream(String baseFolder, ClassDescription classDescription) throws IOException
    {
        return createCsFile(baseFolder,"OpenDMA.Api.Collections","I"+classDescription.getApiName()+"Enumerable");
    }
    
    protected void createEnumerationFile(ClassDescription classDescription, String baseFolder) throws IOException
    {
        CsEnumerableFileWriter enumerableFileWriter = new CsEnumerableFileWriter();
        enumerableFileWriter.createEnumerationFile(classDescription, getEnumerationFileStream(baseFolder,classDescription));
    }

    protected OutputStream getListFileStream(String baseFolder, ScalarTypeDescription scalarTypeDescription) throws IOException
    {
        return createCsFile(baseFolder,"OpenDMA.Api.Collections",getScalarDataType(scalarTypeDescription,true));
    }

    protected void createListFile(ScalarTypeDescription scalarTypeDescription, String baseFolder) throws IOException
    {
        CsListFileWriter listFileWriter = new CsListFileWriter(this);
        listFileWriter.createListFile(scalarTypeDescription, getListFileStream(baseFolder,scalarTypeDescription));
    }
    
    //-------------------------------------------------------------------------
    // I M P L E M E N T A T I O N   F I L E S
    //-------------------------------------------------------------------------

    protected void createPropertyImplementationFile(ApiDescription apiDescription, String outputFolder) throws IOException
    {
        CsPropertyImplementationFileWriter csPropertyImplementationFileWriter = new CsPropertyImplementationFileWriter(this);
        csPropertyImplementationFileWriter.createPropertyFile(apiDescription, createCsFile(outputFolder,"OpenDMA.Impl","OdmaProperty"));
    }

    protected OutputStream getListImplementationFileStream(String baseFolder, ScalarTypeDescription scalarTypeDescription) throws IOException
    {
        return createCsFile(baseFolder,"OpenDMA.Impl.Collections","Array"+getScalarDataType(scalarTypeDescription,true).substring(1));
    }

    protected void createListImplementationFile(ScalarTypeDescription scalarTypeDescription, String baseFolder) throws IOException
    {
        CsListImplementationFileWriter listImplementationFileWriter = new CsListImplementationFileWriter(this);
        listImplementationFileWriter.createListFile(scalarTypeDescription, getListImplementationFileStream(baseFolder,scalarTypeDescription));
    }

    //-------------------------------------------------------------------------
    // C L A S S   T E M P L A T E S
    //-------------------------------------------------------------------------
    
    protected OutputStream getClassTemplateFileStream(String outputFolder, ClassDescription classDescription) throws IOException
    {
        return createCsFile(outputFolder,"OpenDMA.Templates",classDescription.getApiName()+"Template");
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
        OutputStream to = new FileOutputStream(baseFolder+"OpenDMA.Api.csproj");
        InputStream headerFrom = getTemplateAsStream("CsprojFileHeader");
        streamCopy(headerFrom, to);
        headerFrom.close();
        PrintWriter out = new PrintWriter(to);
        Iterator itCompileFileList = compileFileList.iterator();
        while(itCompileFileList.hasNext())
        {
            String fileName = (String)itCompileFileList.next();
            out.println("    <Compile Include=\""+fileName+"\" />");
        }
        out.flush();
        InputStream footerFrom = getTemplateAsStream("CsprojFileFoot");
        streamCopy(footerFrom, to);
        footerFrom.close();
        to.close();
        // create Properties file
        to = createCsFile(baseFolder,"Properties","AssemblyInfo");
        InputStream assemblyInfoFrom = getTemplateAsStream("AssemblyInfo");
        streamCopy(assemblyInfoFrom, to);
        assemblyInfoFrom.close();
        to.close();
        // create solution file
        to = new FileOutputStream(baseFolder+"OpenDMA.Api.sln");
        InputStream solutionFrom = getTemplateAsStream("OpenDMA.Api");
        streamCopy(solutionFrom, to);
        solutionFrom.close();
        to.close();
   }

}