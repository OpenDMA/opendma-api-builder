package org.opendma.apibuilder.apiwriter.ts;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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

public class TypeScriptObjectsInterfaceFileWriter extends AbstractObjectsInterfaceFileWriter
{
    
    protected OdmaApiWriter apiWriter;
    
    public TypeScriptObjectsInterfaceFileWriter(OdmaApiWriter writer)
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
                out.println("     * @returns "+apiHelper.getAbstract());
                out.println("     */");
                out.println("    getQName(): OdmaQName;");
            }
            public void appendRequiredImportsGlobal(ClassDescription classDescription, ApiHelperDescription apiHelper, ImportsList requiredImports)
            {
                requiredImports.registerImport("OdmaQName");
            }
        });
    }

    protected void writeClassFileHeader(ClassDescription classDescription, List<String> requiredImports, PrintWriter out)
    {
        Iterator<String> itRequiredImports = requiredImports.iterator();
        while(itRequiredImports.hasNext())
        {
            String importDeclaration = itRequiredImports.next();
            if(importDeclaration.equalsIgnoreCase(classDescription.getApiName()))
            {
                continue;
            }
            out.println("import { "+importDeclaration+" } from './"+importDeclaration+"';");
        }
        out.println("");
        String extendsApiName = classDescription.getExtendsApiName();
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
            out.println("export interface "+classDescription.getApiName()+" extends "+extendsApiName+" {");
        }
        else
        {
            if(classDescription.getAspect())
            {
                out.println("export interface "+classDescription.getApiName()+" extends "+classDescription.getContainingApiDescription().getObjectClass().getApiName()+" {");
            }
            else
            {
                out.println("export interface "+classDescription.getApiName()+" {");
            }
        }
    }

    protected void writeClassFileFooter(ClassDescription classDescription, PrintWriter out)
    {
        out.println("");
        out.println("}");
        if( (!classDescription.getAspect()) && (classDescription.getExtendsOdmaName() == null) )
        {
            out.println("");
            out.println("export function is"+classDescription.getApiName()+"(obj: any): obj is "+classDescription.getApiName()+" {");
            out.println("    return (");
            out.println("        obj &&");
            out.println("        typeof obj.getProperty === 'function' &&");
            out.println("        typeof obj.prepareProperties === 'function' &&");
            out.println("        typeof obj.setProperty === 'function' &&");
            out.println("        typeof obj.isDirty === 'function' &&");
            out.println("        typeof obj.save === 'function' &&");
            out.println("        typeof obj.instanceOf === 'function' &&");
            Iterator<ApiHelperDescription> itApiHelperDescription = classDescription.getApiHelpers().iterator();
            while(itApiHelperDescription.hasNext())
            {
                ApiHelperDescription apiHelper = itApiHelperDescription.next();
                out.println("        typeof obj."+apiHelper.getApiName()+" === 'function' &&");
            }
            Iterator<PropertyDescription> itPropertyDescriptions = classDescription.getPropertyDescriptions().iterator();
            while(itPropertyDescriptions.hasNext())
            {
                PropertyDescription property = itPropertyDescriptions.next();
                out.println("        typeof obj.get"+property.getApiName()+" === 'function'"+(itPropertyDescriptions.hasNext()?" &&":""));
            }
            out.println("    );");
            out.println("}");
        }
    }

    protected void appendRequiredImportsGlobal(ClassDescription classDescription, ImportsList requiredImports)
    {
        String extendsApiName = classDescription.getExtendsApiName();
        if(extendsApiName != null)
        {
            requiredImports.registerImport(extendsApiName);
        }
        else
        {
            if(classDescription.getAspect())
            {
                requiredImports.registerImport(classDescription.getContainingApiDescription().getObjectClass().getApiName());
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
        requiredImports.registerImport("OdmaQName");
        requiredImports.registerImport("OdmaProperty");
        requiredImports.registerImport("OdmaPropertyNotFoundError");
        requiredImports.registerImport("OdmaInvalidDataTypeError");
        requiredImports.registerImport("OdmaAccessDeniedError");
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
            String result = property.getContainingClass().getContainingApiDescription().getDescribedClass(property.getReferenceClassName()).getApiName();
            if(property.getMultiValue())
            {
                result = "Iterable<"+result+">";
            }
            else if(!property.getRequired())
            {
                result = result + " | null";
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
            String refClass = property.getContainingClass().getContainingApiDescription().getDescribedClass(property.getReferenceClassName()).getApiName();
            return new String[] { refClass };
        }
        else
        {
            return apiWriter.getScalarDataTypeImports(property.getDataType(),property.getMultiValue(),property.getRequired());
        }
    }

    protected void writeClassPropertyAccess(PropertyDescription property, PrintWriter out)
    {
        // generate names
        String tsDataType = getReturnDataType(property);
        ScalarTypeDescription scalarType = property.getDataType();
        String constantPropertyName = "PROPERTY_" + property.getOdmaName().getName().toUpperCase();
        // getter
        out.println("");
        out.println("    /**");
        out.println("     * Returns "+property.getAbstract()+".<br/>");
        String standardGetterName = "get" + ((!scalarType.isReference()) ? scalarType.getName() : (property.getMultiValue() ? "ReferenceArray" : "Reference"));
        out.println("     * Shortcut for <code>getProperty(OdmaTypes."+constantPropertyName+")."+standardGetterName+"()</code>.");
        out.println("     * ");
        for(String s : getPropertyDetails(property))
        {
            out.println("     * "+s);
        }
        out.println("     * ");
        out.println("     * @returns "+property.getAbstract());
        out.println("     */");
        out.println("    "+(tsDataType.equalsIgnoreCase("boolean")?"is":"get")+property.getApiName()+"(): "+tsDataType+";");
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
            out.println("     * @param newValue - The new value for "+property.getAbstract());
            out.println("     * @throws {@link OdmaAccessDeniedError} Thrown if this OdmaProperty is read-only or cannot be set by the current user");
            out.println("     */");
            out.println("    set"+property.getApiName()+"(newValue: "+tsDataType+"): void;");
        }
    }

    protected void appendRequiredImportsClassPropertyAccess(ImportsList requiredImports, PropertyDescription property)
    {
        requiredImports.registerImports(getRequiredImports(property));
    }

}
