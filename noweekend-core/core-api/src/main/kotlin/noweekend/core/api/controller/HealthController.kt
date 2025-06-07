package noweekend.core.api.controller

import io.swagger.v3.oas.annotations.Hidden
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@Hidden
@RestController
class HealthController {
    @GetMapping("/health")
    fun health(): ResponseEntity<*> {
        return ResponseEntity.status(HttpStatus.OK)
            .body("ok")
    }
}
