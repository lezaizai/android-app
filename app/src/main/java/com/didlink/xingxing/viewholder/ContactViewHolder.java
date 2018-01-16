package com.didlink.xingxing.viewholder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.didlink.xingxing.R;
import com.didlink.xingxing.models.Contact;
import com.lezaizai.atv.model.TreeNode;
import com.lezaizai.imagepicker.GlideImagePresenter;
import com.lezaizai.imagepicker.ImagePresenter;

/**
 * Created by Bogdan Melnychuk on 2/13/15.
 */
public class ContactViewHolder extends TreeNode.BaseNodeViewHolder<Contact> {

    public ContactViewHolder(Context context) {
        super(context);
    }

    @Override
    public View createNodeView(TreeNode node, Contact value) {
        final LayoutInflater inflater = LayoutInflater.from(context);
        final View view = inflater.inflate(R.layout.channel_contact_node, null, false);
        ImageView imageAvator = (ImageView) view.findViewById(R.id.avator);
        ImagePresenter presenter = new GlideImagePresenter();

        TextView userNameLabel = (TextView) view.findViewById(R.id.contactname);
        userNameLabel.setText(value.getNickname());

        presenter.onPresentWebImage(imageAvator,"http://storage.disneyfans.cn/" + value.getGravatarpicture(),55);

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
