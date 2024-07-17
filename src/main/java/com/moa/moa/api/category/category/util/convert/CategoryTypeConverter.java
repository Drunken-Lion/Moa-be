package com.moa.moa.api.category.category.util.convert;

import com.moa.moa.api.category.category.util.enumerated.CategoryType;
import com.moa.moa.global.util.enumconvert.CommonAttributeConverter;

public class CategoryTypeConverter extends CommonAttributeConverter<CategoryType> {
    public CategoryTypeConverter() {
        super(CategoryType.class);
    }
}
