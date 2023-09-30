package org.opendma.apibuilder.apiwriter.py;

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

public class PythonPropertyImplementationFileWriter extends AbstractPropertyFileWriter
{
    
    protected OdmaApiWriter apiWriter;
    
    public PythonPropertyImplementationFileWriter(OdmaApiWriter writer)
    {
        apiWriter = writer;
    }

    protected void writePropertyFileHeader(ApiDescription apiDescription, List<String> requiredImports, PrintWriter out) throws IOException
    {
        out.println();
        out.println();
        InputStream templateIn = apiWriter.getTemplateAsStream("OdmaPropertyImplementation.Header");
        BufferedReader templareReader = new BufferedReader(new InputStreamReader(templateIn));
        String templateLine = null;
        while( (templateLine = templareReader.readLine()) != null)
        {
            out.println(templateLine);
        }
    }

    protected void writePropertyFileFooter(ApiDescription apiDescription, PrintWriter out) throws IOException
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
        out.println("");
        out.println("    def set_value(self, new_value: Any) -> None:");
        out.println("        \"\"\"");
        out.println("        Sets the value of this property. The type and classof the given");
        out.println("        new_value has to match the data type of this OdmaProperty.");
        out.println("");
        out.println("        :param new_value: the new value to set this property to.");
        out.println("        :raises OdmaInvalidDataTypeException: Raised if the type of the assigned value does not match the data type of this OdmaProperty.");
        out.println("        :raises OdmaAccessDeniedException: Raised if this OdmaProperty is read-only or cannot be set by the current user.");
        out.println("        \"\"\"");
        out.println("        if self._read_only:");
        out.println("            raise OdmaAccessDeniedException(\"Cannot modify a read-only property.\")");
        out.println("        if new_value is None:");
        out.println("            self._value = None");
        out.println("            self._dirty = True");
        out.println("            return");
        out.println("        if self._multi_value:");
        writeGenericSectionSwitch(apiDescription,out,true);
        out.println("        else:");
        writeGenericSectionSwitch(apiDescription,out,false);
        out.println("        self._dirty = True");
    }
    
    private String generatePropertyDataTypeDescription(boolean multivalue, ScalarTypeDescription scalarTypeDescription)
    {
        return "This property has a "+(multivalue?"multi-valued":"single-valued")+" "+scalarTypeDescription.getName()+" data type";
    }

    protected void writeGenericSectionSwitch(ApiDescription apiDescription, PrintWriter out, boolean multivalue) throws IOException
    {
        List<ScalarTypeDescription> scalarTypes = apiDescription.getScalarTypes();
        out.println("            match self._data_type:");
        Iterator<ScalarTypeDescription> itScalarTypes = scalarTypes.iterator();
        while(itScalarTypes.hasNext())
        {
            ScalarTypeDescription scalarTypeDescription = itScalarTypes.next();
            String constantScalarTypeName = scalarTypeDescription.getName().toUpperCase();
            out.println("                case OdmaType."+constantScalarTypeName+":");
            String pyReturnType = (scalarTypeDescription.isReference() ? "OdmaObject" : apiWriter.getScalarDataType(scalarTypeDescription,false,true));
            if(multivalue)
            {
                if(scalarTypeDescription.isReference())
                {
                    out.println("                    if isinstance(new_value, Iterable):");
                }
                else
                {
                    out.println("                    if isinstance(new_value, list) and all(isinstance(item, "+pyReturnType+") for item in new_value):");
                }
            }
            else
            {
                out.println("                    if isinstance(new_value, "+pyReturnType+"):");
            }
            out.println("                        self._value = new_value");
            out.println("                    else:");
            String pyType = multivalue ? (scalarTypeDescription.isReference() ? "Iterable[TOdmaObject]" : "list["+apiWriter.getScalarDataType(scalarTypeDescription,false,true)+"]") : (scalarTypeDescription.isReference() ? "OdmaObject" : apiWriter.getScalarDataType(scalarTypeDescription,false,false));
            out.println("                        raise OdmaInvalidDataTypeException(\""+generatePropertyDataTypeDescription(multivalue, scalarTypeDescription)+". It can only be set to values assignable to `"+pyType+"`\");");
        }
        out.println("                case _:");
        out.println("                    raise OdmaException(\"OdmaProperty initialized with unknown data type \"+dataType);");
    }

    protected void writeSingleValueScalarAccess(ScalarTypeDescription scalarTypeDescription, PrintWriter out) throws IOException
    {
        String scalarName =  scalarTypeDescription.getName();
        String returnType = scalarTypeDescription.isReference() ? "Optional[OdmaObject]" : apiWriter.getScalarDataType(scalarTypeDescription,false,false);
        out.println("");
        out.println("    def get_"+scalarName.toLowerCase()+"(self) -> "+returnType+":");
        out.println("        \"\"\" Retrieves the "+scalarName+" value of this property if and only if");
        out.println("        the data type of this property is a single valued "+scalarName+".");
        out.println("        \"\"\"");
        String constantScalarTypeName = scalarTypeDescription.getName().toUpperCase();
        out.println("        if self._multi_value == False and self._data_type == OdmaType."+constantScalarTypeName+":");
        out.println("            return self._value");
        out.println("        raise OdmaInvalidDataTypeException(\"This property has a different data type and/or cardinality. It cannot return values with `get_"+scalarName.toLowerCase()+"(self)`\");");
    }

    protected void writeMultiValueScalarAccess(ScalarTypeDescription scalarTypeDescription, PrintWriter out) throws IOException
    {
        String scalarName =  scalarTypeDescription.getName();
        String returnType = scalarTypeDescription.isReference() ? "Iterable[TOdmaObject]" : apiWriter.getScalarDataType(scalarTypeDescription,true,true);
        out.println("");
        out.println("    def get_"+scalarName.toLowerCase()+"_"+(scalarTypeDescription.isReference()?"iterable":"list")+"(self) -> "+returnType+":");
        out.println("        \"\"\" Retrieves the "+scalarName+" value of this property if and only if");
        out.println("        the data type of this property is a multi valued "+scalarName+".");
        out.println("        \"\"\"");
        String constantScalarTypeName = scalarTypeDescription.getName().toUpperCase();
        out.println("        if self._multi_value == True and self._data_type == OdmaType."+constantScalarTypeName+":");
        out.println("            return self._value");
        out.println("        raise OdmaInvalidDataTypeException(\"This property has a different data type and/or cardinality. It cannot return values with `get_"+scalarName.toLowerCase()+"_list(self)`\");");
    }

    protected void appendRequiredImportsGlobal(ImportsList requiredImports)
    {
    }

    protected void appendRequiredImportsScalarAccess(ImportsList requiredImports, ScalarTypeDescription scalarTypeDescription)
    {
    }

}
