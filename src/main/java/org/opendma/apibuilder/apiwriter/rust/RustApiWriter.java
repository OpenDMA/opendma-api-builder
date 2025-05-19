package org.opendma.apibuilder.apiwriter.rust;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.opendma.apibuilder.Tools;
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
        PrintWriter out = new PrintWriter(createRsFile(opendmaApiSourceFolder, "odma_type"));
        out.println("use std::fmt;");
        out.println("use std::convert::TryFrom;");
        out.println("");
        out.println("#[derive(Debug, Clone, Copy, PartialEq, Eq, Hash)]");
        out.println("pub enum OdmaType {");
        List<ScalarTypeDescription> scalarTypes = apiDescription.getScalarTypes();
        Iterator<ScalarTypeDescription> itScalarTypes = scalarTypes.iterator();
        while(itScalarTypes.hasNext())
        {
            ScalarTypeDescription scalarTypeDescription = (ScalarTypeDescription)itScalarTypes.next();
            out.println("    "+scalarTypeDescription.getName().toUpperCase()+" = "+scalarTypeDescription.getNumericID()+",");
        }
        out.println("}");
        out.println("");
        out.println("impl TryFrom<u8> for OdmaType {");
        out.println("    type Error = ();");
        out.println("    fn try_from(value: u8) -> Result<Self, Self::Error> {");
        out.println("        use OdmaType::*;");
        out.println("        match value {");
        itScalarTypes = scalarTypes.iterator();
        while(itScalarTypes.hasNext())
        {
            ScalarTypeDescription scalarTypeDescription = (ScalarTypeDescription)itScalarTypes.next();
            out.println("            "+scalarTypeDescription.getNumericID()+" => Ok("+scalarTypeDescription.getName().toUpperCase()+"),");
        }
        out.println("            _ => Err(()),");
        out.println("        }");
        out.println("    }");
        out.println("}");
        out.println("");
        out.println("impl fmt::Display for OdmaType {");
        out.println("    fn fmt(&self, f: &mut fmt::Formatter<'_>) -> fmt::Result {");
        out.println("        let name = match self {");
        itScalarTypes = scalarTypes.iterator();
        while(itScalarTypes.hasNext())
        {
            ScalarTypeDescription scalarTypeDescription = (ScalarTypeDescription)itScalarTypes.next();
            out.println("            OdmaType::"+scalarTypeDescription.getName().toUpperCase()+" => \""+scalarTypeDescription.getName().toUpperCase()+"\",");
        }
        out.println("        };");
        out.println("        write!(f, \"{}\", name)");
        out.println("    }");
        out.println("}");
        out.close();
        registerApiModule("odma_type");
        registerApiModuleExport("odma_type", "OdmaType");
    }

    protected void createConstantsFile(ApiDescription apiDescription) throws IOException
    {
        // create common names file
        RustCommonNamesFileWriter constantsFileWriter = new RustCommonNamesFileWriter();
        constantsFileWriter.createConstantsFile(apiDescription, createRsFile(opendmaApiSourceFolder, "odma_common_names"));
        registerApiModule("odma_common_names");
    }

    //-------------------------------------------------------------------------
    // B A S I C   F I L E S
    //-------------------------------------------------------------------------

    protected void createQNameFile(ApiDescription apiDescription) throws IOException
    {
        createClassFromTemplate(opendmaApiSourceFolder, "odma_qname");
        registerApiModule("odma_qname");
        registerApiModuleExport("odma_qname", "OdmaQName");
    }

    protected void createIdFile(ApiDescription apiDescription) throws IOException
    {
        createClassFromTemplate(opendmaApiSourceFolder, "odma_id");
        registerApiModule("odma_id");
        registerApiModuleExport("odma_id", "OdmaId");
    }

    protected void createGuidFile(ApiDescription apiDescription) throws IOException
    {
        createClassFromTemplate(opendmaApiSourceFolder, "odma_guid");
        registerApiModule("odma_guid");
        registerApiModuleExport("odma_guid", "OdmaGuid");
    }

    protected void createContentFile(ApiDescription apiDescription) throws IOException
    {
        createClassFromTemplate(opendmaApiSourceFolder, "odma_content");
        registerApiModule("odma_content");
        registerApiModuleExport("odma_content", "OdmaContent");
    }

    protected void createSearchResultFile(ApiDescription apiDescription) throws IOException
    {
        createClassFromTemplate(opendmaApiSourceFolder, "odma_search_result");
        registerApiModule("odma_search_result");
        registerApiModuleExport("odma_search_result", "OdmaSearchResult");
    }

    protected void createExceptionFiles(ApiDescription apiDescription) throws IOException
    {
        createClassFromTemplate(opendmaApiSourceFolder, "odma_error");
        registerApiModule("odma_error");
        registerApiModuleExport("odma_error", "OdmaError");
    }

    protected void createSessionManagementFiles(ApiDescription apiDescription) throws IOException
    {
        createClassFromTemplate(opendmaApiSourceFolder, "odma_session");
        registerApiModule("odma_session");
        registerApiModuleExport("odma_session", "OdmaSession");
    }

    //-------------------------------------------------------------------------
    // P R O P E R T Y   F I L E
    //-------------------------------------------------------------------------

    protected void createPropertyFile(ApiDescription apiDescription) throws IOException
    {
        RustPropertyFileWriter rustPropertyFileWriter = new RustPropertyFileWriter(this);
        rustPropertyFileWriter.createPropertyFile(apiDescription, createRsFile(opendmaApiSourceFolder, "odma_property"));
        registerApiModule("odma_property");
        registerApiModuleExport("odma_property", "OdmaProperty");
    }

    //-------------------------------------------------------------------------
    // C L A S S   F I L E
    //-------------------------------------------------------------------------

    protected void createCoreObjectFile(ApiDescription apiDescription) throws IOException
    {
        createClassFromTemplate(opendmaApiSourceFolder, "odma_core_object");
        registerApiModule("odma_core_object");
        registerApiModuleExport("odma_core_object", "OdmaCoreObject");
    }

    protected void createClassFile(ClassDescription classDescription) throws IOException
    {
        RustObjectsInterfaceFileWriter classFileWriter = new RustObjectsInterfaceFileWriter(this);
        String moduleName = Tools.toSnakeCase(classDescription.getApiName());
        classFileWriter.createClassFile(classDescription, createRsFile(opendmaApiSourceFolder,moduleName));
        registerApiModule(moduleName);
        registerApiModuleExport(moduleName, classDescription.getApiName());
    }
    
    //-------------------------------------------------------------------------
    // I M P L E M E N T A T I O N   F I L E S
    //-------------------------------------------------------------------------

    protected void createPropertyImplementationFile(ApiDescription apiDescription) throws IOException
    {
        RustPropertyImplementationFileWriter rustPropertyImplementationFileWriter = new RustPropertyImplementationFileWriter(this);
        rustPropertyImplementationFileWriter.createPropertyFile(apiDescription, createRsFile(opendmaApiSourceFolder, "odma_property_impl"));
        registerApiModule("odma_property_impl");
        registerApiModuleExport("odma_property_impl", "OdmaPropertyImpl");
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
    
    private List<String> opendmaApiModules = new LinkedList<String>();
    
    private Map<String, List<String>> opendmaApiModuleExports = new HashMap<String, List<String>>();
    
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
        // opendma-api Cargo.toml
        copyTemplateToStream("opendma-api-cargo", new FileOutputStream(new File(opendmaApiProjectFolder, "Cargo.toml")), resolver);
    }
    
    protected void registerApiModule(String moduleName)
    {
        if(!opendmaApiModules.contains(moduleName))
        {
            opendmaApiModules.add(moduleName);
            opendmaApiModuleExports.put(moduleName, new LinkedList<String>());
        }
    }
    
    protected void registerApiModuleExport(String moduleName, String export)
    {
        if(!opendmaApiModules.contains(moduleName))
        {
            throw new RuntimeException("Module "+moduleName+" not registered.");
        }
        List<String> exports = opendmaApiModuleExports.get(moduleName);
        if(!exports.contains(moduleName))
        {
            exports.add(export);
        }
    }
    
    protected void finaliseProjectStructureAndBuildFiles(ApiDescription apiDescription) throws IOException
    {
        PrintWriter out = new PrintWriter(createRsFile(opendmaApiSourceFolder, "lib"));
        for(String mod : opendmaApiModules)
        {
            out.println("pub mod "+mod+";");
            List<String> exports = opendmaApiModuleExports.get(mod);
            if(!exports.isEmpty())
            {
                if(exports.size() == 1)
                {
                    out.println("pub use "+mod+"::"+exports.get(0)+";");
                }
                else
                {
                    out.print("pub use "+mod+"::{");
                    Iterator<String> it = exports.iterator();
                    while(it.hasNext())
                    {
                        out.print(it.next());
                        if(it.hasNext())
                        {
                            out.print(", ");
                        }
                    }
                    out.println("};");
                    
                }
            }
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