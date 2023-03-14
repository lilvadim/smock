package smock.api

import java.lang.Exception

class ApiDemo() {
    fun demo() {
        val mockedObj = smock<List<String>>()
        every { mockedObj.get(0) } returns "SomeValue"
        every { mockedObj.get(0) } answers { "SomeValue" }
        every { mockedObj.get(0) } throws Exception("msg")
        every {  } just runs
    }
}