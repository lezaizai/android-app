package com.didlink.xingxing.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.app.Fragment;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;

import com.didlink.xingxing.AppSingleton;
import com.didlink.xingxing.R;
import com.didlink.xingxing.activity.AddContactActivity;
import com.didlink.xingxing.activity.AddGroupActivity;
import com.didlink.xingxing.config.Constants;
import com.didlink.xingxing.dialog.TitleMenu.ActionItem;
import com.didlink.xingxing.dialog.TitleMenu.TitlePopup;
import com.didlink.xingxing.models.Channel;
import com.didlink.xingxing.models.Contact;
import com.didlink.xingxing.viewholder.ContactViewHolder;
import com.didlink.xingxing.viewholder.FragmentChannelViewHolder;
import com.didlink.xingxing.viewholder.HeaderHolder;
import com.didlink.xingxing.viewholder.IconTreeItemHolder;
import com.didlink.xingxing.viewholder.SocialActionHolder;
import com.didlink.xingxing.viewholder.SocialViewHolder;
import com.lezaizai.atv.model.TreeNode;
import com.lezaizai.atv.view.AndroidTreeView;
import com.mikepenz.ionicons_typeface_library.Ionicons;

import java.util.List;

/**
 * Created by Bogdan Melnychuk on 2/12/15.
 */
public class ChannelFragment extends Fragment {
    private static final int REQUEST_ADDMYGROUP = 0;
    private static final int REQUEST_ADDPUBLICGROUP = 1;
    private static final int REQUEST_ADDCONTACT = 2;
    private AndroidTreeView tView;
    private OnFragmentInteractionListener mListener;
//    private TreeNode myProfile;
//    private TreeNode mPublic;
    private Handler mHandler = new Handler();
//    private Channel mMyChannel;
//    private Channel mPublicChannel;
    private TreeNode root;
    private ViewGroup containerView;
    private TitlePopup titlePopup;
    private List<Channel> newchannels;
    private Channel channelonaction;

    public static ChannelFragment newInstance() {
        ChannelFragment fragment = new ChannelFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_channel, null, false);
        containerView = (ViewGroup) rootView.findViewById(R.id.container);
        rootView.findViewById(R.id.status_bar).setVisibility(View.GONE);

//        mMyChannel = app.getPramaryChannel().getMyChannel();
//        mPublicChannel = app.getPramaryChannel().getPublicChannel();

        root = TreeNode.root();

//        ProfileHolder myHolder = new ProfileHolder(getActivity());
//        ProfileHolder publicHolder = new ProfileHolder(getActivity());
//
//        myHolder.setOnProfileActionListener(new ProfileHolder.OnProfileActionListener() {
//            @Override
//            public void OnProfileAddAction() {
//                Intent intent = new Intent(getContext(), AddGroupActivity.class);
//                intent.putExtra("profile", "myHolder");
//                startActivityForResult(intent, REQUEST_ADDMYGROUP);
//            }
//        });
//
//        publicHolder.setOnProfileActionListener(new ProfileHolder.OnProfileActionListener() {
//            @Override
//            public void OnProfileAddAction() {
//                Intent intent = new Intent(getContext(), AddGroupActivity.class);
//                intent.putExtra("profile", "publicHolder");
//                startActivityForResult(intent, REQUEST_ADDMYGROUP);
//            }
//        });
//
//        myProfile = new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_person, mMyChannel.getName())).setViewHolder(myHolder);
//        mPublic = new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_person, mPublicChannel.getName())).setViewHolder(publicHolder);
//        addPramaryChannel(myProfile, mMyChannel);
//        addPramaryChannel(mPublic, mPublicChannel);
//        TreeNode facebook = new TreeNode(new SocialViewHolder.SocialItem(R.string.ic_post_facebook)).setViewHolder(new SocialViewHolder(getActivity()));
//        TreeNode facebook1 = new TreeNode(new SocialViewHolder.SocialItem(R.string.ic_post_facebook)).setViewHolder(new SocialViewHolder(getActivity()));
//        root.addChildren(myProfile, mPublic,facebook,facebook1);

        tView = new AndroidTreeView(getActivity(), root);
        tView.setDefaultAnimation(true);
        tView.setDefaultContainerStyle(R.style.TreeNodeStyleDivided, true);
        containerView.addView(tView.getView());
        refreshChannelV();
        initPopWindow();

        if (savedInstanceState != null) {
            String state = savedInstanceState.getString("tState");
            if (!TextUtils.isEmpty(state)) {
                tView.restoreState(state);
            }
        }

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    };

    private void addPramaryChannel(TreeNode profile, Channel channel) {

        for (Channel n : channel.getChildren()) {
            addChannel(profile,n);
//            TreeNode nodechannel = null;
//            if (n.getType() == Constants.CHANNEL_TYPE_SOCIAL) {
//                nodechannel = new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_people, n.getName())).setViewHolder(new HeaderHolder(getActivity()));
//            }
//            if (n.getType() == Constants.CHANNEL_TYPE_PLACE) {
//                nodechannel = new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_place, n.getName())).setViewHolder(new HeaderHolder(getActivity()));
//            }
//            if (nodechannel != null)
//                profile.addChildren(nodechannel);
        }

//        TreeNode socialNetworks = new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_people, "Social")).setViewHolder(new HeaderHolder(getActivity()));
//        TreeNode places = new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_place, "Places")).setViewHolder(new HeaderHolder(getActivity()));
//
//        TreeNode facebook = new TreeNode(new SocialViewHolder.SocialItem(R.string.ic_post_facebook)).setViewHolder(new SocialViewHolder(getActivity()));
//        TreeNode  = new TreeNode(new SocialViewHolder.SocialItem(R.string.ic_post_linkedin)).setViewHolder(new SocialViewHolder(getActivity()));
//        TreeNode google = new TreeNode(new SocialViewHolder.SocialItem(R.string.ic_post_gplus)).setViewHolder(new SocialViewHolder(getActivity()));
//        TreeNode twitter = new TreeNode(new SocialViewHolder.SocialItem(R.string.ic_post_twitter)).setViewHolder(new SocialViewHolder(getActivity()));
//
//        TreeNode lake = new TreeNode(new PlaceHolderHolder.PlaceItem("A rose garden")).setViewHolder(new PlaceHolderHolder(getActivity()));
//        TreeNode mountains = new TreeNode(new PlaceHolderHolder.PlaceItem("The white house")).setViewHolder(new PlaceHolderHolder(getActivity()));
//
//        places.addChildren(lake, mountains);
//        socialNetworks.addChildren(facebook, google, twitter, linkedin);
//        profile.addChildren(socialNetworks, places);
    }

    public void addChannelAction() {
            Intent intent = new Intent(getContext(), AddGroupActivity.class);
            intent.putExtra("profile", "publicHolder");
            startActivityForResult(intent, REQUEST_ADDMYGROUP);
    };


    private void addChannel(TreeNode nodeChannel, Channel channel) {
        TreeNode socialNetworks = new TreeNode(new IconTreeItemHolder.IconTreeItem(Ionicons.Icon.ion_android_add, channel.getName())).setViewHolder(new HeaderHolder(getActivity()));
        for (Contact c : channel.getContacts()) {
            addContact(socialNetworks, c);
        }
        nodeChannel.addChild(0,socialNetworks);
    }

    private void addContact(TreeNode nodeChannel, Contact contact) {
        TreeNode facebook = new TreeNode(new SocialViewHolder.SocialItem(Ionicons.Icon.ion_ios_people)).setViewHolder(new SocialViewHolder(getActivity()));
        SocialActionHolder actionholder = new SocialActionHolder(getActivity());
        actionholder.setSocialActionListener(new SocialActionHolder.OnSocialActionListener(){
            @Override
            public void OnAddContact() {
                Intent intent = new Intent(getContext(), AddContactActivity.class);
                startActivityForResult(intent, REQUEST_ADDCONTACT);
            }

        });
        TreeNode action = new TreeNode(new SocialActionHolder.SocialActionItem(Ionicons.Icon.ion_person_add)).setViewHolder(actionholder);
        nodeChannel.addChildren(facebook,action);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("tState", tView.getSaveState());
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (Constants.ACTIVITY_CHANNELADDGROUP_RESULTOK == resultCode ||
                Constants.ACTIVITY_CHANNELADDCONTACT_RESULTOK== resultCode) {
            refreshChannelV();
            return;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void refreshChannelV() {
        super.onResume();

        List<Channel> channels = AppSingleton.getInstance().getmRealmDBService().getChannels();
        //root.getViewHolder().getNodeItemsView().removeAllViews();
        root = TreeNode.root();
        if (channels != null && channels.size() > 0) {
            for (Channel channel : channels) {
                FragmentChannelViewHolder vHolder = new FragmentChannelViewHolder(getContext());
                vHolder.setOnPopActionListener(new FragmentChannelViewHolder.OnPopActionListener(){
                    @Override
                    public void OnPopAction(Channel channel, View view) {
                        channelonaction = channel;
                        titlePopup.show(view);
                    };
                });
                TreeNode ChannelV = new TreeNode(channel).setViewHolder(vHolder);
                List<Contact> contacts = channel.getContacts();
                if (contacts != null && contacts.size() > 0) {
                    for (Contact contact : contacts) {
                        TreeNode ownerContactV = new TreeNode(contact).setViewHolder(new ContactViewHolder(getContext()));
                        ChannelV.addChild(ownerContactV);
                    }
                }
               root.addChildren(ChannelV);
            }
        }
        tView = new AndroidTreeView(getActivity(), root);
        tView.setDefaultAnimation(true);
        tView.setDefaultContainerStyle(R.style.TreeNodeStyleDivided, true);
        containerView.removeAllViews();
        containerView.addView(tView.getView());

    }
    private void initPopWindow() {
        // 实例化标题栏弹窗
        titlePopup = new TitlePopup(getContext(), LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);
        titlePopup.setItemOnClickListener(onitemClick);
        // 给标题栏弹窗添加子类
        titlePopup.addAction(new ActionItem(getContext(), R.string.channel_pop_addcontact,
                R.drawable.icon_menu_addfriend));
        titlePopup.addAction(new ActionItem(getContext(), R.string.channel_pop_video,
                R.drawable.ic_videocam_white));
        titlePopup.addAction(new ActionItem(getContext(), R.string.channel_pop_exitchannel,
                R.drawable.ic_delete_white));
    }

    private TitlePopup.OnItemOnClickListener onitemClick = new TitlePopup.OnItemOnClickListener() {

        @Override
        public void onItemClick(ActionItem item, int position) {
            // mLoadingDialog.show();
            switch (position) {
                case 0:
                    Intent intent = new Intent(getContext(), AddContactActivity.class);
                    intent.putExtra("channel", channelonaction.getChid());
                    startActivityForResult(intent, REQUEST_ADDCONTACT);
                    break;
                case 1://
                    List<Contact> contacts = channelonaction.getContacts();
/*                    if (contacts != null && contacts.size() > 0) {
                        String[] callers = new String[SocketIORTCClient.MAX_PEER];
                        for (int j = 0; j < SocketIORTCClient.MAX_PEER; j++) {
                            callers[j] = "";
                        }
                        int i = 0;
                        for (Contact contact : contacts) {
                            if (!contact.getUid().equals(app.getUserid()) && !contact.getStreamid().equals("")) {
                                callers[i] = contact.getStreamid();
                                i++;
                                if (i >= SocketIORTCClient.MAX_PEER-1)
                                    break;
                            }
                        }
                        if (i>0) {
                            Intent intent1 = new Intent(getContext(), CallActivity.class);
                            intent1.putExtra(CallActivity.EXTRA_ROOMID, "00000000");
                            intent1.putExtra(CallActivity.EXTRA_VIDEO_CALL, true);
                            intent1.putExtra(CallActivity.EXTRA_VIDEO_CAPTUREQUALITYSLIDER_ENABLED,
                                    true);
                            intent1.putExtra(CallActivity.EXTRA_DISPLAY_HUD, false);
                            intent1.putExtra(CallActivity.EEXTRA_OPPOSITEID, callers);
                            intent1.putExtra(CallActivity.EEXTRA_INITIATOR, true);
                            intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            getContext().startActivity(intent1);
                        }
                    }
*/                    break;
                case 2:
//                    mCtrlmessage.leaveChannel(channelonaction.getChid(),app.getUserid());
                    break;
                case 3://
//                    Utils.start_Activity(MainActivity.this, GetMoneyActivity.class);
                    break;
                default:
                    break;
            }
        }
    };

}
