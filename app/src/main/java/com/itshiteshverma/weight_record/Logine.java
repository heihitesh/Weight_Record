package com.itshiteshverma.weight_record;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by Hitesh Verma on 7/5/2015.
 */
public class Logine extends Activity implements View.OnClickListener {

    Button SignUp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        initilize();
    }

    private void initilize() {
        SignUp = (Button) findViewById(R.id.bSignUp);
        SignUp.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){

            case R.id.bSignUp:
                Intent intent = new Intent(Logine.this,SignUpClass.class);
                startActivity(intent);

                break;

        }
    }
}
