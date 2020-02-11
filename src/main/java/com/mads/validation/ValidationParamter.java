package com.mads.validation;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * @Description:  参数校验,一般写在生产者端 也可以直接在写方法参数时 直接使用Validation的注解来标注参数
 * @Date 2020/2/11
 * @Version V1.0
 * @Author Mads
 **/
public class ValidationParamter implements Serializable {

    private static final long serialVersionUID = 3272130956512010063L;

    @NotNull
    @Min(1)
    private Long uid;

    @NotNull
    @Size(min = 32,max = 32)
    private String token;

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
