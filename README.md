# BioKotlin

[Kotlin programming language](https://kotlinlang.org/) realisation of bioinformatics routine operations and file handling.
FASTA, FASTQ and GFF basics are available. 
There is a genome information module to collect some useful stats like n50, GC% etc.

## Compilation

For sure You need a Kotlin compiler (>= 1.2 was tested). 

To compile multiple files in one jar file use the following command:

`kotlinc -d biokotlin.jar src/ru/nrcki/bioKotlin/*`

If You plan to use it with any JVM language but Kotlin don't forget **-include-runtime** compiler flag.

`kotlinc -include-runtime -d biokotlin.jar src/ru/nrcki/bioKotlin/*`


## Tests

We plan to include more test files, now You can check only very basic things like fasta and fastq handling.

To compile BioKotlin with tests use following:

`kotlinc -include-runtime -d biokotlin.jar src/ru/nrcki/* src/test/kotlin/test.kt`

Tests:

`java -cp "biokotlin.jar" ru.nrcki.bioKotlin.TestKt fasta src/test/resources/antiTGFPv1M13F.fasta`

`java -cp "biokotlin.jar" ru.nrcki.bioKotlin.TestKt fastq src/test/resources/SRR030257_1.head.fq`


## Project structure

- all classes should be inside src/ru/nrcki/bioKotlin (to provide compatibility with build tools (ant, maven, gradle) and IDE)
- all tests located in src/test/kotlin
- all test data located in src/test/resources

#### Package declaration in .kt for an inattentive guy

- all files should begin with *package ru.nrcki.bioKotlin*
- package names should be in lowercase
- package names don't include names of classes
