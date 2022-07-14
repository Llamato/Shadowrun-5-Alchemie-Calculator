package com.cinua.shadowrun5alchemiecalculator;
import android.app.AlertDialog;
import android.content.Context;
import android.widget.EditText;
import com.example.shadowrun5alchemiecalculator.R;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class UserMessagingHelper{

    public static void handleUserInputNumberFormatError(Context context, EditText textbox, NumberFormatException exception){
        textbox.setError(context.getString(R.string.invalid_entry));
        exception.printStackTrace();
    }

    private static void showAlertMaterial(Context c, int title, int message){
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(c);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }

    private static void showAlertClassic(Context c, int title, int message){
        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }

    private static void showAlert(Context c, int title, int message){
        try{
            showAlertMaterial(c, title, message);
        }catch(UnsupportedOperationException e){
            showAlertClassic(c, title, message);
        }
    }

    public static void showIoErrorAlert(Context c){
        showAlert(c, R.string.alert_title, R.string.io_error_alert_content);
    }

    public static  void showUnknownErrorAlert(Context c){
        showAlert(c, R.string.alert_title, R.string.unknown_error_message);
    }
}
