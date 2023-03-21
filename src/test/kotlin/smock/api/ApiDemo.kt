package smock.api

import org.junit.jupiter.api.assertThrows
import smock.api.annotations.SmockSpied
import smock.api.annotations.Smocked
import smock.api.annotations.smockAnnotated
import kotlin.test.*

class ApiDemo {
    open class Foo {
        open fun bar() = "Original Bar"
        open fun baz() = "Original Baz"
    }

    @Smocked
    lateinit var someObj: List<Double>

    @SmockSpied
    lateinit var foo: Foo

    @BeforeTest
    fun initAnnotations() {
        smockAnnotated(this)
    }

    @Test
    fun annotationsDemo() {
        every { someObj.isEmpty() } returns false

        assertFalse { someObj.isEmpty() }


        every { foo.bar() } returns "SMOCK"

        assertEquals("SMOCK", foo.bar())
        assertEquals("Original Baz", foo.baz())
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
    fun spyDemo() {
        val foo = spy<Foo>()

        every { foo.bar() } returns "Mock"

        assertEquals("Mock", foo.bar())
        assertEquals("Original Baz", foo.baz())
    }
}