package org.opendma.apibuilder.apiwriter;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

import org.opendma.apibuilder.structure.ClassDescription;

public abstract class AbstractEnumerationFileWriter
{

    protected abstract void writeEnumerationFileHeader(ClassDescription classDescription, PrintWriter out);

    protected abstract void writeEnumerationFileMethods(ClassDescription classDescription, PrintWriter out);

    protected abstract void writeEnumerationFileFooter(ClassDescription classDescription, PrintWriter out);

    public void createEnumerationFile(ClassDescription classDescription, OutputStream classOutputStream) throws IOException
    {
        // create output Writer
        PrintWriter out = new PrintWriter(classOutputStream);
        // write Header
        writeEnumerationFileHeader(classDescription,out);
        // write the methods
        writeEnumerationFileMethods(classDescription,out);
        // write Footer
        writeEnumerationFileFooter(classDescription,out);
        // close writer and streams
        out.close();
        classOutputStream.close();
    }

}
