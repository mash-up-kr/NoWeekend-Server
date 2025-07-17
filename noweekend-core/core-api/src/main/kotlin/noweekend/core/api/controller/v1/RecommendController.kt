package noweekend.core.api.controller.v1

import noweekend.client.mcp.recommend.model.SandwichApiResponse
import noweekend.client.mcp.recommend.model.SandwichResponse
import noweekend.core.api.controller.v1.docs.RecommendControllerDocs
import noweekend.core.api.controller.v1.request.GenerateVacationRequest
import noweekend.core.api.controller.v1.response.AiGenerateVacationApiResponse
import noweekend.core.api.controller.v1.response.WeatherResponse
import noweekend.core.api.security.annotations.CurrentUserId
import noweekend.core.domain.IconStyle
import noweekend.core.domain.recommend.RecommendService
import noweekend.core.domain.tag.TagRecommendations
import noweekend.core.support.response.ApiResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate

@RestController
@RequestMapping("/api/v1/recommend")
class RecommendController(
    private val recommendService: RecommendService,
) : RecommendControllerDocs {

    @GetMapping("/weather")
    override fun getWeatherRecommend(
        @CurrentUserId userId: String,
    ): ApiResponse<WeatherResponse> {
        return ApiResponse.success(recommendService.getWeatherRecommend(userId))
    }

    @GetMapping("/todo/mixed")
    override fun getTagRecommendMixed(
        @CurrentUserId userId: String,
    ): ApiResponse<TagRecommendations> {
        return ApiResponse.success(
            recommendService.getTagRecommend(userId),
        )
    }

    @GetMapping("/todo/new-only")
    override fun getTagRecommendOnlyNew(
        @CurrentUserId userId: String,
    ): ApiResponse<TagRecommendations> {
        return ApiResponse.success(
            recommendService.getTagRecommendOnlyNew(userId),
        )
    }

    @GetMapping("/sandwich")
    override fun getSandwich(
        @CurrentUserId userId: String,
    ): ApiResponse<SandwichApiResponse> {
//            return ApiResponse.success(recommendService.getSandwich(userId))
        val mockResponse = SandwichApiResponse(
            responses = listOf(
                SandwichResponse(
                    startDate = LocalDate.of(2025, 8, 14),
                    endDate = LocalDate.of(2025, 8, 16),
                    useAnnualLeave = 1,
                    totalVacationDays = 3,
                ),
                SandwichResponse(
                    startDate = LocalDate.of(2025, 9, 11),
                    endDate = LocalDate.of(2025, 9, 15),
                    useAnnualLeave = 2,
                    totalVacationDays = 5,
                ),
            ),
        )
        return ApiResponse.success(mockResponse)
    }

    @PostMapping("/generate-vacation")
    override fun generateVacation(
        @CurrentUserId userId: String,
        @RequestBody request: GenerateVacationRequest,
    ): ApiResponse<AiGenerateVacationApiResponse> {
        return ApiResponse.success(
            AiGenerateVacationApiResponse(
                "title 예시",
                "• Day 1 (07/07 월) - Day 3 of 5\n\n1. Morning: 집→세종문화회관, 지하철 5호선, 08:30 출발\n2. Morning activity: 세종문화회관 뮤지컬 '팬텀' 관람, 도보 이동·10분\n3. Lunch: 세븐스도어(컨템포러리 요리 전문점, 창의적 코스 요리), 종로\n4. Afternoon activity: 한강공원 수상스포츠체험, 지하철 이동·30분\n5. Dinner: 권숙수(고급 한식 파인다이닝, 전통양념갈비), 강남\n6. Night: 앰배서더 서울 - 풀만 호텔, 택시, 20:00 체크인\n\n• Day 2 (07/08 화) - Day 4 of 5\n\n1. Morning: 풀만 호텔→문래창작촌, 지하철 1호선, 09:30 출발\n2. Morning activity: 문래창작촌 예술거리 산책, 도보 이동·60분\n3. Lunch: 문래창작촌 내 카페&레스토랑(브런치 메뉴), 영등포\n4. Afternoon activity: 롯데월드 아이스링크 스케이팅, 지하철 이동·25분\n5. Dinner: 명동 한식당(비빔밥, 불고기), 명동\n6. Night: 앰배서\n\n• Day 3 (07/09 수)\n1. 오전: 집→아차산, 지하철 2호선→5호선 환승, 07:30 출발\n2. 오전 활동: 아차산 산책로 걷기 (야외 운동), 도보 이동, 약 2시간 소요\n3. 점심: 아차산통갈비탕(왕갈비탕), 아차산 근처\n4. 오후 활동: 광림아트센터 BBCH홀 '마리 퀴리' 뮤지컬 관람, 지하철 이용, 15:00 공연\n5. 저녁: 도깨비불고기 동대문본점(깨비불고기), 동대문 지역\n6. 밤: 호텔 디 아크, 택시 이용, 21:00 체크인\n\n• Day 4 (07/10 목)\n1. 오전: 호텔 디 아크→서울 중구, 지하철 2호선 이용, 09:00 출발\n2. 오전 활동: 아이스하키 원데이 클래스 체험, 택시 이동, 약 2시간 소요\n3. 점심: 에베레스트 레스토랑(탄두리치킨), 동대문 인근\n4. 오후 활동: 남산골 한옥마을 전통문화 체험, 도보 이동, 15:00~17:00\n5. 저녁: 다야(민속 국시, 칼제비), 아차산 인근\n6. 밤: 집, 지하철 및 버스 환승, 20:00 도착\n\n• Day 5 (07/11 금)\n1. 집→대학로 한예극장, 지하철 5호선, 오후 4시 출발\n2. 창작뮤지컬 「행사의 여왕」 관람, 도보 이동·10분 소요\n3. 두채 대학로(브런치 세트), 한예극장 근처\n4. 스크린 스포츠 체험, 도보 이동·15분 소요\n5. 삼 삼뚝배기(뚝배기 비빔밥), 대학로\n6. 집, 지하철 5호선, 오후 10시 30분",
                IconStyle.STAR,
            ),
        )
    }
}
