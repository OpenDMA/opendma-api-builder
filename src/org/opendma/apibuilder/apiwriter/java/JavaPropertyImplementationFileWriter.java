package org.opendma.apibuilder.apiwriter.java;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;

import org.opendma.apibuilder.apiwriter.AbstractPropertyImplementationFileWriter;
import org.opendma.apibuilder.apiwriter.ImportsList;
import org.opendma.apibuilder.structure.ApiDescription;
import org.opendma.apibuilder.structure.ScalarTypeDescription;

public class JavaPropertyImplementationFileWriter extends AbstractPropertyImplementationFileWriter
{
    
    protected JavaApiWriter apiWriter;
    
    public JavaPropertyImplementationFileWriter(JavaApiWriter writer)
    {
        apiWriter = writer;
    }

    protected void writePropertyImplementationFileHeader(ApiDescription apiDescription, List<String> requiredImports, PrintWriter out) throws IOException
    {
        out.println("package org.opendma.impl;");
        out.println("");
        Iterator<String> itRequiredImports = requiredImports.iterator();
        while(itRequiredImports.hasNext())
        {
            String importDeclaration = (String)itRequiredImports.next();
            if(JavaApiWriter.needToImportPackage(importDeclaration,"org.opendma.impl"))
            {
                out.println("import "+importDeclaration+";");
            }
        }
        out.println("");
        InputStream templateIn = apiWriter.getTemplateAsStream("OdmaPropertyImplementation.Header");
        BufferedReader templareReader = new BufferedReader(new InputStreamReader(templateIn));
        String templateLine = null;
        while( (templateLine = templareReader.readLine()) != null)
        {
            out.println(templateLine);
        }
    }

    protected void writePropertyImplementationFileFooter(ApiDescription apiDescription, PrintWriter out) throws IOException
    {
        out.println("");
        out.println("}");
    }

    protected void writeGenericSection(ApiDescription apiDescription, PrintWriter out) throws IOException
    {
        InputStream templateIn = apiWriter.getTemplateAsStream("OdmaPropertyImplementation.Generic");
        BufferedReader templareReader = new BufferedReader(new InputStreamReader(templateIn));
        String templateLine = null;
        while( (templateLine = templareReader.readLine()) != null)
        {
            out.println(templateLine);
        }
        out.println("");
        if(apiWriter.supportNullability())
        {
            out.println("    private boolean checkListAndValues(@NotNull Object obj, @NotNull Class<?> expectedElementsClass)");
        }
        else
        {
            out.println("    private boolean checkListAndValues(Object obj, Class<?> expectedElementsClass)");
        }
        out.println("    {");
        out.println("        if(!(obj instanceof List<?>))");
        out.println("        {");
        out.println("            return false;");
        out.println("        }");
        out.println("        for(Object element : (List<?>)obj)");
        out.println("        {");
        out.println("            if(!expectedElementsClass.isAssignableFrom(element.getClass()))");
        out.println("            {");
        out.println("                return false;");
        out.println("            }");
        out.println("        }");
        out.println("        return true;");
        out.println("    }");
        out.println("");
        out.println("    /**");
        out.println("     * Set the value of this property to the given new value. The");
        out.println("     * <code>Class</code> of the given <code>Object</code> has to match the");
        out.println("     * data type of this property.");
        out.println("     * ");
        out.println("     * @param newValue");
        out.println("     *            the new value to set this property to.");
        out.println("     * ");
        out.println("     * @throws OdmaInvalidDataTypeException");
        out.println("     *             if and only if the Class of the given Object does not match");
        out.println("     *             the data type of this property");
        out.println("     * ");
        out.println("     * @throws OdmaAccessDeniedException");
        out.println("     *             if this property can not be set by the current user");
        out.println("     */");
        if(apiWriter.supportNullability())
        {
            out.println("    public void setValue(@Nullable Object newValue) throws OdmaInvalidDataTypeException, OdmaAccessDeniedException");
        }
        else
        {
            out.println("    public void setValue(Object newValue) throws OdmaInvalidDataTypeException, OdmaAccessDeniedException");
        }
        out.println("    {");
        out.println("        if(readonly)");
        out.println("        {");
        out.println("            throw new OdmaAccessDeniedException();");
        out.println("        }");
        out.println("        if(newValue == null)");
        out.println("        {");
        out.println("            value = null;");
        out.println("            return;");
        out.println("        }");
        out.println("        if(multivalue)");
        out.println("        {");
        writeGenericSectionSwitch(apiDescription,out,true);
        out.println("        }");
        out.println("        else");
        out.println("        {");
        writeGenericSectionSwitch(apiDescription,out,false);
        out.println("        }");
        out.println("        dirty = true;");
        out.println("    }");
    }
    
    private String stripAnnotation(String s)
    {
        if(s.startsWith("@") && s.indexOf(' ') > 0)
        {
            return s.substring(s.indexOf(' ')+1);
        }
        return s;
    }

    protected void writeGenericSectionSwitch(ApiDescription apiDescription, PrintWriter out, boolean multivalue) throws IOException
    {
        List<ScalarTypeDescription> scalarTypes = apiDescription.getScalarTypes();
        out.println("            switch(dataType)");
        out.println("            {");
        Iterator<ScalarTypeDescription> itScalarTypes = scalarTypes.iterator();
        while(itScalarTypes.hasNext())
        {
            ScalarTypeDescription scalarTypeDescription = itScalarTypes.next();
            //if(!scalarTypeDescription.isInternal())
            //{
                String constantScalarTypeName = scalarTypeDescription.getName().toUpperCase();
                out.println("            case "+constantScalarTypeName+":");
                if(multivalue)
                {
                    if(scalarTypeDescription.isReference())
                    {
                        out.println("                if(newValue instanceof Iterable<?>)");
                    }
                    else
                    {
                        out.println("                if(checkListAndValues(newValue,"+stripAnnotation(apiWriter.getScalarDataType(scalarTypeDescription,false,false))+".class))");
                    }
                }
                else
                {
                    String javaReturnType = (scalarTypeDescription.isReference() ? "OdmaObject" : apiWriter.getScalarDataType(scalarTypeDescription,false,false));
                    out.println("                if(newValue instanceof "+stripAnnotation(javaReturnType)+")");
                }
                out.println("                {");
                out.println("                    value = newValue;");
                out.println("                }");
                out.println("                else");
                out.println("                {");
                out.println("                    throw new OdmaInvalidDataTypeException(dataType,multivalue);");
                out.println("                }");
                out.println("                break;");
            //}
        }
        out.println("            default:");
        out.println("                throw new OdmaRuntimeException(\"OdmaProperty initialized with unknown data type \"+dataType);");
        out.println("            }");
    }

    protected void writeSingleValueScalarAccess(ScalarTypeDescription scalarTypeDescription, PrintWriter out) throws IOException
    {
        String scalarName =  scalarTypeDescription.getName();
        String javaReturnType = scalarTypeDescription.isReference() ? "OdmaObject" : apiWriter.getScalarDataType(scalarTypeDescription,false,false);
        out.println("");
        out.println("    /**");
        out.println("     * Returns the <code>"+scalarName+"</code> value of this property if and only if");
        out.println("     * the data type of this property is a single valued <i>"+scalarName+"</i>. Throws");
        out.println("     * an <code>OdmaInvalidDataTypeException</code> otherwise.");
        out.println("     * ");
        out.println("     * @return the <code>"+scalarName+"</code> value of this property");
        out.println("     * ");
        out.println("     * @throws OdmaInvalidDataTypeException");
        out.println("     *             if and only if this property is not a single valued <i>"+scalarName+"</i>");
        out.println("     *             property");
        out.println("     */");
        out.println("    public "+javaReturnType+" get"+scalarName+"() throws OdmaInvalidDataTypeException");
        out.println("    {");
        String constantScalarTypeName = scalarTypeDescription.getName().toUpperCase();
        out.println("        if( (multivalue == false) && (dataType == OdmaType."+constantScalarTypeName+") )");
        out.println("        {");
        out.println("            return ("+stripAnnotation(javaReturnType)+")value;");
        out.println("        }");
        out.println("        else");
        out.println("        {");
        out.println("            throw new OdmaInvalidDataTypeException(OdmaType."+constantScalarTypeName+",false,dataType,multivalue);");
        out.println("        }");
        out.println("    }");
    }

    protected void writeMultiValueScalarAccess(ScalarTypeDescription scalarTypeDescription, PrintWriter out) throws IOException
    {
        String scalarName =  scalarTypeDescription.getName();
        String javaReturnType = scalarTypeDescription.isReference() ? (apiWriter.supportNullability() ? "@NotNull " : "") + "Iterable<? extends OdmaObject>" : apiWriter.getScalarDataType(scalarTypeDescription,true,false);
        out.println("");
        out.println("    /**");
        out.println("     * Returns the <code>"+scalarName+"</code> value of this property if and only if");
        out.println("     * the data type of this property is a multi valued <i>"+scalarName+"</i>. Throws");
        out.println("     * an <code>OdmaInvalidDataTypeException</code> otherwise.");
        out.println("     * ");
        out.println("     * @return the <code>"+scalarName+"</code> value of this property");
        out.println("     * ");
        out.println("     * @throws OdmaInvalidDataTypeException");
        out.println("     *             if and only if this property is not a multi valued <i>"+scalarName+"</i>");
        out.println("     *             property");
        out.println("     */");
        out.println("    @SuppressWarnings(\"unchecked\")");
        out.println("    public "+javaReturnType+" get"+scalarName+(scalarTypeDescription.isReference()?"Iterable":"List")+"() throws OdmaInvalidDataTypeException");
        out.println("    {");
        String constantScalarTypeName = scalarTypeDescription.getName().toUpperCase();
        out.println("        if( (multivalue == true) && (dataType == OdmaType."+constantScalarTypeName+") )");
        out.println("        {");
        out.println("            return ("+stripAnnotation(javaReturnType)+")value;");
        out.println("        }");
        out.println("        else");
        out.println("        {");
        out.println("            throw new OdmaInvalidDataTypeException(OdmaType."+constantScalarTypeName+",true,dataType,multivalue);");
        out.println("        }");
        out.println("    }");
    }

    protected void appendRequiredImportsGlobal(ImportsList requiredImports)
    {
        requiredImports.registerImport("org.opendma.api.OdmaType");
        requiredImports.registerImport("org.opendma.api.OdmaQName");
        requiredImports.registerImport("org.opendma.api.OdmaProperty");
        requiredImports.registerImport("org.opendma.exceptions.OdmaInvalidDataTypeException");
        requiredImports.registerImport("org.opendma.exceptions.OdmaAccessDeniedException");
        requiredImports.registerImport("org.opendma.exceptions.OdmaRuntimeException");
    }

    protected void appendRequiredImportsScalarAccess(ImportsList requiredImports, ScalarTypeDescription scalarTypeDescription)
    {
        if(scalarTypeDescription.isReference())
        {
            requiredImports.registerImport("org.opendma.api.OdmaObject");
            return;
        }
        requiredImports.registerImports(apiWriter.getScalarDataTypeImports(scalarTypeDescription,false,false));
        requiredImports.registerImports(apiWriter.getScalarDataTypeImports(scalarTypeDescription,true,false));
    }
    
}
