package com.encore.ordering.item.controller;

import com.encore.ordering.common.CommonResponse;
import com.encore.ordering.item.domain.Item;
import com.encore.ordering.item.dto.ItemQuantityUpdateDto;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class ItemController {

    private final ItemService itemService;
    private final RestTemplate restTemplate;

    @Autowired
    public ItemController(ItemService itemService, RestTemplate restTemplate) {
        this.itemService = itemService;
        this.restTemplate = restTemplate;
    }

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

    @PatchMapping("/item/{id}/update")
    public ResponseEntity<CommonResponse> itemUpdate(@PathVariable Long id, ItemReqDto itemReqDto) {
        Item item = itemService.update(id, itemReqDto);
        CommonResponse commonResponse = new CommonResponse(HttpStatus.OK, "Item updated successfully", item.getId());
        return new ResponseEntity<>(commonResponse, HttpStatus.OK);
    }

    @GetMapping("/item/{id}")
    public ResponseEntity<ItemResDto> findById(@PathVariable Long id) {
        ItemResDto itemResDto = itemService.findById(id);
        return new ResponseEntity<>(itemResDto, HttpStatus.OK);
    }

    @PostMapping("/item/updateQuantity")
    public ResponseEntity<CommonResponse> itemUpdateQuantity(@RequestBody List<ItemQuantityUpdateDto> itemQuantityUpdateDtos) {
        itemService.updateQuantity(itemQuantityUpdateDtos);
        CommonResponse commonResponse = new CommonResponse(HttpStatus.OK, "Item updated successfully", null);
        return new ResponseEntity<>(commonResponse, HttpStatus.OK);
    }

    @DeleteMapping("/item/{id}/delete")
    public ResponseEntity<CommonResponse> itemDelete(@PathVariable Long id) {
        Item item = itemService.delete(id);
        CommonResponse commonResponse = new CommonResponse(HttpStatus.OK, "Item deleted successfully", item.getId());
        return new ResponseEntity<>(commonResponse, HttpStatus.OK);
    }
}
