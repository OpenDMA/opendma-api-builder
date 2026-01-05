package org.opendma.apibuilder.apiwriter.ts;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.opendma.apibuilder.apiwriter.AbstractApiWriter;
import org.opendma.apibuilder.apiwriter.ApiWriterException;
import org.opendma.apibuilder.structure.ApiDescription;
import org.opendma.apibuilder.structure.ClassDescription;
import org.opendma.apibuilder.structure.ScalarTypeDescription;

public class TypeScriptApiWriter extends AbstractApiWriter
{

    public TypeScriptApiWriter(File outputFolderRoot) throws ApiWriterException
    {
        super(outputFolderRoot);
    }

    public String getName()
    {
        return "TypeScript";
    }

    protected String getTargetFolderName()
    {
        return "ts";
    }

    //-------------------------------------------------------------------------
    // S O U R C E   F I L E   H E L P E R
    //-------------------------------------------------------------------------
    
    private OutputStream createTsFile(File targetFolder, String className) throws IOException
    {
        return new FileOutputStream(new File(targetFolder, className+".ts"));
    }
    
    private OutputStream createApiTsFile(String className) throws IOException
    {
        opendmaApiExportSources.add(className);
        return createTsFile(opendmaApiSourceFolder, className);
    }
    
    private void createApiClassFromTemplate(String className) throws IOException
    {
        copyTemplateToStream(className,createApiTsFile(className));
    }

    //-------------------------------------------------------------------------
    // C O N S T A N T S   F I L E
    //-------------------------------------------------------------------------

    protected void createDataTypesFile(ApiDescription apiDescription) throws IOException
    {
        // create type enumeration
        PrintWriter out = new PrintWriter(createApiTsFile("OdmaType"));
        out.println("/**");
        out.println(" * OpenDMA property data types.");
        out.println(" */");
        out.println("export enum OdmaType {");
        List<ScalarTypeDescription> scalarTypes = apiDescription.getScalarTypes();
        Iterator<ScalarTypeDescription> itScalarTypes = scalarTypes.iterator();
        while(itScalarTypes.hasNext())
        {
            ScalarTypeDescription scalarTypeDescription = itScalarTypes.next();
            out.println("    "+scalarTypeDescription.getName().toUpperCase()+" = "+scalarTypeDescription.getNumericID()+(itScalarTypes.hasNext()?",":""));
        }
        out.println("}");
        out.close();
    }

    protected void createConstantsFile(ApiDescription apiDescription) throws IOException
    {
        // create common names file
        TypeScriptCommonNamesFileWriter constantsFileWriter = new TypeScriptCommonNamesFileWriter();
        constantsFileWriter.createConstantsFile(apiDescription, createApiTsFile("OdmaCommonNames"));
    }

    //-------------------------------------------------------------------------
    // B A S I C   F I L E S
    //-------------------------------------------------------------------------

    protected void createQNameFile(ApiDescription apiDescription) throws IOException
    {
        createApiClassFromTemplate("OdmaQName");
    }

    protected void createIdFile(ApiDescription apiDescription) throws IOException
    {
        createApiClassFromTemplate("OdmaId");
    }

    protected void createGuidFile(ApiDescription apiDescription) throws IOException
    {
        createApiClassFromTemplate("OdmaGuid");
    }

    protected void createContentFile(ApiDescription apiDescription) throws IOException
    {
        createApiClassFromTemplate("OdmaContent");
    }

    protected void createSearchResultFile(ApiDescription apiDescription) throws IOException
    {
        createApiClassFromTemplate("OdmaSearchResult");
    }

    protected void createExceptionFiles(ApiDescription apiDescription) throws IOException
    {
        createApiClassFromTemplate("OdmaError");
        // OdmaRuntimeException: TypeScript does not distinguish between checked and unchecked Exceptions
        createApiClassFromTemplate("OdmaObjectNotFoundError");
        createApiClassFromTemplate("OdmaPropertyNotFoundError");
        createApiClassFromTemplate("OdmaInvalidDataTypeError");
        createApiClassFromTemplate("OdmaAccessDeniedError");
        createApiClassFromTemplate("OdmaQuerySyntaxError");
        createApiClassFromTemplate("OdmaServiceError");
        createApiClassFromTemplate("OdmaAuthenticationError");
    }

    protected void createSessionManagementFiles(ApiDescription apiDescription) throws IOException
    {
        createApiClassFromTemplate("OdmaSession");
    }

    //-------------------------------------------------------------------------
    // P R O P E R T Y   F I L E
    //-------------------------------------------------------------------------

    protected void createPropertyFile(ApiDescription apiDescription) throws IOException
    {
        TypeScriptPropertyFileWriter typeScriptPropertyFileWriter = new TypeScriptPropertyFileWriter(this);
        typeScriptPropertyFileWriter.createPropertyFile(apiDescription, createApiTsFile("OdmaProperty"));
    }

    //-------------------------------------------------------------------------
    // C L A S S   F I L E
    //-------------------------------------------------------------------------

    protected void createCoreObjectFile(ApiDescription apiDescription) throws IOException
    {
        createApiClassFromTemplate("OdmaCoreObject");
    }

    protected void createClassFile(ClassDescription classDescription) throws IOException
    {
        TypeScriptObjectsInterfaceFileWriter classFileWriter = new TypeScriptObjectsInterfaceFileWriter(this);
        classFileWriter.createClassFile(classDescription, createApiTsFile(classDescription.getApiName()));
    }
    
    //-------------------------------------------------------------------------
    // I M P L E M E N T A T I O N   F I L E S
    //-------------------------------------------------------------------------

    protected void createPropertyImplementationFile(ApiDescription apiDescription) throws IOException
    {
        TypeScriptPropertyImplementationFileWriter typeScriptPropertyImplementationFileWriter = new TypeScriptPropertyImplementationFileWriter(this);
        typeScriptPropertyImplementationFileWriter.createPropertyFile(apiDescription, createApiTsFile("OdmaPropertyImpl"));
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
    
    private File opendmaApiProjectFolder;
    
    private File opendmaApiSourceFolder;
    
    private List<String> opendmaApiExportSources = new LinkedList<String>();
    
    protected void prepareProjectStructureAndBuildFiles(final ApiDescription apiDescription) throws IOException
    {
        PlaceholderResolver resolver = new PlaceholderResolver()
        {
            public String resolve(String placeholder)
            {
                if("version".equals(placeholder))
                {
                    return apiDescription.getVersion();
                }
                throw new RuntimeException("Unknown placefolder: {{"+placeholder+"}}");
            }
        };
        // opendma-api folder structure
        opendmaApiProjectFolder = new File(baseFolder, "opendma-api");
        opendmaApiProjectFolder.mkdirs();
        opendmaApiSourceFolder = new File(opendmaApiProjectFolder, "src");
        opendmaApiSourceFolder.mkdirs();
        // opendma-api package.json and tsconfig.json
        copyTemplateToStream("opendma-api-package", new FileOutputStream(new File(opendmaApiProjectFolder, "package.json")), resolver);
        copyTemplateToStream("opendma-api-tsconfig", new FileOutputStream(new File(opendmaApiProjectFolder, "tsconfig.json")), resolver);
        // LICENSE
        copyGlobalTemplateToStream("apache-license", new FileOutputStream(new File(opendmaApiProjectFolder, "LICENSE")), null,  true);
    }
    
    protected void finaliseProjectStructureAndBuildFiles(ApiDescription apiDescription) throws IOException
    {
        // create index.ts
        PrintWriter out = new PrintWriter(createTsFile(opendmaApiSourceFolder,"index"));
        Iterator<String> itExportSources = opendmaApiExportSources.iterator();
        while(itExportSources.hasNext())
        {
            out.println("export * from './"+itExportSources.next()+"';");
        }
        out.close();
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