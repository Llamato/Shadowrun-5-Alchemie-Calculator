package com.cinua.shadowrun5alchemiecalculator;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.text.InputType;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import com.example.shadowrun5alchemiecalculator.R;

import java.io.IOException;
import java.util.Arrays;

import static com.cinua.shadowrun5alchemiecalculator.EnchantmentCalculationActivity.*;

public class StatEntryActivity extends AppCompatActivity{
    private static final int IMPORT_FILE_RESULT_CODE = 47799;
    private static final int EXPORT_FILE_RESULT_CODE = 48000;

    private EditText nameTextbox;
    private EditText magicTextbox;
    private EditText skillTextbox;
    private EditText drainPoolTextbox;
    private EditText stateMonitorTextbox;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stat_entry);

        LinearLayout nameLayout = findViewById(R.id.char_name_section);
        TextView nameLabel = nameLayout.findViewById(R.id.section_title);
        nameLabel.setText(R.string.character_name);
        nameTextbox = nameLayout.findViewById(R.id.section_input);
        nameTextbox.setInputType(InputType.TYPE_CLASS_TEXT);
        nameTextbox.setText("");

        LinearLayout magicLayout = findViewById(R.id.magic_section);
        TextView magicLabel = magicLayout.findViewById(R.id.section_title);
        magicLabel.setText(R.string.magic);
        magicTextbox = magicLayout.findViewById(R.id.section_input);

        LinearLayout skillLayout = findViewById(R.id.skill_section);
        TextView skillLabel = skillLayout.findViewById(R.id.section_title);
        skillLabel.setText(R.string.skill);
        skillTextbox = skillLayout.findViewById(R.id.section_input);

        LinearLayout drainPoolLayout = findViewById(R.id.drain_pool_section);
        TextView drainPoolLabel = drainPoolLayout.findViewById(R.id.section_title);
        drainPoolLabel.setText(R.string.drain_pool);
        drainPoolTextbox = drainPoolLayout.findViewById(R.id.section_input);

        LinearLayout stateMonitorLayout = findViewById(R.id.state_monitor_section);
        TextView stateMonitorlabel = stateMonitorLayout.findViewById(R.id.section_title);
        stateMonitorlabel.setText(R.string.state_monitor);
        stateMonitorTextbox = stateMonitorLayout.findViewById(R.id.section_input);

        Button loadFromFileButton = findViewById(R.id.load_button);
        loadFromFileButton.setOnClickListener(new LoadFromFileButtonHandler());

        Button saveExternallyButton = findViewById(R.id.save_externally_button);
        saveExternallyButton.setOnClickListener(new SavetoExternallyButtonHandler());

        Button saveInternallyButton = findViewById(R.id.remember_button);
        saveInternallyButton.setOnClickListener(new SaveInternallyButtonHandler());

        askForStoragePermission();
        /*if(!askForStoragePermission()){
            loadButton.setEnabled(false);
            saveButton.setEnabled(false);
        }*/

        Button continueButton = findViewById(R.id.continue_button);
        continueButton.setOnClickListener(new ContinueButtonHandler());
        try{
            if(Arrays.asList(fileList()).contains(Savefile.FILE_NAME)){
                loadStatsFromSavefile(Savefile.loadFromInternalFile(this, Savefile.FILE_NAME));
            }
        }catch(IOException e){
            UserMessagingHelper.showIoErrorAlert(this);
            e.printStackTrace();
        }
    }

    private void loadStatsFromSavefile(Savefile savefile){
        nameTextbox.setText(savefile.getCharacterName());
        magicTextbox.setText(savefile.getMagicRating());
        skillTextbox.setText(savefile.getAlchemyRating());
        drainPoolTextbox.setText(savefile.getDrainPool());
        stateMonitorTextbox.setText(savefile.getStateMonitor());
    }

    private void saveStatsToInternalFile(){
        Savefile charFile = new Savefile(nameTextbox.getText().toString(), magicTextbox.getText().toString(), skillTextbox.getText().toString(), drainPoolTextbox.getText().toString(), stateMonitorTextbox.getText().toString());
        if(Arrays.asList(fileList()).contains(Savefile.FILE_NAME)){
            deleteFile(Savefile.FILE_NAME);
        }
        try{
            charFile.saveToInternalFile(this);
        }catch(IOException e){
            UserMessagingHelper.showIoErrorAlert(this);
            e.printStackTrace();
        }
    }

    private void saveStatsToExternalFile(Uri choice){
        Savefile charFile = new Savefile(nameTextbox.getText().toString(), magicTextbox.getText().toString(), skillTextbox.getText().toString(), drainPoolTextbox.getText().toString(), stateMonitorTextbox.getText().toString());
        try{
            charFile.saveToExternalFile(this, choice);
        }catch(IOException e){
            UserMessagingHelper.showIoErrorAlert(this);
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == IMPORT_FILE_RESULT_CODE && resultCode == RESULT_OK){
            try{
                loadStatsFromSavefile(Savefile.loadFromUri(this, data.getData()));
            }catch(IOException e){
                UserMessagingHelper.showIoErrorAlert(this);
                e.printStackTrace();
            }
        }else if(requestCode == EXPORT_FILE_RESULT_CODE && resultCode == RESULT_OK){
            saveStatsToExternalFile(data.getData());
        }
    }

    private boolean askForStoragePermission(){
        if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
            //Permission is granted
            return true;
        }else{
            //Permission is revoked
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            return false;
        }
    }

    private boolean putExtraFromTextbox(Context context, Intent intent, String key, EditText textbox){
        try{
            intent.putExtra(key, Integer.parseInt(textbox.getText().toString()));
        }catch(NumberFormatException e){
            UserMessagingHelper.handleUserInputNumberFormatError(context, textbox, e);
            return false;
        }
        return true;
    }

    private class LoadFromFileButtonHandler implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
            chooseFile.setType("*/*");
            chooseFile.addCategory(Intent.CATEGORY_OPENABLE);
            chooseFile = Intent.createChooser(chooseFile, getString(R.string.import_file_chooser_title));
            startActivityForResult(chooseFile, IMPORT_FILE_RESULT_CODE);
        }
    }

    private class SavetoExternallyButtonHandler implements View.OnClickListener{

        @Override
        public void onClick(View v){
            Intent exportIntent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
            exportIntent.setType("*/*");
            exportIntent.addCategory(Intent.CATEGORY_OPENABLE);
            exportIntent.putExtra(Intent.EXTRA_TITLE, nameTextbox.getText().toString() + Savefile.FILE_EXTENSION);
            startActivityForResult(exportIntent, EXPORT_FILE_RESULT_CODE);
        }
    }

    private class SaveInternallyButtonHandler implements View.OnClickListener{

        @Override
        public void onClick(View v){
            saveStatsToInternalFile();
            Toast.makeText(v.getContext(), R.string.stats_saved, Toast.LENGTH_LONG).show();
        }
    }

    private class ContinueButtonHandler implements View.OnClickListener{

        @Override
        public void onClick(View v){
            Context context = v.getContext();
            Intent starter = new Intent(context, EnchantmentCalculationActivity.class);
            starter.putExtra(NAME_KEY, nameTextbox.getText().toString());
            if(putExtraFromTextbox(context, starter, MAGIC_KEY, magicTextbox) && putExtraFromTextbox(context, starter, SKILL_KEY, skillTextbox) && putExtraFromTextbox(context, starter, DRAIN_POOL_KEY, drainPoolTextbox) && putExtraFromTextbox(context, starter, STATE_MONITOR_KEY, stateMonitorTextbox)){
                startActivity(starter);
            }
        }
    }
}