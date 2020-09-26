package com.lianzai.reader.bean;

import java.io.Serializable;
import java.util.Map;

public interface NimUserInfo extends Serializable {
    String getAccount();

    String getName();

    String getAvatar();

    String getExtension();

    Map getExtensionMap();

}
