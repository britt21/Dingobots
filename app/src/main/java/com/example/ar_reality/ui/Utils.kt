package com.example.ar_reality.ui

import android.app.Activity
import android.content.Context
import android.view.WindowManager
import android.widget.Toast


fun fullScreen(activity: Activity){
    activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
}


fun showToast(context: Context,message: String){
    Toast.makeText(context,message,Toast.LENGTH_SHORT).show()
}