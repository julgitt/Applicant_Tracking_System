package com.example.applicanttrackingsystem
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.applicanttrackingsystem.databinding.ActivityStartBinding
import com.example.applicanttrackingsystem.ui.login.LoginActivity

class StartActivity : AppCompatActivity() {
    private lateinit var binding : ActivityStartBinding
    private lateinit var companiesButton : Button
    private lateinit var freelancersButton : Button
    private lateinit var newActivity : Intent
    private lateinit var newBundle : Bundle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
        binding = ActivityStartBinding.inflate(layoutInflater)
        companiesButton = binding.startForCompaniesButton
        freelancersButton = binding.startForFreelancersButton
        newActivity = Intent(this,LoginActivity::class.java)
        newBundle = Bundle()

        companiesButton.setOnClickListener {
            newBundle.putString("type","company")
            newActivity.putExtras(newBundle)
            startActivity(newActivity)
        }
        freelancersButton.setOnClickListener {
            newBundle.putString("type","freelancer")
            newActivity.putExtras(newBundle)
            startActivity(newActivity)
        }
    }
}