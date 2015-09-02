package com.fb.util;

public class FamilyBizException extends Exception {

	private static final long serialVersionUID = 966959823451750330L;

	public FamilyBizException(String message, Exception root) {
		super(message, root);
	}

	public FamilyBizException(String message) {
		super(message);
	}

}
