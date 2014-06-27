package ru.terra.ndo.server.engine;

import ru.terra.ndo.server.dto.captcha.CaptchDTO;

public interface CaptchaEngine {
    public CaptchDTO getCaptcha();

    public Boolean checkCaptcha(String val, String cid);
}
