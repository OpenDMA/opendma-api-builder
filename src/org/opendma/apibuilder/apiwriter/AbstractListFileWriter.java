package org.opendma.apibuilder.apiwriter;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

import org.opendma.apibuilder.structure.ScalarTypeDescription;

public abstract class AbstractListFileWriter
{

    protected abstract void writeListFileHeader(ScalarTypeDescription scalarTypeDescription, PrintWriter out);

    protected abstract void writeListFileMethods(ScalarTypeDescription scalarTypeDescription, PrintWriter out);

    protected abstract void writeListFileFooter(ScalarTypeDescription scalarTypeDescription, PrintWriter out);

    public void createListFile(ScalarTypeDescription scalarTypeDescription, OutputStream classOutputStream) throws IOException
    {
        // create output Writer
        PrintWriter out = new PrintWriter(classOutputStream);
        // write Header
        writeListFileHeader(scalarTypeDescription,out);
        // write the methods
        writeListFileMethods(scalarTypeDescription,out);
        // write Footer
        writeListFileFooter(scalarTypeDescription,out);
        // close writer and streams
        out.close();
        classOutputStream.close();
    }

}
