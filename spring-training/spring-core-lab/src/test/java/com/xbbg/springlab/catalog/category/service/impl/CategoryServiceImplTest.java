package com.xbbg.springlab.catalog.category.service.impl;

import com.xbbg.springlab.catalog.category.dto.CategoryRequest;
import com.xbbg.springlab.catalog.category.dto.CategoryResponse;
import com.xbbg.springlab.catalog.category.repository.InMemoryCategoryRepository;
import com.xbbg.springlab.exception.BadRequestException;
import com.xbbg.springlab.exception.ConflictException;
import com.xbbg.springlab.exception.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CategoryServiceImplTest {

    private CategoryServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new CategoryServiceImpl(new InMemoryCategoryRepository());
    }

    @Test
    void createsHierarchyAndBuildsTree() {
        CategoryResponse electronics = service.create(request("ELECTRONICS", "Electronics", null, 1));
        CategoryResponse mobiles = service.create(request("mobiles", "Mobile Phones", electronics.id(), 1));
        CategoryResponse android = service.create(request("ANDROID", "Android Phones", mobiles.id(), 1));

        assertEquals("MOBILES", mobiles.code());
        assertEquals("mobile-phones", mobiles.slug());
        assertEquals(electronics.id(), mobiles.parentCategoryId());

        var tree = service.tree();
        assertEquals(1, tree.size());
        assertEquals("ELECTRONICS", tree.getFirst().code());
        assertEquals("MOBILES", tree.getFirst().children().getFirst().code());
        assertEquals("ANDROID", tree.getFirst().children().getFirst().children().getFirst().code());
        assertEquals(android.id(), service.getById(android.id()).id());
    }

    @Test
    void rejectsDuplicateCodeMissingParentAndCycles() {
        CategoryResponse electronics = service.create(request("ELECTRONICS", "Electronics", null, 1));
        CategoryResponse mobiles = service.create(request("MOBILES", "Mobile Phones", electronics.id(), 1));

        assertThrows(ConflictException.class, () -> service.create(request("electronics", "Other", null, 2)));
        assertThrows(NotFoundException.class, () -> service.create(request("LAPTOPS", "Laptops", 999L, 1)));
        assertThrows(BadRequestException.class, () -> service.update(
                electronics.id(),
                request("ELECTRONICS", "Electronics", mobiles.id(), 1)
        ));
    }

    @Test
    void deletesLeafAndBlocksParentThatStillHasChildren() {
        CategoryResponse electronics = service.create(request("ELECTRONICS", "Electronics", null, 2));
        CategoryResponse mobiles = service.create(request("MOBILES", "Mobile Phones", electronics.id(), 1));

        assertThrows(ConflictException.class, () -> service.delete(electronics.id()));
        service.delete(mobiles.id());
        service.delete(electronics.id());
        assertThrows(NotFoundException.class, () -> service.getById(electronics.id()));
    }

    @Test
    void updatesStatusAndSortsByDisplayOrder() {
        service.create(request("ACCESSORIES", "Accessories", null, 2));
        CategoryResponse electronics = service.create(request("ELECTRONICS", "Electronics", null, 1));

        CategoryResponse inactive = service.updateStatus(electronics.id(), false);

        assertFalse(inactive.active());
        assertEquals("ELECTRONICS", service.list().getFirst().code());
        assertTrue(service.list().get(1).active());
    }

    private CategoryRequest request(String code, String name, Long parentId, int displayOrder) {
        return new CategoryRequest(code, name, null, name + " products", parentId, displayOrder, true);
    }
}
