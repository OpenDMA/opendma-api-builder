package main

import "github.com/opendma/opendma-api-go"
import "fmt"
import "log"
import "time"

func main() {

    fmt.Println("Welcome to OpenDMA API!")

    // OdmaQName
    fmt.Println("----OdmaQName----");

    //qn, err := OpenDMAApi.NewOdmaQName(nil,"world");
    //qn, err := OpenDMAApi.NewOdmaQName("hello",nil);
    //qn, err := OpenDMAApi.NewOdmaQName("","world");
    //qn, err := OpenDMAApi.NewOdmaQName("hello","");
    qn, err := OpenDMAApi.NewOdmaQName("hello", "world");
    if err != nil {
        log.Fatal(err)
    }
    fmt.Println(qn);
    qn2, err := OpenDMAApi.NewOdmaQName("Hello", "world");
    if err != nil {
        log.Fatal(err)
    }
    qn3, err := OpenDMAApi.NewOdmaQName("hello", "World");
    if err != nil {
        log.Fatal(err)
    }
    fmt.Println(qn.Equals(qn2));
    fmt.Println(qn.Equals(qn3));
    qn4, err := OpenDMAApi.NewOdmaQName("hello", "world");
    if err != nil {
        log.Fatal(err)
    }
    fmt.Println(qn.Equals(qn4));

    // OdmaId
    fmt.Println("----OdmaId----");

    //id, err := OpenDMAApi.NewOdmaId("");
    //id, err := OpenDMAApi.NewOdmaId(nil);
    id, err := OpenDMAApi.NewOdmaId("hello");
    if err != nil {
        log.Fatal(err)
    }
    fmt.Println(id);
    id2, err := OpenDMAApi.NewOdmaId("Hello");
    if err != nil {
        log.Fatal(err)
    }
    id3, err := OpenDMAApi.NewOdmaId("hello");
    if err != nil {
        log.Fatal(err)
    }
    fmt.Println(id.Equals(id2));
    fmt.Println(id.Equals(id3));

    // OdmaGuid
    fmt.Println("----OdmaGuid----");

    //guid, err := OpenDMAApi.NewOdmaGuid(nil, id);
    //guid, err := OpenDMAApi.NewOdmaGuid(id, nil);
    guid, err := OpenDMAApi.NewOdmaGuid(id, id)
    if err != nil {
        log.Fatal(err)
    }
    fmt.Println(guid);
    helloId, err := OpenDMAApi.NewOdmaId("hello")
    if err != nil {
        log.Fatal(err)
    }
    HelloId, err := OpenDMAApi.NewOdmaId("Hello")
    if err != nil {
        log.Fatal(err)
    }
    guid2, err := OpenDMAApi.NewOdmaGuid(helloId, helloId);
    if err != nil {
        log.Fatal(err)
    }
    guid3, err := OpenDMAApi.NewOdmaGuid(HelloId, helloId);
    if err != nil {
        log.Fatal(err)
    }
    guid4, err := OpenDMAApi.NewOdmaGuid(helloId, HelloId);
    if err != nil {
        log.Fatal(err)
    }
    fmt.Println(guid.Equals(guid2));
    fmt.Println(guid.Equals(guid3));
    fmt.Println(guid.Equals(guid4));

    // OdmaPropertyImpl
    fmt.Println("----OdmaPropertyImpl----");

    piString, err := OpenDMAApi.NewOdmaPropertyImpl(qn, "hello", OpenDMAApi.STRING, false, false)
    if err != nil {
        log.Fatal(err)
    }
    piInteger, err := OpenDMAApi.NewOdmaPropertyImpl(qn, 123, OpenDMAApi.INTEGER, false, false)
    if err != nil {
        log.Fatal(err)
    }
    piDouble, err := OpenDMAApi.NewOdmaPropertyImpl(qn, 1.23, OpenDMAApi.DOUBLE, false, false)
    if err != nil {
        log.Fatal(err)
    }
    piDate, err := OpenDMAApi.NewOdmaPropertyImpl(qn, time.Now(), OpenDMAApi.DATETIME, false, false)
    if err != nil {
        log.Fatal(err)
    }
    fmt.Println(piString.GetValue());
    fmt.Println(piString.IsMultiValue());
    fmt.Println(piString.IsReadOnly());
    fmt.Println(piString.IsDirty());
    s, err := piString.GetString()
    if err != nil {
        log.Fatal(err)
    }
    fmt.Println(s);
    _, err = piString.GetInteger()
    if err != nil {
        fmt.Println(err)
    }
    fmt.Println(piInteger.GetValue());
    i, err := piInteger.GetInteger()
    if err != nil {
        log.Fatal(err)
    }
    fmt.Println(i);
    _, err = piInteger.GetString()
    if err != nil {
        fmt.Println(err)
    }
    fmt.Println(piDouble.GetValue());
    fmt.Println(piDate.GetValue());
    fmt.Println("---");
    fmt.Println(piString.IsDirty());
    piString.SetValue("world");
    fmt.Println(piString.IsDirty());
    fmt.Println(piString.GetValue());
    piString.SetValue(nil);
    fmt.Println(piString.GetValue());
    fmt.Println("---");
    piStringRO, err := OpenDMAApi.NewOdmaPropertyImpl(qn, "hello", OpenDMAApi.STRING, false, true)
    if err != nil {
        log.Fatal(err)
    }
    fmt.Println(piStringRO.GetValue());
    fmt.Println(piStringRO.IsReadOnly());
    err = piStringRO.SetValue("world");
    if err != nil {
        fmt.Println(err)
    }
    fmt.Println(piStringRO.GetValue());
    fmt.Println("---");
    _, err = OpenDMAApi.NewOdmaPropertyImpl(qn, "hello", OpenDMAApi.INTEGER, false, false)
    if err != nil {
        fmt.Println(err)
    }
    _, err = OpenDMAApi.NewOdmaPropertyImpl(qn, "hello", OpenDMAApi.STRING, true, false)
    if err != nil {
        fmt.Println(err)
    }
    strSlice := []string{"hello", "world"}
    piStringMV, err := OpenDMAApi.NewOdmaPropertyImpl(qn, strSlice, OpenDMAApi.STRING, true, false)
    if err != nil {
        log.Fatal(err)
    }
    fmt.Println(piStringMV.IsMultiValue());
    fmt.Println(piStringMV.IsReadOnly());
    fmt.Println(piStringMV.GetValue());
    sa, err := piStringMV.GetStringArray()
    if err != nil {
        log.Fatal(err)
    }
    fmt.Println(sa);

}