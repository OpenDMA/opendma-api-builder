from opendma.api import OdmaQName

qn = OdmaQName("hello","world")
qnX = OdmaQName("","world")
qnY = OdmaQName("hello","")
print(qn)
qn2 = OdmaQName("Hello","world")
qn3 = OdmaQName("hello","World")
qn4 = OdmaQName("hello","world")
qn == qn2
qn == qn3
qn == qn4

from opendma.api import OdmaId

id = OdmaId("hello")
print(id)
idX = OdmaId("")
id2 = OdmaId("Hello")
id3 = OdmaId("hello")
id == id2
id == id3

from opendma.api import OdmaGuid

guid = OdmaGuid(id, id)
print(guid)
guidX = OdmaGuid(None, id)
guidY = OdmaGuid(id, None)
guid2 = OdmaGuid(OdmaId("hello"), OdmaId("hello"))
guid3 = OdmaGuid(OdmaId("Hello"), OdmaId("hello"))
guid4 = OdmaGuid(OdmaId("hello"), OdmaId("Hello"))
guid == guid2
guid == guid3
guid == guid4

from opendma.api import OdmaType
from opendma.api import OdmaPropertyImpl
from datetime import datetime

piString = OdmaPropertyImpl(qn, "hello", OdmaType.STRING, False, False)
piInteger = OdmaPropertyImpl(qn, 123, OdmaType.INTEGER, False, False)
piDouble = OdmaPropertyImpl(qn, 1.23, OdmaType.DOUBLE, False, False)
piDate = OdmaPropertyImpl(qn, datetime.today(), OdmaType.DATETIME, False, False)

print(piString.get_value())
print(piString.is_multi_value())
print(piString.is_read_only())
print(piString.is_dirty())
print(piString.get_string())
print(piString.get_integer())
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
