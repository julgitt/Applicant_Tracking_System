package com.example.applicanttrackingsystem
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.applicanttrackingsystem.databinding.ActivityTypeChoiceBinding
import com.example.applicanttrackingsystem.ui.register.RegisterCompanyActivity
import com.example.applicanttrackingsystem.ui.register.RegisterFreelancerActivity

class TypeChoiceActivity : AppCompatActivity() {
    private lateinit var binding : ActivityTypeChoiceBinding
    private lateinit var companiesButton : Button
    private lateinit var freelancersButton : Button
    private lateinit var freelancerActivity : Intent
    private lateinit var companyActivity : Intent
    //private lateinit var newActivity : Intent
    private lateinit var newBundle : Bundle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTypeChoiceBinding.inflate(layoutInflater)
        setContentView(binding.root)
        companiesButton = binding.startForCompaniesButton
        freelancersButton = binding.startForFreelancersButton
        freelancerActivity = Intent(this, RegisterFreelancerActivity::class.java)
        companyActivity = Intent(this,RegisterCompanyActivity::class.java)
        //newActivity = Intent(this,LoginActivity::class.java)
        //newBundle = Bundle()

        companiesButton.setOnClickListener {
            //newBundle.putString("type","company")
            //newActivity.putExtras(newBundle)
            startActivity(companyActivity)
        }
        freelancersButton.setOnClickListener {
            //newBundle.putString("type","freelancer")
            //newActivity.putExtras(newBundle)
            startActivity(freelancerActivity)
        }
    }
}