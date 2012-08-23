package org.opendma.apibuilder.apiwriter.java5;

import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;

import org.opendma.apibuilder.OdmaApiWriter;
import org.opendma.apibuilder.apiwriter.AbstractListFileWriter;
import org.opendma.apibuilder.apiwriter.ImportsList;
import org.opendma.apibuilder.structure.ScalarTypeDescription;

public class Java5ListFileWriter extends AbstractListFileWriter
{
    
    protected OdmaApiWriter apiWriter;
    
    public Java5ListFileWriter(OdmaApiWriter writer)
    {
        apiWriter = writer;
    }

    protected void appendRequiredImportsGlobal(ScalarTypeDescription scalarTypeDescription, ImportsList requiredImports)
    {
        requiredImports.registerImport("java.util.List");
        requiredImports.registerImports(apiWriter.getRequiredScalarDataTypeImports(false,scalarTypeDescription.getNumericID()));
    }

    protected void writeListFileHeader(ScalarTypeDescription scalarTypeDescription, List requiredImports, PrintWriter out)
    {
        out.println("package org.opendma.api.collections;");
        out.println("");
        Iterator itRequiredImports = requiredImports.iterator();
        while(itRequiredImports.hasNext())
        {
            String importDeclaration = (String)itRequiredImports.next();
            if(Java5ApiWriter.needToImportPackage(importDeclaration,"org.opendma.api.collections"))
            {
                out.println("import "+importDeclaration+";");
            }
        }
        String singleValueDataType = apiWriter.getProgrammingLanguageSpecificScalarDataType(false,scalarTypeDescription.getNumericID());
        out.println("");
        out.println("/**");
        out.println(" * Typed version of the <code>List</code> interface for the <i>"+scalarTypeDescription.getName()+"</i>");
        out.println(" * data type.");
        out.println(" * ");
        out.println(" * @author Stefan Kopf, xaldon Technologies GmbH, the OpenDMA architecture board");
        out.println(" */");
        out.println("public interface "+apiWriter.getProgrammingLanguageSpecificScalarDataType(true,scalarTypeDescription.getNumericID())+" extends List<"+singleValueDataType+">");
        out.println("{");
    }

    protected void writeListFileMethods(ScalarTypeDescription scalarTypeDescription, PrintWriter out)
    {
    }

    protected void writeListFileFooter(ScalarTypeDescription scalarTypeDescription, PrintWriter out)
    {
        out.println("");
        out.println("}");
    }

}
