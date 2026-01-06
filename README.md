# OpenDMA API Builder

This Java tool reads the descriptions of abstract API objects from an XML
file and generates APIs for various programming languages.

It currently supports:
- Java
- C#
- Python
- PHP
- JavaScript
- TypeScript
- Go
- Rust
- Swift

## What is OpenDMA

At it's core, OpenDMA (Open Document Management Architecture) defines an
abstract model for Document Management and Enterprise Content Management
Systems.

The OpenDMA API provides an abstract interface to this model. It allows
to write content centric applications once, and run them against multiple
different Enterprise Content and Document Management Systems.

The API is available for different programming languages and enforces a
consistent style across these languages.

## Usage

Build this project with `mvn clean package` and make sure it builds
without any errors.

You can run the code generation with `mvn exec:java`. This will place
the generated APIs in `./src.generated`.

## License

This project contains two kinds of content under different licenses:

- **Code Generator** — GPL v3.0  
  The Java code under `/src/main/java` and the relevant build files are licensed under the GNU General Public License v3.0.

- **Templates** — Apache License 2.0  
  Files under `/src/main/resources` as well as `/OdmaApiDescription*.xml` are licensed under the Apache License 2.0.  
  These templates are partially or fully copied into generated API code. Consequently, the generated APIs are licensed under the Apache License 2.0.

This dual-license structure ensures that while the generator remains open under the GPL, the generated API code can be distributed under a permissive license.