# Dependency Resolver

This program resolves basic dependencies of files and outputs them in such order that all dependency files are shown
before dependent ones.

## Dependency format

This program detects dependencies from strings situated on their own line in the following format:

```
require '<filename_relative_to_root>'
```

## Running

The program will ask you to enter the root path.