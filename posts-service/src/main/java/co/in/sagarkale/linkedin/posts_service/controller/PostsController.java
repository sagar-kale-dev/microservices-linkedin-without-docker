package co.in.sagarkale.linkedin.posts_service.controller;

import co.in.sagarkale.linkedin.posts_service.dto.PostCreateReqDto;
import co.in.sagarkale.linkedin.posts_service.dto.PostDto;
import co.in.sagarkale.linkedin.posts_service.service.PostsService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/core")
@RequiredArgsConstructor
public class PostsController {

    private final PostsService postsService;

    @PostMapping
    public ResponseEntity<PostDto> createPost(
            @RequestBody PostCreateReqDto postCreateReqDto
    ){
        PostDto postDto = postsService.createPost(postCreateReqDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(postDto);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostDto> getPostByPostId(@PathVariable Long postId){
        PostDto postDto = postsService.getPostDetailsByPostId(postId);
        return ResponseEntity.ok(postDto);
    }

    @GetMapping("/users/allPosts")
    public ResponseEntity<List<PostDto>> getAllPostsOfUser(){
        List<PostDto> postDtoList = postsService.getAllPostsOfUser();
        return ResponseEntity.ok(postDtoList);
    }

}
