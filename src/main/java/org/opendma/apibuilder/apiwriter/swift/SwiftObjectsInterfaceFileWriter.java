package org.opendma.apibuilder.apiwriter.swift;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Iterator;
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

public class SwiftObjectsInterfaceFileWriter extends AbstractObjectsInterfaceFileWriter
{
    
    protected OdmaApiWriter apiWriter;
    
    public SwiftObjectsInterfaceFileWriter(OdmaApiWriter writer)
    {
        apiWriter = writer;
        this.apiHelperWriters.put("getQName", new ApiHelperWriter(){
            public void writeApiHelper(ClassDescription classDescription, ApiHelperDescription apiHelper, PrintWriter out)
            {
                // getter
                out.println("");
                out.println("    /// Returns "+apiHelper.getAbstract()+".");
                out.println("    /// "+apiHelper.getDescription());
                out.println("    /// -Returns: "+apiHelper.getAbstract());
                out.println("    func qname() -> OdmaQName;");
            }
            public void appendRequiredImportsGlobal(ClassDescription classDescription, ApiHelperDescription apiHelper, ImportsList requiredImports)
            {
            }});
    }

    protected void writeClassFileHeader(ClassDescription classDescription, List<String> requiredImports, PrintWriter out)
    {
        Iterator<String> itRequiredImports = requiredImports.iterator();
        while(itRequiredImports.hasNext())
        {
            String importDeclaration = (String)itRequiredImports.next();
            out.println("import "+importDeclaration+";");
        }
        out.println("");
        String extendsApiName = classDescription.getExtendsApiName();
        if(extendsApiName != null)
        {
            out.println("/// The "+classDescription.getOdmaName().getName()+" specific version of the "+extendsApiName+" interface");
            out.println("/// offering short-cuts to all defined OpenDMA properties.");
        }
        String classComment = classDescription.getDescription();
        if(classComment != null)
        {
            if(extendsApiName != null)
            {
                out.println("///");
            }
            out.println("/// "+classComment);
        }
        if(extendsApiName != null)
        {
            out.println("public protocol "+classDescription.getApiName()+": "+extendsApiName+" {");
        }
        else
        {
            if(classDescription.getAspect())
            {
                out.println("public protocol "+classDescription.getApiName()+": "+classDescription.getContainingApiDescription().getObjectClass().getApiName()+" {");
            }
            else
            {
                out.println("public protocol "+classDescription.getApiName()+" {");
            }
        }
    }

    protected void writeClassFileFooter(ClassDescription classDescription, PrintWriter out)
    {
        out.println("");
        out.println("}");
    }

    protected void appendRequiredImportsGlobal(ClassDescription classDescription, ImportsList requiredImports)
    {
        // we do not have any globally required imports
    }

    protected void writeClassGenericPropertyAccess(ClassDescription classDescription, PrintWriter out) throws IOException
    {
        out.println("");
        out.println("    // MARK: - Generic property access");
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
        out.println("    // MARK: - Object specific property access");
        /*
        Alternative concept:
        extension OdmaObject {
            var id: OdmaId { try! property(for: OdmaCommonNames.PROPERTY_ID).idValue()! }
            var guid: OdmaGuid { try! property(for: OdmaCommonNames.PROPERTY_GUID).guidValue()! }
            var odmaClass: OdmaClass { try! property(for: OdmaCommonNames.PROPERTY_CLASS).referenceValue() as! OdmaClass }
            var repository: OdmaRepository { try! property(for: OdmaCommonNames.PROPERTY_REPOSITORY).referenceValue() as! OdmaRepository }
        }
        */
    }

    protected String getReturnDataType(PropertyDescription property)
    {
        if(property.getDataType().isReference())
        {
            String result = property.getContainingClass().getContainingApiDescription().getDescribedClass(property.getReferenceClassName()).getApiName();
            if(property.getMultiValue())
            {
                result = "any Sequence<any "+result+">";
            }
            else if(!property.getRequired())
            {
                result = result + "?";
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
        if(property.getDataType().isReference())
        {
            return null;
        }
        else
        {
            return apiWriter.getScalarDataTypeImports(property.getDataType(),property.getMultiValue(),property.getRequired());
        }
    }

    protected void writeClassPropertyAccess(PropertyDescription property, PrintWriter out)
    {
        // generate names
        String swiftDataType = getReturnDataType(property);
        ScalarTypeDescription scalarType = property.getDataType();
        String constantPropertyName = "PROPERTY_" + property.getOdmaName().getName().toUpperCase();
        // getter
        out.println("");
        out.println("    /// Returns "+property.getAbstract()+".<br/>");
        String standardGetterName = Tools.lowerCaseFirstChar(scalarType.getName()) + (scalarType.isReference() ? (property.getMultiValue() ? "Sequence" : "Value") : (property.getMultiValue() ? "Velues" : "Value"));
        out.println("    /// Shortcut for `getProperty(OdmaTypes."+constantPropertyName+")."+standardGetterName+"()`.");
        out.println("    ///");
        for(String s : getPropertyDetails(property))
        {
            out.println("    /// "+s);
        }
        out.println("    ///");
        out.println("    /// -Returns: "+property.getAbstract());
        //out.println("    "+(swiftDataType.equalsIgnoreCase("Bool")?"is":"get")+property.getApiName()+"() -> "+swiftDataType);
        out.println("    var "+Tools.lowerCaseFirstChar(property.getApiName())+": "+swiftDataType+" { get }");
        // setter
        if( (!property.isReadOnly()) && (!property.getMultiValue()) )
        {
            out.println("");
            out.println("    /// Sets "+property.getAbstract()+".<br>");
            String standardSetterName = "setValue";
            out.println("    /// Shortcut for `getProperty(OdmaTypes."+constantPropertyName+")."+standardSetterName+"(newValue)`.");
            out.println("    ///");
            for(String s : getPropertyDetails(property))
            {
                out.println("    /// "+s);
            }
            out.println("    ///");
            out.println("    /// - Parameter: newValue: The new value for "+property.getAbstract());
            out.println("    /// - Throws: An error of type `OdmaError` if this OdmaProperty is read-only or cannot be set by the current user");
            out.println("    func set"+property.getApiName()+"(to newValue: "+swiftDataType+") throws");
        }
    }

    protected void appendRequiredImportsClassPropertyAccess(ImportsList requiredImports, PropertyDescription property)
    {
        requiredImports.registerImports(getRequiredImports(property));
    }

}
