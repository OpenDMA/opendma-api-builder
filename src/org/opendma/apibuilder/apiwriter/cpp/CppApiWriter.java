package org.opendma.apibuilder.apiwriter.cpp;

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

public class CppApiWriter extends AbstractApiWriter
{

    public CppApiWriter(File outputFolderRoot) throws ApiWriterException
    {
        super(outputFolderRoot);
        // TODO Auto-generated constructor stub
    }

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
    
    private OutputStream createHeaderFile(File targetFolder, String headerName) throws IOException
    {
        return new FileOutputStream(new File(targetFolder, headerName+".h"));
    }
    
    private void createHeaderFromTemplate(File targetFolder, String headerName) throws IOException
    {
        copyTemplateToStream(headerName,createHeaderFile(targetFolder,headerName));
    }

    //-------------------------------------------------------------------------
    // C O N S T A N T S   F I L E
    //-------------------------------------------------------------------------

    protected void createDataTypesFile(ApiDescription apiDescription) throws IOException
    {
        // create type enumeration
        PrintWriter out = new PrintWriter(createHeaderFile(includeFolder,"OdmaType"));
        out.println("#ifndef _OdmaType_h_");
        out.println("#define _OdmaType_h_");
        out.println("");
        out.println("enum class OdmaType : int");
        out.println("{");
        List<ScalarTypeDescription> scalarTypes = apiDescription.getScalarTypes();
        Iterator<ScalarTypeDescription> itScalarTypes = scalarTypes.iterator();
        while(itScalarTypes.hasNext())
        {
            ScalarTypeDescription scalarTypeDescription = itScalarTypes.next();
            out.println("    "+scalarTypeDescription.getName().toUpperCase()+" = "+scalarTypeDescription.getNumericID()+(itScalarTypes.hasNext()?",":""));
        }
        out.println("}");
        out.println("");
        out.println("int numericIdFromOdmaType(OdmaType odmaType) {");
        out.println("    return static_cast<int>(odmaType);");
        out.println("}");
        out.println("");
        out.println("OdmaType odmaTypeFromNumericId(int numericId) {");
        out.println("    switch (numericId) {");
        itScalarTypes = scalarTypes.iterator();
        while(itScalarTypes.hasNext())
        {
            ScalarTypeDescription scalarTypeDescription = (ScalarTypeDescription)itScalarTypes.next();
            out.println("        case "+scalarTypeDescription.getNumericID()+":");
            out.println("            return OdmaType::"+scalarTypeDescription.getName().toUpperCase()+";");
        }
        out.println("        default:");
        out.println("            throw std::invalid_argument(\"Unknown numericId \" + std::to_string(numericId));");
        out.println("    }");
        out.println("}");
        out.println("");
        out.println("#endif");
        out.close();
    }

    protected void createConstantsFile(ApiDescription apiDescription) throws IOException
    {
        // create common names file
        CppCommonNamesFileWriter constantsFileWriter = new CppCommonNamesFileWriter();
        constantsFileWriter.createConstantsFile(apiDescription, createHeaderFile(includeFolder,"OdmaCommonNames"));
    }

    //-------------------------------------------------------------------------
    // B A S I C   F I L E S
    //-------------------------------------------------------------------------

    protected void createQNameFile(ApiDescription apiDescription) throws IOException
    {
        createHeaderFromTemplate(includeFolder, "OdmaQName");
    }

    protected void createIdFile(ApiDescription apiDescription) throws IOException
    {
        createHeaderFromTemplate(includeFolder,"OdmaId");
    }

    protected void createGuidFile(ApiDescription apiDescription) throws IOException
    {
        createHeaderFromTemplate(includeFolder,"OdmaGuid");
    }

    protected void createContentFile(ApiDescription apiDescription) throws IOException
    {
        createHeaderFromTemplate(includeFolder,"OdmaContent");
    }

    protected void createSearchResultFile(ApiDescription apiDescription) throws IOException
    {
        createHeaderFromTemplate(includeFolder,"OdmaSearchResult");
    }

    protected void createExceptionFiles(ApiDescription apiDescription) throws IOException
    {
        createHeaderFromTemplate(includeFolder,"OdmaException");
        // OdmaRuntimeException: C++ does not distinguish between checked and unchecked Exceptions
        createHeaderFromTemplate(includeFolder,"OdmaObjectNotFoundException");
        createHeaderFromTemplate(includeFolder,"OdmaPropertyNotFoundException");
        createHeaderFromTemplate(includeFolder,"OdmaInvalidDataTypeException");
        createHeaderFromTemplate(includeFolder,"OdmaAccessDeniedException");
        createHeaderFromTemplate(includeFolder,"OdmaQuerySyntaxException");
    }

    protected void createSessionManagementFiles(ApiDescription apiDescription) throws IOException
    {
//        createHeaderFromTemplate(outputFolder,"OdmaDataSource");
//        createHeaderFromTemplate(outputFolder,"OdmaSession");
    }

    //-------------------------------------------------------------------------
    // P R O P E R T Y   F I L E
    //-------------------------------------------------------------------------

    protected void createPropertyFile(ApiDescription apiDescription) throws IOException
    {
        CppPropertyFileWriter cppPropertyFileWriter = new CppPropertyFileWriter(this);
        cppPropertyFileWriter.createPropertyFile(apiDescription, createHeaderFile(includeFolder,"OdmaProperty"));
    }

    //-------------------------------------------------------------------------
    // C L A S S   F I L E
    //-------------------------------------------------------------------------

    protected void createClassFile(ClassDescription classDescription) throws IOException
    {
        CppObjectsInterfaceFileWriter classFileWriter = new CppObjectsInterfaceFileWriter(this);
        classFileWriter.createClassFile(classDescription, createHeaderFile(includeFolder, classDescription.getApiName()));
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
    
    private File includeFolder;
    
    private File templatesFolder;
    
    protected void prepareProjectStructureAndBuildFiles(ApiDescription apiDescription) throws IOException
    {
        includeFolder = new File(baseFolder, "include");
        includeFolder.mkdirs();
        templatesFolder = new File(baseFolder, "templates");
        templatesFolder.mkdirs();
    }
    
    protected void finaliseProjectStructureAndBuildFiles(ApiDescription apiDescription) throws IOException
    {
    }

}