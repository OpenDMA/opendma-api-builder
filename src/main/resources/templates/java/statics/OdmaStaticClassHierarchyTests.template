package org.opendma.impl.core;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Iterator;
import java.util.List;

import org.junit.Test;
import org.opendma.api.OdmaClass;
import org.opendma.api.OdmaId;
import org.opendma.api.OdmaObject;
import org.opendma.api.OdmaPropertyInfo;
import org.opendma.api.OdmaRepository;
import org.opendma.exceptions.OdmaAccessDeniedException;
import org.opendma.exceptions.OdmaInvalidDataTypeException;
import org.opendma.tck.OdmaTechnologyCompatibilityKit;

public class OdmaStaticClassHierarchyTests {

    @Test
    public void testHierarchy() throws OdmaInvalidDataTypeException, OdmaAccessDeniedException {
    	OdmaId repoId = new OdmaId("repoid");
        OdmaId repoObjectId = new OdmaId("repoobjectid");
    	OdmaStaticClassHierarchy hierarchy = new OdmaStaticClassHierarchy("repo-name","Repo display name",repoId,repoObjectId);
    	for(OdmaObject test : hierarchy.getAllObjectsById().values()) {
    	    boolean verified = false;
    		if(test instanceof OdmaRepository) {
    			List<String> failures = OdmaTechnologyCompatibilityKit.verifyOdmaRepository(test);
    			assertTrue("Failed verifying object of type OdmaRepository:\n"+joinStrings(",\n", failures), failures.isEmpty());
                verified = true;
    		}
    		if(test instanceof OdmaPropertyInfo) {
    		    assertFalse("Object implements complimentary class interfaces: "+test.getId(), verified);
    			List<String> failures = OdmaTechnologyCompatibilityKit.verifyOdmaPropertyInfo(test);
    			assertTrue("Failed verifying object of type OdmaPropertyInfo:\n"+joinStrings(",\n", failures), failures.isEmpty());
                verified = true;
    		}
    		if(test instanceof OdmaClass) {
                assertFalse("Object implements complimentary class interfaces: "+test.getId(), verified);
    			List<String> failures = OdmaTechnologyCompatibilityKit.verifyOdmaClass(test);
    			assertTrue("Failed verifying object of type OdmaClass:\n"+joinStrings(",\n", failures), failures.isEmpty());
                verified = true;
    		}
            assertTrue("Object does not implement OdmaRepository, OdmaPropertyInfo or OdmaClass: "+test.getId(), verified);
    	}
    }
    
    private static String joinStrings(String separator, List<String> lines) {
        StringBuilder sb = new StringBuilder();
        Iterator<String> it = lines.iterator();
        while(it.hasNext()) {
            sb.append(it.next());
            if(it.hasNext()) {
                sb.append(separator);
            }
        }
        return sb.toString();
    }

}
