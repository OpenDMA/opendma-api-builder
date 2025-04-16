package org.opendma.apibuilder.apiwriter.cs;

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

public class CsObjectsInterfaceFileWriter extends AbstractObjectsInterfaceFileWriter
{
    
    protected OdmaApiWriter apiWriter;
    
    public CsObjectsInterfaceFileWriter(OdmaApiWriter writer)
    {
        apiWriter = writer;
        this.apiHelperWriters.put("getQName", new ApiHelperWriter(){
            public void writeApiHelper(ClassDescription classDescription, ApiHelperDescription apiHelper, PrintWriter out)
            {
                // getter
                out.println("");
                out.println("        /// <summary>");
                out.println("        /// "+Tools.upperCaseFirstChar(apiHelper.getAbstract())+".");
                out.println("        /// "+apiHelper.getDescription());
                out.println("        /// </summary>");
                out.println("        /// <returns>"+apiHelper.getAbstract()+".</returns>");
                out.println("        OdmaQName QName { get; }");
            }
            public void appendRequiredImportsGlobal(ClassDescription classDescription, ApiHelperDescription apiHelper, ImportsList requiredImports)
            {
            }});
    }

    protected void writeClassFileHeader(ClassDescription classDescription, List<String> requiredImports, PrintWriter out)
    {
        String extendsApiName = classDescription.getExtendsApiName();
        Iterator<String> itRequiredImports = requiredImports.iterator();
        while(itRequiredImports.hasNext())
        {
            String importPackage = itRequiredImports.next();
            if(!"OpenDMA.Api".equals(importPackage))
            {
                out.println("using "+importPackage+";");
            }
        }
        out.println("");
        out.println("namespace OpenDMA.Api");
        out.println("{");
        out.println("");
        out.println("    /// <summary>");
        if(extendsApiName != null)
        {
            out.println("    /// The "+classDescription.getOdmaName().getName()+" specific version of the <see cref=\""+extendsApiName+"\"/> interface");
            out.println("    /// offering short-cuts to all defined OpenDMA properties.");
        }
        String classComment = classDescription.getDescription();
        if(classComment != null)
        {
            if(extendsApiName != null)
            {
                out.println("    ///");
            }
            out.println("    /// "+classComment);
        }
        out.println("    /// </summary>");
        if(extendsApiName != null)
        {
            out.println("    public interface I"+classDescription.getApiName()+" : I"+extendsApiName);
        }
        else
        {
            if(classDescription.getAspect())
            {
                out.println("    public interface I"+classDescription.getApiName()+" : I"+classDescription.getContainingApiDescription().getObjectClass().getApiName());
            }
            else
            {
                out.println("    public interface I"+classDescription.getApiName());
            }
        }
        out.println("    {");
    }

    protected void writeClassFileFooter(ClassDescription classDescription, PrintWriter out)
    {
        out.println("");
        out.println("    }");
        out.println("");
        out.println("}");
    }

    protected void appendRequiredImportsGlobal(ClassDescription classDescription, ImportsList requiredImports)
    {
        requiredImports.registerImport("System");
        requiredImports.registerImport("System.Collections.Generic");
        requiredImports.registerImport("System.Text");
    }

    protected void writeClassGenericPropertyAccess(ClassDescription classDescription, PrintWriter out) throws IOException
    {
        out.println("");
        out.println("    // ----- Generic property access ---------------------------------------------------------------");
        InputStream templateIn = apiWriter.getTemplateAsStream("IOdmaObject.GenericPropertyAccess");
        BufferedReader templareReader = new BufferedReader(new InputStreamReader(templateIn));
        String templateLine = null;
        while( (templateLine = templareReader.readLine()) != null)
        {
            out.println(templateLine);
        }
    }

    protected void appendRequiredImportsGenericPropertyAccess(ImportsList requiredImports)
    {
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
            String result = "I"+property.getContainingClass().getContainingApiDescription().getDescribedClass(property.getReferenceClassName()).getApiName();
            if(property.getMultiValue())
            {
                result = "IEnumerable<"+result+">";
            }
            else if(!property.getRequired())
            {
                result = result + "?";
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
        String csDataType = getReturnDataType(property);
        ScalarTypeDescription scalarType = property.getDataType();
        String constantPropertyName = "PROPERTY_" + property.getOdmaName().getName().toUpperCase();
        // getter
        out.println("");
        out.println("        /// <summary>");
        out.println("        /// "+Tools.upperCaseFirstChar(property.getAbstract())+".<br/>");
        String standardGetterName = "Get" + ((!scalarType.isReference()) ? scalarType.getName() : (property.getMultiValue() ? "ReferenceEnumerable" : "Reference"));
        out.println("        /// Shortcut for <c>GetProperty(OdmaCommonNames."+constantPropertyName+")."+standardGetterName+"()</c> or <c>GetProperty(OdmaCommonNames."+constantPropertyName+").Value</c>.");
        out.println("        /// ");
        for(String s : getPropertyDetails(property))
        {
            out.println("        /// "+s);
        }
        out.println("        /// </summary>");
        String getset = ( (!property.isReadOnly()) && (!property.getMultiValue()) ) ? "get; set;" : "get;";
        out.println("        "+csDataType+" "+property.getApiName()+" { "+getset+" }");
    }

    protected void appendRequiredImportsClassPropertyAccess(ImportsList requiredImports, PropertyDescription property)
    {
        requiredImports.registerImports(getRequiredImports(property));
    }

}
