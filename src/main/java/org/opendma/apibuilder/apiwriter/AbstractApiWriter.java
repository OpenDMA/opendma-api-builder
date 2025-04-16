package org.opendma.apibuilder.apiwriter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
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
    
    protected File baseFolder;
    
    public AbstractApiWriter(File outputFolderRoot) throws ApiWriterException
    {
        // output target
        if(outputFolderRoot == null)
        {
            throw new NullPointerException("AbstractApiWriter: outputFolderRoot must not be null.");
        }
        // check root folder
        if(!outputFolderRoot.isDirectory())
        {
            throw new ApiWriterException("The output folder '"+outputFolderRoot+"' does not exist or is not a directory.");
        }
        // create base folder
        baseFolder = new File(outputFolderRoot, getTargetFolderName());
        if(!baseFolder.exists())
        {
            if(!baseFolder.mkdir())
            {
                throw new ApiWriterException("Can not create API specific folder '"+baseFolder+".");
            }
        }
        // initialise scalarDataTypes
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
    
    public String getScalarDataType(ScalarTypeDescription scalarTypeDescription, boolean multiValue, boolean notNull)
    {
        if(scalarTypeDescription.isReference())
        {
            throw new ApiCreationException("REFERENCE data type is not scalar");
        }
        String key = scalarTypeDescription.getName()+"."+(multiValue?"multi":"single");
        String nullabilitySuffix = notNull ? ".notNull" : ".nullable";
        String value = scalarDataTypes.getProperty(key+nullabilitySuffix) != null ? scalarDataTypes.getProperty(key+nullabilitySuffix) : scalarDataTypes.getProperty(key);
        if(value == null || value.length() == 0)
        {
            throw new RuntimeException("Resource 'scalarDataTypes.properties' is missing key " + key + " or key " + key+nullabilitySuffix);
        }
        return value;
    }
    
    public String[] getScalarDataTypeImports(ScalarTypeDescription scalarTypeDescription, boolean multiValue, boolean notNull)
    {
        if(scalarTypeDescription.isReference())
        {
            throw new ApiCreationException("REFERENCE data type is not scalar");
        }
        String key = scalarTypeDescription.getName()+"."+(multiValue?"multi":"single");
        String nullabilitySuffix = notNull ? ".notNull" : ".nullable";
        String value = scalarDataTypes.getProperty(key+nullabilitySuffix+".imports") != null ? scalarDataTypes.getProperty(key+nullabilitySuffix+".imports") : scalarDataTypes.getProperty(key+".imports");
        if(value == null)
        {
            throw new RuntimeException("Resource 'scalarDataTypes.properties' is missing key " + key+".imports or key "+key+nullabilitySuffix+".imports");
        }
        return value.length() == 0 ? null : value.split(",");
    }

    protected abstract String getTargetFolderName();

    public void writeOdmaApi(ApiDescription apiDescription) throws IOException, ApiWriterException
    {
        // sanity checks
        if(apiDescription == null)
        {
            throw new NullPointerException("OdmaAbstractApiWriter.writeOdmaApi: apiDescription must not be null.");
        }
        // prepare project structure
        prepareProjectStructureAndBuildFiles(apiDescription);
        // the following files need to be created in the correct order
        createDataTypesFile(apiDescription);
        createQNameFile(apiDescription);
        createIdFile(apiDescription);
        createGuidFile(apiDescription);
        createConstantsFile(apiDescription); // needs OdmaQName
        createContentFile(apiDescription);
        createSearchResultFile(apiDescription);
        createExceptionFiles(apiDescription);
        // create language depending files for the session management
        createSessionManagementFiles(apiDescription);
        // create the properties file
        createPropertyFile(apiDescription);
        // create class files
        List<ClassDescription> classes = apiDescription.getDescribedClasses();
        Iterator<ClassDescription> itClasses = classes.iterator();
        while(itClasses.hasNext())
        {
            ClassDescription classDescription = itClasses.next();
            createClassFile(classDescription);
        }
        // create collection files (Lists and Enumerations)
        itClasses = classes.iterator();
        while(itClasses.hasNext())
        {
            ClassDescription classDescription = (ClassDescription)itClasses.next();
            createEnumerationFile(classDescription);
        }
        List<ScalarTypeDescription> scalarTypes = apiDescription.getScalarTypes();
        Iterator<ScalarTypeDescription> itScalarTypes = scalarTypes.iterator();
        while(itScalarTypes.hasNext())
        {
            ScalarTypeDescription scalarTypeDescription = itScalarTypes.next();
            if(!scalarTypeDescription.isReference())
            {
                createListFile(scalarTypeDescription);
            }
        }
        // create properties implementation file
        createPropertyImplementationFile(apiDescription);
        // create list implementation files
        itScalarTypes = scalarTypes.iterator();
        while(itScalarTypes.hasNext())
        {
            ScalarTypeDescription scalarTypeDescription = (ScalarTypeDescription)itScalarTypes.next();
            if(!scalarTypeDescription.isReference())
            {
                createListImplementationFile(scalarTypeDescription);
            }
        }
        // create class template files
        itClasses = classes.iterator();
        while(itClasses.hasNext())
        {
            ClassDescription classDescription = (ClassDescription)itClasses.next();
            createClassTemplateFile(classDescription);
        }
        // finalise project structure
        finaliseProjectStructureAndBuildFiles(apiDescription);
        // create any extras
        createExtras(apiDescription);
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
        return internalGetResourceAsStream("/templates/"+getTargetFolderName()+"/"+templateName+".template");
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
    
    public static void streamCopy(InputStream from, OutputStream to, PlaceholderResolver placeholderResolver) throws IOException
    {
        BufferedReader reader = new BufferedReader(new InputStreamReader(from, StandardCharsets.UTF_8));
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(to, StandardCharsets.UTF_8));
        String line;
        while ((line = reader.readLine()) != null) {
            writer.write(replacePlaceholders(line, placeholderResolver));
            writer.newLine();
        }
        writer.flush();
    }

    private static String replacePlaceholders(String text, PlaceholderResolver placeholderResolver) {
        StringBuilder result = new StringBuilder();
        int startIndex = 0;
        while (true) {
            int openBraceIndex = text.indexOf("{{", startIndex);
            if (openBraceIndex == -1) {
                result.append(text.substring(startIndex));
                break;
            }
            int closeBraceIndex = text.indexOf("}}", openBraceIndex);
            if (closeBraceIndex == -1) {
                result.append(text.substring(startIndex));
                break;
            }
            result.append(text, startIndex, openBraceIndex);
            String placeholderName = text.substring(openBraceIndex + 2, closeBraceIndex);
            String replacement = placeholderResolver.resolve(placeholderName);
            if (replacement == null) {
                replacement = "";
            }
            result.append(replacement);
            startIndex = closeBraceIndex + 2;
        }
        return result.toString();
    }    

    public interface PlaceholderResolver
    {
        String resolve(String placeholder);
    }
    
    public void copyTemplateToStream(String templateName, OutputStream out) throws IOException
    {
        copyTemplateToStream(templateName, out, null, true);
    }
    
    public void copyTemplateToStream(String templateName, OutputStream out, PlaceholderResolver placeholderResolver) throws IOException
    {
        copyTemplateToStream(templateName, out, placeholderResolver, true);
    }
    
    public void copyTemplateToStream(String templateName, OutputStream out, boolean closeOutput) throws IOException
    {
        copyTemplateToStream(templateName, out, null, closeOutput);
    }
    
    public void copyTemplateToStream(String templateName, OutputStream out, PlaceholderResolver placeholderResolver, boolean closeOutput) throws IOException
    {
        try
        {
            InputStream from = getTemplateAsStream(templateName);
            try
            {
                if(placeholderResolver != null)
                {
                    streamCopy(from, out, placeholderResolver);
                }
                else
                {
                    streamCopy(from, out);
                }
            }
            finally
            {
                from.close();
            }
        }
        finally
        {
            if(closeOutput)
            {
                out.close();
            }
        }
    }

    //-------------------------------------------------------------------------
    // C O N S T A N T S   F I L E
    //-------------------------------------------------------------------------

    protected abstract void createDataTypesFile(ApiDescription apiDescription) throws IOException;

    protected abstract void createConstantsFile(ApiDescription apiDescription) throws IOException;

    //-------------------------------------------------------------------------
    // B A S I C   F I L E S
    //-------------------------------------------------------------------------

    protected abstract void createQNameFile(ApiDescription apiDescription) throws IOException;

    protected abstract void createIdFile(ApiDescription apiDescription) throws IOException;

    protected abstract void createGuidFile(ApiDescription apiDescription) throws IOException;

    protected abstract void createContentFile(ApiDescription apiDescription) throws IOException;

    protected abstract void createSearchResultFile(ApiDescription apiDescription) throws IOException;

    protected abstract void createExceptionFiles(ApiDescription apiDescription) throws IOException;

    protected abstract void createSessionManagementFiles(ApiDescription apiDescription) throws IOException;

    //-------------------------------------------------------------------------
    // P R O P E R T Y   F I L E
    //-------------------------------------------------------------------------

    protected abstract void createPropertyFile(ApiDescription apiDescription) throws IOException;

    //-------------------------------------------------------------------------
    // C L A S S   F I L E S
    //-------------------------------------------------------------------------
    
    protected abstract void createClassFile(ClassDescription classDescription) throws IOException;
    
    //-------------------------------------------------------------------------
    // C O L L E C T I O N   F I L E S
    //-------------------------------------------------------------------------
    
    protected void createEnumerationFile(ClassDescription classDescription) throws IOException
    {
        // We are using generics in the target language instead of creating custom enumeration classes
    }

    protected void createListFile(ScalarTypeDescription scalarTypeDescription) throws IOException
    {
        // We are using generics in the target language instead of creating custom list classes
    }
    
    //-------------------------------------------------------------------------
    // I M P L E M E N T A T I O N   F I L E S
    //-------------------------------------------------------------------------

    protected abstract void createPropertyImplementationFile(ApiDescription apiDescription) throws IOException;

    protected void createListImplementationFile(ScalarTypeDescription scalarTypeDescription) throws IOException
    {
        // We are using generics in the target language instead of creating custom list classes
    }

    //-------------------------------------------------------------------------
    // C L A S S   T E M P L A T E S
    //-------------------------------------------------------------------------
    
    protected abstract void createClassTemplateFile(ClassDescription classDescription) throws IOException;
    
    //-------------------------------------------------------------------------
    // P R O J E C T   S T R U C T U R E   A N  D   B U I L D   F I L E
    //-------------------------------------------------------------------------
    
    protected abstract void prepareProjectStructureAndBuildFiles(ApiDescription apiDescription) throws IOException;
    
    protected abstract void finaliseProjectStructureAndBuildFiles(ApiDescription apiDescription) throws IOException;
    
    //-------------------------------------------------------------------------
    // E X T R A S
    //-------------------------------------------------------------------------
    
    protected void createExtras(ApiDescription apiDescription) throws IOException, ApiWriterException
    {
    }

}