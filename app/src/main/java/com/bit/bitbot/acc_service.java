package com.bit.bitbot;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.GestureDescription;
import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Path;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

public class acc_service extends AccessibilityService {
    public acc_service() {
    }
    int password[]={3,2,1,5,7,8,9};
    AccessibilityNodeInfo mNodeInfo;
    AccessibilityNodeInfo parentInfo;
    SharedPreferences sf;
    SharedPreferences.Editor sfe;

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        sf=getSharedPreferences("command",MODE_PRIVATE);
        sfe=sf.edit();
        mNodeInfo = event.getSource();
        if(mNodeInfo==null)
            return;
        parentInfo =gettosource(mNodeInfo);
        Nodeprinter(parentInfo, "");
        if(parentInfo!=null)
            actiontaken(parentInfo);
        if(sf.getBoolean("doexe",false)==true&&!isMyServiceRunning(acc_service.class))
          executeaction(parentInfo);
        else
            Log.e(":::","Either false execution or service running before");
        Log.v("FINAL:::", String.format("onAccessibilityEvent: type = [ %s ], class = [ %s ], package = [ %s ], time = [ %s ], text = [ %s ]", event.getEventType(), event.getClassName(), event.getPackageName(), event.getEventTime(), event.getText()));
    }

    @Override
    public void onInterrupt() {

    }
    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
    public void executeaction(AccessibilityNodeInfo node){

         Log.v("::::","work done ");
         sfe.putBoolean("doexe",false);
         sfe.apply();
    }
    private void actiontaken(AccessibilityNodeInfo node) {
        if (node.getChildCount() >= 1) {
            if (node.getChild(0) != null){
                if (node.getChild(0).getViewIdResourceName() != null) {
                    Log.e(":::", "done with:" + node.getChild(0).getViewIdResourceName());
                    if (node.getChild(0).getViewIdResourceName().equalsIgnoreCase("com.android.systemui:id/keyguard_indication_text")) {
                        Log.e(":::", "Finnaly execution");
                        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
                        GestureDescription.Builder gestureBuilder = new GestureDescription.Builder();
                        Path path = new Path();
                        int YValue = displayMetrics.heightPixels / 5;
                        int middle = displayMetrics.widthPixels / 2;
                        // int rightSizeOfScreen = leftSideOfScreen * 3;

                        path.moveTo(middle, YValue * 4);
                        path.lineTo(middle, YValue);

                        gestureBuilder.addStroke(new GestureDescription.StrokeDescription(path, 0, 1));
                        dispatchGesture(gestureBuilder.build(), new GestureResultCallback() {
                            @Override
                            public void onCompleted(GestureDescription gestureDescription) {
                                Log.w(":::", "Gesture Completed");
                                super.onCompleted(gestureDescription);
                            }
                        }, null);


                    }
                    else if(node.getChild(0).getViewIdResourceName().equalsIgnoreCase("com.android.systemui:id/keyguard_host_view")){
                        for(int i = 0;i< password.length; i++){
                            Log.e(":::","executing typing"+node.getChild(0).getChild(password[i]+1).getViewIdResourceName());
                            node.getChild(0).getChild(password[i]+1).performAction(AccessibilityNodeInfo.ACTION_CLICK);


                        }

                        Log.e(":::", "Finnaly execution enter");
                        if(node.getChild(0).getChild(12)!=null)
                            node.getChild(0).getChild(12).performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    }
                }
            }

        }

    }
    public void openapp(String appname){
            Intent li = getPackageManager().getLaunchIntentForPackage(appname);
            startActivity(li);
    }

    public boolean clickview(AccessibilityNodeInfo nb){
        if(!nb.isClickable()){
            Log.e("::::>","Not clickable entity >"+ mNodeInfo.getViewIdResourceName());
            return false;
        }
          nb.performAction(AccessibilityNodeInfo.ACTION_CLICK);
          return true;
     }

     public void writeview(AccessibilityNodeInfo nb,String tt){
        Bundle bd =new Bundle();
         bd.putString(AccessibilityNodeInfoCompat.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, tt);
         nb.performAction(AccessibilityNodeInfoCompat.ACTION_SET_TEXT, bd);
     }


    private AccessibilityNodeInfo gettosource(AccessibilityNodeInfo node){
        if(node.getParent()==null)
            return node;
        else
            return gettosource(node.getParent());
    }

    private void Nodeprinter(AccessibilityNodeInfo mNodeInfo,String logu){
        if(mNodeInfo == null) return ;
        String log = "";
        log= logu+ "("+mNodeInfo.getText()+"=="+((mNodeInfo.getViewIdResourceName() != null)?mNodeInfo.getViewIdResourceName():"NO VIEW ID")+"("+((mNodeInfo.isClickable())?"CLICKABLE":"")+")"+ "<--"+((mNodeInfo.getParent() != null)?mNodeInfo.getParent().getViewIdResourceName():"NO PARENT")+")";
        Log.d("::::", log);
        if(mNodeInfo.getChildCount()<1) return ;

        for(int i = 0; i < mNodeInfo.getChildCount(); i++){
            Nodeprinter(mNodeInfo.getChild(i),logu+"."+String.valueOf(i));
        }

    }


}
