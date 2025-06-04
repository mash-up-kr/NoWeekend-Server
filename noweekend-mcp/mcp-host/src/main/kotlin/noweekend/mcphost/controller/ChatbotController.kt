package noweekend.mcphost.controller

import noweekend.mcphost.service.ChatbotService
import noweekend.mcphost.service.GraphChatService
import org.slf4j.LoggerFactory
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
        logger.info("chatRequest.question = ${chatRequest.question}")
        return ResponseEntity.ok(
            ChatResponse(chatbotService.chat(chatRequest.question)),
        )
    }

    @PostMapping("/lang-chat")
    fun langChat(@RequestBody req: ChatRequest): Mono<ChatResponse> {
        logger.info("req.question = ${req.question}")
        return Mono.fromCallable { graphChatService.chat(req.question) }
            .subscribeOn(Schedulers.boundedElastic())
            .map { ChatResponse(it) }
    }

    @PostMapping("/getFutureWeather")
    fun getFutureWeather(@RequestBody chatRequest: ChatRequest): ResponseEntity<ChatResponse> {
        logger.info("getFutureWeather.question = ${chatRequest.question}")
        return ResponseEntity.ok(
            ChatResponse(chatbotService.chatWeatherPrompt(chatRequest.question)),
        )
    }

    companion object {
        private val logger = LoggerFactory.getLogger(ChatbotController::class.java)
    }
}
