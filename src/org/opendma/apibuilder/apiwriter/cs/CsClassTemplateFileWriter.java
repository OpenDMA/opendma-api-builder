package org.opendma.apibuilder.apiwriter.cs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;

import org.opendma.apibuilder.OdmaApiWriter;
import org.opendma.apibuilder.OdmaBasicTypes;
import org.opendma.apibuilder.apiwriter.AbstractApiWriter;
import org.opendma.apibuilder.apiwriter.AbstractClassFileWriter;
import org.opendma.apibuilder.structure.ClassDescription;
import org.opendma.apibuilder.structure.PropertyDescription;
import org.opendma.apibuilder.structure.ScalarTypeDescription;

public class CsClassTemplateFileWriter extends AbstractClassFileWriter
{
    
    protected OdmaApiWriter apiWriter;
    
    public CsClassTemplateFileWriter(OdmaApiWriter writer)
    {
        apiWriter = writer;
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
        out.println("namespace OpenDMA.Templates");
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
            out.println("    public class "+classDescription.getApiName()+"Template : "+extendsApiName+"Template, I"+classDescription.getApiName());
        }
        else
        {
            if(classDescription.getAspect())
            {
                out.println("    public class "+classDescription.getApiName()+"Template : "+classDescription.getContainingApiDescription().getObjectClass().getApiName()+"Template, I"+classDescription.getApiName());
            }
            else
            {
                out.println("    public class "+classDescription.getApiName()+"Template : I"+classDescription.getApiName());
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

    protected void appendRequiredImportsGlobal(ClassDescription classDescription, List requiredImports)
    {
        if(!requiredImports.contains("System"))
        {
            requiredImports.add("System");
        }
        if(!requiredImports.contains("System.Collections.Generic"))
        {
            requiredImports.add("System.Collections.Generic");
        }
        if(!requiredImports.contains("System.Linq"))
        {
            requiredImports.add("System.Linq");
        }
        if(!requiredImports.contains("System.Text"))
        {
            requiredImports.add("System.Text");
        }
        if(!requiredImports.contains("OpenDMA.Api"))
        {
            requiredImports.add("OpenDMA.Api");
        }
    }

    protected void writeClassGenericPropertyAccess(ClassDescription classDescription, PrintWriter out) throws IOException
    {
        out.println("");
        out.println("        // =============================================================================================");
        out.println("        // Generic property access");
        out.println("        // =============================================================================================");
        InputStream templateIn = AbstractApiWriter.getResourceAsStream("/templates/cs/OdmaObject.GenericPropertyAccess.template");
        BufferedReader templareReader = new BufferedReader(new InputStreamReader(templateIn));
        String templateLine = null;
        while( (templateLine = templareReader.readLine()) != null)
        {
            out.println(templateLine);
        }
    }

    protected void appendRequiredImportsGenericPropertyAccess(List requiredImports)
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
        if(property.getDataType() == OdmaBasicTypes.TYPE_REFERENCE)
        {
            if(property.getMultiValue())
            {
                return "I"+property.getContainingClass().getContainingApiDescription().getDescribedClass(property.getReferenceClassName()).getApiName()+"Enumeration";
            }
            else
            {
                return "I"+property.getContainingClass().getContainingApiDescription().getDescribedClass(property.getReferenceClassName()).getApiName();
            }
        }
        else
        {
            return apiWriter.getProgrammingLanguageSpecificScalarDataType(property.getMultiValue(),property.getDataType());
        }
    }
    
    protected String getRequiredImport(PropertyDescription property)
    {
        if(property.getDataType() == OdmaBasicTypes.TYPE_REFERENCE)
        {
            if(property.getMultiValue())
            {
                return "OpenDMA.Api.Collections";
            }
            else
            {
                // located in the same package. no import required
                return null;
            }
        }
        else
        {
            return apiWriter.getRequiredScalarDataTypeImport(property.getMultiValue(),property.getDataType());
        }
    }

    protected void writeClassPropertyAccess(PropertyDescription property, PrintWriter out)
    {
        // generate names
        String csDataType = getReturnDataType(property);
        ScalarTypeDescription scalarType = property.getContainingClass().getContainingApiDescription().getScalarTypeDescription(property.getDataType());
        String constantPropertyName = "PROPERTY_" + property.getOdmaName().getName().toUpperCase();
        // getter
        out.println("");
        out.println("        /// <summary>");
        out.println("        /// Property for "+property.getAbstract()+".<br>");
        String standardGetterName = "get" + ((property.getDataType() != OdmaBasicTypes.TYPE_REFERENCE) ? scalarType.getName() : (property.getMultiValue() ? "ReferenceEnumeration" : "Reference"));
        out.println("        // ");
        ScalarTypeDescription scalarTypeDescription = property.getContainingClass().getContainingApiDescription().getScalarTypeDescription(property.getDataType());
        String dataTypeName = scalarTypeDescription.isInternal() ? scalarTypeDescription.getBaseScalar() : scalarTypeDescription.getName();
        if(property.getDataType() == OdmaBasicTypes.TYPE_REFERENCE)
        {
            dataTypeName = dataTypeName + " to " + property.getReferenceClassName().getName() + " ("+property.getReferenceClassName().getQualifier()+")";
        }
        out.println("        /// <p>Property <b>"+property.getOdmaName().getName()+"</b> ("+property.getOdmaName().getQualifier()+"): <b>"+dataTypeName+"</b><br>");
        out.println("        /// "+(property.getMultiValue()?"[MultiValue]":"[SingleValue]")+" "+(property.isReadOnly()?"[ReadOnly]":"[Writable]")+" "+(property.getRequired()?"[Required]":"[Nullable]")+"<br>");
        out.println("        /// "+property.getDescription()+"</p>");
        out.println("        /// </summary>");
        out.println("        public "+csDataType+" "+property.getApiName());
        out.println("        {");
        out.println("            get");
        out.println("            {");
        out.println("                return "+(property.isReference()?"("+csDataType+")":"")+"getProperty(OdmaTypes."+constantPropertyName+")."+standardGetterName+"();");
        out.println("            }");
        if( (!property.isReadOnly()) && (!property.getMultiValue()) )
        {
            String standardSetterName = "setValue";
            out.println("            set");
            out.println("            {");
            out.println("                getProperty(OdmaTypes."+constantPropertyName+")."+standardSetterName+"(value);");
            out.println("            }");
        }
        out.println("        }");
        
    }

    protected void appendRequiredImportsClassPropertyAccess(List requiredImports, PropertyDescription property)
    {
        String importPackage = getRequiredImport(property);
        if(importPackage != null)
        {
            if(!requiredImports.contains(importPackage))
            {
                requiredImports.add(importPackage);
            }
        }
    }

}
