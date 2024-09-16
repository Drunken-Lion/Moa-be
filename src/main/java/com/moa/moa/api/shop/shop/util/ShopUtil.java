package com.moa.moa.api.shop.shop.util;

import com.moa.moa.api.member.custom.util.enumerated.ClothesType;
import com.moa.moa.api.member.custom.util.enumerated.EquipmentType;
import com.moa.moa.api.member.custom.util.enumerated.PackageType;
import com.moa.moa.api.shop.itemoption.util.enumerated.ItemOptionName;
import com.moa.moa.api.time.operatingtime.util.enumerated.DayType;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ShopUtil {

    // "주중 스마트4시간권+장비+의류"
    // visitDate 보고 주중/주말인지 확인
    // liftType 보고 스마트권/시간지정권[오전,오후] 확인
    // liftTime 보고 숫자 시간 확인 [4,5,6]
    // packageType 보고 리프트 + 장비 + 의류, 리프트 + 의류, 리프트 + 장비 인지 확인
    public static class date {
        public static String getWeekString(LocalDate visitDate) {
            // 날짜의 요일 확인
            DayOfWeek dayOfWeek = visitDate.getDayOfWeek();

            // 주말인지 확인 (토요일 또는 일요일)
            if (dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY) {
                return "주말";
            } else {
                return "주중";
            }
        }

        public static DayType getDayType(LocalDate visitDate) {
            // 날짜의 요일 확인
            DayOfWeek dayOfWeek = visitDate.getDayOfWeek(); // dayOfWeek = TUESDAY

            // TUESDAY -> TUE
            String dayShort = String.valueOf(dayOfWeek).substring(0, 3);

            // DayType enum에서 dayShort와 매칭되는 값 반환
            return DayType.valueOf(dayShort);
        }
    }

    public static class extraction {
        public static String getLiftType(String liftType) {
            if (liftType.contains("스마트")) {
                return "스마트";
            } else {
                // 시간지정권-오후권
                String[] str = liftType.split("-");

                return str[1];
            }
        }

        public static String getPackageType(PackageType packageType) {
            if (packageType == PackageType.LIFT_EQUIPMENT_CLOTHES) {
                return "+장비+의류";
            } else if (packageType == PackageType.LIFT_EQUIPMENT) {
                return "+장비";
            } else {
                return "+의류";
            }
        }
    }

    public static class mix {
        public static String getItemName(LocalDate visitDate,
                                         String liftType,
                                         String liftTime,
                                         PackageType packageType) {
            String weekString = date.getWeekString(visitDate);
            String liftTypeExt = extraction.getLiftType(liftType);
            String packageTypeExt = extraction.getPackageType(packageType);

            if (liftTypeExt.equals("스마트")) {
                return weekString + " 스마트" + liftTime + "시간권" + packageTypeExt;
            } else {
                return weekString + " " + liftTypeExt + packageTypeExt;
            }
        }
    }

    public static class match {
        public static List<ItemOptionName> getItemOptionNames(ClothesType clothesType, EquipmentType equipmentType) {
            List<ItemOptionName> itemOptionNames = new ArrayList<>();

            for (ItemOptionName itemOptionName : ItemOptionName.values()) {
                if (itemOptionName.name().equals(clothesType.name())
                        || itemOptionName.name().equals(equipmentType.name())) {
                    itemOptionNames.add(itemOptionName);
                }
            }

            return itemOptionNames;
        }
    }
}
