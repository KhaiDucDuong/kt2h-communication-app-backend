package duckhai.springsecurity.demo.service.ComicService;

import duckhai.springsecurity.demo.domain.Comic;
import duckhai.springsecurity.demo.dto.ComicDTO;
import duckhai.springsecurity.demo.repository.ComicRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ComicServiceImpl implements ComicService{
    private final ComicRepository comicRepository;

    @Override
    public Comic createComic(ComicDTO comicDTO) {
        Comic newComic = Comic.builder()
                .name(comicDTO.getName())
                .build();
        return comicRepository.save(newComic);
    }

    @Override
    public List<Comic> getAllComics() {
        return comicRepository.findAll();
    }

    @Override
    public Comic updateComic(Long id, ComicDTO comicDTO) {
        return null;
    }

    @Override
    public boolean deleteComic(Long id) {
        return false;
    }
}
