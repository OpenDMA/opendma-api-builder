package org.opendma.apibuilder.apiwriter;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.opendma.apibuilder.OdmaApiWriter;
import org.opendma.apibuilder.structure.ApiDescription;
import org.opendma.apibuilder.structure.ClassDescription;
import org.opendma.apibuilder.structure.ScalarTypeDescription;

public abstract class AbstractApiWriter implements OdmaApiWriter
{
    
    private final Properties scalarDataTypes = new Properties();
    
    protected AbstractApiWriter()
    {
        InputStream is = getClass().getResourceAsStream("scalarDataTypes.properties");
        if(is == null)
        {
            throw new RuntimeException("Resource 'scalarDataTypes.properties' not found in package of " + getClass().getName());
        }
        try
        {
            scalarDataTypes.load(is);
        }
        catch(IOException ioe)
        {
            throw new RuntimeException("Error loading resource 'scalarDataTypes.properties': " + ioe.getMessage(), ioe);
        }
        finally
        {
            try
            {
                is.close();
            }
            catch(IOException ioe)
            {
                throw new RuntimeException("Error closing stream: " + ioe.getMessage(), ioe);                
            }
        }
    }
    
    public String getScalarDataType(ScalarTypeDescription scalarTypeDescription, boolean multiValue)
    {
        if(scalarTypeDescription.isReference())
        {
            throw new ApiCreationException("REFERENCE data type is not scalar");
        }
        String key = scalarTypeDescription.getName()+"."+(multiValue?"multi":"single");
        String value = scalarDataTypes.getProperty(key);
        if(value == null || value.length() == 0)
        {
            throw new RuntimeException("Resource 'scalarDataTypes.properties' is missing key " + key);
        }
        return value;
    }
    
    public String[] getScalarDataTypeImports(ScalarTypeDescription scalarTypeDescription, boolean multiValue)
    {
        if(scalarTypeDescription.isReference())
        {
            throw new ApiCreationException("REFERENCE data type is not scalar");
        }
        String key = scalarTypeDescription.getName()+"."+(multiValue?"multi":"single")+".imports";
        String value = scalarDataTypes.getProperty(key);
        if(value == null)
        {
            throw new RuntimeException("Resource 'scalarDataTypes.properties' is missing key " + key);
        }
        return value.length() == 0 ? null : value.split(",");
    }

    protected abstract String getProgrammingLanguageSpecificFolderName();

    public void writeOdmaApi(ApiDescription apiDescription, String outputFolderRoot) throws IOException, ApiWriterException
    {
        // sanity checks
        if(apiDescription == null)
        {
            throw new NullPointerException("OdmaAbstractApiWriter.writeOdmaApi: apiDescription must not be null.");
        }
        if(outputFolderRoot == null)
        {
            throw new NullPointerException("OdmaAbstractApiWriter.writeOdmaApi: outputFolderRoot must not be null.");
        }
        // check root folder
        File outputFolderRootFile = new File(outputFolderRoot);
        if(!outputFolderRootFile.isDirectory())
        {
            throw new ApiWriterException("The output folder '"+outputFolderRoot+"' does not exist or is not a directory.");
        }
        // create base folder
        String baseFolder = outputFolderRoot;
        if(!baseFolder.endsWith(File.separator))
        {
            baseFolder = baseFolder + File.separator;
        }
        baseFolder = baseFolder + getProgrammingLanguageSpecificFolderName();
        File baseFolderFile = new File(baseFolder);
        if(!baseFolderFile.exists())
        {
            if(!baseFolderFile.mkdir())
            {
                throw new ApiWriterException("Can not create API specific folder '"+baseFolder+".");
            }
        }
        baseFolder = baseFolder + File.separator;
        // create the constants file
        createDataTypesFile(apiDescription,baseFolder);
        createConstantsFile(apiDescription,baseFolder);
        // create basic files that are NOT autocreated
        createQNameFile(apiDescription,baseFolder);
        createIdFile(apiDescription,baseFolder);
        createGuidFile(apiDescription,baseFolder);
        createContentFile(apiDescription,baseFolder);
        createSearchResultFile(apiDescription,baseFolder);
        createExceptionFiles(apiDescription,baseFolder);
        // create language depending files for the session management
        createSessionManagementFiles(apiDescription,baseFolder);
        // create the properties file
        createPropertyFile(apiDescription,baseFolder);
        // create class files
        List classes = apiDescription.getDescribedClasses();
        Iterator itClasses = classes.iterator();
        while(itClasses.hasNext())
        {
            ClassDescription classDescription = (ClassDescription)itClasses.next();
            createClassFile(classDescription,baseFolder);
        }
        // create collection files (Lists and Enumerations)
        itClasses = classes.iterator();
        while(itClasses.hasNext())
        {
            ClassDescription classDescription = (ClassDescription)itClasses.next();
            createEnumerationFile(classDescription,baseFolder);
        }
        List scalarTypes = apiDescription.getScalarTypes();
        Iterator itScalarTypes = scalarTypes.iterator();
        while(itScalarTypes.hasNext())
        {
            ScalarTypeDescription scalarTypeDescription = (ScalarTypeDescription)itScalarTypes.next();
            if(!scalarTypeDescription.isReference())
            {
                createListFile(scalarTypeDescription,baseFolder);
            }
        }
        // create properties implementation file
        createPropertyImplementationFile(apiDescription,baseFolder);
        // create list implementation files
        itScalarTypes = scalarTypes.iterator();
        while(itScalarTypes.hasNext())
        {
            ScalarTypeDescription scalarTypeDescription = (ScalarTypeDescription)itScalarTypes.next();
            if(!scalarTypeDescription.isReference())
            {
                createListImplementationFile(scalarTypeDescription,baseFolder);
            }
        }
        // create class template files
        itClasses = classes.iterator();
        while(itClasses.hasNext())
        {
            ClassDescription classDescription = (ClassDescription)itClasses.next();
            createClassTemplateFile(classDescription,baseFolder);
        }
        // create build file
        createBuildFile(apiDescription,baseFolder);
        // create any extras
        createExtras(apiDescription,baseFolder);
    }

    public static InputStream internalGetResourceAsStream(String resource)
    {
        String stripped = resource.startsWith("/") ? resource.substring(1) : resource;
        InputStream stream = null;
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        if(classLoader!=null)
        {
            stream = classLoader.getResourceAsStream(stripped);
        }
        if(stream == null)
        {
            stream = AbstractApiWriter.class.getResourceAsStream(resource);
        }
        if(stream == null)
        {
            stream = AbstractApiWriter.class.getClassLoader().getResourceAsStream(stripped);
        }
        if(stream == null)
        {
            throw new RuntimeException("Can not read resource " + resource);
        }
        return stream;
    }

    public InputStream getTemplateAsStream(String templateName)
    {
        return internalGetResourceAsStream("/templates/"+getProgrammingLanguageSpecificFolderName()+"/"+templateName+".template");
    }
    
    public static void streamCopy(InputStream from, OutputStream to) throws IOException
    {
        byte[] buffer = new byte[1024];
        int num = 0;
        while((num = from.read(buffer)) > 0)
        {
            to.write(buffer,0,num);
        }
    }

    //-------------------------------------------------------------------------
    // C O N S T A N T S   F I L E
    //-------------------------------------------------------------------------

    protected abstract void createDataTypesFile(ApiDescription apiDescription, String outputFolder) throws IOException;

    protected abstract void createConstantsFile(ApiDescription apiDescription, String outputFolder) throws IOException;

    //-------------------------------------------------------------------------
    // B A S I C   F I L E S
    //-------------------------------------------------------------------------

    protected abstract void createQNameFile(ApiDescription apiDescription, String outputFolder) throws IOException;

    protected abstract void createIdFile(ApiDescription apiDescription, String outputFolder) throws IOException;

    protected abstract void createGuidFile(ApiDescription apiDescription, String outputFolder) throws IOException;

    protected abstract void createContentFile(ApiDescription apiDescription, String outputFolder) throws IOException;

    protected abstract void createSearchResultFile(ApiDescription apiDescription, String outputFolder) throws IOException;

    protected abstract void createExceptionFiles(ApiDescription apiDescription, String outputFolder) throws IOException;

    protected abstract void createSessionManagementFiles(ApiDescription apiDescription, String outputFolder) throws IOException;

    //-------------------------------------------------------------------------
    // P R O P E R T Y   F I L E
    //-------------------------------------------------------------------------

    protected abstract void createPropertyFile(ApiDescription apiDescription, String outputFolder) throws IOException;

    //-------------------------------------------------------------------------
    // C L A S S   F I L E S
    //-------------------------------------------------------------------------
    
    protected abstract void createClassFile(ClassDescription classDescription, String outputFolder) throws IOException;
    
    //-------------------------------------------------------------------------
    // C O L L E C T I O N   F I L E S
    //-------------------------------------------------------------------------
    
    protected abstract void createEnumerationFile(ClassDescription classDescription, String outputFolder) throws IOException;

    protected abstract void createListFile(ScalarTypeDescription scalarTypeDescription, String baseFolder) throws IOException;
    
    //-------------------------------------------------------------------------
    // I M P L E M E N T A T I O N   F I L E S
    //-------------------------------------------------------------------------

    protected abstract void createPropertyImplementationFile(ApiDescription apiDescription, String outputFolder) throws IOException;

    protected abstract void createListImplementationFile(ScalarTypeDescription scalarTypeDescription, String baseFolder) throws IOException;

    //-------------------------------------------------------------------------
    // C L A S S   T E M P L A T E S
    //-------------------------------------------------------------------------
    
    protected abstract void createClassTemplateFile(ClassDescription classDescription, String outputFolder) throws IOException;
    
    //-------------------------------------------------------------------------
    // B U I L D   F I L E
    //-------------------------------------------------------------------------
    
    protected abstract void createBuildFile(ApiDescription apiDescription, String baseFolder) throws IOException;
    
    //-------------------------------------------------------------------------
    // E X T R A S
    //-------------------------------------------------------------------------
    
    protected void createExtras(ApiDescription apiDescription, String baseFolder) throws IOException, ApiWriterException
    {
    }

}