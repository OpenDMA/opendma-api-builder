package org.opendma.apibuilder.apiwriter.ts;

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

public class TypeScriptPropertyFileWriter extends AbstractPropertyFileWriter
{
    
    protected OdmaApiWriter apiWriter;
    
    public TypeScriptPropertyFileWriter(OdmaApiWriter writer)
    {
        apiWriter = writer;
    }

    protected void writePropertyFileHeader(ApiDescription apiDescription, List<String> requiredImports, PrintWriter out) throws IOException
    {
        Iterator<String> itRequiredImports = requiredImports.iterator();
        while(itRequiredImports.hasNext())
        {
            String importDeclaration = itRequiredImports.next();
            out.println("import { "+importDeclaration+" } from './"+importDeclaration+"';");
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
        String returnType = scalarTypeDescription.isReference() ? "OdmaObject | null" : apiWriter.getScalarDataType(scalarTypeDescription,false,false);
        out.println("");
        out.println("    /**");
        out.println("     * Retrieves the "+scalarName+" value of this property if and only if");
        out.println("     * the data type of this property is a single valued "+scalarName+".");
        out.println("     *");
        out.println("     * @returns The "+scalarName+" value of this property");
        out.println("     * @throws OdmaInvalidDataTypeError If the data type of this property is not a single-valued "+scalarName+".");
        out.println("     */");
        out.println("    get"+scalarName+"(): "+returnType+";");
    }

    protected void writeMultiValueScalarAccess(ScalarTypeDescription scalarTypeDescription, PrintWriter out) throws IOException
    {
        String scalarName =  scalarTypeDescription.getName();
        String returnType = scalarTypeDescription.isReference() ? "Iterable<OdmaObject>" : apiWriter.getScalarDataType(scalarTypeDescription,true,true);
        out.println("");
        out.println("    /**");
        out.println("     * Retrieves the "+scalarName+" value of this property if and only if");
        out.println("     * the data type of this property is a multi valued "+scalarName+".");
        out.println("     *");
        out.println("     * @returns The "+scalarName+" value of this property");
        out.println("     * @throws OdmaInvalidDataTypeError If the data type of this property is not a multi-valued "+scalarName+".");
        out.println("     */");
        out.println("    get"+scalarName+(scalarTypeDescription.isReference()?"Iterable":"Array")+"(): "+returnType+";");
    }

    protected void appendRequiredImportsGlobal(ImportsList requiredImports)
    {
        requiredImports.registerImport("OdmaQName");
        requiredImports.registerImport("OdmaType");
    }

    protected void appendRequiredImportsScalarAccess(ImportsList requiredImports, ScalarTypeDescription scalarTypeDescription)
    {
        if(scalarTypeDescription.isReference())
        {
            requiredImports.registerImport("OdmaObject");
            return;
        }
        requiredImports.registerImports(apiWriter.getScalarDataTypeImports(scalarTypeDescription,false,false));
        requiredImports.registerImports(apiWriter.getScalarDataTypeImports(scalarTypeDescription,true,true));
    }

}
