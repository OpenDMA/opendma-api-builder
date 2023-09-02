package org.opendma.apibuilder.apiwriter.cs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;

import org.opendma.apibuilder.OdmaApiWriter;
import org.opendma.apibuilder.apiwriter.AbstractClassFileWriter;
import org.opendma.apibuilder.apiwriter.ApiHelperWriter;
import org.opendma.apibuilder.apiwriter.ImportsList;
import org.opendma.apibuilder.structure.ApiHelperDescription;
import org.opendma.apibuilder.structure.ClassDescription;
import org.opendma.apibuilder.structure.PropertyDescription;
import org.opendma.apibuilder.structure.ScalarTypeDescription;

public class CsClassFileWriter extends AbstractClassFileWriter
{
    
    protected OdmaApiWriter apiWriter;
    
    public CsClassFileWriter(OdmaApiWriter writer)
    {
        apiWriter = writer;
        this.apiHelperWriters.put("getQName", new ApiHelperWriter(){
            public void writeApiHelper(ClassDescription classDescription, ApiHelperDescription apiHelper, PrintWriter out)
            {
                // getter
                out.println("");
                out.println("        /// <summary>");
                out.println("        /// "+apiHelper.getAbstract()+".<br>");
                out.println("        /// "+apiHelper.getDescription()+"</p>");
                out.println("        /// </summary>");
                out.println("        OdmaQName QName { get; }");
            }
            public void appendRequiredImportsGlobal(ClassDescription classDescription, ApiHelperDescription apiHelper, List requiredImports)
            {
                // TODO Auto-generated method stub
                
            }});
    }

    protected void writeClassFileHeader(ClassDescription classDescription, List requiredImports, PrintWriter out)
    {
        String extendsApiName = classDescription.getExtendsApiName();
        Iterator itRequiredImports = requiredImports.iterator();
        while(itRequiredImports.hasNext())
        {
            String importPackage = (String)itRequiredImports.next();
            out.println("using "+importPackage+";");
        }
        out.println("");
        out.println("namespace OpenDMA.Api");
        out.println("{");
        out.println("");
        out.println("    /// <summary>");
        if(extendsApiName != null)
        {
            out.println("    /// The <i>"+classDescription.getOdmaName().getName()+"</i> specific version of the <see cref=\""+extendsApiName+"\"/> interface");
            out.println("    /// that offers short cuts to all defined OpenDMA properties.<p>");
            out.println("    ///");
        }
        String classComment = classDescription.getDescription();
        out.println("    /// "+((classComment==null)?"No description of this class available.":classComment));
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
        requiredImports.registerImport("System.Linq");
        requiredImports.registerImport("System.Text");
        requiredImports.registerImport("OpenDMA.Api.Collections");
    }

    protected void writeClassGenericPropertyAccess(ClassDescription classDescription, PrintWriter out) throws IOException
    {
        out.println("");
        out.println("        // =============================================================================================");
        out.println("        // Generic property access");
        out.println("        // =============================================================================================");
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
        out.println("        // =============================================================================================");
        out.println("        // Object specific property access");
        out.println("        // =============================================================================================");
    }

    protected String getReturnDataType(PropertyDescription property)
    {
        if(property.getDataType().isReference())
        {
            if(property.getMultiValue())
            {
                return "I"+property.getContainingClass().getContainingApiDescription().getDescribedClass(property.getReferenceClassName()).getApiName()+"Enumerable";
            }
            else
            {
                return "I"+property.getContainingClass().getContainingApiDescription().getDescribedClass(property.getReferenceClassName()).getApiName();
            }
        }
        else
        {
            return apiWriter.getScalarDataType(property.getDataType(),property.getMultiValue());
        }
    }
    
    protected String[] getRequiredImports(PropertyDescription property)
    {
        if(property.getDataType().isReference())
        {
            if(property.getMultiValue())
            {
                return new String[] { "OpenDMA.Api.Collections" };
            }
            else
            {
                // located in the same package. no import required
                return null;
            }
        }
        else
        {
            return apiWriter.getScalarDataTypeImports(property.getDataType(),property.getMultiValue());
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
        out.println("        /// Property for "+property.getAbstract()+".<br>");
        String standardGetterName = "get" + ((!property.getDataType().isReference()) ? scalarType.getName() : (property.getMultiValue() ? "ObjectEnumerable" : "Object"));
        String standardSetterName = "set" + ((!property.getDataType().isReference()) ? scalarType.getName() : (property.getMultiValue() ? "ObjectEnumerable" : "Object"));
        out.println("        /// Shortcut for <c>getProperty(OdmaCommonNames."+constantPropertyName+")."+standardGetterName+"()</c> or <c>getProperty(OdmaCommonNames."+constantPropertyName+")."+standardSetterName+"()</c>.");
        out.println("        // ");
        ScalarTypeDescription scalarTypeDescription = property.getDataType();
        String dataTypeName = scalarTypeDescription.isInternal() ? scalarTypeDescription.getBaseScalar() : scalarTypeDescription.getName();
        if(property.getDataType().isReference())
        {
            dataTypeName = dataTypeName + " to " + property.getReferenceClassName().getName() + " ("+property.getReferenceClassName().getQualifier()+")";
        }
        out.println("        /// <p>Property <b>"+property.getOdmaName().getName()+"</b> ("+property.getOdmaName().getQualifier()+"): <b>"+dataTypeName+"</b><br>");
        out.println("        /// "+(property.getMultiValue()?"[MultiValue]":"[SingleValue]")+" "+(property.isReadOnly()?"[ReadOnly]":"[Writable]")+" "+(property.getRequired()?"[Required]":"[Nullable]")+"<br>");
        out.println("        /// "+property.getDescription()+"</p>");
        out.println("        /// </summary>");
        String getset = ( (!property.isReadOnly()) && (!property.getMultiValue()) ) ? "get; set;" : "get;";
        out.println("        "+csDataType+" "+property.getApiName()+" { "+getset+" }");
    }

    protected void appendRequiredImportsClassPropertyAccess(ImportsList requiredImports, PropertyDescription property)
    {
        requiredImports.registerImports(getRequiredImports(property));
    }

}
