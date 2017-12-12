package com.didlink.xingxing.mediacontent.data;

import android.support.annotation.NonNull;
import android.support.v4.util.ArrayMap;
import android.util.Log;

import com.didlink.xingxing.mediacontent.bean.FileSystemType;
import com.didlink.xingxing.mediacontent.bean.ImageItem;
import com.didlink.xingxing.mediacontent.bean.ImageSet;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class ContentDataControl  {
  private static final ArrayMap<FileSystemType, List<ImageItem>> mAllFileItem = new ArrayMap<>();
  private static final ArrayList<ImageSet> mImageSetList = new ArrayList<>();

  public static void addFileByType(FileSystemType type, ImageItem imageItem) {
    if (type == null || imageItem == null) {
      return;
    }
    List<ImageItem> fileItemList = mAllFileItem.get(type);
    if (fileItemList == null) {
      fileItemList = new ArrayList<>();
      mAllFileItem.put(type, fileItemList);
    }

    fileItemList.add(imageItem);
  }


  public static void addFileListByType(FileSystemType type, List<ImageItem> fileItemList) {
    if (type == null || fileItemList == null) {
      return;
    }

    List<ImageItem> fileItems = mAllFileItem.get(type);

    if (fileItems == null) {
      fileItems = new ArrayList<>();
      mAllFileItem.put(type, fileItems);
    }
    fileItems.addAll(fileItemList);
  }

  public static void distributeToImageSet() {
    mImageSetList.clear();
    List<ImageItem> allItems = new ArrayList<>();

    allItems.addAll(mAllFileItem.get(FileSystemType.video));
    allItems.addAll(mAllFileItem.get(FileSystemType.photo));

    List<ImageItem> allImages = new ArrayList<>();
    int count = allItems.size();
    if(count <= 0 ){
      return;
    }

    for( ImageItem imageItem : allItems) {
      //Log.e("ContentDataControl", imageItem.getPath() + imageItem.getName());
      String imagePath = imageItem.getPath();

      allImages.add(imageItem);

      File imageFile = new File(imagePath);
      File imageParentFile = imageFile.getParentFile();

      ImageSet imageSet = new ImageSet();
      imageSet.name = imageParentFile.getName();
      imageSet.path = imageParentFile.getAbsolutePath();
      imageSet.cover = imageItem;

      if(!mImageSetList.contains(imageSet)){
        List<ImageItem> imageList = new ArrayList<>();
        imageList.add(imageItem);
        imageSet.imageItems = imageList;
        mImageSetList.add(imageSet);
      } else {
        mImageSetList.get(mImageSetList.indexOf(imageSet)).imageItems.add(imageItem);
      }
    }

    ImageSet imageSetAll = new ImageSet();
    imageSetAll.name= "All";
    imageSetAll.cover = allImages.get(0);
    imageSetAll.imageItems = allImages;
    imageSetAll.path = "/";

    if(mImageSetList.contains(imageSetAll)){
      mImageSetList.remove(imageSetAll);//the first item is "all images"
    }
    mImageSetList.add(0,imageSetAll);
  }

  public static List<ImageItem> getFileItemListByType(FileSystemType fileSystemType) {
    if (fileSystemType == null) {
      return null;
    }
    return mAllFileItem.get(fileSystemType);
  }

  public static int getTypeCount(FileSystemType fileSystemType) {
    List<ImageItem> fileItemList = mAllFileItem.get(fileSystemType);
    return fileItemList == null ? 0 : fileItemList.size();
  }

  public static void destory() {
    mAllFileItem.clear();
    mImageSetList.clear();
  }

}
