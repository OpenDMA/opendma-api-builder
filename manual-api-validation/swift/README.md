# Manual validation of generated Rust API

Validation steps:
1. In the folder `../../src.generated/swift/opendma-api`, run `swift build`
2. Copy the `./opendma-demo` folder next to the `opendma-api` folder
3. In the `opendma-demo` folder run `swift build` and then `swift run`