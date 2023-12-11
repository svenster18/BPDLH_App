package id.bpdlh.fdb.core.domain.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SelfAssesmentQuestionListEntity(
    val data: List<SelfAssesmentQuestionEntity>,
    val maxData: Int,
    val maxPage: Int
) : Parcelable

@Parcelize
data class SelfAssesmentQuestionEntity(
    val step: Int,
    val title: String,
    val question: List<SelfAssesmentQuestionDataEntity>,
) : Parcelable

@Parcelize
data class SelfAssesmentQuestionDataEntity(
    val idQuestion: String,
    val question: String,
    val type: String,
    val answers: List<AnswersDataEntity>,
) : Parcelable

@Parcelize
data class AnswersDataEntity(
    val idAnswer: Int,
    val answer: String,
) : Parcelable
