package noweekend.client.model

data class ChatRequest(
    val question: String,
)

data class ChatResponse(
    val answer: String,
)
