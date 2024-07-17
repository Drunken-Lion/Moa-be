package com.moa.moa.global.util.enumconvert;

import com.moa.moa.global.common.HttpMessage.Fail;
import com.moa.moa.global.exception.BusinessException;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * {@link CommonEnum}을 구현한 Enum을 제네릭에 사용
 * @author seop
 * @since moa-20
 * @version 0.0.1
 */
@Getter
@Converter
@RequiredArgsConstructor
public class MoaAttributeConverter<E extends Enum<E> & CommonEnum> extends MoaConverterUtils implements AttributeConverter<E, Integer> {
    private final Class<E> enumClass;

    @Override
    public Integer convertToDatabaseColumn(E attribute) {
        // TODO
        //  - Exception 수정 필요
        if (attribute == null) {
            throw BusinessException.builder().response(Fail.NOT_NULL).build();
        }
        return super.toCode(attribute);
    }

    @Override
    public E convertToEntityAttribute(Integer dbData) {
        return super.ofCode(enumClass, dbData);
    }
}
