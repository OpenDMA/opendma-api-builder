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

piString = OdmaPropertyImpl(qn, "hello", None, OdmaType.STRING, False, False)
piInteger = OdmaPropertyImpl(qn, 123, None, OdmaType.INTEGER, False, False)
piDouble = OdmaPropertyImpl(qn, 1.23, None, OdmaType.DOUBLE, False, False)
piDate = OdmaPropertyImpl(qn, datetime.today(), None, OdmaType.DATETIME, False, False)

print(piString.get_value())
print(piString.is_multi_value())
print(piString.is_read_only())
print(piString.is_dirty())
print(piString.get_string())
print(piString.get_integer())
print(piInteger.get_value())
print(piInteger.get_integer())
print(piInteger.get_string())
print(piDouble.get_value())
print(piDate.get_value())
print(piString.is_dirty())
piString.set_value("world")
print(piString.is_dirty())
print(piString.get_value())
piString.set_value(None)
print(piString.get_value())

piStringRO = OdmaPropertyImpl(qn, "hello", None, OdmaType.STRING, False, True)
print(piStringRO.get_value())
print(piStringRO.is_read_only())
piStringRO.set_value("world")

piIntFailing = OdmaPropertyImpl(qn, "hello", None, OdmaType.INTEGER, False, False)

piStringMVFailing = OdmaPropertyImpl(qn, "hello", None, OdmaType.STRING, True, False)

piStringMV = OdmaPropertyImpl(qn, [], None, OdmaType.STRING, True, False)
print(piStringMV.is_multi_value())
print(piStringMV.is_read_only())