package org.opendma.apibuilder.apiwriter.cpp;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;

import org.opendma.apibuilder.OdmaApiWriter;
import org.opendma.apibuilder.apiwriter.AbstractObjectsInterfaceFileWriter;
import org.opendma.apibuilder.apiwriter.ApiHelperWriter;
import org.opendma.apibuilder.apiwriter.ImportsList;
import org.opendma.apibuilder.structure.ApiHelperDescription;
import org.opendma.apibuilder.structure.ClassDescription;
import org.opendma.apibuilder.structure.PropertyDescription;
import org.opendma.apibuilder.structure.ScalarTypeDescription;

public class CppObjectsInterfaceFileWriter extends AbstractObjectsInterfaceFileWriter
{
    
    protected OdmaApiWriter apiWriter;
    
    public CppObjectsInterfaceFileWriter(OdmaApiWriter writer)
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
                out.println("     */");
                out.println("    virtual OdmaQName getQName(void) = 0;");
            }
            public void appendRequiredImportsGlobal(ClassDescription classDescription, ApiHelperDescription apiHelper, ImportsList requiredImports)
            {
                if(!requiredImports.contains("\"OdmaQName.h\""))
                {
                    requiredImports.add("\"OdmaQName.h\"");
                }
            }});
    }

    protected void writeClassFileHeader(ClassDescription classDescription, List<String> requiredImports, PrintWriter out)
    {
        out.println("#ifndef _"+classDescription.getApiName()+"_h_");
        out.println("#define _"+classDescription.getApiName()+"_h_");
        out.println("");
        Iterator<String> itRequiredImports = requiredImports.iterator();
        while(itRequiredImports.hasNext())
        {
            String importPackage = itRequiredImports.next();
            out.println("#include "+importPackage);
        }
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
            out.println("class "+classDescription.getApiName()+" : public "+extendsApiName+" {");
        }
        else
        {
            if(classDescription.getAspect())
            {
                out.println("class "+classDescription.getApiName()+" : public "+classDescription.getContainingApiDescription().getObjectClass().getApiName()+" {");
            }
            else
            {
                out.println("class "+classDescription.getApiName()+" : public OdmaCoreObject {");
            }
        }
        out.println("public:");
    }

    protected void writeClassFileFooter(ClassDescription classDescription, PrintWriter out)
    {
        out.println("");
        out.println("};");
        out.println("");
        out.println("#endif");
    }

    protected void appendRequiredImportsGlobal(ClassDescription classDescription, ImportsList requiredImports)
    {
    }

    protected void writeClassGenericPropertyAccess(ClassDescription classDescription, PrintWriter out) throws IOException
    {
    }

    protected void appendRequiredImportsGenericPropertyAccess(ImportsList requiredImports)
    {
        requiredImports.registerImport("\"OdmaCoreObject.h\"");
    }

    protected void writeClassObjectSpecificPropertyAccessSectionHeader(ClassDescription classDescription, PrintWriter out)
    {
    }

    protected String getReturnDataType(PropertyDescription property)
    {
        if(property.getDataType().isReference())
        {
            String referencedClassName = property.getContainingClass().getContainingApiDescription().getDescribedClass(property.getReferenceClassName()).getApiName();
            if(property.getMultiValue())
            {
                return referencedClassName+"Enumeration";
            }
            else
            {
                if(property.getRequired())
                {
                    return referencedClassName;
                }
                else
                {
                    return "std::optional<"+referencedClassName+">";
                }
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
            String referencedClassName = property.getContainingClass().getContainingApiDescription().getDescribedClass(property.getReferenceClassName()).getApiName();
            if(property.getMultiValue())
            {
                return new String[] { "<vector>", "\""+referencedClassName+".h\"" };
            }
            else
            {
                if(property.getRequired())
                {
                    return new String[] { "\""+referencedClassName+".h\"" }; 
                }
                else
                {
                    return new String[] { "<optional>", "\""+referencedClassName+".h\"" };
                }
            }
        }
        else
        {
            return apiWriter.getScalarDataTypeImports(property.getDataType(),property.getMultiValue(),property.getRequired());
        }
    }

    protected void writeClassPropertyAccess(PropertyDescription property, PrintWriter out)
    {
        // generate names
        String cppDataType = getReturnDataType(property);
        ScalarTypeDescription scalarType = property.getDataType();
        String constantPropertyName = "PROPERTY_" + property.getOdmaName().getName().toUpperCase();
        // getter
        out.println("");
        out.println("    /**");
        out.println("     * Returns "+property.getAbstract()+".<br>");
        String standardGetterName = "get" + ((!scalarType.isReference()) ? scalarType.getName() : (property.getMultiValue() ? "ReferenceVector" : "Reference"));
        out.println("     * Shortcut for <code>getProperty(OdmaTypes."+constantPropertyName+")."+standardGetterName+"()</code>.");
        out.println("     * ");
        for(String s : getPropertyDetails(property))
        {
            out.println("     * "+s);
        }
        out.println("     * ");
        out.println("     * \\return "+property.getAbstract());
        out.println("     */");
        out.println("    virtual "+cppDataType+" "+(cppDataType.equalsIgnoreCase("bool")?"is":"get")+property.getApiName()+"(void) = 0;");
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
            out.println("     * @param newValue The new value for "+property.getAbstract());
            out.println("     * @throws OdmaAccessDeniedException If this OdmaProperty is read-only or cannot be set by the current user.");
            out.println("     */");
            out.println("    virtual void set"+property.getApiName()+"("+cppDataType+" value) = 0;");
        }
    }

    protected void appendRequiredImportsClassPropertyAccess(ImportsList requiredImports, PropertyDescription property)
    {
        requiredImports.registerImports(getRequiredImports(property));
    }

}
