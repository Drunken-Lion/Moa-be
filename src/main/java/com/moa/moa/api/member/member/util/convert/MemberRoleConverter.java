package com.moa.moa.api.member.member.util.convert;

import com.moa.moa.api.member.member.util.enumerated.MemberRole;
import com.moa.moa.global.util.enumconvert.CommonAttributeConverter;

public class MemberRoleConverter extends CommonAttributeConverter<MemberRole> {
    public MemberRoleConverter() {
        super(MemberRole.class);
    }
}
