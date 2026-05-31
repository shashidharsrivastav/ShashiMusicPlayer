package com.audio.webMusicPlayer.repository;

import com.audio.webMusicPlayer.entity.SongEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SongsRepository extends JpaRepository<SongEntity, Long> {

}
