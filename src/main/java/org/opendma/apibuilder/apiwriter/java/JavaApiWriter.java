package org.opendma.apibuilder.apiwriter.java;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.opendma.apibuilder.apiwriter.AbstractApiWriter;
import org.opendma.apibuilder.apiwriter.ApiWriterException;
import org.opendma.apibuilder.structure.ApiDescription;
import org.opendma.apibuilder.structure.ClassDescription;
import org.opendma.apibuilder.structure.PropertyDescription;
import org.opendma.apibuilder.structure.ScalarTypeDescription;

public class JavaApiWriter extends AbstractApiWriter
{

    public JavaApiWriter(File outputFolderRoot) throws ApiWriterException
    {
        super(outputFolderRoot);
    }

    public String getName()
    {
        return "Java";
    }
    
    protected String getTargetFolderName()
    {
        return "java";
    }
    
    public boolean supportNullability()
    {
        return false;
    }

    //-------------------------------------------------------------------------
    // S O U R C E   F I L E   H E L P E R
    //-------------------------------------------------------------------------
    
    private OutputStream createJavaFile(File targetFolder, String packageName, String className) throws IOException
    {
        File packageDirectory = new File(targetFolder, packageName.replace('.',File.separatorChar));
        if(!packageDirectory.exists())
        {
            if(!packageDirectory.mkdirs())
            {
                throw new IOException("Can not create package directory");
            }
        }
        return new FileOutputStream(new File(packageDirectory, className+".java"));
    }
    
    private void createClassFromTemplate(File targetFolder, String packageName, String className) throws IOException
    {
        copyTemplateToStream(className,createJavaFile(targetFolder,packageName,className));
    }

    //-------------------------------------------------------------------------
    // C O N S T A N T S   F I L E
    //-------------------------------------------------------------------------

    protected void createDataTypesFile(ApiDescription apiDescription) throws IOException
    {
        // create type enumeration
        PrintWriter out = new PrintWriter(createJavaFile(opendmaApiSourceFolder,"org.opendma.api","OdmaType"));
        out.println("package org.opendma.api;");
        out.println();
        out.println("/**");
        out.println(" * OpenDMA property data types.");
        out.println(" */");
        out.println("public enum OdmaType {");
        out.println();
        List<ScalarTypeDescription> scalarTypes = apiDescription.getScalarTypes();
        Iterator<ScalarTypeDescription> itScalarTypes = scalarTypes.iterator();
        while(itScalarTypes.hasNext())
        {
            ScalarTypeDescription scalarTypeDescription = itScalarTypes.next();
            out.println("    "+scalarTypeDescription.getName().toUpperCase()+"("+scalarTypeDescription.getNumericID()+")"+(itScalarTypes.hasNext()?",":";"));
        }
        out.println();
        out.println("    private final int numericId;");
        out.println();
        out.println("    private OdmaType(int numericId) {");
        out.println("        this.numericId = numericId;");
        out.println("    }");
        out.println();
        out.println("    public int getNumericId() {");
        out.println("        return numericId;");
        out.println("    }");
        out.println();
        out.println("    public static OdmaType fromNumericId(int numericId) {");
        out.println("        for (OdmaType val : OdmaType.values()) {");
        out.println("            if (val.getNumericId() == numericId) {");
        out.println("                return val;");
        out.println("            }");
        out.println("        }");
        out.println("        throw new IllegalArgumentException(\"Unknown numericId \" + numericId);");
        out.println("    }");
        out.println();
        out.println("    /**");
        out.println("     * Returns the OdmaType corresponding to the given name, ignoring case.");
        out.println("     *");
        out.println("     * @param name the name of the OdmaType (case-insensitive)");
        out.println("     * @return the matching OdmaType");
        out.println("     * @throws IllegalArgumentException if no matching OdmaType exists");
        out.println("     */");
        out.println("    public static OdmaType fromString(String name) {");
        out.println("        if (name == null || name.trim().isEmpty()) {");
        out.println("            throw new IllegalArgumentException(\"OdmaType name must not be null or empty\");");
        out.println("        }");
        out.println("        for (OdmaType type : OdmaType.values()) {");
        out.println("            if (type.name().equalsIgnoreCase(name.trim())) {");
        out.println("                return type;");
        out.println("            }");
        out.println("        }");
        out.println("        throw new IllegalArgumentException(\"Unknown OdmaType name: \" + name);");
        out.println("    }");
        out.println();
        out.println("}");
        out.close();
    }

    protected void createConstantsFile(ApiDescription apiDescription) throws IOException
    {
        // create common names file
        JavaCommonNamesFileWriter constantsFileWriter = new JavaCommonNamesFileWriter();
        constantsFileWriter.createConstantsFile(apiDescription, createJavaFile(opendmaApiSourceFolder,"org.opendma.api","OdmaCommonNames"));
    }

    //-------------------------------------------------------------------------
    // B A S I C   F I L E S
    //-------------------------------------------------------------------------

    protected void createQNameFile(ApiDescription apiDescription) throws IOException
    {
        createClassFromTemplate(opendmaApiSourceFolder,"org.opendma.api","OdmaQName");
    }

    protected void createIdFile(ApiDescription apiDescription) throws IOException
    {
        createClassFromTemplate(opendmaApiSourceFolder,"org.opendma.api","OdmaId");
    }

    protected void createGuidFile(ApiDescription apiDescription) throws IOException
    {
        createClassFromTemplate(opendmaApiSourceFolder,"org.opendma.api","OdmaGuid");
    }

    protected void createContentFile(ApiDescription apiDescription) throws IOException
    {
        createClassFromTemplate(opendmaApiSourceFolder,"org.opendma.api","OdmaContent");
    }

    protected void createSearchResultFile(ApiDescription apiDescription) throws IOException
    {
        createClassFromTemplate(opendmaApiSourceFolder,"org.opendma.api","OdmaSearchResult");
    }

    protected void createExceptionFiles(ApiDescription apiDescription) throws IOException
    {
        createClassFromTemplate(opendmaApiSourceFolder,"org.opendma.exceptions","OdmaException");
        createClassFromTemplate(opendmaApiSourceFolder,"org.opendma.exceptions","OdmaRuntimeException");
        createClassFromTemplate(opendmaApiSourceFolder,"org.opendma.exceptions","OdmaObjectNotFoundException");
        createClassFromTemplate(opendmaApiSourceFolder,"org.opendma.exceptions","OdmaPropertyNotFoundException");
        createClassFromTemplate(opendmaApiSourceFolder,"org.opendma.exceptions","OdmaInvalidDataTypeException");
        createClassFromTemplate(opendmaApiSourceFolder,"org.opendma.exceptions","OdmaAccessDeniedException");
        createClassFromTemplate(opendmaApiSourceFolder,"org.opendma.exceptions","OdmaQuerySyntaxException");
        createClassFromTemplate(opendmaApiSourceFolder,"org.opendma.exceptions","OdmaServiceException");
    }

    protected void createSessionManagementFiles(ApiDescription apiDescription) throws IOException
    {
        createClassFromTemplate(opendmaApiSourceFolder,"org.opendma.api","OdmaSession");
        createClassFromTemplate(opendmaApiSourceFolder,"org.opendma.api","OdmaSessionProvider");
        createClassFromTemplate(opendmaApiSourceFolder,"org.opendma.api","OdmaAdaptor");
        createClassFromTemplate(opendmaApiSourceFolder,"org.opendma.api","OdmaAdaptorDiscovery");
    }

    //-------------------------------------------------------------------------
    // P R O P E R T Y   F I L E
    //-------------------------------------------------------------------------

    protected void createPropertyFile(ApiDescription apiDescription) throws IOException
    {
        JavaPropertyFileWriter javaPropertyFileWriter = new JavaPropertyFileWriter(this);
        javaPropertyFileWriter.createPropertyFile(apiDescription, createJavaFile(opendmaApiSourceFolder,"org.opendma.api","OdmaProperty"));
    }

    //-------------------------------------------------------------------------
    // C L A S S   F I L E
    //-------------------------------------------------------------------------

    protected void createCoreObjectFile(ApiDescription apiDescription) throws IOException
    {
        createClassFromTemplate(opendmaApiSourceFolder,"org.opendma.api","OdmaCoreObject");
    }

    protected void createClassFile(ClassDescription classDescription) throws IOException
    {
        JavaObjectsInterfaceFileWriter classFileWriter = new JavaObjectsInterfaceFileWriter(this);
        classFileWriter.createClassFile(classDescription, createJavaFile(opendmaApiSourceFolder,"org.opendma.api",classDescription.getApiName()));
    }
    
    //-------------------------------------------------------------------------
    // C O L L E C T I O N   F I L E S
    //-------------------------------------------------------------------------

    protected void createPagingFiles(ApiDescription apiDescription) throws IOException
    {
        createClassFromTemplate(opendmaApiSourceFolder,"org.opendma.api","OdmaPageIterator");
        createClassFromTemplate(opendmaApiSourceFolder,"org.opendma.api","OdmaPagingIterable");
        createClassFromTemplate(opendmaApiSourceFolder,"org.opendma.api","OdmaDefaultPagingIterator");
    }
    
    //-------------------------------------------------------------------------
    // I M P L E M E N T A T I O N   F I L E S
    //-------------------------------------------------------------------------

    protected void createPropertyImplementationFile(ApiDescription apiDescription) throws IOException
    {
        JavaPropertyImplementationFileWriter javaPropertyImplementationFileWriter = new JavaPropertyImplementationFileWriter(this);
        javaPropertyImplementationFileWriter.createPropertyFile(apiDescription, createJavaFile(opendmaApiSourceFolder,"org.opendma.impl","OdmaPropertyImpl"));
    }

    //-------------------------------------------------------------------------
    // C L A S S   T E M P L A T E S
    //-------------------------------------------------------------------------

    protected void createClassTemplateFile(ClassDescription classDescription) throws IOException
    {
       JavaClassTemplateFileWriter classtemplateFileWriter = new JavaClassTemplateFileWriter(this);
       classtemplateFileWriter.createClassFile(classDescription, createJavaFile(opendmaApiTemplatesFolder,"org.opendma.templates",classDescription.getApiName()+"Template"));
    }
   
    //-------------------------------------------------------------------------
    // P R O J E C T   S T R U C T U R E   A N  D   B U I L D   F I L E
    //-------------------------------------------------------------------------
    
    private File opendmaApiProjectFolder;
    
    private File opendmaApiSourceFolder;
    
    private File opendmaApiTemplatesFolder;
    
    private File opendmaApiTestFolder;
    
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
        opendmaApiSourceFolder = new File(opendmaApiProjectFolder, "src/main/java");
        opendmaApiSourceFolder.mkdirs();
        opendmaApiTestFolder = new File(opendmaApiProjectFolder, "src/test/java");
        opendmaApiTestFolder.mkdirs();
        opendmaApiTemplatesFolder = new File(opendmaApiProjectFolder, "templates");
        opendmaApiTemplatesFolder.mkdirs();
        // opendma-api maven pom
        copyTemplateToStream("maven-opendma-api-pom", new FileOutputStream(new File(opendmaApiProjectFolder, "pom.xml")), resolver);
    }
    
    protected void finaliseProjectStructureAndBuildFiles(ApiDescription apiDescription) throws IOException
    {
    }
    
    //-------------------------------------------------------------------------
    // S T A T I C   H E L P E R
    //-------------------------------------------------------------------------
    
    public static boolean needToImportPackage(String importDeclaration, String intoPackage)
    {
        boolean notNeeded = importDeclaration.startsWith(intoPackage+".") && importDeclaration.substring(intoPackage.length()+1).indexOf(".") < 0;
        return !notNeeded;
    }

    //-------------------------------------------------------------------------
    // E X T R A S
    //-------------------------------------------------------------------------
    
    protected void createExtras(ApiDescription apiDescription) throws IOException, ApiWriterException
    {
        createTechnologyCompatibilityKit(apiDescription);
        createStaticClassHierarchyCore(apiDescription);
        createStaticClassHierarchyCoreTests(apiDescription);
        createProxyFactory(apiDescription);
    }

    protected void createTechnologyCompatibilityKit(ApiDescription apiDescription) throws IOException, ApiWriterException
    {
        OutputStream tckStream = createJavaFile(opendmaApiTestFolder,"org.opendma.tck","OdmaTechnologyCompatibilityKit");
        PrintWriter out = new PrintWriter(tckStream);
        out.println("package org.opendma.tck;");
        out.println("");
        out.println("import java.util.HashMap;");
        out.println("import java.util.HashSet;");
        out.println("import java.util.LinkedList;");
        out.println("import java.util.List;");
        out.println("import org.opendma.api.*;");
        out.println("import org.opendma.exceptions.OdmaInvalidDataTypeException;");
        out.println("import org.opendma.exceptions.OdmaPropertyNotFoundException;");
        out.println("");
        out.println("public class OdmaTechnologyCompatibilityKit {");
        out.flush();
        copyTemplateToStream("tck-baseline-checks", tckStream, false);
        Iterator<ClassDescription> itClasses = apiDescription.getDescribedClasses().iterator();
        while(itClasses.hasNext())
        {
            ClassDescription classDescription = itClasses.next();
            out.println("");
            out.println("    public static List<String> verify"+classDescription.getApiName()+"(OdmaObject obj) {");
            out.println("        LinkedList<String> result = new LinkedList<>();");
            out.println("        result.addAll(verifyObjectBaseline(obj));");
            if(classDescription == apiDescription.getClassClass()) {
                out.println("        if(obj instanceof OdmaClass) {");
                out.println("            result.addAll(verifyClassBaseline((OdmaClass)obj, new HashSet<OdmaQName>()));");
                out.println("        }");
            }
            if(classDescription.getExtendsOdmaName() != null)
            {
                out.println("        result.addAll(verify"+classDescription.getExtendsApiName()+"(obj));");
            }
            else if(classDescription.getAspect())
            {
                out.println("        result.addAll(verify"+classDescription.getContainingApiDescription().getObjectClass().getApiName()+"(obj));");
            }
            out.println("        OdmaClass clazz = obj.getOdmaClass();");
            out.println("        Iterable<OdmaPropertyInfo> declaredProperties = clazz != null ? clazz.getDeclaredProperties() : null;");
            out.println("        Iterable<OdmaPropertyInfo> allProperties = clazz != null ? clazz.getProperties() : null;");
            List<PropertyDescription> declaredProperties = classDescription.getPropertyDescriptions();
            Iterator<PropertyDescription> itDeclaredProperties = declaredProperties.iterator();
            while(itDeclaredProperties.hasNext())
            {
                PropertyDescription propertyDescription = itDeclaredProperties.next();
                out.println("        // "+propertyDescription.getOdmaName());
                out.println("        OdmaQName qname"+propertyDescription.getApiName()+" = new OdmaQName(\""+propertyDescription.getOdmaName().getNamespace()+"\",\""+propertyDescription.getOdmaName().getName()+"\");");
                out.println("        try {");
                out.println("            OdmaProperty prop"+propertyDescription.getApiName()+" = obj.getProperty(qname"+propertyDescription.getApiName()+");");
                out.println("            if(prop"+propertyDescription.getApiName()+".getName() == null) {");
                out.println("                result.add(\"Property "+propertyDescription.getOdmaName()+" qname is null\");");
                out.println("            }");
                out.println("            if(!\""+propertyDescription.getOdmaName().getNamespace()+"\".equals(prop"+propertyDescription.getApiName()+".getName().getNamespace())) {");
                out.println("                result.add(\"Property "+propertyDescription.getOdmaName()+" qname namespace is not '"+propertyDescription.getOdmaName().getNamespace()+"', found instead'\"+prop"+propertyDescription.getApiName()+".getName().getNamespace()+\"'\");");
                out.println("            }");
                out.println("            if(!\""+propertyDescription.getOdmaName().getName()+"\".equals(prop"+propertyDescription.getApiName()+".getName().getName())) {");
                out.println("                result.add(\"Property "+propertyDescription.getOdmaName()+" qname name is not '"+propertyDescription.getOdmaName().getName()+"', found instead'\"+prop"+propertyDescription.getApiName()+".getName().getName()+\"'\");");
                out.println("            }");
                out.println("            if(prop"+propertyDescription.getApiName()+".getType() != OdmaType."+propertyDescription.getDataType().getName().toUpperCase()+") {");
                out.println("                result.add(\"Property "+propertyDescription.getOdmaName()+" type is not '"+propertyDescription.getDataType().getName().toUpperCase()+"'\");");
                out.println("            }");
                out.println("            if(prop"+propertyDescription.getApiName()+".isMultiValue() != "+(propertyDescription.getMultiValue()?"true":"false")+") {");
                out.println("                result.add(\"Property "+propertyDescription.getOdmaName()+" MultiValue is not '"+(propertyDescription.getMultiValue()?"true":"false")+"'\");");
                out.println("            }");
                if(propertyDescription.isReadOnly())
                {
                    out.println("            if(!prop"+propertyDescription.getApiName()+".isReadOnly()) {");
                    out.println("                result.add(\"Property "+propertyDescription.getOdmaName()+" ReadOnly must be 'true'\");");
                    out.println("            }");
                }
                if(propertyDescription.getMultiValue())
                {
                    out.println("            if(prop"+propertyDescription.getApiName()+".getValue() == null) {");
                    out.println("                result.add(\"Property "+propertyDescription.getOdmaName()+" is multi-valued but value is null\");");
                    out.println("            }");
                }
                if(propertyDescription.getRequired())
                {
                    if(propertyDescription.getMultiValue())
                    {
                        out.println("            if(((List<Object>)prop"+propertyDescription.getApiName()+".getValue()).isEmpty()) {");
                        out.println("                result.add(\"Property "+propertyDescription.getOdmaName()+" is required but value is empty\");");
                        out.println("            }");
                    }
                    else
                    {
                        out.println("            if(prop"+propertyDescription.getApiName()+".getValue() == null) {");
                        out.println("                result.add(\"Property "+propertyDescription.getOdmaName()+" is required but value is null\");");
                        out.println("            }");
                    }
                }
                out.println("        } catch(OdmaPropertyNotFoundException pnfe) {");
                out.println("            result.add(\"Missing property "+propertyDescription.getOdmaName()+"\");");
                out.println("        }");
                out.println("        if(clazz != null && (new OdmaQName(\""+classDescription.getOdmaName().getNamespace()+"\",\""+classDescription.getOdmaName().getName()+"\")).equals(clazz.getQName())) {");
                out.println("            OdmaPropertyInfo piDeclared"+propertyDescription.getApiName()+" = null;");
                out.println("            if(declaredProperties != null) {");
                out.println("                for(OdmaPropertyInfo pi : declaredProperties) {");
                out.println("                    if(qname"+propertyDescription.getApiName()+".equals(pi.getQName())) {");
                out.println("                        if(piDeclared"+propertyDescription.getApiName()+" == null) {");
                out.println("                            piDeclared"+propertyDescription.getApiName()+" = pi;");
                out.println("                        } else {");
                out.println("                            result.add(\"Declared properties in class have multiple property info objects with qname "+propertyDescription.getOdmaName()+"\");");
                out.println("                        }");
                out.println("                    }");
                out.println("                }");
                out.println("            }");
                out.println("            if(piDeclared"+propertyDescription.getApiName()+" == null) {");
                out.println("                result.add(\"Declared properties in class have no property info object with qname "+propertyDescription.getOdmaName()+"\");");
                out.println("            }");
                out.println("            if(piDeclared"+propertyDescription.getApiName()+" != null) {");
                out.println("                if(!\""+propertyDescription.getOdmaName().getNamespace()+"\".equals(piDeclared"+propertyDescription.getApiName()+".getNamespace())) {");
                out.println("                    result.add(\"Property info for "+propertyDescription.getOdmaName()+" in declared properties qname namespace is not '"+propertyDescription.getOdmaName().getNamespace()+"'\");");
                out.println("                }");
                out.println("                if(!\""+propertyDescription.getOdmaName().getName()+"\".equals(piDeclared"+propertyDescription.getApiName()+".getName())) {");
                out.println("                    result.add(\"Property info for "+propertyDescription.getOdmaName()+" in declared properties qname name is not '"+propertyDescription.getOdmaName().getName()+"'\");");
                out.println("                }");
                out.println("                if(piDeclared"+propertyDescription.getApiName()+".getDataType() != "+propertyDescription.getDataType().getNumericID()+") {");
                out.println("                    result.add(\"Property info for "+propertyDescription.getOdmaName()+" in declared properties data type is not '"+propertyDescription.getDataType().getNumericID()+"'\");");
                out.println("                }");
                out.println("                if(piDeclared"+propertyDescription.getApiName()+".isMultiValue() != "+(propertyDescription.getMultiValue()?"true":"false")+") {");
                out.println("                    result.add(\"Property info for "+propertyDescription.getOdmaName()+" in declared properties MultiValue is not '"+(propertyDescription.getMultiValue()?"true":"false")+"'\");");
                out.println("                }");
                out.println("                if(piDeclared"+propertyDescription.getApiName()+".isReadOnly() != "+(propertyDescription.isReadOnly()?"true":"false")+") {");
                out.println("                    result.add(\"Property info for "+propertyDescription.getOdmaName()+" in declared properties ReadOnly is not '"+(propertyDescription.isReadOnly()?"true":"false")+"'\");");
                out.println("                }");
                if(propertyDescription.isReference())
                {
                    out.println("                if(!(new OdmaQName(\""+propertyDescription.getReferenceClassName().getNamespace()+"\",\""+propertyDescription.getReferenceClassName().getName()+"\")).equals(piDeclared"+propertyDescription.getApiName()+".getReferenceClass().getQName())) {");
                    out.println("                    result.add(\"Property info for "+propertyDescription.getOdmaName()+" in declared properties ReadOnly is not '"+(propertyDescription.isReadOnly()?"true":"false")+"'\");");
                    out.println("                }");
                }
                out.println("                if(piDeclared"+propertyDescription.getApiName()+".isHidden() != "+(propertyDescription.getHidden()?"true":"false")+") {");
                out.println("                    result.add(\"Property info for "+propertyDescription.getOdmaName()+" in declared properties Hidden is not '"+(propertyDescription.getHidden()?"true":"false")+"'\");");
                out.println("                }");
                out.println("                if(piDeclared"+propertyDescription.getApiName()+".isRequired() != "+(propertyDescription.getRequired()?"true":"false")+") {");
                out.println("                result.add(\"Property info for "+propertyDescription.getOdmaName()+" in declared properties Required is not '"+(propertyDescription.getRequired()?"true":"false")+"'\");");
                out.println("                }");
                out.println("                if(piDeclared"+propertyDescription.getApiName()+".isSystem() != "+(propertyDescription.getSystem()?"true":"false")+") {");
                out.println("                    result.add(\"Property info for "+propertyDescription.getOdmaName()+" in declared properties System is not '"+(propertyDescription.getSystem()?"true":"false")+"'\");");
                out.println("                }");
                out.println("            }");
                out.println("        }");
                out.println("        OdmaPropertyInfo piAll"+propertyDescription.getApiName()+" = null;");
                out.println("        if(allProperties != null) {");
                out.println("            for(OdmaPropertyInfo pi : allProperties) {");
                out.println("                if(qname"+propertyDescription.getApiName()+".equals(pi.getQName())) {");
                out.println("                    if(piAll"+propertyDescription.getApiName()+" == null) {");
                out.println("                        piAll"+propertyDescription.getApiName()+" = pi;");
                out.println("                    } else {");
                out.println("                        result.add(\"All properties in class have multiple property info objects with qname "+propertyDescription.getOdmaName()+"\");");
                out.println("                    }");
                out.println("                }");
                out.println("            }");
                out.println("        }");
                out.println("        if(piAll"+propertyDescription.getApiName()+" == null) {");
                out.println("            result.add(\"All properties in class have no property info object with qname "+propertyDescription.getOdmaName()+"\");");
                out.println("        }");
                out.println("        if(piAll"+propertyDescription.getApiName()+" != null) {");
                out.println("            if(!\""+propertyDescription.getOdmaName().getNamespace()+"\".equals(piAll"+propertyDescription.getApiName()+".getNamespace())) {");
                out.println("                result.add(\"Property info for "+propertyDescription.getOdmaName()+" in all properties qname namespace is not '"+propertyDescription.getOdmaName().getNamespace()+"'\");");
                out.println("            }");
                out.println("            if(!\""+propertyDescription.getOdmaName().getName()+"\".equals(piAll"+propertyDescription.getApiName()+".getName())) {");
                out.println("                result.add(\"Property info for "+propertyDescription.getOdmaName()+" in all properties qname name is not '"+propertyDescription.getOdmaName().getName()+"'\");");
                out.println("            }");
                out.println("            if(piAll"+propertyDescription.getApiName()+".getDataType() != "+propertyDescription.getDataType().getNumericID()+") {");
                out.println("                result.add(\"Property info for "+propertyDescription.getOdmaName()+" in all properties data type is not '"+propertyDescription.getDataType().getNumericID()+"'\");");
                out.println("            }");
                out.println("            if(piAll"+propertyDescription.getApiName()+".isMultiValue() != "+(propertyDescription.getMultiValue()?"true":"false")+") {");
                out.println("                result.add(\"Property info for "+propertyDescription.getOdmaName()+" in all properties MultiValue is not '"+(propertyDescription.getMultiValue()?"true":"false")+"'\");");
                out.println("            }");
                out.println("            if(piAll"+propertyDescription.getApiName()+".isReadOnly() != "+(propertyDescription.isReadOnly()?"true":"false")+") {");
                out.println("                result.add(\"Property info for "+propertyDescription.getOdmaName()+" in all properties ReadOnly is not '"+(propertyDescription.isReadOnly()?"true":"false")+"'\");");
                out.println("            }");
                if(propertyDescription.isReference())
                {
                    out.println("            if(!(new OdmaQName(\""+propertyDescription.getReferenceClassName().getNamespace()+"\",\""+propertyDescription.getReferenceClassName().getName()+"\")).equals(piAll"+propertyDescription.getApiName()+".getReferenceClass().getQName())) {");
                    out.println("                result.add(\"Property info for "+propertyDescription.getOdmaName()+" in all properties ReadOnly is not '"+(propertyDescription.isReadOnly()?"true":"false")+"'\");");
                    out.println("            }");
                }
                out.println("            if(piAll"+propertyDescription.getApiName()+".isHidden() != "+(propertyDescription.getHidden()?"true":"false")+") {");
                out.println("                result.add(\"Property info for "+propertyDescription.getOdmaName()+" in all properties Hidden is not '"+(propertyDescription.getHidden()?"true":"false")+"'\");");
                out.println("            }");
                out.println("            if(piAll"+propertyDescription.getApiName()+".isRequired() != "+(propertyDescription.getRequired()?"true":"false")+") {");
                out.println("                result.add(\"Property info for "+propertyDescription.getOdmaName()+" in all properties Required is not '"+(propertyDescription.getRequired()?"true":"false")+"'\");");
                out.println("            }");
                out.println("            if(piAll"+propertyDescription.getApiName()+".isSystem() != "+(propertyDescription.getSystem()?"true":"false")+") {");
                out.println("                result.add(\"Property info for "+propertyDescription.getOdmaName()+" in all properties System is not '"+(propertyDescription.getSystem()?"true":"false")+"'\");");
                out.println("            }");
                out.println("        }");
            }
            out.println("        return result;");
            out.println("    }");
        }
        out.println("");
        out.println("}");
        // close writer and streams
        out.close();
        tckStream.close();
    }

    protected void createStaticClassHierarchyCoreTests(ApiDescription apiDescription) throws IOException, ApiWriterException
    {
        createStaticClassFromTemplate(opendmaApiTestFolder, "org.opendma.impl.core", "OdmaStaticClassHierarchyTests", apiDescription);
    }

    protected void createStaticClassHierarchyCore(ApiDescription apiDescription) throws IOException, ApiWriterException
    {
        // copy static helper templates
        createStaticClassFromTemplate(opendmaApiSourceFolder, "org.opendma.impl.core", "OdmaStaticSystemObject", apiDescription);
        createStaticClassFromTemplate(opendmaApiSourceFolder, "org.opendma.impl.core", "OdmaStaticSystemPropertyInfo", apiDescription);
        createStaticClassFromTemplate(opendmaApiSourceFolder, "org.opendma.impl.core", "OdmaStaticSystemClass", apiDescription);
        createStaticClassFromTemplate(opendmaApiSourceFolder, "org.opendma.impl.core", "OdmaStaticSystemRepository", apiDescription);
        // additionally create static class hierarchy helper files
        List<ClassDescription> classes = apiDescription.getDescribedClasses();
        // property
        Iterator<ClassDescription> itClasses = classes.iterator();
        while(itClasses.hasNext())
        {
            ClassDescription classDescription = itClasses.next();
            List<PropertyDescription> declaredProperties = classDescription.getPropertyDescriptions();
            Iterator<PropertyDescription> itDeclaredProperties = declaredProperties.iterator();
            while(itDeclaredProperties.hasNext())
            {
                PropertyDescription propertyDescription = itDeclaredProperties.next();
                createStaticClassHierarchyPropertyInfo(apiDescription,propertyDescription);
            }
        }
        // class
        itClasses = classes.iterator();
        while(itClasses.hasNext())
        {
            ClassDescription classDescription = (ClassDescription)itClasses.next();
            createStaticClassHierarchyClass(apiDescription,classDescription);
        }
        // hierarchy
        createStaticClassHierarchy(apiDescription);
   }
    
    private void createStaticClassFromTemplate(File targetFolder, String packageName, String className, final ApiDescription apiDescription) throws IOException
    {
        PlaceholderResolver placeholderResolver = new PlaceholderResolver() {
            public String resolve(String placeholder)
            {
                String templatePrefix = "template:";
                if(placeholder.startsWith(templatePrefix))
                {
                    String templatename = placeholder.substring(templatePrefix.length());
                    for(ClassDescription clazz : apiDescription.getDescribedClasses())
                    {
                        if(clazz.getApiName().equals(templatename))
                        {
                            ByteArrayOutputStream bos = new ByteArrayOutputStream();
                            PrintWriter out = new PrintWriter(bos);
                            JavaClassTemplateFileWriter classtemplateFileWriter = new JavaClassTemplateFileWriter(JavaApiWriter.this);
                            try
                            {
                                classtemplateFileWriter.writeSpecific(clazz, out);
                                out.flush();
                                bos.flush();
                                bos.close();
                            }
                            catch (IOException e)
                            {
                                throw new RuntimeException(e);
                            }
                            return new String(bos.toByteArray(), StandardCharsets.UTF_8);
                        }
                    }
                    throw new RuntimeException("No class defined with apiname: "+templatename);
                }
                throw new RuntimeException("Unknown placefolder: {{"+placeholder+"}}");
            }};
        copyTemplateToStream("statics/"+className,createJavaFile(targetFolder,packageName,className),placeholderResolver,true);
    }

    private void createStaticClassHierarchyPropertyInfo(ApiDescription apiDescription, PropertyDescription propertyDescription) throws IOException, ApiWriterException
    {
        String className = propertyDescription.getContainingClass().getOdmaName().getName();
        String propName = propertyDescription.getOdmaName().getName();
        OutputStream staticPropertyInfoStream = createJavaFile(opendmaApiSourceFolder,"org.opendma.impl.core","OdmaStaticSystemPropertyInfo"+className+propName);
        PrintWriter out = new PrintWriter(staticPropertyInfoStream);
        out.println("package org.opendma.impl.core;");
        out.println("");
        out.println("import java.util.ArrayList;");
        out.println("import java.util.Collections;");
        out.println("import org.opendma.api.OdmaCommonNames;");
        out.println("import org.opendma.api.OdmaType;");
        out.println("import org.opendma.api.OdmaObject;");
        out.println("import org.opendma.exceptions.OdmaAccessDeniedException;");
        out.println("import org.opendma.exceptions.OdmaInvalidDataTypeException;");
        out.println("import org.opendma.impl.OdmaPropertyImpl;");
        out.println("");
        out.println("public class OdmaStaticSystemPropertyInfo"+className+propName+" extends OdmaStaticSystemPropertyInfo {");
        out.println("");
        out.println("    public OdmaStaticSystemPropertyInfo"+className+propName+"() throws OdmaInvalidDataTypeException, OdmaAccessDeniedException {");
        // iterate through all properties defined in the propertyInfo class
        Iterator<PropertyDescription> itDeclaredPropertyInfoProperties = apiDescription.getPropertyInfoClass().getPropertyDescriptions().iterator();
        while(itDeclaredPropertyInfoProperties.hasNext())
        {
            PropertyDescription pd = itDeclaredPropertyInfoProperties.next();
            printPropertyInfoSystemProperty(out,apiDescription,propertyDescription,pd);
        }
        out.println("    }");
        out.println("");
        out.println("}");
        // close writer and streams
        out.close();
        staticPropertyInfoStream.close();
    }
    
    private void printPropertyInfoSystemProperty(PrintWriter out, ApiDescription apiDescription, PropertyDescription propertyDescription, PropertyDescription pd) throws IOException, ApiWriterException
    {
        String pn = pd.getOdmaName().getName().toUpperCase();
        String constantPropertyName = "PROPERTY_" + propertyDescription.getOdmaName().getName().toUpperCase();
        if(pn.equals("NAME"))
        {
            printX(out,"NAME","OdmaCommonNames."+constantPropertyName+".getName()","STRING");
        }
        else if(pn.equals("NAMESPACE"))
        {
            printX(out,"NAMESPACE","OdmaCommonNames."+constantPropertyName+".getNamespace()","STRING");
        }
        else if(pn.equals("QNAME"))
        {
            printX(out,"QNAME","OdmaCommonNames."+constantPropertyName,"QNAME");
        }
        else if(pn.equals("DISPLAYNAME"))
        {
            printX(out,"DISPLAYNAME","OdmaCommonNames."+constantPropertyName+".getName()","STRING");
        }
        else if(pn.equals("DATATYPE"))
        {
            ScalarTypeDescription scalarTypeDescription = propertyDescription.getDataType();
            printX(out,"DATATYPE","Integer.valueOf("+scalarTypeDescription.getNumericID()+")","INTEGER");
        }
        else if(pn.equals("REFERENCECLASS"))
        {
            printX(out,"REFERENCECLASS","null","REFERENCE");
        }
        else if(pn.equals("MULTIVALUE"))
        {
            printX(out,"MULTIVALUE",(propertyDescription.getMultiValue()?"Boolean.TRUE":"Boolean.FALSE"),"BOOLEAN");
        }
        else if(pn.equals("REQUIRED"))
        {
            printX(out,"REQUIRED",(propertyDescription.getRequired()?"Boolean.TRUE":"Boolean.FALSE"),"BOOLEAN");
        }
        else if(pn.equals("READONLY"))
        {
            printX(out,"READONLY",(propertyDescription.isReadOnly()?"Boolean.TRUE":"Boolean.FALSE"),"BOOLEAN");
        }
        else if(pn.equals("HIDDEN"))
        {
            printX(out,"HIDDEN",(propertyDescription.getHidden()?"Boolean.TRUE":"Boolean.FALSE"),"BOOLEAN");
        }
        else if(pn.equals("SYSTEM"))
        {
            printX(out,"SYSTEM",(propertyDescription.getSystem()?"Boolean.TRUE":"Boolean.FALSE"),"BOOLEAN");
        }
        else if(pn.equals("CHOICES"))
        {
            printXMultivalue(out,"CHOICES","Collections.unmodifiableList(new ArrayList<OdmaObject>(0))","REFERENCE");
        }
        else
        {
            throw new ApiWriterException("The PropertyInfo class declares a property ("+pd.getOdmaName()+") that has been unknown when this ApiWriter has been implemented. Thus this version of the ApiWriter does not know how to create the static content of this PropertyInfo property for the predefined system classes. Please extend the if/else block in the ApiWriter that threw this Exception.");
        }
    }

    private void printX(PrintWriter out, String propertyNameConstant, String value, String typeConstantName)
    {
        out.println("        properties.put(OdmaCommonNames.PROPERTY_"+propertyNameConstant+",OdmaPropertyImpl.fromValue(OdmaCommonNames.PROPERTY_"+propertyNameConstant+","+value+",OdmaType."+typeConstantName+",false,true));");        
    }
    
    private void printXMultivalue(PrintWriter out, String propertyNameConstant, String value, String typeConstantName)
    {
        out.println("        properties.put(OdmaCommonNames.PROPERTY_"+propertyNameConstant+",OdmaPropertyImpl.fromValue(OdmaCommonNames.PROPERTY_"+propertyNameConstant+","+value+",OdmaType."+typeConstantName+",true,true));");        
    }

    protected void createStaticClassHierarchyClass(ApiDescription apiDescription, ClassDescription classDescription) throws IOException, ApiWriterException
    {
        String className = classDescription.getOdmaName().getName();
        OutputStream staticClassStream = createJavaFile(opendmaApiSourceFolder,"org.opendma.impl.core","OdmaStaticSystemClass"+className);
        PrintWriter out = new PrintWriter(staticClassStream);
        out.println("package org.opendma.impl.core;");
        out.println("");
        out.println("import org.opendma.api.OdmaClass;");
        out.println("import org.opendma.api.OdmaCommonNames;");
        out.println("import org.opendma.api.OdmaType;");
        out.println("import org.opendma.api.OdmaPropertyInfo;");
        out.println("import org.opendma.exceptions.OdmaAccessDeniedException;");
        out.println("import org.opendma.exceptions.OdmaInvalidDataTypeException;");
        out.println("import org.opendma.impl.OdmaPropertyImpl;");
        out.println("");
        out.println("public class OdmaStaticSystemClass"+className+" extends OdmaStaticSystemClass");
        out.println("{");
        out.println("");
        out.println("    public OdmaStaticSystemClass"+className+"(OdmaStaticSystemClass superClass, Iterable<OdmaClass> subClasses, Iterable<OdmaClass> aspects, Iterable<OdmaPropertyInfo> declaredProperties, boolean retrievable, boolean searchable) throws OdmaInvalidDataTypeException, OdmaAccessDeniedException");
        out.println("    {");
        out.println("        super(subClasses);");
        // iterate through all properties defined in the propertyInfo class
        Iterator<PropertyDescription> itDeclaredClassProperties = apiDescription.getClassClass().getPropertyDescriptions().iterator();
        while(itDeclaredClassProperties.hasNext())
        {
            PropertyDescription pd = itDeclaredClassProperties.next();
            printClassSystemProperty(out,apiDescription,classDescription,pd);
        }
        out.println("        buildProperties();");
        out.println("    }");
        out.println("");
        out.println("}");
        // close writer and streams
        out.close();
        staticClassStream.close();
    }
    
    private void printClassSystemProperty(PrintWriter out, ApiDescription apiDescription, ClassDescription classDescription, PropertyDescription pd) throws IOException, ApiWriterException
    {
        String pn = pd.getOdmaName().getName().toUpperCase();
        String constantClassName = "CLASS_" + classDescription.getOdmaName().getName().toUpperCase();
        if(pn.equals("NAME"))
        {
            printX(out,"NAME","OdmaCommonNames."+constantClassName+".getName()","STRING");
        }
        else if(pn.equals("NAMESPACE"))
        {
            printX(out,"NAMESPACE","OdmaCommonNames."+constantClassName+".getNamespace()","STRING");
        }
        else if(pn.equals("QNAME"))
        {
            printX(out,"QNAME","OdmaCommonNames."+constantClassName,"QNAME");
        }
        else if(pn.equals("DISPLAYNAME"))
        {
            printX(out,"DISPLAYNAME","OdmaCommonNames."+constantClassName+".getName()","STRING");
        }
        else if(pn.equals("SUPERCLASS"))
        {
            printX(out,"SUPERCLASS","superClass","REFERENCE");
        }
        else if(pn.equals("PARENT"))
        {
            printX(out,"PARENT","parent","REFERENCE");
        }
        else if(pn.equals("ASPECTS"))
        {
            printXMultivalue(out,"ASPECTS","aspects","REFERENCE");
        }
        else if(pn.equals("DECLAREDPROPERTIES"))
        {
            printXMultivalue(out,"DECLAREDPROPERTIES","declaredProperties","REFERENCE");
        }
        else if(pn.equals("INSTANTIABLE"))
        {
            printX(out,"INSTANTIABLE",(classDescription.getInstantiable()?"Boolean.TRUE":"Boolean.FALSE"),"BOOLEAN");
        }
        else if(pn.equals("HIDDEN"))
        {
            printX(out,"HIDDEN",(classDescription.getHidden()?"Boolean.TRUE":"Boolean.FALSE"),"BOOLEAN");
        }
        else if(pn.equals("SYSTEM"))
        {
            printX(out,"SYSTEM",(classDescription.getSystem()?"Boolean.TRUE":"Boolean.FALSE"),"BOOLEAN");
        }
        else if(pn.equals("ASPECT"))
        {
            printX(out,"ASPECT",(classDescription.getAspect()?"Boolean.TRUE":"Boolean.FALSE"),"BOOLEAN");
        }
        else if(pn.equals("RETRIEVABLE"))
        {
            printX(out,"RETRIEVABLE","(retrievable?Boolean.TRUE:Boolean.FALSE)","BOOLEAN");
        }
        else if(pn.equals("SEARCHABLE"))
        {
            printX(out,"SEARCHABLE","(searchable?Boolean.TRUE:Boolean.FALSE)","BOOLEAN");
        }
        else if(pn.equals("PROPERTIES"))
        {
            // the properties property is created by the buildProperties() method
        }
        else if(pn.equals("SUBCLASSES"))
        {
            // the subclasses property is created in the super constructor
        }
        else
        {
            throw new ApiWriterException("The Class class declares a property ("+pd.getOdmaName()+") that has been unknown when this ApiWriter has been implemented. Thus this version of the ApiWriter does not know how to create the static content of this Class property for the predefined system classes. Please extend the if/else block in the ApiWriter that threw this Exception.");
        }
    }

    private void createStaticClassHierarchy(ApiDescription apiDescription) throws IOException
    {
        OutputStream staticClassStream = createJavaFile(opendmaApiSourceFolder,"org.opendma.impl.core","OdmaStaticClassHierarchy");
        copyTemplateToStream("statics/OdmaStaticClassHierarchy.head",staticClassStream,false);
        PrintWriter out = new PrintWriter(staticClassStream);
        out.println("    public void buildClassHierarchy() throws OdmaInvalidDataTypeException, OdmaAccessDeniedException");
        out.println("    {");
        out.println("        ArrayList<OdmaClass> declaredAspects;");
        out.println("        ArrayList<OdmaPropertyInfo> declaredProperties;");
        out.println("        OdmaStaticSystemClass ssc;");
        out.println("");
        Iterator<ClassDescription> itClassDescriptions = apiDescription.getDescribedClasses().iterator();
        HashMap<String, Boolean> uniquePropMap = new HashMap<String, Boolean>();
        while(itClassDescriptions.hasNext())
        {
            ClassDescription classDescription = itClassDescriptions.next();
            String className = classDescription.getOdmaName().getName();
            String constantClassName = "CLASS_" + className.toUpperCase();
            Iterator<PropertyDescription> itPropertyDescriptions = classDescription.getPropertyDescriptions().iterator();
            while(itPropertyDescriptions.hasNext())
            {
                PropertyDescription propertyDescription = itPropertyDescriptions.next();
                String propName = propertyDescription.getOdmaName().getName();
                String constantPropertyName = "PROPERTY_" + propName.toUpperCase();
                if(uniquePropMap.containsKey(constantPropertyName))
                    continue;
                uniquePropMap.put(constantPropertyName,Boolean.TRUE);
                out.println("        registerPropertyInfo(OdmaCommonNames."+constantClassName+", OdmaCommonNames."+constantPropertyName+", new OdmaStaticSystemPropertyInfo"+className+propName+"());");
            }
        }
        itClassDescriptions = apiDescription.getDescribedClasses().iterator();
        while(itClassDescriptions.hasNext())
        {
            ClassDescription classDescription = (ClassDescription)itClassDescriptions.next();
            String className = classDescription.getOdmaName().getName();
            String constantClassName = "CLASS_" + className.toUpperCase();
            out.println("");
            out.println("        declaredAspects = new ArrayList<OdmaClass>();");
            out.println("        declaredProperties = new ArrayList<OdmaPropertyInfo>();");
            Iterator<PropertyDescription> itPropertyDescriptions = classDescription.getPropertyDescriptions().iterator();
            while(itPropertyDescriptions.hasNext())
            {
                PropertyDescription propertyDescription = itPropertyDescriptions.next();
                String propName = propertyDescription.getOdmaName().getName();
                String constantPropertyName = "PROPERTY_" + propName.toUpperCase();
                out.println("        declaredProperties.add(getPropertyInfo(OdmaCommonNames."+constantClassName+", OdmaCommonNames."+constantPropertyName+"));");
            }
            String parentClassExpression = (classDescription.getExtendsOdmaName()==null) ? "null" : "getClassInfo(OdmaCommonNames.CLASS_"+classDescription.getExtendsOdmaName().getName().toUpperCase()+")";
            out.println("        ssc = new OdmaStaticSystemClass"+className+"("+parentClassExpression+", getSubClasses(OdmaCommonNames."+constantClassName+"), Collections.unmodifiableList(declaredAspects), Collections.unmodifiableList(declaredProperties), getRetrievable(OdmaCommonNames."+constantClassName+"), getSearchable(OdmaCommonNames."+constantClassName+"));");
            if(classDescription.getExtendsOdmaName() != null)
            {
                out.println("        registerSubClass(OdmaCommonNames.CLASS_"+classDescription.getExtendsOdmaName().getName().toUpperCase()+", ssc);");
            }
            else
            {
                // only register root aspects
                if(classDescription.getAspect())
                {
                    out.println("        registerRootAspect(ssc);");
                }
            }
            out.println("        classInfos.put(OdmaCommonNames."+constantClassName+", ssc);");
        }
        out.println("");
        out.println("        OdmaClass propertyInfoClass = getClassInfo(OdmaCommonNames.CLASS_PROPERTYINFO);");
        out.println("        Iterator<OdmaStaticSystemPropertyInfo> itPropertyInfos = propertyInfos.values().iterator();");
        out.println("        while(itPropertyInfos.hasNext())");
        out.println("        {");
        out.println("            OdmaStaticSystemPropertyInfo pi = itPropertyInfos.next();");
        out.println("            pi.patchClass(propertyInfoClass);");
        out.println("        }");
        out.println("        OdmaClass classClass = getClassInfo(OdmaCommonNames.CLASS_CLASS);");
        out.println("        Iterator<OdmaStaticSystemClass> itClassInfos = classInfos.values().iterator();");
        out.println("        while(itClassInfos.hasNext())");
        out.println("        {");
        out.println("            OdmaStaticSystemClass ci = itClassInfos.next();");
        out.println("            ci.patchClass(classClass);");
        out.println("        }");
        out.println("");
        itClassDescriptions = apiDescription.getDescribedClasses().iterator();
        uniquePropMap.clear();
        while(itClassDescriptions.hasNext())
        {
            ClassDescription classDescription = (ClassDescription)itClassDescriptions.next();
            String className = classDescription.getOdmaName().getName();
            String constantClassName = "CLASS_" + className.toUpperCase();
            Iterator<PropertyDescription> itPropertyDescriptions = classDescription.getPropertyDescriptions().iterator();
            while(itPropertyDescriptions.hasNext())
            {
                PropertyDescription propertyDescription = itPropertyDescriptions.next();
                if(!propertyDescription.getDataType().isReference())
                    continue;
                String propName = propertyDescription.getOdmaName().getName();
                String constantPropertyName = "PROPERTY_" + propName.toUpperCase();
                if(uniquePropMap.containsKey(constantPropertyName))
                    continue;
                uniquePropMap.put(constantPropertyName,Boolean.TRUE);
                out.println("        getPropertyInfo(OdmaCommonNames."+constantClassName+", OdmaCommonNames."+constantPropertyName+").patchReferenceClass(getClassInfo(OdmaCommonNames.CLASS_"+propertyDescription.getReferenceClassName().getName().toUpperCase()+"));");
            }
        }
        out.println("    }");
        out.println("");
        out.println("}");
        // close writer and streams
        out.close();
        staticClassStream.close();
    }

    private void createProxyFactory(ApiDescription apiDescription) throws IOException
    {
        PlaceholderResolver placeholderResolver = new PlaceholderResolver() {
            public String resolve(String placeholder)
            {
                if("imports".equals(placeholder))
                {
                    StringBuilder sb = new StringBuilder();
                    for(ClassDescription clazz : apiDescription.getDescribedClasses())
                    {
                        if(sb.length() > 0)
                        {
                            sb.append(System.lineSeparator());
                        }
                        sb.append("import org.opendma.api."+clazz.getApiName()+";");
                    }
                    return sb.toString();
                    
                } else if("mapping".equals(placeholder))
                {
                    StringBuilder sb = new StringBuilder();
                    for(ClassDescription clazz : apiDescription.getDescribedClasses())
                    {
                        if(sb.length() > 0)
                        {
                            sb.append(System.lineSeparator());
                        }
                        sb.append("        INTERFACE_MAP.put(OdmaCommonNames.CLASS_"+clazz.getOdmaName().getName().toUpperCase()+", "+clazz.getApiName()+".class);");
                    }
                    return sb.toString();
                }
                throw new RuntimeException("Unknown placefolder: {{"+placeholder+"}}");
            }};
        copyTemplateToStream("OdmaProxyFactory",createJavaFile(opendmaApiSourceFolder,"org.opendma.impl","OdmaProxyFactory"),placeholderResolver,true);
        placeholderResolver = new PlaceholderResolver() {
            public String resolve(String placeholder)
            {
                if("mapping".equals(placeholder))
                {
                    HashSet<String> propSet = new HashSet<String>();
                    StringBuilder sb = new StringBuilder();
                    for(ClassDescription clazz : apiDescription.getDescribedClasses())
                    {
                        for(PropertyDescription prop : clazz.getPropertyDescriptions())
                        {
                            if(propSet.contains(prop.getApiName()))
                            {
                                continue;
                            }
                            if(sb.length() > 0)
                            {
                                sb.append(System.lineSeparator());
                            }
                            String getterPrefix = (!prop.getDataType().isReference()) && "boolean".equalsIgnoreCase(getScalarDataType(prop.getDataType(),prop.getMultiValue(),prop.getRequired())) ? "is" : "get";
                            String getterName = getterPrefix + prop.getApiName();
                            sb.append("        PROPERTY_MAP.put(\""+getterName+"\", new PropertyMapping(OdmaCommonNames.PROPERTY_"+prop.getOdmaName().getName().toUpperCase()+", OdmaType."+prop.getDataType().getName().toUpperCase()+", "+(prop.getMultiValue()?"true":"false")+"));");
                            if( (!prop.isReadOnly()) && (!prop.getMultiValue()) )
                            {
                                if(sb.length() > 0)
                                {
                                    sb.append(System.lineSeparator());
                                }
                                String setterName = "set" + prop.getApiName();
                                sb.append("        PROPERTY_MAP.put(\""+setterName+"\", new PropertyMapping(OdmaCommonNames.PROPERTY_"+prop.getOdmaName().getName().toUpperCase()+", OdmaType."+prop.getDataType().getName().toUpperCase()+", "+(prop.getMultiValue()?"true":"false")+"));");
                            }
                            propSet.add(prop.getApiName());
                        }
                    }
                    return sb.toString();
                }
                if("switch-multivalue".equals(placeholder))
                {
                    StringBuilder sb = new StringBuilder();
                    for(ScalarTypeDescription scalarType : apiDescription.getScalarTypes())
                    {
                        if(sb.length() > 0)
                        {
                            sb.append(System.lineSeparator());
                        }
                        sb.append("            case "+scalarType.getName().toUpperCase()+":");
                        sb.append(System.lineSeparator());
                        sb.append("                return property.get"+scalarType.getName()+(scalarType.isReference()?"Iterable":"List")+"();");
                    }
                    return sb.toString();
                }
                if("switch-singlevalue".equals(placeholder))
                {
                    StringBuilder sb = new StringBuilder();
                    for(ScalarTypeDescription scalarType : apiDescription.getScalarTypes())
                    {
                        if(sb.length() > 0)
                        {
                            sb.append(System.lineSeparator());
                        }
                        sb.append("            case "+scalarType.getName().toUpperCase()+":");
                        sb.append(System.lineSeparator());
                        sb.append("                return property.get"+scalarType.getName()+"();");
                    }
                    return sb.toString();
                }
                throw new RuntimeException("Unknown placefolder: {{"+placeholder+"}}");
            }};
        copyTemplateToStream("OdmaProxyHandler",createJavaFile(opendmaApiSourceFolder,"org.opendma.impl","OdmaProxyHandler"),placeholderResolver,true);
    }

}