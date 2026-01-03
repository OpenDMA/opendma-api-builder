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
