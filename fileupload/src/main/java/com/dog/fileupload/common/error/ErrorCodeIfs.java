package com.dog.fileupload.common.error;

public interface ErrorCodeIfs {
	Integer getHttpStatusCode();

	Integer getErrorCode();

	String getDescription();
}

