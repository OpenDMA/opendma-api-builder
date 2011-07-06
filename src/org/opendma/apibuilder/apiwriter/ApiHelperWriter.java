package org.opendma.apibuilder.apiwriter;

import java.io.PrintWriter;

import org.opendma.apibuilder.structure.ApiHelperDescription;
import org.opendma.apibuilder.structure.ClassDescription;

public interface ApiHelperWriter
{

    void writeApiHelper(ClassDescription classDescription, ApiHelperDescription apiHelper, PrintWriter out);

}
