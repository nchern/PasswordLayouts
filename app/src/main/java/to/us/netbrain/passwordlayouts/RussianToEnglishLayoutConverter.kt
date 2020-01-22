package to.us.netbrain.passwordlayouts

private const val ENGLISH_LAYOUT = "qwertyuiop[]asdfghjkl;'\\`zxcvbnm,."
private const val RUSSIAN_LAYOUT = "йцукенгшщзхъфывапролджэё]ячсмитьбю"

class RussianToEnglishLayoutConverter {

    private val map = HashMap<Char, Char>()

    init {
        for (i: Int in RUSSIAN_LAYOUT.indices) {
            map[RUSSIAN_LAYOUT[i]] = ENGLISH_LAYOUT[i]
        }
    }

    fun convert(s: String): String {
        var result = ""

        for (c: Char in s){
            var mapped = map[c.toLowerCase()]
            mapped = mapped?.let { it } ?: c
            if (c.isUpperCase()) {
                mapped = mapped!!.toUpperCase()
            }
            result += mapped
        }

        return result
    }

}

