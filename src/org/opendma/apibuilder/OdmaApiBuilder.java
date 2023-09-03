package org.opendma.apibuilder;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.opendma.apibuilder.apiwriter.cpp.CppApiWriter;
import org.opendma.apibuilder.apiwriter.cs.CsApiWriter;
import org.opendma.apibuilder.apiwriter.java.Java5ApiWriter;
import org.opendma.apibuilder.apiwriter.java14.Java14ApiWriter;
import org.opendma.apibuilder.apiwriter.php.PhpApiWriter;
import org.opendma.apibuilder.structure.ApiDescription;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

public class OdmaApiBuilder implements DescriptionFileTypes, OdmaBasicTypes
{

    public static void main(String[] args)
    {
        OdmaApiBuilder apiBuilder = new OdmaApiBuilder();
        apiBuilder.run(args);
    }

    public void run(String[] args)
    {
        //-----< STEP 1: check arguments >-------------------------------------
        if ((args == null) || (args.length != 2))
        {
            usage();
            return;
        }
        //-----< STEP 2: read XML file >---------------------------------------
        String descriptionFileName = args[0];
        Element descriptionRootElement = null;
        try
        {
            System.out.println("Reading description file...");
            descriptionRootElement = readDescriptionFile(descriptionFileName);
        }
        catch (Exception e)
        {
            System.out.println("Error reading description file " + descriptionFileName + ": " + e);
            e.printStackTrace(System.out);
            return;
        }
        //-----< STEP 3: read class hierarchy from XML DOM tree >--------------
        ApiDescription odmaClassHierarchy = null;
        try
        {
            System.out.println("Building OpenDMA class hierarchy...");
            odmaClassHierarchy = new ApiDescription(descriptionRootElement);
        }
        catch (Exception e)
        {
            System.out.println("Error loading class hierarchy from description file " + descriptionFileName + ": " + e);
            e.printStackTrace(System.out);
            return;
        }
        //-----< STEP 4: validate the class hierarchy >------------------------
        try
        {
            System.out.println("Validating uniqueness and references...");
            odmaClassHierarchy.checkUniqueness();
            odmaClassHierarchy.checkReferences();
        }
        catch (Exception e)
        {
            System.out.println("Error validating class hierarchy from description file " + descriptionFileName + ": " + e);
            e.printStackTrace(System.out);
            return;
        }
        //-----< STEP 5: validate the existence of predefined classes >--------
        try
        {
            System.out.println("Validating the existence of a class class and a propertyinfo class...");
            odmaClassHierarchy.checkPredefinedClasses();
        }
        catch (Exception e)
        {
            System.out.println("Error validating class hierarchy from description file " + descriptionFileName + ": " + e);
            e.printStackTrace(System.out);
            return;
        }
        //-----< STEP 6: create API for each programming language >------------
        String outputFolderRoot = args[1];
        List odmaApiWriters = getApiWriters();
        Iterator itOdmaApiWriters = odmaApiWriters.iterator();
        while(itOdmaApiWriters.hasNext())
        {
            OdmaApiWriter apiWriter = (OdmaApiWriter)itOdmaApiWriters.next();
            try
            {
                System.out.println("Writing "+apiWriter.getName()+" API...");
                apiWriter.writeOdmaApi(odmaClassHierarchy, outputFolderRoot);
            }
            catch(Exception e)
            {
                System.out.println("Error executing api writer " + apiWriter.getClass().getName());
                e.printStackTrace(System.out);
                return;
            }
        }
        System.out.println("Successfully done.");
    }

    protected void usage()
    {
        System.out.println("OdmaApiBuilder <descriptionFileName> <outputFolder>");
    }

    protected Element readDescriptionFile(String descriptionFileName) throws ParserConfigurationException, SAXException, IOException
    {
        DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document descriptionDocument = docBuilder.parse(new File(descriptionFileName));
        return descriptionDocument.getDocumentElement();
    }
    
    protected List getApiWriters()
    {
        List result = new ArrayList();
        result.add(new Java14ApiWriter());
        result.add(new Java5ApiWriter());
        result.add(new CsApiWriter());
        result.add(new CppApiWriter());
        result.add(new PhpApiWriter());
        return result;
    }

}