package ru.nrcki.bioKotlin

import kotlin.test.*
import kotlin.test.Test
import ru.nrcki.bioKotlin.*
import ru.nrcki.bioKotlin.Fasta

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
	@Test fun readFastqFromFile() {
		assertEquals(Fastq().read("src/test/resources/SRR030257_1.head.fq")[0].asFastq(),
			Fastq.Record("SRR030257.1 HWI-EAS_4_PE-FC20GCB:6:1:385:567/1",
				"TTACACTCCTGTTAATCCATACAGCAACAGTATTGG",
				"AAA;A;AA?A?AAAAA?;?A?1A;;????566)=*1").asFastq())
	}
}