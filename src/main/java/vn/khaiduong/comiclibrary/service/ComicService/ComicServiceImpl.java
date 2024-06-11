package vn.khaiduong.comiclibrary.service.ComicService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.khaiduong.comiclibrary.Response.Meta;
import vn.khaiduong.comiclibrary.Response.ResultPaginationResponse;
import vn.khaiduong.comiclibrary.domain.Comic;
import vn.khaiduong.comiclibrary.dto.ComicDTO;
import vn.khaiduong.comiclibrary.repository.ComicRepository;
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
    public ResultPaginationResponse getAllComics(Pageable pageable) {
        Page<Comic> comicPage= comicRepository.findAll(pageable);
        Meta meta = Meta.builder()
                .page(comicPage.getNumber() + 1)
                .pageSize(comicPage.getSize())
                .pages(comicPage.getTotalPages())
                .total(comicPage.getTotalElements())
                .build();

        ResultPaginationResponse response = ResultPaginationResponse.builder()
                .meta(meta)
                .result(comicPage.getContent())
                .build();

        return response;
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
