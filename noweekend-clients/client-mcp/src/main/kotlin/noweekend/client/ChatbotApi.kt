package noweekend.client.api

import noweekend.client.model.ChatRequest
import noweekend.client.model.ChatResponse
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody

@FeignClient(
    name = "mcp-host",
    url = "\${mcp.host.url}",
)
interface ChatbotApi {
    @PostMapping(
        value = ["/just-chat"],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
    )
    fun chat(@RequestBody req: ChatRequest): ChatResponse

    @PostMapping(
        value = ["/getFutureWeather"],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
    )
    fun getFutureWeather(@RequestBody req: ChatRequest): ChatResponse
}
