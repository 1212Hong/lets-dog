package com.dog.fileupload.common.exception;


import com.dog.fileupload.common.error.ErrorCodeIfs;

public interface ApiExceptionIfs {

	ErrorCodeIfs getErrorCodeIfs();

	String getErrorDescription();
}
