package com.drustii.widget;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.drustii.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputEditText;


public class VideoPrivacySelectorFragment extends BottomSheetDialogFragment {

    private TextInputEditText editText;
    private RadioGroup videoPrivacy;

    public VideoPrivacySelectorFragment(TextInputEditText editText) {
        this.editText = editText;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_video_privacy_selector, container, false);
        videoPrivacy = v.findViewById(R.id.videoPrivacy);
        getSelected(v, videoPrivacy, editText);

        return v;
    }

    public void getSelected(View view, RadioGroup radioGroup, TextInputEditText editText) {
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton rd = view.findViewById(checkedId);
            editText.setText(rd.getText());
            closeWindow(VideoPrivacySelectorFragment.this);

        });
    }

    public void closeWindow(Fragment fr) {
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        manager.getBackStackEntryCount();
        transaction.remove(fr);
        transaction.commit();
    }
}