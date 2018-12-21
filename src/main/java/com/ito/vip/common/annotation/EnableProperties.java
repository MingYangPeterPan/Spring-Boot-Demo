package com.ito.vip.common.annotation;

import com.ito.vip.property.AuthenticationIgnoreProperty;
import com.ito.vip.property.ClientKeyProperty;
import com.ito.vip.property.TokenProperty;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target(value = {ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(value = {TokenProperty.class, AuthenticationIgnoreProperty.class, ClientKeyProperty.class})
public @interface EnableProperties {

}
