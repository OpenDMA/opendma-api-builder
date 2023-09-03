package org.opendma.apibuilder.apiwriter.java14;

import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;

import org.opendma.apibuilder.OdmaApiWriter;
import org.opendma.apibuilder.apiwriter.AbstractListFileWriter;
import org.opendma.apibuilder.apiwriter.ImportsList;
import org.opendma.apibuilder.structure.ScalarTypeDescription;

public class JavaListImplementationFileWriter extends AbstractListFileWriter
{
    
    protected OdmaApiWriter apiWriter;
    
    public JavaListImplementationFileWriter(OdmaApiWriter writer)
    {
        apiWriter = writer;
    }

    protected void appendRequiredImportsGlobal(ScalarTypeDescription scalarTypeDescription, ImportsList requiredImports)
    {
        requiredImports.registerImport("java.util.ArrayList");
        requiredImports.registerImports(apiWriter.getScalarDataTypeImports(scalarTypeDescription,false));
        requiredImports.registerImports(apiWriter.getScalarDataTypeImports(scalarTypeDescription,true));
    }

    protected void writeListFileHeader(ScalarTypeDescription scalarTypeDescription, List requiredImports, PrintWriter out)
    {
        out.println("package org.opendma.impl.collections;");
        out.println("");
        Iterator itRequiredImports = requiredImports.iterator();
        while(itRequiredImports.hasNext())
        {
            String importDeclaration = (String)itRequiredImports.next();
            if(JavaApiWriter.needToImportPackage(importDeclaration,"org.opendma.impl.collections"))
            {
                out.println("import "+importDeclaration+";");
            }
        }
        String interfaceName = apiWriter.getScalarDataType(scalarTypeDescription,true);
        out.println("");
        out.println("/**");
        out.println(" * Implementation of the <code>{@link x}</code> interface based on an <code>ArrayList</code>.");
        out.println(" * ");
        out.println(" * @author Stefan Kopf, xaldon Technologies GmbH, the OpenDMA architecture board");
        out.println(" */");
        out.println("public class Array"+interfaceName+" extends ArrayList implements "+interfaceName);
        out.println("{");
        out.println("");
        out.println("    /** serial version ID */");
        out.println("    private static final long serialVersionUID = -3190299231469094574L;");
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
        out.println("    public "+singleValueDataType+" get"+scalarTypeDescription.getName()+"(int index)");
        out.println("    {");
        out.println("        return ("+singleValueDataType+")this.get(index);");
        out.println("    }");
    }

    protected void writeListFileFooter(ScalarTypeDescription scalarTypeDescription, PrintWriter out)
    {
        out.println("");
        out.println("}");
    }

}
