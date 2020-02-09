package com.szymonosicinski.tripplanner.Service;

import com.szymonosicinski.tripplanner.DTO.BlogDTO.BlogDTO;
import com.szymonosicinski.tripplanner.DTO.BlogDTO.BlogEntryCreateDTO;
import com.szymonosicinski.tripplanner.Entity.Blog;
import com.szymonosicinski.tripplanner.Entity.BlogEntry;
import com.szymonosicinski.tripplanner.Entity.Trip;
import com.szymonosicinski.tripplanner.Exception.ExceptionMessage;
import com.szymonosicinski.tripplanner.Repository.BlogEntryRepository;
import com.szymonosicinski.tripplanner.Repository.BlogRepository;
import com.szymonosicinski.tripplanner.Util.UserPrincipal;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class BlogService {
    @Autowired
    ModelMapper modelMapper;

    @Autowired
    BlogRepository blogRepository;

    @Autowired
    BlogEntryRepository blogEntryRepository;

    @Autowired
    TripService tripService;

    public Blog getBlog(UUID tripId, UserPrincipal currentUser){
        if(currentUser==null)
            throw new RuntimeException(ExceptionMessage.USER_NOT_LOGGED_IN.toString());
        Trip trip=tripService.getById(tripId, currentUser);
        return trip.getBlog();
    }

    public Blog updateBlog(BlogDTO blogDto, UUID tripId, UserPrincipal currentUser){
        if(currentUser==null)
            throw new RuntimeException(ExceptionMessage.USER_NOT_LOGGED_IN.toString());
        Trip trip=tripService.getByIdAsOrganizer(tripId, currentUser);
        Blog blog=trip.getBlog();
        modelMapper.map(blogDto,blog);
        blogRepository.save(blog);
        trip.setBlog(blog);
        return blog;
    }

    public Page<Blog> getBlogs(Pageable pageable, UserPrincipal currentUser){
        Set<Blog> blogs = blogRepository.findAllByVisibility(Blog.Visibility.guests);
        if(currentUser!=null) {
            blogs.addAll(blogRepository.findAllByVisibilityIsNot(Blog.Visibility.participants));
            List<Trip> trips = tripService.getByParticipant(currentUser);
            trips.addAll(tripService.getByOrganizer(currentUser));
            for (Trip trip : trips) {
                blogs.addAll(blogRepository.findAllByVisibilityAndTripId(Blog.Visibility.participants, trip.getId()));
            }
        }
        return new PageImpl<>(new ArrayList<>(blogs),pageable,blogs.size());
    }

    public BlogEntry createBlogEntry(BlogEntryCreateDTO blogEntryCreateDTO, UUID tripId, UserPrincipal currentUser){
        if(currentUser==null)
            throw new RuntimeException(ExceptionMessage.USER_NOT_LOGGED_IN.toString());
        Trip trip=tripService.getByIdAsOrganizer(tripId, currentUser);
        BlogEntry blogEntry = modelMapper.map(blogEntryCreateDTO, BlogEntry.class);
        blogEntry.setDate(new Date());
        blogEntry.setBlog(trip.getBlog());
        blogEntryRepository.save(blogEntry);
        return blogEntry;
    }

    public BlogEntry deleteBlogEntry(UUID blogEntryId, UserPrincipal currentUser){
        if(currentUser==null)
            throw new RuntimeException(ExceptionMessage.USER_NOT_LOGGED_IN.toString());
        BlogEntry blogEntry=blogEntryRepository.findById(blogEntryId)
                .orElseThrow(()->new RuntimeException(ExceptionMessage.RESOURCE_NOT_FOUND.toString()));
        if(!blogEntry.getBlog().getTrip().getOrganizer().equals(currentUser.getId()))
            throw new RuntimeException(ExceptionMessage.ACCESS_DENIED.toString());
        blogEntryRepository.delete(blogEntry);
        return blogEntry;
    }

    public BlogEntry updateBlogEntry(BlogEntryCreateDTO blogEntryCreateDTO, UUID blogEntryId, UserPrincipal currentUser){
        if(currentUser==null)
            throw new RuntimeException(ExceptionMessage.USER_NOT_LOGGED_IN.toString());
        BlogEntry blogEntry=blogEntryRepository.findById(blogEntryId)
                .orElseThrow(()->new RuntimeException(ExceptionMessage.RESOURCE_NOT_FOUND.toString()));
        if(!blogEntry.getBlog().getTrip().getOrganizer().equals(currentUser.getId()))
            throw new RuntimeException(ExceptionMessage.ACCESS_DENIED.toString());
        modelMapper.map(blogEntryCreateDTO,blogEntry);
        blogEntryRepository.save(blogEntry);
        return blogEntry;
    }

    public Page<BlogEntry> getBlogEntries(UUID blogId, Pageable pageable, UserPrincipal currentUser){
        Blog blog = blogRepository.findById(blogId)
                .orElseThrow(()->new RuntimeException(ExceptionMessage.RESOURCE_NOT_FOUND.toString()));
        switch (blog.getVisibility()){
            case guests:
                return blogEntryRepository.findAllByBlogId(blogId, pageable);
            case users:
                if(currentUser!=null)
                    return blogEntryRepository.findAllByBlogId(blogId, pageable);
                else
                    throw new RuntimeException(ExceptionMessage.ACCESS_DENIED.toString());
            case participants:
                if(currentUser==null)
                    throw new RuntimeException(ExceptionMessage.ACCESS_DENIED.toString());
                if(!tripService.isParticipant(blog.getTrip().getId(),currentUser) &&
                !blog.getTrip().getOrganizer().equals(currentUser.getId()))
                    throw new RuntimeException(ExceptionMessage.ACCESS_DENIED.toString());
                return blogEntryRepository.findAllByBlogId(blogId, pageable);
        }
        throw new RuntimeException(ExceptionMessage.ACCESS_DENIED.toString());
    }

    public boolean hasAcces(UUID blogId, UserPrincipal currentUser){
        if(currentUser==null)
            throw new RuntimeException(ExceptionMessage.USER_NOT_LOGGED_IN.toString());
        Blog blog = blogRepository.findById(blogId)
                .orElseThrow(()->new RuntimeException(ExceptionMessage.RESOURCE_NOT_FOUND.toString()));
        switch (blog.getVisibility()){
            case guests:
                return true;
            case users:
                if(currentUser!=null)
                    return true;
                else
                    throw new RuntimeException(ExceptionMessage.ACCESS_DENIED.toString());
            case participants:
                if(currentUser==null)
                    throw new RuntimeException(ExceptionMessage.ACCESS_DENIED.toString());
                if(!tripService.isParticipant(blog.getTrip().getId(),currentUser) &&
                        !blog.getTrip().getOrganizer().equals(currentUser.getId()))
                    throw new RuntimeException(ExceptionMessage.ACCESS_DENIED.toString());
                return true;
        }
        throw new RuntimeException(ExceptionMessage.ACCESS_DENIED.toString());
    }
}
