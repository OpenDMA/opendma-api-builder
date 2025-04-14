package org.opendma.apibuilder.apiwriter.rust;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;

import org.opendma.apibuilder.OdmaApiWriter;
import org.opendma.apibuilder.Tools;
import org.opendma.apibuilder.apiwriter.AbstractObjectsInterfaceFileWriter;
import org.opendma.apibuilder.apiwriter.ApiHelperWriter;
import org.opendma.apibuilder.apiwriter.ImportsList;
import org.opendma.apibuilder.structure.ApiHelperDescription;
import org.opendma.apibuilder.structure.ClassDescription;
import org.opendma.apibuilder.structure.PropertyDescription;
import org.opendma.apibuilder.structure.ScalarTypeDescription;

public class RustObjectsInterfaceFileWriter extends AbstractObjectsInterfaceFileWriter
{
    
    protected OdmaApiWriter apiWriter;
    
    public RustObjectsInterfaceFileWriter(OdmaApiWriter writer)
    {
        apiWriter = writer;
        this.apiHelperWriters.put("getQName", new ApiHelperWriter(){
            public void writeApiHelper(ClassDescription classDescription, ApiHelperDescription apiHelper, PrintWriter out)
            {
                // getter
                out.println("");
                out.println("    // Returns "+apiHelper.getAbstract()+".");
                out.println("    // "+apiHelper.getDescription());
                out.println("    fn get_qname(&self) -> Result<OdmaQName, OdmaError>;");
            }
            public void appendRequiredImportsGlobal(ClassDescription classDescription, ApiHelperDescription apiHelper, ImportsList requiredImports)
            {
                requiredImports.registerImport("crate::OdmaQName");
            }});
    }

    protected void writeClassFileHeader(ClassDescription classDescription, List<String> requiredImports, PrintWriter out)
    {
        Iterator<String> itRequiredImports = requiredImports.iterator();
        while(itRequiredImports.hasNext())
        {
            String importDeclaration = (String)itRequiredImports.next();
            if(importDeclaration.equals("crate::"+classDescription.getApiName()))
            {
                continue;
            }
            out.println("use "+importDeclaration+";");
        }
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
        if(extendsApiName != null)
        {
            out.println("pub trait "+classDescription.getApiName()+": "+extendsApiName+" {");
        }
        else
        {
            if(classDescription.getAspect())
            {
                out.println("pub trait "+classDescription.getApiName()+": "+classDescription.getContainingApiDescription().getObjectClass().getApiName()+" {");
            }
            else
            {
                out.println("pub trait "+classDescription.getApiName()+" {");
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
        String extendsApiName = classDescription.getExtendsApiName();
        if(extendsApiName != null)
        {
            requiredImports.registerImport("crate::"+extendsApiName);
        }
        else
        {
            if(classDescription.getAspect())
            {
                requiredImports.registerImport("crate::"+classDescription.getContainingApiDescription().getObjectClass().getApiName());
            }
        }
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
        requiredImports.registerImport("crate::OdmaQName");
        requiredImports.registerImport("crate::OdmaProperty");
        requiredImports.registerImport("crate::OdmaError");
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
            String result = "&dyn " + property.getContainingClass().getContainingApiDescription().getDescribedClass(property.getReferenceClassName()).getApiName();
            if(property.getMultiValue())
            {
                result = "Box<dyn Iterator<Item = "+result+"> + '_>";
            }
            else if(!property.getRequired())
            {
                result = "Option<" + result + ">";
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
            return new String[] { "crate::" + property.getContainingClass().getContainingApiDescription().getDescribedClass(property.getReferenceClassName()).getApiName() };
        }
        else
        {
            return apiWriter.getScalarDataTypeImports(property.getDataType(),property.getMultiValue(),property.getRequired());
        }
    }

    protected void writeClassPropertyAccess(PropertyDescription property, PrintWriter out)
    {
        // generate names
        String rustDataType = getReturnDataType(property);
        ScalarTypeDescription scalarType = property.getDataType();
        String constantPropertyName = "PROPERTY_" + property.getOdmaName().getName().toUpperCase();
        // getter
        out.println("");
        out.println("    // Returns "+property.getAbstract()+".");
        String standardGetterName = "get_" + ((!scalarType.isReference()) ? scalarType.getName() : (property.getMultiValue() ? "reference_iterator" : "reference"));
        out.println("    // Shortcut for `get_property(OdmaTypes."+constantPropertyName+")."+standardGetterName+"()`.");
        for(String s : getPropertyDetails(property))
        {
            out.println("    //"+s);
        }
        out.println("    fn "+(rustDataType.equalsIgnoreCase("bool")?"is_":"get_")+Tools.toSnakeCase(property.getApiName())+"(&self) -> Result<"+rustDataType+", OdmaError>;");
        // setter
        if( (!property.isReadOnly()) && (!property.getMultiValue()) )
        {
            out.println("");
            out.println("    // Sets "+property.getAbstract()+".");
            String standardSetterName = "setValue";
            out.println("    // Shortcut for <code>getProperty(OdmaTypes."+constantPropertyName+")."+standardSetterName+"(value)</code>.");
            for(String s : getPropertyDetails(property))
            {
                out.println("    // "+s);
            }
            out.println("    //");
            out.println("    // Returns `OdmaError::AccessDenied` if the property cannot be modified by the current user.");
            out.println("    fn set_"+Tools.toSnakeCase(property.getApiName())+"(&mut self, new_value: "+rustDataType+") -> Result<(), OdmaError>;");
        }
    }

    protected void appendRequiredImportsClassPropertyAccess(ImportsList requiredImports, PropertyDescription property)
    {
        requiredImports.registerImports(getRequiredImports(property));
        requiredImports.registerImport("crate::OdmaError");
    }

}
