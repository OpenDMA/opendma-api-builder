package org.opendma.apibuilder.apiwriter.go;

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
    
    private OutputStream createGoFile(File targetFolder, String className) throws IOException
    {
        return new FileOutputStream(new File(targetFolder, className+".go"));
    }
    
    private void createClassFromTemplate(File targetFolder, String className) throws IOException
    {
        copyTemplateToStream(className,createGoFile(targetFolder,className));
    }

    //-------------------------------------------------------------------------
    // C O N S T A N T S   F I L E
    //-------------------------------------------------------------------------

    protected void createDataTypesFile(ApiDescription apiDescription) throws IOException
    {
        // create type enumeration
        PrintWriter out = new PrintWriter(createGoFile(opendmaApiProjectFolder, "OdmaType"));
        out.println("package opendma");
        out.println();
        out.println("type OdmaType int");
        out.println();
        out.println("const (");
        List<ScalarTypeDescription> scalarTypes = apiDescription.getScalarTypes();
        Iterator<ScalarTypeDescription> itScalarTypes = scalarTypes.iterator();
        while(itScalarTypes.hasNext())
        {
            ScalarTypeDescription scalarTypeDescription = itScalarTypes.next();
            out.println("    "+scalarTypeDescription.getName().toUpperCase()+" OdmaType = "+scalarTypeDescription.getNumericID());
        }
        out.println(")");
        out.println();
        out.println("func (t OdmaType) String() string {");
        out.println("    switch t {");
        itScalarTypes = scalarTypes.iterator();
        while(itScalarTypes.hasNext())
        {
            ScalarTypeDescription scalarTypeDescription = (ScalarTypeDescription)itScalarTypes.next();
            out.println("    case "+scalarTypeDescription.getName().toUpperCase()+":");
            out.println("        return \""+scalarTypeDescription.getName().toUpperCase()+"\"");
        }
        out.println("    default:");
        out.println("        return \"UNKNOWN\"");
        out.println("    }");
        out.println("}");
        out.close();
    }

    protected void createConstantsFile(ApiDescription apiDescription) throws IOException
    {
        // create common names file
        GoCommonNamesFileWriter constantsFileWriter = new GoCommonNamesFileWriter();
        constantsFileWriter.createConstantsFile(apiDescription, createGoFile(opendmaApiProjectFolder, "OdmaCommonNames"));
    }

    //-------------------------------------------------------------------------
    // B A S I C   F I L E S
    //-------------------------------------------------------------------------

    protected void createQNameFile(ApiDescription apiDescription) throws IOException
    {
        createClassFromTemplate(opendmaApiProjectFolder, "OdmaQName");
    }

    protected void createIdFile(ApiDescription apiDescription) throws IOException
    {
        createClassFromTemplate(opendmaApiProjectFolder, "OdmaId");
    }

    protected void createGuidFile(ApiDescription apiDescription) throws IOException
    {
        createClassFromTemplate(opendmaApiProjectFolder, "OdmaGuid");
    }

    protected void createContentFile(ApiDescription apiDescription) throws IOException
    {
        createClassFromTemplate(opendmaApiProjectFolder, "OdmaContent");
    }

    protected void createSearchResultFile(ApiDescription apiDescription) throws IOException
    {
        createClassFromTemplate(opendmaApiProjectFolder, "OdmaSearchResult");
    }

    protected void createExceptionFiles(ApiDescription apiDescription) throws IOException
    {
        createClassFromTemplate(opendmaApiProjectFolder, "errors");
    }

    protected void createSessionManagementFiles(ApiDescription apiDescription) throws IOException
    {
        createClassFromTemplate(opendmaApiProjectFolder, "OdmaSession");
    }

    //-------------------------------------------------------------------------
    // P R O P E R T Y   F I L E
    //-------------------------------------------------------------------------

    protected void createPropertyFile(ApiDescription apiDescription) throws IOException
    {
        GoPropertyFileWriter goPropertyFileWriter = new GoPropertyFileWriter(this);
        goPropertyFileWriter.createPropertyFile(apiDescription, createGoFile(opendmaApiProjectFolder, "OdmaProperty"));
    }

    //-------------------------------------------------------------------------
    // C L A S S   F I L E
    //-------------------------------------------------------------------------

    protected void createCoreObjectFile(ApiDescription apiDescription) throws IOException
    {
        createClassFromTemplate(opendmaApiProjectFolder, "OdmaCoreObject");
    }

    protected void createClassFile(ClassDescription classDescription) throws IOException
    {
        GoObjectsInterfaceFileWriter classFileWriter = new GoObjectsInterfaceFileWriter(this);
        classFileWriter.createClassFile(classDescription, createGoFile(opendmaApiProjectFolder,classDescription.getApiName()));
    }
    
    //-------------------------------------------------------------------------
    // C O L L E C T I O N   F I L E S
    //-------------------------------------------------------------------------
    
    protected void createEnumerationFile(ClassDescription classDescription) throws IOException
    {
        createIterableFile(classDescription);
        createIteratorFile(classDescription);
    }
    
    protected void createIterableFile(ClassDescription classDescription) throws IOException
    {
        PrintWriter out = new PrintWriter(createGoFile(opendmaApiProjectFolder, classDescription.getApiName()+"Iterable"));
        out.println("package opendma");
        out.println("");
        out.println("type "+classDescription.getApiName()+"Iterable interface {");
        if(classDescription.getExtendsApiName() != null)
        {
            out.println("    "+classDescription.getExtendsApiName()+"Iterable");
        }
        else if(classDescription.getAspect())
        {
            out.println("    "+classDescription.getContainingApiDescription().getObjectClass().getApiName()+"Iterable");
        }
        out.println("    Get"+classDescription.getApiName()+"Iterator() "+classDescription.getApiName()+"Iterator");
        out.println("}");
        out.close();
    }
    
    protected void createIteratorFile(ClassDescription classDescription) throws IOException
    {
        PrintWriter out = new PrintWriter(createGoFile(opendmaApiProjectFolder, classDescription.getApiName()+"Iterator"));
        out.println("package opendma");
        out.println("");
        out.println("type "+classDescription.getApiName()+"Iterator interface {");
        if(classDescription.getExtendsApiName() != null)
        {
            out.println("    "+classDescription.getExtendsApiName()+"Iterator");
        }
        else if(classDescription.getAspect())
        {
            out.println("    "+classDescription.getContainingApiDescription().getObjectClass().getApiName()+"Iterator");
        }
        out.println("    HasNext() bool");
        out.println("    Next"+classDescription.getApiName()+"() ("+classDescription.getApiName()+", bool)");
        out.println("}");
        out.close();
    }
    
    //-------------------------------------------------------------------------
    // I M P L E M E N T A T I O N   F I L E S
    //-------------------------------------------------------------------------

    protected void createPropertyImplementationFile(ApiDescription apiDescription) throws IOException
    {
        GoPropertyImplementationFileWriter goPropertyImplementationFileWriter = new GoPropertyImplementationFileWriter(this);
        goPropertyImplementationFileWriter.createPropertyFile(apiDescription, createGoFile(opendmaApiProjectFolder, "OdmaPropertyImpl"));
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
        opendmaApiProjectFolder = new File(baseFolder, "opendma");
        opendmaApiProjectFolder.mkdirs();
        // go.mod
        copyTemplateToStream("opendma-api-go.mod", new FileOutputStream(new File(opendmaApiProjectFolder, "go.mod")), resolver);
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