package org.opendma.apibuilder.apiwriter.cs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;

import org.opendma.apibuilder.OdmaApiWriter;
import org.opendma.apibuilder.apiwriter.AbstractApiWriter;
import org.opendma.apibuilder.apiwriter.AbstractPropertyFileWriter;
import org.opendma.apibuilder.structure.ApiDescription;
import org.opendma.apibuilder.structure.ScalarTypeDescription;

public class CsPropertyImplementationFileWriter extends AbstractPropertyFileWriter
{
    
    protected OdmaApiWriter apiWriter;
    
    public CsPropertyImplementationFileWriter(OdmaApiWriter writer)
    {
        apiWriter = writer;
    }

    protected void writePropertyFileHeader(ApiDescription apiDescription, List requiredImports, PrintWriter out) throws IOException
    {
        Iterator itRequiredImports = requiredImports.iterator();
        while(itRequiredImports.hasNext())
        {
            String importPackage = (String)itRequiredImports.next();
            out.println("using "+importPackage+";");
        }
        out.println("");
        out.println("namespace OpenDMA.Api");
        out.println("{");
        out.println("");
        InputStream templateIn = AbstractApiWriter.getResourceAsStream("/templates/cs/OdmaProperty.Header.template");
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
        InputStream templateIn = AbstractApiWriter.getResourceAsStream("/templates/cs/OdmaProperty.Generic.template");
        BufferedReader templareReader = new BufferedReader(new InputStreamReader(templateIn));
        String templateLine = null;
        while( (templateLine = templareReader.readLine()) != null)
        {
            out.println(templateLine);
        }
        out.println("");
        out.println("        /// <summary>");
        out.println("        /// Set the value of this property to the given new value. The");
        out.println("        /// <c>Class</c> of the given <c>object</c> has to match the");
        out.println("        /// data type of this property.");
        out.println("        /// </summary>");
        out.println("        public void setValue(object newValue)");
        out.println("        {");
        out.println("            if(readOnly)");
        out.println("            {");
        out.println("                throw new OdmaAccessDeniedException();");
        out.println("            }");
        out.println("            if(multivalue)");
        out.println("            {");
        writeGenericSectionSwitch(apiDescription,out,true);
        out.println("            }");
        out.println("            else");
        out.println("            {");
        writeGenericSectionSwitch(apiDescription,out,false);
        out.println("            }");
        out.println("            dirty = true;");
        out.println("        }");
    }

    protected void writeGenericSectionSwitch(ApiDescription apiDescription, PrintWriter out, boolean multivalue) throws IOException
    {
        List scalarTypes = apiDescription.getScalarTypes();
        out.println("                switch(dataType)");
        out.println("                {");
        Iterator itScalarTypes = scalarTypes.iterator();
        while(itScalarTypes.hasNext())
        {
            ScalarTypeDescription scalarTypeDescription = (ScalarTypeDescription)itScalarTypes.next();
            //if(!scalarTypeDescription.isInternal())
            //{
                String constantScalarTypeName = "TYPE_" + scalarTypeDescription.getName().toUpperCase();
                String csReturnType;
                if(multivalue)
                {
                    csReturnType = scalarTypeDescription.isReference() ? "IOdmaObjectEnumeration" : apiWriter.getProgrammingLanguageSpecificScalarDataType(true,scalarTypeDescription.getNumericID());
                }
                else
                {
                    csReturnType = scalarTypeDescription.isReference() ? "IOdmaObject" : apiWriter.getProgrammingLanguageSpecificScalarDataType(false,scalarTypeDescription.getNumericID());
                }
                out.println("                case OdmaTypes."+constantScalarTypeName+":");
                out.println("                    if(newValue is "+csReturnType+")");
                out.println("                    {");
                out.println("                        value = newValue;");
                out.println("                    }");
                out.println("                    else");
                out.println("                    {");
                out.println("                        throw new OdmaInvalidDataTypeException(dataType,multivalue);");
                out.println("                    }");
                out.println("                    break;");
            //}
        }
        out.println("                default:");
        out.println("                    throw new OdmaRuntimeException(\"OdmaProperty initialized with unknown data type \"+dataType);");
        out.println("                }");
    }

    protected void writeSingleValueScalarAccess(ScalarTypeDescription scalarTypeDescription, PrintWriter out) throws IOException
    {
        String scalarName =  scalarTypeDescription.getName();
        String csReturnType = scalarTypeDescription.isReference() ? "IOdmaObject" : apiWriter.getProgrammingLanguageSpecificScalarDataType(false,scalarTypeDescription.getNumericID());
        out.println("");
        out.println("        /// <summary>");
        out.println("        /// Returns the <c>"+csReturnType+"</c> value of this property if and only if");
        out.println("        /// the data type of this property is a single valued <i>"+scalarName+"</i>. Throws");
        out.println("        /// an <code>OdmaInvalidDataTypeException</code> otherwise.");
        out.println("        /// </summary>");
        out.println("        /// <returns>Returns the <c>"+csReturnType+"</c> value of this property</returns>");
        out.println("        public virtual "+csReturnType+" get"+scalarName+"()");
        out.println("        {");
        String constantScalarTypeName = "TYPE_" + scalarTypeDescription.getName().toUpperCase();
        out.println("            if( (multivalue == false) && (dataType == OdmaTypes."+constantScalarTypeName+") )");
        out.println("            {");
        out.println("                return ("+csReturnType+")value;");
        out.println("            }");
        out.println("            else");
        out.println("            {");
        out.println("                throw new OdmaInvalidDataTypeException(OdmaTypes."+constantScalarTypeName+",false,dataType,multivalue);");
        out.println("            }");
        out.println("        }");
    }

    protected void writeMultiValueScalarAccess(ScalarTypeDescription scalarTypeDescription, PrintWriter out) throws IOException
    {
        String scalarName =  scalarTypeDescription.getName();
        String csReturnType = scalarTypeDescription.isReference() ? "IOdmaObjectEnumeration" : apiWriter.getProgrammingLanguageSpecificScalarDataType(true,scalarTypeDescription.getNumericID());
        out.println("");
        out.println("        /// <summary>");
        out.println("        /// Returns the <c>"+csReturnType+"</c> value of this property if and only if");
        out.println("        /// the data type of this property is a multi valued <i>"+scalarName+"</i>. Throws");
        out.println("        /// an <code>OdmaInvalidDataTypeException</code> otherwise.");
        out.println("        /// </summary>");
        out.println("        /// <returns>Returns the <c>"+csReturnType+"</c> value of this property</returns>");
        out.println("        public virtual "+csReturnType+" get"+scalarName+(scalarTypeDescription.isReference()?"Enumeration":"List")+"()");
        out.println("        {");
        String constantScalarTypeName = "TYPE_" + scalarTypeDescription.getName().toUpperCase();
        out.println("            if( (multivalue == true) && (dataType == OdmaTypes."+constantScalarTypeName+") )");
        out.println("            {");
        out.println("                return ("+csReturnType+")value;");
        out.println("            }");
        out.println("            else");
        out.println("            {");
        out.println("                throw new OdmaInvalidDataTypeException(OdmaTypes."+constantScalarTypeName+",false,dataType,multivalue);");
        out.println("            }");
        out.println("        }");
    }

    protected void appendRequiredImportsGlobal(List requiredImports)
    {
        if(!requiredImports.contains("System"))
        {
            requiredImports.add("System");
        }
        if(!requiredImports.contains("System.Collections.Generic"))
        {
            requiredImports.add("System.Collections.Generic");
        }
        if(!requiredImports.contains("System.Linq"))
        {
            requiredImports.add("System.Linq");
        }
        if(!requiredImports.contains("System.Text"))
        {
            requiredImports.add("System.Text");
        }
        if(!requiredImports.contains("OpenDMA.Exceptions"))
        {
            requiredImports.add("OpenDMA.Exceptions");
        }
    }

    protected void appendRequiredImportsScalarAccess(List requiredImports, ScalarTypeDescription scalarTypeDescription)
    {
        if(scalarTypeDescription.isReference())
        {
            return;
        }
        String requiredImportSingleValue = apiWriter.getRequiredScalarDataTypeImport(false,scalarTypeDescription.getNumericID());
        String requiredImportMultiValue = apiWriter.getRequiredScalarDataTypeImport(true,scalarTypeDescription.getNumericID());
        if(requiredImportSingleValue != null)
        {
            if(!requiredImports.contains(requiredImportSingleValue))
            {
                requiredImports.add(requiredImportSingleValue);
            }
        }
        if(requiredImportMultiValue != null)
        {
            if(!requiredImports.contains(requiredImportMultiValue))
            {
                requiredImports.add(requiredImportMultiValue);
            }
        }
    }

}
