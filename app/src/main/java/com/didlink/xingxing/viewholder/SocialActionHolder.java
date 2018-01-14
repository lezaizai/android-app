package com.didlink.xingxing.viewholder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.github.johnkil.print.PrintView;
import com.lezaizai.atv.model.TreeNode;
import com.lezaizai.disneyfans.R;

import java.util.Random;

/**
 * Created by Bogdan Melnychuk on 2/13/15.
 */
public class SocialActionHolder extends TreeNode.BaseNodeViewHolder<SocialActionHolder.SocialActionItem> {
    private OnSocialActionListener mActionListener;

    public SocialActionHolder(Context context) {
        super(context);
    }

    @Override
    public View createNodeView(TreeNode node, SocialActionItem value) {
        final LayoutInflater inflater = LayoutInflater.from(context);
        final View view = inflater.inflate(R.layout.channel_social_action, null, false);

        view.findViewById(R.id.add_contact).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(view.getContext(), "add contact",
                        Toast.LENGTH_SHORT).show();
                if (mActionListener != null) {
                    mActionListener.OnAddContact();
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

    public static class SocialActionItem {
        public int icon;

        public SocialActionItem(int icon) {
            this.icon = icon;
        }
        // rest will be hardcoded
    }

    public void setSocialActionListener(OnSocialActionListener listener) {
        this.mActionListener = listener;
    }

    public interface OnSocialActionListener {
        void OnAddContact();
    }

}
