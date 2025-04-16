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
        Iterator<String> itRequiredImports = requiredImports.iterator();
        while(itRequiredImports.hasNext())
        {
            String importDeclaration = (String)itRequiredImports.next();
            out.println("use "+importDeclaration+";");
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
        out.println("    /// Returns OdmaError::InvalidDataType if the type of the newValue does not match the data type of this OdmaProperty.");
        out.println("    /// Returns OdmaError::AccessDenied if this OdmaProperty is read-only or cannot be set by the current user.");
        out.println("    fn set_value(&mut self, new_value: Option<Box<dyn std::any::Any>>) -> Result<(), OdmaError> {");
        out.println("        if self.read_only {");
        out.println("            return Err(OdmaError::AccessDenied)");
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
                out.println("                OdmaType::"+constantScalarTypeName+" => (!self.multi_value && val.is::<Box<dyn OdmaObject>>()) || (self.multi_value && val.is::<Box<dyn Iterator<Item = Box<dyn OdmaObject>>>>()),");
            }
            else
            {
                String rustType = apiWriter.getScalarDataType(scalarTypeDescription,false,true);
                //out.println("                OdmaType::"+constantScalarTypeName+" => (!self.multi_value && val.is::<"+rustType+">()) || (self.multi_value && self.check_list_and_values::<"+rustType+">(val)),");
                out.println("                OdmaType::"+constantScalarTypeName+" => (!self.multi_value && val.is::<"+rustType+">()) || (self.multi_value && val.is::<Vec<"+rustType+">>()),");
            }
        }
        out.println("            };");
        out.println("            if !valid {");
        out.println("                return Err(OdmaError::IllegalArgument(\"Invalid type of value.\".into()))");
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
        String returnType = scalarTypeDescription.isReference() ? "Option<&dyn OdmaObject>" : apiWriter.getScalarDataType(scalarTypeDescription,false,false);
        String constantScalarTypeName = scalarTypeDescription.getName().toUpperCase();
        out.println("");
        out.println("    /// Gets the "+scalarName+" value of this property if and only if");
        out.println("    /// the data type of this property is a single valued "+scalarName+".");
        out.println("    ///");
        out.println("    /// Returns OdmaError::InvalidDataType if the data type of this property is not a single-valued "+scalarName+".");
        out.println("    fn get_"+scalarName.toLowerCase()+"(&self) -> Result<"+returnType+", OdmaError> {");
        out.println("        if !self.multi_value && self.data_type == OdmaType::"+constantScalarTypeName+" {");
        if(scalarTypeDescription.isReference())
        {
            out.println("            if let Some(inner) = self.value.as_ref() {");
            out.println("                if let Some(obj) = inner.downcast_ref::<Box<dyn OdmaObject>>() {");
            out.println("                    return Ok(Some(obj.as_ref()));");
            out.println("                }");
            out.println("            }");
            out.println("            return Ok(None);");
        }
        else if(scalarName.equalsIgnoreCase("content"))
        {
            out.println("            if let Some(inner) = self.value.as_ref() {");
            out.println("                if let Some(content) = inner.downcast_ref::<Box<dyn OdmaContent>>() {");
            out.println("                    return Ok(Some(content.as_ref()));");
            out.println("                }");
            out.println("            }");
            out.println("            return Ok(None);");
        }
        else
        {
            String downcastType = apiWriter.getScalarDataType(scalarTypeDescription,false,true);
            out.println("            return Ok(self.value.as_ref().and_then(|v| v.downcast_ref::<"+downcastType+">().cloned()));");
        }
        out.println("        }");
        out.println("        Err(OdmaError::InvalidDataType(\"invalid type or cardinality for get_"+scalarName.toLowerCase()+"()\".into()))");
        out.println("    }");
    }

    protected void writeMultiValueScalarAccess(ScalarTypeDescription scalarTypeDescription, PrintWriter out) throws IOException
    {
        String scalarName =  scalarTypeDescription.getName();
        String returnType = scalarTypeDescription.isReference() ? "Box<dyn Iterator<Item = &dyn OdmaObject> + '_>" : apiWriter.getScalarDataType(scalarTypeDescription,true,true);
        String constantScalarTypeName = scalarTypeDescription.getName().toUpperCase();
        out.println("");
        out.println("    /// Gets the "+scalarName+" value of this property if and only if");
        out.println("    /// the data type of this property is a multi valued "+scalarName+".");
        out.println("    ///");
        out.println("    /// Returns OdmaError::InvalidDataType if the data type of this property is not a multi-valued "+scalarName+".");
        out.println("    fn get_"+scalarName.toLowerCase()+"_"+(scalarTypeDescription.isReference()?"iter":"vec")+"(&self) -> Result<"+returnType+", OdmaError> {");
        out.println("        if self.multi_value && self.data_type == OdmaType::"+constantScalarTypeName+" {");
        if(scalarTypeDescription.isReference())
        {
            out.println("            if let Some(inner) = self.value.as_ref() {");
            out.println("                if let Some(vec) = inner.downcast_ref::<Vec<Box<dyn OdmaObject>>>() {");
            out.println("                    return Ok(Box::new(vec.iter().map(|b| b.as_ref())));");
            out.println("                }");
            out.println("            }");
            out.println("            return Ok(Box::new(std::iter::empty()));");
        }
        else if(scalarName.equalsIgnoreCase("content"))
        {
            out.println("            if let Some(vec) = self.value.as_ref().and_then(|any| any.downcast_ref::<Vec<Box<dyn OdmaContent>>>()) {");
            out.println("                return Ok(vec.iter().map(|b| b.as_ref()).collect());");
            out.println("            }");
            out.println("            return Ok(Vec::new());");
        }
        else
        {
            out.println("            return Ok(self.value.as_ref().and_then(|v| v.downcast_ref::<"+returnType+">().cloned()).unwrap_or_default());");
        }
        out.println("        }");
        out.println("        Err(OdmaError::InvalidDataType(\"invalid type or cardinality for get_"+scalarName.toLowerCase()+"_"+(scalarTypeDescription.isReference()?"iter":"vec")+"()\".into()))");
        out.println("    }");
    }

    protected void appendRequiredImportsGlobal(ImportsList requiredImports)
    {
        requiredImports.registerImport("crate::OdmaQName");
        requiredImports.registerImport("crate::OdmaType");
        requiredImports.registerImport("crate::OdmaError");
        requiredImports.registerImport("crate::OdmaProperty");
        requiredImports.registerImport("crate::OdmaObject");
        requiredImports.registerImport("std::any::Any");
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
