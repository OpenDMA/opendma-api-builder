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
            out.println("    default");
            out.println("        return \"UNKNOWN\"");
        }
        out.println("    }");
        out.println("}");
        out.close();
    }

    protected void createConstantsFile(ApiDescription apiDescription) throws IOException
    {
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
    // P R O J E C T   S T R U C T U R E   A N  D   B U I L D   F I L E
    //-------------------------------------------------------------------------
    
    private File opendmaApiProjectFolder;
    
    private File opendmaTemplatesFolder;
    
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
        // go.mod
        copyTemplateToStream("opendma-api-go.mod", new FileOutputStream(new File(opendmaApiProjectFolder, "go.mod")), resolver);
        // opendma-templates folder
        opendmaTemplatesFolder = new File(baseFolder, "opendma-templates");
        opendmaTemplatesFolder.mkdirs();
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