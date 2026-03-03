package co.in.sagarkale.linkedin.posts_service.service.impl;

import co.in.sagarkale.linkedin.posts_service.auth.UserContextHolder;
import co.in.sagarkale.linkedin.posts_service.entity.Post;
import co.in.sagarkale.linkedin.posts_service.entity.PostLike;
import co.in.sagarkale.linkedin.posts_service.event.PostLikedEvent;
import co.in.sagarkale.linkedin.posts_service.exception.BadRequestException;
import co.in.sagarkale.linkedin.posts_service.exception.ResourceNotFoundException;
import co.in.sagarkale.linkedin.posts_service.repository.PostLikeRepository;
import co.in.sagarkale.linkedin.posts_service.repository.PostRepository;
import co.in.sagarkale.linkedin.posts_service.service.LikesService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class LikesServiceImpl implements LikesService {

    private final PostLikeRepository postLikeRepository;
    private final PostRepository postRepository;

    private final KafkaTemplate<Long, PostLikedEvent> kafkaTemplate;

    @Override
    public void likePost(Long postId) {
        Long userId = UserContextHolder.getCurrentUserId();
        log.info("Adding like for postId: {} by userId:{}",postId, userId);

        //Check if post existing
        Post post = postRepository.findById(postId).orElseThrow(
                ()-> new ResourceNotFoundException("Post does not exists with postId: "+postId)
        );
        //Check if not already liked
        boolean isAlreadyLiked = postLikeRepository.existsByUserIdAndPostId(userId, postId);
        if(isAlreadyLiked) {
            throw new BadRequestException("Post is already liked");
        }

        PostLike postLike = new PostLike();
        postLike.setPostId(postId);
        postLike.setUserId(userId);

        postLikeRepository.save(postLike);

        PostLikedEvent postLikedEvent = PostLikedEvent.builder()
                .postId(postId)
                .creatorId(post.getUserId())
                .likedByUserId(userId)
                .build();

        kafkaTemplate.send("post-liked-topic",postId, postLikedEvent);
    }

    @Override
    public void unlikePost(Long postId) {
        Long userId = UserContextHolder.getCurrentUserId();
        log.info("Unliking the postId: {} by userId:{}",postId, userId);

        //Check if post existing
        postRepository.findById(postId).orElseThrow(
                ()-> new ResourceNotFoundException("Post does not exists with postId: "+postId)
        );
        //Check if not already liked
        boolean isAlreadyLiked = postLikeRepository.existsByUserIdAndPostId(userId, postId);
        if(!isAlreadyLiked) {
            throw new BadRequestException("Post is already unliked");
        }

        postLikeRepository.deleteByUserIdAndPostId(userId, postId);
    }

}
