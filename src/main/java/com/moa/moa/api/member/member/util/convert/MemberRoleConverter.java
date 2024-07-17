package com.moa.moa.api.member.member.util.convert;

import com.moa.moa.api.member.member.util.enumerated.MemberRole;
import com.moa.moa.global.util.enumconvert.MoaAttributeConverter;

public class MemberRoleConverter extends MoaAttributeConverter<MemberRole> {
    public MemberRoleConverter() {
        super(MemberRole.class);
    }
}
