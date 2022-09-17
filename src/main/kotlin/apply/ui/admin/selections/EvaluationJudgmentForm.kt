package apply.ui.admin.selections

import apply.application.EvaluationJudgementData
import com.vaadin.flow.component.textfield.IntegerField
import com.vaadin.flow.component.textfield.TextArea
import support.views.BindingFormLayout

class EvaluationJudgmentForm() : BindingFormLayout<EvaluationJudgementData>(EvaluationJudgementData::class) {

    private val requestKey: TextArea = TextArea("request_key")
    private val commitHash: TextArea = TextArea("commit_hash")
    private val statusCode: IntegerField = IntegerField("status_code")
    private val passCount: IntegerField = IntegerField("맞춘 테스트케이스 개수")
    private val totalCount: IntegerField = IntegerField("총 테스트케이스 개수")

    init {
        add(requestKey, commitHash, statusCode, passCount, totalCount)
        setColspan(requestKey, 2)
        setColspan(commitHash, 2)
        setColspan(statusCode, 2)
        drawRequired()
    }

    constructor(evaluationJudgementData: EvaluationJudgementData) : this() {
        evaluationJudgementData.requestKey
        evaluationJudgementData.commitHash
        evaluationJudgementData.statusCode
        evaluationJudgementData.passCount
        evaluationJudgementData.totalCount
    }

    override fun fill(data: EvaluationJudgementData) {
        fillDefault(data)
        requestKey.isReadOnly = true
        commitHash.isReadOnly = true
        statusCode.isReadOnly = true
        passCount.isReadOnly = true
        totalCount.isReadOnly = true
    }

    override fun bindOrNull(): EvaluationJudgementData? {
        return bindDefaultOrNull()
    }
}
