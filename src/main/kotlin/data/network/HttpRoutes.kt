package data.network

object HttpRoutes {

    object OpenApi {
        private const val OPEN_API_BASE_URL = "https://api.openai.com/v1/chat"
        const val OPEN_API_COMPLETIONS = "$OPEN_API_BASE_URL/completions"
    }
}
