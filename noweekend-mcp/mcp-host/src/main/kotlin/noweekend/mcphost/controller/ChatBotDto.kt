package noweekend.mcphost.controller

data class ChatRequest(
    val question: String,
)

data class ChatResponse(
    val answer: String,
)
