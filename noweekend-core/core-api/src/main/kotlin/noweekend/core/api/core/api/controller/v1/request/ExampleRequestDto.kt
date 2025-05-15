package noweekend.core.api.core.api.controller.v1.request

import noweekend.core.api.core.domain.ExampleData

data class ExampleRequestDto(
    val data: String,
) {
    fun toExampleData(): ExampleData {
        return ExampleData(data, data)
    }
}
