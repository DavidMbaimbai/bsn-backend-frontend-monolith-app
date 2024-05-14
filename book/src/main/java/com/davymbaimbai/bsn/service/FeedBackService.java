package com.davymbaimbai.bsn.service;

import com.davymbaimbai.bsn.dto.FeedBackRequest;
import com.davymbaimbai.bsn.dto.FeedBackResponse;
import com.davymbaimbai.bsn.dto.PageResponse;
import com.davymbaimbai.bsn.entity.Book;
import com.davymbaimbai.bsn.entity.FeedBack;
import com.davymbaimbai.bsn.entity.User;
import com.davymbaimbai.bsn.exception.OperationNotPermittedException;
import com.davymbaimbai.bsn.mapper.FeedBackMapper;
import com.davymbaimbai.bsn.repository.BookRepository;
import com.davymbaimbai.bsn.repository.FeedRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class FeedBackService {

    private final BookRepository bookRepository;
    private final FeedBackMapper feedBackMapper;
    private final FeedRepository feedRepository;

    public Integer save(FeedBackRequest Request, Authentication connectedUser) {
        Book book = bookRepository.findById(Request.bookId())
                .orElseThrow(() -> new EntityNotFoundException("No book found with ID::" + Request.bookId()));
        if (book.isArchived() || !book.isShareable()) {
            throw new OperationNotPermittedException("You can not give feedback to archived or not shareable book");
        }
        User user = (User) connectedUser.getPrincipal();
        if (Objects.equals(book.getOwner().getId(), user.getId())) {
            throw new OperationNotPermittedException("You can not give feedback to your own book");
        }
        FeedBack feedBack = feedBackMapper.toBook(Request);
        return feedRepository.save(feedBack).getId();
    }

    public PageResponse<FeedBackResponse> findAllFeedBackByBook(Integer bookId, int page, int size, Authentication connectedUser) {
        Pageable pageable = PageRequest.of(page, size);
        User user = (User) connectedUser.getPrincipal();
        Page<FeedBack> feedBack = feedRepository.findAllByBookId(bookId, pageable);
        List<FeedBackResponse> feedBackResponse = feedBack.stream()
                .map(f -> feedBackMapper.toFeedBackResponse(f, user.getId()))
                .toList();

        return new PageResponse<>(
                feedBackResponse,
                feedBack.getNumber(),
                feedBack.getSize(),
                feedBack.getTotalElements(),
                feedBack.getTotalPages(),
                feedBack.isFirst(),
                feedBack.isLast()
        );
    }
}
