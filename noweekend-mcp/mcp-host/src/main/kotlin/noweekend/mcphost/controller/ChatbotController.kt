package noweekend.mcphost.controller

import noweekend.mcphost.service.ChatbotService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class ChatbotController(
    private val chatbotService: ChatbotService,
) {

    @PostMapping("/just-chat")
    fun chat(@RequestBody chatRequest: ChatRequest): ResponseEntity<ChatResponse> {
        println("chatRequest.question = ${chatRequest.question}")
        return ResponseEntity.ok(
            ChatResponse(chatbotService.chat(chatRequest.question)),
        )
    }

    @PostMapping("/getFutureWeather")
    fun getFutureWeather(@RequestBody chatRequest: ChatRequest): ResponseEntity<ChatResponse> {
        println("getFutureWeather.question = ${chatRequest.question}")
        return ResponseEntity.ok(
            ChatResponse(chatbotService.chatWeatherPrompt(chatRequest.question)),
        )
    }
}
