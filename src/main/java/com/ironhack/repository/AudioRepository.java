package com.ironhack.repository;

import com.ironhack.model.Audio;
import com.ironhack.model.Song;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AudioRepository extends JpaRepository<Audio,Long> {
    List<Audio> findByTitleContaining(String title);
}
