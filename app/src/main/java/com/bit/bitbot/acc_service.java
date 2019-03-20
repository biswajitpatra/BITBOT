package com.bit.bitbot;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.GestureDescription;
import android.app.Service;
import android.content.Intent;
import android.graphics.Path;
import android.os.IBinder;
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
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        mNodeInfo = event.getSource();
        if(mNodeInfo==null)
            return;
        parentInfo =gettosource(mNodeInfo);
        Nodeprinter(parentInfo, "");
        if(parentInfo!=null)
            actiontaken(parentInfo);
        Log.v("FINAL:::", String.format("onAccessibilityEvent: type = [ %s ], class = [ %s ], package = [ %s ], time = [ %s ], text = [ %s ]", event.getEventType(), event.getClassName(), event.getPackageName(), event.getEventTime(), event.getText()));
    }

    @Override
    public void onInterrupt() {

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
