package com.example.ar_reality.ui

import android.app.Activity
import android.view.WindowManager


fun fullScreen(activity: Activity){
    activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
}
