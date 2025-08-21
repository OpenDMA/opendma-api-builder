# Manual validation of generated C# API

Validation steps:
1. Open the folder `../../src.generated/cs` in VisualStudio
2. Build the project
3. Copy the folder `./Test` to  `../../src.generated/cs`
4. In VisualStudio, add existing project to solution, select `./Test/Test.csproj`
5. Select "Test" project as "Startup Project" and Build
6. Run the "Test" program