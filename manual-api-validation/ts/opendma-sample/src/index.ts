import { OdmaQName, OdmaId, OdmaGuid, OdmaType, OdmaPropertyImpl } from '@opendma/api';

console.log("Welcome to OpenDMA API!");

// OdmaQName
console.log("----OdmaQName----");

//const qn = new OdmaQName(null,"world");
//const qn = new OdmaQName("hello",null);
//const qn = new OdmaQName("","world");
//const qn = new OdmaQName("hello","");
const qn = new OdmaQName("hello","world");
console.log(qn);
console.log(qn.toString());
const qn2 = new OdmaQName("Hello","world");
const qn3 = new OdmaQName("hello","World");
console.log(qn.equals(qn2));
console.log(qn.equals(qn3));
const qn4 = new OdmaQName("hello","world");
console.log(qn.equals(qn4));

// OdmaId
console.log("----OdmaId----");

//const id = new OdmaId("");
//const id = new OdmaId(null);
const id = new OdmaId("hello");
console.log(id);
console.log(id.toString());
const id2 = new OdmaId("Hello");
const id3 = new OdmaId("hello");
console.log(id.equals(id2));
console.log(id.equals(id3));

// OdmaGuid
console.log("----OdmaGuid----");

//const guid = new OdmaGuid(null, id);
//const guid = new OdmaGuid(id, null);
const guid = new OdmaGuid(id, id);
console.log(guid);
console.log(guid.toString());
const guid2 = new OdmaGuid(new OdmaId("hello"), new OdmaId("hello"));
const guid3 = new OdmaGuid(new OdmaId("Hello"), new OdmaId("hello"));
const guid4 = new OdmaGuid(new OdmaId("hello"), new OdmaId("Hello"));
console.log(guid.equals(guid2));
console.log(guid.equals(guid3));
console.log(guid.equals(guid4));

// OdmaPropertyImpl
console.log("----OdmaPropertyImpl----");

const piString = new OdmaPropertyImpl(qn, "hello", OdmaType.STRING, false, false);
const piInteger = new OdmaPropertyImpl(qn, 123, OdmaType.INTEGER, false, false);
const piDouble = new OdmaPropertyImpl(qn, 1.23, OdmaType.DOUBLE, false, false);
const piDate = new OdmaPropertyImpl(qn, new Date(), OdmaType.DATETIME, false, false);
console.log(piString.getValue());
console.log(piString.isMultiValue());
console.log(piString.isReadOnly());
console.log(piString.isDirty());
console.log(piString.getString());
//console.log(piString.getInteger());
console.log(piInteger.getValue());
console.log(piInteger.getInteger());
//console.log(piInteger.getString());
console.log(piDouble.getValue());
console.log(piDate.getValue());
console.log(piString.isDirty());
piString.setValue("world");
console.log(piString.isDirty());
console.log(piString.getValue());
piString.setValue(null);
console.log(piString.getValue());
const piStringRO = new OdmaPropertyImpl(qn, "hello", OdmaType.STRING, false, true);
console.log(piStringRO.getValue());
console.log(piStringRO.isReadOnly());
//piStringRO.setValue("world");
//const piIntFailing = new OdmaPropertyImpl(qn, "hello", OdmaType.INTEGER, false, false);
//const piStringMVFailing = new OdmaPropertyImpl(qn, "hello", OdmaType.STRING, true, false);

const piStringMV = new OdmaPropertyImpl(qn, new Array(), OdmaType.STRING, true, false);
console.log(piStringMV.isMultiValue());
console.log(piStringMV.isReadOnly());
