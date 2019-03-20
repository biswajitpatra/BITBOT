package com.bit.bitbot;

import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    SharedPreferences sf;
    SharedPreferences.Editor sfe;

    String command_words[]={"open app","open","write","type","quit","close"};
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

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void afterTextChanged(Editable s) {
                     change_text(s.toString());
                     sfe.putString("com",s.toString());
                     sfe.apply();
            }
        });
    }
    public String changeapptop(String appname){
        List<ApplicationInfo> packages;
        PackageManager pm;
        pm = getPackageManager();

        // get a list of installed apps.
        packages = pm.getInstalledApplications(0);

        for (ApplicationInfo packageInfo : packages) {
           if(pm.getApplicationLabel(packageInfo).toString().equalsIgnoreCase(appname))
               return packageInfo.packageName;

        }

            return "error";
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void change_text(String tt){
        String cmdt="";
        String ttsp[]=tt.split("\n");
        for(int x=0;x<ttsp.length;x++){
            for(String y:command_words){
                if(ttsp[x].startsWith(y)){
                    //Log.e("trial :::",y);
                    int cutui=ttsp[x].indexOf(y)+y.length();
                    //Log.e("trial  ::::",String.valueOf(cutui));
                    if(y.equalsIgnoreCase("open app")){

                        ttsp[x]="<"+ttsp[x].substring(0,cutui)+"> "+changeapptop(ttsp[x].substring(cutui+1));
                        break;
                    }

                    ttsp[x]="<"+ttsp[x].substring(0,cutui)+">"+ttsp[x].substring(cutui);
                    break;
                }
            }
        }


        cmdt=String.join("\n",ttsp);
        sfe.putString("commf",cmdt);
        sfe.apply();
        //cmdt.replace("<","<font color=\'#EE0000\' ");
        //cmdt.replace(">"," />");
        EditText ed2=findViewById(R.id.editText2);
        //ed2.setText(Html.fromHtml(cmdt));
        ed2.setText(cmdt);
    }
    public void executest(View v){
         sfe.putBoolean("doexe",true);
         sfe.putInt("stepno",0);
         sfe.apply();



    }
}
