package org.opendma.apibuilder.apiwriter.java;

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

import org.opendma.apibuilder.OdmaBasicTypes;
import org.opendma.apibuilder.apiwriter.AbstractApiWriter;
import org.opendma.apibuilder.apiwriter.ApiCreationException;
import org.opendma.apibuilder.apiwriter.ApiWriterException;
import org.opendma.apibuilder.structure.ApiDescription;
import org.opendma.apibuilder.structure.ClassDescription;
import org.opendma.apibuilder.structure.PropertyDescription;
import org.opendma.apibuilder.structure.ScalarTypeDescription;

public class JavaApiWriter extends AbstractApiWriter
{

    public String getName()
    {
        return "Java";
    }
    
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
    
    protected String getProgrammingLanguageSpecificFolderName()
    {
        return "java";
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
                return "org.opendma.api.collections.DateTimeList";
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
    
    public void writeOdmaApi(ApiDescription apiDescription, String outputFolderRoot) throws IOException, ApiWriterException
    {
        // do the job
        super.writeOdmaApi(apiDescription, outputFolderRoot);
        // create base folder
        String baseFolder = outputFolderRoot;
        if(!baseFolder.endsWith(File.separator))
        {
            baseFolder = baseFolder + File.separator;
        }
        baseFolder = baseFolder + getProgrammingLanguageSpecificFolderName();
        baseFolder = baseFolder + File.separator;
        // copy static helper templates
        copyStaticClassHelperTemplate("OdmaArrayListClassEnumeration",baseFolder);
        copyStaticClassHelperTemplate("OdmaArrayListObjectEnumeration",baseFolder);
        copyStaticClassHelperTemplate("OdmaArrayListPropertyInfoEnumeration",baseFolder);
        copyStaticClassHelperTemplate("OdmaStaticSystemObject",baseFolder);
        copyStaticClassHelperTemplate("OdmaStaticSystemPropertyInfo",baseFolder);
        copyStaticClassHelperTemplate("OdmaStaticSystemClass",baseFolder);
        copyStaticClassHelperTemplate("OdmaStaticSystemRepository",baseFolder);
        copyStaticClassHelperTemplate("OdmaCoreId",baseFolder);
        copyStaticClassHelperTemplate("OdmaCoreGuid",baseFolder);
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
                createStaticClassHierarchyHelperProperty(apiDescription,propertyDescription,baseFolder);
            }
        }
        // class
        itClasses = classes.iterator();
        while(itClasses.hasNext())
        {
            ClassDescription classDescription = (ClassDescription)itClasses.next();
            createStaticClassHierarchyHelperClass(classDescription,baseFolder);
        }
        // hierarchy
        createStaticClassHierarchyHelper(apiDescription,baseFolder);
    }

    private void copyStaticClassHelperTemplate(String className, String outputFolder) throws IOException
    {
        OutputStream to = createJavaFile(outputFolder,"org.opendma.impl.core",className);
        InputStream from = getResourceAsStream("/templates/java/statics/"+className+".template");
        streamCopy(from, to);
        from.close();
        to.close();
    }

    private void createStaticClassHierarchyHelperProperty(ApiDescription apiDescription, PropertyDescription propertyDescription, String outputFolder) throws IOException
    {
        String propName = propertyDescription.getOdmaName().getName();
        OutputStream staticPropertyInfoStream = createJavaFile(outputFolder,"org.opendma.impl.core","OdmaStaticSystemPropertyInfo"+propName);
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
        String constantPropertyName = "PROPERTY_" + propName.toUpperCase();
        ScalarTypeDescription scalarTypeDescription = apiDescription.getScalarTypeDescription(propertyDescription.getDataType());
        String constantScalarTypeName = "TYPE_" + scalarTypeDescription.getName().toUpperCase();
        printX(out,"NAME","OdmaTypes."+constantPropertyName+".getName()","STRING");
        printX(out,"NAMEQUALIFIER","OdmaTypes."+constantPropertyName+".getQualifier()","STRING");
        printX(out,"DISPLAYNAME","OdmaTypes."+constantPropertyName+".getName()","STRING");
        printX(out,"DATATYPE","new Integer(OdmaTypes."+constantScalarTypeName+")","INTEGER");
        printX(out,"REFERENCECLASS","null","REFERENCE");
        printX(out,"MULTIVALUE",(propertyDescription.getMultiValue()?"Boolean.TRUE":"Boolean.FALSE"),"BOOLEAN");
        printX(out,"REQUIRED",(propertyDescription.getRequired()?"Boolean.TRUE":"Boolean.FALSE"),"BOOLEAN");
        printX(out,"READONLY",(propertyDescription.isReadOnly()?"Boolean.TRUE":"Boolean.FALSE"),"BOOLEAN");
        printX(out,"HIDDEN",(propertyDescription.getHidden()?"Boolean.TRUE":"Boolean.FALSE"),"BOOLEAN");
        printX(out,"SYSTEM",(propertyDescription.getSystem()?"Boolean.TRUE":"Boolean.FALSE"),"BOOLEAN");
        out.println("    }");
        out.println("");
        out.println("}");
        // close writer and streams
        out.close();
        staticPropertyInfoStream.close();
    }
    
    private void printX(PrintWriter out, String propertyNameConstant, String value, String typeConstantName)
    {
        out.println("        properties.put(OdmaTypes.PROPERTY_"+propertyNameConstant+",new OdmaPropertyImpl(OdmaTypes.PROPERTY_"+propertyNameConstant+","+value+",OdmaTypes.TYPE_"+typeConstantName+",false,true));");        
    }
    
    private void printX2(PrintWriter out, String propertyNameConstant, String value, String typeConstantName)
    {
        out.println("        properties.put(OdmaTypes.PROPERTY_"+propertyNameConstant+",new OdmaPropertyImpl(OdmaTypes.PROPERTY_"+propertyNameConstant+","+value+",OdmaTypes.TYPE_"+typeConstantName+",true,true));");        
    }

    protected void createStaticClassHierarchyHelperClass(ClassDescription classDescription, String outputFolder) throws IOException
    {
        String className = classDescription.getOdmaName().getName();
        OutputStream staticClassStream = createJavaFile(outputFolder,"org.opendma.impl.core","OdmaStaticSystemClass"+className);
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
        out.println("    public OdmaStaticSystemClass"+className+"(OdmaStaticSystemClass parent, OdmaClassEnumeration subClasses, OdmaClassEnumeration aspects, OdmaPropertyInfoEnumeration declaredProperties) throws OdmaInvalidDataTypeException, OdmaAccessDeniedException");
        out.println("    {");
        out.println("        super(parent,subClasses);");
        String constantClassName = "CLASS_" + className.toUpperCase();
        printX(out,"NAME","OdmaTypes."+constantClassName+".getName()","STRING");
        printX(out,"NAMEQUALIFIER","OdmaTypes."+constantClassName+".getQualifier()","STRING");
        printX(out,"QNAME","OdmaTypes."+constantClassName,"QNAME");
        printX(out,"DISPLAYNAME","OdmaTypes."+constantClassName+".getName()","STRING");
        printX(out,"PARENT","parent","REFERENCE");
        printX2(out,"ASPECTS","aspects","REFERENCE");
        printX2(out,"DECLAREDPROPERTIES","declaredProperties","REFERENCE");
        printX(out,"INSTANTIABLE",(classDescription.getInstantiable()?"Boolean.TRUE":"Boolean.FALSE"),"BOOLEAN");
        printX(out,"HIDDEN",(classDescription.getHidden()?"Boolean.TRUE":"Boolean.FALSE"),"BOOLEAN");
        printX(out,"SYSTEM",(classDescription.getSystem()?"Boolean.TRUE":"Boolean.FALSE"),"BOOLEAN");
        out.println("        buildProperties();");
        out.println("    }");
        out.println("");
        out.println("}");
        // close writer and streams
        out.close();
        staticClassStream.close();
    }
    
    private void createStaticClassHierarchyHelper(ApiDescription apiDescription, String outputFolder) throws IOException
    {
        OutputStream staticClassStream = createJavaFile(outputFolder,"org.opendma.impl.core","OdmaStaticClassHierarchy");
        PrintWriter out = new PrintWriter(staticClassStream);
        InputStream templateIn = AbstractApiWriter.getResourceAsStream("/templates/java/statics/OdmaStaticClassHierarchy.head.template");
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
            out.println("        declaredAspects = new OdmaArrayListClassEnumeration();");
            out.println("        declaredProperties = new OdmaArrayListPropertyInfoEnumeration();");
            Iterator itPropertyDescriptions = classDescription.getPropertyDescriptions().iterator();
            while(itPropertyDescriptions.hasNext())
            {
                PropertyDescription propertyDescription = (PropertyDescription)itPropertyDescriptions.next();
                String propName = propertyDescription.getOdmaName().getName();
                String constantPropertyName = "PROPERTY_" + propName.toUpperCase();
                out.println("        declaredProperties.add(getPropertyInfo(OdmaTypes."+constantPropertyName+"));");
            }
            String parentClassExpression = (classDescription.getExtendsOdmaName()==null) ? "null" : "getClassInfo(OdmaTypes.CLASS_"+classDescription.getExtendsOdmaName().getName().toUpperCase()+")";
            out.println("        ssc = new OdmaStaticSystemClass"+className+"("+parentClassExpression+",getSubClassesEnumeration(OdmaTypes."+constantClassName+"),declaredAspects,declaredProperties);");
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
                if(propertyDescription.getDataType() != OdmaBasicTypes.TYPE_REFERENCE)
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

    //-------------------------------------------------------------------------
    // C O N S T A N T S   F I L E
    //-------------------------------------------------------------------------

    protected OutputStream getConstantsFileStream(String outputFolder) throws IOException
    {
        return createJavaFile(outputFolder,"org.opendma","OdmaTypes");
    }

    protected void createConstantsFile(ApiDescription apiDescription, String outputFolder) throws IOException
    {
        JavaConstantsFileWriter constantsFileWriter = new JavaConstantsFileWriter();
        constantsFileWriter.createConstantsFile(apiDescription, getConstantsFileStream(outputFolder));
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
        internalCreateExceptionFile(outputFolder,"OdmaRuntimeException");
    }
    
    protected void internalCreateExceptionFile(String outputFolder, String exceptionClassName) throws IOException
    {
        OutputStream to = createJavaFile(outputFolder,"org.opendma.exceptions",exceptionClassName);
        InputStream from = getResourceAsStream("/templates/java/"+exceptionClassName+".template");
        streamCopy(from,to);
        from.close();
        to.close();
    }

    //-------------------------------------------------------------------------
    // P R O P E R T Y   F I L E
    //-------------------------------------------------------------------------

    protected void createPropertyFile(ApiDescription apiDescription, String outputFolder) throws IOException
    {
        JavaPropertyFileWriter javaPropertyFileWriter = new JavaPropertyFileWriter(this);
        javaPropertyFileWriter.createPropertyFile(apiDescription, getBasicFileStream("OdmaProperty",outputFolder));
    }

    //-------------------------------------------------------------------------
    // C L A S S   F I L E
    //-------------------------------------------------------------------------

    protected OutputStream getClassFileStream(String outputFolder, ClassDescription classDescription) throws IOException
    {
        return createJavaFile(outputFolder,"org.opendma.api",classDescription.getApiName());
    }

    protected void createClassFile(ClassDescription classDescription, String outputFolder) throws IOException
    {
        JavaClassFileWriter classFileWriter = new JavaClassFileWriter(this);
        classFileWriter.createClassFile(classDescription, getClassFileStream(outputFolder,classDescription));
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
        JavaEnumerationFileWriter enumerationFileWriter = new JavaEnumerationFileWriter();
        enumerationFileWriter.createEnumerationFile(classDescription, getEnumerationFileStream(baseFolder,classDescription));
    }

    protected OutputStream getListFileStream(String baseFolder, ScalarTypeDescription scalarTypeDescription) throws IOException
    {
        return createJavaFile(baseFolder,"org.opendma.api.collections",getProgrammingLanguageSpecificScalarDataType(true,scalarTypeDescription.getNumericID()));
    }

    protected void createListFile(ScalarTypeDescription scalarTypeDescription, String baseFolder) throws IOException
    {
        JavaListFileWriter listFileWriter = new JavaListFileWriter(this);
        listFileWriter.createListFile(scalarTypeDescription, getListFileStream(baseFolder,scalarTypeDescription));
    }
    
    //-------------------------------------------------------------------------
    // I M P L E M E N T A T I O N   F I L E S
    //-------------------------------------------------------------------------

    protected void createPropertyImplementationFile(ApiDescription apiDescription, String outputFolder) throws IOException
    {
        JavaPropertyImplementationFileWriter javaPropertyImplementationFileWriter = new JavaPropertyImplementationFileWriter(this);
        javaPropertyImplementationFileWriter.createPropertyFile(apiDescription, createJavaFile(outputFolder,"org.opendma.impl","OdmaPropertyImpl"));
    }

    protected OutputStream getListImplementationFileStream(String baseFolder, ScalarTypeDescription scalarTypeDescription) throws IOException
    {
        return createJavaFile(baseFolder,"org.opendma.impl.collections","Array"+getProgrammingLanguageSpecificScalarDataType(true,scalarTypeDescription.getNumericID()));
    }

    protected void createListImplementationFile(ScalarTypeDescription scalarTypeDescription, String baseFolder) throws IOException
    {
        JavaListImplementationFileWriter listImplementationFileWriter = new JavaListImplementationFileWriter(this);
        listImplementationFileWriter.createListFile(scalarTypeDescription, getListImplementationFileStream(baseFolder,scalarTypeDescription));
    }

    //-------------------------------------------------------------------------
    // C L A S S   T E M P L A T E S
    //-------------------------------------------------------------------------

    protected OutputStream getClassTemplateFileStream(String outputFolder, ClassDescription classDescription) throws IOException
    {
        return createJavaFile(outputFolder,"org.opendma.templates",classDescription.getApiName()+"Template");
    }

    protected void createClassTemplateFile(ClassDescription classDescription, String outputFolder) throws IOException
    {
       JavaClassTemplateFileWriter classtemplateFileWriter = new JavaClassTemplateFileWriter(this);
       classtemplateFileWriter.createClassFile(classDescription, getClassTemplateFileStream(outputFolder,classDescription));
    }
   
    //-------------------------------------------------------------------------
    // B U I L D   F I L E
    //-------------------------------------------------------------------------
    
    protected void createBuildFile(ApiDescription apiDescription, String baseFolder) throws IOException
    {
        // we do not create a build file for now. We use Eclipse.
    }

}