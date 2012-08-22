package org.opendma.apibuilder.apiwriter.java5;

import java.io.PrintWriter;
import java.util.Iterator;

import org.opendma.apibuilder.apiwriter.AbstractEnumerationFileWriter;
import org.opendma.apibuilder.apiwriter.ImportsList;
import org.opendma.apibuilder.structure.ClassDescription;

public class Java5EnumerationFileWriter extends AbstractEnumerationFileWriter
{

    protected void appendRequiredImportsGlobal(ClassDescription classDescription, ImportsList requiredImports)
    {
        requiredImports.registerImport("org.opendma.api."+classDescription.getApiName());
    }

    protected void writeEnumerationFileHeader(ClassDescription classDescription, ImportsList requiredImports, PrintWriter out)
    {
        out.println("package org.opendma.api.collections;");
        out.println("");
        Iterator itRequiredImports = requiredImports.iterator();
        while(itRequiredImports.hasNext())
        {
            String importPackage = (String)itRequiredImports.next();
            out.println("import "+importPackage+";");
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
        out.println("public interface "+classDescription.getApiName()+"Enumeration extends OdmaCollection<"+classDescription.getApiName()+">");
        out.println("{");
    }

    protected void writeEnumerationFileMethods(ClassDescription classDescription, PrintWriter out)
    {
    }

    protected void writeEnumerationFileFooter(ClassDescription classDescription, PrintWriter out)
    {
        out.println("");
        out.println("}");
    }

}
