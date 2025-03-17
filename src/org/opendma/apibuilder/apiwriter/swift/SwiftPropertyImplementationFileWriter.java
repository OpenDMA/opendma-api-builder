package org.opendma.apibuilder.apiwriter.swift;

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

public class SwiftPropertyImplementationFileWriter extends AbstractPropertyImplementationFileWriter
{
    
    protected OdmaApiWriter apiWriter;
    
    public SwiftPropertyImplementationFileWriter(OdmaApiWriter writer)
    {
        apiWriter = writer;
    }

    protected void writePropertyImplementationFileHeader(ApiDescription apiDescription, List<String> requiredImports, PrintWriter out) throws IOException
    {
        Iterator<String> itRequiredImports = requiredImports.iterator();
        while(itRequiredImports.hasNext())
        {
            String importDeclaration = (String)itRequiredImports.next();
            out.println("import "+importDeclaration);
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
        out.println("    /// Sets the value of this property.");
        out.println("    func setValue(_ newValue: Any?) throws {");
        out.println("        if readOnly {");
        out.println("            throw OdmaError.accessDenied()");
        out.println("        }");
        out.println("        if newValue == nil {");
        out.println("            value = nil");
        out.println("            dirty = true");
        out.println("            return");
        out.println("        }");
        out.println("        if multiValue {");
        out.println("            if dataType == .REFERENCE {");
        out.println("                if newValue is Sequence {");
        out.println("                    value = newValue");
        out.println("                } else {");
        out.println("                    throw OdmaError.invalidDataType(details: \"Expected a Sequence<OdmaObject> for multi-valued property of data type \\(dataType)\")");
        out.println("                }");
        out.println("            } else {");
        out.println("                if let arrayValue = newValue as? [Any] {");
        out.println("                    switch dataType {");
        out.print("                    case ");
        List<ScalarTypeDescription> scalarTypes = apiDescription.getScalarTypes();
        Iterator<ScalarTypeDescription> itScalarTypes = scalarTypes.iterator();
        boolean first = true;
        while(itScalarTypes.hasNext())
        {
            ScalarTypeDescription scalarTypeDescription = itScalarTypes.next();
            if(scalarTypeDescription.isReference())
            {
                continue;
            }
            if(first)
            {
                first = false;
            }
            else
            {
                out.print("                         ");
            }
            String constantScalarTypeName = scalarTypeDescription.getName().toUpperCase();
            out.print("."+constantScalarTypeName+" where arrayValue.allSatisfy { $0 is "+(scalarTypeDescription.isReference() ? "OdmaObject" : apiWriter.getScalarDataType(scalarTypeDescription,false,true))+" }");
            if(itScalarTypes.hasNext())
            {
                out.println(",");
            }
            else
            {
                out.println(":");
            }
        }
        out.println("                        value = newValue");
        out.println("                    default:");
        out.println("                        throw OdmaError.invalidDataType(details: \"Invalid value for multi-valued property of data type \\(dataType)\")");
        out.println("                    }");
        out.println("                } else {");
        out.println("                    throw OdmaError.invalidDataType(details: \"Expected an array for multi-valued property of data type \\(dataType)\")");
        out.println("                }");
        out.println("            }");
        out.println("        } else {");
        out.println("            switch dataType {");
        out.print("            case ");
        itScalarTypes = scalarTypes.iterator();
        first = true;
        while(itScalarTypes.hasNext())
        {
            ScalarTypeDescription scalarTypeDescription = itScalarTypes.next();
            if(first)
            {
                first = false;
            }
            else
            {
                out.print("                 ");
            }
            String constantScalarTypeName = scalarTypeDescription.getName().toUpperCase();
            out.print("."+constantScalarTypeName+" where newValue is "+(scalarTypeDescription.isReference() ? "OdmaObject" : apiWriter.getScalarDataType(scalarTypeDescription,false,true)));
            if(itScalarTypes.hasNext())
            {
                out.println(",");
            }
            else
            {
                out.println(":");
            }
        }
        out.println("                value = newValue");
        out.println("            default:");
        out.println("                throw OdmaError.invalidDataType(details: \"Invalid value for single-valued property of data type \\(dataType)\")");
        out.println("            }");
        out.println("        }");
        out.println("    }");
    }

    protected void writeSingleValueScalarAccess(ScalarTypeDescription scalarTypeDescription, PrintWriter out) throws IOException
    {
        String scalarName =  scalarTypeDescription.getName();
        String returnType = scalarTypeDescription.isReference() ? "OdmaObject?" : apiWriter.getScalarDataType(scalarTypeDescription,false,false);
        out.println("");
        out.println("    /// Gets the "+scalarName+" value of this property if and only if");
        out.println("    /// the data type of this property is a single valued "+scalarName+".");
        out.println("    fn get"+scalarName+"() throws -> "+returnType+" {");
        String constantScalarTypeName = scalarTypeDescription.getName().toUpperCase();
        out.println("        guard !multiValue, dataType == ."+constantScalarTypeName+", let ret = values as? "+returnType+" else {");
        out.println("            throw OdmaError.invalidDataType(details: \"This property has a different data type and/or cardinality. It cannot return values with `get"+scalarName+"()`\")");
        out.println("        }");
        out.println("        return ret");
        out.println("    }");
    }

    protected void writeMultiValueScalarAccess(ScalarTypeDescription scalarTypeDescription, PrintWriter out) throws IOException
    {
        String scalarName =  scalarTypeDescription.getName();
        String returnType = scalarTypeDescription.isReference() ? "Sequence<OdmaObject>" : apiWriter.getScalarDataType(scalarTypeDescription,true,true);
        out.println("");
        out.println("    /// Gets the "+scalarName+" value of this property if and only if");
        out.println("    /// the data type of this property is a multi valued "+scalarName+".");
        out.println("    fn get"+scalarName+(scalarTypeDescription.isReference()?"Sequence":"List")+"() throws -> "+returnType+" {");
        String constantScalarTypeName = scalarTypeDescription.getName().toUpperCase();
        out.println("        guard multiValue, dataType == ."+constantScalarTypeName+", let ret = values as? "+returnType+" else {");
        out.println("            throw OdmaError.invalidDataType(details: \"This property has a different data type and/or cardinality. It cannot return values with `get"+scalarName+(scalarTypeDescription.isReference()?"Sequence":"List")+"()`\")");
        out.println("        }");
        out.println("        return ret");
        out.println("    }");
    }

    protected void appendRequiredImportsGlobal(ImportsList requiredImports)
    {
        requiredImports.registerImport("Foundation");
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
