package com.Non_academicWebsite.Service;

import com.Non_academicWebsite.Config.JwtService;
import com.Non_academicWebsite.CustomException.ResourceNotFoundException;
import com.Non_academicWebsite.DTO.NewsDTO;
import com.Non_academicWebsite.Entity.News;
import com.Non_academicWebsite.Entity.Role;
import com.Non_academicWebsite.Entity.User;
import com.Non_academicWebsite.Repository.NewsRepo;
import com.Non_academicWebsite.Repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class NewsService {
    @Autowired
    private NewsRepo newsRepo;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserRepo userRepo;

    public List<News> add(NewsDTO newsDTO, String header, MultipartFile image)
            throws IOException, ResourceNotFoundException {
        User user = extractUser(header);
        String userId = user.getId();
        String prefix = userId.substring(0, userId.length()-7);

        News news = News.builder()
                .heading(newsDTO.getHeading())
                .body(newsDTO.getBody())
                .createdAt(new Date())
                .updatedAt(new Date())
                .filetype(image != null?image.getContentType():null)
                .filename(image != null? image.getOriginalFilename():null)
                .fileData(image != null? image.getBytes():null)
                .user(user)
                .build();

        newsRepo.save(news);
        return newsRepo.findNews(prefix);
    }

    public List<News> get(String header) throws ResourceNotFoundException {
        User user = extractUser(header);
        String userId = user.getId();
        String prefix = userId.substring(0, userId.length()-7);
        return newsRepo.findNews(prefix);
    }

    public List<News> update(Integer id, NewsDTO newsDTO, String header, MultipartFile images) throws IOException, ResourceNotFoundException {
        User user = extractUser(header);
        String userId = user.getId();
        String prefix = userId.substring(0, userId.length()-7);

        if(newsRepo.existsById(id)){
            News news = newsRepo.findById(id).orElse(null);
            if (news != null && Objects.equals(news.getUser().getId(), user.getId())){
                news.setHeading(newsDTO.getHeading() != null? newsDTO.getHeading(): news.getHeading());
                news.setBody(newsDTO.getBody() != null? newsDTO.getBody(): news.getBody());
                news.setFilename(images != null ? images.getOriginalFilename() : news.getFilename());
                news.setFiletype(images!= null? images.getContentType() : news.getFiletype());
                news.setFileData(images!= null? images.getBytes() : news.getFileData());
                news.setUpdatedAt(new Date());
                newsRepo.save(news);
            }
        }
        return newsRepo.findNews(prefix);
    }

    public User extractUser(String Authorization) throws ResourceNotFoundException {
        String token = Authorization.substring(7);
        String email = jwtService.extractUserEmail(token);
        return userRepo.findByEmail(email).orElseThrow(()-> new ResourceNotFoundException("User is not found!!!"));
    }

    public List<News> delete(Integer id, String header) throws ResourceNotFoundException {
        User user = extractUser(header);
        String userId = user.getId();
        String prefix = userId.substring(0, userId.length()-7);

        if(newsRepo.existsById(id)){
            News news = newsRepo.findById(id).orElse(null);
            if (news != null && (Objects.equals(news.getUser().getId(), user.getId()) || user.getRole() == Role.ADMIN)){
                newsRepo.deleteById(id);
            }
        }
        return newsRepo.findNews(prefix);
    }
}
