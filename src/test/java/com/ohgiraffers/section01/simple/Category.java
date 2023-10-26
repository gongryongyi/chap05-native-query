package com.ohgiraffers.section01.simple;

import javax.persistence.*;

@Entity(name = "category_section01")
@Table(name = "tbl_category")
@SqlResultSetMappings(
        value = {
                // 자동 엔터티 매핑 : @Column으로 매핑 설정이 되어 있는 경우 사용
                @SqlResultSetMapping(
                        name = "categoryCountAutoMapping",
                        entities = {@EntityResult(entityClass = Category.class)},
                        columns = {@ColumnResult(name = "MENU_COUNT")}
                ),
                //수동 엔터티 매핑 : @Column으로 매핑 설정이 되어 있지 않은 경우 사용
                @SqlResultSetMapping(
                        name = "categoryCountManualMapping",
                        entities = {@EntityResult(entityClass = Category.class, fields = {  //직접지정해주는 fields속성
                                @FieldResult(name = "categoryCode", column = "category_code"),
                                @FieldResult(name = "categoryName", column = "category_name"),
                                @FieldResult(name = "refCategoryCode", column = "ref_category_code")
                        })},
                        columns = {@ColumnResult(name = "MENU_COUNT")}
                )
        }
)
public class Category {
    @Id
    private int categoryCode;
    private String categoryName;
    private Integer refCategoryCode;

    public Category() {
    }

    public Category(int categoryCode, String categoryName, Integer refCategoryCode) {
        this.categoryCode = categoryCode;
        this.categoryName = categoryName;
        this.refCategoryCode = refCategoryCode;
    }

    public int getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(int categoryCode) {
        this.categoryCode = categoryCode;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Integer getRefCategoryCode() {
        return refCategoryCode;
    }

    public void setRefCategoryCode(Integer refCategoryCode) {
        this.refCategoryCode = refCategoryCode;
    }

    @Override
    public String toString() {
        return "Category{" +
                "categoryCode=" + categoryCode +
                ", categoryName='" + categoryName + '\'' +
                ", refCategoryCode=" + refCategoryCode +
                '}';
    }
}
