package org.example.backend.services;

import org.example.backend.exceptions.APIException;
import org.example.backend.exceptions.ResourceNotFoundException;
import org.example.backend.models.Category;
import org.example.backend.payload.CategoryDTO;
import org.example.backend.payload.CategoryResponse;
import org.example.backend.repositories.CategoryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService{


    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public CategoryResponse getAllCategories(Integer pageNumber, Integer pageSize, String orderBy, String orderDir) {
        Sort sortByAndOrder = orderBy.equalsIgnoreCase("asc")?
                               Sort.by(orderBy).ascending():
                               Sort.by(orderBy).descending();

        PageRequest pageDetails = PageRequest.of(pageNumber,pageSize,sortByAndOrder);
        Page<Category> categoryPage = categoryRepository.findAll(pageDetails);

        List<Category> categories = categoryPage.getContent();

        List<CategoryDTO> categoryDTOS = categories.stream().map(
                category -> modelMapper.map(category,CategoryDTO.class)
        ).toList();

        CategoryResponse categoryResponse = new CategoryResponse();
        categoryResponse.setContent(categoryDTOS);
        categoryResponse.setPageNumber(categoryPage.getNumber());
        categoryResponse.setTotalElements(categoryPage.getTotalElements());
        categoryResponse.setPageSize(categoryPage.getSize());
        categoryResponse.setTotalPages(categoryPage.getTotalPages());
        categoryResponse.setLastPage(categoryPage.isLast());

        return categoryResponse;
    }


    @Override
    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        Category category = modelMapper.map(categoryDTO,Category.class);
        Category categoryFromDB = categoryRepository.findByCategoryName(category.getCategoryName());

        if(categoryFromDB != null){
            throw new APIException("Category already exists.");
        }

        Category savedCategory = categoryRepository.save(category);
        return modelMapper.map(savedCategory,CategoryDTO.class);
    }


    @Override
    public CategoryDTO updateCategory(Long categoryId, CategoryDTO categoryDTO) {

        Category savedCategory = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", categoryId));

        savedCategory.setCategoryName(categoryDTO.getCategoryName());

        return  modelMapper.map(categoryRepository.save(savedCategory),CategoryDTO.class);
    }


    @Override
    public CategoryDTO deleteCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category","categoryId",categoryId));

        categoryRepository.delete(category);

        return modelMapper.map(category,CategoryDTO.class);
    }
}
