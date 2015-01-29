package ru.terra.ndo.server.constants;

import ru.terra.server.constants.CoreUrlConstants;

public class URLConstants extends CoreUrlConstants {
    public static final String SERVER_URL = "http://terranout.ath.cx:8182/terrando/";


    public class DoJson {

        public class Captcha {
            public static final String CAPTCHA = "/captcha/";
            public static final String CAP_GET = "do.get.json";
        }
    }

    public class Resources {
        public static final String RESOURCES = "res/";
        public static final String PICZ = RESOURCES + "picz/";
    }

    public class Yandex {
        public static final String PARAM_KEY = "$key";
        public static final String PARAM_CID = "$id";
        public static final String PARAM_CVAL = "$val";
        public static final String CAPTCHA_GET_URL = "http://cleanweb-api.yandex.ru/1.0/get-captcha?key="
                + PARAM_KEY;
        public static final String CAPTCHA_CHECK_URL = "http://cleanweb-api.yandex.ru/1.0/check-captcha?key="
                + PARAM_KEY + "&captcha=" + PARAM_CID + "&value=" + PARAM_CVAL;
    }

}
