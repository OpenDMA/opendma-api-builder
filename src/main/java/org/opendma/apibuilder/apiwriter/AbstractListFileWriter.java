package org.opendma.apibuilder.apiwriter;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.List;

import org.opendma.apibuilder.structure.ScalarTypeDescription;

public abstract class AbstractListFileWriter
{

    protected abstract void writeListFileHeader(ScalarTypeDescription scalarTypeDescription, List<String> requiredImports, PrintWriter out);

    protected abstract void writeListFileMethods(ScalarTypeDescription scalarTypeDescription, PrintWriter out);

    protected abstract void writeListFileFooter(ScalarTypeDescription scalarTypeDescription, PrintWriter out);
    
    protected abstract void appendRequiredImportsGlobal(ScalarTypeDescription scalarTypeDescription, ImportsList requiredImports);

    public void createListFile(ScalarTypeDescription scalarTypeDescription, OutputStream classOutputStream) throws IOException
    {
        // create output Writer
        PrintWriter out = new PrintWriter(classOutputStream);
        // collect required imports
        ImportsList requiredImports = new ImportsList();
        appendRequiredImportsGlobal(scalarTypeDescription,requiredImports);
        // write Header
        writeListFileHeader(scalarTypeDescription,requiredImports,out);
        // write the methods
        writeListFileMethods(scalarTypeDescription,out);
        // write Footer
        writeListFileFooter(scalarTypeDescription,out);
        // close writer and streams
        out.close();
        classOutputStream.close();
    }

}
