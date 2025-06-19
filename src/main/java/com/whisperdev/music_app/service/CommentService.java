package com.whisperdev.music_app.service;

import com.whisperdev.music_app.dto.comment.CommentResponse;
import com.whisperdev.music_app.dto.page.MetaResponse;
import com.whisperdev.music_app.dto.page.PageResponse;
import com.whisperdev.music_app.dto.track.TrackResponse;
import com.whisperdev.music_app.model.Comment;
import com.whisperdev.music_app.model.Track;
import com.whisperdev.music_app.model.User;
import com.whisperdev.music_app.repository.CommentRepository;
import com.whisperdev.music_app.repository.TrackRepository;
import com.whisperdev.music_app.repository.UserRepository;
import com.whisperdev.music_app.utils.mapper.CommentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CommentService {
    @Autowired private CommentRepository commentRepository;
    @Autowired private TrackRepository trackRepository;
    @Autowired private UserRepository userRepository;
    public PageResponse<CommentResponse> getByTrackId(Integer current, Integer pageSize, String trackId) {
        Pageable pageable = PageRequest.of(current-1, pageSize, Sort.by("createdAt").descending());
        Optional<Track> trackOptional = trackRepository.findById(trackId);
        if(trackOptional.isPresent()) {
            Page<Comment> commentPage = commentRepository.getByTrack(trackOptional.get(), pageable);
            PageResponse<CommentResponse> pageResponse = new PageResponse<>();
            MetaResponse meta = new MetaResponse();

            meta.setCurrent(pageable.getPageNumber()+1);
            meta.setPageSize(pageable.getPageSize());

            meta.setPages(commentPage.getTotalPages());

            meta.setTotal(commentPage.getTotalElements());

            pageResponse.setMeta(meta);
            List<CommentResponse> responses = commentPage.getContent().stream()
                    .map(i -> CommentMapper.toCommentResponse(i)).toList();
            pageResponse.setResult(responses);
            return pageResponse;
        }
        return null;
    }

    public CommentResponse save(Comment comment) {
        if(trackRepository.existsById(comment.getTrack().getId())) {
            Track track = trackRepository.findById(comment.getTrack().getId()).orElse(null);
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            User user = userRepository.findByUsername(username);
            comment.setUser(user);
            comment.setTrack(track);
            comment = commentRepository.save(comment);
            return CommentMapper.toCommentResponse(comment);
        }
        return null;
    }
}
