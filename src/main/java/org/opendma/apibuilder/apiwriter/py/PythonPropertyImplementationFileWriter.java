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
        out.println("    def _set_value_internal(self, new_value: Any) -> None:");
        out.println("        if new_value is None:");
        out.println("            if self._multi_value:");
        out.println("                raise OdmaInvalidDataTypeException(\"Multi-valued properties must not be `null`. If a value is not required, the collection can be empty.\");");
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
            out.println("                        raise OdmaInvalidDataTypeException(\""+generatePropertyDataTypeDescription(multivalue, scalarTypeDescription)+". It can only be set to values assignable to `"+pyType+"` but got \"+type(new_value).__name__);");

        }
        out.println("                case _:");
        out.println("                    raise OdmaException(\"OdmaProperty initialized with unknown data type \"+self.data_type);");
    }

    protected void writeSingleValueScalarAccess(ScalarTypeDescription scalarTypeDescription, PrintWriter out) throws IOException
    {
        String scalarName =  scalarTypeDescription.getName();
        String returnType = scalarTypeDescription.isReference() ? "Optional[TOdmaObject]" : apiWriter.getScalarDataType(scalarTypeDescription,false,false);
        out.println("");
        out.println("    def get_"+scalarName.toLowerCase()+"(self) -> "+returnType+":");
        out.println("        \"\"\" Retrieves the "+scalarName+" value of this property if and only if");
        out.println("        the data type of this property is a single valued "+scalarName+".");
        out.println("        \"\"\"");
        String constantScalarTypeName = scalarTypeDescription.getName().toUpperCase();
        out.println("        if self._multi_value == False and self._data_type == OdmaType."+constantScalarTypeName+":");
        out.println("            self._enforce_value()");
        out.println("            return self._value  # type: ignore[return-value]");
        out.println("        raise OdmaInvalidDataTypeException(\"This property has a different data type and/or cardinality. It cannot return values with `get_"+scalarName.toLowerCase()+"(self)`\");");
        if(scalarTypeDescription.isReference())
		{
            out.println("");
            out.println("    def get_"+scalarName.toLowerCase()+"_id(self) -> Optional[OdmaId]:");
            out.println("        \"\"\" Retrieves the OdmaId of the "+scalarName+" value of this property if and only if");
            out.println("        the data type of this property is a single valued "+scalarName+".");
	        out.println("        ");
	        out.println("        Based on the PropertyResolutionState, it is possible that this OdmaId is immediately available");
	        out.println("        while the OdmaObject requires an additional round-trip to the server.");
            out.println("        \"\"\"");
            out.println("        if self._multi_value == False and self._data_type == OdmaType."+constantScalarTypeName+":");
            out.println("            if self._value_provider is None:");
            out.println("                if self._value is not None:");
            out.println("                    if isinstance(self._value, OdmaObject):");
            out.println("                        return self._value.get_id()");
            out.println("                    else:");
            out.println("                        raise OdmaException(\"Internal error. Reference value is expected to be instance of OdmaObject\")");
            out.println("                else:");
            out.println("                    return None");
            out.println("            elif self._value_provider.has_reference_id():");
            out.println("                return self._value_provider.get_reference_id()");
            out.println("            else:");
            out.println("                self._enforce_value()");
            out.println("                if self._value is not None:");
            out.println("                    if isinstance(self._value, OdmaObject):");
            out.println("                        return self._value.get_id()");
            out.println("                    else:");
            out.println("                        raise OdmaException(\"Internal error. Reference value is expected to be instance of OdmaObject\")");
            out.println("                else:");
            out.println("                    return None");
            out.println("        raise OdmaInvalidDataTypeException(\"This property has a different data type and/or cardinality. It cannot return values with `get_"+scalarName.toLowerCase()+"(self)`\");");
		}
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
        out.println("            self._enforce_value()");
        out.println("            return self._value  # type: ignore[return-value]");
        out.println("        raise OdmaInvalidDataTypeException(\"This property has a different data type and/or cardinality. It cannot return values with `get_"+scalarName.toLowerCase()+"_list(self)`\");");
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
