package com.moa.moa.global.aws.s3.bucket.domain.dto;

import lombok.Builder;

public record AddBucketDto() {
    @Builder
    public record Response(
            String keyName
    ) {
    }

    public static Response of(String keyName) {
        return Response.builder().keyName(keyName).build();
    }
}
