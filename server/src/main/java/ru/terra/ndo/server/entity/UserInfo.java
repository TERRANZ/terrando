package ru.terra.ndo.server.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Date: 11.08.14
 * Time: 14:26
 */
public class UserInfo {
    private String uid = "";
    private List<PhotoInfo> photoInfos = new ArrayList<>();

    public UserInfo(String uid, List<PhotoInfo> photoInfos) {
        this.uid = uid;
        this.photoInfos = photoInfos;
    }

    public UserInfo() {
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public List<PhotoInfo> getPhotoInfos() {
        return photoInfos;
    }

    public void setPhotoInfos(List<PhotoInfo> photoInfos) {
        this.photoInfos = photoInfos;
    }

    public PhotoInfo getUnrespondedPhoto() {
        for (PhotoInfo photoInfo : photoInfos)
            if (!photoInfo.getRespond()) {
                photoInfo.setRespond(true);
                return photoInfo;
            }
        return null;
    }
}
