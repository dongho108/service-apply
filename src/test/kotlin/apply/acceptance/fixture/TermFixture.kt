package apply.acceptance.fixture

import apply.application.TermResponse
import apply.ui.api.ApiResponse
import io.restassured.RestAssured
import io.restassured.http.ContentType
import io.restassured.mapper.TypeRef

class TermRequest(var name: String)

class TermBuilder {
    var name: String = "단독모집"

    fun build(): TermResponse {
        val termRequest = TermRequest(name)
        return postTerm(termRequest)
    }

    private fun postTerm(termRequest: TermRequest): TermResponse {
        return RestAssured.given().log().all()
            .contentType(ContentType.JSON)
            .body(termRequest)
            .`when`().log().all()
            .post("/api/terms")
            .`as`(object : TypeRef<ApiResponse<TermResponse>>() {})
            .body as TermResponse
    }
}

fun term(builder: TermBuilder.() -> Unit): TermResponse {
    return TermBuilder().apply(builder).build()
}

fun term(): TermResponse {
    return TermBuilder().build()
}
