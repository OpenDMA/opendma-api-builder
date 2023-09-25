package org.opendma.apibuilder.apiwriter.php;

import java.io.PrintWriter;
import java.util.LinkedList;

import org.opendma.apibuilder.apiwriter.AbstractConstantsFileWriter;
import org.opendma.apibuilder.structure.ApiDescription;
import org.opendma.apibuilder.structure.ClassDescription;
import org.opendma.apibuilder.structure.PropertyDescription;
import org.opendma.apibuilder.structure.ScalarTypeDescription;

public class PhpCommonNamesFileWriter extends AbstractConstantsFileWriter
{
    protected void writeConstantsFileHeader(ApiDescription apiDescription, PrintWriter out)
    {
        out.println("<?php");
        out.println("");
        out.println("namespace OpenDMA\\Api;");
        out.println("");
        out.println("require_once 'OdmaQName.php';");
        out.println("");
        out.println("/**");
        out.println(" * Static declaration of all common names used in the OpenDMA specification.");
        out.println(" */");
        out.println("class OdmaCommonNames");
        out.println("{");
    }

    protected void writeConstantsClassesSectionHeader(ApiDescription apiDescription, PrintWriter out)
    {
    }

    protected void writeConstantsScalarTypeConstant(ScalarTypeDescription scalarTypeDescription, String constantName, PrintWriter out)
    {
    }

    protected void writeConstantsTypesSectionHeader(ApiDescription apiDescription, PrintWriter out)
    {
    }

    protected void writeConstantsClassSeperator(ClassDescription classDescription, PrintWriter out)
    {
        StringBuffer sb = new StringBuffer("    // -----< class ");
        sb.append(classDescription.getOdmaName().getName());
        sb.append(" >");
        while(sb.length() < 100)
        {
            sb.append('-');
        }
        out.println("");
        out.println(sb.toString());
    }
    
    private LinkedList<String> initialiserCode = new LinkedList<String>();

    protected void writeConstantsClassnameConstant(ClassDescription classDescription, String constantName, PrintWriter out)
    {
        out.println("");
        out.println("    /** qualified name of the OpenDMA system class <code>"+classDescription.getOdmaName().getName()+"</code> */");
        out.println("    public static OdmaQName $"+constantName+";");
        initialiserCode.add("self::$"+constantName+" = new OdmaQName('"+classDescription.getOdmaName().getQualifier()+"', '"+classDescription.getOdmaName().getName()+"');");
    }

    protected void writeConstantsPropertynameConstant(PropertyDescription propertyDescription, String constantName, PrintWriter out)
    {
        out.println("");
        out.println("    /** qualified name of the OpenDMA system property <code>"+propertyDescription.getOdmaName().getName()+"</code> */");
        out.println("    public static OdmaQName $"+constantName+";");
        initialiserCode.add("self::$"+constantName+" = new OdmaQName('"+propertyDescription.getOdmaName().getQualifier()+"', '"+propertyDescription.getOdmaName().getName()+"');");
    }

    protected void writeConstantsBackrefPropertynameHint(PropertyDescription propertyDescription, String propertyName, PrintWriter out)
    {
        out.println("");
        out.println("    // Property "+propertyDescription.getOdmaName().getName()+" already defined previously");
    }

    protected void writeConstantsFileFooter(ApiDescription apiDescription, PrintWriter out)
    {
        out.println("");
        out.println("    /**");
        out.println("     * Initialise static constants");
        out.println("     */");
        out.println("    public static function initialize(): void");
        out.println("    {");
        for(String initCodeLine : initialiserCode)
        {
            out.println("        "+initCodeLine);
        }
        out.println("    }");
        out.println("");
        out.println("}");
        out.println("");
        out.println("OdmaCommonNames::initialize();");
        out.println("");
    }

}