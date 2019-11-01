package com.dhammika_dev.justgo.ui.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.dhammika_dev.justgo.R;
import com.dhammika_dev.justgo.common.CommonUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import pub.devrel.easypermissions.EasyPermissions;


public class ProPicBottomSheetFragment extends BottomSheetDialogFragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int READ_REQUEST_CODE = 300;
    @BindView(R.id.cameraLayout)
    LinearLayout cameraLayout;
    @BindView(R.id.galleryLayout)
    LinearLayout galleryLayout;
    @BindView(R.id.cancelLayout)
    LinearLayout cancelLayout;
    @BindView(R.id.bottomSheet)
    LinearLayout bottomSheet;
    CircleImageView selectedImageView;
    Integer REQUEST_CAMERA = 200, SELECT_FILE = 1;
    Bitmap image;
    String imageEncoded;
    String id;
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;

    public ProPicBottomSheetFragment() {
        // Required empty public constructor
    }

    public static ProPicBottomSheetFragment newInstance(String param1, String param2) {
        ProPicBottomSheetFragment fragment = new ProPicBottomSheetFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.bottom_sheet, container, false);
        ButterKnife.bind(this, rootView);

        final String[] perms_camera = {Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        final String[] perms_gallery = {Manifest.permission.READ_EXTERNAL_STORAGE};

        Drawable mDrawable21 = this.getResources().getDrawable(R.drawable.camera);
        mDrawable21.setColorFilter(new
                PorterDuffColorFilter(getResources().getColor(R.color.colorPrimaryDark), PorterDuff.Mode.MULTIPLY));
        Drawable mDrawable22 = this.getResources().getDrawable(R.drawable.ic_folder_png);
        mDrawable22.setColorFilter(new
                PorterDuffColorFilter(getResources().getColor(R.color.colorPrimaryDark), PorterDuff.Mode.MULTIPLY));
        Drawable mDrawable23 = this.getResources().getDrawable(R.drawable.ic_cancel);
        mDrawable23.setColorFilter(new
                PorterDuffColorFilter(getResources().getColor(R.color.colorPrimaryDark), PorterDuff.Mode.MULTIPLY));

        cameraLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (EasyPermissions.hasPermissions(getActivity(), perms_camera)) {
                    Intent intent = new Intent();
                    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false);
                    intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, REQUEST_CAMERA);
                } else {
                    EasyPermissions.requestPermissions(getActivity(), getString(R.string.camera_permision_msg),
                            REQUEST_CAMERA, perms_camera);
                }
            }
        });

        galleryLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (EasyPermissions.hasPermissions(getActivity(), perms_gallery)) {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false);
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_FILE);
                } else {
                    EasyPermissions.requestPermissions(getActivity(), getString(R.string.read_file),
                            READ_REQUEST_CODE, perms_gallery);
                }
            }
        });

        cancelLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        return rootView;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CAMERA && null != data) {
            if (data != null) {
                Bundle extras = data.getExtras();
                Bitmap image = (Bitmap) extras.get("data");
                Uri tempUri = getImageUri(getActivity(), image);

                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                image.compress(Bitmap.CompressFormat.JPEG, 50, bytes);

                imageEncoded = CommonUtils.getInstance().getPathFromUri(getActivity(), tempUri);
                requestCameraUploadSurvey(image, tempUri, imageEncoded);
            }
        } else if (resultCode == Activity.RESULT_OK && requestCode == SELECT_FILE && null != data) {
            if (data.getData() != null) {
                Uri selectedImageUri = null;
                InputStream imageStream = null;
                try {
                    selectedImageUri = data.getData();
                    imageStream = getActivity().getContentResolver().openInputStream(selectedImageUri);
                    imageEncoded = CommonUtils.getInstance().getRealPathFromURI(getActivity(), selectedImageUri);
                    requestGalleryUploadSurvey(imageStream, selectedImageUri, imageEncoded);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        dismiss();
    }

    private Bitmap decodeUri(Uri selectedImage) throws FileNotFoundException {
        BitmapFactory.Options op = new BitmapFactory.Options();
        op.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(
                getActivity().getContentResolver().openInputStream(selectedImage), null, op);

        final int REQUIRED_SIZE = 100;

        int width_tmp = op.outWidth, height_tmp = op.outHeight;
        int scale = 1;
        while (true) {
            if (width_tmp / 2 < REQUIRED_SIZE || height_tmp / 2 < REQUIRED_SIZE) {
                break;
            }
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }

        BitmapFactory.Options op2 = new BitmapFactory.Options();
        op2.inSampleSize = scale;
        return BitmapFactory.decodeStream(
                getActivity().getContentResolver().openInputStream(selectedImage), null, op2);
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 50, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }


    private void requestCameraUploadSurvey(Bitmap image, Uri tempUri, String encodedImage) {
        MultipartBody.Part profileImage;
        File file = new File(encodedImage);
        RequestBody surveyBody = RequestBody.create(MediaType.parse("image/*"), file);
        profileImage = MultipartBody.Part.createFormData("profile_pic[]", file.getName(), surveyBody);
        if (profileImage != null) {
            if (SignUpContinueProfileFragment.profileFragment != null)
                SignUpContinueProfileFragment.profileFragment.setProfileImage(image, tempUri, profileImage);
        }
    }

    private void requestGalleryUploadSurvey(InputStream inputStream, Uri profileImgUri, String encodedImage) {
        MultipartBody.Part profileImage;
        File file = new File(encodedImage);
        RequestBody surveyBody = RequestBody.create(MediaType.parse("image/*"), file);
        profileImage = MultipartBody.Part.createFormData("profile_pic[]", file.getName(), surveyBody);
        if (profileImage != null) {
            if (SignUpContinueProfileFragment.profileFragment != null)
                SignUpContinueProfileFragment.profileFragment.setGalleryProfileImage(inputStream, profileImgUri, profileImage);
        }
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
