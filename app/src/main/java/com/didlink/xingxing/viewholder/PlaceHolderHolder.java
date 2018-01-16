package com.didlink.xingxing.viewholder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.didlink.xingxing.R;
import com.lezaizai.atv.model.TreeNode;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.iconics.view.IconicsImageView;
import com.mikepenz.ionicons_typeface_library.Ionicons;

import java.util.Random;

/**
 * Created by Bogdan Melnychuk on 2/13/15.
 */
public class PlaceHolderHolder extends TreeNode.BaseNodeViewHolder<PlaceHolderHolder.PlaceItem> {


    public PlaceHolderHolder(Context context) {
        super(context);
    }

    @Override
    public View createNodeView(TreeNode node, PlaceItem value) {
        final LayoutInflater inflater = LayoutInflater.from(context);
        final View view = inflater.inflate(R.layout.channel_place_node, null, false);


        TextView placeName = (TextView) view.findViewById(R.id.place_name);
        placeName.setText(value.name);

        Random r = new Random();
        boolean like = r.nextBoolean();

        IconicsImageView likeView = (IconicsImageView) view.findViewById(R.id.like);
        if (like) {
            likeView.setIcon(new IconicsDrawable(context).icon(Ionicons.Icon.ion_thumbsup));
        } else {
            likeView.setIcon(new IconicsDrawable(context).icon(Ionicons.Icon.ion_thumbsdown));
        }
        return view;
    }

    @Override
    public void toggle(boolean active) {
    }

    @Override
    public int getContainerStyle() {
        return R.style.TreeNodeStyleCustom;
    }

    public static class PlaceItem {
        public String name;

        public PlaceItem(String name) {
            this.name = name;
        }
        // rest will be hardcoded
    }

}
