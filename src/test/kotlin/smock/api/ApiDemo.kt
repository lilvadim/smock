package smock.api

import org.junit.jupiter.api.assertThrows
import smock.api.annotation.Smock
import smock.api.annotation.Spy
import smock.api.annotation.smock
import smock.exception.SmockException
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class ApiDemo {
    abstract class Foo {
        open fun bar() = "Original Bar"
        open fun baz() = "Original Baz"
        open fun foo() = "Original Foo"
        open fun method() = "Original Method"
    }

    class FinalFoo {
        fun bar() = "Original Bar"
    }

    @Smock
    lateinit var fooMock: Foo

    @Spy
    lateinit var foo: Foo

    @BeforeTest
    fun initAnnotations() {
        smock(this)
    }

    @Test
    fun finalDemo() {
        val finalFoo = smock<FinalFoo>()

        every { finalFoo.bar() } returns "Mocked Bar"

        assertEquals("Mocked Bar", finalFoo.bar())
    }

    @Test
    fun annotationsDemo() {
        every { fooMock.bar() } returns "SMOCK"

        assertEquals("SMOCK", fooMock.bar())

        every { foo.bar() } returns "SMOCK"

        assertEquals("SMOCK", foo.bar())
        assertEquals("Original Baz", foo.baz())
    }

    @Test
    fun smockDemo() {
        val mockedObj = smock<Foo>()

        every { mockedObj.bar() } returns "Smocked Bar"
        every { mockedObj.baz() } returns "Smocked Baz"

        var answerCallbackCnt = 0
        every { mockedObj.toString() } answers {
            answerCallbackCnt++
            "To String Value"
        }

        assertEquals("Smocked Bar", mockedObj.bar())
        assertEquals("Smocked Baz", mockedObj.baz())

        assertEquals("To String Value", mockedObj.toString())
        mockedObj.toString()

        assertEquals(2, answerCallbackCnt)


        every { mockedObj.foo() } throws Exception("Bar Exception")

        assertThrows<Exception>("Bar Exception") { mockedObj.foo() }

        assertThrows<SmockException> { mockedObj.method() }
    }

    @Test
    fun spyDemo() {
        val foo = spy<Foo>()

        every { foo.bar() } returns "Mock"

        assertEquals("Mock", foo.bar())
        assertEquals("Original Baz", foo.baz())
    }
}