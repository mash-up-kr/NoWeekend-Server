package noweekend.mcpserver.tool

import noweekend.mcpserver.client.ClimateClient
import noweekend.mcpserver.tool.dto.ClimateResponse
import noweekend.mcpserver.tool.dto.DailyClimateData
import noweekend.mcpserver.tool.dto.FutureWeatherRequest
import org.springframework.ai.tool.annotation.Tool
import org.springframework.stereotype.Component

@Component
class FutureWeatherTool(
    private val climateClient: ClimateClient
) {

    companion object {
        private const val MODELS     = "EC_Earth3P_HR"
        private const val DAILY_VARS = "temperature_2m_mean,precipitation_sum"
        private const val TIMEZONE   = "Asia/Seoul"
    }

    /**
     * 과거 기후 모델 시뮬레이션을 바탕으로, 지정한 날짜의
     * 평균 기온(°C)과 일일 강수합(mm)을 예측하여 반환합니다.
     */
    @Tool(
        name = "getFutureWeatherDaily",
        description = """
            Return a list of daily mean temperature (°C) and precipitation (mm)
            for each day between startDate and endDate (≤ 2050-01-01) at a given
            latitude/longitude.
        """
    )
    fun getFutureWeatherDaily(req: FutureWeatherRequest): List<DailyClimateData> {
        val cr: ClimateResponse = climateClient.getClimate(
            req.latitude,
            req.longitude,
            req.startDate,
            req.endDate,
            MODELS,
            DAILY_VARS,
            TIMEZONE
        )

        // --- 배열 → DTO 리스트 매핑 ---
        val times = cr.daily.time
        val temps = cr.daily.temperature2mMean
        val precs = cr.daily.precipitationSum

        return times.indices.map { i ->
            DailyClimateData(
                date       = times[i],
                meanTemp = temps[i],
                precipitation = precs[i]
            )
        }
    }
}