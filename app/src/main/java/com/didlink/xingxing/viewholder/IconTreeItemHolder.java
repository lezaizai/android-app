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
import com.mikepenz.ionicons_typeface_library.Ionicons;

/**
 * Created by Bogdan Melnychuk on 2/12/15.
 */
public class IconTreeItemHolder extends TreeNode.BaseNodeViewHolder<IconTreeItemHolder.IconTreeItem> {
    private TextView tvValue;
    private IconicsImageView arrowView;

    public IconTreeItemHolder(Context context) {
        super(context);
    }

    @Override
    public View createNodeView(final TreeNode node, IconTreeItem value) {
        final LayoutInflater inflater = LayoutInflater.from(context);
        final View view = inflater.inflate(R.layout.channel_icon_node, null, false);
        tvValue = (TextView) view.findViewById(R.id.node_value);
        tvValue.setText(value.text);

        final IconicsImageView iconView = (IconicsImageView) view.findViewById(R.id.icon);
        iconView.setIcon(new IconicsDrawable(context).icon(value.icon));

        arrowView = (IconicsImageView) view.findViewById(R.id.arrow_icon);

        view.findViewById(R.id.btn_addFolder).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TreeNode newFolder = new TreeNode(new IconTreeItem(Ionicons.Icon.ion_folder, "New Folder"));
                getTreeView().addNode(node, newFolder);
            }
        });

        view.findViewById(R.id.btn_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getTreeView().removeNode(node);
            }
        });

        //if My computer
        if (node.getLevel() == 1) {
            view.findViewById(R.id.btn_delete).setVisibility(View.GONE);
        }

        return view;
    }

    @Override
    public void toggle(boolean active) {
        if (active) {
            arrowView.setIcon(new IconicsDrawable(context).icon(Ionicons.Icon.ion_android_arrow_down));
        } else {
            arrowView.setIcon(new IconicsDrawable(context).icon(Ionicons.Icon.ion_chevron_right));
        }
    }

    public static class IconTreeItem {
        public IIcon icon;
        public String text;

        public IconTreeItem(IIcon icon, String text) {
            this.icon = icon;
            this.text = text;
        }
    }
}
