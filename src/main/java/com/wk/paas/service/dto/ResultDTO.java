package com.wk.paas.service.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Objects;

/**
 * 返回类
 *
 * @param <T>
 * @author hhf
 */
@Data
public class ResultDTO<T> implements Serializable {

    private static final long serialVersionUID = -3430603013914181383L;

    private Integer code;

    private String msg;

    protected T data;

    private Boolean success;

    public boolean isSuccess() {
        if (null == success) {
            return Objects.nonNull(this.code) && 200 == this.code;
        }
        return success;
    }

}

