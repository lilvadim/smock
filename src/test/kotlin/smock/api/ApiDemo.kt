package smock.api

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.lang.Exception
import kotlin.test.assertEquals
import kotlin.test.assertNull

class ApiDemo() {
    @Smocked
    lateinit var someObj: Any

    @Test
    fun demo() {
        val mockedObj = smock<List<String>>()

        every { mockedObj[0] } returns "SomeValue"
        every { mockedObj[3] } returns "SomeValue 3"

        var answerCallbackCnt = 0
        every { mockedObj.toString() } answers {
            answerCallbackCnt++
            "To String Value"
        }

        every { mockedObj.size } throws Exception("Size Exception")

        assertEquals("SomeValue", mockedObj[0])
        assertEquals("SomeValue 3", mockedObj[3])

        assertEquals("To String Value", mockedObj.toString())
        mockedObj.toString()

        assertEquals(2, answerCallbackCnt)

        assertThrows<Exception>("Size Exception") { mockedObj.size }

        assertNull(mockedObj[100])
    }
}