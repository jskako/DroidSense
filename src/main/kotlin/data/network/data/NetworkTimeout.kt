package data.network.data

data class NetworkTimeout(
    val requestTimeoutMillis: Long = TWO_MINUTES_TIMEOUT, // Set request timeout to 15 seconds
    val connectTimeoutMillis: Long = TWO_MINUTES_TIMEOUT,  // Set connection timeout to 5 seconds
    val socketTimeoutMillis: Long = TWO_MINUTES_TIMEOUT  // Set socket timeout to 10 seconds
)

private const val TWO_MINUTES_TIMEOUT: Long = 120_000L