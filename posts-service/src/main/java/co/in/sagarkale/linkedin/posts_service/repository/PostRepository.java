package co.in.sagarkale.linkedin.posts_service.repository;

import co.in.sagarkale.linkedin.posts_service.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByUserId(Long userId);
}