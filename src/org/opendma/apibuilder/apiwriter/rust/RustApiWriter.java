package org.opendma.apibuilder.apiwriter.rust;

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

public class RustApiWriter extends AbstractApiWriter
{

    public RustApiWriter(File outputFolderRoot) throws ApiWriterException
    {
        super(outputFolderRoot);
    }

    public String getName()
    {
        return "Rust";
    }

    protected String getTargetFolderName()
    {
        return "rs";
    }

    //-------------------------------------------------------------------------
    // S O U R C E   F I L E   H E L P E R
    //-------------------------------------------------------------------------
    
    private OutputStream createRsFile(File targetFolder, String className) throws IOException
    {
        return new FileOutputStream(new File(targetFolder, className+".rs"));
    }
    
    private void createClassFromTemplate(File targetFolder, String className) throws IOException
    {
        copyTemplateToStream(className,createRsFile(targetFolder,className));
    }

    //-------------------------------------------------------------------------
    // C O N S T A N T S   F I L E
    //-------------------------------------------------------------------------

    protected void createDataTypesFile(ApiDescription apiDescription) throws IOException
    {
        // create type enumeration
        PrintWriter out = new PrintWriter(createRsFile(opendmaApiSourceFolder, "OdmaType"));
        out.println("#[derive(Debug)]");
        out.println("enum OdmaType {");
        List<ScalarTypeDescription> scalarTypes = apiDescription.getScalarTypes();
        Iterator<ScalarTypeDescription> itScalarTypes = scalarTypes.iterator();
        while(itScalarTypes.hasNext())
        {
            ScalarTypeDescription scalarTypeDescription = (ScalarTypeDescription)itScalarTypes.next();
            out.println("    "+scalarTypeDescription.getName().toUpperCase()+" = "+scalarTypeDescription.getNumericID()+",");
        }
        out.println("}");
        out.close();
    }

    protected void createConstantsFile(ApiDescription apiDescription) throws IOException
    {
        // create common names file
        RustCommonNamesFileWriter constantsFileWriter = new RustCommonNamesFileWriter();
        constantsFileWriter.createConstantsFile(apiDescription, createRsFile(opendmaApiSourceFolder, "OdmaCommonNames"));
    }

    //-------------------------------------------------------------------------
    // B A S I C   F I L E S
    //-------------------------------------------------------------------------

    protected void createQNameFile(ApiDescription apiDescription) throws IOException
    {
        createClassFromTemplate(opendmaApiSourceFolder, "OdmaQName");
    }

    protected void createIdFile(ApiDescription apiDescription) throws IOException
    {
        createClassFromTemplate(opendmaApiSourceFolder, "OdmaId");
    }

    protected void createGuidFile(ApiDescription apiDescription) throws IOException
    {
        createClassFromTemplate(opendmaApiSourceFolder, "OdmaGuid");
    }

    protected void createContentFile(ApiDescription apiDescription) throws IOException
    {
        createClassFromTemplate(opendmaApiSourceFolder, "OdmaContent");
    }

    protected void createSearchResultFile(ApiDescription apiDescription) throws IOException
    {
    }

    protected void createExceptionFiles(ApiDescription apiDescription) throws IOException
    {
        /*
        Errors are explicitly handled by the Result<T, E> return type:
        ```rust
        enum Result<T, E> {
            Ok(T),   // Holds the success value
            Err(E),  // Holds the error value
        }
        ```
        If a  method can return an OdmaObject or throw an OdmaObjectNotFoundException with
        the GUID of the missing object, this rust API returns the type
            Result<OdmaObject, OdmaGuid>
        and either
            return Ok(theObject)
        or
            return Err(missingObjectGuid)
        */
    }

    protected void createSessionManagementFiles(ApiDescription apiDescription) throws IOException
    {
    }

    //-------------------------------------------------------------------------
    // P R O P E R T Y   F I L E
    //-------------------------------------------------------------------------

    protected void createPropertyFile(ApiDescription apiDescription) throws IOException
    {
        RustPropertyFileWriter rustPropertyFileWriter = new RustPropertyFileWriter(this);
        rustPropertyFileWriter.createPropertyFile(apiDescription, createRsFile(opendmaApiSourceFolder, "OdmaProperty"));
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
    
    private File opendmaApiProjectFolder;
    
    private File opendmaApiSourceFolder;
    
    //private FileOutputStream opendmaApiLibFOS;
    
    //private FileOutputStream opendmaApiHelpersFOS;
    
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
        opendmaApiSourceFolder = new File(opendmaApiProjectFolder, "src");
        opendmaApiSourceFolder.mkdirs();
        //opendmaApiLibFOS = new FileOutputStream(new File(opendmaApiSourceFolder, "lib.rs"));
        //copyTemplateToStream("opendma-api-lib-header", opendmaApiLibFOS, false);
        //opendmaApiHelpersFOS = new FileOutputStream(new File(opendmaApiSourceFolder, "helpers.rs"));
        //copyTemplateToStream("opendma-api-helpers-header", opendmaApiHelpersFOS, false);
        // opendma-api Cargo.toml
        copyTemplateToStream("opendma-api-cargo", new FileOutputStream(new File(opendmaApiProjectFolder, "Cargo.toml")), resolver);
        // opendma-templates folder
        opendmaTemplatesFolder = new File(baseFolder, "opendma-templates");
        opendmaTemplatesFolder.mkdirs();
    }
    
    protected void finaliseProjectStructureAndBuildFiles(ApiDescription apiDescription) throws IOException
    {
        // flush and close files
        //opendmaApiLibFOS.flush();
        //opendmaApiLibFOS.close();
        //opendmaApiHelpersFOS.flush();
        //opendmaApiHelpersFOS.close();
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