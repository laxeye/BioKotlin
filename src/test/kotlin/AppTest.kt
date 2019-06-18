package ru.nrcki.bioKotlin

import kotlin.test.*
import kotlin.test.Test
import ru.nrcki.bioKotlin.Fastq
import ru.nrcki.bioKotlin.Fasta
import ru.nrcki.bioKotlin.DNA

class AppTest {
	@Test fun reverseComplementDNA() {
		assertEquals("ATGC",DNA().revComp("GCAT"))
	}
	@Test fun lengthOfSequence() {
		assertEquals(4,Fasta.Record("ID","GCAT").length)
	}
	@Test fun idOfRecord() {
		assertEquals("ID",Fasta.Record("ID [Taxon]","GCAT").id)
	}
	@Test fun headerOfRecord() {
		assertEquals("ID [Taxon]",Fasta.Record("ID [Taxon]","GCAT").header)
	}
	@Test fun sequenceOfRecord() {
		assertEquals("GCAT",Fasta.Record("ID","GCAT").sequence)
	}
	@Test fun stringifyFastq() {
		assertEquals("@ID\nGCAT\n+\nFFFF\n",Fastq.Record("ID","GCAT","FFFF").asFastq())
	}
	@Test fun stringifyFasta() {
		assertEquals(">ID\nGCAT",Fasta.Record("ID","GCAT").asFasta())
	}
	@Test fun readFastqFromFileBufferedReader() {
		assertEquals(Fastq().readAutoBR("src/test/resources/SRR030257_1.head.fq")[0].asFastq(),
			Fastq.Record("SRR030257.1 HWI-EAS_4_PE-FC20GCB:6:1:385:567/1",
				"TTACACTCCTGTTAATCCATACAGCAACAGTATTGG",
				"AAA;A;AA?A?AAAAA?;?A?1A;;????566)=*1").asFastq())
	}
	@Test fun readFastqFromFileScanner() {
		assertEquals(Fastq().readAutoSC("src/test/resources/SRR030257_1.head.fq")[0].asFastq(),
			Fastq.Record("SRR030257.1 HWI-EAS_4_PE-FC20GCB:6:1:385:567/1",
				"TTACACTCCTGTTAATCCATACAGCAACAGTATTGG",
				"AAA;A;AA?A?AAAAA?;?A?1A;;????566)=*1").asFastq())
	}
	@Test fun readFastqFromGZFileBufferedReader() {
		assertEquals(Fastq().readAutoBR("src/test/resources/SRR030257_1.head.fq.gz")[0].asFastq(),
			Fastq.Record("SRR030257.1 HWI-EAS_4_PE-FC20GCB:6:1:385:567/1",
				"TTACACTCCTGTTAATCCATACAGCAACAGTATTGG",
				"AAA;A;AA?A?AAAAA?;?A?1A;;????566)=*1").asFastq())
	}
	@Test fun readFastqFromGZFileScanner() {
		assertEquals(Fastq().readAutoSC("src/test/resources/SRR030257_1.head.fq.gz")[0].asFastq(),
			Fastq.Record("SRR030257.1 HWI-EAS_4_PE-FC20GCB:6:1:385:567/1",
				"TTACACTCCTGTTAATCCATACAGCAACAGTATTGG",
				"AAA;A;AA?A?AAAAA?;?A?1A;;????566)=*1").asFastq())
	}
	@Test fun `check GC content`() {
		assertEquals(0.5,DNA().getGCContent("ACGT"))
	}
	
}