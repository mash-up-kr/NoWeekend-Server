package noweekend.mcphost.controller

import noweekend.mcphost.controller.request.AiGenerateVacationRequest
import noweekend.mcphost.controller.request.AiVacationResponse
import noweekend.mcphost.controller.request.Tag
import noweekend.mcphost.controller.request.TagRequest
import noweekend.mcphost.controller.request.WeatherRequest
import noweekend.mcphost.controller.response.WeatherResponse
import noweekend.mcphost.service.ChatbotService
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class ChatbotController(
    private val chatbotService: ChatbotService,
) {
    @PostMapping(
        "/getFutureWeather",
        produces = [MediaType.APPLICATION_JSON_VALUE],
    )
    fun getFutureWeather(@RequestBody request: WeatherRequest): List<WeatherResponse> {
        return chatbotService.weatherRecommendation(request)
    }

    @PostMapping("/getTag")
    fun getTag(@RequestBody request: TagRequest): List<Tag> {
        return chatbotService.tagRecommendation(request)
    }

    @PostMapping("/getTagOnlyNew")
    fun getTagOnlyNew(@RequestBody request: TagRequest): List<Tag> {
        return chatbotService.tagRecommendationOnlyNew(request)
    }

    @PostMapping("/generate-vacation")
    fun generateVacation(@RequestBody request: AiGenerateVacationRequest): AiVacationResponse {
        val content = chatbotService.generateVacationContent(request)
        val title = chatbotService.summarizeTitle(content)

        return AiVacationResponse(
            title = title.title,
            content = content.content,
            startDate = request.startDate,
            endDate = request.endDate,
        )
    }
}
