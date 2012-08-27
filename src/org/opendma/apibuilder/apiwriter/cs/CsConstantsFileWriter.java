package org.opendma.apibuilder.apiwriter.cs;

import java.io.PrintWriter;

import org.opendma.apibuilder.apiwriter.AbstractConstantsFileWriter;
import org.opendma.apibuilder.structure.ApiDescription;
import org.opendma.apibuilder.structure.ClassDescription;
import org.opendma.apibuilder.structure.PropertyDescription;
import org.opendma.apibuilder.structure.ScalarTypeDescription;

public class CsConstantsFileWriter extends AbstractConstantsFileWriter
{
    protected void writeConstantsFileHeader(ApiDescription apiDescription, PrintWriter out)
    {
        out.println("using OpenDMA.Api;");
        out.println("");
        out.println("namespace OpenDMA.Api");
        out.println("{");
        out.println("");
        out.println("    /// <summary>Static declaration of all type constants used in the OpenDMA spcification.</summary>");
        out.println("    public class OdmaCommonNames");
        out.println("    {");
    }

    protected void writeConstantsClassesSectionHeader(ApiDescription apiDescription, PrintWriter out)
    {
        out.println("");
        out.println("        // =============================================================================================");
        out.println("        // Declaration of pre-defined class names and property names");
        out.println("        // =============================================================================================");
    }

    protected void writeConstantsScalarTypeConstant(ScalarTypeDescription scalarTypeDescription, String constantName, PrintWriter out)
    {
    }

    protected void writeConstantsTypesSectionHeader(ApiDescription apiDescription, PrintWriter out)
    {
    }

    protected void writeConstantsClassSeperator(ClassDescription classDescription, PrintWriter out)
    {
        StringBuffer sb = new StringBuffer("        // -----< class ");
        sb.append(classDescription.getOdmaName().getName());
        sb.append(" >");
        while(sb.length() < 100)
        {
            sb.append('-');
        }
        out.println("");
        out.println(sb.toString());
    }

    protected void writeConstantsClassnameConstant(ClassDescription classDescription, String constantName, PrintWriter out)
    {
        out.println("");
        out.println("        /// <summary>qualified name of the OpenDMA system class <c>"+classDescription.getOdmaName().getName()+"</c></summary>");
        out.println("        public static OdmaQName "+constantName+" = new OdmaQName(\""+classDescription.getOdmaName().getQualifier()+"\", \""+classDescription.getOdmaName().getName()+"\");");
    }

    protected void writeConstantsPropertynameConstant(PropertyDescription propertyDescription, String constantName, PrintWriter out)
    {
        out.println("");
        out.println("        /// <summary>qualified name of the OpenDMA system property <c>"+propertyDescription.getOdmaName().getName()+"</c><summary>");
        out.println("        public static OdmaQName "+constantName+" = new OdmaQName(\""+propertyDescription.getOdmaName().getQualifier()+"\", \""+propertyDescription.getOdmaName().getName()+"\");");
    }

    protected void writeConstantsBackrefPropertynameHint(PropertyDescription propertyDescription, String propertyName, PrintWriter out)
    {
        out.println("");
        out.println("        // Property "+propertyDescription.getOdmaName().getName()+" already defined previously");
    }

    protected void writeConstantsFileFooter(ApiDescription apiDescription, PrintWriter out)
    {
        out.println("");
        out.println("        //");
        out.println("        // Empty private constructor to prevent instantiation of this class.");
        out.println("        //");
        out.println("        private OdmaTypes()");
        out.println("        {");
        out.println("            // empty constructor");
        out.println("        }");
        out.println("");
        out.println("    }");
        out.println("");
        out.println("}");
    }

}