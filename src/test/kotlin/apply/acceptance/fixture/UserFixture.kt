package apply.acceptance.fixture

import apply.application.RegisterUserRequest
import apply.domain.user.Gender
import apply.domain.user.Password
import apply.ui.api.ApiResponse
import io.restassured.RestAssured
import io.restassured.http.ContentType
import io.restassured.mapper.TypeRef
import java.time.LocalDate

data class RegisterUserRequest(
    val name: String,
    val email: String,
    val phoneNumber: String,
    val gender: Gender,
    val birthday: LocalDate,
    val password: Password,
    val confirmPassword: Password,
    val authenticationCode: String
)

class UserBuilder {
    var name: String = "김아무개"
    var email: String = "test@naver.com"
    var phoneNumber: String = "010-1111-2222"
    var gender: Gender = Gender.MALE
    var birthday: LocalDate = LocalDate.of(1997, 10, 20)
    var password: String = "password"
    var authenticationCode: String = "testAuthCode"

    fun build(): String {
        return postUser()
    }

    private fun postUser(): String {
        val registerUserRequest = RegisterUserRequest(
            name,
            email,
            phoneNumber,
            gender,
            birthday,
            Password(password),
            Password(password),
            authenticationCode
        )

        return RestAssured.given().log().all()
            .contentType(ContentType.JSON)
            .body(registerUserRequest)
            .`when`()
            .post("/api/users/register")
            .`as`(object : TypeRef<ApiResponse<String>>() {})
            .body as String
    }
}

fun userToken(builder: UserBuilder.() -> Unit): String {
    return UserBuilder().apply(builder).build()
}

fun userToken(): String {
    return UserBuilder().build()
}
