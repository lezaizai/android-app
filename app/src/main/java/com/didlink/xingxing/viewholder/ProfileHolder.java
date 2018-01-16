package com.didlink.xingxing.viewholder;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.didlink.xingxing.R;
import com.lezaizai.atv.model.TreeNode;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.iconics.view.IconicsImageView;
import com.mikepenz.ionicons_typeface_library.Ionicons;

/**
 * Created by Bogdan Melnychuk on 2/13/15.
 */
public class ProfileHolder extends TreeNode.BaseNodeViewHolder<IconTreeItemHolder.IconTreeItem> {


    public ProfileHolder(Context context) {
        super(context);
    }
    private OnProfileActionListener mActionListener;

    @Override
    public View createNodeView(TreeNode node, IconTreeItemHolder.IconTreeItem value) {
        final LayoutInflater inflater = LayoutInflater.from(context);
        final View view = inflater.inflate(R.layout.channel_profile_node, null, false);
        TextView tvValue = (TextView) view.findViewById(R.id.node_value);
        tvValue.setText(value.text);

        final IconicsImageView iconView = (IconicsImageView) view.findViewById(R.id.icon);
        iconView.setIcon(new IconicsDrawable(context).icon(value.icon));

        view.findViewById(R.id.channel_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(view.getContext(), "add channel",
                        Toast.LENGTH_SHORT).show();
                if (mActionListener != null) {
                    mActionListener.OnProfileAddAction();
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
        return R.style.TreeNodeStyleCustom;
    }

    public interface OnProfileActionListener {
        // TODO: Update argument type and name
        void OnProfileAddAction();
    }
    public void setOnProfileActionListener(OnProfileActionListener listener) {
        mActionListener = listener;
    }

}
