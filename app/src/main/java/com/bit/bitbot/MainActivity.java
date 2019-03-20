package com.bit.bitbot;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    SharedPreferences sf;
    SharedPreferences.Editor sfe;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sf= getSharedPreferences("command",MODE_PRIVATE);
        sfe=sf.edit();

        EditText ed=findViewById(R.id.editText);
        ed.setText(sf.getString("com","NONE"));
        ed.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                     sfe.putString("com",s.toString());
                     sfe.apply();
            }
        });
    }

    public void executest(View v){
         sfe.putBoolean("doexe",true);
         sfe.apply();



    }
}
