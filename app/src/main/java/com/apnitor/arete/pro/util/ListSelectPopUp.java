package com.apnitor.arete.pro.util;

import android.app.AlertDialog;
import android.os.Build;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;

public class ListSelectPopUp implements View.OnClickListener, AdapterView.OnItemClickListener {

    private TextView mTexView;
    private AlertDialog.Builder builder;
    private SimpleDateFormat mFormat;

    private String selectedId;

    public ListSelectPopUp(TextView editText) {
        this.mTexView = editText;
        mTexView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        showPicker(view);
    }

    private void showPicker(View view) {

        ListView listView = new ListView(view.getContext());

        if (builder == null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                builder = new AlertDialog.Builder(view.getContext(), android.R.style.Theme_Material_Dialog_Alert);
            } else {
                builder = new AlertDialog.Builder(view.getContext());
            }
            builder.setTitle("Select");
        }
        builder.show();
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mTexView.setText("sdfsdf");
        selectedId = "";
    }

    public String getSelectedId() {
        return selectedId;
    }
}