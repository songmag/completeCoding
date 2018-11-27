package com.example.user.dooropenservice.app.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.user.dooropenservice.R;

public class SelectCompanyActivity extends Activity {
    Button finishBtn;
    RadioGroup radioGroup;
    String companyName = "세종대학교";
    Intent intent;
    private final int RESULT_OK=100;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_select_company);

        intent = new Intent();

        radioGroup =(RadioGroup)findViewById(R.id.radioGroup);
        finishBtn = (Button)findViewById(R.id.finishBtn);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.sejongUniv:
                        companyName = "세종대학교";
                        break;
                    case R.id.konkukUniv:
                        companyName = "건국대학교";
                        break;
                    case R.id.samsungElec:
                        companyName = "삼성전자";
                        break;
                    default:
                        companyName = "더미";
                        break;
                }
            }
        });

        finishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("compName",companyName);
                Toast.makeText(getApplicationContext(),companyName,Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK,intent);
                finish();
            }
        });
    }

    public void onBackPressed(){
        return;
    }
}