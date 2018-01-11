package com.didlink.xingxing.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.didlink.xingxing.AppSingleton;
import com.didlink.xingxing.R;
import com.didlink.xingxing.activity.ImagesGridActivity;
import com.didlink.xingxing.activity.LoginActivity;
import com.didlink.xingxing.config.Constants;
import com.lezaizai.imagepicker.AndroidImagePicker;
import com.lezaizai.imagepicker.GlideImagePresenter;
import com.lezaizai.imagepicker.ImagePresenter;
import com.lezaizai.imagepicker.Util;
import com.lezaizai.imagepicker.bean.ImageItem;
import com.sina.cloudstorage.auth.AWSCredentials;
import com.sina.cloudstorage.auth.BasicAWSCredentials;
import com.sina.cloudstorage.services.scs.SCS;
import com.sina.cloudstorage.services.scs.SCSClient;
import com.sina.cloudstorage.services.scs.model.AccessControlList;
import com.sina.cloudstorage.services.scs.model.ObjectMetadata;
import com.sina.cloudstorage.services.scs.model.Permission;
import com.sina.cloudstorage.services.scs.model.PutObjectResult;
import com.sina.cloudstorage.services.scs.model.UserIdGrantee;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment implements AndroidImagePicker.OnPictureTakeCompleteListener,AndroidImagePicker.OnImageCropCompleteListener,AndroidImagePicker.OnImagePickCompleteListener{

    private static final String TAG = ProfileFragment.class.getSimpleName();
    private final int REQ_IMAGE = 1433;
    private final int REQ_IMAGE_CROP = 1435;
    private static final int REQ_LOGIN = 0;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private OnLogoutListener mLogoutListener;
    private ImageView mImageAvator;

    private ImagePresenter presenter = new GlideImagePresenter();
    private SelectAdapter mAdapter;
    private int screenWidth;

    private String mAvatarFilename;
    private TextView mNickView;
    private TextView mLoginnameView;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SharedPreferences mySharedPreferences = getActivity().getSharedPreferences(Constants.SHARED_PREFERENCE_NAME, Activity.MODE_PRIVATE);
        boolean ifLogin = mySharedPreferences.getBoolean(Constants.SHARED_PREFERENCE_KEY_IFLOGIN, false);

        if (ifLogin) {
            getActivity().findViewById(R.id.profile_head).setVisibility(View.VISIBLE);
            getActivity().findViewById(R.id.profile_login).setVisibility(View.INVISIBLE);

            mImageAvator = (ImageView) view.findViewById(R.id.profile_avator_image);
            screenWidth = getActivity().getWindowManager().getDefaultDisplay().getWidth();

            AndroidImagePicker.getInstance().addOnImageCropCompleteListener(this);

            Button logoutButton = (Button) view.findViewById(R.id.profile_logout);
            logoutButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SharedPreferences mySharedPreferences = getActivity().getSharedPreferences(Constants.SHARED_PREFERENCE_NAME, Activity.MODE_PRIVATE);
                    SharedPreferences.Editor editor = mySharedPreferences.edit();
                    editor.putBoolean(Constants.SHARED_PREFERENCE_KEY_IFLOGIN, false);
                    editor.commit();

                    if (mLogoutListener != null)
                        mLogoutListener.onLogout();
                }
            });
            Button avatorButton = (Button) view.findViewById(R.id.profile_avator);
            avatorButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent();
                    int requestCode = REQ_IMAGE_CROP;
                    AndroidImagePicker.getInstance().setSelectMode(AndroidImagePicker.Select_Mode.MODE_SINGLE);
                    AndroidImagePicker.getInstance().setShouldShowCamera(true);
                    intent.putExtra("isCrop", true);
                    intent.setClass(getActivity().getApplicationContext(),ImagesGridActivity.class);
                    startActivityForResult(intent, requestCode);
                }
            });

            mNickView = (TextView) view.findViewById(R.id.profile_text_nickname);
            mNickView.setText(AppSingleton.getInstance().getLoginAuth().getNickname());
            mLoginnameView = (TextView) view.findViewById(R.id.profile_text_loginname);
            mLoginnameView.setText(AppSingleton.getInstance().getLoginAuth().getUsername());
            presenter.onPresentWebImage(mImageAvator,"http://storage.disneyfans.cn/" + AppSingleton.getInstance().getLoginAuth().getAvatar(),55);
        } else {
            getActivity().findViewById(R.id.profile_head).setVisibility(View.INVISIBLE);
            getActivity().findViewById(R.id.profile_login).setVisibility(View.VISIBLE);

            Button loginButton = (Button) view.findViewById(R.id.btn_login);
            loginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity().getApplicationContext(), LoginActivity.class);
                    startActivityForResult(intent, REQ_LOGIN);

                }
            });
        }



        initView();
    }

    // TODO: Rename method, update argument and hook method into UI event
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
    @Override
    public void onDestroy() {
        AndroidImagePicker.getInstance().deleteOnImagePickCompleteListener(this);
        AndroidImagePicker.getInstance().removeOnImageCropCompleteListener(this);
        AndroidImagePicker.getInstance().deleteOnPictureTakeCompleteListener(this);

        super.onDestroy();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            AndroidImagePicker.getInstance().setOnPictureTakeCompleteListener(this);//watching Picture taking
            AndroidImagePicker.getInstance().setOnImagePickCompleteListener(this);
//            Toast.makeText(getContext(), String.valueOf(hidden),
//                    Toast.LENGTH_SHORT).show();

            mNickView.setText(AppSingleton.getInstance().getLoginAuth().getNickname());
            mLoginnameView.setText(AppSingleton.getInstance().getLoginAuth().getUsername());

            SharedPreferences mySharedPreferences = getActivity().getSharedPreferences(Constants.SHARED_PREFERENCE_NAME, Activity.MODE_PRIVATE);
            boolean avatarStat = mySharedPreferences.getBoolean(Constants.SHARED_PREFERENCE_KEY_AVATAR_STATUS, false);

            if (avatarStat) {
                String avatarFile = mySharedPreferences.getString(Constants.SHARED_PREFERENCE_KEY_AVATAR_FILE,"");

                String photoPath = getActivity().getApplicationContext().getFilesDir() + "/" + avatarFile;

                final BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(photoPath, options);

                // Calculate inSampleSize
                options.inSampleSize = 4;
                options.inJustDecodeBounds = false;
                Bitmap bmp = BitmapFactory.decodeFile(photoPath, options);
                new RetrieveFeedTask().execute(bmp);
            } else {
                presenter.onPresentWebImage(mImageAvator,"http://storage.disneyfans.cn/" + AppSingleton.getInstance().getLoginAuth().getAvatar(),55);
            }

        }
    }
    @Override
    public void onResume() {
        AndroidImagePicker.getInstance().setOnPictureTakeCompleteListener(this);//watching Picture taking
        AndroidImagePicker.getInstance().setOnImagePickCompleteListener(this);
        super.onResume();
    }
    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
    public interface OnLogoutListener {
        void onLogout();
    }
    public void setLogoutListener(OnLogoutListener listener) {
        mLogoutListener = listener;
    }
    private void initView() {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(resultCode == Activity.RESULT_OK){
            if (requestCode == REQ_IMAGE) {

                List<ImageItem> imageList = AndroidImagePicker.getInstance().getSelectedImages();
                mAdapter.clear();
                mAdapter.addAll(imageList);
            }/*else if(requestCode == REQ_IMAGE_CROP){
                Bitmap bmp = (Bitmap)data.getExtras().get("bitmap");
                Log.i(TAG,"-----"+bmp.getRowBytes());
            }*/
        }

    }

    @Override
    public void onPictureTakeComplete(String picturePath) {
        List<ImageItem> imageList = AndroidImagePicker.getInstance().getSelectedImages();
        imageList.clear();
        ImageItem item = new ImageItem(picturePath,"",0);
        imageList.add(item);
        mAdapter.clear();
        mAdapter.addAll(imageList);

    }

    @Override
    public void onImageCropComplete(Bitmap bmp, float ratio) {
        Log.i(TAG,"=====onImageCropComplete (get bitmap="+bmp.toString());

        Date date = new Date();
        SimpleDateFormat sy1=new SimpleDateFormat("yyyyMMDD");
        SimpleDateFormat sy2=new SimpleDateFormat("yyyyMMDDHHmmssSSS");
        String dateFormat=sy1.format(date);
        mAvatarFilename = "avator/" + sy1.format(date) + "/" + AppSingleton.getInstance().getLoginAuth().getUid() + "/" + sy2.format(date) + ".png";

        try {
            File folder = new File(getActivity().getApplicationContext().getFilesDir(), "avator/" + sy1.format(date) + "/" + AppSingleton.getInstance().getLoginAuth().getUid());
            folder.mkdirs();
 Log.d(TAG, getActivity().getApplicationContext().getFilesDir() + "/" + mAvatarFilename);
            FileOutputStream out = new FileOutputStream(getActivity().getApplicationContext().getFilesDir() + "/" + mAvatarFilename);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);

        } catch (Exception e) {
            Log.e(TAG, e.toString());
            e.printStackTrace();
            return;
        }

        presenter.onPresentImage(mImageAvator,getActivity().getApplicationContext().getFilesDir() + "/" + mAvatarFilename,55);
        if (AppSingleton.getInstance().getLoginAuth().getAvatar() != "") {
            File avatorFile = new File(getActivity().getApplicationContext().getFilesDir() + "/" + AppSingleton.getInstance().getLoginAuth().getAvatar());
            if (avatorFile.exists()) avatorFile.delete();
        }
        AppSingleton.getInstance().getLoginAuth().setAvatar(mAvatarFilename);

        SharedPreferences mySharedPreferences = getActivity().getSharedPreferences(Constants.SHARED_PREFERENCE_NAME, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.putBoolean(Constants.SHARED_PREFERENCE_KEY_AVATAR_STATUS, true);
        editor.putString(Constants.SHARED_PREFERENCE_KEY_AVATAR_FILE, mAvatarFilename);
        editor.commit();

        new RetrieveFeedTask().execute(bmp);

//        String accessKey = "niklnhOE9JjWCLPB2qXO";
//        String accessSecret = "f48466dc13e58b9f71b984f78d0db16163b0cac4";
//
//        AWSCredentials credentials = new BasicAWSCredentials(accessKey,accessSecret);
//        conn = new SCSClient(credentials);
//
//        ByteArrayOutputStream stream = new ByteArrayOutputStream();
//        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
//        byte[] byteArray = stream.toByteArray();
//        in = new ByteArrayInputStream(byteArray);
//
//        Handler mHandler = new Handler();
//        mHandler.post(new Runnable() {
//            @Override
//            public void run() {
//                ObjectMetadata omdata= new ObjectMetadata();
//                PutObjectResult putObjectResult = conn.putObject("disneyfans", "avator/20160614/222222.png", in, omdata);
//                try {
//                    Log.d(TAG,putObjectResult.toString());
//                } catch (Exception e) {
//                    Log.e(TAG,e.toString());
//                }
//            }
//        });

//        DsnApplication app = (DsnApplication) getActivity().getApplication();
//        Socket socket = app.getSocket();
//        if (socket.connected()) {
//            ByteArrayOutputStream stream = new ByteArrayOutputStream();
//            bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
//            byte[] byteArray = stream.toByteArray();
//
//            JSONObject obj = new JSONObject();
//            try {
//                obj.put("uid", app.getUserid());
//                obj.put("avator", byteArray);
//                socket.emit("upload avator", obj);
//            } catch (JSONException e) {
//                e.printStackTrace();
//                return;
//            }
//        }
        //ivCrop.setImageBitmap(bmp);
    }

    @Override
    public void onImagePickComplete(List<ImageItem> items) {
        Log.i(TAG,"=====onImagePickComplete (get ImageItems size="+items.size());

        //List<ImageItem> imageList = AndroidImagePicker.getInstance().getSelectedImages();
        mAdapter.clear();
        mAdapter.addAll(items);
    }


    class SelectAdapter extends ArrayAdapter<ImageItem> {

        //private int mResourceId;
        public SelectAdapter(Context context) {
            super(context, 0);
            //this.mResourceId = resource;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ImageItem item = getItem(position);
            //LayoutInflater inflater = getLayoutInflater();
            //View view = inflater.inflate(mResourceId, null);
            int width = (screenWidth - Util.dp2px( getContext(),10*2))/3;

            ImageView imageView = new ImageView(getContext());
            imageView.setBackgroundColor(Color.TRANSPARENT);
            GridView.LayoutParams params = new AbsListView.LayoutParams(width, width);
            imageView.setLayoutParams(params);



            //return presenter.onPresentImage(MainActivity.this,item.path,width);

            presenter.onPresentImage(imageView,item.path,width);
            return imageView;
        }

    }

    class RetrieveFeedTask extends AsyncTask<Bitmap, Integer, String> {

        private Exception exception;

        protected void onPostExecute(PutObjectResult result) {
            // TODO: check this.exception
            // TODO: do something with the feed
        }

        @Override
        protected String doInBackground(Bitmap... bitmaps) {
            try {
                String accessKey = "niklnhOE9JjWCLPB2qXO";
                String accessSecret = "f48466dc13e58b9f71b984f78d0db16163b0cac4";

                AWSCredentials credentials = new BasicAWSCredentials(accessKey,accessSecret);
                SCS conn = new SCSClient(credentials);

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmaps[0].compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();
                InputStream in = new ByteArrayInputStream(byteArray);

                ObjectMetadata omdata= new ObjectMetadata();
                PutObjectResult putObjectResult = conn.putObject("disneyfans", mAvatarFilename, in, omdata);

                AccessControlList acl = new AccessControlList();
                acl.grantPermissions(UserIdGrantee.CANONICAL, Permission.Read);
                acl.grantPermissions(UserIdGrantee.ANONYMOUSE,Permission.Read);
                conn.setObjectAcl("disneyfans", mAvatarFilename, acl);

                return putObjectResult.toString();

            } catch (Exception e) {
                this.exception = e;
                return null;
            }
        }
    }

    private Emitter.Listener onUpdatedAvator = new Emitter.Listener() {
        boolean result;
        @Override
        public void call(Object... args) {
            JSONObject data = (JSONObject) args[0];

            try {
                result = data.getBoolean("result");
//Log.d("LoginActivity", String.valueOf(result));
            } catch (JSONException e) {
                Log.e(TAG, e.toString());
                result = false;
            }

            SharedPreferences mySharedPreferences = getActivity().getSharedPreferences(Constants.SHARED_PREFERENCE_NAME, Activity.MODE_PRIVATE);
            SharedPreferences.Editor editor = mySharedPreferences.edit();
            editor.putBoolean(Constants.SHARED_PREFERENCE_KEY_AVATAR_STATUS, false);
            editor.commit();

        }
    };

}
