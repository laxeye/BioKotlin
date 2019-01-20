package ru.nrcki.bioKotlin

class Fasta(var title: String = "", var sequence: String = ""){
    //TO DO: write down secondary constructor (init all fields during creation)

    fun parseList(file: List<String>){
        title = file.get(0).substring(1, file.get(0).length)
        sequence = file.get(1)
    }

    fun printContent(){
        println("Title is: " + title)
        println("Sequence is: " + sequence)
    }
}