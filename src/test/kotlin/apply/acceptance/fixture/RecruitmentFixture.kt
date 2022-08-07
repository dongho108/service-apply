package apply.acceptance.fixture

import apply.application.RecruitmentResponse
import apply.application.TermResponse
import apply.ui.api.ApiResponse
import io.restassured.RestAssured
import io.restassured.http.ContentType
import io.restassured.mapper.TypeRef
import java.time.LocalDateTime

data class RecruitmentRequest(
    var title: String,
    var term: TermResponse,
    var startDateTime: LocalDateTime,
    var endDateTime: LocalDateTime,
    var recruitable: Boolean,
    var hidden: Boolean,
    var recruitmentItems: List<RecruitmentItemRequest>,
)

data class RecruitmentSelectRequest(
    val title: String,
    val id: Long
)

class RecruitmentBuilder {
    var title: String = "웹 백엔드 3기"
    var termId: Long = 0L
    var startDateTime: LocalDateTime = LocalDateTime.now().minusYears(1)
    var endDateTime: LocalDateTime = LocalDateTime.now().plusYears(1)
    var recruitable: Boolean = true
    var hidden: Boolean = false
    var recruitmentItems: List<RecruitmentItemRequest> = emptyList()

    fun build(): RecruitmentResponse {
        val term = when (termId) {
            0L -> term()
            else -> getTermById(termId)
        }

        val recruitmentRequest = RecruitmentRequest(
            title,
            term,
            startDateTime,
            endDateTime,
            recruitable,
            hidden,
            recruitmentItems
        )

        return postRecruitment(recruitmentRequest)
    }

    private fun postRecruitment(recruitmentRequest: RecruitmentRequest): RecruitmentResponse {
        return RestAssured.given().log().all()
            .contentType(ContentType.JSON)
            .body(recruitmentRequest)
            .`when`()
            .post("/api/recruitments")
            .`as`(object : TypeRef<ApiResponse<RecruitmentResponse>>() {})
            .body as RecruitmentResponse
    }
}

fun getRecruitmentById(recruitmentId: Long): RecruitmentResponse {
    return RestAssured.given()
        .get("/api/recruitments/$recruitmentId")
        .then()
        .extract()
        .`as`(object : TypeRef<ApiResponse<RecruitmentResponse>>() {})
        .body as RecruitmentResponse
}

fun recruitment(builder: RecruitmentBuilder.() -> Unit): RecruitmentResponse {
    return RecruitmentBuilder().apply(builder).build()
}

fun recruitment(): RecruitmentResponse {
    return RecruitmentBuilder().build()
}
