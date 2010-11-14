package org.opendma.apibuilder.apiwriter.cpp;

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

public class CppClassTemplateFileWriter extends AbstractClassFileWriter
{
    
    protected OdmaApiWriter apiWriter;
    
    public CppClassTemplateFileWriter(OdmaApiWriter writer)
    {
        apiWriter = writer;
    }

    protected void writeClassFileHeader(ClassDescription classDescription, List requiredImports, PrintWriter out)
    {
        out.println("#ifndef _"+classDescription.getApiName()+"_h_");
        out.println("#define _"+classDescription.getApiName()+"_h_");
        out.println("");
        Iterator itRequiredImports = requiredImports.iterator();
        while(itRequiredImports.hasNext())
        {
            String importPackage = (String)itRequiredImports.next();
            out.println("#include "+importPackage);
        }
        String extendsApiName = classDescription.getExtendsApiName();
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
        out.println(" * \\author Stefan Kopf, xaldon Technologies GmbH, the OpenDMA architecture board");
        out.println(" */");
        if(extendsApiName != null)
        {
            out.println("class "+classDescription.getApiName()+" : public "+extendsApiName);
        }
        else
        {
            if(classDescription.getAspect())
            {
                out.println("class "+classDescription.getApiName()+" : public "+classDescription.getContainingApiDescription().getObjectClass().getApiName());
            }
            else
            {
                out.println("class "+classDescription.getApiName());
            }
        }
        out.println("{");
        out.println("");
        out.println("public:");
    }

    protected void writeClassFileFooter(ClassDescription classDescription, PrintWriter out)
    {
        out.println("");
        out.println("};");
        out.println("");
        out.println("#endif");
    }

    protected void appendRequiredImportsGlobal(ClassDescription classDescription, List requiredImports)
    {
        // we do not have any globally required imports
    }

    protected void writeClassGenericPropertyAccess(ClassDescription classDescription, PrintWriter out) throws IOException
    {
        out.println("");
        out.println("    // =============================================================================================");
        out.println("    // Generic property access");
        out.println("    // =============================================================================================");
        InputStream templateIn = AbstractApiWriter.getResourceAsStream("/templates/cpp/OdmaObject.GenericPropertyAccess.template");
        BufferedReader templareReader = new BufferedReader(new InputStreamReader(templateIn));
        String templateLine = null;
        while( (templateLine = templareReader.readLine()) != null)
        {
            out.println(templateLine);
        }
    }

    protected void appendRequiredImportsGenericPropertyAccess(List requiredImports)
    {
        // nothing for now
    }

    protected void writeClassObjectSpecificPropertyAccessSectionHeader(ClassDescription classDescription, PrintWriter out)
    {
        out.println("");
        out.println("    // =============================================================================================");
        out.println("    // Object specific property access");
        out.println("    // =============================================================================================");
    }

    protected String getReturnDataType(PropertyDescription property)
    {
        if(property.getDataType() == OdmaBasicTypes.TYPE_REFERENCE)
        {
            if(property.getMultiValue())
            {
                return property.getContainingClass().getContainingApiDescription().getDescribedClass(property.getReferenceClassName()).getApiName()+"Enumeration*";
            }
            else
            {
                return property.getContainingClass().getContainingApiDescription().getDescribedClass(property.getReferenceClassName()).getApiName()+"*";
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
                return "\"collections/"+property.getContainingClass().getContainingApiDescription().getDescribedClass(property.getReferenceClassName()).getApiName()+"Enumeration.h\"";
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
        String cppDataType = getReturnDataType(property);
        String constantPropertyName = "PROPERTY_" + property.getOdmaName().getName().toUpperCase();
        // getter
        out.println("");
        out.println("    /**");
        out.println("     * Returns "+property.getAbstract()+".<br>");
        String standardGetterName = "get" + ((property.getDataType() != OdmaBasicTypes.TYPE_REFERENCE) ? cppDataType : (property.getMultiValue() ? "ObjectEnumeration" : "Object"));
        out.println("     * Shortcut for <code>getProperty(OdmaTypes."+constantPropertyName+")."+standardGetterName+"()</code>.");
        out.println("     * ");
        ScalarTypeDescription scalarTypeDescription = property.getContainingClass().getContainingApiDescription().getScalarTypeDescription(property.getDataType());
        String dataTypeName = scalarTypeDescription.isInternal() ? scalarTypeDescription.getBaseScalar() : scalarTypeDescription.getName();
        if(property.getDataType() == OdmaBasicTypes.TYPE_REFERENCE)
        {
            dataTypeName = dataTypeName + " to " + property.getReferenceClassName().getName() + " ("+property.getReferenceClassName().getQualifier()+")";
        }
        out.println("     * <p>Property <b>"+property.getOdmaName().getName()+"</b> ("+property.getOdmaName().getQualifier()+"): <b>"+dataTypeName+"</b><br>");
        out.println("     * "+(property.getMultiValue()?"[MultiValue]":"[SingleValue]")+" "+(property.isReadOnly()?"[ReadOnly]":"[Writable]")+" "+(property.getRequired()?"[Required]":"[Nullable]")+"<br>");
        out.println("     * "+property.getDescription()+"</p>");
        out.println("     * ");
        out.println("     * \\return "+property.getAbstract());
        out.println("     */");
        out.println("    virtual "+cppDataType+" get"+property.getApiName()+"(void) = 0;");
        // setter
        if( (!property.isReadOnly()) && (!property.getMultiValue()) )
        {
            out.println("");
            out.println("    /**");
            out.println("     * Sets "+property.getAbstract()+".<br>");
            String standardSetterName = "set" + ((property.getDataType() != OdmaBasicTypes.TYPE_REFERENCE) ? cppDataType : (property.getMultiValue() ? "ObjectEnumeration" : "Object"));
            out.println("     * Shortcut for <code>getProperty(OdmaTypes."+constantPropertyName+")."+standardSetterName+"(value)</code>.");
            out.println("     * ");
            out.println("     * <p>Property <b>"+property.getOdmaName().getName()+"</b> ("+property.getOdmaName().getQualifier()+"): <b>"+dataTypeName+"</b><br>");
            out.println("     * "+(property.getMultiValue()?"[MultiValue]":"[SingleValue]")+" "+(property.isReadOnly()?"[ReadOnly]":"[Writable]")+" "+(property.getRequired()?"[Required]":"[Nullable]")+"<br>");
            out.println("     * "+property.getDescription()+"</p>");
            out.println("     */");
            out.println("    virtual void set"+property.getApiName()+"("+cppDataType+" value) = 0;");
        }
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
