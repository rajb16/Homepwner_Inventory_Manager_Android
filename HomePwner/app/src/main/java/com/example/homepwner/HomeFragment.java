package com.example.homepwner;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class HomeFragment extends Fragment {
    private static final String ARG_ITEM_ID = "item_id";
    private static final String DIALOG_DATE = "DialogDate";
    private static final int REQUEST_DATE = 0;
    private static final int REQUEST_PHOTO= 1;
    private Button mDateButton;
    private Home mHome;
    private File mPhotoFile;
    private EditText name;
    private EditText serial;
    private EditText value;
    private Button mReportButton;
    private ImageButton mPhotoButton;
    private ImageView mPhotoView;
    private Callbacks mCallbacks;
    /**
     * Required interface for hosting activities
     */
    public interface Callbacks {
        void onItemUpdated(Home item);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID itemId = (UUID) getArguments().getSerializable(ARG_ITEM_ID);
        mHome = HomePwnLab.get(getActivity()).getItem(itemId);
        mPhotoFile = HomePwnLab.get(getActivity()).getPhotoFile(mHome);
    }

    public static HomeFragment newInstance(UUID itemId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_ITEM_ID, itemId);
        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallbacks = (Callbacks) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        mDateButton = (Button) v.findViewById(R.id.date);
        updateDate();
        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
                DatePickerFragment dialog =  DatePickerFragment.newInstance(mHome.getDate());
                dialog.setTargetFragment(HomeFragment.this, REQUEST_DATE);
                dialog.show(manager, DIALOG_DATE);
            }
        });



        name = (EditText) v.findViewById(R.id.home_name);
        name.setText(mHome.getName());
        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(
                    CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(
                    CharSequence s, int start, int before, int count) {
                mHome.setName(s.toString());
                updateItem();
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });


        if (mHome.getSerial() == null){
            mHome.setSerial();
        }
        serial = (EditText) v.findViewById(R.id.home_serial);
        serial.setText(mHome.getSerial());
        serial.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateItem();
            }

            public void afterTextChanged(Editable s) {
            }
        });
        value = (EditText) v.findViewById(R.id.home_value);
        String valueInDollars = "" + mHome.getValue();
        value.setText(valueInDollars);
        value.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                double valueConv ;
                try{
                    valueConv = Double.parseDouble(s.toString());
                }
                catch (NumberFormatException exception) {
                    valueConv = 0;
                }
                mHome.setValue(valueConv);
                updateItem();

            }
            public void afterTextChanged(Editable s) {}
        });


        mReportButton = (Button) v.findViewById(R.id.home_report_button);
        mReportButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_TEXT, getItemReport());
                i.putExtra(Intent.EXTRA_SUBJECT,
                        getString(R.string.subject));
                i = Intent.createChooser(i, getString(R.string.send_report));
                startActivity(i);
            }
        });

        PackageManager packageManager = getActivity().getPackageManager();
        mPhotoButton = (ImageButton) v.findViewById(R.id.item_camera);
        final Intent captureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        boolean canTakePhoto = mPhotoFile != null &&
                captureImage.resolveActivity(packageManager) != null;
        mPhotoButton.setEnabled(canTakePhoto);
        mPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = FileProvider.getUriForFile(getActivity(),
                        "com.example.homepwner.fileprovider",
                        mPhotoFile);
                captureImage.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                List<ResolveInfo> cameraActivities = getActivity()
                        .getPackageManager().queryIntentActivities(captureImage,
                                PackageManager.MATCH_DEFAULT_ONLY);
                for (ResolveInfo activity : cameraActivities) {
                    getActivity().grantUriPermission(activity.activityInfo.packageName,
                            uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                }
                startActivityForResult(captureImage, REQUEST_PHOTO);
            }
        });
        mPhotoView = (ImageView) v.findViewById(R.id.item_photo);
        updatePhotoView();
        return v;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_DATE) {
            Date date = (Date) data
                    .getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mHome.setDate(date);
            updateItem();
            updateDate();
        }
        else if(requestCode == REQUEST_PHOTO)
        {
            Uri uri = FileProvider.getUriForFile(getActivity(), "com.example.homepwner.fileprovider", mPhotoFile);
            getActivity().revokeUriPermission(uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            updateItem();
            updatePhotoView();
        }
    }

    private void updateItem() {
       // HomePwnLab.get(getActivity()).updateItem(mHome);
        mCallbacks.onItemUpdated(mHome);
    }

    private void updateDate() {
        String[] dateSplit = mHome.getDate().toString().split(" ");
        String dateString = dateSplit[0] + " " + dateSplit[1] + " " + dateSplit[2] + ", " + dateSplit[5];
        mDateButton.setText(dateString);
    }

    private String getItemReport()
    {
        String dateFormat = "EEE, MMM dd";
        String dateString = DateFormat.format(dateFormat, mHome.getDate()).toString();
        String mName = mHome.getName();
        if(mName == null)
        {
            mName = getString(R.string.no_name_provided);
        }
        else
        {
            mName = getString(R.string.home_report_name) + " " + mName;
        }
        String mSerial = mHome.getSerial();
        mSerial = getString(R.string.home_report_serial) + " "  + mSerial;
        String mValue = getString(R.string.home_report_value) + mHome.getValue();
        String report = getString(R.string.home_report,  mName, dateString, mSerial, mValue);
        return report;
    }
    private void updatePhotoView() {
        if (mPhotoFile == null || !mPhotoFile.exists()) {
            mPhotoView.setImageDrawable(null);
        } else {
            Bitmap bitmap = PictureUtils.getScaledBitmap(
                    mPhotoFile.getPath(), getActivity());
            mPhotoView.setImageBitmap(bitmap);
        }
    }
}
