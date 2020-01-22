package to.us.netbrain.passwordlayouts

import org.junit.Assert.assertEquals
import org.junit.Assert.assertThat
import org.junit.Test
import java.lang.reflect.Array

import java.util.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class PasswordConverterTest {
    private val converter = RussianToEnglishLayoutConverter()

    @Test
    fun `test russian to english layout conversion`() {
        val inputs = Arrays.asList(
                "тест",
                "Стол!")
        val expected = Arrays.asList(
                "ntcn",
                "Cnjk!")

        for (i in inputs.indices) {
            assertEquals(expected[i], converter.convert(inputs[i]))
        }
    }

    @Test
    fun `test idempotent character`() {
        val inputs = "1234567890!@#$%^&*()/?"

        for (i in inputs.indices) {
            val expected = inputs[i].toString()
            assertEquals(expected, converter.convert(expected))
        }
    }

}
