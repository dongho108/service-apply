package apply.acceptance.fixture

data class EvaluationItemRequest(
    var title: String,
    var maximumScore: Int,
    var position: Int,
    var description: String
)

class EvaluationItemBuilder {
    var title: String = ""
    var maximumScore: Int = 0
    var position: Int = 0
    var description: String = ""

    fun build(): EvaluationItemRequest {
        return EvaluationItemRequest(title, maximumScore, position, description)
    }
}

class EvaluationItemsBuilder {
    private var evaluationItems = mutableListOf<EvaluationItemRequest>()

    fun evaluationItem(builder: EvaluationItemBuilder.() -> Unit) {
        evaluationItems.add(EvaluationItemBuilder().apply(builder).build())
    }

    fun evaluationItem() {
        evaluationItems.add(EvaluationItemBuilder().build())
    }

    fun build(): List<EvaluationItemRequest> {
        return evaluationItems
    }
}

fun evaluationItems(builder: EvaluationItemsBuilder.() -> Unit): List<EvaluationItemRequest> {
    return EvaluationItemsBuilder().apply(builder).build()
}
