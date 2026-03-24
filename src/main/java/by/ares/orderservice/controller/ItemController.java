package by.ares.orderservice.controller;

import by.ares.orderservice.dto.request.ItemRequest;
import by.ares.orderservice.dto.request.StatusRequest;
import by.ares.orderservice.dto.response.ItemDto;
import by.ares.orderservice.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {

    private final ItemService itemService;

    @GetMapping
    public ResponseEntity<Page<ItemDto>> findAll(@PageableDefault Pageable pageable) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(itemService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemDto> findById(@PathVariable Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(itemService.findById(id));
    }

    @PostMapping
    public ResponseEntity<Long> save(@RequestBody ItemRequest itemRequest) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(itemService.save(itemRequest));
    }

    @PutMapping("{/id}")
    public ResponseEntity<Long> update(@PathVariable Long id,
                                       @RequestBody ItemRequest itemRequest) {
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(itemService.update(itemRequest, id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Long> changeStatus(@PathVariable Long id,
                                             @RequestBody StatusRequest statusRequest) {
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(itemService.changeStatus(id, statusRequest));
    }

    @DeleteMapping("{/id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        itemService.delete(id);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }
}
