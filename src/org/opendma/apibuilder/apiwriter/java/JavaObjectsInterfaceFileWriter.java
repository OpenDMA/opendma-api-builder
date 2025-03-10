package org.opendma.apibuilder.apiwriter.java;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;

import org.opendma.apibuilder.apiwriter.AbstractObjectsInterfaceFileWriter;
import org.opendma.apibuilder.apiwriter.ApiHelperWriter;
import org.opendma.apibuilder.apiwriter.ImportsList;
import org.opendma.apibuilder.structure.ApiHelperDescription;
import org.opendma.apibuilder.structure.ClassDescription;
import org.opendma.apibuilder.structure.PropertyDescription;
import org.opendma.apibuilder.structure.ScalarTypeDescription;

public class JavaObjectsInterfaceFileWriter extends AbstractObjectsInterfaceFileWriter
{
    
    protected JavaApiWriter apiWriter;
    
    public JavaObjectsInterfaceFileWriter(JavaApiWriter writer)
    {
        apiWriter = writer;
        this.apiHelperWriters.put("getQName", new ApiHelperWriter(){
            public void writeApiHelper(ClassDescription classDescription, ApiHelperDescription apiHelper, PrintWriter out)
            {
                out.println("");
                out.println("    /**");
                out.println("     * Returns "+apiHelper.getAbstract()+".<br/>");
                out.println("     * "+apiHelper.getDescription());
                out.println("     * ");
                out.println("     * @return "+apiHelper.getAbstract());
                out.println("     */");
                out.println("    public OdmaQName getQName();");
            }
            public void appendRequiredImportsGlobal(ClassDescription classDescription, ApiHelperDescription apiHelper, List<String> requiredImports)
            {
                if(!requiredImports.contains("org.opendma.api.OdmaQName"))
                {
                    requiredImports.add("org.opendma.api.OdmaQName");
                }
            }});
    }

    protected void writeClassFileHeader(ClassDescription classDescription, List<String> requiredImports, PrintWriter out)
    {
        String extendsApiName = classDescription.getExtendsApiName();
        out.println("package org.opendma.api;");
        out.println("");
        Iterator<String> itRequiredImports = requiredImports.iterator();
        while(itRequiredImports.hasNext())
        {
            String importDeclaration = itRequiredImports.next();
            if(JavaApiWriter.needToImportPackage(importDeclaration,"org.opendma.api"))
            {
                out.println("import "+importDeclaration+";");
            }
        }
        out.println("");
        out.println("/**");
        if(extendsApiName != null)
        {
            out.println(" * The <i>"+classDescription.getOdmaName().getName()+"</i> specific version of the <code>{@link "+extendsApiName+"}</code> interface");
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
            out.println("public interface "+classDescription.getApiName()+" extends "+extendsApiName+" {");
        }
        else
        {
            if(classDescription.getAspect())
            {
                out.println("public interface "+classDescription.getApiName()+" extends "+classDescription.getContainingApiDescription().getObjectClass().getApiName()+" {");
            }
            else
            {
                out.println("public interface "+classDescription.getApiName()+" {");
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
        out.println("");
        out.println("    // ----- Generic property access ---------------------------------------------------------------");
        InputStream templateIn = apiWriter.getTemplateAsStream("OdmaObject.GenericPropertyAccess");
        BufferedReader templareReader = new BufferedReader(new InputStreamReader(templateIn));
        String templateLine = null;
        while( (templateLine = templareReader.readLine()) != null)
        {
            out.println(templateLine);
        }
    }

    protected void appendRequiredImportsGenericPropertyAccess(ImportsList requiredImports)
    {
        requiredImports.registerImport("org.opendma.exceptions.OdmaPropertyNotFoundException");
        requiredImports.registerImport("org.opendma.exceptions.OdmaInvalidDataTypeException");
        requiredImports.registerImport("org.opendma.exceptions.OdmaAccessDeniedException");
    }

    protected void writeClassObjectSpecificPropertyAccessSectionHeader(ClassDescription classDescription, PrintWriter out)
    {
        out.println("");
        out.println("    // ----- Object specific property access -------------------------------------------------------");
    }

    protected String getReturnDataType(PropertyDescription property)
    {
        if(property.getDataType().isReference())
        {
            if(property.getMultiValue())
            {
                return (apiWriter.supportNullability() ? "@NotNull " : "") + "Iterable<"+property.getContainingClass().getContainingApiDescription().getDescribedClass(property.getReferenceClassName()).getApiName()+">";
            }
            else
            {
                return (apiWriter.supportNullability() ? (property.getRequired() ? "@NotNull " : "@Nullable ") : "") + property.getContainingClass().getContainingApiDescription().getDescribedClass(property.getReferenceClassName()).getApiName();
            }
        }
        else
        {
            return apiWriter.getScalarDataType(property.getDataType(),property.getMultiValue(),property.getRequired());
        }
    }
    
    protected String[] getRequiredImports(PropertyDescription property)
    {
        if(property.getDataType().isReference())
        {
            return null;
        }
        else
        {
            return apiWriter.getScalarDataTypeImports(property.getDataType(),property.getMultiValue(),property.getRequired());
        }
    }

    protected void writeClassPropertyAccess(PropertyDescription property, PrintWriter out)
    {
        // generate names
        String javaDataType = getReturnDataType(property);
        ScalarTypeDescription scalarType = property.getDataType();
        String constantPropertyName = "PROPERTY_" + property.getOdmaName().getName().toUpperCase();
        // getter
        out.println("");
        out.println("    /**");
        out.println("     * Returns "+property.getAbstract()+".<br/>");
        String standardGetterName = "get" + ((!scalarType.isReference()) ? scalarType.getName() : (property.getMultiValue() ? "ReferenceIterable" : "Reference"));
        out.println("     * Shortcut for <code>getProperty(OdmaTypes."+constantPropertyName+")."+standardGetterName+"()</code>.");
        out.println("     * ");
        for(String s : getPropertyDetails(property))
        {
            out.println("     * "+s);
        }
        out.println("     * ");
        out.println("     * @return "+property.getAbstract());
        out.println("     */");
        out.println("    "+javaDataType+" "+(javaDataType.equalsIgnoreCase("boolean")?"is":"get")+property.getApiName()+"();");
        // setter
        if( (!property.isReadOnly()) && (!property.getMultiValue()) )
        {
            out.println("");
            out.println("    /**");
            out.println("     * Sets "+property.getAbstract()+".<br>");
            String standardSetterName = "setValue";
            out.println("     * Shortcut for <code>getProperty(OdmaTypes."+constantPropertyName+")."+standardSetterName+"(value)</code>.");
            out.println("     * ");
            for(String s : getPropertyDetails(property))
            {
                out.println("     * "+s);
            }
            out.println("     * ");
            out.println("     * @param newValue");
            out.println("     *             The new value for "+property.getAbstract());
            out.println("     * ");
            out.println("     * @throws OdmaAccessDeniedException");
            out.println("     *             If this OdmaProperty is read-only or cannot be set by the current user");
            out.println("     */");
            out.println("    void set"+property.getApiName()+"("+javaDataType+" newValue) throws OdmaAccessDeniedException;");
        }
    }

    protected void appendRequiredImportsClassPropertyAccess(ImportsList requiredImports, PropertyDescription property)
    {
        requiredImports.registerImports(getRequiredImports(property));
        if( (!property.isReadOnly()) && (!property.getMultiValue()) )
        {
            requiredImports.registerImport("org.opendma.exceptions.OdmaAccessDeniedException");
        }
    }

}
