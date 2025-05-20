package noweekend.mcphost.config

import org.bsc.langgraph4j.state.AgentState
import org.bsc.langgraph4j.state.Channel
import org.bsc.langgraph4j.state.Channels
import java.util.function.Supplier

class MyAgentState(initData: Map<String, Any>) : AgentState(initData) {

    fun question() = value<String>("question")
        .orElseThrow { IllegalStateException("question missing") }

    fun chatResponse() = value<String>("chat_response")
        .orElse("")

    fun weatherJson() = value<String>("weather_json")
        .orElse("")

    companion object {
        @JvmStatic
        val SCHEMA: Map<String, Channel<*>> = mapOf(
            "question" to Channels.base(Supplier { "" }),
            "chat_response" to Channels.base(Supplier { "" }),
            "weather_json" to Channels.base(Supplier { "" }),
        )
    }
}
