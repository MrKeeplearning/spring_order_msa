package com.encore.ordering.item.repository;

import com.encore.ordering.item.domain.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    Page<Item> findAll(Specification<Item> specification, Pageable pageable);
    // 검색을 좀 더 간단하게 처리 가능
    Page<Item> findAllByDelYnAndCategoryLikeOrNameLike(String delYn, String category, String name, Pageable pageable);
}
