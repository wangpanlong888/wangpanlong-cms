package com.wangpanlong.applicant.common;

import org.apache.commons.codec.digest.DigestUtils;

public class CmsUtils {

public static String encry(String src,String salt) {
			return DigestUtils.md5Hex(salt+src+salt);
	}
	
}
