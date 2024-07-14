package com.moa.moa.api.shop.shop.domain.entity;

import com.moa.moa.api.address.address.domain.entity.Address;
import com.moa.moa.api.category.category.domain.entity.Category;
import com.moa.moa.api.member.member.domain.entity.Member;
import com.moa.moa.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Comment;

@Entity
@Getter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "shop")
public class Shop extends BaseEntity {
    @Comment("회원")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", columnDefinition = "BIGINT", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Member member;

    @Comment("카테고리")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", columnDefinition = "BIGINT", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Category category;

    @Comment("주소")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "address_id", columnDefinition = "BIGINT", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Address address;

    @Comment("렌탈샵 이름")
    @Column(name = "name", columnDefinition = "VARCHAR(50)")
    private String name;

    @Comment("픽업가능 여부")
    @Column(name = "pick_up", columnDefinition = "BOOLEAN")
    private Boolean pickUp;

    @Comment("스마트스토어 링크")
    @Column(name = "url", columnDefinition = "VARCHAR(2048)")
    private String url;
}
