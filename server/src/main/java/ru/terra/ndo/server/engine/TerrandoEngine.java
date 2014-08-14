package ru.terra.ndo.server.engine;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.terra.ndo.server.dto.PhotoDTO;
import ru.terra.ndo.server.entity.PhotoInfo;
import ru.terra.ndo.server.entity.UserInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Date: 25.06.14
 * Time: 18:16
 */
public class TerrandoEngine {
    private static TerrandoEngine instance = new TerrandoEngine();
    private Map<String, UserInfo> users = new HashMap<>();
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private TerrandoEngine() {
    }

    public static TerrandoEngine getInstance() {
        return instance;
    }

    synchronized
    public PhotoDTO getRandomPhoto(String uid) {
        PhotoDTO ret = new PhotoDTO();
        for (String key : users.keySet())
            if (users.get(key).getPhotoInfos().size() == 0)
                continue;
            else {
                if (!key.equals(uid)) {
                    if (users.get(uid).getPhotoInfos().size() > 0) {
                        PhotoInfo info = users.get(key).getUnrespondedPhoto();
                        if (info != null)
                            return new PhotoDTO(info, key);
                    }
                }
            }
        return ret;
    }

    public void regPhoto(PhotoInfo photoInfo, String uid) {
        logger.info("Reg photo: " + photoInfo);
        UserInfo userInfo = users.get(uid);
        if (userInfo == null) {
            userInfo = new UserInfo(uid, new ArrayList<PhotoInfo>());
            users.put(uid, userInfo);
        }
        userInfo.getPhotoInfos().add(photoInfo);
    }
}
