package com.moa.moa.api.shop.itemoption.domain.entity;

import com.moa.moa.api.shop.item.domain.entity.Item;
import com.moa.moa.api.shop.itemoption.util.convert.ItemOptionNameConverter;
import com.moa.moa.api.shop.itemoption.util.enumerated.ItemOptionName;
import com.moa.moa.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Comment;

import java.math.BigDecimal;

@Entity
@Getter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "item_option")
public class ItemOption extends BaseEntity {
    @Comment("패키지 상품")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", columnDefinition = "BIGINT", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Item item;

    @Comment("상품 옵션 이름")
    @Column(name = "name", columnDefinition = "BIGINT")
    @Convert(converter = ItemOptionNameConverter.class)
    private ItemOptionName name;

    @Comment("사용 여부")
    @Column(name = "use", columnDefinition = "BOOLEAN")
    private Boolean use;

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
