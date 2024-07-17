package com.moa.moa.global.util.enumconvert;

import com.moa.moa.global.common.HttpMessage;
import com.moa.moa.global.exception.BusinessException;
import java.util.EnumSet;

/**
 * Converter Common Function
 * @author seop
 * @since moa-20
 * @version 0.0.1
 */
abstract class MoaConverterUtils {
    /**
     * code -> desc
     */
    protected <T extends Enum<T> & CommonEnum> T ofCode(Class<T> enumClass, Integer code) {
        if (code == null) {
            return null;
        }

        // TODO
        //  - Exception 수정 필요
        return EnumSet.allOf(enumClass).stream()
                .filter(e -> e.getCode() == code)
                .findAny()
                .orElseThrow(() -> BusinessException.builder()
                        .response(HttpMessage.Fail.NOT_FOUND_CODE)
                        .build());
    }

    /**
     * desc -> code
     */
    protected <T extends Enum<T> & CommonEnum> Integer toCode(T desc) {
        return desc.getCode();
    }
}
