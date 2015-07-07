package com.itshiteshverma.weight_record;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.SQLException;

/**
 * Created by Hitesh Verma on 6/10/2015.
 */
public class WeightDataBaseView extends Activity {

    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.database_view);
        tv = (TextView) findViewById(R.id.tvDBView);


        WeightDataBase info = new WeightDataBase(this);
        try {
            info.open();
        } catch (SQLException e) {
            e.printStackTrace();
            Toast.makeText(this, "Exception Caught at SQLView Class", Toast.LENGTH_SHORT).show();
        }
        String data = info.getData();
        info.close();
        tv.setText(data);

    }

    }


