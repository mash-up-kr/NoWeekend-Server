package noweekend.mcphost.config

import org.bsc.langgraph4j.StateGraph
import org.bsc.langgraph4j.action.AsyncNodeAction
import org.slf4j.LoggerFactory
import org.springframework.ai.chat.client.ChatClient
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.LocalDateTime
import java.time.ZoneId

@Configuration
class LangGraphConfig(
    private val chatClient: ChatClient,
) {
    @Bean
    fun myGraph() =
        StateGraph(MyAgentState.SCHEMA, ::MyAgentState)
            .addEdge(StateGraph.START, "dispatch")
            .addNode(
                "dispatch",
                AsyncNodeAction.node_async { st ->
                    logger.info("[LangGraph] dispatch 노드 진입, question=${st.question()}")
                    val today: LocalDateTime = LocalDateTime.now(ZoneId.of("Asia/Seoul"))
                    val systemPrompt1 = """
                        You are an AI assistant. Always communicate in polite Korean honorifics.
                        
                        A predeclared variable is available:
                    """.trimIndent()

                    val systemPrompt2 = """
                        Use this variable to convert Korean relative date expressions to ISO date strings (YYYY-MM-DD):
                          • “오늘” → today.toLocalDate().toString()
                          • “내일” → today.plusDays(1).toLocalDate().toString()
                          • “모레” → today.plusDays(2).toLocalDate().toString()
                          • “어제” → today.minusDays(1).toLocalDate().toString()
                        
                        Tools (use exact calls):
                          • getFutureWeather("YYYY-MM-DD Seoul"): returns a JSON weather forecast for the specified date and “Seoul”
                          • search("…"): returns text search results
                        
                        Workflow:
                        1. Internally translate the Korean user query into English.
                        2. If the query contains any relative date expressions, immediately output only:
                           getCurrentDate()
                        3. After receiving the concrete date, rewrite the English query by replacing all relative dates with their ISO equivalents.
                        4. For each date+“Seoul” pair, output only:
                           getFutureWeather("YYYY-MM-DD Seoul")
                        5. Once you receive the JSON forecast, compose your answer in English.
                        6. Translate that answer back into polite Korean.
                        7. Return only the final Korean text—no code, no markdown.
                    """.trimIndent()

                    val reply = try {
                        chatClient.prompt()
                            .system("$systemPrompt1$today$systemPrompt2")
                            .user(st.question())
                            .call()
                            .content()
                            .orEmpty()
                    } catch (e: Exception) {
                        logger.error("Failed to get chat response", e)
                        "죄송합니다. 요청을 처리하는 중 오류가 발생했습니다."
                    }

                    mapOf("chat_response" to reply)
                },
            )
            .addEdge("dispatch", StateGraph.END)
            .compile()

    companion object {
        private val logger = LoggerFactory.getLogger(LangGraphConfig::class.java)
    }
}
