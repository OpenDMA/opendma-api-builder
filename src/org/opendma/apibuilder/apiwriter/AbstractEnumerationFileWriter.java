package org.opendma.apibuilder.apiwriter;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

import org.opendma.apibuilder.structure.ClassDescription;

public abstract class AbstractEnumerationFileWriter
{

    protected abstract void writeEnumerationFileHeader(ClassDescription classDescription, ImportsList requiredImports, PrintWriter out);

    protected abstract void writeEnumerationFileMethods(ClassDescription classDescription, PrintWriter out);

    protected abstract void writeEnumerationFileFooter(ClassDescription classDescription, PrintWriter out);
    
    protected abstract void appendRequiredImportsGlobal(ClassDescription classDescription, ImportsList requiredImports);

    public void createEnumerationFile(ClassDescription classDescription, OutputStream classOutputStream) throws IOException
    {
        // create output Writer
        PrintWriter out = new PrintWriter(classOutputStream);
        // collect required imports
        ImportsList requiredImports = new ImportsList();
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
