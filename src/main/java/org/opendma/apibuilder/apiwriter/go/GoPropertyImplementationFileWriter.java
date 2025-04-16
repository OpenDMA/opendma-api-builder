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
        out.println("package opendma");
        out.println("");
        out.println("import (");
        Iterator<String> itRequiredImports = requiredImports.iterator();
        while(itRequiredImports.hasNext())
        {
            String importDeclaration = (String)itRequiredImports.next();
            out.println("    \""+importDeclaration+"\"");
        }
        out.println(")");
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
        out.println("    if pi.multiValue {");
        out.println("        switch pi.dataType {");
        List<ScalarTypeDescription> scalarTypes = apiDescription.getScalarTypes();
        Iterator<ScalarTypeDescription> itScalarTypes = scalarTypes.iterator();
        while(itScalarTypes.hasNext())
        {
            ScalarTypeDescription scalarTypeDescription = itScalarTypes.next();
            String constantScalarTypeName = scalarTypeDescription.getName().toUpperCase();
            String goType = scalarTypeDescription.isReference() ? "OdmaObjectIterable" : apiWriter.getScalarDataType(scalarTypeDescription,true,true);
            out.println("        case "+constantScalarTypeName+":");
            out.println("            _, valid := newValue.("+goType+")");
            out.println("            if !valid {");
            out.println("                return &OdmaInvalidDataTypeError{Message: \""+generatePropertyDataTypeDescription(true, scalarTypeDescription)+". It can only be set to values assignable to `"+goType+"`\"}");
            out.println("            }");
        }
        out.println("        default:");
        out.println("            return errors.New(\"Implementation error\")");
        out.println("        }");
        out.println("    } else {");
        out.println("        switch pi.dataType {");
        itScalarTypes = scalarTypes.iterator();
        while(itScalarTypes.hasNext())
        {
            ScalarTypeDescription scalarTypeDescription = itScalarTypes.next();
            String constantScalarTypeName = scalarTypeDescription.getName().toUpperCase();
            String goType = scalarTypeDescription.isReference() ? "OdmaObject" : apiWriter.getScalarDataType(scalarTypeDescription,false,true);
            out.println("        case "+constantScalarTypeName+":");
            out.println("            _, valid := newValue.("+goType+")");
            out.println("            if !valid {");
            out.println("                return &OdmaInvalidDataTypeError{Message: \""+generatePropertyDataTypeDescription(false, scalarTypeDescription)+". It can only be set to values assignable to `"+goType+"`\"}");
            out.println("            }");
        }
        out.println("        default:");
        out.println("            return errors.New(\"Implementation error\")");
        out.println("        }");
        out.println("    }");
        out.println("    pi.value = newValue");
        out.println("    pi.dirty = true");
        out.println("    return nil");
        out.println("}");
    }
    
    private String generatePropertyDataTypeDescription(boolean multivalue, ScalarTypeDescription scalarTypeDescription)
    {
        return "This property has a "+(multivalue?"multi-valued":"single-valued")+" "+scalarTypeDescription.getName()+" data type";
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
        out.println("    if pi.multiValue || pi.dataType != "+constantScalarTypeName+" {");
        out.println("        return nil, errors.New(\"invalid type or cardinality for Get"+scalarName+"()\")");
        out.println("    }");
        out.println("    if ret, ok := pi.value.("+goType+"); ok {");
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
        out.println("    if !pi.multiValue || pi.dataType != "+constantScalarTypeName+" {");
        out.println("        return nil, errors.New(\"invalid type or cardinality for Get"+scalarName+(scalarTypeDescription.isReference()?"Iterable":"Slice")+"()\")");
        out.println("    }");
        out.println("    if ret, ok := pi.value.("+returnType+"); ok {");
        out.println("        return ret, nil");
        out.println("    }");
        out.println("    return nil, errors.New(\"Implementation error\")");
        out.println("}");
    }

    protected void appendRequiredImportsGlobal(ImportsList requiredImports)
    {
        requiredImports.registerImport("errors");
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
