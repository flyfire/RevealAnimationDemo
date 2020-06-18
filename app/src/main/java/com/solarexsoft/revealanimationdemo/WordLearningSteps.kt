package com.solarexsoft.revealanimationdemo

/**
 * Created by houruhou on 2020/6/18/4:32 PM
 * Desc:
 */
sealed class WordLearningSteps {
    object Step1 : WordLearningSteps()
    data class Step2(val answers: MutableList<String>): WordLearningSteps()
}

data class WordQuestionRsp(
    val questionId: String,
    // native
    var steps: WordLearningSteps = WordLearningSteps.Step1
)

fun main() {
    val wordQuestionRsp = WordQuestionRsp("123")
    bindData(wordQuestionRsp)
    wordQuestionRsp.steps = WordLearningSteps.Step2(mutableListOf("1", "2"))
    bindData(wordQuestionRsp)
}

fun bindData(rsp: WordQuestionRsp) {
    when(rsp.steps) {
        WordLearningSteps.Step1 -> println("show question")
        is WordLearningSteps.Step2 -> {
            (rsp.steps as WordLearningSteps.Step2).answers.forEach(::println)
        }
    }
}