package org.opendma.apibuilder.apiwriter.php;

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

public class PhpPropertyFileWriter extends AbstractPropertyFileWriter
{
    
    protected OdmaApiWriter apiWriter;
    
    public PhpPropertyFileWriter(OdmaApiWriter writer)
    {
        apiWriter = writer;
    }

    protected void writePropertyFileHeader(ApiDescription apiDescription, List<String> requiredImports, PrintWriter out) throws IOException
    {
        out.println("<?php");
        out.println("");
        out.println("namespace OpenDMA;");
        Iterator<String> itRequiredImports = requiredImports.iterator();
        while(itRequiredImports.hasNext())
        {
            String importPackage = itRequiredImports.next();
            out.println("use "+importPackage+";");
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
        String returnType = scalarTypeDescription.isReference() ? "?OdmaObject" : apiWriter.getScalarDataType(scalarTypeDescription,false,false);
        out.println("");
        out.println("    /**");
        out.println("     * Retrieves the "+scalarName+" value of this property if and only if");
        out.println("     * the data type of this property is a single valued "+scalarName+".");
        out.println("     *");
        out.println("     * @return "+returnType+" The value of this property");
        out.println("     * @throws OdmaInvalidDataTypeException If the data type of this property is not a single-valued "+scalarName+".");
        out.println("     */");
        out.println("    public function get"+scalarName+"(): "+returnType+";");
    }

    protected void writeMultiValueScalarAccess(ScalarTypeDescription scalarTypeDescription, PrintWriter out) throws IOException
    {
        String scalarName =  scalarTypeDescription.getName();
        String returnType = scalarTypeDescription.isReference() ? "array<OdmaObject>" : apiWriter.getScalarDataType(scalarTypeDescription,true,true);
        out.println("");
        out.println("    /**");
        out.println("     * Retrieves the "+scalarName+" value of this property if and only if");
        out.println("     * the data type of this property is a multi valued "+scalarName+".");
        out.println("     *");
        out.println("     * @return "+returnType+" The value of this property");
        out.println("     * @throws OdmaInvalidDataTypeException If the data type of this property is not a multi-valued "+scalarName+".");
        out.println("     */");
        out.println("    public function get"+scalarName+"Array(): array;");
    }

    protected void appendRequiredImportsGlobal(ImportsList requiredImports)
    {
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
