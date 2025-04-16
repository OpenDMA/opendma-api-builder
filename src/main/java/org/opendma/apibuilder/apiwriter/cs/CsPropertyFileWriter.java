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

    protected void writePropertyFileHeader(ApiDescription apiDescription, List<String> requiredImports, PrintWriter out) throws IOException
    {
        Iterator<String> itRequiredImports = requiredImports.iterator();
        while(itRequiredImports.hasNext())
        {
            String importPackage = itRequiredImports.next();
            if(!importPackage.equals("OpenDMA.Api"))
            {
                out.println("using "+importPackage+";");
            }
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
        String returnType = scalarTypeDescription.isReference() ? "IOdmaObject?" : apiWriter.getScalarDataType(scalarTypeDescription,false,false);
        out.println("");
        out.println("        /// <summary>");
        out.println("        /// Retrieves the "+scalarName+" value of this property if and only if");
        out.println("        /// the data type of this property is a single valued "+scalarName+".");
        out.println("        /// </summary>");
        out.println("        /// <returns>");
        out.println("        /// The "+returnType+" value of this property");
        out.println("        /// </returns>");
        out.println("        /// <exception cref=\"OdmaInvalidDataTypeException\">");
        out.println("        /// Thrown if the data type of this property is not a single-valued "+scalarName+".");
        out.println("        /// </exception>");
        out.println("        "+returnType+" Get"+scalarName+"();");
    }

    protected void writeMultiValueScalarAccess(ScalarTypeDescription scalarTypeDescription, PrintWriter out) throws IOException
    {
        String scalarName =  scalarTypeDescription.getName();
        String returnType = scalarTypeDescription.isReference() ? "IEnumerable<IOdmaObject>" : apiWriter.getScalarDataType(scalarTypeDescription,true,true);
        out.println("");
        out.println("        /// <summary>");
        out.println("        /// Retrieves the "+scalarName+" value of this property if and only if");
        out.println("        /// the data type of this property is a multi valued "+scalarName+".");
        out.println("        /// </summary>");
        out.println("        /// <returns>");
        out.println("        /// The "+returnType+" value of this property");
        out.println("        /// </returns>");
        out.println("        /// <exception cref=\"OdmaInvalidDataTypeException\">");
        out.println("        /// Thrown if the data type of this property is not a multi-valued "+scalarName+".");
        out.println("        /// </exception>");
        out.println("        "+returnType+" Get"+scalarName+(scalarTypeDescription.isReference()?"Enumerable":"List")+"();");
    }

    protected void appendRequiredImportsGlobal(ImportsList requiredImports)
    {
        requiredImports.registerImport("System");
        requiredImports.registerImport("System.Collections.Generic");
        requiredImports.registerImport("System.Text");
    }

    protected void appendRequiredImportsScalarAccess(ImportsList requiredImports, ScalarTypeDescription scalarTypeDescription)
    {
        if(scalarTypeDescription.isReference())
        {
            return;
        }
        requiredImports.registerImports(apiWriter.getScalarDataTypeImports(scalarTypeDescription,false,false));
        requiredImports.registerImports(apiWriter.getScalarDataTypeImports(scalarTypeDescription,true,true));
    }

}
