package apply.acceptance.fixture

import apply.application.AssignmentResponse
import apply.ui.api.ApiResponse
import io.restassured.RestAssured
import io.restassured.http.ContentType
import io.restassured.mapper.TypeRef

private data class AssignmentRequest(
    val githubUsername: String,
    val pullRequestUrl: String,
    val note: String
)

class AssignmentBuilder {
    var githubUsername: String = "ecsimsw"
    var pullRequestUrl: String = "https://github.com/woowacourse/service-apply/pull/367"
    var note: String = "과제 소감입니다."
    var missionId: Long = 0L
    var userToken: String = ""

    fun build(): AssignmentResponse {
        val assignmentRequest = AssignmentRequest(
            githubUsername,
            pullRequestUrl,
            note
        )

        return postAssignment(assignmentRequest)
    }

    private fun postAssignment(assignmentRequest: AssignmentRequest): AssignmentResponse {
        val mission = when (missionId) {
            0L -> mission()
            else -> getMissionById(missionId)
        }
        missionId = mission.id

        return RestAssured.given().log().all()
            .contentType(ContentType.JSON)
            .header("Authorization", "Bearer $userToken")
            .body(assignmentRequest)
            .`when`()
            .post("/api/recruitments/0/missions/$missionId/assignments")
            .`as`(object : TypeRef<ApiResponse<AssignmentResponse>>() {})
            .body as AssignmentResponse
    }
}

fun assignment(builder: AssignmentBuilder.() -> Unit): AssignmentResponse {
    return AssignmentBuilder().apply(builder).build()
}

fun assignment(): AssignmentResponse {
    return AssignmentBuilder().build()
}
