package ru.nrcki.biokotlin.parsers

import java.io.File

interface ITblOut{
    fun read(filename: String, mode: String): MutableList<CmTbl>
}

/*
Infernal file format:
target name | accession query name | accession mdl | mdl from | mdl to | seq from |
    seq to | strand | trunc | pass | gc | bias | score | E-value | inc | description of target
 */

data class CmTbl(
        val target: String, val accession: String, val query: String, val model: String,
        val model_from: Int, val model_to: Int, val seq_from: Int, val seq_to: Int,
        val strand: Char, val trunc: String, val pass: Int, val gc: Float,
        val bias: Float, val score: Float, val evalue: Float, val inc: Char,
        val description: String
)

class TblOut : ITblOut {
    override fun read(filename: String, mode: String): MutableList<CmTbl> {
        println(filename)
        val tempCmTbl = mutableListOf<CmTbl>()
        if(mode == "cmscan"){
            println("Reading cmscan...")
            File(filename).bufferedReader().readLines().forEach(){line ->
                val objs = line.split(" ")
                tempCmTbl.add( CmTbl(objs[0], objs[2], objs[3], objs[4],
                        objs[5].toInt(), objs[6].toInt(), objs[7].toInt(),
                        objs[8].toInt(), objs[9][0], objs[10], objs[11].toInt(),
                        objs[12].toFloat(), objs[13].toFloat(), objs[14].toFloat(),
                        objs[15].toFloat(), objs[16][0], objs[17]) )
            }
        }
        return tempCmTbl
    }
}
