package com.turbosokol.mypurchases.android.common.utils

fun validateCoastsFormat(text: String): String {
    /***
     * . - any symbol
     * [] - any from entered or entered range ([1,2,3,4][1-4])
     * $ - end of a line ($d - d only in cases it's the end of a line)
     * ^ - start of a line
     * \ - shielding of a symbol (\.)
     * \d - any number
     * \D - any but not number
     * \s - space
     * \S - any but not spaces
     * \w - char
     * \W - any but not char
     * \b - word border (\b...\b - word included 3 any symbols)
     * \B - any border but not after or before word
     * n{4} - search cases when n symbol repeats 4 times (gnnnn)
     * n{0,64} - search any cases when n symbol repeats from 0 to 64 times (gn and gnnnnnnn)
     * * - from 0 and above time (n{*} - n{0, infinity})
     * + - from 1 and above times
     * ? - 0 or 1 times
     * (x|n) - or
     *
     * regex101.com - playground
     */
    val validateRegexPattern = """(\d{0,64}[\.,\,]?\d{0,9})""".toRegex()
    return validateRegexPattern.find(text)?.value.toString()
}