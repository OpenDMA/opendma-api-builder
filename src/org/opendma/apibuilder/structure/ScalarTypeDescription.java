package org.opendma.apibuilder.structure;

import org.opendma.apibuilder.DescriptionFileSyntaxException;
import org.opendma.apibuilder.DescriptionFileTypes;
import org.w3c.dom.Element;

public class ScalarTypeDescription implements DescriptionFileTypes
{

    /** the name of the scalar type */
    protected String Name;

    /** the numeric ID of this scalar type */
    protected int NumericID;
    
    /** wheather this scalar type is only used in the API */
    protected boolean Internal;
    
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
        this.Internal = internal;
        parse(scalarTypeElement);
    }

    /**
     * Returns the name of the scalar type.
     * 
     * @return the name of the scalar type
     */
    public String getName()
    {
        return Name;
    }

    /**
     * Returns the numeric ID of this scalar type.
     * 
     * @return the numeric ID of this scalar type
     */
    public int getNumericID()
    {
        return NumericID;
    }
    
    /**
     * Returns wheather this scalar type is only used in the API or not.
     * 
     * @return wheather this scalar type is only used in the API or not
     */
    public boolean isInternal()
    {
        return Internal;
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
        String numericId = scalarTypeElement.getAttribute(DESCRIPTION_ATTRIBUTE_NUMERICID);
        String name = scalarTypeElement.getAttribute(DESCRIPTION_ATTRIBUTE_NAME);
        if((numericId==null) || (numericId.trim().length()==0) )
        {
            throw new DescriptionFileSyntaxException("Missing numericId of ScalarType");
        }
        if((name==null) || (name.trim().length()==0) )
        {
            throw new DescriptionFileSyntaxException("Missing name of ScalarType");
        }
        Name = name;
        try
        {
            NumericID = Integer.parseInt(numericId);
        }
        catch(Exception e)
        {
            throw new DescriptionFileSyntaxException("numericId of ScalarType is not an integer");
        }
    }

}