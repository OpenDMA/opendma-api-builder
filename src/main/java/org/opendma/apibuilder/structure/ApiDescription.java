package org.opendma.apibuilder.structure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
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
    
    /** the version of this OpenDMA API */
    protected String version = null;
    
    /** the List of all described classes */
    protected List<ClassDescription> describedClasses = new ArrayList<ClassDescription>();
    
    /** the Map between the qualified class name and a ClassDescription */
    protected Map<OdmaApiBuilderQName,ClassDescription> classNameMap = new HashMap<OdmaApiBuilderQName,ClassDescription>();
    
    /** the List of all defined scalar types */
    protected List<ScalarTypeDescription> scalarTypes = new ArrayList<ScalarTypeDescription>();
    
    /** the Map between the scalar type names and the scalar type descriptions */
    protected Map<String,ScalarTypeDescription> scalarTypesNameToDescriptionMap = new HashMap<String,ScalarTypeDescription>();
    
    /** the Map between the scalar type IDs and the scalar type descriptions */
    protected Map<Integer,ScalarTypeDescription> scalarTypesIdToDescriptionMap = new HashMap<Integer,ScalarTypeDescription>();
    
    /** the qualified name of the class hierarchy root element */
    protected OdmaApiBuilderQName objectClassQName = new OdmaApiBuilderQName("opendma","Object");
    
    /** the qualified name of the class that describes classes */
    protected OdmaApiBuilderQName classClassQName = new OdmaApiBuilderQName("opendma","Class");
    
    /** the qualified name of the class that describes properties */
    protected OdmaApiBuilderQName propertyInfoClassQName = new OdmaApiBuilderQName("opendma","PropertyInfo");
    
    /** the ClassDescription of the class hierarchy root */
    protected ClassDescription objectClass = null;
    
    /** the ClassDescription of the class that describes classes */
    protected ClassDescription classClass = null;
    
    /** the ClassDescription of the class that describes properties */
    protected ClassDescription propertyInfoClass = null;

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
     * Returns the version of this OpenDMA API.
     * 
     * @return the version of this OpenDMA API
     */
    public String getVersion()
    {
        return version;
    }
    
    /**
     * Returns the List of all described classes.
     * 
     * @return the List of all described classes
     */
    public List<ClassDescription> getDescribedClasses()
    {
        return describedClasses;
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
        return classNameMap.get(name);
    }
    
    /**
     * Returns the List of all defined scalar types.
     * 
     * @return the List of all defined scalar types
     */
    public List<ScalarTypeDescription> getScalarTypes()
    {
        return scalarTypes;
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
        return scalarTypesNameToDescriptionMap.get(scalarTypeName.toLowerCase());
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
        return scalarTypesIdToDescriptionMap.get(new Integer(scalarTypeId));
    }
    
    /**
     * Returns the ClassDescription of the class hierarchy root.
     * 
     * @return the ClassDescription of the class hierarchy root.
     */
    public ClassDescription getObjectClass()
    {
        return objectClass;
    }
    
    /**
     * Returns the ClassDescription of the class defined in the description file that defines the layout of classes.
     * 
     * @return the ClassDescription of the class defined in the description file that defines the layout of classes.
     */
    public ClassDescription getClassClass()
    {
        return classClass;
    }
    
    /**
     * Returns the ClassDescription of the class defined in the description file that defines the layout of properties.
     * 
     * @return the ClassDescription of the class defined in the description file that defines the layout of properties.
     */
    public ClassDescription getPropertyInfoClass()
    {
        return propertyInfoClass;
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
        // find version number
        for (int i = 0; i < childElementsList.getLength(); i++)
        {
            Node testchild = childElementsList.item(i);
            if ((testchild.getNodeType() == Node.ELEMENT_NODE) && ((Element) testchild).getTagName().equals(DESCRIPTION_ELEMENT_VERSION))
            {
                if(version != null)
                {
                    throw new DescriptionFileSyntaxException("Found multiple <"+DESCRIPTION_ELEMENT_VERSION+"> elements");
                }
                version = ((Element)testchild).getTextContent();
            }
        }
        if(version == null)
        {
            throw new DescriptionFileSyntaxException("Can not find required <"+DESCRIPTION_ELEMENT_VERSION+"> element in api description.");
        }
        // iterate through all elements below this element and read in all class descriptions
        for (int i = 0; i < childElementsList.getLength(); i++)
        {
            Node testchild = childElementsList.item(i);
            if ((testchild.getNodeType() == Node.ELEMENT_NODE) && ((Element) testchild).getTagName().equals(DESCRIPTION_ELEMENT_CLASS))
            {
                ClassDescription classDescription = new ClassDescription((Element) testchild, this);
                describedClasses.add(classDescription);
                classNameMap.put(classDescription.getOdmaName(),classDescription);
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
                if(scalarTypesNameToDescriptionMap.containsKey(scalarTypeDescription.getName().toLowerCase()))
                {
                    throw new DescriptionFileSemanticException("Multiple definitions of scalar type name "+scalarTypeDescription.getName());
                }
                if(scalarTypesIdToDescriptionMap.containsKey(new Integer(scalarTypeDescription.getNumericID())))
                {
                    throw new DescriptionFileSemanticException("Multiple definitions of scalar type ID "+scalarTypeDescription.getNumericID());
                }
                scalarTypes.add(scalarTypeDescription);
                scalarTypesNameToDescriptionMap.put(scalarTypeDescription.getName().toLowerCase(),scalarTypeDescription);
                scalarTypesIdToDescriptionMap.put(new Integer(scalarTypeDescription.getNumericID()),scalarTypeDescription);
            }
        }
    }
    
    /**
     * Validates that the base scalar of all internal scalar types exists.
     * 
     * @throws DescriptionFileSemanticException if a uniqueness constraint is violated
     */
    public void checkInternalScalarBaseTypes() throws DescriptionFileSemanticException
    {
        Iterator<ScalarTypeDescription> itScalarTypes = scalarTypes.iterator();
        while(itScalarTypes.hasNext())
        {
            ScalarTypeDescription scalarTypeToCheck = itScalarTypes.next();
            if(scalarTypeToCheck.isInternal())
            {
                String baseScalarTypeName = scalarTypeToCheck.getBaseScalar();
                if(getScalarTypeDescription(baseScalarTypeName) == null)
                {
                    throw new DescriptionFileSemanticException("The internal scalar type "+scalarTypeToCheck.getName()+" references the non-existing base scalar type "+baseScalarTypeName+".");
                }
            }
        }
    }
    
    /**
     * Validates multiple uniqueness constraints in the OpenDMA spec
     * 
     * @throws DescriptionFileSemanticException if a uniqueness constraint is violated
     */
    public void checkUniqueness() throws DescriptionFileSemanticException
    {
        // 1. class qnames and apinames must be globally unique
        HashSet<OdmaApiBuilderQName> uniqueClassQNameTestSet = new HashSet<OdmaApiBuilderQName>();
        HashSet<String> uniqueClassApiNameTestSet = new HashSet<String>();
        Iterator<ClassDescription> itAllClasses = describedClasses.iterator();
        while(itAllClasses.hasNext())
        {
            ClassDescription classDescription = itAllClasses.next();
            // test is qualified class name is unique
            if(uniqueClassQNameTestSet.contains(classDescription.getOdmaName()))
            {
                throw new DescriptionFileSemanticException("The qualified class name "+classDescription.getOdmaName()+" is not unique.");
            }
            uniqueClassQNameTestSet.add(classDescription.getOdmaName());
            if(uniqueClassApiNameTestSet.contains(classDescription.getApiName()))
            {
                throw new DescriptionFileSemanticException("The api name of class "+classDescription.getOdmaName()+" is not unique ("+classDescription.getApiName()+").");
            }
            uniqueClassApiNameTestSet.add(classDescription.getApiName());
        }
        // 2. declared property qnames and apinames must be unique within the class they are declared
        itAllClasses = describedClasses.iterator();
        while(itAllClasses.hasNext())
        {
            ClassDescription classDescription = itAllClasses.next();
            HashSet<OdmaApiBuilderQName> uniquePropertyQNameTestSet = new HashSet<OdmaApiBuilderQName>();
            HashSet<String> uniquePropertyApiNameTestSet = new HashSet<String>();
            List<PropertyDescription> declaredProperties = classDescription.getPropertyDescriptions();
            Iterator<PropertyDescription> itDeclaredProperties = declaredProperties.iterator();
            while(itDeclaredProperties.hasNext())
            {
                PropertyDescription propertyDescription = itDeclaredProperties.next();
                if(uniquePropertyQNameTestSet.contains(propertyDescription.getOdmaName()))
                {
                    throw new DescriptionFileSemanticException("The qualified property name "+propertyDescription.getOdmaName()+" in the class "+classDescription.getOdmaName()+" is not unique across the declared properties.");
                }
                uniquePropertyQNameTestSet.add(propertyDescription.getOdmaName());
                if(uniquePropertyApiNameTestSet.contains(propertyDescription.getApiName()))
                {
                    throw new DescriptionFileSemanticException("The property api name "+propertyDescription.getApiName()+" in the class "+classDescription.getOdmaName()+" is not unique across the declared properties.");
                }
                uniquePropertyApiNameTestSet.add(propertyDescription.getApiName());
            }
        }
        // 3. identically named properties between aspects must have the same signature
        HashMap<OdmaApiBuilderQName,PropertyDescription> aspectPropertyMap = new HashMap<OdmaApiBuilderQName,PropertyDescription>();
        HashMap<String,OdmaApiBuilderQName> aspectPropertyApiNameMap = new HashMap<String,OdmaApiBuilderQName>();
        itAllClasses = describedClasses.iterator();
        while(itAllClasses.hasNext())
        {
            ClassDescription classDescription = itAllClasses.next();
            if(!classDescription.getAspect())
            {
                continue;
            }
            List<PropertyDescription> declaredProperties = classDescription.getPropertyDescriptions();
            Iterator<PropertyDescription> itDeclaredProperties = declaredProperties.iterator();
            while(itDeclaredProperties.hasNext())
            {
                PropertyDescription propertyDescription = itDeclaredProperties.next();
                PropertyDescription other = aspectPropertyMap.get(propertyDescription.getOdmaName());
                if(other != null && !other.signatureEquals(propertyDescription))
                {
                    throw new DescriptionFileSemanticException("Multiple definitions of property qname "+propertyDescription.getOdmaName()+" in aspects have a different signature.\n"+other.toString()+"\n"+propertyDescription.toString());
                }
                aspectPropertyMap.put(propertyDescription.getOdmaName(),propertyDescription);
                OdmaApiBuilderQName otherQName = aspectPropertyApiNameMap.get(propertyDescription.getApiName());
                if(otherQName != null && !otherQName.equals(propertyDescription.getOdmaName()))
                {
                    throw new DescriptionFileSemanticException("The api name "+propertyDescription.getApiName()+" is used for different properties in aspects. "+otherQName.toString()+" and "+propertyDescription.getOdmaName().toString());
                }
            }
        }
        // 4. all properties must be unique in the inheritance hierarchy and share the same signature if they overlap with aspects
        itAllClasses = describedClasses.iterator();
        while(itAllClasses.hasNext())
        {
            ClassDescription classDescription = itAllClasses.next();
            HashMap<OdmaApiBuilderQName,OdmaApiBuilderQName> uniquePropertyQNameTestSet = new HashMap<OdmaApiBuilderQName,OdmaApiBuilderQName>();
            HashMap<String,OdmaApiBuilderQName> uniquePropertyApiNameTestSet = new HashMap<String,OdmaApiBuilderQName>();
            while(classDescription != null)
            {
                List<PropertyDescription> declaredProperties = classDescription.getPropertyDescriptions();
                Iterator<PropertyDescription> itDeclaredProperties = declaredProperties.iterator();
                while(itDeclaredProperties.hasNext())
                {
                    PropertyDescription propertyDescription = itDeclaredProperties.next();
                    if(uniquePropertyQNameTestSet.containsKey(propertyDescription.getOdmaName()))
                    {
                        throw new DescriptionFileSemanticException("The qualified property name "+propertyDescription.getOdmaName()+" in the class "+classDescription.getOdmaName()+" is not unique in the inheritance hierarchy. Desclared as well in "+uniquePropertyQNameTestSet.get(propertyDescription.getOdmaName()));
                    }
                    uniquePropertyQNameTestSet.put(propertyDescription.getOdmaName(),classDescription.getOdmaName());
                    if(uniquePropertyApiNameTestSet.containsKey(propertyDescription.getApiName()))
                    {
                        throw new DescriptionFileSemanticException("The property api name "+propertyDescription.getApiName()+" in the class "+classDescription.getOdmaName()+" is not unique in the inheritance hierarchy. Desclared as well in "+uniquePropertyApiNameTestSet.get(propertyDescription.getApiName()));
                    }
                    uniquePropertyApiNameTestSet.put(propertyDescription.getApiName(),classDescription.getOdmaName());
                    PropertyDescription propertyInAspect = aspectPropertyMap.get(propertyDescription.getOdmaName());
                    if(propertyInAspect != null && !propertyInAspect.signatureEquals(propertyDescription))
                    {
                        throw new DescriptionFileSemanticException("Identical property qname "+propertyDescription.getOdmaName()+" in class "+classDescription.getOdmaName()+" and at least one aspect with a different signature.\n"+propertyInAspect.toString()+"\n"+propertyDescription.toString());
                    }
                    OdmaApiBuilderQName apiNameInAspect = aspectPropertyApiNameMap.get(propertyDescription.getApiName());
                    if(apiNameInAspect != null && !apiNameInAspect.equals(propertyDescription.getOdmaName()))
                    {
                        throw new DescriptionFileSemanticException("The api name "+propertyDescription.getApiName()+" is used for different properties in the aspect "+apiNameInAspect.toString()+" and the class "+propertyDescription.getOdmaName().toString());
                    }
                }
                if(classDescription.getExtendsOdmaName() != null)
                {
                    classDescription = classNameMap.get(classDescription.getExtendsOdmaName());
                }
                else
                {
                    classDescription = null;
                }
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
        Iterator<ClassDescription> itAllClasses = describedClasses.iterator();
        while(itAllClasses.hasNext())
        {
            ClassDescription classDescription = itAllClasses.next();
            OdmaApiBuilderQName extendsName = classDescription.getExtendsOdmaName();
            if(extendsName != null)
            {
                if(!classNameMap.containsKey(extendsName))
                {
                    throw new DescriptionFileSemanticException("The class "+classDescription.getOdmaName()+" extends the class "+extendsName+" that does not exist in the description file");
                }
                boolean thisAspect = classDescription.getAspect();
                boolean extendsAspect = getDescribedClass(extendsName).getAspect();
                if(thisAspect != extendsAspect)
                {
                    throw new DescriptionFileSemanticException("The "+(thisAspect?"aspect":"class")+" "+classDescription.getOdmaName()+" must only extend another "+(thisAspect?"aspect":"class")+", but it extends a "+(extendsAspect?"aspect":"class"));
                }
            }
        }
        //-----< check all reference properties >------------------------------
        itAllClasses = describedClasses.iterator();
        while(itAllClasses.hasNext())
        {
            ClassDescription classDescription = (ClassDescription)itAllClasses.next();
            List<PropertyDescription> propertyDescriptions = classDescription.getPropertyDescriptions();
            Iterator<PropertyDescription> itPropertyDescriptions = propertyDescriptions.iterator();
            while(itPropertyDescriptions.hasNext())
            {
                PropertyDescription propertyDescription = itPropertyDescriptions.next();
                if(propertyDescription.getDataType().isReference())
                {
                    if(!classNameMap.containsKey(propertyDescription.getReferenceClassName()))
                    {
                        throw new DescriptionFileSemanticException("The property "+propertyDescription.getOdmaName()+" in the class "+classDescription.getOdmaName()+" references the class "+propertyDescription.getReferenceClassName()+" that does not exist in the description file");
                    }
                }
            }
        }
        //-----< check loops in class hierarchy >------------------------------
        itAllClasses = describedClasses.iterator();
        while(itAllClasses.hasNext())
        {
            ClassDescription classDescription = itAllClasses.next();
            HashSet<OdmaApiBuilderQName> visitedClassesSet = new HashSet<OdmaApiBuilderQName>();
            while(classDescription != null)
            {
                visitedClassesSet.add(classDescription.getOdmaName());
                if(classDescription.getExtendsOdmaName() != null)
                {
                    classDescription = classNameMap.get(classDescription.getExtendsOdmaName());
                    if(visitedClassesSet.contains(classDescription.getOdmaName()))
                    {
                        throw new DescriptionFileSemanticException("Circular inheritance hierarchy starting at "+classDescription.getOdmaName());
                    }
                }
                else
                {
                    classDescription = null;
                }
            }
        }
    }
    
    /**
     * Prints out all property qnames that are used acroos classes and aspects
     */
    public void printPropertyReuse()
    {
        // 1. class qnames and apinames must be globally unique
        HashMap<OdmaApiBuilderQName,LinkedList<ClassDescription>> propertyNameUsageMap = new HashMap<OdmaApiBuilderQName,LinkedList<ClassDescription>>();
        Iterator<ClassDescription> itAllClasses = describedClasses.iterator();
        while(itAllClasses.hasNext())
        {
            ClassDescription classDescription = itAllClasses.next();
            List<PropertyDescription> declaredProperties = classDescription.getPropertyDescriptions();
            Iterator<PropertyDescription> itDeclaredProperties = declaredProperties.iterator();
            while(itDeclaredProperties.hasNext())
            {
                PropertyDescription propertyDescription = itDeclaredProperties.next();
                if(!propertyNameUsageMap.containsKey(propertyDescription.getOdmaName()))
                {
                    propertyNameUsageMap.put(propertyDescription.getOdmaName(),new LinkedList<ClassDescription>());
                }
                propertyNameUsageMap.get(propertyDescription.getOdmaName()).add(classDescription);
            }
        }
        for(Map.Entry<OdmaApiBuilderQName,LinkedList<ClassDescription>> entry : propertyNameUsageMap.entrySet())
        {
            if(entry.getValue().peekFirst() != entry.getValue().peekLast())
            {
                System.out.println(entry.getKey());
                for(ClassDescription cd : entry.getValue())
                {
                    System.out.println("    "+cd.getOdmaName()+" ("+(cd.getAspect()?"Aspect":"Class")+")");
                }
            }
        }
    }

    /**
     * Validates that there are two classes defined that describe the layout of a class and the
     * layout of a property.
     * 
     * @throws DescriptionFileSemanticException if the class class or the propertyinfo class is missing
     */
    public void checkPredefinedClasses() throws DescriptionFileSemanticException
    {
        Iterator<ClassDescription> itAllClasses = describedClasses.iterator();
        while(itAllClasses.hasNext())
        {
            ClassDescription classDescription = itAllClasses.next();
            OdmaApiBuilderQName odmaName = classDescription.getOdmaName();
            if(odmaName.equals(objectClassQName))
            {
                objectClass = classDescription;
            }
            if(odmaName.equals(classClassQName))
            {
                classClass = classDescription;
            }
            if(odmaName.equals(propertyInfoClassQName))
            {
                propertyInfoClass = classDescription;
            }
        }
        if(objectClass == null)
        {
            throw new DescriptionFileSemanticException("The class hierarchy root ("+objectClassQName+") does not exist in the description file");
        }
        if(classClass == null)
        {
            throw new DescriptionFileSemanticException("The class that describes the layout of classes ("+classClassQName+") does not exist in the description file");
        }
        if(propertyInfoClass == null)
        {
            throw new DescriptionFileSemanticException("The class that describes the layout of properties ("+propertyInfoClassQName+") does not exist in the description file");
        }
    }

}