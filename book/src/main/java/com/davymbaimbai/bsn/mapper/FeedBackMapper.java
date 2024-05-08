package com.davymbaimbai.bsn.mapper;

import com.davymbaimbai.bsn.dto.BookRequest;
import com.davymbaimbai.bsn.dto.FeedBackRequest;
import com.davymbaimbai.bsn.dto.FeedBackResponse;
import com.davymbaimbai.bsn.entity.Book;
import com.davymbaimbai.bsn.entity.FeedBack;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class FeedBackMapper {
    public FeedBack toBook(FeedBackRequest request) {
        return FeedBack.builder()
                .note(request.note())
                .comment(request.comment())
                .book(Book.builder()
                        .id(request.bookId())
                        .archived(false)
                        .shareable(false)
                        .build())
                .build();
    }

    public FeedBackResponse toFeedBackResponse(FeedBack feedBack, Integer id) {
        return FeedBackResponse.builder()
                .note(feedBack.getNote())
                .comment(feedBack.getComment())
                .ownFeedBack(Objects.equals(feedBack.getCreatedBy(),id))
                .build();

    }
}
