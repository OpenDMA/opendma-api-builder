package org.opendma.apibuilder.structure;

import org.opendma.apibuilder.DescriptionFileSyntaxException;
import org.opendma.apibuilder.DescriptionFileTypes;
import org.opendma.apibuilder.OdmaBasicTypes;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ApiHelperDescription implements DescriptionFileTypes, OdmaBasicTypes
{

    /** the name of this property in the programmers API. This might differ from the name in ODMA due to naming conflicts (e.g. Class) */
    protected String apiName;

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
    public ApiHelperDescription(Element propertyDescriptionElement, ClassDescription containingClass) throws DescriptionFileSyntaxException
    {
        this.ContainingClass = containingClass;
        parse(propertyDescriptionElement);
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
        if (!propertyDescriptionElement.getNodeName().equals(DESCRIPTION_ELEMENT_APIHELPER))
        {
            throw new DescriptionFileSyntaxException("The name of a PropertyDescription element must be "+DESCRIPTION_ELEMENT_APIHELPER);
        }
        // read description
        apiName = propertyDescriptionElement.getAttribute(DESCRIPTION_ATTRIBUTE_APINAME);
        if((apiName==null) || (apiName.trim().length()==0) )
        {
            throw new DescriptionFileSyntaxException("Missing apiname of ApiHelper in class "+this.ContainingClass.getOdmaName().toString());
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
                        throw new DescriptionFileSyntaxException("Multiple occurance of the "+DESCRIPTION_ELEMENT_ABSTRACT+" element in the ApiHelper description "+apiName+" in class "+this.ContainingClass.getOdmaName().toString());
                    }
                    abstractElement = (Element)testchild;
                }
                else if(((Element) testchild).getTagName().equals(DESCRIPTION_ELEMENT_DESCRIPTION))
                {
                    if(descriptionElement != null)
                    {
                        throw new DescriptionFileSyntaxException("Multiple occurance of the "+DESCRIPTION_ELEMENT_DESCRIPTION+" element in the ApiHelper description "+apiName+" in class "+this.ContainingClass.getOdmaName().toString());
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
            throw new DescriptionFileSyntaxException("No "+DESCRIPTION_ELEMENT_ABSTRACT+" defined for ApiHelper description "+apiName+" in class "+this.ContainingClass.getOdmaName().toString());
        }
        if( (descriptionComment == null) || (descriptionComment.trim().length() == 0))
        {
            throw new DescriptionFileSyntaxException("No "+DESCRIPTION_ELEMENT_DESCRIPTION+" defined for ApiHelpert description "+apiName+" in class "+this.ContainingClass.getOdmaName().toString());
        }
    }
    
}