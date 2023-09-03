package org.opendma.apibuilder.apiwriter.java14;

import java.io.PrintWriter;
import java.util.Iterator;

import org.opendma.apibuilder.apiwriter.AbstractEnumerationFileWriter;
import org.opendma.apibuilder.apiwriter.ImportsList;
import org.opendma.apibuilder.structure.ClassDescription;

public class JavaEnumerationFileWriter extends AbstractEnumerationFileWriter
{

    protected void appendRequiredImportsGlobal(ClassDescription classDescription, ImportsList requiredImports)
    {
        if( (classDescription.getExtendsApiName() == null) && (!classDescription.getAspect()) )
        {
            requiredImports.registerImport("java.util.Iterator");
        }
    }

    protected void writeEnumerationFileHeader(ClassDescription classDescription, ImportsList requiredImports, PrintWriter out)
    {
        String extendsApiName = classDescription.getExtendsApiName();
        if(extendsApiName == null)
        {
            if(classDescription.getAspect())
            {
                extendsApiName = classDescription.getContainingApiDescription().getObjectClass().getApiName();
            }
        }
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
        out.println(" * The content of a multi-valued <code>reference</code> property in OpenDMA.<br>");
        out.println(" * While scalar multi-value properties typically contain a limited number of");
        out.println(" * items, reference properties might contain very large numbers of items. So");
        out.println(" * they can only be accessed by an <code>Iterator</code> over all contained");
        out.println(" * objects.<br>");
        out.println(" * An implementation of OpenDMA should load the items on demand in pages from");
        out.println(" * the server instead of retrieving all ietms at once.");
        out.println(" * ");
        out.println(" * @author Stefan Kopf, xaldon Technologies GmbH, the OpenDMA architecture board");
        out.println(" */");
        if(extendsApiName != null)
        {
            out.println("public interface "+classDescription.getApiName()+"Enumeration extends "+extendsApiName+"Enumeration");
        }
        else
        {
            out.println("public interface "+classDescription.getApiName()+"Enumeration");
        }
        out.println("{");
    }

    protected void writeEnumerationFileMethods(ClassDescription classDescription, PrintWriter out)
    {
        if( (classDescription.getExtendsApiName() != null) || classDescription.getAspect() )
        {
            // nothing to do here. Required methods are inherited from base class
            return;
        }
        out.println("");
        out.println("    /**");
        out.println("     * Returns an iterator over all <code>"+classDescription.getApiName()+"</code> elements.");
        out.println("     * ");
        out.println("     * @return an iterator over all <code>"+classDescription.getApiName()+"</code> elements.");
        out.println("     */");
        out.println("    public Iterator iterator();");
//        out.println("");
//        out.println("    /**");
//        out.println("     * Returns <code>true</code> if and only if the collection is empty, i.e.");
//        out.println("     * <code>iterator().hasNext()</code> returns <code>false</code>.");
//        out.println("     * ");
//        out.println("     * @return <code>true</code> if and only if the collection is empty.");
//        out.println("     */");
//        out.println("    public boolean isEmpty();");
    }

    protected void writeEnumerationFileFooter(ClassDescription classDescription, PrintWriter out)
    {
        out.println("");
        out.println("}");
    }

}
