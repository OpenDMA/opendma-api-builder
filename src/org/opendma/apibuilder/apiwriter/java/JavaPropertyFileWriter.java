package org.opendma.apibuilder.apiwriter.java;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;

import org.opendma.apibuilder.OdmaApiWriter;
import org.opendma.apibuilder.apiwriter.AbstractApiWriter;
import org.opendma.apibuilder.apiwriter.AbstractPropertyFileWriter;
import org.opendma.apibuilder.apiwriter.ImportsList;
import org.opendma.apibuilder.structure.ApiDescription;
import org.opendma.apibuilder.structure.ScalarTypeDescription;

public class JavaPropertyFileWriter extends AbstractPropertyFileWriter
{
    
    protected OdmaApiWriter apiWriter;
    
    public JavaPropertyFileWriter(OdmaApiWriter writer)
    {
        apiWriter = writer;
    }

    protected void writePropertyFileHeader(ApiDescription apiDescription, List requiredImports, PrintWriter out) throws IOException
    {
        out.println("package org.opendma.api;");
        out.println("");
        Iterator itRequiredImports = requiredImports.iterator();
        while(itRequiredImports.hasNext())
        {
            String importDeclaration = (String)itRequiredImports.next();
            if(JavaApiWriter.needToImportPackage(importDeclaration,"org.opendma.api"))
            {
                out.println("import "+importDeclaration+";");
            }
        }
        out.println("");
        InputStream templateIn = AbstractApiWriter.getResourceAsStream("/templates/java/OdmaProperty.Header.template");
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
        InputStream templateIn = AbstractApiWriter.getResourceAsStream("/templates/java/OdmaProperty.Generic.template");
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
        String javaReturnType = scalarTypeDescription.isReference() ? "OdmaObject" : apiWriter.getScalarDataType(scalarTypeDescription,false);
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
        out.println("    public "+javaReturnType+" get"+scalarName+"() throws OdmaInvalidDataTypeException;");
    }

    protected void writeMultiValueScalarAccess(ScalarTypeDescription scalarTypeDescription, PrintWriter out) throws IOException
    {
        String scalarName =  scalarTypeDescription.getName();
        String javaReturnType = scalarTypeDescription.isReference() ? "OdmaObjectEnumeration" : apiWriter.getScalarDataType(scalarTypeDescription,true);
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
        out.println("    public "+javaReturnType+" get"+scalarName+(scalarTypeDescription.isReference()?"Enumeration":"List")+"() throws OdmaInvalidDataTypeException;");
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
            requiredImports.registerImport("org.opendma.api.collections.OdmaObjectEnumeration");
            return;
        }
        requiredImports.registerImports(apiWriter.getScalarDataTypeImports(scalarTypeDescription,false));
        requiredImports.registerImports(apiWriter.getScalarDataTypeImports(scalarTypeDescription,true));
    }

}
