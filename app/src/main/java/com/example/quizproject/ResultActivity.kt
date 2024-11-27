package com.example.quizproject

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class ResultActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        val scoreTextView: TextView = findViewById<TextView>(R.id.scoreTextView)
        val wrongQuestionsContainer: LinearLayout = findViewById(R.id.wrongQuestionsContainer)
        val backButton: Button = findViewById<Button>(R.id.backButton)

        // Retrieve data from Intent
        val score = intent.getIntExtra("score", 0)
        val totalQuestions = intent.getIntExtra("totalQuestions", 0)
        val wrongQuestions = intent.getSerializableExtra("wrongQuestions") as? List<Pair<String, String>>

        // Display score
        scoreTextView.text = "Your Score: $score / $totalQuestions"

        // Display incorrect questions
        wrongQuestions?.forEach { (question, correctAnswer) ->
            val textView = TextView(this)
            textView.text = "Q: $question\nCorrect Answer: $correctAnswer\n"
            textView.setPadding(8, 8, 8, 16)
            wrongQuestionsContainer.addView(textView)
        }

        // Back to quiz button
        backButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
        }
    }
}
