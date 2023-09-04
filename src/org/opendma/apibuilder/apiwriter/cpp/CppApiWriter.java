package org.opendma.apibuilder.apiwriter.cpp;

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

public class CppApiWriter extends AbstractApiWriter
{

    public String getName()
    {
        return "C++";
    }
    
    protected String getTargetFolderName()
    {
        return "cpp";
    }

    //-------------------------------------------------------------------------
    // S O U R C E   F I L E   H E L P E R
    //-------------------------------------------------------------------------
    
    private OutputStream createHeaderFile(String outputFolder, String headerName) throws IOException
    {
        return new FileOutputStream(outputFolder+headerName+".h");
    }
    
    private void createHeaderFromTemplate(String outputFolder, String headerName) throws IOException
    {
        copyTemplateToStream(headerName,createHeaderFile(outputFolder,headerName));
    }

    //-------------------------------------------------------------------------
    // C O N S T A N T S   F I L E
    //-------------------------------------------------------------------------

    protected void createDataTypesFile(ApiDescription apiDescription, String outputFolder) throws IOException
    {
        // create type enumeration
        PrintWriter out = new PrintWriter(createHeaderFile(outputFolder,"OdmaType"));
        out.println("#ifndef _OdmaType_h_");
        out.println("#define _OdmaType_h_");
        out.println("");
        out.println("enum OdmaType");
        out.println("{");
        List scalarTypes = apiDescription.getScalarTypes();
        Iterator itScalarTypes = scalarTypes.iterator();
        while(itScalarTypes.hasNext())
        {
            ScalarTypeDescription scalarTypeDescription = (ScalarTypeDescription)itScalarTypes.next();
            out.println("        "+scalarTypeDescription.getName().toUpperCase()+" = "+scalarTypeDescription.getNumericID()+(itScalarTypes.hasNext()?",":""));
        }
        out.println("}");
        out.println("");
        out.println("#endif");
        out.close();
    }

    protected void createConstantsFile(ApiDescription apiDescription, String outputFolder) throws IOException
    {
        // create common names file
        CppConstantsFileWriter constantsFileWriter = new CppConstantsFileWriter();
        constantsFileWriter.createConstantsFile(apiDescription, createHeaderFile(outputFolder,"OdmaCommonNames"));
    }

    //-------------------------------------------------------------------------
    // B A S I C   F I L E S
    //-------------------------------------------------------------------------

    protected void createQNameFile(ApiDescription apiDescription, String outputFolder) throws IOException
    {
        createHeaderFromTemplate(outputFolder,"OdmaQName");
    }

    protected void createIdFile(ApiDescription apiDescription, String outputFolder) throws IOException
    {
        createHeaderFromTemplate(outputFolder,"OdmaId");
    }

    protected void createGuidFile(ApiDescription apiDescription, String outputFolder) throws IOException
    {
        createHeaderFromTemplate(outputFolder,"OdmaGuid");
    }

    protected void createContentFile(ApiDescription apiDescription, String outputFolder) throws IOException
    {
        createHeaderFromTemplate(outputFolder,"OdmaContent");
    }

    protected void createSearchResultFile(ApiDescription apiDescription, String outputFolder) throws IOException
    {
        createHeaderFromTemplate(outputFolder,"OdmaSearchResult");
    }

    protected void createExceptionFiles(ApiDescription apiDescription, String outputFolder) throws IOException
    {
        createHeaderFromTemplate(outputFolder,"OdmaException");
        createHeaderFromTemplate(outputFolder,"OdmaObjectNotFoundException");
        createHeaderFromTemplate(outputFolder,"OdmaInvalidDataTypeException");
        createHeaderFromTemplate(outputFolder,"OdmaAccessDeniedException");
        createHeaderFromTemplate(outputFolder,"OdmaQuerySyntaxException");
        createHeaderFromTemplate(outputFolder,"OdmaSearchException");
    }

    protected void createSessionManagementFiles(ApiDescription apiDescription, String outputFolder) throws IOException
    {
        createHeaderFromTemplate(outputFolder,"OdmaDataSource");
        createHeaderFromTemplate(outputFolder,"OdmaSession");
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
        CppClassFileWriter classFileWriter = new CppClassFileWriter(this);
        classFileWriter.createClassFile(classDescription, createHeaderFile(outputFolder, classDescription.getApiName()));
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
    }
   
    //-------------------------------------------------------------------------
    // B U I L D   F I L E
    //-------------------------------------------------------------------------
    
    protected void createBuildFile(ApiDescription apiDescription, String baseFolder) throws IOException
    {
        // we do not create a build file for now. We use Eclipse.
    }

}