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

public class CsPropertyImplementationFileWriter extends AbstractPropertyFileWriter
{
    
    protected OdmaApiWriter apiWriter;
    
    public CsPropertyImplementationFileWriter(OdmaApiWriter writer)
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
        out.println("    }");
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
        out.println("");
        out.println("        protected void SetValueInternal(object? newValue)");
        out.println("        {");
        out.println("            if(newValue == null)");
        out.println("            {");
        out.println("                if(_multiValue)");
        out.println("                {");
        out.println("                    throw new OdmaInvalidDataTypeException(\"Multi-valued properties must not be `null`. If a value is not required, the collection can be empty.\");");
        out.println("                }");
        out.println("                _value = null;");
        out.println("                _dirty = true;");
        out.println("                return;");
        out.println("            }");
        out.println("            if(_multiValue)");
        out.println("            {");
        writeGenericSectionSwitch(apiDescription,out,true);
        out.println("            }");
        out.println("            else");
        out.println("            {");
        writeGenericSectionSwitch(apiDescription,out,false);
        out.println("            }");
        out.println("            _dirty = true;");
        out.println("        }");
    }
    
    private String generatePropertyDataTypeDescription(boolean multivalue, ScalarTypeDescription scalarTypeDescription)
    {
        return "This property has a "+(multivalue?"multi-valued":"single-valued")+" "+scalarTypeDescription.getName()+" data type";
    }

    protected void writeGenericSectionSwitch(ApiDescription apiDescription, PrintWriter out, boolean multivalue) throws IOException
    {
        List<ScalarTypeDescription> scalarTypes = apiDescription.getScalarTypes();
        out.println("                switch(_type)");
        out.println("                {");
        Iterator<ScalarTypeDescription> itScalarTypes = scalarTypes.iterator();
        while(itScalarTypes.hasNext())
        {
            ScalarTypeDescription scalarTypeDescription = itScalarTypes.next();
            String constantScalarTypeName = scalarTypeDescription.getName().toUpperCase();
            out.println("                case OdmaType."+constantScalarTypeName+":");
            String csTestType = multivalue ? (scalarTypeDescription.isReference() ? "IEnumerable<IOdmaObject>" : apiWriter.getScalarDataType(scalarTypeDescription,true,true)) : (scalarTypeDescription.isReference() ? "IOdmaObject" : apiWriter.getScalarDataType(scalarTypeDescription,false,true));
            out.println("                    if(newValue is "+csTestType+")");
            out.println("                    {");
            out.println("                        _value = newValue;");
            out.println("                    }");
            out.println("                    else");
            out.println("                    {");
            String csType = multivalue ? (scalarTypeDescription.isReference() ? "IEnumerable<IOdmaObject>" : apiWriter.getScalarDataType(scalarTypeDescription,true,true)) : (scalarTypeDescription.isReference() ? "IOdmaObject?" : apiWriter.getScalarDataType(scalarTypeDescription,false,false));
            out.println("                        throw new OdmaInvalidDataTypeException(\""+generatePropertyDataTypeDescription(multivalue, scalarTypeDescription)+". It can only be set to values assignable to `"+csType+"`\");");
            out.println("                    }");
            out.println("                    break;");
        }
        out.println("                default:");
        out.println("                    throw new InvalidOperationException(\"OdmaProperty initialized with unknown data type \"+_type);");
        out.println("                }");
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
        out.println("        public "+returnType+" Get"+scalarName+"()");
        out.println("        {");
        String constantScalarTypeName = scalarTypeDescription.getName().toUpperCase();
        out.println("            if( (_multiValue == false) && (_type == OdmaType."+constantScalarTypeName+") )");
        out.println("            {");
        out.println("                EnforceValue();");
        out.println("                return ("+returnType+")_value;");
        out.println("            }");
        out.println("            else");
        out.println("            {");
        out.println("                throw new OdmaInvalidDataTypeException(\"This property has a different data type and/or cardinality. It cannot return values with `Get"+scalarName+"()`\");");
        out.println("            }");
        out.println("        }");
        if(scalarTypeDescription.isReference())
		{
			out.println("");
	        out.println("    public OdmaId get"+scalarName+"Id() throws OdmaInvalidDataTypeException;");
	        out.println("        /// <summary>");
	        out.println("        /// Retrieves the OdmaId of the "+scalarName+" value of this property if and only if");
	        out.println("        /// the data type of this property is a single valued "+scalarName+".");
	        out.println("        /// Based on the PropertyResolutionState, it is possible that this OdmaId is immediately available");
	        out.println("        /// while the OdmaObject requires an additional round-trip to the server.");
	        out.println("        /// </summary>");
	        out.println("        /// <returns>");
	        out.println("        /// The OdmaId of the "+returnType+" value of this property");
	        out.println("        /// </returns>");
	        out.println("        /// <exception cref=\"OdmaInvalidDataTypeException\">");
	        out.println("        /// Thrown if the data type of this property is not a single-valued "+scalarName+".");
	        out.println("        /// </exception>");
	        out.println("        OdmaId? Get"+scalarName+"Id()");
	        out.println("        {");
	        out.println("            if( (_multiValue == false) && (_type == OdmaType."+constantScalarTypeName+") )");
	        out.println("            {");
	        out.println("                if(_valueProvider == null)");
	        out.println("                {");
	        out.println("                    return (("+returnType+")_value).Id;");
	        out.println("                }");
	        out.println("                else if(_valueProvider.HasReferenceId)");
	        out.println("                {");
	        out.println("                    return _valueProvider.GetReferenceId();");
	        out.println("                }");
	        out.println("                else");
	        out.println("                {");
	        out.println("                    EnforceValue();");
	        out.println("                    return (("+returnType+")_value).Id;");
	        out.println("                }");
	        out.println("            }");
	        out.println("            else");
	        out.println("            {");
	        out.println("                throw new OdmaInvalidDataTypeException(\"This property has a different data type and/or cardinality. It cannot return values with `Get"+scalarName+"()`\");");
	        out.println("            }");
	        out.println("        }");
		}
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
        out.println("        public "+returnType+" Get"+scalarName+(scalarTypeDescription.isReference()?"Enumerable":"List")+"()");
        out.println("        {");
        String constantScalarTypeName = scalarTypeDescription.getName().toUpperCase();
        out.println("            if( (_multiValue == true) && (_type == OdmaType."+constantScalarTypeName+") )");
        out.println("            {");
        out.println("                EnforceValue();");
        out.println("                return _value is "+returnType+" ret ? ret : throw new InvalidOperationException(\"Implementation error.\");");
        out.println("            }");
        out.println("            else");
        out.println("            {");
        out.println("                throw new OdmaInvalidDataTypeException(\"This property has a different data type and/or cardinality. It cannot return values with `Get"+scalarName+(scalarTypeDescription.isReference()?"Enumerable":"List")+"()`\");");
        out.println("            }");
        out.println("        }");
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
