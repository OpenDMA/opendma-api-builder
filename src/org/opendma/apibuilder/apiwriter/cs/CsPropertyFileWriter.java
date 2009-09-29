package org.opendma.apibuilder.apiwriter.cs;

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
        InputStream templateIn = AbstractApiWriter.getResourceAsStream("/templates/cs/IOdmaProperty.Header.template");
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
        InputStream templateIn = AbstractApiWriter.getResourceAsStream("/templates/cs/IOdmaProperty.Generic.template");
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
        String csReturnType = scalarTypeDescription.isReference() ? "IOdmaObject" : apiWriter.getProgrammingLanguageSpecificScalarDataType(false,scalarTypeDescription.getNumericID());
        out.println("");
        out.println("        /// <summary>");
        out.println("        /// Returns the <c>"+csReturnType+"</c> value of this property if and only if");
        out.println("        /// the data type of this property is a single valued <i>"+scalarName+"</i>. Throws");
        out.println("        /// an <code>OdmaInvalidDataTypeException</code> otherwise.");
        out.println("        /// </summary>");
        out.println("        /// <returns>Returns the <c>"+csReturnType+"</c> value of this property</returns>");
        out.println("        public "+csReturnType+" get"+scalarName+"();");
    }

    protected void writeMultiValueScalarAccess(ScalarTypeDescription scalarTypeDescription, PrintWriter out) throws IOException
    {
        String scalarName =  scalarTypeDescription.getName();
        String csReturnType = scalarTypeDescription.isReference() ? "IOdmaObjectEnumeration" : apiWriter.getProgrammingLanguageSpecificScalarDataType(true,scalarTypeDescription.getNumericID());
        out.println("");
        out.println("        /// <summary>");
        out.println("        /// Returns the <c>"+csReturnType+"</c> value of this property if and only if");
        out.println("        /// the data type of this property is a multi valued <i>"+scalarName+"</i>. Throws");
        out.println("        /// an <code>OdmaInvalidDataTypeException</code> otherwise.");
        out.println("        /// </summary>");
        out.println("        /// <returns>Returns the <c>"+csReturnType+"</c> value of this property</returns>");
        out.println("        public "+csReturnType+" get"+scalarName+(scalarTypeDescription.isReference()?"Enumeration":"List")+"();");
    }

    protected void appendRequiredImportsGlobal(List requiredImports)
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
    }

    protected void appendRequiredImportsScalarAccess(List requiredImports, ScalarTypeDescription scalarTypeDescription)
    {
        if(scalarTypeDescription.isReference())
        {
            return;
        }
        String requiredImportSingleValue = apiWriter.getRequiredScalarDataTypeImport(false,scalarTypeDescription.getNumericID());
        String requiredImportMultiValue = apiWriter.getRequiredScalarDataTypeImport(true,scalarTypeDescription.getNumericID());
        if(requiredImportSingleValue != null)
        {
            if(!requiredImports.contains(requiredImportSingleValue))
            {
                requiredImports.add(requiredImportSingleValue);
            }
        }
        if(requiredImportMultiValue != null)
        {
            if(!requiredImports.contains(requiredImportMultiValue))
            {
                requiredImports.add(requiredImportMultiValue);
            }
        }
    }

}
