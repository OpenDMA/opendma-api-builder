package test;

import java.util.ArrayList;

import org.opendma.api.OdmaGuid;
import org.opendma.api.OdmaId;
import org.opendma.api.OdmaQName;
import org.opendma.api.OdmaType;
import org.opendma.impl.OdmaPropertyImpl;

public class Test {

	public static void main(String[] args) throws Exception {
	    
	    System.out.println("Welcome to OpenDMA API!");
		
		// OdmaQName
        System.out.println("----OdmaQName----");
		
		//OdmaQName qn = new OdmaQName(null,"world");
		//OdmaQName qn = new OdmaQName("hello",null);
		//OdmaQName qn = new OdmaQName("","world");
		//OdmaQName qn = new OdmaQName("hello","");
		OdmaQName qn = new OdmaQName("hello","world");
		System.out.println(qn);
		OdmaQName qn2 = new OdmaQName("Hello","world");
		OdmaQName qn3 = new OdmaQName("hello","World");
		System.out.println(qn.equals(qn2));
		System.out.println(qn.equals(qn3));
		OdmaQName qn4 = new OdmaQName("hello","world");
		System.out.println(qn.equals(qn4));
		
		// OdmaId
        System.out.println("----OdmaId----");
		
		//OdmaId id = new OdmaId("");
		//OdmaId id = new OdmaId(null);
		OdmaId id = new OdmaId("hello");
		System.out.println(id);
		OdmaId id2 = new OdmaId("Hello");
		OdmaId id3 = new OdmaId("hello");
		System.out.println(id.equals(id2));
		System.out.println(id.equals(id3));
		
		// OdmaGuid
        System.out.println("----OdmaGuid----");
		
		//OdmaGuid guid = new OdmaGuid(null, id);
		//OdmaGuid guid = new OdmaGuid(id, null);
		OdmaGuid guid = new OdmaGuid(id, id);
		System.out.println(guid);
		OdmaGuid guid2 = new OdmaGuid(new OdmaId("hello"), new OdmaId("hello"));
		OdmaGuid guid3 = new OdmaGuid(new OdmaId("Hello"), new OdmaId("hello"));
		OdmaGuid guid4 = new OdmaGuid(new OdmaId("hello"), new OdmaId("Hello"));
		System.out.println(guid.equals(guid2));
		System.out.println(guid.equals(guid3));
		System.out.println(guid.equals(guid4));
		
		// OdmaPropertyImpl
        System.out.println("----OdmaPropertyImpl----");
		
		OdmaPropertyImpl piString = new OdmaPropertyImpl(qn, "hello", OdmaType.STRING, false, false);
		OdmaPropertyImpl piInteger = new OdmaPropertyImpl(qn, 123, OdmaType.INTEGER, false, false);
		OdmaPropertyImpl piDouble = new OdmaPropertyImpl(qn, 1.23, OdmaType.DOUBLE, false, false);
		OdmaPropertyImpl piDate = new OdmaPropertyImpl(qn, new java.util.Date(), OdmaType.DATETIME, false, false);
		System.out.println(piString.getValue());
		System.out.println(piString.isMultiValue());
		System.out.println(piString.isReadOnly());
		System.out.println(piString.isDirty());
		System.out.println(piString.getString());
		//System.out.println(piString.getInteger());
		System.out.println(piInteger.getValue());
		System.out.println(piInteger.getInteger());
		//System.out.println(piInteger.getString());
		System.out.println(piDouble.getValue());
		System.out.println(piDate.getValue());
		System.out.println(piString.isDirty());
		piString.setValue("world");
		System.out.println(piString.isDirty());
		System.out.println(piString.getValue());
		piString.setValue(null);
		System.out.println(piString.getValue());
		OdmaPropertyImpl piStringRO = new OdmaPropertyImpl(qn, "hello", OdmaType.STRING, false, true);
		System.out.println(piStringRO.getValue());
		System.out.println(piStringRO.isReadOnly());
		//piStringRO.setValue("world");
		//OdmaPropertyImpl piIntFailing = new OdmaPropertyImpl(qn, "hello", OdmaType.INTEGER, false, false);
		//OdmaPropertyImpl piStringMV = new OdmaPropertyImpl(qn, "hello", OdmaType.STRING, true, false);
		OdmaPropertyImpl piStringMV = new OdmaPropertyImpl(qn, new ArrayList<String>(), OdmaType.STRING, true, false);
		System.out.println(piStringMV.isMultiValue());
		System.out.println(piStringMV.isReadOnly());
	}

}
