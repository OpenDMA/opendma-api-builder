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

import org.opendma.apibuilder.OdmaBasicTypes;
import org.opendma.apibuilder.apiwriter.AbstractApiWriter;
import org.opendma.apibuilder.apiwriter.ApiCreationException;
import org.opendma.apibuilder.structure.ApiDescription;
import org.opendma.apibuilder.structure.ClassDescription;
import org.opendma.apibuilder.structure.ScalarTypeDescription;

public class CsApiWriter extends AbstractApiWriter
{
    
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
    
    public String getProgrammingLanguageSpecificScalarDataType(boolean multiValue, int dataType)
    {
        if(multiValue)
        {
            switch(dataType)
            {
            case OdmaBasicTypes.TYPE_STRING:
                return "IStringList";
            case OdmaBasicTypes.TYPE_INTEGER:
                return "IIntegerList";
            case OdmaBasicTypes.TYPE_SHORT:
                return "IShortList";
            case OdmaBasicTypes.TYPE_LONG:
                return "ILongList";
            case OdmaBasicTypes.TYPE_FLOAT:
                return "IFloatList";
            case OdmaBasicTypes.TYPE_DOUBLE:
                return "IDoubleList";
            case OdmaBasicTypes.TYPE_BOOLEAN:
                return "IBooleanList";
            case OdmaBasicTypes.TYPE_DATETIME:
                return "IDateTimeList";
            case OdmaBasicTypes.TYPE_BLOB:
                return "IBlobList";
            case OdmaBasicTypes.TYPE_REFERENCE:
                throw new ApiCreationException("REFERENCE data type is not scalar");
            case OdmaBasicTypes.TYPE_CONTENT:
                return "IOdmaContentList";
            case OdmaBasicTypes.TYPE_ID:
                return "IOdmaIdList";
            case OdmaBasicTypes.TYPE_GUID:
                return "IOdmaGuidList";
            case OdmaBasicTypes.TYPE_QNAME:
                return "IOdmaQNameList";
            default:
                throw new ApiCreationException("Unhandled data type "+dataType);
            }
        }
        else
        {
            switch(dataType)
            {
            case OdmaBasicTypes.TYPE_STRING:
                return "string";
            case OdmaBasicTypes.TYPE_INTEGER:
                return "int";
            case OdmaBasicTypes.TYPE_SHORT:
                return "short";
            case OdmaBasicTypes.TYPE_LONG:
                return "long";
            case OdmaBasicTypes.TYPE_FLOAT:
                return "float";
            case OdmaBasicTypes.TYPE_DOUBLE:
                return "double";
            case OdmaBasicTypes.TYPE_BOOLEAN:
                return "bool";
            case OdmaBasicTypes.TYPE_DATETIME:
                return "DateTime";
            case OdmaBasicTypes.TYPE_BLOB:
                return "byte[]";
            case OdmaBasicTypes.TYPE_REFERENCE:
                throw new ApiCreationException("REFERENCE data type is not scalar");
            case OdmaBasicTypes.TYPE_CONTENT:
                return "IOdmaContent";
            case OdmaBasicTypes.TYPE_ID:
                return "IOdmaId";
            case OdmaBasicTypes.TYPE_GUID:
                return "IOdmaGuid";
            case OdmaBasicTypes.TYPE_QNAME:
                return "OdmaQName";
            default:
                throw new ApiCreationException("Unhandled data type "+dataType);
            }
        }
    }

    public String getRequiredScalarDataTypeImport(boolean multiValue, int dataType)
    {
        if(multiValue)
        {
            switch(dataType)
            {
            case OdmaBasicTypes.TYPE_STRING:
            case OdmaBasicTypes.TYPE_INTEGER:
            case OdmaBasicTypes.TYPE_SHORT:
            case OdmaBasicTypes.TYPE_LONG:
            case OdmaBasicTypes.TYPE_FLOAT:
            case OdmaBasicTypes.TYPE_DOUBLE:
            case OdmaBasicTypes.TYPE_BOOLEAN:
            case OdmaBasicTypes.TYPE_DATETIME:
            case OdmaBasicTypes.TYPE_BLOB:
            case OdmaBasicTypes.TYPE_CONTENT:
            case OdmaBasicTypes.TYPE_ID:
            case OdmaBasicTypes.TYPE_GUID:
            case OdmaBasicTypes.TYPE_QNAME:
                return "OpenDMA.Api.Collections";
            case OdmaBasicTypes.TYPE_REFERENCE:
                throw new ApiCreationException("REFERENCE data type is not scalar");
            default:
                throw new ApiCreationException("Unhandled data type "+dataType);
            }
        }
        else
        {
            switch(dataType)
            {
            case OdmaBasicTypes.TYPE_STRING:
                return null;
            case OdmaBasicTypes.TYPE_INTEGER:
                return null;
            case OdmaBasicTypes.TYPE_SHORT:
                return null;
            case OdmaBasicTypes.TYPE_LONG:
                return null;
            case OdmaBasicTypes.TYPE_FLOAT:
                return null;
            case OdmaBasicTypes.TYPE_DOUBLE:
                return null;
            case OdmaBasicTypes.TYPE_BOOLEAN:
                return null;
            case OdmaBasicTypes.TYPE_DATETIME:
                return null;
            case OdmaBasicTypes.TYPE_BLOB:
                return null;
            case OdmaBasicTypes.TYPE_REFERENCE:
                throw new ApiCreationException("REFERENCE data type is not scalar");
            case OdmaBasicTypes.TYPE_CONTENT:
                return "OpenDMA.Api";
            case OdmaBasicTypes.TYPE_ID:
                return "OpenDMA.Api";
            case OdmaBasicTypes.TYPE_GUID:
                return "OpenDMA.Api";
            case OdmaBasicTypes.TYPE_QNAME:
                return "OpenDMA.Api";
            default:
                throw new ApiCreationException("Unhandled data type "+dataType);
            }
        }
    }

    //-------------------------------------------------------------------------
    // C O N S T A N T S   F I L E
    //-------------------------------------------------------------------------

    protected OutputStream getConstantsFileStream(String outputFolder) throws IOException
    {
        return createCsFile(outputFolder,"OpenDMA","OdmaTypes");
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
        InputStream from = getResourceAsStream("/templates/cs/OdmaQName.template");
        streamCopy(from, to);
        from.close();
        to.close();
    }

    protected void createIdFile(ApiDescription apiDescription, String outputFolder) throws IOException
    {
        OutputStream to = getBasicFileStream("IOdmaId",outputFolder);
        InputStream from = getResourceAsStream("/templates/cs/IOdmaId.template");
        streamCopy(from, to);
        from.close();
        to.close();
    }

    protected void createGuidFile(ApiDescription apiDescription, String outputFolder) throws IOException
    {
        OutputStream to = getBasicFileStream("IOdmaGuid",outputFolder);
        InputStream from = getResourceAsStream("/templates/cs/IOdmaGuid.template");
        streamCopy(from, to);
        from.close();
        to.close();
    }

    protected void createContentFile(ApiDescription apiDescription, String outputFolder) throws IOException
    {
        OutputStream to = getBasicFileStream("IOdmaContent",outputFolder);
        InputStream from = getResourceAsStream("/templates/cs/IOdmaContent.template");
        streamCopy(from, to);
        from.close();
        to.close();
    }

    protected void createExceptionFiles(ApiDescription apiDescription, String outputFolder) throws IOException
    {
        internalCreateExceptionFile(outputFolder,"OdmaObjectNotFoundException");
        internalCreateExceptionFile(outputFolder,"OdmaInvalidDataTypeException");
    }
    
    protected void internalCreateExceptionFile(String outputFolder, String exceptionClassName) throws IOException
    {
        OutputStream to = createCsFile(outputFolder,"OpenDMA.Exceptions",exceptionClassName);
        InputStream from = getResourceAsStream("/templates/cs/"+exceptionClassName+".template");
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
        return createCsFile(baseFolder,"OpenDMA.Api.Collections","I"+classDescription.getApiName()+"Enumeration");
    }
    
    protected void createEnumerationFile(ClassDescription classDescription, String baseFolder) throws IOException
    {
        CsEnumerationFileWriter enumerationFileWriter = new CsEnumerationFileWriter();
        enumerationFileWriter.createEnumerationFile(classDescription, getEnumerationFileStream(baseFolder,classDescription));
    }

    protected OutputStream getListFileStream(String baseFolder, ScalarTypeDescription scalarTypeDescription) throws IOException
    {
        return createCsFile(baseFolder,"OpenDMA.Api.Collections","I"+getProgrammingLanguageSpecificScalarDataType(true,scalarTypeDescription.getNumericID()));
    }

    protected void createListFile(ScalarTypeDescription scalarTypeDescription, String baseFolder) throws IOException
    {
        CsListFileWriter listFileWriter = new CsListFileWriter(this);
        listFileWriter.createListFile(scalarTypeDescription, getListFileStream(baseFolder,scalarTypeDescription));
    }

    protected void createBuildFile(ApiDescription apiDescription, String baseFolder) throws IOException
    {
        OutputStream to = new FileOutputStream(baseFolder+"OpenDMA.Api.csproj");
        InputStream headerFrom = getResourceAsStream("/templates/cs/CsprojFileHeader.template");
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
        InputStream footerFrom = getResourceAsStream("/templates/cs/CsprojFileFoot.template");
        streamCopy(footerFrom, to);
        footerFrom.close();
        to.close();
        // create Properties file
        to = createCsFile(baseFolder,"Properties","AssemblyInfo");
        InputStream assemblyInfoFrom = getResourceAsStream("/templates/cs/AssemblyInfo.template");
        streamCopy(assemblyInfoFrom, to);
        assemblyInfoFrom.close();
        to.close();
   }

}