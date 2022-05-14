package com.drustii.widget;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.drustii.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputEditText;


public class GenderPicker extends BottomSheetDialogFragment {
    private TextInputEditText editText;
    private RadioGroup genderGroup;

    public GenderPicker(TextInputEditText editText) {
        this.editText=editText;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_gender_picker, container, false);
        genderGroup=view.findViewById(R.id.userGender);
        getSelected(view,genderGroup,editText);
        return view;
    }


    public void getSelected(View view,RadioGroup genderGroup,TextInputEditText editGender){
        genderGroup.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton rd= view.findViewById(checkedId);
            editGender.setText(rd.getText());
            closeWindow(GenderPicker.this);

        });
    }

    public void closeWindow(Fragment fr){
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        manager.getBackStackEntryCount();
        transaction.remove(fr);
        transaction.commit();
    }


}