package com.ito.vip.common.web.upload;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItemIterator;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest;

public class ExtMultipartHttpRequest extends DefaultMultipartHttpServletRequest{

    private FileItemIterator fileItemIterator;
    
    public ExtMultipartHttpRequest(HttpServletRequest request, MultiValueMap<String, MultipartFile> mpFiles,
            Map<String, String[]> mpParams, Map<String, String> mpParamContentTypes, FileItemIterator fileItemIterator) {
        super(request);
        setMultipartFiles(mpFiles);
        setMultipartParameters(mpParams);
        setMultipartParameterContentTypes(mpParamContentTypes);
        this.fileItemIterator = fileItemIterator;
    }

    public FileItemIterator getFileItemIterator() {
        return fileItemIterator;
    }
}
