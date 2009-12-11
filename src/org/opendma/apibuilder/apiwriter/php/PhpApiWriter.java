package org.opendma.apibuilder.apiwriter.php;

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

public class PhpApiWriter extends AbstractApiWriter
{
    
    private OutputStream createPhpFile(String outputFolder, String packageName, String className) throws IOException
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
        return new FileOutputStream(packageDirectory+(packageName.length()==0?"":File.separator)+className+".php");
    }
    
    protected String getProgrammingLanguageSpecificFolderName()
    {
        return "php";
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
                return "Integer";
            case OdmaBasicTypes.TYPE_SHORT:
                return "Short";
            case OdmaBasicTypes.TYPE_LONG:
                return "Long";
            case OdmaBasicTypes.TYPE_FLOAT:
                return "Float";
            case OdmaBasicTypes.TYPE_DOUBLE:
                return "Double";
            case OdmaBasicTypes.TYPE_BOOLEAN:
                return "Boolean";
            case OdmaBasicTypes.TYPE_DATETIME:
                return "Date";
            case OdmaBasicTypes.TYPE_BLOB:
                return "byte[]";
            case OdmaBasicTypes.TYPE_REFERENCE:
                throw new ApiCreationException("REFERENCE data type is not scalar");
            case OdmaBasicTypes.TYPE_CONTENT:
                return "OdmaContent";
            case OdmaBasicTypes.TYPE_ID:
                return "OdmaId";
            case OdmaBasicTypes.TYPE_GUID:
                return "OdmaGuid";
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
                return "org.opendma.api.collections.StringList";
            case OdmaBasicTypes.TYPE_INTEGER:
                return "org.opendma.api.collections.IntegerList";
            case OdmaBasicTypes.TYPE_SHORT:
                return "org.opendma.api.collections.ShortList";
            case OdmaBasicTypes.TYPE_LONG:
                return "org.opendma.api.collections.LongList";
            case OdmaBasicTypes.TYPE_FLOAT:
                return "org.opendma.api.collections.FloatList";
            case OdmaBasicTypes.TYPE_DOUBLE:
                return "org.opendma.api.collections.DoubleList";
            case OdmaBasicTypes.TYPE_BOOLEAN:
                return "org.opendma.api.collections.BooleanList";
            case OdmaBasicTypes.TYPE_DATETIME:
                return "org.opendma.api.collections.DateList";
            case OdmaBasicTypes.TYPE_BLOB:
                return "org.opendma.api.collections.BlobList";
            case OdmaBasicTypes.TYPE_REFERENCE:
                throw new ApiCreationException("REFERENCE data type is not scalar");
            case OdmaBasicTypes.TYPE_CONTENT:
                return "org.opendma.api.collections.OdmaContentList";
            case OdmaBasicTypes.TYPE_ID:
                return "org.opendma.api.collections.OdmaIdList";
            case OdmaBasicTypes.TYPE_GUID:
                return "org.opendma.api.collections.OdmaGuidList";
            case OdmaBasicTypes.TYPE_QNAME:
                return "org.opendma.api.collections.OdmaQNameList";
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
                return "java.util.Date";
            case OdmaBasicTypes.TYPE_BLOB:
                return null;
            case OdmaBasicTypes.TYPE_REFERENCE:
                throw new ApiCreationException("REFERENCE data type is not scalar");
            case OdmaBasicTypes.TYPE_CONTENT:
                return "org.opendma.api.OdmaContent";
            case OdmaBasicTypes.TYPE_ID:
                return "org.opendma.api.OdmaId";
            case OdmaBasicTypes.TYPE_GUID:
                return "org.opendma.api.OdmaGuid";
            case OdmaBasicTypes.TYPE_QNAME:
                return "org.opendma.api.OdmaQName";
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
        return createPhpFile(outputFolder,"OpenDMA","OdmaTypes");
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
        return createPhpFile(outputFolder,"OpenDMA/Api",classname);
    }

    protected void createQNameFile(ApiDescription apiDescription, String outputFolder) throws IOException
    {
        OutputStream to = getBasicFileStream("OdmaQName",outputFolder);
        InputStream from = getResourceAsStream("/templates/java/OdmaQName.template");
        streamCopy(from, to);
        from.close();
        to.close();
    }

    protected void createIdFile(ApiDescription apiDescription, String outputFolder) throws IOException
    {
        OutputStream to = getBasicFileStream("OdmaId",outputFolder);
        InputStream from = getResourceAsStream("/templates/java/OdmaId.template");
        streamCopy(from, to);
        from.close();
        to.close();
    }

    protected void createGuidFile(ApiDescription apiDescription, String outputFolder) throws IOException
    {
        OutputStream to = getBasicFileStream("OdmaGuid",outputFolder);
        InputStream from = getResourceAsStream("/templates/java/OdmaGuid.template");
        streamCopy(from, to);
        from.close();
        to.close();
    }

    protected void createContentFile(ApiDescription apiDescription, String outputFolder) throws IOException
    {
        OutputStream to = getBasicFileStream("OdmaContent",outputFolder);
        InputStream from = getResourceAsStream("/templates/java/OdmaContent.template");
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
        OutputStream to = createPhpFile(outputFolder,"OpenDMA/Exceptions",exceptionClassName);
        InputStream from = getResourceAsStream("/templates/php/"+exceptionClassName+".template");
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

    protected OutputStream getClassFileStream(String outputFolder, ClassDescription classDescription) throws IOException
    {
        return createPhpFile(outputFolder,"OpenDMA/Api",classDescription.getApiName());
    }

    protected void createClassFile(ClassDescription classDescription, String outputFolder) throws IOException
    {
        PhpClassFileWriter classFileWriter = new PhpClassFileWriter(this);
        classFileWriter.createClassFile(classDescription, getClassFileStream(outputFolder,classDescription));
    }
    
    //-------------------------------------------------------------------------
    // C O L L E C T I O N   F I L E S
    //-------------------------------------------------------------------------

    protected OutputStream getEnumerationFileStream(String baseFolder, ClassDescription classDescription) throws IOException
    {
        return createPhpFile(baseFolder,"OpenDMA/Api/Collections",classDescription.getApiName()+"Enumeration");
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
        return createPhpFile(baseFolder,"OpenDMA/Api/Collections",getProgrammingLanguageSpecificScalarDataType(true,scalarTypeDescription.getNumericID()));
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
        PhpPropertyImplementationFileWriter phpPropertyImplementationFileWriter = new PhpPropertyImplementationFileWriter(this);
        phpPropertyImplementationFileWriter.createPropertyFile(apiDescription, createPhpFile(outputFolder,"org.opendma.impl","OdmaProperty"));
        */
    }

    protected OutputStream getListImplementationFileStream(String baseFolder, ScalarTypeDescription scalarTypeDescription) throws IOException
    {
        return createPhpFile(baseFolder,"OpenDMA/Impl/Collections",getProgrammingLanguageSpecificScalarDataType(true,scalarTypeDescription.getNumericID()));
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

    protected OutputStream getClassTemplateFileStream(String outputFolder, ClassDescription classDescription) throws IOException
    {
        return createPhpFile(outputFolder,"OpenDMA/Templates",classDescription.getApiName());
    }

    protected void createClassTemplateFile(ClassDescription classDescription, String outputFolder) throws IOException
    {
        PhpClassTemplateFileWriter classTemplateFileWriter = new PhpClassTemplateFileWriter(this);
        classTemplateFileWriter.createClassFile(classDescription, getClassTemplateFileStream(outputFolder,classDescription));
    }
   
    //-------------------------------------------------------------------------
    // B U I L D   F I L E
    //-------------------------------------------------------------------------
    
    protected void createBuildFile(ApiDescription apiDescription, String baseFolder) throws IOException
    {
        // we do not create a build file for now. We use Eclipse.
    }

}