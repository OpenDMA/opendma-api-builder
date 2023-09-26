package org.opendma.apibuilder.apiwriter.java;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;

import org.opendma.apibuilder.apiwriter.AbstractPropertyFileWriter;
import org.opendma.apibuilder.apiwriter.ImportsList;
import org.opendma.apibuilder.structure.ApiDescription;
import org.opendma.apibuilder.structure.ScalarTypeDescription;

public class JavaPropertyFileWriter extends AbstractPropertyFileWriter
{
    
    protected JavaApiWriter apiWriter;
    
    public JavaPropertyFileWriter(JavaApiWriter writer)
    {
        apiWriter = writer;
    }

    protected void writePropertyFileHeader(ApiDescription apiDescription, List<String> requiredImports, PrintWriter out) throws IOException
    {
        out.println("package org.opendma.api;");
        out.println("");
        Iterator<String> itRequiredImports = requiredImports.iterator();
        while(itRequiredImports.hasNext())
        {
            String importDeclaration = (String)itRequiredImports.next();
            if(JavaApiWriter.needToImportPackage(importDeclaration,"org.opendma.api"))
            {
                out.println("import "+importDeclaration+";");
            }
        }
        out.println("");
        InputStream templateIn = apiWriter.getTemplateAsStream("OdmaProperty.Header");
        BufferedReader templareReader = new BufferedReader(new InputStreamReader(templateIn));
        String templateLine = null;
        while( (templateLine = templareReader.readLine()) != null)
        {
            out.println(templateLine);
        }
    }

    protected void writePropertyFileFooter(ApiDescription apiDescription, PrintWriter out) throws IOException
    {
        out.println("");
        out.println("}");
    }

    protected void writeGenericSection(ApiDescription apiDescription, PrintWriter out) throws IOException
    {
        InputStream templateIn = apiWriter.getTemplateAsStream("OdmaProperty.Generic");
        BufferedReader templareReader = new BufferedReader(new InputStreamReader(templateIn));
        String templateLine = null;
        while( (templateLine = templareReader.readLine()) != null)
        {
            out.println(templateLine);
        }
    }

    protected void writeSingleValueScalarAccess(ScalarTypeDescription scalarTypeDescription, PrintWriter out) throws IOException
    {
        String scalarName =  scalarTypeDescription.getName();
        String returnType = scalarTypeDescription.isReference() ? (apiWriter.supportNullability() ? "@Nullable " : "") + "OdmaObject" : apiWriter.getScalarDataType(scalarTypeDescription,false,false);
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
        out.println("    public "+returnType+" get"+scalarName+"() throws OdmaInvalidDataTypeException;");
    }

    protected void writeMultiValueScalarAccess(ScalarTypeDescription scalarTypeDescription, PrintWriter out) throws IOException
    {
        String scalarName =  scalarTypeDescription.getName();
        String returnType = scalarTypeDescription.isReference() ? (apiWriter.supportNullability() ? "@NotNull " : "") + "Iterable<? extends OdmaObject>" : apiWriter.getScalarDataType(scalarTypeDescription,true,true);
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
        out.println("    public "+returnType+" get"+scalarName+(scalarTypeDescription.isReference()?"Iterable":"List")+"() throws OdmaInvalidDataTypeException;");
    }

    protected void appendRequiredImportsGlobal(ImportsList requiredImports)
    {
        requiredImports.registerImport("org.opendma.exceptions.OdmaInvalidDataTypeException");
        requiredImports.registerImport("org.opendma.exceptions.OdmaAccessDeniedException");
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
