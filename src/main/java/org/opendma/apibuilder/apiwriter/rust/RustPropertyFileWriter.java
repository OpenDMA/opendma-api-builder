package org.opendma.apibuilder.apiwriter.rust;

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

public class RustPropertyFileWriter extends AbstractPropertyFileWriter
{
    
    protected OdmaApiWriter apiWriter;
    
    public RustPropertyFileWriter(OdmaApiWriter writer)
    {
        apiWriter = writer;
    }

    protected void writePropertyFileHeader(ApiDescription apiDescription, List<String> requiredImports, PrintWriter out) throws IOException
    {
        Iterator<String> itRequiredImports = requiredImports.iterator();
        while(itRequiredImports.hasNext())
        {
            String importDeclaration = (String)itRequiredImports.next();
            out.println("use "+importDeclaration+";");
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
        String returnType = scalarTypeDescription.isReference() ? "Option<&dyn OdmaObject>" : apiWriter.getScalarDataType(scalarTypeDescription,false,false);
        out.println("");
        out.println("    /// Gets the "+scalarName+" value of this property if and only if");
        out.println("    /// the data type of this property is a single valued "+scalarName+".");
        out.println("    ///");
        out.println("    /// Returns OdmaError::InvalidDataType if the data type of this property is not a single-valued "+scalarName+".");
        out.println("    fn get_"+scalarName.toLowerCase()+"(&self) -> Result<"+returnType+", OdmaError>;");
    }

    protected void writeMultiValueScalarAccess(ScalarTypeDescription scalarTypeDescription, PrintWriter out) throws IOException
    {
        String scalarName =  scalarTypeDescription.getName();
        String returnType = scalarTypeDescription.isReference() ? "Box<dyn Iterator<Item = &dyn OdmaObject> + '_>" : apiWriter.getScalarDataType(scalarTypeDescription,true,true);
        out.println("");
        out.println("    /// Gets the "+scalarName+" value of this property if and only if");
        out.println("    /// the data type of this property is a multi valued "+scalarName+".");
        out.println("    ///");
        out.println("    /// Returns OdmaError::InvalidDataType if the data type of this property is not a multi-valued "+scalarName+".");
        out.println("    fn get_"+scalarName.toLowerCase()+"_"+(scalarTypeDescription.isReference()?"iter":"vec")+"(&self) -> Result<"+returnType+", OdmaError>;");
    }

    protected void appendRequiredImportsGlobal(ImportsList requiredImports)
    {
        requiredImports.registerImport("crate::OdmaQName");
        requiredImports.registerImport("crate::OdmaType");
        requiredImports.registerImport("crate::OdmaError");
    }

    protected void appendRequiredImportsScalarAccess(ImportsList requiredImports, ScalarTypeDescription scalarTypeDescription)
    {
        if(scalarTypeDescription.isReference())
        {
            requiredImports.registerImport("crate::OdmaObject");
        }
        else
        {
            requiredImports.registerImports(apiWriter.getScalarDataTypeImports(scalarTypeDescription,false,false));
            requiredImports.registerImports(apiWriter.getScalarDataTypeImports(scalarTypeDescription,true,true));
        }
    }

}
