package ru.terra.ndo.server.dto.captcha;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class CaptchDTO extends CaptchaCommonDTO {
    public String image = "";

    @Override
    public String toString() {
        return "CaptchDTO [image=" + image + ", cid=" + cid + ", text=" + text + "]";
    }

    public CaptchDTO() {
    }
}
