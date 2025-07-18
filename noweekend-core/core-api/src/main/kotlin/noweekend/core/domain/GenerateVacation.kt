package noweekend.core.domain

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "여행 스타일 선택지")
enum class TravelStyle(
    @Schema(description = "한글 라벨")
    val korean: String,
) {
    @Schema(description = "계획형")
    PLANNER("계획형"),

    @Schema(description = "즉흥 자유형")
    SPONTANEOUS("즉흥 자유형"),
}

@Schema(description = "활동 유형 선택지")
enum class ActivityType(
    @Schema(description = "한글 라벨")
    val korean: String,
) {
    @Schema(description = "야외 활동")
    OUTDOOR("야외 활동"),

    @Schema(description = "집콕")
    AT_HOME("집콕"),
}

@Schema(description = "휴식 유형 선택지")
enum class RestPreference(
    @Schema(description = "한글 라벨")
    val korean: String,
) {
    @Schema(description = "휴식")
    REST("휴식"),

    @Schema(description = "자기계발")
    SELF_DEVELOPMENT("자기계발"),
}

@Schema(description = "관심사 선택지")
enum class LeisurePreference(
    @Schema(description = "한글 라벨")
    val korean: String,
) {
    @Schema(description = "음식")
    FOOD("음식"),

    @Schema(description = "관광")
    TOURISM("관광"),
}
