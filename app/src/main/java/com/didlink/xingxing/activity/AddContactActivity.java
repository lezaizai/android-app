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

import com.didlink.xingxing.R;
import com.didlink.xingxing.common.adapter.TextWatcherAdapter;
import com.didlink.xingxing.models.Contact;
import com.didlink.xingxing.service.SocketService;
import com.didlink.xingxing.viewholder.ContactViewHolder;
import com.lezaizai.atv.model.TreeNode;
import com.lezaizai.atv.view.AndroidTreeView;
import com.mikepenz.iconics.view.IconicsTextView;

import java.util.List;

public class AddContactActivity extends AppCompatActivity {
    private EditText mNameView;
    private String mProfile;
    private IconicsTextView mSearchBtn;
    private SocketService mCtrlmessage;
    private AndroidTreeView tView;
    private TreeNode root;
    private ViewGroup containerView;
    private List<Contact> newcontacts;
    private String mPreSearchStr;
    private String mChid;
    private Contact mSelectedContact;
    private boolean longpressing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);
        containerView = (ViewGroup) findViewById(R.id.addcontact_container);

        mNameView = (EditText) findViewById(R.id.search_nameinput);
        mSearchBtn = (IconicsTextView) findViewById(R.id.search_contact);
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
                        mSearchBtn.setTextColor(getResources().getColor(R.color.colorGreen));
                        mSearchBtn.setEnabled(true);
                    } else if (mNameView.getText().toString().length() == 0) {
                        mSearchBtn.setTextColor(getResources().getColor(R.color.text_lgray));
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

                mSearchBtn.setTextColor(getResources().getColor(R.color.text_lgray));

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
