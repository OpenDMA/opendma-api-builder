package org.opendma.apibuilder.apiwriter;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import org.opendma.apibuilder.structure.ClassDescription;

public abstract class AbstractEnumerationFileWriter
{

    protected abstract void writeEnumerationFileHeader(ClassDescription classDescription, List requiredImports, PrintWriter out);

    protected abstract void writeEnumerationFileMethods(ClassDescription classDescription, PrintWriter out);

    protected abstract void writeEnumerationFileFooter(ClassDescription classDescription, PrintWriter out);
    
    protected abstract void appendRequiredImportsGlobal(ClassDescription classDescription, List requiredImports);

    public void createEnumerationFile(ClassDescription classDescription, OutputStream classOutputStream) throws IOException
    {
        // create output Writer
        PrintWriter out = new PrintWriter(classOutputStream);
        // collect required imports
        ArrayList requiredImports = new ArrayList();
        appendRequiredImportsGlobal(classDescription,requiredImports);
        // write Header
        writeEnumerationFileHeader(classDescription,requiredImports,out);
        // write the methods
        writeEnumerationFileMethods(classDescription,out);
        // write Footer
        writeEnumerationFileFooter(classDescription,out);
        // close writer and streams
        out.close();
        classOutputStream.close();
    }

}
