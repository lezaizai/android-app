package com.didlink.xingxing.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.github.johnkil.print.PrintButton;
import com.github.johnkil.print.PrintView;
import com.lezaizai.atv.model.TreeNode;
import com.lezaizai.atv.view.AndroidTreeView;
import com.lezaizai.disneyfans.DsnApplication;
import com.lezaizai.disneyfans.R;
import com.lezaizai.disneyfans.TitleBar;
import com.lezaizai.disneyfans.channel.holder.ChannelViewHolder;
import com.lezaizai.disneyfans.channel.holder.SocialViewHolder;
import com.lezaizai.disneyfans.config.Channel;
import com.lezaizai.disneyfans.config.Constants;
import com.lezaizai.disneyfans.config.Contact;
import com.lezaizai.disneyfans.service.SDBService;
import com.lezaizai.disneyfans.service.SocketService;
import com.lezaizai.floatingsearchview.util.adapter.TextWatcherAdapter;

import java.util.ArrayList;
import java.util.List;

public class AddGroupActivity extends AppCompatActivity {
    private EditText mNameView;
    private PrintView mAddButton;
    private PrintView mSearchButton;
    private String mProfile;
    private TitleBar titleBar;
    private DsnApplication app;
    private SocketService mCtrlmessage;
    private List<Channel> newchannels;

    private AndroidTreeView tView;
    private TreeNode root;
    private ViewGroup containerView;
    private String mPreSearchStr;
    private String mPreAddStr;
    private boolean longpressing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_group);
        containerView = (ViewGroup) findViewById(R.id.addgroup_container);
        mPreSearchStr = "";
        mPreAddStr = "";
        longpressing = false;

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
        }
        app = (DsnApplication) getApplication();
        mCtrlmessage = app.getSocketService();

        mCtrlmessage.setAddChannelListener(new SocketService.OnAddChannelListener(){
            @Override
            public void onAddChannelResult(boolean result, String chid, List<Contact> contacts){
                if (!result) {
                    mSearchButton.setIconColor(getResources().getColor(R.color.colorGreen));
                    return;
                }
                List<Contact> members = new ArrayList<Contact>();
                members.add(new Contact(app.getUserid(),app.getLoginname(),app.getNickname(),app.getAvatar()));

                Intent intent = new Intent();
                intent.putExtra("name", mNameView.getText().toString());
                intent.putExtra("chid", chid);
                //intent.put("contacts", members);
                setResult(Constants.ACTIVITY_CHANNELADDGROUP_RESULTOK, intent);
                finish();
            };

            @Override
            public void onSearchChannelResult(boolean result, List<Channel> channels){
                List<Contact> members = new ArrayList<Contact>();
                members.add(new Contact(app.getUserid(),app.getLoginname(),app.getNickname(),app.getAvatar()));

                newchannels = channels;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAddButton.setIconColor(getResources().getColor(R.color.colorGreen));
                        for (int i=0; i<newchannels.size(); i++) {
                            TreeNode ChannelV = new TreeNode(newchannels.get(i)).setViewHolder(new ChannelViewHolder(getApplicationContext()));
                            ChannelV.setLongClickListener(new TreeNode.TreeNodeLongClickListener(){
                                @Override
                                public boolean onLongClick(TreeNode node, Object value) {
                                    if (longpressing) return true;
                                    else longpressing = true;

                                    Channel channel = (Channel)value;
                                    mCtrlmessage.joinChannel(channel.getChid(), app.getUserid());
//                                    if (!SDBService.joinChannel(getApplicationContext(), (Channel)value)) {
//                                        Toast.makeText(getApplicationContext(), "join channel failed.",
//                                                Toast.LENGTH_SHORT).show();
//                                        return false;
//                                    }
//
//                                    app.addChannel((Channel)value);
//
//                                    Intent intent = new Intent();
//                                    intent.putExtra("name", mNameView.getText().toString());
//                                    //intent.putExtra("chid", chid);
//                                    //intent.put("contacts", members);
//                                    setResult(Constants.ACTIVITY_CHANNELADDGROUP_RESULTOK, intent);
//                                    finish();

                                    return true;
                                };
                            });
                            root.addChildren(ChannelV);
                        }

                        if (newchannels.size()>0) {
                            containerView.addView(tView.getView());
                            titleBar.setTitle(getResources().getString(R.string.title_addgrp_join));

                        }
                    }
                });

            }

            @Override
            public void onJoinChannelResult(boolean result, Channel channel){
                longpressing = false;
                if (result == false || !SDBService.joinChannel(getApplicationContext(), channel)) {
                    Toast.makeText(getApplicationContext(), "join channel failed.",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                if (app.getChannel(channel.getChid()) != null) {
                    app.removeChannel(channel);
                }
                app.addChannel(channel);

                Intent intent = new Intent();
                //intent.put("contacts", members);
                setResult(Constants.ACTIVITY_CHANNELADDGROUP_RESULTOK, intent);
                finish();
            };

        });

        mNameView = (EditText) findViewById(R.id.channel_nameinput);
        mAddButton = (PrintView) findViewById(R.id.addgroup_ok);
        mSearchButton = (PrintView) findViewById(R.id.addgroup_search);

        mNameView.addTextChangedListener(new TextWatcherAdapter() {
            public void onTextChanged(final CharSequence s, int start, int before, int count) {
                    if (mNameView.getText().toString().length() != 0) {
                         mAddButton.setIconColor(getResources().getColor(R.color.colorGreen));
                        mSearchButton.setIconColor(getResources().getColor(R.color.colorGreen));
                    } else if (mNameView.getText().toString().length() == 0) {
                        mAddButton.setIconColor(getResources().getColor(R.color.light_gray));
                        mSearchButton.setIconColor(getResources().getColor(R.color.light_gray));
                    }
            }

        });

        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mNameView.getText().toString().length() == 0) {
                    return;
                }
                if (mPreAddStr.equals(mNameView.getText().toString())) {
                    return;
                } else {
                    mPreAddStr = mNameView.getText().toString();
                }
                mSearchButton.setIconColor(getResources().getColor(R.color.light_gray));
                mAddButton.setIconColor(getResources().getColor(R.color.light_gray));
                mCtrlmessage.addChannel(mNameView.getText().toString(),app.getUserid());
            }
        });

        mSearchButton.setOnClickListener(new View.OnClickListener() {
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
                mSearchButton.setIconColor(getResources().getColor(R.color.light_gray));
                mAddButton.setIconColor(getResources().getColor(R.color.light_gray));

                if (root.getChildren().size()>0) {
                    root = TreeNode.root();
                    tView.setRoot(root);
                    containerView.removeAllViews();
                    titleBar.setTitle(getResources().getString(R.string.title_addgrp));
                }
                mCtrlmessage.searchChannel(mNameView.getText().toString(),app.getUserid());
            }
        });

        root = TreeNode.root();
        tView = new AndroidTreeView(getApplicationContext(), root);
        tView.setDefaultAnimation(true);
        tView.setDefaultContainerStyle(R.style.TreeNodeStyleDivided, true);
        containerView.addView(tView.getView());

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

        titleBar = (TitleBar) findViewById(R.id.title_bar_addgrp);

        titleBar.setBackgroundColor(getResources().getColor(R.color.color_titlebackground));
        titleBar.setHeight(getResources().getDimensionPixelSize(R.dimen.title_height));
        titleBar.setLeftImageResource(R.drawable.ic_back_white_36dp);
        titleBar.setLeftText(getResources().getString(R.string.title_back));
        titleBar.setLeftTextColor(Color.WHITE);
        titleBar.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                setResult(Constants.ACTIVITY_CHANNELADDGROUP_RESULTCANCEL, intent);
                finish();
            }
        });

        titleBar.setTitle(getResources().getString(R.string.title_addgrp));
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
}
