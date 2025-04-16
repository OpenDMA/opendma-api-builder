package org.opendma.apibuilder.apiwriter.rust;

import java.io.PrintWriter;

import org.opendma.apibuilder.apiwriter.AbstractConstantsFileWriter;
import org.opendma.apibuilder.structure.ApiDescription;
import org.opendma.apibuilder.structure.ClassDescription;
import org.opendma.apibuilder.structure.PropertyDescription;
import org.opendma.apibuilder.structure.ScalarTypeDescription;

public class RustCommonNamesFileWriter extends AbstractConstantsFileWriter
{
    protected void writeConstantsFileHeader(ApiDescription apiDescription, PrintWriter out)
    {
        out.println("use crate::odma_qname::OdmaQName;");
        out.println("use once_cell::sync::Lazy;");
        out.println("");
        out.println("/// static declaration of all common names used in the OpenDMA specification.");
        out.println("");
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

    protected void writeConstantsClassnameConstant(ClassDescription classDescription, String constantName, PrintWriter out)
    {
        out.println("");
        out.println("/// qualified name of the OpenDMA system class "+classDescription.getOdmaName().getName());
        out.println("pub static "+constantName+": Lazy<OdmaQName> = Lazy::new(|| { OdmaQName::try_new(\""+classDescription.getOdmaName().getNamespace()+"\", \""+classDescription.getOdmaName().getName()+"\").expect(\"valid static name\") } );");
    }

    protected void writeConstantsPropertynameConstant(PropertyDescription propertyDescription, String constantName, PrintWriter out)
    {
        out.println("");
        out.println("/// qualified name of the OpenDMA system property "+propertyDescription.getOdmaName().getName());
        out.println("pub static "+constantName+": Lazy<OdmaQName> = Lazy::new(|| { OdmaQName::try_new(\""+propertyDescription.getOdmaName().getNamespace()+"\", \""+propertyDescription.getOdmaName().getName()+"\").expect(\"valid static name\") } );");
    }

    protected void writeConstantsBackrefPropertynameHint(PropertyDescription propertyDescription, String propertyName, PrintWriter out)
    {
        out.println("");
        out.println("    // Property "+propertyDescription.getOdmaName().getName()+" already defined previously");
    }

    protected void writeConstantsFileFooter(ApiDescription apiDescription, PrintWriter out)
    {
    }

}