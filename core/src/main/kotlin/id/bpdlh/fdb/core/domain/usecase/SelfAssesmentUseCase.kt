package id.bpdlh.fdb.core.domain.usecase

import id.bpdlh.fdb.core.base.ResultState
import id.bpdlh.fdb.core.domain.common.SelfAssesmentUseCaseContract
import id.bpdlh.fdb.core.domain.entities.AnswersDataEntity
import id.bpdlh.fdb.core.domain.entities.SelfAssesmentQuestionDataEntity
import id.bpdlh.fdb.core.domain.entities.SelfAssesmentQuestionEntity
import id.bpdlh.fdb.core.domain.entities.SelfAssesmentQuestionListEntity

class SelfAssesmentUseCase : SelfAssesmentUseCaseContract {

    override fun fetchQuestion(param: HashMap<String, String>): ResultState<SelfAssesmentQuestionListEntity> {

        // Create Answers Sample
        val answer1 = AnswersDataEntity(
            answer = "A. Jawaban 1",
            idAnswer = 1
        )

        val answer2 = AnswersDataEntity(
            answer = "B. Jawaban 2",
            idAnswer = 1
        )

        val answer3 = AnswersDataEntity(
            answer = "C. Jawaban 3",
            idAnswer = 1
        )
        val answer4 = AnswersDataEntity(
            answer = "D. Jawaban 4",
            idAnswer = 1
        )
        val dataAnswer: MutableList<AnswersDataEntity> = mutableListOf()
        dataAnswer.add(answer1)
        dataAnswer.add(answer2)
        dataAnswer.add(answer3)
        dataAnswer.add(answer4)

        //? Create Question Sample
        val question1Step1 = SelfAssesmentQuestionDataEntity(
            idQuestion = "1",
            question = "Pertanyaan 1",
            answers = dataAnswer,
            type = "1"
        )
        val question2Step1 = SelfAssesmentQuestionDataEntity(
            idQuestion = "2",
            question = "Pertanyaan 2",
            answers = dataAnswer,
            type = "2"
        )
        val dataQuestion: MutableList<SelfAssesmentQuestionDataEntity> = mutableListOf()
        dataQuestion.add(question1Step1)
        dataQuestion.add(question2Step1)

        // Create Step Sample
        val step1 = SelfAssesmentQuestionEntity(
            step = 1,
            title = "Step 1",
            question = dataQuestion
        )

        val data: MutableList<SelfAssesmentQuestionEntity> = mutableListOf()
        data.add(step1)
        data.add(step1)

        val selsAssesmentQuestionListEntity = SelfAssesmentQuestionListEntity(
            data = data,
            maxPage = 1,
            maxData = 6,
        )

        return ResultState.Success(data = selsAssesmentQuestionListEntity)
    }
}