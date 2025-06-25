package com.whisperdev.music_app.service;

import com.whisperdev.music_app.dto.page.MetaResponse;
import com.whisperdev.music_app.dto.page.PageResponse;
import com.whisperdev.music_app.dto.track.TrackResponse;
import com.whisperdev.music_app.model.Track;
import com.whisperdev.music_app.model.User;
import com.whisperdev.music_app.repository.TrackRepository;
import com.whisperdev.music_app.repository.UserRepository;
import com.whisperdev.music_app.utils.mapper.TrackMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
public class TrackService {
    @Autowired private TrackRepository trackRepository;
    @Autowired private UserRepository userRepository;
    public List<Track> getAllByLimitAndCategory(int limit,String category) {

        Instant sevenDaysAgo = Instant.now().minus(7, ChronoUnit.DAYS); // ngày 7 ngày trước

        Pageable pageable = PageRequest.of(0,limit, Sort.by("countPlay").descending());
        return trackRepository.findAllByCategoryAndCreatedAtAfter(category,sevenDaysAgo,pageable).getContent();
    }
    public List<Track> getAllByPage(Integer current,Integer pageSize){
        Pageable pageable = PageRequest.of(current-1,pageSize);
        return trackRepository.findAll(pageable).getContent();
    }
    public void deleteById(String id) {
        trackRepository.deleteById(id);
    }

    public Track save(Track track) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username);
        if(user!=null){
            track.setUser(user);
        }
        return trackRepository.save(track);
    }

    public PageResponse<TrackResponse> getTracksByUser(String id, Integer current, Integer pageSize) {
        Pageable pageable = PageRequest.of(current-1,pageSize);
        Page<Track> trackPage = trackRepository.findByUser(userRepository.findById(id).get(), pageable);
        PageResponse<TrackResponse> pageResponse = new PageResponse<>();
        MetaResponse meta = new MetaResponse();

        meta.setCurrent(pageable.getPageNumber()+1);
        meta.setPageSize(pageable.getPageSize());

        meta.setPages(trackPage.getTotalPages());

        meta.setTotal(trackPage.getTotalElements());

        pageResponse.setMeta(meta);
        List<TrackResponse> trackResponses =
                trackPage.getContent().stream().map(i -> TrackMapper.toTrackResponse(i)).toList();
        pageResponse.setResult(trackResponses);
        return pageResponse;
    }

    public Track getById(String id) {
        Optional<Track> trackOpt = trackRepository.findById(id);
        if(trackOpt.isPresent()){
            return trackOpt.get();
        }
        return null;
    }

    public PageResponse<TrackResponse> getBySearch(String search, Integer current, Integer pageSize) {
        Pageable pageable = PageRequest.of(current-1,pageSize);
        Page<Track> trackPage = trackRepository
                .findByTitleContainingIgnoreCaseOrCategoryContainingIgnoreCase(search,search,pageable);
        PageResponse<TrackResponse> pageResponse = new PageResponse<>();
        MetaResponse meta = new MetaResponse();

        meta.setCurrent(pageable.getPageNumber()+1);
        meta.setPageSize(pageable.getPageSize());

        meta.setPages(trackPage.getTotalPages());

        meta.setTotal(trackPage.getTotalElements());

        pageResponse.setMeta(meta);
        List<TrackResponse> trackResponses = trackPage.getContent().stream().map(TrackMapper::toTrackResponse).toList();
        pageResponse.setResult(trackResponses);
        return pageResponse;
    }
}
