package com.didlink.xingxing.viewholder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.didlink.xingxing.models.Channel;
import com.lezaizai.atv.model.TreeNode;

/**
 * Created by Bogdan Melnychuk on 2/13/15.
 */
public class ChannelViewHolder extends TreeNode.BaseNodeViewHolder<Channel> {

    public ChannelViewHolder(Context context) {
        super(context);
    }

    @Override
    public View createNodeView(TreeNode node, Channel value) {
        final LayoutInflater inflater = LayoutInflater.from(context);
        final View view = inflater.inflate(R.layout.channel_channel_node, null, false);

        TextView userNameLabel = (TextView) view.findViewById(R.id.channelname);
        userNameLabel.setText(value.getName());

        TextView connectionsLabel = (TextView) view.findViewById(R.id.ownername);
        connectionsLabel.setText(value.getOwner().getNickname());


        TextView sizeText = (TextView) view.findViewById(R.id.contactsize);
        sizeText.setText(String.valueOf(value.getContacts_num()));

        return view;
    }

    @Override
    public void toggle(boolean active) {
    }

    @Override
    public int getContainerStyle() {
        return R.style.TreeNodeStyleNone;
    }

}
