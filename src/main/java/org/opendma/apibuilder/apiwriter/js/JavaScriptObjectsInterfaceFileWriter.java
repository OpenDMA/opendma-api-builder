package org.opendma.apibuilder.apiwriter.js;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import org.opendma.apibuilder.OdmaApiWriter;
import org.opendma.apibuilder.apiwriter.AbstractObjectsInterfaceFileWriter;
import org.opendma.apibuilder.apiwriter.ApiHelperWriter;
import org.opendma.apibuilder.apiwriter.ImportsList;
import org.opendma.apibuilder.structure.ApiHelperDescription;
import org.opendma.apibuilder.structure.ClassDescription;
import org.opendma.apibuilder.structure.PropertyDescription;
import org.opendma.apibuilder.structure.ScalarTypeDescription;

public class JavaScriptObjectsInterfaceFileWriter extends AbstractObjectsInterfaceFileWriter
{
    
    protected OdmaApiWriter apiWriter;
    
    public JavaScriptObjectsInterfaceFileWriter(OdmaApiWriter writer)
    {
        apiWriter = writer;
        this.apiHelperWriters.put("getQName", new ApiHelperWriter(){
            public void writeApiHelper(ClassDescription classDescription, ApiHelperDescription apiHelper, PrintWriter out)
            {
                // getter
                out.println("");
                out.println("    /**");
                out.println("     * Returns "+apiHelper.getAbstract()+".");
                out.println("     * "+apiHelper.getDescription());
                out.println("     * @returns {OdmaQName} "+apiHelper.getAbstract());
                out.println("     */");
                out.println("    getQName() {");
                out.println("        throw new Error(\"Method 'getQName' must be implemented.\");");
                out.println("    }");
            }
            public void appendRequiredImportsGlobal(ClassDescription classDescription, ApiHelperDescription apiHelper, ImportsList requiredImports)
            {
            }});
    }

    protected void writeClassFileHeader(ClassDescription classDescription, List<String> requiredImports, PrintWriter out)
    {
        String extendsApiName = classDescription.getExtendsApiName();
        out.println("");
        out.println("/**");
        if(extendsApiName != null)
        {
            out.println(" * The "+classDescription.getOdmaName().getName()+" specific version of the "+extendsApiName+" interface");
            out.println(" * offering short-cuts to all defined OpenDMA properties.");
        }
        String classComment = classDescription.getDescription();
        if(classComment != null)
        {
            if(extendsApiName != null)
            {
                out.println(" * ");
            }
            out.println(" * "+classComment);
        }
        out.println(" */");
        if(extendsApiName != null)
        {
            out.println("class "+classDescription.getApiName()+" extends "+extendsApiName+" {");
        }
        else
        {
            if(classDescription.getAspect())
            {
                out.println("class "+classDescription.getApiName()+" extends "+classDescription.getContainingApiDescription().getObjectClass().getApiName()+" {");
            }
            else
            {
                out.println("class "+classDescription.getApiName()+" extends OdmaCoreObject {");
            }
        }
    }

    protected void writeClassFileFooter(ClassDescription classDescription, PrintWriter out)
    {
        out.println("");
        out.println("}");
    }

    protected void appendRequiredImportsGlobal(ClassDescription classDescription, ImportsList requiredImports)
    {
        // we do not have any globally required imports
    }

    protected void writeClassGenericPropertyAccess(ClassDescription classDescription, PrintWriter out) throws IOException
    {
    }

    protected void appendRequiredImportsGenericPropertyAccess(ImportsList requiredImports)
    {
    }

    protected void writeClassObjectSpecificPropertyAccessSectionHeader(ClassDescription classDescription, PrintWriter out)
    {
    }

    protected String getReturnDataType(PropertyDescription property)
    {
        if(property.getDataType().isReference())
        {
            if(property.getMultiValue())
            {
                return "Iterable<"+property.getContainingClass().getContainingApiDescription().getDescribedClass(property.getReferenceClassName()).getApiName()+">";
            }
            else
            {
                return property.getContainingClass().getContainingApiDescription().getDescribedClass(property.getReferenceClassName()).getApiName();
            }
        }
        else
        {
            return apiWriter.getScalarDataType(property.getDataType(),property.getMultiValue(),property.getRequired());
        }
    }
    
    protected String[] getRequiredImports(PropertyDescription property)
    {
        return null;
    }

    protected void writeClassPropertyAccess(PropertyDescription property, PrintWriter out)
    {
        // generate names
        String jsCommentDataType = getReturnDataType(property);
        ScalarTypeDescription scalarType = property.getDataType();
        String constantPropertyName = "PROPERTY_" + property.getOdmaName().getName().toUpperCase();
        // getter
        out.println("");
        out.println("    /**");
        out.println("     * Returns "+property.getAbstract()+".<br>");
        String standardGetterName = "get" + ((!scalarType.isReference()) ? scalarType.getName() : (property.getMultiValue() ? "ReferenceIterable" : "Reference"));
        out.println("     * Shortcut for <code>getProperty(OdmaTypes."+constantPropertyName+")."+standardGetterName+"()</code>.");
        out.println("     * ");
        for(String s : getPropertyDetails(property, true))
        {
            out.println("     * "+s);
        }
        out.println("     * ");
        out.println("     * @abstract");
        out.println("     * @returns {"+jsCommentDataType+"} "+property.getAbstract());
        out.println("     */");
        out.println("    get"+property.getApiName()+"() {");
        out.println("        throw new Error(\"Method 'get"+property.getApiName()+"' must be implemented.\");");
        out.println("    }");
        // setter
        if( (!property.isReadOnly()) && (!property.getMultiValue()) )
        {
            out.println("");
            out.println("    /**");
            out.println("     * Sets "+property.getAbstract()+".<br>");
            String standardSetterName = "set" + ((!property.getDataType().isReference()) ? jsCommentDataType : (property.getMultiValue() ? "ObjectEnumeration" : "Object"));
            out.println("     * Shortcut for <code>getProperty(OdmaTypes."+constantPropertyName+")."+standardSetterName+"(value)</code>.");
            out.println("     * ");
            for(String s : getPropertyDetails(property, true))
            {
                out.println("     * "+s);
            }
            out.println("     * ");
            out.println("     * @abstract");
            out.println("     * @param {"+jsCommentDataType+"} newValue The new value for "+property.getAbstract());
            out.println("     * @throws {OdmaAccessDeniedError} If this OdmaProperty is read-only or cannot be set by the current user.");
            out.println("     */");
            out.println("    set"+property.getApiName()+"(newValue) {");
            out.println("        throw new Error(\"Method 'set"+property.getApiName()+"' must be implemented.\");");
            out.println("    }");
        }
    }

    protected void appendRequiredImportsClassPropertyAccess(ImportsList requiredImports, PropertyDescription property)
    {
        requiredImports.registerImports(getRequiredImports(property));
    }

}
