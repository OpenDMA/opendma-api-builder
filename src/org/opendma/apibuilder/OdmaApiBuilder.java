package org.opendma.apibuilder;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.opendma.apibuilder.apiwriter.ApiWriterException;
import org.opendma.apibuilder.apiwriter.cpp.CppApiWriter;
import org.opendma.apibuilder.apiwriter.cs.CsApiWriter;
import org.opendma.apibuilder.apiwriter.go.GoApiWriter;
import org.opendma.apibuilder.apiwriter.java.JavaApiWriter;
import org.opendma.apibuilder.apiwriter.js.JavaScriptApiWriter;
import org.opendma.apibuilder.apiwriter.php.PhpApiWriter;
import org.opendma.apibuilder.apiwriter.py.PythonApiWriter;
import org.opendma.apibuilder.apiwriter.rust.RustApiWriter;
import org.opendma.apibuilder.apiwriter.swift.SwiftApiWriter;
import org.opendma.apibuilder.apiwriter.ts.TypeScriptApiWriter;
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
        List<OdmaApiWriter> odmaApiWriters;
        try
        {
            odmaApiWriters = getApiWriters(outputFolderRoot);
        }
        catch (ApiWriterException e)
        {
            System.out.println("Error preparing api writer");
            e.printStackTrace(System.out);
            return;
        }
        Iterator<OdmaApiWriter> itOdmaApiWriters = odmaApiWriters.iterator();
        while(itOdmaApiWriters.hasNext())
        {
            OdmaApiWriter apiWriter = itOdmaApiWriters.next();
            try
            {
                System.out.println("Writing "+apiWriter.getName()+" API...");
                apiWriter.writeOdmaApi(odmaClassHierarchy);
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
    
    protected List<OdmaApiWriter> getApiWriters(String outputFolderRoot) throws ApiWriterException
    {
        File outputFolderRootFile = new File(outputFolderRoot);
        if(!outputFolderRootFile.isDirectory())
        {
            throw new ApiWriterException("The output folder '"+outputFolderRoot+"' does not exist or is not a directory.");
        }
        List<OdmaApiWriter> result = new ArrayList<OdmaApiWriter>();
        result.add(new JavaApiWriter(outputFolderRootFile));
        result.add(new CsApiWriter(outputFolderRootFile));
        result.add(new CppApiWriter(outputFolderRootFile));
        result.add(new PhpApiWriter(outputFolderRootFile));
        result.add(new PythonApiWriter(outputFolderRootFile));
        result.add(new JavaScriptApiWriter(outputFolderRootFile));
        result.add(new TypeScriptApiWriter(outputFolderRootFile));
        result.add(new GoApiWriter(outputFolderRootFile));
        result.add(new RustApiWriter(outputFolderRootFile));
        result.add(new SwiftApiWriter(outputFolderRootFile));
        //result.add(new Java14ApiWriter(outputFolderRootFile));
        return result;
    }

}