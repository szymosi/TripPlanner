package com.szymonosicinski.tripplanner.Controller;

import com.szymonosicinski.tripplanner.Entity.Comment;
import com.szymonosicinski.tripplanner.Service.CommentService;
import com.szymonosicinski.tripplanner.Util.CurrentUser;
import com.szymonosicinski.tripplanner.Util.UserPrincipal;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/Comment")
public class CommentController {
    @Autowired
    CommentService commentService;

    @GetMapping()
    public Page<Comment> getComments(@RequestParam(value = "entryId") final UUID entryId,
                                      @CurrentUser final UserPrincipal currentUser,
                                      @RequestParam(value = "page", defaultValue = "0") final int page,
                                      @RequestParam(value = "size", defaultValue = "100") final int pageSize){
        return commentService.getComments(entryId, currentUser,
                PageRequest.of(page,pageSize, Sort.Direction.ASC,"date"));
    }

    @PutMapping("/CreateComment")
    public ResponseEntity createComment(@RequestParam (value = "text") @Length(max=500) final String text,
                                        @RequestParam(value = "entryId") final UUID entryId,
                                        @CurrentUser final UserPrincipal currentUser){
        return new ResponseEntity( commentService.addComment(text,entryId,currentUser), HttpStatus.OK);
    }

    @DeleteMapping("/DeleteComment")
    public ResponseEntity createComment(@RequestParam(value = "commentId") final UUID commentId,
                                        @CurrentUser final UserPrincipal currentUser){
        return new ResponseEntity( commentService.deleteComment(commentId,currentUser), HttpStatus.OK);
    }
}
