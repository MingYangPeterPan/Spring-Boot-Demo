package com.ito.vip.common.web.upload;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileUpload;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.util.Assert;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.ito.vip.common.util.ExceptionUtil;

/**
 * CommonsMultipartResolver will save the muti-part file on application server
 * first, the return the file item collection to operate the file on server.
 * 
 * This class can be used to parse muti-part request in stream way, so can directly
 * operate the different part in request as stream, then will be more efficient then
 * use CommonsMultipartResolver.
 */
public class ExtMultipartResolver extends CommonsMultipartResolver {
    
    /**
     * For the returned result, need convert to ExtMultipartHttpServletRequest, and call
     * ExtMultipartHttpRequest.getFileItemIterator() method to get FileItemIterator.
     * 
     * To be noted: After get FileItemIterator, need loop to get the FileItemStream, and 
     *              operate the stream IMMEDIATELY. DO NOT store the FileItemStream in other
     *              collection, then loop to operate them, due to the stream always need go ahead.
     */
    @Override
    public MultipartHttpServletRequest resolveMultipart(final HttpServletRequest request) throws MultipartException {
        Assert.notNull(request, "Request must not be null");
        //Step1: Parse muti-part request as the file item iterator, so can access muti-part request as stream
        FileItemIterator iterator = parseMultipartRequest(request);
        /**
         * Step2:
         * Set the initial value of MultipartHttpServletRequest, so avoid the NullPoint exception when call the 
         * methods defines in MultipartRequest interface 
         */
        MultiValueMap<String, MultipartFile> multipartFiles = new LinkedMultiValueMap<String, MultipartFile>();
        Map<String, String[]> multipartParameters = new HashMap<String, String[]>();
        Map<String, String> multipartParameterContentTypes = new HashMap<String, String>();
        
        /**
         * Step3:
         * Return ExtMultipartHttpServletRequest object which contains FileItemIterator, then can use this iterator
         * to assess the multipart request in stream way
         */
        MultipartHttpServletRequest result = new ExtMultipartHttpRequest(request, multipartFiles, multipartParameters, multipartParameterContentTypes, iterator);
        return result;
    }

    private FileItemIterator parseMultipartRequest(HttpServletRequest request){
        String encoding = determineEncoding(request);
        FileUpload fileUpload = prepareFileUpload(encoding);
        FileItemIterator iterator = null;
        try {
            iterator = ((ServletFileUpload) fileUpload).getItemIterator(request);
        }catch(Exception exception){
          throw ExceptionUtil.wrapAsRuntimeException(exception);
        }
        return iterator;
    }
}
