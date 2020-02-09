package com.szymonosicinski.tripplanner.Service;

import com.szymonosicinski.tripplanner.Entity.BlogEntry;
import com.szymonosicinski.tripplanner.Entity.Comment;
import com.szymonosicinski.tripplanner.Entity.User;
import com.szymonosicinski.tripplanner.Exception.ExceptionMessage;
import com.szymonosicinski.tripplanner.Repository.BlogEntryRepository;
import com.szymonosicinski.tripplanner.Repository.BlogRepository;
import com.szymonosicinski.tripplanner.Repository.CommentRepository;
import com.szymonosicinski.tripplanner.Repository.UserRepository;
import com.szymonosicinski.tripplanner.Util.UserPrincipal;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
public class CommentService {

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    BlogEntryRepository blogEntryRepository;

    @Autowired
    BlogRepository blogRepository;

    @Autowired
    TripService tripService;

    @Autowired
    BlogService blogService;

    @Autowired
    UserRepository userRepository;

    public Page<Comment> getComments(UUID blogEntryId,  UserPrincipal currentUser, Pageable pageable){
        if(currentUser==null)
            throw new RuntimeException(ExceptionMessage.USER_NOT_LOGGED_IN.toString());
        BlogEntry blogEntry=blogEntryRepository.findById(blogEntryId)
                .orElseThrow(()->new RuntimeException(ExceptionMessage.RESOURCE_NOT_FOUND.toString()));
        if(blogService.hasAcces(blogEntry.getBlog().getId(),currentUser))
                return commentRepository.findAllByBlogEntryId(blogEntryId, pageable);
        throw new RuntimeException(ExceptionMessage.ACCESS_DENIED.toString());
    }

    public Comment addComment(String text, UUID blogEntryId, UserPrincipal currentUser){
        if(currentUser==null)
            throw new RuntimeException(ExceptionMessage.ACCESS_DENIED.toString());
        BlogEntry blogEntry=blogEntryRepository.findById(blogEntryId)
                .orElseThrow(()->new RuntimeException(ExceptionMessage.RESOURCE_NOT_FOUND.toString()));
        if(blogService.hasAcces(blogEntry.getBlog().getId(),currentUser)){
            Comment comment = new Comment();
            comment.setDate(new Date());
            comment.setText(text);
            User user = userRepository.findById(currentUser.getId())
                    .orElseThrow(()->new RuntimeException(ExceptionMessage.RESOURCE_NOT_FOUND.toString()));
            comment.setUser(user);
            comment.setBlogEntry(blogEntry);
            commentRepository.save(comment);
            return comment;
        }
        throw new RuntimeException(ExceptionMessage.ACCESS_DENIED.toString());
    }

    public Comment deleteComment(UUID commentId, UserPrincipal currentUser){
        if(currentUser==null)
            throw new RuntimeException(ExceptionMessage.ACCESS_DENIED.toString());
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(()->new RuntimeException(ExceptionMessage.RESOURCE_NOT_FOUND.toString()));
        if(comment.getUser().getId().equals(currentUser.getId()) ||
        comment.getBlogEntry().getBlog().getTrip().getOrganizer().equals(currentUser.getId())){
            commentRepository.delete(comment);
            return comment;
        }
        throw new RuntimeException(ExceptionMessage.ACCESS_DENIED.toString());
    }
}
