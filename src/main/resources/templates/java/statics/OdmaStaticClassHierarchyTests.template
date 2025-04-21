package org.opendma.impl.core;

import static org.junit.Assert.assertTrue;

import java.util.Iterator;
import java.util.List;

import org.junit.Test;
import org.opendma.api.OdmaClass;
import org.opendma.api.OdmaGuid;
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
    	OdmaGuid repoGuid = new OdmaGuid(repoId,repoId);
    	OdmaStaticClassHierarchy hierarchy = new OdmaStaticClassHierarchy("repo-name","Repo display name",repoId,repoGuid);
    	for(OdmaObject test : hierarchy.getAllObjectsById().values()) {
    		if(test instanceof OdmaRepository) {
    			List<String> failures = OdmaTechnologyCompatibilityKit.verifyOdmaRepository(test);
    			assertTrue("Failed verifying object of type OdmaRepository: "+joinStrings(", ", failures), failures.isEmpty());
    		}
    		if(test instanceof OdmaPropertyInfo) {
    			List<String> failures = OdmaTechnologyCompatibilityKit.verifyOdmaPropertyInfo(test);
    			assertTrue("Failed verifying object of type OdmaPropertyInfo: "+joinStrings(", ", failures), failures.isEmpty());
    		}
    		if(test instanceof OdmaClass) {
    			List<String> failures = OdmaTechnologyCompatibilityKit.verifyOdmaClass(test);
    			assertTrue("Failed verifying object of type OdmaClass: "+joinStrings(", ", failures), failures.isEmpty());
    		}
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
