package co.in.sagarkale.linkedin.posts_service.service;

public interface LikesService {
    void likePost(Long postId);
    void unlikePost(Long postId);
}
