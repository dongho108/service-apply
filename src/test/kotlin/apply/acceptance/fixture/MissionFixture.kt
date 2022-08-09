package apply.acceptance.fixture

import apply.application.MissionResponse
import apply.ui.api.ApiResponse
import io.restassured.RestAssured
import io.restassured.http.ContentType
import io.restassured.mapper.TypeRef
import java.time.LocalDateTime

private data class MissionRequest(
    var title: String,
    var evaluation: EvaluationSelectRequest,
    var startDateTime: LocalDateTime,
    var endDateTime: LocalDateTime,
    var description: String,
    var submittable: Boolean,
    var hidden: Boolean,
)

class MissionBuilder {
    var title: String = "숫자야구게임"
    var evaluationId: Long = 0L
    var startDateTime: LocalDateTime = LocalDateTime.now().minusYears(1)
    var endDateTime: LocalDateTime = LocalDateTime.now().plusYears(1)
    var description: String = "과제 설명입니다."
    var submittable: Boolean = true
    var hidden: Boolean = false

    fun build(): MissionResponse {
        val evaluation = when (evaluationId) {
            0L -> evaluation()
            else -> getEvaluationById(evaluationId)
        }

        val missionRequest = MissionRequest(
            title,
            EvaluationSelectRequest(evaluation.title, evaluation.id),
            startDateTime,
            endDateTime,
            description,
            submittable,
            hidden
        )

        return postMission(missionRequest, evaluation.recruitmentId)
    }

    private fun postMission(missionRequest: MissionRequest, recruitmentId: Long): MissionResponse {
        return RestAssured.given().log().all()
            .contentType(ContentType.JSON)
            .body(missionRequest)
            .`when`()
            .post("/api/recruitments/$recruitmentId/missions")
            .`as`(object : TypeRef<ApiResponse<MissionResponse>>() {})
            .body as MissionResponse
    }
}

fun getMissionById(missionId: Long): MissionResponse {
    return RestAssured.given()
        .get("/api/recruitments/0/missions/$missionId")
        .then()
        .extract()
        .`as`(object : TypeRef<ApiResponse<MissionResponse>>() {})
        .body as MissionResponse
}

fun mission(builder: MissionBuilder.() -> Unit): MissionResponse {
    return MissionBuilder().apply(builder).build()
}

fun mission(): MissionResponse {
    return MissionBuilder().build()
}
