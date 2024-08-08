package com.moa.moa.api.place.place.presentation;

import com.moa.moa.api.address.address.domain.entity.Address;
import com.moa.moa.api.address.address.domain.persistence.AddressRepository;
import com.moa.moa.api.category.category.domain.entity.Category;
import com.moa.moa.api.category.category.domain.persistence.CategoryRepository;
import com.moa.moa.api.category.category.util.enumerated.CategoryType;
import com.moa.moa.api.place.place.domain.entity.Place;
import com.moa.moa.api.place.place.domain.persistence.PlaceRepository;
import com.moa.moa.api.place.place.util.enumerated.PlaceLevel;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@Transactional
class PlaceControllerTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private PlaceRepository placeRepository;

    private final GeometryFactory geometryFactory = new GeometryFactory();

    @BeforeEach
    @Transactional
    void beforeEach() {
        Category category = Category.builder()
                .categoryType(CategoryType.SKI_RESORT)
                .build();
        categoryRepository.save(category);

        final Coordinate coordinate = new Coordinate(10, 10);
        Point location = geometryFactory.createPoint(coordinate);
        Address address = Address.builder()
                .address("강원도")
                .addressDetail("어쩌군 저쩌리")
                .location(location)
                .url("www.naver.com")
                .build();
        addressRepository.save(address);

        Place place = Place.builder()
                .category(category)
                .address(address)
                .name("비발디")
                .openDate(LocalDate.of(2024, 11, 1))
                .closeDate(LocalDate.of(2025, 2, 28))
                .recLevel(PlaceLevel.LEVEL_1)
                .build();

        placeRepository.save(place);
    }

    @AfterEach
    @Transactional
    void afterEach() {
        addressRepository.deleteAll();
        placeRepository.deleteAll();
    }

    @Test
    @DisplayName("스키장 목록 조회 성공")
    void t1() throws Exception {
        ResultActions actions = mvc
                .perform(get("/v1/places")
                        .param("leftTopX", "0")
                        .param("leftTopY", "20")
                        .param("rightBottomX", "20")
                        .param("rightBottomY", "0"))
                .andDo(print());


        actions
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(PlaceController.class))
                .andExpect(handler().methodName("findAllPlace"))
                .andExpect(jsonPath("$.[0].id", instanceOf(Number.class)))
                .andExpect(jsonPath("$.[0].name", instanceOf(String.class)))
                .andExpect(jsonPath("$.[0].open", instanceOf(String.class)))
                .andExpect(jsonPath("$.[0].close", instanceOf(String.class)))
                .andExpect(jsonPath("$.[0].recLevel", instanceOf(String.class)))
                .andExpect(jsonPath("$.[0].createdAt", instanceOf(String.class)))
                .andExpect(jsonPath("$.[0].images", notNullValue()))
                .andExpect(jsonPath("$.[0].address", notNullValue()))
                .andExpect(jsonPath("$.[0].operatingTimes", notNullValue()))
                .andExpect(jsonPath("$.[0].specificDays", notNullValue()))
                .andExpect(jsonPath("$.[0].amenities", notNullValue()))
                .andExpect(jsonPath("$.[0].slopes", notNullValue()));
    }
}