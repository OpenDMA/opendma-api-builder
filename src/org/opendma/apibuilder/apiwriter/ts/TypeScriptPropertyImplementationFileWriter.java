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

public class TypeScriptPropertyImplementationFileWriter extends AbstractPropertyFileWriter
{
    
    protected OdmaApiWriter apiWriter;
    
    public TypeScriptPropertyImplementationFileWriter(OdmaApiWriter writer)
    {
        apiWriter = writer;
    }

    protected void writePropertyFileHeader(ApiDescription apiDescription, List<String> requiredImports, PrintWriter out) throws IOException
    {
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
        out.println("");
        out.println("    /**");
        out.println("     * Sets the value of this property. The type and class of the given newValue has to match the");
        out.println("     * data type of this OdmaProperty.");
        out.println("     * @param newValue The new value to set.");
        out.println("     * @throws OdmaInvalidDataTypeException If the type of the newValue does not match the data type of this OdmaProperty.");
        out.println("     * @throws OdmaAccessDeniedException If this OdmaProperty is read-only or cannot be set by the current user.");
        out.println("     */");
        out.println("    setValue(newValue: any): void {");
        out.println("        if(this.readOnly) {");
        out.println("            throw new OdmaAccessDeniedException(\"Cannot modify a read-only property.\");");
        out.println("        }");
        out.println("        if(newValue == null) {");
        out.println("            this.value = null;");
        out.println("            this.dirty = true;");
        out.println("            return;");
        out.println("        }");
        out.println("        if(this.multiValue) {");
        writeGenericSectionSwitch(apiDescription,out,true);
        out.println("        } else {");
        writeGenericSectionSwitch(apiDescription,out,false);
        out.println("        }");
        out.println("        this.dirty = true;");
        out.println("    }");
    }
    
    private String generatePropertyDataTypeDescription(boolean multivalue, ScalarTypeDescription scalarTypeDescription)
    {
        return "This property has a "+(multivalue?"multi-valued":"single-valued")+" "+scalarTypeDescription.getName()+" data type";
    }
    
    private String javaScriptValueTest(ScalarTypeDescription scalarTypeDescription, String test)
    {
        String name = scalarTypeDescription.getName().toUpperCase();
        if("STRING".equals(name))
        {
            return "typeof "+test+" == \"string\"";
        }
        if("INTEGER".equals(name))
        {
            return "Number.isInteger("+test+")";
        }
        if("SHORT".equals(name))
        {
            return "Number.isInteger("+test+")";
        }
        if("LONG".equals(name))
        {
            return "Number.isInteger("+test+")";
        }
        if("FLOAT".equals(name))
        {
            return "typeof "+test+" == \"number\"";
        }
        if("DOUBLE".equals(name))
        {
            return "typeof "+test+" == \"number\"";
        }
        if("BOOLEAN".equals(name))
        {
            return "typeof "+test+" == \"boolean\"";
        }
        if("DATETIME".equals(name))
        {
            return test+" instanceof Date";
        }
        if("BLOB".equals(name))
        {
            return test+" instanceof Uint8Array";
        }
        if("REFERENCE".equals(name))
        {
            return test+" instanceof OdmaObject";
        }
        if("CONTENT".equals(name))
        {
            return test+" instanceof OdmaContent";
        }
        if("ID".equals(name))
        {
            return test+" instanceof OdmaId";
        }
        if("GUID".equals(name))
        {
            return test+" instanceof OdmaGuid";
        }
        throw new RuntimeException("Unknown data type: "+name);
    }

    protected void writeGenericSectionSwitch(ApiDescription apiDescription, PrintWriter out, boolean multivalue) throws IOException
    {
        List<ScalarTypeDescription> scalarTypes = apiDescription.getScalarTypes();
        out.println("            switch(this.dataType) {");
        Iterator<ScalarTypeDescription> itScalarTypes = scalarTypes.iterator();
        while(itScalarTypes.hasNext())
        {
            ScalarTypeDescription scalarTypeDescription = itScalarTypes.next();
            String constantScalarTypeName = scalarTypeDescription.getName().toUpperCase();
            out.println("            case OdmaType."+constantScalarTypeName+":");
            if(multivalue)
            {
                String valueTest = javaScriptValueTest(scalarTypeDescription,"item");
                out.println("                if(Array.isArray(newValue) && newValue.every(item => "+valueTest+")) {");
            }
            else
            {
                String valueTest = javaScriptValueTest(scalarTypeDescription,"newValue");
                out.println("                if("+valueTest+") {");
            }
            out.println("                    this.value = newValue;");
            out.println("                } else ");
            String jsType = multivalue ? (scalarTypeDescription.isReference() ? "OdmaObject[]" : apiWriter.getScalarDataType(scalarTypeDescription,false,false)+"[]") : (scalarTypeDescription.isReference() ? "OdmaObject" : apiWriter.getScalarDataType(scalarTypeDescription,false,false));
            out.println("                    throw new OdmaInvalidDataTypeException(\""+generatePropertyDataTypeDescription(multivalue, scalarTypeDescription)+". It can only be set to values assignable to `"+jsType+"`\");");
            out.println("                }");
            out.println("                break;");
        }
        out.println("            default:");
        out.println("                throw new OdmaRuntimeException(\"OdmaProperty initialized with unknown data type \"+dataType);");
        out.println("            }");
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
        out.println("     * @throws OdmaInvalidDataTypeException If the data type of this property is not a single-valued "+scalarName+".");
        out.println("     */");
        out.println("    get"+scalarName+"(): "+returnType+" {");
        String constantScalarTypeName = scalarTypeDescription.getName().toUpperCase();
        out.println("        if( (multivalue == false) && (dataType == OdmaType."+constantScalarTypeName+") )");
        out.println("        {");
        out.println("            return this._value;");
        out.println("        }");
        out.println("        else");
        out.println("        {");
        out.println("            throw new OdmaInvalidDataTypeException(\"This property has a different data type and/or cardinality. It cannot return values with `get"+scalarName+"()`\");");
        out.println("        }");
        out.println("    }");
    }

    protected void writeMultiValueScalarAccess(ScalarTypeDescription scalarTypeDescription, PrintWriter out) throws IOException
    {
        String scalarName =  scalarTypeDescription.getName();
        String returnType = scalarTypeDescription.isReference() ? "OdmaObject[]" : apiWriter.getScalarDataType(scalarTypeDescription,true,true);
        out.println("");
        out.println("    /**");
        out.println("     * Retrieves the "+scalarName+" value of this property if and only if");
        out.println("     * the data type of this property is a multi valued "+scalarName+".");
        out.println("     *");
        out.println("     * @returns The "+scalarName+" value of this property");
        out.println("     * @throws OdmaInvalidDataTypeException If the data type of this property is not a multi-valued "+scalarName+".");
        out.println("     */");
        out.println("    get"+scalarName+"Array(): "+returnType+" {");
        String constantScalarTypeName = scalarTypeDescription.getName().toUpperCase();
        out.println("        if( (multivalue == true) && (dataType == OdmaType."+constantScalarTypeName+") )");
        out.println("        {");
        out.println("            return this.value;");
        out.println("        }");
        out.println("        else");
        out.println("        {");
        out.println("            throw new OdmaInvalidDataTypeException(\"This property has a different data type and/or cardinality. It cannot return values with `get"+scalarName+"Array()`\");");
        out.println("        }");
        out.println("    }");
    }

    protected void appendRequiredImportsGlobal(ImportsList requiredImports)
    {
    }

    protected void appendRequiredImportsScalarAccess(ImportsList requiredImports, ScalarTypeDescription scalarTypeDescription)
    {
    }

}
