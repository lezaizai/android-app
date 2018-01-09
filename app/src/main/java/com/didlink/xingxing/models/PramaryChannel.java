package com.didlink.xingxing.models;

import com.didlink.xingxing.config.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xingxing on 2016/7/10.
 */
public class PramaryChannel {

    private List<Channel> channels;

    public void PramaryChannel(){
    }

    public static PramaryChannel root() {
        PramaryChannel root = new PramaryChannel();
        root.channels = new ArrayList<>();
        Channel myChannel = new Channel(Constants.PRAMARY_CHANNEL_MY, Constants.CHANNEL_TYPE_DEFAULT, "");
        Channel publicChannel = new Channel(Constants.PRAMARY_CHANNEL_PUBLIC, Constants.CHANNEL_TYPE_DEFAULT, "");
        root.channels.add(myChannel);
        root.channels.add(publicChannel);
        return root;
    }

    public Channel getMyChannel() {
        return channels.get(0);
    }
    public Channel getPublicChannel() {
        return channels.get(1);
    }

}
