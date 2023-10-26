package com.ohgiraffers.section01.simple;

import org.junit.jupiter.api.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class NativeQueryTests {
    private static EntityManagerFactory entityManagerFactory;

    private EntityManager entityManager;

    @BeforeAll
    public static void initFactory() {
        entityManagerFactory = Persistence.createEntityManagerFactory("jpatest");
    }

    @BeforeEach
    public void initManager() {
        entityManager = entityManagerFactory.createEntityManager();
    }

    @AfterAll
    public static void closeFactory() {
        entityManagerFactory.close();
    }

    @AfterEach
    public void closeManager() {
        entityManager.close();
    }

    @Test
    public void 결과_타입을_정의한_네이티브_쿼리_사용_테스트(){
        //given
        int menuCodeParameter = 15;
        //when
        String query = "SELECT MENU_CODE, MENU_NAME, MENU_PRICE, CATEGORY_CODE, ORDERABLE_STATUS" +
                " FROM TBL_MENU WHERE MENU_CODE = ?";
//        String query = "SELECT MENU_CODE, MENU_NAME, MENU_PRICE" +  // 네이티브 쿼리 사용할 때 일부 컬럼만 조회할수 없다
//                " FROM TBL_MENU WHERE MENU_CODE = ?";
        Query nativeQuery = entityManager.createNativeQuery(query, Menu.class)
                .setParameter(1, menuCodeParameter);  //위치기반 파라미터만 사용한다.
        Menu foundMenu = (Menu) nativeQuery.getSingleResult();
        //then
        assertNotNull(foundMenu);
        assertTrue(entityManager.contains(foundMenu));
        System.out.println(foundMenu);
    }

    @Test
    public void 결과_타입을_정의할_수_없는_경우_조회_테스트(){
        //when
        String query = "SELECT MENU_NAME, MENU_PRICE FROM TBL_MENU";  //엔티티의 일부 컬럼만 조회할 경우 결과 타입을 특정할 수 없으므로 Object[] 타입을 사용한다.
        List<Object[]> menuList = entityManager.createNativeQuery(query).getResultList();
        //List<Object[]> menuList = entityManager.createNativeQuery(query, Object[].class).getResultList();  //오류남
        //then
        assertNotNull(menuList);
        menuList.forEach(row -> {
            Stream.of(row).forEach(col -> System.out.println(col + " "));
            System.out.println();
        });
    }

    @Test
    public void 자동_결과_매핑을_사용한_조회_테스트(){
        //when
        String query = "SELECT" +
                " A.CATEGORY_CODE, A.CATEGORY_NAME, A.REF_CATEGORY_CODE, NVL(v.MENU_COUNT, 0) MENU_COUNT" +
                " FROM TBL_CATEGORY A" +
                " LEFT JOIN (SELECT COUNT(*) AS MENU_COUNT, B.CATEGORY_CODE" +
                "            FROM TBL_MENU B" +
                "            GROUP BY B.CATEGORY_CODE) V ON (A.CATEGORY_CODE = v.CATEGORY_CODE)" +
                " ORDER BY 1";

        Query nativeQuery = entityManager.createNativeQuery(query, "categoryCountAutoMapping");
        List<Object[]> categoryList = nativeQuery.getResultList();
        assertNotNull(categoryList);
        assertTrue(entityManager.contains(categoryList.get(0)[0]));
        categoryList.forEach(row ->{
            Stream.of(row).forEach(col -> System.out.print(col + " "));
            System.out.println();
        });

    }

    @Test
    public void 수동_결과_매핑을_사용한_조회_테스트(){
        //when
        String query = "SELECT" +
                " A.CATEGORY_CODE, A.CATEGORY_NAME, A.REF_CATEGORY_CODE, NVL(v.MENU_COUNT, 0) MENU_COUNT" +
                " FROM TBL_CATEGORY A" +
                " LEFT JOIN (SELECT COUNT(*) AS MENU_COUNT, B.CATEGORY_CODE" +
                "            FROM TBL_MENU B" +
                "            GROUP BY B.CATEGORY_CODE) V ON (A.CATEGORY_CODE = v.CATEGORY_CODE)" +
                " ORDER BY 1";

        Query nativeQuery = entityManager.createNativeQuery(query, "categoryCountManualMapping");
        List<Object[]> categoryList = nativeQuery.getResultList();
        assertNotNull(categoryList);
        assertTrue(entityManager.contains(categoryList.get(0)[0]));
        categoryList.forEach(row ->{
            Stream.of(row).forEach(col -> System.out.print(col + " "));
            System.out.println();
        });

    }



}
