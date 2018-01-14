package com.didlink.xingxing.viewholder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.didlink.xingxing.models.Channel;
import com.lezaizai.atv.model.TreeNode;

/**
 * Created by Bogdan Melnychuk on 2/13/15.
 */
public class FragmentChannelViewHolder extends TreeNode.BaseNodeViewHolder<Channel> {
    private OnPopActionListener mActionListener;
    private Channel channel;
    private PrintView show_pop;

    public FragmentChannelViewHolder(Context context) {
        super(context);
    }

    @Override
    public View createNodeView(TreeNode node, Channel value) {
        final LayoutInflater inflater = LayoutInflater.from(context);
        final View view = inflater.inflate(R.layout.channel_fragment_channel_node, null, false);

        TextView userNameLabel = (TextView) view.findViewById(R.id.channelname);
        userNameLabel.setText(value.getName());

        TextView connectionsLabel = (TextView) view.findViewById(R.id.ownername);
        connectionsLabel.setText(value.getOwner().getNickname());

        channel = value;

        show_pop = (PrintView) view.findViewById(R.id.show_pop);
        show_pop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mActionListener != null) {
                    mActionListener.OnPopAction(channel, show_pop);
                }
            }
        });

        return view;
    }

    @Override
    public void toggle(boolean active) {
    }

    @Override
    public int getContainerStyle() {
        return R.style.TreeNodeStyleNone;
    }

    public interface OnPopActionListener {
        void OnPopAction(Channel value, View view);
    }
    public void setOnPopActionListener(OnPopActionListener listener) {
        mActionListener = listener;
    }

}
