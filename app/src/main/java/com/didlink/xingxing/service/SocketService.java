package com.didlink.xingxing.service;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.lezaizai.disneyfans.DsnApplication;
import com.lezaizai.disneyfans.R;
import com.lezaizai.disneyfans.auth.LoginActivity;
import com.lezaizai.disneyfans.config.Channel;
import com.lezaizai.disneyfans.config.Constants;
import com.lezaizai.disneyfans.config.Contact;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class SocketService {

    private String mUsername;
    private Socket mSocket;
    private Handler mHandler = new Handler();
    private OnConnectErrorListener mErrorListener;
    private OnNewMessageListener mMessageListener;
    private OnSearContactListener mSearchListener;
    private OnAddChannelListener mAddChannelListener;
    private OnGetChannelsListener mGetChannelsListener;

    public interface OnConnectErrorListener{
        /**
         */
        void onConnectError();
    }
    public interface OnNewMessageListener{
        /**
         */
        void onNewMessage(String username, String message);
    }

    public interface OnSearContactListener{
        /**
         */
        void onSearchResult(List<Contact> contacts);
        void onAddContactResult(boolean result);
    }
    public void setSearchrContactListener(OnSearContactListener listener) {
        mSearchListener = listener;
    }

    public interface OnAddChannelListener{
        /**
         */
        void onAddChannelResult(boolean result, String chid, List<Contact> contacts);
        void onSearchChannelResult(boolean result, List<Channel> channels);
        void onJoinChannelResult(boolean result, Channel channel);
    }
    public void setAddChannelListener(OnAddChannelListener listener) {
        mAddChannelListener = listener;
    }

    public interface OnGetChannelsListener{
        /**
         */
        void onGetUidChannelsResult(boolean result, List<Channel> channels);
        void onLeaveChannelResult(boolean result);
    }
    public void setGetChannelsListener(OnGetChannelsListener listener) {
        mGetChannelsListener = listener;
    }

    public SocketService() {
    }

    public void setConnectErrorListener(OnConnectErrorListener listener) {
        mErrorListener = listener;
    }
    public void setNewMessageListener(OnNewMessageListener listener) {
        mMessageListener = listener;
    }

    public void init(Socket socket, String number) {
        //mQueueDisplay = app.getQueueDisplay();
        mSocket = socket;
        mUsername = "guest";
        if (number!=null) mUsername = number;

        mSocket.on(Socket.EVENT_CONNECT_ERROR, onConnectError);
        mSocket.on(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
        mSocket.on("new message", onNewMessage);
        mSocket.on("user joined", onUserJoined);
        mSocket.on("user left", onUserLeft);
        mSocket.on("login", onLogin);
        mSocket.on("contacts", onListContact);
        mSocket.on("channeladded", onAddChannel);
        mSocket.on("channels", onSearchChannel);
        mSocket.on("channeljoined", onJoinChannel);
        mSocket.on("channelcontactadded", onAddContactChannel);
        mSocket.on("channelsbyuid", onGetChannelsByUid);
        mSocket.on("channelleft", onLeaveChannel);

        if (!mSocket.connected())
            mSocket.connect();

        //startSignIn();

    }

    public void destroy() {
        //mSocket.disconnect();
        mSocket.off(Socket.EVENT_CONNECT_ERROR, onConnectError);
        mSocket.off(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
        mSocket.off("new message", onNewMessage);
        mSocket.off("user joined", onUserJoined);
        mSocket.off("user left", onUserLeft);
        mSocket.off("login", onLogin);
        mSocket.off("contacts", onListContact);
        mSocket.off("channeladded", onAddChannel);
        mSocket.off("channels", onSearchChannel);
        mSocket.off("channeljoined", onJoinChannel);
        mSocket.off("channelcontactadded", onAddContactChannel);
        mSocket.off("channelsbyuid", onGetChannelsByUid);
        mSocket.off("channelleft", onLeaveChannel);
    }

    public void attemptSend(String message) {
        if (null == mUsername) return;
        if (!mSocket.connected()) return;

        if (TextUtils.isEmpty(message)) {
            return;
        }
        // perform the sending message attempt.
        mSocket.emit("new message", message);
    }

    private void startSignIn() {
        mSocket.emit("add user", mUsername);
    }

    private void leave() {
        mUsername = null;
        //mSocket.disconnect();
        //mSocket.connect();
    }

    private Emitter.Listener onConnectError = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (mErrorListener != null) {
                        mErrorListener.onConnectError();
                    }
                }
            });
        }
    };

    private Emitter.Listener onLogin = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            JSONObject data = (JSONObject) args[0];

            int numUsers;
            try {
                numUsers = data.getInt("numUsers");
            } catch (JSONException e) {
                return;
            }
        }
    };

    private Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String username;
                    String message;
                    try {
                        username = data.getString("username");
                        message = data.getString("message");
                    } catch (JSONException e) {
                        return;
                    }
                    if (mMessageListener != null) {
                        mMessageListener.onNewMessage(username, message);
                    }
                }
            });
        }
    };

    private Emitter.Listener onUserJoined = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String username;
                    int numUsers;
                    try {
                        username = data.getString("username");
                        numUsers = data.getInt("numUsers");
                    } catch (JSONException e) {
                        return;
                    }
                }
            };
        };
    };

    private Emitter.Listener onUserLeft = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String username;
                    int numUsers;
                    try {
                        username = data.getString("username");
                        numUsers = data.getInt("numUsers");
                    } catch (JSONException e) {
                        return;
                    }
                }
            };
        }
    };

    public void searchContact(String key) {
        if (!mSocket.connected()) return;

        JSONObject data = new JSONObject();
        try {
            data.put("key",key);
        } catch (JSONException e) {
            Log.e("ControlMessage",e.toString());
            return;
        }
        mSocket.emit( "search contacts", data);
    }

    private Emitter.Listener onListContact = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            JSONObject data = (JSONObject) args[0];
            boolean result;
            List<Contact> contacts = new ArrayList<Contact>();

            int numUsers;
            try {
                result = data.getBoolean("result");

                if (result) {
                    JSONArray users = data.getJSONArray("users");
                    if (users != null && users.length() != 0) {
                        for (int i = 0; i < users.length(); i++) {
                            JSONObject user = users.getJSONObject(i);
                            Contact contact = new Contact(user.getString("uid"),
                                    user.getString("username"),
                                    user.getString("nickname"),
                                    user.getString("gravatarpicture"),
                                    user.getString("streamid"));
                            contacts.add(contact);
                        }
                    }
                }
            } catch (JSONException e) {
                return;
            }
            if (mSearchListener != null) {
                mSearchListener.onSearchResult(contacts);
            }
                }
    };

    public void addChannel(String name, String uid) {
        if (!mSocket.connected()) return;

        JSONObject data = new JSONObject();
        try {
            data.put("name",name);
            data.put("uid",uid);
        } catch (JSONException e) {
            Log.e("ControlMessage",e.toString());
            return;
        }
        mSocket.emit( "add channel", data);
    }

    private Emitter.Listener onAddChannel = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            JSONObject data = (JSONObject) args[0];
            boolean result;
            List<Contact> contacts = new ArrayList<Contact>();

            String chid;
            try {
                result = data.getBoolean("result");
                chid = data.getString("chid");
            } catch (JSONException e) {
                return;
            }
            if (mAddChannelListener != null) {
                mAddChannelListener.onAddChannelResult(result, chid, contacts);
            }
        }
    };

    public void searchChannel(String name, String uid) {
        if (!mSocket.connected()) return;

        JSONObject data = new JSONObject();
        try {
            data.put("name",name);
            data.put("uid",uid);
        } catch (JSONException e) {
            Log.e("ControlMessage",e.toString());
            return;
        }
        mSocket.emit( "search channel", data);
    }

    private Emitter.Listener onSearchChannel = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            JSONObject data = (JSONObject) args[0];
            boolean result;
            List<Channel> channels = new ArrayList<Channel>();

            String chid;
            String name;
            try {
                result = data.getBoolean("result");
                if (result) {
                    JSONArray jchannels = data.getJSONArray("channels");
                    if (jchannels != null && jchannels.length() != 0) {
                        for (int i = 0; i < jchannels.length(); i++) {
                            JSONObject jchannel = jchannels.getJSONObject(i);
                            Channel channel = new Channel(jchannel.getString("chid"), Constants.CHANNEL_TYPE_SOCIAL, jchannel.getString("name"));
                            channel.setContacts_num(jchannel.getInt("membercnt"));
                            JSONObject jowner = jchannel.getJSONObject("owner");
                            Contact owner = new Contact(jowner.getString("uid"),
                                    jowner.getString("username"),
                                    jowner.getString("nickname"),
                                    jowner.getString("gravatarpicture"));
                            channel.setOwner(owner);
                            channels.add(channel);
                        }
                    }
                }
            } catch (JSONException e) {
                Log.e("SocketService",e.toString());
                return;
            }
            if (mAddChannelListener != null) {
                mAddChannelListener.onSearchChannelResult(result, channels);
            }
        }
    };

    public void joinChannel(String chid, String uid) {
        if (!mSocket.connected()) return;

        JSONObject data = new JSONObject();
        try {
            data.put("chid",chid);
            data.put("uid",uid);
        } catch (JSONException e) {
            Log.e("ControlMessage",e.toString());
            return;
        }
        mSocket.emit( "join channel", data);
    }

    private Emitter.Listener onJoinChannel = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            JSONObject data = (JSONObject) args[0];
            boolean result;
            JSONObject jchannel;
            Channel channel;

            String chid;
            try {
                result = data.getBoolean("result");
                jchannel = data.getJSONObject("channel");
                channel = new Channel(jchannel.getString("chid"), Constants.CHANNEL_TYPE_SOCIAL, jchannel.getString("name"));
                JSONObject jowner = jchannel.getJSONObject("owner");
                Contact owner = new Contact(jowner.getString("uid"),
                        jowner.getString("username"),
                        jowner.getString("nickname"),
                        jowner.getString("gravatarpicture"));
                channel.setOwner(owner);
                JSONArray jcontacts = jchannel.getJSONArray("contacts");
                if (jcontacts != null && jcontacts.length() != 0) {
                    for (int i = 0; i < jcontacts.length(); i++) {
                        JSONObject jcontact = jcontacts.getJSONObject(i);
                        Contact contact = new Contact(jcontact.getString("uid"),
                                jcontact.getString("username"),
                                jcontact.getString("nickname"),
                                jcontact.getString("gravatarpicture"));
                        channel.addContact(contact);
                    }
                }
            } catch (JSONException e) {
                return;
            }
            if (mAddChannelListener != null) {
                mAddChannelListener.onJoinChannelResult(result, channel);
            }
        }
    };

    public void addContactChannel(String chid, String uid) {
        if (!mSocket.connected()) return;

        JSONObject data = new JSONObject();
        try {
            data.put("chid",chid);
            data.put("uid",uid);
        } catch (JSONException e) {
            Log.e("ControlMessage",e.toString());
            return;
        }
        mSocket.emit( "add contacttochannel", data);
    }

    private Emitter.Listener onAddContactChannel = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            JSONObject data = (JSONObject) args[0];
            boolean result;

            try {
                result = data.getBoolean("result");
            } catch (JSONException e) {
                Log.e("SocketService",e.toString());
                return;
            }

            if (mSearchListener != null) {
                mSearchListener.onAddContactResult(result);
            }
        }
    };

    public void getChannelByUid(String uid) {
        if (!mSocket.connected()) return;

        JSONObject data = new JSONObject();
        try {
            data.put("uid",uid);
        } catch (JSONException e) {
            Log.e("ControlMessage",e.toString());
            return;
        }
        mSocket.emit( "get channels", data);
    }

    private Emitter.Listener onGetChannelsByUid = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            JSONObject data = (JSONObject) args[0];
            boolean result;
            List<Channel> channels = new ArrayList<Channel>();

            try {
                result = data.getBoolean("result");
                if (result) {
                    JSONArray jchannels = data.getJSONArray("channels");
                    if (jchannels != null && jchannels.length() != 0) {
                        for (int i = 0; i < jchannels.length(); i++) {
                            JSONObject jchannel = jchannels.getJSONObject(i);
                            Channel channel = new Channel(jchannel.getString("chid"), Constants.CHANNEL_TYPE_SOCIAL, jchannel.getString("name"));
                            channel.setContacts_num(jchannel.getInt("membercnt"));
                            JSONObject jowner = jchannel.getJSONObject("owner");
                            Contact owner = new Contact(jowner.getString("uid"),
                                    jowner.getString("username"),
                                    jowner.getString("nickname"),
                                    jowner.getString("gravatarpicture"));
                            channel.setOwner(owner);
                            JSONArray jcontacts = jchannel.getJSONArray("contacts");
                            if (jcontacts != null && jcontacts.length() != 0) {
                                for (int j = 0; j < jcontacts.length(); j++) {
                                    JSONObject jcontact = jcontacts.getJSONObject(j);
                                    Contact contact = new Contact(jcontact.getString("uid"),
                                            jcontact.getString("username"),
                                            jcontact.getString("nickname"),
                                            jcontact.getString("gravatarpicture"),
                                            jcontact.getString("streamid"));
                                    channel.addContact(contact);
                                }
                            }
                            channels.add(channel);
                        }
                    }
                }
            } catch (JSONException e) {
                return;
            }
            if (mGetChannelsListener != null) {
                mGetChannelsListener.onGetUidChannelsResult(result, channels);
            }
        }
    };

    public void leaveChannel(String chid, String uid) {
        if (!mSocket.connected()) return;

        JSONObject data = new JSONObject();
        try {
            data.put("chid",chid);
            data.put("uid",uid);
        } catch (JSONException e) {
            Log.e("ControlMessage",e.toString());
            return;
        }
        mSocket.emit( "leave channel", data);
    }

    private Emitter.Listener onLeaveChannel = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            JSONObject data = (JSONObject) args[0];
            boolean result;

            try {
                result = data.getBoolean("result");
            } catch (JSONException e) {
                return;
            }
            if (mGetChannelsListener != null) {
                mGetChannelsListener.onLeaveChannelResult(result);
            }
        }
    };

}
