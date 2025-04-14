// See https://aka.ms/new-console-template for more information
using OpenDMA.Api;

Console.WriteLine("Welcome to OpenDMA API!");

// OdmaQName
Console.WriteLine("----OdmaQName----");


//OdmaQName qn = new OdmaQName("hello","world");
//OdmaQName qn = new OdmaQName(null,"world");
//OdmaQName qn = new OdmaQName("hello",null);
//OdmaQName qn = new OdmaQName("","world");
//OdmaQName qn = new OdmaQName("hello","");
OdmaQName qn = new OdmaQName("hello", "world");
Console.WriteLine(qn);
OdmaQName qn2 = new OdmaQName("Hello", "world");
OdmaQName qn3 = new OdmaQName("hello", "World");
Console.WriteLine(qn.Equals(qn2));
Console.WriteLine(qn.Equals(qn3));
OdmaQName qn4 = new OdmaQName("hello", "world");
Console.WriteLine(qn.Equals(qn4));

// OdmaId
Console.WriteLine("----OdmaId----");

//OdmaId id = new OdmaId("hello");
//OdmaId id = new OdmaId("");
//OdmaId id = new OdmaId(null);
OdmaId id = new OdmaId("hello");
Console.WriteLine(id);
OdmaId id2 = new OdmaId("Hello");
OdmaId id3 = new OdmaId("hello");
Console.WriteLine(id.Equals(id2));
Console.WriteLine(id.Equals(id3));

// OdmaGuid
Console.WriteLine("----OdmaGuid----");

//OdmaGuid guid = new OdmaGuid(id, id);
//OdmaGuid guid = new OdmaGuid(null, id);
//OdmaGuid guid = new OdmaGuid(id, null);
OdmaGuid guid = new OdmaGuid(id, id);
Console.WriteLine(guid);
OdmaGuid guid2 = new OdmaGuid(new OdmaId("hello"), new OdmaId("hello"));
OdmaGuid guid3 = new OdmaGuid(new OdmaId("Hello"), new OdmaId("hello"));
OdmaGuid guid4 = new OdmaGuid(new OdmaId("hello"), new OdmaId("Hello"));
Console.WriteLine(guid.Equals(guid2));
Console.WriteLine(guid.Equals(guid3));
Console.WriteLine(guid.Equals(guid4));

// OdmaPropertyImpl
Console.WriteLine("----OdmaPropertyImpl----");

OdmaProperty piString = new OdmaProperty(qn, "hello", OdmaType.STRING, false, false);
OdmaProperty piInteger = new OdmaProperty(qn, 123, OdmaType.INTEGER, false, false);
OdmaProperty piDouble = new OdmaProperty(qn, 1.23, OdmaType.DOUBLE, false, false);
OdmaProperty piDate = new OdmaProperty(qn, new DateTime(), OdmaType.DATETIME, false, false);
Console.WriteLine(piString.Value);
Console.WriteLine(piString.IsMultiValue);
Console.WriteLine(piString.IsReadOnly);
Console.WriteLine(piString.IsDirty);
Console.WriteLine(piString.GetString());
//Console.WriteLine(piString.GetInteger());
Console.WriteLine(piInteger.Value);
Console.WriteLine(piInteger.GetInteger());
//Console.WriteLine(piInteger.GetString());
Console.WriteLine(piDouble.Value);
Console.WriteLine(piDate.Value);
Console.WriteLine(piString.IsDirty);
piString.Value = "world";
Console.WriteLine(piString.IsDirty);
Console.WriteLine(piString.Value);
piString.Value = null;
Console.WriteLine(piString.Value);
OdmaProperty piStringRO = new OdmaProperty(qn, "hello", OdmaType.STRING, false, true);
Console.WriteLine(piStringRO.Value);
Console.WriteLine(piStringRO.IsReadOnly);
//piStringRO.Value = "world";
//OdmaProperty piIntFailing = new OdmaProperty(qn, "hello", OdmaType.INTEGER, false, false);
//OdmaProperty piStringMV = new OdmaProperty(qn, "hello", OdmaType.STRING, true, false);
OdmaProperty piStringMV = new OdmaProperty(qn, new List<string>(), OdmaType.STRING, true, false);
Console.WriteLine(piStringMV.IsMultiValue);
Console.WriteLine(piStringMV.IsReadOnly);
