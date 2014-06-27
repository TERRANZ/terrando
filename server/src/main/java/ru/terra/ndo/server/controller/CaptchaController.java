package ru.terra.ndo.server.controller;

import com.sun.jersey.api.core.HttpContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.terra.ndo.server.constants.URLConstants;
import ru.terra.ndo.server.dto.captcha.CaptchDTO;
import ru.terra.ndo.server.engine.CaptchaEngine;
import ru.terra.ndo.server.engine.YandexCaptcha;
import ru.terra.server.controller.AbstractResource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;

/**
 * Date: 24.06.14
 * Time: 17:30
 */
@Path(URLConstants.DoJson.Captcha.CAPTCHA)
public class CaptchaController extends AbstractResource {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private CaptchaEngine captchaEngine = new YandexCaptcha();

    @GET
    @Path(URLConstants.DoJson.Captcha.CAP_GET)
    public CaptchDTO get(@Context HttpContext hc) {
        return captchaEngine.getCaptcha();
    }
}