package org.opendma.apibuilder.apiwriter.java5;

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

public class Java5ApiWriter extends AbstractApiWriter
{

    public String getName()
    {
        return "Java5";
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
        return "java5";
    }
    
    public String getProgrammingLanguageSpecificScalarDataType(boolean multiValue, int dataType)
    {
        if(multiValue)
        {
            switch(dataType)
            {
            case OdmaBasicTypes.TYPE_STRING:
                return "List<String>";
            case OdmaBasicTypes.TYPE_INTEGER:
                return "List<Integer>";
            case OdmaBasicTypes.TYPE_SHORT:
                return "List<Short>";
            case OdmaBasicTypes.TYPE_LONG:
                return "List<Long>";
            case OdmaBasicTypes.TYPE_FLOAT:
                return "List<Float>";
            case OdmaBasicTypes.TYPE_DOUBLE:
                return "List<Double>";
            case OdmaBasicTypes.TYPE_BOOLEAN:
                return "List<Boolean>";
            case OdmaBasicTypes.TYPE_DATETIME:
                return "List<Date>";
            case OdmaBasicTypes.TYPE_BLOB:
                return "List<byte[]>";
            case OdmaBasicTypes.TYPE_REFERENCE:
                throw new ApiCreationException("REFERENCE data type is not scalar");
            case OdmaBasicTypes.TYPE_CONTENT:
                return "List<OdmaContent>";
            case OdmaBasicTypes.TYPE_ID:
                return "List<OdmaId>";
            case OdmaBasicTypes.TYPE_GUID:
                return "List<OdmaGuid>";
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
            default:
                throw new ApiCreationException("Unhandled data type "+dataType);
            }
        }
    }

    public String[] getRequiredScalarDataTypeImports(boolean multiValue, int dataType)
    {
        if(multiValue)
        {
            switch(dataType)
            {
            case OdmaBasicTypes.TYPE_STRING:
                return new String[] { "java.util.List" };
            case OdmaBasicTypes.TYPE_INTEGER:
                return new String[] { "java.util.List" };
            case OdmaBasicTypes.TYPE_SHORT:
                return new String[] { "java.util.List" };
            case OdmaBasicTypes.TYPE_LONG:
                return new String[] { "java.util.List" };
            case OdmaBasicTypes.TYPE_FLOAT:
                return new String[] { "java.util.List" };
            case OdmaBasicTypes.TYPE_DOUBLE:
                return new String[] { "java.util.List" };
            case OdmaBasicTypes.TYPE_BOOLEAN:
                return new String[] { "java.util.List" };
            case OdmaBasicTypes.TYPE_DATETIME:
                return new String[] { "java.util.List", "java.util.Date" };
            case OdmaBasicTypes.TYPE_BLOB:
                return new String[] { "java.util.List" };
            case OdmaBasicTypes.TYPE_REFERENCE:
                throw new ApiCreationException("REFERENCE data type is not scalar");
            case OdmaBasicTypes.TYPE_CONTENT:
                return new String[] { "java.util.List", "org.opendma.api.OdmaContent" };
            case OdmaBasicTypes.TYPE_ID:
                return new String[] { "java.util.List", "org.opendma.api.OdmaId" };
            case OdmaBasicTypes.TYPE_GUID:
                return new String[] { "java.util.List", "org.opendma.api.OdmaGuid" };
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
                return new String[] { "java.util.Date" };
            case OdmaBasicTypes.TYPE_BLOB:
                return null;
            case OdmaBasicTypes.TYPE_REFERENCE:
                throw new ApiCreationException("REFERENCE data type is not scalar");
            case OdmaBasicTypes.TYPE_CONTENT:
                return new String[] { "org.opendma.api.OdmaContent" };
            case OdmaBasicTypes.TYPE_ID:
                return new String[] { "org.opendma.api.OdmaId" };
            case OdmaBasicTypes.TYPE_GUID:
                return new String[] { "org.opendma.api.OdmaGuid" };
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
        // CHECKTEMPLATE: OdmaObjectTemplate
        copyStaticClassHelperTemplate("OdmaStaticSystemObject",baseFolder);
        // CHECKTEMPLATE: OdmaPropertyInfoTemplate
        copyStaticClassHelperTemplate("OdmaStaticSystemPropertyInfo",baseFolder);
        // CHECKTEMPLATE: OdmaClassTemplate
        copyStaticClassHelperTemplate("OdmaStaticSystemClass",baseFolder);
        // CHECKTEMPLATE: OdmaRepositoryTemplate
        copyStaticClassHelperTemplate("OdmaStaticSystemRepository",baseFolder);
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
            createStaticClassHierarchyHelperClass(apiDescription,classDescription,baseFolder);
        }
        // hierarchy
        createStaticClassHierarchyHelper(apiDescription,baseFolder);
    }

    private void copyStaticClassHelperTemplate(String className, String outputFolder) throws IOException
    {
        OutputStream to = createJavaFile(outputFolder,"org.opendma.impl.core",className);
        InputStream from = getResourceAsStream("/templates/java5/statics/"+className+".template");
        streamCopy(from, to);
        from.close();
        to.close();
    }

    private void createStaticClassHierarchyHelperProperty(ApiDescription apiDescription, PropertyDescription propertyDescription, String outputFolder) throws IOException, ApiWriterException
    {
        String propName = propertyDescription.getOdmaName().getName();
        OutputStream staticPropertyInfoStream = createJavaFile(outputFolder,"org.opendma.impl.core","OdmaStaticSystemPropertyInfo"+propName);
        PrintWriter out = new PrintWriter(staticPropertyInfoStream);
        out.println("package org.opendma.impl.core;");
        out.println("");
        out.println("import org.opendma.api.OdmaCommonNames;");
        out.println("import org.opendma.api.OdmaType;");
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
            printX(out,"NAME","OdmaCommonNames."+constantPropertyName+".getName()","STRING");
        }
        else if(pn.equals("NAMEQUALIFIER"))
        {
            printX(out,"NAMEQUALIFIER","OdmaCommonNames."+constantPropertyName+".getQualifier()","STRING");
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
            ScalarTypeDescription scalarTypeDescription = apiDescription.getScalarTypeDescription(propertyDescription.getDataType());
            printX(out,"DATATYPE","new Integer("+scalarTypeDescription.getNumericID()+")","INTEGER");
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
        out.println("        properties.put(OdmaCommonNames.PROPERTY_"+propertyNameConstant+",new OdmaPropertyImpl(OdmaCommonNames.PROPERTY_"+propertyNameConstant+","+value+",OdmaType."+typeConstantName+",false,true));");        
    }
    
    private void printXMultivalue(PrintWriter out, String propertyNameConstant, String value, String typeConstantName)
    {
        out.println("        properties.put(OdmaCommonNames.PROPERTY_"+propertyNameConstant+",new OdmaPropertyImpl(OdmaCommonNames.PROPERTY_"+propertyNameConstant+","+value+",OdmaType."+typeConstantName+",true,true));");        
    }

    protected void createStaticClassHierarchyHelperClass(ApiDescription apiDescription, ClassDescription classDescription, String outputFolder) throws IOException, ApiWriterException
    {
        String className = classDescription.getOdmaName().getName();
        OutputStream staticClassStream = createJavaFile(outputFolder,"org.opendma.impl.core","OdmaStaticSystemClass"+className);
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
        out.println("    public OdmaStaticSystemClass"+className+"(OdmaStaticSystemClass parent, Iterable<OdmaClass> subClasses, Iterable<OdmaClass> aspects, Iterable<OdmaPropertyInfo> declaredProperties, boolean retrievable, boolean searchable) throws OdmaInvalidDataTypeException, OdmaAccessDeniedException");
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
            printX(out,"NAME","OdmaCommonNames."+constantClassName+".getName()","STRING");
        }
        else if(pn.equals("NAMEQUALIFIER"))
        {
            printX(out,"NAMEQUALIFIER","OdmaCommonNames."+constantClassName+".getQualifier()","STRING");
        }
        else if(pn.equals("QNAME"))
        {
            printX(out,"QNAME","OdmaCommonNames."+constantClassName,"QNAME");
        }
        else if(pn.equals("DISPLAYNAME"))
        {
            printX(out,"DISPLAYNAME","OdmaCommonNames."+constantClassName+".getName()","STRING");
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

    private void createStaticClassHierarchyHelper(ApiDescription apiDescription, String outputFolder) throws IOException
    {
        OutputStream staticClassStream = createJavaFile(outputFolder,"org.opendma.impl.core","OdmaStaticClassHierarchy");
        PrintWriter out = new PrintWriter(staticClassStream);
        InputStream templateIn = AbstractApiWriter.getResourceAsStream("/templates/java5/statics/OdmaStaticClassHierarchy.head.template");
        BufferedReader templareReader = new BufferedReader(new InputStreamReader(templateIn));
        String templateLine = null;
        while( (templateLine = templareReader.readLine()) != null)
        {
            out.println(templateLine);
        }
        out.println("    public void buildClassHierarchy() throws OdmaInvalidDataTypeException, OdmaAccessDeniedException");
        out.println("    {");
        out.println("        ArrayList<OdmaClass> declaredAspects;");
        out.println("        ArrayList<OdmaPropertyInfo> declaredProperties;");
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
                out.println("        propertyInfos.put(OdmaCommonNames."+constantPropertyName+", new OdmaStaticSystemPropertyInfo"+propName+"());");
            }
        }
        itClassDescriptions = apiDescription.getDescribedClasses().iterator();
        while(itClassDescriptions.hasNext())
        {
            ClassDescription classDescription = (ClassDescription)itClassDescriptions.next();
            String className = classDescription.getOdmaName().getName();
            String constantClassName = "CLASS_" + className.toUpperCase();
            out.println("");
            out.println("        declaredAspects = null;");
            if(classDescription.getPropertyDescriptions().isEmpty())
            {
                out.println("        declaredProperties = null;");
            }
            else
            {
                out.println("        declaredProperties = new ArrayList<OdmaPropertyInfo>();");
            }
            Iterator itPropertyDescriptions = classDescription.getPropertyDescriptions().iterator();
            while(itPropertyDescriptions.hasNext())
            {
                PropertyDescription propertyDescription = (PropertyDescription)itPropertyDescriptions.next();
                String propName = propertyDescription.getOdmaName().getName();
                String constantPropertyName = "PROPERTY_" + propName.toUpperCase();
                out.println("        declaredProperties.add(getPropertyInfo(OdmaCommonNames."+constantPropertyName+"));");
            }
            String parentClassExpression = (classDescription.getExtendsOdmaName()==null) ? "null" : "getClassInfo(OdmaCommonNames.CLASS_"+classDescription.getExtendsOdmaName().getName().toUpperCase()+")";
            out.println("        ssc = new OdmaStaticSystemClass"+className+"("+parentClassExpression+",getSubClasses(OdmaCommonNames."+constantClassName+"),declaredAspects,declaredProperties,getRetrievable(OdmaCommonNames."+constantClassName+"),getSearchable(OdmaCommonNames."+constantClassName+"));");
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
                out.println("        getPropertyInfo(OdmaCommonNames."+constantPropertyName+").patchReferenceClass(getClassInfo(OdmaCommonNames.CLASS_"+propertyDescription.getReferenceClassName().getName().toUpperCase()+"));");
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

    protected void createDataTypesFile(ApiDescription apiDescription, String outputFolder) throws IOException
    {
        // create type enumeration
        PrintWriter out = new PrintWriter(createJavaFile(outputFolder,"org.opendma.api","OdmaType"));
        out.println("package org.opendma.api;");
        out.println();
        out.println("/**");
        out.println(" * OpenDMA property data types.");
        out.println(" *");
        out.println(" * @author Stefan Kopf, xaldon Technologies GmbH, the OpenDMA architecture board");
        out.println(" */");
        out.println("public enum OdmaType");
        out.println("{");
        out.println();
        List scalarTypes = apiDescription.getScalarTypes();
        Iterator itScalarTypes = scalarTypes.iterator();
        while(itScalarTypes.hasNext())
        {
            ScalarTypeDescription scalarTypeDescription = (ScalarTypeDescription)itScalarTypes.next();
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
        out.println("}");
        out.close();
    }

    protected void createConstantsFile(ApiDescription apiDescription, String outputFolder) throws IOException
    {
        // create common names file
        Java5ConstantsFileWriter constantsFileWriter = new Java5ConstantsFileWriter();
        constantsFileWriter.createConstantsFile(apiDescription, createJavaFile(outputFolder,"org.opendma.api","OdmaCommonNames"));
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

    protected void createSearchResultFile(ApiDescription apiDescription, String outputFolder) throws IOException
    {
        OutputStream to = getBasicFileStream("OdmaSearchResult",outputFolder);
        InputStream from = getResourceAsStream("/templates/java5/OdmaSearchResult.template");
        streamCopy(from, to);
        from.close();
        to.close();
    }

    protected void createExceptionFiles(ApiDescription apiDescription, String outputFolder) throws IOException
    {
        internalCreateExceptionFile(outputFolder,"OdmaException");
        internalCreateExceptionFile(outputFolder,"OdmaObjectNotFoundException");
        internalCreateExceptionFile(outputFolder,"OdmaInvalidDataTypeException");
        internalCreateExceptionFile(outputFolder,"OdmaAccessDeniedException");
        internalCreateExceptionFile(outputFolder,"OdmaRuntimeException");
        internalCreateExceptionFile(outputFolder,"OdmaQuerySyntaxException");
        internalCreateExceptionFile(outputFolder,"OdmaSearchException");
    }
    
    protected void internalCreateExceptionFile(String outputFolder, String exceptionClassName) throws IOException
    {
        OutputStream to = createJavaFile(outputFolder,"org.opendma.exceptions",exceptionClassName);
        InputStream from = getResourceAsStream("/templates/java5/"+exceptionClassName+".template");
        streamCopy(from,to);
        from.close();
        to.close();
    }

    protected void createSessionManagementFiles(ApiDescription apiDescription, String outputFolder) throws IOException
    {
        internalCreateSessionManagementFile(outputFolder,"OdmaDataSource");
        internalCreateSessionManagementFile(outputFolder,"OdmaSession");
    }
    
    protected void internalCreateSessionManagementFile(String outputFolder, String className) throws IOException
    {
        OutputStream to = createJavaFile(outputFolder,"org.opendma",className);
        InputStream from = getResourceAsStream("/templates/java/"+className+".template");
        streamCopy(from,to);
        from.close();
        to.close();
    }

    //-------------------------------------------------------------------------
    // P R O P E R T Y   F I L E
    //-------------------------------------------------------------------------

    protected void createPropertyFile(ApiDescription apiDescription, String outputFolder) throws IOException
    {
        Java5PropertyFileWriter javaPropertyFileWriter = new Java5PropertyFileWriter(this);
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
        Java5ClassFileWriter classFileWriter = new Java5ClassFileWriter(this);
        classFileWriter.createClassFile(classDescription, getClassFileStream(outputFolder,classDescription));
    }
    
    //-------------------------------------------------------------------------
    // C O L L E C T I O N   F I L E S
    //-------------------------------------------------------------------------
    
    protected void createEnumerationFile(ClassDescription classDescription, String baseFolder) throws IOException
    {
        // We are using generics in the form of Iterable<OdmaObject>. There is no need for enumeration files
    }

    protected void createListFile(ScalarTypeDescription scalarTypeDescription, String baseFolder) throws IOException
    {
        // We are using generics in the form of List<Object>. There is no need for list files
    }
    
    //-------------------------------------------------------------------------
    // I M P L E M E N T A T I O N   F I L E S
    //-------------------------------------------------------------------------

    protected void createPropertyImplementationFile(ApiDescription apiDescription, String outputFolder) throws IOException
    {
        Java5PropertyImplementationFileWriter javaPropertyImplementationFileWriter = new Java5PropertyImplementationFileWriter(this);
        javaPropertyImplementationFileWriter.createPropertyFile(apiDescription, createJavaFile(outputFolder,"org.opendma.impl","OdmaPropertyImpl"));
    }

    protected void createListImplementationFile(ScalarTypeDescription scalarTypeDescription, String baseFolder) throws IOException
    {
        // We are using generics in the form of List<Object>. There is no need for list files
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
       Java5ClassTemplateFileWriter classtemplateFileWriter = new Java5ClassTemplateFileWriter(this);
       classtemplateFileWriter.createClassFile(classDescription, getClassTemplateFileStream(outputFolder,classDescription));
    }
   
    //-------------------------------------------------------------------------
    // B U I L D   F I L E
    //-------------------------------------------------------------------------
    
    protected void createBuildFile(ApiDescription apiDescription, String baseFolder) throws IOException
    {
        // we do not create a build file for now. We use Eclipse.
    }
    
    //-------------------------------------------------------------------------
    // S T A T I C   H E L P E R
    //-------------------------------------------------------------------------
    
    public static boolean needToImportPackage(String importDeclaration, String intoPackage) {
        boolean notNeeded = importDeclaration.startsWith(intoPackage+".") && importDeclaration.substring(intoPackage.length()+1).indexOf(".") < 0;
        return !notNeeded;
    }

}