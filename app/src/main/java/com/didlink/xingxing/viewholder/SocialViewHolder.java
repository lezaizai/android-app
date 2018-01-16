package com.didlink.xingxing.viewholder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.didlink.xingxing.R;
import com.lezaizai.atv.model.TreeNode;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.iconics.typeface.IIcon;
import com.mikepenz.iconics.view.IconicsImageView;

import java.util.Random;

/**
 * Created by Bogdan Melnychuk on 2/13/15.
 */
public class SocialViewHolder extends TreeNode.BaseNodeViewHolder<SocialViewHolder.SocialItem> {

    private static final String[] NAMES = new String[]{"Bruce Wayne", "Clark Kent", "Barry Allen", "Hal Jordan"};

    public SocialViewHolder(Context context) {
        super(context);
    }

    @Override
    public View createNodeView(TreeNode node, SocialItem value) {
        final LayoutInflater inflater = LayoutInflater.from(context);
        final View view = inflater.inflate(R.layout.channel_social_node, null, false);

        final IconicsImageView iconView = (IconicsImageView) view.findViewById(R.id.icon);
        iconView.setIcon(new IconicsDrawable(context).icon(value.icon));

        TextView connectionsLabel = (TextView) view.findViewById(R.id.connections);
        Random r = new Random();
        connectionsLabel.setText(r.nextInt(150) + " connections");

        TextView userNameLabel = (TextView) view.findViewById(R.id.username);
        userNameLabel.setText(NAMES[r.nextInt(4)]);

        TextView sizeText = (TextView) view.findViewById(R.id.size);
        sizeText.setText(r.nextInt(10) + " items");

        return view;
    }

    @Override
    public void toggle(boolean active) {
    }

    @Override
    public int getContainerStyle() {
        return R.style.TreeNodeStyleNone;
    }


    public static class SocialItem {
        public IIcon icon;

        public SocialItem(IIcon icon) {
            this.icon = icon;
        }
        // rest will be hardcoded
    }

}
