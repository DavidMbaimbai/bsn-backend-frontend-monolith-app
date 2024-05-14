package com.davymbaimbai.bsn.controller;

import com.davymbaimbai.bsn.dto.BookResponse;
import com.davymbaimbai.bsn.dto.FeedBackRequest;
import com.davymbaimbai.bsn.dto.FeedBackResponse;
import com.davymbaimbai.bsn.dto.PageResponse;
import com.davymbaimbai.bsn.service.FeedBackService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("feedbacks")
@RequiredArgsConstructor
@Tag(name = "FeedBack")
public class FeedBackController {

    private final FeedBackService feedBackService;

    @PostMapping
    public ResponseEntity<Integer> saveFeedBack(@Valid @RequestBody FeedBackRequest feedBackRequest, Authentication connectedUser) {
        return ResponseEntity.ok(feedBackService.save(feedBackRequest, connectedUser));
    }
    @GetMapping("/book/{book_id}")
    public ResponseEntity<PageResponse<FeedBackResponse>> findAllFeedBackByBook(
            @PathVariable("book_id") Integer bookId,
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size,
            Authentication connectedUser) {
        return ResponseEntity.ok(feedBackService.findAllFeedBackByBook(bookId,page,size,connectedUser));
    }

}
