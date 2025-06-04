// src/main/kotlin/noweekend/mcphost/service/GraphChatService.kt
package noweekend.mcphost.service

import noweekend.mcphost.config.MyAgentState
import org.bsc.langgraph4j.CompiledGraph
import org.springframework.stereotype.Service

@Service
class GraphChatService(
    private val graph: CompiledGraph<MyAgentState>,
) {
    fun chat(question: String): String {
        val initState: Map<String, Any> = mapOf("question" to question)
        val finalState: MyAgentState = graph.invoke(initState)
            .orElseThrow { IllegalStateException("그래프가 최종 상태를 생성하지 못했습니다.") }
        return finalState.chatResponse()
    }
}
