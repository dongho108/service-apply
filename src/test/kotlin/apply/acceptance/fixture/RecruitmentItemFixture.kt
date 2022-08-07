package apply.acceptance.fixture

class RecruitmentItemRequest(
    val title: String,
    val position: Int,
    val maximumLength: Int,
    val description: String
)

class RecruitmentItemBuilder {
    var title: String = "프로그래밍 학습 과정과 현재 자신이 생각하는 역량은?"
    var position: Int = 1
    var maximumLength: Int = 1000
    var description: String = "우아한테크코스는..."

    fun build(): RecruitmentItemRequest {
        return RecruitmentItemRequest(title, position, maximumLength, description)
    }
}

class RecruitmentItemsBuilder {
    private var recruitmentItems = mutableListOf<RecruitmentItemRequest>()

    fun recruitmentItem(builder: RecruitmentItemBuilder.() -> Unit) {
        recruitmentItems.add(RecruitmentItemBuilder().apply(builder).build())
    }

    fun recruitmentItem() {
        recruitmentItems.add(RecruitmentItemBuilder().build())
    }

    fun build(): List<RecruitmentItemRequest> {
        return recruitmentItems
    }
}

fun recruitmentItems(builder: RecruitmentItemsBuilder.() -> Unit): List<RecruitmentItemRequest> {
    return RecruitmentItemsBuilder().apply(builder).build()
}
