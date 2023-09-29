package org.opendma.apibuilder.apiwriter.php;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.List;

import org.opendma.apibuilder.OdmaApiWriter;
import org.opendma.apibuilder.apiwriter.AbstractObjectsInterfaceFileWriter;
import org.opendma.apibuilder.apiwriter.ApiHelperWriter;
import org.opendma.apibuilder.apiwriter.ImportsList;
import org.opendma.apibuilder.structure.ApiHelperDescription;
import org.opendma.apibuilder.structure.ClassDescription;
import org.opendma.apibuilder.structure.PropertyDescription;
import org.opendma.apibuilder.structure.ScalarTypeDescription;

public class PhpObjectsInterfaceFileWriter extends AbstractObjectsInterfaceFileWriter
{
    
    protected OdmaApiWriter apiWriter;
    
    public PhpObjectsInterfaceFileWriter(OdmaApiWriter writer)
    {
        apiWriter = writer;
        this.apiHelperWriters.put("getQName", new ApiHelperWriter(){
            public void writeApiHelper(ClassDescription classDescription, ApiHelperDescription apiHelper, PrintWriter out)
            {
                // getter
                out.println("");
                out.println("    /**");
                out.println("     * Returns "+apiHelper.getAbstract()+".");
                out.println("     * "+apiHelper.getDescription());
                out.println("     * @return OdmaQName "+apiHelper.getAbstract());
                out.println("     */");
                out.println("    public function getQName(): OdmaQName;");
            }
            public void appendRequiredImportsGlobal(ClassDescription classDescription, ApiHelperDescription apiHelper, List<String> requiredImports)
            {
                if(!requiredImports.contains("OdmaQName.php"))
                {
                    requiredImports.add("OdmaQName.php");
                }
            }});
    }

    protected void writeClassFileHeader(ClassDescription classDescription, List<String> requiredImports, PrintWriter out)
    {
        String extendsApiName = classDescription.getExtendsApiName();
        out.println("<?php");
        out.println("");
        out.println("namespace OpenDMA\\Api;");
        /* we use class auto loading
        out.println("");
        Iterator<String> itRequiredImports = requiredImports.iterator();
        while(itRequiredImports.hasNext())
        {
            String importPackage = itRequiredImports.next();
            out.println("require_once '"+importPackage+"';");
        }
        */
        out.println("");
        out.println("/**");
        if(extendsApiName != null)
        {
            out.println(" * The "+classDescription.getOdmaName().getName()+" specific version of the "+extendsApiName+" interface");
            out.println(" * offering short-cuts to all defined OpenDMA properties.");
        }
        String classComment = classDescription.getDescription();
        if(classComment != null)
        {
            if(extendsApiName != null)
            {
                out.println(" * ");
            }
            out.println(" * "+classComment);
        }
        out.println(" */");
        if(extendsApiName != null)
        {
            out.println("interface "+classDescription.getApiName()+" extends "+extendsApiName);
        }
        else
        {
            if(classDescription.getAspect())
            {
                out.println("interface "+classDescription.getApiName()+" extends "+classDescription.getContainingApiDescription().getObjectClass().getApiName());
            }
            else
            {
                out.println("interface "+classDescription.getApiName());
            }
        }
        out.println("{");
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
        out.println("    // ----- Generic property access ---------------------------------------------------------------");
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
        requiredImports.registerImport("OdmaQName.php");
        requiredImports.registerImport("OdmaProperty.php");
    }

    protected void writeClassObjectSpecificPropertyAccessSectionHeader(ClassDescription classDescription, PrintWriter out)
    {
        out.println("");
        out.println("    // ----- Object specific property access -------------------------------------------------------");
    }

    protected String getReturnDataType(PropertyDescription property)
    {
        if(property.getDataType().isReference())
        {
            String result = property.getContainingClass().getContainingApiDescription().getDescribedClass(property.getReferenceClassName()).getApiName();
            if(property.getMultiValue())
            {
                result = "array<"+result+">";
            }
            else if(!property.getRequired())
            {
                result = "?" + result;
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
            return new String[] { property.getContainingClass().getContainingApiDescription().getDescribedClass(property.getReferenceClassName()).getApiName()+".php" };
        }
        else
        {
            return apiWriter.getScalarDataTypeImports(property.getDataType(),property.getMultiValue(),property.getRequired());
        }
    }

    protected void writeClassPropertyAccess(PropertyDescription property, PrintWriter out)
    {
        // generate names
        String phpDataType = getReturnDataType(property);
        ScalarTypeDescription scalarType = property.getDataType();
        String constantPropertyName = "PROPERTY_" + property.getOdmaName().getName().toUpperCase();
        // getter
        out.println("");
        out.println("    /**");
        out.println("     * Returns "+property.getAbstract()+".<br/>");
        String standardGetterName = "get" + ((!scalarType.isReference()) ? scalarType.getName() : (property.getMultiValue() ? "ReferenceArray" : "Reference"));
        out.println("     * Shortcut for <code>getProperty(OdmaTypes."+constantPropertyName+")."+standardGetterName+"()</code>.");
        out.println("     * ");
        for(String s : getPropertyDetails(property))
        {
            out.println("     * "+s);
        }
        out.println("     * ");
        out.println("     * @return "+phpDataType+" "+property.getAbstract());
        out.println("     */");
        String returnDeclaration = property.getMultiValue() ? "array" : phpDataType;
        out.println("    public function "+(phpDataType.equalsIgnoreCase("bool")?"is":"get")+property.getApiName()+"(): "+returnDeclaration+";");
        // setter
        if( (!property.isReadOnly()) && (!property.getMultiValue()) )
        {
            out.println("");
            out.println("    /**");
            out.println("     * Sets "+property.getAbstract()+".<br>");
            String standardSetterName = "setValue";
            out.println("     * Shortcut for <code>getProperty(OdmaTypes."+constantPropertyName+")."+standardSetterName+"(value)</code>.");
            out.println("     * ");
            for(String s : getPropertyDetails(property))
            {
                out.println("     * "+s);
            }
            out.println("     * ");
            out.println("     * @param "+phpDataType+" $newValue The new value for "+property.getAbstract());
            out.println("     * @throws OdmaAccessDeniedException If this OdmaProperty is read-only or cannot be set by the current user");
            out.println("     */");
            out.println("    public function void set"+property.getApiName()+"($newValue);");
        }
    }

    protected void appendRequiredImportsClassPropertyAccess(ImportsList requiredImports, PropertyDescription property)
    {
        requiredImports.registerImports(getRequiredImports(property));
    }

}
