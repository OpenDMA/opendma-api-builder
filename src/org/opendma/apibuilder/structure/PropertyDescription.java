package org.opendma.apibuilder.structure;

import org.opendma.apibuilder.DescriptionFileSyntaxException;
import org.opendma.apibuilder.DescriptionFileTypes;
import org.opendma.apibuilder.OdmaApiBuilderQName;
import org.opendma.apibuilder.OdmaBasicTypes;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class PropertyDescription implements DescriptionFileTypes, OdmaBasicTypes
{

    /** the qualified name of the described property */
    protected OdmaApiBuilderQName OdmaName;

    /** the name of this property in the programmers API. This might differ from the name in ODMA due to naming conflicts (e.g. Class) */
    protected String apiName;

    /** the ReadOnly attribute of this property */
    protected boolean readOnly;

    /** the data type of this property */
    protected int dataType;

    /** the multi value attribute of this property */
    protected boolean multiValue;

    /** the Required attribute of this property */
    protected boolean required;
    
    /** the required class of the target object if this is a reference property, null otherwise */
    protected OdmaApiBuilderQName referenceClassName;

    /** the ClassDescription this property has been declared in */
    protected ClassDescription ContainingClass;
    
    /** the short description of this property */
    protected String abstractComment;
    
    /** the full description of this property */
    protected String descriptionComment;
    
    /**
     * Create a new PropertyDescription by reading the definition from a W3C DOM tree.
     * This constructor only performs some basic syntax checks.
     * 
     * @param propertyDescriptionElement the W3C DOM element to read the definition of this property from
     * @param containingClass the ClassDescription this property has been declared in
     * 
     * @throws DescriptionFileSyntaxException if there are syntactical errors in the XML file
     */
    public PropertyDescription(Element propertyDescriptionElement, ClassDescription containingClass) throws DescriptionFileSyntaxException
    {
        this.ContainingClass = containingClass;
        parse(propertyDescriptionElement);
    }

    /**
     * Returns the qualified name of the described property.
     * 
     * @return the qualified name of the described property
     */
    public OdmaApiBuilderQName getOdmaName()
    {
        return OdmaName;
    }

    /**
     * Returns the name of this property in the programmers API. This might differ from the
     * name in ODMA due to naming conflicts (e.g. Class).
     * 
     * @return the name of this property in the programmers API.
     */
    public String getApiName()
    {
        return apiName;
    }

    /**
     * Returns the ReadOnly attribute of this property.
     * 
     * @return the ReadOnly attribute of this property
     */
    public boolean isReadOnly()
    {
        return readOnly;
    }

    /**
     * Returns the data type of this property.
     * 
     * @return the data type of this property
     */
    public int getDataType()
    {
        return dataType;
    }
    
    /**
     * Reaturns the multi value attribute of this property.
     * 
     * @return the multi value attribute of this property
     */
    public boolean getMultiValue()
    {
        return multiValue;
    }
    
    /**
     * Returns the Required attribute of this property.
     * 
     * @return the Required attribute of this property
     */
    public boolean getRequired()
    {
        return required;
    }

    /**
     * Returns the required class of the target object if this is a reference property, null otherwise.
     * 
     * @return the required class of the target object if this is a reference property, null otherwise
     */
    public OdmaApiBuilderQName getReferenceClassName()
    {
        return referenceClassName;
    }
    
    /**
     * Returns the ClassDescription this property has been declared in.
     * 
     * @return the ClassDescription this property has been declared in
     */
    public ClassDescription getContainingClass()
    {
        return ContainingClass;
    }
    
    /**
     * Returns the short description of this property.
     * 
     * @return the short description of this property
     */
    public String getAbstract()
    {
        return abstractComment;
    }
    
    /**
     * Returns the full description of this property.
     * 
     * @return the full description of this property
     */
    public String getDescription()
    {
        return descriptionComment;
    }
    
    /**
     * Returns wheather this property is a reference or not.
     * 
     * @return wheather this property is a reference or not
     */
    public boolean isReference()
    {
        ScalarTypeDescription scalarTypeDescription = ContainingClass.getContainingApiDescription().getScalarTypeDescription(dataType);
        return scalarTypeDescription.isReference();
    }
    
    /**
     * Read the definition of this property from a W3C DOM tree and perform some
     * basic syntax checks (e.g. missing elements, ...).
     * 
     * @param propertyDescriptionElement the W3C DOM element to read the definition from
     * 
     * @throws DescriptionFileSyntaxException if there are syntactical errors in the XML file
     */
    protected void parse(Element propertyDescriptionElement) throws DescriptionFileSyntaxException
    {
        // check element name
        if (!propertyDescriptionElement.getNodeName().equals(DESCRIPTION_ELEMENT_PROPERTY))
        {
            throw new DescriptionFileSyntaxException("The name of a PropertyDescription element must be "+DESCRIPTION_ELEMENT_PROPERTY);
        }
        // read description
        String qualifier = propertyDescriptionElement.getAttribute(DESCRIPTION_ATTRIBUTE_QUALIFIER);
        String name = propertyDescriptionElement.getAttribute(DESCRIPTION_ATTRIBUTE_NAME);
        if((qualifier==null) || (qualifier.trim().length()==0) )
        {
            throw new DescriptionFileSyntaxException("Missing qualifier of property in class "+this.ContainingClass.getOdmaName().toString());
        }
        if((name==null) || (name.trim().length()==0) )
        {
            throw new DescriptionFileSyntaxException("Missing name of property in class "+this.ContainingClass.getOdmaName().toString());
        }
        OdmaName = new OdmaApiBuilderQName(qualifier,name);
        apiName = propertyDescriptionElement.getAttribute(DESCRIPTION_ATTRIBUTE_APINAME);
        String readOnlyString = propertyDescriptionElement.getAttribute(DESCRIPTION_ATTRIBUTE_READONLY);
        String dataTypeString = propertyDescriptionElement.getAttribute(DESCRIPTION_ATTRIBUTE_DATATYPE);
        String multiValueString = propertyDescriptionElement.getAttribute(DESCRIPTION_ATTRIBUTE_MULTIVALUE);
        String requiredString = propertyDescriptionElement.getAttribute(DESCRIPTION_ATTRIBUTE_REQUIRED);
        if((apiName==null) || (apiName.trim().length()==0) )
        {
            throw new DescriptionFileSyntaxException("Missing apiname of property "+OdmaName.toString()+" in class "+this.ContainingClass.getOdmaName().toString());
        }
        if((readOnlyString==null) || (readOnlyString.trim().length()==0) )
        {
            throw new DescriptionFileSyntaxException("Missing readonly of property "+OdmaName.toString()+" in class "+this.ContainingClass.getOdmaName().toString());
        }
        if((dataTypeString==null) || (dataTypeString.trim().length()==0) )
        {
            throw new DescriptionFileSyntaxException("Missing readonly of property "+OdmaName.toString()+" in class "+this.ContainingClass.getOdmaName().toString());
        }
        if((multiValueString==null) || (multiValueString.trim().length()==0) )
        {
            throw new DescriptionFileSyntaxException("Missing multivalue of property "+OdmaName.toString()+" in class "+this.ContainingClass.getOdmaName().toString());
        }
        if((requiredString==null) || (requiredString.trim().length()==0) )
        {
            throw new DescriptionFileSyntaxException("Missing required of property "+OdmaName.toString()+" in class "+this.ContainingClass.getOdmaName().toString());
        }
        readOnly = parseBoolean(readOnlyString,DESCRIPTION_ATTRIBUTE_READONLY);
        dataType = parseDataType(dataTypeString,DESCRIPTION_ATTRIBUTE_DATATYPE);
        multiValue = parseBoolean(multiValueString,DESCRIPTION_ATTRIBUTE_MULTIVALUE);
        required = parseBoolean(requiredString,DESCRIPTION_ATTRIBUTE_REQUIRED);
        String referenceQualifier = propertyDescriptionElement.getAttribute(DESCRIPTION_ATTRIBUTE_REFERENCEQUALIFIER);
        String referenceName = propertyDescriptionElement.getAttribute(DESCRIPTION_ATTRIBUTE_REFERENCENAME);
        boolean referenceQualifierEmpty = ((referenceQualifier==null) || (referenceQualifier.trim().length()==0) );
        boolean referenceNameEmpty = ((referenceName==null) || (referenceName.trim().length()==0) );
        if(dataType == TYPE_REFERENCE)
        {
            if(referenceQualifierEmpty)
            {
                throw new DescriptionFileSyntaxException("The attribute referenceQualifier is required for dataType reference in property "+OdmaName.toString()+" in class "+this.ContainingClass.getOdmaName().toString());
            }
            if(referenceNameEmpty)
            {
                throw new DescriptionFileSyntaxException("The attribute referenceName is required for dataType reference in property "+OdmaName.toString()+" in class "+this.ContainingClass.getOdmaName().toString());
            }
            referenceClassName = new OdmaApiBuilderQName(referenceQualifier,referenceName);
        }
        else
        {
            if(!referenceQualifierEmpty)
            {
                throw new DescriptionFileSyntaxException("The attribute referenceQualifier is only allowed for dataType reference in property "+OdmaName.toString()+" in class "+this.ContainingClass.getOdmaName().toString());
            }
            if(!referenceNameEmpty)
            {
                throw new DescriptionFileSyntaxException("The attribute referenceName is only allowed for dataType reference in property "+OdmaName.toString()+" in class "+this.ContainingClass.getOdmaName().toString());
            }
        }
        Element abstractElement = null;
        Element descriptionElement = null;
        NodeList childNodes = propertyDescriptionElement.getChildNodes();
        for(int i = 0; i < childNodes.getLength(); i++)
        {
            Node testchild = childNodes.item(i);
            if (testchild.getNodeType() == Node.ELEMENT_NODE)
            {
                if(((Element) testchild).getTagName().equals(DESCRIPTION_ELEMENT_ABSTRACT))
                {
                    if(abstractElement != null)
                    {
                        throw new DescriptionFileSyntaxException("Multiple occurance of the "+DESCRIPTION_ELEMENT_ABSTRACT+" element in the property description "+OdmaName.toString()+" in class "+this.ContainingClass.getOdmaName().toString());
                    }
                    abstractElement = (Element)testchild;
                }
                else if(((Element) testchild).getTagName().equals(DESCRIPTION_ELEMENT_DESCRIPTION))
                {
                    if(descriptionElement != null)
                    {
                        throw new DescriptionFileSyntaxException("Multiple occurance of the "+DESCRIPTION_ELEMENT_DESCRIPTION+" element in the property description "+OdmaName.toString()+" in class "+this.ContainingClass.getOdmaName().toString());
                    }
                    descriptionElement = (Element)testchild;
                }
            }
        }
        if(abstractElement != null)
        {
            abstractComment = "";
            NodeList abstractChilds = abstractElement.getChildNodes();
            for(int i = 0; i < abstractChilds.getLength(); i++)
            {
                Node testchild = abstractChilds.item(i);
                if(testchild.getNodeType() == Node.TEXT_NODE)
                {
                    abstractComment = abstractComment + testchild.getNodeValue();
                }
            }
        }
        if(descriptionElement != null)
        {
            descriptionComment = "";
            NodeList descriptionChilds = descriptionElement.getChildNodes();
            for(int i = 0; i < descriptionChilds.getLength(); i++)
            {
                Node testchild = descriptionChilds.item(i);
                if(testchild.getNodeType() == Node.TEXT_NODE)
                {
                    descriptionComment = descriptionComment + testchild.getNodeValue();
                }
            }
        }
        if( (abstractComment == null) || (abstractComment.trim().length() == 0))
        {
            throw new DescriptionFileSyntaxException("No "+DESCRIPTION_ELEMENT_ABSTRACT+" defined for property description "+OdmaName.toString()+" in class "+this.ContainingClass.getOdmaName().toString());
        }
        if( (descriptionComment == null) || (descriptionComment.trim().length() == 0))
        {
            throw new DescriptionFileSyntaxException("No "+DESCRIPTION_ELEMENT_DESCRIPTION+" defined for property description "+OdmaName.toString()+" in class "+this.ContainingClass.getOdmaName().toString());
        }
    }

    /**
     * Returns the boolean value expressed by the given String. Throws a DescriptionFileSyntaxError
     * if the given String does not represent a boolean value.
     * 
     * @param String that represents a boolean Value
     * @param attributeName the attribute name used in the Exception message if this fails
     * 
     * @return the boolean value expressed by the given String
     * 
     * @throws DescriptionFileSyntaxException if the given String does not represent a boolean value
     */
    protected boolean parseBoolean(String value, String attributeName) throws DescriptionFileSyntaxException
    {
        if(value.equalsIgnoreCase("true"))
        {
            return true;
        }
        else if(value.equalsIgnoreCase("false"))
        {
            return false;
        }
        else
        {
            throw new DescriptionFileSyntaxException("Invalid boolean value in attribute "+attributeName+" of class "+this.ContainingClass.toString());
        }
    }

    /**
     * Returns the int data type value expressed by the given String. Throws a DescriptionFileSyntaxError
     * if the given String does not represent a data type.
     * 
     * @param String that represents a data type
     * @param attributeName the attribute name used in the Exception message if this fails
     * 
     * @return the boolean value expressed by the given String
     * 
     * @throws DescriptionFileSyntaxException if the given String does not represent a data type
     */
    protected int parseDataType(String value, String attributeName) throws DescriptionFileSyntaxException
    {
        ScalarTypeDescription scalarTypeDescription = ContainingClass.getContainingApiDescription().getScalarTypeDescription(value);
        if(scalarTypeDescription == null)
        {
            throw new DescriptionFileSyntaxException("Invalid scalar type value in attribute "+attributeName+" of property "+this.OdmaName.toString()+" in class "+this.ContainingClass.toString());
        }
        return scalarTypeDescription.getNumericID();
    }
    
}