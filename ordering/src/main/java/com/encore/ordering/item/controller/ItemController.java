package com.encore.ordering.item.controller;

import com.encore.ordering.common.CommonResponse;
import com.encore.ordering.item.domain.Item;
import com.encore.ordering.item.dto.ItemReqDto;
import com.encore.ordering.item.dto.ItemResDto;
import com.encore.ordering.item.dto.ItemSearchDto;
import com.encore.ordering.item.service.ItemService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ItemController {

    private final ItemService itemService;

    @Autowired
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/item/create")
    public ResponseEntity<CommonResponse> itemCreate(ItemReqDto itemReqDto) {
        Item item = itemService.create(itemReqDto);
        CommonResponse commonResponse = new CommonResponse(HttpStatus.CREATED, "Item created successfully", item.getId());
        return new ResponseEntity<>(commonResponse, HttpStatus.CREATED);
    }

    @GetMapping("/items")
    public ResponseEntity<List<ItemResDto>> items(ItemSearchDto itemSearchDto, Pageable pageable) {
        List<ItemResDto> itemResDtos = itemService.findAll(itemSearchDto, pageable);
        return new ResponseEntity<>(itemResDtos, HttpStatus.OK);
    }

    @GetMapping("/item/{id}/image")
    public ResponseEntity<Resource> getImage(@PathVariable Long id) {
        Resource resource = itemService.getImage(id);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        return new ResponseEntity<>(resource, headers, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/item/{id}/update")
    public ResponseEntity<CommonResponse> itemUpdate(@PathVariable Long id, ItemReqDto itemReqDto) {
        Item item = itemService.update(id, itemReqDto);
        CommonResponse commonResponse = new CommonResponse(HttpStatus.OK, "Item updated successfully", item.getId());
        return new ResponseEntity<>(commonResponse, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/item/{id}/delete")
    public ResponseEntity<CommonResponse> itemDelete(@PathVariable Long id) {
        Item item = itemService.delete(id);
        CommonResponse commonResponse = new CommonResponse(HttpStatus.OK, "Item deleted successfully", item.getId());
        return new ResponseEntity<>(commonResponse, HttpStatus.OK);
    }
}
