package ru.terra.ndo.server.dto.captcha;

import ru.terra.server.dto.CommonDTO;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class CaptchaCommonDTO extends CommonDTO
{
	public String cid = "";
	public String text = "";

    public CaptchaCommonDTO() {
    }
}
