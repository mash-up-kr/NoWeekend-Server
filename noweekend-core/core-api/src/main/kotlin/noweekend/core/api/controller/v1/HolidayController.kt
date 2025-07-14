package noweekend.core.api.controller.v1

import noweekend.core.api.controller.v1.docs.HolidayControllerDocs
import noweekend.core.api.controller.v1.response.HolidayResponses
import noweekend.core.domain.holiday.HolidayService
import noweekend.core.support.response.ApiResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/holiday")
class HolidayController(
    private val holidayService: HolidayService,
) : HolidayControllerDocs {

    @GetMapping
    override fun getHolidays(@RequestParam year: Int, @RequestParam month: Int): ApiResponse<HolidayResponses> {
        return ApiResponse.success(
            HolidayResponses(
                holidayService.getMonthHolidays(year, month),
            ),
        )
    }

    @GetMapping("/remaining")
    override fun getRemainingHolidays(): ApiResponse<HolidayResponses> {
        return ApiResponse.success(
            HolidayResponses(holidayService.getRemainingHolidays()),
        )
    }
}
