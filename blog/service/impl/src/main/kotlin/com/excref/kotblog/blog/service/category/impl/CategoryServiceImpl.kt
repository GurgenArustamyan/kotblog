package com.excref.kotblog.blog.service.category.impl

import com.excref.kotblog.blog.persistence.category.CategoryRepository
import com.excref.kotblog.blog.service.category.CategoryService
import com.excref.kotblog.blog.service.category.domain.Category
import com.excref.kotblog.blog.service.category.exception.CategoryAlreadyExistsForNameException
import com.excref.kotblog.blog.service.category.exception.CategoryNotExistsForUuidException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * @author Ruben Vardanyan
 * @since 06/07/2017 12:31
 */
@Service
class CategoryServiceImpl : CategoryService {

    //region Dependencies
    @Autowired
    private lateinit var categoryRepository: CategoryRepository
    //endregion

    //region Public methods
    @Transactional
    override fun create(name: String): Category {
        assertCategoryNotExistForName(name)
        logger.debug("Creating category with name - $name")
        return categoryRepository.save(Category(name))
    }

    @Transactional(readOnly = true)
    override fun getByUuid(uuid: String): Category {
        logger.debug("Getting category with uuid - $uuid")
        val category = categoryRepository.findByUuid(uuid)
        assertCategoryNotNullForUuid(category, uuid)
        logger.debug("Successfully got category - $category")
        return category as Category
    }

    @Transactional(readOnly = true)
    override fun existsForName(name: String): Boolean {
        logger.debug("Getting category with name - $name")
        return categoryRepository.findByName(name) != null
    }
    //endregion

    //region Utility methods
    fun assertCategoryNotExistForName(name: String) {
        if (existsForName(name)) {
            logger.error("The category with name $name already exists")
            throw CategoryAlreadyExistsForNameException(name, "The category with name $name already exists")
        }
    }

    fun assertCategoryNotNullForUuid(category: Category?, uuid: String) {
        if (category == null) {
            logger.error("Can not find category for uuid $uuid")
            throw CategoryNotExistsForUuidException(uuid, "Can not find category for uuid $uuid")
        }
    }
    //endregion

    //region Companion
    companion object {
        private val logger: Logger = LoggerFactory.getLogger(CategoryServiceImpl::class.java)
    }
    //endregion
}