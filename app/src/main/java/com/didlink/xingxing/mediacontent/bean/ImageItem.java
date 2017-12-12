package com.didlink.xingxing.mediacontent.bean;

import java.io.Serializable;

public class ImageItem implements Serializable{
    public String path;
    public String name;
    public long time;

    public ImageItem(String path, String name, long time){
        this.path = path;
        this.name = name;
        this.time = time;
    }

    public String getPath() {
        return path;
    }

    public String getName() {
        return name;
    }

    public long getTime() {
        return time;
    }

    @Override
    public boolean equals(Object o) {
        try {
            ImageItem other = (ImageItem) o;
            return this.path.equalsIgnoreCase(other.path) && this.time == other.time;
        }catch (ClassCastException e){
            e.printStackTrace();
        }
        return super.equals(o);
    }
}
