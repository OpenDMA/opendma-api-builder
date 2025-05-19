package org.opendma.apibuilder.apiwriter.go;

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

public class GoObjectsInterfaceFileWriter extends AbstractObjectsInterfaceFileWriter
{
    
    protected OdmaApiWriter apiWriter;
    
    public GoObjectsInterfaceFileWriter(OdmaApiWriter writer)
    {
        apiWriter = writer;
        this.apiHelperWriters.put("getQName", new ApiHelperWriter(){
            public void writeApiHelper(ClassDescription classDescription, ApiHelperDescription apiHelper, PrintWriter out)
            {
                // getter
                out.println("");
                out.println("    // GetQName returns "+apiHelper.getAbstract()+".");
                out.println("    // "+apiHelper.getDescription());
                out.println("    GetQName() OdmaQName");
            }
            public void appendRequiredImportsGlobal(ClassDescription classDescription, ApiHelperDescription apiHelper, ImportsList requiredImports)
            {
            }});
    }

    protected void writeClassFileHeader(ClassDescription classDescription, List<String> requiredImports, PrintWriter out)
    {
        out.println("package opendma");
        out.println("");
        out.println("import (");
        Iterator<String> itRequiredImports = requiredImports.iterator();
        while(itRequiredImports.hasNext())
        {
            String importDeclaration = (String)itRequiredImports.next();
            out.println("    \""+importDeclaration+"\"");
        }
        out.println(")");
        out.println("");
        String extendsApiName = classDescription.getExtendsApiName();
        if(extendsApiName != null)
        {
            out.println("// The "+classDescription.getOdmaName().getName()+" specific version of the "+extendsApiName+" interface");
            out.println("// offering short-cuts to all defined OpenDMA properties.");
        }
        String classComment = classDescription.getDescription();
        if(classComment != null)
        {
            if(extendsApiName != null)
            {
                out.println("//");
            }
            out.println("// "+classComment);
        }
        out.println("type "+classDescription.getApiName()+" interface {");
        if(extendsApiName != null)
        {
            out.println("");
            out.println("    // "+classDescription.getApiName()+" extends "+extendsApiName);
            out.println("    "+extendsApiName);
        }
        else if(classDescription.getAspect())
        {
            out.println("");
            out.println("    // "+classDescription.getApiName()+" extends "+classDescription.getContainingApiDescription().getObjectClass().getApiName());
            out.println("    "+classDescription.getContainingApiDescription().getObjectClass().getApiName());
        }
        else
        {
            out.println("");
            out.println("    // "+classDescription.getApiName()+" extends OdmaCoreObject");
            out.println("    OdmaCoreObject");
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
            String result = property.getContainingClass().getContainingApiDescription().getDescribedClass(property.getReferenceClassName()).getApiName();
            if(property.getMultiValue())
            {
                result = result+"Iterable";
            }
            else if(!property.getRequired())
            {
                result = "*" + result;
            }
            return result;
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
            // located in the same package. no import required
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
        String goDataType = getReturnDataType(property);
        ScalarTypeDescription scalarType = property.getDataType();
        String constantPropertyName = "PROPERTY_" + property.getOdmaName().getName().toUpperCase();
        // getter
        out.println("");
        out.println("    // "+(goDataType.equalsIgnoreCase("bool")?"Is":"Get")+property.getApiName()+" returns "+property.getAbstract()+".");
        String standardGetterName = "Get" + ((!scalarType.isReference()) ? scalarType.getName() : (property.getMultiValue() ? "ReferenceIterable" : "Reference"));
        out.println("    // Shortcut for `GetProperty(OdmaTypes."+constantPropertyName+")."+standardGetterName+"()`.");
        for(String s : getPropertyDetails(property))
        {
            out.println("    //"+s);
        }
        out.println("    "+(goDataType.equalsIgnoreCase("bool")?"Is":"Get")+property.getApiName()+"() "+goDataType);
        // setter
        if( (!property.isReadOnly()) && (!property.getMultiValue()) )
        {
            out.println("");
            out.println("    // Set"+property.getApiName()+" sets "+property.getAbstract()+".");
            String standardSetterName = "setValue";
            out.println("    // Shortcut for `getProperty(OdmaTypes."+constantPropertyName+")."+standardSetterName+"(value)`.");
            for(String s : getPropertyDetails(property))
            {
                out.println("    // "+s);
            }
            out.println("    //");
            out.println("    // Returns error if this OdmaProperty is read-only or cannot be set by the current user");
            out.println("    Set"+property.getApiName()+"(newValue "+goDataType+") error");
        }
    }

    protected void appendRequiredImportsClassPropertyAccess(ImportsList requiredImports, PropertyDescription property)
    {
        requiredImports.registerImports(getRequiredImports(property));
    }

}
