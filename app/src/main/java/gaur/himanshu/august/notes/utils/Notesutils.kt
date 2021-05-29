package gaur.himanshu.august.notes.utils

import android.content.Context
import android.view.View
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar

fun View.makeSnack(message: String) {
    Snackbar.make(this, message, Snackbar.LENGTH_SHORT).show()
}

fun Context.makeToast(message: String){
    Toast.makeText(this,message,Toast.LENGTH_SHORT).show()
}