package com.example.geoquiz

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    //    private var mTrueButton: Button? = null;
    private val falseButton: Button by lazy {
        findViewById<Button>(R.id.false_button)
    }


    private val TAG = "QuizActivity"
    private val KEY_INDEX = "index"
    private val KEY_CHEAT = "cheat"
    private val REQUEST_CODE_CHEAT = 10
    private val cheatButton: Button
        get() = findViewById(R.id.cheat_button)
    private val trueButton: Button
        get() = findViewById(R.id.true_button)
    private val nextButton: ImageButton
        get() = findViewById(R.id.next_button)
    private val prevButton: ImageButton
        get() = findViewById(R.id.prev_button)
    private val questionTextView: TextView
        get() = findViewById(R.id.question_text_view)
    private val questionBank = arrayOf(
        Question(TextResId = R.string.question_britain, AnswerTrue = true),
        Question(TextResId = R.string.question_russia, AnswerTrue = true),
        Question(TextResId = R.string.question_germany, AnswerTrue = false)
    )

    private var currentIndex = 0

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.i(TAG, "onSaveInstanceState")
        outState.putInt(KEY_INDEX, currentIndex)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate(Bundle) called")
        setContentView(R.layout.activity_main)

        if (savedInstanceState != null) {
            currentIndex = savedInstanceState.getInt(KEY_INDEX, 0)
        }

        trueButton.setOnClickListener(
            object : View.OnClickListener {
                override fun onClick(v: View?) {
                    checkAnswer(true)
                    falseButton.isEnabled = false
                    trueButton.isEnabled = false
                }
            }
        )

        falseButton.setOnClickListener {
            checkAnswer(false)
            falseButton.isEnabled = false
            trueButton.isEnabled = false
        }

        nextButton.setOnClickListener {
            this.currentIndex = (this.currentIndex + 1) % questionBank.size
            updateQuestion()
        }

        prevButton.setOnClickListener {
            val nextIter = (this.currentIndex - 1)
            this.currentIndex = if (nextIter < 0) questionBank.size - 1 else nextIter
            updateQuestion()
        }

        questionTextView.setOnClickListener {
            this.currentIndex = (this.currentIndex + 1) % questionBank.size
            updateQuestion()
        }

        cheatButton.setOnClickListener {

            val intent = CheatActivity.newIntent(this, questionBank[currentIndex].answeTrue)
            startActivityForResult(intent, REQUEST_CODE_CHEAT)
        }

        updateQuestion()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.i(TAG, "onActivityResult: result $resultCode  request $requestCode RESULT_OK ${Activity.RESULT_OK} data $data")

        if (resultCode != Activity.RESULT_OK) {
            return
        }
        if (requestCode == REQUEST_CODE_CHEAT) {
            if (data == null) {
                return
            }
            questionBank[currentIndex].cheatUsed = CheatActivity.wasAnswerShow(data)
            Log.i(TAG, "Result isCheater $questionBank[currentIndex].cheatUsed")
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart() called")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume() called")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause() called")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop() called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy() called")
    }

    private fun updateQuestion() {
        val question = questionBank[this.currentIndex].textResId
        questionTextView.setText(question)
        if (questionBank[this.currentIndex].answer == null) {
            falseButton.isEnabled = true
            trueButton.isEnabled = true
        } else {
            falseButton.isEnabled = false
            trueButton.isEnabled = false
        }
    }

    private fun checkAnswer(userPressedTrue: Boolean) {
        val isCorrect = questionBank[currentIndex].answerQuestion(userPressedTrue)

        val textId = if (questionBank[currentIndex].cheatUsed) R.string.judgment_toast else if (isCorrect) R.string.correct_toast else R.string.incorrect_toast
        val toast = Toast.makeText(
            this,
            textId,
            Toast.LENGTH_SHORT
        )
        toast.setGravity(Gravity.TOP, 0, 50)
        toast.show()
        val result = resultState()
        if (result != null) {
            val text: CharSequence = "You have result " + result + "/" + questionBank.size
            Log.i(TAG, text.toString())
            // mQuestionTextView.text = text
            Toast.makeText(
                this,
                text,
                Toast.LENGTH_LONG
            ).show()
            questionBank.forEach { it.answer = null }
        }
    }

    private fun resultState(): Int? {
        var result = 0
        for (quest in this.questionBank) {
            if (quest.answer == null) {
                return null
            } else {
                if (quest.answer == quest.answeTrue) {
                    result++
                }
            }
        }
        return result
    }
}
