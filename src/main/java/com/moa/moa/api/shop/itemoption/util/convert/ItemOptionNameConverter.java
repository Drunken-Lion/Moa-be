package com.moa.moa.api.shop.itemoption.util.convert;

import com.moa.moa.api.shop.itemoption.util.enumerated.ItemOptionName;
import com.moa.moa.global.util.enumconvert.MoaAttributeConverter;

public class ItemOptionNameConverter extends MoaAttributeConverter<ItemOptionName> {
    public ItemOptionNameConverter() {
        super(ItemOptionName.class);
    }
}
