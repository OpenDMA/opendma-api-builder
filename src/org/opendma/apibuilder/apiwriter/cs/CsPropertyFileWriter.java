package org.opendma.apibuilder.apiwriter.cs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;

import org.opendma.apibuilder.OdmaApiWriter;
import org.opendma.apibuilder.apiwriter.AbstractPropertyFileWriter;
import org.opendma.apibuilder.apiwriter.ImportsList;
import org.opendma.apibuilder.structure.ApiDescription;
import org.opendma.apibuilder.structure.ScalarTypeDescription;

public class CsPropertyFileWriter extends AbstractPropertyFileWriter
{
    
    protected OdmaApiWriter apiWriter;
    
    public CsPropertyFileWriter(OdmaApiWriter writer)
    {
        apiWriter = writer;
    }

    protected void writePropertyFileHeader(ApiDescription apiDescription, List requiredImports, PrintWriter out) throws IOException
    {
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
        InputStream templateIn = apiWriter.getTemplateAsStream("IOdmaProperty.Header");
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
        out.println("    }");
        out.println("");
        out.println("}");
    }

    protected void writeGenericSection(ApiDescription apiDescription, PrintWriter out) throws IOException
    {
        InputStream templateIn = apiWriter.getTemplateAsStream("IOdmaProperty.Generic");
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
        String csReturnType = scalarTypeDescription.isReference() ? "IOdmaObject" : apiWriter.getScalarDataType(scalarTypeDescription,false);
        out.println("");
        out.println("        /// <summary>");
        out.println("        /// Returns the <c>"+csReturnType+"</c> value of this property if and only if");
        out.println("        /// the data type of this property is a single valued <i>"+scalarName+"</i>. Throws");
        out.println("        /// an <code>OdmaInvalidDataTypeException</code> otherwise.");
        out.println("        /// </summary>");
        out.println("        /// <returns>Returns the <c>"+csReturnType+"</c> value of this property</returns>");
        out.println("        "+csReturnType+" get"+scalarName+"();");
    }

    protected void writeMultiValueScalarAccess(ScalarTypeDescription scalarTypeDescription, PrintWriter out) throws IOException
    {
        String scalarName =  scalarTypeDescription.getName();
        String csReturnType = scalarTypeDescription.isReference() ? "IOdmaObjectEnumerable" : apiWriter.getScalarDataType(scalarTypeDescription,true);
        out.println("");
        out.println("        /// <summary>");
        out.println("        /// Returns the <c>"+csReturnType+"</c> value of this property if and only if");
        out.println("        /// the data type of this property is a multi valued <i>"+scalarName+"</i>. Throws");
        out.println("        /// an <code>OdmaInvalidDataTypeException</code> otherwise.");
        out.println("        /// </summary>");
        out.println("        /// <returns>Returns the <c>"+csReturnType+"</c> value of this property</returns>");
        out.println("        "+csReturnType+" get"+scalarName+(scalarTypeDescription.isReference()?"Enumerable":"List")+"();");
    }

    protected void appendRequiredImportsGlobal(ImportsList requiredImports)
    {
        requiredImports.registerImport("System");
        requiredImports.registerImport("System.Collections.Generic");
        requiredImports.registerImport("System.Linq");
        requiredImports.registerImport("System.Text");
    }

    protected void appendRequiredImportsScalarAccess(ImportsList requiredImports, ScalarTypeDescription scalarTypeDescription)
    {
        if(scalarTypeDescription.isReference())
        {
            return;
        }
        requiredImports.registerImports(apiWriter.getScalarDataTypeImports(scalarTypeDescription,false));
        requiredImports.registerImports(apiWriter.getScalarDataTypeImports(scalarTypeDescription,true));
    }

}
