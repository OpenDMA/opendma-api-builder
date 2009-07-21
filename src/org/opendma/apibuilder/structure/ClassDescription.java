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
    protected OdmaApiBuilderQName OdmaName;

    /** the qualified name of the class that is extended by this class. Can be null */
    protected OdmaApiBuilderQName ExtendsName;

    /** the name of this class in the programmers API. This might differ from the name in ODMA due to naming conflicts (e.g. Class) */
    protected String ApiName;
    
    /** the full description of this class. Can be null. */
    protected String Description;

    /** the List of declared properties in this class */
    protected List PropertyDescriptions;
    
    /** the ApiDescription this class description is part of */
    protected ApiDescription ContainingApiDescription;
    
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
        this.ContainingApiDescription = containingApiDescription;
        parse(classDescriptionElement);
    }

    /**
     * Returns the qualified name of the described class.
     * 
     * @return the qualified name of the described class
     */
    public OdmaApiBuilderQName getOdmaName()
    {
        return OdmaName;
    }

    /**
     * Returns the qualified name of the class that is extended by this class. Can be null.
     * 
     * @return the qualified name of the class that is extended by this class. Can be null
     */
    public OdmaApiBuilderQName getExtendsName()
    {
        return ExtendsName;
    }

    /**
     * Returns the name of this class in the programmers API. This might differ from the
     * name in ODMA due to naming conflicts (e.g. Class).
     * 
     * @return the name of this class in the programmers API.
     */
    public String getApiName()
    {
        return ApiName;
    }
    
    /**
     * Returns the name of the parent class in the programmers API. This might differ from the
     * name in ODMA due to naming conflicts (e.g. Class).
     * 
     * @return the name of the parent class in the programmers API.
     */
    public String getExtendsApiName()
    {
        return (ExtendsName == null) ? null : ContainingApiDescription.getDescribedClass(ExtendsName).getApiName();
    }

    /**
     * Returns the full description of this class. Can be null.
     * 
     * @return the full description of this class. Can be null.
     */
    public String getDescription()
    {
        return Description;
    }
    

    /**
     * Returns the List of declared properties in this class.
     * 
     * @return the List of declared properties in this class
     */
    public List getPropertyDescriptions()
    {
        return PropertyDescriptions;
    }
    
    /**
     * Returns the ApiDescription this class description is part of.
     * 
     * @return the ApiDescription this class description is part of
     */
    public ApiDescription getContainingApiDescription()
    {
        return ContainingApiDescription;
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
        OdmaName = new OdmaApiBuilderQName(qualifier,name);
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
            ExtendsName = new OdmaApiBuilderQName(extendsQualifier,extendsName);
        }
        String apiname = classDescriptionElement.getAttribute(DESCRIPTION_ATTRIBUTE_APINAME);
        if((apiname==null) || (apiname.trim().length()==0) )
        {
            throw new DescriptionFileSyntaxException("Missing apiname of class "+OdmaName.toString());
        }
        ApiName = apiname;
        // iterate through all elements below the <Class> Element
        PropertyDescriptions = new ArrayList();
        Element descriptionElement = null;
        NodeList pluginlist = classDescriptionElement.getChildNodes();
        for (int i = 0; i < pluginlist.getLength(); i++)
        {
            Node testchild = pluginlist.item(i);
            if (testchild.getNodeType() == Node.ELEMENT_NODE)
            {
                if (((Element) testchild).getTagName().equals(DESCRIPTION_ELEMENT_PROPERTY))
                {
                    PropertyDescriptions.add(new PropertyDescription((Element) testchild, this));
                }
                else if (((Element) testchild).getTagName().equals(DESCRIPTION_ELEMENT_DESCRIPTION))
                {
                    if(descriptionElement != null)
                    {
                        throw new DescriptionFileSyntaxException("Multiple occurance of the "+DESCRIPTION_ELEMENT_DESCRIPTION+" element in class "+OdmaName.toString());
                    }
                    descriptionElement = (Element)testchild;
                }
            }
        }
        if(descriptionElement != null)
        {
            Description = "";
            NodeList descriptionChilds = descriptionElement.getChildNodes();
            for(int i = 0; i < descriptionChilds.getLength(); i++)
            {
                Node testchild = descriptionChilds.item(i);
                if(testchild.getNodeType() == Node.TEXT_NODE)
                {
                    Description = Description + testchild.getNodeValue();
                }
            }
        }
    }

}