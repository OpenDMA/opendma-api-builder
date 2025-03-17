package org.opendma.apibuilder.apiwriter.go;

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

public class GoPropertyImplementationFileWriter extends AbstractPropertyImplementationFileWriter
{
    
    protected OdmaApiWriter apiWriter;
    
    public GoPropertyImplementationFileWriter(OdmaApiWriter writer)
    {
        apiWriter = writer;
    }

    protected void writePropertyImplementationFileHeader(ApiDescription apiDescription, List<String> requiredImports, PrintWriter out) throws IOException
    {
        out.println("package OpenDMAApi");
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
        out.println("// SetValue sets the value of this property.");
        out.println("// The type and class of the given newValue has to match the data type of this OdmaProperty.");
        out.println("//  Returns an OdmaInvalidDataTypeError if the type of the newValue does not match the data type of this OdmaProperty and an OdmaAccessDeniedError If this OdmaProperty is read-only or cannot be set by the current user");
        out.println("func (pi *OdmaPropertyImpl) SetValue(newValue interface{}) error {");
        out.println("    if pi.readOnly {");
        out.println("        return ErrAccessDenied");
        out.println("    }");
        out.println("    if newValue == nil {");
        out.println("        pi.value = nil");
        out.println("        pi.dirty = true");
        out.println("        return nil");
        out.println("    }");
        out.println("    validType := false");
        out.println("    if pi.multiValue {");
        out.println("        if pi.dataType == REFERENCE {");
        out.println("            _, validType = newValue.(OdmaObjectIterable)");
        out.println("        } else {");
        out.println("            if values, ok := newValue.([]interface{}); ok {");
        out.println("                validType = true");
        out.println("                for _, v := range values {");
        out.println("                    switch pi.dataType {");
        List<ScalarTypeDescription> scalarTypes = apiDescription.getScalarTypes();
        Iterator<ScalarTypeDescription> itScalarTypes = scalarTypes.iterator();
        while(itScalarTypes.hasNext())
        {
            ScalarTypeDescription scalarTypeDescription = itScalarTypes.next();
            if(scalarTypeDescription.isReference())
            {
                continue;
            }
            String constantScalarTypeName = scalarTypeDescription.getName().toUpperCase();
            out.println("                    case "+constantScalarTypeName+":");
            out.println("                        if _, valid = v.("+apiWriter.getScalarDataType(scalarTypeDescription,false,true)+"); !valid {");
            out.println("                            validType = false");
            out.println("                        }");
        }
        out.println("                    default");
        out.println("                        return errors.New(\"Implementation error\")");
        out.println("                    }");
        out.println("                }");
        out.println("            }");
        out.println("        }");
        out.println("    } else {");
        out.println("        switch pi.dataType {");
        itScalarTypes = scalarTypes.iterator();
        while(itScalarTypes.hasNext())
        {
            ScalarTypeDescription scalarTypeDescription = itScalarTypes.next();
            String constantScalarTypeName = scalarTypeDescription.getName().toUpperCase();
            out.println("        case "+constantScalarTypeName+":");
            out.println("             _, validType = newValue.("+(scalarTypeDescription.isReference() ? "OdmaObject" : apiWriter.getScalarDataType(scalarTypeDescription,false,true))+")");
        }
        out.println("        default");
        out.println("            return errors.New(\"Implementation error\")");
        out.println("        }");
        out.println("    }");
        out.println("    if !validType {");
        out.println("        return errors.New(\"invalid data type\")");
        out.println("    }");
        out.println("    pi.value = newValue");
        out.println("    pi.dirty = true");
        out.println("    return nil");
        out.println("}");
    }

    protected void writeSingleValueScalarAccess(ScalarTypeDescription scalarTypeDescription, PrintWriter out) throws IOException
    {
        String scalarName =  scalarTypeDescription.getName();
        String returnType = scalarTypeDescription.isReference() ? "*OdmaObject" : apiWriter.getScalarDataType(scalarTypeDescription,false,false);
        String goType = scalarTypeDescription.isReference() ? "OdmaObject" : apiWriter.getScalarDataType(scalarTypeDescription,false,true);
        out.println("");
        out.println("// Returns the "+scalarName+" value of this property if and only if");
        out.println("// the data type of this property is a single valued "+scalarName+".");
        out.println("// Returns an OdmaInvalidDataTypeError if the data type of this property is not a single-valued "+scalarName+".");
        out.println("func (pi *OdmaPropertyImpl) Get"+scalarName+"() ("+returnType+",error) {");
        String constantScalarTypeName = scalarTypeDescription.getName().toUpperCase();
        out.println("    if p.multiValue || p.dataType != "+constantScalarTypeName+" {");
        out.println("        return nil, errors.New(\"invalid type or cardinality for GetString\")");
        out.println("    }");
        out.println("    if ret, ok := p.value.("+goType+"); ok {");
        out.println("        return &ret, nil");
        out.println("    }");
        out.println("    return nil, errors.New(\"Implementation error\")");
        out.println("}");
    }

    protected void writeMultiValueScalarAccess(ScalarTypeDescription scalarTypeDescription, PrintWriter out) throws IOException
    {
        String scalarName =  scalarTypeDescription.getName();
        String returnType = scalarTypeDescription.isReference() ? "OdmaObjectIterable" : apiWriter.getScalarDataType(scalarTypeDescription,true,true);
        out.println("");
        out.println("// Returns the "+scalarName+" value of this property if and only if");
        out.println("// the data type of this property is a multi valued "+scalarName+".");
        out.println("// Returns an OdmaInvalidDataTypeError if the data type of this property is not a multi-valued "+scalarName+".");
        out.println("func (pi *OdmaPropertyImpl) Get"+scalarName+(scalarTypeDescription.isReference()?"Iterable":"Array")+"() ("+returnType+",error) {");
        String constantScalarTypeName = scalarTypeDescription.getName().toUpperCase();
        out.println("    if !p.multiValue || p.dataType != "+constantScalarTypeName+" {");
        out.println("        return nil, errors.New(\"invalid type or cardinality for GetString\")");
        out.println("    }");
        out.println("    if ret, ok := p.value.("+returnType+"); ok {");
        out.println("        return &ret, nil");
        out.println("    }");
        out.println("    return nil, errors.New(\"Implementation error\")");
        out.println("}");
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
