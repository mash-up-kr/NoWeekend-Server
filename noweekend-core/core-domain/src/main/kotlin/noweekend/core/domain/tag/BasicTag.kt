package noweekend.core.domain.tag

enum class BasicTag(
    val koreanContent: String,
) {
    MEETING("회의 참석"),
    LUNCH_APPOINTMENT("점심 식사 약속"),
    GYM("헬스장 운동"),
    GROCERY_SHOPPING("장 보기 / 마트 가기"),
    FAMILY_GATHERING("가족 모임"),
    HOSPITAL_APPOINTMENT("병원 예약"),
    CAFE_WORK("카페에서 작업 / 휴식"),
    FRIEND_MEETING("친구 만남"),
    DRINKING("술자리"),
    STUDY("스터디"),
    ACADEMY_CLASS("학원 수업"),
    NIGHT_SHIFT("야근"),
    EXTRA_WORK("추가 업무"),
    WALKING("산책"),
    PET_WALKING("반려동물 산책"),
    HOUSEWORK("집안일"),
    BANK("은행 업무"),
    GOVERNMENT_OFFICE("관공서 업무"),
    READING("독서"),
    DATE("데이트"),
    BEAUTY_SALON("미용실"),
    DRIVING("드라이브"),
    PICNIC("나들이"),
    NETFLIX("넷플릭스 시청"),
    YOUTUBE("유튜브 시청"),
    TV("치지직 시청"),
    ;

    companion object {
        fun fromKorean(korean: String): BasicTag {
            return entries.find { it.koreanContent == korean } ?: throw NoSuchElementException()
        }
    }
}
