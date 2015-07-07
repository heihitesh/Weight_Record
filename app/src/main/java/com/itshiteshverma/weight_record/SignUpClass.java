package com.itshiteshverma.weight_record;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Hitesh Verma on 7/6/2015.
 */
public class SignUpClass extends Activity implements View.OnClickListener {

    Button Next;
    EditText Name,UName,Pass,CPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personal_details);
        intilize();
    }

    private void intilize() {
        Next = (Button) findViewById(R.id.bNext);
        Next.setOnClickListener(this);
        Name = (EditText) findViewById(R.id.etName);
        UName = (EditText) findViewById(R.id.etUsername);
        Pass= (EditText) findViewById(R.id.etPassword);
        CPass = (EditText) findViewById(R.id.etConfirmPassword);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.bNext:
                if(Pass.getText().toString().equals(CPass.getText().toString())){ //means the password is same

                    Intent intent = new Intent(SignUpClass.this,BodyType.class);
                    intent.putExtra("Name",Name.getText().toString());
                    intent.putExtra("UName",UName.getText().toString());
                    intent.putExtra("Pass",Pass.getText().toString());
                    startActivity(intent);  //open a new activtiy with all this information
                }
                else {
                    Pass.setBackgroundColor(Color.RED);
                    CPass.setBackgroundColor(Color.RED);
                    Toast.makeText(this,"Password Don't Match",Toast.LENGTH_SHORT).show();
                }
                break;

        }
    }
}
