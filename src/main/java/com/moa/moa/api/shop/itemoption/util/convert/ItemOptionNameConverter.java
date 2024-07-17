package com.moa.moa.api.shop.itemoption.util.convert;

import com.moa.moa.api.shop.itemoption.util.enumerated.ItemOptionName;
import com.moa.moa.global.util.enumconvert.CommonAttributeConverter;

public class ItemOptionNameConverter extends CommonAttributeConverter<ItemOptionName> {
    public ItemOptionNameConverter() {
        super(ItemOptionName.class);
    }
}
