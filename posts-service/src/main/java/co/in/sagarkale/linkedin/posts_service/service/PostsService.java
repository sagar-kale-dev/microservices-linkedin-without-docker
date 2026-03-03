package co.in.sagarkale.linkedin.posts_service.service;

import co.in.sagarkale.linkedin.posts_service.dto.PostCreateReqDto;
import co.in.sagarkale.linkedin.posts_service.dto.PostDto;

import java.util.List;

public interface PostsService {
    PostDto createPost(PostCreateReqDto postCreateReqDto);

    PostDto getPostDetailsByPostId(Long postId);

    List<PostDto> getAllPostsOfUser();
}
