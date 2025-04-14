package org.opendma.apibuilder.apiwriter.py;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.List;

import org.opendma.apibuilder.OdmaApiWriter;
import org.opendma.apibuilder.Tools;
import org.opendma.apibuilder.apiwriter.AbstractObjectsInterfaceFileWriter;
import org.opendma.apibuilder.apiwriter.ApiHelperWriter;
import org.opendma.apibuilder.apiwriter.ImportsList;
import org.opendma.apibuilder.structure.ApiHelperDescription;
import org.opendma.apibuilder.structure.ClassDescription;
import org.opendma.apibuilder.structure.PropertyDescription;
import org.opendma.apibuilder.structure.ScalarTypeDescription;

public class PythonObjectsInterfaceFileWriter extends AbstractObjectsInterfaceFileWriter
{
    
    protected OdmaApiWriter apiWriter;
    
    public PythonObjectsInterfaceFileWriter(OdmaApiWriter writer)
    {
        apiWriter = writer;
        this.apiHelperWriters.put("getQName", new ApiHelperWriter(){
            public void writeApiHelper(ClassDescription classDescription, ApiHelperDescription apiHelper, PrintWriter out)
            {
                // getter
                out.println("");
                out.println("    @abstractmethod");
                out.println("    def get_qname(self) -> OdmaQName:");
                out.println("        \"\"\"");
                out.println("        Returns "+apiHelper.getAbstract()+".");
                out.println("        "+apiHelper.getDescription());
                out.println("        ");
                out.println("        :return: "+Tools.upperCaseFirstChar(apiHelper.getAbstract()));
                out.println("        \"\"\"");
                out.println("        pass");
            }
            public void appendRequiredImportsGlobal(ClassDescription classDescription, ApiHelperDescription apiHelper, ImportsList requiredImports)
            {
            }});
    }

    protected void writeClassFileHeader(ClassDescription classDescription, List<String> requiredImports, PrintWriter out)
    {
        //out.println("");
        //out.println("T"+classDescription.getApiName()+" = TypeVar(\"T"+classDescription.getApiName()+"\", bound=\""+classDescription.getApiName()+"\")");
        out.println("");
        String extendsApiName = classDescription.getExtendsApiName();
        if(extendsApiName != null)
        {
            out.println("class "+classDescription.getApiName()+"("+extendsApiName+"):");
        }
        else
        {
            if(classDescription.getAspect())
            {
                out.println("class "+classDescription.getApiName()+"("+classDescription.getContainingApiDescription().getObjectClass().getApiName()+"):");
            }
            else
            {
                out.println("class "+classDescription.getApiName()+"(ABC):");
            }
        }
        out.println("    \"\"\"");
        if(extendsApiName != null)
        {
            out.println("    The "+classDescription.getOdmaName().getName()+" specific version of the "+extendsApiName+" interface");
            out.println("    offering short-cuts to all defined OpenDMA properties.");
        }
        String classComment = classDescription.getDescription();
        if(classComment != null)
        {
            if(extendsApiName != null)
            {
                out.println("");
            }
            out.println("    "+classComment);
        }
        out.println("    \"\"\"");
    }

    protected void writeClassFileFooter(ClassDescription classDescription, PrintWriter out)
    {
    }

    protected void appendRequiredImportsGlobal(ClassDescription classDescription, ImportsList requiredImports)
    {
        // we do not have any globally required imports
    }

    protected void writeClassGenericPropertyAccess(ClassDescription classDescription, PrintWriter out) throws IOException
    {
        out.println("");
        out.println("    # Generic property access");
        InputStream templateIn = apiWriter.getTemplateAsStream("OdmaObject.GenericPropertyAccess");
        BufferedReader templareReader = new BufferedReader(new InputStreamReader(templateIn));
        String templateLine = null;
        while( (templateLine = templareReader.readLine()) != null)
        {
            out.println(templateLine);
        }
    }

    protected void appendRequiredImportsGenericPropertyAccess(ImportsList requiredImports)
    {
    }

    protected void writeClassObjectSpecificPropertyAccessSectionHeader(ClassDescription classDescription, PrintWriter out)
    {
        out.println("");
        out.println("    # Object specific property access");
    }

    protected String getReturnDataType(PropertyDescription property)
    {
        if(property.getDataType().isReference())
        {
            String result = property.getContainingClass().getContainingApiDescription().getDescribedClass(property.getReferenceClassName()).getApiName();
            if(property.getMultiValue())
            {
                result = "Iterable[T"+result+"]";
            }
            else if(!property.getRequired())
            {
                result = "Optional[\""+result+"\"]";
            }
            else
            {
                result = "\""+result+"\"";
            }
            return result;
        }
        else
        {
            return apiWriter.getScalarDataType(property.getDataType(),property.getMultiValue(),property.getRequired());
        }
    }
    
    protected String[] getRequiredImports(PropertyDescription property)
    {
        return null;
    }

    protected void writeClassPropertyAccess(PropertyDescription property, PrintWriter out)
    {
        // generate names
        String pythonDataType = getReturnDataType(property);
        ScalarTypeDescription scalarType = property.getDataType();
        String constantPropertyName = "PROPERTY_" + property.getOdmaName().getName().toUpperCase();
        // getter
        out.println("");
        out.println("    @abstractmethod");
        out.println("    def get_"+Tools.toSnakeCase(property.getApiName())+"(self) -> "+pythonDataType+":");
        out.println("        \"\"\"");
        out.println("        Returns "+property.getAbstract()+".<br>");
        String standardGetterName = "get_" + scalarType.getName().toLowerCase() + (property.getMultiValue() ? "_list" : "");
        out.println("        Shortcut for <code>get_property(OdmaTypes."+constantPropertyName+")."+standardGetterName+"()</code>.");
        out.println("        ");
        for(String s : getPropertyDetails(property))
        {
            out.println("        "+s);
        }
        out.println("        ");
        out.println("        :return: "+property.getAbstract());
        out.println("        \"\"\"");
        out.println("        pass");
        // setter
        if( (!property.isReadOnly()) && (!property.getMultiValue()) )
        {
            out.println("");
            out.println("    @abstractmethod");
            out.println("    def set_"+Tools.toSnakeCase(property.getApiName())+"(self, new_value: "+pythonDataType+") -> None:");
            out.println("        \"\"\"");
            out.println("        Sets "+property.getAbstract()+".<br>");
            String standardSetterName = "set_value";
            out.println("        Shortcut for <code>get_property(OdmaTypes."+constantPropertyName+")."+standardSetterName+"()</code>.");
            out.println("        ");
            for(String s : getPropertyDetails(property))
            {
                out.println("        "+s);
            }
            out.println("        ");
            out.println("        :param new_value: the new value for "+property.getAbstract());
            out.println("        :raises OdmaAccessDeniedException: Raised if this OdmaProperty is read-only or cannot be set by the current user.");
            out.println("        \"\"\"");
            out.println("        pass");
        }
    }

    protected void appendRequiredImportsClassPropertyAccess(ImportsList requiredImports, PropertyDescription property)
    {
        requiredImports.registerImports(getRequiredImports(property));
    }

}
