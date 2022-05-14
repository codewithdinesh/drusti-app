package com.drustii.account.create;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.drustii.R;
import com.drustii.utility.network.noNetworkFragment;
import com.drustii.utility.validateInput;
import com.drustii.widget.DataPickerView;
import com.drustii.widget.GenderPicker;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;

import java.net.URI;


public class userDetailsSetupFragment extends Fragment {
    // private userDetailsSetupFragment userDetailsSetupFragment=new userDetailsSetupFragment();
    private int PICK_IMAGE = 200;
    private URI imagePath;
    private String uPassword, uName, uDob, uGender, uEmail;
    private ImageView userImage;

    public userDetailsSetupFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (getArguments() != null) {
            uPassword = getArguments().getString("uPassword");
            uEmail=getArguments().getString("uEmail");
        }

        //inflate layout
        View view = inflater.inflate(R.layout.fragment_user_details_setup, container, false);

        EditText uDatePicker;
        TextInputEditText uGenderPicker, mName, mDob, mGender;
        MaterialCardView userImageContainer;
        Button nextBtn;

        uDatePicker = view.findViewById(R.id.userDOB);
        userImageContainer = view.findViewById(R.id.userImageContainer);
        userImage = view.findViewById(R.id.userImage);
        uGenderPicker = view.findViewById(R.id.userGender);
        nextBtn = view.findViewById(R.id.nextBtn);
        mName = view.findViewById(R.id.userName);
        validateInput validateInput = new validateInput();

        // Date Picker
        new DataPickerView(uDatePicker, "dd-MMM-yyyy");

        // Image Picker
        // Click Listener on userImage Container
        userImageContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImage();
            }
        });

        //Open Gender select window
        uGenderPicker.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    GenderPicker genderPicker = new GenderPicker(uGenderPicker);
                    genderPicker.show(getFragmentManager(), "fragment_gender_picker");
                }

            }
        });

        //On click open
        uGenderPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GenderPicker genderPicker = new GenderPicker(uGenderPicker);
                if (getFragmentManager() != null) {
                    genderPicker.show(getFragmentManager(), "fragment_gender_picker");
                }
            }
        });

        // ON click on next btn
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Validate input, Input should not be null
                uName = mName.getText().toString();
                uDob = uDatePicker.getText().toString();
                uGender = uGenderPicker.getText().toString();

                if (validateInput.validInput(uName)) {
                    if (validateInput.validInput(uDob)) {
                        if (validateInput.validInput(uGender)) {

                            displayFragment(uPassword, uName, uDob, uGender,uEmail);

                        } else {
                            displayError("Please choose Gender", "Ok");
                        }
                    } else {
                        displayError("Please choose Date of Birth ", "Ok");
                    }
                } else {
                    displayError("Please Enter name", "ok");
                }


            }
        });

        return view;
    }


    // Image picker
    public void pickImage() {

        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);

    }

    //Function when user selects the image from pickImage()

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // if user choosed image
        if (resultCode == RESULT_OK) {

            if (requestCode == PICK_IMAGE) {
                // GET the url of image from data
                Uri selectedImageUri = data.getData();
                if (null != selectedImageUri) {
                    userImage.setImageURI(selectedImageUri);
                }
            }
        }
    }

    // display next fragment of username
    public void displayFragment(String uPass, String uName, String uDob, String uGender, String uEmail) {

        // Next Fragment For User Details
        usernameFragment usernameFragment = new usernameFragment();

        // Passing user name, password, email, dob, gender to next fragment
        Bundle args = new Bundle();
        args.putString("uPass", uPass);
        args.putString("uName", uName);
        args.putString("uDob", uDob);
        args.putString("uGender", uGender);
        args.putString("uEmail",uEmail);
        usernameFragment.setArguments(args);

        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.SetupContainer, usernameFragment);
        fragmentTransaction.setReorderingAllowed(true);
        fragmentTransaction.addToBackStack("name");
        fragmentTransaction.commit();
    }

    // display error
    public void displayError(String msg, String BtnMsg) {
        noNetworkFragment networkFragment = new noNetworkFragment(msg, BtnMsg);
        FragmentManager fragmentManager = getFragmentManager();
        if (fragmentManager != null) {
            networkFragment.show(fragmentManager, "fragment_no_network");
        }
    }
}