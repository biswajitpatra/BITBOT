package com.bit.bitbot;

import android.annotation.TargetApi;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
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
    //TODO: change the compilation from gui thread to async thread by checking every second
    String command_words[]={"open lock","open app","back","open","write","type","quit","close","click"};
    boolean runasync=true;
    EditText ed2;
    EditText ed;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sf= getSharedPreferences("command",MODE_PRIVATE);
        sfe=sf.edit();
        ed2=findViewById(R.id.editText2);
        ed2.setFocusable(false);
        ed2.setFocusableInTouchMode(false);
        ed2.setClickable(false);
        printcomm(sf.getString("commf","NONE"));
        ed=findViewById(R.id.editText);
        ed.setText(sf.getString("com","NONE"));
        new compiler().execute();
        /*ed.addTextChangedListener(new TextWatcher() {
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
        });*/
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
        printcomm(cmdt);
        /*
        cmdt=cmdt.replace("<","<<");
        cmdt=cmdt.replace(">",">>");
        cmdt=cmdt.replace("<<","<font color='red'>");
        cmdt=cmdt.replace(">>","</font>");
        cmdt=cmdt.replace("\n","</br>");
        //cmdt.replace(">"," />");
        Log.v("::: trial",cmdt);
        EditText ed2=findViewById(R.id.editText2);
        ed2.setText(Html.fromHtml(cmdt));
        //ed2.setText(cmdt);
        */
    }
    public void executest(View v){
         sfe.putBoolean("doexe",true);
         sfe.putInt("stepno",0);
         sfe.apply();
         runasync=false;
    }

    public void printcomm(String ss){
        ss=ss.replace("<","<<");
        ss=ss.replace(">",">>");
        ss=ss.replace("<<","<font color='red'><b>");
        ss=ss.replace(">>","</b></font>");
        ss=ss.replace("\n","<br>");
        EditText ed2=findViewById(R.id.editText2);
        ed2.setText(Html.fromHtml(ss));
    }

    private class compiler extends AsyncTask<Void,Void,Void>{


        @Override
        protected Void doInBackground(Void... voids) {
            while(runasync){
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                publishProgress();

            }
            return null;
        }
        @TargetApi(Build.VERSION_CODES.O)
        @Override
        protected void onProgressUpdate(Void... values){
            //achange_text(ed.getText().toString());
            String cmdt="";
            String ttsp[]=ed.getText().toString().split("\n");
            for(int x=0;x<ttsp.length;x++){
                for(String y:command_words){
                    if(ttsp[x].startsWith(y)){
                        //Log.e("trial :::",y);
                        int cutui=ttsp[x].indexOf(y)+y.length();
                        //Log.e("trial  ::::",String.valueOf(cutui));
                        if(y.equalsIgnoreCase("open app")) {
                          String rapp = "error";
                          if(ttsp[x].length()>=cutui+1){
                            List<ApplicationInfo> packages;
                            PackageManager pm;
                            pm = getPackageManager();

                            // get a list of installed apps.
                            packages = pm.getInstalledApplications(0);

                            for (ApplicationInfo packageInfo : packages)
                                if (pm.getApplicationLabel(packageInfo).toString().equalsIgnoreCase(ttsp[x].substring(cutui + 1)))
                                    rapp = packageInfo.packageName;

                          }
                          ttsp[x]="<"+ttsp[x].substring(0,cutui)+"> "+rapp;
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
            cmdt=cmdt.replace("<","<<");
            cmdt=cmdt.replace(">",">>");
            cmdt=cmdt.replace("<<","<font color='red'><b>");
            cmdt=cmdt.replace(">>","</b></font>");
            cmdt=cmdt.replace("\n","<br>");
            ed2=findViewById(R.id.editText2);
            ed2.setText(Html.fromHtml(cmdt));


            sfe.putString("com",ed.getText().toString());
            sfe.apply();
        }
        @TargetApi(Build.VERSION_CODES.O)
        public void achange_text(String tt){
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
            aprintcomm(cmdt);
        }
        public void aprintcomm(String ss){
            ss=ss.replace("<","<<");
            ss=ss.replace(">",">>");
            ss=ss.replace("<<","<font color='red'><b>");
            ss=ss.replace(">>","</b></font>");
            ss=ss.replace("\n","<br>");
            ed2=findViewById(R.id.editText2);
            ed2.setText(Html.fromHtml(ss));
        }
    }
}
