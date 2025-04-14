// swift-tools-version: 6.1

import PackageDescription

let package = Package(
    name: "opendma-demo",
    dependencies: [
        .package(name: "OpenDMA", path: "../opendma-api"),
    ],
    targets: [
        .executableTarget(name: "opendma-demo", dependencies: ["OpenDMA"]),
    ]
)