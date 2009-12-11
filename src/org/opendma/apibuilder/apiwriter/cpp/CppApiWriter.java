package org.opendma.apibuilder.apiwriter.cpp;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.opendma.apibuilder.OdmaBasicTypes;
import org.opendma.apibuilder.apiwriter.AbstractApiWriter;
import org.opendma.apibuilder.apiwriter.ApiCreationException;
import org.opendma.apibuilder.structure.ApiDescription;
import org.opendma.apibuilder.structure.ClassDescription;
import org.opendma.apibuilder.structure.ScalarTypeDescription;

public class CppApiWriter extends AbstractApiWriter
{
    
    private OutputStream createJavaFile(String outputFolder, String packageName, String className) throws IOException
    {
        String packageDirectory = outputFolder + packageName.replace('.',File.separatorChar);
        File containingDir = new File(packageDirectory);
        if(!containingDir.exists())
        {
            if(!containingDir.mkdirs())
            {
                throw new IOException("Can not create package directory");
            }
        }
        return new FileOutputStream(packageDirectory+File.separator+className+".java");
    }
    
    private OutputStream createHeaderFile(String outputFolder, String headerName) throws IOException
    {
        return new FileOutputStream(outputFolder+headerName+".h");
    }
    
    private OutputStream createCppFile(String outputFolder, String headerName) throws IOException
    {
        return new FileOutputStream(outputFolder+headerName+".cpp");
    }
    
    protected String getProgrammingLanguageSpecificFolderName()
    {
        return "cpp";
    }
    
    public String getProgrammingLanguageSpecificScalarDataType(boolean multiValue, int dataType)
    {
        if(multiValue)
        {
            switch(dataType)
            {
            case OdmaBasicTypes.TYPE_STRING:
                return "StringList";
            case OdmaBasicTypes.TYPE_INTEGER:
                return "IntegerList";
            case OdmaBasicTypes.TYPE_SHORT:
                return "ShortList";
            case OdmaBasicTypes.TYPE_LONG:
                return "LongList";
            case OdmaBasicTypes.TYPE_FLOAT:
                return "FloatList";
            case OdmaBasicTypes.TYPE_DOUBLE:
                return "DoubleList";
            case OdmaBasicTypes.TYPE_BOOLEAN:
                return "BooleanList";
            case OdmaBasicTypes.TYPE_DATETIME:
                return "DateTimeList";
            case OdmaBasicTypes.TYPE_BLOB:
                return "BlobList";
            case OdmaBasicTypes.TYPE_REFERENCE:
                throw new ApiCreationException("REFERENCE data type is not scalar");
            case OdmaBasicTypes.TYPE_CONTENT:
                return "OdmaContentList";
            case OdmaBasicTypes.TYPE_ID:
                return "OdmaIdList";
            case OdmaBasicTypes.TYPE_GUID:
                return "OdmaGuidList";
            case OdmaBasicTypes.TYPE_QNAME:
                return "OdmaQNameList";
            default:
                throw new ApiCreationException("Unhandled data type "+dataType);
            }
        }
        else
        {
            switch(dataType)
            {
            case OdmaBasicTypes.TYPE_STRING:
                return "String";
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
                return "time_t";
            case OdmaBasicTypes.TYPE_BLOB:
                return "byte[]";
            case OdmaBasicTypes.TYPE_REFERENCE:
                throw new ApiCreationException("REFERENCE data type is not scalar");
            case OdmaBasicTypes.TYPE_CONTENT:
                return "OdmaContent*";
            case OdmaBasicTypes.TYPE_ID:
                return "OdmaId*";
            case OdmaBasicTypes.TYPE_GUID:
                return "OdmaGuid*";
            case OdmaBasicTypes.TYPE_QNAME:
                return "OdmaQName*";
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
                return "\"collections/StringList.h\"";
            case OdmaBasicTypes.TYPE_INTEGER:
                return "\"collections/IntegerList.h\"";
            case OdmaBasicTypes.TYPE_SHORT:
                return "\"collections/ShortList.h\"";
            case OdmaBasicTypes.TYPE_LONG:
                return "\"collections/LongList.h\"";
            case OdmaBasicTypes.TYPE_FLOAT:
                return "\"collections/FloatList.h\"";
            case OdmaBasicTypes.TYPE_DOUBLE:
                return "\"collections/DoubleList.h\"";
            case OdmaBasicTypes.TYPE_BOOLEAN:
                return "\"collections/BooleanList.h\"";
            case OdmaBasicTypes.TYPE_DATETIME:
                return "\"collections/DateList.h\"";
            case OdmaBasicTypes.TYPE_BLOB:
                return "\"collections/BlobList.h\"";
            case OdmaBasicTypes.TYPE_REFERENCE:
                throw new ApiCreationException("REFERENCE data type is not scalar");
            case OdmaBasicTypes.TYPE_CONTENT:
                return "\"collections/OdmaContentList.h\"";
            case OdmaBasicTypes.TYPE_ID:
                return "\"collections/OdmaIdList.h\"";
            case OdmaBasicTypes.TYPE_GUID:
                return "\"collections/OdmaGuidList.h\"";
            case OdmaBasicTypes.TYPE_QNAME:
                return "\"collections/OdmaQNameList.h\"";
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
                return "<time>";
            case OdmaBasicTypes.TYPE_BLOB:
                return null;
            case OdmaBasicTypes.TYPE_REFERENCE:
                throw new ApiCreationException("REFERENCE data type is not scalar");
            case OdmaBasicTypes.TYPE_CONTENT:
                return "\"OdmaContent.h\"";
            case OdmaBasicTypes.TYPE_ID:
                return "\"OdmaId.h\"";
            case OdmaBasicTypes.TYPE_GUID:
                return "\"OdmaGuid.h\"";
            case OdmaBasicTypes.TYPE_QNAME:
                return "\"OdmaQName.h\"";
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
        return createJavaFile(outputFolder,"org.opendma","OdmaTypes");
    }

    protected void createConstantsFile(ApiDescription apiDescription, String outputFolder) throws IOException
    {
        /*
        JavaConstantsFileWriter constantsFileWriter = new JavaConstantsFileWriter();
        constantsFileWriter.createConstantsFile(apiDescription, getConstantsFileStream(outputFolder));
        */
    }

    //-------------------------------------------------------------------------
    // B A S I C   F I L E S
    //-------------------------------------------------------------------------

    protected OutputStream getBasicFileStream(String classname, String outputFolder) throws IOException
    {
        return createJavaFile(outputFolder,"org.opendma.api",classname);
    }

    protected void createQNameFile(ApiDescription apiDescription, String outputFolder) throws IOException
    {
        OutputStream to = createHeaderFile(outputFolder,"OdmaQName");
        InputStream from = getResourceAsStream("/templates/cpp/OdmaQName.template");
        streamCopy(from, to);
        from.close();
        to.close();
    }

    protected void createIdFile(ApiDescription apiDescription, String outputFolder) throws IOException
    {
        OutputStream to = createHeaderFile(outputFolder,"OdmaId");
        InputStream from = getResourceAsStream("/templates/cpp/OdmaId.template");
        streamCopy(from, to);
        from.close();
        to.close();
    }

    protected void createGuidFile(ApiDescription apiDescription, String outputFolder) throws IOException
    {
        OutputStream to = createHeaderFile(outputFolder,"OdmaGuid");
        InputStream from = getResourceAsStream("/templates/cpp/OdmaGuid.template");
        streamCopy(from, to);
        from.close();
        to.close();
    }

    protected void createContentFile(ApiDescription apiDescription, String outputFolder) throws IOException
    {
        OutputStream to = createHeaderFile(outputFolder,"OdmaContent");
        InputStream from = getResourceAsStream("/templates/cpp/OdmaContent.template");
        streamCopy(from, to);
        from.close();
        to.close();
    }

    protected void createExceptionFiles(ApiDescription apiDescription, String outputFolder) throws IOException
    {
        internalCreateExceptionFile(outputFolder,"OdmaObjectNotFoundException");
        internalCreateExceptionFile(outputFolder,"OdmaInvalidDataTypeException");
        internalCreateExceptionFile(outputFolder,"OdmaAccessDeniedException");
    }
    
    protected void internalCreateExceptionFile(String outputFolder, String exceptionClassName) throws IOException
    {
        OutputStream to = createHeaderFile(outputFolder,exceptionClassName);
        InputStream from = getResourceAsStream("/templates/cpp/"+exceptionClassName+".template");
        streamCopy(from,to);
        from.close();
        to.close();
    }

    //-------------------------------------------------------------------------
    // P R O P E R T Y   F I L E
    //-------------------------------------------------------------------------

    protected void createPropertyFile(ApiDescription apiDescription, String outputFolder) throws IOException
    {
        /*
        JavaPropertyFileWriter javaPropertyFileWriter = new JavaPropertyFileWriter(this);
        javaPropertyFileWriter.createPropertyFile(apiDescription, getBasicFileStream("OdmaProperty",outputFolder));
        */
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

    protected OutputStream getEnumerationFileStream(String baseFolder, ClassDescription classDescription) throws IOException
    {
        return createJavaFile(baseFolder,"org.opendma.api.collections",classDescription.getApiName()+"Enumeration");
    }
    
    protected void createEnumerationFile(ClassDescription classDescription, String baseFolder) throws IOException
    {
        /*
        JavaEnumerationFileWriter enumerationFileWriter = new JavaEnumerationFileWriter();
        enumerationFileWriter.createEnumerationFile(classDescription, getEnumerationFileStream(baseFolder,classDescription));
        */
    }

    protected OutputStream getListFileStream(String baseFolder, ScalarTypeDescription scalarTypeDescription) throws IOException
    {
        return createJavaFile(baseFolder,"org.opendma.api.collections",getProgrammingLanguageSpecificScalarDataType(true,scalarTypeDescription.getNumericID()));
    }

    protected void createListFile(ScalarTypeDescription scalarTypeDescription, String baseFolder) throws IOException
    {
        /*
        JavaListFileWriter listFileWriter = new JavaListFileWriter(this);
        listFileWriter.createListFile(scalarTypeDescription, getListFileStream(baseFolder,scalarTypeDescription));
        */
    }
    
    //-------------------------------------------------------------------------
    // I M P L E M E N T A T I O N   F I L E S
    //-------------------------------------------------------------------------

    protected void createPropertyImplementationFile(ApiDescription apiDescription, String outputFolder) throws IOException
    {
        /*
        CppPropertyImplementationFileWriter cppPropertyImplementationFileWriter = new CppPropertyImplementationFileWriter(this);
        cppPropertyImplementationFileWriter.createPropertyFile(apiDescription, createCppFile(outputFolder,"org.opendma.impl","OdmaProperty"));
        */
    }

    protected OutputStream getListImplementationFileStream(String baseFolder, ScalarTypeDescription scalarTypeDescription) throws IOException
    {
        return createJavaFile(baseFolder,"org.opendma.impl.collections",getProgrammingLanguageSpecificScalarDataType(true,scalarTypeDescription.getNumericID()));
    }

    protected void createListImplementationFile(ScalarTypeDescription scalarTypeDescription, String baseFolder) throws IOException
    {
        /*
        JavaListFileWriter listFileWriter = new JavaListFileWriter(this);
        listFileWriter.createListFile(scalarTypeDescription, getListImplementationFileStream(baseFolder,scalarTypeDescription));
        */
    }

    //-------------------------------------------------------------------------
    // C L A S S   T E M P L A T E S
    //-------------------------------------------------------------------------

    protected void createClassTemplateFile(ClassDescription classDescription, String outputFolder) throws IOException
    {
        CppClassTemplateFileWriter classTemplateFileWriter = new CppClassTemplateFileWriter(this);
        classTemplateFileWriter.createClassFile(classDescription, createCppFile(outputFolder, classDescription.getApiName()));
    }
   
    //-------------------------------------------------------------------------
    // B U I L D   F I L E
    //-------------------------------------------------------------------------
    
    protected void createBuildFile(ApiDescription apiDescription, String baseFolder) throws IOException
    {
        // we do not create a build file for now. We use Eclipse.
    }

}