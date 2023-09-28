package org.opendma.apibuilder.apiwriter.py;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.opendma.apibuilder.apiwriter.AbstractApiWriter;
import org.opendma.apibuilder.apiwriter.ApiWriterException;
import org.opendma.apibuilder.structure.ApiDescription;
import org.opendma.apibuilder.structure.ClassDescription;
import org.opendma.apibuilder.structure.ScalarTypeDescription;

public class PythonApiWriter extends AbstractApiWriter
{

    public PythonApiWriter(File outputFolderRoot) throws ApiWriterException
    {
        super(outputFolderRoot);
    }

    public String getName()
    {
        return "Python";
    }

    protected String getTargetFolderName()
    {
        return "py";
    }

    //-------------------------------------------------------------------------
    // S O U R C E   F I L E   H E L P E R
    //-------------------------------------------------------------------------
    
    private void copyClassToApiHelpers(String className) throws IOException
    {
        PrintWriter out = new PrintWriter(opendmaApiHelpersFOS);
        out.println();
        out.println();
        out.flush();
        copyTemplateToStream(className, opendmaApiHelpersFOS, false);
        classesImportFromHelpers.add(className);
    }

    //-------------------------------------------------------------------------
    // C O N S T A N T S   F I L E
    //-------------------------------------------------------------------------

    protected void createDataTypesFile(ApiDescription apiDescription) throws IOException
    {
        // create type enumeration
        PrintWriter out = new PrintWriter(opendmaApiHelpersFOS);
        out.println();
        out.println();
        out.println("from enum import Enum");
        out.println();
        out.println("class OdmaType(Enum):");
        List<ScalarTypeDescription> scalarTypes = apiDescription.getScalarTypes();
        Iterator<ScalarTypeDescription> itScalarTypes = scalarTypes.iterator();
        while(itScalarTypes.hasNext())
        {
            ScalarTypeDescription scalarTypeDescription = itScalarTypes.next();
            out.println("    "+scalarTypeDescription.getName().toUpperCase()+" = "+scalarTypeDescription.getNumericID());
        }
        out.flush();
        classesImportFromHelpers.add("OdmaType");
    }

    protected void createConstantsFile(ApiDescription apiDescription) throws IOException
    {
        // create constants file
        PythonConstantsFileWriter constantsFileWriter = new PythonConstantsFileWriter();
        constantsFileWriter.createConstantsFile(apiDescription, new FileOutputStream(new File(opendmaApiSourceFolder, "constants.py")));
        classesImportFromConstants.addAll(constantsFileWriter.getDefinedConstants());
    }

    //-------------------------------------------------------------------------
    // B A S I C   F I L E S
    //-------------------------------------------------------------------------

    protected void createQNameFile(ApiDescription apiDescription) throws IOException
    {
        copyClassToApiHelpers("OdmaQName");
    }

    protected void createIdFile(ApiDescription apiDescription) throws IOException
    {
        copyClassToApiHelpers("OdmaId");
    }

    protected void createGuidFile(ApiDescription apiDescription) throws IOException
    {
        copyClassToApiHelpers("OdmaGuid");
    }

    protected void createContentFile(ApiDescription apiDescription) throws IOException
    {
        copyClassToApiHelpers("OdmaContent");
    }

    protected void createSearchResultFile(ApiDescription apiDescription) throws IOException
    {
    }

    protected void createExceptionFiles(ApiDescription apiDescription) throws IOException
    {
        copyClassToApiHelpers("OdmaException");
        // OdmaRuntimeException: Python does not distinguish between checked and unchecked Exceptions
        copyClassToApiHelpers("OdmaObjectNotFoundException");
        copyClassToApiHelpers("OdmaPropertyNotFoundException");
        copyClassToApiHelpers("OdmaInvalidDataTypeException");
        copyClassToApiHelpers("OdmaAccessDeniedException");
        copyClassToApiHelpers("OdmaQuerySyntaxException");
    }

    protected void createSessionManagementFiles(ApiDescription apiDescription) throws IOException
    {
    }

    //-------------------------------------------------------------------------
    // P R O P E R T Y   F I L E
    //-------------------------------------------------------------------------

    protected void createPropertyFile(ApiDescription apiDescription) throws IOException
    {
        PythonPropertyFileWriter pythonPropertyFileWriter = new PythonPropertyFileWriter(this);
        pythonPropertyFileWriter.createPropertyFile(apiDescription, opendmaApiHelpersFOS, false);
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
    
    private FileOutputStream opendmaApiInterfacesFOS;
    
    private FileOutputStream opendmaApiHelpersFOS;
    
    private File opendmaTemplatesFolder;
    
    private List<String> classesImportFromInterfaces = new LinkedList<String>();
    
    private List<String> classesImportFromHelpers = new LinkedList<String>();
    
    private List<String> classesImportFromConstants = new LinkedList<String>();
    
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
        opendmaApiSourceFolder = new File(opendmaApiProjectFolder, "opendma/api");
        opendmaApiSourceFolder.mkdirs();
        opendmaApiInterfacesFOS = new FileOutputStream(new File(opendmaApiSourceFolder, "interfaces.py"));
        copyTemplateToStream("opendma-api-interfaces-header", opendmaApiInterfacesFOS, false);
        opendmaApiHelpersFOS = new FileOutputStream(new File(opendmaApiSourceFolder, "helpers.py"));
        copyTemplateToStream("opendma-api-helpers-header", opendmaApiHelpersFOS, false);
        // build file
        copyTemplateToStream("opendma-api-pyproject", new FileOutputStream(new File(opendmaApiProjectFolder, "pyproject.toml")), resolver);
        // templates folder
        opendmaTemplatesFolder = new File(baseFolder, "opendma-templates");
        opendmaTemplatesFolder.mkdirs();
    }
    
    protected void finaliseProjectStructureAndBuildFiles(ApiDescription apiDescription) throws IOException
    {
        // flush and close files
        opendmaApiInterfacesFOS.flush();
        opendmaApiInterfacesFOS.close();
        opendmaApiHelpersFOS.flush();
        opendmaApiHelpersFOS.close();
        // generate __INIT__.py file importing classes into namespace
        FileOutputStream initFOS = new FileOutputStream(new File(opendmaApiSourceFolder, "__INIT__.py"));
        copyTemplateToStream("opendma-api-init-header", initFOS, false);
        PrintWriter out = new PrintWriter(initFOS);
        if(!classesImportFromInterfaces.isEmpty())
        {
            out.print("from .interfaces import ");
            boolean needSep = false;
            for(String className : classesImportFromInterfaces)
            {
                if(needSep)
                {
                    out.print(", ");
                }
                else
                {
                    needSep = true;
                }
                out.print(className);
            }
            out.println();
        }
        if(!classesImportFromHelpers.isEmpty())
        {
            out.print("from .helpers import ");
            boolean needSep = false;
            for(String className : classesImportFromHelpers)
            {
                if(needSep)
                {
                    out.print(", ");
                }
                else
                {
                    needSep = true;
                }
                out.print(className);
            }
            out.println();
        }
        if(!classesImportFromConstants.isEmpty())
        {
            out.print("from .constants import ");
            boolean needSep = false;
            for(String className : classesImportFromConstants)
            {
                if(needSep)
                {
                    out.print(", ");
                }
                else
                {
                    needSep = true;
                }
                out.print(className);
            }
            out.println();
        }
        out.close();
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