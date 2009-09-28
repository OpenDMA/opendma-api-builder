package org.opendma.apibuilder.apiwriter.cs;

import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;

import org.opendma.apibuilder.apiwriter.AbstractEnumerationFileWriter;
import org.opendma.apibuilder.structure.ClassDescription;

public class CsEnumerationFileWriter extends AbstractEnumerationFileWriter
{

    protected void appendRequiredImportsGlobal(ClassDescription classDescription, List requiredImports)
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
    }

    protected void writeEnumerationFileHeader(ClassDescription classDescription, List requiredImports, PrintWriter out)
    {
        Iterator itRequiredImports = requiredImports.iterator();
        while(itRequiredImports.hasNext())
        {
            String importPackage = (String)itRequiredImports.next();
            out.println("using "+importPackage+";");
        }
        out.println("");
        out.println("namespace OpenDMA.Api.Collections");
        out.println("");
        out.println("    /// <summary>");
        out.println("    /// The content of a multi-valued <c>reference</c> property in OpenDMA.<br>");
        out.println("    /// While scalar multi-value properties typically contain a limited number of");
        out.println("    /// items, reference properties might contain very large numbers of items. So");
        out.println("    /// they can only be accessed by an <code>Iterator</code> over all contained");
        out.println("    /// objects.<br>");
        out.println("    /// An implementation of OpenDMA should load the items on demand in pages from");
        out.println("    /// the server instead of retrieving all ietms at once.");
        out.println("    /// </summary>");
        out.println("    public interface I"+classDescription.getApiName()+"Enumeration : IEnumerable");
        out.println("    {");
    }

    protected void writeEnumerationFileMethods(ClassDescription classDescription, PrintWriter out)
    {
    }

    protected void writeEnumerationFileFooter(ClassDescription classDescription, PrintWriter out)
    {
        out.println("");
        out.println("    }");
        out.println("");
        out.println("}");
    }

}
