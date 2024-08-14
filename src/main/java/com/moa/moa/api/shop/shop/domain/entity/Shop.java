package com.moa.moa.api.shop.shop.domain.entity;

import com.moa.moa.api.address.address.domain.entity.Address;
import com.moa.moa.api.category.category.domain.entity.Category;
import com.moa.moa.api.member.member.domain.entity.Member;
import com.moa.moa.api.shop.item.domain.entity.Item;
import com.moa.moa.api.shop.itemoption.domain.entity.ItemOption;
import com.moa.moa.api.shop.naverreview.domain.entity.NaverReview;
import com.moa.moa.api.shop.placeshop.domain.entity.PlaceShop;
import com.moa.moa.api.shop.review.domain.entity.Review;
import com.moa.moa.api.time.businesstime.domain.entity.BusinessTime;
import com.moa.moa.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Comment;

import java.util.ArrayList;
import java.util.List;

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

    @Comment("비지니스 타임")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "business_time_id", columnDefinition = "BIGINT", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private BusinessTime businessTime;
    
    @Comment("렌탈샵 이름")
    @Column(name = "name", columnDefinition = "VARCHAR(50)")
    private String name;

    @Comment("픽업가능 여부")
    @Column(name = "pick_up", columnDefinition = "BOOLEAN")
    private Boolean pickUp;

    @Comment("스마트스토어 링크")
    @Column(name = "url", columnDefinition = "VARCHAR(2048)")
    private String url;

    @Comment("샵과 관련된 장소들")
    @OneToMany(mappedBy = "shop")
    @Builder.Default
    private List<PlaceShop> placeShops = new ArrayList<>();

    @Comment("샵의 네이버 리뷰")
    @OneToMany(mappedBy = "shop")
    @Builder.Default
    private List<NaverReview> naverReviews = new ArrayList<>();

    @Comment("샵의 리뷰")
    @OneToMany(mappedBy = "shop")
    @Builder.Default
    private List<Review> reviews = new ArrayList<>();

    @Comment("샵의 패키지 상품들")
    @OneToMany(mappedBy = "shop")
    @Builder.Default
    private List<Item> items = new ArrayList<>();

    @Comment("패키지 상품 옵션들")
    @OneToMany(mappedBy = "shop")
    @Builder.Default
    private List<ItemOption> itemOptions = new ArrayList<>();
}
