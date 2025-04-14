package org.opendma.apibuilder.apiwriter.cs;

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

public class CsApiWriter extends AbstractApiWriter
{

    public CsApiWriter(File outputFolderRoot) throws ApiWriterException
    {
        super(outputFolderRoot);
    }

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
    
    private OutputStream createCsFile(File targetFolder, String className) throws IOException
    {
        return new FileOutputStream(new File(targetFolder, className+".cs"));
    }

    private void createClassFromTemplate(File targetFolder, String className) throws IOException
    {
        copyTemplateToStream(className,createCsFile(targetFolder, className));
    }

    //-------------------------------------------------------------------------
    // C O N S T A N T S   F I L E
    //-------------------------------------------------------------------------

    protected void createDataTypesFile(ApiDescription apiDescription) throws IOException
    {
        // create type enumeration
        PrintWriter out = new PrintWriter(createCsFile(opendmaApiFolder,"OdmaType"));
        out.println("using System;");
        out.println("using System.Collections.Generic;");
        out.println("using System.Text;");
        out.println("");
        out.println("namespace OpenDMA.Api");
        out.println("{");
        out.println("");
        out.println("    /// <summary>OpenDMA property data types.</summary>");
        out.println("    public enum OdmaType");
        out.println("    {");
        List<ScalarTypeDescription> scalarTypes = apiDescription.getScalarTypes();
        Iterator<ScalarTypeDescription> itScalarTypes = scalarTypes.iterator();
        while(itScalarTypes.hasNext())
        {
            ScalarTypeDescription scalarTypeDescription = itScalarTypes.next();
            out.println("        "+scalarTypeDescription.getName().toUpperCase()+" = "+scalarTypeDescription.getNumericID()+(itScalarTypes.hasNext()?",":""));
        }
        out.println("    }");
        out.println("");
        out.println("}");
        out.close();
    }

    protected void createConstantsFile(ApiDescription apiDescription) throws IOException
    {
        // create common names file
        CsCommonNamesFileWriter constantsFileWriter = new CsCommonNamesFileWriter();
        constantsFileWriter.createConstantsFile(apiDescription, createCsFile(opendmaApiFolder,"OdmaCommonNames"));
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
        createClassFromTemplate(opendmaApiFolder,"OdmaId");
    }

    protected void createGuidFile(ApiDescription apiDescription) throws IOException
    {
        createClassFromTemplate(opendmaApiFolder,"OdmaGuid");
    }

    protected void createContentFile(ApiDescription apiDescription) throws IOException
    {
        createClassFromTemplate(opendmaApiFolder,"IOdmaContent");
    }

    protected void createSearchResultFile(ApiDescription apiDescription) throws IOException
    {
        createClassFromTemplate(opendmaApiFolder,"IOdmaSearchResult");
    }

    protected void createExceptionFiles(ApiDescription apiDescription) throws IOException
    {
        createClassFromTemplate(opendmaApiFolder,"OdmaException");
        // OdmaRuntimeException: C# does not distinguish between checked and unchecked Exceptions
        createClassFromTemplate(opendmaApiFolder,"OdmaObjectNotFoundException");
        createClassFromTemplate(opendmaApiFolder,"OdmaPropertyNotFoundException");
        createClassFromTemplate(opendmaApiFolder,"OdmaInvalidDataTypeException");
        createClassFromTemplate(opendmaApiFolder,"OdmaAccessDeniedException");
        createClassFromTemplate(opendmaApiFolder,"OdmaQuerySyntaxException");
    }

    protected void createSessionManagementFiles(ApiDescription apiDescription) throws IOException
    {
//        createClassFromTemplate(outputFolder,"IOdmaDataSource");
//        createClassFromTemplate(outputFolder,"IOdmaSession");
    }

    //-------------------------------------------------------------------------
    // P R O P E R T Y   F I L E
    //-------------------------------------------------------------------------

    protected void createPropertyFile(ApiDescription apiDescription) throws IOException
    {
        CsPropertyFileWriter csPropertyFileWriter = new CsPropertyFileWriter(this);
        csPropertyFileWriter.createPropertyFile(apiDescription, createCsFile(opendmaApiFolder,"IOdmaProperty"));
    }

    //-------------------------------------------------------------------------
    // C L A S S   F I L E
    //-------------------------------------------------------------------------

    protected void createClassFile(ClassDescription classDescription) throws IOException
    {
        CsObjectsInterfaceFileWriter classFileWriter = new CsObjectsInterfaceFileWriter(this);
        classFileWriter.createClassFile(classDescription, createCsFile(opendmaApiFolder,"I"+classDescription.getApiName()));
    }
    
    //-------------------------------------------------------------------------
    // I M P L E M E N T A T I O N   F I L E S
    //-------------------------------------------------------------------------

    protected void createPropertyImplementationFile(ApiDescription apiDescription) throws IOException
    {
        CsPropertyImplementationFileWriter csPropertyImplementationFileWriter = new CsPropertyImplementationFileWriter(this);
        csPropertyImplementationFileWriter.createPropertyFile(apiDescription, createCsFile(opendmaApiFolder,"OdmaProperty"));
    }

    //-------------------------------------------------------------------------
    // C L A S S   T E M P L A T E S
    //-------------------------------------------------------------------------
    
//    protected OutputStream getClassTemplateFileStream(String outputFolder, ClassDescription classDescription) throws IOException
//    {
//        return createOdmaTemplatesCsFile(outputFolder,classDescription.getApiName()+"Template");
//    }

    protected void createClassTemplateFile(ClassDescription classDescription) throws IOException
    {
//        CsClassTemplateFileWriter classTemplateFileWriter = new CsClassTemplateFileWriter(this);
//        classTemplateFileWriter.createClassFile(classDescription, getClassTemplateFileStream(outputFolder,classDescription));
    }
   
    //-------------------------------------------------------------------------
    // P R O J E C T   S T R U C T U R E   A N  D   B U I L D   F I L E
    //-------------------------------------------------------------------------
    
    private File opendmaApiFolder;
    
    private File opendmaTemplatesFolder;
    
    protected void prepareProjectStructureAndBuildFiles(ApiDescription apiDescription) throws IOException
    {
        // solution
        copyTemplateToStream("OpenDMA.sln", new FileOutputStream(new File(baseFolder, "OpenDMA.sln")));
        // OpenDMA.Api folder structure
        opendmaApiFolder = new File(baseFolder, "OpenDMA.Api");
        opendmaApiFolder.mkdirs();
        // OpenDMA.Api project file
        copyTemplateToStream("OpenDMA.Api.csproj", new FileOutputStream(new File(opendmaApiFolder, "OpenDMA.Api.csproj")));
        // OpenDMA.Templates folder structure
        opendmaTemplatesFolder = new File(baseFolder, "OpenDMA.Templates");
        opendmaTemplatesFolder.mkdirs();
        // OpenDMA.Api project file
        copyTemplateToStream("OpenDMA.Templates.csproj", new FileOutputStream(new File(opendmaTemplatesFolder, "OpenDMA.Templates.csproj")));
    }
    
    protected void finaliseProjectStructureAndBuildFiles(ApiDescription apiDescription) throws IOException
    {
    }

}