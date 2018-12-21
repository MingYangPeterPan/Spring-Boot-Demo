package com.ito.vip.common.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.ito.vip.common.transaction.JdbcTransactionConfiguration;

@Target(value = {ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@EnableTransactionManagement
@Import(value = {JdbcTransactionConfiguration.class})
public @interface EnableJdbcTransaction {

}
