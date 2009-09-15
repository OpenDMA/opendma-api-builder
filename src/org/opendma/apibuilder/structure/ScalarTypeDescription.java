package org.opendma.apibuilder.structure;

import org.opendma.apibuilder.DescriptionFileSyntaxException;
import org.opendma.apibuilder.DescriptionFileTypes;
import org.w3c.dom.Element;

public class ScalarTypeDescription implements DescriptionFileTypes
{

    /** the name of the scalar type */
    protected String name;

    /** the numeric ID of this scalar type */
    protected int numericID;
    
    /** wheather this scalar type is only used in the API */
    protected boolean internal;
    
    /** wheather this scalar type is a reference or not */
    protected boolean reference;
    
    /** the base scalar type of this internal scalar type if this is an internal scalar type, <code>null</code> otherwise */
    protected String baseScalar;
    
    /**
     * Create a new ScalarTypeDescription by reading the definition from a W3C DOM tree.
     * This constructor only performs some basic syntax checks.
     * 
     * @param scalarTypeElement the W3C DOM element to read the definition of this scalar type from
     * @param internal wheather this scalar type is only used in the API
     * 
     * @throws DescriptionFileSyntaxException if there are syntactical errors in the XML file
     */
    public ScalarTypeDescription(Element scalarTypeElement, boolean internal) throws DescriptionFileSyntaxException
    {
        this.internal = internal;
        parse(scalarTypeElement);
    }

    /**
     * Returns the name of the scalar type.
     * 
     * @return the name of the scalar type
     */
    public String getName()
    {
        return name;
    }

    /**
     * Returns the numeric ID of this scalar type.
     * 
     * @return the numeric ID of this scalar type
     */
    public int getNumericID()
    {
        return numericID;
    }
    
    /**
     * Returns wheather this scalar type is only used in the API or not.
     * 
     * @return wheather this scalar type is only used in the API or not
     */
    public boolean isInternal()
    {
        return internal;
    }
    
    /**
     * Returns wheather this scalar type is a reference or not.
     * 
     * @return wheather this scalar type is a reference or not
     */
    public boolean isReference()
    {
        return reference;
    }
    
    /**
     * Returns the base scalar type of this internal scalar type if this is an internal scalar type, <code>null</code> otherwise.
     * 
     * @return the base scalar type of this internal scalar type if this is an internal scalar type, <code>null</code> otherwise
     */
    public String getBaseScalar()
    {
        return baseScalar;
    }

    /**
     * Read the definition of this scalar type from a W3C DOM tree and perform some
     * basic syntax checks (e.g. missing elements, ...).
     * 
     * @param scalarTypeElement the W3C DOM element to read the definition of this scalar type from
     * 
     * @throws DescriptionFileSyntaxException if there are syntactical errors in the XML file
     */
    protected void parse(Element scalarTypeElement) throws DescriptionFileSyntaxException
    {
        // check element name
        if (!(scalarTypeElement.getNodeName().equals(DESCRIPTION_ELEMENT_SCALARTYPE)||scalarTypeElement.getNodeName().equals(DESCRIPTION_ELEMENT_INTERNALSCALARTYPE)))
        {
            throw new DescriptionFileSyntaxException("The name of a ScalarTypeDescription element must be "+DESCRIPTION_ELEMENT_SCALARTYPE+" or "+DESCRIPTION_ELEMENT_INTERNALSCALARTYPE);
        }
        // read description
        String numericIdString = scalarTypeElement.getAttribute(DESCRIPTION_ATTRIBUTE_NUMERICID);
        name = scalarTypeElement.getAttribute(DESCRIPTION_ATTRIBUTE_NAME);
        String referenceString = scalarTypeElement.getAttribute(DESCRIPTION_ATTRIBUTE_REFERENCE);
        baseScalar = scalarTypeElement.getAttribute(DESCRIPTION_ATTRIBUTE_BASESCALAR);
        if((numericIdString==null) || (numericIdString.trim().length()==0) )
        {
            throw new DescriptionFileSyntaxException("Missing numericId of ScalarType");
        }
        if((name==null) || (name.trim().length()==0) )
        {
            throw new DescriptionFileSyntaxException("Missing name of ScalarType");
        }
        if((referenceString==null) || (referenceString.trim().length()==0) )
        {
            throw new DescriptionFileSyntaxException("Missing reference property of ScalarType");
        }
        if(internal)
        {
            if((baseScalar==null) || (baseScalar.trim().length()==0) )
            {
                throw new DescriptionFileSyntaxException("Missing baseScalar of internal ScalarType");
            }
        }
        try
        {
            numericID = Integer.parseInt(numericIdString);
        }
        catch(Exception e)
        {
            throw new DescriptionFileSyntaxException("numericId of ScalarType is not an integer");
        }
        if(referenceString.equalsIgnoreCase("true"))
        {
            reference = true;
        }
        else if(referenceString.equalsIgnoreCase("false"))
        {
            reference = false;
        }
        else
        {
            throw new DescriptionFileSyntaxException("reference of ScalarType is not boolean (true/false)");
        }
    }

}