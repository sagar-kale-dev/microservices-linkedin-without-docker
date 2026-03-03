package co.in.sagarkale.linkedin.posts_service.controller;

import co.in.sagarkale.linkedin.posts_service.service.LikesService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/likes")
@RequiredArgsConstructor
public class LikesController {

    private final LikesService likesService;

    @PostMapping("/{postId}")
    public ResponseEntity<Void> likePost(
            @PathVariable Long postId,
            HttpServletRequest httpServletRequest
    ){
        likesService.likePost(postId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> unlikePost(
            @PathVariable Long postId,
            HttpServletRequest httpServletRequest
    ){
        likesService.unlikePost(postId);
        return ResponseEntity.noContent().build();
    }
}
