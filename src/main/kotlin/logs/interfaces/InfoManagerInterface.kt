package logs.interfaces

interface InfoManagerInterface {
    fun showInfoMessage(message: String, duration: Long = 2000L)
}