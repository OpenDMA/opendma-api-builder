package org.opendma.apibuilder.apiwriter.cs;

import java.io.PrintWriter;

import org.opendma.apibuilder.apiwriter.AbstractEnumerationFileWriter;
import org.opendma.apibuilder.structure.ClassDescription;

public class CsEnumerationFileWriter extends AbstractEnumerationFileWriter
{

    protected void writeEnumerationFileHeader(ClassDescription classDescription, PrintWriter out)
    {
        out.println("unsing System.Collection.IEnumeration;");
        out.println("");
        out.println("namespace OpenDMA.Api.Collections;");
        out.println("{");
        out.println("");
        out.println("    // The content of a multi-valued <code>reference</code> property in OpenDMA.<br>");
        out.println("    // While scalar multi-value properties typically contain a limited number of");
        out.println("    // items, reference properties might contain very large numbers of items. So");
        out.println("    // they can only be accessed by an <code>Iterator</code> over all contained");
        out.println("    // objects.<br>");
        out.println("    // An implementation of OpenDMA should load the items on demand in pages from");
        out.println("    // the server instead of retrieving all ietms at once.");
        out.println("    // ");
        out.println("    // @author Stefan Kopf, xaldon Technologies GmbH, the OpenDMA architecture board");
        out.println("    public interface I"+classDescription.getApiName()+"Enumeration : IEnumeration");
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
