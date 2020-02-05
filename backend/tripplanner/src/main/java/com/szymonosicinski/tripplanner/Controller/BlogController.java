package com.szymonosicinski.tripplanner.Controller;

import com.szymonosicinski.tripplanner.Entity.Blog;
import com.szymonosicinski.tripplanner.Entity.BlogEntry;
import com.szymonosicinski.tripplanner.Service.BlogService;
import com.szymonosicinski.tripplanner.Util.CurrentUser;
import com.szymonosicinski.tripplanner.Util.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/Blog")
public class BlogController {
    @Autowired
    BlogService blogService;

    @GetMapping("/GetBlogs")
    public Page<Blog> getBlogs(@RequestParam(value = "page", defaultValue = "0") final int page,
                               @RequestParam(value = "size", defaultValue = "100") final int pageSize,
                               @CurrentUser final UserPrincipal currentUser){
        return blogService.getBlogs(
                PageRequest.of(page,pageSize, Sort.Direction.ASC,"name"),currentUser);
    }

    @GetMapping("/GetEntries")
    public Page<BlogEntry> getBlogEntries(@RequestParam(value = "blogId") final UUID blogId,
                                          @RequestParam(value = "page", defaultValue = "0") final int page,
                                          @RequestParam(value = "size", defaultValue = "100") final int pageSize,
                                          @CurrentUser final UserPrincipal currentUser){
        return blogService.getBlogEntries(blogId,
                PageRequest.of(page,pageSize, Sort.Direction.DESC,"date"), currentUser);
    }
}
