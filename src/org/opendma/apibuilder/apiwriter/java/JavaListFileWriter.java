package org.opendma.apibuilder.apiwriter.java;

import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;

import org.opendma.apibuilder.OdmaApiWriter;
import org.opendma.apibuilder.apiwriter.AbstractListFileWriter;
import org.opendma.apibuilder.apiwriter.ImportsList;
import org.opendma.apibuilder.structure.ScalarTypeDescription;

public class JavaListFileWriter extends AbstractListFileWriter
{
    
    protected OdmaApiWriter apiWriter;
    
    public JavaListFileWriter(OdmaApiWriter writer)
    {
        apiWriter = writer;
    }

    protected void appendRequiredImportsGlobal(ScalarTypeDescription scalarTypeDescription, ImportsList requiredImports)
    {
        requiredImports.registerImport("java.util.List");
        requiredImports.registerImports(apiWriter.getScalarDataTypeImports(scalarTypeDescription,false));
    }

    protected void writeListFileHeader(ScalarTypeDescription scalarTypeDescription, List requiredImports, PrintWriter out)
    {
        out.println("package org.opendma.api.collections;");
        out.println("");
        Iterator itRequiredImports = requiredImports.iterator();
        while(itRequiredImports.hasNext())
        {
            String importDeclaration = (String)itRequiredImports.next();
            if(JavaApiWriter.needToImportPackage(importDeclaration,"org.opendma.api.collections"))
            {
                out.println("import "+importDeclaration+";");
            }
        }
        out.println("");
        out.println("/**");
        out.println(" * Type safe version of the <code>List</code> interface for the <i>"+scalarTypeDescription.getName()+"</i>");
        out.println(" * data type.");
        out.println(" * ");
        out.println(" * @author Stefan Kopf, xaldon Technologies GmbH, the OpenDMA architecture board");
        out.println(" */");
        out.println("public interface "+apiWriter.getScalarDataType(scalarTypeDescription,true)+" extends List");
        out.println("{");
    }

    protected void writeListFileMethods(ScalarTypeDescription scalarTypeDescription, PrintWriter out)
    {
        String singleValueDataType = apiWriter.getScalarDataType(scalarTypeDescription,false);
        out.println("");
        out.println("    /**");
        out.println("     * Returns the <code>"+singleValueDataType+"</code> element at the specified position in");
        out.println("     * this list.");
        out.println("     * ");
        out.println("     * @param index");
        out.println("     *            position of the element to return");
        out.println("     * ");
        out.println("     * @return the <code>"+singleValueDataType+"</code> element at the specified position in");
        out.println("     *         this list.");
        out.println("     * ");
        out.println("     * @throws IndexOutOfBoundsException");
        out.println("     *             if the index is out of range (index < 0 || index >= size()).");
        out.println("     */");
        out.println("    public "+singleValueDataType+" get"+scalarTypeDescription.getName()+"(int index);");
    }

    protected void writeListFileFooter(ScalarTypeDescription scalarTypeDescription, PrintWriter out)
    {
        out.println("");
        out.println("}");
    }

}
