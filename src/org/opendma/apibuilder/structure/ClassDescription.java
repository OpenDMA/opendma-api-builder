package org.opendma.apibuilder.structure;

import java.util.ArrayList;
import java.util.List;

import org.opendma.apibuilder.DescriptionFileSyntaxException;
import org.opendma.apibuilder.DescriptionFileTypes;
import org.opendma.apibuilder.OdmaApiBuilderQName;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ClassDescription implements DescriptionFileTypes
{

    /** the qualified name of the described class */
    protected OdmaApiBuilderQName odmaName;

    /** the qualified name of the class that is extended by this class. Can be null */
    protected OdmaApiBuilderQName extendsOdmaName;

    /** the name of this class in the programmers API. This might differ from the name in ODMA due to naming conflicts (e.g. Class) */
    protected String apiName;
    
    protected boolean instantiable;
    
    protected boolean hidden;
    
    protected boolean system;
    
    protected boolean aspect;
    
    /** the full description of this class. Can be null. */
    protected String descriptionComment;

    /** the List of declared properties in this class */
    protected List<PropertyDescription> propertyDescriptions;

    /** the List of ApiHelpers for this class */
    protected List apiHelpers;
    
    /** the ApiDescription this class description is part of */
    protected ApiDescription containingApiDescription;
    
    /**
     * Create a new ClassDescription by reading the definition from a W3C DOM tree.
     * This constructor only performs some basic syntax checks.
     * 
     * @param classDescriptionElement the W3C DOM element to read the definition of this class from
     * @param containingApiDescription the ApiDescription this class description is part of
     * 
     * @throws DescriptionFileSyntaxException if there are syntactical errors in the XML file
     */
    public ClassDescription(Element classDescriptionElement, ApiDescription containingApiDescription) throws DescriptionFileSyntaxException
    {
        this.containingApiDescription = containingApiDescription;
        parse(classDescriptionElement);
    }
    
    public String toString()
    {
        return odmaName.toString();
    }

    /**
     * Returns the qualified name of the described class.
     * 
     * @return the qualified name of the described class
     */
    public OdmaApiBuilderQName getOdmaName()
    {
        return odmaName;
    }

    /**
     * Returns the qualified name of the class that is extended by this class. Can be null.
     * 
     * @return the qualified name of the class that is extended by this class. Can be null
     */
    public OdmaApiBuilderQName getExtendsOdmaName()
    {
        return extendsOdmaName;
    }

    /**
     * Returns the name of this class in the programmers API. This might differ from the
     * name in ODMA due to naming conflicts (e.g. Class).
     * 
     * @return the name of this class in the programmers API.
     */
    public String getApiName()
    {
        return apiName;
    }

    public boolean getInstantiable()
    {
        return instantiable;
    }

    public boolean getHidden()
    {
        return hidden;
    }

    public boolean getSystem()
    {
        return system;
    }
    
    public boolean getAspect()
    {
        return aspect;
    }
    
    /**
     * Returns the name of the parent class in the programmers API. This might differ from the
     * name in ODMA due to naming conflicts (e.g. Class).
     * 
     * @return the name of the parent class in the programmers API.
     */
    public String getExtendsApiName()
    {
        return (extendsOdmaName == null) ? null : containingApiDescription.getDescribedClass(extendsOdmaName).getApiName();
    }

    /**
     * Returns the full description of this class. Can be null.
     * 
     * @return the full description of this class. Can be null.
     */
    public String getDescription()
    {
        return descriptionComment;
    }

    /**
     * Returns the List of declared properties in this class.
     * 
     * @return the List of declared properties in this class
     */
    public List<PropertyDescription> getPropertyDescriptions()
    {
        return propertyDescriptions;
    }

    /**
     * Returns the List of ApiHelpers for this class.
     * 
     * @return the List of ApiHelpers for this class
     */
    public List getApiHelpers()
    {
        return apiHelpers;
    }
    
    /**
     * Returns the ApiDescription this class description is part of.
     * 
     * @return the ApiDescription this class description is part of
     */
    public ApiDescription getContainingApiDescription()
    {
        return containingApiDescription;
    }
    
    /**
     * Read the definition of this class from a W3C DOM tree and perform some
     * basic syntax checks (e.g. missing elements, ...).
     * 
     * @param classDescriptionElement the W3C DOM element to read the definition from
     * 
     * @throws DescriptionFileSyntaxException if there are syntactical errors in the XML file
     */
    protected void parse(Element classDescriptionElement) throws DescriptionFileSyntaxException
    {
        // check element name
        if (!classDescriptionElement.getNodeName().equals(DESCRIPTION_ELEMENT_CLASS))
        {
            throw new DescriptionFileSyntaxException("The name of a ClassDescription element must be "+DESCRIPTION_ELEMENT_CLASS);
        }
        // read description
        String qualifier = classDescriptionElement.getAttribute(DESCRIPTION_ATTRIBUTE_QUALIFIER);
        String name = classDescriptionElement.getAttribute(DESCRIPTION_ATTRIBUTE_NAME);
        if((qualifier==null) || (qualifier.trim().length()==0) )
        {
            throw new DescriptionFileSyntaxException("Missing qualifier of class");
        }
        if((name==null) || (name.trim().length()==0) )
        {
            throw new DescriptionFileSyntaxException("Missing name of class");
        }
        odmaName = new OdmaApiBuilderQName(qualifier,name);
        String extendsQualifier = classDescriptionElement.getAttribute(DESCRIPTION_ATTRIBUTE_EXTENDSQUALIFIER);
        String extendsName = classDescriptionElement.getAttribute(DESCRIPTION_ATTRIBUTE_EXTENDSNAME);
        if(extendsQualifier.trim().length()==0)
        {
            extendsQualifier = null;
        }
        if(extendsName.trim().length()==0)
        {
            extendsName = null;
        }
        if( (extendsQualifier!=null) && (extendsName!=null) )
        {
            extendsOdmaName = new OdmaApiBuilderQName(extendsQualifier,extendsName);
        }
        apiName = classDescriptionElement.getAttribute(DESCRIPTION_ATTRIBUTE_APINAME);
        if((apiName==null) || (apiName.trim().length()==0) )
        {
            throw new DescriptionFileSyntaxException("Missing apiname of class "+odmaName.toString());
        }
        String hiddenString = classDescriptionElement.getAttribute(DESCRIPTION_ATTRIBUTE_HIDDEN);
        String systemString = classDescriptionElement.getAttribute(DESCRIPTION_ATTRIBUTE_SYSTEM);
        String instantiableString = classDescriptionElement.getAttribute(DESCRIPTION_ATTRIBUTE_INSTANTIABLE);
        String aspectString = classDescriptionElement.getAttribute(DESCRIPTION_ATTRIBUTE_ASPECT);
        if((hiddenString==null) || (hiddenString.trim().length()==0) )
        {
            throw new DescriptionFileSyntaxException("Missing hidden attribute in class "+odmaName.toString());
        }
        if((systemString==null) || (systemString.trim().length()==0) )
        {
            throw new DescriptionFileSyntaxException("Missing system attribute in class "+odmaName.toString());
        }
        if((instantiableString==null) || (instantiableString.trim().length()==0) )
        {
            throw new DescriptionFileSyntaxException("Missing instantiable attribute in class "+odmaName.toString());
        }
        if((aspectString==null) || (aspectString.trim().length()==0) )
        {
            throw new DescriptionFileSyntaxException("Missing aspect attribute in class "+odmaName.toString());
        }
        hidden = parseBoolean(hiddenString,DESCRIPTION_ATTRIBUTE_HIDDEN);
        system = parseBoolean(systemString,DESCRIPTION_ATTRIBUTE_SYSTEM);
        instantiable = parseBoolean(instantiableString,DESCRIPTION_ATTRIBUTE_INSTANTIABLE);
        aspect = parseBoolean(aspectString,DESCRIPTION_ATTRIBUTE_ASPECT);
        // iterate through all elements below the <Class> Element
        propertyDescriptions = new ArrayList();
        apiHelpers = new ArrayList();
        Element descriptionElement = null;
        NodeList pluginlist = classDescriptionElement.getChildNodes();
        for (int i = 0; i < pluginlist.getLength(); i++)
        {
            Node testchild = pluginlist.item(i);
            if (testchild.getNodeType() == Node.ELEMENT_NODE)
            {
                if (((Element) testchild).getTagName().equals(DESCRIPTION_ELEMENT_PROPERTY))
                {
                    propertyDescriptions.add(new PropertyDescription((Element) testchild, this));
                }
                else if (((Element) testchild).getTagName().equals(DESCRIPTION_ELEMENT_APIHELPER))
                {
                    apiHelpers.add(new ApiHelperDescription((Element) testchild, this));
                }
                else if (((Element) testchild).getTagName().equals(DESCRIPTION_ELEMENT_DESCRIPTION))
                {
                    if(descriptionElement != null)
                    {
                        throw new DescriptionFileSyntaxException("Multiple occurance of the "+DESCRIPTION_ELEMENT_DESCRIPTION+" element in class "+odmaName.toString());
                    }
                    descriptionElement = (Element)testchild;
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
            throw new DescriptionFileSyntaxException("Invalid boolean value in attribute "+attributeName+" of class "+odmaName.toString());
        }
    }

}