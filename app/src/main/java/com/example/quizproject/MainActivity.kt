package com.example.quizproject

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {private lateinit var subjectSpinner: Spinner
    private lateinit var questionTextView: TextView
    private lateinit var option1Button: Button
    private lateinit var option2Button: Button
    private lateinit var option3Button: Button
    private lateinit var option4Button: Button
    private lateinit var scoreTextView: TextView

    private var currentQuestionIndex = 0
    private var score = 0

    private var selectedQuestions: List<Question> = listOf()
    private val wrongQuestions: MutableList<Pair<String, String>> = mutableListOf()

    private val quizData = mapOf(
        "Python" to listOf(
            Question("What is the output of print(2 ** 3)?",listOf("6","8","9","4"),1),
            Question("Which of the following is a valid variable name in Python?" ,listOf("123var","var_name","var-name","None"),1),
            Question("What is the data type of the result of 3 / 2 in Python 3?",listOf("int","float","str","None"),1),
            Question("Which method is used to add an item to the end of a list?",listOf("insert","add","append","extend"),2),
            Question("How can you start a comment in Python?",listOf("//","<!--","#",";;"),2),
            Question("What is the result of the expression: 4 + 3 * 2?",listOf("14","10","16","12"),1),
            Question("Which keyword is used to define a function in Python?",listOf("function","def","func","lambda"),1),
            Question("How do you create a dictionary in Python?",listOf("{}","[]","()","<>"),0),
            Question("What does the len() function return when applied to a list?",listOf("Number of items","Size in bytes","Length of the longest item","None"),0),
            Question("What will `print('5' + '3')` output?",listOf("8","53","Error","5 3"),1)
        ),
        "Machine Learning" to listOf(
            Question("What is the primary goal of supervised learning?",listOf(" Find patterns in data without labels","Map input data to output labels","Reduce the dimensionality of data","Detect anomalies in datasets"),1),
            Question("Which algorithm is used for classification problems?",listOf("Linear Regression","K-Means Clustering","Decision Tree","Principal Component Analysis"),2),
            Question("What is overfitting in machine learning?",listOf("A. Model performs well on unseen data","Model memorizes training data but fails on test data","Model has too few features","Model uses too little data for training"),1),
            Question("What is the purpose of a validation dataset?",listOf("To train the model","To test the model's final accuracy","To tune model parameters","To visualize the data"),2),
            Question( "Which evaluation metric is suitable for classification?",listOf("Mean Squared Error","R-Squared","Precision","Silhouette Score"),2),
            Question("Which of the following is an unsupervised learning technique?",listOf("Logistic Regression","Random Forest","K-Means Clustering","Gradient Boosting"),2),
            Question("what is the main concept behind gradient descent?",listOf("Maximizing the loss function","Minimizing the loss function","Increasing learning rate","Using large datasets"),1),
            Question("What does 'epoch' mean in machine learning?",listOf("A single training iteration over a batch","A full pass through the entire dataset","A random subset of data","A hyperparameter of a model"),1),
            Question("Which of these is a feature scaling technique?",listOf("Mean Absolute Error","Normalization","Cross-Validation","Bagging") ,1),
            Question("What is a hyper parameter?",listOf("A parameter learned by the model","A parameter set before training","A parameter used for validation","A parameter used for testing") ,1),
        ),
        "SQL" to listOf(
            Question("Which SQL statement is used to retrieve data from a database?",listOf( "DELETE" ,"INSERT" ,"SELECT", "UPDATE"),2),
            Question("Which SQL clause is used to filter records?",listOf("WHERE","GROUP BY","ORDER BY","LIMIT"),0),
            Question("What does the COUNT() function do?",listOf( "Adds numerical values in a column","Counts the number of rows in a result set","Returns the maximum value in a column","Counts distinct values in a table"),1),
            Question("How do you select all columns from a table named \"users\"?",listOf("SELECT all FROM users;","SELECT * FROM users;","SELECT columns FROM users;","SELECT all_columns FROM users;"),1),
            Question("Which of the following is used to sort the results in SQL?",listOf( "GROUP BY","SORT BY","ORDER BY","ARRANGE"),2),
            Question("What is the correct syntax to create a table?",listOf("CREATE users TABLE;","CREATE TABLE users;","CREATE users;","CREATE TABLES users;"),1),
            Question("Which keyword is used to remove duplicate values in the result set?",listOf( "UNIQUE" ,"DISTINCT","REMOVE","EXCLUDE"),1),
            Question("Which JOIN returns all records from the left table and matched records from the right table?",listOf("INNER JOIN","LEFT JOIN","RIGHT JOIN","FULL OUTER JOIN"),1),
            Question("What does the SQL statement SELECT name FROM users WHERE age > 30; do?",listOf("Selects all users" ,"Selects names of users aged 30 and below","Selects names of users older than 30" ,"Selects names and ages of users older than 30"),2),
            Question("What is the result of this query: SELECT AVG(salary) FROM employees;?",listOf("The sum of all salaries","The average salary of employees","The maximum salary","The number of employees"),1),

            ),
        "Android" to listOf(
            Question("What is Android Studio?",listOf(" video editing tool","An IDE for Android development ","A photo editing software","A database management system"),1),
            Question("Which programming languages are supported by Android Studio for app development?",listOf("Python and JavaScript","Java and Kotlin","PHP and Ruby","C++ and Swift"),1),
            Question("What is the purpose of the Gradle build system in Android Studio?",listOf("To run the app on the device","To compile and build Android applications","To edit XML files","To manage database connections"),2),
            Question("What is the default layout file format used in Android Studio?",listOf("HTML","XML","JSON","CSS"),1),
            Question("Which file contains the application version and SDK information?",listOf("AndroidManifest.xml","build.gradle","MainActivity.kt","res/layout/activity_main.xml"),1),
            Question("What is the purpose of the AndroidManifest.xml file?",listOf("To define the UI layout of the app","To declare app components, permissions, and metadata","To store app data","To handle app logic"),1),
            Question("Which tool in Android Studio helps debug layout issues?",listOf("Logcat","Layout Inspector","AVD Manager","Gradle Console"),1),
            Question("What does AVD stand for in Android Studio?",listOf("Android Virtual Driver","Android Virtual Display","Android Virtual Device ","Android Video Device"),2),
            Question("Which method is called when an activity is first created?",listOf("onResume()","onCreate()","onStart()","onPause()"),1),
            Question("What is the purpose of the R.java file in Android Studio?",listOf("To manage app permissions","To map resources and IDs in the app","To store user data","To define the app database schema"),1),

            )
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        subjectSpinner = findViewById(R.id.subjectSpinner)
        questionTextView = findViewById(R.id.questionTextView)
        option1Button = findViewById(R.id.option1Button)
        option2Button = findViewById(R.id.option2Button)
        option3Button = findViewById(R.id.option3Button)
        option4Button = findViewById(R.id.option4Button)
        scoreTextView = findViewById(R.id.scoreTextView)

        // Spinner setup
        val subjects = quizData.keys.toList()
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, subjects)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        subjectSpinner.adapter = adapter

        subjectSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedSubject = subjects[position]
                selectedQuestions = quizData[selectedSubject] ?: listOf()
                resetQuiz()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        // Button setup
        val buttons = listOf(option1Button, option2Button, option3Button, option4Button)
        buttons.forEachIndexed { index, button ->
            button.setOnClickListener { checkAnswer(index) }
        }
    }

    private fun resetQuiz() {
        currentQuestionIndex = 0
        score = 0
        wrongQuestions.clear()
        scoreTextView.text = "Score: $score"
        loadQuestion()
    }

    private fun loadQuestion() {
        if (currentQuestionIndex < selectedQuestions.size) {
            val currentQuestion = selectedQuestions[currentQuestionIndex]
            questionTextView.text = currentQuestion.text
            option1Button.text = currentQuestion.options[0]
            option2Button.text = currentQuestion.options[1]
            option3Button.text = currentQuestion.options[2]
            option4Button.text = currentQuestion.options[3]
        } else {
            showResults()
        }
    }

    private fun checkAnswer(selectedOptionIndex: Int) {
        if (currentQuestionIndex < selectedQuestions.size) {
            val currentQuestion = selectedQuestions[currentQuestionIndex]
            if (selectedOptionIndex == currentQuestion.correctAnswerIndex) {
                score++
            } else {
                wrongQuestions.add(Pair(currentQuestion.text, currentQuestion.options[currentQuestion.correctAnswerIndex]))
            }
            currentQuestionIndex++
            scoreTextView.text = "Score: $score"
            loadQuestion()
        }
    }

    private fun showResults() {
        val intent = Intent(this, ResultActivity::class.java)
        intent.putExtra("score", score)
        intent.putExtra("totalQuestions", selectedQuestions.size)
        intent.putExtra("wrongQuestions", ArrayList(wrongQuestions))
        startActivity(intent)
    }
}

data class Question(
    val text: String,
    val options: List<String>,
    val correctAnswerIndex: Int
)