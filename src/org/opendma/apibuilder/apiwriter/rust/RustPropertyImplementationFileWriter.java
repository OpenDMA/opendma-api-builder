package org.opendma.apibuilder.apiwriter.rust;

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

public class RustPropertyImplementationFileWriter extends AbstractPropertyImplementationFileWriter
{
    
    protected OdmaApiWriter apiWriter;
    
    public RustPropertyImplementationFileWriter(OdmaApiWriter writer)
    {
        apiWriter = writer;
    }

    protected void writePropertyImplementationFileHeader(ApiDescription apiDescription, List<String> requiredImports, PrintWriter out) throws IOException
    {
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
        out.println("    /// Returns OdmaInvalidDataTypeError if the type of the newValue does not match the data type of this OdmaProperty.");
        out.println("    /// Returns OdmaAccessDeniedError if this OdmaProperty is read-only or cannot be set by the current user.");
        out.println("    fn set_value(&mut self, new_value: Option<Box<dyn std::any::Any>>) -> Result<(), Box<dyn Error>> {");
        out.println("        if self.read_only {");
        out.println("            return Err(Box::new(OdmaAccessDeniedError));");
        out.println("        }");
        out.println("        if let Some(ref val) = new_value {");
        out.println("            let valid = match self.data_type {");
        List<ScalarTypeDescription> scalarTypes = apiDescription.getScalarTypes();
        Iterator<ScalarTypeDescription> itScalarTypes = scalarTypes.iterator();
        while(itScalarTypes.hasNext())
        {
            ScalarTypeDescription scalarTypeDescription = itScalarTypes.next();
            String constantScalarTypeName = scalarTypeDescription.getName().toUpperCase();
            if(scalarTypeDescription.isReference())
            {
                out.println("                OdmaType::"+constantScalarTypeName+" => (!self.multi_value && val.is::<OdmaObject>()) || (self.multi_value && self.check_list_and_values::<Iterator>(val)),");
            }
            else
            {
                String rustType = apiWriter.getScalarDataType(scalarTypeDescription,false,true);
                out.println("                OdmaType::"+constantScalarTypeName+" => (!self.multi_value && val.is::<"+rustType+">()) || (self.multi_value && self.check_list_and_values::<"+rustType+">(val)),");
            }
        }
        out.println("            };");
        out.println("            if !valid {");
        out.println("                return Err(Box::new(OdmaInvalidDataTypeError));");
        out.println("            }");
        out.println("        }");
        out.println("        self.value = new_value;");
        out.println("        self.dirty = true;");
        out.println("        Ok(())");
        out.println("    }");
    }

    protected void writeSingleValueScalarAccess(ScalarTypeDescription scalarTypeDescription, PrintWriter out) throws IOException
    {
        String scalarName =  scalarTypeDescription.getName();
        String returnType = scalarTypeDescription.isReference() ? "Option<OdmaObject>" : apiWriter.getScalarDataType(scalarTypeDescription,false,false);
        String constantScalarTypeName = scalarTypeDescription.getName().toUpperCase();
        out.println("");
        out.println("    /// Gets the "+scalarName+" value of this property if and only if");
        out.println("    /// the data type of this property is a single valued "+scalarName+".");
        out.println("    ///");
        out.println("    /// Returns OdmaInvalidDataTypeError if the data type of this property is not a single-valued "+scalarName+".");
        out.println("    fn get_"+scalarName.toLowerCase()+"(&self) -> Result<"+returnType+", Box<dyn Error>> {");
        out.println("        if !self.multi_value && self.data_type == OdmaType::"+constantScalarTypeName+" {");
        out.println("            return Ok(self.value.as_ref().and_then(|v| v.downcast_ref::<"+returnType+">().copied()));");
        out.println("        }");
        out.println("        Err(Box::new(OdmaInvalidDataTypeError))");
        out.println("    }");
    }

    protected void writeMultiValueScalarAccess(ScalarTypeDescription scalarTypeDescription, PrintWriter out) throws IOException
    {
        String scalarName =  scalarTypeDescription.getName();
        String returnType = scalarTypeDescription.isReference() ? "Iterator<OdmaObject>" : apiWriter.getScalarDataType(scalarTypeDescription,true,true);
        String constantScalarTypeName = scalarTypeDescription.getName().toUpperCase();
        out.println("");
        out.println("    /// Gets the "+scalarName+" value of this property if and only if");
        out.println("    /// the data type of this property is a multi valued "+scalarName+".");
        out.println("    ///");
        out.println("    /// Returns OdmaInvalidDataTypeError if the data type of this property is not a multi-valued "+scalarName+".");
        out.println("    fn get_"+scalarName.toLowerCase()+"_"+(scalarTypeDescription.isReference()?"iterator":"list")+"(&self) -> Result<"+returnType+", Box<dyn Error>> {");
        out.println("        if self.multi_value && self.data_type == OdmaType::"+constantScalarTypeName+" {");
        out.println("            return Ok(self.value.as_ref().and_then(|v| v.downcast_ref::<Vec<"+returnType+">>().cloned()).unwrap_or_else(Vec::new));");
        out.println("        }");
        out.println("        Err(Box::new(OdmaInvalidDataTypeError))");
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
