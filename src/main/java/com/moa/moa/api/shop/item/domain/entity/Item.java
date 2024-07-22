package com.moa.moa.api.shop.item.domain.entity;

import com.moa.moa.api.place.liftticket.domain.entity.LiftTicket;
import com.moa.moa.api.shop.item.util.convert.LiftTicketTypeConverter;
import com.moa.moa.api.shop.item.util.enumerated.LiftTicketType;
import com.moa.moa.api.shop.itemoption.domain.entity.ItemOption;
import com.moa.moa.api.shop.shop.domain.entity.Shop;
import com.moa.moa.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Comment;

@Entity
@Getter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "item")
public class Item extends BaseEntity {
    @Comment("렌탈샵")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_id", columnDefinition = "BIGINT", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Shop shop;

    @Comment("리프트권")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lift_ticket_id", columnDefinition = "BIGINT", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private LiftTicket liftTicket;

    @Comment("리프트권 타입")
    @Column(name = "type", columnDefinition = "BIGINT")
    @Convert(converter = LiftTicketTypeConverter.class)
    private LiftTicketType type;

    @Comment("리프트권 이름")
    @Column(name = "name", columnDefinition = "VARCHAR(255)")
    private String name;

    @Comment("기본 가격")
    @Column(name = "price", columnDefinition = "DECIMAL(64, 3)")
    private BigDecimal price;

    @Comment("사용 여부")
    @Column(name = "used", columnDefinition = "TINYINT")
    private Boolean used;

    @Comment("패키지 상품 옵션들")
    @OneToMany(mappedBy = "item")
    @Builder.Default
    private List<ItemOption> itemOptions = new ArrayList<>();
}
