# BioKotlin

[Kotlin programming language](https://kotlinlang.org/) realisation of bioinformatics routine operations and file handling.
FASTA, FASTQ and GFF basics are available. 
There is a genome information module to collect some useful stats like n50, GC% etc.

## Compilation

Use Gradle to build the project.


## Tests

We plan to include more test files, now You can check only very basic things like fasta and fastq handling.

To compile BioKotlin with tests use following:

`kotlinc -include-runtime -d biokotlin.jar src/ru/nrcki/* src/test/kotlin/test.kt`

Tests:

`java -cp "biokotlin.jar" ru.nrcki.bioKotlin.TestKt fasta src/test/resources/antiTGFPv1M13F.fasta`

`java -cp "biokotlin.jar" ru.nrcki.bioKotlin.TestKt fastq src/test/resources/SRR030257_1.head.fq`


## Project structure

- source src/main/kotlin/ru/nrcki/bioKotlin
- tests src/test/kotlin
- test data src/test/resources
