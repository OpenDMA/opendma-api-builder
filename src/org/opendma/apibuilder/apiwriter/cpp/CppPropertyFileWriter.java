package org.opendma.apibuilder.apiwriter.cpp;

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

public class CppPropertyFileWriter extends AbstractPropertyFileWriter
{
    
    protected OdmaApiWriter apiWriter;
    
    public CppPropertyFileWriter(OdmaApiWriter writer)
    {
        apiWriter = writer;
    }

    protected void writePropertyFileHeader(ApiDescription apiDescription, List<String> requiredImports, PrintWriter out) throws IOException
    {
        Iterator<String> itRequiredImports = requiredImports.iterator();
        while(itRequiredImports.hasNext())
        {
            String importPackage = itRequiredImports.next();
            out.println("#include "+importPackage);
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
        String returnType = scalarTypeDescription.isReference() ? "std::optional<OdmaObject>" : apiWriter.getScalarDataType(scalarTypeDescription,false,false);
        out.println("");
        out.println("        /**");
        out.println("        * @brief Retrieves the "+scalarName+" value of this property if and only if");
        out.println("        * the data type of this property is a single valued "+scalarName+".");
        out.println("        * @return The "+returnType+" value of this property");
        out.println("        * @throws OdmaInvalidDataTypeException if the data type of this property is not a single-valued "+scalarName+".");
        out.println("        */");
        out.println("        virtual "+returnType+" get"+scalarName+"() = 0;");
    }

    protected void writeMultiValueScalarAccess(ScalarTypeDescription scalarTypeDescription, PrintWriter out) throws IOException
    {
        String scalarName =  scalarTypeDescription.getName();
        String returnType = scalarTypeDescription.isReference() ? "OdmaObjectEnumeration" : apiWriter.getScalarDataType(scalarTypeDescription,true,true);
        out.println("");
        out.println("        /**");
        out.println("        * @brief Retrieves the "+scalarName+" value of this property if and only if");
        out.println("        * the data type of this property is a single valued "+scalarName+".");
        out.println("        * @return The "+returnType+" value of this property");
        out.println("        * @throws OdmaInvalidDataTypeException if the data type of this property is not a single-valued "+scalarName+".");
        out.println("        */");
        out.println("        virtual "+returnType+" get"+scalarName+(scalarTypeDescription.isReference()?"Vector":"List")+"() = 0;");
    }

    protected void appendRequiredImportsGlobal(ImportsList requiredImports)
    {
        requiredImports.registerImport("<any>");
        requiredImports.registerImport("<memory>");
        requiredImports.registerImport("\"OdmaQName.h\"");
        requiredImports.registerImport("\"OdmaType.h\"");
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
