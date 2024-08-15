package com.moa.moa.api.shop.itemoption.domain.entity;

import com.moa.moa.api.shop.itemoption.util.convert.ItemOptionNameConverter;
import com.moa.moa.api.shop.itemoption.util.enumerated.ItemOptionName;
import com.moa.moa.api.shop.shop.domain.entity.Shop;
import com.moa.moa.global.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
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
@Table(name = "item_option")
public class ItemOption extends BaseEntity {
    @Comment("렌탈샵 정보")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_id", columnDefinition = "BIGINT", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Shop shop;

    @Comment("상품 옵션 이름")
    @Column(name = "name", columnDefinition = "BIGINT")
    @Convert(converter = ItemOptionNameConverter.class)
    private ItemOptionName name;

    @Comment("사용 여부")
    @Column(name = "used", columnDefinition = "TINYINT")
    private Boolean used;

    @Comment("대여 시작 시간")
    @Column(name = "start_time", columnDefinition = "BIGINT")
    private Long startTime;

    @Comment("대여 끝 시간")
    @Column(name = "end_time", columnDefinition = "BIGINT")
    private Long endTime;

    @Comment("추가 금액")
    @Column(name = "add_price", columnDefinition = "DECIMAL(64, 3)")
    private BigDecimal addPrice;
}
