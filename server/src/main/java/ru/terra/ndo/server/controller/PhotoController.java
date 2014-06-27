package ru.terra.ndo.server.controller;

import com.sun.jersey.api.core.HttpContext;
import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.terra.ndo.server.constants.FilePatchConstants;
import ru.terra.ndo.server.dto.PhotoDTO;
import ru.terra.ndo.server.engine.CaptchaEngine;
import ru.terra.ndo.server.engine.PhotoInfo;
import ru.terra.ndo.server.engine.TerrandoEngine;
import ru.terra.ndo.server.engine.YandexCaptcha;
import ru.terra.server.controller.AbstractResource;
import ru.terra.server.dto.CommonDTO;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.io.*;
import java.util.Date;

/**
 * Date: 24.06.14
 * Time: 17:28
 */
@Path("/photo")
public class PhotoController extends AbstractResource {
    private CaptchaEngine captchaEngine = new YandexCaptcha();
    private TerrandoEngine terrandoEngine = TerrandoEngine.getInstance();
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @POST
    @Path("/add")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public CommonDTO add(@Context HttpContext hc,
                         @FormDataParam("info") String info,
                         @FormDataParam("lat") Double lat,
                         @FormDataParam("lon") Double lon,
                         @FormDataParam("captcha") String captcha,
                         @FormDataParam("capval") String capval,
                         @FormDataParam("file") InputStream uploadedInputStream,
                         @FormDataParam("file") FormDataContentDisposition fileDetail) {
        logger.info("Receiving image from " + info + " with lat=" + lat + " lon=" + lon);
        logger.info("Captcha: " + captcha + " and val = " + capval);
        if (captchaEngine.checkCaptcha(capval, captcha)) {
            logger.info("Captcha is OK, saving photo");
            String fileName = "";
            if (uploadedInputStream != null && fileDetail != null && fileDetail.getFileName() != null) {
                String uploadFileFileName = fileDetail.getFileName();
                fileName += String.valueOf(new Date().getTime());
                fileName += uploadFileFileName.substring(uploadFileFileName.lastIndexOf("."), uploadFileFileName.length());

                String uploadedFileLocation = FilePatchConstants.getPiczFolder() + "/" + info + "/" + fileName;
                // save it
                File targetDir = new File(FilePatchConstants.getPiczFolder() + "/" + info + "/");
                if (!targetDir.exists())
                    targetDir.mkdirs();
                writeToFile(uploadedInputStream, uploadedFileLocation);
                terrandoEngine.regPhoto(new PhotoInfo(info, lon, lat, fileName));
            }
            return new CommonDTO();
        }
        return new CommonDTO();
    }

    @GET
    @Path("/get")
    public PhotoDTO get(@Context HttpContext hc) {
        String uid = hc.getRequest().getCookieNameValueMap().getFirst("uid");
        logger.info(uid + " requested new photo");
        return terrandoEngine.getRandomPhoto(uid);
    }

    // save uploaded file to new location
    private void writeToFile(InputStream uploadedInputStream, String uploadedFileLocation) {
        logger.info("Saving photo to " + uploadedFileLocation);
        try {
            OutputStream out;
            int read = 0;
            byte[] bytes = new byte[1024];

            out = new FileOutputStream(new File(uploadedFileLocation));
            while ((read = uploadedInputStream.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }
            out.flush();
            out.close();
        } catch (IOException e) {

            e.printStackTrace();
        }
    }
}
