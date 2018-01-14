package com.didlink.xingxing.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.lezaizai.atv.model.TreeNode;
import com.lezaizai.atv.view.AndroidTreeView;

import java.util.List;

public class AddContactActivity extends AppCompatActivity {
    private EditText mNameView;
    private String mProfile;
    private PrintView mSearchBtn;
    private SocketService mCtrlmessage;
    private AndroidTreeView tView;
    private TreeNode root;
    private ViewGroup containerView;
    private List<Contact> newcontacts;
    private String mPreSearchStr;
    private String mChid;
    private DsnApplication app;
    private Contact mSelectedContact;
    private boolean longpressing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);
        containerView = (ViewGroup) findViewById(R.id.addcontact_container);

        app = (DsnApplication) getApplication();

        mNameView = (EditText) findViewById(R.id.search_nameinput);
        mSearchBtn = (PrintView) findViewById(R.id.search_contact);
        mPreSearchStr = "";
        mCtrlmessage = app.getSocketService();
        longpressing = false;

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                mChid= null;
            } else {
                mChid= extras.getString("channel");
            }
        }

        root = TreeNode.root();
        tView = new AndroidTreeView(getApplicationContext(), root);
        tView.setDefaultAnimation(true);
        tView.setDefaultContainerStyle(R.style.TreeNodeStyleDivided, true);
        containerView.addView(tView.getView());

        mNameView.addTextChangedListener(new TextWatcherAdapter() {
            public void onTextChanged(final CharSequence s, int start, int before, int count) {
                    if (mNameView.getText().toString().length() != 0) {
                        mSearchBtn.setIconColor(getResources().getColor(R.color.colorGreen));
                        mSearchBtn.setEnabled(true);
                    } else if (mNameView.getText().toString().length() == 0) {
                        mSearchBtn.setIconColor(getResources().getColor(R.color.light_gray));
                        mSearchBtn.setEnabled(false);
                    }
            }

        });

        mCtrlmessage.setSearchrContactListener(new SocketService.OnSearContactListener(){
            @Override
            public void onSearchResult(List<Contact> contacts){
Log.e("AddContactActivity",contacts.toString());
                newcontacts = contacts;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        for (Contact contact : newcontacts) {

                            TreeNode contactV = new TreeNode(contact).setViewHolder(new ContactViewHolder(getApplicationContext()));
                            contactV.setLongClickListener(new TreeNode.TreeNodeLongClickListener(){
                                @Override
                                public boolean onLongClick(TreeNode node, Object value) {
                                    if (longpressing) return true;
                                    else longpressing = true;

                                    mSelectedContact = (Contact)value;
                                    mCtrlmessage.addContactChannel(mChid, mSelectedContact.getUid());
                                    return true;
//                                    Channel channel = app.getChannel(mChid);
//                                    if (channel == null) return false;
//                                    channel.addContact((Contact)value);
//                                    if (!SDBService.updateChannel(getApplicationContext(), channel)) {
//                                        Toast.makeText(getApplicationContext(), "join channel failed.",
//                                                Toast.LENGTH_SHORT).show();
//                                        return false;
//                                    }
//
//                                    app.removeChannel(channel);
//                                    app.addChannel(channel);
//
//                                    Intent intent = new Intent();
//                                     setResult(Constants.ACTIVITY_CHANNELADDCONTACT_RESULTOK, intent);
//                                    finish();
//                                    return true;
                                };
                            });
                            root.addChildren(contactV);
                        }
                        containerView.addView(tView.getView());
                    }
                });
            };
            @Override
            public void onAddContactResult(boolean result){
                longpressing = false;
                if (!result) return;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Channel channel = app.getChannel(mChid);
                        if (channel == null) return;
                        channel.addContact(mSelectedContact);
                        if (!SDBService.updateChannel(getApplicationContext(), channel)) {
                            Toast.makeText(getApplicationContext(), "join channel failed.",
                                    Toast.LENGTH_SHORT).show();
                            return;
                        }

                        app.removeChannel(channel);
                        app.addChannel(channel);
                        Intent intent = new Intent();
                        setResult(Constants.ACTIVITY_CHANNELADDCONTACT_RESULTOK, intent);
                        finish();
                    }
                });
            };

        });

        mSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mNameView.getText().toString().length() == 0) {
                    return;
                }
                if (mPreSearchStr.equals(mNameView.getText().toString())) {
                    return;
                } else {
                    mPreSearchStr = mNameView.getText().toString();
                }

                mSearchBtn.setIconColor(getResources().getColor(R.color.light_gray));

                if (root.getChildren().size()>0) {
                    //root.getViewHolder().getNodeItemsView().removeAllViews();
                    root = TreeNode.root();
                    tView.setRoot(root);
                    //tView = new AndroidTreeView(getApplicationContext(), root);
                    //tView.setDefaultAnimation(true);
                    //tView.setDefaultContainerStyle(R.style.TreeNodeStyleDivided, true);
                    containerView.removeAllViews();
                    //containerView.addView(tView.getView());
                }

                mCtrlmessage.searchContact(mNameView.getText().toString());

//                Intent intent = new Intent();
//                intent.putExtra("name", mNameView.getText().toString());
//                intent.putExtra("profile", mProfile);
//                setResult(Constants.ACTIVITY_CHANNELADDCONTACT_RESULTOK, intent);
//                finish();
            }
        });

        if (hasKitKat() && !hasLollipop()) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            ViewGroup mContentView = (ViewGroup) findViewById(Window.ID_ANDROID_CONTENT);

            //int statusBarHeight = (int)Math.ceil(25 * getResources().getDisplayMetrics().density);
            int statusBarHeight = TitleBar.getStatusBarHeight();

            View statusBarView = mContentView.getChildAt(0);
            if (statusBarView != null && statusBarView.getLayoutParams() != null && statusBarView.getLayoutParams().height == statusBarHeight) {
                //避免重复调用时多次添加 View
                statusBarView.setBackgroundColor(getResources().getColor(R.color.color_titlebackground));
                return;
            }
            statusBarView = new View(this);
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, statusBarHeight);
            statusBarView.setBackgroundColor(getResources().getColor(R.color.color_titlebackground));
            //向 ContentView 中添加假 View
            mContentView.addView(statusBarView, 0, lp);
        } else if (hasLollipop()) {
            Window window = getWindow();
            //取消设置透明状态栏,使 ContentView 内容不再覆盖状态栏
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

//需要设置这个 flag 才能调用 setStatusBarColor 来设置状态栏颜色
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//设置状态栏颜色
            window.setStatusBarColor(getResources().getColor(R.color.color_titlebackground));

            ViewGroup mContentView = (ViewGroup) findViewById(Window.ID_ANDROID_CONTENT);
            View mChildView = mContentView.getChildAt(0);
            if (mChildView != null) {
                //注意不是设置 ContentView 的 FitsSystemWindows, 而是设置 ContentView 的第一个子 View . 预留出系统 View 的空间.
                ViewCompat.setFitsSystemWindows(mChildView, true);
            }
        }

        titleBar = (TitleBar) findViewById(R.id.title_bar_addcontact);

        titleBar.setBackgroundColor(getResources().getColor(R.color.color_titlebackground));
        titleBar.setHeight(getResources().getDimensionPixelSize(R.dimen.title_height));
        titleBar.setLeftImageResource(R.drawable.ic_back_white_36dp);
        titleBar.setLeftText(getResources().getString(R.string.title_back));
        titleBar.setLeftTextColor(Color.WHITE);
        titleBar.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                setResult(Constants.ACTIVITY_CHANNELADDCONTACT_RESULTCANCEL, intent);
                finish();
            }
        });

        titleBar.setTitle(getResources().getString(R.string.title_addcontact ));
        titleBar.setTitleColor(Color.WHITE);
        titleBar.setSubTitleColor(Color.WHITE);
        titleBar.setDividerColor(Color.GRAY);

    }
    public static boolean hasKitKat() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
    }

    public static boolean hasLollipop() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
