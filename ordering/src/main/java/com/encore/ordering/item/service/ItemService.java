package com.encore.ordering.item.service;

import com.encore.ordering.item.domain.Item;
import com.encore.ordering.item.dto.ItemReqDto;
import com.encore.ordering.item.dto.ItemResDto;
import com.encore.ordering.item.dto.ItemSearchDto;
import com.encore.ordering.item.repository.ItemRepository;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Transactional
@Slf4j
@Service
public class ItemService {

    private final ItemRepository itemRepository;

    @Autowired
    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public Item create(ItemReqDto itemReqDto) {
        MultipartFile multipartFile = itemReqDto.getItemImage();
        String fileName = multipartFile.getOriginalFilename();
        Item newItem = Item.builder()
                .name(itemReqDto.getName())
                .category(itemReqDto.getCategory())
                .price(itemReqDto.getPrice())
                .stockQuantity(itemReqDto.getStockQuantity())
                .build();
        Item item = itemRepository.save(newItem);
        Path path = Paths.get("C:/Users/nokbu/Desktop/tmp/", item.getId() + "_" + fileName);
        item.setImagePath(path.toString());
        try {
            byte[] bytes = multipartFile.getBytes();    // 이미지 파일을 바이트 형태로 변환
            Files.write(path, bytes, StandardOpenOption.CREATE, StandardOpenOption.WRITE);
        } catch (IOException e) {
            throw new IllegalArgumentException("image not available");
        }
        return item;
    }

    public Item delete(Long id) {
        Item item = itemRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("not found item"));
        item.deleteItem();
        return item;
    }

    public Resource getImage(Long id) {
        // item url이 무조건 있다고 가정하고 진행한다.(추가적으로 valid 체크 필요)
        Item item = itemRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("not found item"));
        String imagePath = item.getImagePath();
        Path path = Paths.get(imagePath);
        Resource resource;
        try {
            resource = new UrlResource(path.toUri());
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("url form is not valid");
        }
        return resource;
    }

    public Item update(Long id, ItemReqDto itemReqDto) {
        Item item = itemRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("not found item"));
        MultipartFile multipartFile = itemReqDto.getItemImage();
        String fileName = multipartFile.getOriginalFilename();
        Path path = Paths.get("C:/Users/nokbu/Desktop/tmp/", item.getId() + "_" + fileName);
        item.updateItem(itemReqDto.getName(), itemReqDto.getCategory(), itemReqDto.getPrice(),
                itemReqDto.getStockQuantity(), path.toString());
        try {
            byte[] bytes = multipartFile.getBytes();    // 이미지 파일을 바이트 형태로 변환
            Files.write(path, bytes, StandardOpenOption.CREATE, StandardOpenOption.WRITE);
        } catch (IOException e) {
            throw new IllegalArgumentException("image not available");
        }
        return item;
    }

    public List<ItemResDto> findAll(ItemSearchDto itemSearchDto, Pageable pageable) {
        // 검색을 위해 Specification 객체를 사용
        // Specification 객체는 복잡한 쿼리를 명세를 이용한 정의를 하여 쉽게 생성
        Specification<Item> spec = new Specification<Item>() {
            @Override
            // root: 엔티티의 속성에 접근하기 위한 객체
            // CriteriaBuilder: 쿼리를 생성하기 위한 객체
            public Predicate toPredicate(Root<Item> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                ArrayList<Predicate> predicates = new ArrayList<>();
                if (itemSearchDto.getName() != null) {
                    predicates.add(criteriaBuilder.like(root.get("name"), "%" + itemSearchDto.getName() + "%"));
                }
                if (itemSearchDto.getCategory() != null) {
                    predicates.add(criteriaBuilder.like(root.get("category"), "%" + itemSearchDto.getCategory() + "%"));
                }
                predicates.add(criteriaBuilder.equal(root.get("delYn"), "N"));
                Predicate[] predicatesArr = new Predicate[predicates.size()];
                for (int i = 0; i < predicates.size(); i++) {
                    predicatesArr[i] = predicates.get(i);
                }
                Predicate predicate = criteriaBuilder.and(predicatesArr);
                return predicate;
            }
        };
        Page<Item> items = itemRepository.findAll(spec, pageable);
        List<Item> itemList = items.getContent();
        List<ItemResDto> itemResDtos = itemList.stream().map(item -> ItemResDto.builder()
                .id(item.getId())
                .name(item.getName())
                .category(item.getCategory())
                .price(item.getPrice())
                .stockQuantity(item.getStockQuantity())
                .imagePath(item.getImagePath())
                .build()).collect(Collectors.toList());
        return itemResDtos;
    }
}
