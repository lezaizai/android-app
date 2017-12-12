package com.didlink.xingxing.mediacontent.data;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.util.Log;

import com.didlink.xingxing.mediacontent.bean.FileSystemType;
import com.didlink.xingxing.mediacontent.bean.ImageItem;

import java.util.ArrayList;
import java.util.List;

public class ContentDataLoadTask extends AsyncTask<Void, Void, Void> {
  private Context mContext;
  private ContentResolver mContentResolver;
  private OnContentDataLoadListener mOnContentDataLoadListener;
  public ContentDataLoadTask(Context mContext) {
    this.mContext = mContext;
  }

  public OnContentDataLoadListener getmOnContentDataLoadListener() {
    return mOnContentDataLoadListener;
  }

  public void setmOnContentDataLoadListener(OnContentDataLoadListener mOnContentDataLoadListener) {
    this.mOnContentDataLoadListener = mOnContentDataLoadListener;
  }

  @Override
  protected void onPreExecute() {
    super.onPreExecute();

    if (mOnContentDataLoadListener != null) {
      mOnContentDataLoadListener.onStartLoad();
    }

    mContentResolver = mContext.getContentResolver();
  }

  @Override
  protected Void doInBackground(Void... params) {
    ContentDataControl.addFileListByType(FileSystemType.photo, getAllPhoto());
    ContentDataControl.addFileListByType(FileSystemType.video, getAllVideo());
//    ContentDataControl.addFileListByType(FileSystemType.music, getAllMusic());
//    ContentDataControl.addFileListByType(FileSystemType.text, getAllText());
//    ContentDataControl.addFileListByType(FileSystemType.zip, getAllZip());
    //
    ContentDataControl.distributeToImageSet();

    return null;
  }

  private List<ImageItem> getAllPhoto() {
    List<ImageItem> photos = new ArrayList<>();
    String[] projection = new String[]{
            MediaStore.Images.ImageColumns._ID,
            MediaStore.Images.ImageColumns.DATA,
            MediaStore.Images.ImageColumns.DISPLAY_NAME,
            MediaStore.Images.ImageColumns.DATE_MODIFIED};

    //asc 按升序排列
    //desc 按降序排列
    //projection 是定义返回的数据，selection 通常的sql 语句，例如  selection=MediaStore.Images.ImageColumns.MIME_TYPE+"=? " 那么 selectionArgs=new String[]{"jpg"};
    Cursor cursor = mContentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection,
            null,
            null,
            MediaStore.Images.ImageColumns.DATE_MODIFIED + "  desc");

    String imageId = null;
    String fileName;
    String filePath;
    long time;

    while (cursor.moveToNext()) {
      imageId = cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns._ID));
      fileName = cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.DISPLAY_NAME));
      filePath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA));
      time = Long.valueOf(cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATE_MODIFIED)));
      ImageItem fileItem = new ImageItem(filePath, fileName, time);

      //Log.e("ryze_photo", imageId + " -- " + fileName + " -- " + filePath);

      photos.add(fileItem);
    }
    cursor.close();
    cursor = null;
    return photos;
  }

  private List<ImageItem> getAllMusic() {
    List<ImageItem> musics = new ArrayList<>();
    String[] projection = new String[]{
            MediaStore.Audio.AudioColumns._ID,
            MediaStore.Audio.AudioColumns.DATA,
            MediaStore.Audio.AudioColumns.DISPLAY_NAME,
            MediaStore.Audio.AudioColumns.DATE_MODIFIED};

    Cursor cursor = mContentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            projection,
            null,
            null,
            MediaStore.Audio.AudioColumns.DATE_MODIFIED + " desc");

    String fileId;
    String fileName;
    String filePath;
    long time;

    while (cursor.moveToNext()) {
      fileId = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.AudioColumns._ID));
      fileName = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.DISPLAY_NAME));
      filePath = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.DATA));
      time = Long.valueOf(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.DATE_MODIFIED)));

      Log.e("ryze_music", fileId + " -- " + fileName + " -- " + filePath);

      ImageItem fileItem = new ImageItem(filePath, fileName, time);
      musics.add(fileItem);
    }

    cursor.close();
    cursor = null;
    return musics;
  }

  private List<ImageItem> getAllVideo() {
    List<ImageItem> videos = new ArrayList<>();

    String[] projection = new String[]{
            MediaStore.Video.VideoColumns._ID,
            MediaStore.Video.VideoColumns.DATA,
            MediaStore.Video.VideoColumns.DISPLAY_NAME,
            MediaStore.Video.VideoColumns.DATE_MODIFIED};

    Cursor cursor = mContentResolver.query(
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
            projection,
            null,
            null,
            MediaStore.Video.VideoColumns.DATE_MODIFIED + " desc");

    String fileId;
    String fileName;
    String filePath;
    long time;

    while (cursor.moveToNext()) {
      fileId = cursor.getString(cursor.getColumnIndex(MediaStore.Video.VideoColumns._ID));
      fileName = cursor.getString(cursor.getColumnIndex(MediaStore.Video.VideoColumns.DISPLAY_NAME));
      filePath = cursor.getString(cursor.getColumnIndex(MediaStore.Video.VideoColumns.DATA));
      time = Long.valueOf(cursor.getString(cursor.getColumnIndex(MediaStore.Video.VideoColumns.DATE_MODIFIED)));

      //Log.e("ryze_video", fileId + " -- " + fileName + " -- " + filePath);

      ImageItem fileItem = new ImageItem(filePath, fileName, time);
      videos.add(fileItem);
    }

    cursor.close();
    cursor = null;
    return videos;

  }

  private List<ImageItem> getAllText() {
    List<ImageItem> texts = new ArrayList<>();
    String[] projection = new String[]{
            MediaStore.Files.FileColumns._ID,
            MediaStore.Files.FileColumns.DATA,
            MediaStore.Files.FileColumns.TITLE,
            MediaStore.Files.FileColumns.MIME_TYPE,
            MediaStore.Files.FileColumns.DATE_MODIFIED};

    String selection = MediaStore.Files.FileColumns.MIME_TYPE + "= ? "
        + " or " + MediaStore.Files.FileColumns.MIME_TYPE + " = ? "
        + " or " + MediaStore.Files.FileColumns.MIME_TYPE + " = ? "
        + " or " + MediaStore.Files.FileColumns.MIME_TYPE + " = ? "
        + " or " + MediaStore.Files.FileColumns.MIME_TYPE + " = ? ";

    String[] selectionArgs = new String[]{
            "text/plain",
            "application/msword",
            "application/pdf",
            "application/vnd.ms-powerpoint",
            "application/vnd.ms-excel"};

    Cursor cursor = mContentResolver.query(
            MediaStore.Files.getContentUri("external"),
            projection,
            selection,
            selectionArgs,
            MediaStore.Files.FileColumns.DATE_MODIFIED + " desc");

    String fileId;
    String fileName;
    String filePath;
    long time;

    while (cursor.moveToNext()) {
      fileId = cursor.getString(cursor.getColumnIndex(MediaStore.Files.FileColumns._ID));
      fileName = cursor.getString(cursor.getColumnIndex(MediaStore.Files.FileColumns.TITLE));
      filePath = cursor.getString(cursor.getColumnIndex(MediaStore.Files.FileColumns.DATA));
      time = Long.valueOf(cursor.getString(cursor.getColumnIndex(MediaStore.Files.FileColumns.DATE_MODIFIED)));

      Log.e("ryze_text", fileId + " -- " + fileName + " -- " + "--" + cursor.getString(cursor.getColumnIndex(MediaStore.Files.FileColumns.MIME_TYPE)) + filePath);

      ImageItem fileItem = new ImageItem(filePath, fileName, time);
      texts.add(fileItem);
    }

    cursor.close();
    cursor = null;
    return texts;
  }

  private List<ImageItem> getAllZip() {
    List<ImageItem> zips = new ArrayList<>();

    String[] projection = new String[]{
            MediaStore.Files.FileColumns._ID,
            MediaStore.Files.FileColumns.DATA,
            MediaStore.Files.FileColumns.TITLE,
            MediaStore.Files.FileColumns.DATE_MODIFIED};

    String selection = MediaStore.Files.FileColumns.MIME_TYPE + "= ? ";

    String[] selectionArgs = new String[]{"application/zip"};

    Cursor cursor = mContentResolver.query(
            MediaStore.Files.getContentUri("external"),
            projection,
            selection,
            selectionArgs,
            MediaStore.Files.FileColumns.DATE_MODIFIED + " desc");

    String fileId;
    String fileName;
    String filePath;
    long time;

    while (cursor.moveToNext()) {
      fileId = cursor.getString(cursor.getColumnIndex(MediaStore.Files.FileColumns._ID));
      fileName = cursor.getString(cursor.getColumnIndex(MediaStore.Files.FileColumns.TITLE));
      filePath = cursor.getString(cursor.getColumnIndex(MediaStore.Files.FileColumns.DATA));
      time = Long.valueOf(cursor.getString(cursor.getColumnIndex(MediaStore.Files.FileColumns.DATE_MODIFIED)));

      Log.e("ryze_zip", fileId + " -- " + fileName + " -- " + filePath);

      ImageItem fileItem = new ImageItem(filePath, fileName, time);
      zips.add(fileItem);
    }
    return zips;
  }

  @Override
  protected void onPostExecute(Void aVoid) {
    super.onPostExecute(aVoid);
    if (mOnContentDataLoadListener != null) {
      mOnContentDataLoadListener.onFinishLoad();
    }
  }

  @Override
  protected void onCancelled() {
    super.onCancelled();
    if (mOnContentDataLoadListener != null) {
      mOnContentDataLoadListener.onFinishLoad();
    }
  }

  public interface OnContentDataLoadListener {
    public void onStartLoad();
    public void onFinishLoad();
  }

}
