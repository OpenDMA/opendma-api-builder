package org.opendma.apibuilder.apiwriter.java14;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;

import org.opendma.apibuilder.OdmaApiWriter;
import org.opendma.apibuilder.apiwriter.AbstractPropertyImplementationFileWriter;
import org.opendma.apibuilder.apiwriter.ImportsList;
import org.opendma.apibuilder.structure.ApiDescription;
import org.opendma.apibuilder.structure.ScalarTypeDescription;

public class Java14PropertyImplementationFileWriter extends AbstractPropertyImplementationFileWriter
{
    
    protected OdmaApiWriter apiWriter;
    
    public Java14PropertyImplementationFileWriter(OdmaApiWriter writer)
    {
        apiWriter = writer;
    }

    protected void writePropertyImplementationFileHeader(ApiDescription apiDescription, List requiredImports, PrintWriter out) throws IOException
    {
        out.println("package org.opendma.impl;");
        out.println("");
        Iterator itRequiredImports = requiredImports.iterator();
        while(itRequiredImports.hasNext())
        {
            String importDeclaration = (String)itRequiredImports.next();
            if(Java14ApiWriter.needToImportPackage(importDeclaration,"org.opendma.impl"))
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
        out.println("    public void setValue(Object newValue) throws OdmaInvalidDataTypeException, OdmaAccessDeniedException");
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

    protected void writeGenericSectionSwitch(ApiDescription apiDescription, PrintWriter out, boolean multivalue) throws IOException
    {
        List scalarTypes = apiDescription.getScalarTypes();
        out.println("            switch(dataType)");
        out.println("            {");
        Iterator itScalarTypes = scalarTypes.iterator();
        while(itScalarTypes.hasNext())
        {
            ScalarTypeDescription scalarTypeDescription = (ScalarTypeDescription)itScalarTypes.next();
            //if(!scalarTypeDescription.isInternal())
            //{
                String constantScalarTypeName = "TYPE_" + scalarTypeDescription.getName().toUpperCase();
                String javaReturnType = multivalue
                                        ?
                                        (scalarTypeDescription.isReference() ? "OdmaObjectEnumeration" : apiWriter.getScalarDataType(scalarTypeDescription,true,false))
                                        :
                                        (scalarTypeDescription.isReference() ? "OdmaObject" : apiWriter.getScalarDataType(scalarTypeDescription,false,false));

                out.println("            case OdmaTypes."+constantScalarTypeName+":");
                out.println("                if(newValue instanceof "+javaReturnType+")");
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
        out.println("                throw new OdmaRuntimeException(\"OdmaProperty initialized with unknown data type \"+Integer.toString(dataType));");
        out.println("            }");
    }

    protected void writeSingleValueScalarAccess(ScalarTypeDescription scalarTypeDescription, PrintWriter out) throws IOException
    {
        String scalarName =  scalarTypeDescription.getName();
        String javaReturnType = scalarTypeDescription.isReference() ? "OdmaObject" : apiWriter.getScalarDataType(scalarTypeDescription,false,false);
        out.println("");
        out.println("    /**");
        out.println("     * Returns the <code>"+javaReturnType+"</code> value of this property if and only if");
        out.println("     * the data type of this property is a single valued <i>"+scalarName+"</i>. Throws");
        out.println("     * an <code>OdmaInvalidDataTypeException</code> otherwise.");
        out.println("     * ");
        out.println("     * @return the <code>"+javaReturnType+"</code> value of this property");
        out.println("     * ");
        out.println("     * @throws OdmaInvalidDataTypeException");
        out.println("     *             if and only if this property is not a single valued <i>"+scalarName+"</i>");
        out.println("     *             property");
        out.println("     */");
        out.println("    public "+javaReturnType+" get"+scalarName+"() throws OdmaInvalidDataTypeException");
        out.println("    {");
        String constantScalarTypeName = "TYPE_" + scalarTypeDescription.getName().toUpperCase();
        out.println("        if( (multivalue == false) && (dataType == OdmaTypes."+constantScalarTypeName+") )");
        out.println("        {");
        out.println("            return ("+javaReturnType+")value;");
        out.println("        }");
        out.println("        else");
        out.println("        {");
        out.println("            throw new OdmaInvalidDataTypeException(OdmaTypes."+constantScalarTypeName+",false,dataType,multivalue);");
        out.println("        }");
        out.println("    }");
    }

    protected void writeMultiValueScalarAccess(ScalarTypeDescription scalarTypeDescription, PrintWriter out) throws IOException
    {
        String scalarName =  scalarTypeDescription.getName();
        String javaReturnType = scalarTypeDescription.isReference() ? "OdmaObjectEnumeration" : apiWriter.getScalarDataType(scalarTypeDescription,true,false);
        out.println("");
        out.println("    /**");
        out.println("     * Returns the <code>"+javaReturnType+"</code> value of this property if and only if");
        out.println("     * the data type of this property is a multi valued <i>"+scalarName+"</i>. Throws");
        out.println("     * an <code>OdmaInvalidDataTypeException</code> otherwise.");
        out.println("     * ");
        out.println("     * @return the <code>"+javaReturnType+"</code> value of this property");
        out.println("     * ");
        out.println("     * @throws OdmaInvalidDataTypeException");
        out.println("     *             if and only if this property is not a multi valued <i>"+scalarName+"</i>");
        out.println("     *             property");
        out.println("     */");
        out.println("    public "+javaReturnType+" get"+scalarName+(scalarTypeDescription.isReference()?"Enumeration":"List")+"() throws OdmaInvalidDataTypeException");
        out.println("    {");
        String constantScalarTypeName = "TYPE_" + scalarTypeDescription.getName().toUpperCase();
        out.println("        if( (multivalue == true) && (dataType == OdmaTypes."+constantScalarTypeName+") )");
        out.println("        {");
        out.println("            return ("+javaReturnType+")value;");
        out.println("        }");
        out.println("        else");
        out.println("        {");
        out.println("            throw new OdmaInvalidDataTypeException(OdmaTypes."+constantScalarTypeName+",true,dataType,multivalue);");
        out.println("        }");
        out.println("    }");
    }

    protected void appendRequiredImportsGlobal(ImportsList requiredImports)
    {
        requiredImports.registerImport("org.opendma.OdmaTypes");
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
            requiredImports.registerImport("org.opendma.api.collections.OdmaObjectEnumeration");
            return;
        }
        requiredImports.registerImports(apiWriter.getScalarDataTypeImports(scalarTypeDescription,false,false));
        requiredImports.registerImports(apiWriter.getScalarDataTypeImports(scalarTypeDescription,true,false));
    }
    
}
