import Foundation
import OpenDMA

print("Welcome to OpenDMA API!")

// MARK: - OdmaQName
print("----OdmaQName----")

do {
    // let qn = try OdmaQName(namespace: nil, name: "world") ? not allowed in Swift
    // let qn = try OdmaQName(namespace: "", name: "world")
    // let qn = try OdmaQName(namespace: "hello", name: "")

    let qn = try OdmaQName(namespace: "hello", name: "world")
    print(qn)

    let qn2 = try OdmaQName(namespace: "Hello", name: "world")
    let qn3 = try OdmaQName(namespace: "hello", name: "World")

    print(qn == qn2) // false
    print(qn == qn3) // false

    let qn4 = try OdmaQName(namespace: "hello", name: "world")
    print(qn == qn4) // true

    // MARK: - OdmaId
    print("----OdmaId----")

    // let id = try OdmaId(id: "") // invalid
    let id = try OdmaId(id: "hello")
    print(id)

    let id2 = try OdmaId(id: "Hello")
    let id3 = try OdmaId(id: "hello")

    print(id == id2) // false
    print(id == id3) // true

    // MARK: - OdmaGuid
    print("----OdmaGuid----")

    let guid = OdmaGuid(objectId: id, repositoryId: id)
    print(guid)

    let guid2 = OdmaGuid(
        objectId: try OdmaId(id: "hello"),
        repositoryId: try OdmaId(id: "hello")
    )

    let guid3 = OdmaGuid(
        objectId: try OdmaId(id: "Hello"),
        repositoryId: try OdmaId(id: "hello")
    )

    let guid4 = OdmaGuid(
        objectId: try OdmaId(id: "hello"),
        repositoryId: try OdmaId(id: "Hello")
    )

    print(guid == guid2) // true
    print(guid == guid3) // false
    print(guid == guid4) // false

    // MARK: - OdmaPropertyImpl
    print("----OdmaPropertyImpl----")

    let piString = try OdmaPropertyImpl(name: qn, value: "hello", type: .string, isMultiValue: false, isReadOnly: false)
    let piInteger = try OdmaPropertyImpl(name: qn, value: 123, type: .integer, isMultiValue: false, isReadOnly: false)
    let piDouble = try OdmaPropertyImpl(name: qn, value: 1.23, type: .double, isMultiValue: false, isReadOnly: false)
    let piDate = try OdmaPropertyImpl(name: qn, value: Date(), type: .datetime, isMultiValue: false, isReadOnly: false)

    print(piString.getValue() ?? "<nil>")
    print(piString.isMultiValue)
    print(piString.isReadOnly)
    print(piString.isDirty)
    print(try piString.stringValue() ?? "<nil>")
    // print(try piString.integerValue()) // will throw

    print(piInteger.getValue() ?? "<nil>")
    print(try piInteger.integerValue() ?? "<nil>")
    // print(try piInteger.stringValue()) // will throw

    print(piDouble.getValue() ?? "<nil>")
    print(piDate.getValue() ?? "<nil>")

    print("Dirty (before): \(piString.isDirty)")
    try piString.setValue("world")
    print("Dirty (after): \(piString.isDirty)")
    print(piString.getValue() ?? "<nil>")
    try piString.setValue(nil)
    print(piString.getValue() ?? "<nil>")

    let piStringRO = try OdmaPropertyImpl(name: qn, value: "hello", type: .string, isMultiValue: false, isReadOnly: true)
    print(piStringRO.getValue() ?? "<nil>")
    print(piStringRO.isReadOnly)
    // try piStringRO.setValue("world") // will throw OdmaError.accessDenied

    // let piIntFailing = try OdmaPropertyImpl(name: qn, value: "hello", type: .integer, isMultiValue: false, isReadOnly: false) // will throw
    // let piStringMVFail = try OdmaPropertyImpl(name: qn, value: "hello", type: .string, isMultiValue: true, isReadOnly: false) // will throw

    let piStringMV = try OdmaPropertyImpl(name: qn, value: [String](), type: .string, isMultiValue: true, isReadOnly: false)
    print(piStringMV.isMultiValue)
    print(piStringMV.isReadOnly)
    print(piStringMV.getValue() ?? "<nil>")

} catch {
    print("Caught error: \(error)")
}