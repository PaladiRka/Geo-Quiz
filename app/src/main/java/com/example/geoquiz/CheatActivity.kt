package com.example.geoquiz

import android.animation.Animator
import android.animation.AnimatorSet
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewAnimationUtils
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity


class CheatActivity : AppCompatActivity() {
    private val TAG = "CheatActivity"
    private val KEY_CHEAT = "cheat"

    private val answerIsTrue: Boolean
        get() = intent.getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false)


    private val answerTextView: TextView
        get() = findViewById(R.id.answer_text_view)
    private val showAnswerButton: Button
        get() = findViewById(R.id.show_answer_button)

    private var isShowAnswer = false

    companion object {
        private const val EXTRA_ANSWER_IS_TRUE = "com.example.geoquiz.answer_is_true";
        private const val EXTAR_ANSWER_SHOW = "com.example.geoquiz.answer_show";

        fun newIntent(packageContext: Context, answerIsTrue: Boolean): Intent {
            val intent = Intent(packageContext, CheatActivity::class.java)
            intent.putExtra(EXTRA_ANSWER_IS_TRUE, answerIsTrue)
            return intent
        }

        fun wasAnswerShow(result: Intent): Boolean {
            return result.getBooleanExtra(EXTAR_ANSWER_SHOW, false)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(KEY_CHEAT, isShowAnswer)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cheat)

        if (savedInstanceState != null) {
            isShowAnswer = savedInstanceState.getBoolean(KEY_CHEAT, false)
        }

        showAnswerButton.setOnClickListener {
            if (answerIsTrue) {
                answerTextView.setText(R.string.true_button)
            } else {
                answerTextView.setText(R.string.false_button)
            }
            isShowAnswer = true

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Log.i(TAG, "${Build.VERSION.SDK_INT} >= ${Build.VERSION_CODES.LOLLIPOP}")
                val cx = showAnswerButton.width / 2 - 10
                val cy = showAnswerButton.height / 2 - 10
                val radius = showAnswerButton.width.toFloat()
                val anim = ViewAnimationUtils.createCircularReveal(showAnswerButton, cx, cy, radius, 0F)
                anim.addListener(object : Animator.AnimatorListener {
                    override fun onAnimationEnd(animation: Animator) {
                        showAnswerButton.visibility = View.INVISIBLE
                    }

                    override fun onAnimationStart(animation: Animator?) {}

                    override fun onAnimationCancel(animation: Animator?) {}

                    override fun onAnimationRepeat(animation: Animator?) {}
                })
                anim?.start()
            } else {
                Log.i(TAG, "${Build.VERSION.SDK_INT} >= ${Build.VERSION_CODES.LOLLIPOP} NOT")
                showAnswerButton.visibility = View.INVISIBLE
            }
        }
    }

    override fun finish() {
        super.finish()
        setAnswerResult(isShowAnswer)
    }

    private fun setAnswerResult(isAnswerShow: Boolean) {
        val data = intent
        Log.d(TAG, "setAnswerResult:  $isShowAnswer")
        data.putExtra(EXTAR_ANSWER_SHOW, isAnswerShow)
        setResult(RESULT_OK, data)
    }
}
