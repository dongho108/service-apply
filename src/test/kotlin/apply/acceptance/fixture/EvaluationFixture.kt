package apply.acceptance.fixture

import apply.application.EvaluationResponse
import apply.ui.api.ApiResponse
import io.restassured.RestAssured
import io.restassured.http.ContentType
import io.restassured.mapper.TypeRef

data class EvaluationRequest(
    var title: String,
    var description: String,
    var recruitment: RecruitmentSelectRequest,
    var beforeEvaluation: EvaluationSelectRequest,
    var evaluationItems: List<EvaluationItemRequest>
)

data class EvaluationSelectRequest(
    val title: String = "",
    val id: Long = 0L
)

class EvaluationBuilder {
    var title: String = "프리코스 대상자 선발"
    var description: String = "[리뷰 절차]\n" +
        "https://github.com/woowacourse/woowacourse-docs/tree/master/precourse"
    var recruitmentId: Long = 0L
    var beforeEvaluationId: Long = 0L
    var evaluationItems: List<EvaluationItemRequest> = emptyList()

    fun build(): EvaluationResponse {
        val recruitment = when (recruitmentId) {
            0L -> {
                val recruitment = recruitment()
                recruitmentId = recruitment.id
                RecruitmentSelectRequest(recruitment.title, recruitment.id)
            }
            else -> {
                val recruitment = getRecruitmentById(recruitmentId)
                RecruitmentSelectRequest(recruitment.title, recruitment.id)
            }
        }

        val beforeEvaluation = when (beforeEvaluationId) {
            0L -> EvaluationSelectRequest()
            else -> {
                val evaluation = getEvaluationById(beforeEvaluationId, recruitmentId)
                EvaluationSelectRequest(evaluation.title, evaluation.id)
            }
        }

        val evaluationRequest = EvaluationRequest(
            title,
            description,
            recruitment,
            beforeEvaluation,
            evaluationItems
        )

        return postEvaluation(evaluationRequest)
    }

    private fun postEvaluation(evaluationRequest: EvaluationRequest): EvaluationResponse {
        return RestAssured.given().log().all()
            .contentType(ContentType.JSON)
            .body(evaluationRequest)
            .`when`()
            .post("/api/recruitments/$recruitmentId/evaluations")
            .`as`(object : TypeRef<ApiResponse<EvaluationResponse>>() {})
            .body as EvaluationResponse
    }
}

fun getEvaluationById(evaluationId: Long, recruitmentId: Long): EvaluationResponse {
    return RestAssured.given()
        .get("/api/recruitments/$recruitmentId/evaluations/$evaluationId")
        .then()
        .extract()
        .`as`(object : TypeRef<ApiResponse<EvaluationResponse>>() {})
        .body as EvaluationResponse
}

fun evaluation(builder: EvaluationBuilder.() -> Unit): EvaluationResponse {
    return EvaluationBuilder().apply(builder).build()
}

fun evaluation(): EvaluationResponse {
    return EvaluationBuilder().build()
}
