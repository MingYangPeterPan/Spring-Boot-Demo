package com.ito.vip.common.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Import;

import com.ito.vip.common.web.upload.ExtMultipartResolver;
import com.ito.vip.common.web.upload.MutiPartRequestPreProcessFilterConfiguration;

@Target(value = {ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(value = {ExtMultipartResolver.class, MutiPartRequestPreProcessFilterConfiguration.class})
public @interface EnableStreamUpload {

}
