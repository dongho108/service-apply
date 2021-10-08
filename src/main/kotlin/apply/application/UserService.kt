package apply.application

import apply.domain.user.User
import apply.domain.user.UserRepository
import apply.domain.user.findByEmail
import apply.domain.user.getById
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional
@Service
class UserService(
    private val userRepository: UserRepository,
    private val passwordGenerator: PasswordGenerator
) {
    fun getByEmail(email: String): User {
        return userRepository.findByEmail(email) ?: throw IllegalArgumentException("회원이 존재하지 않습니다. email: $email")
    }

    fun findAllByKeyword(keyword: String): List<UserResponse> {
        return userRepository.findAllByKeyword(keyword).map(::UserResponse)
    }

    fun resetPassword(request: ResetPasswordRequest): String {
        return passwordGenerator.generate().also {
            getByEmail(request.email).resetPassword(request.name, request.birthday, it)
        }
    }

    fun editPassword(id: Long, request: EditPasswordRequest) {
        userRepository.getById(id).changePassword(request.password, request.newPassword)
    }

    fun editInformation(id: Long, request: EditInformationRequest) {
        userRepository.getById(id).changePhoneNumber(request.phoneNumber)
    }
}
