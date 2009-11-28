package org.opendma.apibuilder.apiwriter.cs;

import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;

import org.opendma.apibuilder.OdmaApiWriter;
import org.opendma.apibuilder.apiwriter.AbstractListFileWriter;
import org.opendma.apibuilder.structure.ScalarTypeDescription;

public class CsListFileWriter extends AbstractListFileWriter
{
    
    protected OdmaApiWriter apiWriter;
    
    public CsListFileWriter(OdmaApiWriter writer)
    {
        apiWriter = writer;
    }

    protected void appendRequiredImportsGlobal(ScalarTypeDescription scalarTypeDescription, List requiredImports)
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
        if(!requiredImports.contains("System.Collections"))
        {
            requiredImports.add("System.Collections");
        }
        String singleValueDataTypeImport = apiWriter.getRequiredScalarDataTypeImport(false,scalarTypeDescription.getNumericID());
        if(singleValueDataTypeImport != null)
        {
            if(!requiredImports.contains(singleValueDataTypeImport))
            {
                requiredImports.add(singleValueDataTypeImport);
            }
        }
    }

    protected void writeListFileHeader(ScalarTypeDescription scalarTypeDescription, List requiredImports, PrintWriter out)
    {
        Iterator itRequiredImports = requiredImports.iterator();
        while(itRequiredImports.hasNext())
        {
            String importPackage = (String)itRequiredImports.next();
            out.println("using "+importPackage+";");
        }
        out.println("");
        out.println("namespace OpenDMA.Api.Collections");
        out.println("{");
        out.println("");
        out.println("    /// <summary>");
        out.println("    /// Type safe version of the <code>List</code> interface for the <i>"+scalarTypeDescription.getName()+"</i>");
        out.println("    /// data type.");
        out.println("    /// </summary>");
        out.println("    public interface "+apiWriter.getProgrammingLanguageSpecificScalarDataType(true,scalarTypeDescription.getNumericID())+" : IList");
        out.println("    {");
    }

    protected void writeListFileMethods(ScalarTypeDescription scalarTypeDescription, PrintWriter out)
    {
        /*
        String singleValueDataType = apiWriter.getProgrammingLanguageSpecificScalarDataType(false,scalarTypeDescription.getNumericID());
        out.println("");
        out.println("        // Returns the <code>"+singleValueDataType+"</code> element at the specified position in");
        out.println("        // this list.");
        out.println("        // ");
        out.println("        // @param index");
        out.println("        //            position of the element to return");
        out.println("        // ");
        out.println("        // @return the <code>"+singleValueDataType+"</code> element at the specified position in");
        out.println("        //         this list.");
        out.println("        // ");
        out.println("        // @throws IndexOutOfBoundsException");
        out.println("        //             if the index is out of range (index < 0 || index >= size()).");
        out.println("        public "+singleValueDataType+" get"+scalarTypeDescription.getName()+"(int index);");
        */
    }

    protected void writeListFileFooter(ScalarTypeDescription scalarTypeDescription, PrintWriter out)
    {
        out.println("");
        out.println("    }");
        out.println("");
        out.println("}");
    }

}
