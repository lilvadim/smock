package smock.api

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import smock.api.annotations.Smocked
import smock.api.annotations.smockAnnotated
import kotlin.test.Ignore
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull

class ApiDemo {
    @Smocked
    lateinit var someObj: List<Double>

    @Test
    fun annotationsDemo() {
        smockAnnotated(this)

        every { someObj.isEmpty() } returns false

        assertFalse { someObj.isEmpty() }
    }

    @Test
    fun smockDemo() {
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

    @Test
    @Ignore
    fun spyDemo() {
        val spiedObj = spy<List<Int>>()

        every { spiedObj[0] } returns 1

        assertEquals(1, spiedObj[0])

        assertThrows<IndexOutOfBoundsException> { spiedObj[1] }
    }
}