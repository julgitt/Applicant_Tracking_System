package com.example.applicanttrackingsystem
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.applicanttrackingsystem.ui.login.LoginActivity

class StartActivity : AppCompatActivity() {
    private lateinit var buttonCompanies : Button
    private lateinit var buttonFreelancers : Button
    private lateinit var newActivity : Intent
    private lateinit var newBundle : Bundle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
        buttonCompanies = findViewById(R.id.start_for_companies_button)
        buttonFreelancers = findViewById(R.id.start_for_freelancers_button)
        newActivity = Intent(this,LoginActivity::class.java)
        newBundle = Bundle()

        buttonCompanies.setOnClickListener {
            newBundle.putString("type","company")
            newActivity.putExtras(newBundle)
            startActivity(newActivity)
        }
        buttonFreelancers.setOnClickListener {
            newBundle.putString("type","freelancer")
            newActivity.putExtras(newBundle)
            startActivity(newActivity)
        }
    }
}