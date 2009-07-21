package org.opendma.apibuilder.structure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.opendma.apibuilder.DescriptionFileSemanticException;
import org.opendma.apibuilder.DescriptionFileSyntaxException;
import org.opendma.apibuilder.DescriptionFileTypes;
import org.opendma.apibuilder.OdmaApiBuilderQName;
import org.opendma.apibuilder.OdmaBasicTypes;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ApiDescription implements DescriptionFileTypes, OdmaBasicTypes
{
    
    /** the List of all described classes */
    protected List DescribedClasses = new ArrayList();
    
    /** the Map between the qualified class name and a ClassDescription */
    protected Map ClassNameMap = new HashMap();
    
    /** the List of all defined scalar types */
    protected List ScalarTypes = new ArrayList();
    
    /** the Map between the scalar type names and the scalar type descriptions */
    protected Map ScalarTypesNameToDescriptionMap = new HashMap();
    
    /** the Map between the scalar type IDs and the scalar type descriptions */
    protected Map ScalarTypesIdToDescriptionMap = new HashMap();

    /**
     * Create a new ApiDescription by reading the definition from a W3C DOM tree.
     * This constructor only performs some basic syntax checks.
     * 
     * @param apiDescriptionElement the W3C DOM element to read the class definitions of this API from
     * 
     * @throws DescriptionFileSyntaxException if there are syntactical errors in the XML file
     * @throws DescriptionFileSemanticException if there are semantic errors
     */
    public ApiDescription(Element apiDescriptionElement) throws DescriptionFileSyntaxException, DescriptionFileSemanticException
    {
        parse(apiDescriptionElement);
    }
    
    /**
     * Returns the List of all described classes.
     * 
     * @return the List of all described classes
     */
    public List getDescribedClasses()
    {
        return DescribedClasses;
    }
    
    /**
     * Returns the <code>{@link ClassDescription}</code> for the given class name or null if no such class exists.
     *
     * @param name the <code>{@link OdmaApiBuilderQName}</code> of the class to return the <code>{@link ClassDescription}</code> for
     *
     * @return the <code>{@link ClassDescription}</code> for the given class name or null if no such class exists
     * 
     * @throws NullPointerException if parameter <code>name</code> is <code>null</code>
     */
    public ClassDescription getDescribedClass(OdmaApiBuilderQName name)
    {
        return (ClassDescription)ClassNameMap.get(name);
    }
    
    /**
     * Returns the List of all defined scalar types.
     * 
     * @return the List of all defined scalar types
     */
    public List getScalarTypes()
    {
        return ScalarTypes;
    }
    
    /**
     * Returns the <code>ScalarTypeDescription</code> for the given scalar type name or null, if no such scalar type name is defined.
     * 
     * @param scalarTypeName the scalar type name to return the <code>ScalarTypeDescription</code> for
     * 
     * @return the <code>ScalarTypeDescription</code> for the given scalar type name or null, if no such scalar type name is defined
     */
    public ScalarTypeDescription getScalarTypeDescription(String scalarTypeName)
    {
        return (ScalarTypeDescription)ScalarTypesNameToDescriptionMap.get(scalarTypeName.toLowerCase());
    }
    
    /**
     * Returns the <code>ScalarTypeDescription</code> for the given numeric scalar type ID or null, if no such scalar type ID is defined.
     * 
     * @param scalarTypeId the numeric scalar type ID to return the <code>ScalarTypeDescription</code> for
     * 
     * @return the <code>ScalarTypeDescription</code> for the given numeric scalar type ID or null, if no such scalar type ID is defined
     */
    public ScalarTypeDescription getScalarTypeDescription(int scalarTypeId)
    {
        return (ScalarTypeDescription)ScalarTypesNameToDescriptionMap.get(new Integer(scalarTypeId));
    }

    /**
     * Read the definition of this API from a W3C DOM tree and perform some
     * basic syntax checks (e.g. missing elements, ...).
     * 
     * @param apiDescriptionElement the W3C DOM element to read the class definitions of this class hierarchy from
     * 
     * @throws DescriptionFileSyntaxException if there are syntactical errors in the XML file
     * @throws DescriptionFileSemanticException if there are semantic errors
     */
    protected void parse(Element apiDescriptionElement) throws DescriptionFileSyntaxException, DescriptionFileSemanticException
    {
        // check element name
        if (!apiDescriptionElement.getNodeName().equals(DESCRIPTION_ELEMENT_ROOT))
        {
            throw new DescriptionFileSyntaxException("The name of the root element must be "+DESCRIPTION_ELEMENT_ROOT);
        }
        // get list of all child elements
        NodeList childElementsList = apiDescriptionElement.getChildNodes();
        // first of all, we need to find the <ScalarTypes> element.
        Element scalarTypesElement = null;
        for (int i = 0; i < childElementsList.getLength(); i++)
        {
            Node testchild = childElementsList.item(i);
            if ((testchild.getNodeType() == Node.ELEMENT_NODE) && ((Element) testchild).getTagName().equals(DESCRIPTION_ELEMENT_SCALARTYPES))
            {
                if(scalarTypesElement != null)
                {
                    throw new DescriptionFileSyntaxException("Found multiple <"+DESCRIPTION_ELEMENT_SCALARTYPES+"> elements");
                }
                scalarTypesElement = (Element)testchild;
            }
        }
        if(scalarTypesElement == null)
        {
            throw new DescriptionFileSyntaxException("Can not find required <"+DESCRIPTION_ELEMENT_SCALARTYPES+"> element in api description.");
        }
        parseScalarTypes(scalarTypesElement);
        // iterate through all elements below this element and read in all class descriptions
        for (int i = 0; i < childElementsList.getLength(); i++)
        {
            Node testchild = childElementsList.item(i);
            if ((testchild.getNodeType() == Node.ELEMENT_NODE) && ((Element) testchild).getTagName().equals(DESCRIPTION_ELEMENT_CLASS))
            {
                ClassDescription classDescription = new ClassDescription((Element) testchild, this);
                DescribedClasses.add(classDescription);
                ClassNameMap.put(classDescription.getOdmaName(),classDescription);
            }
        }
    }
    
    /**
     * Read the definition of all scalar types from a W3C DOM tree and perform some
     * basic syntax checks (e.g. missing elements, ...).
     * 
     * @param scalarTypesElement the W3C DOM element to read the scalar type definitions from
     * 
     * @throws DescriptionFileSyntaxException if there are syntactical errors in the XML file
     * @throws DescriptionFileSemanticException if there are semantic errors
     */
    protected void parseScalarTypes(Element scalarTypesElement) throws DescriptionFileSyntaxException, DescriptionFileSemanticException
    {
        // get list of all child elements
        NodeList childElementsList = scalarTypesElement.getChildNodes();
        // iterate through all elements below this element and read in all scalar types
        for (int i = 0; i < childElementsList.getLength(); i++)
        {
            Node testchild = childElementsList.item(i);
            if ((testchild.getNodeType() == Node.ELEMENT_NODE) && ( ((Element) testchild).getTagName().equals(DESCRIPTION_ELEMENT_INTERNALSCALARTYPE) || ((Element) testchild).getTagName().equals(DESCRIPTION_ELEMENT_SCALARTYPE) ) )
            {
                ScalarTypeDescription scalarTypeDescription = new ScalarTypeDescription((Element) testchild,((Element) testchild).getTagName().equals(DESCRIPTION_ELEMENT_INTERNALSCALARTYPE));
                if(ScalarTypesNameToDescriptionMap.containsKey(scalarTypeDescription.getName().toLowerCase()))
                {
                    throw new DescriptionFileSemanticException("Multiple definitions of scalar type name "+scalarTypeDescription.getName());
                }
                if(ScalarTypesIdToDescriptionMap.containsKey(new Integer(scalarTypeDescription.getNumericID())))
                {
                    throw new DescriptionFileSemanticException("Multiple definitions of scalar type ID "+scalarTypeDescription.getNumericID());
                }
                ScalarTypes.add(scalarTypeDescription);
                ScalarTypesNameToDescriptionMap.put(scalarTypeDescription.getName().toLowerCase(),scalarTypeDescription);
                ScalarTypesIdToDescriptionMap.put(new Integer(scalarTypeDescription.getNumericID()),scalarTypeDescription);
            }
        }
    }
    
    /**
     * Validates the uniqueness of (a) all qualified class names in this hierarchy
     * and (b) all declared properties within each class for all declared classes.<br>
     * DOES NOT yet validate the uniqueness between declared and inherited propertoes!
     * 
     * @throws DescriptionFileSemanticException if a uniqueness constraint is violated
     */
    public void checkUniqueness() throws DescriptionFileSemanticException
    {
        // map for the uniqueness test of the class names
        Map uniqueClassnameTestMap = new HashMap();
        // iterate through all described classes
        Iterator itAllClasses = DescribedClasses.iterator();
        while(itAllClasses.hasNext())
        {
            ClassDescription classDescription = (ClassDescription)itAllClasses.next();
            // test is qualified class name is unique
            if(uniqueClassnameTestMap.containsKey(classDescription.getOdmaName()))
            {
                throw new DescriptionFileSemanticException("The qualified class name "+classDescription.getOdmaName()+" is not unique.");
            }
            uniqueClassnameTestMap.put(classDescription.getOdmaName(),classDescription);
            // test uniqueness of all qualified property names within this class
            Map uniquePropertynameTestMap = new HashMap();
            List declaredProperties = classDescription.getPropertyDescriptions();
            Iterator itDeclaredProperties = declaredProperties.iterator();
            while(itDeclaredProperties.hasNext())
            {
                PropertyDescription propertyDescription = (PropertyDescription)itDeclaredProperties.next();
                if(uniquePropertynameTestMap.containsKey(propertyDescription.getOdmaName()))
                {
                    throw new DescriptionFileSemanticException("The qualified property name "+propertyDescription.getOdmaName()+" in the class "+classDescription.getOdmaName()+" is not unique.");
                }
                uniquePropertynameTestMap.put(propertyDescription.getOdmaName(),propertyDescription);
            }
            
        }
    }

    /**
     * Validates the existance of the target class of all reference properties in all
     * declared classes.
     * 
     * @throws DescriptionFileSemanticException if a target class of a reference property does not exist
     */
    public void checkReferences() throws DescriptionFileSemanticException
    {
        //-----< check all extends relations >---------------------------------
        Iterator itAllClasses = DescribedClasses.iterator();
        while(itAllClasses.hasNext())
        {
            ClassDescription classDescription = (ClassDescription)itAllClasses.next();
            OdmaApiBuilderQName extendsName = classDescription.getExtendsName();
            if(extendsName != null)
            {
                if(!ClassNameMap.containsKey(extendsName))
                {
                    throw new DescriptionFileSemanticException("The class "+classDescription.getOdmaName()+" extends the class "+extendsName+" that does not exist in the description file");
                }
            }
        }
        //-----< check all reference properties >------------------------------
        itAllClasses = DescribedClasses.iterator();
        while(itAllClasses.hasNext())
        {
            ClassDescription classDescription = (ClassDescription)itAllClasses.next();
            List propertyDescriptions = classDescription.getPropertyDescriptions();
            Iterator itPropertyDescriptions = propertyDescriptions.iterator();
            while(itPropertyDescriptions.hasNext())
            {
                PropertyDescription propertyDescription = (PropertyDescription)itPropertyDescriptions.next();
                if(propertyDescription.getDataType() == TYPE_REFERENCE)
                {
                    if(!ClassNameMap.containsKey(propertyDescription.getReferenceClassName()))
                    {
                        throw new DescriptionFileSemanticException("The property "+propertyDescription.getOdmaName()+" in the class "+classDescription.getOdmaName()+" references the class "+propertyDescription.getReferenceClassName()+" that does not exist in the description file");
                    }
                }
            }
        }
    }

}