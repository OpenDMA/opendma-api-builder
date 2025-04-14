# Manual validation of generated Rust API

Validation steps:
1. Copy the content of `../../src.generated/rs` somewhere to a folder named `opendma-api`
2. In that folder, run `cargo build`. This will compile the opendma-api into a library
3. Copy the `./opendma-demo` folder next to the `opendma-api` folder
4. In the `opendma-demo` folder run `cargo run`