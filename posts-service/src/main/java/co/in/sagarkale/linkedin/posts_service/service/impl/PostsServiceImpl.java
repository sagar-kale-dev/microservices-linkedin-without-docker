package co.in.sagarkale.linkedin.posts_service.service.impl;

import co.in.sagarkale.linkedin.posts_service.advice.ApiResponse;
import co.in.sagarkale.linkedin.posts_service.auth.UserContextHolder;
import co.in.sagarkale.linkedin.posts_service.client.ConnectionsClient;
import co.in.sagarkale.linkedin.posts_service.dto.PersonDto;
import co.in.sagarkale.linkedin.posts_service.dto.PostCreateReqDto;
import co.in.sagarkale.linkedin.posts_service.dto.PostDto;
import co.in.sagarkale.linkedin.posts_service.entity.Post;
import co.in.sagarkale.linkedin.posts_service.event.PostCreatedEvent;
import co.in.sagarkale.linkedin.posts_service.exception.ResourceNotFoundException;
import co.in.sagarkale.linkedin.posts_service.repository.PostRepository;
import co.in.sagarkale.linkedin.posts_service.service.PostsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostsServiceImpl implements PostsService {

    private final PostRepository postRepository;
    private final ModelMapper modelMapper;
    private final ConnectionsClient connectionsClient;

    private final KafkaTemplate<Long, PostCreatedEvent> kafkaTemplate;

    @Override
    public PostDto createPost(PostCreateReqDto postCreateReqDto) {
        Long userId = UserContextHolder.getCurrentUserId();
        log.info("Creating post for user: {}", userId);

        Post post = modelMapper.map(postCreateReqDto, Post.class);
        post.setUserId(userId);
        Post savedPost = postRepository.save(post);

        PostCreatedEvent postCreatedEvent = PostCreatedEvent.builder()
                .postId(savedPost.getId())
                .content(savedPost.getContent())
                .creatorId(userId)
                .build();

        kafkaTemplate.send("post-created-topic", postCreatedEvent);

        return modelMapper.map(savedPost, PostDto.class);
    }

    @Override
    public PostDto getPostDetailsByPostId(Long postId) {
        log.info("Fetching post details for postId: {}", postId);

        ApiResponse<List<PersonDto>> apiResponse = connectionsClient.getAllFirstDegreeConn();
        log.info("Fetching first degree connection: {}", apiResponse.getData());

        Post post = postRepository.findById(postId).orElseThrow(()->new ResourceNotFoundException("Post not found with ID: "+postId));
        return modelMapper.map(post, PostDto.class);
    }

    @Override
    public List<PostDto> getAllPostsOfUser() {
        Long userId = UserContextHolder.getCurrentUserId();
        log.info("Fetching all posts for userId: {}", userId);

        List<Post> postList = postRepository.findByUserId(userId);
        return postList.stream().map(
                (post)->modelMapper.map(post, PostDto.class)
        ).collect(Collectors.toList());
    }

}
