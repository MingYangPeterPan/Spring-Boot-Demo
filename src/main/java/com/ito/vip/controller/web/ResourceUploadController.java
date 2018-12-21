package com.ito.vip.controller.web;

import com.ito.vip.common.util.ExceptionUtil;
import com.ito.vip.common.web.Message;
import com.ito.vip.common.web.Message.MessageEntry;
import com.ito.vip.common.web.upload.ExtMultipartHttpRequest;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.util.Streams;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import java.io.FileOutputStream;
import java.io.InputStream;

@RequestMapping(value = {"/resources"})
@RestController
@ApiIgnore
public class ResourceUploadController {

    @RequestMapping(value = {"/upload"}, method = {RequestMethod.POST}, consumes = {"multipart/form-data"})
    public MessageEntry streamUploadResource(HttpServletRequest httpServletRequest) throws Exception{
        if (httpServletRequest instanceof ExtMultipartHttpRequest) {
            ExtMultipartHttpRequest request = (ExtMultipartHttpRequest) httpServletRequest;
            FileItemIterator inerator = request.getFileItemIterator();
            while (inerator.hasNext()) {
                FileItemStream stream = inerator.next();
                this.saveStreamIntoFile(stream);
            }
        }
        return Message.ok("success");
    }

    private void saveStreamIntoFile(FileItemStream fileItemStream){
        FileOutputStream outputStream = null;
        InputStream inputStream = null;
        try{
            if(!fileItemStream.isFormField()){
                String fileName = fileItemStream.getName();
                if(!StringUtils.isEmpty(fileName)){
                    inputStream = fileItemStream.openStream();
                    outputStream = new FileOutputStream("C:\\UploadResource\\" + fileName);
                    byte[] buffer = new byte[10240];
                    Streams.copy(inputStream, outputStream, true, buffer);
                }
            }
        }catch(Exception exception){
            throw ExceptionUtil.wrapAsRuntimeException(exception);
        }finally{
            try{
                if(inputStream!=null){
                    inputStream.close();
                }
                if(outputStream!=null){
                    outputStream.close();
                }
            }catch(Exception exception){
                throw ExceptionUtil.wrapAsRuntimeException(exception);
            }
        }
    }
}
