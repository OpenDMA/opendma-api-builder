package org.opendma.apibuilder.apiwriter.java;

import java.io.PrintWriter;

import org.opendma.apibuilder.OdmaApiWriter;
import org.opendma.apibuilder.OdmaBasicTypes;
import org.opendma.apibuilder.apiwriter.AbstractClassFileWriter;
import org.opendma.apibuilder.structure.ClassDescription;
import org.opendma.apibuilder.structure.PropertyDescription;

public class JavaClassFileWriter extends AbstractClassFileWriter
{
    
    protected OdmaApiWriter apiWriter;
    
    public JavaClassFileWriter(OdmaApiWriter writer)
    {
        apiWriter = writer;
    }

    protected void writeClassFileHeader(ClassDescription classDescription, PrintWriter out)
    {
        String extendsApiName = classDescription.getExtendsApiName();
        out.println("package org.opendma.api;");
        out.println("");
        out.println("import org.opendma.api.OdmaQName;");
        out.println("");
        out.println("/**");
        if(extendsApiName != null)
        {
            out.println(" * The <i>"+classDescription.getOdmaName().getName()+"</i> specific version of the <code>{@link "+extendsApiName+"}</code> interface");
            out.println(" * that offers short cuts to all defined OpenDMA properties.<p>");
            out.println(" * ");
        }
        String classComment = classDescription.getDescription();
        out.println(" * "+((classComment==null)?"No description of this class available.":classComment));
        out.println(" * ");
        out.println(" * @author Stefan Kopf, xaldon Technologies GmbH, the OpenDMA architecture board");
        out.println(" */");
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
        if(property.getDataType() == OdmaBasicTypes.TYPE_REFERENCE)
        {
            if(property.getMultiValue())
            {
                return property.getContainingClass().getContainingApiDescription().getDescribedClass(property.getReferenceClassName()).getApiName()+"Enumeration";
            }
            else
            {
                return property.getContainingClass().getContainingApiDescription().getDescribedClass(property.getReferenceClassName()).getApiName();
            }
        }
        else
        {
            return apiWriter.getProgrammingLanguageSpecificScalarDataType(property.getMultiValue(),property.getDataType());
        }
        
        /*
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
        */
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
        out.println("     * ");
        out.println("     * @return "+property.getAbstract());
        out.println("     */");
        out.println("    public "+javaDataType+" get"+property.getApiName()+"();");
    }

}
