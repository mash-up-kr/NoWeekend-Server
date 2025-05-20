package noweekend.mcphost.controller

import noweekend.mcphost.service.ChatbotService
import noweekend.mcphost.service.GraphChatService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers

@RestController
class ChatbotController(
    private val chatbotService: ChatbotService,
    private val graphChatService: GraphChatService,
) {

    @PostMapping("/just-chat")
    fun chat(@RequestBody chatRequest: ChatRequest): ResponseEntity<ChatResponse> {
        println("chatRequest.question = ${chatRequest.question}")
        return ResponseEntity.ok(
            ChatResponse(chatbotService.chat(chatRequest.question)),
        )
    }

    @PostMapping("/lang-chat")
    fun langChat(@RequestBody req: ChatRequest): Mono<ChatResponse> {
        println("req.question = ${req.question}")
        return Mono.fromCallable { graphChatService.chat(req.question) }
            .subscribeOn(Schedulers.boundedElastic()) // ← 여기서만 블로킹 허용
            .map { ChatResponse(it) }
    }

    @PostMapping("/getFutureWeather")
    fun getFutureWeather(@RequestBody chatRequest: ChatRequest): ResponseEntity<ChatResponse> {
        println("getFutureWeather.question = ${chatRequest.question}")
        return ResponseEntity.ok(
            ChatResponse(chatbotService.chatWeatherPrompt(chatRequest.question)),
        )
    }
}
