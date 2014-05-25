package com.impaq.pos.service;

public class CodeValidationUtil {

	public static boolean isValidProductCode(String code) {
		return code != null && !code.isEmpty();
	}
}
