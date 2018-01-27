package com.didlink.xingxing.viewholder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.didlink.xingxing.R;
import com.didlink.xingxing.models.Channel;
import com.lezaizai.atv.model.TreeNode;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.iconics.view.IconicsImageView;
import com.mikepenz.ionicons_typeface_library.Ionicons;

/**
 * Created by Bogdan Melnychuk on 2/13/15.
 */
public class FragmentChannelViewHolder extends TreeNode.BaseNodeViewHolder<Channel> {
    private OnPopActionListener mActionListener;
    private Channel channel;
    private IconicsImageView show_pop;
    private IconicsImageView arrowView;

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
        connectionsLabel.setText(value.getOwner()==null? "" : value.getOwner().getNickname());
        arrowView = (IconicsImageView) view.findViewById(R.id.arrow_icon);

        channel = value;

        show_pop = (IconicsImageView) view.findViewById(R.id.show_pop);
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
        if (active) {
            arrowView.setIcon(new IconicsDrawable(context)
                    .icon(Ionicons.Icon.ion_arrow_down_b)
                    .color(context.getResources().getColor(R.color.text_lgray))
                    .sizeDp(20));
        } else {
            arrowView.setIcon(new IconicsDrawable(context)
                    .icon(Ionicons.Icon.ion_arrow_right_b)
                    .color(context.getResources().getColor(R.color.text_lgray))
                    .sizeDp(20));
        }

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
