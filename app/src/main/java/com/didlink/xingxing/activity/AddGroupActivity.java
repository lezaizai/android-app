package com.didlink.xingxing.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.didlink.systembar.Base.BaseActivity;
import com.didlink.systembar.Tools.StatusBarManager;
import com.didlink.xingxing.AppSingleton;
import com.didlink.xingxing.R;
import com.didlink.xingxing.common.adapter.TextWatcherAdapter;
import com.didlink.xingxing.config.Constants;
import com.didlink.xingxing.models.Channel;
import com.didlink.xingxing.service.RetrofitChannelService;
import com.lezaizai.atv.model.TreeNode;
import com.lezaizai.atv.view.AndroidTreeView;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.iconics.view.IconicsTextView;
import com.mikepenz.ionicons_typeface_library.Ionicons;

import java.util.ArrayList;
import java.util.List;

public class AddGroupActivity extends BaseActivity {
    private EditText mNameView;
    private IconicsTextView mAddButton;
    private IconicsTextView mSearchButton;
    private String mProfile;
    //private SocketService mCtrlmessage;
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

        setTitleBgColor(R.color.deepgrey);
        setToolbarTitleTv(R.string.activity_title_addgrp);
        setTitleNavigationIcon(new IconicsDrawable(this)
                .icon(Ionicons.Icon.ion_arrow_left_c)
                .color(Color.WHITE)
                .sizeDp(18));

        new StatusBarManager.builder(this)
                .setStatusBarColor(R.color.deepgrey)
                .setTintType(StatusBarManager.TintType.PURECOLOR)
                .setAlpha(0)
                .create();

        containerView = (ViewGroup) findViewById(R.id.addgroup_container);
        mPreSearchStr = "";
        mPreAddStr = "";
        longpressing = false;

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
        }
/*
        mCtrlmessage = app.getSocketService();

        mCtrlmessage.setAddChannelListener(new SocketService.OnAddChannelListener(){
            @Override
            public void onAddChannelResult(boolean result, String chid, List<Contact> contacts){
                if (!result) {
                    mSearchButton.setIconColor(getResources().getColor(R.color.colorGreen));
                    return;
                }
                List<Contact> members = new ArrayList<Contact>();
                members.add(new Contact(AppSingleton.getInstance().getLoginAuth().getUid(),
                        AppSingleton.getInstance().getLoginAuth().getUsername(),
                        AppSingleton.getInstance().getLoginAuth().getNickname(),
                        AppSingleton.getInstance().getLoginAuth().getAvatar()));

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
*/
        mNameView = (EditText) findViewById(R.id.channel_nameinput);
        mAddButton = (IconicsTextView) findViewById(R.id.addgroup_ok);
        mSearchButton = (IconicsTextView) findViewById(R.id.addgroup_search);

        mNameView.addTextChangedListener(new TextWatcherAdapter() {
            public void onTextChanged(final CharSequence s, int start, int before, int count) {
                    if (mNameView.getText().toString().length() != 0) {
                         mAddButton.setTextColor(getResources().getColor(R.color.colorGreen));
                        mSearchButton.setTextColor(getResources().getColor(R.color.colorGreen));
                    } else if (mNameView.getText().toString().length() == 0) {
                        mAddButton.setTextColor(getResources().getColor(R.color.text_lgray));
                        mSearchButton.setTextColor(getResources().getColor(R.color.text_lgray));
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
                mSearchButton.setTextColor(getResources().getColor(R.color.text_lgray));
                mAddButton.setTextColor(getResources().getColor(R.color.text_lgray));
                //mCtrlmessage.addChannel(mNameView.getText().toString(),AppSingleton.getInstance().getLoginAuth().getUid());

                Channel channel = new Channel();
                channel.setOwner(AppSingleton.getInstance().getLoginAuth().toContact());
                channel.setName(mPreAddStr);
                channel.setStatus(0);
                channel.setType(1);
                channel.setContacts_num(1);

                RetrofitChannelService channelService = new RetrofitChannelService();
                channelService.setChannelListener(new RetrofitChannelService.ChannelListener() {
                    @Override
                    public void OnChannelAdded(boolean result, Channel channel) {
                        System.out.println("aaaaaaaaaaaaa");
                        if (!result) {
                            mSearchButton.setTextColor(getResources().getColor(R.color.colorGreen));
                            Toast.makeText(getApplicationContext(), "add channel failed.",
                                    Toast.LENGTH_SHORT).show();
                            return;
                        }
System.out.println("bbbbbbbbbbbbb");
                        AppSingleton.getInstance().getmRealmDBService().joinChannel(channel);

                        Intent intent = new Intent();
                        intent.putExtra("channel", new ArrayList<>().add(channel));
                        setResult(Constants.ACTIVITY_CHANNELADDGROUP_RESULTOK, intent);
                        finish();
System.out.println("ccccccccccccccccccc");
                    }
                });

                channelService.newChannel(Constants.HTTP_BASE_URL,
                        AppSingleton.getInstance().getLoginAuth().getToken(),
                        channel);

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
                mSearchButton.setTextColor(getResources().getColor(R.color.text_lgray));
                mAddButton.setTextColor(getResources().getColor(R.color.text_lgray));

                if (root.getChildren().size()>0) {
                    root = TreeNode.root();
                    tView.setRoot(root);
                    containerView.removeAllViews();
                    //titleBar.setTitle(getResources().getString(R.string.title_addgrp));
                }
                //mCtrlmessage.searchChannel(mNameView.getText().toString(),app.getUserid());
            }
        });

        root = TreeNode.root();
        tView = new AndroidTreeView(getApplicationContext(), root);
        tView.setDefaultAnimation(true);
        tView.setDefaultContainerStyle(R.style.TreeNodeStyleDivided, true);
        containerView.addView(tView.getView());

    }

    @Override
    public int getContentViewID() {
        return R.layout.activity_add_group;
    }

    @Override
    public void initViews() {

    }
}
