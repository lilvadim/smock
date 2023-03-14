package smock.api

class ApiDemo() {
    fun demo() {
        val mockedObj = smock<List<Any>>()
        every { return@every }
    }
}