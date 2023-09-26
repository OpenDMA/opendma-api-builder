package org.opendma.apibuilder.apiwriter.java14;

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

public class Java14ClassTemplateFileWriter extends AbstractClassFileWriter
{
    
    protected OdmaApiWriter apiWriter;
    
    public Java14ClassTemplateFileWriter(OdmaApiWriter writer)
    {
        apiWriter = writer;
        this.apiHelperWriters.put("getQName", new ApiHelperWriter(){
            public void writeApiHelper(ClassDescription classDescription, ApiHelperDescription apiHelper, PrintWriter out)
            {
                out.println("");
                out.println("    /**");
                out.println("     * "+apiHelper.getAbstract()+"<br>");
                out.println("     * <p>"+apiHelper.getDescription()+"</p>");
                out.println("     * ");
                out.println("     * @return "+apiHelper.getAbstract());
                out.println("     */");
                out.println("    public OdmaQName getQName()");
                out.println("    {");
                out.println("        return new OdmaQName(getNameQualifier(),getName());");
                out.println("    }");
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
        out.println("package org.opendma.templates;");
        out.println("");
        Iterator<String> itRequiredImports = requiredImports.iterator();
        while(itRequiredImports.hasNext())
        {
            String importDeclaration = itRequiredImports.next();
            if(Java14ApiWriter.needToImportPackage(importDeclaration,"org.opendma.templates"))
            {
                out.println("import "+importDeclaration+";");
            }
        }
        out.println("");
        out.println("/**");
        out.println(" * Template implementation of the interface <code>{@link "+classDescription.getApiName()+"}</code>.<p>");
        out.println(" * ");
        String classComment = classDescription.getDescription();
        out.println(" * "+((classComment==null)?"No description of this class available.":classComment));
        out.println(" * ");
        out.println(" * @author Stefan Kopf, xaldon Technologies GmbH, the OpenDMA architecture board");
        out.println(" */");
        if(extendsApiName != null)
        {
            out.println("public class "+classDescription.getApiName()+"Template extends "+extendsApiName+"Template implements "+classDescription.getApiName());
        }
        else
        {
            if(classDescription.getAspect())
            {
                out.println("public class "+classDescription.getApiName()+"Template extends "+classDescription.getContainingApiDescription().getObjectClass().getApiName()+"Template implements "+classDescription.getApiName());
            }
            else
            {
                out.println("public class "+classDescription.getApiName()+"Template implements "+classDescription.getApiName());
            }
        }
        out.println("{");
        if(classDescription.getAspect())
        {
            out.println("");
            out.println("    public OdmaProperty getProperty(OdmaQName propertyName) throws OdmaObjectNotFoundException");
            out.println("    {");
            out.println("        // TODO: implement me");
            out.println("        return null;");
            out.println("    }");
        }
    }

    protected void writeClassFileFooter(ClassDescription classDescription, PrintWriter out)
    {
        out.println("");
        out.println("}");
    }

    protected void appendRequiredImportsGlobal(ClassDescription classDescription, ImportsList requiredImports)
    {
        requiredImports.registerImport("org.opendma.api."+classDescription.getApiName());
        requiredImports.registerImport("org.opendma.OdmaTypes");
        requiredImports.registerImport("org.opendma.exceptions.OdmaInvalidDataTypeException");
        requiredImports.registerImport("org.opendma.exceptions.OdmaObjectNotFoundException");
        requiredImports.registerImport("org.opendma.exceptions.OdmaRuntimeException");
        if(classDescription.getAspect())
        {
            requiredImports.registerImport("org.opendma.api.OdmaProperty");
            requiredImports.registerImport("org.opendma.api.OdmaQName");
        }
    }

    protected void writeClassGenericPropertyAccess(ClassDescription classDescription, PrintWriter out) throws IOException
    {
        out.println("");
        out.println("    // =============================================================================================");
        out.println("    // Generic property access");
        out.println("    // =============================================================================================");
        InputStream templateIn = apiWriter.getTemplateAsStream("OdmaObjectTemplate.GenericPropertyAccess");
        BufferedReader templareReader = new BufferedReader(new InputStreamReader(templateIn));
        String templateLine = null;
        while( (templateLine = templareReader.readLine()) != null)
        {
            out.println(templateLine);
        }
    }

    protected void appendRequiredImportsGenericPropertyAccess(ImportsList requiredImports)
    {
        requiredImports.registerImport("java.util.Iterator");
        requiredImports.registerImport("org.opendma.api.collections.OdmaClassEnumeration");
        requiredImports.registerImport("org.opendma.exceptions.OdmaObjectNotFoundException");
        requiredImports.registerImport("org.opendma.exceptions.OdmaInvalidDataTypeException");
        requiredImports.registerImport("org.opendma.exceptions.OdmaAccessDeniedException");
        requiredImports.registerImport("org.opendma.api.OdmaProperty");
        requiredImports.registerImport("org.opendma.api.OdmaQName");
    }

    protected void writeClassObjectSpecificPropertyAccessSectionHeader(ClassDescription classDescription, PrintWriter out)
    {
        out.println("");
        out.println("    // =============================================================================================");
        out.println("    // Object specific property access");
        out.println("    // =============================================================================================");
        out.println("");
        out.println("    // CHECKTEMPLATE: the following code has most likely been copied from a class template. Make sure to keep this code up to date!");
        out.println("    // The following template code is available as "+classDescription.getApiName()+"Template");
    }

    protected String getReturnDataType(PropertyDescription property)
    {
        if(property.getDataType().isReference())
        {
            if(property.getMultiValue())
            {
                return property.getContainingClass().getContainingApiDescription().getDescribedClass(property.getReferenceClassName()).getApiName()+"Enumeration";
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
        if(property.getDataType().isReference())
        {
            if(property.getMultiValue())
            {
                return new String[] { "org.opendma.api.collections."+property.getContainingClass().getContainingApiDescription().getDescribedClass(property.getReferenceClassName()).getApiName()+"Enumeration" };
            }
            else
            {
                return new String[] { "org.opendma.api."+property.getContainingClass().getContainingApiDescription().getDescribedClass(property.getReferenceClassName()).getApiName() };
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
        String javaDataType = getReturnDataType(property);
        ScalarTypeDescription scalarType = property.getDataType();
        String constantPropertyName = "PROPERTY_" + property.getOdmaName().getName().toUpperCase();
        // getter
        out.println("");
        out.println("    /**");
        out.println("     * Returns "+property.getAbstract()+".<br>");
        String standardGetterName = "get" + ((!property.getDataType().isReference()) ? scalarType.getName() : (property.getMultiValue() ? "ReferenceEnumeration" : "Reference"));
        out.println("     * ");
        ScalarTypeDescription scalarTypeDescription = property.getDataType();
        String dataTypeName = scalarTypeDescription.isInternal() ? scalarTypeDescription.getBaseScalar() : scalarTypeDescription.getName();
        if(property.getDataType().isReference())
        {
            dataTypeName = dataTypeName + " to " + property.getReferenceClassName().getName() + " ("+property.getReferenceClassName().getQualifier()+")";
        }
        out.println("     * <p>Property <b>"+property.getOdmaName().getName()+"</b> ("+property.getOdmaName().getQualifier()+"): <b>"+dataTypeName+"</b><br>");
        out.println("     * "+(property.getMultiValue()?"[MultiValue]":"[SingleValue]")+" "+(property.isReadOnly()?"[ReadOnly]":"[Writable]")+" "+(property.getRequired()?"[Required]":"[NotRequired]")+"<br>");
        out.println("     * "+property.getDescription()+"</p>");
        out.println("     * ");
        out.println("     * @return "+property.getAbstract());
        out.println("     */");
        out.println("    public "+javaDataType+" get"+property.getApiName()+"()");
        out.println("    {");
        out.println("        try");
        out.println("        {");
        out.println("            return "+(property.isReference()?"("+javaDataType+")":"")+"getProperty(OdmaTypes."+constantPropertyName+")."+standardGetterName+"();");
        out.println("        }");
        if(property.isReference())
        {
            out.println("        catch(ClassCastException cce)");
            out.println("        {");
            out.println("            throw new OdmaRuntimeException(\"Invalid data type of system property\",cce);");
            out.println("        }");
        }
        out.println("        catch(OdmaInvalidDataTypeException oidte)");
        out.println("        {");
        out.println("            throw new OdmaRuntimeException(\"Invalid data type of system property\",oidte);");
        out.println("        }");
        out.println("        catch(OdmaObjectNotFoundException oonfe)");
        out.println("        {");
        out.println("            throw new OdmaRuntimeException(\"Predefined system property missing\",oonfe);");
        out.println("        }");
        out.println("    }");
        // setter
        if( (!property.isReadOnly()) && (!property.getMultiValue()) )
        {
            out.println("");
            out.println("    /**");
            out.println("     * Sets "+property.getAbstract()+".<br>");
            String standardSetterName = "setValue";
            out.println("     * ");
            out.println("     * <p>Property <b>"+property.getOdmaName().getName()+"</b> ("+property.getOdmaName().getQualifier()+"): <b>"+dataTypeName+"</b><br>");
            out.println("     * "+(property.getMultiValue()?"[MultiValue]":"[SingleValue]")+" "+(property.isReadOnly()?"[ReadOnly]":"[Writable]")+" "+(property.getRequired()?"[Required]":"[NotRequired]")+"<br>");
            out.println("     * "+property.getDescription()+"</p>");
            out.println("     * ");
            out.println("     * @throws OdmaAccessDeniedException");
            out.println("     *             if this property can not be set by the current user");
            out.println("     */");
            out.println("    public void set"+property.getApiName()+"("+javaDataType+" value) throws OdmaAccessDeniedException");
            out.println("    {");
            out.println("        try");
            out.println("        {");
            out.println("            getProperty(OdmaTypes."+constantPropertyName+")."+standardSetterName+"(value);");
            out.println("        }");
            out.println("        catch(OdmaInvalidDataTypeException oidte)");
            out.println("        {");
            out.println("            throw new OdmaRuntimeException(\"Invalid data type of system property\",oidte);");
            out.println("        }");
            out.println("        catch(OdmaObjectNotFoundException oonfe)");
            out.println("        {");
            out.println("            throw new OdmaRuntimeException(\"Predefined system property missing\",oonfe);");
            out.println("        }");
            out.println("    }");
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
