package org.opendma.apibuilder.apiwriter.py;

import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;

import org.opendma.apibuilder.apiwriter.AbstractConstantsFileWriter;
import org.opendma.apibuilder.structure.ApiDescription;
import org.opendma.apibuilder.structure.ClassDescription;
import org.opendma.apibuilder.structure.PropertyDescription;
import org.opendma.apibuilder.structure.ScalarTypeDescription;

public class PythonConstantsFileWriter extends AbstractConstantsFileWriter
{

    private LinkedList<String> definedConstants = new LinkedList<String>();
    
    public List<String> getDefinedConstants()
    {
        return definedConstants;
    }
    
    protected void writeConstantsFileHeader(ApiDescription apiDescription, PrintWriter out)
    {
        out.println("# Static declaration of all common names used in the OpenDMA specification.");
        out.println("# This file is auto-generated.");
        out.println();
        out.println("from .helpers import OdmaQName");
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
        StringBuffer sb = new StringBuffer("# ----------< class ");
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
        out.println("# qualified name of the OpenDMA system class "+classDescription.getOdmaName().getName());
        out.println(constantName+" = OdmaQName(\""+classDescription.getOdmaName().getNamespace()+"\", \""+classDescription.getOdmaName().getName()+"\")");
        definedConstants.add(constantName);
    }

    protected void writeConstantsPropertynameConstant(PropertyDescription propertyDescription, String constantName, PrintWriter out)
    {
        out.println("");
        out.println("# qualified name of the OpenDMA system property "+propertyDescription.getOdmaName().getName());
        out.println(constantName+" = OdmaQName(\""+propertyDescription.getOdmaName().getNamespace()+"\", \""+propertyDescription.getOdmaName().getName()+"\")");
        definedConstants.add(constantName);
    }

    protected void writeConstantsBackrefPropertynameHint(PropertyDescription propertyDescription, String propertyName, PrintWriter out)
    {
        out.println("");
        out.println("# Property "+propertyDescription.getOdmaName().getName()+" already defined previously");
    }

    protected void writeConstantsFileFooter(ApiDescription apiDescription, PrintWriter out)
    {
    }

}