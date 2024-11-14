package data.network


import data.network.data.NetworkTimeout
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json


object NetworkModule {
    fun provideHttpClient(
        bearerToken: BearerTokens? = null,
        networkTimeout: NetworkTimeout = NetworkTimeout()
    ): HttpClient {
        return HttpClient(CIO) {
            install(Logging) {
                level = LogLevel.ALL
            }
            install(ContentNegotiation) {
                json(
                    Json {
                        ignoreUnknownKeys = true
                        isLenient = true
                    }
                )
            }

            install(HttpTimeout) {
                requestTimeoutMillis = networkTimeout.requestTimeoutMillis
                connectTimeoutMillis = networkTimeout.connectTimeoutMillis
                socketTimeoutMillis = networkTimeout.socketTimeoutMillis
            }

            bearerToken?.let {
                install(Auth) {
                    bearer {
                        loadTokens {
                            BearerTokens(
                                accessToken = bearerToken.accessToken,
                                refreshToken = bearerToken.refreshToken
                            )
                        }
                    }
                }
            }
        }
    }
}