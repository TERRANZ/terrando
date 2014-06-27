package ru.terra.ndo.server.engine;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.terra.ndo.server.constants.URLConstants;
import ru.terra.ndo.server.dto.PhotoDTO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Date: 25.06.14
 * Time: 18:16
 */
public class TerrandoEngine {
    private static TerrandoEngine instance = new TerrandoEngine();
    private Map<String, List<PhotoInfo>> photos = new HashMap<>();
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private TerrandoEngine() {
    }

    public static TerrandoEngine getInstance() {
        return instance;
    }


    public PhotoDTO getRandomPhoto(String uid) {
        PhotoDTO ret = new PhotoDTO();
        for (String key : photos.keySet())
            if (photos.get(key).size() == 0)
                continue;
            else {
                if (!key.equals(uid)) {
                    List<PhotoInfo> photoInfos = photos.get(key);
                    PhotoInfo info = photoInfos.get(0);
                    photoInfos.remove(info);
                    ret.url = URLConstants.SERVER_URL + "res/picz/" + info.uid + "/" + info.photo;
                    ret.lat = info.lat;
                    ret.lon = info.lon;
                }
            }
        return ret;
    }

    public void regPhoto(PhotoInfo photoInfo) {
        logger.info("Reg photo: " + photoInfo);
        String uid = photoInfo.uid;
        List<PhotoInfo> photoInfos = photos.get(uid);
        if (photoInfos == null) {
            photoInfos = new ArrayList<>();
            photos.put(uid, photoInfos);
        }
        photoInfos.add(photoInfo);
    }
}
