package org.opendma.apibuilder.apiwriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

import org.opendma.apibuilder.OdmaBasicTypes;
import org.opendma.apibuilder.structure.ApiDescription;
import org.opendma.apibuilder.structure.ClassDescription;
import org.opendma.apibuilder.structure.PropertyDescription;
import org.opendma.apibuilder.structure.ScalarTypeDescription;

public class OdmaJavaApiWriter extends OdmaAbstractApiWriter
{
    
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

    //-------------------------------------------------------------------------
    // C O N S T A N T S   F I L E
    //-------------------------------------------------------------------------

    protected OutputStream getConstantsFileStream(String outputFolder) throws IOException
    {
        return createJavaFile(outputFolder,"org.opendma","OdmaTypes");
    }

    protected void writeConstantsFileHeader(ApiDescription apiDescription, PrintWriter out)
    {
        out.println("package org.opendma;");
        out.println("");
        out.println("import org.opendma.api.OdmaQName;");
        out.println("");
        out.println("/**");
        out.println(" * Static declaration of all type constants used in the OpenDMA spcification.");
        out.println(" *");
        out.println(" * @author Stefan Kopf, xaldon Technologies GmbH, the OpenDMA architecture board");
        out.println(" */");
        out.println("public class OdmaTypes");
        out.println("{");
    }

    protected void writeConstantsClassesSectionHeader(ApiDescription apiDescription, PrintWriter out)
    {
        out.println("");
        out.println("    // =============================================================================================");
        out.println("    // Declaration of pre-defined class names and property names");
        out.println("    // =============================================================================================");
    }

    protected void writeConstantsScalarTypeConstant(ScalarTypeDescription scalarTypeDescription, String constantName, PrintWriter out)
    {
        out.println("");
        out.println("    /**");
        out.println("     * Numeric type identifier of the data type <code>"+scalarTypeDescription.getName()+"</code>.<br>");
        out.println("     * Value is "+scalarTypeDescription.getNumericID()+".");
        out.println("     */");
        out.println("    public final static int "+constantName+" = "+scalarTypeDescription.getNumericID()+";");
    }

    protected void writeConstantsTypesSectionHeader(ApiDescription apiDescription, PrintWriter out)
    {
        out.println("");
        out.println("    // =============================================================================================");
        out.println("    // Declaration of numeric data type IDs");
        out.println("    // =============================================================================================");
    }

    protected void writeConstantsClassSeperator(ClassDescription classDescription, PrintWriter out)
    {
        StringBuffer sb = new StringBuffer("    // -----< class ");
        sb.append(classDescription.getOdmaName().getName());
        sb.append(" >");
        while(sb.length() < 100)
        {
            sb.append('-');
        }
        out.println("");
        out.println(sb.toString());
    }

    protected void writeConstantsClassnameConstant(ClassDescription classDescription, String constantName, PrintWriter out)
    {
        out.println("");
        out.println("    /** qualified name of the OpenDMA system class <code>"+classDescription.getOdmaName().getName()+"</code> */");
        out.println("    public final static OdmaQName "+constantName+" = new OdmaQName(\""+classDescription.getOdmaName().getQualifier()+"\", \""+classDescription.getOdmaName().getName()+"\");");
    }

    protected void writeConstantsPropertynameConstant(PropertyDescription propertyDescription, String constantName, PrintWriter out)
    {
        out.println("");
        out.println("    /** qualified name of the OpenDMA system property <code>"+propertyDescription.getOdmaName().getName()+"</code> */");
        out.println("    public final static OdmaQName "+constantName+" = new OdmaQName(\""+propertyDescription.getOdmaName().getQualifier()+"\", \""+propertyDescription.getOdmaName().getName()+"\");");
    }

    protected void writeConstantsBackrefPropertynameHint(PropertyDescription propertyDescription, String propertyName, PrintWriter out)
    {
        out.println("");
        out.println("    // Property "+propertyDescription.getOdmaName().getName()+" already defined previously");
    }

    protected void writeConstantsFileFooter(ApiDescription apiDescription, PrintWriter out)
    {
        out.println("");
        out.println("    // =============================================================================================");
        out.println("    // TECHNICAL: empty private default constructor to prevent instantiation");
        out.println("    // =============================================================================================");
        out.println("");
        out.println("    /**");
        out.println("     * Empty private constructor to prevent instantiation of this class.");
        out.println("     */");
        out.println("    private OdmaTypes()");
        out.println("    {");
        out.println("        // empty constructor");
        out.println("    }");
        out.println("");
        out.println("}");
    }
    
    //-------------------------------------------------------------------------
    // C L A S S   F I L E
    //-------------------------------------------------------------------------

    protected OutputStream getClassFileStream(String outputFolder, ClassDescription classDescription) throws IOException
    {
        return createJavaFile(outputFolder,"org.opendma.api",classDescription.getApiName());
    }

    protected void writeClassFileHeader(ClassDescription classDescription, PrintWriter out)
    {
        out.println("package org.opendma;");
        out.println("");
        out.println("import org.opendma.api.OdmaQName;");
        out.println("");
        out.println("/**");
        String classComment = classDescription.getDescription();
        out.println(" * "+((classComment==null)?"No description of this class available.":classComment));
        out.println(" *");
        out.println(" * @author Stefan Kopf, xaldon Technologies GmbH, the OpenDMA architecture board");
        out.println(" */");
        String extendsApiName = classDescription.getExtendsApiName();
        if(extendsApiName != null)
        {
            out.println("public interface "+classDescription.getApiName()+" extends "+extendsApiName);
        }
        else
        {
            out.println("public interface "+classDescription.getApiName());
        }
        out.println("{");
    }

    protected void writeClassFileFooter(ClassDescription classDescription, PrintWriter out)
    {
        out.println("");
        out.println("}");
    }

    protected void writeClassObjectSpecificPropertyAccessSectionHeader(ClassDescription classDescription, PrintWriter out)
    {
        out.println("");
        out.println("    // =============================================================================================");
        out.println("    // Object specific property access");
        out.println("    // =============================================================================================");
    }

    private String getReturnDataType(PropertyDescription property)
    {
        if(property.getMultiValue())
        {
            switch(property.getDataType())
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
                return property.getContainingClass().getContainingApiDescription().getDescribedClass(property.getReferenceClassName()).getApiName()+"Enumeration";
            case OdmaBasicTypes.TYPE_CONTENT:
                return "OdmaContentList";
            case OdmaBasicTypes.TYPE_ID:
                return "OdmaIdList";
            case OdmaBasicTypes.TYPE_GUID:
                return "OdmaGuidList";
            default:
                throw new ApiCreationException("Unhandled data type "+Integer.toString(property.getDataType())+" for property "+property.getOdmaName());
            }
        }
        else
        {
            switch(property.getDataType())
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
                return "DateTime";
            case OdmaBasicTypes.TYPE_BLOB:
                return "byte[]";
            case OdmaBasicTypes.TYPE_REFERENCE:
                return property.getContainingClass().getContainingApiDescription().getDescribedClass(property.getReferenceClassName()).getApiName();
            case OdmaBasicTypes.TYPE_CONTENT:
                return "OdmaContent";
            case OdmaBasicTypes.TYPE_ID:
                return "OdmaId";
            case OdmaBasicTypes.TYPE_GUID:
                return "OdmaGuid";
            default:
                throw new ApiCreationException("Unhandled data type "+Integer.toString(property.getDataType())+" for property "+property.getOdmaName());
            }
        }
    }

    protected void writeClassPropertyAccess(PropertyDescription property, PrintWriter out)
    {
        // getter
        String javaDataType = getReturnDataType(property);
        String constantPropertyName = "PROPERTY_" + property.getOdmaName().getName().toUpperCase();
        out.println("");
        out.println("    /**");
        out.println("     * Returns "+property.getAbstract()+".<br>");
        String standardGetterName = "get" + ((property.getDataType() != OdmaBasicTypes.TYPE_REFERENCE) ? javaDataType : (property.getMultiValue() ? "ObjectEnumeration" : "Object"));
        out.println("     * Shortcut for <code>getProperty(OdmaTypes."+constantPropertyName+")."+standardGetterName+"()</code>.");
        out.println("     *");
        out.println("     * @return "+property.getAbstract());
        out.println("     */");
        out.println("    public "+javaDataType+" get"+property.getApiName()+"();");
    }

}