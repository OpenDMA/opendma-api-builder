package org.opendma.apibuilder.apiwriter.js;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;

import org.opendma.apibuilder.apiwriter.AbstractApiWriter;
import org.opendma.apibuilder.apiwriter.ApiWriterException;
import org.opendma.apibuilder.structure.ApiDescription;
import org.opendma.apibuilder.structure.ClassDescription;
import org.opendma.apibuilder.structure.ScalarTypeDescription;

public class JavaScriptApiWriter extends AbstractApiWriter
{

    public JavaScriptApiWriter(File outputFolderRoot) throws ApiWriterException
    {
        super(outputFolderRoot);
    }

    public String getName()
    {
        return "JavaScript";
    }

    protected String getTargetFolderName()
    {
        return "js";
    }

    //-------------------------------------------------------------------------
    // S O U R C E   F I L E   H E L P E R
    //-------------------------------------------------------------------------
    
    private void copyClassToApi(String className) throws IOException
    {
        PrintWriter out = new PrintWriter(opendmaApiFOS);
        out.println();
        out.println();
        out.flush();
        copyTemplateToStream(className, opendmaApiFOS, false);
    }

    //-------------------------------------------------------------------------
    // C O N S T A N T S   F I L E
    //-------------------------------------------------------------------------

    protected void createDataTypesFile(ApiDescription apiDescription) throws IOException
    {
        // create type enumeration
        PrintWriter out = new PrintWriter(opendmaApiFOS);
        out.println();
        out.println("/**");
        out.println(" * OpenDMA property data types.");
        out.println(" */");
        out.println("const OdmaType = Object.freeze({");
        List<ScalarTypeDescription> scalarTypes = apiDescription.getScalarTypes();
        Iterator<ScalarTypeDescription> itScalarTypes = scalarTypes.iterator();
        while(itScalarTypes.hasNext())
        {
            ScalarTypeDescription scalarTypeDescription = (ScalarTypeDescription)itScalarTypes.next();
            out.println("    "+scalarTypeDescription.getName().toUpperCase()+": "+scalarTypeDescription.getNumericID()+(itScalarTypes.hasNext()?",":""));
        }
        out.println("});");
        out.flush();
    }

    protected void createConstantsFile(ApiDescription apiDescription) throws IOException
    {
        // create common names
        JavaScriptCommonNamesFileWriter constantsFileWriter = new JavaScriptCommonNamesFileWriter();
        constantsFileWriter.createConstantsFile(apiDescription, opendmaApiFOS, false);
    }

    //-------------------------------------------------------------------------
    // B A S I C   F I L E S
    //-------------------------------------------------------------------------

    protected void createQNameFile(ApiDescription apiDescription) throws IOException
    {
        copyClassToApi("OdmaQName");
    }

    protected void createIdFile(ApiDescription apiDescription) throws IOException
    {
        copyClassToApi("OdmaId");
    }

    protected void createGuidFile(ApiDescription apiDescription) throws IOException
    {
        copyClassToApi("OdmaGuid");
    }

    protected void createContentFile(ApiDescription apiDescription) throws IOException
    {
        copyClassToApi("OdmaContent");
    }

    protected void createSearchResultFile(ApiDescription apiDescription) throws IOException
    {
    }

    protected void createExceptionFiles(ApiDescription apiDescription) throws IOException
    {
        copyClassToApi("OdmaError");
        // OdmaRuntimeException: JavaScript does not distinguish between checked and unchecked Exceptions
        copyClassToApi("OdmaObjectNotFoundError");
        copyClassToApi("OdmaPropertyNotFoundError");
        copyClassToApi("OdmaInvalidDataTypeError");
        copyClassToApi("OdmaAccessDeniedError");
        copyClassToApi("OdmaQuerySyntaxError");
    }

    protected void createSessionManagementFiles(ApiDescription apiDescription) throws IOException
    {
    }

    //-------------------------------------------------------------------------
    // P R O P E R T Y   F I L E
    //-------------------------------------------------------------------------

    protected void createPropertyFile(ApiDescription apiDescription) throws IOException
    {
        JavaScriptPropertyFileWriter javaScriptPropertyFileWriter = new JavaScriptPropertyFileWriter(this);
        javaScriptPropertyFileWriter.createPropertyFile(apiDescription, opendmaApiFOS, false);
    }

    //-------------------------------------------------------------------------
    // C L A S S   F I L E
    //-------------------------------------------------------------------------

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
    
    private FileOutputStream opendmaApiFOS;
    
    protected void prepareProjectStructureAndBuildFiles(ApiDescription apiDescription) throws IOException
    {
        opendmaApiFOS = new FileOutputStream(new File(baseFolder, "opendma-api.js"));
        copyTemplateToStream("opendma-api-header", opendmaApiFOS, false);
    }
    
    protected void finaliseProjectStructureAndBuildFiles(ApiDescription apiDescription) throws IOException
    {
        opendmaApiFOS.flush();
        opendmaApiFOS.close();
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