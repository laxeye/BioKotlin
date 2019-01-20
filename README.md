# BioKotlin
Kotlin realisation of bioinformatics routine operations and file handling.

### Some info

To compile multiple files in one jar file use the following code:
Из папки BioKotlin
kotlinc ./src/ru/nrcki/bioKotlin/Class1.kt ./src/ru/nrcki/bioKotlin/Class2.kt -include-runtime -d test.jar

### Структура проекта
-все классы находятся в папке src/ru/nrcki/bioKotlin (такая иерархия с директориями позволит подключать среды разработки [надеюсь проект когда-нибудь разрастётся до таких масштабов] и автоматические сборщики [ant, maven, gradle])

Декларация пакетов в файлах *.kt:
-шаблон - package ru.nrcki.bioKotlin
-названия пакетов только с маленькой буквы
-пакеты НЕ включают названия файлов/классов (только директории)
