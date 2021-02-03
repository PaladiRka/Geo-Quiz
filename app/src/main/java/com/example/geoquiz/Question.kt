package com.example.geoquiz

class Question(AnswerTrue: Boolean, TextResId: Int) {
    val textResId: Int = TextResId
    val answeTrue: Boolean = AnswerTrue

    var cheatUsed: Boolean = false
    var answer: Boolean? = null

    fun answerQuestion(Answer: Boolean): Boolean {
        this.answer = Answer
        return Answer == answeTrue
    }
}