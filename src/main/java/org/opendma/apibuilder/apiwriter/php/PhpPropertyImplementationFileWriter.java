package org.opendma.apibuilder.apiwriter.php;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.opendma.apibuilder.OdmaApiWriter;
import org.opendma.apibuilder.apiwriter.AbstractPropertyImplementationFileWriter;
import org.opendma.apibuilder.apiwriter.ImportsList;
import org.opendma.apibuilder.structure.ApiDescription;
import org.opendma.apibuilder.structure.ScalarTypeDescription;

public class PhpPropertyImplementationFileWriter extends AbstractPropertyImplementationFileWriter
{
    
    protected OdmaApiWriter apiWriter;
    
    public PhpPropertyImplementationFileWriter(OdmaApiWriter writer)
    {
        apiWriter = writer;
    }

    protected void writePropertyImplementationFileHeader(ApiDescription apiDescription, List<String> requiredImports, PrintWriter out) throws IOException
    {
        out.println("<?php");
        out.println("");
        out.println("namespace OpenDMA\\Api;");
        Iterator<String> itRequiredImports = requiredImports.iterator();
        while(itRequiredImports.hasNext())
        {
            String importPackage = itRequiredImports.next();
            out.println("use "+importPackage+";");
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
    
    private static Set<String> scalarPhpTypes = new HashSet<String>(Arrays.asList("string", "int", "float", "bool" ));

    protected void writeGenericSection(ApiDescription apiDescription, PrintWriter out) throws IOException
    {
        InputStream templateIn = apiWriter.getTemplateAsStream("OdmaPropertyImplementation.Generic");
        BufferedReader templareReader = new BufferedReader(new InputStreamReader(templateIn));
        String templateLine = null;
        while( (templateLine = templareReader.readLine()) != null)
        {
            out.println(templateLine);
        }
        out.println("    private function checkValue(mixed $value, OdmaType $expectedType): bool");
        out.println("    {");
        Iterator<ScalarTypeDescription> itScalarTypes = apiDescription.getScalarTypes().iterator();
        out.println("        switch($expectedType) {");
        while(itScalarTypes.hasNext())
        {
            ScalarTypeDescription scalarTypeDescription = itScalarTypes.next();
            String constantScalarTypeName = scalarTypeDescription.getName().toUpperCase();
            String phpType = scalarTypeDescription.isReference() ? "OdmaObject" : apiWriter.getScalarDataType(scalarTypeDescription,false,true);
            out.println("            case OdmaType::"+constantScalarTypeName+":");
            if(scalarPhpTypes.contains(phpType))
            {
                out.println("                return is_"+phpType+"($value);");
            }
            else
            {
                out.println("                return $value instanceof "+phpType+";");
            }
            out.println("                break;");
        }
        out.println("            default:");
        out.println("                throw new OdmaRuntimeException(\"OdmaProperty initialized with unknown data type \".$expectedType->name);");
        out.println("        }");
        out.println("    }");
        out.println("");
        out.println("    /**");
        out.println("     * Sets the value of this property. The type and class of the given $newValue has to match the");
        out.println("     * data type of this OdmaProperty.");
        out.println("     * ");
        out.println("     * @param mixed $newValue The new value to set.");
        out.println("     * @throws OdmaInvalidDataTypeException If the type of the $newValue does not match the data type of this OdmaProperty.");
        out.println("     * @throws OdmaAccessDeniedException If this OdmaProperty is read-only or cannot be set by the current user.");
        out.println("     */");
        out.println("    public function setValue(mixed $newValue): void");
        out.println("    {");
        out.println("        if ($this->readOnly) {");
        out.println("            throw new OdmaAccessDeniedException();");
        out.println("        }");
        out.println("        if ($newValue === null) {");
        out.println("            $this->value = null;");
        out.println("            $this->dirty = true;");
        out.println("            return;");
        out.println("        }");
        out.println("        $expectedType = match ($this->dataType) {");
        itScalarTypes = apiDescription.getScalarTypes().iterator();
        while(itScalarTypes.hasNext())
        {
            ScalarTypeDescription scalarTypeDescription = itScalarTypes.next();
            String constantScalarTypeName = scalarTypeDescription.getName().toUpperCase();
            out.println("            OdmaType::"+constantScalarTypeName+" => '"+(scalarTypeDescription.isReference() ? "OdmaObject" : apiWriter.getScalarDataType(scalarTypeDescription,false,true))+"',");
        }
        out.println("            default =>  throw new OdmaRuntimeException(\"OdmaProperty initialized with unknown data type \".$this->dataType->name)");
        out.println("        };");
        out.println("        if ($this->multiValue) {");
        out.println("            if (!$this->checkListAndValues($newValue, $this->dataType)) {");
        out.println("                throw new OdmaInvalidDataTypeException(\"This property has a multi-valued \".$this->dataType->name.\" data type. It can only be set to values assignable to \".$expectedType);");
        out.println("            }");
        out.println("        } else {");
        out.println("            if (!$this->checkValue($newValue, $this->dataType)) {");
        out.println("                throw new OdmaInvalidDataTypeException(\"This property has a single-valued \".$this->dataType->name.\" data type. It can only be set to values assignable to \".$expectedType);");
        out.println("            }");
        out.println("        }");
        out.println("        $this->value = $newValue;");
        out.println("        $this->dirty = true;");
        out.println("    }");
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
        out.println("    public function get"+scalarName+"(): "+returnType+"");
        out.println("    {");
        String constantScalarTypeName = scalarTypeDescription.getName().toUpperCase();
        out.println("        if (!$this->multiValue && $this->dataType === OdmaType::"+constantScalarTypeName+") {");
        out.println("            return $this->value;");
        out.println("        } else {");
        out.println("            throw new OdmaInvalidDataTypeException(\"This property has a different data type and/or cardinality. It cannot return values with `get"+scalarName+"()`\");");
        out.println("        }");
        out.println("    }");
    }

    protected void writeMultiValueScalarAccess(ScalarTypeDescription scalarTypeDescription, PrintWriter out) throws IOException
    {
        String scalarName =  scalarTypeDescription.getName();
        String returnType = scalarTypeDescription.isReference() ? "IteratorAggregate<OdmaObject>" : apiWriter.getScalarDataType(scalarTypeDescription,true,true);
        out.println("");
        out.println("    /**");
        out.println("     * Retrieves the "+scalarName+" value of this property if and only if");
        out.println("     * the data type of this property is a multi valued "+scalarName+".");
        out.println("     *");
        out.println("     * @return "+returnType+" The value of this property");
        out.println("     * @throws OdmaInvalidDataTypeException If the data type of this property is not a multi-valued "+scalarName+".");
        out.println("     */");
        out.println("    public function get"+scalarName+(scalarTypeDescription.isReference()?"IteratorAggregate":"Array")+"(): "+(scalarTypeDescription.isReference()?"IteratorAggregate":"array"));
        out.println("    {");
        String constantScalarTypeName = scalarTypeDescription.getName().toUpperCase();
        out.println("        if ($this->multiValue && $this->dataType === OdmaType::"+constantScalarTypeName+") {");
        out.println("            return $this->value;");
        out.println("        } else {");
        out.println("            throw new OdmaInvalidDataTypeException(\"This property has a different data type and/or cardinality. It cannot return values with `get"+scalarName+(scalarTypeDescription.isReference()?"IteratorAggregate":"Array")+"()`\");");
        out.println("        }");
        out.println("    }");
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
