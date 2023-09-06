package org.opendma.apibuilder.apiwriter.java14;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.opendma.apibuilder.apiwriter.AbstractApiWriter;
import org.opendma.apibuilder.apiwriter.ApiWriterException;
import org.opendma.apibuilder.structure.ApiDescription;
import org.opendma.apibuilder.structure.ClassDescription;
import org.opendma.apibuilder.structure.PropertyDescription;
import org.opendma.apibuilder.structure.ScalarTypeDescription;

public class Java14ApiWriter extends AbstractApiWriter
{

    public Java14ApiWriter(File outputFolderRoot) throws ApiWriterException
    {
        super(outputFolderRoot);
    }

    public String getName()
    {
        return "Java 1.4";
    }
    
    protected String getTargetFolderName()
    {
        return "java14";
    }

    //-------------------------------------------------------------------------
    // S O U R C E   F I L E   H E L P E R
    //-------------------------------------------------------------------------
    
    private OutputStream createJavaFile(String packageName, String className) throws IOException
    {
        // String packageDirectory = baseFolder.getAbsolutePath() + File.separatorChar + packageName.replace('.',File.separatorChar);
        // File containingDir = new File(packageDirectory);
        File containingDir = new File(baseFolder, packageName.replace('.',File.separatorChar));
        if(!containingDir.exists())
        {
            if(!containingDir.mkdirs())
            {
                throw new IOException("Can not create package directory");
            }
        }
        return new FileOutputStream(new File(containingDir, className+".java"));
    }
    
    private void createClassFromTemplate(String packageName, String className) throws IOException
    {
        copyTemplateToStream(className,createJavaFile(packageName,className));
    }

    //-------------------------------------------------------------------------
    // C O N S T A N T S   F I L E
    //-------------------------------------------------------------------------

    protected void createDataTypesFile(ApiDescription apiDescription) throws IOException
    {
        // part of constants file
    }

    protected OutputStream getConstantsFileStream() throws IOException
    {
        return createJavaFile("org.opendma","OdmaTypes");
    }

    protected void createConstantsFile(ApiDescription apiDescription) throws IOException
    {
        Java14ConstantsFileWriter constantsFileWriter = new Java14ConstantsFileWriter();
        constantsFileWriter.createConstantsFile(apiDescription, getConstantsFileStream());
    }

    //-------------------------------------------------------------------------
    // B A S I C   F I L E S
    //-------------------------------------------------------------------------

    protected void createQNameFile(ApiDescription apiDescription) throws IOException
    {
        createClassFromTemplate("org.opendma.api","OdmaQName");
    }

    protected void createIdFile(ApiDescription apiDescription) throws IOException
    {
        createClassFromTemplate("org.opendma.api","OdmaId");
    }

    protected void createGuidFile(ApiDescription apiDescription) throws IOException
    {
        createClassFromTemplate("org.opendma.api","OdmaGuid");
    }

    protected void createContentFile(ApiDescription apiDescription) throws IOException
    {
        createClassFromTemplate("org.opendma.api","OdmaContent");
    }

    protected void createSearchResultFile(ApiDescription apiDescription) throws IOException
    {
        createClassFromTemplate("org.opendma.api","OdmaSearchResult");
    }

    protected void createExceptionFiles(ApiDescription apiDescription) throws IOException
    {
        createClassFromTemplate("org.opendma.exceptions","OdmaException");
        createClassFromTemplate("org.opendma.exceptions","OdmaObjectNotFoundException");
        createClassFromTemplate("org.opendma.exceptions","OdmaInvalidDataTypeException");
        createClassFromTemplate("org.opendma.exceptions","OdmaAccessDeniedException");
        createClassFromTemplate("org.opendma.exceptions","OdmaRuntimeException");
        createClassFromTemplate("org.opendma.exceptions","OdmaQuerySyntaxException");
        createClassFromTemplate("org.opendma.exceptions","OdmaSearchException");
    }

    protected void createSessionManagementFiles(ApiDescription apiDescription) throws IOException
    {
        createClassFromTemplate("org.opendma","OdmaDataSource");
        createClassFromTemplate("org.opendma","OdmaSession");
    }

    //-------------------------------------------------------------------------
    // P R O P E R T Y   F I L E
    //-------------------------------------------------------------------------

    protected void createPropertyFile(ApiDescription apiDescription) throws IOException
    {
        Java14PropertyFileWriter javaPropertyFileWriter = new Java14PropertyFileWriter(this);
        javaPropertyFileWriter.createPropertyFile(apiDescription, createJavaFile("org.opendma.api","OdmaProperty"));
    }

    //-------------------------------------------------------------------------
    // C L A S S   F I L E
    //-------------------------------------------------------------------------

    protected void createClassFile(ClassDescription classDescription) throws IOException
    {
        Java14ClassFileWriter classFileWriter = new Java14ClassFileWriter(this);
        classFileWriter.createClassFile(classDescription, createJavaFile("org.opendma.api",classDescription.getApiName()));
    }
    
    //-------------------------------------------------------------------------
    // C O L L E C T I O N   F I L E S
    //-------------------------------------------------------------------------

    protected OutputStream getEnumerationFileStream(ClassDescription classDescription) throws IOException
    {
        return createJavaFile("org.opendma.api.collections",classDescription.getApiName()+"Enumeration");
    }
    
    protected void createEnumerationFile(ClassDescription classDescription) throws IOException
    {
        Java14EnumerationFileWriter enumerationFileWriter = new Java14EnumerationFileWriter();
        enumerationFileWriter.createEnumerationFile(classDescription, getEnumerationFileStream(classDescription));
    }

    protected OutputStream getListFileStream(ScalarTypeDescription scalarTypeDescription) throws IOException
    {
        return createJavaFile("org.opendma.api.collections",getScalarDataType(scalarTypeDescription,true,false));
    }

    protected void createListFile(ScalarTypeDescription scalarTypeDescription) throws IOException
    {
        Java14ListFileWriter listFileWriter = new Java14ListFileWriter(this);
        listFileWriter.createListFile(scalarTypeDescription, getListFileStream(scalarTypeDescription));
    }
    
    //-------------------------------------------------------------------------
    // I M P L E M E N T A T I O N   F I L E S
    //-------------------------------------------------------------------------

    protected void createPropertyImplementationFile(ApiDescription apiDescription) throws IOException
    {
        Java14PropertyImplementationFileWriter javaPropertyImplementationFileWriter = new Java14PropertyImplementationFileWriter(this);
        javaPropertyImplementationFileWriter.createPropertyFile(apiDescription, createJavaFile("org.opendma.impl","OdmaPropertyImpl"));
    }

    protected OutputStream getListImplementationFileStream(ScalarTypeDescription scalarTypeDescription) throws IOException
    {
        return createJavaFile("org.opendma.impl.collections","Array"+getScalarDataType(scalarTypeDescription,true,false));
    }

    protected void createListImplementationFile(ScalarTypeDescription scalarTypeDescription) throws IOException
    {
        Java14ListImplementationFileWriter listImplementationFileWriter = new Java14ListImplementationFileWriter(this);
        listImplementationFileWriter.createListFile(scalarTypeDescription, getListImplementationFileStream(scalarTypeDescription));
    }

    //-------------------------------------------------------------------------
    // C L A S S   T E M P L A T E S
    //-------------------------------------------------------------------------

    protected void createClassTemplateFile(ClassDescription classDescription) throws IOException
    {
       Java14ClassTemplateFileWriter classtemplateFileWriter = new Java14ClassTemplateFileWriter(this);
       classtemplateFileWriter.createClassFile(classDescription, createJavaFile("org.opendma.templates",classDescription.getApiName()+"Template"));
    }
   
    //-------------------------------------------------------------------------
    // B U I L D   F I L E
    //-------------------------------------------------------------------------
    
    protected void prepareProjectStructureAndBuildFiles(ApiDescription apiDescription) throws IOException
    {
    }
    
    protected void finaliseProjectStructureAndBuildFiles(ApiDescription apiDescription) throws IOException
    {
    }
    
    //-------------------------------------------------------------------------
    // S T A T I C   H E L P E R
    //-------------------------------------------------------------------------
    
    public static boolean needToImportPackage(String importDeclaration, String intoPackage) {
        boolean notNeeded = importDeclaration.startsWith(intoPackage+".") && importDeclaration.substring(intoPackage.length()+1).indexOf(".") < 0;
        return !notNeeded;
    }
    
    //-------------------------------------------------------------------------
    // E X T R A S
    //-------------------------------------------------------------------------
    
    protected void createExtras(ApiDescription apiDescription) throws IOException, ApiWriterException
    {
        // copy static helper templates
        copyStaticClassHelperTemplate("OdmaArrayListClassEnumeration");
        copyStaticClassHelperTemplate("OdmaArrayListObjectEnumeration");
        copyStaticClassHelperTemplate("OdmaArrayListPropertyInfoEnumeration");
        // CHECKTEMPLATE: OdmaObjectTemplate
        copyStaticClassHelperTemplate("OdmaStaticSystemObject");
        // CHECKTEMPLATE: OdmaPropertyInfoTemplate
        copyStaticClassHelperTemplate("OdmaStaticSystemPropertyInfo");
        // CHECKTEMPLATE: OdmaClassTemplate
        copyStaticClassHelperTemplate("OdmaStaticSystemClass");
        // CHECKTEMPLATE: OdmaRepositoryTemplate
        copyStaticClassHelperTemplate("OdmaStaticSystemRepository");
        // additionally create static class hierarchy helper files
        List classes = apiDescription.getDescribedClasses();
        // property
        Iterator itClasses = classes.iterator();
        while(itClasses.hasNext())
        {
            ClassDescription classDescription = (ClassDescription)itClasses.next();
            List declaredProperties = classDescription.getPropertyDescriptions();
            Iterator itDeclaredProperties = declaredProperties.iterator();
            while(itDeclaredProperties.hasNext())
            {
                PropertyDescription propertyDescription = (PropertyDescription)itDeclaredProperties.next();
                createStaticClassHierarchyHelperProperty(apiDescription,propertyDescription);
            }
        }
        // class
        itClasses = classes.iterator();
        while(itClasses.hasNext())
        {
            ClassDescription classDescription = (ClassDescription)itClasses.next();
            createStaticClassHierarchyHelperClass(apiDescription,classDescription);
        }
        // hierarchy
        createStaticClassHierarchyHelper(apiDescription);
   }

    private void copyStaticClassHelperTemplate(String className) throws IOException
    {
        OutputStream to = createJavaFile("org.opendma.impl.core",className);
        InputStream from = getTemplateAsStream("statics/"+className);
        streamCopy(from, to);
        from.close();
        to.close();
    }

    private void createStaticClassHierarchyHelperProperty(ApiDescription apiDescription, PropertyDescription propertyDescription) throws IOException, ApiWriterException
    {
        String propName = propertyDescription.getOdmaName().getName();
        OutputStream staticPropertyInfoStream = createJavaFile("org.opendma.impl.core","OdmaStaticSystemPropertyInfo"+propName);
        PrintWriter out = new PrintWriter(staticPropertyInfoStream);
        out.println("package org.opendma.impl.core;");
        out.println("");
        out.println("import org.opendma.OdmaTypes;");
        out.println("import org.opendma.exceptions.OdmaAccessDeniedException;");
        out.println("import org.opendma.exceptions.OdmaInvalidDataTypeException;");
        out.println("import org.opendma.impl.OdmaPropertyImpl;");
        out.println("");
        out.println("public class OdmaStaticSystemPropertyInfo"+propName+" extends OdmaStaticSystemPropertyInfo");
        out.println("{");
        out.println("");
        out.println("    public OdmaStaticSystemPropertyInfo"+propName+"() throws OdmaInvalidDataTypeException, OdmaAccessDeniedException");
        out.println("    {");
        // iterate through all properties defined in the propertyInfo class
        Iterator itDeclaredPropertyInfoProperties = apiDescription.getPropertyInfoClass().getPropertyDescriptions().iterator();
        while(itDeclaredPropertyInfoProperties.hasNext())
        {
            PropertyDescription pd = (PropertyDescription)itDeclaredPropertyInfoProperties.next();
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
            printX(out,"NAME","OdmaTypes."+constantPropertyName+".getName()","STRING");
        }
        else if(pn.equals("NAMEQUALIFIER"))
        {
            printX(out,"NAMEQUALIFIER","OdmaTypes."+constantPropertyName+".getQualifier()","STRING");
        }
        else if(pn.equals("QNAME"))
        {
            printX(out,"QNAME","OdmaTypes."+constantPropertyName,"QNAME");
        }
        else if(pn.equals("DISPLAYNAME"))
        {
            printX(out,"DISPLAYNAME","OdmaTypes."+constantPropertyName+".getName()","STRING");
        }
        else if(pn.equals("DATATYPE"))
        {
            ScalarTypeDescription scalarTypeDescription = propertyDescription.getDataType();
            String constantScalarTypeName = "TYPE_" + scalarTypeDescription.getName().toUpperCase();
            printX(out,"DATATYPE","new Integer(OdmaTypes."+constantScalarTypeName+")","INTEGER");
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
            printXMultivalue(out,"CHOICES","null","REFERENCE");
        }
        else
        {
            throw new ApiWriterException("The PropertyInfo class declares a property ("+pd.getOdmaName()+") that has been unknown when this ApiWriter has been implemented. Thus this version of the ApiWriter does not know how to create the static content of this PropertyInfo property for the predefined system classes. Please extend the if/else block in the ApiWriter that threw this Exception.");
        }
    }

    private void printX(PrintWriter out, String propertyNameConstant, String value, String typeConstantName)
    {
        out.println("        properties.put(OdmaTypes.PROPERTY_"+propertyNameConstant+",new OdmaPropertyImpl(OdmaTypes.PROPERTY_"+propertyNameConstant+","+value+",OdmaTypes.TYPE_"+typeConstantName+",false,true));");        
    }
    
    private void printXMultivalue(PrintWriter out, String propertyNameConstant, String value, String typeConstantName)
    {
        out.println("        properties.put(OdmaTypes.PROPERTY_"+propertyNameConstant+",new OdmaPropertyImpl(OdmaTypes.PROPERTY_"+propertyNameConstant+","+value+",OdmaTypes.TYPE_"+typeConstantName+",true,true));");        
    }

    protected void createStaticClassHierarchyHelperClass(ApiDescription apiDescription, ClassDescription classDescription) throws IOException, ApiWriterException
    {
        String className = classDescription.getOdmaName().getName();
        OutputStream staticClassStream = createJavaFile("org.opendma.impl.core","OdmaStaticSystemClass"+className);
        PrintWriter out = new PrintWriter(staticClassStream);
        out.println("package org.opendma.impl.core;");
        out.println("");
        out.println("import org.opendma.OdmaTypes;");
        out.println("import org.opendma.api.collections.OdmaClassEnumeration;");
        out.println("import org.opendma.api.collections.OdmaPropertyInfoEnumeration;");
        out.println("import org.opendma.exceptions.OdmaAccessDeniedException;");
        out.println("import org.opendma.exceptions.OdmaInvalidDataTypeException;");
        out.println("import org.opendma.impl.OdmaPropertyImpl;");
        out.println("");
        out.println("public class OdmaStaticSystemClass"+className+" extends OdmaStaticSystemClass");
        out.println("{");
        out.println("");
        out.println("    public OdmaStaticSystemClass"+className+"(OdmaStaticSystemClass parent, OdmaClassEnumeration subClasses, OdmaClassEnumeration aspects, OdmaPropertyInfoEnumeration declaredProperties, boolean retrievable, boolean searchable) throws OdmaInvalidDataTypeException, OdmaAccessDeniedException");
        out.println("    {");
        out.println("        super(parent,subClasses);");
        // iterate through all properties defined in the propertyInfo class
        Iterator itDeclaredClassProperties = apiDescription.getClassClass().getPropertyDescriptions().iterator();
        while(itDeclaredClassProperties.hasNext())
        {
            PropertyDescription pd = (PropertyDescription)itDeclaredClassProperties.next();
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
            printX(out,"NAME","OdmaTypes."+constantClassName+".getName()","STRING");
        }
        else if(pn.equals("NAMEQUALIFIER"))
        {
            printX(out,"NAMEQUALIFIER","OdmaTypes."+constantClassName+".getQualifier()","STRING");
        }
        else if(pn.equals("QNAME"))
        {
            printX(out,"QNAME","OdmaTypes."+constantClassName,"QNAME");
        }
        else if(pn.equals("DISPLAYNAME"))
        {
            printX(out,"DISPLAYNAME","OdmaTypes."+constantClassName+".getName()","STRING");
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

    private void createStaticClassHierarchyHelper(ApiDescription apiDescription) throws IOException
    {
        OutputStream staticClassStream = createJavaFile("org.opendma.impl.core","OdmaStaticClassHierarchy");
        PrintWriter out = new PrintWriter(staticClassStream);
        InputStream templateIn = getTemplateAsStream("statics/OdmaStaticClassHierarchy.head");
        BufferedReader templareReader = new BufferedReader(new InputStreamReader(templateIn));
        String templateLine = null;
        while( (templateLine = templareReader.readLine()) != null)
        {
            out.println(templateLine);
        }
        out.println("    public void buildClassHierarchy() throws OdmaInvalidDataTypeException, OdmaAccessDeniedException");
        out.println("    {");
        out.println("        OdmaArrayListClassEnumeration declaredAspects;");
        out.println("        OdmaArrayListPropertyInfoEnumeration declaredProperties;");
        out.println("        OdmaStaticSystemClass ssc;");
        out.println("");
        Iterator itClassDescriptions = apiDescription.getDescribedClasses().iterator();
        HashMap uniquePropMap = new HashMap();
        while(itClassDescriptions.hasNext())
        {
            ClassDescription classDescription = (ClassDescription)itClassDescriptions.next();
            Iterator itPropertyDescriptions = classDescription.getPropertyDescriptions().iterator();
            while(itPropertyDescriptions.hasNext())
            {
                PropertyDescription propertyDescription = (PropertyDescription)itPropertyDescriptions.next();
                String propName = propertyDescription.getOdmaName().getName();
                String constantPropertyName = "PROPERTY_" + propName.toUpperCase();
                if(uniquePropMap.containsKey(constantPropertyName))
                    continue;
                uniquePropMap.put(constantPropertyName,Boolean.TRUE);
                out.println("        propertyInfos.put(OdmaTypes."+constantPropertyName+", new OdmaStaticSystemPropertyInfo"+propName+"());");
            }
        }
        itClassDescriptions = apiDescription.getDescribedClasses().iterator();
        while(itClassDescriptions.hasNext())
        {
            ClassDescription classDescription = (ClassDescription)itClassDescriptions.next();
            String className = classDescription.getOdmaName().getName();
            String constantClassName = "CLASS_" + className.toUpperCase();
            out.println("");
            //out.println("        declaredAspects = new OdmaArrayListClassEnumeration();");
            out.println("        declaredAspects = null;");
            if(classDescription.getPropertyDescriptions().isEmpty())
            {
                out.println("        declaredProperties = null;");
            }
            else
            {
                out.println("        declaredProperties = new OdmaArrayListPropertyInfoEnumeration();");
            }
            Iterator itPropertyDescriptions = classDescription.getPropertyDescriptions().iterator();
            while(itPropertyDescriptions.hasNext())
            {
                PropertyDescription propertyDescription = (PropertyDescription)itPropertyDescriptions.next();
                String propName = propertyDescription.getOdmaName().getName();
                String constantPropertyName = "PROPERTY_" + propName.toUpperCase();
                out.println("        declaredProperties.add(getPropertyInfo(OdmaTypes."+constantPropertyName+"));");
            }
            String parentClassExpression = (classDescription.getExtendsOdmaName()==null) ? "null" : "getClassInfo(OdmaTypes.CLASS_"+classDescription.getExtendsOdmaName().getName().toUpperCase()+")";
            out.println("        ssc = new OdmaStaticSystemClass"+className+"("+parentClassExpression+",getSubClassesEnumeration(OdmaTypes."+constantClassName+"),declaredAspects,declaredProperties,getRetrievable(OdmaTypes."+constantClassName+"),getSearchable(OdmaTypes."+constantClassName+"));");
            if(classDescription.getExtendsOdmaName() != null)
            {
                out.println("        registerSubClass(OdmaTypes.CLASS_"+classDescription.getExtendsOdmaName().getName().toUpperCase()+", ssc);");
            }
            else
            {
                // only register root aspects
                if(classDescription.getAspect())
                {
                    out.println("        registerRootAspect(ssc);");
                }
            }
            out.println("        classInfos.put(OdmaTypes."+constantClassName+", ssc);");
        }
        out.println("");
        out.println("        OdmaClass propertyInfoClass = getClassInfo(OdmaTypes.CLASS_PROPERTYINFO);");
        out.println("        Iterator itPropertyInfos = propertyInfos.values().iterator();");
        out.println("        while(itPropertyInfos.hasNext())");
        out.println("        {");
        out.println("            OdmaStaticSystemPropertyInfo pi = (OdmaStaticSystemPropertyInfo)itPropertyInfos.next();");
        out.println("            pi.patchClass(propertyInfoClass);");
        out.println("        }");
        out.println("        OdmaClass classClass = getClassInfo(OdmaTypes.CLASS_CLASS);");
        out.println("        Iterator itClassInfos = classInfos.values().iterator();");
        out.println("        while(itClassInfos.hasNext())");
        out.println("        {");
        out.println("            OdmaStaticSystemClass ci = (OdmaStaticSystemClass)itClassInfos.next();");
        out.println("            ci.patchClass(classClass);");
        out.println("        }");
        out.println("");
        itClassDescriptions = apiDescription.getDescribedClasses().iterator();
        uniquePropMap.clear();
        while(itClassDescriptions.hasNext())
        {
            ClassDescription classDescription = (ClassDescription)itClassDescriptions.next();
            Iterator itPropertyDescriptions = classDescription.getPropertyDescriptions().iterator();
            while(itPropertyDescriptions.hasNext())
            {
                PropertyDescription propertyDescription = (PropertyDescription)itPropertyDescriptions.next();
                if(!propertyDescription.getDataType().isReference())
                    continue;
                String propName = propertyDescription.getOdmaName().getName();
                String constantPropertyName = "PROPERTY_" + propName.toUpperCase();
                if(uniquePropMap.containsKey(constantPropertyName))
                    continue;
                uniquePropMap.put(constantPropertyName,Boolean.TRUE);
                out.println("        getPropertyInfo(OdmaTypes."+constantPropertyName+").patchReferenceClass(getClassInfo(OdmaTypes.CLASS_"+propertyDescription.getReferenceClassName().getName().toUpperCase()+"));");
            }
        }
        out.println("    }");
        out.println("");
        out.println("}");
        // close writer and streams
        out.close();
        staticClassStream.close();
    }

}