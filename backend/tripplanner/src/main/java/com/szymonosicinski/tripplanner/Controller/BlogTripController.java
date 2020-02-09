package com.szymonosicinski.tripplanner.Controller;

import com.szymonosicinski.tripplanner.DTO.BlogDTO.BlogDTO;
import com.szymonosicinski.tripplanner.DTO.BlogDTO.BlogEntryCreateDTO;
import com.szymonosicinski.tripplanner.Service.BlogService;
import com.szymonosicinski.tripplanner.Util.CurrentUser;
import com.szymonosicinski.tripplanner.Util.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/{tripId}/Blog")
public class BlogTripController {
    @Autowired
    BlogService blogService;

    @GetMapping()
    public ResponseEntity getBlog(@PathVariable("tripId") final UUID tripId,
                        @CurrentUser final UserPrincipal currentUser){
        return new ResponseEntity(blogService.getBlog(tripId, currentUser), HttpStatus.OK);
    }

    @PostMapping("/Update")
    public ResponseEntity updateBlog(@RequestBody @Valid final BlogDTO blogDTO,
                                       @PathVariable("tripId") final UUID tripId,
                                       @CurrentUser final UserPrincipal currentUser){
        return new ResponseEntity(blogService.updateBlog(blogDTO,tripId,currentUser),HttpStatus.OK);
    }

    @PutMapping("/AddEntry")
    public ResponseEntity createEntry(@RequestBody @Valid final BlogEntryCreateDTO blogEntryCreateDTO,
                                      @PathVariable("tripId") final UUID tripId,
                                      @CurrentUser final UserPrincipal currentUser){
        return new ResponseEntity(blogService.createBlogEntry(blogEntryCreateDTO, tripId, currentUser), HttpStatus.OK);
    }

    @DeleteMapping("/DeleteEntry")
    public ResponseEntity deleteEntry(@RequestParam(value = "entryId") final UUID entryId,
                                      @CurrentUser final UserPrincipal currentUser){
        return new ResponseEntity(blogService.deleteBlogEntry(entryId,currentUser),HttpStatus.OK);
    }

    @PostMapping("/UpdateEntry")
    public ResponseEntity updateEntry(@RequestBody @Valid final BlogEntryCreateDTO blogEntryCreateDTO,
                                      @RequestParam(value = "entryId") final UUID entryId,
                                      @CurrentUser final UserPrincipal currentUser){
        return new ResponseEntity(blogService.updateBlogEntry(blogEntryCreateDTO,entryId,currentUser),HttpStatus.OK);
    }

}
