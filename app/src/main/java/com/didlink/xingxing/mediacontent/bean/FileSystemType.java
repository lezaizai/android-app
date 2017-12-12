package com.didlink.xingxing.mediacontent.bean;

public enum FileSystemType {
  photo,
  music,
  video,
  text,
  zip;

  public static FileSystemType getFileTypeByOrdinal(int ordinal) {
    for (FileSystemType type : values()) {
      if (type.ordinal() == ordinal) {
        return type;
      }
    }
    return photo;
  }
}
