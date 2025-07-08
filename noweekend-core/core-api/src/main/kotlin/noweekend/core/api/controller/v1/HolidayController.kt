package noweekend.core.api.controller.v1

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/holiday")
class HolidayController {

    @GetMapping
    fun getHolidays() {

    }
}